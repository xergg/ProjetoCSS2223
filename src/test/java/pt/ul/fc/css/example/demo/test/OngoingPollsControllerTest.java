package pt.ul.fc.css.example.demo.test;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pt.ul.fc.css.example.demo.business.entities.Delegate;
import pt.ul.fc.css.example.demo.business.entities.Poll;
import pt.ul.fc.css.example.demo.business.entities.Project;
import pt.ul.fc.css.example.demo.business.entities.Theme;
import pt.ul.fc.css.example.demo.business.repositories.CitizenRepository;
import pt.ul.fc.css.example.demo.business.repositories.PollRepository;
import pt.ul.fc.css.example.demo.business.repositories.ProjectRepository;
import pt.ul.fc.css.example.demo.business.repositories.ThemeRepository;
import pt.ul.fc.css.example.demo.utils.StatusPoll;

@SpringBootTest
@AutoConfigureMockMvc
/** Class that represents tests for a REST API Poll Controller. */
public class OngoingPollsControllerTest {

  @Autowired private PollRepository pollRepository;

  @Autowired private ProjectRepository projectRepository;

  @Autowired private CitizenRepository citizenRepository;

  @Autowired private ThemeRepository themeRepository;

  @Autowired private MockMvc mockMvc;

  @Test
  /**
   * Test that verifies if all the ongoing polls are accounted for
   *
   * @throws Exception Exception in case of no data received correctly
   */
  public void getOngoingPolls() throws Exception {

    projectRepository.deleteAll();
    citizenRepository.deleteAll();
    themeRepository.deleteAll();
    pollRepository.deleteAll();

    LocalDateTime endDate = LocalDateTime.of(2023, 5, 13, 15, 56);
    Delegate delegate = new Delegate("Amilcar");
    Theme theme = new Theme("Desporto");
    citizenRepository.save(delegate);
    themeRepository.save(theme);

    Project project1 = new Project("Projeto1", "Texto projeto1", endDate, delegate, theme);
    Project projectT = projectRepository.save(project1);

    Project project2 = new Project("p2", "p2", endDate, delegate, theme);
    Project p2 = projectRepository.save(project2);

    Poll poll =
        new Poll(projectT.getTitle(), 0, 0, projectT, StatusPoll.ONGOING, projectT.getDate());

    Poll poll2 = new Poll(p2.getTitle(), 0, 0, p2, StatusPoll.ONGOING, endDate);

    Poll pt1 = pollRepository.save(poll);
    Poll pt2 = pollRepository.save(poll2);

    mockMvc
        .perform(get("/api/polls"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].id").value(pt1.getId()))
        .andExpect(jsonPath("$[0].name").value("Projeto1"))
        .andExpect(jsonPath("$[0].negativeVotes").value(0))
        .andExpect(jsonPath("$[0].positiveVotes").value(0))
        .andExpect(jsonPath("$[0].statusPoll").value("ONGOING"))
        .andExpect(jsonPath("$[0].endDate").exists())
        .andExpect(jsonPath("$[0].idList").isArray())
        .andExpect(jsonPath("$[0].delegateVotes").doesNotExist())
        .andExpect(jsonPath("$[1].id").value(pt2.getId()))
        .andExpect(jsonPath("$[1].name").value("p2"))
        .andExpect(jsonPath("$[1].negativeVotes").value(0))
        .andExpect(jsonPath("$[1].positiveVotes").value(0))
        .andExpect(jsonPath("$[1].statusPoll").value("ONGOING"))
        .andExpect(jsonPath("$[1].endDate").exists())
        .andExpect(jsonPath("$[1].idList").isArray())
        .andExpect(jsonPath("$[1].delegateVotes").doesNotExist())
        .andExpect(jsonPath("$").value(hasSize(2)));
  }

  @Test
  /**
   * Verifies that the number of ongoing polls are the same as expected
   *
   * @throws Exception Exception in case of no data received correctly
   */
  public void checkNumberOnGoingPolls() throws Exception {

    projectRepository.deleteAll();
    citizenRepository.deleteAll();
    themeRepository.deleteAll();
    pollRepository.deleteAll();

    LocalDateTime endDate = LocalDateTime.of(2023, 5, 13, 15, 56);
    Delegate delegate = new Delegate("Amilcar");
    Theme theme = new Theme("Desporto");
    citizenRepository.save(delegate);
    themeRepository.save(theme);

    Project project1 = new Project("Projeto1", "Texto projeto1", endDate, delegate, theme);
    Project projectT = projectRepository.save(project1);

    Poll poll =
        new Poll(projectT.getTitle(), 0, 0, projectT, StatusPoll.ONGOING, projectT.getDate());

    Poll pt1 = pollRepository.save(poll);

    mockMvc
        .perform(get("/api/polls"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].id").value(pt1.getId()))
        .andExpect(jsonPath("$[0].name").value("Projeto1"))
        .andExpect(jsonPath("$[0].negativeVotes").value(0))
        .andExpect(jsonPath("$[0].positiveVotes").value(0))
        .andExpect(jsonPath("$[0].statusPoll").value("ONGOING"))
        .andExpect(jsonPath("$[0].endDate").exists())
        .andExpect(jsonPath("$[0].idList").isArray())
        .andExpect(jsonPath("$[0].delegateVotes").doesNotExist())
        .andExpect(jsonPath("$").value(hasSize(1)));
  }

  @Test
  /**
   * Verifies that there are no polls available.
   *
   * @throws Exception Exception in case of no data received correctly
   */
  public void noPollosWhatsoever() throws Exception {

    projectRepository.deleteAll();
    citizenRepository.deleteAll();
    themeRepository.deleteAll();
    pollRepository.deleteAll();

    mockMvc.perform(get("/api/polls")).andExpect(status().isNotFound());
  }
}
