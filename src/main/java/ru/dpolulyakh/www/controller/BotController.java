package ru.dpolulyakh.www.controller;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import ru.dpolulyakh.www.dao.message.MessageDataBaseDAO;
import ru.dpolulyakh.www.entity.Message;
import ru.dpolulyakh.www.process.BotProcess;
import ru.dpolulyakh.www.pattern.factory.Processor;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

;


/**
 * @author Denis Polulyakh
 */

@RestController
@EnableWebMvc
public class BotController {
    private static final String CLASS_NAME = BotController.class.getName();
    private static final Logger log = Logger.getLogger(CLASS_NAME);
    @Autowired
    BotProcess botProcessor;
    @Autowired
    Message message;

    @Autowired
    MessageDataBaseDAO messageDataBaseDAO;

    @RequestMapping(value = "/botmind", method = RequestMethod.GET)
    public Message getAnswer(@RequestParam(value = "message", required = false, defaultValue = "") String messageJSON) throws UnsupportedEncodingException, SQLException {
        final String METHOD_NAME = "getAnswer";
        Processor processor = botProcessor.getProcessor(messageJSON);
        Message message = new Message();
        message.addPhrase(processor.getMessageToAnswer());
        log.info("EXIT " + CLASS_NAME);
        return message;
    }

}