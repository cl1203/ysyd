/**
 * FtpUtils.java
 * Created at 2019-04-12
 * Created by ZJB
 * Copyright (C) 2019 SAIC VOLKSWAGEN, All rights reserved.
 */
package com.cl.ysyd.common.utils;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.TimeZone;

/**
 * Description: ftp工具类
 */
public class FtpUtil {

    /**
     * <p>
     * Field ftpClient: ftp连接
     * </p>
     */
    private FTPClient ftpClient;

    /**
     * <p>
     * Field strIp: ip地址
     * </p>
     */
    private String strIp;

    /**
     * <p>
     * Field intPort: 端口号
     * </p>
     */
    private int intPort;

    /**
     * <p>
     * Field user: 用户名
     * </p>
     */
    private String user;

    /**
     * <p>
     * Field password: 密码
     * </p>
     */
    private String password;

    /**
     * <p>
     * Field LOGGER: 日志对象
     * </p>
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(FtpUtil.class);

    /**
     * <p>
     * Description: Ftp构造函数
     * </p>
     *
     * @param strIp ip
     * @param intPort 端口
     * @param user 用户名
     * @param password 密码
     */
    public FtpUtil(String strIp, int intPort, String user, String password) {
        this.strIp = strIp;
        this.intPort = intPort;
        this.user = user;
        this.password = password;
        this.ftpClient = new FTPClient();
    }

