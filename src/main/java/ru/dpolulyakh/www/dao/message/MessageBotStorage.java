package ru.dpolulyakh.www.dao.message;

import org.apache.log4j.Logger;

import java.util.Random;

public class MessageBotStorage

{

    private static final String CLASS_NAME=MessageBotStorage.class.getName();
    private static final Logger log = Logger.getLogger(CLASS_NAME);
    String answer;
    String command;
    String question;

    public MessageBotStorage(String question,String answer, String command ) {
        this.answer = answer;
        this.command = command;
        this.question = question;
    }

    public String getAnswer() {
        if(answer.indexOf(";")!=-1){
            String[] randomAnswer = answer.split(";");
            log.info("РАЗМЕР МАССИВА ОТВЕТОВ "+randomAnswer.length);
            Random random = new Random();
            int rIndex = random.nextInt(randomAnswer.length);
            log.info("ИНДЕКС "+rIndex);
            answer = randomAnswer[rIndex];
        }

        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}