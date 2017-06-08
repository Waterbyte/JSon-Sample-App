package sagar.mehar.jsonhandler;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;

public class MainActivity extends AppCompatActivity {

    private GridAdapter gridAdapter;
    private String[][] data;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("                        TOPICS       ");//temporary way to get alignment right

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(false)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).defaultDisplayImageOptions(options).build();
        ImageLoader.getInstance().init(config);


        dialog();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageBitmap(textAsBitmap(10+"", 40, Color.WHITE));

    }
    public static Bitmap textAsBitmap(String text, float textSize, int textColor) {
        Paint paint = new Paint(ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.0f); // round
        int height = (int) (baseline + paint.descent() + 0.0f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }

    public void setupFab(int value)
    {
        if(value<=10&&value>0)
        {
            fab.setEnabled(false);
            fab.setImageBitmap(textAsBitmap(value+"", 40, Color.WHITE));
        }
        else
        {
            fab.setEnabled(true);
            fab.setImageBitmap(textAsBitmap("Go",40,Color.WHITE));
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 Intent intent=new Intent(MainActivity.this,BlogActivity.class);
                 startActivity(intent);
            }
        });
    }

    public void dialog()
    {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://zailet.com/zailet_internship_assignment/get_data.php?get_topics=1";
        final ProgressDialog pd = new ProgressDialog(MainActivity.this);
        pd.setMessage("Loading Data...");
        pd.show();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null,
                // The third parameter Listener overrides the method onResponse() and passes
                //JSONObject as a parameter
                new Response.Listener<JSONObject>() {

                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(JSONObject response) {
                        pd.dismiss();
                        Toast.makeText(MainActivity.this,"Please Select atleast 10 topics...",Toast.LENGTH_LONG).show();
                        parse(response);
                    }
                },
                // The final parameter overrides the method onErrorResponse() and passes VolleyError
                //as a parameter
                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");
                    }
                }
        );
        queue.add(jsonObjectRequest);
    }

    public void parse(JSONObject response)
    {
        try {
            JSONArray jsonMainArr = response.getJSONArray("result");
            data=new String[jsonMainArr.length()][2];
            for (int i = 0; i < jsonMainArr.length(); i++) {
                JSONObject childJSONObject = jsonMainArr.getJSONObject(i);
                String id=childJSONObject.getString("id");
                String interest = childJSONObject.getString("interest");
                String  image = "http://zailet.com/"+childJSONObject.getString("cover");

                data[i][0]=interest;
                data[i][1]=image;

            }
        }
        catch (JSONException e)
        {
            Log.e("Parse Error",e.toString());
        }

        GridView gridView = (GridView)findViewById(R.id.gridView);
        gridAdapter = new GridAdapter(MainActivity.this, data);
        gridView.setAdapter(gridAdapter);


    }



}
