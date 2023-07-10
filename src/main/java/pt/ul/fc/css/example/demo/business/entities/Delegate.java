package pt.ul.fc.css.example.demo.business.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import org.springframework.lang.NonNull;

/**
 * Class that represents and Delegate entity, which is a child from Citizen hierarchy. Has all the
 * Citizen methods such as getters and setters, and a couple more specific ones exclusive to a
 * delegate. Also has annotations to describe relationships in tables.
 */
@Entity
@DiscriminatorValue("delegate")
public class Delegate extends Citizen {

  @OneToMany(mappedBy = "delegate", cascade = CascadeType.ALL)
  private List<Project> projects;

  /** Default constructor for Delegate */
  public Delegate() {}

  /**
   * Constructor for a delegate.
   *
   * @param name Delegate's name
   */
  public Delegate(@NonNull String name) {
    super(name);
    this.projects = new ArrayList<Project>();
  }

  /**
   * Adds a project to a delegate
   *
   * @param project project being given
   */
  public void addProject(Project project) {
    projects.add(project);
  }

  /**
   * Gets all projects a Delegate is assigned to
   *
   * @return projects assigned to
   */
  public List<Project> getProjects() {
    return projects;
  }
}
