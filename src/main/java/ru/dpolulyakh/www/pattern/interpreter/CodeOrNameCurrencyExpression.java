package ru.dpolulyakh.www.pattern.interpreter;

import org.apache.log4j.Logger;
import ru.dpolulyakh.www.dao.cource.CodeNameCurrency;

/**
 * @author Denis Polulyakh
 *         11.01.2017
 */
public class CodeOrNameCurrencyExpression implements Expression {
    private static final String CLASS_NAME = CodeOrNameCurrencyExpression.class.getName();
    private static final Logger log = Logger.getLogger(CLASS_NAME);
    private boolean isExpressionFind = false;
    private CodeNameCurrency codeNameCurrency;
    private String codeCurrency;

    public String getCode() {
        return codeCurrency;
    }

    public void setCodeNameCurrency(CodeNameCurrency codeNameCurrency) {
        this.codeNameCurrency = codeNameCurrency;
    }

    public boolean isExpressionFind() {
        return isExpressionFind;
    }

    @Override
    public void interpret(Context context) {
        String input = context.getInput();
        for (String codeCurrency : codeNameCurrency.getCode()) {
            if (input.toLowerCase().indexOf(codeCurrency.toLowerCase()) != -1) {
                log.info("Code:" + codeCurrency);
                this.codeCurrency = codeCurrency;
                input = input.toLowerCase().replaceAll(codeCurrency,"");
                context.setInput(input);
                isExpressionFind=true;
                return;
            }
        }

        int i = 0;
        for (String nameCurrency : codeNameCurrency.getNames()) {
            for (String name : nameCurrency.split(";")) {
                if (input.toLowerCase().indexOf(name.toLowerCase()) != -1) {
                    this.codeCurrency = codeNameCurrency.getCode().get(i);
                    isExpressionFind=true;
                    return;
                }

            }
            i++;

        }
    }

}

