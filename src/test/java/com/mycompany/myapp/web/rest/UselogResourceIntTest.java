package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.KucunApp;

import com.mycompany.myapp.domain.Uselog;
import com.mycompany.myapp.repository.UselogRepository;
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
 * Test class for the UselogResource REST controller.
 *
 * @see UselogResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KucunApp.class)
public class UselogResourceIntTest {

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
    private UselogRepository uselogRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restUselogMockMvc;

    private Uselog uselog;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UselogResource uselogResource = new UselogResource(uselogRepository);
        this.restUselogMockMvc = MockMvcBuilders.standaloneSetup(uselogResource)
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
    public static Uselog createEntity(EntityManager em) {
        Uselog uselog = new Uselog()
            .name_id(DEFAULT_NAME_ID)
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE)
            .count(DEFAULT_COUNT)
            .username(DEFAULT_USERNAME)
            .date(DEFAULT_DATE);
        return uselog;
    }

    @Before
    public void initTest() {
        uselog = createEntity(em);
    }

    @Test
    @Transactional
    public void createUselog() throws Exception {
        int databaseSizeBeforeCreate = uselogRepository.findAll().size();

        // Create the Uselog
        restUselogMockMvc.perform(post("/api/uselogs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(uselog)))
            .andExpect(status().isCreated());

        // Validate the Uselog in the database
        List<Uselog> uselogList = uselogRepository.findAll();
        assertThat(uselogList).hasSize(databaseSizeBeforeCreate + 1);
        Uselog testUselog = uselogList.get(uselogList.size() - 1);
        assertThat(testUselog.getName_id()).isEqualTo(DEFAULT_NAME_ID);
        assertThat(testUselog.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUselog.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testUselog.getCount()).isEqualTo(DEFAULT_COUNT);
        assertThat(testUselog.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testUselog.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void createUselogWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = uselogRepository.findAll().size();

        // Create the Uselog with an existing ID
        uselog.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUselogMockMvc.perform(post("/api/uselogs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(uselog)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Uselog> uselogList = uselogRepository.findAll();
        assertThat(uselogList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllUselogs() throws Exception {
        // Initialize the database
        uselogRepository.saveAndFlush(uselog);

        // Get all the uselogList
        restUselogMockMvc.perform(get("/api/uselogs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(uselog.getId().intValue())))
            .andExpect(jsonPath("$.[*].name_id").value(hasItem(DEFAULT_NAME_ID.intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].count").value(hasItem(DEFAULT_COUNT)))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    public void getUselog() throws Exception {
        // Initialize the database
        uselogRepository.saveAndFlush(uselog);

        // Get the uselog
        restUselogMockMvc.perform(get("/api/uselogs/{id}", uselog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(uselog.getId().intValue()))
            .andExpect(jsonPath("$.name_id").value(DEFAULT_NAME_ID.intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.count").value(DEFAULT_COUNT))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUselog() throws Exception {
        // Get the uselog
        restUselogMockMvc.perform(get("/api/uselogs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUselog() throws Exception {
        // Initialize the database
        uselogRepository.saveAndFlush(uselog);
        int databaseSizeBeforeUpdate = uselogRepository.findAll().size();

        // Update the uselog
        Uselog updatedUselog = uselogRepository.findOne(uselog.getId());
        updatedUselog
            .name_id(UPDATED_NAME_ID)
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .count(UPDATED_COUNT)
            .username(UPDATED_USERNAME)
            .date(UPDATED_DATE);

        restUselogMockMvc.perform(put("/api/uselogs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUselog)))
            .andExpect(status().isOk());

        // Validate the Uselog in the database
        List<Uselog> uselogList = uselogRepository.findAll();
        assertThat(uselogList).hasSize(databaseSizeBeforeUpdate);
        Uselog testUselog = uselogList.get(uselogList.size() - 1);
        assertThat(testUselog.getName_id()).isEqualTo(UPDATED_NAME_ID);
        assertThat(testUselog.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUselog.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testUselog.getCount()).isEqualTo(UPDATED_COUNT);
        assertThat(testUselog.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testUselog.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingUselog() throws Exception {
        int databaseSizeBeforeUpdate = uselogRepository.findAll().size();

        // Create the Uselog

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restUselogMockMvc.perform(put("/api/uselogs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(uselog)))
            .andExpect(status().isCreated());

        // Validate the Uselog in the database
        List<Uselog> uselogList = uselogRepository.findAll();
        assertThat(uselogList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteUselog() throws Exception {
        // Initialize the database
        uselogRepository.saveAndFlush(uselog);
        int databaseSizeBeforeDelete = uselogRepository.findAll().size();

        // Get the uselog
        restUselogMockMvc.perform(delete("/api/uselogs/{id}", uselog.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Uselog> uselogList = uselogRepository.findAll();
        assertThat(uselogList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Uselog.class);
        Uselog uselog1 = new Uselog();
        uselog1.setId(1L);
        Uselog uselog2 = new Uselog();
        uselog2.setId(uselog1.getId());
        assertThat(uselog1).isEqualTo(uselog2);
        uselog2.setId(2L);
        assertThat(uselog1).isNotEqualTo(uselog2);
        uselog1.setId(null);
        assertThat(uselog1).isNotEqualTo(uselog2);
    }
}
