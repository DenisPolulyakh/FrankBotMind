package ru.dpolulyakh.www.model;

import javax.persistence.*;

/**
 * @author Denis Polulyakh
 *         21.01.2017.
 */
@Entity
@Table(name="Commands")
public class Commands

{
    @Id
    @GeneratedValue
    @Column(name = "ID_COMMAND")
    private int id;

    @Column(name = "COMMAND")
    private String command;

    public Commands() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
