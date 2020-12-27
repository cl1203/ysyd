package com.cl.ysyd.controller.order;


import com.cl.ysyd.common.constants.ResponseData;
import com.cl.ysyd.common.constants.SortConstant;
import com.cl.ysyd.common.exception.BusiException;
import com.cl.ysyd.common.utils.DateUtil;
import com.cl.ysyd.common.utils.FtpFileUtil;
import com.cl.ysyd.dto.order.res.FileResDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;


@RestController
@Slf4j
@RequestMapping("/ftpFile")
@CrossOrigin
@Api(tags = "ftp上传")
public class FtpFileUploadController {

    private static final String SUFFIXLIST = "jpg,png,pdf,jpeg,zip,7z,rar";

    @PostMapping("/uploadImg")
    @ApiOperation(value = "上传图片")
    public ResponseData<FileResDto> uploadImgs(@RequestParam("file")MultipartFile file) {
        String filePath = "http://47.106.34.32/img/";
        FileResDto fileResDto = new FileResDto();
        //判断导入文件是否为空
        if(file.isEmpty()) {
            throw new BusiException("文件不能为空!");
        }
        //判断导入文件大小
        if (file.getSize() > Long.parseLong(SortConstant.MAX_IMG_SIZE)) {
            throw new BusiException("文件大小不能超过50M！");
        }
        String fileName = file.getOriginalFilename();
        //获取文件后缀
        Assert.hasText(fileName, "文件名不能为空!");
        String suffix=fileName.substring(fileName.lastIndexOf(".")+1);
        if(!SUFFIXLIST.contains(suffix.trim().toLowerCase())){
            throw new BusiException("只能上传jpg、png、pdf、jpeg、zip、7z、rar格式的文件！");
        }
        //重命名图片
        fileName = fileName.substring(SortConstant.ZERO, fileName.lastIndexOf(".")) + DateUtil.getDateString(new Date(), DateUtil.DATETIMESHOWFORMAT5) + "." + suffix;
        if (fileName.indexOf(" ") > 0) {
            throw new BusiException("文件名不能包含空格！请修改图片名称后重新上传！");
        }
        boolean flag= FtpFileUtil.uploadFile(file , fileName);
        if(flag){
            filePath = filePath + fileName;
            fileResDto.setFileName(fileName);
            fileResDto.setFileUrl(filePath);
            return new ResponseData<>(fileResDto);//该路径图片名称，前端框架可用ngnix指定的路径+filePath,即可访问到ngnix图片服务器中的图片
        }else{
            throw new BusiException("上传失败!");
        }
    }

}
