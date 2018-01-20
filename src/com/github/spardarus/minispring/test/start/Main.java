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

        PrintService2 ps22=(PrintService2)context.getBean("printService2");
        ps22.go();
        PrintService ps=(PrintService)context.getBean("printService");
        ps.say();
        ps.s="22222";
        System.out.println("***** "+ps.s);
        PrintService ps2=(PrintService)context.getBean("printService");
        System.out.println("***** "+ps2.s);


        System.out.println();
        System.out.println("End");
    }
}
