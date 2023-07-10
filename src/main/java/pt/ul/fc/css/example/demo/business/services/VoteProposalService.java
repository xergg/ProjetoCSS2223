package pt.ul.fc.css.example.demo.business.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.ul.fc.css.example.demo.business.dtos.PollDTO;
import pt.ul.fc.css.example.demo.business.entities.Citizen;
import pt.ul.fc.css.example.demo.business.entities.Delegate;
import pt.ul.fc.css.example.demo.business.entities.Poll;
import pt.ul.fc.css.example.demo.business.entities.Theme;
import pt.ul.fc.css.example.demo.business.entities.Vote;
import pt.ul.fc.css.example.demo.business.entities.Voter;
import pt.ul.fc.css.example.demo.business.exceptions.AssignedDelegateNullVoteException;
import pt.ul.fc.css.example.demo.business.exceptions.CitizenNotFoundException;
import pt.ul.fc.css.example.demo.business.exceptions.DelegateNotFoundException;
import pt.ul.fc.css.example.demo.business.exceptions.HasAlreadyVotedException;
import pt.ul.fc.css.example.demo.business.exceptions.NoDelegateAssignedException;
import pt.ul.fc.css.example.demo.business.exceptions.PollNotFoundException;

/** Class that represents the Voter Proposal use case, making sure to vote in a proposal. */
@Service
public class VoteProposalService {

  @Autowired private PollService pollService;
  @Autowired private CitizenService citizenService;
  @Autowired private OngoingPollsService ongoingPollsService;

  /**
   * Method that gets all polls.
   *
   * @return returns an success message with all the polls available.
   * @throws PollNotFoundException
   */
  public List<PollDTO> getPolls() throws PollNotFoundException {
    List<PollDTO> polls = ongoingPollsService.getOngoingPolls();
    return polls;
  }

  /**
   * Returns a delegate response given a poll id and a citizen id
   *
   * @param id given poll id
   * @param citizenID given citizen id
   * @return success response with the delegate's choice
   * @throws NoDelegateAssignedException
   * @throws PollNotFoundException
   * @throws CitizenNotFoundException
   */
  public boolean pickPolls(Long id, Long citizenID)
      throws NoDelegateAssignedException, PollNotFoundException, CitizenNotFoundException {

    Optional<Citizen> optionalCitizen = citizenService.getCitizen(citizenID);

    if (optionalCitizen.isEmpty()) {
      throw new CitizenNotFoundException();
    }

    Optional<Poll> optionalPoll = pollService.findById(id);

    if (optionalPoll.isEmpty()) {
      throw new PollNotFoundException();
    }

    Theme theme = optionalPoll.get().getProject().getTheme();
    Delegate delegate = getParentThemeDelegate(theme, optionalCitizen.get(), optionalPoll.get());

    if (delegate == null) {
      throw new NoDelegateAssignedException();
    }

    return optionalPoll.get().getDelegateVote(delegate);
  }

  /**
   * Given a citizen id, a poll id, a voter and a choice, makes sure the citizen votes
   *
   * @param citizenID given citizen id
   * @param pollID given poll id
   * @param voter given voter
   * @param choice given choice
   * @return success message informing the vote was successful
   * @throws AssignedDelegateNullVoteException
   * @throws HasAlreadyVotedException
   * @throws DelegateNotFoundException
   * @throws PollNotFoundException
   * @throws CitizenNotFoundException
   */
  public void voterChoice(Long citizenID, Long pollID, Boolean voter, Boolean choice)
      throws AssignedDelegateNullVoteException,
          HasAlreadyVotedException,
          DelegateNotFoundException,
          PollNotFoundException,
          CitizenNotFoundException {

    Optional<Citizen> optionalCitizen = citizenService.getCitizen(citizenID);

    if (optionalCitizen.isEmpty()) {
      throw new CitizenNotFoundException();
    }

    Optional<Poll> optionalPoll = pollService.findById(pollID);

    if (optionalPoll.isEmpty()) {
      throw new PollNotFoundException();
    }

    Theme theme = optionalPoll.get().getProject().getTheme();
    Delegate delegate = getParentThemeDelegate(theme, optionalCitizen.get(), optionalPoll.get());

    if (delegate == null && voter == true) {
      throw new DelegateNotFoundException();
    }

    Citizen citizen = optionalCitizen.get();
    Poll poll = optionalPoll.get();
    if (!poll.hasVoted(citizenID)) {

      if (citizen instanceof Voter) {
        if (voter) {
          Boolean delVote = poll.getDelegateVote(delegate);

          if (delVote == null) {
            throw new AssignedDelegateNullVoteException();
          }

          Vote vote = new Vote(citizenID, delVote);
          poll.voteOn(vote);
          poll.addVoteId(citizenID);
        } else {
          Vote vote = new Vote(citizenID, choice);
          poll.voteOn(vote);
          poll.addVoteId(citizenID);
        }
      }

      if (citizen instanceof Delegate) {
        if (voter) {
          Boolean delVote = poll.getDelegateVote(delegate);

          if (delVote == null) {
            throw new AssignedDelegateNullVoteException();
          }

          Vote vote = new Vote(citizenID, delVote);
          poll.voteOn(vote);
          poll.addVoteId(citizenID);
          poll.addDelegateVotes(citizenID, delVote);
        } else {
          Vote vote = new Vote(citizenID, choice);
          poll.voteOn(vote);
          poll.addVoteId(citizenID);
          poll.addDelegateVotes(citizenID, choice);
        }
      }

      pollService.putPoll(poll);

    } else {
      throw new HasAlreadyVotedException();
    }
  }

  /**
   * Method that searches for the most specific delegate given a theme in a poll, for a certain
   * citizen.
   *
   * @param theme given theme
   * @param citizen given citizen
   * @param poll given poll
   * @return the most specific delegate
   */
  private Delegate getParentThemeDelegate(Theme theme, Citizen citizen, Poll poll) {
    Boolean delegateVote = null;
    Delegate delegate = null;
    Theme nextTheme = theme;
    do {
      theme = nextTheme;
      delegate = citizen.getDelegate(nextTheme);
      if (delegate != null) {
        delegateVote = poll.getDelegateVote(delegate);
        if (delegateVote == null) {
          nextTheme = theme.getParent();
          delegate = null;
        }
      } else {
        nextTheme = theme.getParent();
      }
    } while (theme.hasParent() && delegateVote == null);
    return delegate;
  }
}
