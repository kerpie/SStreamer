package co.herovitamin.sstreamer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import co.herovitamin.sstreamer.R;
import co.herovitamin.sstreamer.models.MyMiniTrack;

public class TopTrackAdapter extends ArrayAdapter<MyMiniTrack> {

    public TopTrackAdapter(Context context, int resource, List<MyMiniTrack> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_top_track, parent, false);
        }
        else{
            view = convertView;
        }

        ((TextView) view.findViewById(R.id.top_track_name)).setText(getItem(position).getTrack_name());
        ((TextView) view.findViewById(R.id.top_track_album_name)).setText(getItem(position).getTrack_album());
        Picasso.with(getContext()).load(getItem(position).getTrack_image_url()).into(((ImageView) view.findViewById(R.id.top_track_image)));

        return view;
    }
}
