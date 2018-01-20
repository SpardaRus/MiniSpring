package com.github.spardarus.minispring.test.start;

import com.github.spardarus.minispring.context.ApplicationContext;
import com.github.spardarus.minispring.test.beans.PrintService;
import com.github.spardarus.minispring.test.beans.PrintService2;
import com.github.spardarus.minispring.test.beans.Printable;
import com.github.spardarus.minispring.test.config.AppConfiguration;

public class Main {
    public static void main(String[] args) {
        System.out.println("Start");
        System.out.println();

        ApplicationContext context=new ApplicationContext(AppConfiguration.class);
        Printable p=(Printable)context.getBean("zik");
        p.print();

        PrintService ps=(PrintService)context.getBean("printService");
        ps.say();
        PrintService2 ps2=(PrintService2)context.getBean("printService2");
        ps2.go();
        System.out.println();
        System.out.println("End");
    }
}
