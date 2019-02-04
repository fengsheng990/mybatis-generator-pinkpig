package com.pinkpig.mybatis.generator.api;

public enum PluginInternalAttribute {

    ATTR_METHOD_SELECT_BY_PK_WHITH_LOCK("selectByPkWithLock", "根据主键获取记录锁"),
    ATTR_METHOD_SELECT_BY_UK_INDEX("selectByUkIndex", "根据唯一索引获取记录"),
    ATTR_METHOD_SELECT_BY_INDEX("selectBy%s", "根据索引获取记录"),
    ATTR_METHOD_SELECT_BY_INDEX_WITH_BLOBS("selectBy%sWithBlobs", "根据索引获取记录"),
    ;
    private String value;
    private String desc;

    private PluginInternalAttribute(String value, String desc){
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
