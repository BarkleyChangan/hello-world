package com.post.ssl;

import javax.net.ssl.*;
import java.io.*;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @author Barkley.Chang
 * @className:Janbar
 * @description: 忽略http的SSL证书认证
 * @date 2020-11-17 08:31
 */
public class IgnoreHttpSSLVerify {
    public static void main(String[] args) {
        // 只在开头之执行一次即可
        trustAllHosts();

        String data = httpPost("https://www.baidu.com", "");
        System.out.println(data);
    }

    static String httpPost(String httpUrl, String param) {
        HttpsURLConnection connection = null;
        InputStream is = null;
        OutputStream os = null;
        BufferedReader br = null;
        String result = "";

        try {
            // 通过远程url链接对象打开连接
            connection = (HttpsURLConnection) new URL(httpUrl).openConnection();
            // 忽略证书认证
            connection.setHostnameVerifier(DO_NOT_VERIFY);
            // 设置连接请求方式
            connection.setRequestMethod("POST");
            // 设置连接主机服务器超时时间:15000毫秒
            connection.setConnectTimeout(15000);
            // 设置读取主机服务器返回数据超时时间:60000毫秒
            connection.setReadTimeout(60000);

            // 默认为:false,当向远程服务器发送数据/写数据时,需要设置为true
            connection.setDoOutput(true);
            // 默认值为:true,当向远程服务器读取数据时,需要设置为true,该参数可有可无
            connection.setDoInput(true);
            // 设置传入参数的格式:请求参数应该是name1=value1&name2=value2的形式
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            // 通过连接对象获取一个输出流
            os = connection.getOutputStream();
            // 通过输出流对象将参数写出去/传输出去，他是通过字节数组写出的
            os.write(param.getBytes());
            // 通过连接对象获取一个输入流，向远程读取
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                // 对输入流对象进行包装:charset根据工作项目组要求来设置
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

                StringBuilder sb = new StringBuilder();
                String temp = null;
                // 循环遍历一行一行读取数据
                while ((temp = br.readLine()) != null) {
                    sb.append(temp).append(System.lineSeparator());
                }

                result = sb.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // 断开与远程地址url的连接
            connection.disconnect();
        }

        return result;
    }

    static void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };
        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };
}