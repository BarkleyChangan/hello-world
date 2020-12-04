using System;
using System.Collections.Generic;
using System.Text;
using System.Net;
using System.IO;

namespace WindowsFormsApp1
{
    class IgnoreHttpSSLVerify
    {
        static void Main(string[] args) {
            string data = apiPost("http://www.baidu.com/", "");
            Console.WriteLine(data);

            Console.ReadLine();
        }

        static string apiPost(string url, string body)
        {
            string content = string.Empty;
            try
            {
                /* 跳过ssl验证 */
                ServicePointManager.ServerCertificateValidationCallback = (sender, certificate, chain, sslPolicyErrors) => true;
                HttpWebRequest request = HttpWebRequest.Create(preUrl + url) as HttpWebRequest;
                request.Method = "POST";
                request.ContentType = "application/x-www-form-urlencoded";
                byte[] data = Encoding.UTF8.GetBytes(body);
                Stream newStream = request.GetRequestStream();
                newStream.Write(data, 0, data.Length);
                newStream.Close();
                HttpWebResponse response = (HttpWebResponse)request.GetResponse();
                Stream stream = response.GetResponseStream();
                StreamReader reader = new StreamReader(stream, Encoding.UTF8);
                content = reader.ReadToEnd();
                reader.Close();
                response.Close();
            }
            catch (Exception ex)
            {
                content = ex.ToString();
            }
            return content;
        }
    }
}
