/**
 * OrderServiceImpl.java
 * Created at 2020-11-24
 * Created by chenlong
 * Copyright (C) 2020 ANJI LOGISTICS, All rights reserved.
 **/
package com.cl.ysyd.service.order.impl;

import com.cl.ysyd.common.constants.SortConstant;
import com.cl.ysyd.common.enums.AuditStatusEnum;
import com.cl.ysyd.common.enums.DictType;
import com.cl.ysyd.common.enums.OrderStatusEnum;
import com.cl.ysyd.common.exception.BusiException;
import com.cl.ysyd.common.utils.DateUtil;
import com.cl.ysyd.common.utils.ExcelUtils;
import com.cl.ysyd.common.utils.LoginUtil;
import com.cl.ysyd.common.utils.UuidUtil;
import com.cl.ysyd.dto.order.req.TmOrderReqDto;
import com.cl.ysyd.dto.order.res.TmOrderResDto;
import com.cl.ysyd.entity.order.TmOrderEntity;
import com.cl.ysyd.entity.sys.TsRoleEntity;
import com.cl.ysyd.entity.sys.TsUserEntity;
import com.cl.ysyd.mapper.order.TmOrderMapper;
import com.cl.ysyd.mapper.sys.TcBizDictionaryMapper;
import com.cl.ysyd.mapper.sys.TsRoleMapper;
import com.cl.ysyd.mapper.sys.TsUserMapper;
import com.cl.ysyd.service.order.IOrderService;
import com.cl.ysyd.service.order.helper.OrderHelper;
import com.cl.ysyd.service.sys.IBizDictionaryService;
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
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单service实现类
 * @author chenlong  2020-11-24
 */
@Service
@Slf4j
@Transactional
public class OrderServiceImpl implements IOrderService {
    /**
     * 订单Mapper
     */
    @Autowired
    private TmOrderMapper tmOrderMapper;

    @Autowired
    private TsUserMapper userMapper;

    @Autowired
    private IBizDictionaryService iTcDictService;

    @Autowired
    private TcBizDictionaryMapper bizDictionaryMapper;

    @Autowired
    private TsRoleMapper roleMapper;

    /**
     * 订单Helper
     */
    @Autowired
    private OrderHelper orderHelper;

    private final static String SERIAL = "00001";
    private final static int LENGTH = 5;
    private final static int MAXNUMBER = 5;
    private static final String CONTENT_TYPE = "application/octet-stream";
    private static final String ENCODING = "utf-8";


    //订单导出表头 fileName
    private static final String[] HEADERS = {"订单号(orderNo)" , "交货日期(deliveryDate)" , "订单单价(unitPrice)" , "所属用户(orderUserName)" ,"用户类型(userTypeText)",
            "创建日期(establishDate)" , "完成日期(completeDate)", "订单状态(orderStatusText)" , "SKU(sku)",
            "下单人(orderPeople)" , "尺码(orderSize)", "类型(orderTypeText)" , "状态(statusText)",};
    private static final String FILE_NAME = "订单列表信息";

    //对账单导出表头 fileName sheetName
    private static final String[] HEADERS_BILL = {"订单号" , "交货日期" , "订单状态" , "订单单价" ,"接单人", "创建日期" , "完成日期"};
    private static final String FILE_NAME_BILL = "订单对账单信息";
    private static final String SHEET_NAME = "订单对账单";

    @Override
    public void cancelByPrimaryKey(String pkId) {
        log.info("Service deleteByPrimaryKey start. primaryKey=【{}】",pkId);
        Assert.hasText(pkId, "主键ID不能为空!");
        //根据主键查询  校验数据是否存在
        TmOrderEntity checkEntity = this.getTmOrderEntity(pkId);
        Assert.notNull(checkEntity, "订单不存在!");
        checkEntity.setStatus(SortConstant.ZERO.byteValue());
        checkEntity.setLastUpdateTime(new Date());
        checkEntity.setLastUpdateUser(LoginUtil.getUserId());
        int ret = this.tmOrderMapper.updateByPrimaryKeySelective(checkEntity);
        Assert.isTrue(ret==SortConstant.ONE, "作废失败!");
        log.info("Service deleteByPrimaryKey end. ret=【{}】",ret);
    }

    /**
     * 根据主键查询  校验数据是否存在
     * @param pkId 订单ID
     * @return 订单对象
     */
    private TmOrderEntity getTmOrderEntity(String pkId) {
        Assert.hasText(pkId, "主键ID不能为空!");
        TmOrderEntity checkEntity = this.tmOrderMapper.selectByPrimaryKey(pkId);
        if (checkEntity == null) {
            log.info("根据主键 pkId【{}】查询信息不存在",pkId);
            throw new BusiException("根据ID查询订单数据不存在!");
        }
        return checkEntity;
    }