    /**
     * <p>
     * Description: 判断是否登入成功
     * </p>
     * 
     * @return true/false
     */
    public boolean ftpLogin() {
        boolean isLogin = false;
        FTPClientConfig ftpClientConfig = new FTPClientConfig();
        ftpClientConfig.setServerTimeZoneId(TimeZone.getDefault().getID());
        this.ftpClient.setControlEncoding("GBK");
        this.ftpClient.configure(ftpClientConfig);
        try {
            if (this.intPort > 0) {
                this.ftpClient.connect(this.strIp, this.intPort);
            } else {
                this.ftpClient.connect(this.strIp);
            }
            // FTP服务器连接回答  
            int reply = this.ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                this.ftpClient.disconnect();
                LOGGER.error("登录FTP服务失败");
                return isLogin;
            }
            this.ftpClient.login(this.user, this.password);
            // 设置传输协议  
            this.ftpClient.enterLocalPassiveMode();
            this.ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            LOGGER.info(this.user + "成功登陆");
            isLogin = true;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(this.user + "登录失败" + e.getMessage());
        }
        this.ftpClient.setBufferSize(1024 * 2);
        this.ftpClient.setDataTimeout(30 * 1000);
        return isLogin;
    }

    /**
     * <p>
     * Description: 退出关闭服务器链接
     * </p>
     */
    public void ftpLogOut() {
        if (null != this.ftpClient && this.ftpClient.isConnected()) {
            try {
                // 退出FTP服务器  
                boolean reuslt = this.ftpClient.logout();
                if (reuslt) {
                    LOGGER.info("成功退出服务器");
                }
            } catch (IOException e) {
                e.printStackTrace();
                LOGGER.warn("退出FTP服务器异常" + e.getMessage());
            } finally {
                try {
                    // 关闭FTP服务器的连接  
                    this.ftpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                    LOGGER.warn("关闭FTP服务器的连接异常");
                }
            }
        }
    }

    /**
     * <p>
     * Description: 上传Ftp文件
     * </p>
     * 
     * @param localFile 本地文件
     * @param romotUpLoadePath 上传服务器路径(注意应该以/结束)
     * @return true/false
     */
    public boolean uploadFile(File localFile, String romotUpLoadePath) {
        BufferedInputStream inStream = null;
        boolean success = false;
        try {
            // 改变工作路径  
            this.ftpClient.changeWorkingDirectory(romotUpLoadePath);
            inStream = new BufferedInputStream(new FileInputStream(localFile));
            LOGGER.info(localFile.getName() + "开始上传");
            success = this.ftpClient.storeFile(localFile.getName(), inStream);
            if (success) {
                LOGGER.info(localFile.getName() + "上传成功");
                return success;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            LOGGER.error(localFile + "未找到文件");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return success;
    }

    /**
     * <p>
     * Description: 下载文件
     * </p>
     * 
     * @param remoteFileName 待下载文件名称
     * @param localDires 下载到当地那个路径下
     * @param remoteDownLoadPath remoteFileName所在的路径
     * @return true/false
     */
    public boolean downloadFile(String remoteFileName, String localDires, String remoteDownLoadPath) {
        String strFilePath = localDires + remoteFileName;
        File file = new File(strFilePath);
        if (!file.getParentFile().exists()) {
            boolean result = file.getParentFile().mkdirs();
            if (!result) {
                LOGGER.info("创建失败");
            }
        }
        BufferedOutputStream outStream = null;
        boolean success = false;
        try {
            this.ftpClient.changeWorkingDirectory(remoteDownLoadPath);
            outStream = new BufferedOutputStream(new FileOutputStream(strFilePath));
            LOGGER.info(remoteFileName + "开始下载");
            success = this.ftpClient.retrieveFile(remoteFileName, outStream);
            if (success) {
                LOGGER.info("下载成功");
                return success;
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(remoteFileName + "下载失败");
        } finally {
            if (null != outStream) {
                try {
                    outStream.flush();
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return success;
    }

    /**
     * <p>
     * Description: 上传文件夹(递归包含文件夹下的文件)
     * </p>
     * 
     * @param localDirectory 本地文件夹
     * @param remoteDirectoryPath ftp服务器路径以目录"/"结束(不以/结束文件名及保存路径会有问题)
     * @return true/false
     */
    public boolean uploadDirectory(String localDirectory, String remoteDirectoryPath) {
        File src = new File(localDirectory);
        try {
            remoteDirectoryPath = remoteDirectoryPath + src.getName() + "/";
            this.ftpClient.makeDirectory(remoteDirectoryPath);
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.info(remoteDirectoryPath + "目录创建失败");
        }
        File[] allFile = src.listFiles();
        for (int currentFile = 0; currentFile < allFile.length; currentFile++) {
            if (!allFile[currentFile].isDirectory()) {
                String srcName = allFile[currentFile].getPath().toString();
                uploadFile(new File(srcName), remoteDirectoryPath);
            }
        }
        for (int currentFile = 0; currentFile < allFile.length; currentFile++) {
            if (allFile[currentFile].isDirectory()) {
                // 递归  
                uploadDirectory(allFile[currentFile].getPath().toString(), remoteDirectoryPath);
            }
        }
        return true;
    }

    /**
     * <p>
     * Description: 下载文件夹
     * </p>
     * 
     * @param localDirectoryPath 本地地址
     * @param remoteDirectory 远程文件夹
     * @return true/false
     */
    public boolean downLoadDirectory(String localDirectoryPath, String remoteDirectory) {
        try {
            String fileName = new File(remoteDirectory).getName();
            localDirectoryPath = localDirectoryPath + fileName + "//";
            new File(localDirectoryPath).mkdirs();
            FTPFile[] allFile = this.ftpClient.listFiles(remoteDirectory);
            for (int currentFile = 0; currentFile < allFile.length; currentFile++) {
                if (!allFile[currentFile].isDirectory()) {
                    downloadFile(allFile[currentFile].getName(), localDirectoryPath, remoteDirectory);
                }
            }
            for (int currentFile = 0; currentFile < allFile.length; currentFile++) {
                if (allFile[currentFile].isDirectory()) {
                    String strremoteDirectoryPath = remoteDirectory + "/" + allFile[currentFile].getName();
                    downLoadDirectory(localDirectoryPath, strremoteDirectoryPath);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.info("下载文件夹失败");
            return false;
        }
        return true;
    }

    public FTPClient getFtpClient() {
        return this.ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }

}
