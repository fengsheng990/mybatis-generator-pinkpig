package com.pinkpig.mybatis.generator.plugins;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.AbstractJavaMapperMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;
import org.mybatis.generator.internal.util.JavaBeansUtil;

import com.pinkpig.mybatis.generator.api.IntrospectedIndex;
import com.pinkpig.mybatis.generator.api.PluginContext;
import com.pinkpig.mybatis.generator.javamapper.elements.SelectByIndexWithBLOBsMethodGenerator;
import com.pinkpig.mybatis.generator.javamapper.elements.SelectByIndexWithoutBLOBsMethodGenerator;
import com.pinkpig.mybatis.generator.javamapper.elements.SelectByUkIndexMethodGenerator;
import com.pinkpig.mybatis.generator.xmlmapper.elements.SelectByIndexWithBLOBsElementGenerator;
import com.pinkpig.mybatis.generator.xmlmapper.elements.SelectByIndexWithoutBLOBsElementGenerator;
import com.pinkpig.mybatis.generator.xmlmapper.elements.SelectByUkIndexElementGenerator;

/**
 * 根据索引生成select方法插件
 * <p>Title: SelectByIndexPlugin.java</p>
 * <p>Description: TODO</p>
 *
 * @author vincenfeng
 * @date 2019年1月24日 下午2:23:00
 * @version V1.0
 */
public class SelectByIndexPlugin extends PluginAdapter {

    private Map<String, List<IntrospectedIndex>> tableIndex = new HashMap<>();

    private Map<FullyQualifiedTable, List<AbstractJavaMapperMethodGenerator>> methodGeneratorMap =
            new HashMap<FullyQualifiedTable, List<AbstractJavaMapperMethodGenerator>>();

