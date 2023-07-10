package pt.ul.fc.css.example.demo.business.dtos;

/**
 * Class that represents a Delegate DTO, for all the REST API, to encapsulate the data, for easier
 * handling between the backend and frontend.
 */
public class DelegateDTO {

  private Long id;

  private String name;

  /**
   * Constructor for a Delegate DTO
   *
   * @param id dto's id
   * @param name dto name
   */
  public DelegateDTO(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  /**
   * Getter for a id
   *
   * @return id
   */
  public Long getId() {
    return id;
  }

  /**
   * Getter for a name
   *
   * @return name
   */
  public String getName() {
    return name;
  }
}
