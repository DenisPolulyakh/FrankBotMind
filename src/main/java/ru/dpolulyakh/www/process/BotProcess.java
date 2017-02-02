package ru.dpolulyakh.www.process;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.dpolulyakh.www.dao.cource.MessageDataBaseDAO;
import ru.dpolulyakh.www.pattern.factory.Processor;
import ru.dpolulyakh.www.pattern.factory.ProcessorFactory;
import ru.dpolulyakh.www.pattern.interpreter.Context;
import ru.dpolulyakh.www.pattern.interpreter.WordExpression;
import ru.dpolulyakh.www.spring.config.ApplicationContextConfig;
import ru.dpolulyakh.www.utils.BotUtilMethods;

import java.util.*;


/**
 * @author Denis Polulyakh
 */
@Component
public class BotProcess {
    private static final String CLASS_NAME = BotProcess.class.getName();
    private static final Logger log = Logger.getLogger(CLASS_NAME);

    @Autowired
    ProcessorFactory processorFactory;


    public Processor getProcessor(String message) {
        return processorFactory.getProcessor(message);
    }
}
