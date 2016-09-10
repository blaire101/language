package com.qunar.fresh.librarysystem.utils;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA. User: he.chen Date: 14-4-12 Time: 上午11:31 这个类是参考第一组的Exam项目的
 * 
 */
public class HttpUtil {
    private final static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    private static HttpClient getHc() {
        HttpClient client = new HttpClient();

        List<Header> headers = new ArrayList<Header>();
        headers.add(new Header("Accept", "*/*"));
        headers.add(new Header("Accept-Language", "zh-cn"));
        headers.add(new Header("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0)"));
        headers.add(new Header("UA-CPU", "x86"));
        client.getHostConfiguration().getParams().setParameter("http.default-headers", headers);

        client.getHttpConnectionManager().getParams().setConnectionTimeout(30000);
        client.getHttpConnectionManager().getParams().setSoTimeout(30000);

        return client;
    }

    public static String getContent(String url, String encoding) throws HttpException, IOException {
        String content = null;
        GetMethod get = null;
        try {
            if (encoding == null || encoding.equals("")) {
                encoding = "UTF-8";
            }

            HttpClient client = getHc();
            get = new GetMethod(url);
            get.getParams().setContentCharset(encoding);
            int status = client.executeMethod(get);
            if (status == 200) {
                Header encodeHeader = get.getResponseHeader("Content-Encoding");
                if (encodeHeader != null && encodeHeader.getValue().equals("gzip")) {
                    content = UncompressTool.unZip(get.getResponseBodyAsStream(), encoding);
                } else if (encodeHeader != null && encodeHeader.getValue().equals("deflate")) {
                    content = UncompressTool.unDeflater(get.getResponseBodyAsStream(), encoding);
                } else {
                    content = IoTool.stream2String(get.getResponseBodyAsStream(), encoding);
                }
            }
        } catch (Exception e) {
            logger.warn("get http content error:" + url, e);
        } finally {
            if (get != null) {
                get.releaseConnection();
            }
        }
        return content;
    }

    public static int getContent2(String url, String encoding) {
        GetMethod get = null;
        try {
            if (encoding == null || encoding.equals("")) {
                encoding = "UTF-8";
            }

            HttpClient client = getHc();
            get = new GetMethod(url);
            get.getParams().setContentCharset(encoding);
            int status = client.executeMethod(get);
            return status;
        } catch (Exception e) {
            logger.warn("get http content error:" + url, e);
        } finally {
            if (get != null) {
                get.releaseConnection();
            }
        }
        return 0;
    }

    public static int postContent2(String url, String encoding, String args) {
        PostMethod post = null;
        try {
            if (encoding == null || encoding.equals("")) {
                encoding = "UTF-8";
            }
            HttpClient client = getHc();
            post = new PostMethod(url);
            post.getParams().setContentCharset(encoding);
            post.setRequestEntity(new StringRequestEntity(args, "application/x-www-form-urlencoded", null));

            int status = client.executeMethod(post);
            return status;
        } catch (Exception e) {
            logger.warn("post http content error:" + url, e);
        } finally {
            if (post != null) {
                post.releaseConnection();
            }
        }
        return 0;
    }

    public static String postContent(String url, String encoding, String args) {
        String content = null;
        PostMethod post = null;
        try {
            if (encoding == null || encoding.equals("")) {
                encoding = "UTF-8";
            }
            HttpClient client = getHc();
            post = new PostMethod(url);
            post.getParams().setContentCharset(encoding);
            post.setRequestEntity(new StringRequestEntity(args, "application/x-www-form-urlencoded", null));

            int status = client.executeMethod(post);
            if (status == 200) {
                Header encodeHeader = post.getResponseHeader("Content-Encoding");
                if (encodeHeader != null && encodeHeader.getValue().equals("gzip")) {
                    content = UncompressTool.unZip(post.getResponseBodyAsStream(), encoding);
                } else if (encodeHeader != null && encodeHeader.getValue().equals("deflate")) {
                    content = UncompressTool.unDeflater(post.getResponseBodyAsStream(), encoding);
                } else {
                    content = IoTool.stream2String(post.getResponseBodyAsStream(), encoding);
                }
            }
        } catch (Exception e) {
            logger.warn("post http content error:" + url, e);
        } finally {
            if (post != null) {
                post.releaseConnection();
            }
        }
        return content;
    }

    private static HttpClient getSpringClient() {
        HttpClient client = new HttpClient();

        List<Header> headers = new ArrayList<Header>();
        headers.add(new Header("Accept", "*/*"));
        headers.add(new Header("Accept-Language", "zh-cn"));
        headers.add(new Header("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0)"));
        headers.add(new Header("UA-CPU", "x86"));
        headers.add(new Header("Content-Type", "application/json;charset=UTF-8"));
        client.getHostConfiguration().getParams().setParameter("http.default-headers", headers);

        client.getHttpConnectionManager().getParams().setConnectionTimeout(30000);
        client.getHttpConnectionManager().getParams().setSoTimeout(30000);

        return client;
    }

    public static String postContentForSpring(String url, String encoding, String args) {
        String content = null;
        PostMethod post = null;
        try {
            if (encoding == null || encoding.equals("")) {
                encoding = "UTF-8";
            }
            HttpClient client = getSpringClient();
            post = new PostMethod(url);
            post.getParams().setContentCharset(encoding);
            post.setRequestEntity(new StringRequestEntity(args, "application/x-www-form-urlencoded", null));

            int status = client.executeMethod(post);
            if (status == 200) {
                Header encodeHeader = post.getResponseHeader("Content-Encoding");
                if (encodeHeader != null && encodeHeader.getValue().equals("gzip")) {
                    content = UncompressTool.unZip(post.getResponseBodyAsStream(), encoding);
                } else if (encodeHeader != null && encodeHeader.getValue().equals("deflate")) {
                    content = UncompressTool.unDeflater(post.getResponseBodyAsStream(), encoding);
                } else {
                    content = IoTool.stream2String(post.getResponseBodyAsStream(), encoding);
                }
            }
        } catch (Exception e) {
            logger.warn("post http content error:" + url, e);
        } finally {
            if (post != null) {
                post.releaseConnection();
            }
        }
        return content;
    }

    public static void main(String[] args) {
        getContent2("http://baidu.com", "utf-8");
    }
}
