package com.github.spardarus.minispring.context;

public class Bean {
    private String name;
    private Object object;

    public String getName() {
        return name;
    }


    public Object getObject() {
        return object;
    }

    public Bean(String name, Object object) {
        this.name = name;
        this.object = object;
    }
}
