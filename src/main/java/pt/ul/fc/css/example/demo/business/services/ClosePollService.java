package pt.ul.fc.css.example.demo.business.services;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pt.ul.fc.css.example.demo.business.entities.Citizen;
import pt.ul.fc.css.example.demo.business.entities.Delegate;
import pt.ul.fc.css.example.demo.business.entities.Poll;
import pt.ul.fc.css.example.demo.business.entities.Theme;
import pt.ul.fc.css.example.demo.business.entities.Vote;
import pt.ul.fc.css.example.demo.utils.StatusPoll;

/** Class that represents a Service for the Close Poll use case, making sure to close polls. */
@Service
public class ClosePollService {

  @Autowired private PollService pollService;

  @Autowired private CitizenService citizenService;

  private LocalDateTime dateNow;

  /**
   * Method that closes all polls that have had their end date due, proceeding to distribute the
   * delegate's choice to all citizens that hadn't explicitly voted, then counting all the votes
   * making sure that if more than half the votes are positive, it's closed and approved, otherwise
   * it's closed and denied.
   */
  @Scheduled(fixedDelay = 3600000)
  @Transactional
  public void closePolls() {
    try {
      List<Citizen> citizens = citizenService.findAll();
      List<Poll> polls = pollService.getAllPolls();
      dateNow = LocalDateTime.now();
      for (Poll poll : polls) {
        if (poll.verifyFinished(dateNow)) {

          Theme pollTheme = poll.getProject().getTheme();

          for (Citizen citizen : citizens) {

            Delegate citizenDelegate = getParentThemeDelegate(pollTheme, citizen, poll);

            if (citizenDelegate != null && !poll.hasVoted(citizen.getId())) {

              Boolean delVote = poll.getDelegateVote(citizenDelegate);

              if (delVote != null) {

                Vote vote = new Vote(citizen.getId(), delVote);
                poll.addVoteId(citizen.getId());
                poll.voteOn(vote);
              }
            }
          }

          if (poll.getPositiveVotes() > poll.getNegativeVotes()) {

            poll.setStatusPoll(StatusPoll.APPROVED);
          } else {
            poll.setStatusPoll(StatusPoll.DENIED);
          }

          pollService.putPoll(poll);
        }
      }
    } catch (Exception e) {

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
