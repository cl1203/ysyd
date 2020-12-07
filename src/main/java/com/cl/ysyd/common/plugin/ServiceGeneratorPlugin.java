package com.cl.ysyd.common.plugin;

import com.cl.ysyd.common.utils.DateUtil;
import com.cl.ysyd.common.utils.TableColumnUtil;
import com.cl.ysyd.common.utils.ToolSupportUtil;
import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.exception.ShellException;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.mybatis.generator.internal.util.JavaBeansUtil.getJavaBeansGetter;
import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

/**
 * 生成service
 */
public class ServiceGeneratorPlugin extends PluginAdapter {

    private ShellCallback shellCallback = null;

    private String targetDir;

    private String targetPackage;

    private String implTargetDir;

    private String implTargetPackage;


    private static final String ENTITY_HELPER = "entityHelper";
    private static final String ENTITY_HELPER_FILED_NAME = "entityHelperFiledName";
    /**
     * 获取操作人
     */
    private String author = System.getProperty("user.name");

    /**
     * 是否覆盖标识
     */
    private boolean isOverride = false;
    private String busiExceptionPackage;

    public ServiceGeneratorPlugin() {
        System.out.println("service生成开始 ");
        shellCallback = new DefaultShellCallback(false);
    }

    @Override
    public boolean validate(List<String> warnings) {
        System.out.println("servcie生成必须参数校验开始 ");
        // tableDataState = properties.getProperty("tableDataState");
        // tableDataUnique = properties.getProperty("tableDataUnique");
        // this.tableUniqueColumnMap =
        // CommentUtils.parseTableUniqueColumn(tableDataUnique);
        targetDir = properties.getProperty("targetDir");
        boolean valid = stringHasValue(targetDir);

        targetPackage = properties.getProperty("targetPackage");
        boolean valid2 = stringHasValue(targetPackage);

        implTargetDir = properties.getProperty("implTargetDir");
        boolean valid3 = stringHasValue(targetDir);

        implTargetPackage = properties.getProperty("implTargetPackage");
        boolean valid4 = stringHasValue(targetPackage);

        busiExceptionPackage = properties.getProperty("busiExceptionPackage");
        String isOverrideFlag = properties.getProperty("isOverride");
        if (stringHasValue(isOverrideFlag)) {
            isOverride = Boolean.valueOf(isOverrideFlag);
        }
        boolean check = valid && valid2 && valid3 && valid4;
        if (!check) {
            System.out.println("servcie生成必须参数缺少.");
        } else {
            System.out.println("servcie生成必须参数校验通过");
        }
        return check;
    }

    /**
     * 生成servcie文件
     */
    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        System.out.println("------ 开始: 生成servcie类文件 ------");

