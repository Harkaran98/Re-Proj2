import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import processing.core.PImage;

public final class ImageStore
{
   private Map<String, List<PImage>> images;
   private List<PImage> defaultImages;

   public Map<String, List<PImage>> getImages() {
      return images;
   }

   public ImageStore(PImage defaultImage)
   {
      this.images = new HashMap<>();
      defaultImages = new LinkedList<>();
      defaultImages.add(defaultImage);
   }

   /**
    * Gets the list of images associated with the given key.
    */
   public List<PImage> getImageList(String key)
   {
      return this.images.getOrDefault(key, this.defaultImages);
   }



}
