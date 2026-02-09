package com.idea.ad.service.impl;

import com.idea.ad.constant.CommonStatus;
import com.idea.ad.constant.Constants;
import com.idea.ad.dao.AdPlanRepository;
import com.idea.ad.dao.AdUserRepository;
import com.idea.ad.entity.ADPlan;
import com.idea.ad.entity.AdUser;
import com.idea.ad.exception.ADException;
import com.idea.ad.service.IAdPlanService;
import com.idea.ad.utils.CommonUtils;
import com.idea.ad.vo.AdPlanGetRequest;
import com.idea.ad.vo.AdPlanRequest;
import com.idea.ad.vo.AdPlanResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AdPlanServiceImpl implements IAdPlanService {
    private final AdUserRepository userRepository;
    private final AdPlanRepository planRepository;
    @Autowired
    public AdPlanServiceImpl(AdPlanRepository planRepository, AdUserRepository userRepository) {
        this.planRepository = planRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public AdPlanResponse createAdPlan(AdPlanRequest request) throws ADException {
        if (!request.validate()){
            throw new ADException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
        Optional< AdUser> adUser = userRepository.findById(request.getUserId());
        if (!adUser.isPresent()){
            throw new ADException(Constants.ErrorMsg.CAN_NOT_FIND_RECORD);
        }
        ADPlan oldPlan = planRepository.findByUserIdAndPlanName(request.getUserId(), request.getPlanName());
        if (oldPlan != null){
            throw new ADException(Constants.ErrorMsg.SAME_NAME_ERROR);
        }
        ADPlan newAdPlan = planRepository.save(new ADPlan(request.getUserId(), request.getPlanName(), CommonUtils.parseStringDate(request.getStartDate()),
                CommonUtils.parseStringDate(request.getEndDate())));
        return new AdPlanResponse(newAdPlan.getId(), newAdPlan.getPlanName());
    }

    @Override
    public List<ADPlan> getAdPlanByIds(AdPlanGetRequest request) throws ADException {
        if (!request.validate()){
            throw new ADException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
        return planRepository.findAllByIdInAndUserId(request.getIds(),request.getUserId());
    }

    @Override
    @Transactional
    public AdPlanResponse updateAdPlan(AdPlanRequest request) throws ADException {
        if (!request.updateValidate()){
            throw new ADException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
        ADPlan plan = planRepository.findByIdAndUserId(request.getId(), request.getUserId());
        if (plan == null){
            throw new ADException(Constants.ErrorMsg.CAN_NOT_FIND_RECORD);
        }
        if (request.getPlanName() != null){
            plan.setPlanName(request.getPlanName());
        }
        if (request.getStartDate() != null){
            plan.setStartDate(CommonUtils.parseStringDate(request.getStartDate()));
        }
        if (request.getEndDate() != null){
            plan.setEndDate(CommonUtils.parseStringDate(request.getEndDate()));
        }
        plan.setUpdateTime(new Date());
        plan = planRepository.save(plan);
        return new AdPlanResponse(plan.getId(), plan.getPlanName());
    }

    @Override
    @Transactional
    public void deleteAdPlan(AdPlanRequest request) throws ADException {
        if (!request.deleteValidate()){
            throw new ADException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
        ADPlan plan = planRepository.findByIdAndUserId(request.getId(), request.getUserId());
        if (plan == null){
            throw new ADException(Constants.ErrorMsg.CAN_NOT_FIND_RECORD);
        }
        plan.setPlanStatus(CommonStatus.INVALID.getStatus());
        plan.setUpdateTime(new Date());
        planRepository.save(plan);
    }
}
