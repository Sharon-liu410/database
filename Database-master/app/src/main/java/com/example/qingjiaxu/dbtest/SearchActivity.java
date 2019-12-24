package com.example.qingjiaxu.dbtest;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private static final String TAG = "SearchActivity";
    protected FragmentTransaction fragmentTransaction;
    List<Word> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_main);

        ListView listView = findViewById(R.id.list_view);
        Intent intent=getIntent();
        String searchWord = intent.getStringExtra("searchWord");

        WordsDBHelper mDbHelper = new WordsDBHelper(this);
        list = mDbHelper.selectCertain(searchWord);

        // 判断横屏
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // 添加框架
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            RightFragment rightFragment = new RightFragment();
            fragmentTransaction.add(R.id.right_layout, rightFragment);
            fragmentTransaction.commit();
            FloatingActionButton fab = findViewById(R.id.favorite);
            fab.setVisibility(View.INVISIBLE);
        }

        if (list.size() > 0){
            MyAdapter adapter=new MyAdapter(list,getApplicationContext());
            listView.setAdapter(adapter);
        }
        else Toast.makeText(SearchActivity.this,"没有找到", Toast.LENGTH_LONG).show();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Word w = list.get(position);

                int orientation = getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    replaceFragment(w);

                } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
//                Toast.makeText(MainActivity.this, w.getName(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SearchActivity.this, DisplayActivity.class);
                    Log.d(TAG, "*******onItemClick*********");
                    Log.d(TAG, "" + w.getWordId());
                    Log.d(TAG, "*******onItemClick*********");
                    intent.putExtra("wordId", w.getWordId());
                    intent.putExtra("name", w.getName());
                    intent.putExtra("meaning", w.getMeaning());
                    intent.putExtra("sample", w.getSample());
                    intent.putExtra("collect", w.getCollect());
                    Log.d(TAG, "onItemClick: "+w.getName());
                    startActivity(intent);
                }
            }
        });

    }

    public void replaceFragment(Word word){
        fragmentTransaction= getSupportFragmentManager().beginTransaction();
        //通过调用RightFragment中的getInstance方法传修改文本
        RightFragment rightFragment = RightFragment.getInstance(word);
        //此时使用add方法会造成右侧fragment中文本重叠（未设置BackGround时）
        fragmentTransaction.replace(R.id.right_layout, rightFragment);
        fragmentTransaction.commit();
    }
}
