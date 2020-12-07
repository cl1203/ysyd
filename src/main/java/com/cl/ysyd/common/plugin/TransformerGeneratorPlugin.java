package com.cl.ysyd.common.plugin;

import com.cl.ysyd.common.utils.StringUtil;
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

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

/**
 * 生成transform
 */
public class TransformerGeneratorPlugin extends PluginAdapter {

	private ShellCallback shellCallback = null;

	private String transformerTargetDir;

	private String transformerTargetPackage;
	
	/**
	 * 继承类
	 */
	private String supperClass;
	
//	/**
//	 * 继承接口
//	 */
//	private String supperFace;

	/**
	 * 是否覆盖标识
	 */
	private boolean isOverride = false;

	public TransformerGeneratorPlugin() {
		System.out.println("transform生成开始 ");
		shellCallback = new DefaultShellCallback(false);
	}

	@Override
	public boolean validate(List<String> warnings) {
		System.out.println("transform生成必须参数校验开始 ");

		transformerTargetDir = properties.getProperty("transformerTargetDir");
		boolean valid = stringHasValue(transformerTargetDir);

		transformerTargetPackage = properties.getProperty("transformerTargetPackage");
		boolean valid2 = stringHasValue(transformerTargetPackage);
		
		String isOverrideFlag = properties.getProperty("isOverride");
		if(stringHasValue(isOverrideFlag)) {
			isOverride = Boolean.valueOf(isOverrideFlag);
		}
		supperClass = properties.getProperty("supperClass");
		boolean check = valid && valid2;
		if (!check) {
			System.out.println("transform生成必须参数缺少.");
		}else {
			System.out.println("transform生成必须参数校验通过");
		}
		return check;
	}

	/**
	 * 生成扩展transform文件
	 */
	@Override
	public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
		System.out.println("------ 开始: 生成输出transform类文件 ------");

		List<GeneratedJavaFile> mapperJavaFiles = new ArrayList<GeneratedJavaFile>();

