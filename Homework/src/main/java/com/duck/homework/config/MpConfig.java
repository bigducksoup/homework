package com.duck.homework.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.duck.homework.mapper")
public class MpConfig {
}
