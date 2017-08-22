package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.KucunApp;

import com.mycompany.myapp.domain.Type;
import com.mycompany.myapp.repository.TypeRepository;
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
 * Test class for the TypeResource REST controller.
 *
 * @see TypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KucunApp.class)
public class TypeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_RESERVES = new BigDecimal(1);
    private static final BigDecimal UPDATED_RESERVES = new BigDecimal(2);

    private static final BigDecimal DEFAULT_LIMIT = new BigDecimal(1);
    private static final BigDecimal UPDATED_LIMIT = new BigDecimal(2);

    private static final Integer DEFAULT_DEL_FLAG = 1;
    private static final Integer UPDATED_DEL_FLAG = 2;

    private static final String DEFAULT_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_UNIT = "BBBBBBBBBB";

    @Autowired
    private TypeRepository typeRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTypeMockMvc;

    private Type type;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TypeResource typeResource = new TypeResource(typeRepository);
        this.restTypeMockMvc = MockMvcBuilders.standaloneSetup(typeResource)
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
    public static Type createEntity(EntityManager em) {
        Type type = new Type()
            .name(DEFAULT_NAME)
            .reserves(DEFAULT_RESERVES)
            .limit(DEFAULT_LIMIT)
            .delFlag(DEFAULT_DEL_FLAG)
            .unit(DEFAULT_UNIT);
        return type;
    }

    @Before
    public void initTest() {
        type = createEntity(em);
    }

    @Test
    @Transactional
    public void createType() throws Exception {
        int databaseSizeBeforeCreate = typeRepository.findAll().size();

        // Create the Type
        restTypeMockMvc.perform(post("/api/types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(type)))
            .andExpect(status().isCreated());

        // Validate the Type in the database
        List<Type> typeList = typeRepository.findAll();
        assertThat(typeList).hasSize(databaseSizeBeforeCreate + 1);
        Type testType = typeList.get(typeList.size() - 1);
        assertThat(testType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testType.getReserves()).isEqualTo(DEFAULT_RESERVES);
        assertThat(testType.getLimit()).isEqualTo(DEFAULT_LIMIT);
        assertThat(testType.getDelFlag()).isEqualTo(DEFAULT_DEL_FLAG);
        assertThat(testType.getUnit()).isEqualTo(DEFAULT_UNIT);
    }

    @Test
    @Transactional
    public void createTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = typeRepository.findAll().size();

        // Create the Type with an existing ID
        type.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypeMockMvc.perform(post("/api/types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(type)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Type> typeList = typeRepository.findAll();
        assertThat(typeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTypes() throws Exception {
        // Initialize the database
        typeRepository.saveAndFlush(type);

        // Get all the typeList
        restTypeMockMvc.perform(get("/api/types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(type.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].reserves").value(hasItem(DEFAULT_RESERVES.intValue())))
            .andExpect(jsonPath("$.[*].limit").value(hasItem(DEFAULT_LIMIT.intValue())))
            .andExpect(jsonPath("$.[*].delFlag").value(hasItem(DEFAULT_DEL_FLAG)))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT.toString())));
    }

    @Test
    @Transactional
    public void getType() throws Exception {
        // Initialize the database
        typeRepository.saveAndFlush(type);

        // Get the type
        restTypeMockMvc.perform(get("/api/types/{id}", type.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(type.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.reserves").value(DEFAULT_RESERVES.intValue()))
            .andExpect(jsonPath("$.limit").value(DEFAULT_LIMIT.intValue()))
            .andExpect(jsonPath("$.delFlag").value(DEFAULT_DEL_FLAG))
            .andExpect(jsonPath("$.unit").value(DEFAULT_UNIT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingType() throws Exception {
        // Get the type
        restTypeMockMvc.perform(get("/api/types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateType() throws Exception {
        // Initialize the database
        typeRepository.saveAndFlush(type);
        int databaseSizeBeforeUpdate = typeRepository.findAll().size();

        // Update the type
        Type updatedType = typeRepository.findOne(type.getId());
        updatedType
            .name(UPDATED_NAME)
            .reserves(UPDATED_RESERVES)
            .limit(UPDATED_LIMIT)
            .delFlag(UPDATED_DEL_FLAG)
            .unit(UPDATED_UNIT);

        restTypeMockMvc.perform(put("/api/types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedType)))
            .andExpect(status().isOk());

        // Validate the Type in the database
        List<Type> typeList = typeRepository.findAll();
        assertThat(typeList).hasSize(databaseSizeBeforeUpdate);
        Type testType = typeList.get(typeList.size() - 1);
        assertThat(testType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testType.getReserves()).isEqualTo(UPDATED_RESERVES);
        assertThat(testType.getLimit()).isEqualTo(UPDATED_LIMIT);
        assertThat(testType.getDelFlag()).isEqualTo(UPDATED_DEL_FLAG);
        assertThat(testType.getUnit()).isEqualTo(UPDATED_UNIT);
    }

    @Test
    @Transactional
    public void updateNonExistingType() throws Exception {
        int databaseSizeBeforeUpdate = typeRepository.findAll().size();

        // Create the Type

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTypeMockMvc.perform(put("/api/types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(type)))
            .andExpect(status().isCreated());

        // Validate the Type in the database
        List<Type> typeList = typeRepository.findAll();
        assertThat(typeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteType() throws Exception {
        // Initialize the database
        typeRepository.saveAndFlush(type);
        int databaseSizeBeforeDelete = typeRepository.findAll().size();

        // Get the type
        restTypeMockMvc.perform(delete("/api/types/{id}", type.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Type> typeList = typeRepository.findAll();
        assertThat(typeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Type.class);
        Type type1 = new Type();
        type1.setId(1L);
        Type type2 = new Type();
        type2.setId(type1.getId());
        assertThat(type1).isEqualTo(type2);
        type2.setId(2L);
        assertThat(type1).isNotEqualTo(type2);
        type1.setId(null);
        assertThat(type1).isNotEqualTo(type2);
    }
}
