package com.idea.ad.service.impl;

import com.idea.ad.constant.Constants;
import com.idea.ad.dao.AdUserRepository;
import com.idea.ad.entity.AdUser;
import com.idea.ad.exception.ADException;
import com.idea.ad.service.IUserService;
import com.idea.ad.utils.CommonUtils;
import com.idea.ad.vo.CreateUserRequest;
import com.idea.ad.vo.CreateUserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UserServiceImpl implements IUserService {
    private final AdUserRepository userRepository;
    @Autowired
    public UserServiceImpl(AdUserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    @Transactional
    public CreateUserResponse createUser(CreateUserRequest request) throws ADException {
        if (!request.validate()){
            throw new ADException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
        AdUser oldUser = userRepository.findByUsername(request.getUsername());
        if (oldUser != null){
            throw new ADException(Constants.ErrorMsg.SAME_NAME_ERROR);
        }
        AdUser newUser = userRepository.save(new AdUser(request.getUsername(),
                CommonUtils.md5(request.getUsername())));
        return new CreateUserResponse(newUser.getId(), newUser.getUsername(),
                newUser.getToken(), newUser.getCreateTime(), newUser.getUpdateTime());
    }
}
