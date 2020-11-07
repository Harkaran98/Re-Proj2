import processing.core.PImage;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class MinerNotFull implements NewEntity, AnimEntity {
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int resourceLimit;
    private int resourceCount;
    private int actionPeriod;
    private int animationPeriod;

    public MinerNotFull(String id, Point position, List<PImage> images, int resourceLimit, int actionPeriod, int animationPeriod)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.resourceLimit = 4;
        this.resourceCount = resourceLimit;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }

    public Optional<Entity> findNearest(WorldModel world, Point pos, Entity e)
    {
        List<Entity> ofType = new LinkedList<>();
        for (Entity entity : world.getEntities())
        {
            if (entity instanceof Ore)
            {
                ofType.add(entity);
            }
        }

        return world.nearestEntity(ofType, pos);
    }


    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> notFullTarget = findNearest(world, this.position,
                this);

        if (!notFullTarget.isPresent() ||
                !moveToNotFull(world, notFullTarget.get(), scheduler) ||
                !transformNotFull(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    Activity.createActivityAction(this, world, imageStore),
                    this.actionPeriod);
        }
    }


    public boolean moveToNotFull(WorldModel world,
                                 Entity target, EventScheduler scheduler)
    {
        if (position.adjacent(target.getPosition()))
        {
            this.resourceCount += 1;
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);

            return true;
        }
        else
        {
            Point nextPos = this.nextPositionMiner(world, target.getPosition());

            if (!this.position.equals(nextPos))
            {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent())
                {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                this.moveEntity(world,nextPos);
            }
            return false;
        }
    }

    public  boolean transformNotFull(WorldModel world,
                                     EventScheduler scheduler, ImageStore imageStore)
    {
        if (this.resourceCount >= this.resourceLimit)
        {
            MinerFull miner = Functions.createMinerFull(this.id, this.resourceLimit,
                    this.position, this.actionPeriod, this.animationPeriod,
                    this.images);

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            miner.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }
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

    public void scheduleActions(EventScheduler scheduler,WorldModel world, ImageStore imageStore)
    {


                scheduler.scheduleEvent( this,
                        Activity.createActivityAction(this, world, imageStore),
                        this.actionPeriod);
                scheduler.scheduleEvent( this,
                        Animation.createAnimationAction(this, 0), this.animationPeriod);


    }


    //public int getactionPeriod() {
      //  return actionPeriod;}

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
