import processing.core.PImage;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class MinerFull implements NewEntity, AnimEntity{

    private Point position;
    private int actionPeriod;
    private int animationPeriod;
    private List<PImage> Images;
    private int resourceLimit;
    private String id;
    private int imageIndex;

    public MinerFull(String id, Point position, List<PImage> images, int resourceLimit, int actionPeriod, int animationPeriod)
    {
        this.id = id;
        this.position = position;
        this.Images = images;
        this.imageIndex = 0;
        this.resourceLimit = resourceLimit;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }
    public Optional<Entity> findNearest(WorldModel world, Point pos, Entity e)
    {
        List<Entity> ofType = new LinkedList<>();
        for (Entity entity : world.getEntities())
        {
            if (entity instanceof Blacksmith)
            {
                ofType.add(entity);
            }
        }

        return world.nearestEntity(ofType, pos);
    }

    public void executeActivity(WorldModel world,
                                         ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> fullTarget = findNearest(world,this.position,
                this);

        if (fullTarget.isPresent() &&
                moveToFull(world, fullTarget.get(), scheduler))
        {
            transformFull(world, scheduler, imageStore);
        }
        else
        {
            scheduler.scheduleEvent(this,
                    Activity.createActivityAction(this, world, imageStore),
                    this.actionPeriod);
        }
    }


    public  boolean moveToFull(WorldModel world,
                               Entity target, EventScheduler scheduler)
    {
        if (position.adjacent(target.getPosition()))
        {
            return true;
        }
        else
        {
            Point nextPos = nextPositionMiner(world, target.getPosition());

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
    public void transformFull(WorldModel world,
                              EventScheduler scheduler, ImageStore imageStore)
    {
        Entity miner = Functions.createMinerNotFull(this.id, this.resourceLimit,
                this.position, this.actionPeriod, this.animationPeriod,
                this.Images);

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(miner);
        ((NewEntity)miner).scheduleActions(scheduler, world, imageStore);
    }

    /**
     * Gets the miner entity's next position.
     */
    public  Point nextPositionMiner(WorldModel world,
                                    Point destPos)
    {
        int horiz = Integer.signum(destPos.getX() - this.position.getX());
        Point newPos = new Point(this.position.getX() + horiz,
                this.position.getY());

        if (horiz == 0 || world.isOccupied(newPos))
        {
            int vert = Integer.signum(destPos.getY() - this.position.getY());
            newPos = new Point(this.position.getX(),
                    this.position.getY() + vert);

            if (vert == 0 || world.isOccupied(newPos))
            {
                newPos = this.position;
            }
        }

        return newPos;
    }



    public void scheduleActions(EventScheduler scheduler,WorldModel world, ImageStore imageStore)
    {
                scheduler.scheduleEvent( this,
                        Activity.createActivityAction(this, world, imageStore),
                        this.actionPeriod);
                scheduler.scheduleEvent( this, Animation.createAnimationAction(this, 0),
                        this.animationPeriod);
    }


    //public int getactionPeriod() {
      //  return actionPeriod;}

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
        return Images;
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
        imageIndex = (imageIndex + 1) % Images.size();
    }

    @Override
    public int getAnimationPeriod() {
        return animationPeriod;
    }


}
