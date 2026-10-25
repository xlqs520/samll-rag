package com.xlqs.smallrag.entity;


import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
* 系统用户表
*
* @author 卿本佳人
* @since 2026年10月23日
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@TableName("sys_user")
public class SysUser implements Serializable {


    /**
    * 用户ID
    */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
    * 用户名
    */
    private String username;

    /**
    * 密码哈希
    */
    private String password;

    /**
    * 密码随机盐值
    */
    private String salt;

    /**
    * 昵称
    */
    private String nickname;

    /**
    * 状态：1启用，0禁用
    */
    private Integer status;

    /**
    * 创建时间
    */
    private Date createdAt;

    /**
    * 更新时间
    */
    private Date updatedAt;

}