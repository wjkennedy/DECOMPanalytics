// 
// Decompiled by Procyon v0.6.0
// 

package com.google.gson;

import java.lang.reflect.Type;

public interface JsonSerializer<T>
{
    JsonElement serialize(final T p0, final Type p1, final JsonSerializationContext p2);
}
