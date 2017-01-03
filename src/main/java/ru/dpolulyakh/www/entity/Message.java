package ru.dpolulyakh.www.entity;

import java.util.Set;

/**
 * @author Denis Polulyakh
 */
public class Message {
    private String phrase = new String();

    private Set codeCurrency;
    private Set nameCurrency;

    public Set getCodeCurrency() {
        return codeCurrency;
    }

    public void setCodeCurrency(Set codeCurrency) {
        this.codeCurrency = codeCurrency;
    }

    public Set getNameCurrency() {
        return nameCurrency;
    }

    public void setNameCurrency(Set nameCurrency) {
        this.nameCurrency = nameCurrency;
    }

    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }
}
