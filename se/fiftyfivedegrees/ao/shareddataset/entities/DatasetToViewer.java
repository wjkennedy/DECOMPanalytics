// 
// Decompiled by Procyon v0.6.0
// 

package se.fiftyfivedegrees.ao.shareddataset.entities;

import net.java.ao.schema.Indexed;
import net.java.ao.schema.Table;
import net.java.ao.Entity;

@Table("DS2VIEWER")
public interface DatasetToViewer extends Entity
{
    void setDataset(final SharedDataset p0);
    
    SharedDataset getDataset();
    
    void setViewer(final Viewer p0);
    
    @Indexed
    Viewer getViewer();
}
