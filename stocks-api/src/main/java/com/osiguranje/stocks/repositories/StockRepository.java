package com.osiguranje.stocks.repositories;

import com.osiguranje.stocks.model.StockModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<StockModel, Long>{
    List<StockModel> findAll();

     StockModel save(StockModel stock);
}
