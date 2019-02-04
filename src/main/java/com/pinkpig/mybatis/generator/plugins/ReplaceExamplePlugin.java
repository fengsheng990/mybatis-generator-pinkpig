package com.pinkpig.mybatis.generator.plugins;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.lang.reflect.Field;
import java.util.List;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;

import com.pinkpig.mybatis.generator.plugins.utils.XmlElementTools;

public class ReplaceExamplePlugin extends PluginAdapter {
    
    private static final String EXAMPLE = "Example";
    
    private static String replaceString;
    
    //替换字符串首字母小写
    private static String firstCharLowercaseReplaceString;

    private static boolean valid;

    public ReplaceExamplePlugin() {
    }

    @Override
    public boolean validate(List<String> warnings) {
        replaceString = properties.getProperty("replaceString"); //$NON-NLS-1$
        valid = stringHasValue(replaceString);
        if (!valid) {
            warnings.add(getString("ValidationError.18", "ReplaceExamplePlugin", 
                        "replaceString")); //$NON-NLS-1$
        }
        StringBuilder sb = new StringBuilder(replaceString.toLowerCase());
        //替换字符串首字母大写
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        replaceString = sb.toString();
        sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
        firstCharLowercaseReplaceString = sb.toString();
        return valid;
    }

    @Override
    public void initialized(IntrospectedTable introspectedTable) {
        String oldType = introspectedTable.getExampleType();
        int lastIndex = oldType.lastIndexOf(EXAMPLE);
        if(lastIndex >= 0) {
            oldType = oldType.substring(0, lastIndex) + oldType.substring(lastIndex).replaceFirst(EXAMPLE, replaceString);
        }
        introspectedTable.setExampleType(oldType);
        introspectedTable.setMyBatis3UpdateByExampleWhereClauseId(introspectedTable.getMyBatis3UpdateByExampleWhereClauseId().replace(EXAMPLE, replaceString));
        introspectedTable.setCountByExampleStatementId(introspectedTable.getCountByExampleStatementId().replace(EXAMPLE, replaceString)); //$NON-NLS-1$
        introspectedTable.setDeleteByExampleStatementId(introspectedTable.getDeleteByExampleStatementId().replace(EXAMPLE, replaceString)); //$NON-NLS-1$
        introspectedTable.setSelectByExampleStatementId(introspectedTable.getSelectByExampleStatementId().replace(EXAMPLE, replaceString)); //$NON-NLS-1$
        introspectedTable.setSelectByExampleWithBLOBsStatementId(introspectedTable.getSelectByExampleWithBLOBsStatementId().replace(EXAMPLE, replaceString)); //$NON-NLS-1$
        introspectedTable.setUpdateByExampleStatementId(introspectedTable.getUpdateByExampleStatementId().replace(EXAMPLE, replaceString)); //$NON-NLS-1$
        introspectedTable.setUpdateByExampleSelectiveStatementId(introspectedTable.getUpdateByExampleSelectiveStatementId().replace(EXAMPLE, replaceString)); //$NON-NLS-1$
        introspectedTable.setUpdateByExampleWithBLOBsStatementId(introspectedTable.getUpdateByExampleWithBLOBsStatementId().replace(EXAMPLE, replaceString)); //$NON-NLS-1$
        introspectedTable.setExampleWhereClauseId(introspectedTable.getExampleWhereClauseId().replace(EXAMPLE, replaceString)); //$NON-NLS-1$
    }

    @Override
    public boolean clientSelectByExampleWithoutBLOBsMethodGenerated(Method method, Interface interfaze,
            IntrospectedTable introspectedTable) {
        replaceMethod(method);
        return super.clientSelectByExampleWithoutBLOBsMethodGenerated(method, interfaze, introspectedTable);
    }

    @Override
    public boolean clientCountByExampleMethodGenerated(Method method, Interface interfaze,
            IntrospectedTable introspectedTable) {
        replaceMethod(method);
        return super.clientCountByExampleMethodGenerated(method, interfaze, introspectedTable);
    }

    @Override
    public boolean clientCountByExampleMethodGenerated(Method method, TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
        replaceMethod(method);
        return super.clientCountByExampleMethodGenerated(method, topLevelClass, introspectedTable);
    }

    @Override
    public boolean clientDeleteByExampleMethodGenerated(Method method, Interface interfaze,
            IntrospectedTable introspectedTable) {
        replaceMethod(method);
        return super.clientDeleteByExampleMethodGenerated(method, interfaze, introspectedTable);
    }

