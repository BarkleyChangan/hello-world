public class SearchFriendAdapter extends CommonAdapter<OrayUser> {
   public SearchFriendAdapter(Context context,List<OrayUser> datas,int item_layoutId)
   {
       super(context,datas,item_layoutId);
   }

    @Override
    protected void setConvertView(View view, OrayUser orayUser,int itemViewType) {
        try{
            SearchFriendAdapter.ViewHolder holder;
            if(view.getTag()==null)
            {
                holder = new SearchFriendAdapter.ViewHolder(view);
                view.setTag(holder);
            }
            else {
                holder = (SearchFriendAdapter.ViewHolder) view.getTag();
            }
            HeadImgHelper.setUserHeadImg(holder.headImg,orayUser);
            if(orayUser.getName().equals(orayUser.getCommentName()))
            {
                holder.userName.setText(orayUser.getName());
            }
            else {
                holder.userName.setText(orayUser.getCommentName()+"（"+ orayUser.getName()+"）");
            }
            String catalogName= ClientGlobalCache.getInstance().getCurrentUser().getCatalogNameByFriend(orayUser.getUserID());
            if(!StringHelper.isNullOrEmpty(catalogName))
            {
                holder.describe.setText("[ "+ catalogName+" ]");
                holder.describe.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.describe.setText("");
                holder.describe.setVisibility(View.GONE);
            }
            holder.orgName.setText(orayUser.getUserID());
        }catch (Exception ex){ex.printStackTrace();}
    }

    private class ViewHolder
    {
        public ImageView headImg;
        public TextView userName;
        public TextView describe;
        public TextView orgName;

        public ViewHolder(View view)
        {
            this.headImg = (ImageView) view.findViewById(R.id.ct_photo);
            ImageView statusImg=(ImageView) view.findViewById(R.id.ct_status);
            statusImg.setVisibility(View.GONE);
            this.userName=(TextView) view.findViewById(R.id.ct_name);
            this.orgName=(TextView) view.findViewById(R.id.ct_sign);
            this.describe=(TextView) view.findViewById(R.id.ct_describe);
        }
    }
}