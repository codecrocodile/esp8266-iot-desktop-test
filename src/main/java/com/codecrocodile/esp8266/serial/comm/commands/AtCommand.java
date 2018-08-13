package com.codecrocodile.esp8266.serial.comm.commands;

import com.codecrocodile.esp8266.serial.comm.AtCommander;

import java.io.*;

public abstract class AtCommand {

    public AtCommander.Status parseResponse(byte[] responseData) {
        AtCommander.Status responseStatus = AtCommander.Status.READY_FOR_COMMAND;

        InputStream is = null;
        BufferedReader bfReader = null;
        try {
            is = new ByteArrayInputStream(responseData);
            bfReader = new BufferedReader(new InputStreamReader(is));
            responseStatus = handleResponse(bfReader);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try{
                if(is != null) is.close();
            } catch (Exception ex){}
        }

        return responseStatus;
    }

    /**
     String temp = null;
         while((temp = bfReader.readLine()) != null){
         System.out.println(temp);
     }
     * @param bufferedReader
     */
    public abstract AtCommander.Status handleResponse(BufferedReader bufferedReader) throws IOException;

}
