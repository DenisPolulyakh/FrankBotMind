package ru.dpolulyakh.www.process.interpreter;

import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Denis Polulyakh
 *         11.01.2017
 */
public class DateExpression implements Expression {

    private static final String CLASS_NAME = DateExpression.class.getName();
    private static final Logger log = Logger.getLogger(CLASS_NAME);
    private String pattern = "\\d{1,2}\\.\\d{1,2}\\.\\d{4}";
    private String dateFormat = new String("dd.MM.yyyy");
    private boolean isExpressionFind = false;
    private boolean isIncorrectedDate = false;
    private String dateString = "";

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public boolean isIncorrectedDate() {
        return isIncorrectedDate;
    }

    public String getDateString() {
        return dateString;
    }

    public boolean isExpressionFind() {
        return isExpressionFind;
    }

    @Override
    public void interpret(Context context) {
        isIncorrectedDate=false;
        isExpressionFind=false;
        String input = context.getInput();
        log.info("DATE "+input);
        //get dateString
        Matcher matcher = Pattern.compile(pattern).matcher(input);
        if (!matcher.find()) {
            return;
        }
        dateString = input.substring(matcher.start(), matcher.end());
        log.info("dateString: " + dateString + ";");

        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setLenient(false);
        try {
            sdf.parse(dateString);
        } catch (ParseException e) {
            log.info("Incorrect Date: " + dateString);
            isIncorrectedDate = true;
        }
        isExpressionFind = true;
    }
}
