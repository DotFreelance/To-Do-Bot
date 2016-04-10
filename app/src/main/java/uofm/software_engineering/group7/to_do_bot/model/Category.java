package uofm.software_engineering.group7.to_do_bot.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Category implements Parcelable {
    private long mId;
    private String mName;
    private String mDescription;
    private int mNumberOfTasks;

    public Category() {
        mId = -1;
        mName = null;
        mDescription = null;
        mNumberOfTasks = -1;
    }

    public Category(String name, String description) {
        this.mName = name;
        this.mDescription = description;
    }

    public Category(String name, String description, Bitmap icon) {
        this.mName = name;
        this.mDescription = description;
    }

    public Category(long id, String name, String description, Bitmap icon) {
        this.mId = id;
        this.mName = name;
        this.mDescription = description;
    }

    protected Category(Parcel in) {
        this.mId = in.readLong();
        this.mName = in.readString();
        this.mDescription = in.readString();
        this.mNumberOfTasks = in.readInt();
    }

    @Override
    public String toString() {
        return mName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.mId);
        dest.writeString(this.mName);
        dest.writeString(this.mDescription);
        dest.writeInt(this.mNumberOfTasks);
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public void setNumberOfTasks(int no) {
        this.mNumberOfTasks = no;
    }

    public int getNumberOfTasks() {
        return mNumberOfTasks;
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel source) {
            return new Category(source);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
}
