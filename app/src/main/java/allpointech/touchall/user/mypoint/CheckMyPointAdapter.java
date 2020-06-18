package allpointech.touchall.user.mypoint;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tuna.ui.fragment.BaseDialogFragment;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

import allpointech.touchall.R;
import allpointech.touchall.dialog.SpinnerDateDialog;
import allpointech.touchall.network.http.json.JSONParser;

/**
 * Created by jay on 2018. 6. 27..
 */

public class CheckMyPointAdapter extends RecyclerView.Adapter<CheckMyPointAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<JSONObject> mDataList = new ArrayList<>();

    private OnMyPointClickListener mListener;

    public void setOnMyPointClickListener(OnMyPointClickListener listener) {
        mListener = listener;
    }

    protected interface OnMyPointClickListener {
        void onItemClick(View v);

        void onPayCostClick(int position);
        void onPointClick(int position);
    }

    public CheckMyPointAdapter(Context context) {
        mContext = context;
    }

    public CheckMyPointAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v;
//        //viewType이 0이면 헤더이므로 flag를 설정하고 헤더 아이템을 inflater를 이용해서 할당한다
//        if(viewType == 0) {
//            v = LayoutInflater.from(mContext).inflate(R.layout.row_user_check_mypoint_header, parent, false);
//        }
//        else if(viewType == 1) {
//            v = LayoutInflater.from(mContext).inflate(R.layout.row_user_check_mypoint_header_find, parent, false);
//        }
//        //헤더가 아닌 나머지 콘텐츠들
//        else
        {
            v = LayoutInflater.from(mContext).inflate(R.layout.row_user_mypoint, parent, false);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //int itemPosition = ;
                    if (mListener != null)
                        mListener.onItemClick(v);
                }
            });
        }
        ViewHolder holder = new ViewHolder(v, viewType);
        return holder;
    }

    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if (position == 0 )
            return ;

        int index = position-1;
        JSONObject row_data = mDataList.get(index);
        if (row_data != null) {
            String storeName = JSONParser.getString(row_data, "name");
            String payCost = JSONParser.getString(row_data, "amount");
            String pointType = JSONParser.getString(row_data, "typeName");
            String payDate = JSONParser.getString(row_data, "payDate");
            String pointKind = JSONParser.getString(row_data, "pointType");
            String storeType = JSONParser.getString(row_data, "earnName");
            String point = JSONParser.getString(row_data, "point");

            holder.mTvMyPointIndex.setText(String.valueOf(index));
            holder.mTvMyPointDate.setText(payDate);
            holder.mTvMyPointStoreKind.setText("");
            holder.mTvMyPointStoreBonus.setText(pointKind);

            holder.mTvMyPointStoreAddr.setText("");
            holder.mTvMyPointStoreName.setText(storeName);

            holder.mTvMyPointStoreType.setText(storeType);
            holder.mTvMyPointPayCost.setText(payCost);
            holder.mTvMyPointPayCost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null)
                        mListener.onPayCostClick(position-1);
                }
            });

            holder.mTvMyPointStoreUseType.setText(pointType);
            holder.mTvMyPointPoint.setText(point);
            holder.mTvMyPointPoint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null)
                        mListener.onPointClick(position-1);
                }
            });
        }



//        holder.mTvMyPointIndex.setText(String.valueOf(position));
//        holder.mTvMyPointStoreName.setText(storeName);
//        holder.mTvMyPointPayCost.setText(payCost);
//        holder.mTvMyPointPointType.setText(pointType);
//        holder.mTvMyPointDate.setText(payDate);
//        holder.mTvMyPointPointKind.setText(pointKind);
//        holder.mTvMyPointStroreType.setText(storeType);
//        holder.mTvMyPointPoint.setText(point);
    }

    @Override
    public int getItemCount() {
        if (mDataList.size() == 0)
            return 0;
        return mDataList.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    public void addHeaderView() {

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTvMyPointIndex;

        private TextView mTvMyPointDate;
        private TextView mTvMyPointStoreKind;
        private TextView mTvMyPointStoreBonus;

        private TextView mTvMyPointStoreAddr;
        private TextView mTvMyPointStoreName;

        private TextView mTvMyPointStoreType;
        private TextView mTvMyPointPayCost;

        private TextView mTvMyPointStoreUseType;
        //private TextView mTvMyPointDate;
        //private TextView mTvMyPointPointKind;
        //private TextView mTvMyPointStroreType;
        private TextView mTvMyPointPoint;

        public ViewHolder(View view, int viewType) {
            super(view);

            mTvMyPointIndex = (TextView) view.findViewById(R.id.tv_mypoint_list_index);

            mTvMyPointDate = (TextView) view.findViewById(R.id.tv_mypoint_list_use_date);
            mTvMyPointStoreKind = (TextView) view.findViewById(R.id.tv_mypoint_list_store_kind);
            mTvMyPointStoreBonus = (TextView) view.findViewById(R.id.tv_mypoint_list_store_bonus);

            mTvMyPointStoreAddr = (TextView) view.findViewById(R.id.tv_mypoint_list_store_addr);
            mTvMyPointStoreName = (TextView) view.findViewById(R.id.tv_mypoint_list_storename);

            mTvMyPointStoreType = (TextView) view.findViewById(R.id.tv_mypoint_list_store_type);
            mTvMyPointPayCost = (TextView) view.findViewById(R.id.tv_mypoint_list_paycost);

            mTvMyPointStoreUseType = (TextView) view.findViewById(R.id.tv_mypoint_list_store_use_type);
            //mTvMyPointPointKind = (TextView) view.findViewById(R.id.tv_mypoint_list_pointkind);
            //mTvMyPointStroreType = (TextView) view.findViewById(R.id.tv_mypoint_list_storetype);
            mTvMyPointPoint = (TextView) view.findViewById(R.id.tv_mypoint_list_point);
        }
    }

    public void refreshData(ArrayList<JSONObject> data) {
        mDataList.clear();
        mDataList.addAll(data);
        notifyDataSetChanged();
    }

}
