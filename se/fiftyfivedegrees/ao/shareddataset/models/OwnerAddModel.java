// 
// Decompiled by Procyon v0.6.0
// 

package se.fiftyfivedegrees.ao.shareddataset.models;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class OwnerAddModel
{
    @XmlAttribute(name = "ownerId")
    private String ownerId;
    
    public OwnerAddModel() {
    }
    
    public OwnerAddModel(final String ownerId) {
        this.ownerId = ownerId;
    }
    
    public String getOwnerId() {
        return this.ownerId;
    }
    
    public void setOwnerId(final String ownerId) {
        this.ownerId = ownerId;
    }
}
