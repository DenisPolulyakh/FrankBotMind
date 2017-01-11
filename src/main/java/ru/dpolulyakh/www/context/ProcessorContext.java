package ru.dpolulyakh.www.context;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Denis Polulyakh
 * 08.01.2017
 */
public class ProcessorContext {
    private String pathXmlConfiguration;
    private ApplicationContext context;

   public void ProcessorContext(){
      context = new ClassPathXmlApplicationContext("/spring/Spring-Module.xml");
    }

    public Object getBean(String nameBean){
        return context.getBean(nameBean);
    }
}
