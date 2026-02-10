package com.idea.ad.controller;

import com.alibaba.fastjson.JSON;
import com.idea.ad.annotation.IgnoreResponseAdvice;
import com.idea.ad.client.SponsorClient;
import com.idea.ad.client.vo.AdPlan;
import com.idea.ad.client.vo.AdPlanGetRequest;
import com.idea.ad.vo.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@RestController
public class SearchController {
    private final RestTemplate restTemplate;
    private final SponsorClient sponsorClient;
    @Autowired
    public SearchController(RestTemplate restTemplate, SponsorClient sponsorClient) {
        this.restTemplate = restTemplate;
        this.sponsorClient = sponsorClient;
    }
    @IgnoreResponseAdvice
    @PostMapping("/getAdPlans")
    public R<List<AdPlan>> getAdPlans(@RequestBody AdPlanGetRequest request){
        log.info("ad-search: getAdPlans->{}", JSON.toJSONString(request));
        return sponsorClient.getAdPlans(request);
    }
    @SuppressWarnings("all")
    @IgnoreResponseAdvice
    @PostMapping("/getAdPlanByRibbon")
    public R<List<AdPlan>> getAdPlansByRibbon(@RequestBody AdPlanGetRequest request){
        log.info("ad-search: getAdPlanByRibbon->{}", JSON.toJSONString(request));
        return restTemplate.postForEntity("http://eureka-client-ad-sponsor/ad-sponsor/get/adPlan",
                request,R.class).getBody();
    }
}
