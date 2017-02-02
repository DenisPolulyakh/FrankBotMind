package ru.dpolulyakh.www.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Denis Polulyakh
 *         01.02.2017.
 */
public class CodeCurrency {
    @Id
    @Column(name = "ID",nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "CODE_CURRENCY")
    private String codeCurrency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="NAME_CURRECY",nullable = false)
    private NameCurrency nameCurrency;

    public CodeCurrency() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodeCurrency() {
        return codeCurrency;
    }

    public void setCodeCurrency(String codeCurrency) {
        this.codeCurrency = codeCurrency;
    }

    public NameCurrency getNameCurrency() {
        return nameCurrency;
    }

    public void setNameCurrency(NameCurrency nameCurrency) {
        this.nameCurrency = nameCurrency;
    }
}
