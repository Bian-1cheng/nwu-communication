package com.bian.nwucommunication.dto;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class FileInfoDTO implements Serializable {

    private Integer id;

    private String title;

    private Integer greatNum;

    private Boolean isScore;

    private Integer isPass;

    private LocalDate pushDate;

    private String keyWord;

    private Integer downNum;

}
