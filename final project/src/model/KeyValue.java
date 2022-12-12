package model;

import java.lang.constant.Constable;
import java.util.HashMap;

public class KeyValue {

    public HashMap<String, String> store;

    public KeyValue() {
        this.store = new HashMap<>();
    }

    public String put(String key, String value) {
        return store.put(key, value);
    }

    public String get(String key) {
        return store.get(key);
    }

    public String delete(String key) {
        return store.remove(key);
    }
}
