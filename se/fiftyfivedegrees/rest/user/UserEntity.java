// 
// Decompiled by Procyon v0.6.0
// 

package se.fiftyfivedegrees.rest.user;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserEntity
{
    @XmlElement
    public String name;
    @XmlElement
    public String displayName;
    
    public UserEntity(final String name, final String displayName) {
        this.name = name;
        this.displayName = displayName;
    }
}
