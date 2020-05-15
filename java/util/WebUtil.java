package com.post.util;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Map;

/**
 * @author Barkley.Chang
 * @className:WebUtil
 * @description: TODO
 * @date 2020-04-04 23:49
 */
public final class WebUtil {
    private WebUtil() {
    }

    public static <T> T copyParamToBean(Map<String, String> map, T bean) {

        try {
            BeanUtils.populate(bean, map);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bean;
    }

    public static String getIpAddr(MockHttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");

        if (ip == null || ip.trim().length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (ip == null || ip.trim().length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.trim().length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }