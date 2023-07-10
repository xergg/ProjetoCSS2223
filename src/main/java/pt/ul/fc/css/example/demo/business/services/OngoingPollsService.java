package pt.ul.fc.css.example.demo.business.services;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.ul.fc.css.example.demo.business.dtos.PollDTO;
import pt.ul.fc.css.example.demo.business.entities.Poll;
import pt.ul.fc.css.example.demo.business.exceptions.PollNotFoundException;

/**
 * This class represents a Service for the Ongoing Polls use case, making sure to get all ongoing
 * polls.
 */
@Service
public class OngoingPollsService {

  @Autowired private PollService pollService;

  /**
   * Method that allows the service to get a list of ongoing polls.
   *
   * @return all ongoing polls
   * @throws PollNotFoundException
   */
  public List<PollDTO> getOngoingPolls() throws PollNotFoundException {
    List<Poll> ongoingPolls = pollService.getOngoingPolls();
    List<PollDTO> ongoingPollDTOs = new ArrayList<>();
    if (ongoingPolls.isEmpty()) {
      throw new PollNotFoundException();
    } else {
      for (Poll poll : ongoingPolls) {
        ongoingPollDTOs.add(
            new PollDTO(
                poll.getId(),
                poll.getName(),
                poll.getNegativeVotes(),
                poll.getPositiveVotes(),
                poll.getStatusPoll(),
                poll.getEndDate(),
                poll.getIdList(),
                poll.getDelegateVotesMap()));
      }

      return ongoingPollDTOs;
    }
  }
}
