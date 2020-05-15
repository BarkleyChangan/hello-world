1. 标题
   + 使用 = 和 - 标记一级和二级标题
       ```markdown
       我是一级标题
       === 
       ```
       我是一级标题
       ===
       ```markdown
       我是二级标题
       ---
       ```
       我是二级标题
       ---
   + 使用#号标记
       ```markdown
       # 一级标题
       ## 二级标题
       ### 三级标题
       #### 四级标题
       ##### 五级标题
       ###### 六级标题
       ```
2. 段落格式
   * 换行: 使用两个以上的空格加上回车
     RUNOOB.COM  
   * 字体
     ```markdown
     *斜体文本* 
     _斜体文本_
     **粗体文本** 
     ***粗斜体文本*** 
     ___粗斜体文本___
     ```
     *斜体文本* 
     _斜体文本_
     **粗体文本** 
     ***粗斜体文本*** 
     ___粗斜体文本___    
     
   * 分割线
     ```markdown
     ***
     ---
     ___
     * * *
     - - -
     -----
     ```
     ***
     ---
     ___
     * * *
     - - -
     -----
   * 删除线
     ```markdown
     ~~BAIDU.COM~~
     ```
     ~~BAIDU.COM~~  
   * 下划线
     ```markdown
     <u>带下划线文本</u>
     ```
     <u>带下划线文本</u>
   * 脚注
     ```markdown
     创建脚本格式类似这样 [^RUN6NOOB]
     [^RUNOOB]:菜鸟教程 -- 学的不仅是技术,更是梦想 
     ```
     创建脚本格式类似这样 [^RUN6NOOB]
     
     [^RUNOOB]:菜鸟教程 -- 学的不仅是技术,更是梦想  
3. 列表
   - 无序列表
       ```markdown
       * 第一项
       * 第二项
       * 第三项
       ```
       * 第一项
       * 第二项
       * 第三项  
       ```markdown
       + 第一项
       + 第二项
       + 第三项
       ```
       + 第一项
       + 第二项
       + 第三项  
       ```markdown
       - 第一项
       - 第二项
       - 第三项
       ```
       - 第一项
       - 第二项
       - 第三项
   - 有序列表
       ```markdown
       1. 第一项
       2. 第二项
       3. 第三项
       ```
       1. 第一项
       2. 第二项
       3. 第三项
   - 可选列表
       ```markdown
       - [x] GFM task list 1
	   - [x] GFM task list 2
	   - [ ] GFM task list 3
	       - [ ] GFM task list 3-1
           - [ ] GFM task list 3-2
           - [ ] GFM task list 3-3
       - [ ] GFM task list 4
           - [ ] GFM task list 4-1
           - [ ] GFM task list 4-2
      ```
       - [x] GFM task list 1
	   - [x] GFM task list 2
	   - [ ] GFM task list 3
	       - [ ] GFM task list 3-1
           - [ ] GFM task list 3-2
           - [ ] GFM task list 3-3
       - [ ] GFM task list 4
           - [ ] GFM task list 4-1
           - [ ] GFM task list 4-2
   - 注意事项:
       如果在单一列表项中包含了多个段落，为了保证渲染正常，*与段落首字母之间必须保留四个空格。
       记住一个原则，如果在和列表配合使用的时候出现了问题，就缩进一次，四个空格或者一个制表符代表一次缩进。如果一次缩进没有解决问题，那就两次。
       *    段落一  
            小段一
       *    段落二  
            小段二
   
4. 区块
   + 区块引用
       ```markdown
       > 区块引用
       ```
       > 区块引用
   + 区块嵌套
       ```markdown
       > 最外层
       >
       > > 第一层嵌套
       > >
       > > > 第二层嵌套
       ```
       > 最外层
       >
       > > 第一层嵌套
       > >
       > > > 第二层嵌套
   + 区块中使用列表
       ```markdown
       > 区块中使用列表
       >
       > 1. 第一项
       > 2. 第二项
       >
       > + 第二项
       > + 第三项
       ```
       > 区块中使用列表
       >
       > 1. 第一项
       > 2. 第二项
       >
       > + 第二项
       > + 第三项
   + 列表中使用区块
     在列表中加入了区块引用，区域引用标记符也需要缩进4个空格:
     ```markdown
     * 第一项
         > 菜鸟教程
     * 第二项
         > 学的不仅是技术更是梦想
     ```
     * 第一项
       
         > 菜鸟教程
     * 第二项
       
         > 学的不仅是技术更是梦想
5. 代码
   + 行内代码引用，使用单个反引号进行包裹（**`**）
     `printf()`  
   + 代码区块使用 **4 个空格**或者一个制表符
         <?php  
         echo 'RUNOOB';
         function test(){
             echo 'test';
         }
   + 用 **```** 包裹一段代码，并指定一种语言（也可以不指定）
     ```javascript
     jQuery(document).ready(function($){
        console.log("load"); 
     });
     ```
