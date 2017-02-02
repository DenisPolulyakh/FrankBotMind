package ru.dpolulyakh.www.controller;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import ru.dpolulyakh.www.dao.cource.MessageDataBaseDAO;
import ru.dpolulyakh.www.entity.Message;
import ru.dpolulyakh.www.model.MemoryProcessTable;
import ru.dpolulyakh.www.process.BotProcess;
import ru.dpolulyakh.www.pattern.factory.Processor;
import ru.dpolulyakh.www.process.MemoryProcessor;

import javax.sql.rowset.serial.SerialBlob;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;

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
    //  MemoryProcessTable mpt = new MemoryProcessTable();
       // mpt.setIdUser("default-user");
     //   mpt.setUserName("F");
      //  mpt.setMemoryProcessor(new SerialBlob("String".getBytes()));
      //  messageDataBaseDAO.saveOrUpdate(mpt);
        //KeyPhrase keyPhrase = new KeyPhrase();
        // keyPhrase.setPhrase("USD");
        // keyPhrase.setTypePhrase("currency");
        //Set<ValueAnswer> set = new HashSet<ValueAnswer>();
        // ValueAnswer ans = new ValueAnswer();
        // ans.setAnswer("бакса");
        // ans.setKeyPhrase(keyPhrase);
        // keyPhrase.setAnswer(set);
        // messageDataBaseDAO.saveOrUpdate(keyPhrase);
        // messageDataBaseDAO.saveOrUpdate(ans);

        /*log.info("ENTRY "+CLASS_NAME+" " + messageJSON);
        List<KeyPhrase> lkp = messageDataBaseDAO.listKeyPhraseByTypePhrase("currency");
        for(KeyPhrase kp:lkp){
            log.info(kp.getPhrase()+" "+kp.getTypePhrase());
        }*/

        /*List<MemoryProcessTable> mpt = messageDataBaseDAO.getMemoryProcessTable("default-user");
        for(MemoryProcessTable mt:mpt){
            log.info(mt.getUserName()+" "+mt.getIdUser());
        }*/


        Processor processor = botProcessor.getProcessor(messageJSON);
        Message message = new Message();
        message.addPhrase(processor.getMessageToAnswer());
        log.info("EXIT " + CLASS_NAME);
        return message;
    }

}