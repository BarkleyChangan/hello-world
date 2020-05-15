package com.post.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.retry.RetryOneTime;
import org.apache.curator.retry.RetryUntilElapsed;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

/**
 * @author Barkley.Chang
 * @className:CuratorFrameworkTest
 * @description: ZooKeeper第三方客户端Curator的使用
 * @date 2020-05-14 10:05
 */
public class CuratorFrameworkDemo {
    final static String CONNECTION_STRING = "192.168.100.20:2181,192.168.100.21:2181,192.168.100.22:2181";
    final static int SESSION_TIMEOUT = 5 * 1000;

    public static void main(String[] args) {
        /* Maven引入
        <!-- ZooKeeper相关 -->
        <!-- https://mvnrepository.com/artifact/org.apache.zookeeper/zookeeper -->
        <dependency>
            <groupId>org.apache.zookeeper</groupId>
            <artifactId>zookeeper</artifactId>
            <version>3.4.10</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/org.apache.curator/curator-framework -->
        <dependency>
            <groupId>org.apache.curator</groupId>
            <artifactId>curator-framework</artifactId>
            <version>4.3.0</version>
            <exclusions>
                <exclusion>
                    <groupId>org.apache.zookeeper</groupId>
                    <artifactId>zookeeper</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <!-- https://mvnrepository.com/artifact/org.apache.curator/curator-recipes -->
        <dependency>
            <groupId>org.apache.curator</groupId>
            <artifactId>curator-recipes</artifactId>
            <version>4.3.0</version>
            <exclusions>
                <exclusion>
                    <groupId>org.apache.zookeeper</groupId>
                    <artifactId>zookeeper</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
         */
        CuratorFramework client = null;

        try {
//            client = getConnection("create");
//            client = getConnection("set");
//            client = getConnection("delete");
//            client = getConnection("get");
            client = getConnection("");
            client.start();
//            create(client);
//            set(client);
//            delete(client);
//            get(client);
//            getChildren(client);
//            exists(client);
//            watch(client);
//            transaction(client);
            lock(client);
            System.out.println("finish...");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static CuratorFramework getConnection(String namespace) {
        Objects.requireNonNull(namespace);
        /* 四种重连策略 */
        // 3秒后重连1次,只重连1次
        RetryPolicy retryPolicy1 = new RetryOneTime(3000);
        // 每3秒重连1次,重连3次
        RetryPolicy retryPolicy2 = new RetryNTimes(3, 300);
        // 每3秒重连1次,总等待时间超过10秒后停止重连
        RetryPolicy retryPolicy3 = new RetryUntilElapsed(10000, 3000);
        // 重连3次,间隔时间=baseSleepTimeMs*Math.max(1, random.nextInt(1<<(retryCount+1)))
        RetryPolicy retryPolicy4 = new ExponentialBackoffRetry(1000, 3);

        // 创建链接对象
        CuratorFramework client = CuratorFrameworkFactory
                .builder()
                // IP地址和端口号
                .connectString(CONNECTION_STRING)
                // 会话超时时间
                .sessionTimeoutMs(SESSION_TIMEOUT)
                // 重连机制
                .retryPolicy(retryPolicy1)
                // 命名空间(可选)
                .namespace(namespace)
                // 构建连接对象
                .build();

//        // 打开链连接
//        client.start();
//        System.out.println(client.isStarted());
//        // 关闭连接
//        client.close();
        return client;
    }

    static void lock(CuratorFramework client) throws Exception {
        Objects.requireNonNull(client);
//        分布式锁
//                * 1.InterProcessMutex: 分布式可重入排它锁
//                * 2.InterProcessReadWriteLock: 分布式读写锁

//        // 排它锁: args1:连接对象;args2:节点路径
//        InterProcessLock interProcessLock = new InterProcessMutex(client, "/lock1");
//        System.out.println("等待获取锁对象...");
//        // 获取锁
//        interProcessLock.acquire();
//        for (int i = 0; i <= 10; i++) {
//            Thread.sleep(3000);
//            System.out.println(i);
//        }
//        // 释放锁
//        interProcessLock.release();
//        System.out.println("等待释放锁...");

        // 读写锁
        InterProcessReadWriteLock interProcessReadWriteLock = new InterProcessReadWriteLock(client, "/lock1");
        // 获取读锁对象
        InterProcessLock interProcessLock = interProcessReadWriteLock.readLock();
//        InterProcessLock interProcessLock = interProcessReadWriteLock.writeLock();
        System.out.println("等待锁对象");
        // 获取锁
        interProcessLock.acquire();
        for (int i = 0; i <= 10; i++) {
            Thread.sleep(3000);
            System.out.println(i);
        }
        // 释放锁
        interProcessLock.release();
        System.out.println("等待释放锁...");
    }

    static void transaction(CuratorFramework client) throws Exception {
        Objects.requireNonNull(client);

        client
                // 开启事务
                .inTransaction()
                .create()
                .forPath("/node1", "node1".getBytes())
                .and()
                .setData()
                .forPath("/node2", "node22".getBytes())
                .and()
                // 提交事务
                .commit();

    }

    static void watch(CuratorFramework client) throws Exception {
        Objects.requireNonNull(client);

//        // NodeCache: 只是监听某一个特定的节点,监听节点的新增和修改
//        // PathChildrenCache: 监控一个ZNode的子节点,当一个子节点增加、更新、删除时,Path Cache会改变它的状态
//        //                    包含最新的子节点,子节点的数据和状态
//        CountDownLatch countDownLatch = new CountDownLatch(1);
//        NodeCache nodeCache = new NodeCache(client, "/watcher1");
//        nodeCache.start();
//        nodeCache.getListenable().addListener(new NodeCacheListener() {
//            // 节点变化时回调的方法
//            @Override
//            public void nodeChanged() throws Exception {
//                System.out.println(nodeCache.getCurrentData().getPath());
//                System.out.println(new String(nodeCache.getCurrentData().getData()));
//                //countDownLatch.countDown();
//            }
//        });
//        System.out.println("await");
//        // countDownLatch.await();
//        Thread.sleep(Long.MAX_VALUE);
//        nodeCache.close();

        // 监视子节点的变化
        PathChildrenCache pathChildrenCache = new PathChildrenCache(client, "/watcher1", true);
        // 启动监听
        pathChildrenCache.start();
        pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            // 当子节点发生变化时回调的方法
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                // 节点事件类型
                System.out.println(pathChildrenCacheEvent.getType());
                System.out.println(pathChildrenCacheEvent.getData().getPath());
                System.out.println(new String(pathChildrenCacheEvent.getData().getData()));
            }
        });
        Thread.sleep(Long.MAX_VALUE);
        // 关闭监听
        pathChildrenCache.close();
    }

    static void exists(CuratorFramework client) throws Exception {
        Objects.requireNonNull(client);

        // 判断节点是否存在
//        Stat stat = client
//                .checkExists()
//                .forPath("/node2");
//        System.out.println(stat);

        // 异步方式判断节点是否存在
        CountDownLatch countDownLatch = new CountDownLatch(1);
        client
                .checkExists()
                .inBackground(new BackgroundCallback() {
                    @Override
                    public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
                        System.out.println(curatorEvent.getType());
                        System.out.println(curatorEvent.getPath());
                        System.out.println(curatorEvent.getStat());
                    }
                })
                .forPath("/node2");
    }

    static void getChildren(CuratorFramework client) throws Exception {
        Objects.requireNonNull(client);

//        // 读取子节点
//        List<String> list = client
//                .getChildren()
//                .forPath("/get");
//        for (String s : list) {
//            System.out.println(s);
//        }

        // 异步方式读取子节点数据
        CountDownLatch countDownLatch = new CountDownLatch(1);
        client
                .getChildren()
                .inBackground(new BackgroundCallback() {
                    @Override
                    public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
                        // 节点路径
                        System.out.println(curatorEvent.getType());
                        // 事件类型
                        System.out.println(curatorEvent.getPath());
                        // 读取子节点数据
                        List<String> children = curatorEvent.getChildren();
                        for (String child : children) {
                            System.out.println(child);
                        }
                        countDownLatch.countDown();
                    }
                })
                .forPath("/get");
        countDownLatch.await();
    }

    static void get(CuratorFramework client) throws Exception {
        Objects.requireNonNull(client);

//        // 读取节点数据
//        byte[] bytes = client
//                .getData()
//                .forPath("/node1");
//        System.out.println(new String(bytes));

//        // 读取节点属性
//        Stat stat = new Stat();
//        byte[] bytes = client
//                .getData()
//                // 读取属性
//                .storingStatIn(stat)
//                .forPath("/node1");
//        System.out.println(new String(bytes));
//        System.out.println(stat.getVersion());

        // 异步方式读取节点数据
        CountDownLatch countDownLatch = new CountDownLatch(1);
        client
                .getData()
                .inBackground(new BackgroundCallback() {
                    @Override
                    public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
                        // 节点路径
                        System.out.println(curatorEvent.getPath());
                        // 事件类型
                        System.out.println(curatorEvent.getType());
                        // 节点数据
                        System.out.println(new String(curatorEvent.getData()));
                        countDownLatch.countDown();
                    }
                })
                .forPath("/node1");
        countDownLatch.await();
    }

    static void delete(CuratorFramework client) throws Exception {
        Objects.requireNonNull(client);

//        // 删除节点
//        client
//                .delete()
//                .forPath("/node1");

//        // 根据版本号删除节点
//        client
//                .delete()
//                .withVersion(0)
//                .forPath("/node1");

//        // 删除包含子节点的节点
//        client
//                .delete()
//                .deletingChildrenIfNeeded()
//                .withVersion(-1)
//                .forPath("/node1");

        // 异步方式删除节点
        CountDownLatch countDownLatch = new CountDownLatch(1);
        client
                .delete()
                .deletingChildrenIfNeeded()
                .withVersion(-1)
                .inBackground(new BackgroundCallback() {
                    @Override
                    public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
                        System.out.println(curatorEvent.getType());
                        System.out.println(curatorEvent.getPath());
                        countDownLatch.countDown();
                    }
                })
                .forPath("/node1");
        countDownLatch.await();
    }

    static void set(CuratorFramework client) throws Exception {
//        // 更新节点
//        client
//                .setData()
//                // arg1:节点路径;arg2:节点数据
//                .forPath("/node1", "node11".getBytes());

//        // 更加版本号更新节点
//        client
//                .setData()
//                // 指定版本号
//                .withVersion(2)
//                .forPath("/node1", "node111".getBytes());

        // 异步方式修改节点数据
        CountDownLatch countDownLatch = new CountDownLatch(1);
        client
                .setData()
                .withVersion(-1)
                .inBackground(new BackgroundCallback() {
                    @Override
                    public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
                        // 节点路径
                        System.out.println(curatorEvent.getType());
                        // 事件类型
                        System.out.println(curatorEvent.getPath());
                        countDownLatch.countDown();
                    }
                })
                .forPath("/node1", "node1".getBytes());
        countDownLatch.await();
    }

    static void create(CuratorFramework client) throws Exception {
        Objects.requireNonNull(client);

//        // 新增节点
//        client
//                .create()
//                // 节点类型
//                .withMode(CreateMode.PERSISTENT)
//                // 节点权限列表world:anyone:cdrwa
//                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
//                // args1:节点路径;args2:节点数据
//                .forPath("/node1", "node1".getBytes());

//        // 自定义权限列表
//        List<ACL> list = new ArrayList<>();
//        // 授权模式和授权对象
//        Id id = new Id("ip", "192.168.100.20");
//        list.add(new ACL(ZooDefs.Perms.ALL, id));
//        client
//                .create()
//                .withMode(CreateMode.PERSISTENT)
//                .withACL(list)
//                .forPath("/node2", "node2".getBytes());

//        // 递归创建节点树
//        client
//                .create()
//                // 递归节点创建
//                .creatingParentsIfNeeded()
//                .withMode(CreateMode.PERSISTENT)
//                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
//                .forPath("/node3/node31", "node31".getBytes());

        // 异步方式创建节点
        CountDownLatch countDownLatch = new CountDownLatch(1);
        client
                .create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                .inBackground(new BackgroundCallback() {
                    @Override
                    public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
                        // 事件类型
                        System.out.println(curatorEvent.getType());
                        // 节点路径
                        System.out.println(curatorEvent.getPath());
                        countDownLatch.countDown();
                    }
                })
                .forPath("/node4", "node4".getBytes());
        countDownLatch.await();
    }
}