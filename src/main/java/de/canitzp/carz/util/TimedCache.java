package de.canitzp.carz.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MisterErwin on 18.08.2017.
 * In case you need it, ask me ;)
 */
public class TimedCache<E> {
    private final long liveTime;
    private final Map<E,Long> data = new HashMap<>();

    public TimedCache(long liveTime) {
        this.liveTime = liveTime;
    }

    public boolean contains(E e){
        Long d = data.get(e);
        if (d == null)
            return false;
        if (d+liveTime < System.currentTimeMillis()){
            data.remove(e);
            return false;
        }
        return true;
    }

    public void remove(E e){
          data.remove(e);
    }

    public void add(E e){
        data.put(e, System.currentTimeMillis());
    }
}
