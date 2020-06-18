package allpointech.touchall.user.store;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;

import allpointech.touchall.R;
import allpointech.touchall.custom.glide.GlideLoader;
import allpointech.touchall.network.http.HttpInfo;
import allpointech.touchall.network.http.json.JSONParser;

/**
 * Created by jay on 2018. 6. 27..
 */

public class FindStoreAdapter extends RecyclerView.Adapter<FindStoreAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<JSONObject> mDataList = new ArrayList<>();

    private OnFindStoreClickListener mListener;

    public void setOnFindStoreClickListener(OnFindStoreClickListener listener) {
        mListener = listener;
    }

    protected interface OnFindStoreClickListener {
        void onItemClick(int position);
    }

    public FindStoreAdapter(Context context) {
        mContext = context;
    }

    public FindStoreAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(mContext).inflate(R.layout.row_user_findstore, parent, false);
        ViewHolder holder = new ViewHolder(v, viewType);
        return holder;
    }

    public void onBindViewHolder(final ViewHolder holder, final int position) {
        JSONObject row_data = mDataList.get(position);

        String image = JSONParser.getString(row_data, "image");
        String storeName = JSONParser.getString(row_data, "name");
        String storeAddr = JSONParser.getString(row_data, "addr");
        String storePhone = JSONParser.getString(row_data, "phone");
//        String storePoint = JSONParser.getString(row_data, "point");
//        String storeStemp = JSONParser.getString(row_data, "stemp");
//
//        if (storePoint != null && storePoint.length() > 0) {
//            storePoint = "P : " + storePoint;
//        }
//        else {
//            storePoint = "P : 0";
//        }

        String storeKind = JSONParser.getString(row_data, "kind");
        String storeSid = JSONParser.getString(row_data, "sid");
        String storeSSN = JSONParser.getString(row_data, "ssn");

        if (image != null && image.length() > 0)
            GlideLoader.loadView(mContext, HttpInfo.HOST_URL + image, holder.mIvThumbNail);
        holder.mTvStoreName.setText(storeName);
        holder.mTvStoreKinds.setText(storeKind);
        holder.mTvStoreAddress.setText(storeAddr);
        holder.mTvStoreEvaluationPhonenumber.setText(storePhone);
        //holder.mTvStorePoint.setText(storePoint);
        holder.mBtnUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    public void addHeaderView() {

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mIvThumbNail;
        private TextView mTvStoreName;
        private TextView mTvStoreKinds;
        private TextView mTvStoreAddress;
        private TextView mTvStoreEvaluationPhonenumber;
        private TextView mTvStorePoint;
        private TextView mTvStoreStamp;
        private TextView mBtnUse;

        public ViewHolder(View view, int viewType) {
            super(view);

            mIvThumbNail = (ImageView) view.findViewById(R.id.tv_findstore_list_thumb);
            mTvStoreName = (TextView) view.findViewById(R.id.tv_findstore_list_storename);
            mTvStoreKinds = (TextView) view.findViewById(R.id.tv_findstore_list_kinds);
            mTvStoreAddress = (TextView) view.findViewById(R.id.tv_findstore_list_address);
            mTvStoreEvaluationPhonenumber = (TextView) view.findViewById(R.id.tv_findstore_list_user_evaluation_phone);

            mTvStorePoint = (TextView) view.findViewById(R.id.tv_findstore_list_point);
            mTvStoreStamp = (TextView) view.findViewById(R.id.tv_findstore_list_stamp);
            mBtnUse = (TextView) view.findViewById(R.id.tv_findstore_list_use);
            //mBtnUse.setVisibility(View.GONE);
        }
    }

    public void refreshData(ArrayList<JSONObject> data) {
        mDataList.clear();
        mDataList.addAll(data);
        notifyDataSetChanged();
    }

}

