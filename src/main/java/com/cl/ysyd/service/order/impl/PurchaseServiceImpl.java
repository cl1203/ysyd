/**
 * PurchaseServiceImpl.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.service.order.impl;

import com.cl.ysyd.common.constants.SortConstant;
import com.cl.ysyd.common.enums.OrderStatusEnum;
import com.cl.ysyd.common.enums.PurchaseStatusEnum;
import com.cl.ysyd.common.exception.BusiException;
import com.cl.ysyd.common.utils.CheckMatchAndSpaceUtil;
import com.cl.ysyd.common.utils.LoginUtil;
import com.cl.ysyd.common.utils.UuidUtil;
import com.cl.ysyd.dto.order.req.TmPurchaseDetailReqDto;
import com.cl.ysyd.dto.order.req.TmPurchaseReqDto;
import com.cl.ysyd.dto.order.res.TmPurchaseBillResDto;
import com.cl.ysyd.dto.order.res.TmPurchaseDetailResDto;
import com.cl.ysyd.dto.order.res.TmPurchaseResDto;
import com.cl.ysyd.entity.order.TmOrderEntity;
import com.cl.ysyd.entity.order.TmPurchaseDetailEntity;
import com.cl.ysyd.entity.order.TmPurchaseEntity;
import com.cl.ysyd.entity.sys.TsRoleEntity;
import com.cl.ysyd.entity.sys.TsUserEntity;
import com.cl.ysyd.mapper.order.TmOrderMapper;
import com.cl.ysyd.mapper.order.TmPurchaseDetailMapper;
import com.cl.ysyd.mapper.order.TmPurchaseMapper;
import com.cl.ysyd.mapper.sys.TsRoleMapper;
import com.cl.ysyd.mapper.sys.TsUserMapper;
import com.cl.ysyd.service.order.IOrderService;
import com.cl.ysyd.service.order.IPurchaseDetailService;
import com.cl.ysyd.service.order.IPurchaseService;
import com.cl.ysyd.service.order.helper.PurchaseDetailHelper;
import com.cl.ysyd.service.order.helper.PurchaseHelper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 采购单service实现类
 * @author chenlong  2020-11-24
 */
@Service
@Slf4j
@Transactional
public class PurchaseServiceImpl implements IPurchaseService {
    /**
     * 采购单Mapper
     */
    @Autowired
    private TmPurchaseMapper tmPurchaseMapper;

    @Autowired
    private TsUserMapper userMapper;

    private static final String P = "P";

    @Autowired
    private IPurchaseDetailService purchaseDetailService;

    @Autowired
    private TmPurchaseDetailMapper tmPurchaseDetailMapper;

    @Autowired
    private IOrderService iOrderService;
    /**
     * 采购单明细Helper
     */
    @Autowired
    private PurchaseDetailHelper purchaseDetailHelper;

    @Autowired
    private TsRoleMapper roleMapper;

    /**
     * 订单Mapper
     */
    @Autowired
    private TmOrderMapper tmOrderMapper;

    /**
     * 采购单Helper
     */
    @Autowired
    private PurchaseHelper purchaseHelper;

    /**
     * 采购导出第一页sheet名称
     */
    private static final String PURCHASE_TAB="采购信息";

    /**
     * 采购导出第二页sheet名称
     */
    private static final String PURCHASE_DETAIL_TAB="采购明细信息";

    /**
     * 采购信息表头
     */
    private static final String[] PURCHASE_TITLE={"采购单号", "订单号", "采购状态", "采购人员", "采购总金额", "状态", "备注"};
    /**
     * 采购明细信息表头
     */
    private static final String[] PURCHASE_DETAIL_TITLE={"采购单号", "采购编号", "物料名称", "克重","幅宽", "采购单价", "数量",
                                                "单位", "颜色", "供应商", "总金额", "采购日期", "状态", "备注"};

    private static final String CONTENT_TYPE = "application/octet-stream";

