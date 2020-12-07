package com.cl.ysyd.common.plugin;

import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.ShellCallback;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.XmlConstants;
import org.mybatis.generator.exception.ShellException;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

/**
 * 生成扩展xml
 */
public class XmlExtendGeneratorPlugin extends PluginAdapter {

	private ShellCallback shellCallback;

	private String extendTargetPackage;

	/**
	 * 是否覆盖标识
	 */
	private boolean isOverride = false;

	public XmlExtendGeneratorPlugin() {
		System.out.println("扩展xml生成开始 ");
		shellCallback = new DefaultShellCallback(false);
	}

	@Override
	public boolean validate(List<String> warnings) {
		System.out.println("扩展xml生成必须参数校验开始 ");

		extendTargetPackage = properties.getProperty("extendTargetPackage");
		boolean valid = stringHasValue(extendTargetPackage);

		String isOverrideFlag = properties.getProperty("isOverride");
		if(stringHasValue(isOverrideFlag)) {
			isOverride = Boolean.parseBoolean(isOverrideFlag);
		}

		if (!valid) {
			System.out.println("扩展xml生成必须参数缺少.");
		}else {
			System.out.println("扩展xml生成必须参数校验通过");
		}
		return valid;
	}

	/** 
     * 生成扩展Mapper Xml文件
     */
    @Override
    public List<GeneratedXmlFile> contextGenerateAdditionalXmlFiles(IntrospectedTable introspectedTable) {
		System.out.println("------ 开始：生成扩展xml文件 ------");

		List<GeneratedXmlFile> answer = new ArrayList<>();

		try {
			// 生成新的空的xml
			Document document = new Document(XmlConstants.MYBATIS3_MAPPER_PUBLIC_ID, XmlConstants.MYBATIS3_MAPPER_SYSTEM_ID);
			XmlElement root = new XmlElement("mapper");
			root.addAttribute(new Attribute("namespace", introspectedTable.getAttribute("mapperName").toString()));
			//root.addAttribute(new Attribute("namespace", introspectedTable.getAttribute("mapperName").toString()));
			document.setRootElement(root);

			String fileName = introspectedTable.getMyBatis3XmlMapperFileName();
			String xmlPackage= introspectedTable.getMyBatis3XmlMapperPackage()+"."+this.extendTargetPackage;
			String targetProject = context.getSqlMapGeneratorConfiguration().getTargetProject();
			
			GeneratedXmlFile gxf = new GeneratedXmlFile(document, fileName, xmlPackage, 
					targetProject, false, context.getXmlFormatter());

			File mapperXmlDir = shellCallback.getDirectory(targetProject,xmlPackage);
			File mapperXmlFile = new File(mapperXmlDir, fileName);
			// 删除EXT Mapper Xml文件，否则会被merge。
			if (!mapperXmlFile.exists()) {
				answer.add(gxf);
			}else if(this.isOverride){
				answer.add(gxf);
			}
			File srcMapperXmlDir = shellCallback.getDirectory(targetProject, introspectedTable.getMyBatis3XmlMapperPackage());
			File srcMapperXmlFile = new File(srcMapperXmlDir, fileName);
			// 删除原生Mapper Xml文件，否则会被merge。
			if (srcMapperXmlFile.exists()) {
				srcMapperXmlFile.delete();
			}
			
			
		}catch (ShellException e) {
			e.printStackTrace();
		}

		System.out.println("------ 结束：生成扩展xml文件 ------");
    	
    	return answer;
    }
}
