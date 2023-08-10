// 
// Decompiled by Procyon v0.6.0
// 

package se.fiftyfivedegrees.ao.shareddataset.models;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ViewerAddModel
{
    @XmlAttribute(name = "key")
    private String key;
    @XmlAttribute(name = "type")
    private String type;
    
    public ViewerAddModel() {
    }
    
    public ViewerAddModel(final String key, final String type) {
        this.key = key;
        this.type = type;
    }
    
    public String getKey() {
        return this.key;
    }
    
    public void setKey(final String key) {
        this.key = key;
    }
    
    public String getType() {
        return this.type;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
}
