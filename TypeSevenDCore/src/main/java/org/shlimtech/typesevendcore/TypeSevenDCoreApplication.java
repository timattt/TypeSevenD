package org.shlimtech.typesevendcore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "org.shlimtech")
@ComponentScan(basePackages = "org.shlimtech")
@EntityScan(basePackages = "org.shlimtech")
public class TypeSevenDCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(TypeSevenDCoreApplication.class, args);
    }

}
