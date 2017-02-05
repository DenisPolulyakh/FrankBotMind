package ru.dpolulyakh.www.process;

import org.apache.log4j.Logger;
import ru.dpolulyakh.www.dao.message.MessageDataBaseDAO;
import ru.dpolulyakh.www.entity.Currency;
import ru.dpolulyakh.www.model.KeyQuestion;
import ru.dpolulyakh.www.pattern.factory.Processor;
import ru.dpolulyakh.www.pattern.interpreter.DateExpression;
import ru.dpolulyakh.www.utils.BotUtilMethods;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author Denis Polulyakh
 *         18.01.2017.
 */

public class CurrencyProcessor implements Processor {
    private static final String CLASS_NAME = "CurrencyProcessor";

    private DateExpression dateExpression;


    String date = "";
    String code = "";
    private String messageToAnswer;
    private static String DATE_FORMAT = "dd.MM.yyyy";
    private final String DATE_PATTERN = "\\d{1,2}\\.\\d{1,2}\\.\\d{4}";
    private final String DEFAULT_ANSWER = "Простите, я вас не понял :)";
    private final String CURRENCY_DEFAULT_ANSWER = "О такой валюте я ничего не знаю";
    private final String DATE_DEFAULT_ANSWER = "Неверный формат даты, введите дату в фоормате " + DATE_FORMAT;
    private static final Logger log = Logger.getLogger(CLASS_NAME);
    Map<String, Currency> currencyMap = new HashMap<String, Currency>();

    private String inputMessage;
    private MessageDataBaseDAO messageDataBaseDAO;


    private static Map<String, String> dayWord = new HashMap<String, String>();


    public CurrencyProcessor() {
        initDayArray();

    }

    public CurrencyProcessor(String inputMessage) {
        this.inputMessage = inputMessage;
        initDayArray();
    }

    public CurrencyProcessor(String inputMessage, MessageDataBaseDAO messageDataBaseDAO) {
        this.inputMessage = inputMessage;
        this.messageDataBaseDAO = messageDataBaseDAO;
        initDayArray();
    }

    public MessageDataBaseDAO getMessageDataBaseDAO() {
        return messageDataBaseDAO;
    }

    public void setMessageDataBaseDAO(MessageDataBaseDAO messageDataBaseDAO) {
        this.messageDataBaseDAO = messageDataBaseDAO;
    }

    public String getInputMessage() {
        return inputMessage;
    }

    public void setInputMessage(String inputMessage) {
        this.inputMessage = inputMessage;
    }

    private void initDayArray() {
        dayWord.put("вчера", newDate(-1));
        dayWord.put("завтра", newDate(1));
        dayWord.put("позавчера", newDate(-2));
        dayWord.put("месяц назад", newDate(-30));
        dayWord.put("неделю назад", newDate(-7));
        dayWord.put("год назад", newDate(-365));
    }

    @Override
    public String getMessageToAnswer() {
        final String METHOD_NAME = "getMessageToAnswer";
        String text = BotUtilMethods.getPropertyFromJSON(inputMessage, "text");
        log.info(CLASS_NAME + " " + METHOD_NAME + " question: " + text);
        messageToAnswer = DEFAULT_ANSWER;


        if (!findDateInText(text).equals("")) {
            String date = dateExpression.getDateString();
        } else {

            Set<String> keySet = dayWord.keySet();
            for (String key : keySet) {

                if (text.toLowerCase().indexOf(key.toLowerCase()) != -1) {
                    date = dayWord.get(key);
                    break;
                }
            }
        }
        if (date.equals("")) {
            date = newDate(0);
        }
        log.info("Find in datebase");
        List<KeyQuestion> listKeyQuestion = messageDataBaseDAO.listKeyQuestion();
        for (KeyQuestion keyQquest : listKeyQuestion) {
            String question = keyQquest.getQuestion();
            //delete common symbols
            question = BotUtilMethods.replaseSymbols(question);
            int k = 0;
            for (String w : question.split(" ")) {
                log.info("Word: " + w);
                if (text.toLowerCase().indexOf(w.toLowerCase()) != -1) {
                    k++;
                }
            }
            log.info("Number of matches " + k);
            if (k == question.split(" ").length) {
                code = messageDataBaseDAO.listAnswersByKeyQuestion(keyQquest.getQuestion()).get(0).getAnswer();
                log.info("CODE CURRENCY:" + code);
                break;
            }
        }
        if (!code.equals("")) {
            currencyMap = BotUtilMethods.getMapCurrency(BotUtilMethods.getProperty("cbr.url") + date);
            if (currencyMap.get(code) == null) {
                messageToAnswer = "Информации по данной валюте в ЦБ нет";
            } else {
                messageToAnswer = "Курс ЦБ РФ " + code + " на дату " + date + " " + currencyMap.get(code).getNominal() + " " + currencyMap.get(code).getCharCode() + "=" + currencyMap.get(code).getValue() + " RUB";
            }

        }
        return messageToAnswer;
    }

    private String newDate(int amount) {
        Date day = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, amount);
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(calendar.getTime());
    }


    private String findDateInText(String text) {

        String dateString = "";
        log.info("TEXT " + text);
        //get dateString
        Matcher matcher = Pattern.compile(DATE_PATTERN).matcher(text);
        if (!matcher.find()) {
            return "";
        }
        dateString = text.substring(matcher.start(), matcher.end());
        log.info("dateString: " + dateString + ";");

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        sdf.setLenient(false);
        try {
            sdf.parse(dateString);
        } catch (ParseException e) {
            log.info("Incorrect Date: " + dateString);
            return "";
        }
        return dateString;
    }

}
