package com.tx.practisesmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tx.practisesmanagement.model.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {

}
