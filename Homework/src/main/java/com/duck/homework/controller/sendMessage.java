package com.duck.homework.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.duck.homework.entity.Friend;
import com.duck.homework.entity.Msg;
import com.duck.homework.result.Result;
import com.duck.homework.result.websocketResult;
import com.duck.homework.service.FriendService;
import com.duck.homework.service.MsgService;
import com.duck.homework.websocket.webSocketUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.Session;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@CrossOrigin
@RestController
@RequestMapping("/sendMsg")
public class sendMessage {


    @Autowired
    private MsgService msgService;


    @Autowired
    private FriendService friendService;

    @SaCheckLogin
    @PostMapping("/text")
    public Result send(@RequestParam String userId,
                       @RequestParam String targetId,
                       @RequestParam String msg
                       ){

        Map<SFunction<Friend,?>,Object> map = new HashMap<>();
        map.put(Friend::getFollowerid,userId);
        map.put(Friend::getTargetid,targetId);
        map.put(Friend::getBidirectional,1);
        Friend one = friendService.getOne(new LambdaQueryWrapper<Friend>().allEq(map));
        Map<SFunction<Friend,?>,Object> map2 = new HashMap<>();
        map2.put(Friend::getFollowerid,targetId);
        map2.put(Friend::getTargetid,userId);
        map2.put(Friend::getBidirectional,1);
        Friend one1 = friendService.getOne(new LambdaQueryWrapper<Friend>().allEq(map2));
        if (one==null & one1==null){
            return new Result(403,"权限不足",null);
        }

        Session session = webSocketUsers.getUsers().get(targetId);
        Msg msg1 = new Msg();
        msg1.setMsg(msg);
        msg1.setFromid(userId);
        msg1.setToid(targetId);
        msg1.setCreatime(LocalDateTime.now());
        msg1.setId(UUID.randomUUID().toString());



        if (session==null){
            msg1.setIfsend(0);
            boolean save = msgService.save(msg1);
        }
        else {
            msg1.setIfsend(1);
            msgService.save(msg1);

            websocketResult websocketResult = new websocketResult();
            websocketResult.setMsg(msg);
            websocketResult.setId(userId);
            websocketResult.setType("text");
            websocketResult.setTime(LocalDateTime.now());
            String s = JSON.toJSONString(websocketResult);

            session.getAsyncRemote().sendText(s);
        }
        return new Result(200,"success",null);

    }



    @PostMapping("/img")
    public Result sendimg(@RequestParam MultipartFile file,@RequestParam String targetId,@RequestHeader String token){
        //TODO 发送并上传图片




        return null;
    }

}
