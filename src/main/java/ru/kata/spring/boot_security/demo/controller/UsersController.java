package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UsersService;

import javax.validation.Valid;

@Controller
@RequestMapping("/users")
public class UsersController {

    private final UsersService usersService;

    @Autowired
    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping({"/", "/index"})
    public String index(Model model, @AuthenticationPrincipal User user) {
        if (user != null) {
            model.addAttribute("id", user.getId());
        }
        return "users/index";
    }

    @GetMapping("/user/{id}")
    public String showUser(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", usersService.show(id));
        return "users/show";
    }

    @GetMapping("/admin")
    public String showAdmin(Model model) {
        model.addAttribute("user", usersService.show(1L)); // Используем Long
        return "users/show";
    }

    @GetMapping({"admin/new", "admin/edit"})
    public String newPersonByAdmin(@ModelAttribute("user") User user) {
        return "users/new";
    }

    @GetMapping("/new")
    public String newUser(@ModelAttribute("user") User user) {
        return "users/new";
    }

    @PostMapping
    public String create(@ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "users/new";
        }
        usersService.save(user);

        // Аутентифицируем пользователя после регистрации
        User authUser = usersService.loadUserByUsername(user.getUsername());
        System.err.println("Зарегался пользователь" + authUser.getUsername() + "    " + authUser.getPassword());
        Authentication authentication = new UsernamePasswordAuthenticationToken(authUser, null, authUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "redirect:/users/user/" + user.getId(); // Перенаправляем на страницу пользователя
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", usersService.show(id));
        return "users/edit";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable("id") Long id, @ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "users/edit";
        }
        usersService.update(id, user);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        usersService.delete(id);
        return "redirect:/admin";
    }
}