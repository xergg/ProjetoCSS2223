package pt.ul.fc.css.example.demo.business.dtos;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import pt.ul.fc.css.example.demo.business.entities.Delegate;
import pt.ul.fc.css.example.demo.utils.StatusPoll;

/**
 * Class that represents a Poll DTO, for all the REST API, to encapsulate the data, for easier
 * handling between the backend and frontend.
 */
public class PollDTO {

  private Long id;

  private List<Long> idList;

  private int positiveVotes;

  private int negativeVotes;

  private String name;

  private LocalDateTime endDate;

  private StatusPoll statusPoll;

  private Map<Long, Boolean> delegateVotes;

  /**
   * Constructor for a poll dto
   *
   * @param id dto id
   * @param name dto name
   * @param negativeVotes dto number of negative votes
   * @param positiveVotes dto number of positive votes
   * @param statuspoll dto poll's status
   * @param endDate dto end date
   * @param idList dto id list
   * @param delegateVotes dto number of delegate votes
   */
  public PollDTO(
      Long id,
      String name,
      int negativeVotes,
      int positiveVotes,
      StatusPoll statuspoll,
      LocalDateTime endDate,
      List<Long> idList,
      Map<Long, Boolean> delegateVotes) {

    this.id = id;
    this.name = name;
    this.negativeVotes = negativeVotes;
    this.positiveVotes = positiveVotes;
    this.statusPoll = statuspoll;
    this.idList = idList;
    this.delegateVotes = delegateVotes;
    this.endDate = endDate;
  }

  /**
   * Getter for id
   *
   * @return id
   */
  public Long getId() {
    return id;
  }

  /**
   * Getter for name
   *
   * @return name
   */
  public String getName() {
    return name;
  }

  /**
   * Getter for end date
   *
   * @return end date
   */
  public LocalDateTime getEndDate() {
    return endDate;
  }

  /**
   * Getter for id list
   *
   * @return id list
   */
  public List<Long> getIdList() {
    return idList;
  }

  /**
   * Getter for positive votes
   *
   * @return positive votes
   */
  public int getPositiveVotes() {
    return positiveVotes;
  }

  /**
   * Getter for negative votes
   *
   * @return negative votes
   */
  public int getNegativeVotes() {
    return negativeVotes;
  }

  /**
   * Getter for status poll
   *
   * @return status poll
   */
  public StatusPoll getStatusPoll() {
    return this.statusPoll;
  }

  /**
   * Getter for delegate vote
   *
   * @param delegate delegate to get the vote from
   * @return delegate vote
   */
  public Boolean getDelegateVote(Delegate delegate) {
    return delegateVotes.get(delegate.getId());
  }

  /**
   * Checks if a citizen based on an id has voted or not
   *
   * @param citizenID id of the citizen
   * @return true if voted, false otherwise
   */
  public boolean hasVoted(Long citizenID) {
    return this.idList.contains(citizenID);
  }
}
