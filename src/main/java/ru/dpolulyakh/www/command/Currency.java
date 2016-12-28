package ru.dpolulyakh.www.command;

import ru.dpolulyakh.www.entity.Message;

/**
 * Created by Денис on 27.12.2016.
 */
public class Currency implements CommandBot{
    private Command command;
    private String charCode;
    private String date;
    private Message message;

    public Message getMessage() {
        return message;
    }

    public void setCharCode(String charCode) {
        this.charCode = charCode;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Currency(Command command) {
        this.command = command;
    }

    @Override
    public void execute() {
        message=command.getValuteMessage(charCode,date);
    }
}
