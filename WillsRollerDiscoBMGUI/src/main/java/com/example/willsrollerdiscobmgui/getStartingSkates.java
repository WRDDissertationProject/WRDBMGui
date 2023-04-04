package com.example.willsrollerdiscobmgui;

public class getStartingSkates {

    public void initialiseSkates(){
        System.out.println("Initialised Skates Test");
        int size1Min = 0;
        int size2Min = 0;
        int size3Min = 0;
        int size4Min = 0;
        int size5Min = 0;

        int size1Max = DBConnect.fetchSkateSizeAmount("1");
        int size2Max = DBConnect.fetchSkateSizeAmount("2");
        int size3Max = DBConnect.fetchSkateSizeAmount("3");
        int size4Max = DBConnect.fetchSkateSizeAmount("4");
        int size5Max = DBConnect.fetchSkateSizeAmount("5");

        DBConnect.insertIntoCurrentSkates("1",size1Max);
        DBConnect.insertIntoCurrentSkates("2",size2Max);
        DBConnect.insertIntoCurrentSkates("3",size3Max);
        DBConnect.insertIntoCurrentSkates("4",size4Max);
        DBConnect.insertIntoCurrentSkates("5",size5Max);

    }
}
