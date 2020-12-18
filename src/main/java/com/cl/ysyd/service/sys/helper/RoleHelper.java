/**
 * RoleHelper.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.service.sys.helper;

import com.cl.ysyd.common.constants.SortConstant;
import com.cl.ysyd.common.enums.DictType;
import com.cl.ysyd.common.utils.DateUtil;
import com.cl.ysyd.common.utils.LoginUtil;
import com.cl.ysyd.dto.sys.req.TsRoleReqDto;
import com.cl.ysyd.dto.sys.res.TsRoleResDto;
import com.cl.ysyd.entity.sys.TsRoleEntity;
import com.cl.ysyd.entity.sys.TsUserEntity;
import com.cl.ysyd.mapper.sys.TsUserMapper;
import com.cl.ysyd.service.sys.IBizDictionaryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 角色帮助类
 * @author chenlong  2020-11-24
 */
@Component
public class RoleHelper {

    @Autowired
    private IBizDictionaryService iTcDictService;

    @Autowired
    private TsUserMapper userMapper;

    /**
     * entity转resDto
     * @param TsRole
     * @return TsRoleResDto
     */
    public TsRoleResDto editResDto(TsRoleEntity TsRole) {
        if (TsRole == null) {
            return null;
        }
        TsRoleResDto resDto = new TsRoleResDto();
        resDto.setCreateUser(TsRole.getCreateUser());
        if(StringUtils.isNotBlank(TsRole.getCreateUser())){
            TsUserEntity userEntity = this.userMapper.selectByPrimaryKey(TsRole.getCreateUser());
            if(null != userEntity){
                resDto.setCreateUserName(userEntity.getRealName());
            }
        }
        resDto.setCreateTime(DateUtil.getDateString(TsRole.getCreateTime(),DateUtil.DATETIMESHOWFORMAT));
        resDto.setRoleName(TsRole.getRoleName());
        resDto.setRemarks(TsRole.getRemarks());
        resDto.setLastUpdateTime(DateUtil.getDateString(TsRole.getLastUpdateTime(),DateUtil.DATETIMESHOWFORMAT));
        String isAllText = this.iTcDictService.getTextByBizCode(DictType.IS_ALL.getCode(), String.valueOf(TsRole.getIsAll()));
        resDto.setIsAllText(isAllText);
        resDto.setIsAll(TsRole.getIsAll());
        resDto.setLastUpdateUser(TsRole.getLastUpdateUser());
        String statusText = this.iTcDictService.getTextByBizCode(DictType.VALID_STATUS.getCode(), String.valueOf(TsRole.getStatus()));
        resDto.setStatusText(statusText);
        resDto.setStatus(TsRole.getStatus());
        resDto.setPkId(TsRole.getPkId());
        return resDto;
    }

    /**
     * entity集合转resDto集合
     * @param entityList
     * @return List<TsRoleResDto>
     */
    public List<TsRoleResDto> editResDtoList(List<TsRoleEntity> entityList) {
        List<TsRoleResDto> resDtoList = new ArrayList<>();
        if (entityList == null || entityList.isEmpty()){
            return resDtoList;
        }
        entityList.forEach(entity -> {
            resDtoList.add(this.editResDto(entity));
        });
        return resDtoList;
    }

    /**
     * reqDto转Entity
     * @param reqDto 请求dto
     * @return TsRoleEntity
     */
    public TsRoleEntity editEntity(TsRoleReqDto reqDto) {
        if (reqDto == null) {
            return null;
        }
        TsRoleEntity entity = new TsRoleEntity();
        entity.setRoleName(reqDto.getRoleName());
        String isAllText = this.iTcDictService.getTextByBizCode(DictType.IS_ALL.getCode(), reqDto.getIsAll());
        Assert.hasText(isAllText, "所选是否拥有全部权限状态不存在, 请修改!");
        entity.setIsAll(reqDto.getIsAll());
        entity.setStatus(SortConstant.ONE.byteValue());
        entity.setRemarks(reqDto.getRemarks());
        entity.setCreateUser(LoginUtil.getUserId());
        entity.setLastUpdateTime(new Date());
        entity.setLastUpdateUser(LoginUtil.getUserId());
        return entity;
    }

    /**
     * reqDto集合转entity集合
     * @param reqDtoList
     * @return List<TsRoleEntity>
     */
    public List<TsRoleEntity> editEntityList(List<TsRoleReqDto> reqDtoList) {
        List<TsRoleEntity> entityList = new ArrayList<>();
        if (reqDtoList == null || reqDtoList.isEmpty()){
            return entityList;
        }
        reqDtoList.forEach(reqDto -> {
            entityList.add(this.editEntity(reqDto));
        });
        return entityList;
    }
}
