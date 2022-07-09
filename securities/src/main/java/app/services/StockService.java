package app.services;

import app.model.Stock;
import app.model.dto.StockDTO;
import app.repositories.StocksRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Setter
public class StockService {
    private final StocksRepository stockRepository;
    public StockService(StocksRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public Stock save(Stock stock){
        return stockRepository.save(stock);
    }

//    public List<Stock> findByDateWindow(Date startDate, Date endDate){
//        return stockRepository.findByDateWindow(startDate, endDate);
//    }

    public List<Stock> findByTicker(String symbol){
        return stockRepository.findStockByTicker(symbol);
    }

    public List<StockDTO> getStocksDTOData(){
        List<Stock> stockList = getStocksData();
        List<StockDTO> dtoList = new ArrayList<>();
        for (Stock s: stockList){
            dtoList.add(new StockDTO(s));
        }
        return dtoList;
    }

    public List<Stock> getStocksData() {
        return stockRepository.findAll();
    }

    public List<Stock> updateData() throws IOException {
        System.out.println("Updating stocks");

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        List<Stock> stocks = stockRepository.findAll();

        for (Stock s: stocks){
            yahoofinance.Stock stock = YahooFinance.get(s.getTicker());
            if (stock == null || !stock.isValid()) {
                continue;
            }
            s.setLastUpdated(formatter.format(date));
            s.setPrice(stock.getQuote().getPrice());
            s.setAsk(stock.getQuote().getAsk());
            s.setBid(stock.getQuote().getBid());
            s.setPriceChange(stock.getQuote().getChange());
            s.setVolume(stock.getQuote().getVolume());

            if(!s.getOutstandingShares().equals(stock.getStats().getSharesOutstanding()))
                s.setOutstandingShares(stock.getStats().getSharesOutstanding());
            if(!s.getDividendYield().equals(stock.getDividend().getAnnualYield()))
                s.setDividendYield(stock.getDividend().getAnnualYield());

            stockRepository.save(s);
        }
        return stocks;
    }

    public StockDTO findById(long id) {
        Optional<Stock> opStock = stockRepository.findById(id);
        if(opStock.isEmpty())
            return null;
        Stock stock = opStock.get();
        return new StockDTO(stock);
    }
}
