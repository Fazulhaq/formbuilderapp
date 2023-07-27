package com.mcit.formbuilderapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mcit.formbuilderapp.IntegrationTest;
import com.mcit.formbuilderapp.domain.EmptyForm;
import com.mcit.formbuilderapp.repository.EmptyFormRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EmptyFormResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmptyFormResourceIT {

    private static final String DEFAULT_J_SON_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_J_SON_TEXT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/empty-forms";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmptyFormRepository emptyFormRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmptyFormMockMvc;

    private EmptyForm emptyForm;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmptyForm createEntity(EntityManager em) {
        EmptyForm emptyForm = new EmptyForm().jSONText(DEFAULT_J_SON_TEXT);
        return emptyForm;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmptyForm createUpdatedEntity(EntityManager em) {
        EmptyForm emptyForm = new EmptyForm().jSONText(UPDATED_J_SON_TEXT);
        return emptyForm;
    }

    @BeforeEach
    public void initTest() {
        emptyForm = createEntity(em);
    }

    @Test
    @Transactional
    void createEmptyForm() throws Exception {
        int databaseSizeBeforeCreate = emptyFormRepository.findAll().size();
        // Create the EmptyForm
        restEmptyFormMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(emptyForm)))
            .andExpect(status().isCreated());

        // Validate the EmptyForm in the database
        List<EmptyForm> emptyFormList = emptyFormRepository.findAll();
        assertThat(emptyFormList).hasSize(databaseSizeBeforeCreate + 1);
        EmptyForm testEmptyForm = emptyFormList.get(emptyFormList.size() - 1);
        assertThat(testEmptyForm.getjSONText()).isEqualTo(DEFAULT_J_SON_TEXT);
    }

    @Test
    @Transactional
    void createEmptyFormWithExistingId() throws Exception {
        // Create the EmptyForm with an existing ID
        emptyForm.setId(1L);

        int databaseSizeBeforeCreate = emptyFormRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmptyFormMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(emptyForm)))
            .andExpect(status().isBadRequest());

        // Validate the EmptyForm in the database
        List<EmptyForm> emptyFormList = emptyFormRepository.findAll();
        assertThat(emptyFormList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkjSONTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = emptyFormRepository.findAll().size();
        // set the field null
        emptyForm.setjSONText(null);

        // Create the EmptyForm, which fails.

        restEmptyFormMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(emptyForm)))
            .andExpect(status().isBadRequest());

        List<EmptyForm> emptyFormList = emptyFormRepository.findAll();
        assertThat(emptyFormList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEmptyForms() throws Exception {
        // Initialize the database
        emptyFormRepository.saveAndFlush(emptyForm);

        // Get all the emptyFormList
        restEmptyFormMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(emptyForm.getId().intValue())))
            .andExpect(jsonPath("$.[*].jSONText").value(hasItem(DEFAULT_J_SON_TEXT)));
    }

    @Test
    @Transactional
    void getEmptyForm() throws Exception {
        // Initialize the database
        emptyFormRepository.saveAndFlush(emptyForm);

        // Get the emptyForm
        restEmptyFormMockMvc
            .perform(get(ENTITY_API_URL_ID, emptyForm.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(emptyForm.getId().intValue()))
            .andExpect(jsonPath("$.jSONText").value(DEFAULT_J_SON_TEXT));
    }

    @Test
    @Transactional
    void getNonExistingEmptyForm() throws Exception {
        // Get the emptyForm
        restEmptyFormMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmptyForm() throws Exception {
        // Initialize the database
        emptyFormRepository.saveAndFlush(emptyForm);

        int databaseSizeBeforeUpdate = emptyFormRepository.findAll().size();

        // Update the emptyForm
        EmptyForm updatedEmptyForm = emptyFormRepository.findById(emptyForm.getId()).get();
        // Disconnect from session so that the updates on updatedEmptyForm are not directly saved in db
        em.detach(updatedEmptyForm);
        updatedEmptyForm.jSONText(UPDATED_J_SON_TEXT);

        restEmptyFormMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEmptyForm.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEmptyForm))
            )
            .andExpect(status().isOk());

        // Validate the EmptyForm in the database
        List<EmptyForm> emptyFormList = emptyFormRepository.findAll();
        assertThat(emptyFormList).hasSize(databaseSizeBeforeUpdate);
        EmptyForm testEmptyForm = emptyFormList.get(emptyFormList.size() - 1);
        assertThat(testEmptyForm.getjSONText()).isEqualTo(UPDATED_J_SON_TEXT);
    }

    @Test
    @Transactional
    void putNonExistingEmptyForm() throws Exception {
        int databaseSizeBeforeUpdate = emptyFormRepository.findAll().size();
        emptyForm.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmptyFormMockMvc
            .perform(
                put(ENTITY_API_URL_ID, emptyForm.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(emptyForm))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmptyForm in the database
        List<EmptyForm> emptyFormList = emptyFormRepository.findAll();
        assertThat(emptyFormList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmptyForm() throws Exception {
        int databaseSizeBeforeUpdate = emptyFormRepository.findAll().size();
        emptyForm.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmptyFormMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(emptyForm))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmptyForm in the database
        List<EmptyForm> emptyFormList = emptyFormRepository.findAll();
        assertThat(emptyFormList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmptyForm() throws Exception {
        int databaseSizeBeforeUpdate = emptyFormRepository.findAll().size();
        emptyForm.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmptyFormMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(emptyForm)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmptyForm in the database
        List<EmptyForm> emptyFormList = emptyFormRepository.findAll();
        assertThat(emptyFormList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmptyFormWithPatch() throws Exception {
        // Initialize the database
        emptyFormRepository.saveAndFlush(emptyForm);

        int databaseSizeBeforeUpdate = emptyFormRepository.findAll().size();

        // Update the emptyForm using partial update
        EmptyForm partialUpdatedEmptyForm = new EmptyForm();
        partialUpdatedEmptyForm.setId(emptyForm.getId());

        partialUpdatedEmptyForm.jSONText(UPDATED_J_SON_TEXT);

        restEmptyFormMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmptyForm.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmptyForm))
            )
            .andExpect(status().isOk());

        // Validate the EmptyForm in the database
        List<EmptyForm> emptyFormList = emptyFormRepository.findAll();
        assertThat(emptyFormList).hasSize(databaseSizeBeforeUpdate);
        EmptyForm testEmptyForm = emptyFormList.get(emptyFormList.size() - 1);
        assertThat(testEmptyForm.getjSONText()).isEqualTo(UPDATED_J_SON_TEXT);
    }

    @Test
    @Transactional
    void fullUpdateEmptyFormWithPatch() throws Exception {
        // Initialize the database
        emptyFormRepository.saveAndFlush(emptyForm);

        int databaseSizeBeforeUpdate = emptyFormRepository.findAll().size();

        // Update the emptyForm using partial update
        EmptyForm partialUpdatedEmptyForm = new EmptyForm();
        partialUpdatedEmptyForm.setId(emptyForm.getId());

        partialUpdatedEmptyForm.jSONText(UPDATED_J_SON_TEXT);

        restEmptyFormMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmptyForm.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmptyForm))
            )
            .andExpect(status().isOk());

        // Validate the EmptyForm in the database
        List<EmptyForm> emptyFormList = emptyFormRepository.findAll();
        assertThat(emptyFormList).hasSize(databaseSizeBeforeUpdate);
        EmptyForm testEmptyForm = emptyFormList.get(emptyFormList.size() - 1);
        assertThat(testEmptyForm.getjSONText()).isEqualTo(UPDATED_J_SON_TEXT);
    }

    @Test
    @Transactional
    void patchNonExistingEmptyForm() throws Exception {
        int databaseSizeBeforeUpdate = emptyFormRepository.findAll().size();
        emptyForm.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmptyFormMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, emptyForm.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(emptyForm))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmptyForm in the database
        List<EmptyForm> emptyFormList = emptyFormRepository.findAll();
        assertThat(emptyFormList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmptyForm() throws Exception {
        int databaseSizeBeforeUpdate = emptyFormRepository.findAll().size();
        emptyForm.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmptyFormMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(emptyForm))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmptyForm in the database
        List<EmptyForm> emptyFormList = emptyFormRepository.findAll();
        assertThat(emptyFormList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmptyForm() throws Exception {
        int databaseSizeBeforeUpdate = emptyFormRepository.findAll().size();
        emptyForm.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmptyFormMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(emptyForm))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmptyForm in the database
        List<EmptyForm> emptyFormList = emptyFormRepository.findAll();
        assertThat(emptyFormList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmptyForm() throws Exception {
        // Initialize the database
        emptyFormRepository.saveAndFlush(emptyForm);

        int databaseSizeBeforeDelete = emptyFormRepository.findAll().size();

        // Delete the emptyForm
        restEmptyFormMockMvc
            .perform(delete(ENTITY_API_URL_ID, emptyForm.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EmptyForm> emptyFormList = emptyFormRepository.findAll();
        assertThat(emptyFormList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
