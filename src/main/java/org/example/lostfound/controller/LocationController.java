package org.example.lostfound.controller;

import org.example.lostfound.model.Location;
import org.example.lostfound.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    @Autowired
    private LocationService locationService;

    /**
     * 查询所有地点
     */
    @GetMapping
    public List<Location> getAllLocations() {
        return locationService.getAllLocations();
    }

    /**
     * 根据ID查询地点
     */
    @GetMapping("/{id}")
    public Location getLocationById(@PathVariable Integer id) {
        return locationService.getLocationById(id);
    }
}
