package sagar.mehar.jsonhandler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mountain on 07-06-2017.
 */

public class BlogActivity extends AppCompatActivity {
private BlogAdapter blogAdapter;
private String[][] data;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blog_layout);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("                        ARTICLES       ");//temporary way to get alignment right

        dialog();
    }
    public void dialog()
    {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://zailet.com/zailet_internship_assignment/get_data.php?topics_info=1";
        final ProgressDialog pd = new ProgressDialog(BlogActivity.this);
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
        String title="";
        String cover="";
        String thumbnail="";
        String description="";
        String author_name="";
        String author_user_name="";
        String author_dp="";



        try {
            JSONArray jsonMainArr = response.getJSONArray("posts");
            data=new String[jsonMainArr.length()][7];
            for (int i = 0; i < jsonMainArr.length(); i++) {
                JSONObject childJSONObject = jsonMainArr.getJSONObject(i);
                JSONObject postInfo=childJSONObject.getJSONObject("post_info");
                JSONObject authorInfo=childJSONObject.getJSONObject("author_info");
                data[i][0]=title=postInfo.getString("title");
                data[i][1]=cover= "http://zailet.com/"+postInfo.getString("cover");
                data[i][2]=thumbnail= "http://zailet.com/"+postInfo.getString("thumbnail");
                data[i][3]=description=postInfo.getString("description");
                data[i][4]=author_name=authorInfo.getString("name");
                data[i][5]=author_user_name=authorInfo.getString("username");
                data[i][6]=author_dp= "http://zailet.com/"+authorInfo.getString("dp");
            }
        }
        catch (JSONException e)
        {
            Log.e("Parse Error",e.toString());
        }
        ListView listView=(ListView)findViewById(R.id.list_view);
        blogAdapter=new BlogAdapter(BlogActivity.this,data);
        listView.setAdapter(blogAdapter);
    }


}
