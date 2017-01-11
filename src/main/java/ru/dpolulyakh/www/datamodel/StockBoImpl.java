package ru.dpolulyakh.www.datamodel;

/**
 * Created by Денис on 05.01.2017.
 */
public class StockBoImpl implements StockBo{

    StockDao stockDao;

    public void setStockDao(StockDao stockDao) {
        this.stockDao = stockDao;
    }


    @Override
    public void save(Stock stock){
        stockDao.save(stock);
    }
    @Override
    public void update(Stock stock){
        stockDao.update(stock);
    }
    @Override
    public void delete(Stock stock){
        stockDao.delete(stock);
    }
    @Override
    public Stock findByStockCode(String stockCode){
        return stockDao.findByStockCode(stockCode);
    }
}