package com.seven.mutlidatasource.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author
 * 应用信息
 */
@Table(name="t_pm_app")
@Setter
@Getter
@ToString
@NoArgsConstructor
public class PnApp {
    /**
     * 主键
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
     * 城市组编码
     */
    private String regionCode;
    /**
     * 秘钥
     */
    private String secret;
    /**
     *  应用环境
     **/
    private String envInfo;
    /**
     * 创建人
     */
    private String createUserCode;
    /**
     * 更新人
     */
    private String updateUserCode;
    /**
     * 有效标识 0:无效   1:有效
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

}