package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.UseLog;

import com.mycompany.myapp.repository.UseLogRepository;
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
 * REST controller for managing UseLog.
 */
@RestController
@RequestMapping("/api")
public class UseLogResource {

    private final Logger log = LoggerFactory.getLogger(UseLogResource.class);

    private static final String ENTITY_NAME = "useLog";

    private final UseLogRepository useLogRepository;

    public UseLogResource(UseLogRepository useLogRepository) {
        this.useLogRepository = useLogRepository;
    }

    /**
     * POST  /use-logs : Create a new useLog.
     *
     * @param useLog the useLog to create
     * @return the ResponseEntity with status 201 (Created) and with body the new useLog, or with status 400 (Bad Request) if the useLog has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/use-logs")
    @Timed
    public ResponseEntity<UseLog> createUseLog(@RequestBody UseLog useLog) throws URISyntaxException {
        log.debug("REST request to save UseLog : {}", useLog);
        if (useLog.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new useLog cannot already have an ID")).body(null);
        }
        UseLog result = useLogRepository.save(useLog);
        return ResponseEntity.created(new URI("/api/use-logs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /use-logs : Updates an existing useLog.
     *
     * @param useLog the useLog to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated useLog,
     * or with status 400 (Bad Request) if the useLog is not valid,
     * or with status 500 (Internal Server Error) if the useLog couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/use-logs")
    @Timed
    public ResponseEntity<UseLog> updateUseLog(@RequestBody UseLog useLog) throws URISyntaxException {
        log.debug("REST request to update UseLog : {}", useLog);
        if (useLog.getId() == null) {
            return createUseLog(useLog);
        }
        UseLog result = useLogRepository.save(useLog);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, useLog.getId().toString()))
            .body(result);
    }

    /**
     * GET  /use-logs : get all the useLogs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of useLogs in body
     */
    @GetMapping("/use-logs")
    @Timed
    public List<UseLog> getAllUseLogs() {
        log.debug("REST request to get all UseLogs");
        return useLogRepository.findAll();
    }

    /**
     * GET  /use-logs/:id : get the "id" useLog.
     *
     * @param id the id of the useLog to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the useLog, or with status 404 (Not Found)
     */
    @GetMapping("/use-logs/{id}")
    @Timed
    public ResponseEntity<UseLog> getUseLog(@PathVariable Long id) {
        log.debug("REST request to get UseLog : {}", id);
        UseLog useLog = useLogRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(useLog));
    }

    /**
     * DELETE  /use-logs/:id : delete the "id" useLog.
     *
     * @param id the id of the useLog to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/use-logs/{id}")
    @Timed
    public ResponseEntity<Void> deleteUseLog(@PathVariable Long id) {
        log.debug("REST request to delete UseLog : {}", id);
        useLogRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
