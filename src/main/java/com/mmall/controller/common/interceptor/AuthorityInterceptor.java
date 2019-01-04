package com.mmall.controller.common.interceptor;

import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ServiceResponse;
import com.mmall.pojo.User;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

/**
 * @Author: dazou
 * @Description:
 * @Date: Create in 下午5:15 19/1/4
 */
@Slf4j
public class AuthorityInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("preHandle");
        //请求中Controller中的方法名
        HandlerMethod handlerMethod = (HandlerMethod)handler;

        //解析HandlerMethod

        String methodName = handlerMethod.getMethod().getName();
        String className = handlerMethod.getBean().getClass().getSimpleName();

        //解析参数,具体的参数key以及value是什么,打印日志出来
        StringBuffer requestParamBuffer = new StringBuffer();
        Map paramMap = request.getParameterMap();
        Iterator it = paramMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String mapKey = (String)entry.getKey();
            String mapValue = StringUtils.EMPTY;
            //request这个参数的map。里面的value返回的是一个String[]
            Object obj = entry.getValue();
            if (obj instanceof String[]) {
                String[] strs = (String[])obj;
                mapValue = Arrays.toString(strs);
            }
            requestParamBuffer.append(mapKey).append("=").append(mapValue);
        }

        //防止拦截登录循环
        if (StringUtils.equals(className, "UserManagerController") && StringUtils.equals(methodName, "login")) {
            log.info("权限拦截器拦截到请求,className:{},methodName:{}",className,methodName);
            //如果是拦截到登录请求,不打印参数,因为参数里面有密码,全部会打印到日志中,防止日志泄露
            return true;
        }

        //如果是拦截到不是登录请求,打印参数
        log.info("权限拦截器拦截到请求,className:{},methodName:{},param:{}",className,methodName,requestParamBuffer.toString());

        User user = null;
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isNotEmpty(loginToken)) {
            String userJsonStr = RedisShardedPoolUtil.get(loginToken);
            user = JsonUtil.string2Obj(userJsonStr, User.class);
        }

        if (user == null || (user.getRole().intValue() != Const.Role.ROLE_ADMIN)) {
            //返回false,即不会调用Controller里的方法
            //response托管到拦截器中,必须对对应的属性设置
            response.reset();//这里要添加reset方法,否则报异常 getWriter() has already been called for this response.
            response.setCharacterEncoding("UTF-8");//这里要添加编码,否则会乱码
            response.setContentType("application/json;charset=UTF-8");//这里要设置返回值的类型,因为都是json接口

            PrintWriter out = response.getWriter();

            //上传由于富文本的控件要求,要特殊处理返回值,这里区分是否登录以及是否有权限
            if (user == null) {
                //匹配富文本请求的返回值是Map,做出拦截调整
                if (StringUtils.equals(className, "productManageController") && StringUtils.equals(methodName, "richtextImgUpload")) {
                    Map resultMap = Maps.newHashMap();
                    resultMap.put("success", false);
                    resultMap.put("msg", "未登录,请登录管理员");
                    out.print(JsonUtil.obj2String(resultMap));
                } else {
                    out.print(JsonUtil.obj2String(ServiceResponse.createByErrorMessage("拦截器拦截,用户未登录")));
                }
            } else {
                //匹配富文本请求的返回值是Map,做出拦截调整
                if (StringUtils.equals(className, "productManageController") && StringUtils.equals(methodName, "richtextImgUpload")) {
                    Map resultMap = Maps.newHashMap();
                    resultMap.put("success", false);
                    resultMap.put("msg", "无权限操作");
                    out.print(JsonUtil.obj2String(resultMap));
                } else {
                    out.print(JsonUtil.obj2String(ServiceResponse.createByErrorMessage("拦截器拦截,用户无权限操作")));
                }
            }
            out.flush();
            out.close();//这里要关闭流

            return false;
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("afterCompletion");
    }
}
