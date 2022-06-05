package com.duck.homework.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName t_friend
 */
@TableName(value ="t_friend")
@Data
public class Friend implements Serializable {
    /**
     * 
     */
    @TableId
    private String id;

    /**
     * 
     */
    private String followerid;

    /**
     * 
     */
    private String targetid;

    /**
     * 
     */
    private LocalDateTime createtime;

    /**
     * 
     */
    private Integer bidirectional;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}