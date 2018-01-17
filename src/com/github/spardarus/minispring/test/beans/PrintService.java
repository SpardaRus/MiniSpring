package com.github.spardarus.minispring.test.beans;

import com.github.spardarus.minispring.context.annotations.Autowired;
import com.github.spardarus.minispring.context.annotations.Qualifire;
import com.github.spardarus.minispring.context.annotations.Service;

@Service
public class PrintService {
    public PrintService() {
    }

    @Autowired
    @Qualifire("getPrintable")
    Printable p;

    @Autowired
    @Qualifire("getHPrintable")
    Printable p2;

    public void say(){
        System.out.print("This @Autowired:\n");
        p.print();
        p2.print();
    }
}
