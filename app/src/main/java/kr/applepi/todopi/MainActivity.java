package kr.applepi.todopi;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private final static int SECOND_ACTIVITY = 2;
    ListView list;
    MyAdapter adapter;
    ArrayList<Data> items;
    SharedPreferences pref;
    String str;
    int Month;
    int Year;
    int Day;
    static double cnt=0, currcnt=0;
    static double delcnt=0;
    ImageView img_change;
    TextView percent;
    double percentResult;
    long dday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        items = new ArrayList<>();
        list = (ListView) findViewById(R.id.item_add);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        adapter = new MyAdapter(this, items);
        img_change = (ImageView) findViewById(R.id.img_change);
        percent = (TextView) findViewById(R.id.percent);

        list.setAdapter(adapter);
        loadNowData();
        adapter.notifyDataSetChanged();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClassName("kr.applepi.todopi", "kr.applepi.todopi.ToDoAdd");

                startActivityForResult(intent, SECOND_ACTIVITY);
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                currcnt++;
                cnt++;
                Log.v("currcnt", String.valueOf(currcnt));
                Log.v("카운트", String.valueOf(cnt));
                Toast.makeText(MainActivity.this, "Done Task!", Toast.LENGTH_SHORT).show();
                items.remove(i);
                saveNowData();
                adapter.notifyDataSetChanged();
                //Toast.makeText(getActivity(), Integer.toString(i), Toast.LENGTH_SHORT).show();
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(MainActivity.this);
                alert_confirm.setMessage("이 할일을 삭제하시겠습니까?").setCancelable(false).setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 'YES'
                                items.remove(i);
                                cnt+=1;
                                Log.v("currcnt", String.valueOf(currcnt));
                                Log.v("카운트", String.valueOf(cnt));
                                saveNowData();
                                adapter.notifyDataSetChanged();
                                Toast.makeText(MainActivity.this, "Deleted Task!", Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 'No'
                                return;
                            }
                        });
                AlertDialog alert = alert_confirm.create();
                alert.show();

                return false;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SECOND_ACTIVITY){
            Log.d("SECOND_ACTIVITY_LOG", "THIS CLOSE !!");

            if(resultCode == -1){
                str = data.getStringExtra("key1");
                Month = data.getIntExtra("key2", 0);
                Day = data.getIntExtra("key3", 0);
                Year = data.getIntExtra("key4", 0);
                dday = getDday(Month, Day, Year);
                String Dday = Long.toString(dday)+"일";
                items.add(new Data(str, Dday));
                adapter.notifyDataSetChanged();
                saveNowData();

                //this.updateHomeList();
            }
        }
    }

    long getDday(int month, int day, int year) {
        Calendar c = Calendar.getInstance();
        int currMonth = c.get(Calendar.MONTH);
        int currDay = c.get(Calendar.DAY_OF_MONTH);
        int currYear = c.get(Calendar.YEAR);
        //int resMonth = month - currMonth;
        //int resDay = day - currDay;
        //int resYear = year - currYear;
        //String dday = Integer.toString(resYear)+"년/"+Integer.toString(resMonth-1)+"월/"+Integer.toString(resDay)+"일";

        // Creates two calendars instances
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        // Set the date for both of the calendar instance
        cal1.set(currYear, currMonth, currDay);
        cal2.set(year, month-1, day);

        // Get the represented date in milliseconds
        long milis1 = cal1.getTimeInMillis();
        long milis2 = cal2.getTimeInMillis();

        // Calculate difference in milliseconds
        long diff = milis2 - milis1;

        // Calculate difference in days
        long diffDays = diff / (24 * 60 * 60 * 1000);

        long dday = diffDays;
        return dday;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Log.v("refreshed", "");
            percentResult = (currcnt/cnt)*100;
            String percentResStr = String.format("%.2f", percentResult);
            percent.setText(percentResStr+"%");
            if(percentResult >= 90.0){
                Drawable myDrawable = getResources().getDrawable(R.drawable.ic_sentiment_very_satisfied_black_48dp);
                img_change.setImageDrawable(myDrawable);
            }else if(percentResult >= 70.0){
                Drawable myDrawable = getResources().getDrawable(R.drawable.ic_sentiment_satisfied_black_48dp);
                img_change.setImageDrawable(myDrawable);
            }else if(percentResult >=50.0){
                Drawable myDrawable = getResources().getDrawable(R.drawable.ic_sentiment_neutral_black_48dp);
                img_change.setImageDrawable(myDrawable);
            }else{
                Drawable myDrawable = getResources().getDrawable(R.drawable.ic_sentiment_dissatisfied_black_48dp);
                img_change.setImageDrawable(myDrawable);
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    void saveNowData() {
        String json = new Gson().toJson(items);
        pref.edit().putString("name", json).apply();
    }

    void loadNowData() {
        String json = pref.getString("name", "");
        Type listType = new TypeToken<ArrayList<Data>>(){}.getType();
        ArrayList<Data> list = new Gson().fromJson(json, listType);
        if(list != null){
            items.addAll(list);
        }
    }
}
