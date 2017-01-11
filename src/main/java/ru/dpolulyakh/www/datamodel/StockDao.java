package ru.dpolulyakh.www.datamodel;

/**
 * Created by Денис on 05.01.2017.
 */
public interface StockDao {

    void save(Stock stock);
    void update(Stock stock);
    void delete(Stock stock);
    Stock findByStockCode(String stockCode);

}
