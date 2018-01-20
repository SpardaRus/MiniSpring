package com.github.spardarus.minispring.test.beans;

import com.github.spardarus.minispring.context.annotations.Autowired;
import com.github.spardarus.minispring.context.annotations.Component;

@Component
public class PrintService2 {
    private Light l;
    @Autowired
    public PrintService2(Light l) {
        this.l = l;
    }
    public void go(){
        l.on();
        l.off();
    }
}
