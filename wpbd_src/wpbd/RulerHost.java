package wpbd;

import java.awt.Component;

public abstract interface RulerHost
{
  public abstract Component getComponent();
  
  public abstract ViewportTransform getViewportTransform();
  
  public abstract DraftingCoordinates getDraftingCoordinates();
}


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.RulerHost
 * JD-Core Version:    0.7.0.1
 */