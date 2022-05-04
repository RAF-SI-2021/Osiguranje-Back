package app.services;

import app.model.Stock;
import app.model.dto.StockDTO;
import app.repositories.StocksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StockService {
    private final StocksRepository stockRepository;

    @Autowired
    public StockService(StocksRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public Stock save(Stock stock){
        return stockRepository.save(stock);
    }

    public List<Stock> saveAll(List<Stock> stocks){
        return stockRepository.saveAll(stocks);
    }

//    public List<Stock> findByDateWindow(Date startDate, Date endDate){
//        return stockRepository.findByDateWindow(startDate, endDate);
//    }

    public List<Stock> findByTicker(String symbol){
        return stockRepository.findStockByTicker(symbol);
    }

    public List<Stock> getStocksData() {
        return stockRepository.findAll();
    }

    public List<StockDTO> getStocksDTOData(){
        List<Stock> stockList = getStocksData();
        List<StockDTO> dtoList = new ArrayList<>();
        for (Stock s: stockList){
            dtoList.add(new StockDTO(s));
        }
        return dtoList;
    }
}