6. 链接
   ```markdown
   [菜鸟教程](https://www.runoob.com)
   <https://www.runoob.com>  
   
   链接也可以用变量来代替，文档末尾附带变量地址:  
   这个链接用 1 作为网址变量 [Google][1]  
   这个链接用 runoob 作为网址变量 [Runoob][runoob]  
   然后在文档的结尾为变量赋值（网址）
   
   [1]: http://www.google.com
   [runoob]: http://www.runoob.com
   ```
   [菜鸟教程](https://www.runoob.com)
   <https://www.runoob.com>  
   
   链接也可以用变量来代替，文档末尾附带变量地址:  
   这个链接用 1 作为网址变量 [Google][1]  
   这个链接用 runoob 作为网址变量 [Runoob][runoob]  
   然后在文档的结尾为变量赋值（网址）
   
   [1]: http://www.google.com
   [runoob]: http://www.runoob.com
7. 图片
   ```markdown
   ![alt 属性文本](图片地址)
   ![RUNOOB 图标](http://static.runoob.com/images/runoob-logo.png)  
   ![RUNOOB 图标](http://static.runoob.com/images/runoob-logo.png "RUNOOB")
   ```
   ![RUNOOB 图标](http://static.runoob.com/images/runoob-logo.png)  
   ![RUNOOB 图标](http://static.runoob.com/images/runoob-logo.png "RUNOOB")
8. 表格
   ```markdown
   | 左对齐 | 居中对齐 | 右对齐 |
   | :---- | :---: | ---: |
   | 单元格 | 单元格 | 单元格 |
   | 单元格 | 单元格 | 单元格 |
   ```
   | 左对齐 | 居中对齐 | 右对齐 |
   | :---- | :---: | ---: |
   | 单元格 | 单元格 | 单元格 |
   | 单元格 | 单元格 | 单元格 |
9. 高级技巧
   + 支持的 HTML 元素
     不在 Markdown 涵盖范围之内的标签，都可以直接在文档里面用 HTML 撰写。目前支持的HTML元素有:kbd,b,i,em,sup,sub,br等,如:  
     使用 <kbd>Ctrl</kbd>+<kbd>Alt</kbd>+<kbd>Del</kbd> 重启电脑  
     上标：X<sub>2</sub>，下标：O<sup>2</sup>
   + 使用特殊占位符，不同占位符所占空白是不一样大的，由小到大的顺序如下：
       ```markdown
       小:&nbsp;或&#160;
       中:&ensp;或&#8194;
       大:&emsp;或&#8195;
       ```
       小:&nbsp;或&#160;
       中:&ensp;或&#8194;
       大:&emsp;或&#8195;
   + 居中显示内容
       ```markdown
       <center>居中的内容</center>
       <center><font size=4>设置居中内容的大小</font></center>
       ```
       <center>居中的内容</center>
       <center><font size=4>设置居中内容的大小</font></center>
   + 换行
       就像使用html一样，使用br标签.
       或者在末尾敲击两个空格，然后回车
   + 更改字体大小、颜色
       ```markdown
       <font face="黑体">我是黑体字</font>
       <font face="微软雅黑">我是微软雅黑</font>
       <font color=red>我是红色</font>
       <font color=#008000>我是绿色</font>
       <font size=5>我是5号大小</font>
       <font size=5>*我是加粗5号大小*</font>
       <font face="黑体" color=green size=5>我是黑体，绿色，尺寸为5</font>
       ```
       <font face="黑体">我是黑体字</font>
       <font face="微软雅黑">我是微软雅黑</font>
       <font color=red>我是红色</font>
       <font color=#008000>我是绿色</font>
       <font size=5>我是5号大小</font>
       <font size=5>*我是加粗5号大小*</font>
       <font face="黑体" color=green size=5>我是黑体，绿色，尺寸为5</font>  
   + 转义
     \*\* 正常显示星号 \*\*  
     Markdown 支持以下这些符号前面加上反斜杠来帮助插入普通的符号:
     ```markdown
     \   反斜线
     `   反引号
     *   星号
     _   下划线
     {}  花括号
     []  方括号
     ()  小括号
     #   井字号
     +   加号
     -   减号
     .   英文句点
     !   感叹号
     ```
   + 公式
     当你需要在编辑器中插入数学公式时，可以使用两个美元符 $$ 包裹 TeX 或 LaTeX 格式的数学公式来实现。提交后，问答和文章页会根据需要加载 Mathjax 对数学公式进行渲染。如:
     $$
     \mathbf{V}_1 \times \mathbf{V}_2 =  \begin{vmatrix} 
        \mathbf{i} & \mathbf{j} & \mathbf{k} \\
        \frac{\partial X}{\partial u} &  \frac{\partial Y}{\partial u} & 0 \\
        \frac{\partial X}{\partial v} &  \frac{\partial Y}{\partial v} & 0 \\
        \end{vmatrix}
     $$