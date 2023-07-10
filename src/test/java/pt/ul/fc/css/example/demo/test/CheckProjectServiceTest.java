package pt.ul.fc.css.example.demo.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pt.ul.fc.css.example.demo.business.dtos.ProjectDTO;
import pt.ul.fc.css.example.demo.business.entities.Delegate;
import pt.ul.fc.css.example.demo.business.entities.Project;
import pt.ul.fc.css.example.demo.business.entities.Theme;
import pt.ul.fc.css.example.demo.business.exceptions.ProjectNotFoundException;
import pt.ul.fc.css.example.demo.business.repositories.CitizenRepository;
import pt.ul.fc.css.example.demo.business.repositories.ProjectRepository;
import pt.ul.fc.css.example.demo.business.repositories.ThemeRepository;
import pt.ul.fc.css.example.demo.business.services.CheckProjectService;

/** Class that represents a Test class for the Check Project use case's service. */
@SpringBootTest
public class CheckProjectServiceTest {

  @Autowired private CheckProjectService checkProjectService;

  @Autowired private ProjectRepository projectRepository;

  @Autowired private CitizenRepository delegateRepository;

  @Autowired private ThemeRepository themeRepository;

  /**
   * This test makes sure that all projects are still not expired. The size of the repository should
   * be the same as the check non expired projects function from the service.
   *
   * @throws ProjectNotFoundException
   */
  @Test
  @Transactional
  public void testNonExpiredProjects() throws ProjectNotFoundException {

    projectRepository.deleteAll();
    delegateRepository.deleteAll();
    themeRepository.deleteAll();

    LocalDateTime endDate = LocalDateTime.of(2023, 5, 13, 15, 56);
    Delegate delegate = new Delegate("Amilcar");
    Theme theme = new Theme("Desporto");
    delegateRepository.save(delegate);
    themeRepository.save(theme);

    Project project1 = new Project("Projeto1", "Texto projeto1", endDate, delegate, theme);
    Project project2 = new Project("Projeto2", "Texto projeto1", endDate, delegate, theme);
    projectRepository.save(project1);
    projectRepository.save(project2);

    List<ProjectDTO> projectList = checkProjectService.CheckNonExpiredProjects();

    assertEquals(projectRepository.findAll().size(), projectList.size());
  }

  /**
   * This test makes sure there's at least one expired project. We then compare the first element of
   * the repository list to make sure they're the same, meaning there's one expired project.
   *
   * @throws ProjectNotFoundException
   */
  @Test
  @Transactional
  public void testAtLeastOneExpiredProjects() throws ProjectNotFoundException {

    projectRepository.deleteAll();
    delegateRepository.deleteAll();
    themeRepository.deleteAll();

    LocalDateTime endDate = LocalDateTime.of(2023, 5, 13, 15, 56);
    Delegate delegate = new Delegate("Amilcar");
    Theme theme = new Theme("Desporto");
    delegateRepository.save(delegate);
    themeRepository.save(theme);

    Project project1 = new Project("Projeto1", "Texto projeto1", endDate, delegate, theme);
    Project project2 = new Project("Projeto2", "Texto projeto1", endDate, delegate, theme);
    project2.setOngoing(false);
    projectRepository.save(project1);
    projectRepository.save(project2);

    // citizen tries to get projects , only one not being expired
    List<ProjectDTO> projectList = checkProjectService.CheckNonExpiredProjects();

    assertEquals(projectRepository.findAll().get(0).getTitle(), projectList.get(0).getTitle());
  }

  /**
   * This test makes sure all projects are expired. By setting the ongoing to false, there's nothing
   * going to the function check non expired projects, making the size of the response entity 0,
   * meaning there are no Non Expired Projects.
   *
   * @throws ProjectNotFoundException
   */
  @Test
  @SuppressWarnings("unused")
  public void testAllExpiredProjects() throws ProjectNotFoundException {

    projectRepository.deleteAll();
    delegateRepository.deleteAll();
    themeRepository.deleteAll();

    LocalDateTime endDate = LocalDateTime.of(2023, 5, 13, 15, 56);
    Delegate delegate = new Delegate("Amilcar");
    Theme theme = new Theme("Desporto");
    delegateRepository.save(delegate);
    themeRepository.save(theme);

    Project project1 = new Project("Projeto1", "Texto projeto1", endDate, delegate, theme);
    Project project2 = new Project("Projeto2", "Texto projeto1", endDate, delegate, theme);
    project1.setOngoing(false);
    project2.setOngoing(false);
    projectRepository.save(project1);
    projectRepository.save(project2);

    // citizen tries to get none expired projects, existing none

    try {
      List<ProjectDTO> projectList = checkProjectService.CheckNonExpiredProjects();
      fail("Expected ProjectNotFoundException to be thrown");

    } catch (ProjectNotFoundException e) {
      assertEquals(0, projectRepository.findByongoingTrue().size());
    }
  }
}
