package buyingmarket.bootstrap;

import buyingmarket.model.Actuary;
import buyingmarket.model.Agent;
import buyingmarket.model.Supervisor;
import buyingmarket.repositories.ActuaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ActuatorBootstrap implements CommandLineRunner {

    private ActuaryRepository actuaryRepository;

    @Autowired
    public ActuatorBootstrap(ActuaryRepository actuaryRepository) {
        this.actuaryRepository = actuaryRepository;
    }


    @Override
    public void run(String... args) throws Exception {
        Actuary actuary = new Supervisor(1L);
        actuaryRepository.save(actuary);
        actuary = new Agent(2L, BigDecimal.valueOf(100000L), false);
        actuaryRepository.save(actuary);
        actuary = new Agent(2L, BigDecimal.valueOf(10000L), true);
        actuaryRepository.save(actuary);
    }
}
