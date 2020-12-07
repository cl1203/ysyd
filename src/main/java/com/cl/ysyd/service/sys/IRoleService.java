/**
 * IRoleService.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.service.sys;

import com.cl.ysyd.dto.order.req.BindingRoleAndMenuReqDto;
import com.cl.ysyd.dto.sys.req.TsRoleReqDto;
import com.cl.ysyd.dto.sys.res.TsRoleResDto;
import com.github.pagehelper.PageInfo;

/**
 * 角色 service接口类
 * @author chenlong  2020-11-24 23:03:59
 */
public interface IRoleService {
    /**
     * 根据主键删除信息
     * @param pkId 主键
     * @return int
     */
    int deleteByPrimaryKey(String pkId);

    /**
     * 根据主键查询信息
     * @param pkId 主键
     * @return TsRoleResDto
     */
    TsRoleResDto queryByPrimaryKey(String pkId);

    /**
     * 新增方法
     * @param reqDto 请求dto
     * @return int
     */
    int createRole(TsRoleReqDto reqDto);

    /**
     * 根据主键更新信息
     * @param pkId 主键
     * @param reqDto 请求dto
     * @return int
     */
    int updateByPrimaryKey(String pkId, TsRoleReqDto reqDto);

    /**
     * 查询角色
     *
     * @return 角色分页列表
     */
    PageInfo<TsRoleResDto> queryRoleByPage(Integer pageNum, Integer pageSize, String roleName, String isAll, String status);

    /**
     * @Author 陈龙
     * @Description 绑定菜单按钮
     * @Date 14:50 2019/8/16
     * @Param [reqBeanModel]
     * @return void
     **/
    void bindingMenu(BindingRoleAndMenuReqDto reqDto);
}