package pt.ul.fc.css.example.demo.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pt.ul.fc.css.example.demo.business.dtos.PollDTO;
import pt.ul.fc.css.example.demo.business.entities.Delegate;
import pt.ul.fc.css.example.demo.business.entities.Poll;
import pt.ul.fc.css.example.demo.business.entities.Project;
import pt.ul.fc.css.example.demo.business.entities.Theme;
import pt.ul.fc.css.example.demo.business.exceptions.PollNotFoundException;
import pt.ul.fc.css.example.demo.business.repositories.CitizenRepository;
import pt.ul.fc.css.example.demo.business.repositories.PollRepository;
import pt.ul.fc.css.example.demo.business.repositories.ProjectRepository;
import pt.ul.fc.css.example.demo.business.repositories.ThemeRepository;
import pt.ul.fc.css.example.demo.business.services.OngoingPollsService;
import pt.ul.fc.css.example.demo.utils.StatusPoll;

/** This class represents a Test class to the Ongoing Polls use case's service. */
@SpringBootTest
public class OngoingPollsServiceTest {

  @Autowired private OngoingPollsService ongoingPollsService;

  @Autowired private ProjectRepository projectRepository;

  @Autowired private CitizenRepository delegateRepository;

  @Autowired private ThemeRepository themeRepository;

  @Autowired private PollRepository pollRepository;

  /**
   * This test makes sure that all ongoing polls in the poll repository are the same as the ones on
   * the response entity
   *
   * @throws PollNotFoundException
   */
  @Test
  public void testOngoingPolls() throws PollNotFoundException {

    projectRepository.deleteAll();
    delegateRepository.deleteAll();
    themeRepository.deleteAll();
    pollRepository.deleteAll();

    LocalDateTime endDate = LocalDateTime.of(2023, 5, 13, 15, 56);
    Delegate delegate = new Delegate("Amilcar");
    Theme theme = new Theme("Desporto");
    delegateRepository.save(delegate);
    themeRepository.save(theme);

    Project project1 = new Project("Projeto1", "Texto projeto1", endDate, delegate, theme);
    projectRepository.save(project1);
    Project projectT = projectRepository.findAll().get(0);
    Poll poll =
        new Poll(projectT.getTitle(), 0, 0, projectT, StatusPoll.ONGOING, projectT.getDate());

    pollRepository.save(poll);

    // Case : citizen tries to get all on going polls, only existing one , should
    // work
    List<PollDTO> pollList = ongoingPollsService.getOngoingPolls();

    assertEquals(pollRepository.findAll().size(), pollList.size());
  }

  /**
   * This test makes sure there's at least on expired poll, in this case there's been a poll that's
   * been APPROVED
   *
   * @throws PollNotFoundException
   */
  @Test
  public void oneExpiredPoll() throws PollNotFoundException {

    projectRepository.deleteAll();
    delegateRepository.deleteAll();
    themeRepository.deleteAll();
    pollRepository.deleteAll();

    LocalDateTime endDate = LocalDateTime.of(2025, 5, 13, 15, 56);
    Delegate delegate = new Delegate("Amilcar");
    Theme theme = new Theme("Desporto");
    delegateRepository.save(delegate);
    themeRepository.save(theme);

    Project project1 = new Project("Projeto1", "Texto projeto1", endDate, delegate, theme);
    Project project2 = new Project("Projeto2", "Texto projeto2", endDate, delegate, theme);
    projectRepository.save(project1);
    projectRepository.save(project2);
    Poll poll =
        new Poll(project1.getTitle(), 0, 0, project1, StatusPoll.ONGOING, project1.getDate());
    Poll poll2 =
        new Poll(project2.getTitle(), 0, 0, project2, StatusPoll.APPROVED, project2.getDate());

    pollRepository.save(poll);
    pollRepository.save(poll2);

    // Case tries to get on going polls , only existing one (we dont take account of Dates in
    // this test, theres already a use case for that)
    List<PollDTO> pollList = ongoingPollsService.getOngoingPolls();

    assertEquals(1, pollList.size());
  }
}
