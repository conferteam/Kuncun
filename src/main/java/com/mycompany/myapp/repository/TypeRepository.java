package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Type;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Type entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeRepository extends JpaRepository<Type,Long> {
	
	public Optional<Type> findOneByNameAndDelFlag(String name, Integer delFlag);
	
	public Optional<Type> findOneByIdAndDelFlag(Long id, Integer delFlag);
	
	public List<Type> findAllByDelFlag(Integer delFlag);
    
}
