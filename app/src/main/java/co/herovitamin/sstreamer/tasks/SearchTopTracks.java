package co.herovitamin.sstreamer.tasks;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import co.herovitamin.sstreamer.interfaces.OnTopTracksSearchDone;
import co.herovitamin.sstreamer.models.MyMiniTrack;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import retrofit.RetrofitError;

public class SearchTopTracks extends AsyncTask<Void, Integer, ArrayList<MyMiniTrack>> {

    public static final String LOG_TAG = SearchTopTracks.class.getSimpleName();

    OnTopTracksSearchDone listener;
    String artistId;
    List<Track> tracks_result;
    private String artist_name;

    public SearchTopTracks(OnTopTracksSearchDone listener, String artistId, String artist_name){
        this.listener = listener;
        this.artistId = artistId;
        this.artist_name = artist_name;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onPrepareStuff();
    }

    @Override
    protected ArrayList<MyMiniTrack> doInBackground(Void... params) {
        SpotifyApi api = new SpotifyApi();
        SpotifyService spotify = api.getService();
        Map<String, Object> options =new Hashtable<>();
        options.put("country", "US");

        ArrayList<MyMiniTrack> top_tracks = new ArrayList<MyMiniTrack>();

        try {
            tracks_result = spotify.getArtistTopTrack(artistId, options).tracks;
        } catch (RetrofitError error){
            tracks_result = null;
        }
        if(tracks_result != null && tracks_result.size() > 0){
            for (Track track:tracks_result){
                String tmp_url = track.album.images.size() > 0 ? track.album.images.get(0).url : null;
                top_tracks.add(
                        new MyMiniTrack(
                                track.name,
                                track.album.name,
                                artist_name,
                                tmp_url,
                                track.preview_url,
                                track.duration_ms));
            }
        }
        return top_tracks;
    }

    @Override
    protected void onPostExecute(ArrayList<MyMiniTrack> top_tracks) {
        super.onPostExecute(top_tracks);
        listener.onTaskDone(top_tracks);
    }
}
