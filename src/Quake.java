import processing.core.PImage;

import java.util.List;

public class Quake implements NewEntity,AnimEntity {

    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int actionPeriod;
    private int animationPeriod;
    private final int QUAKE_ANIMATION_REPEAT_COUNT = 10;


    public Quake(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }


    public void scheduleActions(EventScheduler scheduler,WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent( this,
                        Activity.createActivityAction(this, world, imageStore),
                        this.actionPeriod);
                scheduler.scheduleEvent( this,
                        Animation.createAnimationAction(this, QUAKE_ANIMATION_REPEAT_COUNT),
                        this.animationPeriod);


    }


    public void executeActivity(WorldModel world,
                                     ImageStore imageStore, EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
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

    @Override
    public void nextImage() {
        imageIndex = (imageIndex + 1) % images.size();
    }

    @Override
    public int getAnimationPeriod() {
        return animationPeriod;
    }
}