        List<GeneratedJavaFile> mapperJavaFiles = new ArrayList<GeneratedJavaFile>();
        List<CompilationUnit> listApi = new ArrayList<>();
        List<CompilationUnit> listImpl = new ArrayList<>();
        // 生成service接口
        getCompilationUnitsForServiceApi(introspectedTable, listApi);
        // 实现service接口
        getCompilationUnitsForServiceImpl(introspectedTable, listImpl);
        try {
            for (CompilationUnit cu : listApi) {
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
            for (CompilationUnit cu : listImpl) {
                GeneratedJavaFile mapperJavafile = new GeneratedJavaFile(cu, implTargetDir, context.getJavaFormatter());
                // 判断文件是否存在
                File mapperDir = shellCallback.getDirectory(implTargetDir, implTargetPackage);
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
        System.out.println("------ 结束: servcie类文件 ------");
        return mapperJavaFiles;
    }

    /**
     * 生成service实现类
     *
     * @param introspectedTable
     * @param list
     * @return
     */
    private void getCompilationUnitsForServiceImpl(IntrospectedTable introspectedTable, List<CompilationUnit> list) {
        // 设置servcie实现类信息
        FullyQualifiedTable table = introspectedTable.getFullyQualifiedTable();
        String javaFileName = ToolSupportUtil.getJavaFileName(table.getDomainObjectName());
        FullyQualifiedJavaType type = new FullyQualifiedJavaType(
                this.implTargetPackage + "." + javaFileName + "ServiceImpl");
        TopLevelClass topLevelClass = new TopLevelClass(type);
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);

        // 添加java文件注释
        CommentGenerator commentGenerator = context.getCommentGenerator();
        commentGenerator.addJavaFileComment(topLevelClass);

        // 继承父接口
        FullyQualifiedJavaType supperInterFace = new FullyQualifiedJavaType(
                this.targetPackage + ".I" + javaFileName + "Service");
        topLevelClass.addSuperInterface(supperInterFace);
        topLevelClass.addImportedType(supperInterFace);

        topLevelClass.addAnnotation("@Service");
        topLevelClass.addImportedType(new FullyQualifiedJavaType("org.springframework.stereotype.Service"));
        topLevelClass.addAnnotation("@Slf4j");
        topLevelClass.addImportedType("lombok.extern.slf4j.Slf4j");
        topLevelClass.addImportedType("org.springframework.util.StringUtils");

        // 添加类注释
        commentGenerator.addModelClassComment(topLevelClass, introspectedTable);

        // 添加mapper属性字段
        String mapperName = setMapperField(topLevelClass, introspectedTable, commentGenerator);
        // 引入transformer转换属性
        setHelperField(topLevelClass, introspectedTable, commentGenerator);
        // 实现删除方法
        implDeleteByPrimaryKeyFn(topLevelClass, introspectedTable, mapperName);
        // 实现SelectByPrimaryKey方法
        implSelectByPrimaryKeyFn(topLevelClass, introspectedTable, mapperName);
        // 实现insert方法
        implInsertFn(topLevelClass, introspectedTable, mapperName);
        // 实现UpdateByPrimaryKey方法
        implUpdateByPrimaryKeyFn(topLevelClass, introspectedTable, mapperName);
//		implQueryPageModelFn(topLevelClass, introspectedTable, mapperName);
        if (context.getPlugins().modelBaseRecordClassGenerated(topLevelClass, introspectedTable)) {
            list.add(topLevelClass);
        }
    }

    /**
     * 引入service属性
     *
     * @param topLevelClass
     * @param introspectedTable
     */
    private String setServiceField(TopLevelClass topLevelClass, IntrospectedTable introspectedTable,
                                   String serviceName) {
        // FullyQualifiedJavaType mapper = new
        // FullyQualifiedJavaType(introspectedTable.getMyBatis3JavaMapperType());
        FullyQualifiedJavaType service = new FullyQualifiedJavaType(serviceName);
        Field field = new Field();
        field.setVisibility(JavaVisibility.PRIVATE);
        field.setType(service);
        field.addAnnotation("@Autowired");
        topLevelClass
                .addImportedType(new FullyQualifiedJavaType("org.springframework.beans.factory.annotation.Autowired"));
        // service属性名
        String name = service.getShortName().substring(0, 1).toLowerCase() + service.getShortName().substring(1);
        field.setName(name);
        topLevelClass.addField(field);
        topLevelClass.addImportedType(field.getType());
        context.getCommentGenerator().addFieldComment(field, introspectedTable);
        return name;
    }

    /**
     * 引入mapper属性
     *
     * @param topLevelClass
     * @param introspectedTable
     */
    private String setMapperField(TopLevelClass topLevelClass, IntrospectedTable introspectedTable,
                                  CommentGenerator commentGenerator) {
//		 FullyQualifiedJavaType mapper = new
//		 FullyQualifiedJavaType(introspectedTable.getMyBatis3JavaMapperType());
        FullyQualifiedJavaType mapper = new FullyQualifiedJavaType(
                introspectedTable.getAttribute("mapperName").toString());

        Field field = new Field();
        field.setVisibility(JavaVisibility.PRIVATE);
        field.setType(mapper);
        field.addAnnotation("@Autowired");
        topLevelClass
                .addImportedType(new FullyQualifiedJavaType("org.springframework.beans.factory.annotation.Autowired"));
        // mapper属性名
        String name = mapper.getShortName().substring(0, 1).toLowerCase() + mapper.getShortName().substring(1);
        field.setName(name);
        topLevelClass.addField(field);
        topLevelClass.addImportedType(field.getType());
        commentGenerator.addFieldComment(field, introspectedTable);
        return name;
    }


    /**
     * 引入transfomer属性
     *
     * @param topLevelClass
     * @param introspectedTable
     */
    private void setHelperField(TopLevelClass topLevelClass, IntrospectedTable introspectedTable,
                                CommentGenerator commentGenerator) {
        FullyQualifiedJavaType helperType = (FullyQualifiedJavaType) introspectedTable.getAttribute(ENTITY_HELPER);
        topLevelClass.addImportedType(helperType);
        Field field = new Field();
        field.setVisibility(JavaVisibility.PRIVATE);
        field.setType(helperType);
        field.addAnnotation("@Autowired");
        String filedName = helperType.getShortName().substring(0, 1).toLowerCase()
                + helperType.getShortName().substring(1);
        field.setName(filedName);
        topLevelClass.addField(field);
        introspectedTable.setAttribute("entityHelperFiledName", filedName);
        commentGenerator.addFieldComment(field, introspectedTable);
    }

    /**
     * 生成service接口
     *
     * @param introspectedTable
     * @param list
     * @return
     */
    private void getCompilationUnitsForServiceApi(IntrospectedTable introspectedTable, List<CompilationUnit> list) {
        // 设置service接口类信息
        FullyQualifiedTable table = introspectedTable.getFullyQualifiedTable();
        FullyQualifiedJavaType type = new FullyQualifiedJavaType(
                this.targetPackage + ".I" + ToolSupportUtil.getJavaFileName(table.getDomainObjectName()) + "Service");

        Interface interfaze = new Interface(type);
        interfaze.setVisibility(JavaVisibility.PUBLIC);

        // 添加java文件注释
        CommentGenerator commentGenerator = context.getCommentGenerator();
        commentGenerator.addJavaFileComment(interfaze);

        // 添加service接口类注解
        addMapperClassComment(interfaze, introspectedTable);

        // 生成删除方法
        getDeleteByPrimaryKeyFn(interfaze, introspectedTable);
        // 生成SelectByPrimaryKey方法
        getSelectByPrimaryKeyFn(interfaze, introspectedTable);
        // 生成insert方法
        getInsertFn(interfaze, introspectedTable);
        // 生成UpdateByPrimaryKey
        getUpdateByPrimaryKeyFn(interfaze, introspectedTable);

//		getQueryPageModelFn(interfaze, introspectedTable);

        if (context.getPlugins().clientGenerated(interfaze, null, introspectedTable)) {
            list.add(interfaze);
        }
    }

    /**
     * 生成delete方法
     *
     * @param introspectedTable
     * @return
     */
    private void getDeleteByPrimaryKeyFn(Interface interfaze, IntrospectedTable introspectedTable) {
        if (!introspectedTable.hasPrimaryKeyColumns()) {
            return;
        }
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);

        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.setName("deleteByPrimaryKey");

        if (introspectedTable.getRules().generatePrimaryKeyClass()) {
            FullyQualifiedJavaType type = new FullyQualifiedJavaType(introspectedTable.getPrimaryKeyType());
            importedTypes.add(type);
            method.addParameter(new Parameter(type, "key"));
        } else {
            List<IntrospectedColumn> introspectedColumns = introspectedTable.getPrimaryKeyColumns();
            for (IntrospectedColumn introspectedColumn : introspectedColumns) {
                FullyQualifiedJavaType type = introspectedColumn.getFullyQualifiedJavaType();
                importedTypes.add(type);
                Parameter parameter = new Parameter(type, introspectedColumn.getJavaProperty());
                method.addParameter(parameter);
            }
        }
        if (TableColumnUtil.exsitState) {
            method.addParameter(new Parameter(new FullyQualifiedJavaType("java.lang.String"), "updateBy"));
        }
        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
        if (context.getPlugins().clientDeleteByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable)) {
            interfaze.addImportedTypes(importedTypes);
            interfaze.addMethod(method);
        }
    }

    /**
     * 生成selectByPrimaryKey方法
     *
     * @param introspectedTable
     * @return
     */
    private void getSelectByPrimaryKeyFn(Interface interfaze, IntrospectedTable introspectedTable) {
        if (!introspectedTable.hasPrimaryKeyColumns()) {
            return;
        }
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType((FullyQualifiedJavaType) introspectedTable.getAttribute("dtoType"));
        method.setName("queryByPrimaryKey");

        if (introspectedTable.getRules().generatePrimaryKeyClass()) {
            FullyQualifiedJavaType type = new FullyQualifiedJavaType(introspectedTable.getPrimaryKeyType());
            importedTypes.add(type);
            method.addParameter(new Parameter(type, "key"));
        } else {
            List<IntrospectedColumn> introspectedColumns = introspectedTable.getPrimaryKeyColumns();
            for (IntrospectedColumn introspectedColumn : introspectedColumns) {
                FullyQualifiedJavaType type = introspectedColumn.getFullyQualifiedJavaType();
                importedTypes.add(type);
                Parameter parameter = new Parameter(type, introspectedColumn.getJavaProperty());
                method.addParameter(parameter);
            }
        }
        importedTypes.add((FullyQualifiedJavaType) introspectedTable.getAttribute("dtoType"));
        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
        if (context.getPlugins().clientDeleteByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable)) {
            interfaze.addImportedTypes(importedTypes);
            interfaze.addMethod(method);
        }
    }

    /**
     * 生成insert方法
     *
     * @param introspectedTable
     * @return
     */
    private void getInsertFn(Interface interfaze, IntrospectedTable introspectedTable) {
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.setName("create" + ToolSupportUtil.getJavaFileName(introspectedTable.getFullyQualifiedTable().getDomainObjectName()));

        FullyQualifiedJavaType type = (FullyQualifiedJavaType) introspectedTable.getAttribute("commandType");
        importedTypes.add(type);
        Parameter parameter = new Parameter(type, "reqDto");
        method.addParameter(parameter);

        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
        if (context.getPlugins().clientDeleteByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable)) {
            interfaze.addImportedTypes(importedTypes);
            interfaze.addMethod(method);
        }
    }

    /**
     * 生成updateByPrimaryKey方法
     *
     * @param introspectedTable
     * @return
     */
    private void getUpdateByPrimaryKeyFn(Interface interfaze, IntrospectedTable introspectedTable) {
        if (!introspectedTable.hasPrimaryKeyColumns()) {
            return;
        }
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.setName("updateByPrimaryKey");
        String primaryKey = introspectedTable.getPrimaryKeyColumns().get(0).getJavaProperty();
        Parameter pkIdParameter = new Parameter(FullyQualifiedJavaType.getStringInstance(), primaryKey);
        method.addParameter(pkIdParameter);
        FullyQualifiedJavaType type = (FullyQualifiedJavaType) introspectedTable.getAttribute("commandType");
        importedTypes.add(type);
        Parameter parameter = new Parameter(type, "reqDto");
        method.addParameter(parameter);

        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
        if (context.getPlugins().clientDeleteByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable)) {
            interfaze.addImportedTypes(importedTypes);
            interfaze.addMethod(method);
        }
    }

    /**
     * 实现delete方法
     *
     * @param topLevelClass
     * @param introspectedTable
     * @param mapperName
     * @return
     */
    private void implDeleteByPrimaryKeyFn(TopLevelClass topLevelClass, IntrospectedTable introspectedTable,
                                          String mapperName) {
        if (!introspectedTable.hasPrimaryKeyColumns()) {
            return;
        }
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addAnnotation("@Override");
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.setName("deleteByPrimaryKey");
        String fnParameter = "key,";
        if (introspectedTable.getRules().generatePrimaryKeyClass()) {
            FullyQualifiedJavaType type = new FullyQualifiedJavaType(introspectedTable.getPrimaryKeyType());
            importedTypes.add(type);
            method.addParameter(new Parameter(type, "key"));
        } else {
            fnParameter = "";
            List<IntrospectedColumn> introspectedColumns = introspectedTable.getPrimaryKeyColumns();
            for (IntrospectedColumn introspectedColumn : introspectedColumns) {
                FullyQualifiedJavaType type = introspectedColumn.getFullyQualifiedJavaType();
                importedTypes.add(type);
                Parameter parameter = new Parameter(type, introspectedColumn.getJavaProperty());
                method.addParameter(parameter);
                fnParameter += introspectedColumn.getJavaProperty() + ",";
            }
        }

        String primaryKey = fnParameter.substring(0, fnParameter.length() - 1);
        method.addBodyLine("log.info(\"Service deleteByPrimaryKey start. primaryKey=【{}】\"," + primaryKey + ");");
        this.judgeByPrimaryKey(topLevelClass, introspectedTable, mapperName, method);
        if (TableColumnUtil.exsitState) {
            topLevelClass.addImportedType("com.shibm.common.constant.common.CommonStatus");
            method.addParameter(new Parameter(new FullyQualifiedJavaType("java.lang.String"), "updateBy"));
            topLevelClass.addImportedType("com.shibm.param.constant.DeleteFlag");
            topLevelClass.addImportedType("com.shibm.param.constant.DeleteTableEnum");
            String serviceName = setServiceField(topLevelClass, introspectedTable,
                    "com.shibm.param.service.faces.DeleteConfigService");
            method.addBodyLine(
                    "DeleteFlag deleteFlag = " + serviceName + ".getDeleteConfig(DeleteTableEnum.在枚举类中配置表的信息);");
            method.addBodyLine("if(DeleteFlag.PHYSICS.equals(deleteFlag)) {");
            method.addBodyLine(
                    "log.warn(\"表【{}】模块【{}】主键【{}】进行了删除动作\",DeleteTableEnum.在枚举类中配置表的信息.getTableName(), DeleteTableEnum.在枚举类中配置表的信息.getModuleName(),"
                            + primaryKey + ");");
            method.addBodyLine("return this." + mapperName + ".deleteByPrimaryKey(" + primaryKey + ");");
            method.addBodyLine("} else {");
            method.addBodyLine("if(CommonStatus.DELETE.name().equals(checkEntity.getState())) {");
            method.addBodyLine("log.warn(\"要删除的主键" + primaryKey + "【{}】信息状态已经是删除\"," + primaryKey + ");");
            method.addBodyLine("return 0;");
            method.addBodyLine("}");
            method.addBodyLine("return this." + mapperName + ".updateStateByPkId(" + primaryKey
                    + ", CommonStatus.DELETE.name(), updateBy);");
            method.addBodyLine("}");
        } else {
            method.addBodyLine("int ret = this." + mapperName + ".deleteByPrimaryKey(" + primaryKey + "); ");
            method.addBodyLine("log.info(\"Service deleteByPrimaryKey end. ret=【{}】\",ret);");
            method.addBodyLine("return ret;");
        }
        topLevelClass.addMethod(method);
        topLevelClass.addImportedTypes(importedTypes);

    }

    /**
     * <pre>
     * 主键判断数据是否存在
     * </pre>
     *
     * @param topLevelClass
     * @param introspectedTable
     * @param mapperName
     * @param method
     */
    private void judgeByPrimaryKey(TopLevelClass topLevelClass, IntrospectedTable introspectedTable, String mapperName,
                                   Method method) {
        // 添加判断
        FullyQualifiedJavaType entityType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        String shortName = entityType.getShortName();
        String primaryKey = introspectedTable.getPrimaryKeyColumns().get(0).getJavaProperty();
        if ("deleteByPrimaryKey".equals(method.getName()) || "updateByPrimaryKey".equals(method.getName())) {
            method.addBodyLine(
                    shortName + " checkEntity = this." + mapperName + ".selectByPrimaryKey(" + primaryKey + ");");
        }
        method.addBodyLine("if (checkEntity == null) {");
        if ("deleteByPrimaryKey".equals(method.getName()) || "updateByPrimaryKey".equals(method.getName())) {
            method.addBodyLine("log.info(\"根据主键 " + primaryKey + "【{}】查询信息不存在\"," + primaryKey + ");");
        }
        method.addBodyLine("throw new BusiException(\"数据不存在\");");
        method.addBodyLine("}");
        topLevelClass.addImportedType(busiExceptionPackage);
        // 添加判断结束
    }

    /**
     * 实现selectByPrimaryKey方法
     *
     * @param topLevelClass
     * @param introspectedTable
     * @param mapperName
     * @return
     */
    private void implSelectByPrimaryKeyFn(TopLevelClass topLevelClass, IntrospectedTable introspectedTable,
                                          String mapperName) {
        if (!introspectedTable.hasPrimaryKeyColumns()) {
            return;
        }
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        // 参数名称
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addAnnotation("@Override");
        FullyQualifiedJavaType resDtoType = (FullyQualifiedJavaType) introspectedTable.getAttribute("dtoType");
        topLevelClass.addImportedType(resDtoType);
        method.setReturnType(resDtoType);
        method.setName("queryByPrimaryKey");

        String fnParameter = "key,";
        if (introspectedTable.getRules().generatePrimaryKeyClass()) {
            FullyQualifiedJavaType type = new FullyQualifiedJavaType(introspectedTable.getPrimaryKeyType());
            importedTypes.add(type);
            method.addParameter(new Parameter(type, "key"));
        } else {
            fnParameter = "";
            List<IntrospectedColumn> introspectedColumns = introspectedTable.getPrimaryKeyColumns();
            for (IntrospectedColumn introspectedColumn : introspectedColumns) {
                FullyQualifiedJavaType type = introspectedColumn.getFullyQualifiedJavaType();
                importedTypes.add(type);
                Parameter parameter = new Parameter(type, introspectedColumn.getJavaProperty());
                method.addParameter(parameter);
                fnParameter += introspectedColumn.getJavaProperty() + ",";
            }
        }
        FullyQualifiedJavaType fromType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        String primaryKey = fnParameter.substring(0, fnParameter.length() - 1);
        String fn = "selectByPrimaryKey";
        method.addBodyLine("log.info(\"Service selectByPrimaryKey start. primaryKey=【{}】\"," + primaryKey + ");");
        method.addBodyLine(fromType.getShortName() + " entity = this." + mapperName + "." + fn + "("
                + fnParameter.substring(0, fnParameter.length() - 1) + ");");
        if (TableColumnUtil.exsitState) {
            method.addBodyLine("if (!CommonStatus.ENABLED.name().equals(entity.getState())) {");
            method.addBodyLine("return null;");
            method.addBodyLine("}");
        }
        String filedName = (String) introspectedTable.getAttribute(ENTITY_HELPER_FILED_NAME);
        method.addBodyLine(((FullyQualifiedJavaType) introspectedTable.getAttribute("dtoType")).getShortName() + " resDto = this." + filedName + ".editResDto(entity);");
        method.addBodyLine("log.info(\"Service selectByPrimaryKey end. res=【{}】\",resDto);");
        method.addBodyLine("return resDto;");
        topLevelClass.addMethod(method);
        topLevelClass.addImportedTypes(importedTypes);
    }

    /**
     * 实现insert方法
     *
     * @param topLevelClass
     * @param introspectedTable
     * @param mapperName
     * @return
     */
    private void implInsertFn(TopLevelClass topLevelClass, IntrospectedTable introspectedTable, String mapperName) {
        // 参数名称
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addAnnotation("@Override");
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        String methodname = "create" + ToolSupportUtil.getJavaFileName(introspectedTable.getFullyQualifiedTable().getDomainObjectName());
        method.setName(methodname);

        FullyQualifiedJavaType type = (FullyQualifiedJavaType) introspectedTable.getAttribute("commandType");
        topLevelClass.addImportedType(type);
        Parameter parameter = new Parameter(type, "reqDto");
        method.addParameter(parameter);

        FullyQualifiedJavaType entity = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        method.addBodyLine("log.info(\"Service " + methodname + " start. reqDto=【{}】\",reqDto);");
        // 添加判断重复
        addJudgeUniqueConlumnMethod(topLevelClass, introspectedTable, mapperName, method);
        String filedName = (String) introspectedTable.getAttribute(ENTITY_HELPER_FILED_NAME);
        method.addBodyLine(entity.getShortName() + " entity = this." + filedName + ".editEntity(reqDto);");
        List<IntrospectedColumn> baseColumns = introspectedTable.getBaseColumns();
        baseColumns.forEach(baseColumn -> {
            if (baseColumn.getJavaProperty().equals("createTime")) {
                method.addBodyLine("entity.setCreateTime(new Date());");
                topLevelClass.addImportedType(FullyQualifiedJavaType.getDateInstance());
            }
        });
        method.addBodyLine("// TODO 添加主键");
        method.addBodyLine("int ret = this." + mapperName + ".insert(entity);");
        method.addBodyLine("log.info(\"Service " + methodname + " end. ret=【{}】\",ret);");
        method.addBodyLine("return ret;");
        topLevelClass.addMethod(method);

    }

    /**
     * <pre>
     * 检查主键是否存在
     * </pre>
     *
     * @param topLevelClass
     * @param introspectedTable
     * @param method
     * @param entity
     */
    private void checkPrimary(TopLevelClass topLevelClass, IntrospectedTable introspectedTable, Method method,
                              FullyQualifiedJavaType entity) {
        topLevelClass.addImportedType(entity);
        String primaryKey = introspectedTable.getPrimaryKeyColumns().get(0).getJavaProperty();
        String primaryKeyUpper = primaryKey.substring(0, 1).toUpperCase() + primaryKey.substring(1);
        if ("insert".equals(method.getName())) {
            method.addBodyLine("if(!StringUtils.isEmpty(reqDto.get" + primaryKeyUpper + "())) {");
            method.addBodyLine("throw new BusiException(\"参数错误," + primaryKey + "不需要\");");
        } else if ("updateByPrimaryKey".equals(method.getName()) || "deleteByPrimaryKey".equals(method.getName())) {
            method.addBodyLine("if(StringUtils.isEmpty(" + primaryKey + ")) {");
            method.addBodyLine("throw new BusiException(\"参数错误,缺少" + primaryKey + "\");");
        }
        method.addBodyLine("}");
    }

    /**
     * <pre>
     * 判断重复项
     * </pre>
     *
     * @param introspectedTable
     * @param mapperName
     * @param method
     */
    private void addJudgeUniqueConlumnMethod(TopLevelClass topLevelClass, IntrospectedTable introspectedTable,
                                             String mapperName, Method method) {
        List<String> uniqueColumnList = TableColumnUtil.tableUniqueColumnMap
                .get(introspectedTable.getTableConfiguration().getTableName());
        if (uniqueColumnList == null || uniqueColumnList.isEmpty()) {
            return;
        }
        FullyQualifiedJavaType recordType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        String[] methodNameArray = getJudgeUniqueConlumnMethodName(uniqueColumnList);
        method.addBodyLine(
                "List<" + recordType.getShortName() + "> checkList = this." + mapperName + "." + methodNameArray[0]);
        method.addBodyLine("if (!CollectionUtils.isEmpty(checkList)) {");
        topLevelClass.addImportedType("java.util.List");
        topLevelClass.addImportedType("org.springframework.util.CollectionUtils");
        String primaryKeyGetMethod = getJavaBeansGetter(introspectedTable.getPrimaryKeyColumns().get(0), context,
                introspectedTable).getName() + "()";
        if ("updateByPrimaryKey".equals(method.getName())) {
            method.addBodyLine("boolean exist = checkList.stream().anyMatch(check -> !check." + primaryKeyGetMethod
                    + ".equals(reqDto." + primaryKeyGetMethod + "));");
            method.addBodyLine("if (exist) {");
            method.addBodyLine("log.info(\"要修改的主键【{}】" + methodNameArray[1] + "已经存在了,无法修改\",reqDto."
                    + primaryKeyGetMethod + "," + methodNameArray[2] + ");");
            method.addBodyLine("throw new BusiException(\"修改后的数据已存在\");");
            method.addBodyLine("}");
        } else if ("insert".equals(method.getName())) {
            method.addBodyLine("log.info(\"新增数据已存在" + methodNameArray[1] + "\"," + methodNameArray[2] + ");");
            method.addBodyLine("throw new BusiException(\"数据已存在\");");
        }
        method.addBodyLine("}");
    }

    /**
     * 实现updateByPrimaryKey方法
     *
     * @param topLevelClass
     * @param introspectedTable
     * @param mapperName
     * @return
     */
    private void implUpdateByPrimaryKeyFn(TopLevelClass topLevelClass, IntrospectedTable introspectedTable,
                                          String mapperName) {
        if (!introspectedTable.hasPrimaryKeyColumns()) {
            return;
        }
        // 参数名称
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addAnnotation("@Override");
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.setName("updateByPrimaryKey");
        String primaryKey = introspectedTable.getPrimaryKeyColumns().get(0).getJavaProperty();
        // 添加参数
        Parameter pkIdParameter = new Parameter(FullyQualifiedJavaType.getStringInstance(), primaryKey);
        FullyQualifiedJavaType type = (FullyQualifiedJavaType) introspectedTable.getAttribute("commandType");
        topLevelClass.addImportedType(type);
        Parameter reqDtoParameter = new Parameter(type, "reqDto");
        method.addParameter(pkIdParameter);
        method.addParameter(reqDtoParameter);

        FullyQualifiedJavaType entity = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        topLevelClass.addImportedType(entity);
        method.addBodyLine("log.info(\"Service updateByPrimaryKey start. pkId=【{}】, reqDto =【{}】\",pkId,reqDto);");
        checkPrimary(topLevelClass, introspectedTable, method, entity);
        judgeByPrimaryKey(topLevelClass, introspectedTable, mapperName, method);
        addJudgeUniqueConlumnMethod(topLevelClass, introspectedTable, mapperName, method);
        String filedName = (String) introspectedTable.getAttribute(ENTITY_HELPER_FILED_NAME);
        method.addBodyLine(entity.getShortName() + " entity = this." + filedName + ".editEntity(reqDto);");
        String fn = "updateByPrimaryKey";
//        if (introspectedTable.hasBLOBColumns()) {
//            fn = "updateByPrimaryKeyWithBLOBs";
//        }
        method.addBodyLine("entity.set" + primaryKey.substring(0, 1).toUpperCase() + primaryKey.substring(1) + "(" + primaryKey + ");");
        method.addBodyLine("int ret = this." + mapperName + "." + fn + "(entity);");
        method.addBodyLine("log.info(\"Service updateByPrimaryKey end. ret=【{}】\",ret);");
        method.addBodyLine("return ret;");
        topLevelClass.addMethod(method);

    }

    /**
     * 为表对应的Service接口类添加类注释
     */
    public void addMapperClassComment(Interface interfaze, IntrospectedTable introspectedTable) {
        interfaze.addJavaDocLine("/**");
        String remarks = introspectedTable.getRemarks();
        interfaze.addJavaDocLine(" * " + remarks + " service接口类");
        interfaze.addJavaDocLine(" * @author " + author + "  " + DateUtil.getNowTime("yyyy-MM-dd HH:mm:ss"));
        interfaze.addJavaDocLine(" */");
    }

    private String[] getJudgeUniqueConlumnMethodName(List<String> uniqueColumnList) {
        String reqDtoGet = "reqDto.get";
        String[] methodArray = new String[3];
        StringBuilder methodName = new StringBuilder();
        StringBuilder methodNameParam = new StringBuilder();
        StringBuilder logParam = new StringBuilder();
        methodName.append("queryBy");
        for (int i = 0; i < uniqueColumnList.size(); i++) {
            String uniqueColumn = uniqueColumnList.get(i);
            String upperCaseColumn = uniqueColumn.substring(0, 1).toUpperCase() + uniqueColumn.substring(1);
            if (i == uniqueColumnList.size() - 1) {
                methodName.append(upperCaseColumn);
                methodNameParam.append(reqDtoGet);
                methodNameParam.append(upperCaseColumn);
                methodNameParam.append("()");
            } else {
                methodName.append(upperCaseColumn);
                methodName.append("And");
                methodNameParam.append(reqDtoGet);
                methodNameParam.append(upperCaseColumn);
                methodNameParam.append("()");
                methodNameParam.append(",");
            }
            logParam.append(uniqueColumn);
            logParam.append("【{}】");
        }
        methodArray[0] = methodName.toString() + "(" + methodNameParam.toString() + ");";
        methodArray[1] = logParam.toString();
        methodArray[2] = methodNameParam.toString();
        return methodArray;
    }
}
