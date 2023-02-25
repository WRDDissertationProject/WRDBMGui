package com.example.willsrollerdiscobmgui;

public class skateInventory {
    private String skateSizeInventory;
    private Integer skateAmountInventory;

    public skateInventory(String skateSizeInventory, Integer skateAmountInventory) {
        this.skateSizeInventory = skateSizeInventory;
        this.skateAmountInventory = skateAmountInventory;
    }

    public String getSkateSizeInventory() {
        return this.skateSizeInventory;
    }

    public Integer getSkateAmountInventory() {
        return this.skateAmountInventory;
    }

    public void setSkateSizeInventory(String skateSize) {
        this.skateSizeInventory = skateSize;
    }

    public void setSkateAmountInventory(Integer skateAmount) {
        this.skateAmountInventory = skateAmount;
    }
}


