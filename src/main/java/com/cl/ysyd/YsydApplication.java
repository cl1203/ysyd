package com.cl.ysyd;

import com.cl.ysyd.common.utils.ContextUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan("com.cl.ysyd.mapper")
public class YsydApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(YsydApplication.class, args);
        ContextUtil.setApplicationContext(context);
    }

}
