package co.herovitamin.sstreamer.interfaces;

import java.util.ArrayList;

import co.herovitamin.sstreamer.models.MyMiniArtist;

public interface OnArtistSearchDone {

    void prepareStuffBeforeSearchForArtist();
    void onSearchDone(ArrayList<MyMiniArtist> artists);

}