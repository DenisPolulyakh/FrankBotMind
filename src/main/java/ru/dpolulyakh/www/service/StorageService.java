package ru.dpolulyakh.www.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import ru.dpolulyakh.www.dao.message.MessageDataBaseDAO;
import ru.dpolulyakh.www.dao.message.MessageDataBaseDAOImpl;
import ru.dpolulyakh.www.model.KeyQuestion;
import ru.dpolulyakh.www.model.MemoryProcessTable;
import ru.dpolulyakh.www.model.ValueAnswer;
import ru.dpolulyakh.www.utils.BotUtilMethods;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Denis Polulyakh
 *         09.02.2017.
 */
public class StorageService implements StorageServiceInterface {
    private static final String CLASS_NAME = "StorageService";
    private static final Logger log = Logger.getLogger(CLASS_NAME);

    MessageDataBaseDAO messageDataBaseDAO;

    public StorageService() {
        messageDataBaseDAO = new MessageDataBaseDAOImpl() ;
    }



    @Override
    public KeyQuestion findToStorage(String key) {
        log.info("findToStorage key=" + key);
        Set<ValueAnswer> answersSet = null;
        List<KeyQuestion> listKeyQuestion = messageDataBaseDAO.listKeyQuestion();
        KeyQuestion keyQuestion = listKeyQuestion.get(0);
        log.info("KeyQuestion=" + keyQuestion);
        return keyQuestion;
    }

    @Override
    public void deleteToStorage(KeyQuestion key) {

    }

    @Override
    public boolean addToStorage(KeyQuestion key) {
        log.info("findToStorage key=" + key);
        if (key == null) {
            log.error("Entity not saved to Database, key is null");
            return false;
        }
        Set<ValueAnswer> answersSet = null;
        KeyQuestion keyQuestion = null;
        Set<ValueAnswer> valueToRemember = key.getValueAnswer();

        String question = key.getQuestion();
        question = BotUtilMethods.replaseSymbols(question);
        question = question.toLowerCase();
        if (question.equals("")) {
            log.error("Entity not saved to Database, key is empty");
            return false;
        }
        KeyQuestion kQ = findToStorage(question);
        if (kQ != null) {
            answersSet = kQ.getValueAnswer();
            answersSet.addAll(valueToRemember);
            kQ.setValueAnswer(answersSet);
        } else {
            kQ = new KeyQuestion();
            kQ.setValueAnswer(valueToRemember);
        }
        messageDataBaseDAO.saveOrUpdate(kQ);
        return true;
    }

    @Override
    public List<ValueAnswer> getAnswersByQuestion(String key) {
        return messageDataBaseDAO.listAnswersByKeyQuestion(key);
    }

    @Override
    public void saveOrUpdate(MemoryProcessTable memoryProcessTable) {
        messageDataBaseDAO.saveOrUpdate(memoryProcessTable);
    }

    @Override
    public void deleteMemoryProcessor(MemoryProcessTable memoryProcessTable) {
        messageDataBaseDAO.deleteMemoryProcessor(memoryProcessTable);
    }

    @Override
    public List<MemoryProcessTable> getMemoryProcessTable(String id) {
        return messageDataBaseDAO.getMemoryProcessTable(id);
    }
}
