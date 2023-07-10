package pt.ul.fc.css.example.demo.business.services;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.ul.fc.css.example.demo.business.entities.Citizen;
import pt.ul.fc.css.example.demo.business.entities.CitizenThemeDelegate;
import pt.ul.fc.css.example.demo.business.entities.Delegate;
import pt.ul.fc.css.example.demo.business.entities.Theme;
import pt.ul.fc.css.example.demo.business.exceptions.CitizenNotFoundException;
import pt.ul.fc.css.example.demo.business.exceptions.DelegateNotFoundException;
import pt.ul.fc.css.example.demo.business.exceptions.SameIdException;
import pt.ul.fc.css.example.demo.business.exceptions.ThemeNotFoundException;

@Service
public class ChooseDelegateService {

  @Autowired private ThemeService themeService;

  @Autowired private DelegateService delegateService;

  @Autowired private CitizenService citizenService;

  public void chooseDelegate(Long userID, Long delegateID, Long themeID)
      throws ThemeNotFoundException,
          DelegateNotFoundException,
          CitizenNotFoundException,
          SameIdException {

    Optional<Theme> optionalTheme = themeService.findById(themeID);
    if (optionalTheme.isEmpty()) {
      throw new ThemeNotFoundException();
    }
    Delegate delegate = delegateService.getDelegate(delegateID);
    if (delegate == null) {
      throw new DelegateNotFoundException();
    }
    Optional<Citizen> optionalCitizen = citizenService.getCitizen(userID);
    if (optionalCitizen.isEmpty()) {
      throw new CitizenNotFoundException();
    }

    if (optionalCitizen.get().getId() == delegate.getId()) {
      throw new SameIdException();
    }

    Citizen citizen = optionalCitizen.get();

    CitizenThemeDelegate themeDelegate =
        new CitizenThemeDelegate(optionalTheme.get(), delegate, citizen);

    citizen.addThemeDelegate(themeDelegate);

    citizenService.putCitizen(citizen);
  }
}
