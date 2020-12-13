package com.cl.ysyd.aop;

import com.cl.ysyd.common.constants.AopConstant;
import com.cl.ysyd.common.constants.TokenInfo;
import com.cl.ysyd.common.exception.BusiException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public final static String USER_KEY = "userId";

    public final static String TOKEN_KEY = "token";

    //不需要登录就可以访问的路径(比如:登录等)
    private String[] includeUrls = new String[]{
            "/ysyd/v1/user/login"
    };

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURL().toString();
        int i = this.getIndex(url);
        url = url.substring(i);
        //是否需要过滤
        boolean needFilter = isNeedFilter(url);
        if (!needFilter
                || url.startsWith("/v1/user/xxxxz")
                || url.startsWith("/v1/user/xxx")
        ) { //不需要过滤直接传给下一个过滤器
        } else {
            String token = request.getHeader(TOKEN_KEY);
            String userId = request.getHeader(USER_KEY);
            if(StringUtils.isBlank(token)){
                throw new BusiException("Token为空, 请求失败!");
            }
            logger.info("登录用户的token为: token{}", token);
            if(StringUtils.isBlank(userId)){
                throw new BusiException("用户ID为空, 请求失败!");
            }
            logger.info("登录用户为: userId{}", userId);
            //如果token无效
            if (!checkToken(userId)){
                throw new BusiException("token无效, 登陆失败!");
            }else{
                // 用户登录唯一检验
                logger.info("current_userId === {}",userId);
                TokenInfo tokenInfo = AopConstant.currentLoginTokenMap.get(userId);
                if (!token.equals(tokenInfo.getToken())){
                    throw new BusiException("用户在其他地方登陆,请重新登录!");
                }
            }
        }
        return true;
    }

    /**
     * 校验当前token是否有效
     * @param userId
     * @return
     */
    private boolean checkToken(String userId){
        TokenInfo tokenInfo = AopConstant.currentLoginTokenMap.get(userId);
        if(null == tokenInfo){
            throw new BusiException("请求异常,请重新登录!");
        }
        long tokenTime = tokenInfo.getDate();
        if ((System.currentTimeMillis() - tokenTime) > AopConstant.loginUserfulTime){
            throw new BusiException("登录超时,请重新登录!");
        }
        //检验通过 , 更新时间
        tokenInfo.setDate(System.currentTimeMillis());
        return true;
    }

    /**
     * @Author: xxxxx
     * @Description: 是否需要过滤
     * @Date: 2018-03-12 13:20:54
     * @param url
     */
    public boolean isNeedFilter(String url) {
        for (String includeUrl : includeUrls) {
            if(includeUrl.equals(url)) {
                return false;
            }
        }
        return true;
    }

    private int getIndex(String url) {
        //这里是获取"/"符号第三次出现的下标
        Matcher slashMatcher = Pattern.compile("/").matcher(url);
        int i = 3;
        int mIdx = 0;
        while (slashMatcher.find()) {
            mIdx++;
            //当"/"符号第三次出现的位置
            if (mIdx == i) {
                break;
            }
        }
        return slashMatcher.start();
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
