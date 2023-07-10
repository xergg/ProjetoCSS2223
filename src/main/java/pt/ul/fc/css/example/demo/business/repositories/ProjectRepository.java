package pt.ul.fc.css.example.demo.business.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pt.ul.fc.css.example.demo.business.entities.Project;

/**
 * Class that defines a ProjectRepository interface using JPA Repository methods along with custom
 * methods.
 */
public interface ProjectRepository extends JpaRepository<Project, Long> {

  /**
   * Finds Projects given a title
   *
   * @param query given title
   * @return list of projects with the given title
   */
  List<Project> findByTitle(String query);

  /**
   * Finds projects that are still ongoing
   *
   * @return list of projects that are ongoing
   */
  List<Project> findByongoingTrue();
}
