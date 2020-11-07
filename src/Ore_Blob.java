import processing.core.PImage;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class Ore_Blob implements NewEntity, AnimEntity {

    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int actionPeriod;
    private int animationPeriod;
    private  final String QUAKE_KEY = "quake";


    public Ore_Blob(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod)
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
                        Animation.createAnimationAction(this, 0), this.animationPeriod);

    }
      //public int getactionPeriod() {
    //    return actionPeriod;}

    public Optional<Entity> findNearest(WorldModel world, Point pos, Entity e)
    {
        List<Entity> ofType = new LinkedList<>();
        for (Entity entity : world.getEntities())
        {
            if (entity instanceof Vein)
            {
                ofType.add(entity);
            }
        }

        return world.nearestEntity(ofType, pos);
    }

    public void executeActivity(WorldModel world,
                                       ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> blobTarget = findNearest(world, this.position, this);
        long nextPeriod = this.actionPeriod;

        if (blobTarget.isPresent())
        {
            Point tgtPos = blobTarget.get().getPosition();

            if (moveToOreBlob(world, blobTarget.get(), scheduler))
            {
                Entity quake = Functions.createQuake(tgtPos,
                        imageStore.getImageList(QUAKE_KEY));

                world.addEntity(quake);
                nextPeriod += this.actionPeriod;
                ((NewEntity)quake).scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this,
                Activity.createActivityAction(this, world, imageStore),
                nextPeriod);
    }


    public boolean moveToOreBlob(WorldModel world,
                                 Entity target, EventScheduler scheduler)
    {
        if (position.adjacent(target.getPosition()))
        {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else
        {
            Point nextPos = nextPositionOreBlob(world, target.getPosition());

            if (!this.position.equals(nextPos))
            {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent())
                {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                moveEntity(world, nextPos);
            }
            return false;
        }
    }
    public  void moveEntity(WorldModel Model, Point pos)
    {
        Point oldPos = this.position;
        if (Model.withinBounds(pos) && !pos.equals(oldPos))
        {
            Model.setOccupancyCell(oldPos, null);
            Model.removeEntityAt(pos);
            Model.setOccupancyCell(pos, this);
            this.position = pos;
        }
    }
    /**
     * Gets the ore blob entity's next position.
     */
    public Point nextPositionOreBlob(WorldModel world,
                                     Point destPos)
    {
        int horiz = Integer.signum(destPos.getX() - this.position.getX());
        Point newPos = new Point(this.position.getX() + horiz,
                this.position.getY());

        Optional<Entity> occupant = world.getOccupant(newPos);

        if (horiz == 0 ||
                (occupant.isPresent() && !(occupant.get() instanceof Ore)))
        {
            int vert = Integer.signum(destPos.getY() - this.position.getY());
            newPos = new Point(this.position.getX(), this.position.getY() + vert);
            occupant = world.getOccupant(newPos);

            if (vert == 0 ||
                    (occupant.isPresent() && !(occupant.get() instanceof Ore)))
            {
                newPos = this.position;
            }
        }

        return newPos;
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
