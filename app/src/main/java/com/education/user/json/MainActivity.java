package com.education.user.json;

import android.content.Context;
import android.os.AsyncTask;
import android.renderscript.ScriptGroup;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.education.user.json.model.Moviemodel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {
    Button b;
    TextView tv;
    private ListView listView;
    HttpURLConnection connection =null;
    BufferedReader reader=null;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       listView = (ListView)findViewById(R.id.listview);
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()

           .cacheInMemory(true)
                .cacheOnDisk(true)

           .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())

           .defaultDisplayImageOptions(defaultOptions).build();
        ImageLoader.getInstance().init(config);


    }

    public class JSON extends  AsyncTask<String,String,List<Moviemodel>>{

        @Override
        protected List<Moviemodel> doInBackground(String... strings) {

            try {
                URL url = new URL(strings[0]);
             connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
               reader = new BufferedReader((new InputStreamReader(stream)));

                String line = "";
                StringBuffer buffer = new StringBuffer();

                while((line = reader.readLine())!=null){
                    buffer.append(line);
                }

                String finalJson = buffer.toString();

                JSONObject obj = new JSONObject(finalJson);
                JSONArray childarray = obj.getJSONArray("movies");
                List<Moviemodel> moviemodelList  = new ArrayList <>();
                for(int i=0;i<childarray.length();i++) {

                    JSONObject childobj = childarray.getJSONObject(i);
                    Moviemodel moviemodel = new Moviemodel();
                    moviemodel.setMovie(childobj.getString("movie"));
                    moviemodel.setYear( childobj.getInt("year"));
                    moviemodel.setDirector(childobj.getString("director"));
                    moviemodel.setTagline(childobj.getString("tagline"));
                    moviemodel.setImage(childobj.getString("image"));
                    moviemodel.setStory(childobj.getString("story"));

                    moviemodelList.add(moviemodel);
                }
                return moviemodelList;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if(connection!=null){
                connection.disconnect();}
                if(reader!=null){
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Moviemodel> result) {
            super.onPostExecute(result);
            MovieAdapter movieAdapter = new MovieAdapter(getApplicationContext(),R.layout.row,result);
            listView.setAdapter(movieAdapter) ;
           // tv.setText(result);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
                if(id==R.id.refresh){
                    new JSON().execute("https://jsonparsingdemo-cec5b.firebaseapp.com/jsonData/moviesData.txt");
                    return  true;
                }
        return super.onOptionsItemSelected(item);
    }



    public class MovieAdapter extends ArrayAdapter{
        private int resource;
        private List<Moviemodel> moviemodelList;
        private LayoutInflater inflater;
        public MovieAdapter(@NonNull Context context, int resource, @NonNull List<Moviemodel> objects) {
            super(context, resource, objects);
            moviemodelList=objects;
            this.resource = resource;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            if(convertView  == null ){
                convertView = inflater.inflate(resource,null);
            }

            ImageView imageicon;
            TextView moviename;
            TextView year;
            TextView director;
            TextView tagline;

            imageicon = (ImageView)convertView.findViewById(R.id.imageView2);
            moviename  = (TextView)convertView.findViewById(R.id.moviename);
            year  = (TextView)convertView.findViewById(R.id.year);
            director  = (TextView)convertView.findViewById(R.id.director);
            tagline  = (TextView)convertView.findViewById(R.id.tagline);

            ImageLoader.getInstance().displayImage(moviemodelList.get(position).getImage(), imageicon);
            moviename.setText(moviemodelList.get(position).getMovie());
            year.setText("Year : " +moviemodelList.get(position).getYear());
            director.setText("Director :"+moviemodelList.get(position).getDirector());
            tagline.setText("Tagline :"+moviemodelList.get(position).getTagline());

            return convertView;
        }
    }
}

//    private  TextView textView;
//    private  Button button;
//    private  HttpURLConnection connection;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        button=(Button)findViewById(R.id.button);
//        textView=(TextView)findViewById(R.id.textView);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new Background().execute("https://jsonparsingdemo-cec5b.firebaseapp.com/jsonData/moviesData.txt");
//            }
//        });
//
//    }
//
//
//    public class Background extends AsyncTask <String,String,String> {
//        String json ="";
//        HttpURLConnection connection = null;
//        BufferedReader reader =null;
//        @Override
//        protected String doInBackground(String... strings) {
//
//            try {
//                URL url = new URL(strings[0]);
//                connection =(HttpURLConnection) url.openConnection();
//                connection.connect();
//
//                InputStream stream = connection.getInputStream();
//               reader = new BufferedReader(new InputStreamReader(stream));
//                StringBuffer buffer =new StringBuffer();
//                String line = "";
//                while((line = reader.readLine())!=null){
//                    buffer.append(line);
//                }
//                StringBuffer jsonvalue = new StringBuffer();
//                JSONObject parentobj = new JSONObject();
//                JSONArray jsonArray = parentobj.getJSONArray("movies");
//
//                for(int i=0;i<jsonArray.length() ;i++){
//
//                    JSONObject finalobj = jsonArray.getJSONObject(0);
//
//                    String moviename = finalobj.getString("movie");
//                    int year = finalobj.getInt("year");
//
//                    jsonvalue.append(moviename + "" + year + "\n");
//
//                }
//                    return jsonvalue.toString();
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            } finally {
//                if (connection != null) {
//                    connection.disconnect();
//                }
//                if(reader!=null){
//                    try {
//                        reader.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            textView.setText(result);
//        }
//}


