package org.springbus.binlog;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.BinaryLogFileReader;
import com.github.shyiko.mysql.binlog.event.*;
import com.github.shyiko.mysql.binlog.event.deserialization.EventDeserializer;

import java.io.File;
import java.io.IOException;


public class Application {


    private static void netLink() throws IOException {
        BinaryLogClient binaryLogClient=new BinaryLogClient("localhost",3306,"maxwell","root");
        EventDeserializer eventDeserializer = new EventDeserializer();
        eventDeserializer.setCompatibilityMode(
                EventDeserializer.CompatibilityMode.DATE_AND_TIME_AS_LONG,
                EventDeserializer.CompatibilityMode.DATE_AND_TIME_AS_LONG
        );
        // binaryLogClient.setEventDeserializer(eventDeserializer);
        binaryLogClient.registerEventListener(new BinaryLogClient.EventListener() {
            @Override
            public void onEvent(Event event) {
                PP(event);
            }
        });
        binaryLogClient.connect();
    }

    public static void main(String[] args) throws IOException {
        //fileLink();
        netLink();
    }

    private static void PP(Event event) {
        EventData data = event.getData();
        if (data instanceof QueryEventData) {
            QueryEventData d = (QueryEventData) data;
            System.out.println("数据库DDL操作select=" + d.getSql());
        } else if (data instanceof TableMapEventData) {

            TableMapEventData d = (TableMapEventData) data;
            System.out.println("数据库DDL操作=" + d.toString());
        } else if (data instanceof UpdateRowsEventData) {
            UpdateRowsEventData d = (UpdateRowsEventData) data;

            System.out.println("数据库DDL操作update=" + d.toString());
        } else if (data instanceof WriteRowsEventData) {
            WriteRowsEventData d = (WriteRowsEventData) data;
            System.out.println("数据库DDL操作insert=" + d.toString());
        } else if (data instanceof DeleteRowsEventData) {
            DeleteRowsEventData d = (DeleteRowsEventData) data;
            System.out.println("数据库DDL操作delete=" + d.toString());
        }

    }



    public static void fileLink() throws IOException {

        File file = new File("D:\\phpStudy\\PHPTutorial\\MySQL\\data\\bin-log.000003");
        EventDeserializer eventDeserializer = new EventDeserializer();
        eventDeserializer.setCompatibilityMode(
                EventDeserializer.CompatibilityMode.DATE_AND_TIME_AS_LONG,
                EventDeserializer.CompatibilityMode.CHAR_AND_BINARY_AS_BYTE_ARRAY
        );
        BinaryLogFileReader reader = new BinaryLogFileReader(file);

        try {
            for (Event event; (event = reader.readEvent()) != null; ) {
                EventData data = event.getData();
                if(data instanceof QueryEventData){
                    QueryEventData d= (QueryEventData) data;
                    System.out.println("数据库DDL操作select="+d.getSql());
                }
                else if(data instanceof TableMapEventData){
                    TableMapEventData d= (TableMapEventData) data;
                    System.out.println("数据库DDL操作="+d.getTable());
                }
                else if(data instanceof UpdateRowsEventData){
                    UpdateRowsEventData d= (UpdateRowsEventData) data;
                    System.out.println("数据库DDL操作update="+d.toString());
                }

                else if(data instanceof WriteRowsEventData){
                    WriteRowsEventData d= (WriteRowsEventData) data;
                    System.out.println("数据库DDL操作insert="+d.toString());
                }
                else if(data instanceof DeleteRowsEventData){
                    DeleteRowsEventData d= (DeleteRowsEventData) data;
                    System.out.println("数据库DDL操作delete="+d.toString());
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            reader.close();
        }
    }
}
