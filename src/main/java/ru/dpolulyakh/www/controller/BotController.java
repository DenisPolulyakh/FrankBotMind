package ru.dpolulyakh.www.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.dpolulyakh.www.command.exchange.ExchangeCommand;
import ru.dpolulyakh.www.command.exchange.ExchangeCurrency;
import ru.dpolulyakh.www.command.exchange.ListCurrency;
import ru.dpolulyakh.www.command.exchange.ListCurrencyCommand;
import ru.dpolulyakh.www.entity.Message;

/**
 * @author  Denis Polulyakh
 */

@RestController
public class BotController {

    private ExchangeCommand exchangeCommand;
    private ExchangeCurrency exchangeCurrency;
    private ListCurrency listCurrency;
    private  ListCurrencyCommand listCurrencyCommand;


    @Autowired
    BotController(ExchangeCommand exchangeCommand, ExchangeCurrency exchangeCurrency, ListCurrency listCurrency, ListCurrencyCommand listCurrencyCommand){
        this.exchangeCommand=exchangeCommand;
        this.exchangeCurrency = exchangeCurrency;
        this.listCurrency = listCurrency;
        this.listCurrencyCommand = listCurrencyCommand;
    }

    @RequestMapping("/exchange")
    public Message exchange(@RequestParam(value = "name", required = false, defaultValue = "USD") String name, @RequestParam(value = "date", required = false, defaultValue = "27.12.2016") String date) {
        exchangeCurrency.setCommand(exchangeCommand);
        exchangeCurrency.setCharCode(name);
        exchangeCurrency.setDate(date);
        return  exchangeCurrency.execute();
    }
    @RequestMapping("/listcurrency")
    public Message exchange() {
        listCurrency.setCommand(listCurrencyCommand);
        return  listCurrency.execute();
    }

}