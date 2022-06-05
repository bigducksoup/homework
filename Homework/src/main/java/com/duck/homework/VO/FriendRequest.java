package com.duck.homework.VO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FriendRequest {
    private String type;
    private String requesterId;
    private String requestNickName;
    private LocalDateTime requestTime;
    private String requesterAccount;


}
