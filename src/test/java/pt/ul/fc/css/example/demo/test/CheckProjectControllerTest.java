package pt.ul.fc.css.example.demo.test;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import pt.ul.fc.css.example.demo.business.entities.Delegate;
import pt.ul.fc.css.example.demo.business.entities.Project;
import pt.ul.fc.css.example.demo.business.entities.Theme;
import pt.ul.fc.css.example.demo.business.repositories.CitizenRepository;
import pt.ul.fc.css.example.demo.business.repositories.PollRepository;
import pt.ul.fc.css.example.demo.business.repositories.ProjectRepository;
import pt.ul.fc.css.example.demo.business.repositories.ThemeRepository;
import pt.ul.fc.css.example.demo.presentation.restcontrollers.RestProjectController;

@SpringBootTest
@AutoConfigureMockMvc
public class CheckProjectControllerTest {

  @Autowired private RestProjectController restProjectController;

  @Autowired private PollRepository pollRepository;

  @Autowired private ProjectRepository projectRepository;

  @Autowired private CitizenRepository citizenRepository;

  @Autowired private ThemeRepository themeRepository;

  @Autowired private MockMvc mockMvc;

  @Test
  public void getOngoingProjects() throws Exception {

    projectRepository.deleteAll();
    citizenRepository.deleteAll();
    themeRepository.deleteAll();
    pollRepository.deleteAll();

    LocalDateTime endDate = LocalDateTime.of(2025, 5, 13, 15, 56);
    Delegate delegate = new Delegate("Amilcar");
    Theme theme = new Theme("Desporto");
    citizenRepository.save(delegate);
    themeRepository.save(theme);

    Project project1 = new Project("Projeto1", "Texto projeto1", endDate, delegate, theme);
    Project p1 = projectRepository.save(project1);

    Project project2 = new Project("p2", "p2", endDate, delegate, theme);
    Project p2 = projectRepository.save(project2);

    mockMvc
        .perform(get("/api/projects"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].title").value("Projeto1"))
        .andExpect(jsonPath("$[0].delegate.name").value("Amilcar"))
        .andExpect(jsonPath("$[0].d").exists())
        .andExpect(jsonPath("$[0].theme.name").value("Desporto"))
        .andExpect(jsonPath("$[1].title").value("p2"))
        .andExpect(jsonPath("$[1].delegate.name").value("Amilcar"))
        .andExpect(jsonPath("$[1].d").exists())
        .andExpect(jsonPath("$[1].theme.name").value("Desporto"))
        .andExpect(jsonPath("$").value(hasSize(2)));
  }

  @Test
  public void checkOneOngoingProject() throws Exception {

    projectRepository.deleteAll();
    citizenRepository.deleteAll();
    themeRepository.deleteAll();
    pollRepository.deleteAll();

    LocalDateTime endDate = LocalDateTime.of(2025, 5, 13, 15, 56);
    Delegate delegate = new Delegate("Amilcar");
    Theme theme = new Theme("Desporto");
    citizenRepository.save(delegate);
    themeRepository.save(theme);

    Project project1 = new Project("Projeto1", "Texto projeto1", endDate, delegate, theme);
    Project p1 = projectRepository.save(project1);

    Project project2 = new Project("p2", "p2", endDate, delegate, theme);

    mockMvc
        .perform(get("/api/projects"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].title").value("Projeto1"))
        .andExpect(jsonPath("$[0].delegate.name").value("Amilcar"))
        .andExpect(jsonPath("$[0].d").exists())
        .andExpect(jsonPath("$[0].theme.name").value("Desporto"))
        .andExpect(jsonPath("$").value(hasSize(1)));
  }

  @Test
  public void noOngoingProjects() throws Exception {

    projectRepository.deleteAll();
    citizenRepository.deleteAll();
    themeRepository.deleteAll();
    pollRepository.deleteAll();

    mockMvc.perform(get("/api/projects")).andExpect(status().isNotFound());
  }

  @Test
  public void stopBeingOngoingProject() throws Exception {

    projectRepository.deleteAll();
    citizenRepository.deleteAll();
    themeRepository.deleteAll();
    pollRepository.deleteAll();

    LocalDateTime endDate = LocalDateTime.of(2025, 5, 13, 15, 56);
    Delegate delegate = new Delegate("Amilcar");
    Theme theme = new Theme("Desporto");
    citizenRepository.save(delegate);
    themeRepository.save(theme);

    Project project1 = new Project("Projeto1", "Texto projeto1", endDate, delegate, theme);
    Project p1 = projectRepository.save(project1);

    Project project2 = new Project("p2", "p2", endDate, delegate, theme);
    project2.setOngoing(false);
    Project p2 = projectRepository.save(project2);

    mockMvc
        .perform(get("/api/projects"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$").value(hasSize(1)));
  }

  @Test
  public void stopBeingOngoingMidwayProject() throws Exception {

    projectRepository.deleteAll();
    citizenRepository.deleteAll();
    themeRepository.deleteAll();
    pollRepository.deleteAll();

    LocalDateTime endDate = LocalDateTime.of(2025, 5, 13, 15, 56);
    Delegate delegate = new Delegate("Amilcar");
    Theme theme = new Theme("Desporto");
    citizenRepository.save(delegate);
    themeRepository.save(theme);

    Project project1 = new Project("Projeto1", "Texto projeto1", endDate, delegate, theme);
    Project p1 = projectRepository.save(project1);

    Project project2 = new Project("p2", "p2", endDate, delegate, theme);
    Project p2 = projectRepository.save(project2);

    mockMvc
        .perform(get("/api/projects"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].title").value("Projeto1"))
        .andExpect(jsonPath("$[0].delegate.name").value("Amilcar"))
        .andExpect(jsonPath("$[0].d").exists())
        .andExpect(jsonPath("$[0].theme.name").value("Desporto"))
        .andExpect(jsonPath("$[1].title").value("p2"))
        .andExpect(jsonPath("$[1].delegate.name").value("Amilcar"))
        .andExpect(jsonPath("$[1].d").exists())
        .andExpect(jsonPath("$[1].theme.name").value("Desporto"))
        .andExpect(jsonPath("$").value(hasSize(2)));

    p2.setOngoing(false);
    p2 = projectRepository.save(p2);

    mockMvc
        .perform(get("/api/projects"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].title").value("Projeto1"))
        .andExpect(jsonPath("$[0].delegate.name").value("Amilcar"))
        .andExpect(jsonPath("$[0].d").exists())
        .andExpect(jsonPath("$[0].theme.name").value("Desporto"))
        .andExpect(jsonPath("$[0].id").value(p1.getId()))
        .andExpect(jsonPath("$").value(hasSize(1)));
  }

  @Test
  public void getProject() throws Exception {

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
    Project p1 = projectRepository.save(project1);

    Project project2 = new Project("p2", "p2", endDate, delegate, theme);
    Project p2 = projectRepository.save(project2);

    mockMvc
        .perform(get("/api/projects/" + p1.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(p1.getId()));
  }

  @Test
  public void noProject() throws Exception {

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
    Project p1 = projectRepository.save(project1);

    Project project2 = new Project("p2", "p2", endDate, delegate, theme);
    Project p2 = projectRepository.save(project2);

    Long id = 123L;

    mockMvc.perform(get("/api/projects/" + id)).andExpect(status().isNotFound());
  }
}