    private static final String ENCODING = "utf-8";

    private static final String FILE_NAME = "采购数据";

    @Override
    public int deleteByPrimaryKey(String pkId) {
        log.info("Service deleteByPrimaryKey start. primaryKey=【{}】",pkId);
        Assert.hasText(pkId, "主键id不能为空!");
        TmPurchaseEntity checkEntity = this.tmPurchaseMapper.selectByPrimaryKey(pkId);
        if (checkEntity == null) {
            log.info("根据主键 pkId【{}】查询信息不存在",pkId);
            throw new BusiException("数据不存在");
        }
        int ret = this.tmPurchaseMapper.deleteByPrimaryKey(pkId); 
        log.info("Service deleteByPrimaryKey end. ret=【{}】",ret);
        return ret;
    }

    @Override
    public TmPurchaseResDto queryByPrimaryKey(String pkId) {
        log.info("Service selectByPrimaryKey start. primaryKey=【{}】",pkId);
        Assert.hasText(pkId, "主键ID不能为空!");
        TmPurchaseEntity entity = this.tmPurchaseMapper.selectByPrimaryKey(pkId);
        if(null == entity){
            return null;
        }
        TmPurchaseResDto resDto = this.purchaseHelper.editResDto(entity);
        List<TmPurchaseDetailEntity> tmPurchaseDetailEntityList = this.tmPurchaseDetailMapper.queryByPurchaseNo(entity.getPurchaseNo());
        List<TmPurchaseDetailResDto> tmPurchaseDetailResDtos = this.purchaseDetailHelper.editResDtoList(tmPurchaseDetailEntityList);
        resDto.setPurchaseDetailReqDtoList(tmPurchaseDetailResDtos);
        log.info("Service selectByPrimaryKey end. res=【{}】",resDto);
        return resDto;
    }

    @Override
    public int createPurchase(TmPurchaseReqDto reqDto) {
        log.info("Service createPurchase start. reqDto=【{}】",reqDto);
        TmPurchaseEntity entity = this.purchaseHelper.editEntity(reqDto);
        TmOrderEntity tmOrderEntity = this.tmOrderMapper.selectByPrimaryKey(reqDto.getOrderId());
        Assert.notNull(tmOrderEntity, "订单号对应的订单不存在!");
        List<TmPurchaseEntity> purchaseEntityList = this.tmPurchaseMapper.queryByOrderNo(tmOrderEntity.getOrderNo());
        Assert.isTrue(purchaseEntityList.size()==SortConstant.ZERO, "订单已经生成采购单,请选择其他订单!");
        entity.setOrderNo(tmOrderEntity.getOrderNo());
        entity.setPurchaseNo(P + tmOrderEntity.getOrderNo());
        entity.setCreateTime(new Date());
        entity.setPkId(UuidUtil.getUuid());
        //新增采购单
        int ret = this.tmPurchaseMapper.insertSelective(entity);
        log.info("Service createPurchase end. ret=【{}】",ret);
        return ret;
    }

