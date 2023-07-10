package pt.ul.fc.css.example.demo.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pt.ul.fc.css.example.demo.business.entities.CitizenThemeDelegate;
import pt.ul.fc.css.example.demo.business.entities.Delegate;
import pt.ul.fc.css.example.demo.business.entities.Poll;
import pt.ul.fc.css.example.demo.business.entities.Project;
import pt.ul.fc.css.example.demo.business.entities.Theme;
import pt.ul.fc.css.example.demo.business.entities.Vote;
import pt.ul.fc.css.example.demo.business.entities.Voter;
import pt.ul.fc.css.example.demo.business.repositories.CitizenRepository;
import pt.ul.fc.css.example.demo.business.repositories.PollRepository;
import pt.ul.fc.css.example.demo.business.repositories.ProjectRepository;
import pt.ul.fc.css.example.demo.business.repositories.ThemeRepository;
import pt.ul.fc.css.example.demo.business.services.ClosePollService;
import pt.ul.fc.css.example.demo.utils.StatusPoll;

/** This class represents a Test class to the Close Poll use case's service. */
@SpringBootTest
public class ClosePollServiceTest {

  @Autowired private ClosePollService closePollService;

  @Autowired private ProjectRepository projectRepository;

  @Autowired private CitizenRepository citizenRepository;

  @Autowired private ThemeRepository themeRepository;

  @Autowired private PollRepository pollRepository;

  /**
   * This test verifies the following assertions:
   *
   * <p>1) Following a set number of votes, that it matches the number 3 2) Following a set number
   * of votes, that it matches the number 2 3) Making sure that after having more positive than
   * negative votes, it ends up as APPROVED 4) Making sure that after having more negative than
   * positive votes, it ends up as DENIED
   */
  @Test
  public void closePolls() {

    projectRepository.deleteAll();
    citizenRepository.deleteAll();
    themeRepository.deleteAll();
    pollRepository.deleteAll();

    Delegate delegate = new Delegate("Amilcar");
    Delegate delegate2 = new Delegate("Patricio");
    Delegate delegate3 = new Delegate("Clarinda");
    Voter citizen = new Voter("Fabricio");
    Voter citizen2 = new Voter("Horacio");
    Voter citizen3 = new Voter("Feliciano");
    Voter citizen4 = new Voter("Armelindo");
    Theme theme = new Theme("Desporto");
    Theme theme2 = new Theme("Saude");
    LocalDateTime endDate = LocalDateTime.of(2017, 5, 13, 15, 56);
    delegate = citizenRepository.save(delegate);
    delegate2 = citizenRepository.save(delegate2);
    delegate3 = citizenRepository.save(delegate3);

    citizen = citizenRepository.save(citizen);
    citizen2 = citizenRepository.save(citizen2);
    citizen3 = citizenRepository.save(citizen3);
    citizen4 = citizenRepository.save(citizen4);

    theme = themeRepository.save(theme);
    theme2 = themeRepository.save(theme2);

    theme.setParent(theme2);
    theme2.addSubTheme(theme);

    theme = themeRepository.save(theme);
    theme2 = themeRepository.save(theme2);

    Project project1 = new Project("Projeto1", "Texto projeto1", endDate, delegate, theme);
    Project project2 = new Project("Projeto2", "Texto projeto2", endDate, delegate, theme);
    project1 = projectRepository.save(project1);
    project2 = projectRepository.save(project2);

    Poll poll =
        new Poll(project1.getTitle(), 0, 0, project1, StatusPoll.ONGOING, project1.getDate());
    Poll poll2 =
        new Poll(project2.getTitle(), 0, 0, project2, StatusPoll.ONGOING, project2.getDate());
    poll = pollRepository.save(poll);
    poll2 = pollRepository.save(poll2);

    CitizenThemeDelegate ctd = new CitizenThemeDelegate(theme, delegate, citizen);
    CitizenThemeDelegate ctd2 = new CitizenThemeDelegate(theme, delegate2, citizen2);
    CitizenThemeDelegate ctd3 = new CitizenThemeDelegate(theme, delegate, citizen3);
    CitizenThemeDelegate ctd4 = new CitizenThemeDelegate(theme2, delegate3, citizen4);

    // citizen chooses delegate for theme which will vote positive
    citizen.addThemeDelegate(ctd);
    citizen = citizenRepository.save(citizen);

    // citizen2 chooses delegate2 for theme which will vote negative
    citizen2.addThemeDelegate(ctd2);
    citizen2 = citizenRepository.save(citizen2);

    // citizen3 chooses delegate for theme
    citizen3.addThemeDelegate(ctd3);
    citizen3 = citizenRepository.save(citizen3);

    // citizen4 chooses delegate for theme
    citizen4.addThemeDelegate(ctd4);
    citizen4 = citizenRepository.save(citizen4);

    // delegate votes on poll
    Vote vote = new Vote(delegate.getId(), true);
    poll.voteOn(vote);
    poll.addVoteId(delegate.getId());
    poll.addDelegateVotes(delegate.getId(), true);

    // delegate2 votes on poll
    Vote vote2 = new Vote(delegate2.getId(), false);
    poll.voteOn(vote2);
    poll.addVoteId(delegate2.getId());
    poll.addDelegateVotes(delegate2.getId(), false);

    // delegate3 votes on poll
    Vote vote3 = new Vote(delegate3.getId(), true);
    poll.voteOn(vote3);
    poll.addVoteId(delegate3.getId());
    poll.addDelegateVotes(delegate3.getId(), true);

    poll = pollRepository.save(poll);

    // dated polls should close
    closePollService.closePolls();

    // votes for delegate , delegate3 , citizen1(delegate) , citizen3(delegate) and
    // citizen4(delegate3) , this last one being trough hierarchy
    assertEquals(5, pollRepository.findAll().get(0).getPositiveVotes());

    assertEquals(2, pollRepository.findAll().get(0).getNegativeVotes());

    assertEquals(StatusPoll.APPROVED, pollRepository.findAll().get(0).getStatusPoll());

    assertEquals(StatusPoll.DENIED, pollRepository.findAll().get(1).getStatusPoll());
  }
}
