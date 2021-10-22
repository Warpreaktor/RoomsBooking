package ru.application.calendar.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import ru.application.calendar.domain.entity.UserEntity;
import ru.application.calendar.service.impl.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class RegistrationController {
    private final UserServiceImpl userService;

    @GetMapping("/")
    public RedirectView homePage() {
        return new RedirectView("/calendar");
    }

    @GetMapping("/login")
    public String getLoginForm() {
        return "/users/login";
    }

    @GetMapping("/login/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "/users/login";
    }

    @GetMapping("/login/logout")
    public String logout(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            request.getSession().invalidate();
            return "redirect:/login";
        }
        return "redirect:/login";
    }

    @GetMapping("/login/registration")
    public String getRegistrationForm(Model model) {
        model.addAttribute("userForm", new UserEntity());
        model.addAttribute("userExists", true);
        return "/users/user-registration";
    }

    @PostMapping("/login/registration")
    public String registration(@ModelAttribute("userForm") @Valid UserEntity user,
                               BindingResult bindingResult,
                               Model model) {

        if (bindingResult.hasFieldErrors("password")) {
            model.addAttribute("passwordError", true);
            model.addAttribute("username", user.getUsername());
            return "/users/user-registration";
        }
        if (!user.getPassword().equals(user.getPasswordConfirm())) {
            model.addAttribute("passwordConfirmError", true);
            return "users/user-registration";
        }
        if (bindingResult.hasFieldErrors("email")) {
            model.addAttribute("emailError", true);
            return "users/user-registration";
        }
        if (userService.findByEmail(user.getEmail()).isPresent()){
            model.addAttribute("emailExistsError", true);
            return "users/user-registration";
        }
        if (userService.findByUsername(user.getUsername()).isPresent()) {
            model.addAttribute("usernameError", true);
            return "users/user-registration";
        }
        userService.saveUser(user);
        return "redirect:/login";
    }
}
