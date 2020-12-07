/**
 * IUserService.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.service.sys;

import com.cl.ysyd.dto.order.req.BindingUserAndRoleReqDto;
import com.cl.ysyd.dto.sys.req.TsUserReqDto;
import com.cl.ysyd.dto.sys.res.TsUserResDto;
import com.github.pagehelper.PageInfo;

/**
 * 用户 service接口类
 * @author chenlong  2020-11-24 22:59:23
 */
public interface IUserService {
    /**
     * 根据主键删除信息
     * @param pkId 主键
     * @return int
     */
    int deleteByPrimaryKey(String pkId);

    /**
     * 根据主键查询信息
     * @param pkId 主键
     * @return TsUserResDto
     */
    TsUserResDto queryByPrimaryKey(String pkId);

    /**
     * 新增方法
     * @param reqDto 请求dto
     * @return int
     */
    int createUser(TsUserReqDto reqDto);

    /**
     * 根据主键更新信息
     * @param pkId 主键
     * @param reqDto 请求dto
     * @return int
     */
    int updateByPrimaryKey(String pkId, TsUserReqDto reqDto);

    /**
     * 查询用户
     *
     * @return 用户分页列表
     */
    PageInfo<TsUserResDto> queryUserByPage(Integer pageNum, Integer pageSize, String auditStatus,String userName, String realName, String type, String status);

    /**
     * 重置密码
     *
     * @param userId 用户id
     */
    void resetPassword(String userId);

    /**
     * 修改密码
     * @param reqDto 用户对象
     */
    void updatePassword(TsUserReqDto reqDto);

    /**
     * 绑定角色
     * @param reqDto 请求对象
     */
    void bindingRole(BindingUserAndRoleReqDto reqDto);
}