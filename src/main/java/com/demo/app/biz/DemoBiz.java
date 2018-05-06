package com.demo.app.biz;

import com.demo.app.dao.DemoMapper;
import com.demo.app.entity.DataAnalysisResult;
import com.demo.app.entity.DataEntity;
import com.demo.app.entity.DataQuery;
import com.demo.app.entity.common.Result;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by xiongcaesar on 2018/4/29.
 */
@Service
public class DemoBiz {

    private static final Logger logger = LoggerFactory.getLogger(DemoBiz.class);

    private static String targetFileName = "测试题数据样本-v4.0.csv";

    @Value("${upload.directory}")
    private String uploadDir;

    @Resource
    private DemoMapper demoMapper;

    /**
     * 保存文件，并解析其中的数据到数据库.
     */
    public Result parseFileData(MultipartFile dataFile) {
        File uploadedFile = new File(uploadDir, dataFile.getOriginalFilename());
        try {
            FileUtils.copyInputStreamToFile(dataFile.getInputStream(), uploadedFile);
        } catch (IOException exe) {
            logger.error("上传文件失败", exe);
            return Result.fail("上传文件到服务器失败，请稍后重试");
        }
        targetFileName = dataFile.getOriginalFilename();
        try (FileReader fReader = new FileReader(uploadedFile);
             CSVReader csvReader = new CSVReader(fReader)) {
            // TODO 校验数据
            // 写入数据库（考虑异步写入）
            this.batchInsertData(csvReader);
        } catch (IOException exe) {
            logger.error("解析文件出错", exe);
            return Result.fail("解析文件失败，请检查文件格式或内容是否有误");
        }
        return Result.success("success");
    }

    private void batchInsertData(CSVReader csvReader) throws IOException {
        // 每次读取一行，每500行存一次数据库
        String[] eachLine = csvReader.readNext();
        List<DataEntity> dataEntities = new ArrayList<>(500);
        int i = 0;
        while (eachLine != null && eachLine.length > 0) {
            // 过滤掉表头，过滤掉列数不够的行
            if (i == 0 || eachLine.length < 13) {
                i++;
                eachLine = csvReader.readNext();
                continue;
            }
            DataEntity dataEntity = new DataEntity();
            dataEntity.setColumnA(new BigDecimal(StringUtils.isBlank(eachLine[0]) ? 0 :
                    Float.parseFloat(eachLine[0])));
            dataEntity.setColumnB(new BigDecimal(StringUtils.isBlank(eachLine[0]) ? 0 :
                    Float.parseFloat(eachLine[1])));
            dataEntity.setColumnC(new BigDecimal(StringUtils.isBlank(eachLine[0]) ? 0 :
                    Float.parseFloat(eachLine[2])));
            dataEntity.setColumnD(new BigDecimal(StringUtils.isBlank(eachLine[0]) ? 0 :
                    Float.parseFloat(eachLine[3])));
            dataEntity.setColumnE(new BigDecimal(StringUtils.isBlank(eachLine[0]) ? 0 :
                    Float.parseFloat(eachLine[4])));
            dataEntity.setColumnF(new BigDecimal(StringUtils.isBlank(eachLine[0]) ? 0 :
                    Float.parseFloat(eachLine[5])));
            dataEntity.setColumnG(new BigDecimal(StringUtils.isBlank(eachLine[0]) ? 0 :
                    Float.parseFloat(eachLine[6])));
            dataEntity.setColumnH(new BigDecimal(StringUtils.isBlank(eachLine[0]) ? 0 :
                    Float.parseFloat(eachLine[7])));
            dataEntity.setColumnI(new BigDecimal(StringUtils.isBlank(eachLine[0]) ? 0 :
                    Float.parseFloat(eachLine[8])));
            dataEntity.setColumnJ(new BigDecimal(StringUtils.isBlank(eachLine[0]) ? 0 :
                    Float.parseFloat(eachLine[9])));
            dataEntity.setColumnK(new BigDecimal(StringUtils.isBlank(eachLine[0]) ? 0 :
                    Float.parseFloat(eachLine[10])));
            dataEntity.setColumnL(new BigDecimal(StringUtils.isBlank(eachLine[0]) ? 0 :
                    Float.parseFloat(eachLine[11])));
            dataEntity.setQuality(eachLine[12]);
            dataEntities.add(dataEntity);
            if (i == 500) {
                demoMapper.insertList(dataEntities);
                dataEntities = new ArrayList<>(500);
                i = 1;
                eachLine = csvReader.readNext();
                continue;
            }
            i++;
            eachLine = csvReader.readNext();
        }
        if (i > 0 && i < 500) {
            demoMapper.insertList(dataEntities);
        }
    }

    public int countDataEntities(DataQuery query) {
        return demoMapper.countByParam(query);
    }

    public List<DataEntity> getPagedDataEntities(DataQuery query) {
        return demoMapper.selectByParam(query);
    }

    public File getTargetFile() {
        return new File(uploadDir, targetFileName);
    }

