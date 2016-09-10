package com.qunar.fresh.librarysystem.exception;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import com.google.common.base.Optional;
import com.qunar.fresh.librarysystem.resulttype.JsonResult;

/**
 * 全局的异常处理
 * 
 * @author hang.gao
 * 
 */
public class CustomExceptionResolver extends AbstractHandlerExceptionResolver {

    /**
     * 记录日志
     */
    private Logger logger = LoggerFactory.getLogger(CustomExceptionResolver.class);

    /**
     * 异常信息映射
     */
    private Map<String, String> errorMessageMapping;

    /**
     * 默认使用的异常信息，当errorMessageMapping中找不到异常信息时使用此信息
     */
    private String defaultMessage;

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) {
        try {
            Map<String, Object> attributes = JsonResult.statusJson(1,
                    Optional.fromNullable(errorMessageMapping.get(ex.getClass().getName())).or(defaultMessage),
                    JsonResult.dataJson(false, "data", ""));
            MappingJacksonJsonView mappingJacksonJsonView = new MappingJacksonJsonView();
            mappingJacksonJsonView.setAttributesMap(attributes);
            return new ModelAndView(mappingJacksonJsonView);
        } catch (Throwable t) {
            logger.error("System error", t);
            return null;
        }
    }

    public Map<String, String> getErrorMessageMapping() {
        return errorMessageMapping;
    }

    public void setErrorMessageMapping(Map<String, String> errorMessageMapping) {
        this.errorMessageMapping = errorMessageMapping;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    public void setDefaultMessage(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

}
