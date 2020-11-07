import java.util.List;
import java.util.Optional;

import processing.core.PImage;

public final class Background
{
   private String id;
   private List<PImage> images;
   private int imageIndex;

   public int getImageIndex() {
      return imageIndex;
   }

   public List<PImage> getImages() {
      return images;
   }

   public Background(String id, List<PImage> images)
   {
      this.id = id;
      this.images = images;
   }

   public void setBackground(WorldModel world, Point pos)
   {
      if (world.withinBounds(pos))
      {
         world.setBackgroundCell(pos,this);
      }
   }

}
