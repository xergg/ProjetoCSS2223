package pt.ul.fc.css.example.demo;

import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pt.ul.fc.css.example.demo.business.entities.Delegate;
import pt.ul.fc.css.example.demo.business.entities.Project;
import pt.ul.fc.css.example.demo.business.entities.Theme;
import pt.ul.fc.css.example.demo.business.entities.Voter;
import pt.ul.fc.css.example.demo.business.repositories.CitizenRepository;
import pt.ul.fc.css.example.demo.business.repositories.ProjectRepository;
import pt.ul.fc.css.example.demo.business.repositories.ThemeRepository;
import pt.ul.fc.css.example.demo.business.services.SupportProjectService;

@SpringBootApplication
public class DemoApplication {

  private static final Logger log = LoggerFactory.getLogger(DemoApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(DemoApplication.class, args);
  }

  @Bean
  public CommandLineRunner demo(
      CitizenRepository repository,
      ProjectRepository projectRepository,
      ThemeRepository themeRepository,
      SupportProjectService supportProjectService) {
    return (args) -> {
      // save a few customers
      Delegate Jack = repository.save(new Delegate("Jack"));
      repository.save(new Delegate("Bauer"));
      repository.save(new Delegate("Kim"));
      repository.save(new Voter("David"));
      repository.save(new Voter("Michelle"));
      repository.save(new Voter("beep"));

      LocalDateTime endDate = LocalDateTime.of(2025, 5, 13, 15, 56);
      Delegate delegate = new Delegate("Amilcar");
      Theme theme = new Theme("Desporto");
      Theme theme2 = new Theme("Saude");
      repository.save(delegate);
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
    };
  }
}
