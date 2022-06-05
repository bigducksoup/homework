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
 * @TableName t_msg
 */
@TableName(value ="t_msg")
@Data
public class Msg implements Serializable {
    /**
     * 
     */
    @TableId
    private String id;

    /**
     * 
     */
    private String fromid;

    /**
     * 
     */
    private String toid;

    /**
     * 
     */
    private String msg;

    /**
     * 
     */
    private LocalDateTime creatime;

    /**
     * 
     */
    private Integer ifsend;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}