package com.cl.ysyd.common.utils;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.internal.util.StringUtility;

/**
 * 注释工具类
 */
public class CommentUtil {

	/**
	 * 区别设置方法的注解
	 * 
	 * @param method
	 */
	public static void setMethodComment(Method method) {
		String methodName = method.getName();
		// 判断是否是构造函数
		if (method.isConstructor()) {
			method.addJavaDocLine(" * 构造方法");
			return;
		}
		if (methodName.startsWith("create")) {
			method.addJavaDocLine(" * 新增方法");
		} else {
			switch (methodName) {
			case "toString":
				method.addJavaDocLine(" * toString 方法重载");
				break;
			case "selectByPrimaryKey":
				method.addJavaDocLine(" * 根据主键查询信息-不包含blob字段");
				break;
			case "selectByPrimaryKeyWithBLOBs":
				method.addJavaDocLine(" * 根据主键查询信息-包含blob字段");
				break;
			case "select":
				method.addJavaDocLine(" * 有条件查询信息--不包含blob字段");
				break;
			case "selectWithBLOBs":
				method.addJavaDocLine(" * 有条件查询信息--包含blob字段");
				break;
			case "deleteByPrimaryKey":
				method.addJavaDocLine(" * 根据主键删除信息");
				break;
			case "insert":
				method.addJavaDocLine(" * 新增信息");
				break;
			case "insertSelective":
				method.addJavaDocLine(" * 有选择性字段新增信息");
				break;
			case "updateByPrimaryKeyWithBLOBs":
				method.addJavaDocLine(" * 根据主键更新信息-包含blob字段");
				break;
			case "updateByPrimaryKey":
				method.addJavaDocLine(" * 根据主键更新信息");
				break;
			case "transform":
				method.addJavaDocLine(" * 数据转换");
				break;
			case "count":
				method.addJavaDocLine(" * 统计查询接口");
				break;
			case "queryPageModel":
				method.addJavaDocLine(" * 分页查询接口");
				break;
			case "countByExample":
				method.addJavaDocLine(" * 根据example条件统计接口");
				break;
			case "deleteByExample":
				method.addJavaDocLine(" * 根据example条件删除接口");
				break;
			case "selectByExample":
				method.addJavaDocLine(" * 根据example条件查询接口");
				break;
			case "updateByExampleSelective":
				method.addJavaDocLine(" * 根据example条件有选择性的更新接口");
				break;
			case "updateByExample":
				method.addJavaDocLine(" * 根据example条件更新接口");
				break;
			case "updateByPrimaryKeySelective":
				method.addJavaDocLine(" * 根据主键有选择性的更新接口");
				break;
			case "selectByExampleWithBLOBs":
				method.addJavaDocLine(" * 根据example条件查询接口-包含blob字段");
				break;
			case "updateByExampleWithBLOBs":
				method.addJavaDocLine(" * 根据example条件更新接口-包含blob字段");
				break;
			case "updateStateByPkId":
				method.addJavaDocLine(" * 根据主键更新数据状态");
				break;
			case "editResDto":
				method.addJavaDocLine(" * entity转resDto");
				break;
			case "editResDtoList":
				method.addJavaDocLine(" * entity集合转resDto集合");
				break;
			case "editEntity":
				method.addJavaDocLine(" * reqDto转Entity");
				break;
			case "editEntityList":
				method.addJavaDocLine(" * reqDto集合转entity集合");
				break;
			case "queryByPrimaryKey":
				method.addJavaDocLine(" * 根据主键查询信息");
				break;
			default:
				method.addJavaDocLine(" * example method");
				method.addJavaDocLine(" * method:" + method.getName());
			}
		}
		if (!CollectionUtil.isEmpty(method.getParameters())) {
			for (Parameter pt : method.getParameters()) {
				if ("pkId".equals(pt.getName())) {
					method.addJavaDocLine(" * @param " + pt.getName() + " 主键");
				} else if ("reqDto".equals(pt.getName())) {
					method.addJavaDocLine(" * @param " + pt.getName() + " 请求dto");
				} else {
					method.addJavaDocLine(" * @param " + pt.getName());
				}
			}
		}
		if (null != method.getReturnType()) {
			if (method.getReturnType().getShortName().contains("ResponseData")) {
				method.addJavaDocLine(" * @return " +  "响应结果:" + method.getReturnType().getShortName());
			} else {
				method.addJavaDocLine(" * @return " + method.getReturnType().getShortName());
			}
			
		}
	}

	/**
	 * 区别设置方法的注解
	 * 
	 * @param method
	 */
	public static void setFieldComment(Field field) {
		String methodName = field.getName();
		switch (methodName) {
		case "serialVersionUID":
			field.addJavaDocLine(" * " + field.getName());
			break;
		default:
			field.addJavaDocLine(" * example field");
			field.addJavaDocLine(" * field:" + field.getName());
		}
	}

	public static void setClassComment(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {

		String className = topLevelClass.getType().getShortName();
		String tableRemark = "model类";
		if (className.endsWith("WithBLOBs")) {
			tableRemark += "----包含大字段对象";
		} else if (className.endsWith("Key")) {
			tableRemark += "----只含主键类";
		} else if (className.endsWith("Dto")) {
			tableRemark = "输出Dto类";
		} else if (className.endsWith("Transformer")) {
			tableRemark = "transformer转换类";
		} else if (className.endsWith("Helper")) {
			tableRemark = "帮助类";
		}else if (className.endsWith("Command")) {
			tableRemark = "输入类";
		} else if (className.endsWith("Extend")) {
			tableRemark = "model扩展类";
		} else if (className.endsWith("ServiceImpl")) {
			tableRemark = "service实现类";
		} else if (className.endsWith("PageCommand")) {
			tableRemark = "page输入类";
		} else if (className.endsWith("Example")) {
			tableRemark = "example查询条件类";
		} else if (className.endsWith("Controller")) {
			tableRemark = "控制层";
		}

		// 获取数据库表的注释
		String remarks = introspectedTable.getRemarks();
		if (StringUtility.stringHasValue(remarks)) {
			String[] remarkLines = remarks.split(System.getProperty("line.separator"));
			for (String remarkLine : remarkLines) {
				topLevelClass.addJavaDocLine(" * " + remarkLine + tableRemark);
			}
		}
		// topLevelClass.addJavaDocLine(" * table:" +
		// introspectedTable.getFullyQualifiedTable());
	}
}
