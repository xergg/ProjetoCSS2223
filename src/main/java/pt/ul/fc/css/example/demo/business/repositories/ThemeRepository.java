package pt.ul.fc.css.example.demo.business.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pt.ul.fc.css.example.demo.business.entities.Theme;

/**
 * Class that defines a ThemeRepository interface using JPA Repository methods along with custom
 * methods.
 */
public interface ThemeRepository extends JpaRepository<Theme, Long> {

  /**
   * Finds a theme by name
   *
   * @param name name being searched for
   * @return theme with given name
   */
  Theme findByName(String name);

  /** Returns themes with the given id */
  Optional<Theme> findById(Long themeID);
}
