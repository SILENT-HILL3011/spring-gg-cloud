package com.idea.ad.search;

import com.alibaba.fastjson.JSON;
import com.idea.ad.SearchApplication;
import com.idea.ad.search.vo.SearchRequest;
import com.idea.ad.search.vo.feature.DistrictFeature;
import com.idea.ad.search.vo.feature.FeatureRelation;
import com.idea.ad.search.vo.feature.ITFeature;
import com.idea.ad.search.vo.feature.KeywordFeature;
import com.idea.ad.search.vo.media.AdSlot;
import com.idea.ad.search.vo.media.App;
import com.idea.ad.search.vo.media.Device;
import com.idea.ad.search.vo.media.Geo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SearchApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)

public class SearchTest {
    @Autowired
    private ISearch search;
    @Test
    public void testFetchAds(){
        SearchRequest request = new SearchRequest();
        request.setMediaId("idea-ad");
        request.setRequestInfo(new SearchRequest.RequestInfo(
                "aaa", Collections.singletonList(new AdSlot("ad-x",1,1080,720, Arrays.asList(1,2,3,4),1000)),
                buildExampleApp(),
                buildExampleGeo(),
                buildExampleDevice()
        ));
        request.setFeatureInfo(buildExampleFeatureInfo(
                Arrays.asList("宝马","奔驰"),
                Collections.singletonList(new DistrictFeature.ProvinceAndCity("安徽省","合肥市")),
                Arrays.asList("台球","游泳"),
                FeatureRelation.OR
        ));
        System.out.println(JSON.toJSONString(request));
        System.out.println(JSON.toJSON(search.fetchAds(request)));

        request.setRequestInfo(new SearchRequest.RequestInfo(
                "aaa", Collections.singletonList(new AdSlot("ad-y",1,1080,720, Arrays.asList(1,2,3,4),1000)),
                buildExampleApp(),
                buildExampleGeo(),
                buildExampleDevice()
        ));
        request.setFeatureInfo(buildExampleFeatureInfo(
                Arrays.asList("宝马","大众","标志"),
                Collections.singletonList(new DistrictFeature.ProvinceAndCity("安徽省","合肥市")),
                Arrays.asList("台球","游泳"),
                FeatureRelation.AND
        ));
        System.out.println(JSON.toJSONString(request));
        System.out.println(JSON.toJSON(search.fetchAds(request)));
    }
    private App buildExampleApp(){
        return new App("idea-ad", "idea-ad", "com.idea.ad", "MainActivity");
    }
    private Geo buildExampleGeo(){
        return new Geo(1.1f, 1.2f, "哈尔滨市", "黑龙江");
    }
    private Device buildExampleDevice(){
        return new Device(
                "iphone",
                "0xxxxx",
                "192.168.1.1",
                "x",
                "1808 780",
                "1920*1080",
                "123"
        );
    }
    private SearchRequest.FeatureInfo buildExampleFeatureInfo(List<String> keyword, List<DistrictFeature.ProvinceAndCity> provinceAndCities, List<String> its, FeatureRelation relation){
        return new SearchRequest.FeatureInfo(
                new KeywordFeature(keyword),
                new DistrictFeature(provinceAndCities),
                new ITFeature(its),
                relation
        );
    }
}
