package net.bfcode.fullpvp.utilities;

import gnu.trove.strategy.HashingStrategy;

@SuppressWarnings({ "serial", "rawtypes" })
public class CaseInsensitiveHashingStrategy implements HashingStrategy {
    static final CaseInsensitiveHashingStrategy INSTANCE;
    
    static {
        INSTANCE = new CaseInsensitiveHashingStrategy();
    }
    
    public int computeHashCode(final Object object) {
        return ((String)object).toLowerCase().hashCode();
    }
    
    public boolean equals(final Object o1, final Object o2) {
        return o1.equals(o2) || (o1 instanceof String && o2 instanceof String && ((String)o1).toLowerCase().equals(((String)o2).toLowerCase()));
    }
}
