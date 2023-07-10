package pt.ul.fc.css.example.demo.business.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import pt.ul.fc.css.example.demo.business.entities.Project;
import pt.ul.fc.css.example.demo.business.repositories.ProjectRepository;

/**
 * Class that represents a Project service, making use of its Project Repository methods, and
 * getting Projects, a list of Projects, putting projects and finding specific projects by specific
 * conditions.
 */
@Service
public class ProjectService {

  @Autowired private final ProjectRepository projectRepository;

  /**
   * Constructor for the project service, using a project repository.
   *
   * @param projectRepository project repository
   */
  public ProjectService(ProjectRepository projectRepository) {
    this.projectRepository = projectRepository;
  }

  /**
   * Getter for a project given a specific id
   *
   * @param id id being given
   * @return gets a project with the given id
   */
  public Project getProject(@PathVariable Long id) {
    return this.projectRepository.getReferenceById(id);
  }

  /**
   * Getter for all the projects
   *
   * @return all projects
   */
  public List<Project> getProjects() {
    return this.projectRepository.findAll();
  }

  /**
   * Puts a given project in the repository.
   *
   * @param project given project
   * @return the saved project's id
   */
  public Long putProject(@NonNull @RequestBody Project project) {
    try {
      Project saved = projectRepository.save(project);
      return saved.getId();
    } catch (Exception e) {
      throw new RuntimeException("Failed to save project: " + e.getMessage(), e);
    }
  }

  /**
   * Returns all the projects still ongoing
   *
   * @return projects still ongoing
   */
  public List<Project> findByOnGoingTrue() {
    return this.projectRepository.findByongoingTrue();
  }

  /**
   * Finder for a project given an id
   *
   * @param projectId given project id
   * @return project
   */
  public Optional<Project> findById(Long projectId) {
    return projectRepository.findById(projectId);
  }
}
