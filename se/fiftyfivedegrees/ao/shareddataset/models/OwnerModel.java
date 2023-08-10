// 
// Decompiled by Procyon v0.6.0
// 

package se.fiftyfivedegrees.ao.shareddataset.models;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "owner")
@XmlAccessorType(XmlAccessType.FIELD)
public class OwnerModel
{
    @XmlAttribute(name = "key")
    private String key;
    @XmlAttribute(name = "name")
    private String name;
    @XmlAttribute(name = "isOwner")
    private boolean isOwner;
    @XmlAttribute(name = "avatarURL")
    private String avatarURL;
    
    public OwnerModel() {
    }
    
    public OwnerModel(final String key, final String name, final boolean isOwner, final String avatarURL) {
        this.key = key;
        this.name = name;
        this.isOwner = isOwner;
        this.avatarURL = avatarURL;
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
    
    public boolean getIsOwner() {
        return this.isOwner;
    }
    
    public void setIsOwner(final boolean isOwner) {
        this.isOwner = isOwner;
    }
    
    public String getAvatarURL() {
        return this.avatarURL;
    }
    
    public void setAvatarURL(final String avatarURL) {
        this.avatarURL = avatarURL;
    }
}
