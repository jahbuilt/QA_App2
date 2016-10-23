package jp.techacademy.atsushi.ninomiya.qa_app;

/**
 * Created by ninomiyaatsushi on 2016/10/23.
 */

import java.io.Serializable;

public class Favorite implements Serializable {
    private String mFavoriteUid;
    private String mQuestionUid;;

    public Favorite(String favoriteUid, String questionUid) {
        mFavoriteUid = favoriteUid;
        mQuestionUid = questionUid;
    }

    public String getFavoriteUid() {
        return mFavoriteUid;
    }

    public String getQuestionUid() {
        return mQuestionUid;
    }
}