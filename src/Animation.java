public class Animation implements Action{

    private Entity entity;
    private int repeatCount;

public Animation(Entity entity, int repeatCount){

    this.entity=entity;
    this.repeatCount=repeatCount;


}


    /**
     * Asks the scheduler to execute the specified animation action.
     */
    public void executeAction(EventScheduler scheduler)
    {
        //if(entity instanceof  AnimEntity)
        ((AnimEntity)this.entity).nextImage();

        if (repeatCount != 1 )//&& entity instanceof  AnimEntity)
        {
            scheduler.scheduleEvent(entity,
                    createAnimationAction(entity,
                            Math.max(repeatCount - 1, 0)),
                    ((AnimEntity)entity).getAnimationPeriod());
        }
    }

    public static Action createAnimationAction(Entity entity, int repeatCount)
    {
        return new Animation(entity, repeatCount);
    }
}