    private Map<FullyQualifiedTable, List<AbstractXmlElementGenerator>> elementGeneratorMap =
            new HashMap<FullyQualifiedTable, List<AbstractXmlElementGenerator>>();

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }
    
    @Override
    public boolean clientSelectByExampleWithBLOBsMethodGenerated(Method method, Interface interfaze,
            IntrospectedTable introspectedTable) {
        calculationIndex(introspectedTable);
        indexMethodGenerator(interfaze, introspectedTable, true);
        return super.clientSelectByExampleWithBLOBsMethodGenerated(method, interfaze, introspectedTable);
    }

    @Override
    public boolean clientSelectByExampleWithoutBLOBsMethodGenerated(Method method, Interface interfaze,
            IntrospectedTable introspectedTable) {
        calculationIndex(introspectedTable);
        indexMethodGenerator(interfaze, introspectedTable, false);
        return super.clientSelectByExampleWithoutBLOBsMethodGenerated(method, interfaze, introspectedTable);
    }

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        List<AbstractJavaMapperMethodGenerator> methodGenerators = methodGeneratorMap.get(introspectedTable.getFullyQualifiedTable());
        if (methodGenerators != null) {
            for (AbstractJavaMapperMethodGenerator methodGenerator : methodGenerators) {
                methodGenerator.setContext(context);
                methodGenerator.setIntrospectedTable(introspectedTable);
                methodGenerator.setProgressCallback(null);
                methodGenerator.setWarnings(null);
                methodGenerator.addInterfaceElements(interfaze);
            }
        }
        return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
    }

    @Override
    public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable) {
        indexSqlMapGenerator(element, introspectedTable, false);
        return super.sqlMapSelectByExampleWithoutBLOBsElementGenerated(element, introspectedTable);
    }

    @Override
    public boolean sqlMapSelectByExampleWithBLOBsElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable) {
        indexSqlMapGenerator(element, introspectedTable, true);
        return super.sqlMapSelectByExampleWithBLOBsElementGenerated(element, introspectedTable);
    }
    
    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
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

    private void indexSqlMapGenerator(XmlElement element, IntrospectedTable introspectedTable, boolean isBlob) {
        List<IntrospectedIndex> indexs = tableIndex.get(introspectedTable.getTableConfiguration().getTableName());
        for(IntrospectedIndex index : indexs) {
            AbstractXmlElementGenerator elementGenerator = null;
            if(isBlob) {
                if(index.isUniqueIndex()) {
                    elementGenerator = new SelectByUkIndexElementGenerator(index);
                }else {
                    elementGenerator = new SelectByIndexWithBLOBsElementGenerator(index);
                }
            }else {
                if(!index.isUniqueIndex()) {
                    elementGenerator = new SelectByIndexWithoutBLOBsElementGenerator(index);
                }
            }
            if(null != elementGenerator) {
                if(elementGeneratorMap.containsKey(introspectedTable.getFullyQualifiedTable())){
                    elementGeneratorMap.get(introspectedTable.getFullyQualifiedTable()).add(elementGenerator);
                }else{
                    List<AbstractXmlElementGenerator> elementGenerators = new ArrayList<>();
                    elementGenerators.add(elementGenerator);
                    elementGeneratorMap.put(introspectedTable.getFullyQualifiedTable(), elementGenerators);
                }
            }
        }
    }
    
    private void indexMethodGenerator(Interface interfaze, IntrospectedTable introspectedTable, boolean isBlob) {
        List<IntrospectedIndex> indexs = tableIndex.get(introspectedTable.getTableConfiguration().getTableName());
        for(IntrospectedIndex index : indexs) {
            AbstractJavaMapperMethodGenerator methodGenerator = null;
            if(isBlob) {
                if(index.isUniqueIndex()) {
                    methodGenerator = new SelectByUkIndexMethodGenerator(index);
                }else {
                    methodGenerator = new SelectByIndexWithBLOBsMethodGenerator(index);
                }
            }else {
                if(!index.isUniqueIndex()) {
                    methodGenerator = new SelectByIndexWithoutBLOBsMethodGenerator(index);
                }
            }
            if(null != methodGenerator) {
                if(methodGeneratorMap.containsKey(introspectedTable.getFullyQualifiedTable())){
                    methodGeneratorMap.get(introspectedTable.getFullyQualifiedTable()).add(methodGenerator);
                }else{
                    List<AbstractJavaMapperMethodGenerator> methodGenerators = new ArrayList<>();
                    methodGenerators.add(methodGenerator);
                    methodGeneratorMap.put(introspectedTable.getFullyQualifiedTable(), methodGenerators);
                }
            }
        }
    }
    
    

    private void calculationIndex(IntrospectedTable introspectedTable) {
        if(!tableIndex.containsKey(introspectedTable.getTableConfiguration().getTableName())) {
            try {
                Connection connection = PluginContext.getConnection(context);
                DatabaseMetaData databaseMetaData = connection.getMetaData();
                ResultSet rs = databaseMetaData.getIndexInfo(introspectedTable.getTableConfiguration().getCatalog(),introspectedTable.getTableConfiguration().getSchema(),
                        introspectedTable.getTableConfiguration().getTableName(), false, true);
                Map<String, IntrospectedIndex> indexMap = new HashMap<>();
                while(rs.next()){
                    String indexName = rs.getString("INDEX_NAME");
                    if("PRIMARY".equals(indexName)) {
                        continue;
                    }else {
                        IntrospectedIndex index = null;
                        if(indexMap.containsKey(indexName)) {
                            index = indexMap.get(indexName);
                        }else {
                            index = new IntrospectedIndex();
                            index.setActualIndexName(indexName);
                            index.setCamelIndexName(JavaBeansUtil.getCamelCaseString(indexName, true));
                            indexMap.put(indexName, index);
                        }
                        if(!rs.getBoolean("NON_UNIQUE")){
                            index.setUniqueIndex(true);
                        }
                        index.addIndexColumn(introspectedTable.getColumn(rs.getString("COLUMN_NAME")));
                    }
                }
                tableIndex.put(introspectedTable.getTableConfiguration().getTableName(), new ArrayList<>(indexMap.values()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
}
