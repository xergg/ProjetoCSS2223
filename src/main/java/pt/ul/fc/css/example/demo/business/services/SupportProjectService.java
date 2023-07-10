package pt.ul.fc.css.example.demo.business.services;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.ul.fc.css.example.demo.business.entities.Citizen;
import pt.ul.fc.css.example.demo.business.entities.Poll;
import pt.ul.fc.css.example.demo.business.entities.Project;
import pt.ul.fc.css.example.demo.business.entities.Vote;
import pt.ul.fc.css.example.demo.business.exceptions.AlreadyVotedException;
import pt.ul.fc.css.example.demo.business.exceptions.CitizenNotFoundException;
import pt.ul.fc.css.example.demo.business.exceptions.ProjectNotFoundException;
import pt.ul.fc.css.example.demo.business.exceptions.ProjectNotOnGoingException;
import pt.ul.fc.css.example.demo.utils.StatusPoll;

/** Class that represents the Support Project use case, making sure to support a project. */
@Service
public class SupportProjectService {

  @Autowired private CitizenService citizenService;

  @Autowired private ProjectService projectService;

  @Autowired private PollService pollService;

  /**
   * Method that makes sure that a citizen given an id, supports a project with a specific id
   *
   * @param citizenId given citizen id
   * @param projectId given project id
   * @return a response entity with a confirmation that the project/signature has been signed
   * @throws CitizenNotFoundException
   * @throws ProjectNotFoundException
   * @throws AlreadyVotedException
   * @throws ProjectNotOnGoingException
   */
  @Transactional
  public void supportProject(Long citizenId, Long projectId)
      throws CitizenNotFoundException,
          ProjectNotFoundException,
          AlreadyVotedException,
          ProjectNotOnGoingException {

    LocalDateTime dateNow;
    dateNow = LocalDateTime.now();

    Optional<Citizen> optionalcitizen = citizenService.getCitizen(citizenId);

    if (optionalcitizen.isEmpty()) {
      throw new CitizenNotFoundException();
    }

    Citizen citizen = optionalcitizen.get();

    Optional<Project> optionalProject = projectService.findById(projectId);

    if (optionalProject.isEmpty()) {
      throw new ProjectNotFoundException();
    }

    Project project = optionalProject.get();

    if (project.checkIfSigned(citizenId)) {
      throw new AlreadyVotedException();
    }

    if (!project.verifyOngoing(dateNow)) {
      throw new ProjectNotOnGoingException();
    }

    project.addSignature(citizen.getId());
    projectService.putProject(project);

    if (project.getSignatures().size() >= 10000 && !project.hasPoll()) {

      Poll poll =
          new Poll(project.getTitle(), 0, 0, project, StatusPoll.ONGOING, project.getDate());

      Long delId = project.getDelegate().getId();
      Vote vote = new Vote(delId, true);
      poll.voteOn(vote);
      poll.addVoteId(delId);
      poll.addDelegateVotes(delId, true);

      Long id = pollService.putPoll(poll);

      project.setPoll(pollService.findById(id).get());

      projectService.putProject(project);
    }
  }
}
