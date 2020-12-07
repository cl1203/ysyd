package com.cl.ysyd.common.exception;

/**
 * Description: 业务异常.
 */
public class BusiException extends RuntimeException {

    /**
     * <p>
     * Field serialVersionUID: 序列号.
     * </p>
     */
    private static final long serialVersionUID = -8942916856187828201L;

    /**
     * <p>
     * Field exceptionType: 业务异常.
     * </p>
     */
    private static final String EXCEPTION_TYPE = "业务异常";

    /**
     * <p>
     * Field errorCode: 错误代码6位长度,若为9999，表示未定义错误信息.
     * </p>
     */
    private String errorCode = "9999";

    /**
     * <p>
     * Description: 异常.
     * </p>
     *
     * @param msg 信息
     */
    public BusiException(String msg) {
        super(msg);
    }

    /**
     * <p>
     * Description: 异常.
     * </p>
     *
     * @param code 编码
     * @param msg 信息
     */
    public BusiException(String code, String msg) {
        super(msg);
        this.errorCode = code;
    }

    /**
     * <p>
     * Description: 异常.
     * </p>
     *
     * @param code 编码
     * @param msg 信息
     * @param cause 原因
     */
    public BusiException(String code, String msg, Throwable cause) {
        super(msg, cause);
        this.errorCode = code;
    }

    /**
     * <p>
     * Description: 异常.
     * </p>
     *
     * @param message 信息
     * @param cause 原因
     */
    public BusiException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * <p>
     * Description: errorCode.
     * </p>
     *
     * @return String
     */
    public String getErrorCode() {
        return this.errorCode;
    }

    /**
     * <p>
     * Description: EXCEPTION_TYPE.
     * </p>
     *
     * @return 结果
     */
    public String getType() {
        return EXCEPTION_TYPE;
    }

    /**
     * <p>
     * Description: toString.
     * </p>
     *
     * @return String
     */
    @Override
    public String toString() {
        String message = this.getLocalizedMessage();
        return "[" + this.getType() + "]-[" + this.getErrorCode() + "]-[" + message + "]";
    }
}
