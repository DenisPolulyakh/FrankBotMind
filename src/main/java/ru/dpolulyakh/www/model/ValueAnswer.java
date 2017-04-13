package ru.dpolulyakh.www.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
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
    @Type(type="org.hibernate.type.PrimitiveByteArrayBlobType")
    @Column(name="ANSWER")
    private byte[] answer;
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "valueAnswer")
    private Set<KeyQuestion> keyQuestion = new HashSet<KeyQuestion>();

    public ValueAnswer() {
    }

    public ValueAnswer(byte[] answer) {
        this.answer = answer;
    }

    public ValueAnswer(byte[] answer, Set<KeyQuestion> keyQuestion) {
        this.answer = answer;
        this.keyQuestion = keyQuestion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getAnswer() {
        return answer;
    }

    public void setAnswer(byte[] answer) {
        this.answer = answer;
    }

    public Set<KeyQuestion> getKeyQuestions() {
        return keyQuestion;
    }

    public void setKeyQuestions(Set<KeyQuestion> keyQuestion) {
        this.keyQuestion = keyQuestion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValueAnswer that = (ValueAnswer) o;
        return Objects.equals(answer, that.answer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(answer);
    }

    @Override
    public String toString() {
        return "ValueAnswer{" +
                "id=" + id +
                ", answer=" + new String(answer) + '\'' +
                '}';
    }
}
