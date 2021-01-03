/**
 * TcBizDictionaryMapper.java
 * Created at 2020-04-02
 * Created by xieyb
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.mapper.sys;

import com.cl.ysyd.common.base.IBaseMapper;
import com.cl.ysyd.common.constants.SortConstant;
import com.cl.ysyd.common.enums.DictType;
import com.cl.ysyd.entity.sys.TcBizDictionaryEntity;
import com.cl.ysyd.entity.sys.TcBizDictionaryEntityExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

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

    /**
     * 根据当前订单状态查询下一个订单状态
     * @param orderStatus
     * @return
     */
    default String getOrderStatusNext(String orderStatus){
        //根据订单状态查询所对应的状态字段seq 然后+1 为下一个状态
        TcBizDictionaryEntityExample tcBizDictionaryEntityExample = new TcBizDictionaryEntityExample();
        TcBizDictionaryEntityExample.Criteria criteria = tcBizDictionaryEntityExample.createCriteria();
        criteria.andBizTypeEqualTo(DictType.ORDER_STATUS.getCode());
        criteria.andBizCodeEqualTo(orderStatus);
        List<TcBizDictionaryEntity> tcBizDictionaryEntities1 = this.selectByExample(tcBizDictionaryEntityExample);
        Assert.notEmpty(tcBizDictionaryEntities1, "订单当前处于状态不存在!  数据有误请联系管理员!");
        int seq = tcBizDictionaryEntities1.get(SortConstant.ZERO).getSeq() + SortConstant.ONE;
        Assert.isTrue(seq > 4, "此订单状态是根据系统流程自动变换, 请勿操作!");
        Assert.isTrue(seq < 9, "订单已经是已完成状态, 请勿再次操作!" );
        TcBizDictionaryEntityExample tcBizDictionaryEntityExample2 = new TcBizDictionaryEntityExample();
        TcBizDictionaryEntityExample.Criteria criteria2 = tcBizDictionaryEntityExample2.createCriteria();
        criteria2.andBizTypeEqualTo(DictType.ORDER_STATUS.getCode());
        criteria2.andSeqEqualTo(seq);
        List<TcBizDictionaryEntity> tcBizDictionaryEntities2 = this.selectByExample(tcBizDictionaryEntityExample2);
        Assert.notEmpty(tcBizDictionaryEntities2, "订单状态对应的下一个状态已经不存在, 数据错误, 请联系管理员!");
        return  tcBizDictionaryEntities2.get(SortConstant.ZERO).getBizCode();
    }

    default int querySeq(String bizType, String bizText){
        TcBizDictionaryEntityExample tcBizDictionaryEntityExample = new TcBizDictionaryEntityExample();
        TcBizDictionaryEntityExample.Criteria criteria = tcBizDictionaryEntityExample.createCriteria();
        criteria.andBizTypeEqualTo(bizType);
        criteria.andBizTextEqualTo(bizText);
        List<TcBizDictionaryEntity> tcBizDictionaryEntities = this.selectByExample(tcBizDictionaryEntityExample);
        return tcBizDictionaryEntities.get(SortConstant.ZERO).getSeq();
    }

}