    @Override
    public int updateByPrimaryKey(String pkId, TmPurchaseReqDto reqDto) {
        log.info("Service updateByPrimaryKey start. pkId=【{}】, reqDto =【{}】",pkId,reqDto);
        if(StringUtils.isEmpty(pkId)) {
            throw new BusiException("参数错误,缺少pkId");
        }
        TmPurchaseEntity checkEntity = this.tmPurchaseMapper.selectByPrimaryKey(pkId);
        if (checkEntity == null) {
            log.info("根据主键 pkId【{}】查询信息不存在",pkId);
            throw new BusiException("数据不存在");
        }
        TmPurchaseEntity entity = this.purchaseHelper.editEntity(reqDto);
        entity.setPkId(pkId);
        //修改采购明细
        List<TmPurchaseDetailReqDto> purchaseDetailReqDtoList = reqDto.getPurchaseDetailReqDtoList();
        int i = SortConstant.ZERO;
        if(CollectionUtils.isNotEmpty(purchaseDetailReqDtoList)){
            //先删除所有的明细 再新增
            Assert.hasText(reqDto.getPurchaseNo(), "采购单号不能为空!");
            this.tmPurchaseDetailMapper.deleteByPurchaseNo(reqDto.getPurchaseNo());
            //采购总金额  等于每个明细总金额之和
            BigDecimal totalAmount = new BigDecimal("0");
            //订单状态 采购中
            TmOrderEntity tmOrderEntity = this.tmOrderMapper.queryByOrderNo(checkEntity.getOrderNo());
            //修改订单状态
            this.iOrderService.updateOrderStatus(tmOrderEntity.getPkId(), OrderStatusEnum.PURCHASING.getCode());
            //purchaseDetailReqDtoList.forEach(tmPurchaseDetailReqDto -> {
            for(TmPurchaseDetailReqDto tmPurchaseDetailReqDto :  purchaseDetailReqDtoList){
                tmPurchaseDetailReqDto.setPurchaseNo(reqDto.getPurchaseNo());
                tmPurchaseDetailReqDto.setPurchaseNumber(reqDto.getPurchaseNo() + "-" + (i + SortConstant.ONE));
                if(StringUtils.isNotBlank(tmPurchaseDetailReqDto.getUnitPrice())){
                    if(!CheckMatchAndSpaceUtil.match(SortConstant.REGEXP, tmPurchaseDetailReqDto.getUnitPrice())) {
                        throw new BusiException("采购单价不符合规则, 整数位最多8位, 小数位最多2位!");
                    }
                }
                if(StringUtils.isNotBlank(tmPurchaseDetailReqDto.getQuantity())){
                    if(!CheckMatchAndSpaceUtil.match(SortConstant.REGEXP_INT, tmPurchaseDetailReqDto.getQuantity())) {
                        throw new BusiException("数量不符合规则, 整数位最多10位, 不能有小数位!");
                    }
                }
                if(StringUtils.isNotBlank(tmPurchaseDetailReqDto.getUnitPrice()) && StringUtils.isNotBlank(tmPurchaseDetailReqDto.getQuantity())){
                    BigDecimal totalAmountDetail = new BigDecimal(tmPurchaseDetailReqDto.getUnitPrice()).multiply(new BigDecimal(tmPurchaseDetailReqDto.getQuantity())).setScale(SortConstant.TWO, BigDecimal.ROUND_HALF_UP);
                    tmPurchaseDetailReqDto.setTotalAmountDetail(totalAmountDetail);
                    totalAmount = totalAmount.add(totalAmountDetail);
                }
                this.purchaseDetailService.createPurchaseDetail(tmPurchaseDetailReqDto);
           }
            //采购状态 采购中
            entity.setPurchaseStatus(PurchaseStatusEnum.PURCHASE_ING.getCode());
            //采购总金额
            entity.setTotalAmount(totalAmount);
            entity.setLastUpdateUser(LoginUtil.getUserId());
            entity.setLastUpdateTime(new Date());
        }
        //修改采购表
        int ret = this.tmPurchaseMapper.updateByPrimaryKeySelective(entity);
        log.info("Service updateByPrimaryKey end. ret=【{}】",ret);
        return ret;
    }

    @Override
    public PageInfo<TmPurchaseResDto> queryPurchaseByPage(Integer pageNum, Integer pageSize, String orderNo, String purchaseNo, String purchaseStatus, String purchasePersonnel, String orderStatus) {
        //获取用户名
        String userId = this.getUserId();
        //获取是否拥有所有权限
        String isAll = this.getIsAll(userId);
        //PageHelper.orderBy("P.CREATE_TIME DESC");
        Page<TmPurchaseResDto> startPage = PageHelper.startPage(pageNum, pageSize);
        List<TmPurchaseEntity> purchaseEntityList = this.tmPurchaseMapper.queryPurchaseList(orderNo, purchaseNo, purchaseStatus, purchasePersonnel, orderStatus, isAll, userId);
        List<TmPurchaseResDto> purchaseResDtoList = this.purchaseHelper.editResDtoList(purchaseEntityList);
        PageInfo<TmPurchaseResDto> pageInfo = new PageInfo<>(startPage);
        pageInfo.setList(purchaseResDtoList);
        return pageInfo;
    }

