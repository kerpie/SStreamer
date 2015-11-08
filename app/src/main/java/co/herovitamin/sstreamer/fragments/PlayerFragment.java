package co.herovitamin.sstreamer.fragments;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.herovitamin.sstreamer.R;
import co.herovitamin.sstreamer.interfaces.PlayerCallback;
import co.herovitamin.sstreamer.models.MyMiniTrack;
import co.herovitamin.sstreamer.services.PlayerService;

public class PlayerFragment extends DialogFragment implements PlayerCallback{

    public static final String LOG_TAG = PlayerFragment.class.getSimpleName();
    public static final String SHOULD_KEEP_PLAYING = "should_keep_playing";

    ArrayList<MyMiniTrack> top_tracks;
    int selected_position = 0;
    PlayerService player_service;
    boolean is_connected_to_service;
    Intent service_intent = null;

    @Bind(R.id.artist_name)
    TextView artist_name;
    @Bind(R.id.album_name)
    TextView album_name;
    @Bind(R.id.album_image)
    ImageView album_image;
    @Bind(R.id.song_name)
    TextView song_name;
    @Bind(R.id.song_progress)
    SeekBar song_progress;
    @Bind(R.id.song_initial_time)
    TextView song_initial_time;
    @Bind(R.id.song_final_time)
    TextView song_final_time;
    @Bind(R.id.song_previous)
    ImageButton song_previous;
    @Bind(R.id.song_play_pause)
    ImageButton song_play_pause;
    @Bind(R.id.song_next)
    ImageButton song_next;

    MediaPlayer media_player;

    Handler seek_handler = new Handler();

    PlayerService.MusicBinder binder;

    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (PlayerService.MusicBinder) service;
            player_service = binder.get_service();
            player_service.set_current_position(selected_position);
            player_service.set_top_tracks(top_tracks);
            player_service.init_player();
            player_service.register_fragment(PlayerFragment.this);
            if(is_connected_to_service){
                init_seek_bar(player_service.get_track_duration());
                player_service.play_or_pause_track();
            }
            else
                player_service.play_track();
            is_connected_to_service = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            is_connected_to_service = false;
        }
    };

    Runnable run = new Runnable() {
        @Override
        public void run() {
            update_seek_bar();
        }
    };

    public void update_seek_bar() {
        if(is_connected_to_service){
            song_progress.setProgress(player_service.get_track_progress());
            song_initial_time.setText(format_time(player_service.get_track_progress()));
            seek_handler.postDelayed(run, 1000);
        }
    }

    public static PlayerFragment newInstance(int selected_position, ArrayList<? extends Parcelable> top_tracks) {

        Bundle args = new Bundle();
        PlayerFragment fragment = new PlayerFragment();
        args.putInt(TopTracksFragment.CHOOSEN_TRACK_POSITION, selected_position);
        args.putParcelableArrayList(TopTracksFragment.ARG_ITEMS_TRACKS, top_tracks);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null) {
            selected_position = getArguments().getInt(TopTracksFragment.CHOOSEN_TRACK_POSITION);
            top_tracks = getArguments().getParcelableArrayList(TopTracksFragment.ARG_ITEMS_TRACKS);
        }
        else{
            selected_position = savedInstanceState.getInt(TopTracksFragment.CHOOSEN_TRACK_POSITION);
            top_tracks = savedInstanceState.getParcelableArrayList(TopTracksFragment.ARG_ITEMS_TRACKS);
            is_connected_to_service = savedInstanceState.getBoolean(SHOULD_KEEP_PLAYING, false);
            if(is_connected_to_service){
                if(service_intent == null){
                    service_intent = new Intent(getContext(), PlayerService.class);
                    getActivity().bindService(service_intent, connection, Context.BIND_AUTO_CREATE);
                }
            }
        }

        if(service_intent == null){
            service_intent = new Intent(getContext(), PlayerService.class);
            getActivity().bindService(service_intent, connection, Context.BIND_AUTO_CREATE);
        }

        media_player = new MediaPlayer();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(TopTracksFragment.CHOOSEN_TRACK_POSITION, selected_position);
        outState.putParcelableArrayList(TopTracksFragment.ARG_ITEMS_TRACKS, top_tracks);
        if(is_connected_to_service){
            outState.putBoolean(SHOULD_KEEP_PLAYING, is_connected_to_service);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        update_track_information(selected_position);

        set_time_for_songs();
        set_listeners();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(is_connected_to_service){
            if(player_service.is_playing()){
                player_service.play_or_pause_track();
            }
            else{
                player_service.stop_track();
                getActivity().unbindService(connection);
            }
            is_connected_to_service = false;
        }
    }

    private void set_time_for_songs() {
        song_final_time.setText(format_time(top_tracks.get(selected_position).get_track_duration()));
        song_initial_time.setText(format_time(0));
    }

    private String format_time(long progress){
        return String.format("%d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(progress),
                TimeUnit.MILLISECONDS.toSeconds(progress) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(progress)));
    }

    private void set_listeners() {
        song_play_pause.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                player_service.play_or_pause_track();
                update_play_pause_button_image();
            }
        });
        song_previous.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                update_track_information(player_service.play_previous());
            }
        });
        song_next.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                update_track_information(player_service.play_next());
            }
        });
    }

    private void update_track_information(int new_position) {
        if(new_position != -1){
            this.selected_position = new_position;
            artist_name.setText(top_tracks.get(selected_position).getTrack_artist_name());
            album_name.setText(top_tracks.get(selected_position).getTrack_album());
            Picasso.with(getContext()).load(top_tracks.get(Integer.valueOf(selected_position)).getTrack_image_url()).into(album_image);
            song_name.setText(top_tracks.get(selected_position).getTrack_name());
        }
        else{
            dismiss();
        }
    }

    public void update_play_pause_button_image() {
        if(player_service.is_playing()){
            song_play_pause.setImageResource(android.R.drawable.ic_media_pause);
        }
        else{
            song_play_pause.setImageResource(android.R.drawable.ic_media_play);
        }
    }

    @Override
    public void set_progress() {

    }

    @Override
    public void start_playing(int position) {
        update_track_information(position);
    }

    public void init_seek_bar(int duration) {
        song_final_time.setText(format_time(duration));
        song_progress.setMax(duration);
    }
}