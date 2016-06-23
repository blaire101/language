package com.qunar.fresh.librarysystem.controller;

import com.qunar.fresh.librarysystem.resulttype.JsonResult;
import com.qunar.fresh.librarysystem.service.impl.CategoryService;
import com.qunar.fresh.librarysystem.service.impl.HomePageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created with IntelliJ IDEA. User: he.chen Date: 14-3-31 Time: 下午8:06 To change this template use File | Settings |
 * File Templates.
 */
@Controller
@RequestMapping("")
public class HomePageController {

    @Resource
    private HomePageService homePageService;
    @Resource
    private CategoryService categoryService;
    /**
     * 返回某类型下的图书的top n热度的图书时，n的大小
     */
    private static final int TOP_N = 10;

    @RequestMapping("home.do")
    @ResponseBody
    public Map<String, Object> loadHomePage(HttpServletRequest request) {
        Map<String, Object> categoryList = homePageService.fetchAllCategoriesInfo();
        Map<String, Object> topKindInfo = categoryService.getLoadTopKindInfo(TOP_N);
        if (categoryList == null) {
            return JsonResult.errInfo("获取分类失败");

        } else if (topKindInfo == null) {
            return JsonResult.errInfo("获取top失败");
        }
        JsonResult jsonResult = new JsonResult(true);
        jsonResult.put("nav", categoryList).put("kind", topKindInfo);
        return JsonResult.statusJson(0, "", jsonResult.getJsonDataMap());
    }

}
