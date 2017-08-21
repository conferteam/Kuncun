package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Type;

import com.mycompany.myapp.repository.TypeRepository;
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
 * REST controller for managing Type.
 */
@RestController
@RequestMapping("/api")
public class TypeResource {

    private final Logger log = LoggerFactory.getLogger(TypeResource.class);

    private static final String ENTITY_NAME = "type";

    private final TypeRepository typeRepository;

    public TypeResource(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    /**
     * POST  /types : Create a new type.
     *
     * @param type the type to create
     * @return the ResponseEntity with status 201 (Created) and with body the new type, or with status 400 (Bad Request) if the type has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/types")
    @Timed
    public ResponseEntity<Type> createType(@RequestBody Type type) throws URISyntaxException {
    	type.setDel_flag(0);
        log.debug("REST request to save Type : {}", type);
        if (type.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new type cannot already have an ID")).body(null);
        }
        Type result = typeRepository.save(type);
        return ResponseEntity.created(new URI("/api/types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /types : Updates an existing type.
     *
     * @param type the type to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated type,
     * or with status 400 (Bad Request) if the type is not valid,
     * or with status 500 (Internal Server Error) if the type couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/types")
    @Timed
    public ResponseEntity<Type> updateType(@RequestBody Type type) throws URISyntaxException {
        log.debug("REST request to update Type : {}", type);
        if (type.getId() == null) {
            return createType(type);
        }
        Type result = typeRepository.save(type);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, type.getId().toString()))
            .body(result);
    }

    /**
     * GET  /types : get all the types.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of types in body
     */
    @GetMapping("/types")
    @Timed
    public List<Type> getAllTypes() {
        log.debug("REST request to get all Types");
        return typeRepository.findAll();
    }

    /**
     * GET  /types/:id : get the "id" type.
     *
     * @param id the id of the type to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the type, or with status 404 (Not Found)
     */
    @GetMapping("/types/{id}")
    @Timed
    public ResponseEntity<Type> getType(@PathVariable Long id) {
        log.debug("REST request to get Type : {}", id);
        Type type = typeRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(type));
    }

    /**
     * DELETE  /types/:id : delete the "id" type.
     *
     * @param id the id of the type to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/types/{id}")
    @Timed
    public ResponseEntity<Void> deleteType(@PathVariable Long id) {
        log.debug("REST request to delete Type : {}", id);
        typeRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
