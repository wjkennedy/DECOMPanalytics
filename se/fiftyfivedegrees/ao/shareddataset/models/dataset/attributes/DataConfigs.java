// 
// Decompiled by Procyon v0.6.0
// 

package se.fiftyfivedegrees.ao.shareddataset.models.dataset.attributes;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "DataConfigs")
public class DataConfigs
{
    @XmlAttribute(name = "id")
    private Integer id;
    @XmlAttribute(name = "type")
    private String type;
    @XmlAttribute(name = "name")
    private String name;
    
    public DataConfigs() {
    }
    
    public DataConfigs(final Integer id, final String type, final String name) {
        this.id = id;
        this.type = type;
        this.name = name;
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public String getType() {
        return this.type;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
}
