package pt.ul.fc.css.example.demo.business.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * Class that represents a CitizenThemeDelegate entity with their respective information such as
 * their theme, delegate and citizen, along with getters and setters for themes, delegates and
 * citizens and their respective annotations for tables, and strategies. This entity serves to make
 * a connection between a Citizen, their respective Delegate with a given theme.
 */
@Entity
public class CitizenThemeDelegate {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "voter_id")
  private Citizen citizen;

  @ManyToOne
  @JoinColumn(name = "delegate_id")
  private Delegate delegate;

  @ManyToOne
  @JoinColumn(name = "theme_id")
  private Theme theme;

  /** Default constructor for CitizenThemeDelegate. */
  public CitizenThemeDelegate() {}

  /**
   * Constructor for CitizenThemeDelegate, making use of a theme, a delegate and a citizen.
   *
   * @param theme delegate's theme
   * @param delegate citizen's chosen delegate
   * @param citizen given citizen
   */
  public CitizenThemeDelegate(Theme theme, Delegate delegate, Citizen citizen) {
    this.theme = theme;
    this.delegate = delegate;
    this.citizen = citizen;
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
   * @param theme theme to be set
   */
  public void setTheme(Theme theme) {
    this.theme = theme;
  }

  /**
   * Getter for delegate
   *
   * @return delegate
   */
  public Delegate getDelegate() {
    return delegate;
  }

  /**
   * Setter for delegate
   *
   * @param delegate delegate to be set
   */
  public void setDelegate(Delegate delegate) {
    this.delegate = delegate;
  }

  /**
   * Setter for citizen
   *
   * @param citizen citizen to be set
   */
  public void setCitizen(Citizen citizen) {
    this.citizen = citizen;
  }

  /**
   * Getter for citizen
   *
   * @return citizen
   */
  public Citizen getCitizen() {
    return citizen;
  }
}
