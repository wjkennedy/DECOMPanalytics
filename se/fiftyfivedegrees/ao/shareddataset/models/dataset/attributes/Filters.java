// 
// Decompiled by Procyon v0.6.0
// 

package se.fiftyfivedegrees.ao.shareddataset.models.dataset.attributes;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Filters")
public class Filters
{
    @XmlAttribute(name = "createdDate")
    private String createdDate;
    @XmlAttribute(name = "resolvedDate")
    private String resolvedDate;
    @XmlAttribute(name = "subquery")
    private boolean subquery;
    @XmlAttribute(name = "excludeSubtasks")
    private boolean excludeSubtasks;
    
    public Filters() {
    }
    
    public Filters(final String createdDate, final String resolvedDate, final boolean subquery, final boolean excludeSubtasks) {
        this.createdDate = createdDate;
        this.resolvedDate = resolvedDate;
        this.subquery = subquery;
        this.excludeSubtasks = excludeSubtasks;
    }
    
    public String getCreatedDate() {
        return this.createdDate;
    }
    
    public void setCreatedDate(final String createdDate) {
        this.createdDate = createdDate;
    }
    
    public String getResolvedDate() {
        return this.resolvedDate;
    }
    
    public void setResolvedDate(final String resolvedDate) {
        this.resolvedDate = resolvedDate;
    }
    
    public boolean isSubquery() {
        return this.subquery;
    }
    
    public void setSubquery(final boolean subquery) {
        this.subquery = subquery;
    }
    
    public boolean isExcludeSubtasks() {
        return this.excludeSubtasks;
    }
    
    public void setExcludeSubtasks(final boolean excludeSubtasks) {
        this.excludeSubtasks = excludeSubtasks;
    }
}
