package com.idea.ad.search.impl;

import com.idea.ad.index.DataTable;
import com.idea.ad.index.adunit.AdUnitIndex;
import com.idea.ad.search.ISearch;
import com.idea.ad.search.vo.SearchRequest;
import com.idea.ad.search.vo.SearchResponse;
import com.idea.ad.search.vo.feature.DistrictFeature;
import com.idea.ad.search.vo.feature.FeatureRelation;
import com.idea.ad.search.vo.feature.ITFeature;
import com.idea.ad.search.vo.feature.KeywordFeature;
import com.idea.ad.search.vo.media.AdSlot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class SearchImpl implements ISearch {
    @Override
    public SearchResponse fetchAds(SearchRequest request) {
        // 获取请求的广告位信息
        List<AdSlot> adSlots = request.getRequestInfo().getAdSlots();
        KeywordFeature keywordFeature = request.getFeatureInfo().getKeywordFeature();
        DistrictFeature districtFeature = request.getFeatureInfo().getDistrictFeature();
        ITFeature itFeature = request.getFeatureInfo().getItFeature();
        FeatureRelation featureRelation = request.getFeatureInfo().getFeatureRelation();
        SearchResponse response = new SearchResponse();
        Map<String,List<SearchResponse.Creative>> adSlot2Ads = response.getAdSlot2Ads();
        for (AdSlot adSlot : adSlots) {
            Set<Long> targetUnitIdSet;
            // 根据流量类型获取初始 AdUnit
            Set<Long> adUnitIdSet = DataTable.of(AdUnitIndex.class).match(adSlot.getPositionType());
        }
        return null;
    }
}
