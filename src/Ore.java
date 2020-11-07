import processing.core.PImage;

import java.util.List;
import java.util.Random;

public class Ore implements NewEntity{
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int actionPeriod;

    private  final String BLOB_KEY = "blob";
    private  final String BLOB_ID_SUFFIX = " -- blob";
    private  final int BLOB_PERIOD_SCALE = 4;
    private  final int BLOB_ANIMATION_MIN = 50;
    private  final int BLOB_ANIMATION_MAX = 150;
    private  final Random rand = new Random();


    public Ore(String id, Point position, List<PImage> images, int actionPeriod)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.actionPeriod = actionPeriod;
    }



    public void scheduleActions(EventScheduler scheduler,WorldModel world, ImageStore imageStore) {


        scheduler.scheduleEvent(this,
                Activity.createActivityAction(this, world, imageStore),
                this.actionPeriod);
    }


   // public int getactionPeriod() {
     //   return actionPeriod;


    public void executeActivity(WorldModel world,
                                   ImageStore imageStore, EventScheduler scheduler)
    {
        Point pos = this.position;  // store current position before removing

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        Entity blob = Functions.createOreBlob(this.id + BLOB_ID_SUFFIX,
                pos, this.actionPeriod / BLOB_PERIOD_SCALE,
                BLOB_ANIMATION_MIN +
                        rand.nextInt(BLOB_ANIMATION_MAX - BLOB_ANIMATION_MIN),
                imageStore.getImageList(BLOB_KEY));

        world.addEntity(blob);
        ((NewEntity)blob).scheduleActions(scheduler, world, imageStore);
    }

    @Override
    public Point getPosition() {
        return position;
    }

    @Override
    public void setposition(Point point) {
        this.position=point;

    }

    @Override
    public List<PImage> getImages() {
        return images;
    }

    @Override
    public String getid() {
        return id;
    }

    @Override
    public int getImageIndex() {
        return imageIndex;
    }
}
