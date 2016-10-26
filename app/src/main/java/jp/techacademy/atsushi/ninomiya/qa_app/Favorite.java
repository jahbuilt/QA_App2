package jp.techacademy.atsushi.ninomiya.qa_app;

/**
 * Created by ninomiyaatsushi on 2016/10/23.
 */

import java.io.Serializable;

public class Favorite implements Serializable {
    public String mFavoriteUid;
    public String mQuestionUid;

    public Favorite(String favoriteUid, String questionUid) {
        mFavoriteUid = favoriteUid;
        mQuestionUid = questionUid;
    }

    @Override
    public String toString() {
        return mFavoriteUid + mQuestionUid;
    }


    }
