package com.deeptactback.deeptact_back.repository;

import com.deeptactback.deeptact_back.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    // 이메일로 사용자 찾기
    User findByEmail(String email);

    // 이메일 중복 체크
    boolean existsByEmail(String email);

    User findByUuid(String uuid);
}
