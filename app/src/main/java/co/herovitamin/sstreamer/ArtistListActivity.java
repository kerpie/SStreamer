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

/**
 * An activity representing a list of Artists. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link TopTracksActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link ArtistListFragment} and the item details
 * (if present) is a {@link TopTracksFragment}.
 * <p/>
 * This activity also implements the required
 * {@link ArtistListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class ArtistListActivity extends AppCompatActivity implements ArtistListFragment.Callbacks, OnArtistSearchDone {

    public static final String LOG_TAG = ArtistListActivity.class.getSimpleName();
    public static final String SEARCH_RESULT = "searchResult";

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
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
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((ArtistListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.artist_list))
                    .setActivateOnItemClick(true);
        }
    }

    /**
     * Callback method from {@link ArtistListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
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
