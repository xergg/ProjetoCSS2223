package pt.ul.fc.css.example.demo.business.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import pt.ul.fc.css.example.demo.business.entities.Poll;
import pt.ul.fc.css.example.demo.business.repositories.PollRepository;
import pt.ul.fc.css.example.demo.utils.StatusPoll;

/**
 * Class that represents a Poll Service, making use of its Poll Repository methods and getting
 * Citizens, a list of Citizens, finding specific citizens and putting them, given specific
 * conditions.
 */
@Service
public class PollService {

  @Autowired private final PollRepository pollRepository;

  /**
   * Constructor for Poll service, using a Poll Repository.
   *
   * @param pollRepository poll repository
   */
  public PollService(PollRepository pollRepository) {
    this.pollRepository = pollRepository;
  }

  /**
   * Get's all polls from the repository
   *
   * @return all polls
   */
  public List<Poll> getAllPolls() {
    return this.pollRepository.findAll();
  }

  /**
   * Gets a list of polls given a specific name
   *
   * @param name given name
   * @return list of polls from the repository with a specific name
   */
  public List<Poll> getPollByName(@RequestParam String name) {
    return this.pollRepository.findByName(name);
  }

  /**
   * Posts poll in the repository
   *
   * @param poll given poll
   * @return saved poll's id
   */
  public long putPoll(@RequestParam Poll poll) {
    Poll saved = pollRepository.save(poll);
    return saved.getId();
  }

  /**
   * Gets all ongoing polls
   *
   * @return a list of ongoing polls.
   */
  public List<Poll> getOngoingPolls() {

    return this.pollRepository.findBystatusPoll(StatusPoll.ONGOING);
  }

  /**
   * Find a poll given a specific id
   *
   * @param id given id
   * @return poll with the specific id
   */
  public Optional<Poll> findById(Long id) {

    return this.pollRepository.findById(id);
  }
}
