package pt.ul.fc.css.example.demo.business.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import org.springframework.lang.NonNull;

/**
 * Class that represents a Citizen entity with their respective information such as their name and
 * ID, along with getters and setters, furthermore being able to get their Delegates and setting
 * them as well. Also being defined are the associations to tables, along with strategies.
 */
@Entity
@Table(name = "citizen")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public class Citizen {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @OneToMany(mappedBy = "citizen", cascade = CascadeType.ALL)
  private List<CitizenThemeDelegate> citizenThemeDelegates;

  /** Default constructor for citizen. */
  public Citizen() {}

  /**
   * Constructor for citizen
   *
   * @param name citizen's name
   */
  public Citizen(@NonNull String name) {
    this.name = name;
    this.citizenThemeDelegates = new ArrayList<CitizenThemeDelegate>();
  }

  /**
   * Returns citizen id
   *
   * @return citizen id
   */
  public long getId() {
    return id;
  }

  /**
   * Sets citizen id
   *
   * @param id id being set
   */
  public void setId(long id) {
    this.id = id;
  }

  /**
   * Gets citizen name
   *
   * @return citizen name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets citizen name
   *
   * @param name name being set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Adds to a citizen a delegate and its theme.
   *
   * @param citizenThemeDelegate citizen-theme-delegate being added.
   */
  public void addThemeDelegate(CitizenThemeDelegate citizenThemeDelegate) {
    Integer index = null;
    for (int i = 0; i < citizenThemeDelegates.size(); i++) {
      if (citizenThemeDelegates.get(i).getTheme().equals(citizenThemeDelegate.getTheme())) {
        index = i;
      }
    }
    if (index == null) {
      this.citizenThemeDelegates.add(citizenThemeDelegate);
    } else {
      this.citizenThemeDelegates.set(index, citizenThemeDelegate);
    }
  }

  /**
   * Returns a citizen's delegate given a theme
   *
   * @param theme theme the delegate's being searched for
   * @return a delegate in case there's a theme for them, null otherwise
   */
  public Delegate getDelegate(Theme theme) {
    for (CitizenThemeDelegate ctd : citizenThemeDelegates) {
      if (ctd.getTheme().equals(theme)) {
        return ctd.getDelegate();
      }
    }
    return null;
  }
}
