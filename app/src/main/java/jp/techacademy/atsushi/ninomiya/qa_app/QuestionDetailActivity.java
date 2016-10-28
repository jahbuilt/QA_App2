package jp.techacademy.atsushi.ninomiya.qa_app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
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

public class QuestionDetailActivity extends AppCompatActivity implements View.OnClickListener, DatabaseReference.CompletionListener {

    private ListView mListView;
    private Question mQuestion;
    private String mQuestionUid;

    private QuestionDetailListAdapter mAdapter;
    private ImageButton mImageButton;
    private ProgressDialog mProgress;

    private DatabaseReference mDatabaseReference;
    private DatabaseReference mFavoriteRef;
    private FirebaseUser user;
    private String mFavoriteKey;
    private String mFavoriteValue;
    private ArrayList<Favorite> favoriteArrayList = new ArrayList<Favorite>();


    private ChildEventListener mEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            HashMap map = (HashMap) dataSnapshot.getValue();

            String answerUid = dataSnapshot.getKey();

            for (Answer answer : mQuestion.getAnswers()) {
                // 同じAnswerUidのものが存在しているときは何もしない
                if (answerUid.equals(answer.getAnswerUid())) {
                    return;
                }
            }

            String body = (String) map.get("body");
            String name = (String) map.get("name");
            String uid = (String) map.get("uid");

            Answer answer = new Answer(body, name, uid, answerUid);
            mQuestion.getAnswers().add(answer);
            mAdapter.notifyDataSetChanged();

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
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            mFavoriteValue = (String) dataSnapshot.getValue();
            Log.d("mFavoriteValue", mFavoriteValue);

            if (mFavoriteValue.equals(mQuestionUid)) {
                mImageButton.setImageResource(R.drawable.star_tapped);
                mFavoriteKey = (String) dataSnapshot.getKey();
                Log.d("mFavoriteKey", mFavoriteKey);
            }
            Favorite favorite = new Favorite(mFavoriteKey, mFavoriteValue);
            favoriteArrayList.add(favorite);
            Log.d("favoriteArrayList",favoriteArrayList.toString());
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            mImageButton.setImageResource(R.drawable.star_tapped);
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            mImageButton.setImageResource(R.drawable.star);
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detail);

        // 渡ってきたQuestionのオブジェクトを保持する
        Bundle extras = getIntent().getExtras();
        mQuestion = (Question) extras.get("question");
        setTitle(mQuestion.getTitle());
        mQuestionUid = mQuestion.getQuestionUid();


        // ListViewの準備
        mListView = (ListView) findViewById(R.id.listView);
        mAdapter = new QuestionDetailListAdapter(this, mQuestion);
        mListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        mProgress = new ProgressDialog(this);
        mProgress.setMessage("お気に入り更新中...");

        mImageButton = (ImageButton) findViewById(R.id.imageButton);
        mImageButton.setVisibility(View.INVISIBLE);

        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            // ログインしていなければログイン画面に遷移させる
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        } else {
            mImageButton.setVisibility(View.VISIBLE);
            mImageButton.setOnClickListener(this);

            mDatabaseReference = FirebaseDatabase.getInstance().getReference();
            mFavoriteRef = mDatabaseReference.child(Const.UsersPATH).child(user.getUid()).child(Const.FavoritesPATH);
            mFavoriteRef.addChildEventListener(mEventListener2);
        }



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ログイン済みのユーザーを収録する

                if (user == null) {
                    // ログインしていなければログイン画面に遷移させる
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    // Questionを渡して回答作成画面を起動する
                    // --- ここから ---
                    Intent intent = new Intent(getApplicationContext(), AnswerSendActivity.class);
                    intent.putExtra("question", mQuestion);
                    startActivity(intent);
                    // --- ここまで ---
                }
            }
        });
    }

    @Override
    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
        mProgress.dismiss();

        if (databaseError == null) {
            finish();
        } else {
            Snackbar.make(findViewById(android.R.id.content), "お気に入り更新に失敗しました", Snackbar.LENGTH_LONG).show();
        }

    }

    @Override
    public void onClick(View view) {
        // キーボードが出てたら閉じる
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFavoriteRef = mDatabaseReference.child(Const.UsersPATH).child(user.getUid()).child(Const.FavoritesPATH);

        if (mFavoriteKey == null) {
            mProgress.show();
            mFavoriteRef.push().setValue(mQuestionUid, this);
        } else {
            mProgress.show();
            mFavoriteRef.child(mFavoriteKey).removeValue(this);
        }

    }

}
