package com.post.util;

import java.net.URLDecoder;

/**
 * @author Barkley.Chang
 * @date 2019-10-08 09:49
 */
public class JarUtil {
    private JarUtil() {
    }

    public static void printJarFile(Object obj, Class<?> clazz){
        try {

        } catch (Throwable e) {
            String loc = "";
            String urlLoc = "";
            try {
                loc = clazz.getProtectionDomain().getCodeSource().getLocation().getFile();
                urlLoc = URLDecoder.decode(loc, "UTF-8");
            } catch (Throwable e2) {
                e.printStackTrace();
            }
        }
    }
}