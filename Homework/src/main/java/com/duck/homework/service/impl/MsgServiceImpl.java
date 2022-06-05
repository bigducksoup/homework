package com.duck.homework.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.duck.homework.entity.Msg;
import com.duck.homework.service.MsgService;
import com.duck.homework.mapper.MsgMapper;
import org.springframework.stereotype.Service;

/**
* @author desopu
* @description 针对表【t_msg】的数据库操作Service实现
* @createDate 2022-05-30 20:39:08
*/
@Service
public class MsgServiceImpl extends ServiceImpl<MsgMapper, Msg>
    implements MsgService{

}




