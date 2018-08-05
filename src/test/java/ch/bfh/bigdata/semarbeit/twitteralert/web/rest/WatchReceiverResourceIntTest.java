package ch.bfh.bigdata.semarbeit.twitteralert.web.rest;

import ch.bfh.bigdata.semarbeit.twitteralert.TwitteralertApp;

import ch.bfh.bigdata.semarbeit.twitteralert.domain.WatchReceiver;
import ch.bfh.bigdata.semarbeit.twitteralert.repository.WatchReceiverRepository;
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
 * Test class for the WatchReceiverResource REST controller.
 *
 * @see WatchReceiverResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TwitteralertApp.class)
public class WatchReceiverResourceIntTest {

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_CHANNEL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CHANNEL_NAME = "BBBBBBBBBB";

    @Autowired
    private WatchReceiverRepository watchReceiverRepository;


    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restWatchReceiverMockMvc;

    private WatchReceiver watchReceiver;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WatchReceiverResource watchReceiverResource = new WatchReceiverResource(watchReceiverRepository);
        this.restWatchReceiverMockMvc = MockMvcBuilders.standaloneSetup(watchReceiverResource)
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
    public static WatchReceiver createEntity(EntityManager em) {
        WatchReceiver watchReceiver = new WatchReceiver()
            .email(DEFAULT_EMAIL)
            .channelName(DEFAULT_CHANNEL_NAME);
        return watchReceiver;
    }

    @Before
    public void initTest() {
        watchReceiver = createEntity(em);
    }

    @Test
    @Transactional
    public void createWatchReceiver() throws Exception {
        int databaseSizeBeforeCreate = watchReceiverRepository.findAll().size();

        // Create the WatchReceiver
        restWatchReceiverMockMvc.perform(post("/api/watch-receivers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(watchReceiver)))
            .andExpect(status().isCreated());

        // Validate the WatchReceiver in the database
        List<WatchReceiver> watchReceiverList = watchReceiverRepository.findAll();
        assertThat(watchReceiverList).hasSize(databaseSizeBeforeCreate + 1);
        WatchReceiver testWatchReceiver = watchReceiverList.get(watchReceiverList.size() - 1);
        assertThat(testWatchReceiver.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testWatchReceiver.getChannelName()).isEqualTo(DEFAULT_CHANNEL_NAME);
    }

    @Test
    @Transactional
    public void createWatchReceiverWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = watchReceiverRepository.findAll().size();

        // Create the WatchReceiver with an existing ID
        watchReceiver.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWatchReceiverMockMvc.perform(post("/api/watch-receivers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(watchReceiver)))
            .andExpect(status().isBadRequest());

        // Validate the WatchReceiver in the database
        List<WatchReceiver> watchReceiverList = watchReceiverRepository.findAll();
        assertThat(watchReceiverList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllWatchReceivers() throws Exception {
        // Initialize the database
        watchReceiverRepository.saveAndFlush(watchReceiver);

        // Get all the watchReceiverList
        restWatchReceiverMockMvc.perform(get("/api/watch-receivers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(watchReceiver.getId().intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].channelName").value(hasItem(DEFAULT_CHANNEL_NAME.toString())));
    }
    

    @Test
    @Transactional
    public void getWatchReceiver() throws Exception {
        // Initialize the database
        watchReceiverRepository.saveAndFlush(watchReceiver);

        // Get the watchReceiver
        restWatchReceiverMockMvc.perform(get("/api/watch-receivers/{id}", watchReceiver.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(watchReceiver.getId().intValue()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.channelName").value(DEFAULT_CHANNEL_NAME.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingWatchReceiver() throws Exception {
        // Get the watchReceiver
        restWatchReceiverMockMvc.perform(get("/api/watch-receivers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWatchReceiver() throws Exception {
        // Initialize the database
        watchReceiverRepository.saveAndFlush(watchReceiver);

        int databaseSizeBeforeUpdate = watchReceiverRepository.findAll().size();

        // Update the watchReceiver
        WatchReceiver updatedWatchReceiver = watchReceiverRepository.findById(watchReceiver.getId()).get();
        // Disconnect from session so that the updates on updatedWatchReceiver are not directly saved in db
        em.detach(updatedWatchReceiver);
        updatedWatchReceiver
            .email(UPDATED_EMAIL)
            .channelName(UPDATED_CHANNEL_NAME);

        restWatchReceiverMockMvc.perform(put("/api/watch-receivers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWatchReceiver)))
            .andExpect(status().isOk());

        // Validate the WatchReceiver in the database
        List<WatchReceiver> watchReceiverList = watchReceiverRepository.findAll();
        assertThat(watchReceiverList).hasSize(databaseSizeBeforeUpdate);
        WatchReceiver testWatchReceiver = watchReceiverList.get(watchReceiverList.size() - 1);
        assertThat(testWatchReceiver.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testWatchReceiver.getChannelName()).isEqualTo(UPDATED_CHANNEL_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingWatchReceiver() throws Exception {
        int databaseSizeBeforeUpdate = watchReceiverRepository.findAll().size();

        // Create the WatchReceiver

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restWatchReceiverMockMvc.perform(put("/api/watch-receivers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(watchReceiver)))
            .andExpect(status().isBadRequest());

        // Validate the WatchReceiver in the database
        List<WatchReceiver> watchReceiverList = watchReceiverRepository.findAll();
        assertThat(watchReceiverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteWatchReceiver() throws Exception {
        // Initialize the database
        watchReceiverRepository.saveAndFlush(watchReceiver);

        int databaseSizeBeforeDelete = watchReceiverRepository.findAll().size();

        // Get the watchReceiver
        restWatchReceiverMockMvc.perform(delete("/api/watch-receivers/{id}", watchReceiver.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<WatchReceiver> watchReceiverList = watchReceiverRepository.findAll();
        assertThat(watchReceiverList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WatchReceiver.class);
        WatchReceiver watchReceiver1 = new WatchReceiver();
        watchReceiver1.setId(1L);
        WatchReceiver watchReceiver2 = new WatchReceiver();
        watchReceiver2.setId(watchReceiver1.getId());
        assertThat(watchReceiver1).isEqualTo(watchReceiver2);
        watchReceiver2.setId(2L);
        assertThat(watchReceiver1).isNotEqualTo(watchReceiver2);
        watchReceiver1.setId(null);
        assertThat(watchReceiver1).isNotEqualTo(watchReceiver2);
    }
}
