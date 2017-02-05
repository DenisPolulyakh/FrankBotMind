package ru.dpolulyakh.www.process;

import org.apache.log4j.Logger;
import ru.dpolulyakh.www.dao.message.MessageDataBaseDAO;
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
    private transient MessageDataBaseDAO messageDataBaseDAO;
    private static List<String> botQuestions = new ArrayList<String>();
    private static List<String> botAnswers = new ArrayList<String>();
    private static List<String> userAnswers = new ArrayList<String>();
    private static List<String> userAnswersexit = new ArrayList<String>();

    private Set<String> key = new HashSet<String>();
    private Set<String> value = new HashSet<String>();
    private String keyQuestionForOutputAnswer;

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

    public MemoryProcessor() {
    }

    public MemoryProcessor(String inputMessage) {
        this.inputMessage = inputMessage;

    }

    public MemoryProcessor(String inputMessage, MessageDataBaseDAO messageDataBaseDAO) {
        this.inputMessage = inputMessage;
        this.messageDataBaseDAO = messageDataBaseDAO;
    }

    public String getInputMessage() {
        return inputMessage;
    }

    public void setInputMessage(String inputMessage) {
        this.inputMessage = inputMessage;
    }

    public void setMessageDataBaseDAO(MessageDataBaseDAO messageDataBaseDAO) {
        this.messageDataBaseDAO = messageDataBaseDAO;
    }

    @Override
    public String getMessageToAnswer() {
        final String METHOD_NAME = "getMessageToAnswer";
        log.info(inputMessage);
        log.info("QUESTION " + numberQuestions);
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

        if ((text.toLowerCase().indexOf("выведи")) != -1) {
            String question = text.replaceAll("выведи", "");
            KeyQuestion keyQuestion = findQuestionToDB(question.trim());
            List<ValueAnswer> tempAnswer = new ArrayList<ValueAnswer>();
            if (keyQuestion != null) {
                tempAnswer = messageDataBaseDAO.listAnswersByKeyQuestion(keyQuestion.getQuestion());
                log.info("TempAnswer size " + tempAnswer.size());
                if (tempAnswer.size() == 0) {
                    numberQuestions = 0;
                    selfSafe();
                    return "Нет сообщений для данной ключевой фразы";

                } else {
                    if (tempAnswer.size() > 1) {
                        numberQuestions = 4;
                        keyQuestionForOutputAnswer = keyQuestion.getQuestion();
                        selfSafe();
                        return "У меня " + tempAnswer.size() + " сохраненных сообщений, соответствующих этому ключевому слову. Вывести их?";
                    } else {
                        numberQuestions = 0;
                        selfSafe();
                        return tempAnswer.get(0).getAnswer();
                    }
                }
            } else {
                numberQuestions = 0;
                selfSafe();
                return "Такой ключевой фразы не найдено";
            }
        }

        if (numberQuestions == 0) {
            messageToAnswer = "";
            messageToAnswer = botQuestions.get(numberQuestions);
            numberQuestions++;
            selfSafe();
            return messageToAnswer;
        }
        if (numberQuestions == 1) {
            messageToAnswer = "";
            text = text.toLowerCase();
            for (String answer : userAnswers) {
                if (text.indexOf("не " + answer) != -1 || text.indexOf(answer + " не") != -1) {
                    messageToAnswer = "Не хотите? Если все же хотите, наберите: да, yes, y, хочу. Для выхода из режима запоминания наберите: выход, exit, гудбай, чао";
                    selfSafe();
                    return messageToAnswer;
                }else{
                    messageToAnswer = "Для выхода из режима запоминания наберите: выход, exit, гудбай, чао";
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
            messageToAnswer = "Не хотите? Если все же хотите, наберите: да, yes, y, хочу. Для выхода из режима запоминания наберите: выход, exit, гудбай, чао";
            selfSafe();
            return messageToAnswer;
        }
        if (numberQuestions == 2) {
            messageToAnswer = "";
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
                i++;
            }
            messageToAnswer = messageToAnswer + "Cообещения (в том числе сохраненные ранее, если они есть): ";


            i = 0;
            for (String v : value) {
                if (i == value.size() - 1 && answersSet.size() == 0) {
                    messageToAnswer = messageToAnswer + v + ". ";
                } else {
                    messageToAnswer = messageToAnswer + v + ", ";
                }
                i++;
            }
            i = 0;
            for (ValueAnswer va : answersSet) {
                if (!value.contains(va.getAnswer())) {
                    if (i == answersSet.size() - 1) {
                        messageToAnswer = messageToAnswer + va.getAnswer() + ". ";
                    } else {
                        messageToAnswer = messageToAnswer + va.getAnswer() + ", ";
                    }
                }
                i++;
            }
            numberQuestions = 0;
            storeToDataBase();
            value.clear();
            selfSafe();
            return messageToAnswer;
        }
        if (numberQuestions == 4) {
            log.info("QUESTION 4");
            text = text.toLowerCase();
            messageToAnswer = "";
            List<ValueAnswer> tempAnswer = messageDataBaseDAO.listAnswersByKeyQuestion(keyQuestionForOutputAnswer);
            for (String answer : userAnswers) {
                if (text.indexOf(answer) != -1) {
                    int k = 0;
                    for (ValueAnswer va : tempAnswer) {
                        if (k == tempAnswer.size() - 1) {
                            messageToAnswer = messageToAnswer + va.getAnswer() + ".";
                        } else {
                            messageToAnswer = messageToAnswer + va.getAnswer() + ", ";
                        }
                        k++;
                    }
                    numberQuestions = 0;
                    selfSafe();
                    return messageToAnswer;
                }

            }


            numberQuestions = 0;
            selfSafe();
            return "";
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

        memoryProcessTable.setMemoryProcessor(BotUtilMethods.serializeObject(this).getBytes());

        log.info(memoryProcessTable.toString());
        messageDataBaseDAO.saveOrUpdate(memoryProcessTable);
    }

    private void storeToDataBase() {
        Set<ValueAnswer> answersSet = null;
        KeyQuestion keyQuestion = null;
        Set<ValueAnswer> valueToRemember = new HashSet<ValueAnswer>();
        log.info("STORTODATABASE");

        // Create set value to remembering
        for (String v : value) {
            log.info("Value " + v);

            ValueAnswer valueAnswer = new ValueAnswer();
            valueAnswer.setAnswer(v);
            valueToRemember.add(valueAnswer);

        }


        for (String k : key) {
            log.info("Key " + k);
            keyQuestion = findQuestionToDB(k);
            k = BotUtilMethods.replaseSymbols(k);
            if (keyQuestion == null) {
                keyQuestion = new KeyQuestion();
                answersSet = new HashSet<ValueAnswer>();
            } else {
                answersSet = new HashSet<ValueAnswer>(messageDataBaseDAO.listAnswersByKeyQuestion(k));
            }
            answersSet.addAll(valueToRemember);
            keyQuestion.setQuestion(k);
            keyQuestion.setValueAnswer(answersSet);
            messageDataBaseDAO.saveOrUpdate(keyQuestion);

        }
    }

    private KeyQuestion findQuestionToDB(String key) {
        log.info("Find key to DataBase");
        List<KeyQuestion> listKeyQuestion = messageDataBaseDAO.listKeyQuestion();
        for (KeyQuestion keyQquest : listKeyQuestion) {
            String question = keyQquest.getQuestion();
            //delete common symbols
            question = BotUtilMethods.replaseSymbols(question);
            int k = 0;
            for (String w : question.split(" ")) {
                log.info("Word: " + w);
                if (key.toLowerCase().contains(w.toLowerCase())) {
                    k++;
                }
            }
            log.info("Number of matches " + k);
            if (k == question.split(" ").length) {
                return keyQquest;
            }
            log.info("NULL");
        }
        return null;
    }


}
