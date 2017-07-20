package ru.dpolulyakh.www.process;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.dpolulyakh.www.command.exchange.ExchangeCommand;
import ru.dpolulyakh.www.command.exchange.ExchangeCurrency;
import ru.dpolulyakh.www.dao.cource.NameCurrencyDAO;
import ru.dpolulyakh.www.dao.message.MessageBotDAO;
import ru.dpolulyakh.www.dao.message.MessageBotStorage;
import ru.dpolulyakh.www.process.interpreter.CodeOrNameCurrencyExpression;
import ru.dpolulyakh.www.process.interpreter.Context;
import ru.dpolulyakh.www.process.interpreter.DateExpression;
import ru.dpolulyakh.www.process.interpreter.WordCourseExpression;
import ru.dpolulyakh.www.utils.BotUtilMethods;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * @author Denis Polulyakh
 */

public class BotProcessor {
    private static final String CLASS_NAME = BotProcessor.class.getName();
    private static final Logger log = Logger.getLogger(CLASS_NAME);
    private String message;
    private ApplicationContext context;
    private final String DATE_FORMAT = "dd.MM.yyyy";
    private final String DEFAULT_ANSWER = "Не знаю )";
    private final String CURRENCY_DEFAULT_ANSWER = "О такой валюте я ничего не знаю";
    private final String DATE_DEFAULT_ANSWER = "Неверный формат даты, введите дату в фоормате " + DATE_FORMAT;

    private String code;
    private String date;


    public void setJsonRequest(String message) {
        this.message = message;
    }


    public String getAnswer() {
        String result = DEFAULT_ANSWER;
        //Get user message from json
        String text = BotUtilMethods.getPropertyFromJSON(message, "text");
        log.info(text);
        //Analyse message


        ApplicationContext context = new ClassPathXmlApplicationContext("/spring/Spring-Module.xml");
        Context contextMessage = new Context(text);
        WordCourseExpression wce = (WordCourseExpression) context.getBean("wordExpression");
        // load all code and names from database
        NameCurrencyDAO codeNameDao = (NameCurrencyDAO) context.getBean("currencyDAO");
        CodeOrNameCurrencyExpression currencyExpression = (CodeOrNameCurrencyExpression) context.getBean("currencyExpression");
        DateExpression dateExpression = (DateExpression) context.getBean("dateExpression");
        dateExpression.setPattern("\\d{1,2}\\.\\d{1,2}\\.\\d{4}");
        dateExpression.setDateFormat("dd.MM.yyyy");
        wce.setWord("курс");
        wce.interpret(contextMessage);
        //word 'курс'
        if (wce.isExpressionFind()) {

            currencyExpression.setCodeNameCurrency(codeNameDao.load());
            currencyExpression.interpret(contextMessage);
            if (currencyExpression.isExpressionFind()) {
                code = currencyExpression.getCode();
            } else {
                return CURRENCY_DEFAULT_ANSWER;
            }
            dateExpression.interpret(contextMessage);

            if (!dateExpression.isExpressionFind()) {

                date = newDate(0);
                wce.setWord("завтра");
                wce.interpret(contextMessage);
                if(wce.isExpressionFind()){
                    date = newDate(1);
                }
                wce.setWord("вчера");
                wce.interpret(contextMessage);
                if(wce.isExpressionFind()){
                    date = newDate(-1);
                }

                wce.setWord("неделю назад");
                wce.interpret(contextMessage);
                if(wce.isExpressionFind()){
                    date = newDate(-7);
                }
                wce.setWord("месяц назад");
                wce.interpret(contextMessage);
                if(wce.isExpressionFind()){
                    date = newDate(-30);
                }
                wce.setWord("через год");
                wce.interpret(contextMessage);
                if(wce.isExpressionFind()){
                   return "Ага, щас... в будущее загляну и через год ты будешь богат";
                }
                wce.setWord("через месяц");
                wce.interpret(contextMessage);
                if(wce.isExpressionFind()){
                    return "Ага, щас... в будущее загляну и через месяц ты будешь богат";
                }

            } else {
                date = dateExpression.getDateString();
            }
            if (dateExpression.isIncorrectedDate()) {
                return DATE_DEFAULT_ANSWER;
            }

            log.info("DATE=" + date);
            ExchangeCommand exchangeCommand = (ExchangeCommand) context.getBean("exchangeCommand");
            ExchangeCurrency exchangeCurrency = (ExchangeCurrency) context.getBean("exchangeCurrency");
            exchangeCurrency.setCommand(exchangeCommand);
            exchangeCurrency.setCharCode(code);
            exchangeCurrency.setDate(date);
            return exchangeCurrency.execute();

        } else {
            MessageBotDAO messageBotDAO = (MessageBotDAO) context.getBean("messageBotDAO");
            // Message answer = (Message) context.getBean("message");
            MessageBotStorage messageBotStorage = messageBotDAO.findByQuestion(text);
            if (messageBotStorage != null) {
                return messageBotStorage.getAnswer();
            } else {
                return DEFAULT_ANSWER;
            }
        }





    }
    private String newDate(int amount){
        Date day = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR,amount);
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return  sdf.format(calendar.getTime());
    }

}
