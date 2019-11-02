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
import java.util.ArrayList;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder> {

    private ArrayList<File> mData = null;
    private Context context;

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView1 ;

        ViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            textView1 = itemView.findViewById(R.id.file_location) ;

            // 파일을 클릭하면 파일 열기
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        File item = mData.get(pos);
                        fileOpen(item);
                    }
                }
            });

            // 파일을 길게 누르면 삭제 Dialog 보여주기
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // 오랫동안 눌렀을 때 이벤트가 발생됨
                    int pos = getAdapterPosition();
                    File file = mData.get(pos);

                    fileDeleteDialog(file);

                    // 리턴값이 있다
                    // 이메서드에서 이벤트에대한 처리를 끝냈음
                    //    그래서 다른데서는 처리할 필요없음 true
                    // 여기서 이벤트 처리를 못했을 경우는 false
                    return true;
                }
            });
        }
    }

    //
    // 생성자에서 데이터 리스트 객체를 전달받음.
    //
    public FileAdapter(ArrayList<File> list) {
        mData = list ;
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
        String text = file.getAbsolutePath();

        holder.textView1.setText(text) ;
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size() ;
    }

    public void setData(ArrayList<File> list) {
        mData = list;
        notifyDataSetChanged();
    }

    //
    // 사용자 정의 함수
    //


    // 엑셀 파일 열기
    private void fileOpen(File item) {
        Uri uri;
        // API 24 이상 일경우
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", item);
        }

        // API 24 미만 일경우
        else {
            uri = Uri.fromFile(item);
        }

        Intent shareIntent = new Intent(Intent.ACTION_VIEW);
        shareIntent.setDataAndType(uri, "application/vnd.ms-excel");
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_GRANT_READ_URI_PERMISSION);

        context.startActivity(shareIntent);

        // TODO : use item.
        //Uri path = Uri.fromFile(item);


        // 파일 공유하기
//                        Uri uri = FileProvider.getUriForFile(context, "com.bignerdranch.android.test.fileprovider", item);
//
//                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
//                        shareIntent.setType("application/excel");
//                        shareIntent.putExtra(Intent.EXTRA_STREAM,uri);
//
//                        Intent intent = Intent.createChooser(shareIntent, "엑셀 내보내기");
//
//                        context.startActivity(intent);

        //출처: https://liveonthekeyboard.tistory.com/entry/안드로이드-엑셀-파일-생성-내보내기 [키위남]
    }

    // 삭제 Dialog
    private void fileDeleteDialog(final File file){
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(context);
        alt_bld.setMessage("해당 파일을 삭제하시겠습니까?").setCancelable(
                false).setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        file.delete();
                        mData.remove(file);
                        notifyDataSetChanged();
                        //mData.remove(file);
                        // Action for 'Yes' Button
                    }
                }).setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Action for 'NO' Button
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alt_bld.create();
        // Title for AlertDialog
        alert.setTitle("파일 삭제");

        alert.show();
    }
}
