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

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

/**
 * 生成Controller
 */
public class ControllerGeneratorPlugin extends PluginAdapter {

    private ShellCallback shellCallback = null;

    private String targetDir;

    private String targetPackage;

    private String servicePackage;
    private String responseDataPackage;

    /**
     * 获取操作人
     */
    private String author = System.getProperty("user.name");

    /**
     * 是否覆盖标识
     */
    private boolean isOverride = false;

    public ControllerGeneratorPlugin() {
        System.out.println("Controller生成开始 ");
        shellCallback = new DefaultShellCallback(false);
    }

    @Override
    public boolean validate(List<String> warnings) {
        System.out.println("Controller生成必须参数校验开始 ");
        targetDir = properties.getProperty("targetDir");
        boolean valid = stringHasValue(targetDir);

        targetPackage = properties.getProperty("targetPackage");
        boolean valid2 = stringHasValue(targetPackage);

        servicePackage = properties.getProperty("servicePackage");
        boolean valid3 = stringHasValue(servicePackage);

        responseDataPackage = properties.getProperty("responseDataPackage");

        String isOverrideFlag = properties.getProperty("isOverride");
        if (stringHasValue(isOverrideFlag)) {
            isOverride = Boolean.valueOf(isOverrideFlag);
        }
        boolean check = valid && valid2 && valid3;
        if (!check) {
            System.out.println("Controller生成必须参数缺少.");
        } else {
            System.out.println("Controller生成必须参数校验通过");
        }
        return check;
    }

    /**
     * 生成Controller文件
     */
    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        System.out.println("------ 开始: 生成Controller类文件 ------");

