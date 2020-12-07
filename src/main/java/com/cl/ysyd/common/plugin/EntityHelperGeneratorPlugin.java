package com.cl.ysyd.common.plugin;

import com.cl.ysyd.common.utils.ToolSupportUtil;
import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.exception.ShellException;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static org.mybatis.generator.internal.util.JavaBeansUtil.getJavaBeansGetter;
import static org.mybatis.generator.internal.util.JavaBeansUtil.getJavaBeansSetter;
import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

/**
 * 生成helper
 */
public class EntityHelperGeneratorPlugin extends PluginAdapter {

	private ShellCallback shellCallback = null;

	private String helperTargetDir;

	private String helperTargetPackage;

	/**
	 * 是否覆盖标识
	 */
	private boolean isOverride = false;

	public EntityHelperGeneratorPlugin() {
		System.out.println("helper生成开始 ");
		shellCallback = new DefaultShellCallback(false);
	}

	@Override
	public boolean validate(List<String> warnings) {
		System.out.println("helper生成必须参数校验开始 ");
		helperTargetDir = properties.getProperty("helperTargetDir");
		boolean valid = stringHasValue(helperTargetDir);

		helperTargetPackage = properties.getProperty("helperTargetPackage");
		boolean valid2 = stringHasValue(helperTargetPackage);
		boolean check = valid && valid2;
		if (!check) {
			System.out.println("helper生成必须参数缺少.");
		} else {
			System.out.println("helper生成必须参数校验通过");
		}
		return check;
	}

	/**
	 * 生成扩展helper文件
	 */
	@Override
	public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
		System.out.println("------ 开始: 生成输出helper类文件 ------");

		List<GeneratedJavaFile> mapperJavaFiles = new ArrayList<GeneratedJavaFile>();

		List<CompilationUnit> list = getCompilationUnits(introspectedTable);
		try {
			for (CompilationUnit cu : list) {
				GeneratedJavaFile mapperJavafile = new GeneratedJavaFile(cu, helperTargetDir,
						context.getJavaFormatter());
				// 判断文件是否存在
				File mapperDir = shellCallback.getDirectory(helperTargetDir, helperTargetPackage);
				File mapperFile = new File(mapperDir, mapperJavafile.getFileName());
				// 文件不存在
				if (!mapperFile.exists()) {
					mapperJavaFiles.add(mapperJavafile);
				} else if (this.isOverride) {
					mapperJavaFiles.add(mapperJavafile);
				}
			}

		} catch (ShellException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		System.out.println("------ 结束: 输出helper类文件 ------");
		return mapperJavaFiles;
	}

	private List<CompilationUnit> getCompilationUnits(IntrospectedTable introspectedTable) {

		// 设置Transformer类信息
		FullyQualifiedJavaType type = new FullyQualifiedJavaType(
				this.helperTargetPackage + "." + ToolSupportUtil.getJavaFileName(introspectedTable.getFullyQualifiedTable().getDomainObjectName()) + "Helper");
		introspectedTable.setAttribute("entityHelper", type);
		TopLevelClass topLevelClass = new TopLevelClass(type);
		topLevelClass.setVisibility(JavaVisibility.PUBLIC);

		// 添加java文件注释
		CommentGenerator commentGenerator = context.getCommentGenerator();
		commentGenerator.addJavaFileComment(topLevelClass);
		FullyQualifiedJavaType fromType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());

		topLevelClass.addImportedType(fromType);

		topLevelClass.addAnnotation("@Component");
		topLevelClass.addImportedType(new FullyQualifiedJavaType("org.springframework.stereotype.Component"));

		// 添加类注释
		commentGenerator.addModelClassComment(topLevelClass, introspectedTable);
		// 设置helper内部方法
		setMethod(context, introspectedTable, topLevelClass, fromType);

