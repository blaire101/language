package com.x.demo.util.esjar;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.count.CountRequestBuilder;
import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.elasticsearch.index.query.QueryBuilders.termQuery;

public class ElasticSearchHandler {

    private static ElasticSearchHandler instance;
    private Client client;

    /***
     * @param ipAddresses ip90, ip96, ip98
     * @return
     */
    public static ElasticSearchHandler getInstance(String ipAddresses) {
        if (null == instance) {
            instance = new ElasticSearchHandler(ipAddresses);
        }
        return instance;
    }

    private ElasticSearchHandler(String ipAddresses) {
        Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", "elasticsearch_x")
                .put("client.transport.ignore_cluster_name", false)
                .put("node.client", true)
                .put("client.transport.sniff", true)
                .put("client.transport.ping_timeout", "60s").build();
        TransportClient client = new TransportClient(settings);
        String[] ipAddress = ipAddresses.split(",");
        for (String ip : ipAddress) {
            client.addTransportAddress(new InetSocketTransportAddress(ip, 9300));
        }
        this.client = client;
    }

    public void close() {
        client.close();
    }

    public boolean replaceIndexBatch(String indexName, String indexType, List<String> jsonDatas, List<String> ids) {
        if (StringUtils.isEmpty(indexName) || StringUtils.isEmpty(indexType)) {
            return true;
        }
        if (null == jsonDatas || jsonDatas.isEmpty()) {
            return true;
        }
        boolean ret = true;
        if (ids == null || ids.isEmpty()) {
            ret = this.replaceIndexResponse(indexName, indexType, jsonDatas);
        } else {
            ret = this.replaceIndexResponse(indexName, indexType, jsonDatas, ids);
            ids.clear();
        }
        jsonDatas.clear();
        return ret;
    }

    /**
     * 批量写入索引数据
     *
     * @param indexName 为索引库名，一个es集群中可以有多个索引库。 名称必须为小写
     * @param indexType Type为索引类型，是用来区分同索引库下不同类型的数据的，一个索引库下可以有多个索引类型。
     * @param jsonDatas json格式的数据集合
     * @param ids       索引Id
     * @return
     */
    private boolean replaceIndexResponse(String indexName, String indexType, List<String> jsonDatas, List<String> ids) {
        if (jsonDatas.size() == 0 || ids.size() == 0) {
            return true;
        }
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        for (int i = 0; i < jsonDatas.size(); i++) {
            IndexRequestBuilder indexRequestBuilder = client.prepareIndex(indexName, indexType, ids.get(i));
            indexRequestBuilder.setSource(jsonDatas.get(i));
            bulkRequest.add(indexRequestBuilder);
        }
        BulkResponse bulkResponse = bulkRequest.execute().actionGet();
        return !bulkResponse.hasFailures();
    }

    /***
     * 批量写入索引数据
     *
     * @param indexName
     * @param indexType
     * @param jsonDatas
     */
    private boolean replaceIndexResponse(String indexName, String indexType, List<String> jsonDatas) {
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        for (int i = 0; i < jsonDatas.size(); i++) {
            IndexRequestBuilder indexRequestBuilder = client.prepareIndex(indexName, indexType);
            indexRequestBuilder.setSource(jsonDatas.get(i));
            bulkRequest.add(indexRequestBuilder);
        }
        BulkResponse bulkResponse = bulkRequest.execute().actionGet();
        return !bulkResponse.hasFailures();
    }


    /**
     * 建立索引,索引建立好之后,会在elasticsearch-0.20.6\data\elasticsearch\nodes\0创建所以你看
     *
     * @param indexName 为索引库名，一个es集群中可以有多个索引库。 名称必须为小写
     * @param indexType Type为索引类型，是用来区分同索引库下不同类型的数据的，一个索引库下可以有多个索引类型。
     * @param jsondata  json格式的数据集合
     * @return
     */

