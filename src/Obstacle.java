import processing.core.PImage;

import java.util.List;

public class Obstacle implements Entity {

    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;


    public Obstacle(String id, Point position, List<PImage> images)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;

    }

    public Point getPosition() { return position; }
    public void setposition(Point point) { this.position = point; }
    public List<PImage> getImages() { return images; }
    public String getid() { return id; }
    public int getImageIndex() { return imageIndex; }


}
