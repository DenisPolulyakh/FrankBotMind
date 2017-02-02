package ru.dpolulyakh.www.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Denis Polulyakh
 *         15.01.2017.
 */
@Entity
@Table(name="ANSWERS")
public class ValueAnswer {
    @Id
    @GeneratedValue
    @Column(name = "ID_ANSWER")
    private int id;
    @Column(name="ANSWER")
    private String answer;
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "valueAnswer")
    private Set<KeyQuestion> keyQuestion = new HashSet<KeyQuestion>();

    public ValueAnswer() {
    }

    public ValueAnswer(String answer) {
        this.answer = answer;
    }

    public ValueAnswer(String answer, Set<KeyQuestion> keyQuestion) {
        this.answer = answer;
        this.keyQuestion = keyQuestion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Set<KeyQuestion> getKeyQuestions() {
        return keyQuestion;
    }

    public void setKeyQuestions(Set<KeyQuestion> keyQuestion) {
        this.keyQuestion = keyQuestion;
    }
}
