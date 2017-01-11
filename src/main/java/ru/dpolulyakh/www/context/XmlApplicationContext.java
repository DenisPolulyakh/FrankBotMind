package ru.dpolulyakh.www.context;

/**
 * @author  Denis Polulaykh
 */

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
public class XmlApplicationContext {

    private String pathXmlConfiguration;
    private ApplicationContext context;

    public void setPathXmlConfiguration(String pathXmlConfiguration) {
        this.pathXmlConfiguration = pathXmlConfiguration;
        context = new ClassPathXmlApplicationContext(pathXmlConfiguration);
    }

    public Object getBean(String nameBean){
       return context.getBean(nameBean);
    }



}
