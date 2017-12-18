package com.github.spardarus.minispring.context;

public class ApplicationContext {
    private Object object;

    public Object getBean() {
        return object;
    }
    public void setBean(Object object) {
        this.object = object;
    }
}
