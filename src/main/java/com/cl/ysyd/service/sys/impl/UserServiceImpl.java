/**
 * UserServiceImpl.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.service.sys.impl;

import com.cl.ysyd.common.constants.SortConstant;
import com.cl.ysyd.common.exception.BusiException;
import com.cl.ysyd.common.utils.CheckMatchAndSpaceUtil;
import com.cl.ysyd.common.utils.LoginUtil;
import com.cl.ysyd.common.utils.MD5Util;
import com.cl.ysyd.common.utils.UuidUtil;
import com.cl.ysyd.dto.order.req.BindingUserAndRoleReqDto;
import com.cl.ysyd.dto.sys.req.TsUserReqDto;
import com.cl.ysyd.dto.sys.res.TsRoleResDto;
import com.cl.ysyd.dto.sys.res.TsUserResDto;
import com.cl.ysyd.entity.sys.TrUserRoleEntity;
import com.cl.ysyd.entity.sys.TsRoleEntity;
import com.cl.ysyd.entity.sys.TsUserEntity;
import com.cl.ysyd.entity.sys.TsUserEntityExample;
import com.cl.ysyd.mapper.sys.TrUserRoleMapper;
import com.cl.ysyd.mapper.sys.TsRoleMapper;
import com.cl.ysyd.mapper.sys.TsUserMapper;
import com.cl.ysyd.service.sys.IUserService;
import com.cl.ysyd.service.sys.helper.RoleHelper;
import com.cl.ysyd.service.sys.helper.UserHelper;
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

import java.util.Date;
import java.util.List;

/**
 * 用户service实现类
 *
 * @author chenlong  2020-11-24
 */
@Service
@Slf4j
@Transactional
public class UserServiceImpl implements IUserService {
    /**
     * 用户Mapper
     */
    @Autowired
    private TsUserMapper tsUserMapper;

    @Autowired
    private TrUserRoleMapper trUserRoleMapper;

    @Autowired
    private TsRoleMapper tsRoleMapper;

    /**
     * 用户Helper
     */
    @Autowired
    private UserHelper userHelper;

    @Autowired
    private RoleHelper roleHelper;

    /**
     * 初始密码，由MD5加密签名。
     */
    private static final String INIT_PASSWORD = "12345678";

    @Override
    public int deleteByPrimaryKey(String pkId) {
        log.info("Service deleteByPrimaryKey start. primaryKey=【{}】", pkId);
        TsUserEntity checkEntity = this.tsUserMapper.selectByPrimaryKey(pkId);
        if (checkEntity == null) {
            log.info("根据主键 pkId【{}】查询信息不存在", pkId);
            throw new BusiException("数据不存在");
        }
        int ret = this.tsUserMapper.deleteByPrimaryKey(pkId);
        //删除用户角色关系表
        this.trUserRoleMapper.deleteUserRoleByUserId(pkId);
        log.info("Service deleteByPrimaryKey end. ret=【{}】", ret);
        return ret;
    }

    @Override
    public TsUserResDto queryByPrimaryKey(String pkId) {
        log.info("Service selectByPrimaryKey start. primaryKey=【{}】", pkId);
        TsUserEntity entity = this.tsUserMapper.selectByPrimaryKey(pkId);
        TsUserResDto resDto = this.userHelper.editResDto(entity);
        // 用户角色ID
        List<String> roleIds = this.trUserRoleMapper.getUserRoleIdList(entity.getPkId());
        if(CollectionUtils.isEmpty(roleIds)){
            return resDto;
        }
        //角色集合
        List<TsRoleEntity> roleEntityList = this.tsRoleMapper.getRolesTextLine(roleIds);
        TsRoleResDto roleResDto = this.roleHelper.editResDto(roleEntityList.get(SortConstant.ZERO));
        resDto.setRoleResDto(roleResDto);
        log.info("Service selectByPrimaryKey end. res=【{}】", resDto);
        return resDto;
    }

    @Override
    public int createUser(TsUserReqDto reqDto) {
        log.info("Service createUser start. reqDto=【{}】", reqDto);
        //校验
        this.checkParameter(reqDto);
        TsUserEntity entity = this.userHelper.editEntity(reqDto);
        String password = MD5Util.getInstance().encrypt(INIT_PASSWORD);
        password = MD5Util.getInstance().encryptBySalt(password);
        entity.setPassword(password);
        entity.setCreateTime(new Date());
        entity.setPkId(UuidUtil.getUuid());
        int ret = this.tsUserMapper.insertSelective(entity);
        log.info("Service createUser end. ret=【{}】", ret);
        return ret;
    }

