package com.sunjet.vm.agency;

/**
 * Created by lhj on 16/10/11.
 */
public class RowWrapper {
    private String key;
    private String name;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RowWrapper(String key, String name) {
        this.key = key;
        this.name = name;
    }
}
