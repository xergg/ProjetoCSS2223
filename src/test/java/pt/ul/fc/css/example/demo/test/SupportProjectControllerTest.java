package pt.ul.fc.css.example.demo.test;

import static org.hamcrest.Matchers.hasSize;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pt.ul.fc.css.example.demo.business.entities.Delegate;
import pt.ul.fc.css.example.demo.business.entities.Project;
import pt.ul.fc.css.example.demo.business.entities.Theme;
import pt.ul.fc.css.example.demo.business.repositories.CitizenRepository;
import pt.ul.fc.css.example.demo.business.repositories.PollRepository;
import pt.ul.fc.css.example.demo.business.repositories.ProjectRepository;
import pt.ul.fc.css.example.demo.business.repositories.ThemeRepository;

/** Class that represents a test class for the REST API Project Controller. */
@SpringBootTest
@AutoConfigureMockMvc
public class SupportProjectControllerTest {

  @Autowired private PollRepository pollRepository;

  @Autowired private ProjectRepository projectRepository;

  @Autowired private CitizenRepository citizenRepository;

  @Autowired private ThemeRepository themeRepository;

  @Autowired private MockMvc mockMvc;

  /**
   * Test that verifies that the project id is present and that the delegate id is the one that was
   * assigned
   *
   * @throws Exception in case of no data received correctly
   */
  @Test
  public void getOngoingProjects() throws Exception {

    projectRepository.deleteAll();
    citizenRepository.deleteAll();
    themeRepository.deleteAll();
    pollRepository.deleteAll();

    LocalDateTime endDate = LocalDateTime.of(2025, 5, 13, 15, 56);
    Delegate delegate = new Delegate("Amilcar");
    Theme theme = new Theme("Desporto");
    delegate = citizenRepository.save(delegate);
    themeRepository.save(theme);

    Project project1 = new Project("Projeto1", "Texto projeto1", endDate, delegate, theme);
    Project p1 = projectRepository.save(project1);

    Long citizenId = delegate.getId();
    Long id = p1.getId();

    mockMvc
        .perform(
            put("/api/projects/" + id)
                .param("citizenId", String.valueOf(citizenId))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    mockMvc
        .perform(get("/api/projects/" + id))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(p1.getId()))
        .andExpect(jsonPath("$.signatures[0]").value(delegate.getId()));
  }

  /**
   * Test that makes sure that a citizen has voted only once, even though it tried twice.
   *
   * @throws Exception Exception in case of no data received correctly
   */
  @Test
  public void supportTwiceProjects() throws Exception {

    projectRepository.deleteAll();
    citizenRepository.deleteAll();
    themeRepository.deleteAll();
    pollRepository.deleteAll();

    LocalDateTime endDate = LocalDateTime.of(2025, 5, 13, 15, 56);
    Delegate delegate = new Delegate("Amilcar");
    Theme theme = new Theme("Desporto");
    delegate = citizenRepository.save(delegate);
    themeRepository.save(theme);

    Project project1 = new Project("Projeto1", "Texto projeto1", endDate, delegate, theme);
    Project p1 = projectRepository.save(project1);

    Long citizenId = delegate.getId();

    mockMvc
        .perform(
            put("/api/projects/" + p1.getId())
                .param("citizenId", String.valueOf(citizenId))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    mockMvc
        .perform(
            put("/api/projects/" + p1.getId())
                .param("citizenId", String.valueOf(citizenId))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Este cidadao ja apoiou este projeto"));

    mockMvc
        .perform(get("/api/projects/" + p1.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(p1.getId()))
        .andExpect(jsonPath("$.signatures[0]").value(delegate.getId()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.signatures", hasSize(1)));
  }

  @Test
  /**
   * Test that makes sure that after the the project's voting is closed after the expiration date is
   * over.
   *
   * @throws Exception Exception in case of no data received correctly
   */
  public void closeProjectVote() throws Exception {

    projectRepository.deleteAll();
    citizenRepository.deleteAll();
    themeRepository.deleteAll();
    pollRepository.deleteAll();

    LocalDateTime endDate = LocalDateTime.of(2023, 5, 13, 15, 56);
    Delegate delegate = new Delegate("Amilcar");
    Theme theme = new Theme("Desporto");
    delegate = citizenRepository.save(delegate);
    themeRepository.save(theme);

    Project project1 = new Project("Projeto1", "Texto projeto1", endDate, delegate, theme);
    project1.setOngoing(false);
    Project p1 = projectRepository.save(project1);

    Long citizenId = delegate.getId();

    mockMvc
        .perform(
            put("/api/projects/" + p1.getId())
                .param("citizenId", String.valueOf(citizenId))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("O projeto ja nao se encontra disponivel para apoiar"));
  }

  @Test
  /**
   * Test that makes sure that after 9999 creates a new poll
   *
   * @throws Exception Exception in case of no data received correctly
   */
  public void createNewPoll() throws Exception {

    projectRepository.deleteAll();
    citizenRepository.deleteAll();
    themeRepository.deleteAll();
    pollRepository.deleteAll();

    LocalDateTime endDate = LocalDateTime.of(2025, 5, 13, 15, 56);
    Delegate delegate = new Delegate("Amilcar");
    Theme theme = new Theme("Desporto");
    delegate = citizenRepository.save(delegate);
    themeRepository.save(theme);

    Project project1 = new Project("Projeto1", "Texto projeto1", endDate, delegate, theme);
    project1.setOngoing(false);
    Project p1 = projectRepository.save(project1);

    Long citizenId = delegate.getId();

    for (Long i = (long) 50000001; i <= 50009999; i++) {
      p1.addSignature(i);
    }

    p1 = projectRepository.save(p1);

    mockMvc
        .perform(
            put("/api/projects/" + p1.getId())
                .param("citizenId", String.valueOf(citizenId))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    mockMvc
        .perform(get("/api/polls").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").value(hasSize(1)));
  }
}
