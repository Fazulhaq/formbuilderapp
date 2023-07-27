package com.mcit.formbuilderapp.web.rest;

import com.mcit.formbuilderapp.domain.FilledForm;
import com.mcit.formbuilderapp.repository.FilledFormRepository;
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
 * REST controller for managing {@link com.mcit.formbuilderapp.domain.FilledForm}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FilledFormResource {

    private final Logger log = LoggerFactory.getLogger(FilledFormResource.class);

    private static final String ENTITY_NAME = "filledForm";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FilledFormRepository filledFormRepository;

    public FilledFormResource(FilledFormRepository filledFormRepository) {
        this.filledFormRepository = filledFormRepository;
    }

    /**
     * {@code POST  /filled-forms} : Create a new filledForm.
     *
     * @param filledForm the filledForm to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new filledForm, or with status {@code 400 (Bad Request)} if the filledForm has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/filled-forms")
    public ResponseEntity<FilledForm> createFilledForm(@Valid @RequestBody FilledForm filledForm) throws URISyntaxException {
        log.debug("REST request to save FilledForm : {}", filledForm);
        if (filledForm.getId() != null) {
            throw new BadRequestAlertException("A new filledForm cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FilledForm result = filledFormRepository.save(filledForm);
        return ResponseEntity
            .created(new URI("/api/filled-forms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /filled-forms/:id} : Updates an existing filledForm.
     *
     * @param id the id of the filledForm to save.
     * @param filledForm the filledForm to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated filledForm,
     * or with status {@code 400 (Bad Request)} if the filledForm is not valid,
     * or with status {@code 500 (Internal Server Error)} if the filledForm couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/filled-forms/{id}")
    public ResponseEntity<FilledForm> updateFilledForm(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FilledForm filledForm
    ) throws URISyntaxException {
        log.debug("REST request to update FilledForm : {}, {}", id, filledForm);
        if (filledForm.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, filledForm.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!filledFormRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FilledForm result = filledFormRepository.save(filledForm);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, filledForm.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /filled-forms/:id} : Partial updates given fields of an existing filledForm, field will ignore if it is null
     *
     * @param id the id of the filledForm to save.
     * @param filledForm the filledForm to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated filledForm,
     * or with status {@code 400 (Bad Request)} if the filledForm is not valid,
     * or with status {@code 404 (Not Found)} if the filledForm is not found,
     * or with status {@code 500 (Internal Server Error)} if the filledForm couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/filled-forms/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FilledForm> partialUpdateFilledForm(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FilledForm filledForm
    ) throws URISyntaxException {
        log.debug("REST request to partial update FilledForm partially : {}, {}", id, filledForm);
        if (filledForm.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, filledForm.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!filledFormRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FilledForm> result = filledFormRepository
            .findById(filledForm.getId())
            .map(existingFilledForm -> {
                if (filledForm.getjSONText() != null) {
                    existingFilledForm.setjSONText(filledForm.getjSONText());
                }

                return existingFilledForm;
            })
            .map(filledFormRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, filledForm.getId().toString())
        );
    }

    /**
     * {@code GET  /filled-forms} : get all the filledForms.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of filledForms in body.
     */
    @GetMapping("/filled-forms")
    public ResponseEntity<List<FilledForm>> getAllFilledForms(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of FilledForms");
        Page<FilledForm> page = filledFormRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /filled-forms/:id} : get the "id" filledForm.
     *
     * @param id the id of the filledForm to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the filledForm, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/filled-forms/{id}")
    public ResponseEntity<FilledForm> getFilledForm(@PathVariable Long id) {
        log.debug("REST request to get FilledForm : {}", id);
        Optional<FilledForm> filledForm = filledFormRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(filledForm);
    }

    /**
     * {@code DELETE  /filled-forms/:id} : delete the "id" filledForm.
     *
     * @param id the id of the filledForm to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/filled-forms/{id}")
    public ResponseEntity<Void> deleteFilledForm(@PathVariable Long id) {
        log.debug("REST request to delete FilledForm : {}", id);
        filledFormRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
