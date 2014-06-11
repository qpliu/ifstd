package com.yrek.ifstd.glk;

import java.util.ArrayList;

public class GlkObjectPool<T extends GlkObject> {
    private final ArrayList<T> pool = new ArrayList<T>();

    public void add(T obj) {
        for (int i = 0; i < pool.size(); i++) {
            T elt = pool.get(i);
            if (elt == null || elt.isDestroyed()) {
                obj.setPointer(i+1);
                pool.set(i, obj);
            }
        }
        obj.setPointer(pool.size());
        pool.add(obj);
    }

    public T iterate(T obj) {
        for (int i = obj == null ? 0 : obj.getPointer(); i < pool.size(); i++) {
            T elt = pool.get(i);
            if (elt != null) {
                if (!elt.isDestroyed()) {
                    return elt;
                } else {
                    pool.set(i, null);
                }
            }
        }
        return null;
    }

    public T get(int pointer) {
        return pool.get(pointer-1);
    }

    public void destroy(int pointer) {
        T obj = pool.get(pointer-1);
        obj.destroy();
        pool.set(pointer-1, null);
    }
}
