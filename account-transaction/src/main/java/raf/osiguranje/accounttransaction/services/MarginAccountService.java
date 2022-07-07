package raf.osiguranje.accounttransaction.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raf.osiguranje.accounttransaction.repositories.MarginAccountRepository;

@Service
public class MarginAccountService {
    private MarginAccountRepository marginAccountRepository;

    @Autowired
    public MarginAccountService(MarginAccountRepository marginAccountRepository) {
        this.marginAccountRepository = marginAccountRepository;
    }


}
