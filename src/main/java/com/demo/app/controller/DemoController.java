package com.demo.app.controller;

import com.demo.app.biz.DemoBiz;
import com.demo.app.controller.base.BaseController;
import com.demo.app.entity.DataEntity;
import com.demo.app.entity.DataQuery;
import com.demo.app.entity.common.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by xiongcaesar on 2018/4/28.
 *
 */
@Controller
@RequestMapping("/app/data")
public class DemoController extends BaseController {

    @Resource
    private DemoBiz demoBiz;

    /**
     * 页面展示.
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView index() {
        ModelAndView mv = new ModelAndView();
        mv.addObject("name", "Caesar");
        mv.setViewName("index");
        return mv;
    }

    /**
     * 上传文件.
     */
    @ResponseBody
    @RequestMapping(value = "/upload", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Result upload(@RequestParam(value = "dataFile", required = false)MultipartFile dataFile) {
        if (dataFile == null || dataFile.isEmpty()) {
            return Result.fail("Please choose a file to upload！");
        }
        if (StringUtils.isNotBlank(dataFile.getOriginalFilename())
                && !dataFile.getOriginalFilename().endsWith(".csv")) {
            return Result.fail("File format error，not a csv file！");
        }
        return demoBiz.parseFileData(dataFile);
    }

    /**
     * 分页获取数据.
     */
    @ResponseBody
    @RequestMapping(value = "getDatas", method = RequestMethod.GET)
    public Result getDatas(DataQuery query) {
        if (query == null) {
            return Result.fail("请求参数为空");
        }
        if (query.getCurrentPage() < 0) {
            query.setCurrentPage(0);
        }
        if (query.getCurrentPage() > 0) {
            query.setCurrentPage(query.getCurrentPage() - 1);
        }
        if (query.getPageSize() <= 0) {
            query.setPageSize(10);
        }
        int totalSize = demoBiz.countDataEntities(query);
        List<DataEntity> dataEntities = demoBiz.getPagedDataEntities(query);
        return Result
                .success(dataEntities)
                .setCurrentPage(query.getCurrentPage() + 1)
                .setPageSize(query.getPageSize())
                .setTotalSize(totalSize)
                .setTotalPage(totalSize, dataEntities.size());
    }

    /**
     * 下载文件.
     */
    @ResponseBody
    @RequestMapping(value = "downloadFile", method = RequestMethod.POST)
    public Result downloadFile(HttpServletRequest request, HttpServletResponse response) {
        return demoBiz.downloadFile(request, response);
    }

    /**
     * 分析结果.
     */
    @ResponseBody
    @RequestMapping(value = "getDataAnalysisResults", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getDataAnalysisResults() {
        return demoBiz.getDataAnalysisResults();
    }
}
