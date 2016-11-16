package jp.techacademy.atsushi.ninomiya.qa_app;

/**
 * Created by ninomiyaatsushi on 2016/10/23.
 */

import java.io.Serializable;

public class Favorite implements Serializable {
    private String mQuestionUid;
    private int mGenre;
    private String mFavoriteKey;

    public Favorite(String questionUid, int genre, String favoriteKey) {
        mQuestionUid = questionUid;
        mGenre = genre;
        mFavoriteKey = favoriteKey;

    }

    public String getUid() {
        return mQuestionUid;
    }


    public int getGerre() {
        return mGenre;
    }
}