package co.herovitamin.sstreamer.services;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.util.ArrayList;

import co.herovitamin.sstreamer.fragments.TopTracksFragment;
import co.herovitamin.sstreamer.fragments.PlayerFragment;
import co.herovitamin.sstreamer.models.MyMiniTrack;

/**
 * Created by kerry on 4/11/15.
 */
public class PlayerService extends Service implements
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnSeekCompleteListener{

    ArrayList<MyMiniTrack> top_tracks;
    int current_position;

    MediaPlayer media_player = null;

    IBinder binder = new MusicBinder();

    PlayerFragment fragment = null  ;

    @Override
    public void onCreate() {
        super.onCreate();
        media_player = new MediaPlayer();
        media_player.setOnPreparedListener(this);
        media_player.setOnCompletionListener(this);
        media_player.setOnErrorListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        update_states(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    public void update_states(Intent intent){
        init_player();
        set_current_position(intent.getIntExtra(TopTracksFragment.CHOOSEN_TRACK_POSITION, 0));
        set_top_tracks(intent.<MyMiniTrack>getParcelableArrayListExtra(TopTracksFragment.ARG_ITEMS_TRACKS));
    }

    public void init_player(){
        if(media_player == null){
            media_player = new MediaPlayer();
        }
    }

    public void set_top_tracks(ArrayList<MyMiniTrack> top_tracks){
        this.top_tracks = top_tracks;
    }

    public void set_current_position(int current_position){
        this.current_position = current_position;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        update_states(intent);
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        media_player.stop();
        media_player.release();
        media_player = null;
        return super.onUnbind(intent);
    }

    public boolean is_playing() {
        return media_player.isPlaying();
    }

    public void play_track() {
        try {
            if (is_playing())
                stop_track();
            media_player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            media_player.setDataSource(top_tracks.get(current_position).get_track_preview_url());
            media_player.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void play_or_pause_track() {
        if(is_playing())
            media_player.pause();
        else
            media_player.start();
        if(fragment != null){
            fragment.update_seek_bar();
        }
    }

    public int play_next(){
        current_position++;
        if(current_position >= top_tracks.size()){
            stop_track();
            current_position = -1;
        }
        else {
            stop_track();
            play_track();
        }
        return current_position;
    }

    public int play_previous() {
        current_position--;
        if (current_position <= 0){
            stop_track();
            current_position = -1;
        }
        else{
            stop_track();
            play_track();
        }
        return current_position;
    }

    public void stop_track(){
        media_player.stop();
        media_player.reset();
    }

    public int get_track_progress(){
        return media_player.getCurrentPosition();
    }

    public int get_track_duration(){
        return media_player.getDuration();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        if(fragment != null){
            fragment.update_play_pause_button_image();
            fragment.init_seek_bar(mp.getDuration());
            fragment.update_seek_bar();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        play_next();
        if(fragment != null){
            fragment.start_playing(current_position);
        }
    }

    public void register_fragment(PlayerFragment fragment){
        this.fragment = fragment;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        if(fragment != null){
            fragment.set_progress(mp.getCurrentPosition());
        }
    }

    public void seek_to(int progress) {
        if(media_player != null){
            media_player.seekTo(progress);
        }
    }

    public class MusicBinder extends Binder{

        public PlayerService get_service(){
            return PlayerService.this;
        }

    }
}