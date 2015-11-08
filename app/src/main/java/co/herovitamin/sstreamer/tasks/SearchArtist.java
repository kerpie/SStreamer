package co.herovitamin.sstreamer.tasks;

import android.os.AsyncTask;

import java.util.ArrayList;

import co.herovitamin.sstreamer.interfaces.OnArtistSearchDone;
import co.herovitamin.sstreamer.models.MyMiniArtist;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;

/**
 * Created by kerry on 6/10/15.
 */
public class SearchArtist extends AsyncTask<Void, Integer, ArrayList<MyMiniArtist>>{

    ArtistsPager results;
    String artist_name;
    OnArtistSearchDone listener;

    public SearchArtist(OnArtistSearchDone listener, String artist_name){
        this.listener = listener;
        this.artist_name = artist_name;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.prepareStuffBeforeSearchForArtist();
    }

    @Override
    protected ArrayList<MyMiniArtist> doInBackground(Void... params) {

        SpotifyApi api = new SpotifyApi();

        SpotifyService spotify = api.getService();

        results = spotify.searchArtists(artist_name);
        ArrayList<MyMiniArtist> artists = null;
        if(results != null && results.artists.items.size() > 0) {
            artists = new ArrayList<MyMiniArtist>();
            for (Artist tmp_artist:results.artists.items){
                String tmp_url = tmp_artist.images.size() > 0 ? tmp_artist.images.get(0).url : null ;
                artists.add(new MyMiniArtist(tmp_artist.id, tmp_artist.name, tmp_url));
            }
        }

        return artists;
    }

    @Override
    protected void onPostExecute(ArrayList<MyMiniArtist> artists) {
        super.onPostExecute(artists);
        listener.onSearchDone(artists);
    }
}
