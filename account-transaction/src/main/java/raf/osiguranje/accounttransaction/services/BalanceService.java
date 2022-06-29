package raf.osiguranje.accounttransaction.services;

import io.jsonwebtoken.Jwts;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import raf.osiguranje.accounttransaction.model.Account;
import raf.osiguranje.accounttransaction.model.Balance;
import raf.osiguranje.accounttransaction.model.BalanceId;
import raf.osiguranje.accounttransaction.model.dto.SecurityDto;
import raf.osiguranje.accounttransaction.model.dto.SecurityType;
import raf.osiguranje.accounttransaction.model.dto.UserDto;
import raf.osiguranje.accounttransaction.repositories.AccountRepository;
import raf.osiguranje.accounttransaction.repositories.BalanceRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
@Service
public class BalanceService {

    private AccountRepository accountRepository;
    private BalanceRepository balanceRepository;

    private RestTemplate rest;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${api.securities}")
    private String securitiesApiUrl;

    @Value("${api.usercrud}")
    private String usercrudApiUrl;

    @Autowired
    public BalanceService(AccountRepository accountRepository, BalanceRepository balanceRepository, RestTemplate rest) {
        this.accountRepository = accountRepository;
        this.balanceRepository = balanceRepository;
        this.rest = rest;
    }

    public boolean createBalance(Long accountNumber,Long securityId,SecurityType securityType,int amount) {
        Account account = accountRepository.findAccountByAccountNumber(accountNumber);
        if(accountNumber==null){
            return false;
        }
        System.out.println(accountNumber + " {} " + account);
//        String email = extractUsername(jwtToken);
        /*
        Proveravam da li postoji securiti u nase sistemu
         */
//        try {
//            SecurityDto securityDto = getSecurityByTypeAndId(securityType,securityId);
//        } catch (Exception e) {
//            System.err.println(e);
//            return false;
//        }
        Balance balance;
        try {
            balance = new Balance(account, securityId, securityType, amount);
            System.out.println(balance);
            balanceRepository.save(balance);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean deleteBalance(Long accountNumber,Long securityId){
        Account account = accountRepository.findAccountByAccountNumber(accountNumber);
        if(accountNumber==null){
            return false;
        }

        Optional<Balance> balance = balanceRepository.findById(new BalanceId(accountNumber,securityId));
        if(balance.isEmpty())
            return false;

        balanceRepository.delete(balance.get());
        return true;
    }

    public List<Balance> getAllBalances(){
        return balanceRepository.findAll();
    }

    @Transactional
    public List<Balance> getBalancesByAccount(Long accountId){
        Account account = accountRepository.findAccountByAccountNumber(accountId);
        if(account==null){
            return new ArrayList<>();
        }
        return balanceRepository.findBalanceByAccount(account);
    }

    @Transactional
    public List<Balance> getBalancesBySecurity(Long security){
        return balanceRepository.findBalanceBySecurityId(security);
    }

    @Transactional
    public Optional<Balance> getBalancesByFullId(Long accountId, Long security){
        Account account = accountRepository.findAccountByAccountNumber(accountId);
        if(account==null){
            return Optional.empty();
        }
        return balanceRepository.findById(new BalanceId(accountId,security));
    }

    @Transactional
    public boolean updateAmount(Long accountId,Long securityId,int amount){

        Optional<Balance> balanceOptional = getBalancesByFullId(accountId,securityId);
        if(balanceOptional.isEmpty()){
            return false;
        }

        Balance balance = balanceOptional.get();

        if(balance.getAmount() + amount < 0){
            return false;
        }
        balance.setAmount(balance.getAmount()+amount);
        balanceRepository.save(balance);

        return true;
    }

    @Transactional
    public boolean updateReserve(Long accountId,Long securityId,int reserve){
        Optional<Balance> balanceOptional = getBalancesByFullId(accountId,securityId);
        if(balanceOptional.isEmpty()){
            return false;
        }

        Balance balance = balanceOptional.get();

        int newReserve = balance.getReserved() + reserve;

        if(newReserve < 0  || balance.getAmount() < newReserve ){
            return false;
        }
        balance.setReserved(newReserve);
        balanceRepository.save(balance);

        return true;
    }

    public String extractUsername(String jws) {
        jws = jws.replace("Bearer ", "");
        return Jwts.parser()
                .setSigningKey(jwtSecret.getBytes())
                .parseClaimsJws(jws)
                .getBody()
                .getSubject();
    }

    public UserDto getUserByUsernameFromUserService(String username) throws Exception {
        String urlString = usercrudApiUrl + "/api/users/search/email";
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(urlString);
        String urlTemplate = uriComponentsBuilder.queryParam("email", username).encode().toUriString();
        ResponseEntity<UserDto> response = null;
        try {
            response = rest.exchange(urlTemplate, HttpMethod.GET, null, UserDto.class);
        } catch(RestClientException e) {
            throw new Exception("Something went wrong while trying to retrieve user info");
        }
        UserDto user = null;
        if(response.getBody() != null) {
            user = response.getBody();
        }
        if(user == null) {
            throw new IllegalArgumentException("Something went wrong trying to find user");
        }
        return user;
    }

    protected SecurityDto getSecurityByTypeAndId(SecurityType securityType, Long securityId) throws Exception {
        String urlString = securitiesApiUrl + "/api/data/" + securityType.toString().toLowerCase() + "/" + securityId;
        ResponseEntity<SecurityDto> response;
        try {
            response = rest.exchange(urlString, HttpMethod.GET, null, SecurityDto.class);
        } catch(RestClientException e) {
            throw new Exception("Something went wrong while trying to retrieve security info");
        }
        SecurityDto security = null;
        if(response.getBody() != null) {
            security = response.getBody();
        }
        if (security == null) {
            throw new IllegalArgumentException("Something went wrong trying to find security");
        }
        return security;
    }
}