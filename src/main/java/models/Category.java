package models;

public class Category {
    private int categoryID;
    private byte image;

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public int getImage() {
        return image;
    }

    public void setImage(byte image) {
        this.image = image;
    }
}
