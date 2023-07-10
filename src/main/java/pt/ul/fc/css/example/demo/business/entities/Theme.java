package pt.ul.fc.css.example.demo.business.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents a Theme entity, along with it's getters and setters, which can be a child
 * from Theme hierarchy and also a parent Also has annotations for the relationships being set in
 * tables, along with strategies.
 */
@Entity
public class Theme {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @OneToMany(mappedBy = "theme")
  private List<Project> projects;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_theme_id")
  private Theme parentTheme;

  @OneToMany(mappedBy = "parentTheme", cascade = CascadeType.ALL)
  private List<Theme> subthemes;

  /** Default constructor for theme. */
  public Theme() {}

  /**
   * Constructor for theme, with the respective name.
   *
   * @param name name being given to theme.
   */
  public Theme(String name) {
    this.name = name;
    projects = new ArrayList<Project>();
    subthemes = new ArrayList<Theme>();
  }

  /**
   * Getter for id
   *
   * @return theme's id
   */
  public long getId() {
    return id;
  }

  /**
   * Setter for id
   *
   * @param id id being set
   */
  public void setId(long id) {
    this.id = id;
  }

  /**
   * Getter for name
   *
   * @return theme's name
   */
  public String getName() {
    return name;
  }

  /**
   * Setter for name
   *
   * @param name name being set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Adds a project to a theme
   *
   * @param project project being added
   */
  public void addProject(Project project) {
    this.projects.add(project);
  }

  /**
   * Gets all the projects associated with a theme
   *
   * @return projects associated with theme
   */
  public List<Project> getProjects() {
    return projects;
  }

  /**
   * Gets all subthemes from a theme
   *
   * @return all subthemes from a theme
   */
  public List<Theme> getSubThemes() {
    return subthemes;
  }

  /**
   * Adds a subtheme
   *
   * @param theme given subtheme
   */
  public void addSubTheme(Theme theme) {
    this.subthemes.add(theme);
  }

  /**
   * Sets a specific theme as a parent
   *
   * @param theme given theme to be set as a parent
   */
  public void setParent(Theme theme) {
    this.parentTheme = theme;
  }

  /**
   * Getter for a parent theme
   *
   * @return father theme
   */
  public Theme getParent() {
    return this.parentTheme;
  }

  /**
   * Checks whether or not a theme has a parent theme
   *
   * @return true if it has a parent theme, false otherwise
   */
  public boolean hasParent() {
    return (this.parentTheme != null);
  }
}
