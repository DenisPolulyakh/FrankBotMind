package ru.dpolulyakh.www.controller;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.dpolulyakh.www.command.exchange.ExchangeCommand;
import ru.dpolulyakh.www.command.exchange.ExchangeCurrency;
import ru.dpolulyakh.www.context.XmlApplicationContext;
import ru.dpolulyakh.www.entity.Message;
import ru.dpolulyakh.www.process.BotProcessor;

import java.io.UnsupportedEncodingException;

;


/**
 * @author  Denis Polulyakh
 */

@RestController
public class BotController {
    private static final String CLASS_NAME = BotController.class.getName();
    private static final Logger log = Logger.getLogger(CLASS_NAME);
    @Autowired
    private XmlApplicationContext context;

    @RequestMapping("/exchange")
    public Message exchange(@RequestParam(value = "name", required = false, defaultValue = "USD") String name, @RequestParam(value = "date", required = false, defaultValue = "27.12.2016") String date) {
        ExchangeCurrency exchangeCurrency = (ExchangeCurrency) context.getBean("exchangeCurrencyBean");
        ExchangeCommand exchangeCommand =(ExchangeCommand) context.getBean("exchangeCommand");
        exchangeCurrency.setCommand(exchangeCommand);
        exchangeCurrency.setCharCode(name);
        exchangeCurrency.setDate(date);
        return  null;
    }
    @RequestMapping("/botmind")
    public Message getAnswer(@RequestParam(value = "message", required = false, defaultValue = "")String messageJSON) throws UnsupportedEncodingException {
        final String METHOD_NAME = "getAnswer";
        log.info("ENTRY "+messageJSON);
        BotProcessor botProcessor = (BotProcessor) context.getBean("botProcessor");
        Message answer = (Message) context.getBean("message");
        answer.resetPhrase();
        botProcessor.setJsonRequest(messageJSON);
        answer.addPhrase(botProcessor.getAnswer());
        log.info("EXIT");
        return  answer;
    }

}