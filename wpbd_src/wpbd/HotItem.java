package wpbd;

import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public abstract interface HotItem<PaintContext>
{
  public abstract void paint(Graphics2D paramGraphics2D, ViewportTransform paramViewportTransform, PaintContext paramPaintContext);
  
  public abstract void paintHot(Graphics2D paramGraphics2D, ViewportTransform paramViewportTransform, PaintContext paramPaintContext);
  
  public abstract void getViewportExtent(Rectangle paramRectangle, ViewportTransform paramViewportTransform);
  
  public abstract Cursor getCursor();
}


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.HotItem
 * JD-Core Version:    0.7.0.1
 */