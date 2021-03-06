package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.NewGoodsBean;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.views.FooterViewHolder;

/**
 * Created by Administrator on 2016/10/17.
 */
public class NewGoodsAdapter extends RecyclerView.Adapter {
    List<NewGoodsBean> newGoodsList = new ArrayList<>();
    Context context;

    RecyclerView parent;
    // String textFooter;
    boolean isMore;
    int sortBy = I.SORT_BY_ADDTIME_DESC;

    public void setSortBy(int sortBy) {
        this.sortBy = sortBy;
        sortBy();
        notifyDataSetChanged();

    }

    public NewGoodsAdapter(List<NewGoodsBean> newGoodsList, Context context) {
        this.newGoodsList = newGoodsList;
        this.context = context;
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
        notifyDataSetChanged();
    }

    /*public void setTextFooter(String textFooter) {
        this.textFooter = textFooter;
        notifyDataSetChanged();
    }*/


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent = (RecyclerView) parent;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = null;
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case I.TYPE_FOOTER:
                view = inflater.inflate(R.layout.item_footer, parent, false);
                holder = new FooterViewHolder(view);
                break;
            case I.TYPE_ITEM:
                view = inflater.inflate(R.layout.item_newgoods, parent, false);
                holder = new NewGoodsViewHolder(view);
                break;

        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == getItemCount() - 1) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            footerViewHolder.tvFooter.setText(getFooter());
            return;
        }
        NewGoodsViewHolder newGoodsViewHolder = (NewGoodsViewHolder) holder;
        NewGoodsBean newGoods = newGoodsList.get(position);
        newGoodsViewHolder.tvGoodsName.setText(newGoods.getGoodsName());
        newGoodsViewHolder.tvGoodsPrice.setText(newGoods.getCurrencyPrice());
        ImageLoader.downloadImg(context, newGoodsViewHolder.ivNewGoods, newGoods.getGoodsThumb());
        newGoodsViewHolder.layoutNewGood.setTag(newGoods.getGoodsId());
    }

    private int getFooter() {
        return isMore ? R.string.load_more : R.string.no_more;
    }

    @Override
    public int getItemCount() {
        return newGoodsList != null ? newGoodsList.size() + 1 : 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return I.TYPE_FOOTER;
        } else {
            return I.TYPE_ITEM;
        }
    }

    public void initData(ArrayList<NewGoodsBean> goodsList) {
        if (newGoodsList != null) {
            this.newGoodsList.clear();
        }
        this.newGoodsList.addAll(goodsList);
        notifyDataSetChanged();
    }

    public void addData(ArrayList<NewGoodsBean> goodsList) {
        this.newGoodsList.addAll(goodsList);
        notifyDataSetChanged();
    }



    class NewGoodsViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.ivNewGoods)
        ImageView ivNewGoods;
        @Bind(R.id.tvGoodsName)
        TextView tvGoodsName;
        @Bind(R.id.tvGoodsPrice)
        TextView tvGoodsPrice;
        @Bind(R.id.layout_new_good)
        LinearLayout layoutNewGood;


        public NewGoodsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.layout_new_good)
        public void onGoodsItemClick() {
            int goodsId = (int) layoutNewGood.getTag();
           /* context.startActivity(new Intent(context, GoodsDetailsActivity.class)
                    .putExtra(I.GoodsDetails.KEY_GOODS_ID,goodsId));*/
            MFGT.gotoGoodsDetailsActivity(context, goodsId);
        }


    }
    private void sortBy(){
        Collections.sort(newGoodsList, new Comparator<NewGoodsBean>() {
            @Override
            public int compare(NewGoodsBean left, NewGoodsBean right) {
                int result = 0;
                switch (sortBy) {
                    case I.SORT_BY_ADDTIME_ASC:
                        result = (int) (Long.valueOf(left.getAddTime()) - Long.valueOf(right.getAddTime()));
                        break;
                    case I.SORT_BY_ADDTIME_DESC:
                        result = (int) (Long.valueOf(right.getAddTime()) - Long.valueOf(left.getAddTime()));
                        break;
                    case I.SORT_BY_PRICE_ASC:
                        result = getPrice(left.getCurrencyPrice()) - getPrice(right.getCurrencyPrice());
                        break;
                    case I.SORT_BY_PRICE_DESC:
                        result = getPrice(right.getCurrencyPrice()) - getPrice(left.getCurrencyPrice());
                        break;
                }
                return result;
            }

            private int getPrice(String price) {
                price = price.substring(price.indexOf("￥") + 1);
                return Integer.valueOf(price);
            }
        });
    }


}
