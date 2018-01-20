package com.github.spardarus.minispring.test.beans;

public class Light {
    private int lump;

    public Light(int lump) {
        this.lump = lump;
    }

    public void on(){
        for(int i=0;i<lump;i++){
            System.out.println("Light is On");
        }
    }
    public void off(){
        for(int i=0;i<lump;i++){
            System.out.println("Light is Off");
        }
    }
}

