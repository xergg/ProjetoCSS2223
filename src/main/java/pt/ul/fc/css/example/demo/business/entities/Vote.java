package pt.ul.fc.css.example.demo.business.entities;

/** Class that represents a Vote entity, along with all its getters and setters. */
public class Vote {

  private Long id;
  private boolean decision;

  /**
   * Constructor for a vote, which uses a vote id and a decision
   *
   * @param id vote id
   * @param decision decision given
   */
  public Vote(Long id, boolean decision) {
    this.id = id;
    this.decision = decision;
  }

  /**
   * Getter for id
   *
   * @return vote's id
   */
  public Long getId() {
    return id;
  }

  /**
   * Setter for id
   *
   * @param id id being set
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Getter for the decision
   *
   * @return vote's decision
   */
  public boolean getDecision() {
    return decision;
  }

  /**
   * Setter for the decision
   *
   * @param decision decision being set
   */
  public void setDecision(boolean decision) {
    this.decision = decision;
  }
}
