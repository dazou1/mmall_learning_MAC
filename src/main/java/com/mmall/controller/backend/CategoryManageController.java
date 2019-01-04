package com.mmall.controller.backend;

import com.mmall.common.ResponseCode;
import com.mmall.common.ServiceResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisShardedPoolUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: dazou
 * @Description: 后台_品类接口
 * @Date: Create in 上午10:44 18/11/13
 */

@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private ICategoryService iCategoryService;


    //添加品类
    @RequestMapping("add_category.do")
    @ResponseBody
    public ServiceResponse addCategory(HttpServletRequest httpServletRequest, String categoryName, @RequestParam(value = "parentId", defaultValue = "0") int parentId) {
////        User user = (User) session.getAttribute(Const.CURRENT_USER);
//        //从httpServletRequest中获取Token Cookie值,根据名为:mmall_login_token的键值获取对应Cookie,获得SessionId
//        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
//        //查看浏览器中是否有cookie,没有的话就是用户还没有登录
//        if (StringUtils.isEmpty(loginToken)) {
//            return ServiceResponse.createByErrorMessage("用户未登录，无法获取当前用户信息");
//        }
//        //根据读取的SessionId从redis中获取cookie对应的值,即用户信息
//        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
//        //通过反序列化将Redis中获取的用户信息字符串转化为User对象
//        User user = JsonUtil.string2Obj(userJsonStr, User.class);
//
//        if (user == null) {
//            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
//        }
//        //校验一下是否是管理员
//        if (iUserService.checkAdminRole(user).isSuccess()) {
//            //是管理员
//            //增加我们处理分类的逻辑
//            return iCategoryService.addCategory(categoryName, parentId);
//        } else {
//            return ServiceResponse.createByErrorMessage("无权限操作,需要管理员权限");
//        }

        //全部通过拦截器验证是否登录以及权限
        return iCategoryService.addCategory(categoryName, parentId);
    }

    //更新品类名
    @RequestMapping("set_category_name.do")
    @ResponseBody
    public ServiceResponse setCategoryName(HttpServletRequest httpServletRequest, Integer categoryId, String categoryName) {
////        User user = (User) session.getAttribute(Const.CURRENT_USER);
//        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
//        if (StringUtils.isEmpty(loginToken)) {
//            return ServiceResponse.createByErrorMessage("用户未登录，无法获取当前用户信息");
//        }
//        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
//        User user = JsonUtil.string2Obj(userJsonStr, User.class);
//
//        if (user == null) {
//            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
//        }
//        //校验一下是否是管理员
//        if (iUserService.checkAdminRole(user).isSuccess()) {
//            //是管理员
//            //更新categoryName
//            return iCategoryService.updateCategoryName(categoryId, categoryName);
//        } else {
//            return ServiceResponse.createByErrorMessage("无权限操作,需要管理员权限");
//        }

        //全部通过拦截器验证是否登录以及权限
        return iCategoryService.updateCategoryName(categoryId, categoryName);
    }

    //根据所传入的categoryId,获取当前categoryId下边子节点的category信息,并且是平级的,并且不递归
    @RequestMapping("get_category.do")
    @ResponseBody
    public ServiceResponse getChildrenParallelCategory(HttpServletRequest httpServletRequest,@RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
////        User user = (User) session.getAttribute(Const.CURRENT_USER);
//        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
//        if (StringUtils.isEmpty(loginToken)) {
//            return ServiceResponse.createByErrorMessage("用户未登录，无法获取当前用户信息");
//        }
//        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
//        User user = JsonUtil.string2Obj(userJsonStr, User.class);
//
//        if (user == null) {
//            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
//        }
//        //校验一下是否是管理员
//        if (iUserService.checkAdminRole(user).isSuccess()) {
//            //是管理员
//            //查询子节点的category信息,并且不递归,保持平级
//            return iCategoryService.getChildrenParallelCategory(categoryId);
//        } else {
//            return ServiceResponse.createByErrorMessage("无权限操作,需要管理员权限");
//        }

        //全部通过拦截器验证是否登录以及权限
        return iCategoryService.getChildrenParallelCategory(categoryId);
    }

    //根据所传入的categoryId,获取当前categoryId下边子节点的category信息,并且递归其子节点下的Id
    @RequestMapping("get_deep_category.do")
    @ResponseBody
    public ServiceResponse getChildrenAndDeepChildrenCategory(HttpServletRequest httpServletRequest,@RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
////        User user = (User) session.getAttribute(Const.CURRENT_USER);
//        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
//        if (StringUtils.isEmpty(loginToken)) {
//            return ServiceResponse.createByErrorMessage("用户未登录，无法获取当前用户信息");
//        }
//        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
//        User user = JsonUtil.string2Obj(userJsonStr, User.class);
//
//        if (user == null) {
//            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
//        }
//        //校验一下是否是管理员
//        if (iUserService.checkAdminRole(user).isSuccess()) {
//            //是管理员
//            //查询当前节点的Id和递归子节点的Id
//            //0--->10000---->100000
//            return iCategoryService.selectCategoryAndChildrenById(categoryId);
//        } else {
//            return ServiceResponse.createByErrorMessage("无权限操作,需要管理员权限");
//        }

        //全部通过拦截器验证是否登录以及权限
        return iCategoryService.selectCategoryAndChildrenById(categoryId);
    }
}
