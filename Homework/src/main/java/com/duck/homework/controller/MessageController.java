package com.duck.homework.controller;


import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.duck.homework.entity.Msg;
import com.duck.homework.result.Result;
import com.duck.homework.result.websocketResult;
import com.duck.homework.service.MsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MsgService msgService;

    @PostMapping("/history")
    public Result gethistory(@RequestParam String id, @RequestHeader String token){

        String loginIdByToken = (String) StpUtil.getLoginIdByToken(token);
        Map<SFunction<Msg,?>,Object> firstmap = new HashMap<>();
        firstmap.put(Msg::getToid,loginIdByToken);
        firstmap.put(Msg::getFromid,id);
        firstmap.put(Msg::getIfsend,0);
        List<Msg> msgList = msgService.list(new LambdaQueryWrapper<Msg>().allEq(firstmap));
        firstmap.clear();
        firstmap.put(Msg::getFromid,loginIdByToken);
        firstmap.put(Msg::getToid,id);
        firstmap.put(Msg::getIfsend,0);
        List<Msg> msgList1 = msgService.list(new LambdaQueryWrapper<Msg>().allEq(firstmap));
        msgList.addAll(msgList1);
        List<websocketResult> resultList = new ArrayList<>();
        for(Msg m : msgList){
            websocketResult websocketResult = new websocketResult();
            websocketResult.setType("text");
            websocketResult.setMsg(m.getMsg());
            websocketResult.setId(m.getFromid());
            websocketResult.setTime(m.getCreatime());
            resultList.add(websocketResult);
        }
        List<String> ids = msgList.stream().map(Msg::getId).collect(Collectors.toList());
        msgService.update(new LambdaUpdateWrapper<Msg>().set(Msg::getIfsend,1).in(Msg::getId,ids));

        return new Result(200,"success",resultList);


    }

}
