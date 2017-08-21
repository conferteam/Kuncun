package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Alarm;

import com.mycompany.myapp.repository.AlarmRepository;
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
 * REST controller for managing Alarm.
 */
@RestController
@RequestMapping("/api")
public class AlarmResource {

    private final Logger log = LoggerFactory.getLogger(AlarmResource.class);

    private static final String ENTITY_NAME = "alarm";

    private final AlarmRepository alarmRepository;

    public AlarmResource(AlarmRepository alarmRepository) {
        this.alarmRepository = alarmRepository;
    }

    /**
     * POST  /alarms : Create a new alarm.
     *
     * @param alarm the alarm to create
     * @return the ResponseEntity with status 201 (Created) and with body the new alarm, or with status 400 (Bad Request) if the alarm has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/alarms")
    @Timed
    public ResponseEntity<Alarm> createAlarm(@RequestBody Alarm alarm) throws URISyntaxException {
        log.debug("REST request to save Alarm : {}", alarm);
        if (alarm.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new alarm cannot already have an ID")).body(null);
        }
        Alarm result = alarmRepository.save(alarm);
        return ResponseEntity.created(new URI("/api/alarms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /alarms : Updates an existing alarm.
     *
     * @param alarm the alarm to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated alarm,
     * or with status 400 (Bad Request) if the alarm is not valid,
     * or with status 500 (Internal Server Error) if the alarm couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/alarms")
    @Timed
    public ResponseEntity<Alarm> updateAlarm(@RequestBody Alarm alarm) throws URISyntaxException {
        log.debug("REST request to update Alarm : {}", alarm);
        if (alarm.getId() == null) {
            return createAlarm(alarm);
        }
        Alarm result = alarmRepository.save(alarm);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, alarm.getId().toString()))
            .body(result);
    }

    /**
     * GET  /alarms : get all the alarms.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of alarms in body
     */
    @GetMapping("/alarms")
    @Timed
    public List<Alarm> getAllAlarms() {
        log.debug("REST request to get all Alarms");
        return alarmRepository.findAll();
    }

    /**
     * GET  /alarms/:id : get the "id" alarm.
     *
     * @param id the id of the alarm to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the alarm, or with status 404 (Not Found)
     */
    @GetMapping("/alarms/{id}")
    @Timed
    public ResponseEntity<Alarm> getAlarm(@PathVariable Long id) {
        log.debug("REST request to get Alarm : {}", id);
        Alarm alarm = alarmRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(alarm));
    }

    /**
     * DELETE  /alarms/:id : delete the "id" alarm.
     *
     * @param id the id of the alarm to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/alarms/{id}")
    @Timed
    public ResponseEntity<Void> deleteAlarm(@PathVariable Long id) {
        log.debug("REST request to delete Alarm : {}", id);
        alarmRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
