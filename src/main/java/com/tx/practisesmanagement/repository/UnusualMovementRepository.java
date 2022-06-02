package com.tx.practisesmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tx.practisesmanagement.model.UnusualMovement;

/**
 * Repositorio de movimientos inusuales
 * @author Salvador
 *
 */
@Repository
public interface UnusualMovementRepository extends JpaRepository<UnusualMovement, Integer> {

}
