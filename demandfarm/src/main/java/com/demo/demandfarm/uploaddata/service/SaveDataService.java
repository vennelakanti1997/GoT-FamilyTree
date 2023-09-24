package com.demo.demandfarm.uploaddata.service;

import org.springframework.web.multipart.MultipartFile;

public interface SaveDataService {

    /**
     *
     * @param multipartFile file that contains the family details of GOT
     */
    void saveData(final MultipartFile multipartFile);
}
