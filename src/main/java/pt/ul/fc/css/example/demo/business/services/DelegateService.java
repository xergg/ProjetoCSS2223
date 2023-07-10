package pt.ul.fc.css.example.demo.business.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import pt.ul.fc.css.example.demo.business.entities.Citizen;
import pt.ul.fc.css.example.demo.business.entities.Delegate;
import pt.ul.fc.css.example.demo.business.repositories.CitizenRepository;

/**
 * Class that represents a Delegate Service, making use of its Citizen Repository methods and
 * getting Citizens, a list of Citizens, finding specific citizens and putting them, given specific
 * conditions. All citizen methods are then cast to Delegate.
 */
@Service
public class DelegateService {

  @Autowired private final CitizenRepository citizenRepository;

  /**
   * Constructor for Delegate service, using a Citizen Repository.
   *
   * @param delegateRepository citizen repository
   */
  public DelegateService(CitizenRepository delegateRepository) {
    this.citizenRepository = delegateRepository;
  }

  /**
   * Gets a delegate given a specific id
   *
   * @param id given id
   * @return delegate with the specific id, if it exists, null otherwise
   */
  public Delegate getDelegate(@PathVariable Long id) {
    Optional<Citizen> optionalCitizen = this.citizenRepository.findById(id);
    if (!optionalCitizen.isEmpty()) {
      Citizen citizen = optionalCitizen.get();
      if (citizen instanceof Delegate) {
        return (Delegate) citizen;
      }
    }
    return null;
  }

  /**
   * Gets delegates with a specific name
   *
   * @return list of delegates with the specific name
   */
  public List<Delegate> getDelegates() {
    List<Citizen> listCitizens = this.citizenRepository.findAll();
    ArrayList<Delegate> delegates = new ArrayList<Delegate>();
    for (Citizen citizen : listCitizens) {
      if (citizen instanceof Delegate) delegates.add((Delegate) citizen);
    }
    return delegates;
  }

  public List<Delegate> getDelegatesExcept(Long id) {
    List<Citizen> listCitizens = this.citizenRepository.findAll();
    ArrayList<Delegate> delegates = new ArrayList<Delegate>();
    for (Citizen citizen : listCitizens) {
      if ((citizen instanceof Delegate) && (citizen.getId() != id))
        delegates.add((Delegate) citizen);
    }
    return delegates;
  }

  /**
   * Puts the specific delegate in the repository
   *
   * @param delegate given delegate
   * @return saved delegate's id
   */
  public Long putDelegate(@NonNull Delegate delegate) {
    Delegate saved = citizenRepository.save(delegate);
    return saved.getId();
  }

  // encontrar por delegado, ver se tem tema etc
  // public List<Delegate> getDelegatesByTheme(@RequestParam String query){
  // return this.citizenRepository.findByTheme(query);
  // }
}
