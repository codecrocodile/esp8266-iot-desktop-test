package com.codecrocodile.esp8266.serial.comm;

import com.codecrocodile.esp8266.serial.comm.commands.AtCommand;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import java.io.*;

public class AtCommander {

    private SerialPort comPort;

    public enum Status {
        READY_FOR_COMMAND,
        WAITING_FOR_RESPONSE,
        TIMEOUT,
        ERROR
    }

    private Status currentStatus = Status.READY_FOR_COMMAND;

    private AtCommand lastCommand;


    /**
     * @param comPort
     */
    public AtCommander(SerialPort comPort) {
        this.comPort = comPort;
    }

    private void addListener() {
        comPort.addDataListener(new SerialPortDataListener() {
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
            }

            public void serialEvent(SerialPortEvent event)
            {
                if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_RECEIVED) {
                    return;
                }
                byte[] responseData = event.getReceivedData();
                currentStatus = lastCommand.parseResponse(responseData);
            }
        });
    }

    public AtCommand executeCommands(AtCommand... commands) throws CommandException {

        return null;
    }

}

