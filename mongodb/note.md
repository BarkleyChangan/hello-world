* 官网
  官网地址: [MongoDB]("https://www.mongodb.com" "MongoDB官网")
  下载地址: <https://www.mongodb.com/download-center/community>
                    <https://www.mongodb.org/dl/win32>
* 版本说明
  MongoDB的版本偶数版为稳定版,奇数版为开发版。MongoDB对于32位系统支持不好,所以3.2版本以后没有再对32位系统支持
* 安装
  1. 直接双击安装
  2. path中添加: `D:\program\install\64\MongoDB4.2\bin`
  3. 在C盘根目录创建一个**c:\data\db**文件夹
  4. 启动服务: `mongod`
     **NOTE:** 32位系统启动服务时,需要输入如下内容(仅第一次启动时需要添加如下参数):
                  `mongod --storageEngine=mmapv1`
                  通过参数指定数据文件路径:
                  `mongod --dbpath d:\data\db --port 1234`
  5. 客户端连接: `mongo`
  6. 默认端口: 27017
* 将MongoDB设为系统服务
  1. 创建相应目录: `mkdir c:\data\db` `mkdir c:\data\log`
  2. 创建配置文件: `D:\program\install\64\MongoDB4.2\mongod.cfg`
     文件内容如下:
     ```text
     systemLog:
         destination: file
         path: c:\data\log\mongod.log
     storage:
         dbPath: c:\data\db
     ```
  3. 以管理员身份打开CMD:
     ```cmd
     sc create MongoDB binPath="\"D:\program\install\64\MongoDB4.2\bin\mongod.exe\" --service --config=\"D:\program\install\64\MongoDB4.2\mongod.cfg\"" DisplayName="MongoDB" start="auto"
     sc delete MongoDB
     ```
* 数据库操作
  在MongoDB中,数据库和集合都不需要手动创建,当我们创建文档时,如果文档所在集合或数据库不存在会自动创建数据库和集合。
  显示所有数据库: `show databases`
  进入指定数据库: `use testdb`
  显示当前所在数据库: `db`
  显示数据库中所有集合: `show collections`
数据库CRUD操作:
  
  ```sql
  -- 获取ID
  ObjectId()
  - 向数据库中插入文档
  db.<collection>.insert(doc)
  - 查询当前集合中所有文档
  db.<collection>.find()
  -- find()查询:可以接收一个对象做为条件参数
  db.stus.find({age:18,name:"白骨精"})
  db.stus.find({num:{$gt:40,$lt:50}})
  db.stus.find().skip(10).limit(10);
  db.stus.find({$or:[{sal:{$lt:1000}},{sal:{$gt:2000}}])
  db.stus.find({}).sort({sal:1|-1}); --排序
  db.stus.find({},{name:1,age:0}); --投影
  -- 查询符合条件的第一个文档
  db.stus.findOne({age:18,name:"白骨精"})
  -- 获取结果集数量
  db.stus.findOne({}).count()
  db.stus.findOne({}).length()
  -- update(查询条件,新对象)默认情况下使用新对象来替换旧对象
  db.staus.update({age:18},{age:28});
  db.staus.update({age:18},{$set:{age:20,address:"彩虹心愿"}});
  db.staus.update({age:18},{$set:{age:20,address:"彩虹心愿"}},{multi:true});
  $set:   修改字段
  $unset: 删除文档指定属性
  $push:  向数组中添加一个新元素
  $addToSet: 向数组中添加一个新元素(若重复舍弃)
  -- remove、deleteOne、deleteMany删除
  db.staus.remove({})             全部删除
  db.staus.remove({age:22}, true) 只会删除一个
  db.staus.drop()
  db.dropDatabase()
  ```
* Mongoose
  官方网站: <https://mongoosejs.com>
  通过Node操作MongoDB模块。Mongoose是一个对象文档模型(ODM),它对Node原生的MongoDB模块进行了进一步的优化封装并提供了更多的功能。
  1. Schema模式对象
     定义约束数据库中的文档结构
  2. Model
     Model对象作为集合中的所有文档的表示,相当于MongoDB数据库中的集合Collection
  3. Document
     表示集合中的具体文档,相当于集合中的一个具体文档
  * 使用方法
    1. 安装 `npm i mongoose --save`
    2. 连接数据库
       ```javascript
       var mongoose = require("mongoose");
       mongoose.connect("mongodb://127.0.0.1:27017/my_test",{useNewUrlParser: true, useUnifiedTopology: true});
       mongoose.connection.once("open", function(){
           console.log("open");
       });
       mongoose.connection.once("close", function(){
           console.log("close");
       });
       // 断开数据库连接
       mongoose.disconnect();
       ```
  
       
  
    
  
    
  
  
  

