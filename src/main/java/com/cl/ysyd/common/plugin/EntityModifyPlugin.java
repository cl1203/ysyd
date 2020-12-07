package com.cl.ysyd.common.plugin;

import com.cl.ysyd.common.utils.CommentUtil;
import com.cl.ysyd.common.utils.DateUtil;
import com.cl.ysyd.common.utils.LombokUtil;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;

import java.util.List;

/**
 * 更改生成的entity类，使用lombok
 */
public class EntityModifyPlugin extends PluginAdapter {
	
	/**
	 * 获取操作人
	 */
	private String author = System.getProperty("user.name");

	public EntityModifyPlugin() {
		System.out.println("使用lombok插件生成开始 ");
	}

	@Override
	public boolean validate(List<String> warnings) {
		System.out.println("使用lombok插件 ");
		return true;
	}
	
	
	/**
	 * 设置example的文件目录
	 */
	@Override
	public void initialized(IntrospectedTable introspectedTable) {

    	// 自定义Example文件名
    	String exampleType = introspectedTable.getExampleType();
    	exampleType = exampleType.replace("Example", "EntityExample");
    	introspectedTable.setExampleType(exampleType);
        System.out.println("Java Example FileName= " + exampleType);

    	// 自定义Entity文件名
        introspectedTable.setBaseRecordType(introspectedTable.getBaseRecordType() + "Entity");
        System.out.println("Java Entity FileName = " + introspectedTable.getBaseRecordType());
		super.initialized(introspectedTable);
	}

	@Override
	public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		//添加example类注释
		topLevelClass.addJavaDocLine("/**");
		CommentUtil.setClassComment(topLevelClass, introspectedTable);
		//topLevelClass.addJavaDocLine(" * @author " + author + "  " + DateUtils.getNowTime("yyyy-MM-dd HH:mm:ss"));
		topLevelClass.addJavaDocLine(" */");
		return super.modelExampleClassGenerated(topLevelClass, introspectedTable);
	}

	@Override
	public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
		List<Attribute> list=document.getRootElement().getAttributes();
		if(list!=null&&list.size()>0) {
			for(Attribute attr: list) {
				if("namespace".equals(attr.getName())) {
					introspectedTable.setAttribute("extendMapperName", attr.getValue());
//					list.add(new Attribute("namespace",introspectedTable.getAttribute("extendMapperName").toString()));
					break;
				}
			}
		}
//		document.getRootElement().addAttribute(new Attribute("namespace",introspectedTable.getAttribute("extendMapperName").toString()));
		return super.sqlMapDocumentGenerated(document, introspectedTable);
	}

	@Override
	public boolean sqlMapGenerated(GeneratedXmlFile sqlMap, IntrospectedTable introspectedTable) {
		return super.sqlMapGenerated(sqlMap, introspectedTable);
	}

	/**
	 * 添加接口注释
	 */
	@Override
	public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		//接口添加注解
		addMapperClassComment(interfaze, introspectedTable);
		return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
	}
	
	/**
	 * 为表对应的mapper类class添加类注释
	 */
	public void addMapperClassComment(Interface interfaze, IntrospectedTable introspectedTable) {
		if(interfaze.getJavaDocLines()!=null && interfaze.getJavaDocLines().size()>0) {
			System.out.println("接口："+interfaze.getType().getShortName()+"已经有注释，不添加");
			return;
		}
		interfaze.addJavaDocLine("/**");
		String remarks = introspectedTable.getRemarks();
		interfaze.addJavaDocLine(" * " + remarks + " mapper类");
		interfaze.addJavaDocLine(" * @author " + author + "  " +  DateUtil.getNowTime("yyyy-MM-dd"));
		interfaze.addJavaDocLine(" */");
	}

	/**
	 * 生成的baseRecordClass类增加lombok注解
	 */
	@Override
	public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
//		List<String> annotations=topLevelClass.getAnnotations();
//		boolean isLombok=true;
//		String className = topLevelClass.getType().getShortName();
//		if(className.endsWith("Transformer")) {
//			isLombok = false;
//		}else {
//			if(annotations!=null && annotations.size()>0) {
//				for(String annotation: annotations) {
//					if("@Data".equals(annotation)) {
//						isLombok=false;
//						break;
//					}
//				}
//			}
//		}
//		//pkId设置初始值
//		PkUtils.setPkIdValue(topLevelClass);
//		if(isLombok) {
//			LombokUtils.useLomBok(topLevelClass,true);
//		}
		return super.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);
	}

	/**
	 * 生成的primaryKey类增加lombok注解
	 */
	@Override
	public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		List<String> annotations=topLevelClass.getAnnotations();
		boolean isLombok=true;
		if(annotations!=null && annotations.size()>0) {
			for(String annotation: annotations) {
				if("@Data".equals(annotation)) {
					isLombok=false;
					System.out.println("类:"+topLevelClass.getType().getShortName()+"已经有了lombok注解");
					break;
				}
			}
		}
		if(isLombok) {
			LombokUtil.useLomBok(topLevelClass,true);
		}
		return super.modelPrimaryKeyClassGenerated(topLevelClass, introspectedTable);
	}

	/**
	 * 生成的recordWithBlob的类增加lombok注解
	 */
	@Override
	public boolean modelRecordWithBLOBsClassGenerated(TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		List<String> annotations=topLevelClass.getAnnotations();
		boolean isLombok=true;
		if(annotations!=null && annotations.size()>0) {
			for(String annotation: annotations) {
				if("@Data".equals(annotation)) {
					System.out.println("类:"+topLevelClass.getType().getShortName()+"已经有了lombok注解");
					isLombok=false;
					break;
				}
			}
		}
		if(isLombok) {
			LombokUtil.useLomBok(topLevelClass,true);
		}
		return super.modelRecordWithBLOBsClassGenerated(topLevelClass, introspectedTable);
	}
	
	/**
	 * 不生成set方法
	 */
	@Override
	public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass,
			IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
		return true;
	}
	
	/**
	 * 不生成get方法
	 */
	@Override
	public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass,
			IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
		return true;
	}

}
