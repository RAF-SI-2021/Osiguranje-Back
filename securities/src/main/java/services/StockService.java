package services;

import model.Stock;
import repositories.StocksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class StockService {
    private final StocksRepository stockRepository;

    @Autowired
    public StockService(StocksRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public List<Stock> getStocks(){
        return stockRepository.findAll();
    }

    public Stock save(Stock stock){
        return stockRepository.save(stock);
    }

    public List<Stock> saveAll(List<Stock> stocks){
        return stockRepository.saveAll(stocks);
    }

    public List<Stock> findByDateWindow(Date startDate, Date endDate){
        return stockRepository.findByDateWindow(startDate, endDate);
    }

    public List<Stock> findBySymbol(String symbol){
        return stockRepository.findStockBySymbol(symbol);
    }

    public List<Stock> getStocksData() {
        return stockRepository.findAll();
    }
}
