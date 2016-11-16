package com.qunar.fresh.librarysystem.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 访问资源的接口
 * 
 * @author hang.gao
 * 
 */
public interface ResourceAccess {

    /**
     * 从本地加载资源，并写入outputStream中，此方法不会关闭outputStream，如果本地资源不存在，则什么也不做
     * 
     * @param filename 文件名，
     * @param outputStream 输出流，将数据写入此流
     */
    void loadPrivateAndStore(String filename, OutputStream outputStream);

    /**
     * 从URL中读取数据
     * 
     * @param resourceUrl
     * @return 数据存储之后的URL
     * @throws IOException
     */
    String loadAndStore(String resourceUrl);

    /**
     * 加载文本
     * 
     * @param resourceUrl 资源的URL
     * @return 资源的文本内容
     */
    String loadText(String resourceUrl);

}