package com.mmall.service.impl;

import com.mmall.common.ServiceResponse;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: dazou
 * @Description:
 * @Date: Create in 下午4:03 18/11/6
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService{

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServiceResponse<User> login(String username, String password) {

        int resultCount = userMapper.checkUsername(username);
        if (resultCount == 0) {
            return ServiceResponse.createByErrorMessage("用户不存在");
        }

        //todo 密码登录MD5


        User user = userMapper.selectLogin(username, password);
        if (user == null) {
            return ServiceResponse.createByErrorMessage("密码错误");
        }

        //设置密码为空,不能返回密码到前端
        user.setPassword(StringUtils.EMPTY);
        return ServiceResponse.createBySuccess("登录成功", user);
    }
}
