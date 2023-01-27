package com.dev.vetbackend;

import com.dev.vetbackend.security.WebMvcConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import(WebMvcConfig.class)
@SpringBootApplication
public class VetBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(VetBackendApplication.class, args);

    }

}
