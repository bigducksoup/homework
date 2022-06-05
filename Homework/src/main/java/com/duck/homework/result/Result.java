package com.duck.homework.result;


import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Result {

    private Integer code;
    private String message;
    private Object data;

}
