package com.idea.ad.service;

import com.idea.ad.Application;
import com.idea.ad.vo.AdPlanGetRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

@SpringBootTest(classes = {Application.class},webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RunWith(SpringRunner.class)
public class AdPlanServiceTest {
    @Autowired
    private IAdPlanService adPlanService;
    @Test
    public void testGetAdPlan() throws Exception {
        System.out.println(adPlanService.getAdPlanByIds(new AdPlanGetRequest(15L, Collections.singletonList(10L))));
    }

}
