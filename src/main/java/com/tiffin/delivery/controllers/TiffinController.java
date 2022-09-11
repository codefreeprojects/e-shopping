package com.tiffin.delivery.controllers;

import com.tiffin.delivery.dao.AddressDAO;
import com.tiffin.delivery.dao.TiffinPlanDAO;
import com.tiffin.delivery.dto.BasicResponseDTO;
import com.tiffin.delivery.dto.SaveTiffinPlanRequestDTO;
import com.tiffin.delivery.models.Address;
import com.tiffin.delivery.models.TiffinPlan;
import com.tiffin.delivery.services.FilesStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@CrossOrigin @RestController @RequestMapping("/api/tiffin")
public class TiffinController {
    @Autowired
    TiffinPlanDAO tiffinPlanDAO;
    @Value("${files.upload.url}")
    private String filesUrl;
    @Autowired
    FilesStorageService storageService;

    @Autowired
    AddressDAO addressDAO;

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/save", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<BasicResponseDTO<TiffinPlan>> saveTiffinPlan(@ModelAttribute SaveTiffinPlanRequestDTO r){
        TiffinPlan tiffinPlan = new TiffinPlan();
        Optional<Address> _add = addressDAO.findById(r.getAddressId());
        if(_add.isPresent()){
            tiffinPlan.setAddress(_add.get());
        }
        tiffinPlan.setPlanName(r.getPlanName());
        tiffinPlan.setDescription(r.getDescription());
        tiffinPlan.setPricePerDay(r.getPricePerDay());
        tiffinPlan.setCreatedOn(new Date());
        Optional<String> fileName =  storageService.save(r.getBannerImg());
        if(fileName.isPresent())
            tiffinPlan.setBannerUrl(filesUrl+fileName.get());
        tiffinPlanDAO.save(tiffinPlan);
        return new ResponseEntity<>(new BasicResponseDTO<>(true, "Record saved", tiffinPlan), HttpStatus.CREATED);

    }
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping(value = "/save/{tiffinId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<BasicResponseDTO<TiffinPlan>> updateTiffinPlan(@PathVariable(value = "tiffinId") Long tiffinId,@ModelAttribute SaveTiffinPlanRequestDTO r){
        Optional<TiffinPlan> _tiffinPlan = tiffinPlanDAO.findById(tiffinId);
        if(_tiffinPlan.isEmpty()){
            return new ResponseEntity<>(new BasicResponseDTO<>(false, "Record not found", null), HttpStatus.OK);
        }
        TiffinPlan tiffinPlan = _tiffinPlan.get();
        tiffinPlan.setPlanName(r.getPlanName());
        tiffinPlan.setDescription(r.getDescription());
        tiffinPlan.setPricePerDay(r.getPricePerDay());
        tiffinPlan.setCreatedOn(new Date());
        Optional<String> fileName =  storageService.save(r.getBannerImg());
        if(fileName.isPresent())
            tiffinPlan.setBannerUrl(filesUrl+fileName.get());
        tiffinPlanDAO.save(tiffinPlan);
        return new ResponseEntity<>(new BasicResponseDTO<>(true, "Record updated", tiffinPlan), HttpStatus.CREATED);
    }
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping( "/delete/{tiffinId}")
    public ResponseEntity<BasicResponseDTO<TiffinPlan>> updateTiffinPlan(@PathVariable(value = "tiffinId") Long tiffinId){
        tiffinPlanDAO.deleteById(tiffinId);
        return new ResponseEntity<>(new BasicResponseDTO<>(true, "Record deleted", null), HttpStatus.CREATED);
    }
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping( "/all")
    public ResponseEntity<BasicResponseDTO<List<TiffinPlan>>> getAllTiffinPlan(){
        List<TiffinPlan> tiffinPlans=  tiffinPlanDAO.findAll();
        return new ResponseEntity<>(new BasicResponseDTO<>(true, "Records", tiffinPlans), HttpStatus.CREATED);
    }

}
