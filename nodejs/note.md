* 环境配置
    1.  下载
        <http://nodejs.cn/#download>
   2. NodeJS安装成功后会自带NPM工具,NPM作为了node_modules的一个模块
   3. 配置NodeJS的环境变量,直接添加到path中
        `D:\program\install\64\nodejs\`
   4. 验证安装是否成功
        `node -v`
        `npm -v`
   
* 模块化
    exports, require, module, __filename, __dirname
    1. 引入模块
        通过require()函数来引入外部模块,可以传递一个文件的路径作为参数。相对路径必须以.或..开头。
        使用require()引入模块后,该函数会返回一个对象,这个对象代表的是引入的模块。
        使用require()引入第三方库时要放在自定义模块的上面
    2. 暴露模块
        使用module.exports来向外部暴露变量和方法
    3. 包名全部为小写
    
* 全局变量
    global
    
* NPM(Node Package Manager)
    CommonJS包规范是理论,NPM是其中一种实践
    `npm -v` 查看版本
    `npm` 帮助说明
    `npm search 包名` 搜索模块包
    `npm init` 创建package.json 不能含有大写字母
    `npm config get prefix` 获取npm所在目录的路径

    1. 全局安装
       * `npm install -g 模块名`
       * 安装命令行工具，例如 grunt CLI的时候，使用全局安装
    2. 本地安装
       * `npm install 模块名`:安装好后不写入package.json中
       * `npm install 模块名 --save`:安装好后写入package.json的dependencies中(生产环境依赖)
       * `npm install 模块名 --save-dev`:安装好后写入package.json的devDepencies中(开发环境依赖)  
    3.  删除全局模块 `npm uninstall -g 模块名`
    4. 删除本地模块
   * `npm uninstall 模块名`:删除模块,但不删除模块留在package.json中的对应信息
       * `npm uninstall 模块名 --save`:删除模块,同时删除模块留在package.json中dependencies下的对应信息
       * `npm uninstall 模块名 --save-dev`:删除模块,同时删除模块留在package.json中devDependencies下的对应信息
   
* 配置CNPM
    `npm install -g cnpm --registry=https://registry.npm.taobao.org` 
    
    检查安装结果: `node -v`
    
​						  `npm -v`
    
    ​						  `cnpm -v`
    
    `npm list --depth=0 -global`
    安装完以后注意配置path环境变量`D:\program\install\64\nodejs\node_global` 
    如果出现如下错误:  
    
```markdown
    cnpm:无法加载文件 C:\Users\93457\AppData\Roaming\npm\cnpm.ps1，因为在此系统上禁止运行脚本。有关详细信息 。。。 
    ```
    以管理员身份运行Windows PowerShell,并输入如下指令:  
    `set-ExecutionPolicy RemoteSigned`  
    输入: A
         检查是否设置正确: `get-ExecutionPolicy`
    
    使用cnpm命令安装模块:`cnpm install 模块名`
    
* Browserify
    下载安装: 1. 全局: npm install browserify -g
    ​                  2. 局部: npm install browserify --save-dev



