* 帮助文档
  <http://www.emeditor.org/zh-cn/index.html>

* BOM
  BOM(Byte Order Mark)，是UTF编码方案里用于标识编码的标准标记，在UTF-16里本来是FF FE，变成UTF-8就成了EF BB BF。这个标记是可选的，因为UTF8字节没有顺序，所以它可以被用来检测一个字节流是否是UTF-8编码的。
  微软在自己的UTF-8格式的文本文件之前加上了EF BB BF三个字节, Windows上面的Notepad等程序就是根据这三个字节来确定一个文本文件是ASCII的还是UTF-8的, 然而这个只是微软暗自作的标记, 其它平台上并没有对UTF-8文本文件做个这样的标记。

* 设置新建文件为无BOM

  > 工具 -> 所有配置的属性 -> 文件 -> 新建文件 -> 取消勾选"添加一个Unicode签名(BOM)"