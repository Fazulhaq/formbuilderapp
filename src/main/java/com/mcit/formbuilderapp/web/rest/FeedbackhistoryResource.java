package com.mcit.formbuilderapp.web.rest;

import com.mcit.formbuilderapp.domain.Feedbackhistory;
import com.mcit.formbuilderapp.repository.FeedbackhistoryRepository;
import com.mcit.formbuilderapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mcit.formbuilderapp.domain.Feedbackhistory}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FeedbackhistoryResource {

    private final Logger log = LoggerFactory.getLogger(FeedbackhistoryResource.class);

    private static final String ENTITY_NAME = "feedbackhistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FeedbackhistoryRepository feedbackhistoryRepository;

    public FeedbackhistoryResource(FeedbackhistoryRepository feedbackhistoryRepository) {
        this.feedbackhistoryRepository = feedbackhistoryRepository;
    }

    /**
     * {@code POST  /feedbackhistories} : Create a new feedbackhistory.
     *
     * @param feedbackhistory the feedbackhistory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new feedbackhistory, or with status {@code 400 (Bad Request)} if the feedbackhistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/feedbackhistories")
    public ResponseEntity<Feedbackhistory> createFeedbackhistory(@RequestBody Feedbackhistory feedbackhistory) throws URISyntaxException {
        log.debug("REST request to save Feedbackhistory : {}", feedbackhistory);
        if (feedbackhistory.getId() != null) {
            throw new BadRequestAlertException("A new feedbackhistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Feedbackhistory result = feedbackhistoryRepository.save(feedbackhistory);
        return ResponseEntity
            .created(new URI("/api/feedbackhistories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /feedbackhistories/:id} : Updates an existing feedbackhistory.
     *
     * @param id the id of the feedbackhistory to save.
     * @param feedbackhistory the feedbackhistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated feedbackhistory,
     * or with status {@code 400 (Bad Request)} if the feedbackhistory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the feedbackhistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/feedbackhistories/{id}")
    public ResponseEntity<Feedbackhistory> updateFeedbackhistory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Feedbackhistory feedbackhistory
    ) throws URISyntaxException {
        log.debug("REST request to update Feedbackhistory : {}, {}", id, feedbackhistory);
        if (feedbackhistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, feedbackhistory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!feedbackhistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Feedbackhistory result = feedbackhistoryRepository.save(feedbackhistory);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, feedbackhistory.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /feedbackhistories/:id} : Partial updates given fields of an existing feedbackhistory, field will ignore if it is null
     *
     * @param id the id of the feedbackhistory to save.
     * @param feedbackhistory the feedbackhistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated feedbackhistory,
     * or with status {@code 400 (Bad Request)} if the feedbackhistory is not valid,
     * or with status {@code 404 (Not Found)} if the feedbackhistory is not found,
     * or with status {@code 500 (Internal Server Error)} if the feedbackhistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/feedbackhistories/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Feedbackhistory> partialUpdateFeedbackhistory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Feedbackhistory feedbackhistory
    ) throws URISyntaxException {
        log.debug("REST request to partial update Feedbackhistory partially : {}, {}", id, feedbackhistory);
        if (feedbackhistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, feedbackhistory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!feedbackhistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Feedbackhistory> result = feedbackhistoryRepository
            .findById(feedbackhistory.getId())
            .map(existingFeedbackhistory -> {
                if (feedbackhistory.getFeedbackText() != null) {
                    existingFeedbackhistory.setFeedbackText(feedbackhistory.getFeedbackText());
                }
                if (feedbackhistory.getFeedbackDate() != null) {
                    existingFeedbackhistory.setFeedbackDate(feedbackhistory.getFeedbackDate());
                }

                return existingFeedbackhistory;
            })
            .map(feedbackhistoryRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, feedbackhistory.getId().toString())
        );
    }

    /**
     * {@code GET  /feedbackhistories} : get all the feedbackhistories.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of feedbackhistories in body.
     */
    @GetMapping("/feedbackhistories")
    public ResponseEntity<List<Feedbackhistory>> getAllFeedbackhistories(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Feedbackhistories");
        Page<Feedbackhistory> page = feedbackhistoryRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /feedbackhistories/:id} : get the "id" feedbackhistory.
     *
     * @param id the id of the feedbackhistory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the feedbackhistory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/feedbackhistories/{id}")
    public ResponseEntity<Feedbackhistory> getFeedbackhistory(@PathVariable Long id) {
        log.debug("REST request to get Feedbackhistory : {}", id);
        Optional<Feedbackhistory> feedbackhistory = feedbackhistoryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(feedbackhistory);
    }

    /**
     * {@code DELETE  /feedbackhistories/:id} : delete the "id" feedbackhistory.
     *
     * @param id the id of the feedbackhistory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/feedbackhistories/{id}")
    public ResponseEntity<Void> deleteFeedbackhistory(@PathVariable Long id) {
        log.debug("REST request to delete Feedbackhistory : {}", id);
        feedbackhistoryRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