    @Override
    public TmOrderResDto queryByPrimaryKey(String pkId) {
        log.info("Service selectByPrimaryKey start. primaryKey=【{}】",pkId);
        Assert.hasText(pkId, "主键ID不能为空!");
        TmOrderEntity entity = this.tmOrderMapper.selectByPrimaryKey(pkId);
        if(null == entity){
            return null;
        }
        TmOrderResDto resDto = this.orderHelper.editResDto(entity);
        log.info("Service selectByPrimaryKey end. res=【{}】",resDto);
        return resDto;
    }

    @Override
    public void createOrder(TmOrderReqDto reqDto) {
        log.info("Service createOrder start. reqDto=【{}】",reqDto);
        TmOrderEntity entity = this.orderHelper.editEntity(reqDto);
        //当前年月日
        String dateString = DateUtil.getDateString(new Date(), DateUtil.DATETIMESHOWFORMAT4);
        //获取当前流水号
        String date = DateUtil.getDateString(new Date(), DateUtil.DATESHOWFORMAT);
        String orderNoMax = this.tmOrderMapper.querySerialNumber(date);
        //订单号
        String orderNo;
        if(StringUtils.isNotBlank(orderNoMax)){
            int length = orderNoMax.length();
            String serialNumber = orderNoMax.substring(length - LENGTH, length);
            orderNo = String.valueOf(Long.parseLong(dateString + serialNumber) + SortConstant.ONE);
        }else{
            orderNo = dateString + SERIAL;
        }
        entity.setOrderNo(orderNo);
        entity.setCreateTime(new Date());
        entity.setPkId(UuidUtil.getUuid());
        entity.setEstablishDate(DateUtil.dateToDate(new Date(), DateUtil.DATESHOWFORMAT));
        int ret = this.tmOrderMapper.insertSelective(entity);
        Assert.isTrue(ret==SortConstant.ONE, "新增失败!");
        log.info("Service createOrder end. ret=【{}】",ret);
    }

    @Override
    public void updateByPrimaryKey(String pkId, TmOrderReqDto reqDto) {
        log.info("Service updateByPrimaryKey start. pkId=【{}】, reqDto =【{}】",pkId,reqDto);
        //校验主键ID是否存在数据
        this.getTmOrderEntity(pkId);
        TmOrderEntity entity = this.orderHelper.editEntity(reqDto);
        entity.setPkId(pkId);
        entity.setLastUpdateTime(new Date());
        entity.setLastUpdateUser(LoginUtil.getUserId());
        int ret = this.tmOrderMapper.updateByPrimaryKeySelective(entity);
        Assert.isTrue(ret==SortConstant.ONE, "修改失败!");
        log.info("Service updateByPrimaryKey end. ret=【{}】",ret);
    }

    @Override
    public PageInfo<TmOrderResDto> queryOrderByPage(Integer pageNum, Integer pageSize, String orderUser, String orderStatus, String deliveryDate,
                                                    String establishDate, String completeDate, String examineStatus,String status) {
        //查询当前用户是否拥有全部权限
        String isAll = this.getIsAll();
        PageHelper.orderBy("STATUS DESC, CREATE_TIME DESC");
        Page<TmOrderResDto> startPage = PageHelper.startPage(pageNum, pageSize);
        List<TmOrderEntity> orderEntityList = this.tmOrderMapper.queryOrderList(orderUser, orderStatus, deliveryDate, establishDate, completeDate, examineStatus, isAll ,status);
        List<TmOrderResDto> orderResDtoList = this.orderHelper.editResDtoList(orderEntityList);
        PageInfo<TmOrderResDto> pageInfo = new PageInfo<>(startPage);
        pageInfo.setList(orderResDtoList);
        return pageInfo;
    }

    /**
     * 查询当前用户是否拥有全部权限
     * @return 是否
     */
    private String getIsAll() {
        String userId = LoginUtil.getUserId();
        Assert.hasText(userId, "用户ID为空!");
        TsUserEntity userEntity = this.userMapper.selectByPrimaryKey(userId);
        Assert.notNull(userEntity, "userId对应的用户不存在!");

        //根据用户ID查询对应角色是否拥有所有数据权限
        TsRoleEntity tsRoleEntity = this.roleMapper.queryByUserId(userId);
        Assert.notNull(tsRoleEntity, "用户对应的角色为空!");
        return tsRoleEntity.getIsAll();
    }

