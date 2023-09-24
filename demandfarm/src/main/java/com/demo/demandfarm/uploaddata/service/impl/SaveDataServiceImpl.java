package com.demo.demandfarm.uploaddata.service.impl;

import com.demo.demandfarm.dto.error.ErrorResponse;
import com.demo.demandfarm.dto.error.ErrorWrapper;
import com.demo.demandfarm.entity.GoTFamTreeEntity;
import com.demo.demandfarm.error.BusinessException;
import com.demo.demandfarm.repository.GoTFamTreeRepository;
import com.demo.demandfarm.uploaddata.service.SaveDataService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@Slf4j
public class SaveDataServiceImpl implements SaveDataService {
    private transient ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private transient GoTFamTreeRepository goTFamTreeRepository;
    @Override
    @Transactional
    public void saveData(final MultipartFile multipartFile) {

        if(multipartFile.isEmpty()){
            throw new BusinessException(HttpStatus.BAD_REQUEST,
                    new ErrorWrapper(List.of(new ErrorResponse(9004,"Please upload JSON file with content"))));
        }
        final String originalFileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        log.info("File Name is {}", originalFileName);
        if(!"json".equals(StringUtils.getFilenameExtension(originalFileName))){
            throw new BusinessException(HttpStatus.BAD_REQUEST,
                    new ErrorWrapper(List.of(new ErrorResponse(9005,"Please upload JSON file"))));
        }
        final JsonNode fileContent;
        try (final InputStream inputStream =multipartFile.getInputStream()){
            fileContent = objectMapper.readTree(inputStream);

        } catch (IOException e) {
            log.error("Failed to read file",e);
            throw new BusinessException(HttpStatus.BAD_REQUEST,
                    new ErrorWrapper(List.of(new ErrorResponse(9006,"Failed to read file"))));
        }
        final JsonNode characters = fileContent.get("characters");

        if(characters.isNull() || characters.isEmpty() || !characters.isArray()){
            throw new BusinessException(HttpStatus.BAD_REQUEST,
                    new ErrorWrapper(List.of(new ErrorResponse(9007,"Make sure the data is in correct format"))));
        }
        characters.forEach(character->{
            if(character.get("houseName")!= null){
                final String house = String.valueOf(character.get("houseName"));
                if (house.startsWith("[") && house.endsWith("]")){
                    final String[] houses = house.replace("[","")
                            .replace("]","")
                            .replace("\\\"", "")
                            .split(",");
                    final ObjectNode objectNode = (ObjectNode) character;
                    for (String singleHouse:houses
                         ) {
                        objectNode.put("houseName", singleHouse.replace("\"","").trim());
                        goTFamTreeRepository.save(new GoTFamTreeEntity(objectNode));
                    }
                }else{
                    goTFamTreeRepository.save(new GoTFamTreeEntity(character));
                }

            }

        });
    }
}
