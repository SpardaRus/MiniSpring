package com.github.spardarus.minispring.test.beans;

public class Gun {
    int ammo;
    public Gun(int i){
        ammo=i;
    }
    public void shoot(){
        for(int i=0;i<ammo;i++){
            System.out.print("Babax ");
        }
        System.out.println();
    }
}
