* 官网
  <http://lesscss.org>
* 编译工具
  [koala](www.koala-app.com "koala")
* 搭建环境
  1. 在浏览器中引入less.js
     ```html
     <!-- link标签一定要在less.js之前引入,并且link标签的rel属性要设置为stylesheet/less -->
     <link rel="stylesheet/less" href="css/less_demo.less">
     <script src="js/less.min.js"></script>
     
     <script>less = { env: 'development'};</script>
     <script>less.watch();</script>
     ```
  2. 使用koala编译工具
  3. 在vscode中安装Easy Less插件
* 注释
    ```less
    // 单行注释,不会被编译到css文件中
    
    /*
     * 多行注释,会被编译到css文件中
     */
    ```

* 变量
  1. 以@作为变量的起始标识，变量名由字母、数字、_和-组成
  2. 没有先定义后使用的规定
  3. 最后定义的值为最终值
  4. 定义时 "@变量名: 变量值;" 的形式；引用时采用 "@变量名" 或 "@{变量名}" 的形式
  5. 存在作用域，局部作用域优先级高于全局作用域
  6. 作为普通属性值只来使用:直接使用@pink;作为选择器和属性名:#@{selector的值}的形式;作为URL:@{url}或background:url("@{url}/1.png");
  7. 列表类型
     通过内置函数extract通过索引获取列表元素，通过内置函数length获取列表的元素个数
     ```less
     @colors:#fff,#0f0,#f0f;
     .skin{
         color:extract(@colors,0);
         height:12px*length(@colors);
     }
     ```
* 嵌套
  Less源码:
  ```less
  .main {
      padding: 10px;
      >div {
          width: 100px;
      }
      .aside {
          width: 200px;
      }
  }
  ```
  输出:
  ```css
  .main {
    padding: 10px;
  }
  .main > div {
    width: 100px;
  }
  .main .aside {
    width: 200px;
  }
  ```
* 父选择器引用
  1. 采用&引用完整的父选择器
  2. 可通过追加和预追加的方式加工&,从而生成新的选择器
  3. 通过&::after等方式添加伪元素、伪类样式规则
  4. 同一个选择器可使用多个&
  5. 通过在选择器后添加“空格&”的方式,可将当前选择器排列到最前面
  6. &指向组选择器时,会生成新的组选择器
* 导入指令(Import)
    1. less样式文件可通过 @import "文件路径";引入外部的less文件
    2. 不带扩展名或带非.less的扩展名均被视为less文件
    3. @import可出现在任何位置,而不像css的@import那样只能放在文件第一行
    4. 另外@import还提供了6个可选配置项(分别为reference,inline,less,css,once,multiple)，用来改变引入文件的特性。语法为：`@import (reference) '文件路径';`
       1. reference: 将引入的文件作为样式库使用，因此文件中样式不会被直接编译为css样式规则。当前样式文件通过extend和mixins的方式引用样式库的内容
       2. inline: 用于引入与less不兼容的css文件，通过inline配置告知编译器不对引入的文件进行编译处理，直接输出到最终输出。注意:**引入的文件和当前文件会被编译为一个样式样式**
       3. less: 默认使用该配置项，表示引入的文件为less文件
       4. css: 表示当前操作为CSS中的`@import`操作。*当前文件会输出一个样式文件，而被引入的文件自身为一个独立的样式文件*
       5. once: 默认使用该配置项,表示对同一个资源仅引入一次
       6. multiple: 表示对同一资源可引入多次
* 继承(Extend)
    1. `<selector>:extend(<parentSelector>){}` 
    2. `<selector>{&:extend(<parentSelector>);}`
       > NOTE: 
       > **all关键字会匹配所有包含parentSelector内容的选择器,并以selector替换parentSelector来生成新的选择器**
       > **父选择器不支持变量形式**
       > **media query内的extend操作,仅能继承当前块的其他选择器样式;非media query内的extend操作,将会继承所有media query中匹配的选择器样式**
* 混合(Mixin)
  1. 类选择器、ID选择器自动被定义为mixin,而且具有命名空间
  2. 显示定义不带参数和带参数的样式库(mixin库)不会输出到最终输出中,仅供调用
     ```less
     .animal(){
         color:#000;
     }
     .dog(@type;@age){
         height:@type*@age*12px;
     }
     .cat(@type;@age:1){
         height:@type*@age*5px;
     }
     .chihuahua{
         .dog(1;2);
     }
     ```
  3. mixin内置两个特殊的对象 @arguments 和 @reset 。@arguments代表mixin的所有入参,而@reset代表mixin的...入参数组
     ```less
     .dog(@type; @age; @rest...) {
         height: @type*@age*12px;
         border: @rest;
     }
     .cat(@solid; @w; @color) {
         border: @arguments;
     }
     .chihuahua {
         .dog(1; 2; solid; 1px; red);
     }
     .mimi {
         .cat(solid; 2px; blue);
     }
     ```
  4. mixin的重载可定义多个同名mixin,调用时只要参数数量匹配则会执行相应的mixin
      ```less
      .dog(@name) {
          &::after {
              content: @name;
          }
      }
      .dog(@name; @age) {
          height: @age*4px;
      }
      .dog(@name; @age; @width: 20px) {
          height: @age*12px;
          width: @width;
      }
      .one-dog {
          .dog('chihuahua');
      }
      .two-three-dog {
          .dog('two-three-dog', 2);
      }
      .cat(mimi, @age) {
          height: @age*22px;
      }
      .cat(mini, @age) {
          height: @age*12px;
      }
      // 不管第一参数值为啥均调用该mixin
      .cat(@any, @age) {
          color: #f3c;
      }
      .mycat {
          .cat(mini, 1);
      }
      ```
  5. 选择、循环作业控制
      Less中通过混合(Mixin)后的when关键字来提供选择的作业控制,通过递归来实现循环的作业控制
* 运算符
  1. 运算数与运算符间必须用空格分隔
  2. 以第一个运算数的单位作为运算结果的单位