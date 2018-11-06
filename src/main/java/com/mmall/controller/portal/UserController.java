package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ServiceResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @Author: dazou
 * @Description:
 * @Date: Create in 下午3:48 18/11/6
 */

@Controller
@RequestMapping("/user/")
public class UserController {


    //注入IUserService,iUserService名字要与Service注解中设置的名字一致,这样就可以将service注入了
    @Autowired
    private IUserService iUserService;


    /*
    * 用户登录
    * */
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    //返回的时候自动通过Spring MVC的Jackson插件,让返回值序列化为Jackson
    @ResponseBody
    public ServiceResponse<User> login(String username, String password, HttpSession session) {
        //service--->mybatis--->dao

        ServiceResponse<User> response = iUserService.login(username, password);
        //登录成功后,在session中加入该用户
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }
}
