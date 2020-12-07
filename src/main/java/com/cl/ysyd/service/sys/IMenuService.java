/**
 * IMenuService.java
 * Created at 2020-11-26
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.service.sys;

import com.cl.ysyd.dto.sys.req.TsMenuReqDto;
import com.cl.ysyd.dto.sys.res.TsMenuResDto;

import java.util.List;

/**
 * 菜单 service接口类
 * @author chenlong  2020-11-26 13:56:39
 */
public interface IMenuService {
    /**
     * 根据主键删除信息
     * @param pkId 主键
     * @return int
     */
    int deleteByPrimaryKey(String pkId);

    /**
     * 根据主键查询信息
     * @param pkId 主键
     * @return TsMenuResDto
     */
    TsMenuResDto queryByPrimaryKey(String pkId);

    /**
     * 新增方法
     * @param reqDto 请求dto
     * @return int
     */
    int createMenu(TsMenuReqDto reqDto);

    /**
     * 根据主键更新信息
     * @param pkId 主键
     * @param reqDto 请求dto
     * @return int
     */
    int updateByPrimaryKey(String pkId, TsMenuReqDto reqDto);

    /**
     * 获取所有菜单和按钮
     * @return 结果集
     */
    List<TsMenuResDto> queryMenuAndButton();
}