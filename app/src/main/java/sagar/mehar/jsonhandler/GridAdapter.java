package sagar.mehar.jsonhandler;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;


public class GridAdapter extends BaseAdapter  {

    private String[][] data;
    private Context context;
    private LayoutInflater inflater = null;
    private int Count=0;
    private boolean checkBoxState[];

    public GridAdapter(Context context, String[][] data) {
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        checkBoxState=new boolean[getCount()];
    }

    public class Holder {
        TextView tv;
        ImageView img;
        ProgressBar progressBar;
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
        return 0;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        final Holder holder = new Holder();
        ImageLoader imageLoader = ImageLoader.getInstance();

        View rowView;
        if (convertView == null) {
            rowView = inflater.inflate(R.layout.grid_item, parent, false);
        } else {
            rowView = convertView;
        }
        holder.tv = (TextView) rowView.findViewById(R.id.textView);
        holder.img=(ImageView)rowView.findViewById(R.id.imageView);
        holder.progressBar=(ProgressBar)rowView.findViewById(R.id.progressBar);

        imageLoader.displayImage(data[position][1],holder.img,new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
              holder.progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
               holder.img.setImageResource(android.R.drawable.stat_notify_error);
               holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                holder.progressBar.setVisibility(View.GONE);
            }
        });


        holder.tv.setText(data[position][0]);
        if(checkBoxState[position])
            holder.img.setColorFilter(Color.parseColor("#dd000000"));
        else
            holder.img.setColorFilter(Color.TRANSPARENT);

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             if(!checkBoxState[position])
             {
                 checkBoxState[position]=true;
                 holder.img.setColorFilter(Color.parseColor("#dd000000"));
                 Count++;
                 ((MainActivity)context).setupFab(10-Count);

             }
                else
             {
                 checkBoxState[position]=false;
                 holder.img.setColorFilter(Color.TRANSPARENT);
                 Count--;
                 ((MainActivity)context).setupFab(10-Count);
             }
            }
        });


        return rowView;
    }


}
