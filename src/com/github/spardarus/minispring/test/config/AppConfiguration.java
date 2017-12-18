package com.github.spardarus.minispring.test.config;

import com.github.spardarus.minispring.context.annotations.Bean;
import com.github.spardarus.minispring.context.annotations.ComponentScan;
import com.github.spardarus.minispring.context.annotations.Configuration;
import com.github.spardarus.minispring.test.beans.PrintWithHello;
import com.github.spardarus.minispring.test.beans.Printable;

@Configuration
@ComponentScan("com.github.spardarus.springtest.bean")
public class AppConfiguration {
    @Bean(name="Hello")
    public Printable getPrintable() {
        return new PrintWithHello("hELL");
    }
}
