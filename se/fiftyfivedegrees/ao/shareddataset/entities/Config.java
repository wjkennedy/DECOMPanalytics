// 
// Decompiled by Procyon v0.6.0
// 

package se.fiftyfivedegrees.ao.shareddataset.entities;

import net.java.ao.Entity;

public interface Config extends Entity
{
    SharedDataset getDataSet();
    
    void setDataSet(final SharedDataset p0);
    
    String getOwnerId();
    
    void setOwnerId(final String p0);
    
    boolean isStarred();
    
    void setStarred(final boolean p0);
}
