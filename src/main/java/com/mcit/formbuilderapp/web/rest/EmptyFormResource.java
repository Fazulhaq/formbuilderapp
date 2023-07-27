package com.mcit.formbuilderapp.web.rest;

import com.mcit.formbuilderapp.domain.EmptyForm;
import com.mcit.formbuilderapp.repository.EmptyFormRepository;
import com.mcit.formbuilderapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.mcit.formbuilderapp.domain.EmptyForm}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class EmptyFormResource {

    private final Logger log = LoggerFactory.getLogger(EmptyFormResource.class);

    private static final String ENTITY_NAME = "emptyForm";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmptyFormRepository emptyFormRepository;

    public EmptyFormResource(EmptyFormRepository emptyFormRepository) {
        this.emptyFormRepository = emptyFormRepository;
    }

    /**
     * {@code POST  /empty-forms} : Create a new emptyForm.
     *
     * @param emptyForm the emptyForm to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new emptyForm, or with status {@code 400 (Bad Request)} if the emptyForm has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/empty-forms")
    public ResponseEntity<EmptyForm> createEmptyForm(@Valid @RequestBody EmptyForm emptyForm) throws URISyntaxException {
        log.debug("REST request to save EmptyForm : {}", emptyForm);
        if (emptyForm.getId() != null) {
            throw new BadRequestAlertException("A new emptyForm cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EmptyForm result = emptyFormRepository.save(emptyForm);
        return ResponseEntity
            .created(new URI("/api/empty-forms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /empty-forms/:id} : Updates an existing emptyForm.
     *
     * @param id the id of the emptyForm to save.
     * @param emptyForm the emptyForm to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated emptyForm,
     * or with status {@code 400 (Bad Request)} if the emptyForm is not valid,
     * or with status {@code 500 (Internal Server Error)} if the emptyForm couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/empty-forms/{id}")
    public ResponseEntity<EmptyForm> updateEmptyForm(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EmptyForm emptyForm
    ) throws URISyntaxException {
        log.debug("REST request to update EmptyForm : {}, {}", id, emptyForm);
        if (emptyForm.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, emptyForm.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!emptyFormRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EmptyForm result = emptyFormRepository.save(emptyForm);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, emptyForm.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /empty-forms/:id} : Partial updates given fields of an existing emptyForm, field will ignore if it is null
     *
     * @param id the id of the emptyForm to save.
     * @param emptyForm the emptyForm to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated emptyForm,
     * or with status {@code 400 (Bad Request)} if the emptyForm is not valid,
     * or with status {@code 404 (Not Found)} if the emptyForm is not found,
     * or with status {@code 500 (Internal Server Error)} if the emptyForm couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/empty-forms/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EmptyForm> partialUpdateEmptyForm(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EmptyForm emptyForm
    ) throws URISyntaxException {
        log.debug("REST request to partial update EmptyForm partially : {}, {}", id, emptyForm);
        if (emptyForm.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, emptyForm.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!emptyFormRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EmptyForm> result = emptyFormRepository
            .findById(emptyForm.getId())
            .map(existingEmptyForm -> {
                if (emptyForm.getjSONText() != null) {
                    existingEmptyForm.setjSONText(emptyForm.getjSONText());
                }

                return existingEmptyForm;
            })
            .map(emptyFormRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, emptyForm.getId().toString())
        );
    }

    /**
     * {@code GET  /empty-forms} : get all the emptyForms.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of emptyForms in body.
     */
    @GetMapping("/empty-forms")
    public ResponseEntity<List<EmptyForm>> getAllEmptyForms(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of EmptyForms");
        Page<EmptyForm> page = emptyFormRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /empty-forms/:id} : get the "id" emptyForm.
     *
     * @param id the id of the emptyForm to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the emptyForm, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/empty-forms/{id}")
    public ResponseEntity<EmptyForm> getEmptyForm(@PathVariable Long id) {
        log.debug("REST request to get EmptyForm : {}", id);
        Optional<EmptyForm> emptyForm = emptyFormRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(emptyForm);
    }

    /**
     * {@code DELETE  /empty-forms/:id} : delete the "id" emptyForm.
     *
     * @param id the id of the emptyForm to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/empty-forms/{id}")
    public ResponseEntity<Void> deleteEmptyForm(@PathVariable Long id) {
        log.debug("REST request to delete EmptyForm : {}", id);
        emptyFormRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
