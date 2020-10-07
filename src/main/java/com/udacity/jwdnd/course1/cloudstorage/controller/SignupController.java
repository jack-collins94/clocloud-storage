package com.udacity.jwdnd.course1.cloudstorage.controller;


import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import com.udacity.jwdnd.course1.cloudstorage.services.model.SignupUserRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/signup")
public class SignupController {

    private final UserService userService;

    public SignupController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String signupView(){return "signup";}

    @PostMapping
    public String signupUser(@ModelAttribute SignupUserRequest signupUserRequest, Model model) {
        try {
            userService.createUser(signupUserRequest);
            model.addAttribute("signupSuccess",true);
        } catch (UserService.UserServiceException e) {
            model.addAttribute("signupError", e.getMessage());
        }

        return "signup";
    }


    // latered architecture
// controller (http, make the service call, tranlate error back into http status codes or redirect to another page)
// -> service (use cases , busines logic, possibly throw an error if something fails)
// -> data access/repository/ data access object / resource (mapper)  (bring data into and out of the system)

    // modelor data transfer object
    // domain model object (store data to and from the database )


}

