import java.util.*;

public final class EventScheduler
{
   private PriorityQueue<Event> eventQueue;
   private Map<Entity, List<Event>> pendingEvents;
   private double timeScale;

   public Map<Entity, List<Event>> getPendingEvents() {
      return pendingEvents;
   }

  // private final int QUAKE_ANIMATION_REPEAT_COUNT = 10;

   public EventScheduler(double timeScale)
   {
      this.eventQueue = new PriorityQueue<>(new EventComparator());
      this.pendingEvents = new HashMap<>();
      this.timeScale = timeScale;
   }


   /**
    * Asks the scheduler to schedule an Action for the given entity,
    * to be take after the specified period of time.
    */
   public void scheduleEvent(Entity entity, Action action, long afterPeriod)
   {
      long time = System.currentTimeMillis() +
              (long)(afterPeriod * timeScale);
      Event event = new Event(action, time, entity);

      eventQueue.add(event);

      // update list of pending events for the given entity
      List<Event> pending = pendingEvents.getOrDefault(entity,
              new LinkedList<>());
      pending.add(event);
      pendingEvents.put(entity, pending);
   }

   /**
    * Asks the scheduler to unschedule all events for the given entity.
    */
   public void unscheduleAllEvents(Entity entity)
   {
      List<Event> pending = this.pendingEvents.remove(entity);

      if (pending != null)
      {
         for (Event event : pending)
         {
            this.eventQueue.remove(event);
         }
      }
   }



   /**
    * Asks the scheduler to execute all events that take place
    * before the specified time.
    */
   public void updateOnTime(long time)
   {
      while (!this.eventQueue.isEmpty() &&
              this.eventQueue.peek().getTime() < time)
      {
         Event next = this.eventQueue.poll();

         next.removePendingEvent(this);

         next.getAction().executeAction(this);
      }
   }


}
