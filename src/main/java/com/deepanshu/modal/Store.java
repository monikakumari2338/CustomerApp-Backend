package com.deepanshu.modal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;
import java.util.Map;

@Entity
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String city;
    private String pincode;

    private String address;

    @ElementCollection
    @CollectionTable(name = "store_schedule", joinColumns = @JoinColumn(name = "store_id"))
    @MapKeyColumn(name = "day_of_week")
    @Column(name = "timings")
    @JsonIgnore
    private Map<String, String> schedule;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<StorePickup> pickups;

    private double latitude;
    private double longitude;


    public Store() {
    }

    public Store(Long id, String name, String city, String pincode, String address, List<StorePickup> pickups) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.pincode = pincode;
        this.address = address;
        this.pickups = pickups;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<StorePickup> getPickups() {
        return pickups;
    }

    public void setPickups(List<StorePickup> pickups) {
        this.pickups = pickups;
    }

    public Map<String, String> getSchedule() {
        return schedule;
    }

    public void setSchedule(Map<String, String> schedule) {
        this.schedule = schedule;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
