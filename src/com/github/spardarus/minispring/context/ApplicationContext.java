package com.github.spardarus.minispring.context;

import com.github.spardarus.minispring.context.annotations.*;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

public class ApplicationContext {
    private Class configClass;
    private List<Bean> bean = new ArrayList<>();

    public ApplicationContext(Class configClass) {
        this.configClass = configClass;
        if(configClass.isAnnotationPresent(Import.class)){
            addBeanImport();
        }
        addBean();
    }

    List<Bean> getApConBean() {
        return bean;
    }

    private void addBeanImport() {
        Import imp= (Import) configClass.getAnnotation(Import.class);
        Class[] impClass=imp.value();
        for (Class c:impClass){
            bean.addAll(new ApplicationContext(c).getApConBean());
        }

    }

    private void setObjectFields(Object object) throws IllegalAccessException {
        int na = 0;
        String qualiName = "";
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            if (f.isAnnotationPresent(Autowired.class)
                    &&f.getAnnotation(Autowired.class).required()
                    && f.isAnnotationPresent(Qualifire.class)) {
                qualiName = f.getAnnotation(Qualifire.class).value();
                for (Bean b : bean) {
                    Type[] t = b.getObject().getClass().getGenericInterfaces();
                    for (Type type : t) {
                        if (f.getType().equals(type) && b.getName().equals(qualiName)) {
                            f.set(object, b.getObject());
                        }

                    }
                }
            }else{
                if (f.isAnnotationPresent(Autowired.class)
                        &&f.getAnnotation(Autowired.class).required()) {
                    for (Bean b : bean) {
                        Type[] t = b.getObject().getClass().getGenericInterfaces();
                        for (Type type : t) {
                            if (f.getType().equals(type)) {
                                f.set(object, b.getObject());
                                na++;
                            }
                            if (na == 2) {
                                throw new IllegalArgumentException("Need Qualifier");
                            }
                        }
                    }
                }
            }

        }
    }
    private Object getAutowired(String className) throws ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException {
        ComponentScan componentScan = (ComponentScan) configClass.getAnnotation(ComponentScan.class);
        String thisName=null;
        if(!componentScan.value().equals("")){
        thisName = "" + componentScan.value() + "." + className.substring(0, 1).toUpperCase() +
                className.substring(1);}
        else{
            thisName = "" + configClass.getPackageName() + "." + className.substring(0, 1).toUpperCase() +
                    className.substring(1);
        }
        Class classAutowired = Class.forName(thisName);
        if(classAutowired.isAnnotationPresent(Service.class)
                ||classAutowired.isAnnotationPresent(Component.class)){
        }else{
            throw new IllegalArgumentException("Class must be @Service or @Component");
        }
        Object objectAutowired = newObjectAutowired(classAutowired);
        setObjectFields(objectAutowired);
        setObjectMethods(objectAutowired);
        if(!classAutowired.isAnnotationPresent(Scope.class)){
            bean.add(new Bean(className,objectAutowired));
        }else{
            Scope scope=(Scope)classAutowired.getAnnotation(Scope.class);
            if(scope.value().equals("prototype")){
                }else{
            if(scope.value().equals("singleton")){
                bean.add(new Bean(className,objectAutowired));
            }else{
                throw new IllegalArgumentException("Class:'"+className+"'" +
                        "must 'prototype' or 'singleton'");
            }
            }
        }
        return objectAutowired;
    }

    private void setObjectMethods(Object object) throws InvocationTargetException, IllegalAccessException {
        Method[] methods=object.getClass().getDeclaredMethods();
        for(Method m:methods){
            if (m.isAnnotationPresent(Autowired.class)
                    &&m.getAnnotation(Autowired.class).required()) {
                m.setAccessible(true);
                Type[] typesParametersMethods = m.getParameterTypes();
                Object[] parametersObject = new Object[typesParametersMethods.length];
                for(int i=0;i<typesParametersMethods.length;i++){
                    parametersObject[i]=getObjectType(typesParametersMethods[i]);
                }
                m.invoke(object,parametersObject);
            }
        }
    }
    private Object newObjectAutowired(Class classAutowired) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Object objectAutowired = null;
        Constructor[] constructors = classAutowired.getDeclaredConstructors();
        int n = 0;
        boolean isConstructor=false;
        for (Constructor c : constructors) {
            c.setAccessible(true);
            if (c.isAnnotationPresent(Autowired.class)) {
                n++;
                Type[] typesParametersConstructor = c.getParameterTypes();
                Object[] parametersObject = new Object[typesParametersConstructor.length];
                for(int i=0;i<typesParametersConstructor.length;i++){
                    parametersObject[i]=getObjectType(typesParametersConstructor[i]);
                }
                objectAutowired = c.newInstance(parametersObject);
                isConstructor=true;
            }
            if (n == 2) {
                throw new IllegalArgumentException("Two or more constructors @Autowired ");
            }
        }
        if(isConstructor==false){
            return objectAutowired=classAutowired.newInstance();
        }

        return objectAutowired;
    }

    private Object getObjectType(Type type) {
        Object result = null;
        int n = 0;
        for (Bean b : bean) {
            if (b.getObject().getClass().getTypeName().equals(type.getTypeName())) {
                result = b.getObject();
                n++;
            }
            if (n == 2) {
                throw new IllegalArgumentException("Two or more have one type: " + type.getTypeName());
            }
        }
        return result;
    }

    public Object getBean(String className) {
        for (Bean b : bean) {
            if (b.getName().equals(className)) {
                return b.getObject();
            }
        }
        if (configClass.isAnnotationPresent(Configuration.class)) {

            try {
                return getAutowired(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private void addBean() {
        Method[] m = configClass.getDeclaredMethods();
        String name;
        for (Method met : m) {
            if (met.isAnnotationPresent(com.github.spardarus.minispring.context.annotations.Bean.class)) {
                name = met.getAnnotation(com.github.spardarus.minispring.context.annotations.Bean.class).name();
                if (name.equals("")) {
                    name = met.getName();
                }
                for(Bean b:bean){
                    if(name.equals(b.getName())){
                        throw new IllegalArgumentException("Bean name:'"+name+"' two or more");
                    }
                }
                try {
                    Object object = met.invoke(configClass.newInstance());
                    bean.add(new Bean(name, object));
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