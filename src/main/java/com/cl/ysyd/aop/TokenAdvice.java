package com.cl.ysyd.aop;

import com.cl.ysyd.common.constants.AopConstant;
import com.cl.ysyd.common.constants.TokenInfo;
import com.cl.ysyd.common.exception.BusiException;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Aspect
@Component
public class TokenAdvice {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final static String USER_KEY = "userId";

    private final static String TOKEN_KEY = "token";

    //不需要登录就可以访问的路径(比如:登录等)
    private String[] includeUrls = new String[]{
            "/ysyd/v1/user/login"
    };

    @Before("within(com.cl.ysyd.controller..*)")
    public void preHandle() {
        //获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        //从获取RequestAttributes中获取HttpServletRequest的信息
        assert requestAttributes != null;
        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
        assert request != null;
        String url = request.getRequestURL().toString();
        int i = this.getIndex(url);
        url = url.substring(i);
        //是否需要过滤
        boolean needFilter = isNeedFilter(url);
        /*if (needFilter) {
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
        }*/
    }

    /**
     * 校验当前token是否有效
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
     * Author: xxxxx
     * Description: 是否需要过滤
     * Date: 2018-03-12 13:20:54
     */
    private boolean isNeedFilter(String url) {
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

}
