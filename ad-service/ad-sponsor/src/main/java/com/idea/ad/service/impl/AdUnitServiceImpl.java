package com.idea.ad.service.impl;

import com.idea.ad.constant.Constants;
import com.idea.ad.dao.AdPlanRepository;
import com.idea.ad.dao.AdUnitRepository;
import com.idea.ad.dao.unit_condition.AdUnitDistrictRepository;
import com.idea.ad.dao.unit_condition.AdUnitItRepository;
import com.idea.ad.dao.unit_condition.AdUnitKeywordRepository;
import com.idea.ad.entity.ADPlan;
import com.idea.ad.entity.AdUnit;
import com.idea.ad.entity.unit_condition.ADUnitKeyWord;
import com.idea.ad.entity.unit_condition.AdUnitDistrict;
import com.idea.ad.entity.unit_condition.AdUnitIt;
import com.idea.ad.exception.ADException;
import com.idea.ad.service.IAdUnitService;
import com.idea.ad.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdUnitServiceImpl implements IAdUnitService {
    private final AdPlanRepository adPlanRepository;
    private final AdUnitRepository adUnitRepository;
    private final AdUnitKeywordRepository adUnitKeywordRepository;
    private final AdUnitItRepository adUnitItRepository;
    private final AdUnitDistrictRepository adUnitDistrictRepository;
    @Autowired
    public AdUnitServiceImpl(AdPlanRepository adPlanRepository, AdUnitRepository adUnitRepository, AdUnitKeywordRepository adUnitKeywordRepository, AdUnitItRepository adUnitItRepository, AdUnitDistrictRepository adUnitDistrictRepository) {
        this.adPlanRepository = adPlanRepository;
        this.adUnitRepository = adUnitRepository;
        this.adUnitKeywordRepository = adUnitKeywordRepository;
        this.adUnitItRepository = adUnitItRepository;
        this.adUnitDistrictRepository = adUnitDistrictRepository;
    }

    @Override
    public AdUnitResponse createUnit(AdUnitRequest request) throws ADException {
        if (!request.createValidate()){
            throw new ADException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
        Optional<ADPlan> adPlan = adPlanRepository.findById(request.getPlanId());
        if (!adPlan.isPresent()){
            throw new ADException(Constants.ErrorMsg.CAN_NOT_FIND_RECORD);
        }
        AdUnit oldAdUnit = adUnitRepository.findByPlanIdAndUnitName(request.getPlanId(), request.getUnitName());
        if (oldAdUnit != null){
            throw new ADException(Constants.ErrorMsg.SAME_NAME_UNIT_ERROR);
        }
        AdUnit newAdUnit = adUnitRepository.save(new AdUnit(request.getPlanId(), request.getUnitName(),
                request.getPositionType(), request.getBudget()));
        return new AdUnitResponse(newAdUnit.getId(), newAdUnit.getUnitName());
    }

    @Override
    public AdUnitKeywordResponse createUnitKeyword(AdUnitKeywordRequest request) throws ADException {
        List<Long> unitIds = request.getUnitKeywords().stream().map(AdUnitKeywordRequest.UnitKeyword::getUnitId)
                .collect(Collectors.toList());
        if (!isRelatedUnitExist(unitIds)){
            throw new ADException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
        List<Long> ids = Collections.emptyList();
        List<ADUnitKeyWord> adUnitKeyWords = new ArrayList<>();
        if (!CollectionUtils.isEmpty(request.getUnitKeywords())){
            request.getUnitKeywords().forEach(i->adUnitKeyWords.add(
                    new ADUnitKeyWord(i.getUnitId(),i.getKeyword())
            ));
            ids = adUnitKeywordRepository.saveAll(adUnitKeyWords).stream()
                    .map(ADUnitKeyWord::getId).collect(Collectors.toList());
        }
        return new AdUnitKeywordResponse(ids);
    }

    @Override
    public AdUnitItResponse createUnitIt(AdUnitItRequest request) throws ADException {
        List<Long> unitIds = request.getUnitIts().stream().map(AdUnitItRequest.UnitIt::getUnitId)
                .collect(Collectors.toList());
        if (!isRelatedUnitExist(unitIds)){
            throw new ADException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
        List< AdUnitIt> adUnitIts = new ArrayList<>();
        request.getUnitIts().forEach(i->adUnitIts.add(
                new AdUnitIt(i.getUnitId(),i.getItTag())
        ));
        List<Long> ids = adUnitItRepository.saveAll(adUnitIts).stream()
                .map(AdUnitIt::getId).collect(Collectors.toList());
        return new AdUnitItResponse(ids);
    }

    @Override
    public AdUnitDistrictResponse createUnitDistrict(AdUnitDistrictRequest request) throws ADException {
        List<Long> unitIds = request.getUnitDistricts().stream().map(
                AdUnitDistrictRequest.UnitDistrict::getUnitId
        ).collect(Collectors.toList());
        if (!isRelatedUnitExist(unitIds)){
            throw new ADException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
        List<AdUnitDistrict> adUnitDistricts = new ArrayList<>();
         request.getUnitDistricts().forEach(i->adUnitDistricts.add(
                 new AdUnitDistrict(i.getUnitId(),i.getProvince(),i.getCity())
         ));
         List<Long> ids = adUnitDistrictRepository.saveAll(adUnitDistricts)
                 .stream().map(AdUnitDistrict::getId).collect(Collectors.toList());
        return new AdUnitDistrictResponse(ids);
    }
    private boolean isRelatedUnitExist(List<Long> unitIds){
        if (CollectionUtils.isEmpty(unitIds)){
            return false;
        }
        return adUnitRepository.findAllById(unitIds).size() ==
                new HashSet<>(unitIds).size();
    }
}
