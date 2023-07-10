package pt.ul.fc.css.example.demo.business.services;

import io.micrometer.common.lang.NonNull;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.ul.fc.css.example.demo.business.entities.Citizen;
import pt.ul.fc.css.example.demo.business.repositories.CitizenRepository;

/**
 * Class that represents a Citizen Service, making use of its Citizen Repository methods and getting
 * Citizens, a list of Citizens, finding specific citizens and putting them, given specific
 * conditions.
 */
@Service
public class CitizenService {

  @Autowired private final CitizenRepository citizenRepository;

  /**
   * Constructor for the Citizen Service, given a citizen repository.
   *
   * @param citizenRepository citizen repository
   */
  public CitizenService(CitizenRepository citizenRepository) {
    this.citizenRepository = citizenRepository;
  }

  /**
   * Getter for a citizen given a specific id
   *
   * @param id given id
   * @return citizen according to the id
   */
  public Optional<Citizen> getCitizen(Long id) {
    return this.citizenRepository.findById(id);
  }

  /**
   * Getter for all citizens
   *
   * @return list of all citizens
   */
  public List<Citizen> findAll() {
    return this.citizenRepository.findAll();
  }

  /**
   * Getter for a citizen given a specific name
   *
   * @param name given name
   * @return list of citizens according to the name
   */
  public List<Citizen> getCitizenByName(String name) {
    return this.citizenRepository.findByName(name);
  }

  /**
   * Puts a citizen in the repository
   *
   * @param citizen citizen being put in the repository
   * @return the saved citizen's id
   */
  public Long putCitizen(@NonNull Citizen citizen) {
    Citizen saved = citizenRepository.save(citizen);
    return saved.getId();
  }
}
