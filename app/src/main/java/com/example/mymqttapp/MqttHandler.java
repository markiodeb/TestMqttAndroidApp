package com.example.mymqttapp;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttHandler {

    private MqttClient client;
    private Activity activity;

    public void connect(String brokerUrl, String clientId, Activity activity) {
        try {
            // Set up the persistence layer
            MemoryPersistence persistence = new MemoryPersistence();

            // Initialize the MQTT client
            client = new MqttClient(brokerUrl, clientId, persistence);
            this.activity = activity;

            // Set up the connection options
            MqttConnectOptions connectOptions = new MqttConnectOptions();
            connectOptions.setCleanSession(true);

            // Connect to the broker
            client.connect(connectOptions);
            client.setCallback( new MyCallBack(this.activity));

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public class MyCallBack implements MqttCallback{
        Context context;

        public MyCallBack(Context context) {
            this.context = context;
        }

        @Override
        public void connectionLost(Throwable cause) {
            System.out.println("Conexión perdida, razon: " + cause);
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            System.out.println("Mensaje recibido: " + message);
            //Toast.makeText(this.context, message.toString(), Toast.LENGTH_SHORT).show();
            showMessage(message);
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            System.out.println("Entrega completada, token: " + token);
        }
        public void showMessage(MqttMessage message){
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, "Tempratura recibida: " + message.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void disconnect() {
        try {
            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void publish(String topic, String message) {
        try {
            MqttMessage mqttMessage = new MqttMessage(message.getBytes());
            client.publish(topic, mqttMessage);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void subscribe(String topic) {
        try {
            client.subscribe(topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void unsuscribe(String topic){
        try {
            client.unsubscribe(topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}