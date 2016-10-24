package jp.techacademy.atsushi.ninomiya.qa_app;

/**
 * Created by ninomiyaatsushi on 2016/10/18.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class FavoritesListAdapter extends BaseAdapter{
    private LayoutInflater mLayoutInflater = null;
    private ArrayList<Favorite> mFavoriteArrayList;

    public FavoritesListAdapter(Context context) {
        mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mFavoriteArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return mFavoriteArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_favorites, parent, false);
        }

        TextView titleText = (TextView) convertView.findViewById(R.id.titleTextView);
        titleText.setText(mFavoriteArrayList.get(position).getQuestionUid());


        return convertView;
    }

    public void setmFavoriteArrayList(ArrayList<Favorite> favoriteArrayList) {
        mFavoriteArrayList = favoriteArrayList;
    }
}