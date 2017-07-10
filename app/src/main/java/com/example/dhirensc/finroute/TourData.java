package com.example.dhirensc.finroute;

/**
 * Created by dhirensc on 3/7/17.
 */

public class TourData {
    private int id;
    private String name;
    private String status;
    private int waypt_order;
    private int tour_id;
    public TourData()
    {
    }
    public TourData(int id,String name,String status, int waypt_order, int tour_id)
    {
        this.id=id;
        this.name=name;
        this.status=status;
        this.waypt_order=waypt_order;
        this.tour_id=tour_id;
    }

    public TourData(String name,String status, int waypt_order, int tour_id)
    {
        //this.id=id;
        this.name=name;
        this.status=status;
        this.waypt_order=waypt_order;
        this.tour_id=tour_id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setWaypt_order(int waypt_order) {
        this.waypt_order = waypt_order;
    }
    public void setTour_id(int tour_id) {
        this.tour_id = tour_id;
    }



    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getStatus() {
        return status;
    }
    public int getWaypt_order() {
        return waypt_order;
    }
    public int getTour_id() {
        return tour_id;
    }
}