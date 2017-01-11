package ru.dpolulyakh.www.dao.message;

public interface MessageBotDAO
{
    public void insert(MessageBotStorage messagebot);
    public MessageBotStorage findByQuestion(String question);
}
