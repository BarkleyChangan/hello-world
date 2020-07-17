* 官网地址
  [ZooKeeper官网地址](https://zookeeper.apache.org)

* 本地模式安装
  1. 安装JDK
  2. 解压到指定目录
     `tar -zxvf apache-zookeeper-3.4.11.tar.gz`
  3. 新建用户
     ```
     useradd zookeeper
     passwd zookeeper
     ```
  4. 修改配置
     a. 在conf路径下修改zoo_sample.cfg文件名: `mv zoo_sample.cfg zoo.cfg`
     b. 打开zoo.cfg文件修改dataDir路径为: /usr/local/zookeeper-3.4.11/data
     ​     (可选)打开zoo.cfg文件新增dataLogDir路径为: /usr/local/zookeeper-3.4.11/log
     c. 创建data这个目录: `mkdir data`
     ​    创建log这个目录(可选): `mkdir log`
     d. 在data目录下新建myid文件,并设置服务器号(集群安装)
     e. 集群模式配置参数: zoo.cfg文件新增配置项
     ​    参数解读: server.A=B:C:D
     ​    A: myid文件中配置的服务器号
     ​    B: 当前服务器IP
     ​    C: Leader的端口,这个服务器与集群中的Leader服务器交换信息的端口
     ​    D: ZooKeeper服务器之间内部通信端口,如果集群中的Leader服务器挂了,需要一个端口来重新进行选举,选出一个新的Leader,而这个端口就是用来执行选举时服务器相互通信用的端口
     ​    配置示例:
     
         ########## Cluster ##########
         server.2=192.168.100.20:2888:3888
         server.3=192.168.100.21:2888:3888
         server.4=192.168.100.22:2888:3888

* 操作ZooKeeper
  1. 启动服务器 `./zkServer.sh start`

  2. 停止服务器 `./zkServer.sh stop`

  3. 查看服务器状态 `./zkServer.sh status`

  4. 启动客户端 `./zkCli.sh -server ip:port`

  5. 退出客户端 `quit`

  6. 四字命令: `echo ruok | nc 127.0.0.1 2181` 
     安装nc命令: `yum -y install nmap-ncat`
     a. ruok: 测试服务是否处于正确状态,如果确实如此则返回imok,否则不做任何回应
     b. stat: 输出关于性能和连接的客户端列表
     c. conf: 输出相关服务配置的详细信息
     d. cons: 列出所有连接到服务器的客户端的完全连接/会话详细信息,包括接受/发送的包数量、会话ID、操作延迟、最后的操作执行等
     e. dump: 列出未经处理的会话和临时节点
     f. envi: 输出关于服务环境的详细信息(区别于conf命令)
     g. reqs: 列出未经处理的请求
     h. wchs: 理出服务器watch的详细信息
     i. wchc: 通过session列出服务器watch的详细 信息,它的输出是一个与watch相关的会话列表(需加入白名单)
     
     ```
     # 修改启动指令: zkServer.sh
     
     # NOTE:找到如下信息
     else
         echo "JMX disabled by user request" >&2
         ZOOMAIN="org.apache.zookeeper.server.quorum.QuorumPeerMain"
     fi
     
     # 添加如下信息:
     ZOOMAIN="-Dzookeeper.4lw.commands.whitelist=* ${ZOOMAIN}"
     ```
     
     j. wchp: 通过路径列出服务器watch的详细信息.它输出一个与session相关的路径 (需加入白名单)
     k. mntr: 列出集群的健康状态。包括"接收/发送"的包数量、操作延迟、当前服务模式、节点总数、Watch总数、临时节点总数
     l. crst: 重置当前这台服务器所有连接/会话的统计信息
     m. srst: 重置服务器状态

* 配置文件zoo.cfg参数设置
  1. tickTime: 通信心跳数,ZooKeeper服务器与客户端心跳时间,单位:毫秒
     它用于心跳机制,并且设置最小的session超时时间为两倍心跳时间(2*tickTime)
  2. initLimit: Leader和Follower初始通信时限,集群中Follower跟随着服务器与Leader领导者服务器之间初始连接时能容忍的最多心跳数(tickTime的数量),用它来限定集群中ZooKeeper服务器连接到Leader的时限
  3. syncLimit: Leader和Follower同步通信时限,集群中Leader与Follower之间的最大响应时间单位;如果响应时间超过syncLimit*tickTime,Leader认为Follower死掉,从服务器列表中删除Follower
  4. dataDir: 数据文件目录+数据持久化路径,主要用于保存Zookeeper中的数据
  5. dataLogdDir: 指定事务日志的存储路径,可以和dataDir在不同设备,意味着可以使用一个日志的专用磁盘,避免日志IO和快照竞争
  6. clientPort: 客户端连接端口号
  7. maxClientCnxns: 最大的并发连接数限制,设置为0或者不设置该参数,表示不进行连接数的限制
  8. minSessionTimeout: 最小的会话超时时间,默认值 minSession=2*tickTime
  9. maxSessionTimeout: 最大的会话超时时间,默认值 maxSession=20*tickTime

* ZooKeeper内部原理
  1. 选举机制: ZooKeeper虽然在配置文件中没有指定Master和Slave,但是Zookeeper工作时是有一个节点为Leader,其他则为Follower,Leader是通过内部机制选举产生的
  2. 半数机制: 集群中半数以上机器存活则集群可用,所以ZooKeeper适合安装奇数台服务器

* 节点类型
  Znode = Path + nodeValue + Stat
  * 持久型(Persistent): 客户端和服务器断开连接后,创建的节点不删除
    1. 持久化目录节点
    2. 持久化顺序编号目录节点: /znode2_001
  * 短暂型(Ephemeral): 客户端和服务器断开连接后,创建的节点被删除
    1. 临时目录节点
    2. 临时顺序编号目录节点

* 节点属性
  ```
  cZxid: 数据节点创建时的事务ID
  ctime: 数据节点创建时的时间
  mZxid: 数据节点最后一次更新时的事务ID
  mtime: 数据节点最后一次更新时的时间
  pZxid: 数据节点的子节点最后一次被修改时的事务ID
  cversion: 子节点的更改次数
  dataVersion: 节点数据的更改次数
  aclVersion: 节点的ACL更改次数
  ephemeralOwner: 如果节点是临时节点,则表示创建该节点的会话SessionID;如果节点是持久节点,则该属性为0
  dataLength: 数据内容长度
  numChildren: 数据节点当前子节点的个数
  ```

* 客户端命令行操作
  1. 显示所有操作命令: `help`
  2. 新增节点: `create [-s] [-e] path value` -s: 创建有序节点 -e: 创建临时节点
  3. 删除节点: `delete /nodename dataVersion` `rmr /nodename`
  4. 是否存在节点并获取元数据: `exists /nodename`
  5. 获取设置ACL: `getAcl/setAcl`
  6. 获取所有子节点列表: `getChildren`
  7. 更新节点: `set /nodename value dataVersion`
  8. 查看当前znode中所包含的内容: `ls /`
  9. 查看当前节点详细数据(ls2 = ls + stat): `ls2 /`
  10. 监听节点数据变化: `get path [watch]`
  11. 监听子节点增减的变化: `ls\ls2 path [watch]`
  12. 监听节点状态变化: `stat path [watch]`
  13. 客户端的Znode视图与ZooKeeper同步: `sync`

* ZooKeeper的ACL权限控制
  ACL权限控制: `scheme:id:permission`
  a. scheme权限模式:授权的策略

  1. world: 只有一个用户,anyone表示登录zookeeper所有人(默认的模式)
    2. ip: 对客户端使用IP地址认证
    3. auth: 使用已添加认证的用户认证(密码明文)
    4. digest: 使用用户名:密码方式认证(密码密文)

  b. id授权对象: 授权的对象
  c. permission权限: 授予的权限(包括create、delete、read、writer、admin也就是增、删、改、查、管理的权限,简写为:cdrwa);NOTE: 以上5中权限中,delete是对子节点的删除权限,其他4种是对自身节点的操作权限。
  d. 命令

  ```
  读取指定节点的ACL: getAcl [path]
  设置ACL权限: setAcl [path] [acl]
  添加认证用户,和auth,digest授权模式相关: addauth <schema> <auth>
  # 添加一个认证账户
  addauth digest [username]:[password]
  addauth digest chenmou:123456
  
  # 添加权限
  setAcl /node1 ip:192.168.10.1:crdwa,ip:192.168.10.2:crdwa
  setAcl [path] auth:[user]:[acl]
  setAcl /node1 auth:chenmou:crdwa
  
  # 生成digest授权模式的密文
  echo -n itheima:123456 | openssl dgst -binary -sha1 | openssl base64
  setAcl /node4 digest:itheima:qlzQzCLkooLvb+Mlwv4A=:cdrwa
  ```
  f.说明:
  1. 权限控制是基于每个节点的,需要每个节点设置权限
  2. 每个节点支持设置多种权限控制方案和多个权限
  3. 子节点不会继承父节点的权限,客户端无法访问某节点,但可能可以访问它的子节点
     

* ACL配置超级管理员
  1. 生成超级管理员密码的密文
     `echo -n super:admin | openssl dgst -binary -sha1 | openssl base64`
     执行结果: `xQJmxLMiHGwaqBvst5y6rkB6HQs=`
     
  2. 打开izookeeper目录下/bin/zkServer.sh,找到如下一行:
     `nohup JAVA&quot;-Dzookeeper.log.dir=JAVA"-Dzookeeper.log.dir={ZOO_LOG_DIR}" "-Dzookeeper.root.logger=${ZOO_LOG4J_PROP}"`
     在后面添加一行脚本,如下(`xQJmxLMiHGwaqBvst5y6rkB6HQs=`为第1步生成的密文):
     `-Dzookeeper.DigestAuthenticationProvider.superDigest=super:xQJmxLMiHGwaqBvst5y6rkB6HQs=`
     
  3. 此时完整的脚本如下
     
     ```
     nohup "$JAVA" "-Dzookeeper.log.dir=${ZOO_LOG_DIR}" "-Dzookeeper.root.logger=${ZOO_LOG4J_PROP}" "-Dzookeeper.DigestAuthenticationProvider.superDigest=super:xQJmxLMiHGwaqBvst5y6rkB6HQs=" \
         -cp "$CLASSPATH" $JVMFLAGS $ZOOMAIN "$ZOOCFG" > "$_ZOO_DAEMON_OUT" 2>&1 < /dev/null &
     ```
     
  4. 重启zookeeper

  5. 重启后添加超管用户
     `addauth digest super:admin`

* Watcher特性
  1. 一次性: Watcher是一次性的,一旦被触发就会移除,再次使用时需要重新注册
  2. 客户端顺序回调: Watcher回调是顺序串行化执行的,只有回调客户端才能看到最新的数据状态。一个Watcher回调逻辑不应该太多,以免影响别的Watcher执行
  3. 轻量级: WatchEvent是最小的通信单元,结构上只包含通知状态、事件类型和节点路径,并不会告诉数据节点变化前后的具体内容
  4. 时效性: Watcher只有在当前Session彻底失效时才会无效,若在Session有效期内快速重连成功,则Watcher依然存在,仍可接收通知

* Watch捕获相应事件

  |            注册方式             | Created | ChildrenChanged | Changed | Deleted |
  | :-----------------------------: | :-----: | :-------------: | :-----: | :-----: |
  |   zk.exists("/node",watcher)    | 可监控  |                 | 可监控  | 可监控  |
  |   zk.getData("/node",watcher)   |         |                 | 可监控  | 可监控  |
  | zk.getChildren("/node",watcher) |         |     可监控      |         | 可监控  |

* 一致性协议:zab协议
  ZooKeeper Atomic Broadcast(ZooKeeper原子广播),ZooKeeper是通过zab协议来保证分布式事务的最终一致性

* Observer角色及其配置

  Observer角色特点:

  1. 不参与集权的Leader选举
  2. 不参与集群中写数据时的ACK反馈

  具体配置:

  1. 在将变成Observer角色服务器的zoo.cfg配置文件中添加如下配置:
     `peerType=observer`
  2. 并在所有服务器的zoo.cfg配置文件中修改:
     `server.4=192.168.100.22:2888:3888:observer`