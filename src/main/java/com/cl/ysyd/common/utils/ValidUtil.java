package com.cl.ysyd.common.utils;

import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.TopLevelClass;

/**
 * 添加javax.validation.constraints校验工具类
 */
public class ValidUtil {
    /**
     * 添加校验注解
     * @param topLevelClass
     * @param field
     * @param fieldName
     * @param length
     */
    public static void addValidationAnnotation(TopLevelClass topLevelClass, Field field, String fieldName, Integer length) {
    	if("createBy".equals(field.getName())||
    			"createTime".equals(field.getName())||
				"updateTime".equals(field.getName())||
				"updateBy".equals(field.getName())||
				"pkId".equals(field.getName())) {
    		return;
    	}
    	if(FullyQualifiedJavaType.getStringInstance().equals(field.getType())) {
    		field.addAnnotation("@NotBlank(message=\""+fieldName+"不能为空\")");
    		field.addAnnotation("@Length(max="+length+",message=\""+fieldName+"字段过长, 最大长度为" + length + "\")");
    		topLevelClass.addImportedType("org.hibernate.validator.constraints.Length");
    		topLevelClass.addImportedType("org.hibernate.validator.constraints.NotBlank");
    	}else if(FullyQualifiedJavaType.getNewMapInstance().equals(field.getType())) {
    		field.addAnnotation("@NotEmpty(message=\""+fieldName+"不能为空\")");
    		topLevelClass.addImportedType("javax.validation.constraints.NotEmpty");
    	}else if(FullyQualifiedJavaType.getNewListInstance().equals(field.getType())) {
    		field.addAnnotation("@NotEmpty(message=\""+fieldName+"不能为空\")");
    		topLevelClass.addImportedType("javax.validation.constraints.NotEmpty");
    	}else if(FullyQualifiedJavaType.getIntInstance().equals(field.getType())) {
    		field.addAnnotation("@NotNull(message=\""+fieldName+"不能为空\")");
    		field.addAnnotation("@Min(value=0,message=\""+fieldName+"字段必须大于0\")");
    		field.addAnnotation("@Max(value="+length+",message=\""+fieldName+"字段必须小于"+length+"\")");
    		topLevelClass.addImportedType("javax.validation.constraints.Max");
    		topLevelClass.addImportedType("javax.validation.constraints.Min");
    		topLevelClass.addImportedType("javax.validation.constraints.NotNull");
    	}else if(FullyQualifiedJavaType.getDateInstance().equals(field.getType())) {
    		field.addAnnotation("@NotNull(message=\""+fieldName+"不能为空\")");
    		topLevelClass.addImportedType("javax.validation.constraints.NotNull");
    	} else if(FullyQualifiedJavaType.getBooleanPrimitiveInstance().equals(field.getType())) {
    		field.addAnnotation("@NotNull(message=\""+fieldName+"不能为空\")");
    		topLevelClass.addImportedType("javax.validation.constraints.NotNull");
    	}
    }
}
