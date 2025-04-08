package com.deeptactback.deeptact_back.repository;

import com.deeptactback.deeptact_back.domain.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
}
