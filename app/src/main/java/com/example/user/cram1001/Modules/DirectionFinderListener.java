package com.example.user.cram1001.Modules;

import java.util.List;

/**
 * Created by cheng ying on 2016/10/8.
 */
public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
}