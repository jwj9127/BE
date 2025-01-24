package com.example.cloud.oauth2.repository;

import com.example.cloud.oauth2.entity.SocialUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<SocialUserEntity, Long> {
    SocialUserEntity findByUsername(String username);
}
