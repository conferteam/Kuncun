package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Uselog;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Uselog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UselogRepository extends JpaRepository<Uselog,Long> {
    
}
