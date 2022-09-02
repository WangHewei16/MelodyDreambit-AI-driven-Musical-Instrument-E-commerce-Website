package com.group13;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author xujinfengxu
 */

@SpringBootApplication
@MapperScan("com.group13.mapper")
@EnableScheduling
public class StaffPortalApplication {

    public static void main(String[] args) {
        SpringApplication.run(StaffPortalApplication.class, args);
    }

}


