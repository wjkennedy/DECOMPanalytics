// 
// Decompiled by Procyon v0.6.0
// 

package se.fiftyfivedegrees.ao.shareddataset.models.dataset.attributes;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "FieldList")
public class FieldList
{
    @XmlAttribute(name = "id")
    private String id;
    @XmlAttribute(name = "value")
    private String value;
    @XmlAttribute(name = "name")
    private String name;
    @XmlAttribute(name = "label")
    private String label;
    @XmlAttribute(name = "custom")
    private boolean custom;
    @XmlAttribute(name = "customSelected")
    private boolean customSelected;
    @XmlAttribute(name = "parentId")
    private String parentId;
    @XmlAttribute(name = "homemade")
    private boolean homemade;
    
    public FieldList() {
    }
    
    public FieldList(final String id, final String value, final String name, final String label, final boolean custom, final boolean customSelected, final String parentId, final boolean homemade) {
        this.id = id;
        this.value = value;
        this.name = name;
        this.label = label;
        this.custom = custom;
        this.customSelected = customSelected;
        this.parentId = parentId;
        this.homemade = homemade;
    }
    
    public String getId() {
        return this.id;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public String getValue() {
        return this.value;
    }
    
    public void setValue(final String value) {
        this.value = value;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getLabel() {
        return this.label;
    }
    
    public void setLabel(final String label) {
        this.label = label;
    }
    
    public boolean isCustom() {
        return this.custom;
    }
    
    public void setCustom(final boolean custom) {
        this.custom = custom;
    }
    
    public boolean isCustomSelected() {
        return this.customSelected;
    }
    
    public void setCustomSelected(final boolean customSelected) {
        this.customSelected = customSelected;
    }
    
    public String getParentId() {
        return this.parentId;
    }
    
    public void setParentId(final String parentId) {
        this.parentId = parentId;
    }
    
    public boolean isHomemade() {
        return this.homemade;
    }
    
    public void setHomemade(final boolean homemade) {
        this.homemade = homemade;
    }
}
