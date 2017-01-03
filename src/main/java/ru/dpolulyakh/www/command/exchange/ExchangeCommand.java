package ru.dpolulyakh.www.command.exchange;

import ru.dpolulyakh.www.entity.Currency;
import ru.dpolulyakh.www.entity.Message;
import ru.dpolulyakh.www.utils.BotUtilMethods;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Denis Polulyakh
 */
public class ExchangeCommand {
    public Message getCurrencyMessage(String charCode, String date){
        Map<String, Currency> currencyMap = new HashMap<String,Currency>();
        String phrase = "";
        currencyMap = BotUtilMethods.getMapCurrency(BotUtilMethods.getProperty("cbr.url")+date);
        phrase="Курс ЦБ РФ "+charCode+" на дату "+date+" "+currencyMap.get(charCode).getNominal()+" "+currencyMap.get(charCode).getCharCode()+"="+currencyMap.get(charCode).getValue()+" RUB";
        Message msg = new Message();
        msg.setPhrase(phrase);
        return msg;
    }
}
