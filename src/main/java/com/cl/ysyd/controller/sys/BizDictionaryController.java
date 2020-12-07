/**
 * BizDictionaryController.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.controller.sys;

import com.cl.ysyd.common.constants.ResponseData;
import com.cl.ysyd.dto.sys.req.DictReqDto;
import com.cl.ysyd.dto.sys.res.DictCodeResDto;
import com.cl.ysyd.dto.sys.res.DictResDto;
import com.cl.ysyd.service.sys.IBizDictionaryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

/**
 * 数据字典表控制层
 * @author chenlong  2020-11-24
 */
@RestController
@Slf4j
@Api(tags="数据字典表接口")
@RequestMapping(value = "/v1/bizDictionary")
@CrossOrigin
public class BizDictionaryController {
    /**
     * 字典服务类
     */
    @Autowired
    private IBizDictionaryService dictService;

    @ApiOperation(value = "单类型编码查询接口")
    @GetMapping(path = "/type/{type}")
    public ResponseData<List<DictCodeResDto>> queryCodeListByType(
            @ApiParam(value = "类型", required = true) @PathVariable String type) {
        log.info("Controller queryCodeListByType start. type={}", type);

        List<DictCodeResDto> codeList = this.dictService.queryListByBizType(type);

        log.info("Controller queryCodeListByType end.");
        return new ResponseData<>(codeList);
    }

    @ApiOperation(value = "单类型编码查询接口")
    @GetMapping(path = "/portal/type/{type}")
    public ResponseData<List<DictCodeResDto>> queryCodeListByTypePortal(
            @ApiParam(value = "类型", required = true) @PathVariable String type) {
        log.info("Controller queryCodeListByType start. type={}", type);

        List<DictCodeResDto> codeList = this.dictService.queryListByBizType(type);

        log.info("Controller queryCodeListByType end.");
        return new ResponseData<>(codeList);
    }

    @ApiOperation(value = "多类型编码查询接口")
    @GetMapping(path = "/types")
    public ResponseData<Map<String, List<DictCodeResDto>>> queryMutiCodeListByTypes(
            @ApiParam(value = "类型(逗号分割)", required = true) @RequestParam(required = false) String types) {
        log.info("Controller queryMutiCodeListByTypes start. types={}", types);
        Map<String, List<DictCodeResDto>> retMap = new HashMap<>();

        if (StringUtils.isEmpty(types)) {
            return new ResponseData<>(retMap);
        }

        String[] bizTypes = types.split(",");
        Stream.of(bizTypes).forEach(type -> {
            List<DictCodeResDto> codeList = this.dictService.queryListByBizType(type);
            retMap.put(type, codeList);
        });

        log.info("Controller queryMutiCodeListByTypes end.");
        return new ResponseData<>(retMap);
    }

    @ApiOperation(value = "多类型编码查询接口")
    @GetMapping(path = "/portal/types")
    public ResponseData<Map<String, List<DictCodeResDto>>> queryMutiCodeListByTypesPortal(
            @ApiParam(value = "类型(逗号分割)", required = true) @RequestParam(required = false) String types) {
        log.info("Controller queryMutiCodeListByTypes start. types={}", types);
        Map<String, List<DictCodeResDto>> retMap = new HashMap<>();

        if (StringUtils.isEmpty(types)) {
            return new ResponseData<>(retMap);
        }

        String[] bizTypes = types.split(",");
        Stream.of(bizTypes).forEach(type -> {
            List<DictCodeResDto> codeList = this.dictService.queryListByBizType(type);
            retMap.put(type, codeList);
        });

        log.info("Controller queryMutiCodeListByTypes end.");
        return new ResponseData<>(retMap);
    }

    /**
     * 字典编码查询
     *
     * @param bizType 字典类型
     *
     * @return 字典编码查询结果
     */
    @ApiOperation(value = "字典编码查询")
    @GetMapping(path = "/man/codes")
    public ResponseData<List<DictResDto>> queryDictionaryCode(String bizType) {
        log.info("Controller queryDictionaryCode start. bizType={}", bizType);
        List<DictResDto> list = this.dictService.queryDictionaryCode(bizType);
        log.info("Controller queryDictionaryCode end.");
        return new ResponseData<>(list);
    }

    /**
     * 字典编码新增
     *
     * @param dictReqDto 字典编码dto
     * @return 新增结果
     */
    @ApiOperation(value = "字典编码新增")
    @PostMapping(path = "/man/codes")
    public ResponseData<Integer> createDictionaryCode(@RequestBody DictReqDto dictReqDto) {
        log.info("Controller createDictionaryCode start.");

        int ret = this.dictService.createDictionaryCode(dictReqDto);

        log.info("Controller createDictionaryCode end. ret={}", ret);
        return new ResponseData<>(ret);
    }

    /**
     * 字典编码修改
     *
     * @param dictReqDto 字典编码dto
     * @param pkId       pkId
     * @return 修改结果
     */
    @ApiOperation(value = "字典编码修改")
    @PutMapping(path = "/man/codes/{pkId}")
    public ResponseData<Integer> updateDictionaryCode(@PathVariable String pkId, @RequestBody DictReqDto dictReqDto) {
        log.info("Controller updateDictionaryCode start. pkId={}", pkId);

        int ret = this.dictService.updateDictionaryCode(pkId, dictReqDto);

        log.info("Controller updateDictionaryCode end. ret={}", ret);
        return new ResponseData<>(ret);
    }

    /**
     * 字典编码删除
     *
     * @param pkId 主键
     * @return 删除结果
     */
    @ApiOperation(value = "字典编码删除")
    @DeleteMapping(path = "/man/codes/{pkId}")
    public ResponseData<Integer> deleteDictionaryCode(@PathVariable String pkId) {
        log.info("Controller deleteDictionaryCode start. pkId={}", pkId);

        int ret = this.dictService.deleteDictionaryCode(pkId);

        log.info("Controller deleteDictionaryCode end. ret={}", ret);
        return new ResponseData<>(ret);
    }

    /**
     * 字典类型下拉框
     *
     * @return 字典类型下拉框结果
     */
    @ApiOperation(value = "字典类型下拉框")
    @GetMapping(path = "/man/types")
    public ResponseData<Set<String>> queryDictionaryType() {
        log.info("Controller queryDictionaryType start.");
        Set<String> typeList = this.dictService.queryDictionaryType();
        log.info("Controller queryDictionaryType end.");
        return new ResponseData<>(typeList);
    }
}