    /**
     *
     * @param userId 用户id
     * @return 是否拥有所有权限
     */
    private String getIsAll(String userId) {
        TsRoleEntity tsRoleEntity = this.roleMapper.queryByUserId(userId);
        Assert.notNull(tsRoleEntity, "用户对应的角色为空!");
        return tsRoleEntity.getIsAll();
    }

    /**
     * 获取用户名
     * @return 用户id
     */
    private String getUserId() {
        String userId = LoginUtil.getUserId();
        Assert.hasText(userId, "用户ID为空!");
        TsUserEntity userEntity = this.userMapper.selectByPrimaryKey(userId);
        Assert.notNull(userEntity, "userId对应的用户不存在!");
        return userId;
    }

    @Override
    public PageInfo<TmPurchaseBillResDto> queryPurchaseBillByPage(Integer pageNum, Integer pageSize, String purchasePersonnel, String supplier, String purchaseDate) {
        //获取用户名
        String userId = this.getUserId();
        //获取是否拥有所有权限
        String isAll = this.getIsAll(userId);
        //PageHelper.orderBy("TP.CREATE_TIME DESC");
        Page<TmPurchaseBillResDto> startPage = PageHelper.startPage(pageNum, pageSize);
        List<TmPurchaseBillResDto> resDtoList = this.tmPurchaseMapper.queryBillPurchaseList(purchasePersonnel,  supplier, purchaseDate, isAll, userId);
        BigDecimal totalMoney = new BigDecimal(SortConstant.ZERO);
        if(CollectionUtils.isNotEmpty(resDtoList)){
            for(TmPurchaseBillResDto resDto : resDtoList){
                totalMoney = totalMoney.add(new BigDecimal(resDto.getUnitPrice())).setScale(SortConstant.TWO, BigDecimal.ROUND_HALF_UP);;
            }
            resDtoList.get(SortConstant.ZERO).setTotalMoney(totalMoney.toString());
        }
        PageInfo<TmPurchaseBillResDto> pageInfo = new PageInfo<>(startPage);
        pageInfo.setList(resDtoList);
        return pageInfo;
    }

    @Override
    public void export(HttpServletResponse response, String orderNo, String purchaseNo, String purchaseStatus, String purchasePersonnel, String orderStatus) throws IOException {
        //获取采购列表信息
        List<TmPurchaseResDto> purchaseResDtoList = this.queryPurchaseByPage(SortConstant.ONE, SortConstant.PAGE_SIZE, orderNo, purchaseNo, purchaseStatus, purchasePersonnel, orderStatus).getList();
        if(CollectionUtils.isNotEmpty(purchaseResDtoList)){
            List<String> purchaseNoList = purchaseResDtoList.stream().map(TmPurchaseResDto::getPurchaseNo).collect(Collectors.toList());
            List<TmPurchaseDetailEntity> tmPurchaseDetailEntities = this.tmPurchaseDetailMapper.queryByPurchaseNo(purchaseNoList);
            //采购明细list
            List<TmPurchaseDetailResDto> purchaseDetailResDtoList = this.purchaseDetailHelper.editResDtoList(tmPurchaseDetailEntities);
            //导出
            this.exportExcel(purchaseResDtoList, purchaseDetailResDtoList, response);
        }
    }

    @Override
    public void exportPurchaseBill(HttpServletResponse response, String purchasePersonnel, String supplier, String purchaseDate) throws IOException {
        //查询采购对账单
        List<TmPurchaseBillResDto> list = this.queryPurchaseBillByPage(SortConstant.ONE, SortConstant.PAGE_SIZE, purchasePersonnel, supplier, purchaseDate).getList();
    }

