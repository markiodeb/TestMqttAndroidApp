package com.example.mymqttapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //private static final String BROKER_URL = "tcp://mqtt.eclipseprojects.io:1883";
    private static final String BROKER_URL2 = "tcp://test.mosquitto.org:1883";
    private static final String CLIENT_ID = "TERMOSTAT2";
    private static final String TOPIC = "TEMPERATURE";
    private MqttHandler mqttHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mqttHandler = new MqttHandler();
        mqttHandler.connect(BROKER_URL2, CLIENT_ID, this);

        //subscribe(TOPIC);

    }

    @Override
    protected void onDestroy() {
        mqttHandler.disconnect();
        super.onDestroy();
    }

    public void publishClick(View view){
        publicMessage(TOPIC, "Temperatura 23 grados");
    }
    private void publicMessage(String topic, String message){
        Toast.makeText(this, "Publish message: " + message, Toast.LENGTH_SHORT).show();
        mqttHandler.publish(topic, message);
    }

    private void subscribe(String topic){
        Toast.makeText(this, "Subscribing to: " + topic, Toast.LENGTH_SHORT).show();
        mqttHandler.subscribe(topic);
    }

    public void showMessageClick(View view){
        subscribe(TOPIC);
        Toast.makeText(this, "Suscrito al topic: " + TOPIC, Toast.LENGTH_SHORT).show();
    }
    public void unsuscribe(View view){
        mqttHandler.unsuscribe(TOPIC);
        Toast.makeText(this, "Desuscrito al topic: " + TOPIC, Toast.LENGTH_SHORT).show();
    }
}