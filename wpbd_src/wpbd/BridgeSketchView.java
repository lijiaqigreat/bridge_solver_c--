/*  1:   */ package wpbd;
/*  2:   */ 
/*  3:   */ import java.awt.Graphics2D;
/*  4:   */ 
/*  5:   */ public abstract class BridgeSketchView
/*  6:   */ {
/*  7:   */   protected BridgeSketchModel model;
/*  8:   */   
/*  9:   */   public void setModel(BridgeSketchModel model)
/* 10:   */   {
/* 11:37 */     this.model = model;
/* 12:   */   }
/* 13:   */   
/* 14:   */   public BridgeSketchModel getModel()
/* 15:   */   {
/* 16:46 */     return this.model;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public abstract void paint(Graphics2D paramGraphics2D, ViewportTransform paramViewportTransform);
/* 20:   */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.BridgeSketchView
 * JD-Core Version:    0.7.0.1
 */