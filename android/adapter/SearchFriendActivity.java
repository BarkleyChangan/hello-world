public class SearchFriendActivity extends Activity implements TextView.OnEditorActionListener{
    private DrawableEditText input;
    private ListView listView;
    private TextView search_resultStr;
    private List<OrayUser> userList=new ArrayList<OrayUser>() ;
    private SearchFriendAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friend);
        this.initView();
    }

    private void initView()
    {
        TextView pageTitle=(TextView)findViewById(R.id.headerText);
        pageTitle.setText("查找用户");
        search_resultStr =(TextView) findViewById(R.id.search_resultStr);
        listView=(ListView)findViewById(R.id.listview_user);
        listView.setOnItemClickListener(this);
        adapter = new SearchFriendAdapter(this,this.userList,R.layout.friend_child);
        listView.setAdapter(adapter);       

    }   /**
     * 执行点击搜索指令
     * */
    private void action_search(String idOrName)
    {
        List<OrayUser> temp_users=Manager.getInstance().doSearchUser(idOrName);
        search_resultStr.setText("没有找到符合条件的用户或群组");
        this.setSearchResult(temp_users);
    }

    private void setSearchResult(List<OrayUser> temp_users)
    {
        this.userList.clear();
        if(temp_users==null||temp_users.size()==0)
        {
            search_resultStr.setVisibility(View.VISIBLE);
        }
        else
        {
            for (OrayUser orayUser :temp_users) {
                if(orayUser.getUserID().equals(ClientGlobalCache.getInstance().getCurrentUser().getUserID()))
                {
                    temp_users.remove(orayUser);
                    break;
                }
            }
            this.userList.addAll(temp_users) ;
            search_resultStr.setVisibility(View.GONE);
        }
        this.adapter.notifyDataSetChanged();
    }
}