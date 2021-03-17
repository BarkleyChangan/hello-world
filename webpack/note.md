[TOC]

### webpack安装

1. 安装webpack首先需要安装Node.js,Node.js自带了软件包管理工具npm

2. 查看node版本: `node -v`

3. 全局安装webpack指定版本:`npm install webpack@3.6.0 -g`

   3.1安装报错rollbackFailedOptional:verb npm-session解决办法

   ```
   1.第一种方式
   npm config set registry http://registry.npm.taobao.org
   npm install webpack@3.6.0 -g
   2.第二种方式
   npm --registry https://registry.npm.taobao.org install -g + 包名
   3.第三种方式
   npm install -g cnpm --registry=https://registry.npm.taobao.org
   cnpm install -g 包名
   ```

4. 查看webpack安装版本:`webpack --version`

### webpack起步

 1. 新建webpack.config.js

    ```
    const path = require('path');
    
    module.exports = {
      entry: './src/main.js',
      output: {
        path: path.resolve(__dirname, 'dist'),
        filename: 'bundle.js'
      }
    }
    ```

	2. 需要Node.js环境: `npm init `执行完此命令后会创建package.json文件

	3. 如果package.json文件中有依赖需要执行:`npm intall`

	4. 项目级别安装webpack:`npm --registry https://registry.npm.taobao.org install webpack@3.6.0 --save-dev`

### ES6转ES5

 1. 安装Babel

    ```
    npm --registry https://registry.npm.taobao.org install babel-loader@7 babel-core babel-preset-es2015 --save-dev
    ```

	2. 在webpack.config.js文件中添加配置

    ```
    module.exports = {
      module: {
        rules: [ 
          {
            test: /\.js$/,
            exclude: /node_modules|bower_components/,
            use: {
              loader: 'babel-loader',
              options: {
                presets: ['es2015']
              }
            }
          }
        ]
      }
    }
    ```

	3. `npm run build`

### 引入Vue

 1. 安装 `npm --registry https://registry.npm.taobao.org install vue --save`

 2. 模块化引入

    ```
    import Vue from 'vue'
    const app = new Vue({
      el:"#app",
      data:{
        message:'Hello Webpack'
      }
    });
    ```

	3. Vue编译两种类型

    a. runtime-only:代码中不可以有任何template

    b. runtime-compile:代码中可以有template,因为有compiler可以用于编译template

    解决方法:在webpack.config.js文件中添加配置

    ```
    module.exports = {resolve: {
        alias: {
          'vue$':'vue/dist/vue.esm.js'
        }
      }
    }
    ```

	4. 引入vue-loader、vue-template-compiler

    * 安装 `npm --registry https://registry.npm.taobao.org install vue-loader@13.4.2 vue-template-compiler@2.5.21 --save-dev`

    * ```
      module.exports = {
        module: {
          rules: [
            {
              test:/\.vue$/,
              use: ['vue-loader']
            }
          ]
        }
      }
      ```

    * `npm run build`

### JS Minify

1. 安装:`npm --registry https://registry.npm.taobao.org install uglifyjs-webpack-plugin@1.1.1 --save-dev`

2. 配置webpack.config.js文件

   ```
   const UglifyjsWebpackPlugin = require('uglifyjs-webpack-plugin');
   
   module.exports = {
     plugins:[
       new UglifyjsWebpackPlugin()
     ]
   }
   ```

3. `npm run build`

### 搭建本地服务器

1. 安装:`npm --registry https://registry.npm.taobao.org install webpack-dev-server@2.9.3 --save-dev`

2. 配置webpack.config.js文件

   ```
   module.exports = {
     devServer: {
       contentBase: './dist',
       inline: true
     }
   }
   ```

3. `.\node_modules\.bin\webpack-dev-server --open`

4. `npm run build`