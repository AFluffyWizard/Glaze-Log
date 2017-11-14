package nh.glazelog.database;

/**
 * Created by Nick Hansen on 11/3/2017.
 */

public interface ParseFromString<T> {
    public T parseFromString(String data);
}
