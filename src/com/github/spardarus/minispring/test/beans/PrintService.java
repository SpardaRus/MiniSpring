package com.github.spardarus.minispring.test.beans;

import com.github.spardarus.minispring.context.annotations.Autowired;
import com.github.spardarus.minispring.context.annotations.Service;

@Service
public class PrintService {
    public PrintService() {
    }

    @Autowired
    Printable p;
    public void say(){
        System.out.print("This, ");
        p.print();
    }
}
