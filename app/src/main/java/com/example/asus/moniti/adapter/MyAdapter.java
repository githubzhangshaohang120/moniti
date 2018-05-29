package com.example.asus.moniti.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.moniti.MessageEvent;
import com.example.asus.moniti.PriceAndCountEvent;
import com.example.asus.moniti.R;
import com.example.asus.moniti.bean.BaseBean;
import com.example.asus.moniti.bean.CartBean;
import com.example.asus.moniti.utils.RetrofitHelper;
import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MyAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<CartBean.DataBean> groupList;
    private List<List<CartBean.DataBean.ListBean>> childList;
    private LayoutInflater inflater;

    private AlertDialog show;
    private Boolean i01 = false;

    public MyAdapter(Context context, List<CartBean.DataBean> groupList, List<List<CartBean.DataBean.ListBean>> childList) {
        this.context = context;
        this.groupList = groupList;
        this.childList = childList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childList.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final GroupViewHolder holder1;
        View view = null;
        if(convertView == null)
        {
            holder1 = new GroupViewHolder();
            view = inflater.inflate(R.layout.group, parent, false);
            holder1.shopName = view.findViewById(R.id.shop_name);
            holder1.itemBj = view.findViewById(R.id.item_bj);
            view.setTag(holder1);
        }else {
            view = convertView;
            holder1 = (GroupViewHolder) view.getTag();
        }
        final CartBean.DataBean dataBean = groupList.get(groupPosition);
        holder1.shopName.setText(dataBean.getSellerName());
        Boolean check = dataBean.getCheck();
        holder1.shopName.setChecked(dataBean.getCheck());
        holder1.itemBj.setText(dataBean.getBj());

        holder1.itemBj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  List<CartBean.DataBean.ListBean> listBeans = childList.get(groupPosition);
                List<CartBean.DataBean.ListBean> list = dataBean.getList();
                if(i01==false)
                {   i01=true;
                    dataBean.setBj("完成");
                    for(int i = 0;i<list.size();i++)
                    {
                        list.get(i).setShow(false);

                    }
                    notifyDataSetChanged();
                }
                else {
                    i01=false;
                    dataBean.setBj("编辑");
                    for(int i = 0;i<list.size();i++)
                    {
                        list.get(i).setShow(true);

                    }
                    notifyDataSetChanged();
                }

                notifyDataSetChanged();
            }
        });
        holder1.shopName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataBean.setCheck(holder1.shopName.isChecked());
                changeChildCbState(groupPosition, holder1.shopName.isChecked());
                EventBus.getDefault().post(compute());
                changeAllCbState(isAllGroupCbSelected());
                notifyDataSetChanged();
            }
        });

        return view;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildViewHolder holder;
        View view = null;
        if(convertView == null)
        {
            holder = new ChildViewHolder();
            view = inflater.inflate(R.layout.child, parent, false);
            holder.dele = view.findViewById(R.id.del);
            holder.add = view.findViewById(R.id.add);
            holder.end = view.findViewById(R.id.end);
            holder.sel = view.findViewById(R.id.sel);
            holder.img = view.findViewById(R.id.img);
            holder.name = view.findViewById(R.id.name);
            holder.price = view.findViewById(R.id.price);
            holder.prices = view.findViewById(R.id.prices);
            holder.jieShao = view.findViewById(R.id.jie_shao);
            holder.num = view.findViewById(R.id.num);
            holder.show = view.findViewById(R.id.show);
            holder.hide = view.findViewById(R.id.hide);
            holder.num01 = view.findViewById(R.id.num01);
            view.setTag(holder);
        }
        else {
            view = convertView;
            holder = (ChildViewHolder) view.getTag();
        }
        final CartBean.DataBean.ListBean listBean = childList.get(groupPosition).get(childPosition);
        holder.hide.setVisibility(View.GONE);
        holder.jieShao.setText(listBean.getSubhead());
        holder.num.setText("数量:"+listBean.getNum());
        holder.num01.setText(listBean.getNum()+"");
        holder.price.setText("$"+listBean.getPrice());
        holder.prices.setText("$"+listBean.getBargainPrice());
        holder.sel.setChecked(listBean.getCheck());
        String images = listBean.getImages();
        String[] split = images.split("\\|");
        Uri parse = Uri.parse(split[0]);
        holder.img.setImageURI(parse);
        holder.name.setText(listBean.getTitle());
        int pid = listBean.getPid();
        final String pid01  = pid+"";
        final int selected = listBean.getSelected();
        final String sellerid = listBean.getSellerid()+"";
        final String num = listBean.getNum()+"";
        if(listBean.getShow()==false)
        {
            holder.hide.setVisibility(View.VISIBLE);
            holder.show.setVisibility(View.GONE);
        }
        else{
            holder.hide.setVisibility(View.GONE);
            holder.show.setVisibility(View.VISIBLE);
        }
        holder.sel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //设置该条目对象里的checked属性值
                listBean.setCheck(holder.sel.isChecked());
                PriceAndCountEvent priceAndCountEvent = compute();
                EventBus.getDefault().post(priceAndCountEvent);

                if (holder.sel.isChecked()) {
                    //当前checkbox是选中状态
                    if (isAllChildCbSelected(groupPosition)) {
                        changGroupCbState(groupPosition, true);
                        changeAllCbState(isAllGroupCbSelected());
                    }
                } else {
                    changGroupCbState(groupPosition, false);
                    changeAllCbState(isAllGroupCbSelected());
                }
                notifyDataSetChanged();
            }
        });
        //加号
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num1 = listBean.getNum();
                num1++;
                holder.num01.setText(num1 + "");
                holder.num.setText(num1+"");
                listBean.setNum(num1);
                if (holder.sel.isChecked()) {
                    PriceAndCountEvent priceAndCountEvent = compute();
                    EventBus.getDefault().post(priceAndCountEvent);
                }
            }
        });
        //减号
        holder.end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = listBean.getNum();
                if (num == 1) {
                    Toast.makeText(context,"最小值为1",Toast.LENGTH_SHORT).show();
                    return;
                }
                holder.num01.setText(--num + "");
                holder.num.setText(num+"");
                listBean.setNum(num);
                if (holder.sel.isChecked()) {
                    PriceAndCountEvent priceAndCountEvent = compute();
                    EventBus.getDefault().post(priceAndCountEvent);
                }
            }
        });
        //删除
        holder.dele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final AlertDialog.Builder normalDialog =
                        new AlertDialog.Builder(context);
                normalDialog.setIcon(R.drawable.ic_launcher_background);
                normalDialog.setTitle("确认要删除此商品吗");
                normalDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //...To-do
                                RetrofitHelper.getApi().deleteGoodCar("71", childList.get(groupPosition).get(childPosition).getPid() + "")
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Observer<BaseBean>() {
                                            @Override
                                            public void onSubscribe(Disposable d) {

                                            }

                                            @Override
                                            public void onNext(BaseBean baseBean) {
                                                if (baseBean.getCode().equals("0")){
                                                    List<CartBean.DataBean.ListBean> datasBeen = childList.get(groupPosition);
                                                    CartBean.DataBean.ListBean remove = datasBeen.remove(childPosition);
                                                    if (datasBeen.size() == 0) {
                                                        childList.remove(groupPosition);
                                                        groupList.remove(groupPosition);
                                                    }
                                                    EventBus.getDefault().post(compute());
                                                    notifyDataSetChanged();
                                                }else{
                                                    Toast.makeText(context,"删除失败",Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onError(Throwable e) {

                                            }

                                            @Override
                                            public void onComplete() {

                                            }
                                        });

                            }
                        });
                normalDialog.setNegativeButton("关闭",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //...To-do
                                show.dismiss();
                            }
                        });
                // 显示
                show = normalDialog.show();

            }
        });
        notifyDataSetChanged();
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class GroupViewHolder {
        CheckBox shopName;
        TextView itemBj;

    }

    class ChildViewHolder {
        CheckBox sel;
        SimpleDraweeView img;
        TextView name;
        TextView jieShao;
        TextView price;
        TextView prices;
        TextView num;
        TextView num01;
        Button dele;
        TextView add;
        TextView end;
        LinearLayout show;
        RelativeLayout hide;
    }
    /**
     * 计算列表中，选中的钱和数量
     */
    private PriceAndCountEvent compute() {
        int count = 0;
        int price = 0;
        for (int i = 0; i < childList.size(); i++) {
            List<CartBean.DataBean.ListBean> listBeans = childList.get(i);
            for (int j = 0; j < listBeans.size(); j++) {
                CartBean.DataBean.ListBean listBean = listBeans.get(j);
                if (listBean.getCheck()) {
                    price += listBean.getNum() * listBean.getPrice();
                    count += listBean.getNum();
                }
            }
        }
        PriceAndCountEvent priceAndCountEvent = new PriceAndCountEvent();
        priceAndCountEvent.setCount(count);
        priceAndCountEvent.setPrice(price);
        return priceAndCountEvent;
    }
    /**
     * 设置全选、反选
     *
     * @param flag
     */
    public void changeAllListCbState(boolean flag) {
        for (int i = 0; i < groupList.size(); i++) {
            changGroupCbState(i, flag);
            changeChildCbState(i, flag);
        }
        EventBus.getDefault().post(compute());
        notifyDataSetChanged();
    }
    /**
     * 判断一级列表是否全部选中
     *
     * @return
     */
    private boolean isAllGroupCbSelected() {
        for (int i = 0; i < groupList.size(); i++) {
            CartBean.DataBean dataBean = groupList.get(i);
            if (!dataBean.getCheck()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断二级列表是否全部选中
     *
     * @param groupPosition
     * @return
     */
    private boolean isAllChildCbSelected(int groupPosition) {
        List<CartBean.DataBean.ListBean> listBeans = childList.get(groupPosition);
        for (int i = 0; i < listBeans.size(); i++) {
            CartBean.DataBean.ListBean listBean = listBeans.get(i);
            if (!listBean.getCheck()) {
                return false;
            }
        }
        return true;
    }
    /**
     * 改变全选的状态
     *
     * @param flag
     */
    private void changeAllCbState(boolean flag) {
        MessageEvent messageEvent = new MessageEvent();
        messageEvent.setChecked(flag);
        EventBus.getDefault().post(messageEvent);
    }

    /**
     * 改变一级列表checkbox状态
     *
     * @param groupPosition
     */
    private void changGroupCbState(int groupPosition, boolean flag) {
        CartBean.DataBean dataBean = groupList.get(groupPosition);
        dataBean.setCheck(flag);
    }

    /**
     * 改变二级列表checkbox状态
     *
     * @param groupPosition
     * @param flag
     */
    private void changeChildCbState(int groupPosition, boolean flag) {
        List<CartBean.DataBean.ListBean> listBeans = childList.get(groupPosition);
        for (int i = 0; i < listBeans.size(); i++) {
            CartBean.DataBean.ListBean listBean = listBeans.get(i);
            listBean.setCheck(flag);
        }
    }
}
