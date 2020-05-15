* 两种过滤器

  1. 抓包过滤器:
     必须在开始捕捉前设置完毕,只抓取你设置的规则,同时丢弃其他信息

     ```
     常见的抓包过滤器BPF:
     来源或目的地是指定地址的包
     host 192.168.0.123
     host www.taobao.com
     
     
     范围内的包
     net 192.168.0.0/24
     or
     net 192.168.0.0 mask 255.255.255.0
     
     
     抓取目的地是某范围的包
     dst net 192.168.0.0/24
     or
     dst net 192.168.0.0 mask 255.255.255.0
     
     
     抓取来源是某范围的包
     src net 192.168.0.0/24
     or
     src net 192.168.0.0 mask 255.255.255.0
     
     
     仅抓取DNS(端口是53)的包
     port 53
     ```

  2. 显示过滤器
     并不会丢弃信息,只是将不符合规则的数据隐藏起来而已

     ```
     常见的显示过滤器BPF:
     只显示本地发出去的包
     ip.src==192.168.8.60
     
     
     过滤从某地址发出的请求
     ip.src==192.168.8.60
     
     
     过滤发送到某地址的请求
     ip.dst==192.168.8.60
     
     
     过滤http协议
     http
     
     
     过滤某地址
     http.request.uri=="/projectname/a.html"
     
     
     过滤全地址（它与uri的区别是，包含host）
     http.request.full_uri=="www.mydomain.com/projectname/a.html"
     ```

     