// 
// Decompiled by Procyon v0.6.0
// 

package se.fiftyfivedegrees.ao.shareddataset.models;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class StarredModel
{
    @XmlAttribute(name = "starred")
    private boolean starred;
    
    public StarredModel() {
    }
    
    public StarredModel(final boolean starred) {
        this.starred = starred;
    }
    
    public boolean isStarred() {
        return this.starred;
    }
    
    public void setStarred(final boolean starred) {
        this.starred = starred;
    }
}
