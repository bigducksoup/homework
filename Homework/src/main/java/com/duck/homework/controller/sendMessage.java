package com.duck.homework.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.duck.homework.VO.img;
import com.duck.homework.entity.Friend;
import com.duck.homework.entity.Msg;
import com.duck.homework.result.Result;
import com.duck.homework.result.websocketResult;
import com.duck.homework.service.FriendService;
import com.duck.homework.service.MsgService;
import com.duck.homework.service.SaveFileService;
import com.duck.homework.websocket.webSocketUsers;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.Session;
import java.io.IOException;
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
    private SaveFileService saveFileService;

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
            return new Result(403,"????????????",null);
        }

        Session session = webSocketUsers.getUsers().get(targetId);
        Msg msg1 = new Msg();
        msg1.setMsg(msg);
        msg1.setFromid(userId);
        msg1.setToid(targetId);
        msg1.setType("text");
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



    @SaCheckLogin
    @PostMapping("/img")
    public Result sendimg(@RequestParam MultipartFile file, @RequestParam String targetId, @RequestHeader String token) throws IOException {
        //TODO ?????????????????????
        String id = (String) StpUtil.getLoginIdByToken(token);
        //????????????
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String imgName = UUID.randomUUID().toString();
        boolean b = saveFileService.saveFile(file, imgName+'.'+extension);
        if (!b){
            return new Result(400,"failed",null);
        }

        Msg msg = new Msg();
        msg.setId(imgName);
        msg.setFromid(id);
        msg.setToid(targetId);
        msg.setType("img");
        msg.setCreatime(LocalDateTime.now());
        msg.setMsg("http://localhost:90/"+"imgmsg/"+imgName+'.'+extension);



        Session session = webSocketUsers.getUsers().get(targetId);
        if (session!=null){
            msg.setIfsend(1);
            msgService.save(msg);
            websocketResult websocketResult = new websocketResult();
            websocketResult.setId(id);
            websocketResult.setMsg(msg.getMsg());
            websocketResult.setType("img");
            websocketResult.setTime(LocalDateTime.now());
            session.getAsyncRemote().sendText(JSON.toJSONString(websocketResult));
        }
        else{
            msg.setIfsend(0);
            msgService.save(msg);
        }


        return new Result(200,"success",msg.getMsg());
    }




}
