package com.cl.ysyd.common.plugin;

import com.cl.ysyd.common.utils.DateUtil;
import com.cl.ysyd.common.utils.TableColumnUtil;
import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.exception.ShellException;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.mybatis.generator.internal.util.JavaBeansUtil.getJavaBeansSetter;
import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

/**
 * 扩展Mapper生成
 */
public class MapperExtendGeneratorPlugin extends PluginAdapter {

	private ShellCallback shellCallback = null;

	private String extendTargetPackage;
	private String mapperTargetPackage;
	private String mapperTargetDir;

	/**
	 * mapper父类
	 */
	private String daoSuperClass;

	/**
	 * 获取操作人
	 */
	private String author = System.getProperty("user.name");

	/**
	 * 是否覆盖标识
	 */
	private boolean isOverride = false;

	public MapperExtendGeneratorPlugin() {
		System.out.println("扩展Mapper生成开始 ");
		shellCallback = new DefaultShellCallback(false);
	}

	// @Override
	public void initialized1(IntrospectedTable introspectedTable) {
		// 获取生成的扩展mapper目录
		String targetPackage = context.getJavaClientGeneratorConfiguration().getTargetPackage() + "."
				+ this.extendTargetPackage;
		// 获取扩展之后的名
		String extendFileName = introspectedTable.getMyBatis3JavaMapperType()
				.substring(introspectedTable.getMyBatis3JavaMapperType().lastIndexOf(".") + 1);
		// 设置SqlMapper的namespace
		introspectedTable.setAttribute("extendMapperName", targetPackage + "." + extendFileName + "Extend");
	}

	@Override
	public void initialized(IntrospectedTable introspectedTable) {
		// 获取生成的扩展mapper目录
		String targetPackage = mapperTargetPackage;
		String baseRecordType = introspectedTable.getBaseRecordType();
		// 自定义JavaMapper文件名
		String shortName = baseRecordType.substring(baseRecordType.lastIndexOf(".") + 1);
		String extendFileName = targetPackage + "." + shortName + "Mapper";
		// 获取扩展之后的名
		// 设置SqlMapper的namespace
		introspectedTable.setAttribute("mapperName", extendFileName);
		introspectedTable.setMyBatis3JavaMapperType(extendFileName);
	}

	@Override
	public boolean validate(List<String> warnings) {
		System.out.println("扩展Mapper生成必须参数校验开始 ");
		TableColumnUtil.parseTableUniqueColumn(properties.getProperty("tableDataUnique"));
		TableColumnUtil.tableDataState = properties.getProperty("tableDataState");
		extendTargetPackage = properties.getProperty("extendTargetPackage");
		mapperTargetPackage = properties.getProperty("mapperTargetPackage");
		mapperTargetDir = properties.getProperty("mapperTargetDir");
		daoSuperClass = properties.getProperty("daoSuperClass");

//		boolean extendTargetPackageValid = stringHasValue(extendTargetPackage);
		boolean daoSuperClassValid = stringHasValue(daoSuperClass);
		String isOverrideFlag = properties.getProperty("isOverride");
		if (stringHasValue(isOverrideFlag)) {
			isOverride = Boolean.valueOf(isOverrideFlag);
		}

		boolean check = daoSuperClassValid;
		if (!check) {
			System.out.println("扩展Mapper生成必须参数缺少.");
		} else {
			System.out.println("扩展Mapper生成必须参数校验通过");
		}
		return check;
	}

