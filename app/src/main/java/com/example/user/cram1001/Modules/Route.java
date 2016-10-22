package com.example.user.cram1001.Modules;

import com.google.android.gms.maps.model.LatLng;
import java.util.List;

/**
 * Created by cheng ying on 2016/10/8.
 */
public class Route {
    public Distance distance;
    public Duration duration;
    public String endAddress;
    public LatLng endLocation;
    public String startAddress;
    public LatLng startLocation;

    public List<LatLng> points;
}
