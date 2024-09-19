package com.jlu.webcommunity.enums;

import lombok.Getter;

@Getter
public enum StatusCodeEnum {
    SUCCESS(0, "请求成功"),
    FAIL(1, "请求失败"),
    ERROR(10, "服务器发生错误"),
    USER_STATUS_ERROR(100, "用户未登录"),
    ROLE_STATUS_ERROR(200, "没有操作权限"),
    USER_BANNED(300, "用户已被封禁"),
    LOGIN_ERROR(1000, "登录失败，用户名或密码错误"),
    WRONG_CAPTCHA_ERROR(1001, "验证码错误"),
    OLD_CAPTCHA_ERROR(1002, "验证码已过期"),
    REGISTER_ERROR(1010, "注册失败，该用户名已存在"),
    MODIFY_PASSWORD_ERROR(1020, "修改密码失败，旧密码错误"),
    POST_NOT_EXIST(1100, "帖子不存在"),
    SECTION_NOT_EXIST(1101, "版块不存在"),
    USER_NOT_EXIST(1102, "用户不存在"),
    COMMENT_NOT_EXIST(1103, "评论不存在"),
    ADD_POST_FAILED(1200, "发帖失败"),
    MODIFY_POST_FAILED(1201, "修改帖子失败"),
    DELETE_POST_FAILED(1202, "删帖失败"),
    MODIFY_POST_TOP_FAILED(1203, "修改帖子置顶状态失败"),
    MODIFY_USER_INFO_FAILED(1300, "修改用户信息失败"),
    MODIFY_USER_STATUS_FAILED(1301, "修改用户状态失败"),
    ADD_COMMENT_FAILED(1400, "评论失败"),
    DELETE_COMMENT_FAILED(1401, "删除评论失败"),
    ADD_FOLLOW_FAILED(1500, "添加关注失败"),
    REMOVE_FOLLOW_FAILED(1510, "取消关注失败"),
    UPLOAD_IMAGE_FAILED(2000, "上传图片失败");

    private final int code;
    private final String msg;
    StatusCodeEnum(int code, String msg){
        this.code = code;
        this.msg = msg;
    }
}