    public IndexResponse replaceIndexResponse(String indexName, String indexType, String jsondata) {
        IndexResponse response = client.prepareIndex(indexName, indexType)
                .setSource(jsondata)
                .execute()
                .actionGet();
        return response;
    }

    /***
     * 写入索引数据，若无索引类型则动态创建
     *
     * @param indexName
     * @param indexType
     * @param jsondata
     * @param indexUrl
     * @param typeUrl
     * @param typeDdl
     * @return
     */
    public IndexResponse replaceIndexResponse(String indexName, String indexType, String jsondata, String indexUrl, String typeUrl, String typeDdl) {
        try {
            if (!indexExist(indexName)) {
                createIndex(indexUrl);
            }
            if (!typeExist(indexName, indexType)) {
                createType(typeUrl, typeDdl);
            }
        } catch (IOException e) {
            throw new XSystemException("创建索引或类型时出错", e);
        }
        IndexResponse response = replaceIndexResponse(indexName, indexType, jsondata);
        return response;
    }


    public void createIndex(String indexUrl) throws IOException {
        HttpPut request = new HttpPut(indexUrl);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = httpclient.execute(request);
        httpclient.close();
    }

    public void createType(String typeUrl, String typeDdl) throws IOException {
        HttpPut request = new HttpPut(typeUrl);
        String typeDdlFmt = typeDdl.replaceAll("\\s*", "");//匹配任何空白字符
        request.setEntity(new StringEntity(typeDdlFmt));
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = httpclient.execute(request);
        httpclient.close();
    }

    public boolean indexExist(String indexName) {
        boolean indexExists = client.admin().indices().prepareExists(indexName).execute().actionGet().isExists();
        return indexExists;
    }

    public boolean typeExist(String indexName, String typeName) {
        boolean indexExists = client.admin().indices().prepareTypesExists(indexName).setTypes(typeName).execute().actionGet().isExists();
        return indexExists;
    }

    /**
     * 执行搜索
     *
     * @param queryBuilder
     * @param indexName
     * @param typeName
     * @return
     */
    public SearchHit[] searcher(QueryBuilder queryBuilder, String indexName, String typeName, int size) {
        SearchResponse searchResponse = client.prepareSearch(indexName).setTypes(typeName)
                .setQuery(queryBuilder).setSize(size)
                .execute()
                .actionGet();
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHists = hits.getHits();
        return searchHists;
    }
    /**
     * 执行带有一个过滤条件的搜索
     *
     * @param queryBuilder
     * @param indexName
     * @param typeName
     * @return
     */
    public SearchHit[] searcherOneFilter(QueryBuilder queryBuilder, String indexName, String typeName, String key, Object value, int size) {

        SearchResponse searchResponse = client.prepareSearch(indexName).setTypes(typeName)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(termQuery(key, value))             // Query
                .setFrom(0).setSize(size).setExplain(true)
                .execute()
                .actionGet();
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHists = hits.getHits();
        return searchHists;
        // .setPostFilter(FilterBuilders.rangeFilter("age").from(12).to(18))   // Filter
    }
    public long getSize(String indexName, String typeName) throws ExecutionException, InterruptedException {
        CountRequestBuilder builder = client.prepareCount(indexName).setTypes(typeName);
        CountResponse response = builder.execute().get();
        long count = response.getCount();
        return count;
    }

    /**
     * 执行带有多个过滤条件的搜索
     *
     * @param queryBuilder
     * @param indexName
     * @param typeName
     * @return
     */
    public SearchHit[] searcherMoreCondition(QueryBuilder queryBuilder, String indexName, String typeName, int size) {

        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(indexName).setTypes(typeName).setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        SearchResponse searchResponse = searchRequestBuilder.setQuery(queryBuilder).setFrom(0).setSize(size).setExplain(true)
                .execute()
                .actionGet();
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHists = hits.getHits();
        return searchHists;
    }
}
