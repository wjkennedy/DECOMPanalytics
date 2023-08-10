// 
// Decompiled by Procyon v0.6.0
// 

package se.fiftyfivedegrees.ao.shareddataset.entities;

import net.java.ao.schema.StringLength;
import net.java.ao.OneToMany;
import net.java.ao.ManyToMany;
import net.java.ao.Entity;

public interface SharedDataset extends Entity
{
    @ManyToMany(DatasetToOwner.class)
    Owner[] getOwners();
    
    @ManyToMany(DatasetToViewer.class)
    Viewer[] getViewers();
    
    @OneToMany
    Config[] getConfigs();
    
    String getUuid();
    
    void setUuid(final String p0);
    
    boolean isShared();
    
    void setShared(final boolean p0);
    
    String getName();
    
    void setName(final String p0);
    
    String getDate();
    
    void setDate(final String p0);
    
    String getOwner();
    
    void setOwner(final String p0);
    
    @StringLength(-1)
    String getColumnOrder();
    
    void setColumnOrder(final String p0);
    
    @StringLength(-1)
    String getColumns();
    
    void setColumns(final String p0);
    
    @StringLength(-1)
    String getDataObjects();
    
    void setDataObjects(final String p0);
    
    @StringLength(-1)
    String getDataConfigs();
    
    void setDataConfigs(final String p0);
    
    @StringLength(-1)
    String getFieldList();
    
    void setFieldList(final String p0);
    
    @StringLength(-1)
    String getFilters();
    
    void setFilters(final String p0);
}
