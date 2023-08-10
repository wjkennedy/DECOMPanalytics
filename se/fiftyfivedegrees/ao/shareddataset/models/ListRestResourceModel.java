// 
// Decompiled by Procyon v0.6.0
// 

package se.fiftyfivedegrees.ao.shareddataset.models;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "shared")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListRestResourceModel
{
    @XmlAttribute(name = "id")
    private String id;
    @XmlElement(name = "value")
    private List<?> list;
    
    public ListRestResourceModel() {
    }
    
    public ListRestResourceModel(final String id, final List<?> list) {
        this.id = id;
        this.list = list;
    }
    
    public String getId() {
        return this.id;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public List<?> getList() {
        return this.list;
    }
    
    public void setList(final List<?> list) {
        this.list = list;
    }
}
