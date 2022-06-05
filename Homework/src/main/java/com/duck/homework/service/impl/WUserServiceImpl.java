package com.duck.homework.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.duck.homework.entity.WUser;
import com.duck.homework.service.WUserService;
import com.duck.homework.mapper.WUserMapper;
import org.springframework.stereotype.Service;

/**
* @author desopu
* @description 针对表【w_user】的数据库操作Service实现
* @createDate 2022-05-30 20:39:29
*/
@Service
public class WUserServiceImpl extends ServiceImpl<WUserMapper, WUser>
    implements WUserService{

}




