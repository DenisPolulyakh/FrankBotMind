package ru.dpolulyakh.www.command;

import ru.dpolulyakh.www.entity.Currency;
import ru.dpolulyakh.www.entity.Message;
import ru.dpolulyakh.www.utils.BotUtilMethods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Denis Polulyakh
 */
public class Command {
    public Message getCurrencyMessage(String charCode, String date){
        Map<String, Currency> currencyMap = new HashMap<String,Currency>();
        ArrayList<String> phrase = new ArrayList<String>();
        currencyMap = BotUtilMethods.getMapCurrency(BotUtilMethods.getProperty("cbr.url")+date);
        phrase.add("Курс ЦБ РФ "+charCode+" на дату "+date+" ");
        phrase.add(currencyMap.get(charCode).getNominal()+" "+currencyMap.get(charCode).getCharCode()+"="+currencyMap.get(charCode).getValue()+" RUB");
        Message msg = new Message();
        msg.setPhrase(phrase);
        return msg;
    }
}
