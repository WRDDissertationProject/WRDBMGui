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

/*Resources Used:
 * Java DateTime Layouts:  */
public class dateTime {
    //Method that fetches the full date combined with the full time (Including Seconds), returned to class that called it.
    public static String fullDateTime() {
        //Fetches current machine time
        LocalDateTime fullDateTime = LocalDateTime.now();
        //Passes into a layout
        DateTimeFormatter formattedFullDate = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return fullDateTime.format(formattedFullDate);
    }
    //Creates a date object, passes into a format so just the date is used, returned to class that called it.
    public static String justDate(){
        //Fetches current machine time
        LocalDateTime justDate = LocalDateTime.now();
        //Passes into a layout
        DateTimeFormatter formattedJustDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return justDate.format(formattedJustDate);
    }
    //Creates a time object, passes into a format so just the date is used, returned to class that called it.
    public static String justTime(){
        //Fetches current machine time
        LocalDateTime justTime = LocalDateTime.now();
        //Passes into a layout
        DateTimeFormatter formattedJustTime = DateTimeFormatter.ofPattern("HH:mm:ss");
        return justTime.format(formattedJustTime);
    }
}
