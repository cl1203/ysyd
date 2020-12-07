package com.cl.ysyd.controller.sys;

import com.cl.ysyd.aop.LoggerManage;
import com.cl.ysyd.common.constants.ResponseData;
import com.cl.ysyd.dto.sys.req.LoginReqDto;
import com.cl.ysyd.dto.sys.res.LoginResDto;
import com.cl.ysyd.service.sys.ILoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/v1/user")
@CrossOrigin
@Slf4j
@Api(tags="登录接口")
public class LoginController {

    @Resource
    private ILoginService loginService;


    @PostMapping("/login")
    @ApiOperation(value = "登录" , notes = "用户登录")
    @LoggerManage(description = "用户登录")
    public ResponseData<LoginResDto> login(@RequestBody @Valid LoginReqDto loginReqDto){
        LoginResDto resDto = this.loginService.login(loginReqDto);
        return new ResponseData<>(resDto);
    }

}
