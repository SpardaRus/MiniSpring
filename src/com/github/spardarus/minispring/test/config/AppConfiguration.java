package com.github.spardarus.minispring.test.config;

import com.github.spardarus.minispring.context.annotations.Bean;
import com.github.spardarus.minispring.context.annotations.ComponentScan;
import com.github.spardarus.minispring.context.annotations.Configuration;
import com.github.spardarus.minispring.test.beans.Gun;
import com.github.spardarus.minispring.test.beans.PrintWithHello;
import com.github.spardarus.minispring.test.beans.PrintWithWorld;
import com.github.spardarus.minispring.test.beans.Printable;

@Configuration
@ComponentScan("com.github.spardarus.minispring.test.beans")
public class AppConfiguration {
    @Bean
    public Printable getHPrintable() {
        return new PrintWithHello("hELL");
    }
    @Bean
    public Printable getWPrintable() {
        return new PrintWithWorld("848");
    }
    @Bean
    public PrintWithWorld getWWPrintable() {
        return new PrintWithWorld("999");
    }
    @Bean
    public Gun getGun(){
        return new Gun(2);
    }
    @Bean
    public Printable getPrintable() {
        return new PrintWithWorld("quali");
    }
}
