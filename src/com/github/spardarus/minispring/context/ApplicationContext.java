package com.github.spardarus.minispring.context;

import com.github.spardarus.minispring.context.annotations.Autowired;
import com.github.spardarus.minispring.context.annotations.ComponentScan;
import com.github.spardarus.minispring.context.annotations.Qualifire;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ApplicationContext {
    private Class configClass;
    private List<Bean> bean=new ArrayList<>();

    public ApplicationContext(Class configClass) {
        this.configClass=configClass;
        addBean();
    }

    private Object getAutowiredType(String className) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
            Object qualiObj=getAutowiredName(className);
            if(qualiObj!=null){
                return qualiObj;
            }
            ComponentScan componentScan= (ComponentScan) configClass.getAnnotation(ComponentScan.class);
            String thisName=""+componentScan.value()+"."+className.substring(0,1).toUpperCase()+
                    className.substring(1);
            Class classAutowired=Class.forName(thisName);
            Object objectAutowired=null;
            objectAutowired = classAutowired.newInstance();
            int n=0;
            Field[] fields=classAutowired.getDeclaredFields();
            for(Field f:fields){
                if (f.isAnnotationPresent(Autowired.class)){
                        f.setAccessible(true);
                    for(Bean b:bean) {
                        Type[] t = b.getObject().getClass().getGenericInterfaces();
                        for (Type type : t) {
                            if(n==1){
                                throw new IllegalArgumentException("Need Qualifier");
                            }
                            if (f.getType().equals(type)) {
                                f.set(objectAutowired, b.getObject());
                                n++;
                            }
                        }
                    }
                    }
                }
        return objectAutowired;
    }
    private Object getAutowiredName(String className) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        ComponentScan componentScan= (ComponentScan) configClass.getAnnotation(ComponentScan.class);
        String thisName=""+componentScan.value()+"."+className.substring(0,1).toUpperCase()+
                className.substring(1);
        Class classAutowired=Class.forName(thisName);
        Object objectAutowired=null;
        objectAutowired = classAutowired.newInstance();
        int n=0;
        String qualiName="";
        Field[] fields=classAutowired.getDeclaredFields();
        for(Field f:fields){
            if (f.isAnnotationPresent(Autowired.class)
                    &&f.isAnnotationPresent(Qualifire.class)){
                f.setAccessible(true);
                qualiName=f.getAnnotation(Qualifire.class).value();
                for(Bean b:bean) {
                    Type[] t = b.getObject().getClass().getGenericInterfaces();
                    for (Type type : t) {
                        if(n==2){
                            throw new IllegalArgumentException("Qualifier have two or more name: "+qualiName);
                        }
                        if (f.getType().equals(type)&&b.getName().equals(qualiName)) {
                            f.set(objectAutowired, b.getObject());
                            //n++; Proverka nujna esli mnogo imen odinkovix
                        }
                    }
                }
            }
        }
        return objectAutowired;
    }

    public Object getBean(String className) {
        for(Bean b:bean){
            if(b.getName().equals(className)){
                return b.getObject();
            }
        }
        if(configClass.isAnnotationPresent(ComponentScan.class)){
            try {
                return getAutowiredType(className);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
    private void addBeanAnn() throws ClassNotFoundException {
        Method[] m=configClass.getDeclaredMethods();
        String name;
        for(Method met:m){
            if (met.isAnnotationPresent(com.github.spardarus.minispring.context.annotations.Bean.class)){
                name=met.getAnnotation(com.github.spardarus.minispring.context.annotations.Bean.class).name();
               if(name.equals("")){
                    name=met.getName();
                }
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
    private void addBean(){
        try {
            addBeanAnn();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}