package com.space.controller;

import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ShipWithSpringDataController {

    @Autowired
    ShipRepository shipRepository;

}
