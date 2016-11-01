package jp.techacademy.atsushi.ninomiya.qa_app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FavoritesListActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private int mGenre = 0;

    // --- ここから ---
    private DatabaseReference mDatabaseReference;
    private ListView mListView;
    private ArrayList<Question> mQuestionArrayList = new ArrayList<Question>();
    private FavoritesListAdapter mAdapter;

    private DatabaseReference mFavoriteRef;
    private FirebaseUser user;
    private String mFavoriteValue;
    private String mGenreString;
    private DatabaseReference mGenreRef;

    List<String[]> favoriteMap = new ArrayList<String[]>();



    private ChildEventListener mEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            Log.d("dataSnapshot.getValue()", dataSnapshot.getValue().toString());

            HashMap map = (HashMap) dataSnapshot.getValue();

            String title = (String) map.get("title");
                String body = (String) map.get("body");
                String name = (String) map.get("name");
                String uid = (String) map.get("uid");
                String imageString = (String) map.get("image");
                Bitmap image = null;
                byte[] bytes;
                if (imageString != null) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    bytes = Base64.decode(imageString, Base64.DEFAULT);
                } else {
                    bytes = new byte[0];
                }

                ArrayList<Answer> answerArrayList = new ArrayList<Answer>();
                HashMap answerMap = (HashMap) map.get("answers");
                if (answerMap != null) {
                    for (Object key : answerMap.keySet()) {
                        HashMap temp = (HashMap) answerMap.get((String) key);
                        String answerBody = (String) temp.get("body");
                        String answerName = (String) temp.get("name");
                        String answerUid = (String) temp.get("uid");
                        Answer answer = new Answer(answerBody, answerName, answerUid, (String) key);
                        answerArrayList.add(answer);
                    }

                }
                mGenre = Integer.parseInt(mGenreString);

                Question question = new Question(title, body, name, uid, dataSnapshot.getKey(), mGenre, bytes, answerArrayList);
                mQuestionArrayList.add(question);
                mAdapter.notifyDataSetChanged();
                Log.d("body", question.toString());

        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }

    };

    private ChildEventListener mEventListener2 = new ChildEventListener() {
        int count = 0;
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Log.d("bbb", dataSnapshot.getValue().toString());

            HashMap map = (HashMap) dataSnapshot.getValue();

            if (map != null) {
                    count++;
                    Log.d("bbb", ""+count);
                    for (Object key : map.keySet()) {
                    mFavoriteValue = (String) map.get("uid");
                    mGenreString = (String) map.get("genre");
                    favoriteMap.add(new String[]{mGenreString, mFavoriteValue});
                        }
//                    if (count >= dataSnapshot.getChildrenCount()) {
//                        getQuestions();
//
//                }
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }

    };
    // --- ここまで追加する ---

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites_list);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(mToolbar);

        // ナビゲーションドロワーの設定
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, mToolbar, R.string.app_name, R.string.app_name);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.list_favorite) {
                    return true;
                } else if (id == R.id.nav_hobby) {
                    mToolbar.setTitle("趣味");
                    mGenre = 1;
                } else if (id == R.id.nav_life) {
                    mToolbar.setTitle("生活");
                    mGenre = 2;
                } else if (id == R.id.nav_health) {
                    mToolbar.setTitle("健康");
                    mGenre = 3;
                } else if (id == R.id.nav_compter) {
                    mToolbar.setTitle("コンピューター");
                    mGenre = 4;
                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);

                // 選択したジャンルにリスナーを登録する
                if (mGenre != 0) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("genre", mGenre);
                    startActivity(intent);
                }

                return true;
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            // ログインしていなければログイン画面に遷移させる
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        } else {

            // ListViewの準備
            mListView = (ListView) findViewById(R.id.listView);
            mAdapter = new FavoritesListAdapter(this);

            mAdapter.setQuestionArrayList(mQuestionArrayList);
            mListView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();

            mDatabaseReference = FirebaseDatabase.getInstance().getReference();
            mFavoriteRef = mDatabaseReference.child(Const.UsersPATH).child(user.getUid()).child(Const.FavoritesPATH);
            Log.d("aaa", mFavoriteRef.toString());
            mFavoriteRef.addChildEventListener(mEventListener2);
        }
    }

//    public void getQuestions() {
//        Log.d("aaa", "aaaa");
//        for (int i = 0; i < favoriteMap.size(); i++) {
//            DatabaseReference mQuestionUidRef = mDatabaseReference.child(Const.ContentsPATH).child(favoriteMap.get(i)[0]).child(favoriteMap.get(i)[1]);
//            mQuestionUidRef.addChildEventListener(mEventListener);
//        }
//    }

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
