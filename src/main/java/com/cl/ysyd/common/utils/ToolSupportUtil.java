/**
 * 
 */
package com.cl.ysyd.common.utils;

import java.util.ArrayList;
import java.util.List;

public class ToolSupportUtil {
	
	/**
	 * 替换生成java文件的文件名
	 * @param domainObjectName 带前缀的表名
	 * @return 去掉前缀
	 */
	public static String getJavaFileName (String domainObjectName) {
		List<String> tablePrefixList = new ArrayList<>();
		tablePrefixList.add("Tc");
		tablePrefixList.add("Te");
		tablePrefixList.add("Th");
		tablePrefixList.add("Ti");
		tablePrefixList.add("Tl");
		tablePrefixList.add("Tm");
		tablePrefixList.add("Tr");
		tablePrefixList.add("Ts");
		tablePrefixList.add("Tt");
		for (String prefix : tablePrefixList) {
			if (domainObjectName.startsWith(prefix)) {
				return domainObjectName.replaceFirst(prefix, "");
			}
		}
		return domainObjectName;
	}
}
