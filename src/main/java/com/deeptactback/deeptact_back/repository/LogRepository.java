package com.deeptactback.deeptact_back.repository;

import com.deeptactback.deeptact_back.domain.DeepfakeAnalysisLog;
import com.deeptactback.deeptact_back.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<DeepfakeAnalysisLog, Integer> {
    List<DeepfakeAnalysisLog> findAllByUser(User user);
}
