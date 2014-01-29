/*  1:   */ package wpbd;
/*  2:   */ 
/*  3:   */ import java.awt.Color;
/*  4:   */ import java.awt.Graphics2D;
/*  5:   */ import java.awt.Stroke;
/*  6:   */ 
/*  7:   */ public class BridgeSketchDraftingView
/*  8:   */   extends BridgeSketchView
/*  9:   */ {
/* 10:   */   public void paint(Graphics2D g, ViewportTransform viewportTransform)
/* 11:   */   {
/* 12:36 */     if (this.model == null) {
/* 13:37 */       return;
/* 14:   */     }
/* 15:39 */     Stroke savedStroke = g.getStroke();
/* 16:   */     
/* 17:41 */     g.setColor(Color.LIGHT_GRAY);
/* 18:42 */     for (int i = 0; i < this.model.getSketchMemberCount(); i++) {
/* 19:43 */       g.drawLine(viewportTransform.worldToViewportX(this.model.getSketchMember(i).jointA.x), viewportTransform.worldToViewportY(this.model.getSketchMember(i).jointA.y), viewportTransform.worldToViewportX(this.model.getSketchMember(i).jointB.x), viewportTransform.worldToViewportY(this.model.getSketchMember(i).jointB.y));
/* 20:   */     }
/* 21:49 */     for (int i = this.model.getDesignConditions().getNPrescribedJoints(); i < this.model.getJointLocationCount(); i++)
/* 22:   */     {
/* 23:50 */       int x = viewportTransform.worldToViewportX(this.model.getJointLocation(i).x) - 8;
/* 24:51 */       int y = viewportTransform.worldToViewportY(this.model.getJointLocation(i).y) - 8;
/* 25:52 */       int size = 16;
/* 26:53 */       g.setColor(Color.WHITE);
/* 27:54 */       g.fillOval(x, y, size, size);
/* 28:55 */       g.setColor(Color.LIGHT_GRAY);
/* 29:56 */       g.drawOval(x, y, size, size);
/* 30:   */     }
/* 31:58 */     g.setStroke(savedStroke);
/* 32:   */   }
/* 33:   */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.BridgeSketchDraftingView
 * JD-Core Version:    0.7.0.1
 */