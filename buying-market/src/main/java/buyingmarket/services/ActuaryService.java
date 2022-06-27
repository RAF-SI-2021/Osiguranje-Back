package buyingmarket.services;

import buyingmarket.exceptions.UserNotFoundException;
import buyingmarket.mappers.ActuaryMapper;
import buyingmarket.model.Actuary;
import buyingmarket.model.dto.ActuaryCreateDto;
import buyingmarket.repositories.ActuaryRepository;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Service
public class ActuaryService {
    private final ActuaryRepository actuaryRepository;
    private final ActuaryMapper actuaryMapper;
    private final RestTemplate rest;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${api.usercrud}")
    private String usercrudApiUrl;

    @Autowired
    public ActuaryService(ActuaryRepository actuaryRepository, ActuaryMapper actuaryMapper) {
        this.actuaryRepository = actuaryRepository;
        this.actuaryMapper = actuaryMapper;
        this.rest = new RestTemplate();
    }

    public void createActuary(ActuaryCreateDto dto) throws Exception {
        Optional<Actuary> act = actuaryRepository.findActuaryByUserId(dto.getUserId());
        if (act.isEmpty()) {
            actuaryRepository.save(actuaryMapper.actuaryCreateDtoToActuary(dto));
        } else if (!act.get().getActive()) {
            Actuary actuary = act.get();
            actuary.setActive(true);
            actuaryRepository.save(actuary);
        }
    }

    private String extractUsername(String jws) {
        jws = jws.replace("Bearer ", "");
        return Jwts.parser()
                .setSigningKey(jwtSecret.getBytes())
                .parseClaimsJws(jws)
                .getBody()
                .getSubject();
    }

    private Long getUserId(String jws) {
        String username = extractUsername(jws);
        String urlString = usercrudApiUrl + "/api/users/id";
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(urlString);
        String urlTemplate = uriComponentsBuilder.queryParam("email", username).encode().toUriString();
        ResponseEntity<Long> response = rest.exchange(urlTemplate, HttpMethod.GET, null, Long.class);
        return response.getBody();
    }

    public Actuary getActuary(String jws) {
        Optional<Actuary> actuary = actuaryRepository.findActuaryByUserId(getUserId(jws));
        if (actuary.isPresent()) {
            return actuary.get();
        } else {
            throw new UserNotFoundException("No actuary found");
        }
    }
}
