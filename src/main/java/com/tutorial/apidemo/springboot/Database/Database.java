package com.tutorial.apidemo.springboot.Database;

import com.tutorial.apidemo.springboot.Models.Product;
import com.tutorial.apidemo.springboot.Repositories.ProductRepository;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;


@Configuration
public class Database {
    //logger
    private static final Logger logger = LoggerFactory.getLogger(Database.class);
    @Bean
    CommandLineRunner initDatabase(ProductRepository productRepository){
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
//                Product a = new Product( "Macbook pro 16 inch", 2020, 2400.0, "");
//                Product b = new Product( "Ipad air Green pro", 2021, 2500.0, "");
//                logger.info("insert data: "+ productRepository.save(a));
//                logger.info("insert data: "+ productRepository.save(b));
            }
        };
    }
}
