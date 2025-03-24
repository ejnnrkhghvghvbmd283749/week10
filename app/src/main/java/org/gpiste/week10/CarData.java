package org.gpiste.week10;

public class CarData {

    private String type;
    private int amount;

    public CarData(String type, int amount) {

        this.type = type;
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

}
