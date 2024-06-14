package com.bian.nwucommunication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class FileUploadDTO {

    private String title;

    private String intro;

    private Boolean isPublic;

    private String SchoolName;

    private String keyWord;

}
