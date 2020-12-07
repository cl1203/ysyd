/**
 * IBizDictionaryService.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.service.sys;

import com.cl.ysyd.dto.sys.req.DictReqDto;
import com.cl.ysyd.dto.sys.res.DictCodeResDto;
import com.cl.ysyd.dto.sys.res.DictResDto;

import java.util.List;
import java.util.Set;

/**
 * 数据字典表 service接口类
 * @author chenlong  2020-11-24 18:47:26
 */
public interface IBizDictionaryService {
    /**
     * 根据类型获取对应编码列表
     *
     * @param bizType 类型
     *
     * @return 编码列表
     */
    List<DictCodeResDto> queryListByBizType(String bizType);

    /**
     * 根据类型和编码获取对应文本内容
     *
     * @param bizType 类型
     * @param bizCode 编码
     *
     * @return 编码文本内容
     */
    String getTextByBizCode(String bizType, String bizCode);

    /**
     * 定时加载数据字典缓存（间隔5分钟）
     *
     * @throws Exception 异常信息
     */
    void loadDataTask() throws Exception;

    /**
     * 字典编码查询
     *
     * @param bizType 类型
     * @return List<DictionaryResDto> 编码查询结果
     */
    List<DictResDto> queryDictionaryCode(String bizType);

    /**
     * 字典编码新增
     *
     * @param dictReqDto 字典编码dto
     * @return
     */
    int createDictionaryCode(DictReqDto dictReqDto);

    /**
     * 字典编码修改
     *
     * @param dictId     字典编码pkId
     * @param dictReqDto 字典编码dto
     */
    int updateDictionaryCode(String dictId, DictReqDto dictReqDto);

    /**
     * 字典编码删除
     *
     * @param dictId pkId
     */
    int deleteDictionaryCode(String dictId);

    /**
     * 字典类型下拉框
     *
     * @return 字典类型集合
     */
    Set<String> queryDictionaryType();

    void reload();
}