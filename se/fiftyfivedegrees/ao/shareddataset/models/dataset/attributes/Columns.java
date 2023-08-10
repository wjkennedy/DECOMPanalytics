// 
// Decompiled by Procyon v0.6.0
// 

package se.fiftyfivedegrees.ao.shareddataset.models.dataset.attributes;

import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Columns")
public class Columns
{
    @XmlAttribute(name = "id")
    private String id;
    @XmlAttribute(name = "title")
    private String title;
    @XmlAttribute(name = "statusIds")
    private List<String> statusIds;
    
    public Columns() {
    }
    
    public Columns(final String id, final String title, final List<String> statusIds) {
        this.id = id;
        this.title = title;
        this.statusIds = statusIds;
    }
    
    public String getId() {
        return this.id;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public void setTitle(final String title) {
        this.title = title;
    }
    
    public List<String> getStatusIds() {
        return this.statusIds;
    }
    
    public void setStatusIds(final List<String> statusIds) {
        this.statusIds = statusIds;
    }
}
