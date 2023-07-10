package pt.ul.fc.css.example.demo.business.dtos;

import java.util.List;

/**
 * Class that represents a Project DTO, for all the REST API, to encapsulate the data, for easier
 * handling between the backend and frontend.
 */
public class ProjectDTO {
  private Long id;

  private String title;
  private String text;
  private List<Long> signatures;
  private boolean ongoing;
  private DelegateDTO delegate;
  private ThemeDTO theme;
  private String d;

  /**
   * Constructor for a Project DTO
   *
   * @param id dto id
   * @param title dto title
   * @param text dto text
   * @param d dto end date
   * @param delegate dto's chosen delegate
   * @param theme dto's theme
   * @param signatures dto's signature
   * @param ongoing dto's status to check if it's ongoing
   */
  public ProjectDTO(
      Long id,
      String title,
      String text,
      String d,
      DelegateDTO delegate,
      ThemeDTO theme,
      List<Long> signatures,
      boolean ongoing) {
    this.id = id;
    this.title = title;
    this.text = text;
    this.signatures = signatures;
    this.d = d;
    this.delegate = delegate;
    this.ongoing = ongoing;
    this.theme = theme;
  }

  /**
   * Getter for ID
   *
   * @return id
   */
  public Long getId() {
    return id;
  }

  /**
   * Setter for id
   *
   * @param id id to be set
   */
  public void setId(long id) {
    this.id = id;
  }

  /**
   * Getter for title
   *
   * @return title
   */
  public String getTitle() {
    return title;
  }

  /**
   * Getter for text
   *
   * @return text
   */
  public String getText() {
    return text;
  }

  /**
   * Getter for signatures
   *
   * @return list of signatures
   */
  public List<Long> getSignatures() {
    return signatures;
  }

  /**
   * Checks if a given id has signed
   *
   * @param id given id
   * @return true if signed, false otherwise
   */
  public boolean checkIfSigned(Long id) {
    return signatures.contains(id);
  }

  /**
   * Getter for ongoing
   *
   * @return ongoing value
   */
  public boolean isOngoing() {
    return ongoing;
  }

  /**
   * Getter for theme
   *
   * @return theme dto
   */
  public ThemeDTO getTheme() {
    return theme;
  }

  /**
   * Getter for delegate
   *
   * @return delegate dto
   */
  public DelegateDTO getDelegate() {
    return this.delegate;
  }

  /**
   * Getter for end date
   *
   * @return end date
   */
  public String getD() {
    return d;
  }
}
