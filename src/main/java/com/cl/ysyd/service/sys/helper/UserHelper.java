/**
 * UserHelper.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.service.sys.helper;

import com.cl.ysyd.common.enums.DictType;
import com.cl.ysyd.common.utils.DateUtil;
import com.cl.ysyd.common.utils.LoginUtil;
import com.cl.ysyd.dto.sys.req.TsUserReqDto;
import com.cl.ysyd.dto.sys.res.TsUserResDto;
import com.cl.ysyd.entity.sys.TsUserEntity;
import com.cl.ysyd.service.sys.IBizDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户帮助类
 * @author chenlong  2020-11-24
 */
@Component
public class UserHelper {

    @Autowired
    private IBizDictionaryService iTcDictService;

    /**
     * entity转resDto
     * @param TsUser
     * @return TsUserResDto
     */
    public TsUserResDto editResDto(TsUserEntity TsUser) {
        if (TsUser == null) {
            return null;
        }
        TsUserResDto resDto = new TsUserResDto();
        resDto.setCreateUser(TsUser.getCreateUser());
        resDto.setCreateTime(DateUtil.getDateString(TsUser.getCreateTime(),DateUtil.DATETIMESHOWFORMAT));
        resDto.setRemarks(TsUser.getRemarks());
        resDto.setLastUpdateTime(DateUtil.getDateString(TsUser.getLastUpdateTime(),DateUtil.DATETIMESHOWFORMAT));
        resDto.setLastUpdateUser(TsUser.getLastUpdateUser());
        String statusText = this.iTcDictService.getTextByBizCode(DictType.VALID_STATUS.getCode(), String.valueOf(TsUser.getStatus()));
        resDto.setStatus(TsUser.getStatus());
        resDto.setStatusText(statusText);
        resDto.setRealName(TsUser.getRealName());
        resDto.setPkId(TsUser.getPkId());
        resDto.setPassword(TsUser.getPassword());
        String typeText = this.iTcDictService.getTextByBizCode(DictType.USER_TYPE.getCode(), TsUser.getType());
        resDto.setTypeText(typeText);
        resDto.setType(TsUser.getType());
        resDto.setUserName(TsUser.getUserName());
        resDto.setMobile(TsUser.getMobile());
        resDto.setEmail(TsUser.getEmail());
        String auditStatusText = this.iTcDictService.getTextByBizCode(DictType.AUDIT_STATUS.getCode(), TsUser.getAuditStatus());
        resDto.setAuditStatusText(auditStatusText);
        resDto.setAuditStatus(TsUser.getAuditStatus());
        return resDto;
    }

    /**
     * entity集合转resDto集合
     * @param entityList
     * @return List<TsUserResDto>
     */
    public List<TsUserResDto> editResDtoList(List<TsUserEntity> entityList) {
        List<TsUserResDto> resDtoList = new ArrayList<>();
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
     * @return TsUserEntity
     */
    public TsUserEntity editEntity(TsUserReqDto reqDto) {
        if (reqDto == null) {
            return null;
        }
        TsUserEntity entity = new TsUserEntity();
        String typeText = this.iTcDictService.getTextByBizCode(DictType.USER_TYPE.getCode(), reqDto.getType());
        Assert.hasText(typeText, "所选用户类型不存在, 请修改!");
        entity.setType(reqDto.getType());
        entity.setUserName(reqDto.getUserName());
        entity.setPassword(reqDto.getPassword());
        entity.setRealName(reqDto.getRealName());
        String auditStatusText = this.iTcDictService.getTextByBizCode(DictType.AUDIT_STATUS.getCode(), reqDto.getAuditStatus());
        Assert.hasText(auditStatusText, "所选审核状态不存在, 请修改!");
        entity.setAuditStatus(reqDto.getAuditStatus());
        entity.setMobile(reqDto.getMobile());
        entity.setEmail(reqDto.getEmail());
        String statusText = this.iTcDictService.getTextByBizCode(DictType.VALID_STATUS.getCode(), reqDto.getStatus());
        Assert.hasText(statusText, "所选用户状态不存在, 请修改!");
        entity.setStatus(Byte.valueOf(reqDto.getStatus()));
        entity.setRemarks(reqDto.getRemarks());
        entity.setCreateUser(LoginUtil.getUserId());
        entity.setLastUpdateTime(new Date());
        entity.setLastUpdateUser(LoginUtil.getUserId());
        return entity;
    }

    /**
     * reqDto集合转entity集合
     * @param reqDtoList
     * @return List<TsUserEntity>
     */
    public List<TsUserEntity> editEntityList(List<TsUserReqDto> reqDtoList) {
        List<TsUserEntity> entityList = new ArrayList<>();
        if (reqDtoList == null || reqDtoList.isEmpty()){
            return entityList;
        }
        reqDtoList.forEach(reqDto -> {
            entityList.add(this.editEntity(reqDto));
        });
        return entityList;
    }
}