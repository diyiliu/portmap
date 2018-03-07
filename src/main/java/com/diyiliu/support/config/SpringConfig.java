package com.diyiliu.support.config;

import com.diyiliu.support.util.SpringUtil;
import com.diyiliu.ui.PortMapApp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description: SpringConfig
 * Author: DIYILIU
 * Update: 2018-03-07 14:49
 */

@Configuration
public class SpringConfig {

    /**
     * spring 工具类
     *
     * @return
     */
    @Bean
    public SpringUtil springUtil() {

        return new SpringUtil();
    }

    @Bean(initMethod = "display")
    public PortMapApp portMapApp(){

        return new PortMapApp();
    }
}
