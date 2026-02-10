package com.idea.ad.controller;

import com.alibaba.fastjson.JSON;
import com.idea.ad.exception.ADException;
import com.idea.ad.service.ICreativeService;
import com.idea.ad.vo.CreativeRequest;
import com.idea.ad.vo.CreativeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class CreativeOPController {
    private final ICreativeService creativeOPService;
    @Autowired
    public CreativeOPController(ICreativeService creativeOPService) {
        this.creativeOPService = creativeOPService;
    }
    @PostMapping("/create/creative")
    public CreativeResponse createCreative(@RequestBody CreativeRequest request) throws ADException {
        log.info("ad-sponsor: createCreative->{}", JSON.toJSONString(request));
        return creativeOPService.createCreative(request);
    }
}
