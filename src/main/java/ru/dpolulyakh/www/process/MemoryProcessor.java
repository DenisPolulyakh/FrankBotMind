package ru.dpolulyakh.www.process;

import org.apache.log4j.Logger;
import ru.dpolulyakh.www.dao.cource.MessageDataBaseDAO;
import ru.dpolulyakh.www.model.KeyQuestion;
import ru.dpolulyakh.www.model.ValueAnswer;
import ru.dpolulyakh.www.model.MemoryProcessTable;
import ru.dpolulyakh.www.pattern.factory.Processor;
import ru.dpolulyakh.www.utils.BotUtilMethods;

import javax.sql.rowset.serial.SerialBlob;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Denis Polulyakh
 *         22.01.2017.
 */

public class MemoryProcessor implements Processor, Serializable {

    private static final String CLASS_NAME = "MemoryProcessor";
    private static final Logger log = Logger.getLogger(CLASS_NAME);
    private int numberQuestions = 0;
    private transient String inputMessage;

    public String getInputMessage() {
        return inputMessage;
    }

    public void setInputMessage(String inputMessage) {
        this.inputMessage = inputMessage;
    }

    public MemoryProcessor() {
    }

    public MemoryProcessor(String inputMessage) {
        this.inputMessage = inputMessage;

    }

    public void setMessageDataBaseDAO(MessageDataBaseDAO messageDataBaseDAO) {
        this.messageDataBaseDAO = messageDataBaseDAO;
    }

    private transient MessageDataBaseDAO messageDataBaseDAO;
    private static List<String> botQuestions = new ArrayList<String>();
    private static List<String> botAnswers = new ArrayList<String>();
    private static List<String> userAnswers = new ArrayList<String>();
    private static List<String> userAnswersexit = new ArrayList<String>();
    private List<String> key = new ArrayList<String>();
    private List<String> value = new ArrayList<String>();

    static {
        botQuestions.add("Я могу сохранить для вас любую информацию. Вы хотите что-то сохранить?");
        botQuestions.add("Какую информацию вы хотите сохранить? Введенное вами сообщение будет сохранено");
        botQuestions.add("По каким ключевым словам мне выводить Вам сохраненное сообщение? Введите их через запятую");
        userAnswers.add("да");
        userAnswers.add("yes");
        userAnswers.add("y");
        userAnswers.add("хочу");
        userAnswersexit.add("выход");
        userAnswersexit.add("exit");
        userAnswersexit.add("гудбай");
        userAnswersexit.add("чао");

    }


    private String messageToAnswer;
    private String id;
    private String userName;

    @Override
    public String getMessageToAnswer() {
        final String METHOD_NAME = "getMessageToAnswer";
        log.info(inputMessage);
        String address = BotUtilMethods.getPropertyFromJSON(inputMessage, "address");
        String user = BotUtilMethods.getPropertyFromJSON(address, "user");
        id = BotUtilMethods.getPropertyFromJSON(user, "id");
        userName = BotUtilMethods.getPropertyFromJSON(user, "name");
        log.info(userName);
        String text = BotUtilMethods.getPropertyFromJSON(inputMessage, "text");

        log.info(CLASS_NAME + " " + METHOD_NAME + " entry id=" + id);

        for (String exit : userAnswersexit) {
            if (text.indexOf(exit) != -1 && numberQuestions != 4) {
                messageToAnswer = "пока :)";
                numberQuestions = 0;
                selfDelete();
                return messageToAnswer;
            }

        }

        if (numberQuestions == 0) {
            log.info("QUESTION " + numberQuestions);
            messageToAnswer = botQuestions.get(numberQuestions);
            numberQuestions++;
            selfSafe();
            return messageToAnswer;
        }
        if (numberQuestions == 1) {
            text = text.toLowerCase();
            for (String answer : userAnswers) {
                if (text.indexOf("не " + answer) != -1 || text.indexOf(answer + " не") != -1) {
                    messageToAnswer = "Не хотите? Если все же хотите, наберите: да, yes, y, хочу. Для выхода из режима запоминания наберите: выход, exit, гудбай, чао";
                    selfSafe();
                    return messageToAnswer;
                }

            }
            for (String answer : userAnswers) {
                if (text.indexOf(answer) != -1) {
                    messageToAnswer = botQuestions.get(numberQuestions);
                    numberQuestions++;
                    selfSafe();
                    return messageToAnswer;
                }
            }
        }
        if (numberQuestions == 2) {
            value.add(text);
            messageToAnswer = botQuestions.get(numberQuestions);
            numberQuestions++;
            selfSafe();
            return messageToAnswer;
        }
        if (numberQuestions == 3) {

            String[] keys = text.split(",");
            messageToAnswer = "Я выведу по ключевым словам: ";
            int i = 0;
            Set<ValueAnswer> answersSet = new HashSet<ValueAnswer>();
            for (String k : keys) {
                key.add(k.trim());
                if (i == keys.length - 1) {
                    messageToAnswer = messageToAnswer + k + ". ";
                } else {
                    messageToAnswer = messageToAnswer + k + ", ";
                }

                answersSet.addAll(messageDataBaseDAO.listAnswersByKeyQuestion(k));

            }
            messageToAnswer = messageToAnswer + "Cообещения (в том числе сохраненные ранее, если они есть): ";


            i = 0;
            for (String v : value) {
                if (i == value.size() - 1 && answersSet.size() == 0) {
                    messageToAnswer = messageToAnswer + v + ". ";
                } else {
                    messageToAnswer = messageToAnswer + v + ", ";
                }
            }
            i = 0;
            for (ValueAnswer va : answersSet) {
                if (i == answersSet.size() - 1) {
                    messageToAnswer = messageToAnswer + va.getAnswer() + ". ";
                } else {
                    messageToAnswer = messageToAnswer + va.getAnswer() + ", ";
                }
            }
            messageToAnswer = messageToAnswer + "сохранить / выход ?";
            numberQuestions++;
            selfSafe();
            return messageToAnswer;
        }
        if (numberQuestions == 4) {

            if (text.indexOf("сохранить") != -1) {
                storeToDataBase();
                messageToAnswer = "Ваше сообщение сохранено!";
                numberQuestions = 0;
                selfSafe();
                return messageToAnswer;

            }

            for (String exit : userAnswersexit) {
                if (text.indexOf(exit) != -1) {
                    //storeToDataBase();
                    messageToAnswer = "пока :)";
                    numberQuestions = 0;
                    selfDelete();
                    return messageToAnswer;
                }
            }


            messageToAnswer = messageToAnswer + "Изменить/Удалить/Продолжить/Выход?";
            numberQuestions++;
            return messageToAnswer;
        }


        return null;
    }

