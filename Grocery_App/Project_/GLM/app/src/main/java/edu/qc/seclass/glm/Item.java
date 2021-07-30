package edu.qc.seclass.glm;

public class Item {

    String itemId;
    Boolean checked;
    String itemName;
    String itemQuantity;
    String itemQuantityType;

    public Item(String itemId, Boolean checked, String itemName, String itemQuantity, String itemQuantityType) {
        this.itemId = itemId;
        this.checked = checked;
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.itemQuantityType = itemQuantityType;
    }

    public String getItemId() {
        return itemId;
    }

    public Boolean getChecked() {
        return checked;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemQuantity() {
        return itemQuantity;
    }

    public String getItemQuantityType() {
        return itemQuantityType;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemQuantity(String itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public void setItemQuantityType(String itemQuantityType) {
        this.itemQuantityType = itemQuantityType;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

}
