package com.cl.ysyd.common.plugin;

import com.cl.ysyd.common.utils.LombokUtil;
import com.cl.ysyd.common.utils.SwaggerUtil;
import com.cl.ysyd.common.utils.ValidUtil;
import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.codegen.RootClassInfo;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.exception.ShellException;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.*;

import static org.mybatis.generator.internal.util.JavaBeansUtil.*;
import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

/**
 * 生成请求reqDto
 */
public class ReqDtoGeneratorPlugin extends PluginAdapter {

	private ShellCallback shellCallback = null;

	private String targetDir;

	private String targetPackage;

	/**
	 * 是否覆盖标识
	 */
	private boolean isOverride = false;

	private List<String> warnings;

	/**
	 * 标识是否使用lombok注解取代set/get方法
	 */
	private boolean isLombok;
	
	/**
	 * 是否添加swagger注释标识
	 */
	private boolean isSwaggerFlag = true;

	public ReqDtoGeneratorPlugin() {
		System.out.println("reqDto生成开始 ");
		shellCallback = new DefaultShellCallback(false);
	}

	@Override
	public boolean validate(List<String> warnings) {
		System.out.println("reqDto生成必须参数校验开始 ");

		targetDir = properties.getProperty("targetDir");
		boolean valid = stringHasValue(targetDir);

		targetPackage = properties.getProperty("targetPackage");
		boolean valid2 = stringHasValue(targetPackage);
		
		String isOverrideFlag = properties.getProperty("isOverride");
		if(stringHasValue(isOverrideFlag)) {
			isOverride = Boolean.valueOf(isOverrideFlag);
		}
		String swaggerFlag = properties.getProperty("isSwaggerFlag");
		if(stringHasValue(swaggerFlag)) {
			isSwaggerFlag = Boolean.valueOf(swaggerFlag);
		}
		
		boolean check = valid && valid2;
		if (!check) {
			System.out.println("reqDto生成必须参数缺少.");
		}else {
			System.out.println("reqDto生成必须参数校验通过");
		}
		this.warnings = warnings;
		return check;
	}

	/**
	 * 生成reqDto文件
	 */
	@Override
	public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
		System.out.println("------ 开始: 生成输入reqDto类文件 ------");

		List<GeneratedJavaFile> mapperJavaFiles = new ArrayList<GeneratedJavaFile>();

		List<CompilationUnit> list = getCompilationUnits(introspectedTable);
		try {
			for(CompilationUnit cu: list ) {
				GeneratedJavaFile mapperJavafile = new GeneratedJavaFile(cu, targetDir,context.getJavaFormatter());
				//判断文件是否存在
				File mapperDir = shellCallback.getDirectory(targetDir, targetPackage);
				File mapperFile = new File(mapperDir, mapperJavafile.getFileName());
				// 文件不存在
				if (!mapperFile.exists()) {
					mapperJavaFiles.add(mapperJavafile);
				}else if(this.isOverride){
					mapperJavaFiles.add(mapperJavafile);
				}
			}
			
		} catch (ShellException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		System.out.println("------ 结束: 输入reqDto类文件 ------");
		return mapperJavaFiles;
	}

