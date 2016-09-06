package com.gmail.bogatyr.alexander.intech.controller;

import com.gmail.bogatyr.alexander.intech.domain.Authority;
import com.gmail.bogatyr.alexander.intech.domain.User;
import com.gmail.bogatyr.alexander.intech.dto.UserDTO;
import com.gmail.bogatyr.alexander.intech.dto.UserDTO.ChangePass;
import com.gmail.bogatyr.alexander.intech.dto.UserDTO.CreateUser;
import com.gmail.bogatyr.alexander.intech.dto.UserDTO.Registration;
import com.gmail.bogatyr.alexander.intech.dto.UserDTO.UpdateUser;
import com.gmail.bogatyr.alexander.intech.repository.AuthorityRepository;
import com.gmail.bogatyr.alexander.intech.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

/**
 * Created by Alexander Bogatyrenko on 21.08.16.
 * <p>
 * This class represents...
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorityRepository authorityRepository;

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!isNull(auth)) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registration(Model model) {
        model.addAttribute(new UserDTO());
        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registration(@Validated(Registration.class) UserDTO userDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        boolean registration = userService.registration(userDTO);
        if (registration) {
            return "redirect:/";
        } else {
            model.addAttribute("errorMessage", "Registration failed, user with login - " + userDTO.getLogin() + " exists");
            return "registration";
        }
    }

    @RequestMapping(value = "/user/change-pass", method = RequestMethod.GET)
    public String changePass(Model model) {
        model.addAttribute(new UserDTO());
        return "change-pass";
    }

    @RequestMapping(value = "/user/change-pass", method = RequestMethod.POST)
    public String changePass(@Validated(ChangePass.class) UserDTO userDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "redirect:/";
        }
        User user = userService.changePass(userDTO);
        if (isNull(user)) {
            redirectAttributes.addFlashAttribute("errorRedirectMessage",
                    "Password can not be updated, you have entered an incorrect current password");
        } else {
            redirectAttributes.addFlashAttribute("successRedirectMessage", "Password has been updated successfully");
        }
        return "redirect:/";
    }

    @RequestMapping(value = "/user-management", method = RequestMethod.GET)
    public String userManagement(Model model) {
        model.addAttribute(new UserDTO());
        return "user/users";
    }

    @RequestMapping(value = "/user/find-all", method = RequestMethod.GET)
    @ResponseBody
    public List<UserDTO> users() {
        return userService.findAll();
    }

    @RequestMapping(value = "/user/create", method = RequestMethod.POST)
    public String create(@Validated(CreateUser.class) UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user/users";
        }
        userService.create(userDTO);
        return "redirect:/user-management";
    }

    @RequestMapping(value = "/user/read/{id}", method = RequestMethod.GET)
    public String read(@PathVariable("id") Long id, Model model) {
        UserDTO userDTO = userService.read(id);
        model.addAttribute(userDTO);
        model.addAttribute("authorities", StringUtils.join(userDTO.getAuthorities(), ","));
        return "user/user-read";
    }

    @RequestMapping(value = "/user/update/{id}", method = RequestMethod.GET)
    public String update(@PathVariable("id") Long id, Model model) {
        model.addAttribute(userService.read(id));
        List<Authority> authorities = authorityRepository.findAll();
        model.addAttribute("authorities", authorities.stream().map(Authority::getName).collect(Collectors.toList()));
        return "user/user-edit";
    }

    @RequestMapping(value = "/user/update", method = RequestMethod.POST)
    public String update(@Validated(UpdateUser.class) UserDTO userDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            sb.append("User can not be updated \n");
            bindingResult.getAllErrors().forEach(e -> sb
                    .append(((FieldError) e).getField()).append(" - ").append(e.getDefaultMessage()).append("\n"));
            redirectAttributes.addFlashAttribute("errorRedirectMessage", sb.toString());
            return "redirect:/user-management";
        }
        User user = userService.update(userDTO);
        if (isNull(user)) {
            redirectAttributes.addFlashAttribute("errorRedirectMessage", "User has not been updated");
        } else {
            redirectAttributes.addFlashAttribute("successRedirectMessage", "User has been updated successfully");
        }
        return "redirect:/user-management";
    }

    @RequestMapping(value = "/user/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public MessageResponse delete(@PathVariable("id") Long id) {
        boolean delete = userService.delete(id);
        if (!delete) {
            return new MessageResponse("errorJsMessage", "User could not be deleted, user has links with messages");
        } else {
            return new MessageResponse("successJsMessage", "User has been deleted successfully");
        }
    }
}
