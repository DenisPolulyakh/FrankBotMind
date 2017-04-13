package ru.dpolulyakh.www.service;

import ru.dpolulyakh.www.model.KeyQuestion;
import ru.dpolulyakh.www.model.MemoryProcessTable;
import ru.dpolulyakh.www.model.ValueAnswer;

import java.util.List;

/**
 * @author Denis Polulyakh
 *         09.02.2017.
 */
interface StorageServiceInterface {
    KeyQuestion findToStorage(String key);

    void deleteToStorage(KeyQuestion key);

    boolean addToStorage(KeyQuestion key);

    List<ValueAnswer> getAnswersByQuestion(String key);

    void saveOrUpdate(MemoryProcessTable memoryProcessTable);

    void deleteMemoryProcessor(MemoryProcessTable memoryProcessTable);

    List<MemoryProcessTable> getMemoryProcessTable(String id);
}
