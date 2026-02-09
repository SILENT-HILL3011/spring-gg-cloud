package com.idea.ad.dao;

import com.idea.ad.entity.AdUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdUserRepository extends JpaRepository<AdUser,Long> {
    AdUser findByUsername(String username);
}
