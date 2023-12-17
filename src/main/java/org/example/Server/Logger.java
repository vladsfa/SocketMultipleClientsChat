package org.example.Server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Date;

public class Logger {
    private PrintWriter printWriter;
    public Logger(String path) {
        try {
            File file = new File(path);
            this.printWriter = new PrintWriter(file);
        }catch (FileNotFoundException e){
            close();
            e.printStackTrace();
        }
    }

    public void log(String from, String to, String msg){
        String logMsg = "[" + new Date() + "] " + from + " to " + to + ": "+ msg;
        this.printWriter.println(logMsg);
        this.printWriter.flush();
        System.out.println(logMsg);
    }

    public void close(){
        if (printWriter != null){
            printWriter.close();
        }
    }
}
