package com.logigear.crm.admins;

import com.logigear.crm.admins.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

@SpringBootApplication
@EnableConfigurationProperties({
        AppProperties.class
})
@EntityScan(basePackageClasses = {
        AdminsApplication.class,
        Jsr310JpaConverters.class
})
public class AdminsApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminsApplication.class, args);
    }

}
