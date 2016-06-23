package com.qunar.fresh.librarysystem.io;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.client.ResourceAccessException;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ExecutionException;

/**
 * 使用缓存的资源访问
 * 
 * @author hang.gao
 */
public final class CachedResourceAccess implements ResourceAccess, InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(CachedResourceAccess.class);

    /**
     * 委托对象
     */
    private final ResourceAccess resourceAccess;

    /**
     * 资源的缓存
     */
    private LoadingCache<String, byte[]> resourceCache;

    /**
     * 缓存大小
     */
    private long cacheSize;

    public CachedResourceAccess(ResourceAccess resourceAccess) {
        this.resourceAccess = resourceAccess;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.trace("Initial cache.The size is {}", cacheSize);
        resourceCache = CacheBuilder.newBuilder().weakValues().maximumSize(cacheSize)
                .build(new CacheLoader<String, byte[]>() {
                    @Override
                    public byte[] load(String key) throws Exception {
                        logger.trace("Load resource {}", key);
                        return readResource(key);
                    }
                });
    }

    @Override
    public void loadPrivateAndStore(String filename, OutputStream outputStream) {
        try {
            outputStream.write(resourceCache.get(filename));
        } catch (IOException e) {
            throw new ResourceAccessException("Write to the output stream failed", e);
        } catch (ExecutionException e) {
            throw new RuntimeException("Cache error", e);
        }
    }

    @Override
    public String loadAndStore(String resourceUrl) {
        return resourceAccess.loadAndStore(resourceUrl);
    }

    @Override
    public String loadText(String resourceUrl) {
        return resourceAccess.loadText(resourceUrl);
    }

    public void setCacheSize(long cacheSize) {
        this.cacheSize = cacheSize;
    }

    /**
     * 读取资源文件
     * 
     * @param filename 文件名
     * @return 资源文件的byte数组
     */
    private byte[] readResource(String filename) {
        ByteArrayOutputStream out = null;
        BufferedOutputStream bufferedOutputStream = null;
        try {
            out = new ByteArrayOutputStream();
            bufferedOutputStream = new BufferedOutputStream(out);
            resourceAccess.loadPrivateAndStore(filename, bufferedOutputStream);
            bufferedOutputStream.flush();
            return out.toByteArray();
        } catch (IOException e) {
            throw new ResourceAccessException("Load ersource error", e);
        } finally {
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (IOException e) {
                    logger.error("Close the ByteArrayOutputStream failed", e);
                }
            }
        }
    }
}
