package com.idea.ad.service;

import com.idea.ad.exception.ADException;
import com.idea.ad.vo.CreativeRequest;
import com.idea.ad.vo.CreativeResponse;

public interface ICreativeService {
    CreativeResponse createCreative(CreativeRequest request) throws ADException;
}