		List<CompilationUnit> list = getCompilationUnits(introspectedTable);
		try {
			for(CompilationUnit cu: list ) {
				GeneratedJavaFile mapperJavafile = new GeneratedJavaFile(cu,transformerTargetDir,context.getJavaFormatter());
				//判断文件是否存在
				File mapperDir = shellCallback.getDirectory(transformerTargetDir, transformerTargetPackage);
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
		System.out.println("------ 结束: 输出transform类文件 ------");
		return mapperJavaFiles;
	}

	private List<CompilationUnit> getCompilationUnits(IntrospectedTable introspectedTable) {

		// 设置Transformer类信息
		FullyQualifiedTable table = introspectedTable.getFullyQualifiedTable();
		FullyQualifiedJavaType type = new FullyQualifiedJavaType(
				this.transformerTargetPackage + "." + table.getDomainObjectName() + "Transformer");
		TopLevelClass topLevelClass = new TopLevelClass(type);
		topLevelClass.setVisibility(JavaVisibility.PUBLIC);

		// 添加java文件注释
		CommentGenerator commentGenerator = context.getCommentGenerator();
		commentGenerator.addJavaFileComment(topLevelClass);
		FullyQualifiedJavaType fromType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
		FullyQualifiedJavaType toType = (FullyQualifiedJavaType) introspectedTable.getAttribute("dtoType");
		topLevelClass.addImportedType(fromType);
		topLevelClass.addImportedType(toType);
		
		if(!StringUtil.isEmpty(this.supperClass)) {
			FullyQualifiedJavaType superClass = new FullyQualifiedJavaType(this.supperClass);
			superClass.addTypeArgument(fromType);
			superClass.addTypeArgument(toType);
			topLevelClass.setSuperClass(superClass);
			topLevelClass.addImportedType(new FullyQualifiedJavaType(this.supperClass));
		}
		if(introspectedTable.hasBLOBColumns()) {
        	topLevelClass.addImportedType(new FullyQualifiedJavaType(introspectedTable.getRecordWithBLOBsType()));
        }
		
		topLevelClass.addAnnotation("@Component");
		topLevelClass.addImportedType(new FullyQualifiedJavaType("org.springframework.stereotype.Component"));
		
		// 添加类注释
		commentGenerator.addModelClassComment(topLevelClass, introspectedTable);
		//设置transform内部方法
		setMethod(context,introspectedTable,topLevelClass,fromType,toType);
		
		List<CompilationUnit> answer = new ArrayList<CompilationUnit>();
		if (context.getPlugins().modelBaseRecordClassGenerated(topLevelClass, introspectedTable)) {
			answer.add(topLevelClass);
		}
		return answer;
	}
	
	/**
	 * 设置transform内部方法
	 * @param context
	 * @param introspectedTable
	 * @param topLevelClass
	 */
	private void setMethod(Context context,IntrospectedTable introspectedTable,TopLevelClass topLevelClass,FullyQualifiedJavaType fromType,FullyQualifiedJavaType toType) {
		//重写transform方法
		Method method = overrideTransformerMethod(context, introspectedTable,fromType,toType);
		topLevelClass.addMethod(method);
	}
	
	/**
	 * 重写transform方法
	 * @param context
	 * @param introspectedTable
	 * @return
	 */
	private Method overrideTransformerMethod(Context context,IntrospectedTable introspectedTable,FullyQualifiedJavaType fromType,FullyQualifiedJavaType toType) {
        //参数名称
        String parameterName=introspectedTable.getFullyQualifiedTable().getDomainObjectName();
        parameterName=parameterName.substring(0, 1).toLowerCase()+parameterName.substring(1);
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addAnnotation("@Override");
        method.setReturnType(toType);
        method.setName("transform");
        Parameter parameter=new Parameter(fromType,parameterName);
        method.addParameter(parameter);
        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
        
        method.addBodyLine("if ("+parameterName+" == null) {");
        method.addBodyLine("return null;");
        method.addBodyLine("}");
        method.addBodyLine(toType.getShortName()+" dto = new "+toType.getShortName()+"();");
        //设置set方法
        setDtoValue(method,introspectedTable,parameterName);
        
        method.addBodyLine("return dto;");
        return method;
    }
	
	/**
	 * 设置set方法
	 * @param method
	 * @param introspectedTable
	 * @param pn
	 */
	@SuppressWarnings("unchecked")
	private void setDtoValue(Method method, IntrospectedTable introspectedTable, String pn) {
		Map<String, String> setGetMaps = (Map<String, String>) introspectedTable.getAttribute("setGetMethod");
		Map<String, String> blobSetGetMaps=new HashMap<String, String>();
		List<String> blobColumns=new ArrayList<>();
        if(introspectedTable.hasBLOBColumns()) {
        	//获取blob字段java中的set方法名
    		blobColumns=getBlobColumns(introspectedTable);
        }
        for(Entry<String, String> setGet:setGetMaps.entrySet()) {
			if(blobColumns.contains(setGet.getValue())) {
				blobSetGetMaps.put(setGet.getKey(), setGet.getValue());
			}else {
				//给非blob字段赋值
				method.addBodyLine("dto."+setGet.getValue()+"("+pn+"."+setGet.getKey()+"());");
			}
		}
        
        if(introspectedTable.hasBLOBColumns()) {
        	FullyQualifiedJavaType blobType=new FullyQualifiedJavaType(introspectedTable.getRecordWithBLOBsType());
//        	method.addBodyLine("if ("+pn+" instanceof "+blobType.getShortName()+") {");
        	for(Entry<String, String> setGet:blobSetGetMaps.entrySet()) {
        		//给blob字段赋值
				method.addBodyLine("dto."+setGet.getValue()+"(("+pn+")."+setGet.getKey()+"());");
    		}
//        	method.addBodyLine("}");
        }

	}
	
	/**
	 * 返回blob字段java中的set方法名
	 * @param introspectedTable
	 * @return
	 */
	private List<String> getBlobColumns(IntrospectedTable introspectedTable) {
		List<IntrospectedColumn> columns=introspectedTable.getBLOBColumns();
		List<String> blobColumns=new ArrayList<>();
		for(IntrospectedColumn column : columns) {
			String javaName=column.getJavaProperty();
			javaName=javaName.substring(0, 1).toUpperCase()+javaName.substring(1);
			blobColumns.add("set"+javaName);
		}
		return blobColumns;
	}
}
