package ru.dpolulyakh.www.command.exchange;

import ru.dpolulyakh.www.command.CommandBot;
import ru.dpolulyakh.www.entity.Message;

/**
 * Created by Денис on 03.01.2017.
 */
public class ListCurrency implements CommandBot {
    private ListCurrencyCommand command;

    public void setCommand(ListCurrencyCommand command) {
        this.command = command;
    }

    @Override
    public Message execute() {
        return command.getListCurrency();
    }
}
