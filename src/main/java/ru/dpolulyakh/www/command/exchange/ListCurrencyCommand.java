package ru.dpolulyakh.www.command.exchange;

import ru.dpolulyakh.www.entity.Currency;
import ru.dpolulyakh.www.entity.Message;
import ru.dpolulyakh.www.utils.BotUtilMethods;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author  Denis Polulyakh
 */
public class ListCurrencyCommand {
    public Message getListCurrency(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Map<String, Currency> currencyMap = new HashMap<String,Currency>();
        Set<String> codeSet = new HashSet<String>();
        Set<String> nameSet = new HashSet<String>();
        String phrase = "";
        String date = sdf.format(new Date());
        currencyMap = BotUtilMethods.getMapCurrency(BotUtilMethods.getProperty("cbr.url")+date);
        for(String key: currencyMap.keySet()){
           codeSet.add(currencyMap.get(key).getCharCode());
           nameSet.add(currencyMap.get(key).getName());
        }
        Message msg = new Message();
        msg.setCodeCurrency(codeSet);
        msg.setNameCurrency(nameSet);
        return msg;
    }
}
