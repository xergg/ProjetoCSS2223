package pt.ul.fc.css.example.demo.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pt.ul.fc.css.example.demo.business.dtos.PollDTO;
import pt.ul.fc.css.example.demo.business.entities.CitizenThemeDelegate;
import pt.ul.fc.css.example.demo.business.entities.Delegate;
import pt.ul.fc.css.example.demo.business.entities.Poll;
import pt.ul.fc.css.example.demo.business.entities.Project;
import pt.ul.fc.css.example.demo.business.entities.Theme;
import pt.ul.fc.css.example.demo.business.entities.Vote;
import pt.ul.fc.css.example.demo.business.entities.Voter;
import pt.ul.fc.css.example.demo.business.exceptions.AlreadyVotedException;
import pt.ul.fc.css.example.demo.business.exceptions.AssignedDelegateNullVoteException;
import pt.ul.fc.css.example.demo.business.exceptions.CitizenNotFoundException;
import pt.ul.fc.css.example.demo.business.exceptions.DelegateNotFoundException;
import pt.ul.fc.css.example.demo.business.exceptions.HasAlreadyVotedException;
import pt.ul.fc.css.example.demo.business.exceptions.NoDelegateAssignedException;
import pt.ul.fc.css.example.demo.business.exceptions.PollNotFoundException;
import pt.ul.fc.css.example.demo.business.exceptions.ProjectNotFoundException;
import pt.ul.fc.css.example.demo.business.exceptions.ProjectNotOnGoingException;
import pt.ul.fc.css.example.demo.business.exceptions.SameIdException;
import pt.ul.fc.css.example.demo.business.exceptions.ThemeNotFoundException;
import pt.ul.fc.css.example.demo.business.repositories.CitizenRepository;
import pt.ul.fc.css.example.demo.business.repositories.PollRepository;
import pt.ul.fc.css.example.demo.business.repositories.ProjectRepository;
import pt.ul.fc.css.example.demo.business.repositories.ThemeRepository;
import pt.ul.fc.css.example.demo.business.services.ChooseDelegateService;
import pt.ul.fc.css.example.demo.business.services.SupportProjectService;
import pt.ul.fc.css.example.demo.business.services.VoteProposalService;
import pt.ul.fc.css.example.demo.utils.StatusPoll;

/** This class represents a Test class to the Vote Proposal use case's service. */
@SpringBootTest
public class VoteProposalServiceTest {

  @Autowired private VoteProposalService voteProposalController;

  @Autowired private SupportProjectService supportProjectService;

  @Autowired private ProjectRepository projectRepository;

  @Autowired private CitizenRepository delegateRepository;

  @Autowired private ThemeRepository themeRepository;

  @Autowired private PollRepository pollRepository;

  @Autowired private ChooseDelegateService chooseDelegateService;

