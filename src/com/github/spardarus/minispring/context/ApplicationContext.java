package com.github.spardarus.minispring.context;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ApplicationContext {
    private List<Bean> bean=new ArrayList<>();

    public ApplicationContext(Class configClass) {
        addBean(configClass);
    }

    public Object getBean(String s) {
        for(Bean b:bean){
            if(b.getName().equals(s)){
                return b.getObject();
            }
        }
        return null;
    }

    private void addBean(Class configClass){
        Method[] m=configClass.getDeclaredMethods();
        for(Method met:m){
            if (met.isAnnotationPresent(com.github.spardarus.minispring.context.annotations.Bean.class)){
                String name=met.getAnnotation(com.github.spardarus.minispring.context.annotations.Bean.class).name();
                try {
                    Object object=met.invoke(configClass.newInstance());
                    bean.add(new Bean(name,object));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }


            }
        }

    }

}
