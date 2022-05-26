package app.repositories;

import app.model.Option;
import app.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OptionsRepository extends JpaRepository<Option, Long> {
    List<Option> findAll();
    Option save(Option option);
}
