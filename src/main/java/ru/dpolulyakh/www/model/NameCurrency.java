package ru.dpolulyakh.www.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Denis Polulyakh
 *         01.02.2017.
 */
public class NameCurrency {
    @Id
    @Column(name = "NAME_CURRENCY",nullable = false)
    private String nameCurrency;

    @ManyToOne(fetch = FetchType.LAZY)
    private Set<CodeCurrency> answer=new HashSet<CodeCurrency>();

    public NameCurrency() {
    }

    public String getNameCurrency() {
        return nameCurrency;
    }

    public void setNameCurrency(String nameCurrency) {
        this.nameCurrency = nameCurrency;
    }

    public void setAnswer(Set<CodeCurrency> answer) {
        this.answer = answer;
    }

    public Set<CodeCurrency> getAnswer() {
        return answer;
    }
}
