package com.demo.app.resolver;

import com.demo.app.entity.common.Result;
import org.apache.tomcat.util.http.fileupload.FileUploadBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartException;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by xiongcaesar on 2018/4/29.
 * 全局异常解析
 */
@ControllerAdvice
public class GlobalExceptionResolver {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionResolver.class);

    /**
     * 上传文件大小超出限制.
     */
    @ExceptionHandler({MultipartException.class, IllegalStateException.class,
            FileUploadBase.SizeLimitExceededException.class})
    @ResponseBody
    public Result fileLimitExceptionHandler(Exception exe) {
        logger.error("上传文件大小超出限制：", exe);
        return Result.fail("上传文件大小超出限制");
    }

    /**
     * 统一异常.
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result exceptionHandler(HttpServletRequest request, Exception exe) {
        logger.error("全局异常捕获：", exe);
        return Result.fail("系统出小差了，请稍后再试");
    }
}
