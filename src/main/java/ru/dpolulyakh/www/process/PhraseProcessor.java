package ru.dpolulyakh.www.process;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.dpolulyakh.www.dao.cource.MessageDataBaseDAO;
import ru.dpolulyakh.www.model.KeyQuestion;
import ru.dpolulyakh.www.model.ValueAnswer;
import ru.dpolulyakh.www.pattern.factory.Processor;
import ru.dpolulyakh.www.utils.BotUtilMethods;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Denis Polulyakh
 *         18.01.2017.
 */

public class PhraseProcessor implements Processor {
    private static final String CLASS_NAME = "PhraseProcessor";
    private static final Logger log = Logger.getLogger(CLASS_NAME);
    private String message;
    private MessageDataBaseDAO messageDataBaseDAO;

     public PhraseProcessor() {
    }

    public PhraseProcessor(String inputJSONMessage) {
        message = inputJSONMessage;
    }

    @Override
    public String getMessageToAnswer() {
        final String METHOD_NAME = "getMessageToAnswer";
        String text = BotUtilMethods.getPropertyFromJSON(message,"text");
        log.info(CLASS_NAME + " " + METHOD_NAME + " question: " + text);
        log.info("Find in table");
        List<KeyQuestion> listKeyQuestion = messageDataBaseDAO.listKeyQuestion();
        for (KeyQuestion keyQquest : listKeyQuestion) {
            String question = keyQquest.getQuestion();
            //delete common symbols
            question=BotUtilMethods.replaseSymbols(question);
            int k = 0;
            for (String w : question.split(" ")) {
                log.info("Word: "+w);
                if (text.toLowerCase().indexOf(w.toLowerCase())!=-1) {
                    k++;
                }
            }
            log.info("Number of matches "+k);
            if (k == question.split(" ").length) {
                List<ValueAnswer> answerList = new ArrayList<ValueAnswer>(messageDataBaseDAO.listAnswersByKeyQuestion(question));
                log.info("ANSWERS: "+answerList.size());
                String answer ="";
                for(ValueAnswer ans:answerList) {
                    answer = answer+ans.getAnswer()+"; ";
                }
                log.info("PHRASE:"+ answer);
                return answer;
            }

        }

        return "Извините, я Вас не понял :)";
    }



    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MessageDataBaseDAO getMessageDataBaseDAO() {
        return messageDataBaseDAO;
    }

    public void setMessageDataBaseDAO(MessageDataBaseDAO messageDataBaseDAO) {
        this.messageDataBaseDAO = messageDataBaseDAO;
    }
}
