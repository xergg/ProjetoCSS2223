package pt.ul.fc.css.example.demo.presentation.restcontrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pt.ul.fc.css.example.demo.business.exceptions.AlreadyVotedException;
import pt.ul.fc.css.example.demo.business.exceptions.CitizenNotFoundException;
import pt.ul.fc.css.example.demo.business.exceptions.ProjectNotFoundException;
import pt.ul.fc.css.example.demo.business.exceptions.ProjectNotOnGoingException;
import pt.ul.fc.css.example.demo.business.services.CheckProjectService;
import pt.ul.fc.css.example.demo.business.services.SupportProjectService;

@RestController
@RequestMapping("api")
public class RestProjectController {

  @Autowired private CheckProjectService checkProjectService;

  @Autowired private SupportProjectService supportProjectService;

  @GetMapping("/projects")
  ResponseEntity<?> getProjects() {
    try {

      return ResponseEntity.ok().body(checkProjectService.CheckNonExpiredProjects());

    } catch (ProjectNotFoundException e) {

      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/projects/{id}")
  ResponseEntity<?> getProject(@PathVariable Long id) {

    try {

      return ResponseEntity.ok(checkProjectService.getProject(id));

    } catch (ProjectNotFoundException e) {

      return ResponseEntity.notFound().build();
    }
  }

  @PutMapping("/projects/{id}")
  ResponseEntity<?> supportProject(@RequestParam Long citizenId, @PathVariable Long id) {

    try {

      supportProjectService.supportProject(citizenId, id);

      return ResponseEntity.ok().body("Project Apoiado!");

    } catch (CitizenNotFoundException e) {

      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cidadao nao encontrado");

    } catch (ProjectNotFoundException e) {

      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Projeto nao encontrado");

    } catch (AlreadyVotedException e) {

      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("Este cidadao ja apoiou este projeto");

    } catch (ProjectNotOnGoingException e) {

      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("O projeto ja nao se encontra disponivel para apoiar");
    }
  }
}
