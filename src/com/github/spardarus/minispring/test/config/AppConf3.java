package com.github.spardarus.minispring.test.config;

import com.github.spardarus.minispring.context.annotations.Bean;
import com.github.spardarus.minispring.context.annotations.ComponentScan;
import com.github.spardarus.minispring.context.annotations.Configuration;
import com.github.spardarus.minispring.context.annotations.Import;
import com.github.spardarus.minispring.test.beans.Gun;
@Configuration
public class AppConf3 {
    @Bean
    public Gun getGun5(){
        return new Gun(2);
    }
}
