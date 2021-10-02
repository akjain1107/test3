package com.assessment.mercedesreceiver.controller;


import com.assessment.mercedesreceiver.model.User;
import com.assessment.mercedesreceiver.services.XMLHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//{ name: “Hello”, dob: “20-08-2020”, salary: “122111241.150”, age: 20 }
@RestController
@RequestMapping("/file")
public class UserController {
    @Autowired
    private XMLHandler xmlHandler;

    @PostMapping("/store")
    public String store(@RequestBody User userDetails, @RequestHeader String fileType){
        System.out.println(userDetails);
        System.out.println(fileType);
        return xmlHandler.store(userDetails,fileType);
    }

    @PostMapping("/update")
    public void update(@RequestBody User userDetails){
    }

    @PostMapping("/read")
    public List<User> read(){
        List<User> userList = xmlHandler.read();
        return userList;
    }
}
