package com.pinkpig.mybatis.generator.api;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;


public class IntrospectedIndex {

    protected boolean isUniqueIndex;
    
    protected String actualIndexName;
    
    protected String camelIndexName;
    
    protected List<IntrospectedColumn> indexColumns;

    
    public void addIndexColumn(IntrospectedColumn column) {
        if(indexColumns == null) {
            indexColumns = new ArrayList<>();
        }
        indexColumns.add(column);
    }
    
    public boolean isUniqueIndex() {
        return isUniqueIndex;
    }

    public void setUniqueIndex(boolean isUniqueIndex) {
        this.isUniqueIndex = isUniqueIndex;
    }

    public String getActualIndexName() {
        return actualIndexName;
    }

    public void setActualIndexName(String actualIndexName) {
        this.actualIndexName = actualIndexName;
    }

    public String getCamelIndexName() {
        return camelIndexName;
    }

    public void setCamelIndexName(String camelIndexName) {
        this.camelIndexName = camelIndexName;
    }

    public List<IntrospectedColumn> getIndexColumns() {
        return indexColumns;
    }

    public void setIndexColumns(List<IntrospectedColumn> indexColumns) {
        this.indexColumns = indexColumns;
    }
    
}
