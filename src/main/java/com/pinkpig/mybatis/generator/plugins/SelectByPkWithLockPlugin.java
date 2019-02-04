package com.pinkpig.mybatis.generator.plugins;

import java.util.List;

import com.pinkpig.mybatis.generator.plugins.utils.FormatTools;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

import com.pinkpig.mybatis.generator.api.PluginInternalAttribute;
import com.pinkpig.mybatis.generator.xmlmapper.elements.SelectByPkWithLockElementGenerator;

public class SelectByPkWithLockPlugin extends PluginAdapter {

    private Method lockMethod;

    private AbstractXmlElementGenerator selectByPkWithLockElementGenerator;

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean clientSelectByPrimaryKeyMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
         lockMethod = new Method();
         lockMethod.setName(PluginInternalAttribute.ATTR_METHOD_SELECT_BY_PK_WHITH_LOCK.getValue());
         lockMethod.setVisibility(JavaVisibility.PUBLIC);
         lockMethod.setReturnType(method.getReturnType());
         for(Parameter parameter : method.getParameters())
             lockMethod.addParameter(parameter);
         context.getCommentGenerator().addGeneralMethodComment(lockMethod, introspectedTable);
        return super.clientSelectByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable);
    }

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        if (lockMethod != null) {
            FormatTools.addMethodWithBestPosition(interfaze, lockMethod);
        }
        return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
    }


    @Override
    public boolean sqlMapSelectByPrimaryKeyElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        selectByPkWithLockElementGenerator = new SelectByPkWithLockElementGenerator();
        selectByPkWithLockElementGenerator.setContext(context);
        selectByPkWithLockElementGenerator.setIntrospectedTable(introspectedTable);
        selectByPkWithLockElementGenerator.setProgressCallback(null);
        selectByPkWithLockElementGenerator.setWarnings(null);
        return super.sqlMapSelectByPrimaryKeyElementGenerated(element, introspectedTable);
    }

    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        if(null != selectByPkWithLockElementGenerator){
            selectByPkWithLockElementGenerator.addElements(document.getRootElement());
        }
        return super.sqlMapDocumentGenerated(document, introspectedTable);
    }
}
