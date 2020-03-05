* 官网
  <http://zeptojs.com>
* 事件委托(坑)
  委托的事件先被依次放入数组队列里，然后由自身开始往后找直到找到最后，期间符合条件的元素委托的事件都会执行。
  1.  委托在同一个父元素,或者触发的元素的事件范围小于同类型事件(冒泡能冒到自身范围)
  2.  同一个事件
  3.  委托关联 操作的类要进行关联
  4.  绑定顺序---从当前的位置往后看
* touch方法
  1. tap: 点击事件,利用在document上绑定touch事件来模拟tap事件,并且tap事件会冒泡到document上
  2. singleTap: 单击事件
  3. doubleTap: 双击事件
  4. longTag: 当一个元素持续按住750ms触发
  5. swipe、swipeLeft、swipeRight、swipeUp、swipeDown: 当元素被划过(同一个方向大于30px)时触发