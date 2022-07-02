package buyingmarket.services;

import buyingmarket.exceptions.UserNotFoundException;
import buyingmarket.mappers.ActuaryMapper;
import buyingmarket.model.Actuary;
import buyingmarket.model.Agent;
import buyingmarket.model.dto.ActuaryCreateDto;
import buyingmarket.repositories.ActuaryRepository;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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

    public List<Agent> getAllAgents() {
        List<Actuary> actuaries = actuaryRepository.findAll();
        List<Agent> agents = new ArrayList<>();
        for (Actuary actuary : actuaries) {
            if (actuary instanceof Agent) {
                agents.add((Agent) actuary);
            }
        }
        return agents;
    }

    public void createActuary(ActuaryCreateDto dto) {
        Optional<Actuary> act = actuaryRepository.findActuaryByUserId(dto.getUserId());
        if (act.isEmpty()) {
            actuaryRepository.save(actuaryMapper.actuaryCreateDtoToActuary(dto));
        } else if (!act.get().getActive()) {
            Actuary actuary = act.get();
            actuary.setActive(true);
            actuaryRepository.save(actuary);
        }
    }

    public void resetLimit(Long agentId) {
        Optional<Actuary> a = actuaryRepository.findById(agentId);
        if (a.isPresent()) {
            Agent agent = (Agent) a.get();
            agent.setUsedLimit(new BigDecimal(0));
            actuaryRepository.save(agent);
        } else {
            throw new UserNotFoundException("No such agent found.");
        }
    }

    public void setLimit(Long agentId, BigDecimal newLimit) {
        Optional<Actuary> a = actuaryRepository.findById(agentId);
        if (a.isPresent()) {
            Agent agent = (Agent) a.get();
            agent.setSpendingLimit(newLimit);
            actuaryRepository.save(agent);
        } else {
            throw new UserNotFoundException("No such agent found.");
        }
    }

    public void changeLimit(Long agentId, BigDecimal addLimit) throws Exception {
        Optional<Actuary> a = actuaryRepository.findById(agentId);
        if (a.isPresent()) {
            Agent agent = (Agent) a.get();
            BigDecimal newLimit = agent.getUsedLimit().add(addLimit);
            if(newLimit.compareTo(agent.getSpendingLimit()) > 0)
                throw new Exception("Limit overflow less then zero");
            agent.setUsedLimit(newLimit);
            actuaryRepository.save(agent);
        } else {
            throw new UserNotFoundException("No such agent found.");
        }
    }

    @Scheduled(cron = "0 0 8 * * MON-FRI")
    public void dailyReset() {
        for (Agent agent : getAllAgents()) {
            resetLimit(agent.getId());
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

    public Long getUserId(String jws) {
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
