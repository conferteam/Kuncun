package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.UseLog;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the UseLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UseLogRepository extends JpaRepository<UseLog,Long> {
    
}
