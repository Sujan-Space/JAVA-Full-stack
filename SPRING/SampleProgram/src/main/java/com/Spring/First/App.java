package com.Spring.First;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

public class App {
    public static void main(String[] args) {
        System.out.println("Hello WOrld!");
      // ApplicationContext context = new ClassPathXmlApplicationContext("config.xml");
     /*   Student st = (Student) context.getBean("student1");
        System.out.println(st);

        Customer c = (Customer) context.getBean("customer");*/
        
       AbstractApplicationContext context2 = new ClassPathXmlApplicationContext("config.xml");
       BeanlifeCycle blf = (BeanlifeCycle) context2.getBean("beanlyf");
      System.out.println(blf);
      context2.registerShutdownHook();
    }
}
