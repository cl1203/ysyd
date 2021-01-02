package com.cl.ysyd.service.sys.impl;

import com.cl.ysyd.common.constants.AopConstant;
import com.cl.ysyd.common.constants.SortConstant;
import com.cl.ysyd.common.constants.TokenInfo;
import com.cl.ysyd.common.exception.BusiException;
import com.cl.ysyd.common.utils.MD5Util;
import com.cl.ysyd.common.utils.UuidUtil;
import com.cl.ysyd.dto.sys.req.LoginReqDto;
import com.cl.ysyd.dto.sys.res.LoginResDto;
import com.cl.ysyd.dto.sys.res.TsRoleResDto;
import com.cl.ysyd.dto.sys.res.TsUserResDto;
import com.cl.ysyd.entity.sys.TsRoleEntity;
import com.cl.ysyd.entity.sys.TsUserEntity;
import com.cl.ysyd.mapper.sys.TrUserRoleMapper;
import com.cl.ysyd.mapper.sys.TsRoleMapper;
import com.cl.ysyd.mapper.sys.TsUserMapper;
import com.cl.ysyd.service.sys.ILoginService;
import com.cl.ysyd.service.sys.IRoleService;
import com.cl.ysyd.service.sys.helper.RoleHelper;
import com.cl.ysyd.service.sys.helper.UserHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

@Service
@Slf4j
@Transactional
public class LoginServiceImpl implements ILoginService {

    @Autowired
    private TsUserMapper userMapper;

    @Autowired
    private UserHelper userHelper;

    @Autowired
    private RoleHelper roleHelper;

    @Autowired
    private TrUserRoleMapper userRoleMapper;

    @Autowired
    private TsRoleMapper roleMapper;

    @Autowired
    private IRoleService roleService;

    @Override
    public LoginResDto login(LoginReqDto reqDto) {
        LoginResDto resDto = new LoginResDto();
        //验证用户信息
        TsUserEntity userEntity = this.checkUser(reqDto);
        //用户信息
        TsUserResDto userResDto = this.userHelper.editResDto(userEntity);
        resDto.setUserResDto(userResDto);
        //根据用户ID查询角色ID
        List<String> roleIdList = this.userRoleMapper.getUserRoleIdList(userEntity.getPkId());
        if(CollectionUtils.isEmpty(roleIdList)){
            throw new BusiException("用户未绑定角色, 请联系管理员!");
        }
        String roleId = roleIdList.get(SortConstant.ZERO);
        //调用接口 根据角色ID查询对应的菜单信息   (和按钮信息)
        TsRoleResDto roleResDto = this.roleService.queryByPrimaryKey(roleId);
        resDto.setMenuResDtoList(roleResDto.getMenuResDtoList());
        TsRoleEntity entity = this.roleMapper.selectByPrimaryKey(roleId);
        resDto.setRoleResDto(this.roleHelper.editResDto(entity));
        String token = UuidUtil.getUuid();
        // token存入缓存中
        String userId = userEntity.getPkId();
        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setDate(System.currentTimeMillis());
        tokenInfo.setToken(token);
        AopConstant.currentLoginTokenMap.put(userId,tokenInfo);
        resDto.setToken(token);
        return resDto;
    }

    private TsUserEntity checkUser(LoginReqDto reqDto){
        String userName = reqDto.getUserName();
        String password = reqDto.getPassword();
        List<TsUserEntity> userEntityList = this.userMapper.queyByUserName(userName);
        Assert.notEmpty(userEntityList, "用户名不存在!");
        TsUserEntity userEntity = userEntityList.get(SortConstant.ZERO);
        String pwdInDb = userEntity.getPassword();
        password = MD5Util.getInstance().encryptBySalt(password);
        boolean flag = password.equals(pwdInDb);
        Assert.isTrue(flag , "用户名和密码不匹配!");
        return userEntity;
    }

}
