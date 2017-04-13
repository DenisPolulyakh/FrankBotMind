package ru.dpolulyakh.www.pattern.factory;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.dpolulyakh.www.dao.message.MessageDataBaseDAO;
import ru.dpolulyakh.www.model.MemoryProcessTable;
import ru.dpolulyakh.www.process.CurrencyProcessor;
import ru.dpolulyakh.www.process.MemoryProcessor;
import ru.dpolulyakh.www.process.PhraseProcessor;
import ru.dpolulyakh.www.service.StorageService;
import ru.dpolulyakh.www.spring.config.ApplicationContextConfig;
import ru.dpolulyakh.www.utils.BotUtilMethods;

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
    StorageService storageService;
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
        String text = BotUtilMethods.getPropertyFromJSON(inputJSONMessage, "text");
        //CurrencyProcessor
       /* if (text.toLowerCase().indexOf("курс") != -1) {
            log.info("CURRENCYPROCESSOR");
            return new CurrencyProcessor(inputJSONMessage, messageDataBaseDAO);
        }*/
        //  Memory processor
        String id = BotUtilMethods.getPropertyFromJSON(BotUtilMethods.getPropertyFromJSON(BotUtilMethods.getPropertyFromJSON(inputJSONMessage, "address"), "user"), "id");
        log.info(id);
        List<MemoryProcessTable> memoryProcessorTable = storageService.getMemoryProcessTable(id);
        log.info("Memory Process Table size " + memoryProcessorTable.size());
        MemoryProcessor memoryProcessor = null;
        if (memoryProcessorTable != null && memoryProcessorTable.size() == 0) {
            log.info("Not memory process in database. May be create new MemorProcess object");
            for (String command : commandMemoryList) {
                if (text.toLowerCase().indexOf(command.toLowerCase()) != -1) {

                    return new MemoryProcessor(inputJSONMessage, new StorageService());

                }

            }

        } else {
            log.info("Memory Process is find. Desearize MemoryProcess object");
            byte[] memoryProcessorByte = ((MemoryProcessTable) memoryProcessorTable.get(0)).getMemoryProcessor();
           
            String memoryProcessorString = new String(memoryProcessorByte);
            log.info("String from base " + memoryProcessorString);
            memoryProcessor = (MemoryProcessor) BotUtilMethods.deserializeObject(memoryProcessorString);


            memoryProcessor.setInputMessage(inputJSONMessage);
            memoryProcessor.setStorageService(new StorageService());
            return memoryProcessor;

        }
        //Phrase processor
       // return new PhraseProcessor(inputJSONMessage, new StorageService());
        return memoryProcessor;
    }
}
