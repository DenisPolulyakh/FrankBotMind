package ru.dpolulyakh.www.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.dpolulyakh.www.command.exchange.ExchangeCommand;
import ru.dpolulyakh.www.command.exchange.ExchangeCurrency;
import ru.dpolulyakh.www.command.exchange.ListCurrency;
import ru.dpolulyakh.www.command.exchange.ListCurrencyCommand;
import ru.dpolulyakh.www.datamodel.CustomerDAO;
import ru.dpolulyakh.www.datamodel.Stock;
import ru.dpolulyakh.www.datamodel.StockBo;
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
    private CustomerDAO customerDAO;




    @Autowired
    BotController(ExchangeCommand exchangeCommand, ExchangeCurrency exchangeCurrency, ListCurrency listCurrency, ListCurrencyCommand listCurrencyCommand){
        this.exchangeCommand=exchangeCommand;
        this.exchangeCurrency = exchangeCurrency;
        this.listCurrency = listCurrency;
        this.listCurrencyCommand = listCurrencyCommand;
        this.customerDAO=customerDAO;
    }

    @RequestMapping("/exchange")
    public Message exchange(@RequestParam(value = "name", required = false, defaultValue = "USD") String name, @RequestParam(value = "date", required = false, defaultValue = "27.12.2016") String date) {
        exchangeCurrency.setCommand(exchangeCommand);
        exchangeCurrency.setCharCode(name);
        exchangeCurrency.setDate(date);
        return  exchangeCurrency.execute();
    }
    @RequestMapping("/botmind")
    public Message exchange(@RequestParam(value = "message", required = false, defaultValue = "")String message) {

        ApplicationContext appContext =
                new ClassPathXmlApplicationContext("spring/config/BeanLocations.xml");
        StockBo stockBo = (StockBo)appContext.getBean("stockBo");

        /** insert **/
        Stock stock = new Stock();
        stock.setStockCode("7668");
        stock.setStockName("HAIO");
        stockBo.save(stock);

        return  null;
    }

}