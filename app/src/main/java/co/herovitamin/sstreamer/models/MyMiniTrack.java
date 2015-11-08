package co.herovitamin.sstreamer.models;

import android.os.Parcel;
import android.os.Parcelable;

public class MyMiniTrack implements Parcelable{

    String track_name;
    String track_album;
    String track_artist_name;
    String track_image_url;
    String track_preview_url;

    long track_duration;

    public MyMiniTrack(String track_name, String track_album, String track_artist_name, String track_image_url, String track_preview_url, long track_duration){
        this.track_name = track_name;
        this.track_album = track_album;
        this.track_artist_name = track_artist_name;
        this.track_image_url = track_image_url;
        this.track_preview_url = track_preview_url;
        this.track_duration = track_duration;
    }

    public MyMiniTrack(Parcel in){
        track_name = in.readString();
        track_album = in.readString();
        track_artist_name = in.readString();
        track_image_url = in.readString();
        track_preview_url = in.readString();
        track_duration = in.readLong();
    }

    public String getTrack_image_url() {
        return track_image_url;
    }

    public void setTrack_image_url(String track_image_url) {
        this.track_image_url = track_image_url;
    }

    public String getTrack_name() {
        return track_name;
    }

    public void setTrack_name(String track_name) {
        this.track_name = track_name;
    }

    public String getTrack_album() {
        return track_album;
    }

    public void setTrack_album(String track_album) {
        this.track_album = track_album;
    }

    public String getTrack_artist_name() {
        return track_artist_name;
    }

    public void setTrack_artist_name(String track_artist_name) {
        this.track_artist_name = track_artist_name;
    }

    public String get_track_preview_url() {
        return track_preview_url;
    }

    public void set_track_preview_url(String track_preview_url) {
        this.track_preview_url = track_preview_url;
    }

    public long get_track_duration() {
        return track_duration;
    }

    public void set_track_duration(long track_duration) {
        this.track_duration = track_duration;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(track_name);
        dest.writeString(track_album);
        dest.writeString(track_artist_name);
        dest.writeString(track_image_url);
        dest.writeString(track_preview_url);
        dest.writeLong(track_duration);
    }

    public static final Creator<MyMiniTrack> CREATOR = new Creator<MyMiniTrack>() {
        @Override
        public MyMiniTrack createFromParcel(Parcel in) {
            return new MyMiniTrack(in);
        }

        @Override
        public MyMiniTrack[] newArray(int size) {
            return new MyMiniTrack[size];
        }
    };
}