    @Override
    public boolean clientDeleteByExampleMethodGenerated(Method method, TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
        replaceMethod(method);
        return super.clientDeleteByExampleMethodGenerated(method, topLevelClass, introspectedTable);
    }

    @Override
    public boolean clientSelectByExampleWithBLOBsMethodGenerated(Method method, Interface interfaze,
            IntrospectedTable introspectedTable) {
        replaceMethod(method);
        return super.clientSelectByExampleWithBLOBsMethodGenerated(method, interfaze, introspectedTable);
    }

    @Override
    public boolean clientSelectByExampleWithBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
        replaceMethod(method);
        return super.clientSelectByExampleWithBLOBsMethodGenerated(method, topLevelClass, introspectedTable);
    }

    @Override
    public boolean clientSelectByExampleWithoutBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
        replaceMethod(method);
        return super.clientSelectByExampleWithoutBLOBsMethodGenerated(method, topLevelClass, introspectedTable);
    }

    @Override
    public boolean clientUpdateByExampleSelectiveMethodGenerated(Method method, Interface interfaze,
            IntrospectedTable introspectedTable) {
        replaceMethod(method);
        return super.clientUpdateByExampleSelectiveMethodGenerated(method, interfaze, introspectedTable);
    }

    @Override
    public boolean clientUpdateByExampleSelectiveMethodGenerated(Method method, TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
        replaceMethod(method);
        return super.clientUpdateByExampleSelectiveMethodGenerated(method, topLevelClass, introspectedTable);
    }

    @Override
    public boolean clientUpdateByExampleWithBLOBsMethodGenerated(Method method, Interface interfaze,
            IntrospectedTable introspectedTable) {
        replaceMethod(method);
        return super.clientUpdateByExampleWithBLOBsMethodGenerated(method, interfaze, introspectedTable);
    }

    @Override
    public boolean clientUpdateByExampleWithBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
        replaceMethod(method);
        return super.clientUpdateByExampleWithBLOBsMethodGenerated(method, topLevelClass, introspectedTable);
    }

    @Override
    public boolean clientUpdateByExampleWithoutBLOBsMethodGenerated(Method method, Interface interfaze,
            IntrospectedTable introspectedTable) {
        replaceMethod(method);
        return super.clientUpdateByExampleWithoutBLOBsMethodGenerated(method, interfaze, introspectedTable);
    }

    @Override
    public boolean clientUpdateByExampleWithoutBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
        replaceMethod(method);
        return super.clientUpdateByExampleWithoutBLOBsMethodGenerated(method, topLevelClass, introspectedTable);
    }

    @Override
    public boolean sqlMapExampleWhereClauseElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        XmlElement foreachElement = XmlElementTools.getXmlElement(XmlElementTools.getXmlElement(element, "where"), "foreach");
        Attribute attribute = XmlElementTools.getAttribute(foreachElement, "collection");
        Field valueField;
        try {
            valueField = attribute.getClass().getDeclaredField("value");
            valueField.setAccessible(true);
            valueField.set(attribute, attribute.getValue().replace(EXAMPLE.toLowerCase(), firstCharLowercaseReplaceString));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return super.sqlMapExampleWhereClauseElementGenerated(element, introspectedTable);
    }

    public static void replaceMethod(Method method) {
        if(valid){
            for(Parameter parameter : method.getParameters()) {
                try {
                    if(parameter.getName().equals(EXAMPLE.toLowerCase())){
                        String nameValue = firstCharLowercaseReplaceString;
                        Field nameField = parameter.getClass().getDeclaredField("name");
                        nameField.setAccessible(true);
                        nameField.set(parameter, nameValue);
                    }
                    List<String> annotations = parameter.getAnnotations();
                    if(null!=annotations && annotations.size()>0) {
                        for(int i=0; i<annotations.size() ;i++) {
                            if(annotations.get(i).contains(EXAMPLE.toLowerCase())) {
                                annotations.set(i, annotations.get(i).replace(EXAMPLE.toLowerCase(), firstCharLowercaseReplaceString));
                            }
                        }
                    }
                } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getParamterName(){
        if(valid && null!=firstCharLowercaseReplaceString && ""!=firstCharLowercaseReplaceString){
            return firstCharLowercaseReplaceString;
        }
        return EXAMPLE.toLowerCase();
    }
}
