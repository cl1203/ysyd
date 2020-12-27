package com.cl.ysyd.common.utils;

import com.cl.ysyd.common.exception.BusiException;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class FtpFileUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(FtpFileUtil.class);

    //ftp服务器ip地址
    private static final String FTP_ADDRESS = "47.106.34.32";
    //端口号
    private static final int FTP_PORT = 21;
    //用户名
    private static final String FTP_USERNAME = "ftpysyd";
    //密码
    private static final String FTP_PASSWORD = "ftp999!@#";
    //图片路径
    private static String FTP_BASEPATH = "/img";

    private static FTPClient ftp;

    public  static boolean uploadFile(MultipartFile file , String fileName ){
        boolean success = false;
        ftp = new FTPClient();
        //ftp.enterLocalActiveMode();
        ftp.enterLocalPassiveMode();
        ftp.setControlEncoding("UTF-8");
        try {
            InputStream input = file.getInputStream();
            int reply;
            ftp.connect(FTP_ADDRESS, FTP_PORT);// 连接FTP服务器
            ftp.login(FTP_USERNAME, FTP_PASSWORD);// 登录
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            reply = ftp.getReplyCode();
            LOGGER.info("111111111111111111111111111111" + "reply = " + reply);
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                throw new BusiException("连接失败!");
            }
            FTP_BASEPATH = new String(FTP_BASEPATH.getBytes(StandardCharsets.UTF_8) , StandardCharsets.UTF_8);
            //ftp.makeDirectory(FTP_BASEPATH );// 不存在才会执行这行代码 创建
            success = ftp.changeWorkingDirectory(FTP_BASEPATH );//切换到path下的文件夹下
            LOGGER.info("222222222222222222222222222222" + "success= " +success);
            if(!success){
                createDir(FTP_BASEPATH);
                success = ftp.changeWorkingDirectory(FTP_BASEPATH);
                LOGGER.info("33333333333333333333333333333333" + "success= " +success);
                Assert.isTrue(success , "切换目录失败!");
            }

            fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
            ftp.enterLocalPassiveMode();
            boolean flag  = ftp.storeFile(fileName,input);
            LOGGER.info("444444444444444444444444444444444444" + "flag= " +flag);
            LOGGER.info("upload file: " + flag);
            input.close();
            ftp.logout();
            LOGGER.info("5555555555555555555555555555555555555555555555" + "success= " +success);
            success = flag;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return success;
    }


    /**
     * 目标服务器创建目录
     */
    private static void createDir(String dirname) throws Exception{
        boolean flag = ftp.changeWorkingDirectory(dirname);
        if(flag){
            throw new BusiException("目录已存在！");
        }
        boolean createDir = ftp.makeDirectory(dirname);
        LOGGER.info("在目标服务器上创建文件夹: " + dirname + "," + createDir);
    }
}