    @Override
    public Boolean queryUserByUserName(String userName) {
        long l = this.tsUserMapper.countByUserName(userName);
        if(l == SortConstant.ZERO){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public int registerUser(TsUserReqDto reqDto) {
        log.info("Service createUser start. reqDto=【{}】", reqDto);
        //校验
        this.checkParameter(reqDto);
        TsUserEntity entity = this.userHelper.editEntity(reqDto);
        String password = reqDto.getPassword();
        Assert.hasText(password, "密码不能为空!");
        password = MD5Util.getInstance().encrypt(password);
        password = MD5Util.getInstance().encryptBySalt(password);
        entity.setPassword(password);
        entity.setCreateTime(new Date());
        entity.setPkId(UuidUtil.getUuid());
        int ret = this.tsUserMapper.insertSelective(entity);
        log.info("Service createUser end. ret=【{}】", ret);
        return ret;
    }

    /**
     * 新增校验
     * @param reqDto 请求对象
     */
    private void checkParameter(TsUserReqDto reqDto) {
        //用户名唯一校验
        String userName = reqDto.getUserName();
        long l = this.tsUserMapper.countByUserName(userName);
        Assert.isTrue(l== SortConstant.ZERO, "用户名已经存在,请修改!");
        //校验用户名是否符合规则
        if(userName.length() < 3 || userName.length() > 100){
            throw new BusiException("用户名长度应该在3-100位之间,请修改!");
        }
        boolean flag = CheckMatchAndSpaceUtil.checkBlankSpace(userName);
        Assert.isTrue(flag , "用户名不能包含空格!");
        String regex = "^[a-z0-9A-Z]+$";
        if(!CheckMatchAndSpaceUtil.match(regex , userName)) {
            throw new BusiException("用户名格式规则: 必须只能包含数字和字母! ");
        }
        //校验手机号码和邮箱
        this.checkMobileAndEmain(reqDto);

    }

    /**
     * 校验手机号码和邮箱
     * @param reqDto 请求对象
     */
    private void checkMobileAndEmain(TsUserReqDto reqDto) {
        //校验手机号码和邮箱
        String mobile = reqDto.getMobile();
        //String email = reqDto.getEmail();
        if (StringUtils.isNotBlank(mobile)) {
            String mobileRegex = "^1(3|4|5|7|8|9)\\d{9}$";
            if (!CheckMatchAndSpaceUtil.match(mobileRegex, mobile)) {
                throw new BusiException("手机号码不符合规则,请修改! ");
            }
        }
        /*if(StringUtils.isNotBlank(email)){
            String mobileRegex = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            if(!CheckMatchAndSpaceUtil.match(mobileRegex , mobile)) {
                throw new BusiException("邮箱号不符合规则,请修改! ");
            }
        }*/
    }



    @Override
    public int updateByPrimaryKey(String pkId, TsUserReqDto reqDto) {
        log.info("Service updateByPrimaryKey start. pkId=【{}】, reqDto =【{}】", pkId, reqDto);
        if (StringUtils.isEmpty(pkId)) {
            throw new BusiException("参数错误,缺少pkId");
        }
        TsUserEntity checkEntity = this.tsUserMapper.selectByPrimaryKey(pkId);
        if (checkEntity == null) {
            log.info("根据主键 pkId【{}】查询信息不存在", pkId);
            throw new BusiException("数据不存在");
        }
        if(!checkEntity.getUserName().equals(reqDto.getUserName())){
            log.info("用户名不能修改 userName【{}】", reqDto.getUserName());
            throw new BusiException("用户名不能修改!");
        }
        //校验手机号码和邮箱
        this.checkMobileAndEmain(reqDto);
        TsUserEntity entity = this.userHelper.editEntity(reqDto);
        entity.setPkId(pkId);
        int ret = this.tsUserMapper.updateByPrimaryKeySelective(entity);
        log.info("Service updateByPrimaryKey end. ret=【{}】", ret);
        return ret;
    }

    @Override
    public PageInfo<TsUserResDto> queryUserByPage(Integer pageNum, Integer pageSize, String auditStatus,String userName, String realName, String type, String status) {
        List<TsUserResDto> userResDtoList;
        PageInfo<TsUserResDto> pageInfo;
        if(StringUtils.isNotBlank(auditStatus)){
            //查询条件
            TsUserEntityExample entityExample = this.queryCondition(auditStatus, userName, realName, type, status);
            PageHelper.orderBy("CREATE_TIME DESC");
            Page<TsUserResDto> startPage = PageHelper.startPage(pageNum, pageSize);
            List<TsUserEntity> userEntityList = this.tsUserMapper.selectByExample(entityExample);
            //entity转换resDto
            userResDtoList = this.userHelper.editResDtoList(userEntityList);
            pageInfo = new PageInfo<>(startPage);
            pageInfo.setList(userResDtoList);
        }else{
            throw new BusiException("用户审核状态不能为空!");
        }
        log.info("Service queryUser end.");
        return pageInfo;
    }

    /**
     * 查询条件
     * @return 返回结果集
     */
    private TsUserEntityExample queryCondition(String auditStatus,String userName, String realName, String type, String status) {
        TsUserEntityExample entityExample = new TsUserEntityExample();
        TsUserEntityExample.Criteria criteria = entityExample.createCriteria();
        if(StringUtils.isNotBlank(userName)){//用户名
            criteria.andUserNameLike("%" + userName + "%");
        }
        if(StringUtils.isNotBlank(realName)){//真实姓名
            criteria.andRealNameLike("%" + realName + "%");
        }
        if(StringUtils.isNotBlank(type)){//用户类型
            criteria.andTypeEqualTo(type);
        }
        if(StringUtils.isNotBlank(status)){//状态
            criteria.andStatusEqualTo(Byte.valueOf(status));
        }
        criteria.andAuditStatusEqualTo(auditStatus);
        criteria.andPkIdNotEqualTo(SortConstant.ONE.toString());
        return entityExample;
    }

    @Override
    public void resetPassword(String pkId) {
        log.info("Service resetPassword start. pkId={}", pkId);
        // 用户信息
        TsUserEntity userEntity = this.tsUserMapper.selectByPrimaryKey(pkId);
        if (userEntity == null) {
            log.info("User does not exist. pkId={}", pkId);
            throw new BusiException("用户信息不存在!");
        }
        String password;
        password = MD5Util.getInstance().encrypt(INIT_PASSWORD);
        password = MD5Util.getInstance().encryptBySalt(password);
        userEntity.setPassword(password);
        userEntity.setLastUpdateTime(new Date());
        userEntity.setLastUpdateUser(LoginUtil.getUserId());
        int i = this.tsUserMapper.updateByPrimaryKeySelective(userEntity);

        log.info("Service resetPassword end. ");
        Assert.isTrue(i == SortConstant.ONE, "重置密码失败!");
    }


    @Override
    public void updatePassword(TsUserReqDto reqDto) {
        List<TsUserEntity> userEntityList = this.checkUser(reqDto);
        String newPassword = reqDto.getNewPassword();
        Assert.hasText(newPassword , "新密码不能为空!");
        boolean flag = CheckMatchAndSpaceUtil.checkBlankSpace(newPassword);
        Assert.isTrue(flag , "新密码不能包含空格!");
        String regex = "^[a-z0-9A-Z]+$";
        if(!CheckMatchAndSpaceUtil.match(regex , newPassword)) {
            throw new BusiException("密码格式规则: 必须只能包含数字和字母! ");
        }
        newPassword = MD5Util.getInstance().encryptBySalt(newPassword);
        TsUserEntity userEntity = userEntityList.get(SortConstant.ZERO);
        userEntity.setPassword(newPassword);
        int i = this.tsUserMapper.updateByPrimaryKeySelective(userEntity);
        Assert.isTrue(i == SortConstant.ONE, "修改密码失败!");
    }

    @Override
    public void bindingRole(BindingUserAndRoleReqDto reqDto) {
        // 用户信息
        TsUserEntity userEntity = this.tsUserMapper.selectByPrimaryKey(reqDto.getUserId());
        if (null == userEntity) {
            log.info("User does not exist. userId={}", reqDto.getUserId());
            throw new BusiException("用户信息不存在!");
        }
        //删除原本关系
        this.trUserRoleMapper.deleteUserRoleByUserId(reqDto.getUserId());
        String roleId = reqDto.getRoleId();
        TsRoleEntity roleEntity = this.tsRoleMapper.selectByPrimaryKey(roleId);
        if (null == roleEntity) {
            log.info("User does not exist. roleId={}", roleId);
            throw new BusiException("角色信息不存在!");
        }
        TrUserRoleEntity userRoleEntity = new TrUserRoleEntity();
        userRoleEntity.setPkId(UuidUtil.getUuid());
        userRoleEntity.setUserId(reqDto.getUserId());
        userRoleEntity.setRoleId(roleId);
        userRoleEntity.setCreateTime(new Date());
        userRoleEntity.setLastUpdateTime(new Date());
        userRoleEntity.setCreateUser(LoginUtil.getUserId());
        userRoleEntity.setLastUpdateUser(LoginUtil.getUserId());
        int i = this.trUserRoleMapper.insertSelective(userRoleEntity);
        Assert.isTrue(i == SortConstant.ONE, "绑定角色失败!");

    }

    /**
     * 验证用户的用户名和密码
     */
    private List<TsUserEntity> checkUser(TsUserReqDto reqDto){
        String userName = reqDto.getUserName();
        String password = reqDto.getPassword();
        TsUserEntityExample userEntityExample = new TsUserEntityExample();
        TsUserEntityExample.Criteria criteria = userEntityExample.createCriteria();
        criteria.andUserNameEqualTo(userName);
        List<TsUserEntity> sysUserEntityList = this.tsUserMapper.selectByExample(userEntityExample);
        Assert.notEmpty(sysUserEntityList , "该用户名不存在!");
        TsUserEntity userEntity = sysUserEntityList.get(SortConstant.ZERO);
        String pwdInDb = userEntity.getPassword();
        password = MD5Util.getInstance().encryptBySalt(password);
        boolean flag = password.equals(pwdInDb);
        Assert.isTrue(flag , "用户名和密码不匹配!");
        return  sysUserEntityList;
    }
}
