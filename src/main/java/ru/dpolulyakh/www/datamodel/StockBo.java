package ru.dpolulyakh.www.datamodel;

/**
 * @author Denis Polulyakh
 */
public interface StockBo {

    void save(Stock stock);
    void update(Stock stock);
    void delete(Stock stock);
    Stock findByStockCode(String stockCode);
}
