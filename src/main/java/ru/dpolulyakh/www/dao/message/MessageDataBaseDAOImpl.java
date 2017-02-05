package ru.dpolulyakh.www.dao.message;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.dpolulyakh.www.model.KeyQuestion;
import ru.dpolulyakh.www.model.ValueAnswer;
import ru.dpolulyakh.www.model.MemoryProcessTable;


import javax.sql.DataSource;
import java.util.List;


@Repository
public class MessageDataBaseDAOImpl implements MessageDataBaseDAO {
    @Autowired
    private SessionFactory sessionFactory;

    private DataSource dataSource;


    public MessageDataBaseDAOImpl() {

    }

    public MessageDataBaseDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }




    /*@Override
    @Transactional
    public List<ValueAnswer> list() {
        List<ValueAnswer> answersList = (List<ValueAnswer>) sessionFactory.getCurrentSession()
                .createCriteria(ValueAnswer.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
        return answersList;
    }

    @Override
    @Transactional
    public List<ValueAnswer> listAnswersByTypePhrase(String type) {
        String hql = "select a from Answers as a join a.keyPhrase as  kp where kp.typePhrase=:type";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setString("type", type);
        return query.list();
    }


    @Override
    @Transactional
    public List<KeyPhrase> listKeyPhraseByTypePhrase(String type) {
        String hql = "select kp from KeyPhrase as kp where kp.typePhrase = :type";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setString("type", type);
        return query.list();
    }


    @Override
    @Transactional
    public void saveOrUpdate(KeyPhrase keyPhrase) {
        sessionFactory.getCurrentSession().saveOrUpdate(keyPhrase);
    }

    @Override
    @Transactional
    public void saveOrUpdate(ValueAnswer answers) {
        sessionFactory.getCurrentSession().saveOrUpdate(answers);
    }


    @Override
    @Transactional
    public KeyPhrase getCode(String name) {
        String hql = "select c from CodeCurrency as c join c.variantsName as cv where cv.nameCurrency = :namecurrency";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        return null;
    }*/

    @Override
    @Transactional
    public List<ValueAnswer> listAnswersByKeyQuestion(String keyQuestion) {
        String hql = "select a from ValueAnswer as a join a.keyQuestion as kq where kq.question = :key";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setString("key",keyQuestion);
        return query.list();
    }

    @Override
    @Transactional
    public List<KeyQuestion> listKeyQuestion() {
        List<KeyQuestion> keyQuestionList = (List<KeyQuestion> ) sessionFactory.getCurrentSession()
                .createCriteria(KeyQuestion.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
        return keyQuestionList;
    }

    @Override
    @Transactional
    public List<ValueAnswer> listValueAnswer() {
        List<ValueAnswer> valueAnswerList = (List<ValueAnswer>) sessionFactory.getCurrentSession()
                .createCriteria(ValueAnswer.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
        return valueAnswerList;
    }

    @Override
    @Transactional
    public void saveOrUpdate(KeyQuestion keyQuestion) {
        sessionFactory.getCurrentSession().saveOrUpdate(keyQuestion);
    }

    @Override
    @Transactional
    public void saveOrUpdate(ValueAnswer valueAnswer) {
        sessionFactory.getCurrentSession().saveOrUpdate(valueAnswer);
    }

    @Override
    @Transactional
    public List<MemoryProcessTable> getMemoryProcessTable(String idUser) {
        String hql = "from MemoryProcessTable mpt where mpt.idUser =  :idUser";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setString("idUser", idUser);
        System.out.println("IDUSER "+idUser);
        return  query.list();

        /*List memoryCommandTable = query.list();
        if (memoryCommandTable.size() > 0) {
            Blob blob = ((MemoryProcessTable)memoryCommandTable.get(0)).getMemoryProcessor();
            try {
                return new String(blob.getBytes(1, (int) blob.length()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            return null;
        }
        return null;*/
    }

    @Override
    @Transactional
    public void saveOrUpdate(MemoryProcessTable memoryProcessTable) {
        sessionFactory.getCurrentSession().saveOrUpdate(memoryProcessTable);
    }

    @Override
    @Transactional
    public void deleteMemoryProcessor(MemoryProcessTable memoryProcessTable) {
        sessionFactory.getCurrentSession().delete(memoryProcessTable);
    }

}


/*
    @Override
    public CodeNameCurrency load() {
        String sql = "SELECT * FROM CODE_NAME_CURRENCY";
        Connection conn = null;

        try {
            conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            CodeNameCurrency codeNameCurrency = new CodeNameCurrency();
            ArrayList<String> code = new ArrayList<String>();
            ArrayList<String> nameCurrency = new ArrayList<String>();
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
               code.add(rs.getString("CODE"));
               nameCurrency.add(rs.getString("VARIANTS_NAME"));
            }
            rs.close();
            ps.close();
            codeNameCurrency.setCode(code);
            codeNameCurrency.setNames(nameCurrency);
            return codeNameCurrency;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {}
            }
        }
    }
    */




