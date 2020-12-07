package com.cl.ysyd.common.utils;

import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.List;

/**
 * 给Pk_id属性添加默认值
 */
public class PkUtil {
	
	/**
	 * 给pkId添加默认值UUID
	 * @param topLevelClass
	 */
	public static void setPkIdValue(TopLevelClass topLevelClass) {
    	String className = topLevelClass.getType().getShortName();
    	if(className.endsWith("Command")||
    			className.endsWith("Dto")) {
			return;
		}
    	List<Field> fileds =topLevelClass.getFields();
    	if(!CollectionUtil.isEmpty(fileds)) {
    		for(Field f: fileds) {
    			if("pkId".equals(f.getName())) {
    				f.setInitializationString("UuidUtil.uuid32()");
    				topLevelClass.addImportedType("com.shibm.common.util.UuidUtil");
    				break;
    			}
    		}
    	}
    }
}
