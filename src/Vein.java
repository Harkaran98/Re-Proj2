import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Vein implements NewEntity {


    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int actionPeriod;
    private int animationPeriod;
    private  final Random rand = new Random();
    private   static final String ORE_KEY = "ore";


    private  final String ORE_ID_PREFIX = "ore -- ";
    private  final int ORE_CORRUPT_MIN = 20000;
    private  final int ORE_CORRUPT_MAX = 30000;


    public Vein(String id, Point position, List<PImage> images, int actionPeriod)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.actionPeriod = actionPeriod;

    }

    //public static String getOreKey() {
      //  return ORE_KEY;
    //}

    public void scheduleActions(EventScheduler scheduler,WorldModel world, ImageStore imageStore)
    {
                 scheduler.scheduleEvent(this,
                        Activity.createActivityAction(this, world, imageStore),
                        this.actionPeriod);

    }


    //public int getactionPeriod() {
    //    return actionPeriod;}


    public void executeActivity(WorldModel world,
                                    ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Point> openPt = world.findOpenAround(this.position);

        if (openPt.isPresent())
        {
            Entity ore = Functions.createOre(ORE_ID_PREFIX + this.id,
                    openPt.get(), ORE_CORRUPT_MIN +
                            rand.nextInt(ORE_CORRUPT_MAX - ORE_CORRUPT_MIN),
                    imageStore.getImageList(ORE_KEY));
            world.addEntity(ore);
            ((NewEntity)ore).scheduleActions(scheduler, world, imageStore);
        }

        scheduler.scheduleEvent(this,
                Activity.createActivityAction(this, world, imageStore),
                this.actionPeriod);
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
