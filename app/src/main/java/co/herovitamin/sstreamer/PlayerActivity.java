package co.herovitamin.sstreamer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import co.herovitamin.sstreamer.fragments.TopTracksFragment;
import co.herovitamin.sstreamer.fragments.PlayerFragment;

public class PlayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        if(savedInstanceState == null){

            PlayerFragment player_fragment = PlayerFragment.newInstance(
                    getIntent().getIntExtra(TopTracksFragment.CHOOSEN_TRACK_POSITION, -1),
                    getIntent().getParcelableArrayListExtra(TopTracksFragment.ARG_ITEMS_TRACKS));

            getSupportFragmentManager().beginTransaction().replace(R.id.player_container, player_fragment).commit();
        }

    }

}
