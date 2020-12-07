package com.cl.ysyd.common.utils;

import org.mybatis.generator.api.dom.java.TopLevelClass;

/**
 * 添加lombok工具类
 */
public class LombokUtil {
	
	/**
     * 给头部添加是否使用lombok注解
     * @param topLevelClass
     * @param isLombok : 当为true的时候表示使用lombok
     */
    public static void useLomBok(TopLevelClass topLevelClass,boolean isLombok) {
    	String className = topLevelClass.getType().getShortName();
    	if(className.endsWith("Transformer")||
    			className.endsWith("Extend")||
    			className.endsWith("ServiceImpl")||
    			className.endsWith("Service") ||
    			className.endsWith("Controller") || className.endsWith("Entity")) {
			return;
		}
    	//生成的model使用lombok注解，不生成get/set方法
    	if(isLombok) {
    		topLevelClass.addImportedType("lombok.Data");
            topLevelClass.addAnnotation("@Data");
            
            if(topLevelClass.getSuperClass()!=null) {
            	topLevelClass.addImportedType("lombok.EqualsAndHashCode");
                topLevelClass.addAnnotation("@EqualsAndHashCode(callSuper=false)");
            }
    	}
    }
}
