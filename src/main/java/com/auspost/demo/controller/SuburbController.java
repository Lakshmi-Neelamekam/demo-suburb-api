package com.auspost.demo.controller;

import com.auspost.demo.model.SuburbDetails;
import com.auspost.demo.service.SuburbService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/api/v1/suburbs")
@EnableResourceServer
public class SuburbController {
    @Autowired
    private SuburbService service;

    @RequestMapping(params = "postCode", method = GET)
    public ResponseEntity<SuburbDetails> getSuburbByPostCode(@RequestParam Integer postCode) {
        SuburbDetails suburbByPostCode = service.getSuburbByPostCode(postCode);
        if (suburbByPostCode != null) {
            return new ResponseEntity<>(suburbByPostCode, OK);
        } else {
            return new ResponseEntity<>(NOT_FOUND);
        }
    }

    @RequestMapping(params = "name", method = GET)
    public ResponseEntity<SuburbDetails> getSuburbByName(@RequestParam String name) {
        SuburbDetails suburbByName = service.getSuburbByName(name);
        if (suburbByName != null) {
            return new ResponseEntity<>(suburbByName, OK);
        } else {
            return new ResponseEntity<>(NOT_FOUND);
        }
    }

    @PostMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<SuburbDetails> addSuburb(@RequestBody SuburbDetails suburb) {
        return new ResponseEntity<>(service.addSuburb(suburb), HttpStatus.CREATED);
    }
}