    @Override
    public PageInfo<TmOrderResDto> queryOrderBillByPage(Integer pageNum, Integer pageSize, String orderUser, String deliveryDate, String establishDate, String completeDate) {
        String isAll = this.getIsAll();
        PageHelper.orderBy("STATUS DESC, CREATE_TIME DESC");
        Page<TmOrderResDto> startPage = PageHelper.startPage(pageNum, pageSize);
        List<TmOrderEntity> orderEntityList = this.tmOrderMapper.queryOrderList(orderUser,  deliveryDate, establishDate, completeDate, isAll);
        PageInfo<TmOrderResDto> pageInfo = new PageInfo<>(startPage);
        BigDecimal totalMoney = new BigDecimal(SortConstant.ZERO);
        if(CollectionUtils.isNotEmpty(orderEntityList)){
            for(TmOrderEntity orderEntity : orderEntityList){
                BigDecimal unitPrice = orderEntity.getUnitPrice();
                totalMoney = totalMoney.add(unitPrice).setScale(SortConstant.TWO, BigDecimal.ROUND_HALF_UP);
            }
            List<TmOrderResDto> orderResDtoList = this.orderHelper.editResDtoList(orderEntityList);
            orderResDtoList.get(SortConstant.ZERO).setTotalMoney(totalMoney.toString());
            pageInfo.setList(orderResDtoList);
            return pageInfo;
        }
        return pageInfo;
    }

    @Override
    public void distributionUser(String orderId, String orderUserId) {
        //校验订单ID对应的订单是否存在
        log.info("Service updateByPrimaryKey start. orderId=【{}】, orderUserId =【{}】",orderId,orderUserId);
        //根据主键ID检验订单数据
        TmOrderEntity checkEntity = this.getTmOrderEntity(orderId);
        String orderUser = checkEntity.getOrderUser();
        if(StringUtils.isNotBlank(orderUser)){
            throw new BusiException("订单已经分配用户!");
        }
        Assert.isTrue(checkEntity.getOrderStatus().equals(OrderStatusEnum.WAITING.getCode()), "订单不属于待接单状态,不能分配!");
        TsUserEntity userEntity = this.userMapper.selectByPrimaryKey(orderUserId);
        if(null == userEntity){
            throw new BusiException("选择的用户不存在!");
        }
        Byte status = userEntity.getStatus();
        if(status.equals(SortConstant.ZERO.byteValue())){
            throw new BusiException("选择的用户已经被禁用!");
        }
        String auditStatus = userEntity.getAuditStatus();
        if(auditStatus.equals(AuditStatusEnum.NOT_REVIEWED)){
            throw new BusiException("选择的用户还未审核!");
        }
        checkEntity.setOrderUser(orderUserId);
        checkEntity.setOrderStatus(OrderStatusEnum.ORDERING.getCode());
        checkEntity.setLastUpdateUser(LoginUtil.getUserId());
        checkEntity.setLastUpdateTime(new Date());
        int ret = this.tmOrderMapper.updateByPrimaryKeySelective(checkEntity);
        Assert.isTrue(ret==SortConstant.ONE, "分配失败!");
    }

    @Override
    public void updateOrderStatus(String pkId) {
        //根据主键ID检验订单数据
        TmOrderEntity checkEntity = this.getTmOrderEntity(pkId);
        /*String orderStatusText = this.iTcDictService.getTextByBizCode(DictType.ORDER_STATUS.getCode(), orderStatus);
        Assert.hasText(orderStatusText, "选择的订单状态不存在!");*/
        //根据当前订单状态获取下一个订单状态
        String orderStatus = this.bizDictionaryMapper.getOrderStatusNext(checkEntity.getOrderStatus());
        checkEntity.setOrderStatus(orderStatus);
        checkEntity.setLastUpdateTime(new Date());
        checkEntity.setLastUpdateUser(LoginUtil.getUserId());
        int ret = this.tmOrderMapper.updateByPrimaryKeySelective(checkEntity);
        Assert.isTrue(ret==SortConstant.ONE, "修改订单状态失败!");
    }

    @Override
    public void export(HttpServletResponse response, String orderUser, String orderStatus, String deliveryDate, String establishDate, String completeDate, String status) {
        //查询结果
        List<TmOrderResDto> list = this.queryOrderByPage(SortConstant.ONE, SortConstant.PAGE_SIZE, orderUser, orderStatus, deliveryDate, establishDate, completeDate, null, status).getList();
        //导出
        ExcelUtils.exportExcel( FILE_NAME, HEADERS , list , response , DateUtil.DATESHOWFORMAT);
    }

