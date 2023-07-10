package pt.ul.fc.css.example.demo.business.services;

import jakarta.transaction.Transactional;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.ul.fc.css.example.demo.business.entities.Delegate;
import pt.ul.fc.css.example.demo.business.entities.Project;
import pt.ul.fc.css.example.demo.business.entities.Theme;
import pt.ul.fc.css.example.demo.business.exceptions.DelegateNotFoundException;
import pt.ul.fc.css.example.demo.business.exceptions.InvalidDateException;
import pt.ul.fc.css.example.demo.business.exceptions.InvalidTitleTextException;
import pt.ul.fc.css.example.demo.business.exceptions.ThemeNotFoundException;

/**
 * Class that represents a service for the Present Project use case, making sure to present a
 * project.
 */
@Service
public class PresentProjectService {

  @Autowired private ProjectService projectService;

  @Autowired private ThemeService themeService;

  @Autowired private DelegateService delegateService;

  private LocalDateTime now;

  /**
   * Presenting a project by a delegate, which then attributes the project a title, a descriptive
   * text a pdf, an end date (max 1 year), and a theme.
   *
   * @param delegateID given delegate id
   * @param title given title
   * @param text given descriptive text
   * @param pdf given pdf
   * @param endDate given end date (max 1 year)
   * @param themeS given theme string i.e name
   * @return
   * @throws DelegateNotFoundException
   * @throws ThemeNotFoundException
   * @throws InvalidTitleTextException
   * @throws IOException
   * @throws InvalidDateException
   */
  @Transactional
  public Long presentProject(
      Long delegateID, String title, String text, LocalDateTime endDate, String themeS)
      throws DelegateNotFoundException,
          ThemeNotFoundException,
          InvalidTitleTextException,
          IOException,
          InvalidDateException {

    Theme theme = themeService.getThemeByName(themeS);
    Delegate delegate = delegateService.getDelegate(delegateID);

    if (delegate == null) {
      throw new DelegateNotFoundException();
    }

    if (theme == null) {
      throw new ThemeNotFoundException();
    }

    now = LocalDateTime.now();
    Duration duration = Duration.between(endDate, now);
    double year = 31556926;

    if (endDate.isBefore(now) || duration.toSeconds() > year) throw new InvalidDateException();

    if (title.isEmpty() || text.isEmpty()) throw new InvalidTitleTextException();

    Project project = new Project(title, text, endDate, delegate, theme);

    Long id = projectService.putProject(project);

    Project projectT = projectService.getProject(id);

    delegate.addProject(projectT);
    theme.addProject(projectT);

    delegateService.putDelegate(delegate);

    themeService.putTheme(theme);

    return id;
  }
}
