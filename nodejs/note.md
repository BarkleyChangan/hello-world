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
    2. 暴漏模块
        使用exports来向外部暴露变量和方法
* 全局变量
    global
* NPM(Node Package Manager)
    CommonJS包规范是理论,NPM是其中一种实践
    `npm -v` 查看版本
    `npm` 帮助说明
    `npm search 包名` 搜索模块包
    `npm init` 创建package.json 不能含有大写字母
    `npm install 包名` 在当前目录安装包
    `npm install 包名 -g` 全局模式安装包(全局安装的包一般都是一些工具)
    `npm install` 安装当前项目所依赖的包
    `npm install 包名 --save` 安装包并添加到依赖中
    `npm remove 包名` 移除包
* 配置CNPM
    `npm install -g cnpm --registry=https://registry.npm.taobao.org`  
    `npm list --depth=0 -global`
    安装完以后注意配置path环境变量`D:\program\install\64\nodejs\node_global`  
    如果出现如下错误:  
    ```markdown
    cnpm : 无法加载文件 C:\Users\93457\AppData\Roaming\npm\cnpm.ps1，因为在此系统上禁止运行脚本。有关详细信息 。。。 
    ```
    以管理员身份运行Windows PowerShell,并输入如下指令:  
    `set-ExecutionPolicy RemoteSigned`  
    输入: A
​        检查是否设置正确: `get-ExecutionPolicy`



