/**
 * LoginUtil.java
 * Created at 2020-03-20
 * Created by xieyingbin
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 */
package com.cl.ysyd.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * 用户登录工具类
 * 
 * @author xieyingbin
 *
 */
@Slf4j
public class LoginUtil {
	/**
	 * 系统管理员账号
	 */
	public static final List<String> ADMIN_ACCOUNT = Arrays.asList("admin", "administrator", "root", "sys", "system");

	/**
	 * 用户账号请求参数名
	 */
	public static final String USERNAME_KEY = "username";
	
	/**
	 * 用户密码请求参数名
	 */
	public static final String PASSWORD_KEY = "password";
	
    /**
     * 客户端类型
     */
	public static final String CLIENT_TYPE = "client-type";

    /**
     * 请求代理
     */
	public static final String USER_AGENT = "user-agent";

    /**
     * 请求IP地址
     */
	public static final String X_FORWARDED_FOR = "X-Forwarded-For";

    /**
     * 登录结果成功
     */
	public static final String LOGIN_SUCCESS = "success";

    /**
     * 登录结果失败
     */
	public static final String LOGIN_FAIL = "fail";

    /**
     * 账号冻结的登录失败次数
     */
	public static final int LOCK_COUNT = 5;

    /**
     * 登录失败冻结时间（秒）
     */
	public static final int LOCK_TIME = 30 * 60;


	public static String getUserId(){
		//获取RequestAttributes
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		//从获取RequestAttributes中获取HttpServletRequest的信息
		HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
		String userId = request.getHeader("userId");
		return userId;
	}


    /**
     * 判断账号是否系统管理员
     * 
     * @param account 账号
     * @return true: 是  false: 否
     */
    public static boolean isAdminAccount(String account) {
    	for (String acc : ADMIN_ACCOUNT) {
    		if (acc.equalsIgnoreCase(account)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * 获取请求IP地址信息
     *
     * @param request 请求对象
     * @return 获取请求IP地址
     */
    public static String getRemoteIp(HttpServletRequest request) {
        String xff = request.getHeader(X_FORWARDED_FOR);
        if (StringUtils.isNotBlank(xff)) {
            String[] ips = xff.split(",");
            return ips[0];
        } else {
            return request.getRemoteAddr();
        }
    }
}