    private void exportExcel(List<TmPurchaseResDto> purchaseResDtoList, List<TmPurchaseDetailResDto> purchaseDetailResDtoList, HttpServletResponse response) throws IOException {
        //实例化HSSFWorkbook
        HSSFWorkbook workbook = new HSSFWorkbook();
        //sheet1
        this.createSheet1(purchaseResDtoList, workbook);
        //sheet2
        this.createSheet2(purchaseDetailResDtoList, workbook);
        //准备将Excel的输出流通过response输出到页面下载
        //八进制输出流
        response.setContentType(CONTENT_TYPE);
        response.setCharacterEncoding(ENCODING);
        //这后面可以设置导出Excel的名称，此例中名为student.xls（解决文件名称乱码问题）
        response.setHeader("content-disposition", "attachment;filename=" + new String(FILE_NAME.getBytes(), "ISO8859-1") + ".xls" );
        //刷新缓冲
        response.flushBuffer();
        //workbook将Excel写入到response的输出流中，供页面下载
        workbook.write(response.getOutputStream());
        workbook.close();
    }
    private void createSheet2(List<TmPurchaseDetailResDto> purchaseDetailResDtoList, HSSFWorkbook workbook) {
        //创建sheet
        HSSFSheet sheet = workbook.createSheet(PURCHASE_DETAIL_TAB);
        //设置表格列宽度
        sheet.setDefaultColumnWidth(SortConstant.TWENTY);
        //创建第一行表头
        HSSFRow headrow = sheet.createRow(SortConstant.ZERO);
        //高度
        headrow.setHeight(SortConstant.HEAD_HEIGHT);
        //添加标题
        for(int i = 0; i < PURCHASE_DETAIL_TITLE.length; i++){
            //标题的显示样式
            HSSFCellStyle headerStyle = workbook.createCellStyle();
            //设置样式
            HSSFFont font = this.getHssfFont(workbook, headerStyle);
            //字体大小
            font.setFontHeightInPoints(SortConstant.HEAD_FONT);
            //创建单元格
            HSSFCell cell = headrow.createCell(i);
            //标题写入单元格
            cell.setCellValue(PURCHASE_DETAIL_TITLE[i]);
            headerStyle.setFont(font);
            cell.setCellStyle(headerStyle);
        }
        if(CollectionUtils.isNotEmpty(purchaseDetailResDtoList)){
            //添加数据
            for(int i = 0; i< purchaseDetailResDtoList.size(); i++){
                int j = i + SortConstant.ONE;
                //创建行
                HSSFRow row = sheet.createRow(j);
                //高度
                row.setHeight(SortConstant.ROW_HEIGHT);
                //内容样式
                HSSFCellStyle headerStyle = workbook.createCellStyle();
                //设置样式
                HSSFFont font = this.getHssfFont(workbook, headerStyle);
                //字体大小
                font.setFontHeightInPoints(SortConstant.ROW_FONT);
                headerStyle.setFont(font);
                TmPurchaseDetailResDto resDto = purchaseDetailResDtoList.get(i);
                Cell cell0 = row.createCell(0);
                cell0.setCellValue(resDto.getPurchaseNo());
                cell0.setCellStyle(headerStyle);
                Cell cell1 = row.createCell(1);
                cell1.setCellValue(resDto.getPurchaseNumber());
                cell1.setCellStyle(headerStyle);
                Cell cell2 = row.createCell(2);
                cell2.setCellValue(resDto.getMaterielName());
                cell2.setCellStyle(headerStyle);
                Cell cell3 = row.createCell(3);
                if(null != resDto.getGramWeight()){
                    cell3.setCellValue(resDto.getGramWeight().toString());
                }
                cell3.setCellStyle(headerStyle);
                Cell cell4 = row.createCell(4);
                if(null != resDto.getWidthOfCloth()){
                    cell4.setCellValue(resDto.getWidthOfCloth().toString());
                }
                cell4.setCellStyle(headerStyle);
                Cell cell5 = row.createCell(5);
                if(null != resDto.getUnitPrice()){
                    cell5.setCellValue(resDto.getUnitPrice().toString());
                }
                cell5.setCellStyle(headerStyle);
                Cell cell6 = row.createCell(6);
                if(null != resDto.getQuantity()){
                    cell6.setCellValue(resDto.getQuantity().toString());
                }
                cell6.setCellStyle(headerStyle);
                Cell cell7 = row.createCell(7);
                cell7.setCellValue(resDto.getUnit());
                cell7.setCellStyle(headerStyle);
                Cell cell8 = row.createCell(8);
                cell8.setCellValue(resDto.getColour());
                cell8.setCellStyle(headerStyle);
                Cell cell9 = row.createCell(9);
                cell9.setCellValue(resDto.getSupplier());
                cell9.setCellStyle(headerStyle);
                Cell cell10 = row.createCell(10);
                if(null != resDto.getTotalAmount()){
                    cell10.setCellValue(resDto.getTotalAmount().toString());
                }
                cell10.setCellStyle(headerStyle);
                Cell cell11 = row.createCell(11);
                cell11.setCellValue(resDto.getPurchaseDate());
                cell11.setCellStyle(headerStyle);
                Cell cell12 = row.createCell(12);
                cell12.setCellValue(resDto.getStatusText());
                cell12.setCellStyle(headerStyle);
                Cell cell13 = row.createCell(13);
                cell13.setCellValue(resDto.getRemarks());
                cell13.setCellStyle(headerStyle);
            }
        }
    }


