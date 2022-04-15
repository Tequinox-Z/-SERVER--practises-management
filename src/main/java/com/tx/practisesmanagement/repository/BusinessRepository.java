package com.tx.practisesmanagement.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tx.practisesmanagement.model.Business;

/**
 * Repositorio de empresas
 * @author Salva
 *
 */
@Repository
public interface BusinessRepository extends JpaRepository<Business, String>{

}