        List<GeneratedJavaFile> mapperJavaFiles = new ArrayList<GeneratedJavaFile>();
        List<CompilationUnit> listApi = new ArrayList<>();
        // 生成Controller接口
        getCompilationUnitsForController(introspectedTable, listApi);
        // 实现controller接口
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

        } catch (ShellException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        System.out.println("------ 结束: Controller类文件 ------");
        return mapperJavaFiles;
    }

    /**
     * 为表对应的Service接口类添加类注释
     */
    public void addMapperClassComment(TopLevelClass interfaze, IntrospectedTable introspectedTable) {
        interfaze.addJavaDocLine("/**");
        String remarks = introspectedTable.getRemarks();
        interfaze.addJavaDocLine(" * " + remarks + " controller接口类");
        interfaze.addJavaDocLine(" * @author " + author + "  " + DateUtil.getNowTime("yyyy-MM-dd HH:mm:ss"));
        interfaze.addJavaDocLine(" */");
    }

    /**
     * 实现delete方法
     *
     * @param topLevelClass
     * @param introspectedTable
     * @return
     */
    private void controllerDeleteByPrimaryKeyFn(TopLevelClass topLevelClass, IntrospectedTable introspectedTable,
                                                String serviceName) {
        if (!introspectedTable.hasPrimaryKeyColumns()) {
            return;
        }
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("deleteByPrimaryKey");
        FullyQualifiedJavaType returnType = new FullyQualifiedJavaType(responseDataPackage);
        returnType.addTypeArgument(new FullyQualifiedJavaType("java.lang.Integer"));
        method.setReturnType(returnType);
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
                parameter.addAnnotation("@PathVariable");
                fnParameter += introspectedColumn.getJavaProperty() + ",";
            }
        }
        // 添加注解
        String param = fnParameter.substring(0, fnParameter.length() - 1);
        method.addAnnotation("@ApiOperation(value = \"删除" + introspectedTable.getRemarks() + "\")");
        method.addAnnotation("@DeleteMapping(value = \"/{" + param + "}\")");
        method.addBodyLine("log.info(\"Controller deleteByPrimaryKey start.\");");
        if (TableColumnUtil.exsitState) {
            method.addBodyLine(
                    "this." + serviceName + ".deleteByPrimaryKey(" + param + ",UserContextHolder.getUserId());");
        } else {

            method.addBodyLine("int result = this." + serviceName + ".deleteByPrimaryKey(" + param + ");");
            method.addBodyLine("log.info(\"Controller deleteByPrimaryKey end.\");");
            method.addBodyLine("return new ResponseData<>(result);");
        }

        topLevelClass.addMethod(method);
        topLevelClass.addImportedTypes(importedTypes);
        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
    }

    /**
     * 实现selectByPrimaryKey方法
     *
     * @param topLevelClass
     * @param introspectedTable
     * @return
     */
    private void controllerSelectByPrimaryKeyFn(TopLevelClass topLevelClass, IntrospectedTable introspectedTable,
                                                String serviceName) {
        if (!introspectedTable.hasPrimaryKeyColumns()) {
            return;
        }
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        // 参数名称
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);

        FullyQualifiedJavaType returnType = new FullyQualifiedJavaType(responseDataPackage);
        FullyQualifiedJavaType dtoType = (FullyQualifiedJavaType) introspectedTable.getAttribute("dtoType");
        returnType.addTypeArgument(dtoType);
        method.setReturnType(returnType);
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
                parameter.addAnnotation("@PathVariable");
                fnParameter += introspectedColumn.getJavaProperty() + ",";
            }
        }

        String fn = "queryByPrimaryKey";
        String parmaName = fnParameter.substring(0, fnParameter.length() - 1);
        method.addBodyLine("log.info(\"Controller queryByPrimaryKey start.\");");
        method.addBodyLine(dtoType.getShortName() + " resDto =  this." + serviceName + "." + fn + "(" + parmaName + ");");
        // return new ResponseData<>(retDto);
        method.addBodyLine("log.info(\"Controller queryByPrimaryKey end.\");");
        method.addBodyLine("return new ResponseData<>(resDto);");
        topLevelClass.addMethod(method);
        topLevelClass.addImportedTypes(importedTypes);
        topLevelClass.addImportedType((FullyQualifiedJavaType) introspectedTable.getAttribute("dtoType"));
        // 添加注解
        method.addAnnotation("@ApiOperation(value = \"根据主键查询" + introspectedTable.getRemarks() + "\")");
        method.addAnnotation("@GetMapping(value = \"/{" + parmaName + "}\")");
        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
    }

    /**
     * 实现insert方法
     *
     * @param topLevelClass
     * @param introspectedTable
     * @return
     */
    private void controllerInsertFn(TopLevelClass topLevelClass, IntrospectedTable introspectedTable,
                                    String serviceName) {
        // 参数名称
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        FullyQualifiedJavaType returnType = new FullyQualifiedJavaType(responseDataPackage);
        returnType.addTypeArgument(new FullyQualifiedJavaType("java.lang.Integer"));
        method.setReturnType(returnType);
        // 添加注解
        method.addAnnotation("@ApiOperation(value = \"新增" + introspectedTable.getRemarks() + "\")");
        method.addAnnotation("@PostMapping(value = \"\")");
        String methodName = "create" + ToolSupportUtil.getJavaFileName(introspectedTable.getFullyQualifiedTable().getDomainObjectName());
        method.setName(methodName);
        FullyQualifiedJavaType type = (FullyQualifiedJavaType) introspectedTable.getAttribute("commandType");
        topLevelClass.addImportedType(type);
        Parameter parameter = new Parameter(type, "reqDto");
        parameter.addAnnotation("@RequestBody");
        parameter.addAnnotation("@Valid");
        method.addParameter(parameter);
        method.addBodyLine("log.info(\"Controller queryByPrimaryKey start.\");");
        method.addBodyLine("int result = this." + serviceName + "." + methodName + "(reqDto);");
        method.addBodyLine("log.info(\"Controller queryByPrimaryKey end.\");");
        method.addBodyLine("return new ResponseData<>(result);");
        topLevelClass.addMethod(method);
        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
    }

    /**
     * 实现updateByPrimaryKey方法
     *
     * @param topLevelClass
     * @param introspectedTable
     * @return
     */
    private void controllerUpdateByPrimaryKeyFn(TopLevelClass topLevelClass, IntrospectedTable introspectedTable,
                                                String serviceName) {
        if (!introspectedTable.hasPrimaryKeyColumns()) {
            return;
        }
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        // 参数名称
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("updateByPrimaryKey");
        FullyQualifiedJavaType returnType = new FullyQualifiedJavaType(responseDataPackage);
        returnType.addTypeArgument(new FullyQualifiedJavaType("java.lang.Integer"));
        method.setReturnType(returnType);
        FullyQualifiedJavaType type = (FullyQualifiedJavaType) introspectedTable.getAttribute("commandType");
        topLevelClass.addImportedType(type);

        String fnParameter = "key,";
        if (introspectedTable.getRules().generatePrimaryKeyClass()) {
            FullyQualifiedJavaType typePri = new FullyQualifiedJavaType(introspectedTable.getPrimaryKeyType());
            importedTypes.add(typePri);
            method.addParameter(new Parameter(typePri, "key"));
        } else {
            fnParameter = "";
            List<IntrospectedColumn> introspectedColumns = introspectedTable.getPrimaryKeyColumns();
            for (IntrospectedColumn introspectedColumn : introspectedColumns) {
                FullyQualifiedJavaType typePri = introspectedColumn.getFullyQualifiedJavaType();
                importedTypes.add(typePri);
                Parameter parameter = new Parameter(typePri, introspectedColumn.getJavaProperty());
                method.addParameter(parameter);
                parameter.addAnnotation("@PathVariable");
                fnParameter += introspectedColumn.getJavaProperty() + ",";
            }
        }
        String parmaName = fnParameter.substring(0, fnParameter.length() - 1);
        Parameter parameter = new Parameter(type, "reqDto");
        parameter.addAnnotation("@RequestBody");
        parameter.addAnnotation("@Valid");
        method.addParameter(parameter);
        /**
         * 方法体
         */
        method.addBodyLine("log.info(\"Controller updateByPrimaryKey start.\");");
        method.addBodyLine("int result = this." + serviceName + ".updateByPrimaryKey(" + parmaName + ", reqDto);");
        method.addBodyLine("log.info(\"Controller updateByPrimaryKey end.\");");
        method.addBodyLine("return new ResponseData<>(result);");
        topLevelClass.addMethod(method);
        // 添加注解
        method.addAnnotation("@ApiOperation(value = \"修改" + introspectedTable.getRemarks() + "\")");
        method.addAnnotation("@PutMapping(value = \"/{" + parmaName + "}\")");
        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
    }

    /**
     * 生成Controller
     *
     * @param introspectedTable
     * @param list
     * @return
     */
    private void getCompilationUnitsForController(IntrospectedTable introspectedTable, List<CompilationUnit> list) {
        // 设置servcie实现类信息
        FullyQualifiedTable table = introspectedTable.getFullyQualifiedTable();
        String javaFileName = ToolSupportUtil.getJavaFileName(table.getDomainObjectName());
        FullyQualifiedJavaType type = new FullyQualifiedJavaType(
                this.targetPackage + "." + javaFileName + "Controller");
        TopLevelClass topLevelClass = new TopLevelClass(type);
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        // 添加java文件注释
        CommentGenerator commentGenerator = context.getCommentGenerator();
        commentGenerator.addJavaFileComment(topLevelClass);
        // 添加RestController注解
        topLevelClass.addAnnotation("@RestController");
        topLevelClass
                .addImportedType(new FullyQualifiedJavaType("org.springframework.web.bind.annotation.RestController"));
        // 添加日志
        topLevelClass.addAnnotation("@Slf4j");
        topLevelClass.addImportedType("lombok.extern.slf4j.Slf4j");
        // 添加swaager API注解
		/*topLevelClass.addAnnotation("@Api(description=\"" + introspectedTable.getRemarks()
				+ "接口\",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)");*/
        //修改swagger注解为tags
        topLevelClass.addAnnotation("@Api(tags=\"" + introspectedTable.getRemarks() + "接口\")");
        topLevelClass.addImportedType("io.swagger.annotations.Api");
        topLevelClass.addImportedType("org.springframework.http.MediaType");
        // 添加requestMapping
        topLevelClass.addImportedType("org.springframework.web.bind.annotation.RequestMapping");
        String mappingName = javaFileName.substring(0, 1)
                .toLowerCase() + javaFileName.substring(1);
        topLevelClass.addAnnotation("@RequestMapping(value = \"" + mappingName + "\")");
        // 导入HTTP请求相关包
        topLevelClass.addImportedType("io.swagger.annotations.ApiOperation");
        topLevelClass.addImportedType("org.springframework.web.bind.annotation.PostMapping");
        topLevelClass.addImportedType("org.springframework.web.bind.annotation.GetMapping");
        topLevelClass.addImportedType("org.springframework.web.bind.annotation.PutMapping");
        topLevelClass.addImportedType("org.springframework.web.bind.annotation.DeleteMapping");
        topLevelClass.addImportedType("org.springframework.web.bind.annotation.PathVariable");
        topLevelClass.addImportedType("org.springframework.web.bind.annotation.RequestBody");
        topLevelClass.addImportedType("javax.validation.Valid");
        topLevelClass.addImportedType(responseDataPackage);
//		topLevelClass.addImportedType("com.shibm.common.user.UserContextHolder");

        // 添加类注释
        commentGenerator.addModelClassComment(topLevelClass, introspectedTable);

        // service接口
        FullyQualifiedJavaType service = new FullyQualifiedJavaType(
                this.servicePackage + ".I" + javaFileName + "Service");
        topLevelClass.addImportedType(service);
        // 添加service属性字段
        String serviceName = setServiceField(service, topLevelClass, introspectedTable, commentGenerator);

        // 实现删除方法
        controllerDeleteByPrimaryKeyFn(topLevelClass, introspectedTable, serviceName);
        // 实现SelectByPrimaryKey方法
        controllerSelectByPrimaryKeyFn(topLevelClass, introspectedTable, serviceName);
        // 实现insert方法
        controllerInsertFn(topLevelClass, introspectedTable, serviceName);
        // 实现UpdateByPrimaryKey方法
        controllerUpdateByPrimaryKeyFn(topLevelClass, introspectedTable, serviceName);
        // 实现implQueryPageModelFn方法
//		controllerQueryPageModelFn(topLevelClass, introspectedTable, serviceName);

        if (context.getPlugins().modelBaseRecordClassGenerated(topLevelClass, introspectedTable)) {
            list.add(topLevelClass);
        }
    }

    /**
     * 引入service属性
     *
     * @param topLevelClass
     * @param introspectedTable
     * @param commentGenerator
     */
    private String setServiceField(FullyQualifiedJavaType service, TopLevelClass topLevelClass,
                                   IntrospectedTable introspectedTable, CommentGenerator commentGenerator) {
        Field field = new Field();
        field.setVisibility(JavaVisibility.PRIVATE);
        field.setType(service);
        field.addAnnotation("@Autowired");
        topLevelClass
                .addImportedType(new FullyQualifiedJavaType("org.springframework.beans.factory.annotation.Autowired"));
        // mapper属性名
        String name = service.getShortName().substring(0, 1).toLowerCase() + service.getShortName().substring(1);
        field.setName(name);
        topLevelClass.addField(field);
        topLevelClass.addImportedType(service);
        commentGenerator.addFieldComment(field, introspectedTable);
        return name;
    }

}
