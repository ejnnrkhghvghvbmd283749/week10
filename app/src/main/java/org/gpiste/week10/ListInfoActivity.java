package org.gpiste.week10;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class ListInfoActivity extends AppCompatActivity {
    private TextView cityText;
    private TextView yearText;
    private TextView carInfoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_info);

        cityText = findViewById(R.id.CityText);
        yearText = findViewById(R.id.YearText);
        carInfoText = findViewById(R.id.CarInfoText);

        CarDataStorage storage = CarDataStorage.getInstance();
        String city = storage.getCity();
        int year = storage.getYear();
        ArrayList<CarData> carData = storage.getCarData();

        cityText.setText("" + city);
        yearText.setText("" + year);


        StringBuilder carInfo = new StringBuilder();
        int total = 0;

        for(CarData data : carData) {
            carInfo.append(data.getType()).append(": ").append(data.getAmount()).append("\n");
            total += data.getAmount();

        }
        carInfo.append("\nKokonaismäärä: ").append(total);

        carInfoText.setText(carInfo.toString());
    }
}

