package com.cl.ysyd.aop;

import com.cl.ysyd.common.constants.AopConstant;
import com.cl.ysyd.common.exception.BusiException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/*@Aspect
@Component*/
public class AopContactInfo {

    private static final Integer COUNT = 5;

    private static final long interval = 5 * 60 * 1000;

    private static final Integer INITIAL = 1;

    private Long startTime;


    //@Before("within(com.cl.ysyd.controller..*)")
    public void requestLimit()throws BusiException {
        //获取httprequest
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        //HttpServletResponse response = attributes.getResponse();
        if(null == request){
            throw new BusiException("HttpServletRequest有误...");
        }
        String ip = request.getRemoteAddr();
        String uri = request.getRequestURI();
        String mapKey = "limit-ip-request:" + uri + ":" + ip;
        Integer count = AopConstant.ipCountMap.get(mapKey);
        if(null == count){
            AopConstant.ipCountMap.put(mapKey, INITIAL);
            startTime = System.currentTimeMillis();
        }else {
            if(System.currentTimeMillis() - startTime < interval){
                if(count >= COUNT){
                    throw new BusiException("您的操作太过频繁,请稍后再试!");
                }else{
                    AopConstant.ipCountMap.put(mapKey, count + INITIAL);
                }
            }else{
                AopConstant.ipCountMap.put(mapKey, INITIAL);
                startTime = System.currentTimeMillis();
            }
        }

    }


}
