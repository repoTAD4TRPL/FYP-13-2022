package com.example.apprute.Maps;

public interface TaskLoadedCallback {
    void onTaskDone(Object... values);
    void onDistanceDuration(Object... values);
    void onDistance(Object... values);

}
