package co.herovitamin.sstreamer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Collection;
import java.util.List;

import co.herovitamin.sstreamer.R;
import co.herovitamin.sstreamer.models.MyMiniArtist;

public class ArtistAdapter extends ArrayAdapter<MyMiniArtist> {

    public static final String LOG_TAG = ArtistAdapter.class.getSimpleName();

    public ArtistAdapter(Context context, int resource, List<MyMiniArtist> objects) {
        super(context, resource, objects);
        super.addAll(objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_artist, parent, false);
        }
        else{
            view = convertView;
        }

        ((TextView) view.findViewById(R.id.artist_name)).setText(getItem(position).getName());
        Picasso.with(getContext()).load(getItem(position).getImage_url()).into(((ImageView) view.findViewById(R.id.artist_image)));

        return view;
    }
}