    @Override
    public void exportBill(HttpServletResponse response, String orderUser, String deliveryDate, String establishDate, String completeDate)throws IOException {
        //查询结果
        List<TmOrderResDto> list = this.queryOrderBillByPage(SortConstant.ONE, SortConstant.PAGE_SIZE, orderUser, deliveryDate, establishDate, completeDate).getList();
        //实例化HSSFWorkbook
        HSSFWorkbook workbook = new HSSFWorkbook();
        //sheet1
        this.createSheet(list, workbook);
        //准备将Excel的输出流通过response输出到页面下载
        //八进制输出流
        response.setContentType(CONTENT_TYPE);
        response.setCharacterEncoding(ENCODING);
        //这后面可以设置导出Excel的名称，此例中名为student.xls（解决文件名称乱码问题）
        response.setHeader("content-disposition", "attachment;filename=" + new String(FILE_NAME_BILL.getBytes(), "ISO8859-1") + ".xls" );
        //刷新缓冲
        response.flushBuffer();
        //workbook将Excel写入到response的输出流中，供页面下载
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    private void createSheet(List<TmOrderResDto> list, HSSFWorkbook workbook){
        //创建sheet
        HSSFSheet sheet = workbook.createSheet(SHEET_NAME);
        //设置表格列宽度
        sheet.setDefaultColumnWidth(SortConstant.TWENTY);
        //创建第一行表头
        HSSFRow headrow = sheet.createRow(SortConstant.ZERO);
        //高度
        headrow.setHeight(SortConstant.HEAD_HEIGHT);
        //添加标题
        for(int i = 0; i < HEADERS_BILL.length; i++){
            //标题的显示样式
            HSSFCellStyle headerStyle = workbook.createCellStyle();
            //设置样式
            HSSFFont font = this.getHssfFont(workbook, headerStyle);
            //字体大小
            font.setFontHeightInPoints(SortConstant.HEAD_FONT);
            //创建单元格
            HSSFCell cell = headrow.createCell(i);
            //标题写入单元格
            cell.setCellValue(HEADERS_BILL[i]);
            headerStyle.setFont(font);
            cell.setCellStyle(headerStyle);
        }
        //添加数据
        for(int i = 0; i< list.size(); i++){
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
            TmOrderResDto resDto = list.get(i);
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(resDto.getOrderNo());
            cell0.setCellStyle(headerStyle);
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(resDto.getDeliveryDate());
            cell1.setCellStyle(headerStyle);
            Cell cell2 = row.createCell(2);
            cell2.setCellValue(resDto.getOrderStatusText());
            cell2.setCellStyle(headerStyle);
            Cell cell3 = row.createCell(3);
            cell3.setCellValue(resDto.getUnitPrice());
            cell3.setCellStyle(headerStyle);
            Cell cell4 = row.createCell(4);
            cell4.setCellValue(resDto.getOrderUserName());
            cell4.setCellStyle(headerStyle);
            Cell cell5 = row.createCell(5);
            cell5.setCellValue(resDto.getEstablishDate());
            cell5.setCellStyle(headerStyle);
            Cell cell6 = row.createCell(6);
            cell6.setCellValue(resDto.getCompleteDate());
            cell6.setCellStyle(headerStyle);
        }
        String totalMoney = "";
        if(CollectionUtils.isNotEmpty(list)){
            totalMoney = list.get(SortConstant.ZERO).getTotalMoney();
        }
        int length = HEADERS_BILL.length;
        int size = list.size();
        //创建行
        HSSFRow row = sheet.createRow(size + SortConstant.ONE);
        //高度
        HSSFCell cellText = row.createCell(length - SortConstant.TWO);
        HSSFCell cellMoney = row.createCell(length - SortConstant.ONE);

        //标题的显示样式
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(HorizontalAlignment.CENTER);//水平居中
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        HSSFFont font = workbook.createFont();
        font.setFontName("宋体");
        font.setBold(true);
        font.setFontHeightInPoints(SortConstant.HEAD_FONT);
        headerStyle.setFont(font);
        sheet.addMergedRegion(new CellRangeAddress(size + SortConstant.ONE, size + SortConstant.THREE, length - SortConstant.TWO, length - SortConstant.TWO));
        sheet.addMergedRegion(new CellRangeAddress(size + SortConstant.ONE, size + SortConstant.THREE, length - SortConstant.ONE , length - SortConstant.ONE));
        cellText.setCellStyle(headerStyle);
        cellText.setCellValue("合计: ");

        cellMoney.setCellStyle(headerStyle);
        cellMoney.setCellValue(totalMoney);

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
    public int queryNumByUserId() {
        String userId = LoginUtil.getUserId();
        TsUserEntity userEntity = this.userMapper.selectByPrimaryKey(userId);
        if(null == userEntity){
            throw new BusiException("userId对应的用户数据不存在!");
        }
        return this.tmOrderMapper.queryNumByUserId(userId);
    }

    @Override
    public void connectOrder(String orderType, Integer number) {
        Assert.hasText(orderType, "订单类型不能为空!");
        Assert.notNull(number, "接单数量不能为空!");
        //所有类型的订单 待接单状态的数量是否为0
        List<TmOrderEntity> waitingList = this.tmOrderMapper.queryWaiting(number).getList();
        Assert.isTrue(waitingList.size() > SortConstant.ZERO, "未存在任何待接单状态的订单!");
        //此用户有多少未完成订单
        int i = this.queryNumByUserId();
        Assert.isTrue(i <= MAXNUMBER, "您名下未完成订单大于或等于5单, 不能再接单!");
        Assert.isTrue((i + number) <= MAXNUMBER, "接单数量加您未完成的订单已经大于或等于5单!");
        //查询所选择类型的订单还有多少待接单 按接单的数量分页
        List<TmOrderEntity> list = this.tmOrderMapper.queryWaitingByOrderType(orderType, number).getList();
        //如果 此类型的订单在待接单状态的数量和用户接单数量一样 直接分配
        if(list.size() == number){
            list.forEach(this::updateOrderUser);
        }else{
            //如果此类型待接单的订单数量为0 查询其他类型所有待接单的订单
            int size = list.size();
            if(size == SortConstant.ZERO){
                //直接分配其他类型的订单
                waitingList.forEach(this::updateOrderUser);
            }else{
                //当此类型订单有 但不够的时候 补其他订单
                waitingList = this.tmOrderMapper.queryWaiting(number -size).getList();
                waitingList.forEach(this::updateOrderUser);
            }
        }
    }

    @Override
    public void returnOrder(String pkId) {
        //根据主键ID检验订单数据
        TmOrderEntity checkEntity = this.getTmOrderEntity(pkId);
        boolean equals = checkEntity.getOrderStatus().equals(OrderStatusEnum.ORDERING.getCode());
        Assert.isTrue(equals, "订单不是接单中, 不能进行退单!");
        checkEntity.setOrderUser("");
        checkEntity.setLastUpdateTime(new Date());
        checkEntity.setOrderStatus(OrderStatusEnum.WAITING.getCode());
        checkEntity.setLastUpdateUser(LoginUtil.getUserId());
        int i = this.tmOrderMapper.updateByPrimaryKeySelective(checkEntity);
        Assert.isTrue(i == SortConstant.ONE, "退单失败!");
    }

    @Override
    public void examineOrder(String pkId, String examineStatus) {
        Assert.hasText(pkId, "订单ID不能为空!");
        TmOrderEntity orderEntity = this.tmOrderMapper.selectByPrimaryKey(pkId);
        Assert.notNull(orderEntity, "订单ID对应的订单数据不存在!");
        Assert.hasText(examineStatus, "审核状态不能为空!");
        String examinesStatusText = this.iTcDictService.getTextByBizCode(DictType.EXAMINE_STATUS.getCode(), examineStatus);
        Assert.hasText(examinesStatusText, "审核状态不存在,请重新选择!");
        int i = this.tmOrderMapper.examineOrder(pkId, examineStatus);
        Assert.isTrue(i==SortConstant.ONE, "修改订单审核状态失败!");
    }

    /**
     * 分配订单
     * @param tmOrderEntity 订单对象
     */
    private void updateOrderUser(TmOrderEntity tmOrderEntity) {
        tmOrderEntity.setOrderUser(LoginUtil.getUserId());
        tmOrderEntity.setLastUpdateTime(new Date());
        tmOrderEntity.setLastUpdateUser(LoginUtil.getUserId());
        tmOrderEntity.setOrderStatus(OrderStatusEnum.ORDERING.getCode());
        int j = this.tmOrderMapper.updateByPrimaryKeySelective(tmOrderEntity);
        Assert.isTrue(j == SortConstant.ONE, "分配订单失败!");
    }
}
