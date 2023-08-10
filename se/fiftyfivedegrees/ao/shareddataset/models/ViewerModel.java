// 
// Decompiled by Procyon v0.6.0
// 

package se.fiftyfivedegrees.ao.shareddataset.models;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "viewer")
@XmlAccessorType(XmlAccessType.FIELD)
public class ViewerModel
{
    @XmlAttribute(name = "key")
    private String key;
    @XmlAttribute(name = "name")
    private String name;
    @XmlAttribute(name = "type")
    private String type;
    
    public ViewerModel(final String key, final String name, final String type) {
        this.key = key;
        this.name = name;
        this.type = type;
    }
    
    public String getKey() {
        return this.key;
    }
    
    public void setKey(final String key) {
        this.key = key;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getType() {
        return this.type;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
}
