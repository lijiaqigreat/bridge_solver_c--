/*  1:   */ package wpbd;
/*  2:   */ 
/*  3:   */ import java.awt.Color;
/*  4:   */ import java.awt.Graphics2D;
/*  5:   */ 
/*  6:   */ public class BridgeSketchCartoonView
/*  7:   */   extends BridgeSketchView
/*  8:   */ {
/*  9:   */   public void paint(Graphics2D g, ViewportTransform viewportTransform)
/* 10:   */   {
/* 11:35 */     if (this.model == null) {
/* 12:36 */       return;
/* 13:   */     }
/* 14:38 */     g.setColor(Color.GRAY);
/* 15:39 */     for (int i = 0; i < this.model.getSketchMemberCount(); i++) {
/* 16:40 */       g.drawLine(viewportTransform.worldToViewportX(this.model.getSketchMember(i).jointA.x), viewportTransform.worldToViewportY(this.model.getSketchMember(i).jointA.y), viewportTransform.worldToViewportX(this.model.getSketchMember(i).jointB.x), viewportTransform.worldToViewportY(this.model.getSketchMember(i).jointB.y));
/* 17:   */     }
/* 18:46 */     for (int i = 0; i < this.model.getJointLocationCount(); i++)
/* 19:   */     {
/* 20:47 */       int x = viewportTransform.worldToViewportX(this.model.getJointLocation(i).x);
/* 21:48 */       int y = viewportTransform.worldToViewportY(this.model.getJointLocation(i).y);
/* 22:49 */       g.setColor(Color.WHITE);
/* 23:50 */       g.fillOval(x - 2, y - 2, 4, 4);
/* 24:   */       
/* 25:52 */       g.setColor(Color.BLACK);
/* 26:53 */       g.drawOval(x - 2, y - 2, 4, 4);
/* 27:   */     }
/* 28:   */   }
/* 29:   */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.BridgeSketchCartoonView
 * JD-Core Version:    0.7.0.1
 */