* 添加es用户

  由于不能使用root用户启动es，否则报错:Caused by: java.lang.RuntimeException: can not run elasticsearch as root

  ```bash
  sudo adduser es
  sudo passwd es
  111111
  ```

* 下载安装包

  [elasticsearch-7.3.0 版本](https://mirrors.huaweicloud.com/elasticsearch/7.3.0/elasticsearch-7.3.0-linux-x86_64.tar.gz)

  ```bash
  # 下载安装包
  wget https://mirrors.huaweicloud.com/elasticsearch/7.3.0/elasticsearch-7.3.0-linux-x86_64.tar.gz
  wget https://mirrors.huaweicloud.com/elasticsearch/7.3.0/elasticsearch-7.3.0-linux-x86_64.tar.gz.sha512
  # 验证安装包的完整性，如果没问题，会输出 OK
  shasum -a 512 -c elasticsearch-7.3.0-linux-x86_64.tar.gz.sha512
  
  # 解压
  tar -zxvf elasticsearch-7.3.0-linux-x86_64.tar.gz
  
  # 将目录复制三份，作为三个节点，后面配置 ES 集群时，对应了三个 ES 实例
  cp -R elasticsearch-7.3.0 es-7.3.0-node-1
  cp -R elasticsearch-7.3.0 es-7.3.0-node-2
  mv elasticsearch-7.3.0 es-7.3.0-node-3
  
  # 因为以 root 用户启动不了 ES
  chown -R es es-7.3.0*
  ```

* 启动&停止

  ```
  # 启动
  ./bin/elasticsearch
  
  # 后台启动
  # 如果要将ES作为守护程序运行，请在命令行中指定-d，-p参数，将进程ID记录到pid文件
  ./bin/elasticsearch -d -p pid
  
  # 停止
  pkill -F pid
  ```

* 检查运行状态

  ```
  curl -X GET "localhost:9200/?pretty"
  ```

  