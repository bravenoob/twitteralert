package ch.bfh.bigdata.semarbeit.twitteralert.web.rest;

import ch.bfh.bigdata.semarbeit.twitteralert.TwitteralertApp;

import ch.bfh.bigdata.semarbeit.twitteralert.domain.VersionWatcher;
import ch.bfh.bigdata.semarbeit.twitteralert.repository.VersionWatcherRepository;
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
 * Test class for the VersionWatcherResource REST controller.
 *
 * @see VersionWatcherResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TwitteralertApp.class)
public class VersionWatcherResourceIntTest {

    private static final String DEFAULT_WATCHER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_WATCHER_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    @Autowired
    private VersionWatcherRepository versionWatcherRepository;


    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restVersionWatcherMockMvc;

    private VersionWatcher versionWatcher;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VersionWatcherResource versionWatcherResource = new VersionWatcherResource(versionWatcherRepository);
        this.restVersionWatcherMockMvc = MockMvcBuilders.standaloneSetup(versionWatcherResource)
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
    public static VersionWatcher createEntity(EntityManager em) {
        VersionWatcher versionWatcher = new VersionWatcher()
            .watcherName(DEFAULT_WATCHER_NAME)
            .active(DEFAULT_ACTIVE);
        return versionWatcher;
    }

    @Before
    public void initTest() {
        versionWatcher = createEntity(em);
    }

    @Test
    @Transactional
    public void createVersionWatcher() throws Exception {
        int databaseSizeBeforeCreate = versionWatcherRepository.findAll().size();

        // Create the VersionWatcher
        restVersionWatcherMockMvc.perform(post("/api/version-watchers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(versionWatcher)))
            .andExpect(status().isCreated());

        // Validate the VersionWatcher in the database
        List<VersionWatcher> versionWatcherList = versionWatcherRepository.findAll();
        assertThat(versionWatcherList).hasSize(databaseSizeBeforeCreate + 1);
        VersionWatcher testVersionWatcher = versionWatcherList.get(versionWatcherList.size() - 1);
        assertThat(testVersionWatcher.getWatcherName()).isEqualTo(DEFAULT_WATCHER_NAME);
        assertThat(testVersionWatcher.isActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    public void createVersionWatcherWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = versionWatcherRepository.findAll().size();

        // Create the VersionWatcher with an existing ID
        versionWatcher.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVersionWatcherMockMvc.perform(post("/api/version-watchers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(versionWatcher)))
            .andExpect(status().isBadRequest());

        // Validate the VersionWatcher in the database
        List<VersionWatcher> versionWatcherList = versionWatcherRepository.findAll();
        assertThat(versionWatcherList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkWatcherNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = versionWatcherRepository.findAll().size();
        // set the field null
        versionWatcher.setWatcherName(null);

        // Create the VersionWatcher, which fails.

        restVersionWatcherMockMvc.perform(post("/api/version-watchers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(versionWatcher)))
            .andExpect(status().isBadRequest());

        List<VersionWatcher> versionWatcherList = versionWatcherRepository.findAll();
        assertThat(versionWatcherList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = versionWatcherRepository.findAll().size();
        // set the field null
        versionWatcher.setActive(null);

        // Create the VersionWatcher, which fails.

        restVersionWatcherMockMvc.perform(post("/api/version-watchers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(versionWatcher)))
            .andExpect(status().isBadRequest());

        List<VersionWatcher> versionWatcherList = versionWatcherRepository.findAll();
        assertThat(versionWatcherList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVersionWatchers() throws Exception {
        // Initialize the database
        versionWatcherRepository.saveAndFlush(versionWatcher);

        // Get all the versionWatcherList
        restVersionWatcherMockMvc.perform(get("/api/version-watchers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(versionWatcher.getId().intValue())))
            .andExpect(jsonPath("$.[*].watcherName").value(hasItem(DEFAULT_WATCHER_NAME.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }
    

    @Test
    @Transactional
    public void getVersionWatcher() throws Exception {
        // Initialize the database
        versionWatcherRepository.saveAndFlush(versionWatcher);

        // Get the versionWatcher
        restVersionWatcherMockMvc.perform(get("/api/version-watchers/{id}", versionWatcher.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(versionWatcher.getId().intValue()))
            .andExpect(jsonPath("$.watcherName").value(DEFAULT_WATCHER_NAME.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }
    @Test
    @Transactional
    public void getNonExistingVersionWatcher() throws Exception {
        // Get the versionWatcher
        restVersionWatcherMockMvc.perform(get("/api/version-watchers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVersionWatcher() throws Exception {
        // Initialize the database
        versionWatcherRepository.saveAndFlush(versionWatcher);

        int databaseSizeBeforeUpdate = versionWatcherRepository.findAll().size();

        // Update the versionWatcher
        VersionWatcher updatedVersionWatcher = versionWatcherRepository.findById(versionWatcher.getId()).get();
        // Disconnect from session so that the updates on updatedVersionWatcher are not directly saved in db
        em.detach(updatedVersionWatcher);
        updatedVersionWatcher
            .watcherName(UPDATED_WATCHER_NAME)
            .active(UPDATED_ACTIVE);

        restVersionWatcherMockMvc.perform(put("/api/version-watchers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedVersionWatcher)))
            .andExpect(status().isOk());

        // Validate the VersionWatcher in the database
        List<VersionWatcher> versionWatcherList = versionWatcherRepository.findAll();
        assertThat(versionWatcherList).hasSize(databaseSizeBeforeUpdate);
        VersionWatcher testVersionWatcher = versionWatcherList.get(versionWatcherList.size() - 1);
        assertThat(testVersionWatcher.getWatcherName()).isEqualTo(UPDATED_WATCHER_NAME);
        assertThat(testVersionWatcher.isActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingVersionWatcher() throws Exception {
        int databaseSizeBeforeUpdate = versionWatcherRepository.findAll().size();

        // Create the VersionWatcher

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restVersionWatcherMockMvc.perform(put("/api/version-watchers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(versionWatcher)))
            .andExpect(status().isBadRequest());

        // Validate the VersionWatcher in the database
        List<VersionWatcher> versionWatcherList = versionWatcherRepository.findAll();
        assertThat(versionWatcherList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteVersionWatcher() throws Exception {
        // Initialize the database
        versionWatcherRepository.saveAndFlush(versionWatcher);

        int databaseSizeBeforeDelete = versionWatcherRepository.findAll().size();

        // Get the versionWatcher
        restVersionWatcherMockMvc.perform(delete("/api/version-watchers/{id}", versionWatcher.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<VersionWatcher> versionWatcherList = versionWatcherRepository.findAll();
        assertThat(versionWatcherList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VersionWatcher.class);
        VersionWatcher versionWatcher1 = new VersionWatcher();
        versionWatcher1.setId(1L);
        VersionWatcher versionWatcher2 = new VersionWatcher();
        versionWatcher2.setId(versionWatcher1.getId());
        assertThat(versionWatcher1).isEqualTo(versionWatcher2);
        versionWatcher2.setId(2L);
        assertThat(versionWatcher1).isNotEqualTo(versionWatcher2);
        versionWatcher1.setId(null);
        assertThat(versionWatcher1).isNotEqualTo(versionWatcher2);
    }
}
