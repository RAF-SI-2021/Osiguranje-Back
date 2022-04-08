package com.osiguranjeback.forex.services;

import com.osiguranjeback.forex.model.Forex;

import java.util.List;

public interface ExternalForexService {
    List<Forex> getQuotes(String base);
}
