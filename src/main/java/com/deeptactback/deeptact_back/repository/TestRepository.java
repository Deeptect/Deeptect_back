package com.deeptactback.deeptact_back.repository;

import com.deeptactback.deeptact_back.domain.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends JpaRepository<Test, Integer> {

}