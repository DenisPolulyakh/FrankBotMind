package ru.dpolulyakh.www.entity;

import java.util.ArrayList;

/**
 * @author Denis Polulyakh
 */
public class Message {
    private ArrayList<String> phrase = new ArrayList<String>();

    public ArrayList<String> getPhrase() {
        return phrase;
    }

    public void setPhrase(ArrayList<String> phrase) {
        this.phrase = phrase;
    }
}
