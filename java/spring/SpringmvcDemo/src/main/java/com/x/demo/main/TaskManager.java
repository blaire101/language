package com.x.demo.main;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.x.demo.service.CategoryInfoService;
import com.x.demo.util.Constant;
import com.x.demo.model.CategoryInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class TaskManager {

    private static final TaskManager instance = new TaskManager();

    public static final TaskManager getInstance() {
        return instance;
    }

    private static Log logger = LogFactory.getLog(TaskManager.class);

    private String categoryFile;

    @Resource
    private CategoryInfoService categoryInfoService;

    public String getLogFile() {
        return categoryFile;
    }

    public void setLogFile(String logFile) {
        this.categoryFile = logFile;
    }

    public boolean run() throws IOException {
        this.loadDefaultConfigFiles();
        this.loadCategoryInfoList();
        return true;
    }

    public void loadDefaultConfigFiles() throws IOException {
        this.categoryFile = Constant.JD_ABS_CATEGORY_FILE; // "disable.configFiles/newlog.txt"
    }

    /**
     * load loadShopContextList
     */
    private void loadCategoryInfoList() throws IOException {

        logger.info("categoryFile : " + this.categoryFile + " start analyze...");

        Preconditions.checkArgument(StringUtils.isNotEmpty(this.categoryFile));

//        File file = new ClassPathResource(this.categoryFile).getFile();
        File file = new File(this.categoryFile);

        Preconditions.checkArgument(file.exists());

        List<CategoryInfo> categoryInfoList = Lists.newArrayList();


        /**  add List  **/
        CategoryInfo categoryInfo = new CategoryInfo(0L, "大家电");
        CategoryInfo categoryInfo1 = new CategoryInfo(0L, "大家电");
        CategoryInfo categoryInfo2 = new CategoryInfo(0L, "大家电");

        categoryInfoList.add(categoryInfo);
        categoryInfoList.add(categoryInfo1);
        categoryInfoList.add(categoryInfo2);

        logger.info("start : add db addCategoryInfo ...");
        this.categoryInfoService.addCategoryInfo(categoryInfo);

        this.categoryInfoService.addCategoryInfoList(categoryInfoList);

//
//        LineIterator it = FileUtils.lineIterator(file, "UTF-8");
//
//        try {
//            while (it.hasNext()) {
//                String line = it.nextLine().trim();
//                if (line.isEmpty()) {
//                    continue;
//                }
////                loadShopContextListByLine(line, categoryInfoList);
//                if (categoryInfoList.size() == Constant.INSERT_BATCH_COUNT) {
//                    this.categoryInfoService.addCategoryInfoList(categoryInfoList);
//                    categoryInfoList.clear();
//                }
//            }
//        } finally {
//            logger.info("this analyze and insert mysql end!");
//            LineIterator.closeQuietly(it);
//        }
//        this.categoryInfoService.addCategoryInfoList(categoryInfoList);
//        categoryInfoList.clear();
//        return;
    }
}
