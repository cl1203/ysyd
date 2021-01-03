package com.cl.ysyd.controller.order;


import com.cl.ysyd.aop.LoggerManage;
import com.cl.ysyd.common.constants.ResponseData;
import com.cl.ysyd.dto.order.res.NoticeTopResDto;
import com.cl.ysyd.dto.order.res.SectorResDto;
import com.cl.ysyd.service.order.IOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 订单控制层
 * @author chenlong  2020-12-07
 */
@RestController
@Slf4j
@Api(tags="首页&看板接口")
@RequestMapping(value = "/v1/notice")
@CrossOrigin
public class NoticeController {


    /**
     * 订单service
     */
    @Autowired
    private IOrderService iOrderService;

    /**
     * 查询首页&看板 上部分内容
     * @return 查询结果
     */
    @ApiOperation(value = "查询首页&看板上部分")
    @GetMapping(value = "/queryTop")
    @LoggerManage(description = "查询首页&看板上部分")
    public ResponseData<NoticeTopResDto> queryTop(){
        log.info("NoticeController queryNoticeTop start.");
        NoticeTopResDto resDto =  this.iOrderService.queryTop();
        log.info("NoticeController queryNoticeTop end.");
        return new ResponseData<>(resDto);
    }

    /**
     * 查询首页&看板 曲线部分内容
     * @return 查询结果
     */
    @ApiOperation(value = "查询首页&看板 曲线部分内容")
    @GetMapping(value = "/queryCurve/{year}")
    @LoggerManage(description = "查询首页&查询首页&看板 曲线部分内容")
    public ResponseData<Map<String, List<Integer>>> queryCurve(@PathVariable String year){
        log.info("NoticeController queryCurve start.");
        Map<String, List<Integer>> map =  this.iOrderService.queryCurve(year);
        log.info("NoticeController queryCurve end.");
        return new ResponseData<>(map);
    }

    /**
     * 查询首页&看板 曲线部分内容
     * @return 查询结果
     */
    @ApiOperation(value = "查询首页&看板 扇形部分内容")
    @GetMapping(value = "/querySector/{year}/{month}")
    @LoggerManage(description = "查询首页&查询首页&看板 扇形部分内容")
    public ResponseData<List<SectorResDto>> querySector(@PathVariable String year, @PathVariable String month){
        log.info("NoticeController queryCurve start.");
        List<SectorResDto> list =  this.iOrderService.querySector(year, month);
        log.info("NoticeController queryCurve end.");
        return new ResponseData<>(list);
    }

}
