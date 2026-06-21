package org.example.lostfound.service;

import org.example.lostfound.mapper.LocationMapper;
import org.example.lostfound.model.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    @Autowired
    private LocationMapper locationMapper;

    public List<Location> getAllLocations() {
        return locationMapper.getAllLocations();
    }

    public Location getLocationById(Integer locationId) {
        return locationMapper.getLocationById(locationId);
    }

    public Location getLocationByName(String name) {
        return locationMapper.getLocationByName(name);
    }
}
