package com.idea.ad.client;

import com.idea.ad.client.vo.AdPlan;
import com.idea.ad.client.vo.AdPlanGetRequest;
import com.idea.ad.vo.R;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SponsorClientHystrix implements SponsorClient{
    @Override
    public R<List<AdPlan>> getAdPlans(AdPlanGetRequest request) {
        return new R<>(-1, "eureka-client-ad-sponsor error");
    }
}
