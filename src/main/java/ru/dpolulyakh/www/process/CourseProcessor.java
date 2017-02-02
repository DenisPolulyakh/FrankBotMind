package ru.dpolulyakh.www.process;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import ru.dpolulyakh.www.dao.cource.MessageDataBaseDAO;
import ru.dpolulyakh.www.model.ValueAnswer;
import ru.dpolulyakh.www.pattern.factory.Processor;
import ru.dpolulyakh.www.pattern.interpreter.Context;
import ru.dpolulyakh.www.pattern.interpreter.DateExpression;
import ru.dpolulyakh.www.pattern.interpreter.WordExpression;

import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @author Denis Polulyakh
 *         18.01.2017.
 */

public class CourseProcessor implements Processor {
    private static final String CLASS_NAME = "CourseProcessor";
    @Autowired
    private
    DateExpression dateExpression;
    @Autowired
    WordExpression wordExpression;
    @Autowired
    MessageDataBaseDAO messageDataBaseDAO;
    Context context;
    String date = "";
    private String messageToAnswer;
    private final String DATE_FORMAT = "dd.MM.yyyy";
    private static String STATIC_DATE_FORMAT = "dd.MM.yyyy";
    private final String DATE_PATTERN = "\\d{1,2}\\.\\d{1,2}\\.\\d{4}";
    private final String DEFAULT_ANSWER = "Простите, я вас не понял :)";
    private final String CURRENCY_DEFAULT_ANSWER = "О такой валюте я ничего не знаю";
    private final String DATE_DEFAULT_ANSWER = "Неверный формат даты, введите дату в фоормате " + DATE_FORMAT;
    private static final Logger log = Logger.getLogger(CLASS_NAME);

    private static Map<String, String> dayWord = new HashMap<String, String>();

    static {
        dayWord.put("вчера", newDate(-1));
        dayWord.put("завтра", newDate(1));
        dayWord.put("позавчера", newDate(-2));
        dayWord.put("месяц назад", newDate(-30));
        dayWord.put("год назад", newDate(-365));

    }

    public CourseProcessor() {
    }


    public CourseProcessor(Context context) {
        this.context = context;
    }


    @Override
    public String getMessageToAnswer() {
        final String METHOD_NAME = "getMessageToAnswer";
        /*log.info(CLASS_NAME + " " + METHOD_NAME + " question: " + context);
        messageToAnswer = DEFAULT_ANSWER;
        dateExpression.setDateFormat(DATE_FORMAT);
        dateExpression.setPattern(DATE_PATTERN);
        dateExpression.interpret(context);

        if (dateExpression.isExpressionFind()) {
            if (dateExpression.isIncorrectedDate()) {
                return DATE_DEFAULT_ANSWER;
            }
            String date = dateExpression.getDateString();
        } else {

            Set<String> keySet = dayWord.keySet();
            for (String key : keySet) {
                wordExpression.setWord(key);
                wordExpression.interpret(context);
                if (wordExpression.isExpressionFind()) {
                    date = dayWord.get(key);
                    break;
                }
            }
        }
        if (date.equals("")) {
            date = newDate(0);
        }
        log.info("Find in table");
        List<ValueAnswer> listAnswers = messageDataBaseDAO.listAnswersByTypePhrase("currency");
        boolean isFind = false;
        for (ValueAnswer answers : listAnswers) {
            String[] word = answers.getAnswer().split(" ");
            ;
            int k = 0;
            for (String w : word) {
                log.info("Word: "+w);
                wordExpression.setWord(w);
                wordExpression.interpret(context);
                if (wordExpression.isExpressionFind()) {
                    k++;
                }
            }
            log.info("Number of matches "+k);
            if (k == word.length) {
                String code = answers.getKeyPhrase().getPhrase();
                log.info("PHRASE:"+ code);
                messageToAnswer = code;
                break;
            }

        }
*/
       log.info(CLASS_NAME +" "+METHOD_NAME +" "+messageToAnswer);
        return messageToAnswer;
}

    private static String newDate(int amount) {
        Date day = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, amount);
        SimpleDateFormat sdf = new SimpleDateFormat(STATIC_DATE_FORMAT);
        return sdf.format(calendar.getTime());
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

}
