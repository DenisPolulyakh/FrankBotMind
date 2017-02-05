package ru.dpolulyakh.www.dao.message;

import ru.dpolulyakh.www.model.KeyQuestion;
import ru.dpolulyakh.www.model.ValueAnswer;
import ru.dpolulyakh.www.model.MemoryProcessTable;


import java.util.List;

public interface MessageDataBaseDAO {

    List<ValueAnswer> listAnswersByKeyQuestion(String keyQuestion);

    List<KeyQuestion> listKeyQuestion();

    List<ValueAnswer> listValueAnswer();

    void saveOrUpdate(KeyQuestion keyQuestion);

    void saveOrUpdate(ValueAnswer valueAnswer);

    void saveOrUpdate(MemoryProcessTable memoryProcessTable);

    List<MemoryProcessTable> getMemoryProcessTable(String id);

    void deleteMemoryProcessor(MemoryProcessTable memoryProcessTable);
}
