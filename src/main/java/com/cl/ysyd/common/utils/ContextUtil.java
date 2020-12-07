/**
 * ContextUtil.java
 * Created at 2020-04-17
 * Created by xieyingbin
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.common.utils;

import org.springframework.context.ApplicationContext;

import java.util.Locale;

/**
 * 上下文信息工具类
 * 
 */
public class ContextUtil {
    /**
     * Spring容器上下文
     */
    private static ApplicationContext context = null;
    
	/**
	 * 当前语言环境
	 */
	private static ThreadLocal<Locale> currentLocale = new ThreadLocal<>();

    /**
     * 设置容器上下文
     * 
     * @param applicationContext 容器上下文
     */
    public static void setApplicationContext(ApplicationContext applicationContext) {
    	context = applicationContext;
	}

	/**
	 * 设置当前语言环境
	 * 
	 * @param locale 语言环境
	 */
	public static void setLocale(Locale locale) {
		currentLocale.set(locale);
	}

	/**
	 * 获取当前语言环境
	 * 
	 */
	public static Locale getLocale() {
		return currentLocale.get();
	}

    /**
     * 获取类对象实例
     * 
     * @param <T> 类型
     * @param cls 类型
     * @return 对象实例
     */
    public static <T> T getBean(Class<T> cls) {
        return (T) context.getBean(cls);
    }

    /**
     * 获取类对象实例
     * 
     * @param name 实例id
     * @param <T> 类型
     * @param cls 类型
     * @return 对象实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name, Class<T> cls) {
        return (T) context.getBean(name);
    }
}
