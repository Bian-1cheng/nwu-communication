package com.bian.nwucommunication.dto.resp;

import lombok.Data;

import java.time.LocalDate;

@Data
public class FileInfoDetailRespDTO {

    private Integer id;

    private String title;

    private Integer greatNum;

    private Boolean isScore;

    private Integer isPass;

    private LocalDate pushDate;

    private String keyWord;

    private Integer downNum;

    private String path;
}
