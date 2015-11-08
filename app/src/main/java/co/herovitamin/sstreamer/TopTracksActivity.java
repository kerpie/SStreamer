package co.herovitamin.sstreamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.herovitamin.sstreamer.fragments.TopTracksFragment;

public class TopTracksActivity extends AppCompatActivity{

    @Bind(R.id.artist_top_tracks_toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_tracks);

        ButterKnife.bind(this);

        toolbar.setTitle(getIntent().getStringExtra(TopTracksFragment.ARG_ITEM_NAME) + getString(R.string.title_artist_detail));
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putString(TopTracksFragment.ARG_ITEM_ID, getIntent().getStringExtra(TopTracksFragment.ARG_ITEM_ID));
            arguments.putString(TopTracksFragment.ARG_ITEM_NAME, getIntent().getStringExtra(TopTracksFragment.ARG_ITEM_NAME));
            arguments.putString(TopTracksFragment.ARG_ITEM_IMAGE, getIntent().getStringExtra(TopTracksFragment.ARG_ITEM_IMAGE));

            TopTracksFragment fragment = new TopTracksFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_container, fragment)
                    .commit();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, ArtistListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
