package pt.ul.fc.css.example.demo.business.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents a Project entity,and all it's methods such as getters and setters, methods
 * to verify conditions such as checking if there are signatures to a project, writting to pdf,
 * conversion of pdf to bytes and persistence, along with annotations to tables, and strategies
 */
@Entity
public class Project {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;
  private String text;
  private List<Long> signatures;
  private LocalDateTime endDate;
  private boolean ongoing;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "delegate_id")
  private Delegate delegate;

  @OneToOne(mappedBy = "project", cascade = CascadeType.ALL)
  private Poll poll;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "theme_id")
  private Theme theme;

  /** Default constructor for Project. */
  public Project() {}

  /**
   * Constructor for project given a title, a text description, a pdf file, an end date a delegate
   * and a theme
   *
   * @param title project's title
   * @param text project's text description
   * @param pdf pdf file
   * @param endDate end date for the project
   * @param delegate delegate assigned to the project
   * @param theme project's theme
   */
  public Project(String title, String text, LocalDateTime endDate, Delegate delegate, Theme theme) {
    this.title = title;
    this.text = text;
    this.signatures = new ArrayList<Long>();
    this.endDate = endDate;
    this.delegate = delegate;
    this.ongoing = true;
    this.theme = theme;
  }

  /**
   * Getter for the id
   *
   * @return project's id
   */
  public long getId() {
    return id;
  }

  /**
   * Setter for the id
   *
   * @param id id being set
   */
  public void setId(long id) {
    this.id = id;
  }

  /**
   * Getter for the title
   *
   * @return project's title
   */
  public String getTitle() {
    return title;
  }

  /**
   * Setter for the title
   *
   * @param title title being set
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Getter for the descriptive text
   *
   * @return descriptive text
   */
  public String getText() {
    return text;
  }

  /**
   * Setter for the descriptive text
   *
   * @param text text being set
   */
  public void setText(String text) {
    this.text = text;
  }

  /**
   * Getter for the list of signatures for the project
   *
   * @return list of signatures
   */
  public List<Long> getSignatures() {
    return signatures;
  }

  /**
   * Add a signature to the project's list of signatures
   *
   * @param signature signature being added
   */
  public void addSignature(Long signature) {
    if (!signatures.contains(signature)) {
      signatures.add(signature);
    }
  }

  /**
   * Checks if a citizen has already signed to this poll
   *
   * @param id id being checked
   * @return true if the citizen has already voted, false otherwise
   */
  public boolean checkIfSigned(Long id) {
    return signatures.contains(id);
  }

  /**
   * Sets the project's poll
   *
   * @param poll poll being set
   */
  public void setPoll(Poll poll) {
    this.poll = poll;
  }

  /**
   * Gets the project's poll
   *
   * @return project's poll
   */
  public Poll getPoll() {
    return poll;
  }

  /**
   * Checks if there's a poll
   *
   * @return true if there's a poll, false otherwise
   */
  public boolean hasPoll() {
    return poll != null;
  }

  /**
   * Get's the Project's end date
   *
   * @return project's end date
   */
  public LocalDateTime getDate() {
    return endDate;
  }

  /**
   * Sets the project's end date to the specified date
   *
   * @param date end date being set
   */
  public void setDate(LocalDateTime date) {
    this.endDate = date;
  }

  /**
   * Checks if the project is still ongoing
   *
   * @return true if ongoing, false otherwise
   */
  public boolean isOngoing() {
    return ongoing;
  }

  /**
   * Sets ongoing status
   *
   * @param ongoing status being set
   */
  public void setOngoing(boolean ongoing) {
    this.ongoing = ongoing;
  }

  /**
   * Verify is the project is still ongoing given a date
   *
   * @param date date being checked
   * @return true if it's still ongoing, false otherwise
   */
  public boolean verifyOngoing(LocalDateTime date) {

    return (endDate.isAfter(date) || endDate.isEqual(date));
  }

  /**
   * Getter for delegate
   *
   * @return delegate
   */
  public Delegate getDelegate() {
    return this.delegate;
  }

  /**
   * Getter for theme
   *
   * @return theme
   */
  public Theme getTheme() {
    return theme;
  }

  /**
   * Setter for theme
   *
   * @param theme theme being set
   */
  public void setTheme(Theme theme) {
    this.theme = theme;
  }
}
