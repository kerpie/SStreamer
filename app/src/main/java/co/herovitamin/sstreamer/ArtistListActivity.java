package co.herovitamin.sstreamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

import co.herovitamin.sstreamer.fragments.TopTracksFragment;
import co.herovitamin.sstreamer.fragments.ArtistListFragment;
import co.herovitamin.sstreamer.interfaces.OnArtistSearchDone;
import co.herovitamin.sstreamer.models.MyMiniArtist;

public class ArtistListActivity extends AppCompatActivity implements ArtistListFragment.Callbacks, OnArtistSearchDone {

    public static final String LOG_TAG = ArtistListActivity.class.getSimpleName();
    public static final String SEARCH_RESULT = "searchResult";

    public static boolean mTwoPane;

    ArtistListFragment artist_list;
    ArrayList<MyMiniArtist> searchResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_app_bar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle(getTitle());

        if (findViewById(R.id.artist_detail_container) != null) {

            mTwoPane = true;

            ((ArtistListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.artist_list))
                    .setActivateOnItemClick(true);
        }
    }

    @Override
    public void onItemSelected(MyMiniArtist artist) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putString(TopTracksFragment.ARG_ITEM_ID, artist.getId());
            arguments.putString(TopTracksFragment.ARG_ITEM_NAME, artist.getName());
            arguments.putString(TopTracksFragment.ARG_ITEM_IMAGE, artist.getImage_url());
            TopTracksFragment fragment = new TopTracksFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.artist_detail_container, fragment)
                    .commit();

        } else {
            Intent detailIntent = new Intent(this, TopTracksActivity.class);
            detailIntent.putExtra(TopTracksFragment.ARG_ITEM_ID, artist.getId());
            detailIntent.putExtra(TopTracksFragment.ARG_ITEM_NAME, artist.getName());
            detailIntent.putExtra(TopTracksFragment.ARG_ITEM_IMAGE, artist.getImage_url());
            startActivity(detailIntent);
        }
    }

    @Override
    public void prepareStuffBeforeSearchForArtist() {
        ((ArtistListFragment) getSupportFragmentManager().findFragmentById(R.id.artist_list)).prepareToSearch();
    }

    @Override
    public void onSearchDone(ArrayList<MyMiniArtist> artists) {
        searchResult = artists;
        ((ArtistListFragment) getSupportFragmentManager().findFragmentById(R.id.artist_list)).onSearchDone(artists);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SEARCH_RESULT, searchResult);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null) {
            searchResult = savedInstanceState.getParcelableArrayList(SEARCH_RESULT);
            if (searchResult != null) {
                ((ArtistListFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.artist_list))
                        .onSearchDone(searchResult);
            }
        }
    }
}