    private void createSheet1(List<TmPurchaseResDto> purchaseResDtoList, HSSFWorkbook workbook) {
        //创建sheet
        HSSFSheet sheet = workbook.createSheet(PURCHASE_TAB);
        //设置表格列宽度
        sheet.setDefaultColumnWidth(SortConstant.TWENTY);
        //创建第一行表头
        HSSFRow headrow = sheet.createRow(SortConstant.ZERO);
        //高度
        headrow.setHeight(SortConstant.HEAD_HEIGHT);
        //添加标题
        for(int i = 0; i < PURCHASE_TITLE.length; i++){
            //标题的显示样式
            HSSFCellStyle headerStyle = workbook.createCellStyle();
            //设置样式
            HSSFFont font = this.getHssfFont(workbook, headerStyle);
            //字体大小
            font.setFontHeightInPoints(SortConstant.HEAD_FONT);
            //创建单元格
            HSSFCell cell = headrow.createCell(i);
            //标题写入单元格
            cell.setCellValue(PURCHASE_TITLE[i]);
            headerStyle.setFont(font);
            cell.setCellStyle(headerStyle);
        }
        //添加数据
        for(int i = 0; i< purchaseResDtoList.size(); i++){
            int j = i + SortConstant.ONE;
            //创建行
            HSSFRow row = sheet.createRow(j);
            //高度
            row.setHeight(SortConstant.ROW_HEIGHT);
            //内容样式
            HSSFCellStyle headerStyle = workbook.createCellStyle();
            //设置样式
            HSSFFont font = this.getHssfFont(workbook, headerStyle);
            //字体大小
            font.setFontHeightInPoints(SortConstant.ROW_FONT);
            headerStyle.setFont(font);
            purchaseResDtoList.forEach(resDto -> {
                Cell cell0 = row.createCell(0);
                cell0.setCellValue(resDto.getPurchaseNo());
                cell0.setCellStyle(headerStyle);
                Cell cell1 = row.createCell(1);
                cell1.setCellValue(resDto.getOrderNo());
                cell1.setCellStyle(headerStyle);
                Cell cell2 = row.createCell(2);
                cell2.setCellValue(resDto.getPurchaseStatusText());
                cell2.setCellStyle(headerStyle);
                Cell cell3 = row.createCell(3);
                cell3.setCellValue(resDto.getPurchasePersonnelName());
                cell3.setCellStyle(headerStyle);
                Cell cell4 = row.createCell(4);
                if(null != resDto.getTotalAmount()){
                    cell4.setCellValue(resDto.getTotalAmount().toString());
                    cell4.setCellStyle(headerStyle);
                }else{
                    cell4.setCellValue("");
                    cell4.setCellStyle(headerStyle);
                }
                Cell cell5 = row.createCell(5);
                cell5.setCellValue(resDto.getStatusText());
                cell5.setCellStyle(headerStyle);
                Cell cell6 = row.createCell(6);
                cell6.setCellValue(resDto.getRemarks());
                cell6.setCellStyle(headerStyle);
            });

        }
    }

