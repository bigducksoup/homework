package com.duck.homework.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.duck.homework.VO.FriendRequest;
import com.duck.homework.VO.MyFriend;
import com.duck.homework.entity.Friend;
import com.duck.homework.entity.WUser;
import com.duck.homework.result.Result;
import com.duck.homework.service.FriendService;
import com.duck.homework.service.WUserService;
import com.duck.homework.websocket.webSocketUsers;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.Session;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private WUserService userService;

    @Autowired
    private FriendService friendService;


    @PostMapping("/add")
    @SaCheckLogin
    public Result add(@RequestHeader String token, @RequestParam String targetaccount) throws IOException {

        String userId = (String) StpUtil.getLoginIdByToken(token);
        WUser one = userService.getOne(new LambdaQueryWrapper<WUser>().eq(WUser::getAccount, targetaccount));

        Map<SFunction<Friend,?>,Object> map = new HashMap<>();
        map.put(Friend::getFollowerid,userId);
        map.put(Friend::getTargetid,one.getId());
        Friend onefriend = friendService.getOne(new LambdaQueryWrapper<Friend>().allEq(map));
        if (onefriend!=null){
            return  new Result(403,"请勿重复发送",null);
        }

        Friend friend = new Friend();
        friend.setId(UUID.randomUUID().toString());
        friend.setBidirectional(0);
        friend.setCreatetime(LocalDateTime.now());
        friend.setFollowerid(userId);
        friend.setTargetid(one.getId());
        friendService.save(friend);
        Session session = webSocketUsers.getUsers().get(one.getId());
        if (session!=null){

            WUser requester = userService.getOne(new QueryWrapper<WUser>().lambda().eq(WUser::getId, userId));
            //TODO 发送请求信息
            FriendRequest friendRequest = new FriendRequest();
            friendRequest.setType("friendReauest");
            friendRequest.setRequesterId(userId);
            friendRequest.setRequestNickName(requester.getNickname());
            friendRequest.setRequestTime(LocalDateTime.now());
            friendRequest.setRequesterAccount(requester.getAccount());

            session.getBasicRemote().sendText(JSON.toJSONString(friendRequest));
        }

        return new Result(200,"好友请求已发送",null);
    }


    @SaCheckLogin
    @GetMapping("/friendlist")
    public Result getfriendlist(@RequestHeader String token){
        if (token==""){
            return new Result(400,"请登录",null);
        }
        String userId = (String) StpUtil.getLoginIdByToken(token);

        Map<SFunction<Friend,?>,Object> map = new HashMap<>();
        map.put(Friend::getFollowerid,userId);
        map.put(Friend::getBidirectional,1);
        List<String> friendids1 = friendService.listObjs(new LambdaQueryWrapper<Friend>().allEq(map).select(Friend::getTargetid), Object::toString);
        map.clear();
        map.put(Friend::getTargetid,userId);
        map.put(Friend::getBidirectional,1);
        List<String> friendids2 = friendService.listObjs(new LambdaQueryWrapper<Friend>().allEq(map).select(Friend::getFollowerid), Object::toString);

        friendids1.addAll(friendids2);

        List<WUser> users = userService.list(new LambdaQueryWrapper<WUser>().in(WUser::getId, friendids1));

        List<MyFriend> myFriends = new ArrayList<>();

        for(WUser user : users){
            MyFriend myFriend = new MyFriend();
            BeanUtils.copyProperties(user,myFriend);
            myFriends.add(myFriend);
        }

        return new Result(200,"success",myFriends);


    }




    @SaCheckLogin
    @PostMapping("/requestlist")
    public Result getfriendrequest(@RequestHeader String token){
        String id = StpUtil.getLoginIdByToken(token).toString();
        Map<SFunction<Friend,?>,Object> map = new HashMap<>();
        map.put(Friend::getTargetid,id);
        map.put(Friend::getBidirectional,0);
        List<Friend> list = friendService.list(new LambdaQueryWrapper<Friend>().allEq(map));
        if (list.size()==0){
            return new Result(400,"no result",null);
        }
        List<String> followerids = list.stream().map(Friend::getFollowerid).collect(Collectors.toList());
        List<WUser> nameAndacc = userService.list(new LambdaQueryWrapper<WUser>().select(WUser::getAccount, WUser::getNickname).in(WUser::getId, followerids));
        List<FriendRequest> f = new ArrayList<>();
        for (int i=0;i< list.size();i++){
            Friend friend = list.get(i);
            FriendRequest friendRequest = new FriendRequest();
            friendRequest.setRequesterId(friend.getFollowerid());
            friendRequest.setRequestTime(friend.getCreatetime());
            friendRequest.setType("friendReauest");
            WUser wUser = nameAndacc.get(i);
            friendRequest.setRequestNickName(wUser.getNickname());
            friendRequest.setRequesterAccount(wUser.getAccount());
            f.add(friendRequest);
        }

        return new Result(200,"success",f);


    }



    @PostMapping("/opeartion")
    public Result opforrequest(@RequestHeader String token,@RequestParam String targetId,@RequestParam boolean ifacc){
        String userId = StpUtil.getLoginIdByToken(token).toString();
        Map<SFunction<Friend,?>,Object> map = new HashMap<>();
        map.put(Friend::getFollowerid,targetId);
        map.put(Friend::getTargetid,userId);
        map.put(Friend::getBidirectional,0);
        if (ifacc){
            boolean update = friendService.update(null, new LambdaUpdateWrapper<Friend>().allEq(map).set(Friend::getBidirectional, 1));
        }else{
            boolean remove = friendService.remove(new LambdaQueryWrapper<Friend>().allEq(map));
        }
        return new Result(200,"success",null);
    }








}
