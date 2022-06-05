package com.duck.homework.result;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class websocketResult {

    private String id;
    private String type;
    private String msg;
    private LocalDateTime time;


}
