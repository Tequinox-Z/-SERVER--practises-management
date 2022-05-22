package com.tx.practisesmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tx.practisesmanagement.model.RegTemp;

@Repository
public interface RegTempRepository extends JpaRepository<RegTemp, Integer> {

}
