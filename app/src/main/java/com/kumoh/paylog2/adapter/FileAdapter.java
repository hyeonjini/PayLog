package com.kumoh.paylog2.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.kumoh.paylog2.R;

import java.io.File;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder> {

    FileListRecyclerOnClickListener clickListener;
    FileListRecyclerLongClickListener longClickListener;

    private List<File> mData = null;
    private Context context;

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView file_name;
        TextView file_last_modified_time;

        ViewHolder(View itemView) {
            super(itemView);
            file_name = itemView.findViewById(R.id.file_name);
            file_last_modified_time = itemView.findViewById(R.id.file_last_modified_time);
        }
    }

    //
    // 생성자에서 데이터 리스트 객체를 전달받음.
    //
    public FileAdapter(List<File> data) {
        this.mData = data ;
    }

    //
    // 필수 Override 함수
    //

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public FileAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.item_file_list, parent, false) ;
        FileAdapter.ViewHolder vh = new FileAdapter.ViewHolder(view) ;

        return vh ;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(FileAdapter.ViewHolder holder, int position) {
        File file = mData.get(position);

        //holder.file.setText(file.getAbsolutePath());

        holder.file_name.setText(file.getName());

        Date lastModifiedTime = new Date(file.lastModified());
        Format format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        holder.file_last_modified_time.setText(format.format(lastModifiedTime));

        // onClick 리스너
        if(clickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClicked(position);
                }
            });
        }

        // onLongClick 리스너
        if(longClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    longClickListener.onItemLongClicked(position);
                    return true;
                }
            });
        }
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size() ;
    }

    public File getItem(int position) {
        return mData.get(position);
    }

    public void setData(ArrayList<File> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    public void removeItem(File file) {
        this.mData.remove(file);
        notifyDataSetChanged();
    }

    //
    // RecyclerView item Listener 등록
    //

    // onClick 리스너 인터페이스
    public interface FileListRecyclerOnClickListener{
        void onItemClicked(int position);
    }
    // set 리스너
    public void setOnClickListener(FileListRecyclerOnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    // onLongClick 리스너 인터페이스
    public interface FileListRecyclerLongClickListener {
        void onItemLongClicked(int position);
    }
    // set 리스너
    public void setLongClickListener(FileListRecyclerLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }
}
