package com.seven.mutlidatasource.entity;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author 
 * 应用信息
 */
@Table(name="t_tc_app")
@Data
public class TcApp implements Serializable {
    /**
     * 主键自增ID
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 应用编码
     */
    private String appCode;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 应用入口秘钥
     */
    private String appKey;

    /**
     * 创建人
     */
    private String createUserCode;

    /**
     * 更新人
     */
    private String updateUserCode;

    /**
     * 有效标识0:无效   1:有效
     */
    private Byte enableFlag;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}