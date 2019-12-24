package com.weiwei.websocketclienttest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

  private List<String> mList = new ArrayList<>();

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater
        .from(parent.getContext()).inflate(R.layout.item_msg, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    holder.mTextView.setText(mList.get(holder.getAdapterPosition()));
  }

  @Override
  public int getItemCount() {
    return mList.size();
  }

  void appendMsg(String msg) {
    mList.add(msg);
    notifyDataSetChanged();
  }

  class ViewHolder extends RecyclerView.ViewHolder {

    TextView mTextView;

    ViewHolder(View v) {
      super(v);
      mTextView = v.findViewById(R.id.textView_msg);
    }
  }
}
