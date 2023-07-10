package pt.ul.fc.css.example.demo.presentation.webcontrollers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pt.ul.fc.css.example.demo.business.exceptions.AssignedDelegateNullVoteException;
import pt.ul.fc.css.example.demo.business.exceptions.CitizenNotFoundException;
import pt.ul.fc.css.example.demo.business.exceptions.DelegateNotFoundException;
import pt.ul.fc.css.example.demo.business.exceptions.HasAlreadyVotedException;
import pt.ul.fc.css.example.demo.business.exceptions.NoDelegateAssignedException;
import pt.ul.fc.css.example.demo.business.exceptions.PollNotFoundException;
import pt.ul.fc.css.example.demo.business.services.CitizenService;
import pt.ul.fc.css.example.demo.business.services.OngoingPollsService;
import pt.ul.fc.css.example.demo.business.services.PollService;
import pt.ul.fc.css.example.demo.business.services.VoteProposalService;

@Controller
public class WebPollController {

  @Autowired OngoingPollsService ongoingPollsService;
  @Autowired VoteProposalService voteProposalService;
  @Autowired PollService pollService;
  @Autowired CitizenService citizenService;

  Logger logger = LoggerFactory.getLogger(WebPollController.class);

  public WebPollController() {
    super();
  }

  @GetMapping("/polls")
  String getPolls(final Model model) {
    try {

      model.addAttribute("polls", ongoingPollsService.getOngoingPolls());

    } catch (PollNotFoundException e) {

      model.addAttribute("error", "No polls where found");
    }
    return "polls";
  }

  @GetMapping("/polls/{id}")
  String getPoll(final Model model, @PathVariable Long id) {

    model.addAttribute("users", citizenService.findAll());
    model.addAttribute("poll", pollService.findById(id).get());
    return "poll";
  }

  @PostMapping("/polls/{id}")
  String voteOnPoll(final Model model, @PathVariable Long id, @RequestParam("userid") Long userID) {

    try {

      // voteProposalService.voterChoice(citizenID, id, DelegateVotes, choice);
      model.addAttribute("poll", pollService.findById(id).get());
      model.addAttribute("citizen", citizenService.getCitizen(userID).get());
      model.addAttribute("delegateVote", voteProposalService.pickPolls(id, userID));
      return "vote";

    } catch (PollNotFoundException e) {

      return "error/poll404";

    } catch (CitizenNotFoundException e) {

      return "error/citizen404";

    } catch (NoDelegateAssignedException e) {

      model.addAttribute("poll", pollService.findById(id).get());
      model.addAttribute("citizen", citizenService.getCitizen(userID).get());
      model.addAttribute("delegateVote", null);
      return "vote";
    }
  }

  @PostMapping("/polls/{id}/vote")
  String voted(
      @PathVariable Long id,
      @RequestParam("citizenid") Long citizenID,
      @RequestParam("delegatevotes") boolean delegateVotes,
      @RequestParam("choice") boolean choice) {

    try {

      voteProposalService.voterChoice(citizenID, id, delegateVotes, choice);
      return "redirect:/polls/" + id;

    } catch (AssignedDelegateNullVoteException e) {

      return "error/badrequest";

    } catch (HasAlreadyVotedException e) {

      return "error/voted";

    } catch (DelegateNotFoundException e) {

      return "error/citizen404";

    } catch (PollNotFoundException e) {

      return "error/poll404";

    } catch (CitizenNotFoundException e) {

      return "error/citizen404";
    }
  }
}
