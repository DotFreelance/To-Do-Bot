package uofm.software_engineering.group7.to_do_bot.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;


public class Task implements Parcelable, Comparable {
    // User values for this TaskListItem
    private long id;
    private String dateCreated;
    private String name;
    private Bitmap imageDescription;
    private long categoryId;
    private boolean checked = false;
    private String alarmTime = null;
    private int priority = 0;

    public Task() {
    }

    protected Task(Parcel in) {
        this.id = in.readLong();
        this.dateCreated = in.readString();
        this.name = in.readString();
        this.imageDescription = in.readParcelable(Bitmap.class.getClassLoader());
        this.categoryId = in.readLong();
        this.checked = in.readByte() != 0;
        this.alarmTime = in.readString();
        this.priority = in.readInt();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getImageDescription() {
        return imageDescription;
    }

    public void setImageDescription(Bitmap imageDescription) {
        this.imageDescription = imageDescription;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    //Comparator for sorting purposes
    @Override
    public int compareTo(Object that) {
        return ((Task) that).getPriority() - this.getPriority();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.dateCreated);
        dest.writeString(this.name);
        dest.writeParcelable(this.imageDescription, flags);
        dest.writeLong(this.categoryId);
        dest.writeByte(checked ? (byte) 1 : (byte) 0);
        dest.writeString(this.alarmTime);
        dest.writeInt(this.priority);
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel source) {
            return new Task(source);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };
}