    public List<String[]> readFile(File targetFile) {
        try (FileReader fileReader = new FileReader(targetFile);
             CSVReader csvReader = new CSVReader(fileReader)) {
            return csvReader.readAll();
        } catch (IOException exe) {
            logger.error("读取文件失败", exe);
            return Collections.emptyList();
        }
    }

    public Result editFile(List<String[]> contentArray, int lineIndex, int columnIndex, String newValue) {
        if (lineIndex <= 0 || lineIndex > contentArray.size()) {
            return Result.fail("指定行数不能<=0，且不能大于文件总行数");
        }
        if (columnIndex <= 0) {
            return Result.fail("指定列数不能<=0");
        }
        String[] lineContent = contentArray.get(lineIndex - 1);
        if (columnIndex > lineContent.length) {
            return Result.fail("指定列数大于文件总列数");
        }
        lineContent[columnIndex - 1] = newValue;
        return Result.success("success");
    }

    public Result removeLine(List<String[]> contentArray, int lineIndex) {
        if (lineIndex <= 0 || lineIndex > contentArray.size()) {
            return Result.fail("指定行数不能<=0，且不能大于文件总行数");
        }
        contentArray.remove(lineIndex - 1);
        return Result.success("success");
    }

    public Result<List<String[]>> removeColumn(List<String[]> contentArray, int columnIndex) {
        List<String[]> newContentArray = new ArrayList<>(contentArray.size());
        contentArray.forEach(lineContent ->
                newContentArray.add(ArrayUtils.remove(lineContent, columnIndex - 1)));
        Result<List<String[]>> result = new Result<>();
        result.setMessage("success");
        result.setData(newContentArray);
        return result;
    }

    public Result writeFile(File targetFile, List<String[]> contentArray) {
        // 更新文件内容
        try (FileWriter fileWriter = new FileWriter(targetFile);
             CSVWriter csvWriter = new CSVWriter(fileWriter)) {
            csvWriter.writeAll(contentArray);
        } catch (IOException exe) {
            logger.error("写入文件失败", exe);
            return Result.fail("写入文件失败");
        }
        return Result.success("success");
    }

    /**
     * 下载文件，http断点续传.
     */
    public Result downloadFile(HttpServletRequest request, HttpServletResponse response) {
        File targetFile = new File(uploadDir, targetFileName);
        if (!targetFile.exists()) {
            return Result.fail("要下载的文件不存在");
        }
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(uploadDir + targetFileName, "r")) {
            response.reset();
            response.setHeader("Server", "localhost");
            // 告诉客户端允许断点续传多线程连接下载
            response.setHeader("Accept-Ranges", "bytes");
            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", String.format("attachment;filename=\"%s\"",
                    new String(targetFile.getName().getBytes("UTF-8"), "ISO-8859-1")));
            // 文件下载开始位置
            long startPosition = 0;
            long fileLength = targetFile.length();
            if (request.getHeader("Range") != null) {
                response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
                startPosition = Long.parseLong(request.getHeader("Range")
                        .replaceAll("bytes=", "")
                        .replaceAll("-", ""));
            }
            // 下载的文件长度
            response.setHeader("Content-Length", Long.toString(fileLength - startPosition));
            // 不是从最开始下载
            if (startPosition != 0) {
                response.setHeader("Content-Range", String.format("bytes %s-%s/%s", startPosition, fileLength - 1,
                        fileLength));
            }
            randomAccessFile.seek(startPosition);

            byte[] bytes = new byte[1024 * 100];
            int index;
            while ((index = randomAccessFile.read(bytes)) != -1) {
                response.getOutputStream().write(bytes, 0, index);
            }
        } catch (IOException exe) {
            logger.error("下载文件出现错误", exe);
            return Result.fail("下载出错");
        }
        return Result.success("download success");
    }

    public Result getDataAnalysisResults() {
        List<String> columnNames = demoMapper.selectNeededColumns();
        if (CollectionUtils.isEmpty(columnNames)) {
            return Result.fail("指定表结构有误");
        }
        // 分析数值类型的列
        List<DataAnalysisResult> analysisResults = new ArrayList<>(columnNames.size() + 1);
        columnNames.forEach(columnName -> {
            DataAnalysisResult analysisResult = DataAnalysisResult
                    .create()
                    .columName(columnName)
                    .averageValue(demoMapper.selectAverageValue(columnName))
                    .standardDeviation(demoMapper.selectStdValue(columnName))
                    .outlierNum(demoMapper.selectOuliersNum(columnName));
            analysisResults.add(analysisResult);
        });
        // 分析字符类型的列
        List<String> factorInfos = demoMapper.selectFactorInfo();
        if (!CollectionUtils.isEmpty(factorInfos)) {
            DataAnalysisResult analysisResult = DataAnalysisResult.create().columName("quality");
            factorInfos.forEach(factor ->
                analysisResult.factorNum(factor.split("=")[0], Integer.parseInt(factor.split("=")[1]))
            );
            analysisResults.add(analysisResult);
        }
        return Result.success(analysisResults);
    }
}
