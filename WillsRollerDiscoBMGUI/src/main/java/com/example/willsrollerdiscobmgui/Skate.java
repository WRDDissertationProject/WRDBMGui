/** WILLS ROLLER DISCO - DISSERTATION PROJECT
 *  AUTHOR : EMILY FLETCHER
 *  STUDENT NUMBER: 18410839
 *  APPLICATION: WillsRollerDiscoBM
 *  FILE TITLE: Skate.java
 *  APPLICATION VERSION: 2.0
 *  DATE OF WRITING: 20/06/2023
 *
 *  PURPOSE:
 *   Setters for skate objects. Used for setting values, combines with getStartingSkates and skateInventory
 *   */

//PACKAGE
package com.example.willsrollerdiscobmgui;

public class Skate {
    private String skateSize;
    private Integer skateAmount;

    public Skate(String skateSize, Integer skateAmount){
        this.skateSize = skateSize;
        this.skateAmount = skateAmount;
    }
    public String getSkateSize(){
        return skateSize;
    }
    public Integer getSkateAmount(){
        return skateAmount;
    }
}
