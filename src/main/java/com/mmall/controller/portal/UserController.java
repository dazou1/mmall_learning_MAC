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
@RequestMapping("/user/")
//映射成功后,SpringMVC框架生成一个Handler对象,对象中只包含一个映射成功的method
public class UserController {


    //注入IUserService,iUserService名字要与Service注解中设置的名字一致,这样就可以将service注入了,然后Controller层可以调用service层方法
    @Autowired
    private IUserService iUserService;


    /*
    * 用户登录
    * */
    //@RequestMapping实现login方法和URL进行映射,一个方法对应一个URL---->子路径;  限制HTTP请求方法为POST
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    //返回的时候自动通过SpringMVC的Jackson插件,让返回值序列化为Jackson
    @ResponseBody
    //通过@RequestParam对简单类型参数进行绑定,如果不使用这个注解,要求request传入参数名称和Controller方法的形参一致,方可绑定成功
    public ServiceResponse<User> login(String username, String password, HttpSession session, HttpServletResponse httpServletResponse) {
        //service--->mybatis--->dao
        ServiceResponse<User> response = iUserService.login(username, password);
        //登录成功后,在session中加入该用户
        if (response.isSuccess()) {
//            session.setAttribute(Const.CURRENT_USER, response.getData());
            //在浏览器中写入cookie,键值为mmall_login_token,value值为sessionId
            CookieUtil.writeLoginToken(httpServletResponse, session.getId());
            //在Redis中写入用户信息,并将User对象转化为字符串形式写入Redis,获取用户时再反序列化成User对象:其中键值为sessionID,value值为User对象序列化结果
            RedisShardedPoolUtil.setEx(session.getId(), Const.RedisCacheExtime.REDIS_SESSION_EXTIME, JsonUtil.obj2String(response.getData()));
        }
        return response;
    }

    /*
   * 用户登出
   * */
    @RequestMapping(value = "logout.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<String> logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        //删除session
//        session.removeAttribute(Const.CURRENT_USER);

        //从httpServletRequest中获取Token Cookie值,根据名为:mmall_login_token的键值获取对应Cookie
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        //删除浏览器中的cookie
        CookieUtil.delLoginToken(httpServletRequest, httpServletResponse);
        //在Redis中删除退出用户的Cookie
        RedisShardedPoolUtil.del(loginToken);
        return ServiceResponse.createBySuccess();
    }

    /*
    * 用户注册
    * */
    @RequestMapping(value = "register.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<String> register(User user) {
        return iUserService.register(user);
    }

    //防止恶意用户调用接口，设计这个方法可以实时检测用户名和email是否已经存在,可以被注册阶段重用
    @RequestMapping(value = "check_valid.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<String> checkValid(String str, String type) {
        return iUserService.checkValid(str, type);
    }

    //获取用户登录信息
    @RequestMapping(value = "get_user_info.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<User> getUserInfo(HttpServletRequest httpServletRequest) {
//        User user = (User) session.getAttribute(Const.CURRENT_USER);
        //从httpServletRequest中获取Token Cookie值,根据名为:mmall_login_token的键值获取对应Cookie,获得SessionId
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        //查看浏览器中是否有cookie,没有的话就是用户还没有登录
        if (StringUtils.isEmpty(loginToken)) {
            return ServiceResponse.createByErrorMessage("用户未登录，无法获取当前用户信息");
        }
        //根据读取的SessionId从redis中获取cookie对应的值,即用户信息
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        //通过反序列化将Redis中获取的用户信息字符串转化为User对象
        User user = JsonUtil.string2Obj(userJsonStr, User.class);

        if (user != null) {
            return ServiceResponse.createBySuccess(user);
        }
        return ServiceResponse.createByErrorMessage("用户未登录，无法获取当前用户信息");
    }

    //忘记密码模块，返回密码提示问题
    @RequestMapping(value = "forget_get_question.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<String> forgetGetQuestion(String username) {
        return iUserService.selectQuestion(username);
    }

    //校验问题答案是否正确，返回值是一个String，要将token放入String中
    @RequestMapping(value = "forget_check_answer.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<String> forgetCheckAnswer(String username, String question, String answer) {
        return iUserService.checkAnswer(username, question, answer);
    }

    //忘记密码中的重置密码功能
    @RequestMapping(value = "forget_reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {
        return iUserService.forgetResetPassword(username, passwordNew, forgetToken);
    }

    //登录状态的重置密码功能
    @RequestMapping(value = "reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<String> resetPassword(HttpServletRequest httpServletRequest, String passwordOld, String passwordNew) {
//        User user = (User) session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)) {
            return ServiceResponse.createByErrorMessage("用户未登录，无法获取当前用户信息");
        }
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonStr, User.class);

        if (user == null) {
            return ServiceResponse.createByErrorMessage("用户未登录");
        }
        return iUserService.resetPassword(passwordOld, passwordNew,user);
    }

    //更新用户个人信息功能
    @RequestMapping(value = "update_information.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<User> updateInformation(HttpServletRequest httpServletRequest, User user) {
//        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)) {
            return ServiceResponse.createByErrorMessage("用户未登录，无法获取当前用户信息");
        }
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        User currentUser = JsonUtil.string2Obj(userJsonStr, User.class);

        if (currentUser == null) {
            return ServiceResponse.createByErrorMessage("用户未登录");
        }
        //由于user中没有userid，因此要将userid赋值到当前user
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        ServiceResponse<User> response = iUserService.updateInformation(user);
        if (response.isSuccess()) {
            response.getData().setUsername(currentUser.getUsername());
            RedisShardedPoolUtil.setEx(loginToken, Const.RedisCacheExtime.REDIS_SESSION_EXTIME, JsonUtil.obj2String(response.getData()));
        }
        return response;
    }

    //获取用户信息
    @RequestMapping(value = "get_information.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<User> getInformation(HttpServletRequest httpServletRequest) {
//        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)) {
            return ServiceResponse.createByErrorMessage("用户未登录，无法获取当前用户信息");
        }
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        User currentUser = JsonUtil.string2Obj(userJsonStr, User.class);

        if (currentUser == null) {
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录，需要强制登录status=10");
        }
        return iUserService.getInformation(currentUser.getId());
    }
}
