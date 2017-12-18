package com.github.spardarus.minispring.test;

public class PrintWithHello implements Printable {
    public String s;

    public PrintWithHello(String s) {
        this.s = s;
    }

    public void print() {
        System.out.println("Hello "+s);
    }
}
