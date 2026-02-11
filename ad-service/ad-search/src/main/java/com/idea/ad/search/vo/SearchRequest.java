package com.idea.ad.search.vo;

import com.idea.ad.search.vo.feature.DistrictFeature;
import com.idea.ad.search.vo.feature.FeatureRelation;
import com.idea.ad.search.vo.feature.ITFeature;
import com.idea.ad.search.vo.feature.KeywordFeature;
import com.idea.ad.search.vo.media.AdSlot;
import com.idea.ad.search.vo.media.App;
import com.idea.ad.search.vo.media.Device;
import com.idea.ad.search.vo.media.Geo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequest {
    private String mediaId;
    private RequestInfo requestInfo;
    // 特征信息
    private FeatureInfo featureInfo;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RequestInfo{
        private String requestId;
        private List<AdSlot> adSlots;
        private App app;
        private Geo geo;
        private Device device;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FeatureInfo{
        private KeywordFeature keywordFeature;
        private DistrictFeature districtFeature;
        private ITFeature itFeature;
        private FeatureRelation featureRelation = FeatureRelation.AND;
    }
}
