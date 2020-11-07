import processing.core.PImage;

import java.util.List;

public interface Entity
{

   Point getPosition();
   void setposition(Point point);
   List<PImage> getImages();
   String getid();
   int getImageIndex();

}
