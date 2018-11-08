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

    ServiceResponse<String> register(User user);

    ServiceResponse<String> checkValid(String str, String type);

    ServiceResponse selectQuestion(String username);

    ServiceResponse<String> checkAnswer(String username, String question, String answer);

    ServiceResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken);

    ServiceResponse<String> resetPassword(String passwordOld, String passwordNew, User user);

    ServiceResponse<User> updateInformation(User user);

    ServiceResponse<User> getInformation(Integer userId);
}
