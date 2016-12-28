package ru.dpolulyakh.www.controller;


import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.dpolulyakh.www.command.Command;
import ru.dpolulyakh.www.command.Currency;
import ru.dpolulyakh.www.entity.Message;
import ru.dpolulyakh.www.entity.Valute;

@RestController
public class BotController {

    @RequestMapping("/exchange")
    public Message exchange(@RequestParam(value = "name", required = false, defaultValue = "USD") String name, @RequestParam(value = "date", required = false, defaultValue = "27.12.2016") String date) {
        Command command = new Command();
        Currency cur = new Currency(command);
        cur.setCharCode(name);
        cur.setDate(date);
        cur.execute();
        return cur.getMessage();

    }
}