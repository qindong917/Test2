package com.qd.cjb.api.config;

import com.qd.cjb.api.controller.BatisController;
import com.qd.cjb.common.utils.JSONResult;
import com.qd.cjb.common.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @version : 1.0
 * @auther : hjx
 * @Date : 2021/3/14
 * @Description : short-video
 */
public class Intercepator extends BatisController implements HandlerInterceptor  {

    private static final Logger log = LoggerFactory.getLogger(Intercepator.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userId = request.getHeader("userId");
        String userToken = request.getHeader("userToken");
        log.info("userId : {}",userId);
        log.info("userToken : {}", userToken);
        log.debug("response : {}",response);
        if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(userToken)) {

            String uniqueToken = redisOperator.get(USER_SESSION + ":" + userId);
            log.info("uniqueToken : {}",uniqueToken);

            if (StringUtils.isEmpty(uniqueToken) && StringUtils.isBlank(uniqueToken)) {
                returnErrorResponse(response, JSONResult.errorTokenMsg("账号已过期，请重新登录"));
                return false;
            } else if (uniqueToken.equals(userToken)){
                returnErrorResponse(response,JSONResult.errorTokenMsg("账号已被占用"));
                return false;
            }
        } else {
            returnErrorResponse(response,JSONResult.errorTokenMsg("请注册账号"));
            return false;
        }
        return true;
    }

    /**
     * 输出错误信息，对错误信息矫正编码
     * @param response
     * @param result
     * @throws IOException
     */
    public void returnErrorResponse(HttpServletResponse response, JSONResult result) throws IOException {
        OutputStream outputStream = null;
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json");
            outputStream = response.getOutputStream();
            outputStream.write(JsonUtils.objectToJson(result).getBytes("utf-8"));
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
