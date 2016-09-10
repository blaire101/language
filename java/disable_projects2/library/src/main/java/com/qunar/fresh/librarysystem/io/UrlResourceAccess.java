package com.qunar.fresh.librarysystem.io;

import java.io.*;
import java.util.Random;

import org.slf4j.LoggerFactory;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.client.ResourceAccessException;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.io.ByteStreams;

/**
 * URL资源访问的类
 * 
 * @author hang.gao
 */
public class UrlResourceAccess implements ResourceAccess {

    /**
     * 日志
     */
    private org.slf4j.Logger logger = LoggerFactory.getLogger(UrlResourceAccess.class);

    /**
     * Spring的资源加载器
     */
    private ResourceLoader resourceLoader;

    /**
     * 资源存储的目录u
     */
    private String resourceStorePath;

    /**
     * 资源存储后，访问资源的前缀URL（不包括资源名）
     */
    private String baseResourceAccessUrl;

    /**
     * 随机数生成
     */
    private Random random = new Random();

    @Override
    public void loadPrivateAndStore(String filename, OutputStream outputStream) {
        Preconditions.checkNotNull(filename);
        Preconditions.checkNotNull(outputStream);
        Preconditions.checkArgument(!Strings.isNullOrEmpty(filename));
        File file = new File(resourceStorePath + filename);
        if (file.exists()) {
            InputStream inputStream = null;
            try {
                inputStream = new BufferedInputStream(new FileInputStream(file));
                ByteStreams.copy(inputStream, outputStream);
            } catch (IOException e) {
                throw new ResourceAccessException("Transform file error for local resource " + filename, e);
            } finally {
                // 不关闭outputStream
                closeStream(filename, inputStream, null);
            }
        } else {
            throw new ResourceAccessException("The resource is not exists:" + filename);
        }
    }

    @Override
    public String loadAndStore(String resourceUrl) {
        Preconditions.checkNotNull(resourceUrl);
        Preconditions.checkArgument(!Strings.isNullOrEmpty(resourceUrl));
        String url = resourceUrl.trim();
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = openInputStream(url);
            String filename = generateFileName(url);
            outputStream = openLocalOutputStream(filename);
            ByteStreams.copy(inputStream, outputStream);
            return baseResourceAccessUrl + filename;
        } catch (IOException e) {
            throw new ResourceAccessException("Access the resource from " + url + " failed", e);
        } finally {
            closeStream(url, inputStream, outputStream);
        }
    }

    @Override
    public String loadText(String resourceUrl) {
        Preconditions.checkNotNull(resourceUrl);
        Preconditions.checkArgument(!Strings.isNullOrEmpty(resourceUrl));
        BufferedReader in = null;
        try {
            StringBuilder sb = new StringBuilder();
            in = new BufferedReader(new InputStreamReader(resourceLoader.getResource(resourceUrl).getInputStream()));
            String str;
            while ((str = in.readLine()) != null) {
                sb.append(str);
            }
            return sb.toString();
        } catch (IOException e) {
            throw new ResourceAccessException("Load the resource error:" + resourceUrl, e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error("close the input stream from {} error", resourceUrl);
                }
            }
        }
    }

    /**
     * 关闭流
     * 
     * @param resourceUrl 资源URL
     * @param inputStream 打开资源的输入流
     * @param outputStream 将资源写入本地的输出流
     */
    private void closeStream(String resourceUrl, InputStream inputStream, OutputStream outputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                logger.error("Close the input stream from {} error!", resourceUrl);
            }
        }
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                logger.error("Close the local output stream for {} error!", resourceUrl);
            }
        }
    }

    /**
     * 打开本地输出流，用于将URL指定的资源写在本地
     * 
     * @param filename 写入资源数据的文件名，不包括resourceStorePath
     * @return 打开的新的输出流，将URL表示的资源写入此流
     * @throws IOException 创建文件或者打开文件出错
     */
    private OutputStream openLocalOutputStream(String filename) throws IOException {
        File file = new File(resourceStorePath + filename);
        if (!file.exists()) {
            file.createNewFile();
        }
        return new BufferedOutputStream(new FileOutputStream(file));
    }

    /**
     * 打开资源的输入流
     * 
     * @param resourceUrl 资源的URL
     * @return 打开资源的输入流
     * @throws IOException
     */
    private InputStream openInputStream(String resourceUrl) throws IOException {
        return new BufferedInputStream(resourceLoader.getResource(resourceUrl).getInputStream());
    }

    /**
     * 生成文件名
     * 
     * @param resourceUrl 资源的URL
     * @return 随机生成的文件名
     */
    private String generateFileName(String resourceUrl) {
        int index = resourceUrl.lastIndexOf('.');
        return Long.toString(Math.abs(random.nextLong())) + System.currentTimeMillis()
                + (index == -1 ? "" : resourceUrl.substring(index));
    }

    public void setBaseResourceAccessUrl(String baseResourceAccessUrl) {
        this.baseResourceAccessUrl = baseResourceAccessUrl;
    }

    public void setResourceStorePath(String resourceStorePath) {
        this.resourceStorePath = resourceStorePath;
    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}