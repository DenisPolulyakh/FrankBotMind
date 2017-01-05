package ru.dpolulyakh.www.datamodel;

import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Денис on 05.01.2017.
 */
public class StockDaoImpl extends HibernateDaoSupport implements StockDao{
    @Override
    @Transactional
    public void save(Stock stock){
                getHibernateTemplate().save(stock);
    }
    @Override
    @Transactional
    public void update(Stock stock){
        getHibernateTemplate().update(stock);
    }
    @Override
    @Transactional
    public void delete(Stock stock){
        getHibernateTemplate().delete(stock);
    }
    @Override
    @Transactional
    public Stock findByStockCode(String stockCode){
        List list = getHibernateTemplate().find("from Stock where stockCode=?",stockCode);
        return (Stock)list.get(0);
    }

}
