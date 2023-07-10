package pt.ul.fc.css.example.demo.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pt.ul.fc.css.example.demo.business.entities.Delegate;
import pt.ul.fc.css.example.demo.business.entities.Project;
import pt.ul.fc.css.example.demo.business.entities.Theme;
import pt.ul.fc.css.example.demo.business.repositories.CitizenRepository;
import pt.ul.fc.css.example.demo.business.repositories.PollRepository;
import pt.ul.fc.css.example.demo.business.repositories.ProjectRepository;
import pt.ul.fc.css.example.demo.business.repositories.ThemeRepository;
import pt.ul.fc.css.example.demo.business.services.CloseProjectsService;

/** This class represents a Test class to the Close Project use case's service. */
@SpringBootTest
public class CloseProjectsServiceTest {

  @Autowired private CloseProjectsService closeProjectsService;

  @Autowired private ProjectRepository projectRepository;

  @Autowired private CitizenRepository delegateRepository;

  @Autowired private ThemeRepository themeRepository;

  @Autowired private PollRepository pollRepository;

  /**
   * This test makes sure the following assertions are being fulfilled: 1) The size of ongoing
   * projects in the repository is the same as the local list. 2) The size after closing the
   * projects in the service is well saved in the local list, which is 2 in this case.
   */
  @Test
  public void CloseExpiredProjects() {

    projectRepository.deleteAll();
    delegateRepository.deleteAll();
    themeRepository.deleteAll();
    pollRepository.deleteAll();

    Delegate delegate = new Delegate("Amilcar");
    Theme theme = new Theme("Desporto");
    delegateRepository.save(delegate);
    themeRepository.save(theme);

    LocalDateTime endDate = LocalDateTime.of(2025, 5, 13, 15, 56);
    LocalDateTime endDate2 = LocalDateTime.of(2021, 5, 13, 15, 56);
    LocalDateTime endDate3 = LocalDateTime.of(2024, 5, 13, 15, 56);
    LocalDateTime endDate4 = LocalDateTime.of(2019, 5, 13, 15, 56);

    Project project1 = new Project("Projeto1", "Texto projeto1", endDate, delegate, theme);
    Project project2 = new Project("Projeto2", "Texto projeto2", endDate2, delegate, theme);
    Project project3 = new Project("Projeto3", "Texto projeto3", endDate3, delegate, theme);
    Project project4 = new Project("Projeto4", "Texto projeto4", endDate4, delegate, theme);

    projectRepository.save(project1);
    projectRepository.save(project2);
    projectRepository.save(project3);
    projectRepository.save(project4);

    List<Project> ongoingT = projectRepository.findByongoingTrue();

    assertEquals(ongoingT.size(), projectRepository.findAll().size());

    // dated projects should close (2 staying open , 2 closing)
    closeProjectsService.closeProjects();

    ongoingT = projectRepository.findByongoingTrue();

    assertEquals(2, ongoingT.size());
  }
}
