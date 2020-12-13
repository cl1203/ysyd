/**
 * RoleServiceImpl.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.service.sys.impl;

import com.cl.ysyd.common.constants.SortConstant;
import com.cl.ysyd.common.enums.PowerTypeEnum;
import com.cl.ysyd.common.exception.BusiException;
import com.cl.ysyd.common.utils.LoginUtil;
import com.cl.ysyd.common.utils.UuidUtil;
import com.cl.ysyd.dto.order.req.BindingRoleAndMenuReqDto;
import com.cl.ysyd.dto.sys.req.TsRoleReqDto;
import com.cl.ysyd.dto.sys.res.RoleAllResDto;
import com.cl.ysyd.dto.sys.res.TsMenuResDto;
import com.cl.ysyd.dto.sys.res.TsRoleResDto;
import com.cl.ysyd.entity.sys.TrRoleMenuEntity;
import com.cl.ysyd.entity.sys.TsMenuEntity;
import com.cl.ysyd.entity.sys.TsRoleEntity;
import com.cl.ysyd.entity.sys.TsRoleEntityExample;
import com.cl.ysyd.mapper.sys.TrRoleMenuMapper;
import com.cl.ysyd.mapper.sys.TrUserRoleMapper;
import com.cl.ysyd.mapper.sys.TsMenuMapper;
import com.cl.ysyd.mapper.sys.TsRoleMapper;
import com.cl.ysyd.service.sys.IRoleService;
import com.cl.ysyd.service.sys.helper.MenuHelper;
import com.cl.ysyd.service.sys.helper.RoleHelper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 角色service实现类
 * @author chenlong  2020-11-24
 */
@Service
@Slf4j
@Transactional
public class RoleServiceImpl implements IRoleService {
    /**
     * 角色Mapper
     */
    @Autowired
    private TsRoleMapper tsRoleMapper;

    @Autowired
    private TrRoleMenuMapper roleMenuMapper;

    @Autowired
    private TrUserRoleMapper userRoleMapper;

    @Autowired
    private TsMenuMapper menuMapper;
    /**
     * 角色Helper
     */
    @Autowired
    private RoleHelper roleHelper;

    @Autowired
    private MenuHelper menuHelper;

    @Override
    public int deleteByPrimaryKey(String pkId) {
        log.info("Service deleteByPrimaryKey start. primaryKey=【{}】",pkId);
        TsRoleEntity checkEntity = this.tsRoleMapper.selectByPrimaryKey(pkId);
        if (checkEntity == null) {
            log.info("根据主键 pkId【{}】查询信息不存在",pkId);
            throw new BusiException("数据不存在");
        }
        int ret = this.tsRoleMapper.deleteByPrimaryKey(pkId);
        //删除角色菜单关系表
        roleMenuMapper.deleteRoleMenuByRoleId(pkId);
        //删除用户角色关系表
        userRoleMapper.deleteUserRoleByRoleId(pkId);
        log.info("Service deleteByPrimaryKey end. ret=【{}】",ret);
        return ret;
    }

