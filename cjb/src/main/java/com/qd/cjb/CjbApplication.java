package com.qd.cjb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.qd.cjb.mapper")
public class CjbApplication {
  public static void main(String[] args) {
    SpringApplication.run(CjbApplication.class, args);
  }

}
