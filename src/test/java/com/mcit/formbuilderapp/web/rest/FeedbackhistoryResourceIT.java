package com.mcit.formbuilderapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mcit.formbuilderapp.IntegrationTest;
import com.mcit.formbuilderapp.domain.Feedbackhistory;
import com.mcit.formbuilderapp.repository.FeedbackhistoryRepository;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link FeedbackhistoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FeedbackhistoryResourceIT {

    private static final String DEFAULT_FEEDBACK_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_FEEDBACK_TEXT = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FEEDBACK_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FEEDBACK_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/feedbackhistories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FeedbackhistoryRepository feedbackhistoryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFeedbackhistoryMockMvc;

    private Feedbackhistory feedbackhistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Feedbackhistory createEntity(EntityManager em) {
        Feedbackhistory feedbackhistory = new Feedbackhistory().feedbackText(DEFAULT_FEEDBACK_TEXT).feedbackDate(DEFAULT_FEEDBACK_DATE);
        return feedbackhistory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Feedbackhistory createUpdatedEntity(EntityManager em) {
        Feedbackhistory feedbackhistory = new Feedbackhistory().feedbackText(UPDATED_FEEDBACK_TEXT).feedbackDate(UPDATED_FEEDBACK_DATE);
        return feedbackhistory;
    }

    @BeforeEach
    public void initTest() {
        feedbackhistory = createEntity(em);
    }

    @Test
    @Transactional
    void createFeedbackhistory() throws Exception {
        int databaseSizeBeforeCreate = feedbackhistoryRepository.findAll().size();
        // Create the Feedbackhistory
        restFeedbackhistoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(feedbackhistory))
            )
            .andExpect(status().isCreated());

        // Validate the Feedbackhistory in the database
        List<Feedbackhistory> feedbackhistoryList = feedbackhistoryRepository.findAll();
        assertThat(feedbackhistoryList).hasSize(databaseSizeBeforeCreate + 1);
        Feedbackhistory testFeedbackhistory = feedbackhistoryList.get(feedbackhistoryList.size() - 1);
        assertThat(testFeedbackhistory.getFeedbackText()).isEqualTo(DEFAULT_FEEDBACK_TEXT);
        assertThat(testFeedbackhistory.getFeedbackDate()).isEqualTo(DEFAULT_FEEDBACK_DATE);
    }

    @Test
    @Transactional
    void createFeedbackhistoryWithExistingId() throws Exception {
        // Create the Feedbackhistory with an existing ID
        feedbackhistory.setId(1L);

        int databaseSizeBeforeCreate = feedbackhistoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFeedbackhistoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(feedbackhistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the Feedbackhistory in the database
        List<Feedbackhistory> feedbackhistoryList = feedbackhistoryRepository.findAll();
        assertThat(feedbackhistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFeedbackhistories() throws Exception {
        // Initialize the database
        feedbackhistoryRepository.saveAndFlush(feedbackhistory);

        // Get all the feedbackhistoryList
        restFeedbackhistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(feedbackhistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].feedbackText").value(hasItem(DEFAULT_FEEDBACK_TEXT)))
            .andExpect(jsonPath("$.[*].feedbackDate").value(hasItem(DEFAULT_FEEDBACK_DATE.toString())));
    }

    @Test
    @Transactional
    void getFeedbackhistory() throws Exception {
        // Initialize the database
        feedbackhistoryRepository.saveAndFlush(feedbackhistory);

        // Get the feedbackhistory
        restFeedbackhistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, feedbackhistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(feedbackhistory.getId().intValue()))
            .andExpect(jsonPath("$.feedbackText").value(DEFAULT_FEEDBACK_TEXT))
            .andExpect(jsonPath("$.feedbackDate").value(DEFAULT_FEEDBACK_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingFeedbackhistory() throws Exception {
        // Get the feedbackhistory
        restFeedbackhistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFeedbackhistory() throws Exception {
        // Initialize the database
        feedbackhistoryRepository.saveAndFlush(feedbackhistory);

        int databaseSizeBeforeUpdate = feedbackhistoryRepository.findAll().size();

        // Update the feedbackhistory
        Feedbackhistory updatedFeedbackhistory = feedbackhistoryRepository.findById(feedbackhistory.getId()).get();
        // Disconnect from session so that the updates on updatedFeedbackhistory are not directly saved in db
        em.detach(updatedFeedbackhistory);
        updatedFeedbackhistory.feedbackText(UPDATED_FEEDBACK_TEXT).feedbackDate(UPDATED_FEEDBACK_DATE);

        restFeedbackhistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFeedbackhistory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFeedbackhistory))
            )
            .andExpect(status().isOk());

        // Validate the Feedbackhistory in the database
        List<Feedbackhistory> feedbackhistoryList = feedbackhistoryRepository.findAll();
        assertThat(feedbackhistoryList).hasSize(databaseSizeBeforeUpdate);
        Feedbackhistory testFeedbackhistory = feedbackhistoryList.get(feedbackhistoryList.size() - 1);
        assertThat(testFeedbackhistory.getFeedbackText()).isEqualTo(UPDATED_FEEDBACK_TEXT);
        assertThat(testFeedbackhistory.getFeedbackDate()).isEqualTo(UPDATED_FEEDBACK_DATE);
    }

    @Test
    @Transactional
    void putNonExistingFeedbackhistory() throws Exception {
        int databaseSizeBeforeUpdate = feedbackhistoryRepository.findAll().size();
        feedbackhistory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeedbackhistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, feedbackhistory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(feedbackhistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the Feedbackhistory in the database
        List<Feedbackhistory> feedbackhistoryList = feedbackhistoryRepository.findAll();
        assertThat(feedbackhistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFeedbackhistory() throws Exception {
        int databaseSizeBeforeUpdate = feedbackhistoryRepository.findAll().size();
        feedbackhistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedbackhistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(feedbackhistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the Feedbackhistory in the database
        List<Feedbackhistory> feedbackhistoryList = feedbackhistoryRepository.findAll();
        assertThat(feedbackhistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFeedbackhistory() throws Exception {
        int databaseSizeBeforeUpdate = feedbackhistoryRepository.findAll().size();
        feedbackhistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedbackhistoryMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(feedbackhistory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Feedbackhistory in the database
        List<Feedbackhistory> feedbackhistoryList = feedbackhistoryRepository.findAll();
        assertThat(feedbackhistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFeedbackhistoryWithPatch() throws Exception {
        // Initialize the database
        feedbackhistoryRepository.saveAndFlush(feedbackhistory);

        int databaseSizeBeforeUpdate = feedbackhistoryRepository.findAll().size();

        // Update the feedbackhistory using partial update
        Feedbackhistory partialUpdatedFeedbackhistory = new Feedbackhistory();
        partialUpdatedFeedbackhistory.setId(feedbackhistory.getId());

        partialUpdatedFeedbackhistory.feedbackDate(UPDATED_FEEDBACK_DATE);

        restFeedbackhistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFeedbackhistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFeedbackhistory))
            )
            .andExpect(status().isOk());

        // Validate the Feedbackhistory in the database
        List<Feedbackhistory> feedbackhistoryList = feedbackhistoryRepository.findAll();
        assertThat(feedbackhistoryList).hasSize(databaseSizeBeforeUpdate);
        Feedbackhistory testFeedbackhistory = feedbackhistoryList.get(feedbackhistoryList.size() - 1);
        assertThat(testFeedbackhistory.getFeedbackText()).isEqualTo(DEFAULT_FEEDBACK_TEXT);
        assertThat(testFeedbackhistory.getFeedbackDate()).isEqualTo(UPDATED_FEEDBACK_DATE);
    }

    @Test
    @Transactional
    void fullUpdateFeedbackhistoryWithPatch() throws Exception {
        // Initialize the database
        feedbackhistoryRepository.saveAndFlush(feedbackhistory);

        int databaseSizeBeforeUpdate = feedbackhistoryRepository.findAll().size();

        // Update the feedbackhistory using partial update
        Feedbackhistory partialUpdatedFeedbackhistory = new Feedbackhistory();
        partialUpdatedFeedbackhistory.setId(feedbackhistory.getId());

        partialUpdatedFeedbackhistory.feedbackText(UPDATED_FEEDBACK_TEXT).feedbackDate(UPDATED_FEEDBACK_DATE);

        restFeedbackhistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFeedbackhistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFeedbackhistory))
            )
            .andExpect(status().isOk());

        // Validate the Feedbackhistory in the database
        List<Feedbackhistory> feedbackhistoryList = feedbackhistoryRepository.findAll();
        assertThat(feedbackhistoryList).hasSize(databaseSizeBeforeUpdate);
        Feedbackhistory testFeedbackhistory = feedbackhistoryList.get(feedbackhistoryList.size() - 1);
        assertThat(testFeedbackhistory.getFeedbackText()).isEqualTo(UPDATED_FEEDBACK_TEXT);
        assertThat(testFeedbackhistory.getFeedbackDate()).isEqualTo(UPDATED_FEEDBACK_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingFeedbackhistory() throws Exception {
        int databaseSizeBeforeUpdate = feedbackhistoryRepository.findAll().size();
        feedbackhistory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeedbackhistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, feedbackhistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(feedbackhistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the Feedbackhistory in the database
        List<Feedbackhistory> feedbackhistoryList = feedbackhistoryRepository.findAll();
        assertThat(feedbackhistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFeedbackhistory() throws Exception {
        int databaseSizeBeforeUpdate = feedbackhistoryRepository.findAll().size();
        feedbackhistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedbackhistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(feedbackhistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the Feedbackhistory in the database
        List<Feedbackhistory> feedbackhistoryList = feedbackhistoryRepository.findAll();
        assertThat(feedbackhistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFeedbackhistory() throws Exception {
        int databaseSizeBeforeUpdate = feedbackhistoryRepository.findAll().size();
        feedbackhistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedbackhistoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(feedbackhistory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Feedbackhistory in the database
        List<Feedbackhistory> feedbackhistoryList = feedbackhistoryRepository.findAll();
        assertThat(feedbackhistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFeedbackhistory() throws Exception {
        // Initialize the database
        feedbackhistoryRepository.saveAndFlush(feedbackhistory);

        int databaseSizeBeforeDelete = feedbackhistoryRepository.findAll().size();

        // Delete the feedbackhistory
        restFeedbackhistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, feedbackhistory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Feedbackhistory> feedbackhistoryList = feedbackhistoryRepository.findAll();
        assertThat(feedbackhistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
