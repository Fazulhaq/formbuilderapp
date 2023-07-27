package com.mcit.formbuilderapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mcit.formbuilderapp.IntegrationTest;
import com.mcit.formbuilderapp.domain.FilledForm;
import com.mcit.formbuilderapp.repository.FilledFormRepository;
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
 * Integration tests for the {@link FilledFormResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FilledFormResourceIT {

    private static final String DEFAULT_J_SON_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_J_SON_TEXT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/filled-forms";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FilledFormRepository filledFormRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFilledFormMockMvc;

    private FilledForm filledForm;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FilledForm createEntity(EntityManager em) {
        FilledForm filledForm = new FilledForm().jSONText(DEFAULT_J_SON_TEXT);
        return filledForm;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FilledForm createUpdatedEntity(EntityManager em) {
        FilledForm filledForm = new FilledForm().jSONText(UPDATED_J_SON_TEXT);
        return filledForm;
    }

    @BeforeEach
    public void initTest() {
        filledForm = createEntity(em);
    }

    @Test
    @Transactional
    void createFilledForm() throws Exception {
        int databaseSizeBeforeCreate = filledFormRepository.findAll().size();
        // Create the FilledForm
        restFilledFormMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(filledForm)))
            .andExpect(status().isCreated());

        // Validate the FilledForm in the database
        List<FilledForm> filledFormList = filledFormRepository.findAll();
        assertThat(filledFormList).hasSize(databaseSizeBeforeCreate + 1);
        FilledForm testFilledForm = filledFormList.get(filledFormList.size() - 1);
        assertThat(testFilledForm.getjSONText()).isEqualTo(DEFAULT_J_SON_TEXT);
    }

    @Test
    @Transactional
    void createFilledFormWithExistingId() throws Exception {
        // Create the FilledForm with an existing ID
        filledForm.setId(1L);

        int databaseSizeBeforeCreate = filledFormRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFilledFormMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(filledForm)))
            .andExpect(status().isBadRequest());

        // Validate the FilledForm in the database
        List<FilledForm> filledFormList = filledFormRepository.findAll();
        assertThat(filledFormList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkjSONTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = filledFormRepository.findAll().size();
        // set the field null
        filledForm.setjSONText(null);

        // Create the FilledForm, which fails.

        restFilledFormMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(filledForm)))
            .andExpect(status().isBadRequest());

        List<FilledForm> filledFormList = filledFormRepository.findAll();
        assertThat(filledFormList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFilledForms() throws Exception {
        // Initialize the database
        filledFormRepository.saveAndFlush(filledForm);

        // Get all the filledFormList
        restFilledFormMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(filledForm.getId().intValue())))
            .andExpect(jsonPath("$.[*].jSONText").value(hasItem(DEFAULT_J_SON_TEXT)));
    }

    @Test
    @Transactional
    void getFilledForm() throws Exception {
        // Initialize the database
        filledFormRepository.saveAndFlush(filledForm);

        // Get the filledForm
        restFilledFormMockMvc
            .perform(get(ENTITY_API_URL_ID, filledForm.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(filledForm.getId().intValue()))
            .andExpect(jsonPath("$.jSONText").value(DEFAULT_J_SON_TEXT));
    }

    @Test
    @Transactional
    void getNonExistingFilledForm() throws Exception {
        // Get the filledForm
        restFilledFormMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFilledForm() throws Exception {
        // Initialize the database
        filledFormRepository.saveAndFlush(filledForm);

        int databaseSizeBeforeUpdate = filledFormRepository.findAll().size();

        // Update the filledForm
        FilledForm updatedFilledForm = filledFormRepository.findById(filledForm.getId()).get();
        // Disconnect from session so that the updates on updatedFilledForm are not directly saved in db
        em.detach(updatedFilledForm);
        updatedFilledForm.jSONText(UPDATED_J_SON_TEXT);

        restFilledFormMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFilledForm.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFilledForm))
            )
            .andExpect(status().isOk());

        // Validate the FilledForm in the database
        List<FilledForm> filledFormList = filledFormRepository.findAll();
        assertThat(filledFormList).hasSize(databaseSizeBeforeUpdate);
        FilledForm testFilledForm = filledFormList.get(filledFormList.size() - 1);
        assertThat(testFilledForm.getjSONText()).isEqualTo(UPDATED_J_SON_TEXT);
    }

    @Test
    @Transactional
    void putNonExistingFilledForm() throws Exception {
        int databaseSizeBeforeUpdate = filledFormRepository.findAll().size();
        filledForm.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFilledFormMockMvc
            .perform(
                put(ENTITY_API_URL_ID, filledForm.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(filledForm))
            )
            .andExpect(status().isBadRequest());

        // Validate the FilledForm in the database
        List<FilledForm> filledFormList = filledFormRepository.findAll();
        assertThat(filledFormList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFilledForm() throws Exception {
        int databaseSizeBeforeUpdate = filledFormRepository.findAll().size();
        filledForm.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFilledFormMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(filledForm))
            )
            .andExpect(status().isBadRequest());

        // Validate the FilledForm in the database
        List<FilledForm> filledFormList = filledFormRepository.findAll();
        assertThat(filledFormList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFilledForm() throws Exception {
        int databaseSizeBeforeUpdate = filledFormRepository.findAll().size();
        filledForm.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFilledFormMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(filledForm)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FilledForm in the database
        List<FilledForm> filledFormList = filledFormRepository.findAll();
        assertThat(filledFormList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFilledFormWithPatch() throws Exception {
        // Initialize the database
        filledFormRepository.saveAndFlush(filledForm);

        int databaseSizeBeforeUpdate = filledFormRepository.findAll().size();

        // Update the filledForm using partial update
        FilledForm partialUpdatedFilledForm = new FilledForm();
        partialUpdatedFilledForm.setId(filledForm.getId());

        restFilledFormMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFilledForm.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFilledForm))
            )
            .andExpect(status().isOk());

        // Validate the FilledForm in the database
        List<FilledForm> filledFormList = filledFormRepository.findAll();
        assertThat(filledFormList).hasSize(databaseSizeBeforeUpdate);
        FilledForm testFilledForm = filledFormList.get(filledFormList.size() - 1);
        assertThat(testFilledForm.getjSONText()).isEqualTo(DEFAULT_J_SON_TEXT);
    }

    @Test
    @Transactional
    void fullUpdateFilledFormWithPatch() throws Exception {
        // Initialize the database
        filledFormRepository.saveAndFlush(filledForm);

        int databaseSizeBeforeUpdate = filledFormRepository.findAll().size();

        // Update the filledForm using partial update
        FilledForm partialUpdatedFilledForm = new FilledForm();
        partialUpdatedFilledForm.setId(filledForm.getId());

        partialUpdatedFilledForm.jSONText(UPDATED_J_SON_TEXT);

        restFilledFormMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFilledForm.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFilledForm))
            )
            .andExpect(status().isOk());

        // Validate the FilledForm in the database
        List<FilledForm> filledFormList = filledFormRepository.findAll();
        assertThat(filledFormList).hasSize(databaseSizeBeforeUpdate);
        FilledForm testFilledForm = filledFormList.get(filledFormList.size() - 1);
        assertThat(testFilledForm.getjSONText()).isEqualTo(UPDATED_J_SON_TEXT);
    }

    @Test
    @Transactional
    void patchNonExistingFilledForm() throws Exception {
        int databaseSizeBeforeUpdate = filledFormRepository.findAll().size();
        filledForm.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFilledFormMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, filledForm.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(filledForm))
            )
            .andExpect(status().isBadRequest());

        // Validate the FilledForm in the database
        List<FilledForm> filledFormList = filledFormRepository.findAll();
        assertThat(filledFormList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFilledForm() throws Exception {
        int databaseSizeBeforeUpdate = filledFormRepository.findAll().size();
        filledForm.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFilledFormMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(filledForm))
            )
            .andExpect(status().isBadRequest());

        // Validate the FilledForm in the database
        List<FilledForm> filledFormList = filledFormRepository.findAll();
        assertThat(filledFormList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFilledForm() throws Exception {
        int databaseSizeBeforeUpdate = filledFormRepository.findAll().size();
        filledForm.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFilledFormMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(filledForm))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FilledForm in the database
        List<FilledForm> filledFormList = filledFormRepository.findAll();
        assertThat(filledFormList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFilledForm() throws Exception {
        // Initialize the database
        filledFormRepository.saveAndFlush(filledForm);

        int databaseSizeBeforeDelete = filledFormRepository.findAll().size();

        // Delete the filledForm
        restFilledFormMockMvc
            .perform(delete(ENTITY_API_URL_ID, filledForm.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FilledForm> filledFormList = filledFormRepository.findAll();
        assertThat(filledFormList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
