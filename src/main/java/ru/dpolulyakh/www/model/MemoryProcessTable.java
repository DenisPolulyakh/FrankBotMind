package ru.dpolulyakh.www.model;

import ru.dpolulyakh.www.process.MemoryProcessor;

import javax.persistence.*;
import java.sql.Blob;

/**
 * @author Denis Polulyakh
 *         22.01.2017.
 */
@Entity
@Table(name="MEMORY_PROCESS_TABLE")
public class MemoryProcessTable {

    @Id
    @Column(name="ID_USER")
    private String idUser;
    @Column(name="USER_NAME")
    private String userName;
    @Column(name="MEMORY_PROCESSOR")
    private Blob memoryProcessor;

    public MemoryProcessTable() {
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Blob getMemoryProcessor() {
        return memoryProcessor;
    }

    public void setMemoryProcessor(Blob memoryProcessor) {
        this.memoryProcessor = memoryProcessor;
    }
}