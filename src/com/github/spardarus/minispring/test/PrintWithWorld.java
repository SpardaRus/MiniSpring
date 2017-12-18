package com.github.spardarus.minispring.test;

public class PrintWithWorld implements Printable {
    public String s;

    public PrintWithWorld(String s) {
        this.s = s;
    }

    public void print() {
        System.out.println("World "+s);
    }
}
