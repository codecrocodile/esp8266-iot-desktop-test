package com.codecrocodile.esp8266.serial.comm;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import java.io.*;

/**
 * Created by Chris on 24/07/2018.
 */
public class Main {

    private static final String apiKey = "xxxxx";

    public static void main (String... args) throws InterruptedException {

        final SerialPort comPort = SerialPort.getCommPort("com3");
        comPort.setBaudRate(115200);

        comPort.openPort();
        comPort.addDataListener(new SerialPortDataListener() {
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
            }

            public void serialEvent(SerialPortEvent event)
            {
                if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_RECEIVED) {
                    return;
                }
                byte[] newData = event.getReceivedData();

                ////////////////////////////////////

                InputStream is = null;
                BufferedReader bfReader = null;
                try {
                    is = new ByteArrayInputStream(newData);
                    bfReader = new BufferedReader(new InputStreamReader(is));
                    String temp = null;
                    while((temp = bfReader.readLine()) != null){
                        System.out.println(temp);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try{
                        if(is != null) is.close();
                    } catch (Exception ex){

                    }
                }

            }
        });

        sendCommand("AT+CIPSTATUS", comPort);
        sendCommand("AT+CIPSTART=\"TCP\",\"api.thingspeak.com\",80", comPort);

        String s = "GET /update?api_key=" + apiKey + "&field1=12 HTTP/1.1\r\nHost: api.thingspeak.com\r\n\r\n";
        sendCommand("AT+CIPSEND=" + s.getBytes().length, comPort);
        sendCommand(s, comPort);

        sendCommand("AT+CIPCLOSE", comPort);
    }


    private static void sendCommand(String cmd, SerialPort comPort) throws InterruptedException {
        byte[] cmdBytes = cmd.getBytes();
        comPort.writeBytes(cmdBytes, cmdBytes.length);

        byte[] cmdEmd = "\r\n".getBytes();
        comPort.writeBytes(cmdEmd, cmdEmd.length);

        Thread.sleep(2000);
        System.out.println("\n=====================================================\n");
    }
}
