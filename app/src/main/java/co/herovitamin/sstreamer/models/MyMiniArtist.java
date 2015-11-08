package co.herovitamin.sstreamer.models;

import android.os.Parcel;
import android.os.Parcelable;

public class MyMiniArtist implements Parcelable{

    private String id;
    private String name;
    private String imageUrl;

    public MyMiniArtist(String id, String name, String imageUrl){
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    protected MyMiniArtist(Parcel in) {
        id = in.readString();
        name = in.readString();
        imageUrl = in.readString();
    }

    public static final Creator<MyMiniArtist> CREATOR = new Creator<MyMiniArtist>() {
        @Override
        public MyMiniArtist createFromParcel(Parcel in) {
            return new MyMiniArtist(in);
        }

        @Override
        public MyMiniArtist[] newArray(int size) {
            return new MyMiniArtist[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_url() {
        return imageUrl;
    }

    public void setImage_url(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(imageUrl);
    }
}