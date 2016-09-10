package com.x.dmt.util;

import com.x.dmt.util.esjar.ElasticSearchHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ResourceBundle;

/**
 * Author : clb@xkeshi.com
 * Date : 2015-11-27
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
