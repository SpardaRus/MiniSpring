package com.github.spardarus.minispring.test.beans;

import com.github.spardarus.minispring.context.annotations.Autowired;
import com.github.spardarus.minispring.context.annotations.Qualifier;
import com.github.spardarus.minispring.context.annotations.Scope;
import com.github.spardarus.minispring.context.annotations.Service;

@Service
@Scope("prototype")
public class PrintService {
    public String s;
    @Autowired
    public PrintService(Gun g) {
        g.shoot();
    }

    @Autowired(required = false)
    public void Go(Gun w,PrintWithHello hello){
        w.shoot();
        hello.print();
    }

    @Autowired
    @Qualifier("getPrintable")
    Printable p;

    @Autowired
    @Qualifier("zik")
    Printable p2;

    public void say(){
        System.out.print("This @Autowired:\n");
        p.print();
        p2.print();
    }
}