    private void selfDelete() {
        //String id = BotUtilMethods.getPropertyFromJSON(BotUtilMethods.getPropertyFromJSON(inputMessage, "user"), "id");

        MemoryProcessTable memoryProcessTable = new MemoryProcessTable();
        memoryProcessTable.setIdUser(id);
        memoryProcessTable.setUserName(userName);


        messageDataBaseDAO.deleteMemoryProcessor(memoryProcessTable);
    }

    private void selfSafe() {


        MemoryProcessTable memoryProcessTable = new MemoryProcessTable();
        memoryProcessTable.setIdUser(id);
        memoryProcessTable.setUserName(userName);
        try {
            memoryProcessTable.setMemoryProcessor(new SerialBlob(BotUtilMethods.serializeObject(this).getBytes()));
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        log.info(memoryProcessTable.toString());
        messageDataBaseDAO.saveOrUpdate(memoryProcessTable);
    }

    private void storeToDataBase() {
        Set<ValueAnswer> answersSet = null;
        KeyQuestion keyQuestion = null;
        log.info("STORTODATABASE");

        for (String k : key) {
            log.info("Key " + k);
            keyQuestion = findQuestionToDB(k);
            if (keyQuestion == null) {
                keyQuestion = new KeyQuestion();
                answersSet = new HashSet<ValueAnswer>();
            } else {
                answersSet = new HashSet<ValueAnswer>(messageDataBaseDAO.listAnswersByKeyQuestion(k));
            }
            for (String v : value) {
                log.info("Value " + v);

                if (answersSet == null) {
                    answersSet = new HashSet<ValueAnswer>();
                }
                ValueAnswer valueAnswer = new ValueAnswer();
                valueAnswer.setAnswer(v);
                answersSet.add(valueAnswer);
            }
            keyQuestion.setQuestion(k);
            keyQuestion.setValueAnswer(answersSet);
            messageDataBaseDAO.saveOrUpdate(keyQuestion);

        }
    }

    private KeyQuestion findQuestionToDB(String key) {
        log.info("FIND KEY  TO DATABASE");
        List<KeyQuestion> listKeyQuestion = messageDataBaseDAO.listKeyQuestion();
        Set<ValueAnswer> setValueAnswers = null;
        for (KeyQuestion keyQquest : listKeyQuestion) {
            String question = keyQquest.getQuestion();
            //delete common symbols
            question = BotUtilMethods.replaseSymbols(question);
            int k = 0;
            for (String w : question.split(" ")) {
                log.info("Word: " + w);
                if (key.toLowerCase().indexOf(w.toLowerCase()) != -1) {
                    k++;
                }
            }

            if (k == question.split(" ").length) {
                return keyQquest;
            }
        }
        return null;
    }


}
