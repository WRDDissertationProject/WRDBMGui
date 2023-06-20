/** WILLS ROLLER DISCO - DISSERTATION PROJECT
 *  AUTHOR : EMILY FLETCHER
 *  STUDENT NUMBER: 18410839
 *  APPLICATION: WillsRollerDiscoBM
 *  FILE TITLE: getStartingSkates.java
 *  APPLICATION VERSION: 2.0
 *  DATE OF WRITING: 20/06/2023
 *
 *  PURPOSE:
 *    Method used to get the starting skate values from the database and then set them into the current skates,
 *    needed to ensure skate hire numbers are accurate when a session is restarted.
 *   */

//PACKAGES
package com.example.willsrollerdiscobmgui;

public class getStartingSkates {

    public void initialiseSkates(){
        System.out.println("Initialised Skates Test");

        int size1Max = DBConnect.fetchSkateSizeAmount("1");
        int size2Max = DBConnect.fetchSkateSizeAmount("2");
        int size3Max = DBConnect.fetchSkateSizeAmount("3");
        int size4Max = DBConnect.fetchSkateSizeAmount("4");
        int size5Max = DBConnect.fetchSkateSizeAmount("5");
        int size6Max = DBConnect.fetchSkateSizeAmount("6");
        int size7Max = DBConnect.fetchSkateSizeAmount("7");
        int size8Max = DBConnect.fetchSkateSizeAmount("8");
        int size9Max = DBConnect.fetchSkateSizeAmount("9");
        int size10Max = DBConnect.fetchSkateSizeAmount("10");
        int size11Max = DBConnect.fetchSkateSizeAmount("11");
        int size12Max = DBConnect.fetchSkateSizeAmount("12");
        int size11CHMax = DBConnect.fetchSkateSizeAmount("C11");
        int size12CHMax = DBConnect.fetchSkateSizeAmount("C12");
        int size13CHMax = DBConnect.fetchSkateSizeAmount("C12");

        DBConnect.insertIntoCurrentSkates("1",size1Max);
        DBConnect.insertIntoCurrentSkates("2",size2Max);
        DBConnect.insertIntoCurrentSkates("3",size3Max);
        DBConnect.insertIntoCurrentSkates("4",size4Max);
        DBConnect.insertIntoCurrentSkates("5",size5Max);
        DBConnect.insertIntoCurrentSkates("6",size6Max);
        DBConnect.insertIntoCurrentSkates("7",size7Max);
        DBConnect.insertIntoCurrentSkates("8",size8Max);
        DBConnect.insertIntoCurrentSkates("9",size9Max);
        DBConnect.insertIntoCurrentSkates("10",size10Max);
        DBConnect.insertIntoCurrentSkates("11",size11Max);
        DBConnect.insertIntoCurrentSkates("12",size12Max);
        DBConnect.insertIntoCurrentSkates("C11",size11CHMax);
        DBConnect.insertIntoCurrentSkates("C12",size12CHMax);
        DBConnect.insertIntoCurrentSkates("C13",size13CHMax);

    }
}
