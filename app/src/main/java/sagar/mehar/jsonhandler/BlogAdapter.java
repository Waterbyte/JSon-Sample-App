package sagar.mehar.jsonhandler;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * Created by Mountain on 07-06-2017.
 */

public class BlogAdapter extends BaseAdapter {

    Context context;
    private String[][] data;
    private LayoutInflater inflater;
    public BlogAdapter(Context context,String[][] data)
    {
        this.context=context;
        this.data=data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class Holder
    {
        ImageView thumbnail;
        ImageView author_dp;
        TextView title;
        TextView author_name;
        TextView author_user_name;
    }



    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder = new Holder();
        ImageLoader imageLoader = ImageLoader.getInstance();

        final View rowView;
        if (convertView == null) {
            rowView = inflater.inflate(R.layout.list_item, parent, false);
        } else {
            rowView = convertView;
        }
        holder.thumbnail=(ImageView)rowView.findViewById(R.id.thumbnail);
        holder.title=(TextView)rowView.findViewById(R.id.title);
       // holder.author_dp=(ImageView)rowView.findViewById(R.id.author_dp);
        holder.author_name=(TextView)rowView.findViewById(R.id.author_name);
       // holder.author_user_name=(TextView)rowView.findViewById(R.id.author_user_name);
        imageLoader.displayImage(data[position][2],holder.thumbnail);
        //imageLoader.displayImage(data[position][6],holder.author_dp);

        holder.title.setText(data[position][0]);
        holder.author_name.setText("By: "+data[position][4]);
       // holder.author_user_name.setText(data[position][5]);


        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBox(data,position);
            }
        });

        return rowView;
    }

    public void dialogBox(String[][] data,int position)
    {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_dialog);
        final ImageView cover=(ImageView)dialog.findViewById(R.id.cover);
        TextView description=(TextView)dialog.findViewById(R.id.description);
        final ProgressBar progressBar=(ProgressBar)dialog.findViewById(R.id.progressBarDialog);
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(data[position][1],cover,new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
               progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                cover.setImageResource(android.R.drawable.stat_notify_error);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                progressBar.setVisibility(View.GONE);
            }
        });
        description.setText(data[position][3]);
        dialog.show();
    }

}