		List<CompilationUnit> answer = new ArrayList<CompilationUnit>();
		if (context.getPlugins().modelBaseRecordClassGenerated(topLevelClass, introspectedTable)) {
			answer.add(topLevelClass);
		}
		return answer;
	}

	/**
	 * 设置transform内部方法
	 * 
	 * @param context
	 * @param introspectedTable
	 * @param topLevelClass
	 */
	private void setMethod(Context context, IntrospectedTable introspectedTable, TopLevelClass topLevelClass,
			FullyQualifiedJavaType fromType) {
		// 重写transform方法
		editResDto(context, introspectedTable, fromType, topLevelClass);
		editResDtoList(context, introspectedTable, fromType, topLevelClass);
		editEntity(context, introspectedTable, fromType, topLevelClass);
		editEntityList(context, introspectedTable, fromType, topLevelClass);

	}

	/**
	 * 重写transform方法
	 * 
	 * @param context
	 * @param introspectedTable
	 * @return
	 */
	private Method editEntityList(Context context, IntrospectedTable introspectedTable, FullyQualifiedJavaType fromType,
			TopLevelClass topLevelClass) {
		FullyQualifiedJavaType returnType = FullyQualifiedJavaType.getNewListInstance();
		returnType.addTypeArgument(fromType);
		topLevelClass.addImportedType(new FullyQualifiedJavaType("java.util.List"));
		topLevelClass.addImportedType(FullyQualifiedJavaType.getNewArrayListInstance());
		// 参数名称
//		String parameterName = introspectedTable.getFullyQualifiedTable().getDomainObjectName();
		String parameterName = "reqDtoList";
		Method method = new Method();
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(returnType);
		method.setName("editEntityList");
		FullyQualifiedJavaType paramType = FullyQualifiedJavaType.getNewListInstance();
		FullyQualifiedJavaType reqDtoType = (FullyQualifiedJavaType) introspectedTable.getAttribute("commandType");
		paramType.addTypeArgument(reqDtoType);
		Parameter parameter = new Parameter(paramType, parameterName);
		method.addParameter(parameter);
		context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
		method.addBodyLine("List<" + fromType.getShortName() + "> entityList = new ArrayList<>();");
		method.addBodyLine("if (" + parameterName + " == null || " + parameterName + ".isEmpty()){");
		method.addBodyLine("return entityList;");
		method.addBodyLine("}");
		method.addBodyLine(parameterName + ".forEach(reqDto -> {");
		method.addBodyLine("entityList.add(this.editEntity(reqDto));");
		method.addBodyLine("});");
		method.addBodyLine("return entityList;");
		topLevelClass.addMethod(method);
		return method;
	}

	/**
	 * 重写transform方法
	 * 
	 * @param context
	 * @param introspectedTable
	 * @return
	 */
	private Method editResDtoList(Context context, IntrospectedTable introspectedTable, FullyQualifiedJavaType fromType,
			TopLevelClass topLevelClass) {
		FullyQualifiedJavaType returnType = FullyQualifiedJavaType.getNewListInstance();
		FullyQualifiedJavaType dtoType = (FullyQualifiedJavaType) introspectedTable.getAttribute("dtoType");
		returnType.addTypeArgument(dtoType);
		topLevelClass.addImportedType(new FullyQualifiedJavaType("java.util.List"));
		topLevelClass.addImportedType(FullyQualifiedJavaType.getNewArrayListInstance());
		// 参数名称
//		String parameterName = introspectedTable.getFullyQualifiedTable().getDomainObjectName();
		String parameterName = "entityList";
		Method method = new Method();
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(returnType);
		method.setName("editResDtoList");
		FullyQualifiedJavaType paramType = FullyQualifiedJavaType.getNewListInstance();
		paramType.addTypeArgument(fromType);
		Parameter parameter = new Parameter(paramType, parameterName);
		method.addParameter(parameter);
		context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
		method.addBodyLine("List<" + dtoType.getShortName() + "> resDtoList = new ArrayList<>();");
		method.addBodyLine("if (" + parameterName + " == null || " + parameterName + ".isEmpty()){");
		method.addBodyLine("return resDtoList;");
		method.addBodyLine("}");
		method.addBodyLine(parameterName + ".forEach(entity -> {");
		method.addBodyLine("resDtoList.add(this.editResDto(entity));");
		method.addBodyLine("});");
		method.addBodyLine("return resDtoList;");
		topLevelClass.addMethod(method);
		return method;
	}

	/**
	 * 重写transform方法
	 * 
	 * @param context
	 * @param introspectedTable
	 * @param topLevelClass
	 * @return
	 */
	private Method editEntity(Context context, IntrospectedTable introspectedTable, FullyQualifiedJavaType fromType,
			TopLevelClass topLevelClass) {
		FullyQualifiedJavaType reqDtoType = (FullyQualifiedJavaType) introspectedTable.getAttribute("commandType");
		topLevelClass.addImportedType(reqDtoType);
		topLevelClass.addImportedType(FullyQualifiedJavaType.getDateInstance());
		// 参数名称
//		String parameterName = introspectedTable.getFullyQualifiedTable().getDomainObjectName();
		String parameterName = "reqDto";
		Method method = new Method();
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(fromType);
		method.setName("editEntity");
		Parameter parameter = new Parameter(reqDtoType, parameterName);
		method.addParameter(parameter);
		context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);

		method.addBodyLine("if (" + parameterName + " == null) {");
		method.addBodyLine("return null;");
		method.addBodyLine("}");
		method.addBodyLine(fromType.getShortName() + " entity = new " + fromType.getShortName() + "();");
		// 设置set方法
		setEntityValue(method, introspectedTable, parameterName);

		method.addBodyLine("return entity;");
		topLevelClass.addMethod(method);
		return method;
	}

	private void setEntityValue(Method method, IntrospectedTable introspectedTable, String pn) {
		StringBuilder sb = new StringBuilder();
		List<IntrospectedColumn> introspectedColumns = introspectedTable.getAllColumns();
		for (IntrospectedColumn introspectedColumn : introspectedColumns) {
			if ("createTime".equals(introspectedColumn.getJavaProperty())
					|| "pkId".equals(introspectedColumn.getJavaProperty())
					|| "id".equals(introspectedColumn.getJavaProperty())) {
				continue;
			}
			Method setMethod = getJavaBeansSetter(introspectedColumn, context, introspectedTable);
			String setName = setMethod.getName();
			Method getMethod = getJavaBeansGetter(introspectedColumn, context, introspectedTable);
			String getName = getMethod.getName();
			sb.setLength(0);
			if ("updateTime".equals(introspectedColumn.getJavaProperty())) {
				sb.append("entity.");
				sb.append(setName);
				sb.append("(new Date());");
				method.addBodyLine(sb.toString());
			} else {
				sb.append("entity.");
				sb.append(setName);
				sb.append("(reqDto."); //$NON-NLS-1$
				sb.append(getName);
				sb.append("());");
				method.addBodyLine(sb.toString());
			}
		}
	}

	/**
	 * 重写transform方法
	 * 
	 * @param context
	 * @param introspectedTable
	 * @param topLevelClass
	 * @return
	 */
	private Method editResDto(Context context, IntrospectedTable introspectedTable, FullyQualifiedJavaType fromType,
			TopLevelClass topLevelClass) {
		FullyQualifiedJavaType toType = (FullyQualifiedJavaType) introspectedTable.getAttribute("dtoType");
		topLevelClass.addImportedType(toType);
		// 参数名称
		String parameterName = introspectedTable.getFullyQualifiedTable().getDomainObjectName();
		// String parameterName = "entity";
		Method method = new Method();
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(toType);
		method.setName("editResDto");
		Parameter parameter = new Parameter(fromType, parameterName);
		method.addParameter(parameter);
		context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);

		method.addBodyLine("if (" + parameterName + " == null) {");
		method.addBodyLine("return null;");
		method.addBodyLine("}");
		method.addBodyLine(toType.getShortName() + " resDto = new " + toType.getShortName() + "();");
		// 设置set方法
		setResDtoValue(method, introspectedTable, parameterName);

		method.addBodyLine("return resDto;");
		topLevelClass.addMethod(method);
		return method;
	}

	/**
	 * 设置set方法
	 * 
	 * @param method
	 * @param introspectedTable
	 * @param pn
	 */
	@SuppressWarnings("unchecked")
	private void setResDtoValue(Method method, IntrospectedTable introspectedTable, String pn) {
		Map<String, String> setGetMaps = (Map<String, String>) introspectedTable.getAttribute("setGetMethod");
		Map<String, String> blobSetGetMaps = new HashMap<String, String>();
		List<String> blobColumns = new ArrayList<>();
		if (introspectedTable.hasBLOBColumns()) {
			// 获取blob字段java中的set方法名
			blobColumns = getBlobColumns(introspectedTable);
		}
		for (Entry<String, String> setGet : setGetMaps.entrySet()) {
			if (blobColumns.contains(setGet.getValue())) {
				blobSetGetMaps.put(setGet.getKey(), setGet.getValue());
			} else {
				// 给非blob字段赋值
				method.addBodyLine("resDto." + setGet.getValue() + "(" + pn + "." + setGet.getKey() + "());");
			}
		}

		if (introspectedTable.hasBLOBColumns()) {
			FullyQualifiedJavaType blobType = new FullyQualifiedJavaType(introspectedTable.getRecordWithBLOBsType());
//			method.addBodyLine("if (" + pn + " instanceof " + blobType.getShortName() + ") {");
			for (Entry<String, String> setGet : blobSetGetMaps.entrySet()) {
				// 给blob字段赋值
				method.addBodyLine("resDto." + setGet.getValue() + "((" + pn + ")."
						+ setGet.getKey() + "());");
			}
//			method.addBodyLine("}");
		}

	}

	/**
	 * 返回blob字段java中的set方法名
	 * 
	 * @param introspectedTable
	 * @return
	 */
	private List<String> getBlobColumns(IntrospectedTable introspectedTable) {
		List<IntrospectedColumn> columns = introspectedTable.getBLOBColumns();
		List<String> blobColumns = new ArrayList<>();
		for (IntrospectedColumn column : columns) {
			String javaName = column.getJavaProperty();
			javaName = javaName.substring(0, 1).toUpperCase() + javaName.substring(1);
			blobColumns.add("set" + javaName);
		}
		return blobColumns;
	}
}
