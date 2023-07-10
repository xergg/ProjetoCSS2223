package pt.ul.fc.css.example.demo.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pt.ul.fc.css.example.demo.business.entities.Citizen;
import pt.ul.fc.css.example.demo.business.entities.Delegate;
import pt.ul.fc.css.example.demo.business.entities.Project;
import pt.ul.fc.css.example.demo.business.entities.Theme;
import pt.ul.fc.css.example.demo.business.exceptions.AlreadyVotedException;
import pt.ul.fc.css.example.demo.business.exceptions.CitizenNotFoundException;
import pt.ul.fc.css.example.demo.business.exceptions.ProjectNotFoundException;
import pt.ul.fc.css.example.demo.business.exceptions.ProjectNotOnGoingException;
import pt.ul.fc.css.example.demo.business.repositories.CitizenRepository;
import pt.ul.fc.css.example.demo.business.repositories.PollRepository;
import pt.ul.fc.css.example.demo.business.repositories.ProjectRepository;
import pt.ul.fc.css.example.demo.business.repositories.ThemeRepository;
import pt.ul.fc.css.example.demo.business.services.SupportProjectService;

/** This class represents a Test class to the Support Project use case's service. */
@SpringBootTest
public class SupportProjectServiceTest {

  @Autowired private SupportProjectService supportProjectService;

  @Autowired private ProjectRepository projectRepository;

  @Autowired private CitizenRepository citizenRepository;

  @Autowired private ThemeRepository themeRepository;

  @Autowired private PollRepository pollRepository;

  /**
   * This test makes sure the following assumptions are being met: 1) There's only been one
   * signature made so far 2) There's been two signatures made so far 3) There's been three
   * signatures made so far 4) Even though a citizen tried to vote twice, there's still three
   * signatures made so far
   *
   * @throws ProjectNotOnGoingException
   * @throws AlreadyVotedException
   * @throws ProjectNotFoundException
   * @throws CitizenNotFoundException
   */
  @Test
  public void supportProject()
      throws CitizenNotFoundException,
          ProjectNotFoundException,
          AlreadyVotedException,
          ProjectNotOnGoingException {

    projectRepository.deleteAll();
    citizenRepository.deleteAll();
    themeRepository.deleteAll();
    pollRepository.deleteAll();

    LocalDateTime endDate = LocalDateTime.of(2025, 5, 13, 15, 56);
    Delegate delegate = new Delegate("Amilcar");
    Theme theme = new Theme("Desporto");
    themeRepository.save(theme);

    Citizen Maria = new Citizen("Maria");
    Citizen Manuel = new Citizen("Manuel");
    Citizen Guize = new Citizen("Guize");

    delegate = citizenRepository.save(delegate);
    Maria = citizenRepository.save(Maria);
    Manuel = citizenRepository.save(Manuel);
    Guize = citizenRepository.save(Guize);

    Project project1 = new Project("Projeto1", "Texto projeto1", endDate, delegate, theme);

    project1 = projectRepository.save(project1);

    // Case : citizen maria supports project sucessfully, adding one signature
    supportProjectService.supportProject(Maria.getId(), project1.getId());

    assertEquals(1, projectRepository.findAll().get(0).getSignatures().size());

    // Case : citizen manuel supports project sucessfully adding one signature
    supportProjectService.supportProject(Manuel.getId(), project1.getId());

    assertEquals(2, projectRepository.findAll().get(0).getSignatures().size());

    // Case : citizen guize supports project sucessfully adding one signature
    supportProjectService.supportProject(Guize.getId(), project1.getId());

    assertEquals(3, projectRepository.findAll().get(0).getSignatures().size());

    try {
      // Case: citizen Guize tries to support the project twice, expecting AlreadyVotedException
      supportProjectService.supportProject(Guize.getId(), project1.getId());

      // Fail the test if the above line doesn't throw AlreadyVotedException
      fail("Expected AlreadyVotedException to be thrown");
    } catch (AlreadyVotedException e) {
      // Assert that the size of signatures remains the same
      assertEquals(3, projectRepository.findAll().get(0).getSignatures().size());
    }
  }

  /**
   * This test makes sure the following assumptions are being met: 1) There's only one poll in the
   * repository 2) That the project's name is "Projeto1" 3) That the number of positive votes is 1
   * 4) That the delegate's choice is true/yes
   *
   * @throws ProjectNotOnGoingException
   * @throws AlreadyVotedException
   * @throws ProjectNotFoundException
   * @throws CitizenNotFoundException
   */
  @Test
  @Transactional
  public void createPoll()
      throws CitizenNotFoundException,
          ProjectNotFoundException,
          AlreadyVotedException,
          ProjectNotOnGoingException {

    projectRepository.deleteAll();
    citizenRepository.deleteAll();
    themeRepository.deleteAll();
    pollRepository.deleteAll();

    LocalDateTime endDate = LocalDateTime.of(2025, 5, 13, 15, 56);
    Delegate delegate = new Delegate("Amilcar");
    Theme theme = new Theme("Desporto");
    themeRepository.save(theme);

    Citizen Maria = new Citizen("Maria");

    delegate = citizenRepository.save(delegate);
    Maria = citizenRepository.save(Maria);

    Project project1 = new Project("Projeto1", "Texto projeto1", endDate, delegate, theme);

    project1 = projectRepository.save(project1);

    for (Long i = (long) 50000001; i <= 50009999; i++) {
      project1.addSignature(i);
    }

    project1 = projectRepository.save(project1);

    // Case : Maria is the 100000 citizen to support prject , so it is created a poll where
    // delegate
    // votes automaticly
    supportProjectService.supportProject(Maria.getId(), project1.getId());

    assertEquals(1, pollRepository.findAll().size());

    assertEquals("Projeto1", pollRepository.findAll().get(0).getName());

    assertEquals(1, pollRepository.findAll().get(0).getPositiveVotes());

    assertEquals(true, pollRepository.findAll().get(0).getDelegateVote(delegate));
  }
}
