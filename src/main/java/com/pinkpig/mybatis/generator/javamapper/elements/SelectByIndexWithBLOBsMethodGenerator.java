package com.pinkpig.mybatis.generator.javamapper.elements;

import java.util.Set;
import java.util.TreeSet;

import com.pinkpig.mybatis.generator.plugins.utils.FormatTools;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.AbstractJavaMapperMethodGenerator;

import com.pinkpig.mybatis.generator.api.IntrospectedIndex;
import com.pinkpig.mybatis.generator.api.PluginInternalAttribute;

/**
 * 
 * <p>Title: SelectByUkIndexMethodGenerator.java</p>
 * <p>Description: TODO</p>
 *
 * @author vincenfeng
 * @date 2019年1月24日 下午12:43:20
 * @version V1.0
 */
public class SelectByIndexWithBLOBsMethodGenerator extends
        AbstractJavaMapperMethodGenerator {

    private IntrospectedIndex introspectedIndex;
    
    public SelectByIndexWithBLOBsMethodGenerator(IntrospectedIndex introspectedIndex) {
        super();
        this.introspectedIndex = introspectedIndex;
    }

    @Override
    public void addInterfaceElements(Interface interfaze) {
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);

        FullyQualifiedJavaType returnType = FullyQualifiedJavaType.getNewListInstance();
        returnType.addTypeArgument(new FullyQualifiedJavaType(introspectedTable.getRecordWithBLOBsType()));
        method.setReturnType(returnType);

        method.setName(String.format(PluginInternalAttribute.ATTR_METHOD_SELECT_BY_INDEX.getValue(), introspectedIndex.getCamelIndexName()));
        for(IntrospectedColumn column : introspectedIndex.getIndexColumns()) {
            FullyQualifiedJavaType type = column.getFullyQualifiedJavaType();
            Parameter parameter = new Parameter(type, column.getJavaProperty(), String.format("@Param(\"%s\")",  column.getJavaProperty()));
            method.addParameter(parameter);
        }
        addMapperAnnotations(interfaze, method);
        context.getCommentGenerator().addGeneralMethodComment(method,
                introspectedTable);
        FormatTools.addMethodWithBestPosition(interfaze, method);
    }
    
    public IntrospectedIndex getIntrospectedIndex() {
        return introspectedIndex;
    }

    public void addMapperAnnotations(Interface interfaze, Method method) {
    }

    public void addExtraImports(Interface interfaze) {
    }
}
