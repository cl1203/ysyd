/**
 * MenuServiceImpl.java
 * Created at 2020-12-06
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.service.sys.impl;

import com.cl.ysyd.common.constants.SortConstant;
import com.cl.ysyd.common.enums.PowerTypeEnum;
import com.cl.ysyd.common.exception.BusiException;
import com.cl.ysyd.dto.sys.req.TsMenuReqDto;
import com.cl.ysyd.dto.sys.res.TsMenuResDto;
import com.cl.ysyd.entity.sys.TsMenuEntity;
import com.cl.ysyd.mapper.sys.TsMenuMapper;
import com.cl.ysyd.service.sys.IMenuService;
import com.cl.ysyd.service.sys.helper.MenuHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 菜单service实现类
 * @author chenlong  2020-12-06
 */
@Service
@Slf4j
@Transactional
public class MenuServiceImpl implements IMenuService {
    /**
     * 菜单Mapper
     */
    @Autowired
    private TsMenuMapper tsMenuMapper;

    /**
     * 菜单Helper
     */
    @Autowired
    private MenuHelper menuHelper;

    @Override
    public int deleteByPrimaryKey(String pkId) {
        log.info("Service deleteByPrimaryKey start. primaryKey=【{}】",pkId);
        TsMenuEntity checkEntity = this.tsMenuMapper.selectByPrimaryKey(pkId);
        if (checkEntity == null) {
            log.info("根据主键 pkId【{}】查询信息不存在",pkId);
            throw new BusiException("数据不存在");
        }
        int ret = this.tsMenuMapper.deleteByPrimaryKey(pkId); 
        log.info("Service deleteByPrimaryKey end. ret=【{}】",ret);
        return ret;
    }

    @Override
    public TsMenuResDto queryByPrimaryKey(String pkId) {
        log.info("Service selectByPrimaryKey start. primaryKey=【{}】",pkId);
        TsMenuEntity entity = this.tsMenuMapper.selectByPrimaryKey(pkId);
        TsMenuResDto resDto = this.menuHelper.editResDto(entity);
        log.info("Service selectByPrimaryKey end. res=【{}】",resDto);
        return resDto;
    }

    @Override
    public int createMenu(TsMenuReqDto reqDto) {
        log.info("Service createMenu start. reqDto=【{}】",reqDto);
        TsMenuEntity entity = this.menuHelper.editEntity(reqDto);
        entity.setCreateTime(new Date());
        // TODO 添加主键
        int ret = this.tsMenuMapper.insert(entity);
        log.info("Service createMenu end. ret=【{}】",ret);
        return ret;
    }

    @Override
    public int updateByPrimaryKey(String pkId, TsMenuReqDto reqDto) {
        log.info("Service updateByPrimaryKey start. pkId=【{}】, reqDto =【{}】",pkId,reqDto);
        if(StringUtils.isEmpty(pkId)) {
            throw new BusiException("参数错误,缺少pkId");
        }
        TsMenuEntity checkEntity = this.tsMenuMapper.selectByPrimaryKey(pkId);
        if (checkEntity == null) {
            log.info("根据主键 pkId【{}】查询信息不存在",pkId);
            throw new BusiException("数据不存在");
        }
        TsMenuEntity entity = this.menuHelper.editEntity(reqDto);
        entity.setPkId(pkId);
        int ret = this.tsMenuMapper.updateByPrimaryKey(entity);
        log.info("Service updateByPrimaryKey end. ret=【{}】",ret);
        return ret;
    }

    @Override
    public List<TsMenuResDto> queryMenuAndButton() {
        //获取所有一级菜单
        List<TsMenuEntity> menuEntityListOne = this.tsMenuMapper.queryMenuListAll(String.valueOf(SortConstant.ZERO), PowerTypeEnum.MENU.getCode());
        //一级菜单返回对象list
        List<TsMenuResDto> menuResDtoListOne = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(menuEntityListOne)){
            //遍历一级菜单 获取对应的所有的二级菜单
            menuEntityListOne.forEach(tsMenuEntityOne -> {
                //一级菜单返回对象
                TsMenuResDto menuResDtoOne = this.menuHelper.editResDto(tsMenuEntityOne);
                //二级菜单list
                List<TsMenuEntity> menuEntityListTwo = this.tsMenuMapper.queryMenuListAll(tsMenuEntityOne.getPkId(), PowerTypeEnum.MENU.getCode());
                //二级菜单返回对象list
                List<TsMenuResDto> menuResDtoListTwo = new ArrayList<>();
                if(CollectionUtils.isNotEmpty(menuEntityListTwo)){
                    menuEntityListTwo.forEach(menuEntityTwo ->{
                        //二级菜单返回对象
                        TsMenuResDto menuResDtoTwo = this.menuHelper.editResDto(menuEntityTwo);
                        //二级菜单对应的所有按钮权限
                        List<TsMenuEntity> menuEntityListButton = this.tsMenuMapper.queryMenuListAll(menuEntityTwo.getPkId(), PowerTypeEnum.BUTTON.getCode());
                        List<TsMenuResDto> menuResDtoListButton = this.menuHelper.editResDtoList(menuEntityListButton);
                        //添加按钮权限list
                        menuResDtoTwo.setButtonResDtoList(menuResDtoListButton);
                        menuResDtoListTwo.add(menuResDtoTwo);
                    });
                    //添加所有的二级菜单和按钮权限  或者所有的按钮权限
                    menuResDtoOne.setMenuResDtoList(menuResDtoListTwo);
                }else{
                    //如果没有二级菜单 , 直接查询一级菜单对应所有的按钮权限
                    List<TsMenuEntity> menuEntityListButton = this.tsMenuMapper.queryMenuListAll(tsMenuEntityOne.getPkId(), PowerTypeEnum.BUTTON.getCode());
                    List<TsMenuResDto> menuResDtoListButton = this.menuHelper.editResDtoList(menuEntityListButton);
                    menuResDtoOne.setButtonResDtoList(menuResDtoListButton);
                }
                //添加所有的一级菜单
                menuResDtoListOne.add(menuResDtoOne);
            });
        }
        return menuResDtoListOne;
    }
}