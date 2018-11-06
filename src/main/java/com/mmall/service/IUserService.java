package com.mmall.service;

import com.mmall.common.ServiceResponse;
import com.mmall.pojo.User;

/**
 * @Author: dazou
 * @Description:
 * @Date: Create in 下午4:01 18/11/6
 */
public interface IUserService {
    ServiceResponse<User> login(String username, String password);
}
