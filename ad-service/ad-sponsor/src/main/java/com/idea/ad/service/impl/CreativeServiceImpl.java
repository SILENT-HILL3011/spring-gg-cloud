package com.idea.ad.service.impl;

import com.idea.ad.dao.CreativeRepository;
import com.idea.ad.entity.Creative;
import com.idea.ad.exception.ADException;
import com.idea.ad.service.ICreativeService;
import com.idea.ad.vo.CreativeRequest;
import com.idea.ad.vo.CreativeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreativeServiceImpl implements ICreativeService {

    private final CreativeRepository creativeRepository;
    @Autowired
    public CreativeServiceImpl(CreativeRepository creativeRepository) {
        this.creativeRepository = creativeRepository;
    }

    @Override
    public CreativeResponse createCreative(CreativeRequest request) throws ADException {
        Creative creative = creativeRepository.save(request.convertToEntity());
        return new CreativeResponse(creative.getId(), creative.getName());
    }
}
