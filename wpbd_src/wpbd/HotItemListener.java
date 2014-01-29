package wpbd;

import java.awt.Cursor;
import java.awt.Point;

public abstract interface HotItemListener<TContext>
{
  public abstract HotItem<TContext> getItem(Point paramPoint, Affine.Point paramPoint1);
  
  public abstract TContext getContext();
  
  public abstract Cursor getCursor();
  
  public abstract boolean showToolTip();
}


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.HotItemListener
 * JD-Core Version:    0.7.0.1
 */