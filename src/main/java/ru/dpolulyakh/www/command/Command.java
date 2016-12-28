package ru.dpolulyakh.www.command;

import ru.dpolulyakh.www.entity.Message;
import ru.dpolulyakh.www.entity.Valute;
import ru.dpolulyakh.www.utils.BotConstants;
import ru.dpolulyakh.www.utils.BotUtilMethods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Денис on 27.12.2016.
 */
public class Command {
    public Message getValuteMessage(String charCode, String date){
        Map<String, Valute> valuteMap = new HashMap<String,Valute>();
        ArrayList<String> phrase = new ArrayList<String>();
        valuteMap = BotUtilMethods.getMapValute(BotConstants.CBR_URL+date);
        phrase.add("Курс ЦБ РФ "+charCode+" на дату "+date);
        phrase.add(valuteMap.get(charCode).getNominal()+" "+valuteMap.get(charCode).getCharCode()+"="+valuteMap.get(charCode).getValue()+" RUB");
        Message msg = new Message();
        msg.setPhrase(phrase);
        return msg;
    }
}
