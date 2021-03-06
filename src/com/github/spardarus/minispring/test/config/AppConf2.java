package com.github.spardarus.minispring.test.config;

import com.github.spardarus.minispring.context.annotations.Bean;
import com.github.spardarus.minispring.context.annotations.Configuration;
import com.github.spardarus.minispring.context.annotations.Import;
import com.github.spardarus.minispring.test.beans.Gun;
import com.github.spardarus.minispring.test.beans.Light;

@Configuration
@Import(com.github.spardarus.minispring.test.config.AppConf3.class)
public class AppConf2 {

    @Bean
    public Light getLight(){
        return new Light(2);
    }
}
