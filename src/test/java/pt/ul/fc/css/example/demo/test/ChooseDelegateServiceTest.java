package pt.ul.fc.css.example.demo.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pt.ul.fc.css.example.demo.business.entities.Citizen;
import pt.ul.fc.css.example.demo.business.entities.Delegate;
import pt.ul.fc.css.example.demo.business.entities.Theme;
import pt.ul.fc.css.example.demo.business.exceptions.CitizenNotFoundException;
import pt.ul.fc.css.example.demo.business.exceptions.DelegateNotFoundException;
import pt.ul.fc.css.example.demo.business.exceptions.SameIdException;
import pt.ul.fc.css.example.demo.business.exceptions.ThemeNotFoundException;
import pt.ul.fc.css.example.demo.business.repositories.CitizenRepository;
import pt.ul.fc.css.example.demo.business.repositories.PollRepository;
import pt.ul.fc.css.example.demo.business.repositories.ProjectRepository;
import pt.ul.fc.css.example.demo.business.repositories.ThemeRepository;
import pt.ul.fc.css.example.demo.business.services.ChooseDelegateService;

/** This class represents a Test class to the Choose Delegate use case's service. */
@SpringBootTest
public class ChooseDelegateServiceTest {

  @Autowired private ChooseDelegateService chooseDelegateService;

  @Autowired private ProjectRepository projectRepository;

  @Autowired private CitizenRepository delegateRepository;

  @Autowired private ThemeRepository themeRepository;

  @Autowired private PollRepository pollRepository;

  /**
   * This test makes sure of the following assertions:
   *
   * <p>1) Makes sure that a delegate is well attributed in the repository, making sure they're
   * equal 2) Given another delegate for the same theme , the old delegate should be replaced 3)
   * Given a new theme, adds the new delegate for this theme
   *
   * @throws SameIdException
   * @throws CitizenNotFoundException
   * @throws DelegateNotFoundException
   * @throws ThemeNotFoundException
   */
  @Test
  @Transactional
  public void testChooseDelegateByTheme()
      throws ThemeNotFoundException,
          DelegateNotFoundException,
          CitizenNotFoundException,
          SameIdException {

    projectRepository.deleteAll();
    delegateRepository.deleteAll();
    themeRepository.deleteAll();
    pollRepository.deleteAll();

    Delegate delegate = new Delegate("Amilcar");
    Citizen citizen = new Citizen("Fabricio");
    Theme theme = new Theme("Desporto");
    delegateRepository.save(delegate);
    citizen = delegateRepository.save(citizen);
    theme = themeRepository.save(theme);

    // should add delegate associated with theme
    chooseDelegateService.chooseDelegate(citizen.getId(), delegate.getId(), theme.getId());

    assertEquals(delegateRepository.findAll().get(1).getDelegate(theme), delegate);

    Delegate delegate2 = new Delegate("Joly");
    delegate2 = delegateRepository.save(delegate2);

    // should substitute delegate with delegate2 associated with theme
    chooseDelegateService.chooseDelegate(citizen.getId(), delegate2.getId(), theme.getId());
    assertEquals(delegateRepository.findAll().get(1).getDelegate(theme), delegate2);

    Theme theme2 = new Theme("Saude");
    theme2 = themeRepository.save(theme2);

    // should add delegate associated with theme2
    chooseDelegateService.chooseDelegate(citizen.getId(), delegate.getId(), theme2.getId());
    assertEquals(delegateRepository.findAll().get(1).getDelegate(theme2), delegate);
  }
}
