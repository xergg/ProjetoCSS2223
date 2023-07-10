package pt.ul.fc.css.example.demo.business.dtos;

/**
 * Class that represents a Theme DTO, for all the REST API, to encapsulate the data, for easier
 * handling between the backend and frontend.
 */
public class ThemeDTO {

  public Long id;

  public String name;

  /**
   * Constructor for a theme dto
   *
   * @param id dto's id
   * @param name dto's name
   */
  public ThemeDTO(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  /**
   * Getter for the id
   *
   * @return id
   */
  public Long getId() {
    return id;
  }

  /**
   * Getter for the name
   *
   * @return name
   */
  public String getName() {
    return name;
  }
}
