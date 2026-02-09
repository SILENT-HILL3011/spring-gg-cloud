package com.idea.ad.service;

import com.idea.ad.entity.ADPlan;
import com.idea.ad.exception.ADException;
import com.idea.ad.vo.AdPlanGetRequest;
import com.idea.ad.vo.AdPlanRequest;
import com.idea.ad.vo.AdPlanResponse;

import java.util.List;

public interface IAdPlanService {
    AdPlanResponse createAdPlan(AdPlanRequest request) throws ADException;
    List<ADPlan> getAdPlanByIds(AdPlanGetRequest request)throws ADException;
    AdPlanResponse updateAdPlan(AdPlanRequest request) throws ADException;
    void deleteAdPlan(AdPlanRequest request) throws ADException;
}
