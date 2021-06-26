[TOC]

### 官网 ###

  中文: <https://cn.vuejs.org>    英文: <http://vuejs.org>

  API: <https://cn.vuejs.org/v2/api/#>

  ​		<https://cn.vuejs.org/v2/guide/class-and-style.html>

### 调试工具vue-devtools ###

  Chrome: <https://chrome.google.com/webstore/detail/vuejs-devtools/nhdogjmejiglipccpnnnanhbledajbpd>

  Firefox: <https://addons.mozilla.org/en-US/firefox/addon/vue-js-devtools/>

### 引入 ###

```
开发环境:https://vuejs.org/js/vue.js
生产环境:https://vuejs.org/js/vue.min.js
```

### VSCode中引入Vue插件 ###

```
Vetur
Vue
Vue 3 Snippets
```

### 安装Vue-CLI ###

```
版本2:npm --registry https://registry.npm.taobao.org install -g vue-cli
创建项目:vue init webpack vuecli2test
选择手工创建后:npm install --registry https://registry.npm.taobao.org

版本3:npm --registry https://registry.npm.taobao.org install -g @vue/cli
查看版本:vue -V
拉取版本2的模板:npm --registry https://registry.npm.taobao.org install @vue/cli-init -g
创建项目:vue create my-project
选择手工创建后:npm install --registry https://registry.npm.taobao.org

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
### 安装和使用vue-router  ###
安装: `npm install vue-router --save`

```
# router/index.js #
// 配置路由相关的信息
import Vue from 'vue'
import VueRouter from 'vue-router'

import Home from '../components/Home'
import About from '../components/About'

// 1.通过Vue.use(插件),安装插件
Vue.use(VueRouter)

// 2.创建VueRouter对象
const routes = [
  {
    path: '/home',
    component: Home
  },
  {
    path: '/about',
    component: About
  }
];
const router = new VueRouter({
  // 配置路径和组件之间的映射关系
  routes
});

// 3.将router对象传入到Vue实例
export default router


# main.js #
import Vue from 'vue'
import App from './App'
import router from './router'

Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  render: h => h(App)
})


# App.vue #
<template>
  <div id="app">
    <img src="./assets/logo.png">
    <router-link to="/home">首页</router-link>
    <router-link to="/about">关于</router-link>
    <router-view></router-view>
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>
```







