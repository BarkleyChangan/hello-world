- 下载路径

    <https://code.visualstudio.com>  

- 安装注意事项
    取消勾选“将Code注册为受支持的文件类型编辑器”,其余全部勾选  
    
- 安装插件
  
    1. Chinese 中文插件
    
    2. Live Server 在页面上右键 -> Open with Live Server
    
    ​    设置自动保存: 文件 -> 首选项 -> 设置 -> Files:Auto Save: afterDelay 1000  
    
    3. Beautify  格式化html、css、js文件
    
    4. Easy LESS  
    
    ​    设置less与css文件映射: 管理 -> 设置 -> 扩展 -> Easy LESS configuration -> 在settings.json中编辑 -> 加入如下配置(会生成xxx.css.map文件):  
    
    ```less
    "less.compile": {
             "compress":  false,  // true => remove surplus whitespace
             "sourceMap": true,  // true => generate source maps (.css.map files)
             "out":       true, // false => DON'T output .css files (overridable per-file, see below)
    }
    ```
    
    5. Visual Studio Intellicode代码提示
    6. Code Runner (运行NodeJS)
    7. Node Snippets
    8. Vetur(Vue)
    9. Vue 2 Snippets(Vue)
    10. Auto Close Tag
    
- 设置

    1. 取消迷你地图: 查看 -> 取消勾选"显示迷你地图"
    2. 取消面包屑: 查看 -> 取消勾选"显示面包屑"
    3. 渲染空格: 查看 -> 勾选"渲染空格" 
    4. 修改提示快捷键 文件 - > 首选项 - > 键盘快捷方式 - > 修改"触发建议"

- 快捷键
    1. ctrl + / 注释
    2. alt + shift + a 块区域注释
    3. ! + tab html基本结构
    4. alt + shift + 上|下 向上复制一行|向下复制一行
    5. alt + shift + f 格式化文档
    6. F2 修改文件名
    7. alt + 向上箭头|向下箭头 当前行向上移动|当前行向下移动
    8. ctrl + shift + k,ctrl+x 删除一行
    9. ctrl + shift + p 显示全局命令面板
    10. ctrl + [ or ] 行缩进
    11. ctrl + shift + p 显示全局命令面板
    12. alt + 鼠标左键 选中多个区域编辑
    13. ctrl + shift+ [ or ] 代码折叠or展开
    14. shift + alt + \ or 2,3,4 拆分编辑框
    15. ctrl + d 单词选中
    16. ctrl + b 打开|关闭侧边栏
    17. ctrl + g 跳转到指定行
    18. ctrl + shift + o 文件中符号跳转
    19. ctrl + t 工作区符号跳转
    20. ctrl + backspace 删除上一个词
    21. ctrl + shift + d 复制行
    22. ctrl + alt + up|down 编辑多处光标
    23. shift + alt + 鼠标 块选中
    24. ctrl + p 命令面板:打开文件、查看快捷键

