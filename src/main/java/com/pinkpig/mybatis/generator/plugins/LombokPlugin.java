package com.pinkpig.mybatis.generator.plugins;

import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;

public class LombokPlugin extends PluginAdapter {

    private final String ANNOTATION_DATA = "@Data";
    private final String ANNOTATION_DATA_JAVA_TYPE = "lombok.Data";
    
    private final String ANNOTATION_BUILDER = "@Builder";
    private final String ANNOTATION_BUILDER_JAVA_TYPE = "lombok.Builder";
    
    private final String ANNOTATION_SUPERBUILDER = "@SuperBuilder";
    private final String ANNOTATION_SUPERBUILDER_JAVA_TYPE = "lombok.experimental.SuperBuilder";
    
    private final String ANNOTATION_NOARGSCONSTRUCTOR = "@NoArgsConstructor";
    private final String ANNOTATION_NOARGSCONSTRUCTOR_JAVA_TYPE = "lombok.NoArgsConstructor";
    
    private final String ANNOTATION_ALLARGSCONSTRUCTOR = "@AllArgsConstructor";
    private final String ANNOTATION_ALLARGSCONSTRUCTOR_JAVA_TYPE = "lombok.AllArgsConstructor";
    
    private final String ANNOTATION_EQUALSANDHASHCODE = "@EqualsAndHashCode(callSuper = true)";
    private final String ANNOTATION_EQUALSANDHASHCODE_JAVA_TYPE = "lombok.EqualsAndHashCode";
    
    private final String ANNOTATION_TOSTRING = "@ToString(callSuper = true)";
    private final String ANNOTATION_TOSTRING_JAVA_TYPE = "lombok.ToString";
    
    private boolean isData;
    
    private boolean isBuilder;
    
    private boolean isNoArgsConstructor;
    
    private boolean isAllArgsConstructor;
    
    @Override
    public void initialized(IntrospectedTable introspectedTable) {
        isData = Boolean.valueOf(properties.getProperty(ANNOTATION_DATA));
        isBuilder = Boolean.valueOf(properties.getProperty(ANNOTATION_BUILDER));
        isNoArgsConstructor = Boolean.valueOf(properties.getProperty(ANNOTATION_NOARGSCONSTRUCTOR));
        isAllArgsConstructor = Boolean.valueOf(properties.getProperty(ANNOTATION_ALLARGSCONSTRUCTOR));
        super.initialized(introspectedTable);
    }

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }
    
    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        addAnnotation(topLevelClass, false);
        return super.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);
    }

    @Override
    public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        addAnnotation(topLevelClass, true);
        return super.modelPrimaryKeyClassGenerated(topLevelClass, introspectedTable);
    }

    @Override
    public boolean modelRecordWithBLOBsClassGenerated(TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
        addAnnotation(topLevelClass, false);
        return super.modelRecordWithBLOBsClassGenerated(topLevelClass, introspectedTable);
    }

    @Override
    public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass,
            IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        if(isData) {
            return false;
        }
        return super.modelGetterMethodGenerated(method, topLevelClass, introspectedColumn, introspectedTable, modelClassType);
    }

    @Override
    public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass,
            IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        if(isData) {
            return false;
        }
        return super.modelSetterMethodGenerated(method, topLevelClass, introspectedColumn, introspectedTable, modelClassType);
    }

    private void addAnnotation(TopLevelClass topLevelClass, boolean modelPk) {
        if(isData) {
            topLevelClass.addAnnotation(ANNOTATION_DATA);
            topLevelClass.addImportedType(new FullyQualifiedJavaType(ANNOTATION_DATA_JAVA_TYPE));
        }
        if(isBuilder) {
            if(null != topLevelClass.getSuperClass() || modelPk) {
                topLevelClass.addAnnotation(ANNOTATION_SUPERBUILDER);
                topLevelClass.addImportedType(new FullyQualifiedJavaType(ANNOTATION_SUPERBUILDER_JAVA_TYPE));
            }else {
                topLevelClass.addAnnotation(ANNOTATION_BUILDER);
                topLevelClass.addImportedType(new FullyQualifiedJavaType(ANNOTATION_BUILDER_JAVA_TYPE));
            }
        }
        if(isNoArgsConstructor) {
            topLevelClass.addAnnotation(ANNOTATION_NOARGSCONSTRUCTOR);
            topLevelClass.addImportedType(new FullyQualifiedJavaType(ANNOTATION_NOARGSCONSTRUCTOR_JAVA_TYPE));
        }
        if(isAllArgsConstructor) {
            topLevelClass.addAnnotation(ANNOTATION_ALLARGSCONSTRUCTOR);
            topLevelClass.addImportedType(new FullyQualifiedJavaType(ANNOTATION_ALLARGSCONSTRUCTOR_JAVA_TYPE));
        }
        if(null != topLevelClass.getSuperClass()) {
            topLevelClass.addAnnotation(ANNOTATION_EQUALSANDHASHCODE);
            topLevelClass.addImportedType(new FullyQualifiedJavaType(ANNOTATION_EQUALSANDHASHCODE_JAVA_TYPE));
            
            topLevelClass.addAnnotation(ANNOTATION_TOSTRING);
            topLevelClass.addImportedType(new FullyQualifiedJavaType(ANNOTATION_TOSTRING_JAVA_TYPE));
        }
    }

}
