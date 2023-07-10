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
import pt.ul.fc.css.example.demo.business.exceptions.AssignedDelegateNullVoteException;
import pt.ul.fc.css.example.demo.business.exceptions.CitizenNotFoundException;
import pt.ul.fc.css.example.demo.business.exceptions.DelegateNotFoundException;
import pt.ul.fc.css.example.demo.business.exceptions.HasAlreadyVotedException;
import pt.ul.fc.css.example.demo.business.exceptions.NoDelegateAssignedException;
import pt.ul.fc.css.example.demo.business.exceptions.PollNotFoundException;
import pt.ul.fc.css.example.demo.business.services.OngoingPollsService;
import pt.ul.fc.css.example.demo.business.services.VoteProposalService;

@RestController()
@RequestMapping("api")
public class RestPollController {

  @Autowired private OngoingPollsService ongoingPollsService;

  @Autowired private VoteProposalService voteProposalService;

  @GetMapping("/polls")
  ResponseEntity<?> getPolls() {

    try {

      return ResponseEntity.ok(ongoingPollsService.getOngoingPolls());

    } catch (PollNotFoundException e) {

      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/polls/{id}")
  ResponseEntity<?> getDelegateVote(@PathVariable Long id, @RequestParam Long citizenId) {

    try {

      return ResponseEntity.ok(voteProposalService.pickPolls(id, citizenId));

    } catch (NoDelegateAssignedException e) {

      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("Nao existe delegado associado ao tema do projeto desta votacao");

    } catch (PollNotFoundException e) {

      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Votacao nao encontrada");

    } catch (CitizenNotFoundException e) {

      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cidadao nao existe");
    }
  }

  @PutMapping("/polls/{id}")
  ResponseEntity<?> voteOnPoll(
      @PathVariable Long id,
      @RequestParam Long citizenId,
      @RequestParam Boolean DelegateVotes,
      @RequestParam Boolean choice) {

    try {

      voteProposalService.voterChoice(citizenId, id, DelegateVotes, choice);

      return ResponseEntity.ok("Voto recebido!");

    } catch (AssignedDelegateNullVoteException e) {

      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Delegado associado não votou");

    } catch (HasAlreadyVotedException e) {

      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Já votou neste poll");

    } catch (DelegateNotFoundException e) {

      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("Delegado associado não encontrado");

    } catch (PollNotFoundException e) {

      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Votacao nao encontrada");

    } catch (CitizenNotFoundException e) {

      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body("ID do cidadao nao existe no sistema");
    }
  }
}
