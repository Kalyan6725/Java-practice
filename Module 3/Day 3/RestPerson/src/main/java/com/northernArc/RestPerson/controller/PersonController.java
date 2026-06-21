package com.northernArc.RestPerson.controller;

import com.northernArc.RestPerson.service.PersonServiceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/persons")
public class PersonController {
    @Autowired
    PersonServiceDao personServiceDao;




}
