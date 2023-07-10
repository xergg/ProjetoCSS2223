package pt.ul.fc.css.example.demo.business.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.ul.fc.css.example.demo.business.dtos.DelegateDTO;
import pt.ul.fc.css.example.demo.business.dtos.ProjectDTO;
import pt.ul.fc.css.example.demo.business.dtos.ThemeDTO;
import pt.ul.fc.css.example.demo.business.entities.Project;
import pt.ul.fc.css.example.demo.business.exceptions.ProjectNotFoundException;

/**
 * Class that represents a Service for the Check Project use case, checking overall all the non
 * expired projects.
 */
@Service
public class CheckProjectService {

  @Autowired private ProjectService projectService;

  /**
   * Checks all the non expired projects available.
   *
   * @return List of projects that are not expired.
   * @throws ProjectNotFoundException
   */
  public List<ProjectDTO> CheckNonExpiredProjects() throws ProjectNotFoundException {
    List<Project> projects = projectService.findByOnGoingTrue();
    List<ProjectDTO> projectDTOs = new ArrayList<>();
    if (projects.isEmpty()) {
      throw new ProjectNotFoundException();
    } else {
      for (Project project : projects) {
        projectDTOs.add(
            new ProjectDTO(
                project.getId(),
                project.getTitle(),
                project.getText(),
                project.getDate().toString(),
                new DelegateDTO(project.getDelegate().getId(), project.getDelegate().getName()),
                new ThemeDTO(project.getTheme().getId(), project.getTheme().getName()),
                project.getSignatures(),
                project.isOngoing()));
      }
      return projectDTOs;
    }
  }

  public Object getProject(Long projectId) throws ProjectNotFoundException {

    Optional<Project> project = projectService.findById(projectId);

    if (project.isEmpty()) {

      throw new ProjectNotFoundException();

    } else {

      Project p = project.get();

      return new ProjectDTO(
          p.getId(),
          p.getTitle(),
          p.getText(),
          p.getDate().toString(),
          new DelegateDTO(p.getDelegate().getId(), p.getDelegate().getName()),
          new ThemeDTO(p.getTheme().getId(), p.getTheme().getName()),
          p.getSignatures(),
          p.isOngoing());
    }
  }
}
