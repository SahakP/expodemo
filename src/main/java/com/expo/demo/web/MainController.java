package com.expo.demo.web;

import com.expo.demo.service.SecurityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.expo.demo.util.XUrlAuthenticationSuccessHandler;


@Controller
class MainController {

    @Autowired
    private SecurityServiceImpl mSecurityService;

    @RequestMapping(value = {"/"}, method = RequestMethod.GET)
    public ModelAndView welcome() {
        ModelAndView modelAndView = new ModelAndView();

        XUrlAuthenticationSuccessHandler XHandler = new XUrlAuthenticationSuccessHandler();
        String url = XHandler.determineTargetUrl(mSecurityService.findLoggedInUsername());

        modelAndView.setViewName("redirect:" + url);
        return modelAndView;
    }

    @RequestMapping(value = {"/accessDenied"}, method = RequestMethod.GET)
    public ModelAndView accessDenied() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("access_denied");
        return modelAndView;
    }

    static ModelAndView getErrorPage(String message) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("message", message);
        modelAndView.addObject("title", message);
        modelAndView.setViewName("error_message");
        return modelAndView;
    }
}
