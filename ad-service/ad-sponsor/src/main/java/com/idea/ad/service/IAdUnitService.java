package com.idea.ad.service;

import com.idea.ad.exception.ADException;
import com.idea.ad.vo.*;

public interface IAdUnitService {
    AdUnitResponse createUnit(AdUnitRequest request)throws ADException;

    AdUnitKeywordResponse createUnitKeyword(AdUnitKeywordRequest request)throws ADException;

    AdUnitItResponse createUnitIt(AdUnitItRequest request)throws ADException;

    AdUnitDistrictResponse createUnitDistrict(AdUnitDistrictRequest request)throws ADException;
}
