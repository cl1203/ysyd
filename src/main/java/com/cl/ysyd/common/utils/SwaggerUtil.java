package com.cl.ysyd.common.utils;

import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.TopLevelClass;

/**
 * 添加swagger工具类
 */
public class SwaggerUtil {
    /**
     * 给头部添加是否使用swagger注解
     *
     * @param topLevelClass
     * @param isSwaggerFlag : 当为true的时候表示使用swagger注释类
     * @param tableRemark   :表注释
     */
    public static void useSwagger(TopLevelClass topLevelClass, boolean isSwaggerFlag, String tableRemark) {
        String className = topLevelClass.getType().getShortName();
        //生成的model使用lombok注解，不生成get/set方法
        if (isSwaggerFlag) {
            topLevelClass.addImportedType("io.swagger.annotations.ApiModel");
            topLevelClass.addImportedType("io.swagger.annotations.ApiModelProperty");
            String remark = "输出Dto类";
            if (className.endsWith("ReqDto")) {
                remark = "请求Dto";
            } else if (className.endsWith("ResDto")) {
                remark = "响应Dto";
            }
            topLevelClass.addAnnotation("@ApiModel(value = \"" + tableRemark + remark + "\")");
        }
    }

    /**
     * 给属性添加是否使用swagger注解
     *
     * @param field         : 属性
     * @param isSwaggerFlag : 当为true的时候表示使用swagger注释类
     * @param fieldName     : 属性注释
     * @param position      : 属性的位置
     * @param dtoType       : 添加swaggerdto类型，command，pageCommand，dto
     */
    public static void useSwaggerForField(Field field, boolean isSwaggerFlag, String fieldName, int position, String dtoType) {
        //生成的model使用lombok注解，不生成get/set方法
        if (isSwaggerFlag) {
            String swaggerAnnotation = "@ApiModelProperty(value=\"" + fieldName + "\"";
            if ("pkId".equals(field.getName()) && "command".equals(dtoType)) {
                swaggerAnnotation += "新增为null,更新必传";
            }
            // swaggerAnnotation += "\"";
            if (("command".equals(dtoType) || "pageCommand".equals(dtoType)) && ("createTime".equals(field.getName()) ||
                    "createBy".equals(field.getName()) ||
                    "updateTime".equals(field.getName()) ||
                    "updateBy".equals(field.getName()))) {
                swaggerAnnotation += ",hidden=true";
            } else if ("command".equals(dtoType) && !"pkId".equals(field.getName())) {
                swaggerAnnotation += ",required=true";
            }
            field.addAnnotation(swaggerAnnotation + " )");
        }
    }
}
