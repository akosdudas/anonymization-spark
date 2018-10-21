package com.anonymization.controllers;

import com.anonymization.model.MedicalRecord;
import com.anonymization.model.MedicalRecordWrapper;
import com.anonymization.services.MedicalRecordService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class MedicalRecordController extends BaseController {

    @Autowired
    private MedicalRecordService service;

    @RequestMapping(value = "/medical_data", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<Object> save(@RequestBody MedicalRecord data) {
        MedicalRecord one = service.save(data);
        return new ResponseEntity<>(one, HttpStatus.CREATED);
    }

    //
//    @RequestMapping(value="/medical_data",method = RequestMethod.POST,produces = "application/json")
//    public ResponseEntity<List<Object>> saveAll(@RequestBody List<MedicalRecord> medicalRecords){
//        List<MedicalRecord> added=service.saveAll(medicalRecords);
//        return new ResponseEntity<List<Object>>(Collections.singletonList(added),HttpStatus.CREATED);
//    }
    @RequestMapping(method = RequestMethod.POST, value = "/batch", consumes = "application/json", produces = "application/json")
    public @ResponseBody
    ResponseEntity<?> savePersonList(@RequestBody Resource<MedicalRecordWrapper> medicalRecordWrapper
                                    ) {
        Resources<MedicalRecord> resources = new Resources<MedicalRecord>(service.saveAll(medicalRecordWrapper.getContent().getContent()));
        //TODO add extra links `assembler`
        return ResponseEntity.ok(resources);
    }

    @RequestMapping(value = "/medical_data", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Object> findAll() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/medical_data/id/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Object> findOne(@PathVariable int id) {
        Optional<MedicalRecord> medicalData = service.findOne(id);
        return new ResponseEntity<>(medicalData == null ? Collections.EMPTY_MAP : medicalData, HttpStatus.OK);
    }

    @RequestMapping(value = "/medical_data/id/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<Object> delete(@PathVariable int id) {
        service.delete(id);
        return new ResponseEntity<>(Collections.EMPTY_MAP, HttpStatus.OK);
    }

    @RequestMapping(value = "/medical_data/deleteAll", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<Object> deleteAll() {
        service.deleteAll();
        return new ResponseEntity<>(Collections.EMPTY_MAP, HttpStatus.OK);
    }

    @RequestMapping(value = "/medical_data/count", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Object> count() {
        ObjectNode node = createObjectNode();
        node.put("count", service.count());
        return new ResponseEntity<>(node, HttpStatus.OK);
    }

}
