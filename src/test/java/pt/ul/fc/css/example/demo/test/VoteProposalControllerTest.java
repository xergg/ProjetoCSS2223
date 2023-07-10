package pt.ul.fc.css.example.demo.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
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
import pt.ul.fc.css.example.demo.utils.StatusPoll;

@SpringBootTest
@AutoConfigureMockMvc
/** Class that represents tests for the Poll Repository REST API. */
public class VoteProposalControllerTest {

  @Autowired private PollRepository pollRepository;

  @Autowired private ProjectRepository projectRepository;

  @Autowired private CitizenRepository citizenRepository;

  @Autowired private ThemeRepository themeRepository;

  @Autowired private MockMvc mockMvc;

  @Test
  /**
   * Makes sure that the delegate vote, in case it voted, is correctly assigned.
   *
   * @throws Exception Exception in case of no data received correctly
   */
  public void getProjectDelegateTest() throws Exception {
    projectRepository.deleteAll();
    citizenRepository.deleteAll();
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

    delegate = citizenRepository.save(delegate);
    delegate2 = citizenRepository.save(delegate2);

    citizen = citizenRepository.save(citizen);
    citizen2 = citizenRepository.save(citizen2);
    citizen3 = citizenRepository.save(citizen3);
    citizen4 = citizenRepository.save(citizen4);

    theme = themeRepository.save(theme);

    Project project1 = new Project("Projeto1", "Texto projeto1", endDate, delegate, theme);
    Project projectT = projectRepository.save(project1);
    Poll poll =
        new Poll(projectT.getTitle(), 0, 0, projectT, StatusPoll.ONGOING, projectT.getDate());

    poll = pollRepository.save(poll);

    CitizenThemeDelegate ctd = new CitizenThemeDelegate(theme, delegate, citizen);
    CitizenThemeDelegate ctd2 = new CitizenThemeDelegate(theme, delegate2, citizen4);

    citizen.addThemeDelegate(ctd);
    citizen = citizenRepository.save(citizen);

    citizen4.addThemeDelegate(ctd2);
    citizen4 = citizenRepository.save(citizen4);

    Vote vote = new Vote(delegate.getId(), true);
    poll.voteOn(vote);
    poll.addVoteId(delegate.getId());
    poll.addDelegateVotes(delegate.getId(), true);

    poll = pollRepository.save(poll);

    mockMvc
        .perform(
            get("/api/polls/" + poll.getId())
                .param("citizenId", String.valueOf(citizen.getId()))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").value("true"));
  }

  @Test
  /**
   * Makes sure that there is no delegate.
   *
   * @throws Exception Exception in case of no data received correctly
   */
  public void noDelegateTest() throws Exception {
    projectRepository.deleteAll();
    citizenRepository.deleteAll();
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

    delegate = citizenRepository.save(delegate);
    delegate2 = citizenRepository.save(delegate2);

    citizen = citizenRepository.save(citizen);
    citizen2 = citizenRepository.save(citizen2);
    citizen3 = citizenRepository.save(citizen3);
    citizen4 = citizenRepository.save(citizen4);

    theme = themeRepository.save(theme);

    Project project1 = new Project("Projeto1", "Texto projeto1", endDate, delegate, theme);
    Project projectT = projectRepository.save(project1);
    Poll poll =
        new Poll(projectT.getTitle(), 0, 0, projectT, StatusPoll.ONGOING, projectT.getDate());

    poll = pollRepository.save(poll);

    CitizenThemeDelegate ctd = new CitizenThemeDelegate(theme, delegate, citizen);
    CitizenThemeDelegate ctd2 = new CitizenThemeDelegate(theme, delegate2, citizen4);

    citizen.addThemeDelegate(ctd);
    citizen = citizenRepository.save(citizen);

    citizen4.addThemeDelegate(ctd2);
    citizen4 = citizenRepository.save(citizen4);

    Vote vote = new Vote(delegate.getId(), true);
    poll.voteOn(vote);
    poll.addVoteId(delegate.getId());
    poll.addDelegateVotes(delegate.getId(), true);

    poll = pollRepository.save(poll);

    mockMvc
        .perform(
            get("/api/polls/" + poll.getId())
                .param("citizenId", String.valueOf(citizen2.getId()))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  /**
   * Makes sure that there is a delegate and no delegate vote.
   *
   * @throws Exception Exception in case of no data received correctly
   */
  public void noDelegateVoteTest() throws Exception {
    projectRepository.deleteAll();
    citizenRepository.deleteAll();
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

    delegate = citizenRepository.save(delegate);
    delegate2 = citizenRepository.save(delegate2);

    citizen = citizenRepository.save(citizen);
    citizen2 = citizenRepository.save(citizen2);
    citizen3 = citizenRepository.save(citizen3);
    citizen4 = citizenRepository.save(citizen4);

    theme = themeRepository.save(theme);

    Project project1 = new Project("Projeto1", "Texto projeto1", endDate, delegate, theme);
    Project projectT = projectRepository.save(project1);
    Poll poll =
        new Poll(projectT.getTitle(), 0, 0, projectT, StatusPoll.ONGOING, projectT.getDate());

    poll = pollRepository.save(poll);

    CitizenThemeDelegate ctd = new CitizenThemeDelegate(theme, delegate, citizen);
    CitizenThemeDelegate ctd2 = new CitizenThemeDelegate(theme, delegate2, citizen4);

    citizen.addThemeDelegate(ctd);
    citizen = citizenRepository.save(citizen);

    citizen4.addThemeDelegate(ctd2);
    citizen4 = citizenRepository.save(citizen4);

    mockMvc
        .perform(
            get("/api/polls/" + poll.getId())
                .param("citizenId", String.valueOf(citizen.getId()))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(
            content().string("Nao existe delegado associado ao tema do projeto desta votacao"));
  }

  @Test
  /**
   * Makes sure that the correct amount of votes are received
   *
   * @throws Exception Exception in case of no data received correctly
   */
  public void voteOnPollTest() throws Exception {
    projectRepository.deleteAll();
    citizenRepository.deleteAll();
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

    delegate = citizenRepository.save(delegate);
    delegate2 = citizenRepository.save(delegate2);

    citizen = citizenRepository.save(citizen);
    citizen2 = citizenRepository.save(citizen2);
    citizen3 = citizenRepository.save(citizen3);
    citizen4 = citizenRepository.save(citizen4);

    theme = themeRepository.save(theme);

    Project project1 = new Project("Projeto1", "Texto projeto1", endDate, delegate, theme);
    Project projectT = projectRepository.save(project1);
    Poll poll =
        new Poll(projectT.getTitle(), 0, 0, projectT, StatusPoll.ONGOING, projectT.getDate());

    poll = pollRepository.save(poll);

    CitizenThemeDelegate ctd = new CitizenThemeDelegate(theme, delegate, citizen);
    CitizenThemeDelegate ctd2 = new CitizenThemeDelegate(theme, delegate2, citizen4);

    citizen.addThemeDelegate(ctd);
    citizen = citizenRepository.save(citizen);

    citizen4.addThemeDelegate(ctd2);
    citizen4 = citizenRepository.save(citizen4);

    Vote vote = new Vote(delegate.getId(), true);
    poll.voteOn(vote);
    poll.addVoteId(delegate.getId());
    poll.addDelegateVotes(delegate.getId(), true);

    poll = pollRepository.save(poll);

    mockMvc
        .perform(
            put("/api/polls/" + poll.getId())
                .param("citizenId", String.valueOf(citizen.getId()))
                .param("DelegateVotes", String.valueOf(true))
                .param("choice", String.valueOf(false))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string("Voto recebido!"));

    mockMvc
        .perform(get("/api/polls"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].positiveVotes").value(2));
  }

  @Test
  /**
   * Based on a delegate, makes sure that the vote is based on the user choice
   *
   * @throws Exception Exception in case of no data received correctly
   */
  public void userChoiceTest() throws Exception {
    projectRepository.deleteAll();
    citizenRepository.deleteAll();
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

    delegate = citizenRepository.save(delegate);
    delegate2 = citizenRepository.save(delegate2);

    citizen = citizenRepository.save(citizen);
    citizen2 = citizenRepository.save(citizen2);
    citizen3 = citizenRepository.save(citizen3);
    citizen4 = citizenRepository.save(citizen4);

    theme = themeRepository.save(theme);

    Project project1 = new Project("Projeto1", "Texto projeto1", endDate, delegate, theme);
    Project projectT = projectRepository.save(project1);
    Poll poll =
        new Poll(projectT.getTitle(), 0, 0, projectT, StatusPoll.ONGOING, projectT.getDate());

    poll = pollRepository.save(poll);

    CitizenThemeDelegate ctd = new CitizenThemeDelegate(theme, delegate, citizen);
    CitizenThemeDelegate ctd2 = new CitizenThemeDelegate(theme, delegate2, citizen4);

    citizen.addThemeDelegate(ctd);
    citizen = citizenRepository.save(citizen);

    citizen4.addThemeDelegate(ctd2);
    citizen4 = citizenRepository.save(citizen4);

    Vote vote = new Vote(delegate.getId(), true);
    poll.voteOn(vote);
    poll.addVoteId(delegate.getId());
    poll.addDelegateVotes(delegate.getId(), true);

    poll = pollRepository.save(poll);

    mockMvc
        .perform(
            put("/api/polls/" + poll.getId())
                .param("citizenId", String.valueOf(citizen.getId()))
                .param("DelegateVotes", String.valueOf(false))
                .param("choice", String.valueOf(false))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string("Voto recebido!"));

    mockMvc
        .perform(get("/api/polls"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].positiveVotes").value(1))
        .andExpect(jsonPath("$[0].negativeVotes").value(1));
  }

  @Test
  /**
   * Makes sure that there is no delegate vote
   *
   * @throws Exception Exception in case of no data received correctly
   */
  public void voteOnPollNoDelegateVoteTest() throws Exception {
    projectRepository.deleteAll();
    citizenRepository.deleteAll();
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

    delegate = citizenRepository.save(delegate);
    delegate2 = citizenRepository.save(delegate2);

    citizen = citizenRepository.save(citizen);
    citizen2 = citizenRepository.save(citizen2);
    citizen3 = citizenRepository.save(citizen3);
    citizen4 = citizenRepository.save(citizen4);

    theme = themeRepository.save(theme);

    Project project1 = new Project("Projeto1", "Texto projeto1", endDate, delegate, theme);
    Project projectT = projectRepository.save(project1);
    Poll poll =
        new Poll(projectT.getTitle(), 0, 0, projectT, StatusPoll.ONGOING, projectT.getDate());

    poll = pollRepository.save(poll);

    CitizenThemeDelegate ctd = new CitizenThemeDelegate(theme, delegate, citizen);
    CitizenThemeDelegate ctd2 = new CitizenThemeDelegate(theme, delegate2, citizen4);

    citizen.addThemeDelegate(ctd);
    citizen = citizenRepository.save(citizen);

    citizen4.addThemeDelegate(ctd2);
    citizen4 = citizenRepository.save(citizen4);

    poll = pollRepository.save(poll);

    mockMvc
        .perform(
            put("/api/polls/" + poll.getId())
                .param("citizenId", String.valueOf(citizen.getId()))
                .param("DelegateVotes", String.valueOf(true))
                .param("choice", String.valueOf(false))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Delegado associado não encontrado"));
  }

  @Test
  /**
   * Makes sure that if a user tries to vote twice, it only votes once
   *
   * @throws Exception Exception in case of no data received correctly
   */
  public void voteOnPollTwiceTest() throws Exception {
    projectRepository.deleteAll();
    citizenRepository.deleteAll();
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

    delegate = citizenRepository.save(delegate);
    delegate2 = citizenRepository.save(delegate2);

    citizen = citizenRepository.save(citizen);
    citizen2 = citizenRepository.save(citizen2);
    citizen3 = citizenRepository.save(citizen3);
    citizen4 = citizenRepository.save(citizen4);

    theme = themeRepository.save(theme);

    Project project1 = new Project("Projeto1", "Texto projeto1", endDate, delegate, theme);
    Project projectT = projectRepository.save(project1);
    Poll poll =
        new Poll(projectT.getTitle(), 0, 0, projectT, StatusPoll.ONGOING, projectT.getDate());

    poll = pollRepository.save(poll);

    CitizenThemeDelegate ctd = new CitizenThemeDelegate(theme, delegate, citizen);
    CitizenThemeDelegate ctd2 = new CitizenThemeDelegate(theme, delegate2, citizen4);

    citizen.addThemeDelegate(ctd);
    citizen = citizenRepository.save(citizen);

    citizen4.addThemeDelegate(ctd2);
    citizen4 = citizenRepository.save(citizen4);

    Vote vote = new Vote(delegate.getId(), true);
    poll.voteOn(vote);
    poll.addVoteId(delegate.getId());
    poll.addDelegateVotes(delegate.getId(), true);

    poll = pollRepository.save(poll);

    mockMvc
        .perform(
            put("/api/polls/" + poll.getId())
                .param("citizenId", String.valueOf(citizen.getId()))
                .param("DelegateVotes", String.valueOf(false))
                .param("choice", String.valueOf(false))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string("Voto recebido!"));

    mockMvc
        .perform(
            put("/api/polls/" + poll.getId())
                .param("citizenId", String.valueOf(citizen.getId()))
                .param("DelegateVotes", String.valueOf(false))
                .param("choice", String.valueOf(false))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Já votou neste poll"));
  }

  @Test
  /**
   * Makes sure that if there's no delegate it will be unable to vote
   *
   * @throws Exception Exception in case of no data received correctly
   */
  public void voteOnPollWithoutDelegateTest() throws Exception {
    projectRepository.deleteAll();
    citizenRepository.deleteAll();
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

    delegate = citizenRepository.save(delegate);
    delegate2 = citizenRepository.save(delegate2);

    citizen = citizenRepository.save(citizen);
    citizen2 = citizenRepository.save(citizen2);
    citizen3 = citizenRepository.save(citizen3);
    citizen4 = citizenRepository.save(citizen4);

    theme = themeRepository.save(theme);

    Project project1 = new Project("Projeto1", "Texto projeto1", endDate, delegate, theme);
    Project projectT = projectRepository.save(project1);
    Poll poll =
        new Poll(projectT.getTitle(), 0, 0, projectT, StatusPoll.ONGOING, projectT.getDate());

    poll = pollRepository.save(poll);

    CitizenThemeDelegate ctd = new CitizenThemeDelegate(theme, delegate, citizen);
    CitizenThemeDelegate ctd2 = new CitizenThemeDelegate(theme, delegate2, citizen4);

    citizen.addThemeDelegate(ctd);
    citizen = citizenRepository.save(citizen);

    citizen4.addThemeDelegate(ctd2);
    citizen4 = citizenRepository.save(citizen4);

    poll = pollRepository.save(poll);

    mockMvc
        .perform(
            put("/api/polls/" + poll.getId())
                .param("citizenId", String.valueOf(citizen2.getId()))
                .param("DelegateVotes", String.valueOf(true))
                .param("choice", String.valueOf(false))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Delegado associado não encontrado"));
  }
}
