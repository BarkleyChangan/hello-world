package com.post.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author Barkley.Chang
 * @className:DownloadControlller
 * @description: 解决附件名中文乱码问题
 * @date 2020-08-13 08:53
 */
@Controller
public class DownloadControlller {
    @GetMapping("/downloadFile")
    public String downloadFile(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/octet-stream");

        String clientFileName = "";
        // 对真是文件名进行百分号编码
        String percentEncodedFileName = URLEncoder.encode(clientFileName, "UTF-8").replaceAll("\\+", "%20");

        // 组装contentDisposition的值
        StringBuilder sb = new StringBuilder();
        sb.append("attachment; filename=")
                .append(percentEncodedFileName)
                .append(";")
                .append("filename*=")
                .append("utf-8''")
                .append(percentEncodedFileName);

        response.setHeader("Content-disposition", sb.toString());

        // 将文件流写到response中
//        try (InputStream inputStream = fileService.getInputStream(serverFileName);
//             OutputStream outputStream = response.getOutputStream()) {
//            IOUtils.copy(inputStream, outputStream);
//        }

        return "OK!";
    }
}