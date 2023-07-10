package pt.ul.fc.css.example.demo.presentation.webcontrollers;

import java.io.IOException;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pt.ul.fc.css.example.demo.business.exceptions.AlreadyVotedException;
import pt.ul.fc.css.example.demo.business.exceptions.CitizenNotFoundException;
import pt.ul.fc.css.example.demo.business.exceptions.DelegateNotFoundException;
import pt.ul.fc.css.example.demo.business.exceptions.InvalidDateException;
import pt.ul.fc.css.example.demo.business.exceptions.InvalidTitleTextException;
import pt.ul.fc.css.example.demo.business.exceptions.ProjectNotFoundException;
import pt.ul.fc.css.example.demo.business.exceptions.ProjectNotOnGoingException;
import pt.ul.fc.css.example.demo.business.exceptions.ThemeNotFoundException;
import pt.ul.fc.css.example.demo.business.services.CheckProjectService;
import pt.ul.fc.css.example.demo.business.services.CitizenService;
import pt.ul.fc.css.example.demo.business.services.DelegateService;
import pt.ul.fc.css.example.demo.business.services.PresentProjectService;
import pt.ul.fc.css.example.demo.business.services.SupportProjectService;
import pt.ul.fc.css.example.demo.business.services.ThemeService;

@Controller
public class WebProjectController {

  Logger logger = LoggerFactory.getLogger(WebPollController.class);

  @Autowired CheckProjectService checkProjectService;
  @Autowired SupportProjectService supportProjectService;
  @Autowired PresentProjectService presentProjectService;
  @Autowired ThemeService themeService;
  @Autowired CitizenService citizenService;
  @Autowired DelegateService delegateService;

  public WebProjectController() {
    super();
  }

  @GetMapping("/projects")
  String getProjects(final Model model) {
    try {

      model.addAttribute("projects", checkProjectService.CheckNonExpiredProjects());

    } catch (ProjectNotFoundException e) {

      model.addAttribute("error", "Projects Not Found");
    }

    return "projects";
  }

  @GetMapping("/projects/{id}")
  String getProject(final Model model, @PathVariable Long id) {

    try {

      model.addAttribute("users", citizenService.findAll());

      model.addAttribute("project", checkProjectService.getProject(id));

    } catch (ProjectNotFoundException e) {

      return "error/project404";
    }

    return "project";
  }

  @PostMapping("/projects/{id}/support")
  String supportProject(
      final Model model, @PathVariable Long id, @RequestParam("userid") Long userID) {

    try {

      supportProjectService.supportProject(userID, id);
      model.addAttribute("project", checkProjectService.getProject(id));
      model.addAttribute("mensagem_de_sucesso", "projeto apoiado com sucesso");
      return "redirect:/projects/" + id;

    } catch (CitizenNotFoundException e) {

      return "error/citizen404";

    } catch (ProjectNotFoundException e) {

      return "error/project404";

    } catch (AlreadyVotedException e) {

      return "error/voted";

    } catch (ProjectNotOnGoingException e) {

      return "error/notongoing";
    }
  }

  @GetMapping("/project/new")
  public String createProject(final Model model) {

    model.addAttribute("users", delegateService.getDelegates());
    model.addAttribute("temas", themeService.getThemes());
    return "presentproject";
  }

  @PostMapping("/project/new")
  public String createProjectAction(
      @RequestParam("userid") Long userID,
      @RequestParam("titulo") String titulo,
      @RequestParam("descricao") String descricao,
      @RequestParam("data_validade") LocalDateTime dataValidade,
      @RequestParam("tema") String tema) {

    try {

      Long id = presentProjectService.presentProject(userID, titulo, descricao, dataValidade, tema);
      return "redirect:/projects/" + id;

    } catch (DelegateNotFoundException e) {

      return "error/citizen404";

    } catch (ThemeNotFoundException e) {

      return "error/theme404";

    } catch (InvalidTitleTextException e) {

      return "error/badrequest";

    } catch (IOException e) {

      return "error/badrequest";

    } catch (InvalidDateException e) {

      return "error/badrequest";
    }
  }
}
