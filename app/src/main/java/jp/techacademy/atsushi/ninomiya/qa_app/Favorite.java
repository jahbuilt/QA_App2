package jp.techacademy.atsushi.ninomiya.qa_app;

/**
 * Created by ninomiyaatsushi on 2016/10/23.
 */

import java.io.Serializable;

public class Favorite implements Serializable {
    public String mFavoriteKey;
    public String mFavoriteValue;

    public Favorite(String favoriteKey, String favoriteValue) {
        mFavoriteKey = favoriteKey;
        mFavoriteValue = favoriteValue;
    }

    @Override
    public String toString() {
        return mFavoriteKey + mFavoriteValue;
    }


    }