  /**
   * This test makes sure that the following assertions are being met: 1) That the local list poll
   * size is the same as the one in the repository 2) That the choice made from the delegate is
   * being correctly transcribed 3) That the number of positive votes is 2 4) That the number of
   * positive votes is 3 5) That the number of negative votes is 1 6) That the number of negative
   * votes is still 1, even after the same citizen tried to vote twice
   *
   * @throws PollNotFoundException
   * @throws HasAlreadyVotedException
   * @throws AssignedDelegateNullVoteException
   * @throws ProjectNotOnGoingException
   * @throws AlreadyVotedException
   * @throws ProjectNotFoundException
   * @throws CitizenNotFoundException
   * @throws SameIdException
   * @throws DelegateNotFoundException
   * @throws NoDelegateAssignedException
   * @throws ThemeNotFoundException
   */
  @Test
  @Transactional
  public void testVoteProposal()
      throws AssignedDelegateNullVoteException,
          HasAlreadyVotedException,
          DelegateNotFoundException,
          PollNotFoundException,
          CitizenNotFoundException,
          NoDelegateAssignedException {

    projectRepository.deleteAll();
    delegateRepository.deleteAll();
    themeRepository.deleteAll();
    pollRepository.deleteAll();

    Delegate delegate = new Delegate("Amilcar");
    Delegate delegate2 = new Delegate("Firmino");
    Voter citizen = new Voter("Fabricio");
    Voter citizen2 = new Voter("Horacio");
    Voter citizen3 = new Voter("Feliciano");
    Voter citizen4 = new Voter("Esmeraldo");
    Theme theme = new Theme("Desporto");

    LocalDateTime endDate = LocalDateTime.of(2023, 5, 13, 15, 56);

    delegate = delegateRepository.save(delegate);
    delegate2 = delegateRepository.save(delegate2);

    citizen = delegateRepository.save(citizen);
    citizen2 = delegateRepository.save(citizen2);
    citizen3 = delegateRepository.save(citizen3);
    citizen4 = delegateRepository.save(citizen4);

    theme = themeRepository.save(theme);

    Project project1 = new Project("Projeto1", "Texto projeto1", endDate, delegate, theme);
    projectRepository.save(project1);
    Project projectT = projectRepository.findAll().get(0);
    Poll poll =
        new Poll(projectT.getTitle(), 0, 0, projectT, StatusPoll.ONGOING, projectT.getDate());

    poll = pollRepository.save(poll);

    List<PollDTO> listPolls = voteProposalController.getPolls();

    assertEquals(pollRepository.findAll().size(), listPolls.size());

    CitizenThemeDelegate ctd = new CitizenThemeDelegate(theme, delegate, citizen);
    CitizenThemeDelegate ctd2 = new CitizenThemeDelegate(theme, delegate2, citizen4);

    citizen.addThemeDelegate(ctd);
    citizen = delegateRepository.save(citizen);

    citizen4.addThemeDelegate(ctd2);
    citizen4 = delegateRepository.save(citizen4);

    Vote vote = new Vote(delegate.getId(), true);
    poll.voteOn(vote);
    poll.addVoteId(delegate.getId());
    poll.addDelegateVotes(delegate.getId(), true);

    poll = pollRepository.save(poll);

    // Case : Citizen Citizen checks poll and tries to see what his delegate has voted
    Boolean selectedPoll = voteProposalController.pickPolls(poll.getId(), citizen.getId());

    assertEquals(
        "The delegate's choice was Yes",
        "The delegate's choice was " + (selectedPoll ? "Yes" : "No"));

    // Case : citizen decides to take the assigned delegate vote , should change positive
    // votes
    voteProposalController.voterChoice(citizen.getId(), poll.getId(), true, null);

    assertEquals(2, poll.getPositiveVotes());

    // Case : citizen2 checks poll and tries to see what his delegate voted, but theres no
    // delegate
    try {
      Boolean noDelegate = voteProposalController.pickPolls(poll.getId(), citizen2.getId());

      fail("Expected NoDelegateAssignedException to be thrown");

    } catch (NoDelegateAssignedException e) {
      assertEquals(false, false);
    }
    // Case : citizen2 decides to vote by himself , should change positive votes
    voteProposalController.voterChoice(citizen2.getId(), poll.getId(), false, true);

    assertEquals(3, poll.getPositiveVotes());

    // Case : citizen3 decides to vote by himself , should change negative votes
    voteProposalController.voterChoice(citizen3.getId(), poll.getId(), false, false);

    assertEquals(1, poll.getNegativeVotes());

    // Case : citizen3 tries to vote twice on same poll , shouldnt change ammount of votes

    try {
      voteProposalController.voterChoice(citizen3.getId(), poll.getId(), false, false);

      fail("Expected HasAlreadyVotedException to be thrown");

    } catch (HasAlreadyVotedException e) {
      assertEquals(1, pollRepository.findAll().get(0).getNegativeVotes());
    }
    // Case : Citizen4 checks poll and tries to see what his delegate has voted , but delegate
    // hasnt
    // voted;
    try {
      Boolean delegateDidntVote = voteProposalController.pickPolls(poll.getId(), citizen4.getId());

      fail("Expected NoDelegateAssignedException to be thrown");

    } catch (NoDelegateAssignedException e) {
      assertEquals(false, false);
    }
    // Case : Citizen4 has assigned a delegate for the poll theme but delegate hasnt voted yet

    try {
      voteProposalController.voterChoice(citizen4.getId(), poll.getId(), true, null);

      fail("Expected DelegateNotFoundException to be thrown");

    } catch (DelegateNotFoundException e) {
      assertEquals(1, pollRepository.findAll().get(0).getNegativeVotes());

      assertEquals(3, pollRepository.findAll().get(0).getPositiveVotes());
    }
  }

