package com.duck.homework.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.duck.homework.entity.WUser;
import com.duck.homework.result.Result;
import com.duck.homework.service.WUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@CrossOrigin
@RestController
public class LoginController {

    @Autowired
    private WUserService wUserService;

    @PostMapping("/login")
    public Result login(@RequestParam String account,@RequestParam String password){
        WUser one = wUserService.getOne(new LambdaQueryWrapper<WUser>().eq(WUser::getAccount, account));
        if (one==null){
            return new Result(401,"用户名或密码错误",null);
        }
        if (Objects.equals(one.getPassword(), password)){
            StpUtil.login(one.getId());
            SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
            Map<String,String> map = new HashMap<>();
            map.put(tokenInfo.getTokenName(),tokenInfo.getTokenValue());
            map.put("nickname", one.getNickname());
            map.put("id", one.getId());
            return  new Result(200,"success",map);
        }
        return new Result(400,"failed",null);

    }



    @PostMapping("/register")
    public Result register(@RequestParam String account,@RequestParam String password,@RequestParam String nickname){

        WUser one = wUserService.getOne(new LambdaQueryWrapper<WUser>().eq(WUser::getAccount,account));
        if (one!=null){
            return new Result(400,"该账户已被注册",null);
        }
        WUser wUser = new WUser();
        wUser.setId(UUID.randomUUID().toString());
        wUser.setAccount(account);
        wUser.setCreatetime(LocalDateTime.now());
        wUser.setAvatar("默认");
        wUser.setNickname(nickname);
        wUser.setPassword(password);
        wUserService.save(wUser);
        return new Result(200,"success",null);


    }


    @SaCheckLogin
    @GetMapping("/test")
    public Result test(){
        System.out.println(StpUtil.getLoginId());
        return new Result(200,"success",null);
    }


    @PostMapping("/checklogin")
    public Result check(@RequestHeader String token){
        boolean login = StpUtil.isLogin();
        if (login){
            return new Result(200,"login",null);
        }
        else {
            return new Result(403,"nologin",null);
        }
    }



}
