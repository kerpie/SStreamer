package co.herovitamin.sstreamer.interfaces;

import java.util.ArrayList;

import co.herovitamin.sstreamer.models.MyMiniTrack;

public interface OnTopTracksSearchDone {
    void onPrepareStuff();
    void onTaskDone(ArrayList<MyMiniTrack> top_tracks);
}
