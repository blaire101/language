package com.x.demo.util;

import com.x.demo.util.esjar.ElasticSearchHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ResourceBundle;

/**
 * Date : 2016-06-22
 */
public class EsHandler {


    private static String elasticSearchIPs = ResourceBundle.getBundle("props.es_config").getString("ElasticSearchIPs");

    // online
    private static final EsHandler instance = new EsHandler(
            ElasticSearchHandler.getInstance(elasticSearchIPs));

//    private static final EsHandler instance = new EsHandler(ElasticSearchHandler.getInstance(Constant.DEFAULT_ES_IPS));

    public static final EsHandler getInstance() {
        return instance;
    }


    private static Log logger = LogFactory.getLog(EsHandler.class);

    private ElasticSearchHandler elasticSearchHandler;

    public EsHandler(ElasticSearchHandler elasticSearchHandler) {
        this.elasticSearchHandler = elasticSearchHandler;
    }

    public ElasticSearchHandler getElasticSearchHandler() {
        return elasticSearchHandler;
    }

    public void setElasticSearchHandler(ElasticSearchHandler elasticSearchHandler) {
        this.elasticSearchHandler = elasticSearchHandler;
    }
}
