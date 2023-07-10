package pt.ul.fc.css.example.demo.business.entities;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pt.ul.fc.css.example.demo.utils.StatusPoll;

/**
 * Class that represents a Poll entity and all it's needed methods, such as getters and setters and
 * verifications to check whether or not it's an ongoing poll according to dates, or if a citizen
 * has voted. Also has annotations for tables and strategies.
 */
@Entity
public class Poll {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private List<Long> idList;

  private int positiveVotes;

  private int negativeVotes;

  private String name;

  private LocalDateTime endDate;

  @Enumerated(EnumType.STRING)
  private StatusPoll statusPoll;

  @ElementCollection
  @CollectionTable(name = "delegate_votes", joinColumns = @JoinColumn(name = "poll_id"))
  @MapKeyColumn(name = "delegate_column")
  @Column(name = "vote_column")
  private Map<Long, Boolean> delegateVotes;

  @OneToOne
  @JoinColumn(name = "project_id")
  private Project project;

  /** Default constructor for Poll. */
  public Poll() {}

  /**
   * Constructor for poll given a poll name, the number of positive votes, the number of negative
   * votes the project associated to the poll, the status of the poll, and its end date.
   *
   * @param name Poll name
   * @param positiveVotes number of positive votes
   * @param negativeVotes number of negative votes
   * @param project poll's project
   * @param statusPoll status of the poll
   * @param endDate poll's end date
   */
  public Poll(
      String name,
      int positiveVotes,
      int negativeVotes,
      Project project,
      StatusPoll statusPoll,
      LocalDateTime endDate) {

    this.name = name;
    this.idList = new ArrayList<Long>();
    this.positiveVotes = positiveVotes;
    this.negativeVotes = negativeVotes;
    this.project = project;
    this.endDate = endDate;
    this.statusPoll = statusPoll;

    this.delegateVotes = new HashMap<Long, Boolean>();
  }

  /**
   * Getter for id
   *
   * @return poll's id
   */
  public Long getId() {
    return id;
  }

  /**
   * Setter for id
   *
   * @param id to be set
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Getter for name
   *
   * @return poll's name
   */
  public String getName() {
    return name;
  }

  /**
   * Getter for end date
   *
   * @return poll's end date
   */
  public LocalDateTime getEndDate() {
    return endDate;
  }

  /**
   * Getter for id list
   *
   * @return poll's id list
   */
  public List<Long> getIdList() {
    return idList;
  }

  /**
   * Setter for id list
   *
   * @param idList id list to be set
   */
  public void setIdList(List<Long> idList) {
    this.idList = idList;
  }

  /**
   * Adds a vote id to the poll's id list
   *
   * @param voteId vote id to be added
   */
  public void addVoteId(Long voteId) {
    this.idList.add(voteId);
  }

  /**
   * Getter for the positive votes
   *
   * @return number of positive votes
   */
  public int getPositiveVotes() {
    return positiveVotes;
  }

  /**
   * Setter for positive votes
   *
   * @param positiveVotes number of positive votes to be set
   */
  public void setPositiveVotes(int positiveVotes) {
    this.positiveVotes = positiveVotes;
  }

  /**
   * Getter for the negative votes
   *
   * @return number of negative votes
   */
  public int getNegativeVotes() {
    return negativeVotes;
  }

  /**
   * Setter for negative votes
   *
   * @param negativeVotes number of negative votes to be set
   */
  public void setNegativeVotes(int negativeVotes) {
    this.negativeVotes = negativeVotes;
  }

  /**
   * Setter for the poll's status
   *
   * @param statusPoll poll status to be set
   */
  public void setStatusPoll(StatusPoll statusPoll) {
    this.statusPoll = statusPoll;
  }

  /**
   * Getter for the poll's status
   *
   * @return poll's status
   */
  public StatusPoll getStatusPoll() {
    return this.statusPoll;
  }

  /** Setter for the poll's status to be ongoing */
  public void setOngoing() {
    this.statusPoll = StatusPoll.ONGOING;
  }

  /**
   * Verifies if the poll's end date has transcended.
   *
   * @param date date being checked compared to the end date
   * @return true if it's ongoing, false otherwise
   */
  public boolean verifyFinished(LocalDateTime date) {

    return (endDate.isBefore(date) || endDate.isEqual(date));
  }

  /**
   * Adds the delegate vote and its choice.
   *
   * @param id vote id
   * @param vote choice value
   */
  public void addDelegateVotes(Long id, boolean vote) {
    this.delegateVotes.put(id, vote);
  }

  /**
   * Given a vote, it finds the voter by ID, and then based on the decision adds a positive or
   * negative vote
   *
   * @param vote given vote
   */
  public void voteOn(Vote vote) {
    this.idList.add(vote.getId());
    if (vote.getDecision()) {
      this.positiveVotes++;
    } else {
      this.negativeVotes++;
    }
  }

  /**
   * Setter for project
   *
   * @param project project being set
   */
  public void setProject(Project project) {
    this.project = project;
  }

  /**
   * Getter for project
   *
   * @return poll's project
   */
  public Project getProject() {
    return project;
  }

  /**
   * Getter for a given delegate's vote
   *
   * @param delegate given delegate
   * @return delegate's vote
   */
  public Boolean getDelegateVote(Delegate delegate) {
    return delegateVotes.get(delegate.getId());
  }

  /**
   * Checks whether or not a citizen has voted
   *
   * @param citizenID citizen id
   * @return true if the citizen has voted, false otherwise
   */
  public boolean hasVoted(Long citizenID) {
    return this.idList.contains(citizenID);
  }

  public Map<Long, Boolean> getDelegateVotesMap() {
    return this.delegateVotes;
  }
}
