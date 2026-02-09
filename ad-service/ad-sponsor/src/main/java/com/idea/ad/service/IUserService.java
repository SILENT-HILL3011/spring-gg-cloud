package com.idea.ad.service;

import com.idea.ad.exception.ADException;
import com.idea.ad.vo.CreateUserRequest;
import com.idea.ad.vo.CreateUserResponse;

public interface IUserService {
    CreateUserResponse createUser(CreateUserRequest request) throws ADException;
}
