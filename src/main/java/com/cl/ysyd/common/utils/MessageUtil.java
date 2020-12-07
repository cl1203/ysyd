/**
 * MessageUtil.java
 * Created at 2020-04-19
 * Created by xieyingbin
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 */
package com.cl.ysyd.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 消息工具类
 */
public class MessageUtil {
	private final static Logger logger = LoggerFactory.getLogger(MessageUtil.class);

	/**
	 * 国际化消息解析处理类（SpringBoot有默认加载）
	 */
	private static MessageSource messageSource;

	/**
	 * 消息编码表达式匹配规则 {code}
	 */
	private static Pattern i18n_code_regex = Pattern.compile("\\{(.*?)\\}");

	/**
	 * I18n.NotEmpty表达式
	 */
	private static Pattern i18n_notempty_regex = Pattern.compile("I18n.NotEmpty\\((.*?)\\)");

	/**
	 * I18n.Length表达式
	 */
	private static Pattern i18n_length_regex = Pattern.compile("I18n.Length\\((.*?)\\)");

	/**
	 * 国际化消息转换
	 * 
	 * @param code 消息编码
	 * @return 消息内容
	 */
	public static String getI18nMessage(String code) {
		return getI18nMessage(code, null);
	}

	/**
	 * 国际化消息转换
	 * 
	 * @param code 消息编码
	 * @param args 消息参数
	 * @return 消息内容
	 */
	public static String getI18nMessage(String code, Object[] args) {
		String message = "";
		if (messageSource == null) {
			messageSource = ContextUtil.getBean(MessageSource.class);
		}
		Locale locale = ContextUtil.getLocale();
		if (locale == null) {
			// 默认简体中文
			locale = Locale.SIMPLIFIED_CHINESE;
		}

		try {
			message = messageSource.getMessage(code, args, locale);
		} catch (Exception ex) {
			logger.warn(ex.getMessage());
			try {
				message = MessageFormat.format(code, args);
			} catch (Exception ex2) {
				logger.error(ex2.getMessage());
				message = code;
			}
		}
		return message;
	}

	/**
	 * 组合表达式类型国际化消息转换
	 * 
	 * @param code 消息编码 eg. 组合表达式样例： {xxx} {yyy} {zzz}
	 * 
	 * @return 消息内容
	 */
	public static String getI18nExpMessage(String code) {
		Locale locale = ContextUtil.getLocale();
		if (locale == null) {
			// 默认简体中文
			locale = Locale.SIMPLIFIED_CHINESE;
		}

		return getI18nExpMessage(code, locale);
	}

