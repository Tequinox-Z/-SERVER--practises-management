package com.tx.practisesmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tx.practisesmanagement.model.SensorData;

@Repository
public interface SensorDataRepository extends JpaRepository<SensorData, Long> {

}
