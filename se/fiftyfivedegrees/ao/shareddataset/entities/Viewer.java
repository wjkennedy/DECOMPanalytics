// 
// Decompiled by Procyon v0.6.0
// 

package se.fiftyfivedegrees.ao.shareddataset.entities;

import net.java.ao.schema.StringLength;
import net.java.ao.ManyToMany;
import net.java.ao.Entity;

public interface Viewer extends Entity
{
    @ManyToMany(DatasetToViewer.class)
    SharedDataset[] getDatasets();
    
    String getKey();
    
    @StringLength(255)
    void setKey(final String p0);
    
    String getType();
    
    @StringLength(255)
    void setType(final String p0);
}
