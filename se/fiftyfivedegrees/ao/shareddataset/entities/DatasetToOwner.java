// 
// Decompiled by Procyon v0.6.0
// 

package se.fiftyfivedegrees.ao.shareddataset.entities;

import net.java.ao.schema.Indexed;
import net.java.ao.schema.Table;
import net.java.ao.Entity;

@Table("DS2OWNER")
public interface DatasetToOwner extends Entity
{
    SharedDataset getDataset();
    
    void setDataset(final SharedDataset p0);
    
    @Indexed
    Owner getOwner();
    
    void setOwner(final Owner p0);
}
