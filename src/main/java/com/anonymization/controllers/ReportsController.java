package com.anonymization.controllers;

import com.anonymization.model.Report;
import com.anonymization.model.ReportWrapper;
import com.anonymization.services.ReportService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api")
public class ReportsController extends BaseController {

    @Autowired
    private ReportService service;

    @RequestMapping( value = "/report_data", method = RequestMethod.POST,consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> save(@RequestBody Report data) {
        Report one = service.save(data);
        return new ResponseEntity<>(one, HttpStatus.CREATED);
    }


    @RequestMapping(method = RequestMethod.POST, value = "/reports", consumes = "application/json", produces = "application/json")
    public @ResponseBody
    ResponseEntity<?> savePersonList(@RequestBody Resource<ReportWrapper> reportWrapper
    ) {
        Resources<Report> resources = new Resources<Report>(service.saveAll(reportWrapper.getContent().getContent()));
        //TODO add extra links `assembler`
        return ResponseEntity.ok(resources);
    }


    @RequestMapping(value = "/report_data", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Object> findAll() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }



    @RequestMapping(value = "/report_data/id/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Object> findOne(@PathVariable int id) {
        Optional<Report> medicalData = service.findOne(id);
        return new ResponseEntity<>(medicalData == null ? Collections.EMPTY_MAP : medicalData, HttpStatus.OK);
    }

    @RequestMapping(value = "/report_data/id/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<Object> delete(@PathVariable int id) {
        service.delete(id);
        return new ResponseEntity<>(Collections.EMPTY_MAP, HttpStatus.OK);
    }

    @RequestMapping(value = "/report_data/deleteAll", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<Object> deleteAll() {
        service.deleteAll();
        return new ResponseEntity<>(Collections.EMPTY_MAP, HttpStatus.OK);
    }

    @RequestMapping(value = "/report_data/count", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Object> count() {
        ObjectNode node = createObjectNode();
        node.put("count", service.count());
        return new ResponseEntity<>(node, HttpStatus.OK);
    }

}
