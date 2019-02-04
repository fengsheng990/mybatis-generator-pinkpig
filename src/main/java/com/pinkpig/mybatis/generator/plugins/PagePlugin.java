package com.pinkpig.mybatis.generator.plugins;

import com.pinkpig.mybatis.generator.plugins.utils.FormatTools;
import com.pinkpig.mybatis.generator.xmlmapper.elements.SelectByExamplePageWithBLOBsElementGenerator;
import com.pinkpig.mybatis.generator.xmlmapper.elements.SelectByExamplePageWithoutBLOBsElementGenerator;
import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.IntrospectedTable.TargetRuntime;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 物理分页插件
 */
public class PagePlugin extends PluginAdapter {

    private FullyQualifiedJavaType pageType =
            new FullyQualifiedJavaType("com.pinkpig.mybatis.component.SimplePage"); //$NON-NLS-1$

    private Map<FullyQualifiedTable, List<Method>> methodMap =
            new HashMap<FullyQualifiedTable, List<Method>>();

    private Map<FullyQualifiedTable, List<AbstractXmlElementGenerator>> elementGeneratorMap =
            new HashMap<FullyQualifiedTable, List<AbstractXmlElementGenerator>>();

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean clientSelectByExampleWithBLOBsMethodGenerated(Method method,
            Interface interfaze, IntrospectedTable introspectedTable) {
        if (introspectedTable.getTargetRuntime() == TargetRuntime.MYBATIS3) {
            addMethod(method, interfaze, introspectedTable);
        }
        return true;
    }

    @Override
    public boolean clientSelectByExampleWithoutBLOBsMethodGenerated(
            Method method, Interface interfaze,
            IntrospectedTable introspectedTable) {
        if (introspectedTable.getTargetRuntime() == TargetRuntime.MYBATIS3) {
            addMethod(method, interfaze, introspectedTable);
        }
        return true;
    }

    @Override
    public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(
            XmlElement element, IntrospectedTable introspectedTable) {
        if (introspectedTable.getTargetRuntime() == TargetRuntime.MYBATIS3) {
            addElementGenerator(new SelectByExamplePageWithoutBLOBsElementGenerator(), introspectedTable);
        }
        return true;
    }



    @Override
    public boolean sqlMapSelectByExampleWithBLOBsElementGenerated(
            XmlElement element, IntrospectedTable introspectedTable) {
        if (introspectedTable.getTargetRuntime() == TargetRuntime.MYBATIS3) {
            addElementGenerator(new SelectByExamplePageWithBLOBsElementGenerator(), introspectedTable);
        }
        return true;
    }

    @Override
    public boolean sqlMapDocumentGenerated(Document document,
            IntrospectedTable introspectedTable) {
        List<AbstractXmlElementGenerator> elementGenerators = elementGeneratorMap.get(introspectedTable.getFullyQualifiedTable());
        if (elementGenerators != null) {
            for (AbstractXmlElementGenerator elementGenerator : elementGenerators) {
                elementGenerator.setContext(context);
                elementGenerator.setIntrospectedTable(introspectedTable);
                elementGenerator.setProgressCallback(null);
                elementGenerator.setWarnings(null);
                elementGenerator.addElements(document.getRootElement());
            }
        }
        return super.sqlMapDocumentGenerated(document, introspectedTable);
    }

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        List<Method> methods = methodMap.get(introspectedTable.getFullyQualifiedTable());
        if (methods != null) {
            for (Method method : methods) {
                FormatTools.addMethodWithBestPosition(interfaze, method);
            }
        }
        return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
    }

    private void addMethod(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        Method newMethod = new Method(method);
        newMethod.setName(method.getName() + "WithPage"); //$NON-NLS-1$
        newMethod.addParameter(new Parameter(pageType, "page", "@Param(\"page\")")); //$NON-NLS-1$
        //重命名方法
        ReplaceExamplePlugin.replaceMethod(newMethod);
        Parameter exampleParamter = method.getParameters().get(0);
        Parameter newExampleParamter = new Parameter(exampleParamter.getType(), exampleParamter.getName(),
                "@Param(\""+exampleParamter.getName()+"\")");
        newMethod.getParameters().set(0, newExampleParamter);
        if(methodMap.containsKey(introspectedTable.getFullyQualifiedTable())){
            methodMap.get(introspectedTable.getFullyQualifiedTable()).add(newMethod);
        }else{
            List<Method> methods = new ArrayList<>();
            methods.add(newMethod);
            methodMap.put(introspectedTable.getFullyQualifiedTable(), methods);
        }
    }

    public void addElementGenerator(AbstractXmlElementGenerator elementGenerator, IntrospectedTable introspectedTable){
        if(elementGeneratorMap.containsKey(introspectedTable.getFullyQualifiedTable())){
            elementGeneratorMap.get(introspectedTable.getFullyQualifiedTable()).add(elementGenerator);
        }else{
            List<AbstractXmlElementGenerator> elementGenerators = new ArrayList<>();
            elementGenerators.add(elementGenerator);
            elementGeneratorMap.put(introspectedTable.getFullyQualifiedTable(), elementGenerators);
        }
    }

}
