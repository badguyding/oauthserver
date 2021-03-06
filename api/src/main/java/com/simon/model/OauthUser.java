package com.simon.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.simon.common.config.AppConfig;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
* @author SimonSun
* @date 2018-09-12
**/
@ApiModel(description = "用户")
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name="t_users")
public class OauthUser implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @KeySql(useGeneratedKeys = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(value = "创建人id")
    @Column(name = "create_by")
    private Long createBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConfig.DATE_PATTERN_DATETIME, timezone = AppConfig.DATE_TIMEZONE)
    @ApiModelProperty(value = "创建时间")
    @Column(name = "create_date")
    private Date createDate;

    @ApiModelProperty(value = "更新人id")
    @Column(name = "update_by")
    private Long updateBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConfig.DATE_PATTERN_DATETIME, timezone = AppConfig.DATE_TIMEZONE)
    @ApiModelProperty(value = "更新时间")
    @Column(name = "update_date")
    private Date updateDate;

    @ApiModelProperty(value = "用户名(昵称)")
    @Column(name = "username", nullable = false)
    private String username;

    @ApiModelProperty(value = "密码")
    @Column(name = "password", nullable = false)
    private String password;

    @ApiModelProperty(value = "有效")
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @ApiModelProperty(value = "手机区号")
    @Column(name = "area_code")
    private String areaCode;

    @ApiModelProperty(value = "手机号")
    @Column(name = "phone")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    @Column(name = "email")
    private String email;

    @ApiModelProperty(value = "地址")
    @Column(name = "address")
    private String address;

    @ApiModelProperty(value = "年龄")
    @Column(name = "age")
    private Integer age;

    @DateTimeFormat(pattern = AppConfig.DATE_PATTERN_DAY)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConfig.DATE_PATTERN_DAY, timezone = AppConfig.DATE_TIMEZONE)
    @ApiModelProperty(value = "生日")
    @Column(name = "birth")
    private Date birth;

    @ApiModelProperty(value = "头像")
    @Column(name = "head_photo")
    private String headPhoto;

    @ApiModelProperty(value = "个人简介")
    @Column(name = "person_brief")
    private String personBrief;

    @ApiModelProperty(value = "性别[0:女,1:男]")
    @Column(name = "sex")
    private Boolean sex;

    @ApiModelProperty(value = "邀请码")
    @Column(name = "visit_card")
    private String visitCard;

    @ApiModelProperty(value = "登录方式")
    @Column(name = "login_type")
    private Integer loginType;

    @ApiModelProperty(value = "登录状态")
    @Column(name = "login_status")
    private String loginStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConfig.DATE_PATTERN_DATETIME, timezone = AppConfig.DATE_TIMEZONE)
    @ApiModelProperty(value = "登录时间")
    @Column(name = "login_date")
    private Date loginDate;
}