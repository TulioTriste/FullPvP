package net.bfcode.fullpvp.utilities;

import java.util.Map;

import gnu.trove.map.hash.TCustomHashMap;
import gnu.trove.strategy.HashingStrategy;

public class CaseInsensitiveMap<V> extends TCustomHashMap<String, V>
{
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public CaseInsensitiveMap() {
        super((HashingStrategy)CaseInsensitiveHashingStrategy.INSTANCE);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public CaseInsensitiveMap(final Map<? extends String, ? extends V> map) {
        super((HashingStrategy)CaseInsensitiveHashingStrategy.INSTANCE, (Map)map);
    }
}
