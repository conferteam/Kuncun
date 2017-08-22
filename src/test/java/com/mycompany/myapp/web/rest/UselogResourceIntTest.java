package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.KucunApp;

import com.mycompany.myapp.domain.UseLog;
import com.mycompany.myapp.repository.UseLogRepository;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;

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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the UseLogResource REST controller.
 *
 * @see UseLogResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KucunApp.class)
public class UseLogResourceIntTest {

    private static final Long DEFAULT_NAME_ID = 1L;
    private static final Long UPDATED_NAME_ID = 2L;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_TYPE = 1;
    private static final Integer UPDATED_TYPE = 2;

    private static final Integer DEFAULT_COUNT = 1;
    private static final Integer UPDATED_COUNT = 2;

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private UseLogRepository useLogRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restUseLogMockMvc;

    private UseLog useLog;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UseLogResource useLogResource = new UseLogResource(useLogRepository);
        this.restUseLogMockMvc = MockMvcBuilders.standaloneSetup(useLogResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UseLog createEntity(EntityManager em) {
        UseLog useLog = new UseLog()
            .nameId(DEFAULT_NAME_ID)
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE)
            .count(DEFAULT_COUNT)
            .username(DEFAULT_USERNAME)
            .date(DEFAULT_DATE);
        return useLog;
    }

    @Before
    public void initTest() {
        useLog = createEntity(em);
    }

    @Test
    @Transactional
    public void createUseLog() throws Exception {
        int databaseSizeBeforeCreate = useLogRepository.findAll().size();

        // Create the UseLog
        restUseLogMockMvc.perform(post("/api/use-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(useLog)))
            .andExpect(status().isCreated());

        // Validate the UseLog in the database
        List<UseLog> useLogList = useLogRepository.findAll();
        assertThat(useLogList).hasSize(databaseSizeBeforeCreate + 1);
        UseLog testUseLog = useLogList.get(useLogList.size() - 1);
        assertThat(testUseLog.getNameId()).isEqualTo(DEFAULT_NAME_ID);
        assertThat(testUseLog.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUseLog.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testUseLog.getCount()).isEqualTo(DEFAULT_COUNT);
        assertThat(testUseLog.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testUseLog.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void createUseLogWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = useLogRepository.findAll().size();

        // Create the UseLog with an existing ID
        useLog.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUseLogMockMvc.perform(post("/api/use-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(useLog)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<UseLog> useLogList = useLogRepository.findAll();
        assertThat(useLogList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllUseLogs() throws Exception {
        // Initialize the database
        useLogRepository.saveAndFlush(useLog);

        // Get all the useLogList
        restUseLogMockMvc.perform(get("/api/use-logs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(useLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].nameId").value(hasItem(DEFAULT_NAME_ID.intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].count").value(hasItem(DEFAULT_COUNT)))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    public void getUseLog() throws Exception {
        // Initialize the database
        useLogRepository.saveAndFlush(useLog);

        // Get the useLog
        restUseLogMockMvc.perform(get("/api/use-logs/{id}", useLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(useLog.getId().intValue()))
            .andExpect(jsonPath("$.nameId").value(DEFAULT_NAME_ID.intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.count").value(DEFAULT_COUNT))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUseLog() throws Exception {
        // Get the useLog
        restUseLogMockMvc.perform(get("/api/use-logs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUseLog() throws Exception {
        // Initialize the database
        useLogRepository.saveAndFlush(useLog);
        int databaseSizeBeforeUpdate = useLogRepository.findAll().size();

        // Update the useLog
        UseLog updatedUseLog = useLogRepository.findOne(useLog.getId());
        updatedUseLog
            .nameId(UPDATED_NAME_ID)
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .count(UPDATED_COUNT)
            .username(UPDATED_USERNAME)
            .date(UPDATED_DATE);

        restUseLogMockMvc.perform(put("/api/use-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUseLog)))
            .andExpect(status().isOk());

        // Validate the UseLog in the database
        List<UseLog> useLogList = useLogRepository.findAll();
        assertThat(useLogList).hasSize(databaseSizeBeforeUpdate);
        UseLog testUseLog = useLogList.get(useLogList.size() - 1);
        assertThat(testUseLog.getNameId()).isEqualTo(UPDATED_NAME_ID);
        assertThat(testUseLog.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUseLog.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testUseLog.getCount()).isEqualTo(UPDATED_COUNT);
        assertThat(testUseLog.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testUseLog.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingUseLog() throws Exception {
        int databaseSizeBeforeUpdate = useLogRepository.findAll().size();

        // Create the UseLog

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restUseLogMockMvc.perform(put("/api/use-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(useLog)))
            .andExpect(status().isCreated());

        // Validate the UseLog in the database
        List<UseLog> useLogList = useLogRepository.findAll();
        assertThat(useLogList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteUseLog() throws Exception {
        // Initialize the database
        useLogRepository.saveAndFlush(useLog);
        int databaseSizeBeforeDelete = useLogRepository.findAll().size();

        // Get the useLog
        restUseLogMockMvc.perform(delete("/api/use-logs/{id}", useLog.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<UseLog> useLogList = useLogRepository.findAll();
        assertThat(useLogList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UseLog.class);
        UseLog useLog1 = new UseLog();
        useLog1.setId(1L);
        UseLog useLog2 = new UseLog();
        useLog2.setId(useLog1.getId());
        assertThat(useLog1).isEqualTo(useLog2);
        useLog2.setId(2L);
        assertThat(useLog1).isNotEqualTo(useLog2);
        useLog1.setId(null);
        assertThat(useLog1).isNotEqualTo(useLog2);
    }
}
