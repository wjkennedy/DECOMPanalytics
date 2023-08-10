// 
// Decompiled by Procyon v0.6.0
// 

package se.fiftyfivedegrees.ao.shareddataset.entities;

import net.java.ao.schema.StringLength;
import net.java.ao.ManyToMany;
import net.java.ao.Entity;

public interface Owner extends Entity
{
    @ManyToMany(DatasetToOwner.class)
    SharedDataset[] getDatasets();
    
    String getOwnerId();
    
    @StringLength(255)
    void setOwnerId(final String p0);
}
