public abstract class CommonAdapter<T> extends BaseAdapter {

    private List<T> dataList;
    protected Context context;
    protected int item_layoutId=0;
    protected HashMap<Integer,Integer> layoutIdMap; //多种itemView时用到。 第一个int对应type类型，第二个int对应 itemlayoutId

    /**
     * 设置单个子模版VIew，
     * @param context
     * @param datas 绑定的对象集合
     * @param item_layoutId 被绑定的视图layoutID
    * */
    public CommonAdapter(Context context, List<T> datas, int item_layoutId)
    {
        this.context=context;
        this.dataList=datas;
        this.item_layoutId=item_layoutId;
    }

    /**
     *设置多个子模版VIew， 并配合重写 getItemViewType(int position)来确定是设置哪个模版
     * @param context
     * @param datas 绑定的对象集合
     * @param layoutIdMap 多种itemViewID Map 第一个int对应type类型，第二个int对应 itemlayoutId
    */
    public CommonAdapter(Context context, List<T> datas, HashMap<Integer,Integer> layoutIdMap)
    {
        this.context=context;
        this.dataList=datas;
        this.layoutIdMap=layoutIdMap;
    }

    @Override
    public int getViewTypeCount() {
        if(this.layoutIdMap==null)
        {
            return 1;
        }
        return this.layoutIdMap.size();
    }


    @Override
    public int getCount() {
        return this.dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        return getConvertView(position,convertView,type);
    }

    private View getConvertView(int position,View convertView,int itemViewType)
    {
        if(convertView==null)
        {
            int layoutId =0;
            if(this.item_layoutId!=0)
            {
                layoutId=this.item_layoutId;
            }
            if (this.layoutIdMap != null) {
                layoutId = this.layoutIdMap.get(itemViewType);
            }
            convertView = LayoutInflater.from(context).inflate(layoutId, null);
        }
        T t = this.dataList.get(position);
        this.setConvertView(convertView,t,itemViewType);
        return convertView;
    }

    /**
     * 局部更新数据，调用一次getView()方法；Google推荐的做法
     *
     * @param parent  要更新的listview
     * @param position 要更新的位置
     */
    public void onOneItemChanged(ListView parent, int position) {
        /**第一个可见的位置**/
        int firstVisiblePosition = parent.getFirstVisiblePosition();
        /**最后一个可见的位置**/
        int lastVisiblePosition = parent.getLastVisiblePosition();

        /**在看见范围内才更新，不可见的滑动后自动会调用getView方法更新**/
        if ((position >= firstVisiblePosition && position <= lastVisiblePosition)) {

            /**获取指定位置view对象**/
            View view = parent.getChildAt(position - firstVisiblePosition);
            getView(position, view, parent);
        }
    }

    /**
     * 需要去实现的对item中的view的设置操作
     *
     * @param convertView 转换后的试图
     * @param t Model对象
     * @param itemViewType 试图类型。对应layoutIdMap中的key
     */
    protected abstract void setConvertView(View convertView, T t,int itemViewType);
}