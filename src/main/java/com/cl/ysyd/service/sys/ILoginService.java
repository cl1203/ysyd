package com.cl.ysyd.service.sys;

import com.cl.ysyd.dto.sys.req.LoginReqDto;
import com.cl.ysyd.dto.sys.res.LoginResDto;

public interface ILoginService {

    /**
     * @Author 陈龙
     * @Description 登录
     * @Date 14:50 2019/8/16
     * @Param [reqBeanModel]
     * @return com.cl.bean.res.LoginResBean
     **/
    LoginResDto login(LoginReqDto reqDto);

}
