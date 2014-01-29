/*  1:   */ package wpbd;
/*  2:   */ 
/*  3:   */ public class BridgePaintContext
/*  4:   */ {
/*  5:27 */   public double allowableSlenderness = 1.0E+100D;
/*  6:31 */   public boolean label = false;
/*  7:35 */   public boolean blueprint = false;
/*  8:39 */   public Gusset[] gussets = null;
/*  9:   */   
/* 10:   */   public BridgePaintContext() {}
/* 11:   */   
/* 12:   */   public BridgePaintContext(double allowableSlenderness, boolean label)
/* 13:   */   {
/* 14:53 */     this.allowableSlenderness = allowableSlenderness;
/* 15:54 */     this.label = label;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public BridgePaintContext(Gusset[] gussets)
/* 19:   */   {
/* 20:63 */     this.label = true;
/* 21:64 */     this.blueprint = true;
/* 22:65 */     this.gussets = gussets;
/* 23:   */   }
/* 24:   */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.BridgePaintContext
 * JD-Core Version:    0.7.0.1
 */