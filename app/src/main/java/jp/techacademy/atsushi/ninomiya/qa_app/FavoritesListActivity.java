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
    private FavoritesListAdapter mAdapter;

    private DatabaseReference mFavoriteRef;
    private FirebaseUser user;
    private String mFavoriteQuestion;
    private String mFavoriteGenre;

    private DatabaseReference mGenreRef;
//    private DatabaseReference mGenreRef1;
//    private DatabaseReference mGenreRef2;
//    private DatabaseReference mGenreRef3;
//    private DatabaseReference mGenreRef4;

    private boolean isFinishFavorite;
    private boolean isFinishQuestion1;
    private boolean isFinishQuestion2;
    private boolean isFinishQuestion3;
    private boolean isFinishQuestion4;

    HashMap<String, Question> mQuestionMap = new HashMap<String, Question>();
    private List<String[]> favoriteMap = new ArrayList<String[]>();

    private ChildEventListener mQuestionEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot parentDataSnapshot, String s) {
            if (!parentDataSnapshot.getKey().equals("contents")) {
                return;
            }

            Iterable<DataSnapshot> genreIterable = parentDataSnapshot.getChildren();

            for (DataSnapshot childrenDataSnapshot : genreIterable) {
                Iterable<DataSnapshot> childrenIterable = childrenDataSnapshot.getChildren();

                for (DataSnapshot dataSnapshot : childrenIterable) {

                    String questionId = (String) dataSnapshot.getKey();
                    HashMap map = (HashMap) dataSnapshot.getValue();

                    String title = (String) map.get("title");
                    String body = (String) map.get("body");
                    String name = (String) map.get("name");
                    String uid = (String) map.get("uid");
                    String imageString = (String) map.get("image");
                    Bitmap image = null;
                    byte[] bytes;

                    Log.d("imageString", imageString);

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

                    Question question = new Question(title, body, name, uid, dataSnapshot.getKey(), 1, bytes, answerArrayList);
                    mQuestionMap.put(questionId, question);
                }

            }
            isFinishQuestion1 = true;
            if (isFinishFavorite) {
                matchItems();
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

//    private ChildEventListener mEventListener1 = new ChildEventListener() {
//        int count = 0;
//        @Override
//        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//
//            String questionId = (String) dataSnapshot.getKey();
//            HashMap map = (HashMap) dataSnapshot.getValue();
//
//            String title = (String) map.get("title");
//            String body = (String) map.get("body");
//            String name = (String) map.get("name");
//            String uid = (String) map.get("uid");
//            String imageString = (String) map.get("image");
//            Bitmap image = null;
//            byte[] bytes;
//
//            if (imageString != null) {
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                bytes = Base64.decode(imageString, Base64.DEFAULT);
//            } else {
//                bytes = new byte[0];
//            }
//
//            ArrayList<Answer> answerArrayList = new ArrayList<Answer>();
//            HashMap answerMap = (HashMap) map.get("answers");
//            if (answerMap != null) {
//                for (Object key : answerMap.keySet()) {
//                    HashMap temp = (HashMap) answerMap.get((String) key);
//                    String answerBody = (String) temp.get("body");
//                    String answerName = (String) temp.get("name");
//                    String answerUid = (String) temp.get("uid");
//                    Answer answer = new Answer(answerBody, answerName, answerUid, (String) key);
//                    answerArrayList.add(answer);
//                }
//            }
//
//            Question question = new Question(title, body, name, uid, dataSnapshot.getKey(), 1, bytes, answerArrayList);
//            mQuestionMap.put(questionId, question);
//            count++;
//
//            if (!dataSnapshot.exists()) {
//                isFinishQuestion1 = true;
//            }
//            if (count == ) {
//                isFinishQuestion1 = true;
//                if (isFinishFavorite && isFinishQuestion2 && isFinishQuestion3 && isFinishQuestion4) {
//                    matchItems();
//                }
//            }
//        }
//
//        @Override
//        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//        }
//
//        @Override
//        public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//        }
//
//        @Override
//        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//        }
//
//        @Override
//        public void onCancelled(DatabaseError databaseError) {
//
//        }
//
//    };
//
//    private ChildEventListener mEventListener2 = new ChildEventListener() {
//        int count = 0;
//        @Override
//        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//
//            String questionId = (String) dataSnapshot.getKey();
//            HashMap map = (HashMap) dataSnapshot.getValue();
//
//            String title = (String) map.get("title");
//            String body = (String) map.get("body");
//            String name = (String) map.get("name");
//            String uid = (String) map.get("uid");
//            String imageString = (String) map.get("image");
//            Bitmap image = null;
//            byte[] bytes;
//
//            if (imageString != null) {
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                bytes = Base64.decode(imageString, Base64.DEFAULT);
//            } else {
//                bytes = new byte[0];
//            }
//
//            ArrayList<Answer> answerArrayList = new ArrayList<Answer>();
//            HashMap answerMap = (HashMap) map.get("answers");
//            if (answerMap != null) {
//                for (Object key : answerMap.keySet()) {
//                    HashMap temp = (HashMap) answerMap.get((String) key);
//                    String answerBody = (String) temp.get("body");
//                    String answerName = (String) temp.get("name");
//                    String answerUid = (String) temp.get("uid");
//                    Answer answer = new Answer(answerBody, answerName, answerUid, (String) key);
//                    answerArrayList.add(answer);
//                }
//            }
//
//            Question question = new Question(title, body, name, uid, dataSnapshot.getKey(), 2, bytes, answerArrayList);
//            mQuestionMap.put(questionId, question);
//            count++;
//
//            if (!dataSnapshot.exists()) {
//                isFinishQuestion2 = true;
//            }
//            if (count == dataSnapshot.getChildrenCount()) {
//                isFinishQuestion2 = true;
//                if (isFinishFavorite && isFinishQuestion1 && isFinishQuestion3 && isFinishQuestion4) {
//                    matchItems();
//                }
//            }
//        }
//
//        @Override
//        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//        }
//
//        @Override
//        public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//        }
//
//        @Override
//        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//        }
//
//        @Override
//        public void onCancelled(DatabaseError databaseError) {
//
//        }
//
//    };
//
//    private ChildEventListener mEventListener3 = new ChildEventListener() {
//        int count = 0;
//        @Override
//        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//
//            String questionId = (String) dataSnapshot.getKey();
//            HashMap map = (HashMap) dataSnapshot.getValue();
//
//            String title = (String) map.get("title");
//            String body = (String) map.get("body");
//            String name = (String) map.get("name");
//            String uid = (String) map.get("uid");
//            String imageString = (String) map.get("image");
//            Bitmap image = null;
//            byte[] bytes;
//
//            if (imageString != null) {
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                bytes = Base64.decode(imageString, Base64.DEFAULT);
//            } else {
//                bytes = new byte[0];
//            }
//
//            ArrayList<Answer> answerArrayList = new ArrayList<Answer>();
//            HashMap answerMap = (HashMap) map.get("answers");
//            if (answerMap != null) {
//                for (Object key : answerMap.keySet()) {
//                    HashMap temp = (HashMap) answerMap.get((String) key);
//                    String answerBody = (String) temp.get("body");
//                    String answerName = (String) temp.get("name");
//                    String answerUid = (String) temp.get("uid");
//                    Answer answer = new Answer(answerBody, answerName, answerUid, (String) key);
//                    answerArrayList.add(answer);
//                }
//            }
//
//            Question question = new Question(title, body, name, uid, dataSnapshot.getKey(), 3, bytes, answerArrayList);
//            mQuestionMap.put(questionId, question);
//            count++;
//
//            if (!dataSnapshot.exists()) {
//                isFinishQuestion3 = true;
//            }
//
//            if (count == dataSnapshot.getChildrenCount()) {
//                isFinishQuestion3 = true;
//                if (isFinishFavorite && isFinishQuestion1 && isFinishQuestion2 && isFinishQuestion4) {
//                    matchItems();
//                }
//            }
//        }
//
//        @Override
//        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//        }
//
//        @Override
//        public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//        }
//
//        @Override
//        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//        }
//
//        @Override
//        public void onCancelled(DatabaseError databaseError) {
//
//        }
//
//    };
//
//    private ChildEventListener mEventListener4 = new ChildEventListener() {
//        int count = 0;
//        @Override
//        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//
//            String questionId = (String) dataSnapshot.getKey();
//            HashMap map = (HashMap) dataSnapshot.getValue();
//
//            String title = (String) map.get("title");
//            String body = (String) map.get("body");
//            String name = (String) map.get("name");
//            String uid = (String) map.get("uid");
//            String imageString = (String) map.get("image");
//            Bitmap image = null;
//            byte[] bytes;
//
//            if (imageString != null) {
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                bytes = Base64.decode(imageString, Base64.DEFAULT);
//            } else {
//                bytes = new byte[0];
//            }
//
//            ArrayList<Answer> answerArrayList = new ArrayList<Answer>();
//            HashMap answerMap = (HashMap) map.get("answers");
//            if (answerMap != null) {
//                for (Object key : answerMap.keySet()) {
//                    HashMap temp = (HashMap) answerMap.get((String) key);
//                    String answerBody = (String) temp.get("body");
//                    String answerName = (String) temp.get("name");
//                    String answerUid = (String) temp.get("uid");
//                    Answer answer = new Answer(answerBody, answerName, answerUid, (String) key);
//                    answerArrayList.add(answer);
//                }
//            }
//
//            Question question = new Question(title, body, name, uid, dataSnapshot.getKey(), 4, bytes, answerArrayList);
//            mQuestionMap.put(questionId, question);
//            count++;
//
//            if (!dataSnapshot.exists()) {
//                isFinishQuestion4 = true;
//            }
//
//            if (count == dataSnapshot.getChildrenCount()) {
//                isFinishQuestion4 = true;
//                if (isFinishFavorite && isFinishQuestion1 && isFinishQuestion2 && isFinishQuestion3) {
//                    matchItems();
//                }
//            }
//        }
//
//        @Override
//        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//        }
//
//        @Override
//        public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//        }
//
//        @Override
//        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//        }
//
//        @Override
//        public void onCancelled(DatabaseError databaseError) {
//
//        }
//
//    };

    private ChildEventListener mEventListener = new ChildEventListener() {
        int count = 0;

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            HashMap map = (HashMap) dataSnapshot.getValue();

            if (map != null) {

                mFavoriteQuestion = (String) map.get("uid");
                mFavoriteGenre = (String) map.get("genre");
                favoriteMap.add(new String[]{mFavoriteGenre, mFavoriteQuestion});

                count++;

                if (count == dataSnapshot.getChildrenCount()) {
                    // getQuestions();
                    isFinishFavorite = true;

                    if (isFinishQuestion1 && isFinishQuestion2 && isFinishQuestion3 && isFinishQuestion4) {
                        matchItems();
                    }
                }
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

            mDatabaseReference = FirebaseDatabase.getInstance().getReference();
            mFavoriteRef = mDatabaseReference.child(Const.UsersPATH).child(user.getUid()).child(Const.FavoritesPATH);
            Log.d("aaa", mFavoriteRef.toString());
            mFavoriteRef.addChildEventListener(mEventListener);

            mGenreRef = FirebaseDatabase.getInstance().getReference();
            mGenreRef.addChildEventListener(mQuestionEventListener);


//            mGenreRef1 = mDatabaseReference.child(Const.ContentsPATH).child("1");
//            mGenreRef1.addChildEventListener(mEventListener1);
//
//
//            mGenreRef2 = mDatabaseReference.child(Const.ContentsPATH).child("2");
//            mGenreRef2.addChildEventListener(mEventListener2);
//
//            mGenreRef3 = mDatabaseReference.child(Const.ContentsPATH).child("3");
//            mGenreRef3.addChildEventListener(mEventListener3);
//
//            mGenreRef4 = mDatabaseReference.child(Const.ContentsPATH).child("4");
//            mGenreRef4.addChildEventListener(mEventListener4);
        }
    }


    private void matchItems() {
        // 質問とお気に入りのIDチェック
        // マッチしたものをListViewに追加
        ArrayList<Question> matchedItems = new ArrayList<>();

        for (Object key : mQuestionMap.keySet()) {
            for (String[] pairs : favoriteMap) {
                if (key.equals(pairs[1])) {
                    matchedItems.add(mQuestionMap.get((String) key));
                }
            }
        }

        mAdapter.setQuestionArrayList(matchedItems);
        mListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

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
