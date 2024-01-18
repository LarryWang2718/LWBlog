package com.yjq.programmer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan("com.yjq.programmer.dao")
public class BlogAndBBSApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(BlogAndBBSApplication.class, args);
    }
}
