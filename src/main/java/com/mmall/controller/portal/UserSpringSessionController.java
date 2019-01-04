package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServiceResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisShardedPoolUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * @Author: dazou
 * @Description:
 * @Date: Create in 下午3:48 18/11/6
 */

//这两个注解必须配套使用
//这个注解必须要加,使用Controller标识它是一个Handler处理器
@Controller
//注解映射器,对URL和Handler的方法进行映射
//窄化请求映射:为了对URL进行分类管理,可以在类前面定义根路径,最终访问URL是根路径+子路径
@RequestMapping("/user/springsession/")
//映射成功后,SpringMVC框架生成一个Handler对象,对象中只包含一个映射成功的method
public class UserSpringSessionController {


    //注入IUserService,iUserService名字要与Service注解中设置的名字一致,这样就可以将service注入了,然后Controller层可以调用service层方法
    @Autowired
    private IUserService iUserService;


    /*
    * 用户登录
    * */
    @RequestMapping(value = "login.do", method = RequestMethod.GET)
    @ResponseBody
    public ServiceResponse<User> login(String username, String password, HttpSession session, HttpServletResponse httpServletResponse) {
        //测试全局异常
//        int i =  0;
//        int j = 555/i;

        ServiceResponse<User> response = iUserService.login(username, password);
        //登录成功后,在session中加入该用户
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }

    /*
   * 用户登出
   * */
    @RequestMapping(value = "logout.do", method = RequestMethod.GET)
    @ResponseBody
    public ServiceResponse<String> logout(HttpSession session,HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        //删除session
        session.removeAttribute(Const.CURRENT_USER);
        return ServiceResponse.createBySuccess();
    }


    //获取用户登录信息
    @RequestMapping(value = "get_user_info.do", method = RequestMethod.GET)
    @ResponseBody
    public ServiceResponse<User> getUserInfo(HttpSession session,HttpServletRequest httpServletRequest) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user != null) {
            return ServiceResponse.createBySuccess(user);
        }
        return ServiceResponse.createByErrorMessage("用户未登录，无法获取当前用户信息");
    }

}
