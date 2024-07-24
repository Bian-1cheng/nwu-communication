package com.bian.nwucommunication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
public class NsfwDTO implements Serializable {

    private Double score;

    private String url;
}
