package pt.ul.fc.css.example.demo.business.services;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pt.ul.fc.css.example.demo.business.entities.Project;

/**
 * Class that represents a Service for the Close Project use case, making sure to close projects.
 */
@Service
public class CloseProjectsService {

  @Autowired private ProjectService projectService;

  private LocalDateTime now = LocalDateTime.now();

  /**
   * Closes all projects that have had their end date due, not being able to receive more
   * signatures.
   */
  @Scheduled(fixedDelay = 3600000)
  public void closeProjects() {
    try {
      List<Project> projects = projectService.getProjects();
      for (Project project : projects) {
        if (project.verifyOngoing(now)) {
          project.setOngoing(false);
          projectService.putProject(project);
        }
      }
    } catch (Exception e) {

    }
  }
}
