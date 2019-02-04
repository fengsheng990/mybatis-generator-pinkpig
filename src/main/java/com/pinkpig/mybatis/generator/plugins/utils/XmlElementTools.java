package com.pinkpig.mybatis.generator.plugins.utils;

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Element;
import org.mybatis.generator.api.dom.xml.XmlElement;

import java.lang.reflect.Field;

public class XmlElementTools {

    public static XmlElement getXmlElement(XmlElement element, String name) {
        for(Element tmpElement : element.getElements()) {
            if(XmlElement.class.isInstance(tmpElement) )
                if(((XmlElement)tmpElement).getName().equals(name)) {
                    return (XmlElement)tmpElement;
                }
        }
        return null;
    }

    public static XmlElement getXmlElement(XmlElement element, String name, int i) {
        int n = 0;
        for(Element tmpElement : element.getElements()) {

            if(XmlElement.class.isInstance(tmpElement) )
                if(((XmlElement)tmpElement).getName().equals(name)) {
                    ++n;
                    if(n==i)
                        return (XmlElement)tmpElement;
                }
        }
        return null;
    }

    public static Attribute getAttribute(XmlElement element, String name) {
        for(Attribute tmpAttribute : element.getAttributes()) {
            if(tmpAttribute.getName().equals(name)) {
                return tmpAttribute;
            }
        }
        return null;
    }

    public static Attribute renameAttributeValue(Attribute attribute, String newValue){
        try{
            Field valueField = attribute.getClass().getDeclaredField("value");
            valueField.setAccessible(true);
            valueField.set(attribute, newValue);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return attribute;
    }
    
}
