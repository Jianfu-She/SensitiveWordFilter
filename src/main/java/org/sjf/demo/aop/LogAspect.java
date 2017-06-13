package org.sjf.demo.aop;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 错误日志切面
 * Created by SJF on 2017/3/11.
 */
@Aspect
@Component
public class LogAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogAspect.class);

    @Pointcut("execution(* org.sjf.demo.controller..*.*(..))")
    private void logPointcut() {}

    @AfterThrowing(pointcut = "logPointcut()", throwing = "ex")
    public void exceptionLog(JoinPoint point, Exception ex) {

        //详细错误信息
        String errorMsg = "";
        StackTraceElement[] trace = ex.getStackTrace();
        for (StackTraceElement s : trace) {
            errorMsg += "\tat " + s + "\r\n";
        }

        //写入异常日志
        writeLog(errorMsg, point, ex);
    }

    private void writeLog(String detailErrMsg, JoinPoint joinPoint, Exception ex) {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.
                getRequestAttributes()).getRequest();
        // 获取请求的URL
        StringBuffer requestURL = request.getRequestURL();
        // 获取参数信息
        String queryString = request.getQueryString();
        // 封装完整请求URL带参数
        if(queryString != null){
            requestURL.append("?").append(queryString);
        }

        String cla = joinPoint.getTarget().getClass().getName();//action
        String method = joinPoint.getSignature().getName();//method
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat sdf_simple = new SimpleDateFormat("yyyy-MM-dd");
            // 创建输出异常log日志
            File dir = new File("");
            File file = new File(dir.getAbsolutePath() + "/" + sdf_simple.format(new Date()) + "-" + "exception.log");
            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream out = new FileOutputStream(file,true); //如果追加方式用true
            //日志具体参数
            StringBuffer sb = new StringBuffer();
            sb.append("-----------" + sdf.format(new Date()) + "------------\r\n");
            sb.append("请求URL：[" + requestURL + "]\r\n");
            sb.append("对应API：[" + cla + "." + method + "]\r\n");
            sb.append("错误MSG：" + ex + "\r\n");
            sb.append(detailErrMsg + "\r\n");
            out.write(sb.toString().getBytes("utf-8"));//注意需要转换对应的字符集
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
