package com.github.spardarus.minispring.context;

import com.github.spardarus.minispring.context.annotations.*;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains the context of bins and the logic of their creation.
 */
public class ApplicationContext {
    /**
     * Class configuration
     */
    private Class configClass;
    /**
     * Context bins
     */
    private List<Bean> bean = new ArrayList<>();

    /**
     * Constructor create bins
     * @param configClass Class configuration
     */
    public ApplicationContext(Class configClass) {
        this.configClass = configClass;
        if(configClass.isAnnotationPresent(Import.class)){
            addBeanImport();
        }
        addBean();
    }

    /**
     * Method is to use the import other configuration files
     * @return Context bins
     */
    List<Bean> getApConBean() {
        return bean;
    }

    /**
     * In the configuration file have annotation @Import
     * take classes of other configuration files contained
     * in the bins added to the context.
     */
    private void addBeanImport() {
        Import imp= (Import) configClass.getAnnotation(Import.class);
        Class[] impClass=imp.value();
        for (Class c:impClass){
            bean.addAll(new ApplicationContext(c).getApConBean());
        }

    }

    /**
     * Set values to the fields bean
     * @param object bean
     * @throws IllegalAccessException
     */
    private void setObjectFields(Object object) throws IllegalAccessException {
        int na = 0;
        String qualiName = "";
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            if (f.isAnnotationPresent(Autowired.class)
                    &&f.getAnnotation(Autowired.class).required()
                    && f.isAnnotationPresent(Qualifier.class)) {
                qualiName = f.getAnnotation(Qualifier.class).value();
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

    /**
     * Bean is created autowired
     * @param className name class bean
     * @return
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws InvocationTargetException
     */
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

    /**
     * Set values to the methods bean
     * @param object bean
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
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

    /**
     * Bean is created the constructor
     * @param classAutowired class bean
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
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

    /**
     * The choice of context bean by type
     * @param type type bean
     * @return Bean
     */
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

    /**
     * Obtaining bean out of context on request
     * @param className name bean
     * @return Bean
     */
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

    /**
     * Add bean of the methods in the configuration file
     */
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