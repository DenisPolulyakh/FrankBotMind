package ru.dpolulyakh.www.pattern.interpreter;

/**
 * @author Denis Polulyakh
 *         10.01.2017
 */
public class WordExpression implements Expression{
    private String word = "";
    private boolean isExpressionFind = false;

    public void setWord(String word) {
        this.word = word;
    }

    public boolean isExpressionFind() {
        return isExpressionFind;
    }

    @Override
    public void interpret(Context context) {
        isExpressionFind=false;
        String input = context.getInput();

        if(input.toLowerCase().indexOf(word)!=-1){
           isExpressionFind = true;
        }


    }
}


