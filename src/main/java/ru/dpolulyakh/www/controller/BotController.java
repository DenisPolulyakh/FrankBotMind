package ru.dpolulyakh.www.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.dpolulyakh.www.command.Command;
import ru.dpolulyakh.www.command.ExchangeCurrency;
import ru.dpolulyakh.www.entity.Message;

/**
 * @author  Denis Polulyakh
 */

@RestController
public class BotController {

    private Command command;
    private ExchangeCurrency exchangeCurrency;

    @Autowired
    BotController(Command command,ExchangeCurrency exchangeCurrency){
        this.command=command;
        this.exchangeCurrency=exchangeCurrency;
    }

    @RequestMapping("/exchange")
    public Message exchange(@RequestParam(value = "name", required = false, defaultValue = "USD") String name, @RequestParam(value = "date", required = false, defaultValue = "27.12.2016") String date) {
        exchangeCurrency.setCommand(command);
        exchangeCurrency.setCharCode(name);
        exchangeCurrency.setDate(date);
        return  exchangeCurrency.execute();
    }
}