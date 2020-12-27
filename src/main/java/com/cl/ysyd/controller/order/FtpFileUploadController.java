package com.cl.ysyd.controller.order;


import com.cl.ysyd.common.constants.ResponseData;
import com.cl.ysyd.common.constants.SortConstant;
import com.cl.ysyd.common.exception.BusiException;
import com.cl.ysyd.common.utils.DateUtil;
import com.cl.ysyd.common.utils.FtpFileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;


@RestController
@Slf4j
@RequestMapping("/ftpFile")
@CrossOrigin
@Api(tags = "ftp上传")
public class FtpFileUploadController {


    @PostMapping("/uploadImg")
    @ApiOperation(value = "上传图片")
    public ResponseData<String> uploadImgs(@RequestParam("file")MultipartFile file) {
        //判断导入文件是否为空
        if(file.isEmpty()) {
            throw new BusiException("文件不能为空!");
        }
        //判断导入文件大小
        if (file.getSize() > Long.parseLong(SortConstant.MAX_IMG_SIZE)) {
            throw new BusiException("文件大小不能超过10M！");
        }
        String fileName = file.getOriginalFilename();
        if(StringUtils.isBlank(fileName)){
            fileName = DateUtil.getDateString(new Date(), DateUtil.DATETIMESHOWFORMAT);
        }
        if (fileName.indexOf(" ") > 0) {
            throw new BusiException("文件名不能包含空格！请修改图片名称后重新上传！");
        }
        String filePath = "http://47.106.34.32/img/";
        boolean flag= FtpFileUtil.uploadFile(file , fileName);
        if(flag){
            filePath = filePath + fileName;
        }else{
            throw new BusiException("上传失败!");
        }
        return new ResponseData<>(filePath);  //该路径图片名称，前端框架可用ngnix指定的路径+filePath,即可访问到ngnix图片服务器中的图片
    }
}
