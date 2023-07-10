package pt.ul.fc.css.example.demo.business.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pt.ul.fc.css.example.demo.business.entities.Poll;
import pt.ul.fc.css.example.demo.utils.StatusPoll;

/**
 * Class that defines a PollRepository interface using JPA Repository methods along with custom
 * methods.
 */
public interface PollRepository extends JpaRepository<Poll, Long> {

  /**
   * Finds polls by name
   *
   * @param nome name being searched for
   * @return a list of polls with that name
   */
  List<Poll> findByName(String nome);

  /**
   * Finds polls by a given status
   *
   * @param status status being searched for
   * @return list of polls with the status being searched for
   */
  List<Poll> findBystatusPoll(StatusPoll status);
}
