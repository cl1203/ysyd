/**
 * TcBizDictionaryMapper.java
 * Created at 2020-04-02
 * Created by xieyb
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.mapper.sys;

import com.cl.ysyd.common.base.IBaseMapper;
import com.cl.ysyd.entity.sys.TcBizDictionaryEntity;
import com.cl.ysyd.entity.sys.TcBizDictionaryEntityExample;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 数据字典表 mapper类
 */
public interface TcBizDictionaryMapper extends IBaseMapper<TcBizDictionaryEntity, TcBizDictionaryEntityExample, String> {

    /**
     * 根据业务类型查询字典列表
     *
     * @param bizType
     * @return
     */
    default List<TcBizDictionaryEntity> getListByBizType(String bizType) {
        TcBizDictionaryEntityExample example = new TcBizDictionaryEntityExample();
        TcBizDictionaryEntityExample.Criteria criteria = example.createCriteria();
        if(StringUtils.isNotBlank(bizType)) {
            criteria.andBizTypeEqualTo(bizType);
        }
        example.setOrderByClause("SEQ ASC, BIZ_CODE ASC");
        return selectByExample(example);
    }

    /**
     * 根据业务编码查询文案
     *
     * @param bizType
     * @param bizCode
     * @return
     */
    default String getTextByBizCode(String bizType, String bizCode) {
        TcBizDictionaryEntityExample example = new TcBizDictionaryEntityExample();
        TcBizDictionaryEntityExample.Criteria criteria = example.createCriteria();
        criteria.andBizTypeEqualTo(bizType).andBizCodeEqualTo(bizCode);
        List<TcBizDictionaryEntity> list = selectByExample(example);
        if (list != null && !list.isEmpty()) {
            return list.get(0).getBizText();
        }
        return "";
    }

}