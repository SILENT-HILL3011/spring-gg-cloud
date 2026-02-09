package com.idea.ad.dao;

import com.idea.ad.entity.ADPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdPlanRepository extends JpaRepository<ADPlan, Long> {
    ADPlan findByIdAndUserId(Long id, Long userId);

    List<ADPlan> findAllByIdInAndUserId(List<Long> ids, Long userId);

    ADPlan findByUserIdAndPlanName(Long userId, String planName);

    List<ADPlan> findAllByPlanStatus(Integer status);
}
