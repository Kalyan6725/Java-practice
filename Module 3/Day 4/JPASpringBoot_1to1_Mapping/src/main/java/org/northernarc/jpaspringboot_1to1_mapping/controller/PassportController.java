package org.northernarc.jpaspringboot_1to1_mapping.controller;
import org.northernarc.jpaspringboot_1to1_mapping.entity.Passport;
import org.northernarc.jpaspringboot_1to1_mapping.service.PassportServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/passports")
public class PassportController {
    @Autowired
    PassportServiceImpl passportService;
    @PostMapping
    public Passport addPassport(@RequestBody Passport passport){
        return passportService.addPassport(passport);
    }
    @GetMapping
    public List<Passport> getAll(){
        return passportService.getAll();
    }
}