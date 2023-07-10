package pt.ul.fc.css.example.demo.business.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import org.springframework.lang.NonNull;

/**
 * Class that represents and Voter entity, which is a child from Citizen hierarchy. Has all the
 * Citizen methods such as getters and setters
 */
@Entity
@DiscriminatorValue("voter")
public class Voter extends Citizen {

  /** Default constructor for Voter */
  public Voter() {}

  /**
   * Constructor for a voter given a name
   *
   * @param name name being given
   */
  public Voter(@NonNull String name) {
    super(name);
  }
}
