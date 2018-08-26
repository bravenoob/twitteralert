package ch.bfh.bigdata.semarbeit.twitteralert.web.rest;

import ch.bfh.bigdata.semarbeit.twitteralert.TwitteralertApp;

import ch.bfh.bigdata.semarbeit.twitteralert.domain.WatchChannel;
import ch.bfh.bigdata.semarbeit.twitteralert.repository.WatchChannelRepository;
import ch.bfh.bigdata.semarbeit.twitteralert.service.WatchChannelService;
import ch.bfh.bigdata.semarbeit.twitteralert.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;


import static ch.bfh.bigdata.semarbeit.twitteralert.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the WatchChannelResource REST controller.
 *
 * @see WatchChannelResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TwitteralertApp.class)
public class WatchChannelResourceIntTest {

    private static final String DEFAULT_CHANNEL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CHANNEL_NAME = "BBBBBBBBBB";

    @Autowired
    private WatchChannelRepository watchChannelRepository;

    @Autowired
    private WatchChannelService watchChannelService;


    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restWatchChannelMockMvc;

    private WatchChannel watchChannel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WatchChannelResource watchChannelResource = new WatchChannelResource(watchChannelService);
        this.restWatchChannelMockMvc = MockMvcBuilders.standaloneSetup(watchChannelResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WatchChannel createEntity(EntityManager em) {
        WatchChannel watchChannel = new WatchChannel()
            .channelName(DEFAULT_CHANNEL_NAME);
        return watchChannel;
    }

    @Before
    public void initTest() {
        watchChannel = createEntity(em);
    }

    @Test
    @Transactional
    public void createWatchChannel() throws Exception {
        int databaseSizeBeforeCreate = watchChannelRepository.findAll().size();

        // Create the WatchChannel
        restWatchChannelMockMvc.perform(post("/api/watch-channels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(watchChannel)))
            .andExpect(status().isCreated());

        // Validate the WatchChannel in the database
        List<WatchChannel> watchChannelList = watchChannelRepository.findAll();
        assertThat(watchChannelList).hasSize(databaseSizeBeforeCreate + 1);
        WatchChannel testWatchChannel = watchChannelList.get(watchChannelList.size() - 1);
        assertThat(testWatchChannel.getChannelName()).isEqualTo(DEFAULT_CHANNEL_NAME);
    }

    @Test
    @Transactional
    public void createWatchChannelWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = watchChannelRepository.findAll().size();

        // Create the WatchChannel with an existing ID
        watchChannel.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWatchChannelMockMvc.perform(post("/api/watch-channels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(watchChannel)))
            .andExpect(status().isBadRequest());

        // Validate the WatchChannel in the database
        List<WatchChannel> watchChannelList = watchChannelRepository.findAll();
        assertThat(watchChannelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkChannelNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = watchChannelRepository.findAll().size();
        // set the field null
        watchChannel.setChannelName(null);

        // Create the WatchChannel, which fails.

        restWatchChannelMockMvc.perform(post("/api/watch-channels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(watchChannel)))
            .andExpect(status().isBadRequest());

        List<WatchChannel> watchChannelList = watchChannelRepository.findAll();
        assertThat(watchChannelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWatchChannels() throws Exception {
        // Initialize the database
        watchChannelRepository.saveAndFlush(watchChannel);

        // Get all the watchChannelList
        restWatchChannelMockMvc.perform(get("/api/watch-channels?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(watchChannel.getId().intValue())))
            .andExpect(jsonPath("$.[*].channelName").value(hasItem(DEFAULT_CHANNEL_NAME.toString())));
    }
    

    @Test
    @Transactional
    public void getWatchChannel() throws Exception {
        // Initialize the database
        watchChannelRepository.saveAndFlush(watchChannel);

        // Get the watchChannel
        restWatchChannelMockMvc.perform(get("/api/watch-channels/{id}", watchChannel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(watchChannel.getId().intValue()))
            .andExpect(jsonPath("$.channelName").value(DEFAULT_CHANNEL_NAME.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingWatchChannel() throws Exception {
        // Get the watchChannel
        restWatchChannelMockMvc.perform(get("/api/watch-channels/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWatchChannel() throws Exception {
        // Initialize the database
        watchChannelRepository.saveAndFlush(watchChannel);

        int databaseSizeBeforeUpdate = watchChannelRepository.findAll().size();

        // Update the watchChannel
        WatchChannel updatedWatchChannel = watchChannelRepository.findById(watchChannel.getId()).get();
        // Disconnect from session so that the updates on updatedWatchChannel are not directly saved in db
        em.detach(updatedWatchChannel);
        updatedWatchChannel
            .channelName(UPDATED_CHANNEL_NAME);

        restWatchChannelMockMvc.perform(put("/api/watch-channels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWatchChannel)))
            .andExpect(status().isOk());

        // Validate the WatchChannel in the database
        List<WatchChannel> watchChannelList = watchChannelRepository.findAll();
        assertThat(watchChannelList).hasSize(databaseSizeBeforeUpdate);
        WatchChannel testWatchChannel = watchChannelList.get(watchChannelList.size() - 1);
        assertThat(testWatchChannel.getChannelName()).isEqualTo(UPDATED_CHANNEL_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingWatchChannel() throws Exception {
        int databaseSizeBeforeUpdate = watchChannelRepository.findAll().size();

        // Create the WatchChannel

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restWatchChannelMockMvc.perform(put("/api/watch-channels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(watchChannel)))
            .andExpect(status().isBadRequest());

        // Validate the WatchChannel in the database
        List<WatchChannel> watchChannelList = watchChannelRepository.findAll();
        assertThat(watchChannelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteWatchChannel() throws Exception {
        // Initialize the database
        watchChannelRepository.saveAndFlush(watchChannel);

        int databaseSizeBeforeDelete = watchChannelRepository.findAll().size();

        // Get the watchChannel
        restWatchChannelMockMvc.perform(delete("/api/watch-channels/{id}", watchChannel.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<WatchChannel> watchChannelList = watchChannelRepository.findAll();
        assertThat(watchChannelList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WatchChannel.class);
        WatchChannel watchChannel1 = new WatchChannel();
        watchChannel1.setId(1L);
        WatchChannel watchChannel2 = new WatchChannel();
        watchChannel2.setId(watchChannel1.getId());
        assertThat(watchChannel1).isEqualTo(watchChannel2);
        watchChannel2.setId(2L);
        assertThat(watchChannel1).isNotEqualTo(watchChannel2);
        watchChannel1.setId(null);
        assertThat(watchChannel1).isNotEqualTo(watchChannel2);
    }
}
