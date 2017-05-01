package app.it.hueic.mp3player.adapter;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import app.it.hueic.mp3player.R;
import app.it.hueic.mp3player.model.SongModel;

/**
 * Created by kenhoang on 5/1/17.
 */

public class ListSongAdapter extends ArrayAdapter<SongModel> {
    Activity context;
    int resource;
    List<SongModel> objects;
    public ListSongAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<SongModel> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View rowView = layoutInflater.inflate(R.layout.item_list_song, parent, false);
        TextView tvNameSong = (TextView) rowView.findViewById(R.id.tvNameSong);
        tvNameSong.setText(objects.get(position).title);
        return rowView;
    }
}
