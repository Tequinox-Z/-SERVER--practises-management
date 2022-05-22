package com.tx.practisesmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tx.practisesmanagement.model.ContactWorker;

@Repository
public interface ContactWorkerRepository extends JpaRepository<ContactWorker, Integer>{

}
