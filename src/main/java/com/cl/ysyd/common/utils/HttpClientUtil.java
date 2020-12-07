package com.cl.ysyd.common.utils;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.nio.charset.CodingErrorAction;
import java.rmi.UnknownHostException;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: httpClient工具類</p>
 */
@Component
public class HttpClientUtil {

    /**
         * 声明为 static volatile,会迫使线程每次读取时作为一个全局变量读取
     */
    private static volatile CloseableHttpClient httpClient = null;

    /**
     * 连接池最大连接数
     */
    @Value("${httpclient.max_total_connections}")
    private int maxTotalConnections;

    /**
     * 设置每个路由上的默认连接个数
     */
    @Value("${httpclient.default_max_per_route}")
    private int defaultMaxPerRoute;

    /**
     * 请求的请求超时时间 单位：毫秒
     */
    @Value("${httpclient.request_connection_timeout}")
    private int requestConnectionTimeout;

    /**
     * 请求的等待数据超时时间 单位：毫秒
     */
    @Value("${httpclient.request_socket_timeout}")
    private int requestSocketTimeout;

    /**
     * 请求的连接超时时间 单位：毫秒
     */
    @Value("${httpclient.request_connection_request_timeout}")
    private int requestConnectionRequestTimeout;

    /**
     * 重试次数
     */
    @Value("${httpclient.retry_count:5}")
    private int retryCount;

    /**
     * 连接闲置多久后需要重新检测 单位：毫秒
     */
    @Value("${httpclient.validate_after_in_activity}")
    private int validateAfterInActivity;

    /**
     * 关闭Socket时，要么发送完所有数据，要么等待多少秒后，就关闭连接，此时socket.close()是阻塞的 单位秒
     */
    @Value("${httpclient.socket_config_so_linger}")
    private int socketConfigSoLinger;

    /**
     * 接收数据的等待超时时间,即读超时时间，单位毫秒
     */
    @Value("${httpclient.socket_config_so_timeout}")
    private int socketConfigSoTimeout;



    /**
     * @param uri
     * @return String
     * @description get请求方式
     * @author: long.he01
     */
    public String doGet(String uri) {

        String responseBody;
        HttpGet httpGet = new HttpGet(uri);
        try {
            httpGet.setConfig(getRequestConfig());
            responseBody = executeRequest(httpGet);
        } catch (IOException e) {
            throw new RuntimeException("httpclient doGet方法异常 ", e);
        } finally {
            httpGet.releaseConnection();
        }

        return responseBody;
    }

    /**
     * @param uri
     * @param params
     * @return string
     * @description 带map参数get请求, 此方法会将map参数拼接到连接地址上。
     */
    public String doGet(String uri, Map<String, String> params) {

        return doGet(getGetUrlFromParams(uri, params));

    }

    /**
     * @param uri
     * @param params
     * @return String
     * @description 根据map参数拼接完整的url地址
     */
    private static String getGetUrlFromParams(String uri, Map<String, String> params) {


        List<BasicNameValuePair> resultList = FluentIterable.from(params.entrySet()).transform(
                new Function<Map.Entry<String, String>, BasicNameValuePair>() {
                    @Override
                    public BasicNameValuePair apply(Map.Entry<String, String> innerEntry) {

                        return new BasicNameValuePair(innerEntry.getKey(), innerEntry.getValue());
                    }

                }).toList();

        String paramSectionOfUrl = URLEncodedUtils.format(resultList, Consts.UTF_8);
        StringBuffer resultUrl = new StringBuffer(uri);

        if (StringUtils.isEmpty(uri)) {
            return uri;
        } else {
            if (!StringUtils.isEmpty(paramSectionOfUrl)) {
                if (uri.endsWith("?")) {
                    resultUrl.append(paramSectionOfUrl);
                } else {
                    resultUrl.append("?").append(paramSectionOfUrl);
                }
            }
            return resultUrl.toString();
        }


    }


    /**
     * @param uri
     * @param params
     * @return String
     * @description 带map参数的post请求方法
     */
    public String doPost(String uri, Map<String, String> params) {

        String responseBody;
        HttpPost httpPost = new HttpPost(uri);
        try {
            List<NameValuePair> nvps = Lists.newArrayList();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                nvps.add(new BasicNameValuePair(key, value));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
            httpPost.setConfig(getRequestConfig());
            responseBody = executeRequest(httpPost);

        } catch (Exception e) {
            throw new RuntimeException("httpclient doPost方法异常 ", e);
        } finally {
            httpPost.releaseConnection();
        }

        return responseBody;

    }


    /**
     * @param uri
     * @param param
     * @param contentType 根据具体请求情况指定,比如json可以是 ContentType.APPLICATION_JSON
     * @return String
     * @description 带单string参数执行post方法
     */
    public String doPost(String uri, String param, ContentType contentType) {

        String responseBody;
        HttpPost httpPost = new HttpPost(uri);
        try {
            StringEntity reqEntity = new StringEntity(param, contentType);
            httpPost.setEntity(reqEntity);
            httpPost.setConfig(getRequestConfig());
            responseBody = executeRequest(httpPost);

        } catch (IOException e) {
            throw new RuntimeException("httpclient doPost方法异常 ", e);
        } finally {
            httpPost.releaseConnection();
        }
        return responseBody;
    }

