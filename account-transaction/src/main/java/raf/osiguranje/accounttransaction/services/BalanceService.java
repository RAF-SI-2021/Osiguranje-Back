package raf.osiguranje.accounttransaction.services;

import io.jsonwebtoken.Jwts;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import raf.osiguranje.accounttransaction.model.Account;
import raf.osiguranje.accounttransaction.model.dto.SecurityDto;
import raf.osiguranje.accounttransaction.model.dto.SecurityType;
import raf.osiguranje.accounttransaction.model.dto.UserDto;
import raf.osiguranje.accounttransaction.repositories.AccountRepository;
import raf.osiguranje.accounttransaction.repositories.BalanceRepository;

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

    public boolean createBalance(Long accountNumber,Long securityId,int amount,String jwtToken) {
        Account account = accountRepository.findAccountByAccountNumber(accountNumber);
        if(accountNumber==null){
            return false;
        }

//        String email = extractUsername(jwtToken);


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

    protected SecurityDto getSecurityById(SecurityType securityType, Long securityId) throws Exception {
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
