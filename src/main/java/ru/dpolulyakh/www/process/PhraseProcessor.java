package ru.dpolulyakh.www.process;

import org.apache.log4j.Logger;
import ru.dpolulyakh.www.dao.message.MessageDataBaseDAO;
import ru.dpolulyakh.www.model.KeyQuestion;
import ru.dpolulyakh.www.model.MemoryProcessTable;
import ru.dpolulyakh.www.model.ValueAnswer;
import ru.dpolulyakh.www.pattern.factory.Processor;
import ru.dpolulyakh.www.service.StorageService;
import ru.dpolulyakh.www.utils.BotUtilMethods;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Denis Polulyakh
 *         18.01.2017.
 */

public class PhraseProcessor implements Processor {
    private static final String CLASS_NAME = "PhraseProcessor";
    private static final Logger log = Logger.getLogger(CLASS_NAME);
    private static final String DEFAULT_LOST_DATA="Хотел что-то сказать, но вылетело из головы :)";
    private static final String DEFAULT_ANSWER="Извините, я Вас не понял :)";
    private String message;
    private StorageService storageService;
    private Random rand = new Random();

     public PhraseProcessor() {
    }

    public PhraseProcessor(String inputJSONMessage) {
        message = inputJSONMessage;
    }


    public PhraseProcessor(String message, StorageService storageService) {
        this.message = message;
        this.storageService = storageService;
    }

    @Override
    public String getMessageToAnswer() {
        final String METHOD_NAME = "getMessageToAnswer";
        String text = BotUtilMethods.getPropertyFromJSON(message,"text");
        log.info(CLASS_NAME + " " + METHOD_NAME + " question: " + text);
        String [] question  =  text.split(" ");

        for(String q:question){
            if(q.length()>2){

            }
        }
        return DEFAULT_ANSWER;

       // log.info("Find in table");
        /*List<KeyQuestion> listKeyQuestion = messageDataBaseDAO.listKeyQuestion();
        for (KeyQuestion keyQquest : listKeyQuestion) {
            String question = keyQquest.getQuestion();
            //delete common symbols
            question=BotUtilMethods.replaseSymbols(question);
            int k = 0;
            for (String w : question.split(" ")) {
                log.info("Word: "+w);
                if (text.toLowerCase().contains(w.toLowerCase())) {
                    k++;
                }
            }
            log.info("Number of matches "+k);
            if (k == question.split(" ").length) {
                List<ValueAnswer> answerList = new ArrayList<ValueAnswer>(messageDataBaseDAO.listAnswersByKeyQuestion(keyQquest.getQuestion()));
                int qAnwers = answerList.size();
                log.info("ANSWERS: "+qAnwers);
                String answer ="";
                if(qAnwers>0){
                    if(qAnwers>1){
                        answer = new String(answerList.get(rand.nextInt(qAnwers)).getAnswer());
                    }else{
                        answer=new String(answerList.get(0).getAnswer());
                    }
                }
                if(answer.equals("")){
                    answer = DEFAULT_LOST_DATA;
                }

                log.info("PHRASE:"+ answer);*/
              //  return "";
           // }

     //   }


    }



    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MessageDataBaseDAO getMessageDataBaseDAO() {
        MessageDataBaseDAO messageDataBaseDAO = new MessageDataBaseDAO() {
            @Override
            public List<ValueAnswer> listAnswersByKeyQuestion(String keyQuestion) {
                return null;
            }

            @Override
            public List<KeyQuestion> keyQuestionByKey(String key) {
                return null;
            }

            @Override
            public List<KeyQuestion> listKeyQuestion() {
                return null;
            }

            @Override
            public List<ValueAnswer> listValueAnswer() {
                return null;
            }

            @Override
            public void saveOrUpdate(KeyQuestion keyQuestion) {

            }

            @Override
            public void saveOrUpdate(ValueAnswer valueAnswer) {

            }

            @Override
            public void saveOrUpdate(MemoryProcessTable memoryProcessTable) {

            }

            @Override
            public List<MemoryProcessTable> getMemoryProcessTable(String id) {
                return null;
            }

            @Override
            public void deleteMemoryProcessor(MemoryProcessTable memoryProcessTable) {

            }
        };
        return messageDataBaseDAO;
    }

    public void setStorageService(StorageService storageService) {
        this.storageService = storageService;
    }
}
