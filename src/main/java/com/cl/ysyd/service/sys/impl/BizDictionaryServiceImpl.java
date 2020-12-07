/**
 * BizDictionaryServiceImpl.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.service.sys.impl;

import com.cl.ysyd.common.exception.BusiException;
import com.cl.ysyd.common.utils.UuidUtil;
import com.cl.ysyd.dto.sys.req.DictReqDto;
import com.cl.ysyd.dto.sys.res.DictCodeResDto;
import com.cl.ysyd.dto.sys.res.DictResDto;
import com.cl.ysyd.entity.sys.TcBizDictionaryEntity;
import com.cl.ysyd.entity.sys.TcBizDictionaryEntityExample;
import com.cl.ysyd.mapper.sys.TcBizDictionaryMapper;
import com.cl.ysyd.service.sys.IBizDictionaryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 数据字典表service实现类
 * @author chenlong  2020-11-24
 */
@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRED)
public class BizDictionaryServiceImpl implements IBizDictionaryService, InitializingBean {

    /**
     * List格式数据缓存
     */
    private Map<String, List<DictCodeResDto>> listItemData = new HashMap<>();

    /**
     * Map格式数据缓存
     */
    private Map<String, Map<String, DictCodeResDto>> mapItemData = new HashMap<>();

    /**
     * 字典Mapper
     */
    @Autowired
    private TcBizDictionaryMapper tcBizDictionaryMapper;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.reloadData();
    }

    @Override
    public List<DictCodeResDto> queryListByBizType(String bizType) {
        log.info("Service queryListByBizType start. bizType={}", bizType);
        List<DictCodeResDto> dictItemList = this.listItemData.get(bizType);
        if (dictItemList != null) {
            log.info("Service queryListByBizType end. size={}", dictItemList.size());
            return dictItemList;
        }
        log.info("Service queryListByBizType end. Data empty.");
        return new ArrayList<>();
    }

    @Override
    public String getTextByBizCode(String bizType, String bizCode) {
        log.info("Service getTextByBizCode start. bizType={}, bizCode={}", bizType, bizCode);
        Map<String, DictCodeResDto> map = this.mapItemData.get(bizType);
        if (map != null) {
            DictCodeResDto item = map.get(bizCode);
            if (item != null) {
                log.info("Service getTextByBizCode end. text={}", item.getBizText());
                return item.getBizText();
            }
        }
        log.info("Service getTextByBizCode end. Text empty.");
        return "";
    }

    @Override
    @Scheduled(cron = "0 0/5 * * * ?")
    public void loadDataTask() throws Exception {
        this.reloadData();
        log.info("Service loadDataTask executed.");
    }

    /**
     * 重新加载字典缓存数据
     */
    private void reloadData() {
        log.info("Method reloadData start.");

        Map<String, List<DictCodeResDto>> listItemDataTemp = new HashMap<>();
        Map<String, Map<String, DictCodeResDto>> mapItemDataTemp = new HashMap<>();

        /**
         * 查询所有的字典业务类型
         */
        Map<String, String> bizTypeMap = new HashMap<>();
        TcBizDictionaryEntityExample example = new TcBizDictionaryEntityExample();
        List<TcBizDictionaryEntity> entityList = this.tcBizDictionaryMapper.selectByExample(example);
        if (entityList == null || entityList.isEmpty()) {
            return;
        }
        entityList.forEach(entity -> {
            bizTypeMap.put(entity.getBizType(), entity.getBizType());
        });

        /**
         * List格式数据缓存
         */
        bizTypeMap.forEach((key, value) -> {
            List<DictCodeResDto> dictItemList = new ArrayList<>();
            Map<String, DictCodeResDto> dictItemMap = new HashMap<>();
            List<TcBizDictionaryEntity> list = this.tcBizDictionaryMapper.getListByBizType(key);
            if (list != null) {
                list.forEach(entity -> {
                    DictCodeResDto item = new DictCodeResDto();
                    item.setBizCode(entity.getBizCode());
                    item.setBizText(entity.getBizText());
                    item.setDescription(entity.getDescription());
                    dictItemMap.put(entity.getBizCode(), item);
                    dictItemList.add(item);
                });
            }

            mapItemDataTemp.put(key, dictItemMap);
            listItemDataTemp.put(key, dictItemList);
        });

        this.listItemData = listItemDataTemp;
        this.mapItemData = mapItemDataTemp;

        log.info("Method reloadData end. Successfully.");
    }

    @Override
    public List<DictResDto> queryDictionaryCode(String bizType) {
        log.info("Service queryDictionaryCode start.");
        List<TcBizDictionaryEntity> list = this.tcBizDictionaryMapper.getListByBizType(bizType);
        List<DictResDto> dictResDtoList = new ArrayList<>();
        for (TcBizDictionaryEntity tcBizDictionaryEntity : list) {
            DictResDto dictResDto = new DictResDto();
            dictResDto.setPkId(tcBizDictionaryEntity.getPkId());
            dictResDto.setBizCode(tcBizDictionaryEntity.getBizCode());
            dictResDto.setBizText(tcBizDictionaryEntity.getBizText());
            dictResDto.setBizType(tcBizDictionaryEntity.getBizType());
            dictResDto.setDescription(tcBizDictionaryEntity.getDescription());
            Integer seq = tcBizDictionaryEntity.getSeq();
            if (null != seq) {
                dictResDto.setSeq(String.valueOf(seq));
            } else {
                dictResDto.setSeq(null);
            }
            dictResDtoList.add(dictResDto);
        }
        log.info("Service queryDictionaryCode end.");
        return dictResDtoList;
    }

    @Override
    public int createDictionaryCode(DictReqDto dictReqDto) {
        log.info("Service createDictionaryCode start.");
        if (StringUtils.isBlank(dictReqDto.getSeq())) {
            dictReqDto.setSeq(null);
        }
        TcBizDictionaryEntity entity = new TcBizDictionaryEntity();
        entity.setPkId(UuidUtil.getUuid());
        entity.setBizCode(dictReqDto.getBizCode());
        entity.setBizText(dictReqDto.getBizText());
        entity.setBizType(dictReqDto.getBizType());
        entity.setDescription(dictReqDto.getDescription());
        // 查询该类型的排序最大值
        entity.setSeq(Integer.valueOf(dictReqDto.getSeq()));
        //entity.setCreateUser(LoginUtil.getLoginUserId());
        //entity.setLastUpdateUser(LoginUtil.getLoginUserId());
        entity.setCreateTime(new Date());
        entity.setLastUpdateTime(new Date());

        int ret = this.tcBizDictionaryMapper.insert(entity);
        log.info("Service createDictionaryCode end. ret={}", ret);
        return ret;
    }

    @Override
    public int updateDictionaryCode(String dictId, DictReqDto dictReqDto) {
        log.info("Service updateDictionaryCode start. dictId={}", dictId);
        if (StringUtils.isBlank(dictReqDto.getSeq())) {
            dictReqDto.setSeq(null);
        }
        TcBizDictionaryEntity entity = this.tcBizDictionaryMapper.selectByPrimaryKey(dictId);
        if (null == entity) {
            throw new BusiException("数据不存在");
        }
        TcBizDictionaryEntity ticEntity = new TcBizDictionaryEntity();
        ticEntity.setPkId(dictId);
        ticEntity.setBizCode(dictReqDto.getBizCode());
        ticEntity.setBizText(dictReqDto.getBizText());
        ticEntity.setBizType(dictReqDto.getBizType());
        ticEntity.setSeq(Integer.valueOf(dictReqDto.getSeq()));
        ticEntity.setDescription(dictReqDto.getDescription());
        //ticEntity.setLastUpdateUser(LoginUtil.getLoginUserId());
        ticEntity.setLastUpdateTime(new Date());

        int ret = this.tcBizDictionaryMapper.updateByPrimaryKeySelective(ticEntity);
        log.info("Service updateDictionaryCode end. ret={}", ret);
        return ret;
    }

    @Override
    public int deleteDictionaryCode(String dictId) {
        log.info("Service deleteDictionaryCode start. dictId={}", dictId);
        int ret = this.tcBizDictionaryMapper.deleteByPrimaryKey(dictId);
        log.info("Service deleteDictionaryCode end. ret={}", ret);
        return ret;
    }

    @Override
    public Set<String> queryDictionaryType() {
        log.info("Service queryDictionaryType start.");
        Set<String> set = new TreeSet<>();
        TcBizDictionaryEntityExample example = new TcBizDictionaryEntityExample();
        example.setOrderByClause("BIZ_CODE ASC");
        List<TcBizDictionaryEntity> dicList = this.tcBizDictionaryMapper.selectByExample(example);
        for (TcBizDictionaryEntity tcBizDictionaryEntity : dicList) {
            set.add(tcBizDictionaryEntity.getBizType());
        }
        log.info("Service queryDictionaryType end.");
        return set;
    }

    @Override
    public void reload() {
        this.reloadData();
        log.info("Service loadDataTask executed.");
    }
}