	/**
	 * 生成扩展mapper文件
	 */
	// @Override
	public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles1(IntrospectedTable introspectedTable) {

		List<GeneratedJavaFile> mapperJavaFiles = new ArrayList<GeneratedJavaFile>();

		List<CompilationUnit> list = getCompilationUnits(introspectedTable);
		try {
			for (CompilationUnit cu : list) {
				String targetPackage = context.getJavaClientGeneratorConfiguration().getTargetPackage() + "."
						+ this.extendTargetPackage;
				String targetDir = context.getJavaClientGeneratorConfiguration().getTargetProject();
				GeneratedJavaFile mapperJavafile = new GeneratedJavaFile(cu, targetDir, context.getJavaFormatter());
				// 判断文件是否存在
				File mapperDir = shellCallback.getDirectory(targetDir, targetPackage);
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
		System.out.println("------ 结束: 生成扩展Mapper类文件 ------");
		return mapperJavaFiles;
	}

	/**
	 * 生成扩展mapper文件
	 */
	@Override
	public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {

		List<GeneratedJavaFile> mapperJavaFiles = new ArrayList<GeneratedJavaFile>();
		List<CompilationUnit> list = getNewCompilationUnits(introspectedTable);
		try {
			for (CompilationUnit cu : list) {
//				String targetPackage = context.getJavaClientGeneratorConfiguration().getTargetPackage();
//				String targetDir = context.getJavaClientGeneratorConfiguration().getTargetProject();
				GeneratedJavaFile mapperJavafile = new GeneratedJavaFile(cu, mapperTargetDir, context.getJavaFormatter());
				// 判断文件是否存在
				File mapperDir = shellCallback.getDirectory(mapperTargetDir, mapperTargetPackage);
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
		System.out.println("------ 结束: 生成扩展Mapper类文件 ------");

		return mapperJavaFiles;
	}

	public List<CompilationUnit> getNewCompilationUnits(IntrospectedTable introspectedTable) {
		// 或者对象
		FullyQualifiedJavaType type = new FullyQualifiedJavaType(
				introspectedTable.getAttribute("mapperName").toString());
		Interface interfaze = new Interface(type);

		// 添加import包名
		FullyQualifiedJavaType subModelJavaType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
		FullyQualifiedJavaType subModelExampleJavaType = new FullyQualifiedJavaType(introspectedTable.getExampleType());
		FullyQualifiedJavaType daoSuperType = new FullyQualifiedJavaType(daoSuperClass);
		interfaze.addImportedType(daoSuperType);
		interfaze.addImportedType(subModelJavaType);
		interfaze.addImportedType(subModelExampleJavaType);

		// 添加泛型支持
		FullyQualifiedJavaType daoExtendsType = new FullyQualifiedJavaType(daoSuperType.getShortName());
		daoExtendsType.addTypeArgument(subModelJavaType);
		daoExtendsType.addTypeArgument(subModelExampleJavaType);
		daoExtendsType.addTypeArgument(FullyQualifiedJavaType.getStringInstance()); // 主键类型
		interfaze.addSuperInterface(daoExtendsType);
		interfaze.setVisibility(JavaVisibility.PUBLIC);
//
//		introspectedTable.setAttribute("extendMapperName", type);

		context.getCommentGenerator().addJavaFileComment(interfaze);

		// 添加mapper扩展类注解
		// addMapperClassComment(interfaze, introspectedTable);
		// addJudgeUniqueConlumnMethod(interfaze, introspectedTable);
		// addUpdateStateByPkId(interfaze, introspectedTable);
		// 扩展类添加分页方法
		// getQueryPageModelFn(interfaze, introspectedTable);

		List<CompilationUnit> answer = new ArrayList<CompilationUnit>();
		if (context.getPlugins().clientGenerated(interfaze, null, introspectedTable)) {
			answer.add(interfaze);
		}

		return answer;
	}

	public List<CompilationUnit> getCompilationUnits(IntrospectedTable introspectedTable) {
		// 或者对象
		FullyQualifiedJavaType type = new FullyQualifiedJavaType(
				introspectedTable.getAttribute("extendMapperName").toString());

		Interface interfaze = new Interface(type);
		interfaze.setVisibility(JavaVisibility.PUBLIC);

		introspectedTable.setAttribute("extendMapper", type);

		context.getCommentGenerator().addJavaFileComment(interfaze);

		// 添加继承接口
		FullyQualifiedJavaType supperInterface = new FullyQualifiedJavaType(
				introspectedTable.getMyBatis3JavaMapperType());
		interfaze.addSuperInterface(supperInterface);
		interfaze.addImportedType(supperInterface);

		// 添加mapper扩展类注解
		addMapperClassComment(interfaze, introspectedTable);
		addJudgeUniqueConlumnMethod(interfaze, introspectedTable);
		addUpdateStateByPkId(interfaze, introspectedTable);
		// 扩展类添加分页方法
		getQueryPageModelFn(interfaze, introspectedTable);

		List<CompilationUnit> answer = new ArrayList<CompilationUnit>();
		if (context.getPlugins().clientGenerated(interfaze, null, introspectedTable)) {
			answer.add(interfaze);
		}

		return answer;
	}

	private void addUpdateStateByPkId(Interface interfaze, IntrospectedTable introspectedTable) {
		TableColumnUtil.judgeTableDeleteState(introspectedTable);
		if (!TableColumnUtil.exsitState) {
			return;
		}
		Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
		Method method = new Method();
		method.setDefault(true);
		method.setReturnType(FullyQualifiedJavaType.getIntInstance());
		method.setName("updateStateByPkId");
		String pk = introspectedTable.getPrimaryKeyColumns().get(0).getJavaProperty();
		method.addParameter(
				new Parameter(introspectedTable.getPrimaryKeyColumns().get(0).getFullyQualifiedJavaType(), pk));
		method.addParameter(new Parameter(new FullyQualifiedJavaType("java.lang.String"), "state"));
		method.addParameter(new Parameter(new FullyQualifiedJavaType("java.lang.String"), "updateBy"));

		FullyQualifiedJavaType javaType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
		String primaryKeySetMethod = getJavaBeansSetter(introspectedTable.getPrimaryKeyColumns().get(0), context,
				introspectedTable).getName();
		method.addBodyLine(javaType.getShortName() + " entity = new " + javaType.getShortName() + "();");
		method.addBodyLine("entity." + primaryKeySetMethod + "(" + pk + ");");
		method.addBodyLine("entity.setUpdateBy(updateBy);");
		method.addBodyLine("entity.setState(state);");
		method.addBodyLine("return this.updateByPrimaryKeySelective(entity);");
		context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
		if (context.getPlugins().clientDeleteByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable)) {
			interfaze.addImportedTypes(importedTypes);
			interfaze.addMethod(method);
		}

	}

	/**
	 * 生成queryPageModel方法
	 * 
	 * @param introspectedTable
	 * @return
	 */
	private void getQueryPageModelFn(Interface interfaze, IntrospectedTable introspectedTable) {
		Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
		Method method = new Method();
		// method.setVisibility(JavaVisibility.DEFAULT);
		method.setDefault(true);

		FullyQualifiedJavaType returnType = new FullyQualifiedJavaType("com.github.pagehelper.PageInfo");
		returnType.addTypeArgument(new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()));
		method.setReturnType(returnType);

		method.setName("queryPageModel");
		importedTypes.add(new FullyQualifiedJavaType("com.github.pagehelper.PageInfo"));
		importedTypes.add(new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()));
		importedTypes.add(new FullyQualifiedJavaType("com.github.pagehelper.PageHelper"));

		FullyQualifiedJavaType type = (FullyQualifiedJavaType) introspectedTable.getAttribute("commandPageType");
		importedTypes.add(type);
		Parameter parameter = new Parameter(type, "query");
		method.addParameter(parameter);

		// 设置分页和查询分页信息
		method.addBodyLine("PageHelper.startPage(query.getCurrPage(), query.getPageSize());");
		// 设置排序字段
		method.addBodyLine("if(query.getOrderField()!=null && query.getOrderField().length()>0) {");
		method.addBodyLine("PageHelper.orderBy(query.getOrderField());");
		method.addBodyLine("}");
		// 新建example
		FullyQualifiedJavaType example = new FullyQualifiedJavaType(introspectedTable.getExampleType());
		method.addBodyLine(example.getShortName() + " example = new " + example.getShortName() + "();");
		FullyQualifiedJavaType baseType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());

		/* 添加默认EqualTo查询条件开始 */
		List<IntrospectedColumn> baseColumns = introspectedTable.getBaseColumns();
		StringBuilder sb = new StringBuilder();
		sb.append("example.createCriteria()");
		baseColumns.forEach(column -> {
			sb.append(".and");
			String getJavaPropertyUpperCase = column.getJavaProperty().substring(0, 1).toUpperCase()
					+ column.getJavaProperty().substring(1);
			sb.append(getJavaPropertyUpperCase);
			sb.append("EqualTo(query.get");
			sb.append(getJavaPropertyUpperCase);
			sb.append("())");
		});
		sb.append(";");
		method.addBodyLine(sb.toString());
		method.addBodyLine("List<" + baseType.getShortName() + "> list = this.selectByExample(example);");
		/* 添加EqualTo查询条件结束 */
		method.addBodyLine("PageInfo<" + baseType.getShortName() + "> page = new PageInfo<" + baseType.getShortName()
				+ ">(list);");
		method.addBodyLine("return page;");

		importedTypes.add(example);
		importedTypes.add(FullyQualifiedJavaType.getNewListInstance());
		context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
		if (context.getPlugins().clientDeleteByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable)) {
			interfaze.addImportedTypes(importedTypes);
			interfaze.addMethod(method);
		}
	}

	/**
	 * 为表对应的mapper扩展类class添加类注释
	 */
	public void addMapperClassComment(Interface interfaze, IntrospectedTable introspectedTable) {
		interfaze.addJavaDocLine("/**");
		String remarks = introspectedTable.getRemarks();
		interfaze.addJavaDocLine(" * " + remarks + " mapper扩展类");
		interfaze.addJavaDocLine(" * @author " + author + "  " + DateUtil.getNowTime("yyyy-MM-dd HH:mm:ss"));
		interfaze.addJavaDocLine(" */");
	}

	/**
	 * 生成queryPageModel方法
	 * 
	 * @param introspectedTable
	 * @return
	 */
	public void addJudgeUniqueConlumnMethod(Interface interfaze, IntrospectedTable introspectedTable) {
		List<String> uniqueColumnList = TableColumnUtil.tableUniqueColumnMap
				.get(introspectedTable.getTableConfiguration().getTableName());
		if (uniqueColumnList == null || uniqueColumnList.isEmpty()) {
			return;
		}
		Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
		Method method = new Method();
		method.setDefault(true);

		FullyQualifiedJavaType returnType = new FullyQualifiedJavaType("java.util.List");
		returnType.addTypeArgument(new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()));
		method.setReturnType(returnType);

		StringBuilder methodName = new StringBuilder();
		methodName.append("queryBy");
		for (int i = 0; i < uniqueColumnList.size(); i++) {
			Parameter parameter = new Parameter(new FullyQualifiedJavaType("java.lang.String"),
					uniqueColumnList.get(i));
			method.addParameter(parameter);
			String uniqueColumn = uniqueColumnList.get(i);
			if (i == uniqueColumnList.size() - 1) {
				methodName.append(uniqueColumn.substring(0, 1).toUpperCase());
				methodName.append(uniqueColumn.substring(1));
			} else {
				methodName.append(uniqueColumn.substring(0, 1).toUpperCase());
				methodName.append(uniqueColumn.substring(1));
				methodName.append("And");
			}
		}
		method.setName(methodName.toString());
		// 新建example
		FullyQualifiedJavaType example = new FullyQualifiedJavaType(introspectedTable.getExampleType());
		method.addBodyLine(example.getShortName() + " example = new " + example.getShortName() + "();");
		/* 添加EqualTo查询条件开始 */
		StringBuilder sb = new StringBuilder();
		sb.append("example.createCriteria()");
		uniqueColumnList.forEach(uniqueColumn -> {
			sb.append(".and");
			String getJavaPropertyUpperCase = uniqueColumn.substring(0, 1).toUpperCase() + uniqueColumn.substring(1);
			sb.append(getJavaPropertyUpperCase);
			sb.append("EqualTo(");
			sb.append(uniqueColumn);
			sb.append(")");
		});
		sb.append(";");
		method.addBodyLine(sb.toString());
		/* 添加EqualTo查询条件结束 */
		method.addBodyLine("return this.selectByExample(example);");
		importedTypes.add(example);
		importedTypes.add(FullyQualifiedJavaType.getNewListInstance());

		context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
		if (context.getPlugins().clientDeleteByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable)) {
			interfaze.addImportedTypes(importedTypes);
			interfaze.addMethod(method);
		}
	}

}
