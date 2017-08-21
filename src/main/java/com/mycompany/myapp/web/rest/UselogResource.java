package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Uselog;

import com.mycompany.myapp.repository.UselogRepository;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Uselog.
 */
@RestController
@RequestMapping("/api")
public class UselogResource {

    private final Logger log = LoggerFactory.getLogger(UselogResource.class);

    private static final String ENTITY_NAME = "uselog";

    private final UselogRepository uselogRepository;

    public UselogResource(UselogRepository uselogRepository) {
        this.uselogRepository = uselogRepository;
    }

    /**
     * POST  /uselogs : Create a new uselog.
     *
     * @param uselog the uselog to create
     * @return the ResponseEntity with status 201 (Created) and with body the new uselog, or with status 400 (Bad Request) if the uselog has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/uselogs")
    @Timed
    public ResponseEntity<Uselog> createUselog(@RequestBody Uselog uselog) throws URISyntaxException {
        log.debug("REST request to save Uselog : {}", uselog);
        if (uselog.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new uselog cannot already have an ID")).body(null);
        }
        Uselog result = uselogRepository.save(uselog);
        return ResponseEntity.created(new URI("/api/uselogs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /uselogs : Updates an existing uselog.
     *
     * @param uselog the uselog to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated uselog,
     * or with status 400 (Bad Request) if the uselog is not valid,
     * or with status 500 (Internal Server Error) if the uselog couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/uselogs")
    @Timed
    public ResponseEntity<Uselog> updateUselog(@RequestBody Uselog uselog) throws URISyntaxException {
        log.debug("REST request to update Uselog : {}", uselog);
        if (uselog.getId() == null) {
            return createUselog(uselog);
        }
        Uselog result = uselogRepository.save(uselog);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, uselog.getId().toString()))
            .body(result);
    }

    /**
     * GET  /uselogs : get all the uselogs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of uselogs in body
     */
    @GetMapping("/uselogs")
    @Timed
    public List<Uselog> getAllUselogs() {
        log.debug("REST request to get all Uselogs");
        return uselogRepository.findAll();
    }

    /**
     * GET  /uselogs/:id : get the "id" uselog.
     *
     * @param id the id of the uselog to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the uselog, or with status 404 (Not Found)
     */
    @GetMapping("/uselogs/{id}")
    @Timed
    public ResponseEntity<Uselog> getUselog(@PathVariable Long id) {
        log.debug("REST request to get Uselog : {}", id);
        Uselog uselog = uselogRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(uselog));
    }

    /**
     * DELETE  /uselogs/:id : delete the "id" uselog.
     *
     * @param id the id of the uselog to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/uselogs/{id}")
    @Timed
    public ResponseEntity<Void> deleteUselog(@PathVariable Long id) {
        log.debug("REST request to delete Uselog : {}", id);
        uselogRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