    @Override
    public TsRoleResDto queryByPrimaryKey(String pkId) {
        log.info("Service selectByPrimaryKey start. primaryKey=【{}】",pkId);
        TsRoleEntity entity = this.tsRoleMapper.selectByPrimaryKey(pkId);
        TsRoleResDto resDto = this.roleHelper.editResDto(entity);
        //根据角色ID查询所有一级菜单
        List<TsMenuEntity> menuEntityListOne = this.menuMapper.queryMenuListByRoleIdAndMenuId(pkId, String.valueOf(SortConstant.ZERO), PowerTypeEnum.MENU.getCode());
        //一级菜单返回对象list
        List<TsMenuResDto> menuResDtoListOne = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(menuEntityListOne)){
            //遍历一级菜单 获取对应的所有的二级菜单
            menuEntityListOne.forEach(tsMenuEntityOne -> {
                //一级菜单返回对象
                TsMenuResDto menuResDtoOne = this.menuHelper.editResDto(tsMenuEntityOne);
                //二级菜单list
                List<TsMenuEntity> menuEntityListTwo = this.menuMapper.queryMenuListByRoleIdAndMenuId(pkId, tsMenuEntityOne.getPkId(), PowerTypeEnum.MENU.getCode());
                //二级菜单返回对象list
                List<TsMenuResDto> menuResDtoListTwo = new ArrayList<>();
                if(CollectionUtils.isNotEmpty(menuEntityListTwo)){
                    menuEntityListTwo.forEach(menuEntityTwo ->{
                        //二级菜单返回对象
                        TsMenuResDto menuResDtoTwo = this.menuHelper.editResDto(menuEntityTwo);
                        //二级菜单对应的所有按钮权限
                        List<TsMenuEntity> menuEntityListButton = this.menuMapper.queryMenuListByRoleIdAndMenuId(pkId, menuEntityTwo.getPkId(), PowerTypeEnum.BUTTON.getCode());
                        List<TsMenuResDto> menuResDtoListButton = this.menuHelper.editResDtoList(menuEntityListButton);
                        //添加按钮权限list
                        menuResDtoTwo.setButtonResDtoList(menuResDtoListButton);
                        menuResDtoListTwo.add(menuResDtoTwo);
                    });
                    //添加所有的二级菜单和按钮权限  或者所有的按钮权限
                    menuResDtoOne.setMenuResDtoList(menuResDtoListTwo);
                }else{
                    //如果没有二级菜单 , 直接查询一级菜单对应所有的按钮权限
                    List<TsMenuEntity> menuEntityListButton = this.menuMapper.queryMenuListByRoleIdAndMenuId(pkId, tsMenuEntityOne.getPkId(), PowerTypeEnum.BUTTON.getCode());
                    List<TsMenuResDto> menuResDtoListButton = this.menuHelper.editResDtoList(menuEntityListButton);
                    menuResDtoOne.setButtonResDtoList(menuResDtoListButton);
                }
                //添加所有的一级菜单
                menuResDtoListOne.add(menuResDtoOne);
            });
            resDto.setMenuResDtoList(menuResDtoListOne);
        }
        log.info("Service selectByPrimaryKey end. res=【{}】",resDto);
        return resDto;
    }

    @Override
    public int createRole(TsRoleReqDto reqDto) {
        log.info("Service createRole start. reqDto=【{}】",reqDto);
        TsRoleEntity entity = this.roleHelper.editEntity(reqDto);
        long l = this.tsRoleMapper.countByRoleName(reqDto.getRoleName());
        Assert.isTrue(l== SortConstant.ZERO, "角色名已经存在,请修改!");
        entity.setCreateTime(new Date());
        entity.setPkId(UuidUtil.getUuid());
        int ret = this.tsRoleMapper.insertSelective(entity);
        log.info("Service createRole end. ret=【{}】",ret);
        return ret;
    }

    @Override
    public int updateByPrimaryKey(String pkId, TsRoleReqDto reqDto) {
        log.info("Service updateByPrimaryKey start. pkId=【{}】, reqDto =【{}】",pkId,reqDto);
        if(StringUtils.isEmpty(pkId)) {
            throw new BusiException("参数错误,缺少pkId");
        }
        TsRoleEntity checkEntity = this.tsRoleMapper.selectByPrimaryKey(pkId);
        if (checkEntity == null) {
            log.info("根据主键 pkId【{}】查询信息不存在",pkId);
            throw new BusiException("数据不存在");
        }
        if(!checkEntity.getRoleName().equals(reqDto.getRoleName())){
            throw new BusiException("角色名不能修改!");
        }
        TsRoleEntity entity = this.roleHelper.editEntity(reqDto);
        entity.setPkId(pkId);
        int ret = this.tsRoleMapper.updateByPrimaryKeySelective(entity);
        log.info("Service updateByPrimaryKey end. ret=【{}】",ret);
        return ret;
    }

    @Override
    public PageInfo<TsRoleResDto> queryRoleByPage(Integer pageNum, Integer pageSize, String roleName, String isAll, String status) {
        List<TsRoleResDto> roleResDtoList;
        PageInfo<TsRoleResDto> pageInfo;
        //查询条件
        TsRoleEntityExample entityExample = this.queryCondition(roleName, isAll, status);
        PageHelper.orderBy("CREATE_TIME DESC");
        Page<TsRoleResDto> startPage = PageHelper.startPage(pageNum, pageSize);
        List<TsRoleEntity> roleEntityList = this.tsRoleMapper.selectByExample(entityExample);
        //entity转换resDto
        roleResDtoList = this.roleHelper.editResDtoList(roleEntityList);
        pageInfo = new PageInfo<>(startPage);
        pageInfo.setList(roleResDtoList);

        log.info("Service queryRoleByPage end.");
        return pageInfo;
    }

    /**
     * 查询条件
     * @return 返回结果集
     */
    private TsRoleEntityExample queryCondition(String roleName, String isAll, String status){
        TsRoleEntityExample roleEntityExample = new TsRoleEntityExample();
        TsRoleEntityExample.Criteria criteria = roleEntityExample.createCriteria();
        if(StringUtils.isNotBlank(roleName)){
            criteria.andRoleNameLike("%" + roleName + "%");
        }
        if(StringUtils.isNotBlank(isAll)){
            criteria.andIsAllEqualTo(isAll);
        }
        if(StringUtils.isNotBlank(status)){
            criteria.andStatusEqualTo(Byte.valueOf(status));
        }
        criteria.andPkIdNotEqualTo(SortConstant.ONE.toString());
        return roleEntityExample;
    }

    @Override
    public void bindingMenu(BindingRoleAndMenuReqDto reqDto) {
        // 用户信息
        TsRoleEntity roleEntity = this.tsRoleMapper.selectByPrimaryKey(reqDto.getRoleId());
        if (null == roleEntity) {
            log.info("User does not exist. roleId={}", reqDto.getRoleId());
            throw new BusiException("角色信息不存在!");
        }
        //删除角色已有关系
        this.roleMenuMapper.deleteRoleMenuByRoleId(reqDto.getRoleId());
        List<String> menuIdList = reqDto.getMenuIdList();
        menuIdList.forEach(menuId -> {
            TsMenuEntity menuEntity = this.menuMapper.selectByPrimaryKey(menuId);
            if (null == menuEntity) {
                log.info("User does not exist. menuId={}", menuId);
                throw new BusiException("菜单按钮信息不存在!");
            }
            TrRoleMenuEntity roleMenuEntity = new TrRoleMenuEntity();
            roleMenuEntity.setPkId(UuidUtil.getUuid());
            roleMenuEntity.setRoleId(reqDto.getRoleId());
            roleMenuEntity.setMenuId(menuId);
            roleMenuEntity.setCreateTime(new Date());
            roleMenuEntity.setLastUpdateTime(new Date());
            roleMenuEntity.setCreateUser(LoginUtil.getUserId());
            roleMenuEntity.setLastUpdateUser(LoginUtil.getUserId());
            int i = this.roleMenuMapper.insertSelective(roleMenuEntity);
            Assert.isTrue(i == SortConstant.ONE, "绑定菜单按钮权限失败!");
        });

    }

    @Override
    public List<RoleAllResDto> queryAll() {
        List<RoleAllResDto>  tsRoleResDtoList = this.tsRoleMapper.queryAll();
        return tsRoleResDtoList;
    }
}
