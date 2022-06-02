package com.tx.practisesmanagement.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tx.practisesmanagement.model.Practise;


/**
 * Repositorio de prácticas
 * @author Salvador
 */
@Repository
public interface PractiseRepository extends JpaRepository<Practise, Integer>{

}
