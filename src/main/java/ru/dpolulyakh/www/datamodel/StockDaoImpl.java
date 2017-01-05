package ru.dpolulyakh.www.datamodel;

import org.hibernate.FlushMode;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

/**
 * Created by Денис on 05.01.2017.
 */
public class StockDaoImpl extends HibernateDaoSupport implements StockDao {

    private TransactionTemplate transactionTemplate;

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    @Override
    public void save(Stock stock) {
        // Programmatic transaction management
        transactionTemplate.execute(new TransactionCallback<Object>() {
            @Override
            public Object doInTransaction(TransactionStatus transactionStatus) {
                getHibernateTemplate().save(stock);
                return null;
            }


        });

    }

    @Override

    public void update(Stock stock) {
        getHibernateTemplate().getSessionFactory().getCurrentSession().setFlushMode(FlushMode.AUTO);
        getHibernateTemplate().update(stock);
    }

    @Override
    public void delete(Stock stock) {
        getHibernateTemplate().getSessionFactory().getCurrentSession().setFlushMode(FlushMode.AUTO);
        getHibernateTemplate().delete(stock);
    }

    @Override
    public Stock findByStockCode(String stockCode) {
        List list = getHibernateTemplate().find("from Stock where stockCode=?", stockCode);
        return (Stock) list.get(0);
    }

}
