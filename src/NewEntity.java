public interface NewEntity extends Entity{

    void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);
    void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore);
    //int getactionPeriod();
}
