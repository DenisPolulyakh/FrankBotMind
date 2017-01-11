package ru.dpolulyakh.www.process.interpreter;

/**
 * @author Denis Polulyakh
 *         10.01.2017
 */
public class WordCourseExpression implements Expression{
    private String word = "курс";
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
           input = input.replaceAll(word,"");
           context.setInput(input);
        }


    }
}


