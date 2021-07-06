package com.example.demo.controllers;

import com.example.demo.models.Address;
import com.example.demo.models.School;
import com.example.demo.repository.AddressRepository;
import com.example.demo.repository.SchoolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

@Controller
public class ZoznamSkolController {

    @Autowired
    private SchoolRepository schoolRep;

    @Autowired
    private AddressRepository addressRep;
    @GetMapping("/zoznam_skol")
    public String ZoznamSkol(Model model) {
        Iterable<School> schools = schoolRep.findAll();
        model.addAttribute("Skoly", schools);
        return "zoznamSkol";
    }

    @GetMapping("/zoznam_skol/pridat_skolu")
    public String pridat_skolu(Model model) {
        model.addAttribute("title", "Pridat Skolu");
        return "pridat_skolu";
    }
    @PostMapping("/zoznam_skol/pridat_skolu")
    public String pridat_skolu_do_db(@RequestParam String name,
                                     @RequestParam String street,
                                     @RequestParam String village,
                                     @RequestParam String zip,
                                     @RequestParam String district,
                                     @RequestParam String region,
                                     @RequestParam double footprint,
                                     @RequestParam int year,
                                     Model model) {
        School school = new School(name,year,district,region, footprint);
        Address address = new Address(school.getId(), street, village, zip);
        schoolRep.save(school);
        addressRep.save(address);
        return "pridat_skolu";
    }

    @GetMapping("/zoznam_skol/{id}")
    public String skolaPodrobnosti(@PathVariable(value = "id") long id, Model model) {
        if(schoolRep.existsById(id) && addressRep.existsById(id+1)) {
            Optional<School> school = schoolRep.findById(id);
            Optional<Address> address = addressRep.findById(id + 1);
            ArrayList<School> resultSchool = new ArrayList<>();
            ArrayList<Address> resultAddress = new ArrayList<>();
            school.ifPresent(resultSchool::add);
            address.ifPresent(resultAddress::add);

            model.addAttribute("school", resultSchool);
            model.addAttribute("address", resultAddress);
            return "skola_podrobnosti";
        } else {
            Iterable<School> schools = schoolRep.findAll();
            model.addAttribute("Skoly", schools);
            return "zoznamSkol";
        }
    }


    @GetMapping("/zoznam_skol/{id}/edit")
    public String skolaEdit(@PathVariable(value = "id") long id, Model model) {
        if(schoolRep.existsById(id) && addressRep.existsById(id+1)) {
            Optional<School> school = schoolRep.findById(id);
            Optional<Address> address = addressRep.findById(id + 1);
            ArrayList<School> resultSchool = new ArrayList<>();
            ArrayList<Address> resultAddress = new ArrayList<>();
            school.ifPresent(resultSchool::add);
            address.ifPresent(resultAddress::add);

            model.addAttribute("school", resultSchool);
            model.addAttribute("address", resultAddress);
            return "skolaEdit";
        } else {
            
            return "redirect:/zoznam_skol";
        }
    }


    @PostMapping("/zoznam_skol/{id}/edit")
    public String zoznamSkolUpdate(@PathVariable(value = "id") long id,
                                     @RequestParam String name,
                                     @RequestParam String street,
                                     @RequestParam String village,
                                     @RequestParam String zip,
                                     @RequestParam String district,
                                     @RequestParam String region,
                                     @RequestParam double footprint,
                                     @RequestParam int year,
                                     Model model) {
        School school = schoolRep.findById(id).orElseThrow();
        Address address =addressRep.findById(id+1).orElseThrow();
        school.setDistrict(district);
        school.setFootprint(footprint);
        school.setName(name);
        school.setRegion(region);
        school.setYear(year);

        address.setStreet(street);
        address.setVillage(village);
        address.setZip(zip);

        schoolRep.save(school);
        addressRep.save(address);
        return "redirect:/zoznam_skol";
    }


    @PostMapping("/zoznam_skol/{id}/remove")
    public String zoznamSkolDelete(@PathVariable(value = "id") long id,
                                   Model model) {
        School school = schoolRep.findById(id).orElseThrow();
        Address address =addressRep.findById(id+1).orElseThrow();

        addressRep.delete(address);
        schoolRep.delete(school);

        return "redirect:/zoznam_skol";
    }




}
