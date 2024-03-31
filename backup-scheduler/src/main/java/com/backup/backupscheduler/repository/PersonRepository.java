package com.backup.backupscheduler.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backup.backupscheduler.models.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
