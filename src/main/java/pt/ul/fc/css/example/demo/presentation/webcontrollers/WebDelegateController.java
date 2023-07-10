package pt.ul.fc.css.example.demo.presentation.webcontrollers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pt.ul.fc.css.example.demo.business.exceptions.CitizenNotFoundException;
import pt.ul.fc.css.example.demo.business.exceptions.DelegateNotFoundException;
import pt.ul.fc.css.example.demo.business.exceptions.SameIdException;
import pt.ul.fc.css.example.demo.business.exceptions.ThemeNotFoundException;
import pt.ul.fc.css.example.demo.business.services.ChooseDelegateService;
import pt.ul.fc.css.example.demo.business.services.CitizenService;
import pt.ul.fc.css.example.demo.business.services.DelegateService;
import pt.ul.fc.css.example.demo.business.services.ThemeService;

@Controller
public class WebDelegateController {

  Logger logger = LoggerFactory.getLogger(WebPollController.class);

  public WebDelegateController() {
    super();
  }

  @Autowired ChooseDelegateService chooseDelegateService;
  @Autowired DelegateService delegateService;
  @Autowired ThemeService themeService;
  @Autowired CitizenService citizenService;

  @GetMapping("/delegates/assign")
  String getDelegates(final Model model) {

    model.addAttribute("users", citizenService.findAll());

    model.addAttribute("themes", themeService.getThemes());

    model.addAttribute("delegates", delegateService.getDelegates());

    return "assigndelegate";
  }

  @PostMapping("/delegates/assign")
  String assignDelegates(
      @RequestParam("userid") Long userID,
      @RequestParam("themeid") Long themeID,
      @RequestParam("delegateid") Long delegateID) {

    try {

      chooseDelegateService.chooseDelegate(userID, delegateID, themeID);
      return "success";

    } catch (CitizenNotFoundException e) {

      return "error/citizen404";

    } catch (DelegateNotFoundException e) {

      return "error/citizen404";

    } catch (ThemeNotFoundException e) {

      return "error/theme404";

    } catch (SameIdException e) {

      return "error/sameid";
    }
  }
}
