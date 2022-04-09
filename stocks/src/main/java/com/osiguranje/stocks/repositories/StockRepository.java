package com.osiguranje.stocks.repositories;

import com.osiguranje.stocks.model.StockModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<StockModel, Long> {
    List<StockModel> findAll();

    StockModel save(StockModel stock);

    List<StockModel> findStockModelBySymbol(String stock);

    @Query(value = "SELECT * FROM stocks WHERE date >= ?1 AND date <= ?2", nativeQuery = true)
    List<StockModel> findByDateWindow(Date startDate, Date endDate);
}
