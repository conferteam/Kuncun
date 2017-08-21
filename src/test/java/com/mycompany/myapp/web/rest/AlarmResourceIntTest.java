package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.KucunApp;

import com.mycompany.myapp.domain.Alarm;
import com.mycompany.myapp.repository.AlarmRepository;
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
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AlarmResource REST controller.
 *
 * @see AlarmResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KucunApp.class)
public class AlarmResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_RESERVES = new BigDecimal(1);
    private static final BigDecimal UPDATED_RESERVES = new BigDecimal(2);

    private static final BigDecimal DEFAULT_LIMIT = new BigDecimal(1);
    private static final BigDecimal UPDATED_LIMIT = new BigDecimal(2);

    @Autowired
    private AlarmRepository alarmRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAlarmMockMvc;

    private Alarm alarm;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AlarmResource alarmResource = new AlarmResource(alarmRepository);
        this.restAlarmMockMvc = MockMvcBuilders.standaloneSetup(alarmResource)
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
    public static Alarm createEntity(EntityManager em) {
        Alarm alarm = new Alarm()
            .name(DEFAULT_NAME)
            .reserves(DEFAULT_RESERVES)
            .limit(DEFAULT_LIMIT);
        return alarm;
    }

    @Before
    public void initTest() {
        alarm = createEntity(em);
    }

    @Test
    @Transactional
    public void createAlarm() throws Exception {
        int databaseSizeBeforeCreate = alarmRepository.findAll().size();

        // Create the Alarm
        restAlarmMockMvc.perform(post("/api/alarms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(alarm)))
            .andExpect(status().isCreated());

        // Validate the Alarm in the database
        List<Alarm> alarmList = alarmRepository.findAll();
        assertThat(alarmList).hasSize(databaseSizeBeforeCreate + 1);
        Alarm testAlarm = alarmList.get(alarmList.size() - 1);
        assertThat(testAlarm.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAlarm.getReserves()).isEqualTo(DEFAULT_RESERVES);
        assertThat(testAlarm.getLimit()).isEqualTo(DEFAULT_LIMIT);
    }

    @Test
    @Transactional
    public void createAlarmWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = alarmRepository.findAll().size();

        // Create the Alarm with an existing ID
        alarm.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlarmMockMvc.perform(post("/api/alarms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(alarm)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Alarm> alarmList = alarmRepository.findAll();
        assertThat(alarmList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAlarms() throws Exception {
        // Initialize the database
        alarmRepository.saveAndFlush(alarm);

        // Get all the alarmList
        restAlarmMockMvc.perform(get("/api/alarms?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alarm.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].reserves").value(hasItem(DEFAULT_RESERVES.intValue())))
            .andExpect(jsonPath("$.[*].limit").value(hasItem(DEFAULT_LIMIT.intValue())));
    }

    @Test
    @Transactional
    public void getAlarm() throws Exception {
        // Initialize the database
        alarmRepository.saveAndFlush(alarm);

        // Get the alarm
        restAlarmMockMvc.perform(get("/api/alarms/{id}", alarm.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(alarm.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.reserves").value(DEFAULT_RESERVES.intValue()))
            .andExpect(jsonPath("$.limit").value(DEFAULT_LIMIT.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAlarm() throws Exception {
        // Get the alarm
        restAlarmMockMvc.perform(get("/api/alarms/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAlarm() throws Exception {
        // Initialize the database
        alarmRepository.saveAndFlush(alarm);
        int databaseSizeBeforeUpdate = alarmRepository.findAll().size();

        // Update the alarm
        Alarm updatedAlarm = alarmRepository.findOne(alarm.getId());
        updatedAlarm
            .name(UPDATED_NAME)
            .reserves(UPDATED_RESERVES)
            .limit(UPDATED_LIMIT);

        restAlarmMockMvc.perform(put("/api/alarms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAlarm)))
            .andExpect(status().isOk());

        // Validate the Alarm in the database
        List<Alarm> alarmList = alarmRepository.findAll();
        assertThat(alarmList).hasSize(databaseSizeBeforeUpdate);
        Alarm testAlarm = alarmList.get(alarmList.size() - 1);
        assertThat(testAlarm.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAlarm.getReserves()).isEqualTo(UPDATED_RESERVES);
        assertThat(testAlarm.getLimit()).isEqualTo(UPDATED_LIMIT);
    }

    @Test
    @Transactional
    public void updateNonExistingAlarm() throws Exception {
        int databaseSizeBeforeUpdate = alarmRepository.findAll().size();

        // Create the Alarm

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAlarmMockMvc.perform(put("/api/alarms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(alarm)))
            .andExpect(status().isCreated());

        // Validate the Alarm in the database
        List<Alarm> alarmList = alarmRepository.findAll();
        assertThat(alarmList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAlarm() throws Exception {
        // Initialize the database
        alarmRepository.saveAndFlush(alarm);
        int databaseSizeBeforeDelete = alarmRepository.findAll().size();

        // Get the alarm
        restAlarmMockMvc.perform(delete("/api/alarms/{id}", alarm.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Alarm> alarmList = alarmRepository.findAll();
        assertThat(alarmList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Alarm.class);
        Alarm alarm1 = new Alarm();
        alarm1.setId(1L);
        Alarm alarm2 = new Alarm();
        alarm2.setId(alarm1.getId());
        assertThat(alarm1).isEqualTo(alarm2);
        alarm2.setId(2L);
        assertThat(alarm1).isNotEqualTo(alarm2);
        alarm1.setId(null);
        assertThat(alarm1).isNotEqualTo(alarm2);
    }
}
