/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.cl.ysyd.common.utils;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;

import java.util.*;

/**
 *表字段唯一值获取，状态字段的存在
 */
public class TableColumnUtil {
    public static Map<String, List<String>> tableUniqueColumnMap = new HashMap<>();
    public static String tableDataState;
    public static boolean exsitState;
    
    /**
     * <pre>
     * 解析表唯一字段
     * </pre>
     *
     * @param tableDataUnique
     * @return key 为表名，value为该表不可重复的字段
     */
    public static void parseTableUniqueColumn(String tableDataUnique) {
        String[] tableAndUniqueColumnList = tableDataUnique.split(";");
        for (String tableAndUniqueColumn : tableAndUniqueColumnList) {
            int left = tableAndUniqueColumn.indexOf("(");
            int right = tableAndUniqueColumn.indexOf(")");
            if (left == -1 || right == -1) {
               continue;
            }
            String tableName = tableAndUniqueColumn.substring(0, left);
            String uniqueColumnString = tableAndUniqueColumn.substring(left + 1, right);
            String[] uniqueColumnArray = uniqueColumnString.split(",");
            tableUniqueColumnMap.put(tableName, new ArrayList<String>(new HashSet<String>(Arrays.asList(uniqueColumnArray))));
        }
        tableUniqueColumnMap.putAll(tableUniqueColumnMap);
    }
    
    
    /**
     * <pre>
     * 判断表是否存在删除状态的字段
     * </pre>
     *
     * @param introspectedTable
     * @return
     */
    public static void judgeTableDeleteState(IntrospectedTable introspectedTable) {
        List<IntrospectedColumn> baseColumns = introspectedTable.getBaseColumns();
        exsitState  =  baseColumns.stream().anyMatch(baseColumn -> tableDataState.equals(baseColumn.getJavaProperty()));
    }
}