  @Test
  @Transactional
  public void checkThemeHierarchy()
      throws PollNotFoundException,
          NoDelegateAssignedException,
          CitizenNotFoundException,
          AssignedDelegateNullVoteException,
          HasAlreadyVotedException,
          DelegateNotFoundException {

    projectRepository.deleteAll();
    delegateRepository.deleteAll();
    themeRepository.deleteAll();
    pollRepository.deleteAll();

    Delegate delegate = new Delegate("Amilcar");
    Delegate delegate2 = new Delegate("Firmino");
    Voter citizen = new Voter("Fabricio");
    Voter citizen2 = new Voter("Horacio");
    Voter citizen3 = new Voter("Feliciano");
    Theme theme = new Theme("Futebol");
    Theme theme2 = new Theme("Desporto"); // Father of theme
    Theme theme3 = new Theme("Saude"); // Father of theme2

    LocalDateTime endDate = LocalDateTime.of(2023, 5, 13, 15, 56);
    delegate = delegateRepository.save(delegate);
    delegate2 = delegateRepository.save(delegate2);
    citizen = delegateRepository.save(citizen);
    citizen2 = delegateRepository.save(citizen2);
    citizen3 = delegateRepository.save(citizen3);

    theme = themeRepository.save(theme);
    theme2 = themeRepository.save(theme2);
    theme3 = themeRepository.save(theme3);

    theme3.addSubTheme(theme2);
    theme2.addSubTheme(theme);
    theme.setParent(theme2);
    theme2.setParent(theme3);

    theme = themeRepository.save(theme);
    theme2 = themeRepository.save(theme2);
    theme3 = themeRepository.save(theme3);

    Project project1 = new Project("Projeto1", "Texto projeto1", endDate, delegate, theme);
    projectRepository.save(project1);
    Project projectT = projectRepository.findAll().get(0);
    Poll poll =
        new Poll(projectT.getTitle(), 0, 0, projectT, StatusPoll.ONGOING, projectT.getDate());

    poll = pollRepository.save(poll);

    List<PollDTO> listPolls = voteProposalController.getPolls();

    assertEquals(pollRepository.findAll().size(), listPolls.size());

    CitizenThemeDelegate ctd = new CitizenThemeDelegate(theme2, delegate, citizen);
    CitizenThemeDelegate ctd2 = new CitizenThemeDelegate(theme3, delegate2, citizen2);

    citizen.addThemeDelegate(ctd);
    citizen = delegateRepository.save(citizen);

    citizen2.addThemeDelegate(ctd2);
    citizen2 = delegateRepository.save(citizen2);

    Vote vote = new Vote(delegate.getId(), true);
    poll.voteOn(vote);
    poll.addVoteId(delegate.getId());
    poll.addDelegateVotes(delegate.getId(), true);

    Vote vote2 = new Vote(delegate2.getId(), false);
    poll.voteOn(vote2);
    poll.addVoteId(delegate2.getId());
    poll.addDelegateVotes(delegate2.getId(), false);

    poll = pollRepository.save(poll);

    // Case : citizen checks poll and gets the delegate vote for a a fathertheme
    // theme2(Desporto)
    // of
    // theme(futebol)
    //       since no delegate of theme1 has voted on this poll
    Boolean selectedPoll = voteProposalController.pickPolls(poll.getId(), citizen.getId());

    assertEquals(
        "The delegate's choice was Yes",
        "The delegate's choice was " + (selectedPoll ? "Yes" : "No"));

    // Case : citizen takes the vote of the Delegate , should add votes (2 , 1 from delegate
    // and
    // 1
    // from citizen)
    voteProposalController.voterChoice(citizen.getId(), poll.getId(), true, null);

    assertEquals(2, poll.getPositiveVotes());

    // Case : citizen2 takes vote from delegate2 , wich is his delegate for theme3 wich is
    // father
    // of
    // theme2, and grandfather of theme,
    //       therefore should add a negative vote, having 2 in the end
    voteProposalController.voterChoice(citizen2.getId(), poll.getId(), true, null);

    assertEquals(2, poll.getNegativeVotes());
  }

  @Test
  @Transactional
  public void pollos()
      throws CitizenNotFoundException,
          ProjectNotFoundException,
          AlreadyVotedException,
          ProjectNotOnGoingException,
          ThemeNotFoundException,
          DelegateNotFoundException,
          SameIdException {

    projectRepository.deleteAll();
    delegateRepository.deleteAll();
    themeRepository.deleteAll();
    pollRepository.deleteAll();

    Delegate Jack = delegateRepository.save(new Delegate("Jack"));
    delegateRepository.save(new Delegate("Bauer"));
    delegateRepository.save(new Delegate("Kim"));
    delegateRepository.save(new Voter("David"));
    delegateRepository.save(new Voter("Michelle"));
    delegateRepository.save(new Voter("beep"));

    LocalDateTime endDate = LocalDateTime.of(2025, 5, 13, 15, 56);
    Delegate delegate = new Delegate("Amilcar");
    Theme theme = new Theme("Desporto");
    Theme theme2 = new Theme("Saude");
    delegateRepository.save(delegate);
    themeRepository.save(theme);
    themeRepository.save(theme2);

    Project project1 = new Project("Projeto1", "Texto projeto1", endDate, delegate, theme);
    Project project2 = new Project("Projeto2", "Texto projeto1", endDate, delegate, theme);
    projectRepository.save(project1);
    projectRepository.save(project2);

    for (Long i = (long) 50000001; i <= 50009999; i++) {
      project1.addSignature(i);
    }

    project1 = projectRepository.save(project1);

    supportProjectService.supportProject(Jack.getId(), project1.getId());

    chooseDelegateService.chooseDelegate(Jack.getId(), delegate.getId(), theme.getId());
  }
}
