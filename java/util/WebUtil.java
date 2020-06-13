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
}