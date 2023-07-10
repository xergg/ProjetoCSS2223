package pt.ul.fc.css.example.demo.business.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import pt.ul.fc.css.example.demo.business.entities.Theme;
import pt.ul.fc.css.example.demo.business.repositories.ThemeRepository;

/**
 * Class that represents a Theme service, making use of its Theme Repository methods, and getting
 * Theme, a list of Themes, putting themes and finding specific themes by specific conditions.
 */
@Service
public class ThemeService {

  @Autowired private final ThemeRepository themeRepository;

  /**
   * Constructor for the theme service, using a theme repository.
   *
   * @param themeRepository theme repository
   */
  public ThemeService(ThemeRepository themeRepository) {
    this.themeRepository = themeRepository;
  }

  /**
   * Gets a theme given a specific name
   *
   * @param name given name
   * @return theme with the specific name
   */
  public Theme getThemeByName(@RequestParam String name) {
    return this.themeRepository.findByName(name);
  }

  /**
   * Gets a list of all themes
   *
   * @return all themes
   */
  public List<Theme> getThemes() {
    return this.themeRepository.findAll();
  }

  /**
   * Puts theme in the repository
   *
   * @param theme given theme
   * @return saved theme
   */
  public Theme putTheme(@NonNull @RequestBody Theme theme) {
    return this.themeRepository.save(theme);
  }

  /**
   * Gets a theme given a specific id
   *
   * @param themeID given id
   * @return a theme with the specific id
   */
  public Optional<Theme> findById(Long themeID) {
    return this.themeRepository.findById(themeID);
  }
}
