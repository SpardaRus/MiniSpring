package com.github.spardarus.minispring.context;

import com.github.spardarus.minispring.context.annotations.Autowired;
import com.github.spardarus.minispring.context.annotations.ComponentScan;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ApplicationContext {
    Class configClass;
    private List<Bean> bean=new ArrayList<>();

    public ApplicationContext(Class configClass) {
        this.configClass=configClass;
        addBean(configClass);

    }

    private Object getAutowired(Class configClass,String className){

            ComponentScan componentScan= (ComponentScan) configClass.getAnnotation(
                    ComponentScan.class);
            String thisName=""+componentScan.value()+"."+className;
        try {
            Class classAutowired=Class.forName(thisName);
            Field[] fields=classAutowired.getDeclaredFields();
            for(Field f:fields){
                if (f.isAnnotationPresent(Autowired.class)){
                        f.setAccessible(true);
                    for(Bean b:bean){
                        Method[] m=configClass.getDeclaredMethods();
                        for(Method met:m){
                            if (met.isAnnotationPresent(com.github.spardarus.minispring.context.annotations.Bean.class)&&(
                                    f.getType().equals(met.getGenericReturnType())
                                    )){
                                if(b.getName().equals(met.getAnnotation(com.github.spardarus.minispring.context.annotations.Bean.class).name())){
                                    Object objectAutowired=classAutowired.newInstance();
                                    f.set(objectAutowired,b.getObject());
                                    return objectAutowired;
                                }

                            }
                        }
                    }

                }
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Object getBean(String className) {
        for(Bean b:bean){
            if(b.getName().equals(className)){
                return b.getObject();
            }
        }
        if(configClass.isAnnotationPresent(
                ComponentScan.class)){
            return getAutowired(configClass,className);
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