    /**
     * @return RequestConfig
     * @description: 获得请求配置信息
     */
    private RequestConfig getRequestConfig() {


        RequestConfig defaultRequestConfig = RequestConfig.custom()
                //.setCookieSpec(CookieSpecs.DEFAULT)
                .setExpectContinueEnabled(true)
                //.setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
                //.setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
                .build();
        return RequestConfig.copy(defaultRequestConfig).setSocketTimeout(requestConnectionTimeout)
                .setConnectTimeout(requestSocketTimeout)
                .setConnectionRequestTimeout(requestConnectionRequestTimeout)
                .build();
//        return RequestConfig.copy(defaultRequestConfig)
//                .setSocketTimeout(getBean().getRequestConnectionTimeout())
//                .setConnectTimeout(getBean().getRequestSocketTimeout())
//                .setConnectionRequestTimeout(getBean().getRequestConnectionRequestTimeout())
//                .build();

    }


    /**
     * @param method
     * @return String
     * @throws IOException
     * @description 通用执行请求方法
     */
    private String executeRequest(HttpUriRequest method) throws IOException {

        ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

            @Override
            public String handleResponse(final HttpResponse response) throws IOException {

                int status = response.getStatusLine().getStatusCode();
                String result;
                if (status >= HttpStatus.SC_OK && status < HttpStatus.SC_MULTIPLE_CHOICES) {
                    HttpEntity entity = response.getEntity();
                    result = entity != null ? EntityUtils.toString(entity) : null;
                    EntityUtils.consume(entity);
                    return result;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            }

        };
        String result = getHttpClientInstance().execute(method, responseHandler);

        return result;
    }


    /**
     * @return CloseableHttpClient
     * @description 单例获取httpclient实例
     */
    private CloseableHttpClient getHttpClientInstance() {

        if (httpClient == null) {
            synchronized (CloseableHttpClient.class) {
                if (httpClient == null) {
                    httpClient = HttpClients.custom().setConnectionManager(initConfig()).setRetryHandler(getRetryHandler()).build();
                }
            }
        }
        return httpClient;

    }

    /**
     * @return HttpRequestRetryHandler
     * @description :获取重试handler
     */
    private HttpRequestRetryHandler getRetryHandler() {

        // 请求重试处理
        return new HttpRequestRetryHandler() {
            @Override
            public boolean retryRequest(IOException exception,
                                        int executionCount, HttpContext context) {
                if (executionCount >= retryCount) {
                    // 假设已经重试了5次，就放弃
                    return false;
                }
                if (exception instanceof NoHttpResponseException) {
                    // 假设server丢掉了连接。那么就重试
                    return true;
                }
                if (exception instanceof SSLHandshakeException) {
                    // 不要重试SSL握手异常
                    return false;
                }
                if (exception instanceof InterruptedIOException) {
                    // 超时
                    return false;
                }
                if (exception instanceof UnknownHostException) {
                    // 目标server不可达
                    return false;
                }
                if (exception instanceof ConnectTimeoutException) {
                    // 连接被拒绝
                    return false;
                }
                if (exception instanceof SSLException) {
                    // SSL握手异常
                    return false;
                }

                HttpRequest request = HttpClientContext.adapt(context).getRequest();
                // 假设请求是幂等的，就再次尝试
                return !(request instanceof HttpEntityEnclosingRequest);
            }
        };

    }


    /**
     * @return PoolingHttpClientConnectionManager
     * @description 初始化连接池等配置信息
     */
    private PoolingHttpClientConnectionManager initConfig() {

        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new SSLConnectionSocketFactory(SSLContexts.createSystemDefault()))
                .build();

        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);

        /**
         * 以下参数设置含义分别为:
         * 1 是否立即发送数据，设置为true会关闭Socket缓冲，默认为false
         * 2 是否可以在一个进程关闭Socket后，即使它还没有释放端口，其它进程还可以立即重用端口
         * 3 接收数据的等待超时时间，单位ms
         * 4 关闭Socket时，要么发送完所有数据，要么等待多少秒后，就关闭连接，此时socket.close()是阻塞的
         * 5 开启监视TCP连接是否有效
         * 其中setTcpNoDelay(true)设置是否启用Nagle算法，设置true后禁用Nagle算法，默认为false（即默认启用Nagle算法）。
         * Nagle算法试图通过减少分片的数量来节省带宽。当应用程序希望降低网络延迟并提高性能时，
         * 它们可以关闭Nagle算法，这样数据将会更早地发 送，但是增加了网络消耗。 单位为：毫秒
         */

        SocketConfig socketConfig = SocketConfig.custom()
                .setTcpNoDelay(true)
                .setSoReuseAddress(true)
                .setSoTimeout(socketConfigSoTimeout)
                //.setSoLinger(SOCKET_CONFIG_SO_LINGER)
                //.setSoKeepAlive(true)
                .build();

        connManager.setDefaultSocketConfig(socketConfig);
        connManager.setValidateAfterInactivity(validateAfterInActivity);

        ConnectionConfig connectionConfig = ConnectionConfig.custom()
                .setMalformedInputAction(CodingErrorAction.IGNORE)
                .setUnmappableInputAction(CodingErrorAction.IGNORE)
                .setCharset(Consts.UTF_8)
                .build();
        connManager.setDefaultConnectionConfig(connectionConfig);
        connManager.setDefaultMaxPerRoute(defaultMaxPerRoute);
        connManager.setMaxTotal(maxTotalConnections);
        return connManager;

    }
}
