package pt.ul.fc.css.example.demo.business.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import pt.ul.fc.css.example.demo.business.entities.Citizen;
import pt.ul.fc.css.example.demo.business.entities.Voter;
import pt.ul.fc.css.example.demo.business.repositories.CitizenRepository;

/**
 * Class that represents a Voter Service, making use of its Citizen Repository methods and getting
 * Citizens, a list of Citizens, finding specific citizens and putting them, given specific
 * conditions. All citizen methods are then cast to Voter.
 */
@Service
public class VoterService {

  @Autowired private final CitizenRepository citizenRepository;

  /**
   * Constructor for Voter service, using a Citizen Repository.
   *
   * @param citizenRepository citizen repository
   */
  public VoterService(CitizenRepository citizenRepository) {
    this.citizenRepository = citizenRepository;
  }

  /**
   * Gets a voter given a specific id
   *
   * @param id given id
   * @return voter with the specific id, if it exists, null otherwise
   */
  public Voter getVoter(@PathVariable Long id) {
    Optional<Citizen> optionalCitizen = this.citizenRepository.findById(id);
    if (!optionalCitizen.isEmpty()) {
      Citizen citizen = optionalCitizen.get();
      if (citizen instanceof Voter) {
        return (Voter) citizen;
      }
    }
    return null;
  }

  /**
   * Gets voters with a specific name
   *
   * @param query given name
   * @return list of voters with the specific name
   */
  public List<Voter> getVoters(@RequestParam String query) {
    List<Citizen> listCitizens = this.citizenRepository.findAll();
    ArrayList<Voter> voter = new ArrayList<Voter>();
    for (Citizen citizen : listCitizens) {
      if (citizen instanceof Voter) voter.add((Voter) citizen);
    }
    return voter;
  }

  /**
   * Puts the specific voter in the repository
   *
   * @param voter given voter
   * @return saved voter's id
   */
  public Long putVoter(@NonNull @RequestBody Voter voter) {
    Voter saved = citizenRepository.save(voter);
    return saved.getId();
  }
}
