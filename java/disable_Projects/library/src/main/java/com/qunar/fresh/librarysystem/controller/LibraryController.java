package com.qunar.fresh.librarysystem.controller;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.qunar.fresh.librarysystem.resulttype.JsonResult;
import com.qunar.fresh.librarysystem.model.Library;
import com.qunar.fresh.librarysystem.service.BookService;
import com.qunar.fresh.librarysystem.service.impl.LibraryService;
import com.qunar.fresh.librarysystem.service.impl.ManagerService;
import com.qunar.fresh.librarysystem.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA. User: he.chen Date: 14-3-27 Time: 下午6:31 To change this template use File | Settings |
 * File Templates.
 */
@Controller
@RequestMapping("/superAdmin")
public class LibraryController {
    private static final Logger logger = LoggerFactory.getLogger(LibraryController.class);
    private static final DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
    @Resource
    private LibraryService libraryService;

    @Resource
    private ManagerService managerService;

    @Resource
    private BookService bookService;

    @RequestMapping("load.do")
    @ResponseBody
    public Map<String, Object> loadSuperManager() {
        Map<String, Object> allLibraryInfo = libraryService.getAllLibInfo();
        if (allLibraryInfo == null) {
            return JsonResult.errInfo("获取所有图书馆信息失败");
        }
        return JsonResult.statusJson(0, "", allLibraryInfo);
    }

    @RequestMapping("view.do")
    @ResponseBody
    public Map<String, Object> viewLibrary(@RequestParam("libId") int libId) {
        try {
            Preconditions.checkState(libId >= 0);
        } catch (IllegalStateException e) {
            logger.error("无效的图书馆id", e);
            return JsonResult.errInfo("无效的图书馆id");
        }
        List<Map<String, Object>> managerList = managerService.fetchAllManagerJson(libId);
        int bookCount = bookService.getBookCountInOneLib(libId);
        if (managerList == null) {
            return JsonResult.errInfo("获取管理员列表失败");
        }
        Library library = libraryService.fetchLibraryByLibId(libId);

        JsonResult jsonResult = new JsonResult(true);
        jsonResult.put("libId", libId);
        jsonResult.put("createTime", dateformat.format(library.getCreateTime()));
        jsonResult.put("libName", library.getLibName());
        jsonResult.put("libDept", library.getLibDept());
        jsonResult.put("bookTotalCount", bookCount).put("adminList", managerList);
        return JsonResult.statusJson(0, "", jsonResult.getJsonDataMap());
    }

    @Transactional
    @RequestMapping("addlib.do")
    @ResponseBody
    public Map<String, Object> addOneLibrary(@RequestParam("libDept") String libDept,
            @RequestParam("libName") String libName, HttpServletRequest request) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(libName.trim()));
        Preconditions.checkArgument(!Strings.isNullOrEmpty(libDept.trim()));
        String userRtx = UserUtils.getUserRtx(request);
        int libId = UserUtils.getUserLib(request);
        boolean isAddSuccee = libraryService.addOneLibrary(libDept, libName, userRtx, libId);
        if (!isAddSuccee) {
            return JsonResult.errInfo("已经有同部门同名图书馆");
        }
        Map<String, Object> allLibraryInfo = libraryService.getAllLibInfo();
        if (allLibraryInfo == null) {
            return JsonResult.errInfo("获取所有图书馆信息失败");
        }
        return JsonResult.statusJson(0, "", allLibraryInfo);
    }

    @RequestMapping("deletelib.do")
    @ResponseBody
    public Map<String, Object> deleteLibrary(@RequestParam("libId") int libId, HttpServletRequest request) {
        Preconditions.checkArgument(libId >= 0);
        Library deletedLibrary = libraryService.fetchLibraryByLibId(libId);
        if (deletedLibrary == null) {
            return JsonResult.errInfo("无效的图书馆");
        }
        boolean isDelete = libraryService.deleteLibrary(deletedLibrary, request);
        if (!isDelete) {
            return JsonResult.errInfo("还有未还图书，无法删除");
        }
        Map<String, Object> allLibraryInfo = libraryService.getAllLibInfo();
        if (allLibraryInfo == null) {
            return JsonResult.errInfo("获取所有图书馆信息失败");
        }
        return JsonResult.statusJson(0, "", allLibraryInfo);
    }

}
