package com.expo.demo.web;

import com.expo.demo.config.SecurityConfig;
import com.expo.demo.constants.Diciton;
import com.expo.demo.logic.UserHelper;
import com.expo.demo.model.user.Role;
import com.expo.demo.model.user.User;
import com.expo.demo.model.user.UserInfo;
import com.expo.demo.model.util.BindingMap;
import com.expo.demo.service.UserService;
import com.expo.demo.util.XUtils;
import com.expo.demo.validator.UserInfoValidator;
import com.expo.demo.validator.UserPasswordValidator;
import com.expo.demo.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class UserController {
    //ToDo: Remove word commissioner from: url, method names
    @Autowired
    private UserService mUserService;

    @Autowired
    private UserHelper mUserHelper;

    @Autowired
    private SecurityConfig securityConfig;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserValidator mUserValidator;

    @Autowired
    private UserInfoValidator mUserInfoValidator;

    @Autowired
    private UserPasswordValidator mUserPasswordValidator;

    @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
    public ModelAndView login(String error) {
        ModelAndView modelAndView = new ModelAndView();
        if (error != null) {
            modelAndView.addObject("error", "Incorrect username or password");
        }

        modelAndView.setViewName("login");
        return modelAndView;
    }

    @RequestMapping(value = {"/admin/users"}, method = RequestMethod.GET)
    public ModelAndView users(@RequestParam("tab") Optional<Integer> tab,
                              @RequestParam("pageAdmin") Optional<Integer> pageAdmin,
                              @RequestParam("pageMember") Optional<Integer> pageMember,
                              @RequestParam("pageModerator") Optional<Integer> pageModerator) {

        ModelAndView modelAndView = new ModelAndView();
        boolean isAdmin = mUserHelper.isAdmin();

        int currentPageAdmin = pageAdmin.orElse(1);
        currentPageAdmin = currentPageAdmin < 1 ? 1 : currentPageAdmin;
        int currentPageModerator = pageModerator.orElse(1);
        currentPageModerator = currentPageModerator < 1 ? 1 : currentPageModerator;
        int currentPageMember = pageMember.orElse(1);
        currentPageMember = currentPageMember < 1 ? 1 : currentPageMember;
        int currentTab = tab.orElse(1);

        Page<User> userAdminPage = mUserService.getUsersByPage(Diciton.ROLE_ADMIN, currentPageAdmin - 1);
        Page<User> userModeratorPage = mUserService.getUsersByPage(Diciton.ROLE_MODERATOR, currentPageModerator - 1);
        Page<User> userMemberPage = mUserService.getUsersByPage(Diciton.ROLE_MEMBER, currentPageMember - 1);

        modelAndView.addObject("userAdminPage", userAdminPage);
        modelAndView.addObject("userMemberPage", userMemberPage);
        modelAndView.addObject("userModeratorPage", userModeratorPage);
        modelAndView.addObject("name", XUtils.getCurrentUserName(mUserHelper.getCurrentUser()));

        int totalPagesAdmin = userAdminPage.getTotalPages();
        if (totalPagesAdmin > 0) {
            List<Integer> pageNumbersAdmin = IntStream.rangeClosed(1, totalPagesAdmin).boxed().collect(Collectors.toList());
            modelAndView.addObject("pageNumbersAdmin", pageNumbersAdmin);
        }
        int totalPagesModerator = userModeratorPage.getTotalPages();
        if (totalPagesModerator > 0) {
            List<Integer> pageNumbersModerator = IntStream.rangeClosed(1, totalPagesAdmin).boxed().collect(Collectors.toList());
            modelAndView.addObject("pageNumbersModerator", pageNumbersModerator);
        }

        int totalPagesMember = userMemberPage.getTotalPages();
        if (totalPagesMember > 0) {
            List<Integer> pageNumbersMember = IntStream.rangeClosed(1, totalPagesMember).boxed().collect(Collectors.toList());
            modelAndView.addObject("pageNumbersMember", pageNumbersMember);
        }


        modelAndView.addObject("tab", currentTab);
        modelAndView.addObject("currentPageAdmin", currentPageAdmin);
        modelAndView.addObject("currentPageMember", currentPageMember);
        modelAndView.addObject("currentPageModerator", currentPageModerator);
        modelAndView.addObject("isEditRole", XUtils.isEditRole(mUserHelper.getCurrentUser()));
        modelAndView.addObject("isAdmin", mUserHelper.isAdmin());
        modelAndView.addObject("isModerator", mUserHelper.isModerator());

        BindingMap bindingMap = new BindingMap();
        bindingMap.setTab(currentTab);
        bindingMap.setPageAdmin(currentPageAdmin);
        bindingMap.setPageMember(currentPageMember);
        bindingMap.setPageModerator(currentPageModerator);
        modelAndView.addObject("bindingMap", bindingMap);

        modelAndView.setViewName("users");

        return modelAndView;
    }

    @RequestMapping(value = {"/member/welcome"}, method = RequestMethod.GET)
    public ModelAndView member() {
        ModelAndView modelAndView = new ModelAndView();

        String name = XUtils.getCurrentUserName(mUserHelper.getCurrentUser());

        modelAndView.addObject("name", name);
        modelAndView.setViewName("welcome");
        return modelAndView;
    }


    // @Secured({Diciton.ROLE_ADMIN,})
    @RequestMapping(value = {"/register"}, method = RequestMethod.GET)
    public ModelAndView register(@RequestParam("regAdmin") Optional<Long> regAdmin,
                                 @RequestParam("regMember") Optional<Long> regMember,
                                 @RequestParam("regModerator") Optional<Long> regModerator,
                                 @RequestParam("tab") Optional<Long> tab) {
        ModelAndView modelAndView = new ModelAndView();
        User newUser = new User();
        newUser.setUserInfo(new UserInfo());


        if (regAdmin.isPresent() && regAdmin.get() == 1) {
            newUser.setSelectedRoleName(Diciton.ROLE_ADMIN);
        }

        if (regMember.isPresent() && regMember.get() == 1) {
            newUser.setSelectedRoleName(Diciton.ROLE_MEMBER);
        }

        if (regModerator.isPresent() && regModerator.get() == 1) {
            newUser.setSelectedRoleName(Diciton.ROLE_MODERATOR);
        }


        BindingMap bindingMap = new BindingMap();
        modelAndView.addObject("user", newUser);
        modelAndView.addObject("isAdmin", mUserHelper.isAdmin());
        modelAndView.addObject("isModerator", mUserHelper.isModerator());
        modelAndView.addObject("title", "Գրանցել նոր օգտվող");
        modelAndView.addObject("name", XUtils.getCurrentUserName(mUserHelper.getCurrentUser()));
        modelAndView.addObject("bindingMap", bindingMap);
        modelAndView.addObject("tab", tab.orElse(1L));

        modelAndView.setViewName("register");

        return modelAndView;
    }



    @RequestMapping(value = {"/register"}, method = RequestMethod.POST)
    public ModelAndView register(@Valid @ModelAttribute("user") User user,
                                 BindingResult bindingResult, Model model) {

        /*
        *
        * @Valid @ModelAttribute("user") User user,
                                 BindingResult bindingResult, Model model,
                                 @RequestParam("userId") Optional<Long> userId,
                                 @RequestParam("tab") Optional<Long> tab
        *
        * */

        //System.out.println("Hello from register");

        /*if (userId.isPresent() &&
                (user.getPassword() == null || user.getPassword().isEmpty())
                && (user.getPasswordConfirm() == null || user.getPasswordConfirm().isEmpty())) {
            user.setNotValidatePassword(true);
        }*/

        //userId.ifPresent(user::setId);
        mUserValidator.validate(user, bindingResult);
        ArrayList<String> errorMsgs = new ArrayList<>();

        if (bindingResult.hasErrors()) {
            List<ObjectError> errors = bindingResult.getAllErrors();
            boolean isNotEmptyError = false;
            boolean isUsernameAlsoSet = false;
            boolean isPasswordLengthError = false;
            boolean isPasswordNotSame = false;
            for (ObjectError objectError : errors) {
                if (objectError == null || objectError.getCode() == null) {
                    continue;
                }

                if (objectError.getCode().equals("NotEmpty")) {
                    isNotEmptyError = true;
                }

                if (objectError.getCode().equals("Duplicate.userForm.username")) {
                    isUsernameAlsoSet = true;
                }

                if (objectError.getCode().equals("Size.userForm.password")) {
                    isPasswordLengthError = true;
                }

                if (objectError.getCode().equals("Diff.userForm.passwordConfirm")) {
                    isPasswordNotSame = true;
                }
            }
            if (isNotEmptyError) {
                errorMsgs.add("Please fill in all required fields");
            }
            if (isUsernameAlsoSet) {
                errorMsgs.add("This username is already busy");
            }
            if (isPasswordLengthError) {
                errorMsgs.add("Password length should be between 4 and 32 characters");
            }
            if (isPasswordNotSame) {
                errorMsgs.add("Passwords do not match");
            }


            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("user", user);
            modelAndView.addObject("errors", errorMsgs);
            //userId.ifPresent(aLong -> modelAndView.addObject("userId", aLong));
            modelAndView.addObject("isAdmin", mUserHelper.isAdmin());
            modelAndView.addObject("isAdmin", mUserHelper.isModerator());
            modelAndView.addObject("title", "User editing");
            modelAndView.addObject("name", XUtils.getCurrentUserName(mUserHelper.getCurrentUser()));
            modelAndView.setViewName("register");
            return modelAndView;
        }

       /* if (userId.isPresent()) {
            Optional<User> userOptional = mUserService.findUserById(userId.get());
            if (userOptional.isPresent()) {
                User userDB = userOptional.get();

                user.setId(userDB.getId());
                user.getUserInfo().setId(userDB.getUserInfo().getId());
                user.setLastLogin(userDB.getLastLogin());
                user.setUpdatedAt(new Date());
                user.setCreatedAt(userDB.getCreatedAt());

                if (user.getNotValidatePassword() != null && user.getNotValidatePassword()) {
                    user.setPassword(userDB.getPassword());
                }

            }
        }
*/
        Role role = mUserService.findRoleByName(user.getSelectedRoleName());
        if (role == null) {
            errorMsgs.add("User role not selected, or wrong role selected");
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("user", user);
            modelAndView.addObject("errors", errorMsgs);
            modelAndView.addObject("title", "User editing");
            modelAndView.addObject("name", XUtils.getCurrentUserName(mUserHelper.getCurrentUser()));

            modelAndView.setViewName("register");
            return modelAndView;
        }


        user.setRole(role);
        user.setEnabled(true);

        UserInfo userInfo = mUserService.saveUserInfo(user.getUserInfo());
        user.setUserInfo(userInfo);

        String password = user.getPassword();
        mUserService.save(user);

        ModelAndView modelAndView = new ModelAndView();

        securityConfig.doLogin(user.getUsername(), password);

        modelAndView.setViewName("redirect:/member/welcome");
        return modelAndView;
    }

    @RequestMapping(value = {"/admin/users"}, method = RequestMethod.POST)
    public ModelAndView usersPost(@Valid @ModelAttribute("bindingMap") BindingMap bindingMap) {
        ModelAndView modelAndView = new ModelAndView();

        if (bindingMap.getPageAdmin() != null) {
            modelAndView.setViewName("redirect:/admin/users?tab=1&pageAdmin=" + bindingMap.getPageAdmin());
        } else if (bindingMap.getPageModerator() != null) {
            modelAndView.setViewName("redirect:admin/users?tab=5&PageModerator=" + bindingMap.getPageModerator());
        } else if (bindingMap.getPageMember() != null) {
            modelAndView.setViewName("redirect:/admin/users?tab=3&pageMember=" + bindingMap.getPageMember());
        } else {
            modelAndView.setViewName("redirect:/admin/users");
        }

        return modelAndView;
    }


    @RequestMapping(value = {"/admin/viewUser"}, method = RequestMethod.GET)
    public ModelAndView viewUser(@RequestParam("userId") Optional<Long> userId) {
        if (!userId.isPresent()) {
            return MainController.getErrorPage("Օգտվողի ID-ն բացակայում է");
        }

        Optional<User> userOpt = mUserService.findUserById(userId.get());
        if (!userOpt.isPresent()) {
            return MainController.getErrorPage("Նշված ID-ով օգտվող գոյություն չունի");
        }

        User user = userOpt.get();
        user.setSelectedRoleName(user.getRole().getName());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", user);
        modelAndView.addObject("userId", userId.get());
        modelAndView.addObject("title", "Օգտվողի մանրամասներ");
        modelAndView.addObject("name", XUtils.getCurrentUserName(mUserHelper.getCurrentUser()));


        modelAndView.setViewName("view_user");

        return modelAndView;
    }


    @Secured({Diciton.ROLE_ADMIN, Diciton.ROLE_MODERATOR})
    @RequestMapping(value = {"/admin/editUser"}, method = RequestMethod.GET)
    public ModelAndView editUser(@RequestParam("userId") Optional<Long> userId,
                                 @RequestParam("tab") Optional<Long> tab) {
        if (!userId.isPresent()) {
            return MainController.getErrorPage("Օգտվողի ID-ն բացակայում է");
        }

        Optional<User> userOpt = mUserService.findUserById(userId.get());
        if (!userOpt.isPresent()) {
            return MainController.getErrorPage("Նշված ID-ով օգտվող գոյություն չունի");
        }

        User user = userOpt.get();

        user.setSelectedRoleName(user.getRole().getName());


        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", user);
        modelAndView.addObject("isModerator", mUserHelper.isModerator());
        modelAndView.addObject("isMember", mUserHelper.isMember());
        modelAndView.addObject("userId", userId.get());
        modelAndView.addObject("isAdmin", mUserHelper.isAdmin());
        modelAndView.addObject("title", "Օգտվողի խմբագրում");
        modelAndView.addObject("name", XUtils.getCurrentUserName(mUserHelper.getCurrentUser()));
        modelAndView.addObject("tab", tab.orElse(1L));


        modelAndView.setViewName("add_user");

        return modelAndView;
    }


    @Secured({Diciton.ROLE_ADMIN, Diciton.ROLE_MODERATOR})
    @RequestMapping(value = {"/admin/deleteUser"}, method = RequestMethod.POST)
    public ModelAndView deleteUser(@RequestParam("userId") Optional<Long> userId,
                                   @RequestParam("tab") Optional<Integer> tab) {
        if (!userId.isPresent()) {
            return MainController.getErrorPage("Օգտվողի ID-ն բացակայում է");
        }

        Optional<User> user = mUserService.findUserById(userId.get());
        if (!user.isPresent()) {
            return MainController.getErrorPage("Նշված ID-ով օգտվող գոյություն չունի");
        }

        mUserService.deleteUser(user.get());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("tab", tab.orElse(1));
        modelAndView.setViewName("redirect:/admin/users");

        return modelAndView;
    }

    @RequestMapping(value = {"/member/welcome"}, method = RequestMethod.POST)
    public ModelAndView memberPost(@Valid @ModelAttribute("bindingMap") BindingMap bindingMap) {
        ModelAndView modelAndView = new ModelAndView();


        return modelAndView;
    }

    @Secured({Diciton.ROLE_ADMIN, Diciton.ROLE_MODERATOR})
    @RequestMapping(value = {"/admin/register"}, method = RequestMethod.GET)
    public ModelAndView register(@RequestParam("regAdmin") Optional<Long> regAdmin,
                                 @RequestParam("regMember") Optional<Long> regMember,
                                 @RequestParam("tab") Optional<Long> tab) {
        ModelAndView modelAndView = new ModelAndView();
        User newUser = new User();
        newUser.setUserInfo(new UserInfo());

        if (regAdmin.isPresent() && regAdmin.get() == 1) {
            newUser.setSelectedRoleName(Diciton.ROLE_ADMIN);
        }

        if (regMember.isPresent() && regMember.get() == 1) {
            newUser.setSelectedRoleName(Diciton.ROLE_MEMBER);
        }


        modelAndView.addObject("user", newUser);
        modelAndView.addObject("isAdmin", mUserHelper.isAdmin());
        modelAndView.addObject("title", "Գրանցել նոր օգտվող");
        modelAndView.addObject("name", XUtils.getCurrentUserName(mUserHelper.getCurrentUser()));
        modelAndView.addObject("tab", tab.orElse(1L));

        modelAndView.setViewName("add_user");

        return modelAndView;
    }

    @Secured({Diciton.ROLE_ADMIN, Diciton.ROLE_MODERATOR})
    @RequestMapping(value = {"/admin/register"}, method = RequestMethod.POST)
    public ModelAndView register(@Valid @ModelAttribute("user") User user,
                                 BindingResult bindingResult, Model model,
                                 @RequestParam("userId") Optional<Long> userId,
                                 @RequestParam("tab") Optional<Long> tab) {

        if (userId.isPresent() &&
                (user.getPassword() == null || user.getPassword().isEmpty())
                && (user.getPasswordConfirm() == null || user.getPasswordConfirm().isEmpty())) {
            user.setNotValidatePassword(true);
        }

        userId.ifPresent(user::setId);
        mUserValidator.validate(user, bindingResult);
        ArrayList<String> errorMsgs = new ArrayList<>();

        if (bindingResult.hasErrors()) {
            List<ObjectError> errors = bindingResult.getAllErrors();
            boolean isNotEmptyError = false;
            boolean isUsernameAlsoSet = false;
            boolean isPasswordLengthError = false;
            boolean isPasswordNotSame = false;
            for (ObjectError objectError : errors) {
                if (objectError == null || objectError.getCode() == null) {
                    continue;
                }

                if (objectError.getCode().equals("NotEmpty")) {
                    isNotEmptyError = true;
                }

                if (objectError.getCode().equals("Duplicate.userForm.username")) {
                    isUsernameAlsoSet = true;
                }

                if (objectError.getCode().equals("Size.userForm.password")) {
                    isPasswordLengthError = true;
                }

                if (objectError.getCode().equals("Diff.userForm.passwordConfirm")) {
                    isPasswordNotSame = true;
                }
            }
            if (isNotEmptyError) {
                errorMsgs.add("Խնդրում ենք լրացնել բոլոր պարտադիր դաշտերը");
            }
            if (isUsernameAlsoSet) {
                errorMsgs.add("Տվյալ օգտվողի անունը արդեն զբաղված է");
            }
            if (isPasswordLengthError) {
                errorMsgs.add("Գաղտնաբառի երկարությունը պետք է լինի 8 և 32 սիմվոլների մեջ");
            }
            if (isPasswordNotSame) {
                errorMsgs.add("Գաղտնաբառերը չեն համընկնում");
            }


            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("user", user);
            modelAndView.addObject("errors", errorMsgs);
            userId.ifPresent(aLong -> modelAndView.addObject("userId", aLong));
            modelAndView.addObject("isAdmin", mUserHelper.isAdmin());
            modelAndView.addObject("title", "Օգտվողի խմբագրում");
            modelAndView.addObject("name", XUtils.getCurrentUserName(mUserHelper.getCurrentUser()));
            modelAndView.addObject("tab", tab.orElse(1L));
            modelAndView.setViewName("add_user");
            return modelAndView;
        }

        if (userId.isPresent()) {
            Optional<User> userOptional = mUserService.findUserById(userId.get());
            if (userOptional.isPresent()) {
                User userDB = userOptional.get();

                user.setId(userDB.getId());
                user.getUserInfo().setId(userDB.getUserInfo().getId());
                user.setLastLogin(userDB.getLastLogin());
                user.setUpdatedAt(new Date());
                user.setCreatedAt(userDB.getCreatedAt());

                if (user.getNotValidatePassword() != null && user.getNotValidatePassword()) {
                    user.setPassword(userDB.getPassword());
                }

            }
        }

        Role role = mUserService.findRoleByName(user.getSelectedRoleName());
        if (role == null) {
            errorMsgs.add("Օգտվողի դերը ընտրված չէ, կամ ընտրված է սխալ դեր");
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("user", user);
            modelAndView.addObject("errors", errorMsgs);
            modelAndView.addObject("title", "Օգտվողի խմբագրում");
            modelAndView.addObject("name", XUtils.getCurrentUserName(mUserHelper.getCurrentUser()));

            modelAndView.setViewName("add_user");
            return modelAndView;
        }

        user.setRole(role);
        user.setEnabled(true);

        UserInfo userInfo = mUserService.saveUserInfo(user.getUserInfo());
        user.setUserInfo(userInfo);
        user.setNotValidatePassword(null);

        mUserService.save(user);

        ModelAndView modelAndView = new ModelAndView();

        String tabStr = "admin";
        if (tab.isPresent()) {
            if (tab.get() == 2L) {
                tabStr = "moderator";
            } else if (tab.get() == 3L) {
                tabStr = "member";
            }
        }

        modelAndView.setViewName("redirect:/admin/users#" + tabStr);
        return modelAndView;
    }

    @Secured({Diciton.ROLE_ADMIN, Diciton.ROLE_MODERATOR, Diciton.ROLE_MEMBER})
    @RequestMapping(value = {"/change_info"}, method = RequestMethod.GET)
    public ModelAndView changeInfo(@RequestParam("regAdmin") Optional<Long> regAdmin,
                                   @RequestParam("regMember") Optional<Long> regMember,
                                   @RequestParam("regModerator") Optional<Long> regModerator,
                                   @RequestParam("tab") Optional<Long> tab) {


        ModelAndView modelAndView = new ModelAndView();
        User currentUser = mUserHelper.getCurrentUser();

        modelAndView.addObject("user", currentUser);
        modelAndView.addObject("userId", currentUser.getId());
        modelAndView.addObject("isMember", mUserHelper.isMember());
        modelAndView.addObject("title", "Change info");
        modelAndView.addObject("name", XUtils.getCurrentUserName(mUserHelper.getCurrentUser()));

        modelAndView.setViewName("change_info");

        return modelAndView;
    }

    @Secured({Diciton.ROLE_ADMIN, Diciton.ROLE_MODERATOR, Diciton.ROLE_MEMBER})
    @RequestMapping(value = {"/change_info"}, method = RequestMethod.POST)
    public ModelAndView changeInfo(@Valid @ModelAttribute("user") User user,
                                   BindingResult bindingResult, Model model,
                                   @RequestParam("userId") Optional<Long> userId) {

        userId.ifPresent(user::setId);
        mUserInfoValidator.validate(user, bindingResult);
        ArrayList<String> errorMsgs = new ArrayList<>();

        if (bindingResult.hasErrors()) {
            List<ObjectError> errors = bindingResult.getAllErrors();
            boolean isNotEmptyError = false;
            for (ObjectError objectError : errors) {
                if (objectError == null || objectError.getCode() == null) {
                    continue;
                }

                if (objectError.getCode().equals("NotEmpty")) {
                    isNotEmptyError = true;
                }
            }
            if (isNotEmptyError) {
                errorMsgs.add("Խնդրում ենք լրացնել բոլոր պարտադիր դաշտերը");
            }
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("user", user);
            modelAndView.addObject("errors", errorMsgs);
            modelAndView.addObject("isMember", mUserHelper.isMember());
            userId.ifPresent(aLong -> modelAndView.addObject("userId", aLong));
            modelAndView.addObject("title", "Change info");
            modelAndView.addObject("name", XUtils.getCurrentUserName(mUserHelper.getCurrentUser()));
            modelAndView.setViewName("change_info");
            return modelAndView;
        }


        if (userId.isPresent()) {
            Optional<User> userOptional = mUserService.findUserById(userId.get());
            if (userOptional.isPresent()) {
                User userDB = userOptional.get();
                UserInfo userInfo = mUserService.saveUserInfo(user.getUserInfo());
                userDB.setUserInfo(userInfo);
                userDB.setNotValidatePassword(true);
                userDB.setUpdatedAt(new Date());
                mUserService.save(userDB);
            }
        }
        ModelAndView modelAndView = new ModelAndView();

        if (mUserHelper.isAdmin() || mUserHelper.isModerator()) {
            modelAndView.setViewName("redirect:/admin/users");
        } else {
            modelAndView.setViewName("redirect:/member/welcome");
        }

        return modelAndView;
    }


    @Secured({Diciton.ROLE_ADMIN, Diciton.ROLE_MODERATOR, Diciton.ROLE_MEMBER})
    @RequestMapping(value = {"/change_password"}, method = RequestMethod.GET)
    public ModelAndView changePassword(@RequestParam("regAdmin") Optional<Long> regAdmin,
                                       @RequestParam("regMember") Optional<Long> regMember) {
        ModelAndView modelAndView = new ModelAndView();
        User currentUser = mUserHelper.getCurrentUser();


        modelAndView.addObject("user", currentUser);
        modelAndView.addObject("userId", currentUser.getId());
        modelAndView.addObject("isMember", mUserHelper.isMember());
        modelAndView.addObject("title", "change password");
        modelAndView.addObject("name", XUtils.getCurrentUserName(mUserHelper.getCurrentUser()));

        modelAndView.setViewName("change_password");

        return modelAndView;
    }


    @RequestMapping(value = {"/change_password"}, method = RequestMethod.POST)
    public ModelAndView changePassword(@Valid @ModelAttribute("user") User user,
                                       BindingResult bindingResult, Model model,
                                       @RequestParam("userId") Optional<Long> userId) {


        userId.ifPresent(user::setId);
        mUserPasswordValidator.validate(user, bindingResult);
       /* boolean isOldPasswordNotSame = bCryptPasswordEncoder.matches(user.getOldPassword(),user.getPassword());

        if(isOldPasswordNotSame == false){
            List<ObjectError> errors = bindingResult.getAllErrors();
            errors.errorMsgs.add("Գաղտնաբառերը չեն համընկնում");
        }*/

        //user.setOldPassword()(bCryptPasswordEncoder.encode(user.getOldPassword()));

        boolean isPasswordRight = false;
        if (userId.isPresent()) {
            Optional<User> userOptional = mUserService.findUserById(userId.get());
            User userdb = userOptional.get();
            String oldPassEncoded = userdb.getPassword();
            if (user.getOldPassword() != null) {
                String enteredPassword = bCryptPasswordEncoder.encode(user.getOldPassword());
                isPasswordRight = bCryptPasswordEncoder.matches(user.getOldPassword(),userdb.getPassword());
            }
        }
        ArrayList<String> errorMsgs = new ArrayList<>();

        if (bindingResult.hasErrors() || !isPasswordRight) {
            List<ObjectError> errors = bindingResult.getAllErrors();
            boolean isNotEmptyError = false;
            boolean isUsernameAlsoSet = false;
            boolean isPasswordLengthError = false;
            boolean isPasswordNotSame = false;
            boolean isPasswordSame = false;
           // boolean isOldPasswordNotSame = false;

            for (ObjectError objectError : errors) {
                if (objectError == null || objectError.getCode() == null) {
                    continue;
                }

                if (objectError.getCode().equals("NotEmpty")) {
                    isNotEmptyError = true;
                }

                if (objectError.getCode().equals("Duplicate.userForm.username")) {
                    isUsernameAlsoSet = true;
                }

                if (objectError.getCode().equals("Size.userForm.password")) {
                    isPasswordLengthError = true;
                }

                if (objectError.getCode().equals("Diff.userForm.passwordConfirm")) {
                    isPasswordNotSame = true;
                }

                if (objectError.getCode().equals("Diff.userForm.passwordSame")) {
                    isPasswordSame = true;
                }


            }
            if (isNotEmptyError) {
                errorMsgs.add("Խնդրում ենք լրացնել բոլոր պարտադիր դաշտերը");
            }
            if (isUsernameAlsoSet) {
                errorMsgs.add("Տվյալ օգտվողի անունը արդեն զբաղված է");
            }
            if (isPasswordLengthError) {
                errorMsgs.add("Գաղտնաբառի երկարությունը պետք է լինի 8 և 32 սիմվոլների մեջ");
            }
            if (isPasswordNotSame) {
                errorMsgs.add("Գաղտնաբառերը չեն համընկնում");
            }
            if (isPasswordSame) {
                errorMsgs.add("Գաղտնաբառերը համընկնում են");
            }
            if (!isPasswordRight) {
                errorMsgs.add("Մուտքագրված հին գաղտնաբառը սխալ է");
            }

            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("user", user);
            modelAndView.addObject("errors", errorMsgs);
            userId.ifPresent(aLong -> modelAndView.addObject("userId", aLong));
            modelAndView.addObject("title", "change password");
            modelAndView.addObject("isMember", mUserHelper.isMember());
            modelAndView.addObject("name", XUtils.getCurrentUserName(mUserHelper.getCurrentUser()));
            modelAndView.setViewName("change_password");
            return modelAndView;
        }

        if (userId.isPresent()) {
            Optional<User> userOptional = mUserService.findUserById(userId.get());
            if (userOptional.isPresent()) {
                User userDB = userOptional.get();
                userDB.setUpdatedAt(new Date());
                userDB.setUsername(user.getUsername());
                userDB.setPassword(user.getNewPassword());
                //userDB.setNotValidatePassword(null);
                mUserService.save(userDB);
            }
        }
        ModelAndView modelAndView = new ModelAndView();

        if (mUserHelper.isAdmin() || mUserHelper.isModerator()) {
            modelAndView.setViewName("redirect:/admin/users");
        } else {
            modelAndView.setViewName("redirect:/member/welcome");
        }

        return modelAndView;
    }

}
