package ru.dpolulyakh.www.command.exchange;

import ru.dpolulyakh.www.command.CommandBot;

/**
 * @author Denis Polulyakh
 */

public class ExchangeCurrency implements CommandBot {
    private ExchangeCommand command;
    private String charCode;
    private String date;

    public void setCharCode(String charCode) {
        this.charCode = charCode;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setCommand(ExchangeCommand command) {
        this.command = command;
    }

    @Override
    public String execute() {
        return command.getCurrencyMessage(charCode, date);
    }
}
