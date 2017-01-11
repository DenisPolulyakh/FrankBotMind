package ru.dpolulyakh.www.entity;

import java.util.ArrayList;
import java.util.Set;

/**
 * @author Denis Polulyakh
 */

public class Message {

    private ArrayList<String> phraseAL = new ArrayList<String>();

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

    public ArrayList<String> getPhrase() {
        return phraseAL;
    }

    public void setPhraseAL(ArrayList<String> phraseAL) {
        this.phraseAL = phraseAL;
    }

    public void addPhrase(String phrase){
        phraseAL.add(phrase);
    }

    public void resetPhrase(){
        phraseAL.clear();
    }
}
