package org.gpiste.week10;

import java.util.ArrayList;

public class CarDataStorage {
    private String city;
    private int year;
    private ArrayList<CarData> carDatas = new ArrayList<>();

    private static CarDataStorage carDataStorage = null;

    private CarDataStorage() {
        carDatas = new ArrayList<>();

    }

    public static CarDataStorage getInstance() {
        if(carDataStorage == null) {
            carDataStorage = new CarDataStorage();
        }
        return carDataStorage;
   }


    public String getCity() {
        return city;
    }

    public int getYear() {
        return year;
    }

    public ArrayList<CarData> getCarData() {
        return carDatas;
    }

    public void addCarData(CarData carData) {
        carDatas.add((carData));
   }

    public void setCity(String city) {
        this.city = city;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void ClearData() {
        carDatas.clear();
    }
}