	public List<CompilationUnit> getCompilationUnits(IntrospectedTable introspectedTable) {
		// 获取是否采用lombok
		isLombok = "true".equalsIgnoreCase(this.context.getJavaModelGeneratorConfiguration().getProperty("isLombok"));

		// 设置reqDto类信息
		FullyQualifiedTable table = introspectedTable.getFullyQualifiedTable();
		FullyQualifiedJavaType type = new FullyQualifiedJavaType(
				this.targetPackage + "." + table.getDomainObjectName() + "ReqDto");
		TopLevelClass topLevelClass = new TopLevelClass(type);
		topLevelClass.setVisibility(JavaVisibility.PUBLIC);

		//保存reqDto类型
		introspectedTable.setAttribute("commandType", type);
		
		// 添加java文件注释
		Plugin plugins = context.getPlugins();
		CommentGenerator commentGenerator = context.getCommentGenerator();
		commentGenerator.addJavaFileComment(topLevelClass);

		FullyQualifiedJavaType superClass = getSuperClass(introspectedTable);
		if (superClass != null) {
			topLevelClass.setSuperClass(superClass);
			topLevelClass.addImportedType(superClass);
		}

		// 添加类注释
		commentGenerator.addModelClassComment(topLevelClass, introspectedTable);

		// 添加lombok注解
		LombokUtil.useLomBok(topLevelClass, isLombok);
		
		//添加swagger注释
		SwaggerUtil.useSwagger(topLevelClass, isSwaggerFlag, introspectedTable.getRemarks());

		//构造函数
		if (introspectedTable.isConstructorBased()) {
			addParameterizedConstructor(topLevelClass, introspectedTable);

			if (!introspectedTable.isImmutable()) {
				addDefaultConstructor(topLevelClass, introspectedTable);
			}
		}

		String rootClass = getRootClass(introspectedTable);
		
		//
		Map<String, String> setGetFcName=new HashMap<>();
		
		// 获取所有列
		List<IntrospectedColumn> introspectedColumns = introspectedTable.getAllColumns();
		List<IntrospectedColumn> primaryKeyColumns = introspectedTable.getPrimaryKeyColumns();
		introspectedColumns.removeAll(primaryKeyColumns);
		int i=0;
		for (IntrospectedColumn introspectedColumn : introspectedColumns) {
			if (RootClassInfo.getInstance(rootClass, warnings).containsProperty(introspectedColumn)) {
				continue;
			}
			
			Field field = getJavaBeansField(introspectedColumn, context, introspectedTable);
			
			//添加属性swagger注释
			SwaggerUtil.useSwaggerForField(field, isSwaggerFlag, introspectedColumn.getRemarks(),i++,"reqDto");
			
			if (plugins.modelFieldGenerated(field, topLevelClass, introspectedColumn, introspectedTable,
					Plugin.ModelClassType.BASE_RECORD)) {
				topLevelClass.addField(field);
				topLevelClass.addImportedType(field.getType());
			}
			
			//给字段添加校验注解
			ValidUtil.addValidationAnnotation(topLevelClass, field, introspectedColumn.getRemarks(), introspectedColumn.getLength());;
			
			String getName="";
			String setName="";
			// 不使用lombok注解时生成get/set方法,否则需要生成get/set方法
			Method method = getJavaBeansGetter(introspectedColumn, context, introspectedTable);
			if (plugins.modelGetterMethodGenerated(method, topLevelClass, introspectedColumn, introspectedTable,
					Plugin.ModelClassType.BASE_RECORD)) {
				if (!isLombok) {
					topLevelClass.addMethod(method);
				}
			}
			getName=method.getName();
			if (!introspectedTable.isImmutable()) {
				method = getJavaBeansSetter(introspectedColumn, context, introspectedTable);
				setName=method.getName();
				if (plugins.modelSetterMethodGenerated(method, topLevelClass, introspectedColumn, introspectedTable,
						Plugin.ModelClassType.BASE_RECORD)) {
					if (!isLombok) {
						topLevelClass.addMethod(method);
					}
				}
			}
			setGetFcName.put(getName, setName);
		}

		List<CompilationUnit> answer = new ArrayList<CompilationUnit>();
		if (context.getPlugins().modelBaseRecordClassGenerated(topLevelClass, introspectedTable)) {
			answer.add(topLevelClass);
		}
		//保存set方法
		introspectedTable.setAttribute("commandSetGetMethod", setGetFcName);
		return answer;
	}

	private FullyQualifiedJavaType getSuperClass(IntrospectedTable introspectedTable) {
		FullyQualifiedJavaType superClass;
		String rootClass = getRootClass(introspectedTable);
		if (rootClass != null) {
			superClass = new FullyQualifiedJavaType(rootClass);
		} else {
			superClass = null;
		}

		return superClass;
	}

	private void addParameterizedConstructor(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		Method method = new Method();
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setConstructor(true);
		method.setName(topLevelClass.getType().getShortName());
		context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);

		List<IntrospectedColumn> constructorColumns = introspectedTable.getAllColumns();

		for (IntrospectedColumn introspectedColumn : constructorColumns) {
			method.addParameter(new Parameter(introspectedColumn.getFullyQualifiedJavaType(),
					introspectedColumn.getJavaProperty()));
		}

		StringBuilder sb = new StringBuilder();
		List<IntrospectedColumn> introspectedColumns = introspectedTable.getAllColumns();
		for (IntrospectedColumn introspectedColumn : introspectedColumns) {
			sb.setLength(0);
			sb.append("this."); //$NON-NLS-1$
			sb.append(introspectedColumn.getJavaProperty());
			sb.append(" = "); //$NON-NLS-1$
			sb.append(introspectedColumn.getJavaProperty());
			sb.append(';');
			method.addBodyLine(sb.toString());
		}

		topLevelClass.addMethod(method);
	}

	public String getRootClass(IntrospectedTable introspectedTable) {
		String rootClass = introspectedTable.getTableConfigurationProperty(PropertyRegistry.ANY_ROOT_CLASS);
		if (rootClass == null) {
			Properties properties = context.getJavaModelGeneratorConfiguration().getProperties();
			rootClass = properties.getProperty(PropertyRegistry.ANY_ROOT_CLASS);
		}

		return rootClass;
	}

	protected void addDefaultConstructor(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		topLevelClass.addMethod(getDefaultConstructor(topLevelClass, introspectedTable));
	}

	protected Method getDefaultConstructor(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		Method method = new Method();
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setConstructor(true);
		method.setName(topLevelClass.getType().getShortName());
		method.addBodyLine("super();"); //$NON-NLS-1$
		context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
		return method;
	}
}
