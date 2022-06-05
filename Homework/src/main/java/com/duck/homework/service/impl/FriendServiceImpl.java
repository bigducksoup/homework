package com.duck.homework.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.duck.homework.entity.Friend;
import com.duck.homework.service.FriendService;
import com.duck.homework.mapper.FriendMapper;
import org.springframework.stereotype.Service;

/**
* @author desopu
* @description 针对表【t_friend】的数据库操作Service实现
* @createDate 2022-06-01 09:47:42
*/
@Service
public class FriendServiceImpl extends ServiceImpl<FriendMapper, Friend>
    implements FriendService{

}