    /**
     * 设置样式
     * @param workbook book
     * @param headerStyle  style
     * @return return
     */
    private HSSFFont getHssfFont(HSSFWorkbook workbook, HSSFCellStyle headerStyle) {
        headerStyle.setAlignment(HorizontalAlignment.CENTER);//水平居中
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setWrapText(true);
        headerStyle.setShrinkToFit(true);
        HSSFFont font = workbook.createFont();
        font.setFontName("宋体");
        font.setBold(true);
        return font;
    }

    @Override
    public int completeByPrimaryKeyApp(String pkId, String userId) {
        Assert.hasText(pkId, "采购单ID不能为空!");
        Assert.hasText(userId, "用户ID不能为空!");
        TmPurchaseEntity checkEntity = this.tmPurchaseMapper.selectByPrimaryKey(pkId);
        if (checkEntity == null) {
            log.info("根据主键 pkId【{}】查询信息不存在",pkId);
            throw new BusiException("数据不存在");
        }
        //采购单完成
        checkEntity.setPurchaseStatus(PurchaseStatusEnum.PURCHASE_COMPLETED.getCode());
        checkEntity.setPurchasePersonnel(userId);
        checkEntity.setLastUpdateTime(new Date());
        checkEntity.setLastUpdateUser(userId);
        int i = this.tmPurchaseMapper.updateByPrimaryKeySelective(checkEntity);
        Assert.isTrue(i==SortConstant.ONE, "完成采购单,修改状态失败!");
        //修改订单状态
        TmOrderEntity tmOrderEntity = this.tmOrderMapper.queryByOrderNo(checkEntity.getOrderNo());
        Assert.notNull(tmOrderEntity, "采购单对应的订单不存在!");
        this.iOrderService.updateOrderStatus(tmOrderEntity.getPkId(), OrderStatusEnum.CUTTING.getCode());
        return i;
    }

    @Override
    public int completeByPrimaryKey(String pkId) {
        Assert.hasText(pkId, "采购单ID不能为空!");
        TmPurchaseEntity checkEntity = this.tmPurchaseMapper.selectByPrimaryKey(pkId);
        Assert.notNull(checkEntity, "采购单信息不存在!");
        //采购单完成
        checkEntity.setPurchaseStatus(PurchaseStatusEnum.PURCHASE_COMPLETED.getCode());
        checkEntity.setPurchasePersonnel(LoginUtil.getUserId());
        checkEntity.setLastUpdateTime(new Date());
        checkEntity.setLastUpdateUser(LoginUtil.getUserId());
        int i = this.tmPurchaseMapper.updateByPrimaryKeySelective(checkEntity);
        Assert.isTrue(i==SortConstant.ONE, "完成采购单,修改状态失败!");
        //修改订单状态
        TmOrderEntity tmOrderEntity = this.tmOrderMapper.queryByOrderNo(checkEntity.getOrderNo());
        Assert.notNull(tmOrderEntity, "采购单对应的订单不存在!");
        this.iOrderService.updateOrderStatus(tmOrderEntity.getPkId(), OrderStatusEnum.CUTTING.getCode());
        return i;
    }



}
