[TOC]

### 官网 ###

  中文: <https://cn.vuejs.org>    英文: <http://vuejs.org>

  API: <https://cn.vuejs.org/v2/api/#>

  ​		<https://cn.vuejs.org/v2/guide/class-and-style.html>

### 调试工具vue-devtools ###

  Chrome: <https://chrome.google.com/webstore/detail/vuejs-devtools/nhdogjmejiglipccpnnnanhbledajbpd>

  Firefox: <https://addons.mozilla.org/en-US/firefox/addon/vue-js-devtools/>

### 安装vue-cli ###

```
npm install -g vue-cli
# 进入到项目目录后执行如下语句下载模板
vue init webpack vue_demo
cd vue_demo
npm install
npm run dev

# 打包
npm run build

# 发布1:使用静态服务器工具包
npm install -g serve
serve dist
# 访问
http://localhost:5000

# 发布2:使用动态Web服务器(Tomcat)
修改配置:webpack.prod.conf.js文件
output:{
	publicPath:'/xxx/' // 打包文件夹的名称
}
# 重新打包
npm run build
# 修改dist文件夹为项目名称,将xxx拷贝到运行的tomcat的webapps目录下,访问:http://localhost:8080/xxx
```

