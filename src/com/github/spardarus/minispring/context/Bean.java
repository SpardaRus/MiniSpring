package com.github.spardarus.minispring.context;

/**
 * Bean
 */
public class Bean {
    private String name;
    private Object object;

    /**
     *
     * @return name of bean
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return object of bean
     */
    public Object getObject() {
        return object;
    }

    /**
     * Construcor Bean
     * @param name name of bean
     * @param object object of bean
     */
    public Bean(String name, Object object) {
        this.name = name;
        this.object = object;
    }
}
