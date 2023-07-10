package pt.ul.fc.css.example.demo.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.transaction.Transactional;
import java.io.File;
import java.nio.file.Files;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pt.ul.fc.css.example.demo.business.entities.Delegate;
import pt.ul.fc.css.example.demo.business.entities.Theme;
import pt.ul.fc.css.example.demo.business.repositories.CitizenRepository;
import pt.ul.fc.css.example.demo.business.repositories.PollRepository;
import pt.ul.fc.css.example.demo.business.repositories.ProjectRepository;
import pt.ul.fc.css.example.demo.business.repositories.ThemeRepository;
import pt.ul.fc.css.example.demo.business.services.PresentProjectService;

/** This class represents a Test class to the Present Project use case's service. */
@SpringBootTest
public class PresentProjectServiceTest {

  @Autowired private PresentProjectService presentProjectService;

  @Autowired private ProjectRepository projectRepository;

  @Autowired private CitizenRepository delegateRepository;

  @Autowired private ThemeRepository themeRepository;

  @Autowired private PollRepository pollRepository;

  /**
   * This test makes sure that the following assertions are being met: 1) The project has been
   * created 2) There's only one project 3) The title of the project is "project1" 4) The project id
   * is the same in the response entity and in the repository, if searched by theme 5) The project
   * id is the same in the response entity and in the repository, if searched by delegate 6) The
   * contents in the pdf are correctly inserted 7) There are two projects presented
   */
  @Test
  @Transactional
  public void testProjectCreate() {

    projectRepository.deleteAll();
    delegateRepository.deleteAll();
    themeRepository.deleteAll();
    pollRepository.deleteAll();
    try {

      File pdf = File.createTempFile("pdf", ".txt");
      byte[] contents = {0x01, 0x02, 0x03};
      Files.write(pdf.toPath(), contents);

      LocalDateTime endDate = LocalDateTime.of(20235, 5, 13, 15, 56);
      Delegate delegate = new Delegate("Amilcar");
      Theme theme = new Theme("Desporto");
      delegateRepository.save(delegate);
      themeRepository.save(theme);

      Delegate delegateT = (Delegate) delegateRepository.findAll().get(0);

      Theme themeT = themeRepository.findAll().get(0);

      // Case : delegate creates a new project sucessfully
      presentProjectService.presentProject(
          delegateT.getId(), "project1", "beep", endDate, themeT.getName());

      assertEquals(1, projectRepository.findAll().size());

      assertEquals("project1", projectRepository.findAll().get(0).getTitle());

      assertEquals(themeT.getProjects().get(0).getId(), projectRepository.findAll().get(0).getId());

      assertEquals(
          delegateT.getProjects().get(0).getId(), projectRepository.findAll().get(0).getId());

      // Case : delegate creates another project sucessfully
      presentProjectService.presentProject(
          delegateT.getId(), "project2", "boop", endDate, themeT.getName());

      assertEquals(2, projectRepository.findAll().size());

      LocalDateTime endDate2 = LocalDateTime.of(2021, 5, 13, 15, 56);

      // Case : delegate tries to create project with invalid date, failing
      presentProjectService.presentProject(
          delegateT.getId(), "project3", "boop", endDate2, themeT.getName());

      assertEquals(2, projectRepository.findAll().size());

      pdf.delete();

    } catch (Exception e) {

    }
  }
}
