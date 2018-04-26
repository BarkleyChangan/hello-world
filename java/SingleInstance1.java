package singleton;

import java.io.Serializable;

public class LazySingleton implements Serializable {
    private static boolean initialized = false;

    private LazySingleton() {
        synchronized (LazySingleton.class) {
            if (initialized == false) {
                initialized = !initialized;
            } else {
                throw new RuntimeException("单例已被破坏");
            }
        }
    }

    static class SingletonHolder {
        private static final LazySingleton instance = new LazySingleton();
    }

    public static LazySingleton getInstance() {
        return SingletonHolder.instance;
    }
    
    private Object readResolve() {
        return getInstance();
    }
}