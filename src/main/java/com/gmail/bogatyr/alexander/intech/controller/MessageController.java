package com.gmail.bogatyr.alexander.intech.controller;

import com.gmail.bogatyr.alexander.intech.dto.MessageDTO;
import com.gmail.bogatyr.alexander.intech.service.MessageService;
import com.gmail.bogatyr.alexander.intech.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by Alexander Bogatyrenko on 24.08.16.
 * <p>
 * This class represents...
 */
@Controller
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute(new MessageDTO());
        return "message/messages";
    }

    @RequestMapping(value = "/message/find-all", method = RequestMethod.GET)
    @ResponseBody
    private List<MessageDTO> findAll() {
        return messageService.findAll();
    }

    @RequestMapping(value = "/message/send", method = RequestMethod.POST)
    public String sendMassage(@Valid MessageDTO messageDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("users", userService.findAll());
            return "message/messages";
        }
        messageService.create(messageDTO);
        return "redirect:/";
    }

    @RequestMapping(value = "/message/read/{id}", method = RequestMethod.GET)
    public String read(@PathVariable("id") Long id, Model model) {
        model.addAttribute("message", messageService.read(id));
        return "message/message-read";
    }

    @RequestMapping(value = "/message/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public MessageResponce deleteMessage(@PathVariable("id") Long id) {
        messageService.delete(id);
        return new MessageResponce("successJsMessage", "Message has been deleted successfully");
    }
}
