package co.herovitamin.sstreamer.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import co.herovitamin.sstreamer.TopTracksActivity;
import co.herovitamin.sstreamer.ArtistListActivity;
import co.herovitamin.sstreamer.PlayerActivity;
import co.herovitamin.sstreamer.R;
import co.herovitamin.sstreamer.adapters.TopTrackAdapter;
import co.herovitamin.sstreamer.interfaces.OnTopTracksSearchDone;
import co.herovitamin.sstreamer.models.MyMiniTrack;
import co.herovitamin.sstreamer.tasks.SearchTopTracks;

/**
 * A fragment representing a single Artist detail screen.
 * This fragment is either contained in a {@link ArtistListActivity}
 * in two-pane mode (on tablets) or a {@link TopTracksActivity}
 * on handsets.
 */
public class TopTracksFragment extends ListFragment implements OnTopTracksSearchDone{

    public static final String LOG_TAG = TopTracksFragment.class.getSimpleName();

    public static final String ARG_ITEM_ID = "artist_id";
    public static final String ARG_ITEM_NAME = "artist_name";
    public static final String ARG_ITEM_IMAGE = "artist_image";
    public static final String ARG_ITEMS_TRACKS = "artist_top_tracks";

    public static final String CHOOSEN_TRACK_POSITION = "track_id";


    String mId;
    String mName;
    String mImage;

    TopTrackAdapter adapter;
    ArrayList<MyMiniTrack> top_tracks;

    public TopTracksFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){
            ArrayList<MyMiniTrack> result = savedInstanceState.getParcelableArrayList(ARG_ITEMS_TRACKS);
            onTaskDone(result);
        }
        else{
            if (getArguments().containsKey(ARG_ITEM_NAME)) {
                mId = getArguments().getString(ARG_ITEM_ID);
                mName = getArguments().getString(ARG_ITEM_NAME);
                mImage = getArguments().getString(ARG_ITEM_IMAGE);
                new SearchTopTracks((OnTopTracksSearchDone) this, mId, mName).execute();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_top_tracks, container, false);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if(ArtistListActivity.mTwoPane){
            PlayerFragment.newInstance(position, top_tracks).
                    show(getActivity().getSupportFragmentManager(), "dialog");
        }
        else {
            Intent player_intent = new Intent(getActivity(), PlayerActivity.class);
            player_intent.putParcelableArrayListExtra(ARG_ITEMS_TRACKS, top_tracks);
            player_intent.putExtra(CHOOSEN_TRACK_POSITION, position);
            getActivity().startActivity(player_intent);
        }
    }

    @Override
    public void onPrepareStuff() {
        //TODO: put snackbar to indicate searching
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(ARG_ITEMS_TRACKS, top_tracks);
    }

    @Override
    public void onTaskDone(ArrayList<MyMiniTrack> top_tracks) {
        if(top_tracks != null){

            this.top_tracks = top_tracks;

            if(getListAdapter() == null){
                adapter = new TopTrackAdapter(getContext(), R.layout.item_top_track, top_tracks);
                setListAdapter(adapter);
            }
            else{
                adapter.clear();
                adapter.addAll(top_tracks);
            }
            adapter.notifyDataSetChanged();
        }
        else{
            Snackbar.make(getListView(), R.string.searching_text, Snackbar.LENGTH_SHORT).show();
        }
    }
}
