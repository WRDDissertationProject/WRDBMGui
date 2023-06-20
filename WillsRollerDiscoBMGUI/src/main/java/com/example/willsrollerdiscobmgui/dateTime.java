/** WILLS ROLLER DISCO - DISSERTATION PROJECT
 *  AUTHOR : EMILY FLETCHER
 *  STUDENT NUMBER: 18410839
 *  APPLICATION: WillsRollerDiscoBM
 *  FILE TITLE: dateTime.java
 *  APPLICATION VERSION: 2.0
 *  DATE OF WRITING: 20/06/2023
 *
 *  PURPOSE:
 *    methods relating to the creation of time and date objects, used for other methods for records insertion and
 *    creating skating sessions.
 *   */

//PACKAGES
package com.example.willsrollerdiscobmgui;

//IMPORTS
import java.time.*;
import java.time.format.DateTimeFormatter;

public class dateTime {
    public static String fullDateTime() {
        LocalDateTime fullDateTime = LocalDateTime.now();
        DateTimeFormatter formattedFullDate = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return fullDateTime.format(formattedFullDate);
    }
    public static String justDate(){
        LocalDateTime justDate = LocalDateTime.now();
        DateTimeFormatter formattedJustDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return justDate.format(formattedJustDate);
    }
    public static String justTime(){
        LocalDateTime justTime = LocalDateTime.now();
        DateTimeFormatter formattedJustTime = DateTimeFormatter.ofPattern("HH:mm:ss");
        return justTime.format(formattedJustTime);
    }
}
