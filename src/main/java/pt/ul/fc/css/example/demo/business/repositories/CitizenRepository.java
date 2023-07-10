package pt.ul.fc.css.example.demo.business.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pt.ul.fc.css.example.demo.business.entities.Citizen;

/**
 * Class that defines a CitizenRepository interface using JPA Repository methods along with a custom
 * method.
 */
public interface CitizenRepository extends JpaRepository<Citizen, Long> {

  /**
   * Finds citizens by name
   *
   * @param query name being searched for
   * @return a list of citizens with that name
   */
  List<Citizen> findByName(String query);
}
