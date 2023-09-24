package com.demo.demandfarm.uploaddata;

import com.demo.demandfarm.uploaddata.service.SaveDataService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Validated
public class UploadController {

    @Autowired
    private transient SaveDataService saveDataService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
    value = "/upload")
    public ResponseEntity<Object> uploadFile(@RequestParam(name = "file")
                                                 @Valid @NotNull(message = "File cannot be null") final MultipartFile multipartFile){
        saveDataService.saveData(multipartFile);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
}
