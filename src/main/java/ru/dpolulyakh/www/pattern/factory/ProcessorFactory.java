package ru.dpolulyakh.www.pattern.factory;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.dpolulyakh.www.dao.cource.MessageDataBaseDAO;
import ru.dpolulyakh.www.dao.message.MessageBotDAO;
import ru.dpolulyakh.www.model.MemoryProcessTable;
import ru.dpolulyakh.www.process.MemoryProcessor;
import ru.dpolulyakh.www.process.PhraseProcessor;
import ru.dpolulyakh.www.spring.config.ApplicationContextConfig;
import ru.dpolulyakh.www.utils.BotUtilMethods;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Denis Polulyakh
 *         24.01.2017.
 */
@Component
public class ProcessorFactory extends BotFactory {
    private static final String CLASS_NAME = "ProcessorFactory";
    private static final Logger log = Logger.getLogger(CLASS_NAME);
    @Autowired
    ApplicationContextConfig applicationContextConfig;
    @Autowired
    MessageDataBaseDAO messageDataBaseDAO;
    private static List<String> commandMemoryList = new ArrayList<String>();

    static {
        commandMemoryList.add("save");
        commandMemoryList.add("создай");
        commandMemoryList.add("создать");
        commandMemoryList.add("сохранить");
        commandMemoryList.add("сохрани");
        commandMemoryList.add("запомни");
        commandMemoryList.add("запомнить");
    }


    @Override
    public Processor getProcessor(String inputJSONMessage) {
        log.info(inputJSONMessage);

        //  Memory processor
        String id = BotUtilMethods.getPropertyFromJSON(BotUtilMethods.getPropertyFromJSON(BotUtilMethods.getPropertyFromJSON(inputJSONMessage, "address"), "user"), "id");
        log.info(id);
        List<MemoryProcessTable> memoryProcessorTable = messageDataBaseDAO.getMemoryProcessTable(id);
        log.info("Memory Process Table size "+memoryProcessorTable.size());
        MemoryProcessor memoryProcessor=null;
        if(memoryProcessorTable!=null&&memoryProcessorTable.size()==0) {
            log.info("Not memory process in database. May be create new MemorProcess object");
            for (String command : commandMemoryList) {
                if (inputJSONMessage.indexOf(command) != -1) {
                    log.info(inputJSONMessage);
                    memoryProcessor = new MemoryProcessor();
                    memoryProcessor.setInputMessage(inputJSONMessage);
                    memoryProcessor.setMessageDataBaseDAO(messageDataBaseDAO);
                    return memoryProcessor;
                }

            }

        }else{
            log.info("Memory Process is find. Desearize MemoryProcess object");
            Blob blob = ((MemoryProcessTable)memoryProcessorTable.get(0)).getMemoryProcessor();
            try {
                String memoryProcessorString= new String(blob.getBytes(1, (int) blob.length()));
                log.info("String from base "+memoryProcessorString);
                memoryProcessor = (MemoryProcessor) BotUtilMethods.deserializeObject(memoryProcessorString);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            memoryProcessor.setInputMessage(inputJSONMessage);
            memoryProcessor.setMessageDataBaseDAO(messageDataBaseDAO);
            return memoryProcessor;

        }
        //Phrase processor
        PhraseProcessor phraseProcessor = new PhraseProcessor(inputJSONMessage);
        phraseProcessor.setMessageDataBaseDAO(messageDataBaseDAO);
        return  phraseProcessor;
    }
}
