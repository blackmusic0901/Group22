package my.edu.utar.assignment20;

public class matchChatHelper {

    String name;
    String imageURL;
    int image;

    public matchChatHelper() {
    }
    public matchChatHelper(String name, int image) {
        this.name = name;
        this.image = image;
    }

    public matchChatHelper(String name, String imageURL) {
        this.name = name;
        this.imageURL = imageURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
