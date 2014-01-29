package wpbd;

import java.awt.Point;

public abstract interface HotDraggableItem<TContext>
  extends HotItem<TContext>
{
  public abstract boolean startDrag(Point paramPoint);
  
  public abstract boolean queryDrag(Point paramPoint);
  
  public abstract void updateDrag(Point paramPoint);
  
  public abstract void stopDrag(Point paramPoint);
}


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.HotDraggableItem
 * JD-Core Version:    0.7.0.1
 */