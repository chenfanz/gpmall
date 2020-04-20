package com.mall.user;

import com.mall.user.dto.UserRegisterRequest;
import com.mall.user.dto.UserRegisterResponse;

public interface IUserRegisterService {

    /**
     * 实现用户注册功能
     * @param request
     * @return
     */
    UserRegisterResponse register(UserRegisterRequest request);
}