	/**
	 * 组合表达式类型国际化消息转换
	 * 
	 * @param code 消息编码 eg. 组合表达式样例： {xxx} {yyy} {zzz}
	 * 
	 * @return 消息内容
	 */
	public static String getI18nExpMessage(String code, Locale locale) {
		if (messageSource == null) {
			messageSource = ContextUtil.getBean(MessageSource.class);
		}

		// 国际化消息表达式信息转换
		String editCode = parseAnnotationMessage(code);
		String message = editCode;

		try {
			Matcher matcher = i18n_code_regex.matcher(editCode);
			while (matcher.find()) {
				// 取得匹配到的 {xxx}
				String subMessage = "";
				String key = matcher.group(0);
				try {
					// 取得匹配 {xxx}中的key: xxx
					String subKey = StringUtils.mid(key, 1, key.length() - 2);
					if (!StringUtils.isEmpty(subKey)) {
						subMessage = messageSource.getMessage(subKey, null, locale);
					}
				} catch (Exception ex) {
					logger.warn(ex.getMessage());
					subMessage = key;
				}
				message = message.replace(key, subMessage);
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		return message;
	}

	/**
	 * 国际化消息表达式信息转换: <br>
	 * I18n.NotEmpty({Area.Code}) => {Area.Code}{Common.NotEmpty} <br>
	 * I18n.Length({Area.Code}, 5, 20) => {Area.Code}{Common.Length.Error}({Common.MinLength}5，{Common.MaxLength}20) <br>
	 * 
	 * @param code 消息编码 <br>
	 * @return
	 */
	public static String parseAnnotationMessage(String code) {
		/**
		 * 长度信息编辑
		 */
		String oldCode = code;
		String message = oldCode;
		try {
			Matcher matcher1 = i18n_length_regex.matcher(oldCode);
			while (matcher1.find()) {
				// 取得匹配
				String key = matcher1.group(0);
				String subKey = "";
				try {
					// 取得匹配
					subKey = AnnotationMessageUtil.editLengthMessage(matcher1.group(1));
				} catch (Exception ex) {
					logger.warn(ex.getMessage());
					subKey = key;
				}
				message = message.replace(key, subKey);
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}

		/**
		 * 非空消息编辑
		 */
		oldCode = message;
		try {
			Matcher matcher2 = i18n_notempty_regex.matcher(oldCode);
			while (matcher2.find()) {
				// 取得匹配
				String key = matcher2.group(0);
				String subKey = "";
				try {
					// 取得匹配
					subKey = AnnotationMessageUtil.editNotEmptyMessage(matcher2.group(1));
				} catch (Exception ex) {
					logger.warn(ex.getMessage());
					subKey = key;
				}
				message = message.replace(key, subKey);
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}

		//logger.info("------ Parsed Annotation Message = {}", message);
		return message;
	}
	
	/**
	 * 是否组合表达式类型
	 * 
	 * @param code eg. 组合表达式样例： {xxx} {yyy} {zzz}
	 * @return true: 是，false：否
	 */
	public static boolean isExpMessage(String code) {
		Matcher matcher = i18n_code_regex.matcher(code);
		if (matcher.find()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 是否默认（中文简体）语言环境
	 * 
	 * @return true: 是，false：否
	 */
	public static boolean isDefaultLocale() {
		Locale locale = ContextUtil.getLocale();
		if (locale == null) {
			return true;
		}

		if (locale.equals(Locale.SIMPLIFIED_CHINESE)) {
			return true;
		}

		return false;
	}

	/**
	 * 注解消息转换工具类 <br>
	 * Target Annotation: <br>
	 * 1) javax.validation.constraints.NotEmpty <br>
	 * 2) org.hibernate.validator.constraints.Length <br>
	 * 
	 * @author xieyb
	 *
	 */
	public static class AnnotationMessageUtil {

		/**
		 * NotEmpty注解消息编辑
		 * 
		 * @param code
		 * @return
		 */
		public static String editNotEmptyMessage(String code) {
			return code + "{Common.NotEmpty}";
		}

		/**
		 * Length注解消息编辑
		 * 
		 * @param code
		 * @return
		 */
		public static String editLengthMessage(String code) {
			try {
				String[] params = code.split(",");
				if (params.length != 3) {
					return code;
				}

				for (int i = 0; i < params.length; i++) {
					params[i] = params[i].trim();
				}

				String name = params[0];
				int min = Integer.valueOf(params[1]);
				int max = Integer.valueOf(params[2]);

				if (min > 0) {
					return name + "{Common.Length.Error} ({Common.MinLength}" + min + ", {Common.MaxLength}" + max + ")";
				} else {
					return name + "{Common.Length.Error} ({Common.MaxLength}" + max + ")";
				}
			} catch (Exception ex) {
				logger.error(ex.getMessage(), ex);
				return code;
			}
		}
	}

//	public static void main(String[] args) {
//		String code1 = "I18n.NotEmpty({Area.Code})";
//		String code2 = "I18n.Length({Area.Code}, 5, 20)";
//		String message = "";
//		message = MessageUtil.parseAnnotationMessage(code1);
//		System.out.println("message = " + message);
//		message = MessageUtil.parseAnnotationMessage(code2);
//		System.out.println("message = " + message);
//
//		boolean b1 = MessageUtil.isExpMessage(code1);
//		System.out.println("b1 = " + b1);
//		boolean b2 = MessageUtil.isExpMessage(code2);
//		System.out.println("b2 = " + b2);
//	}
}
