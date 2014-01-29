/*  1:   */ package wpbd;
/*  2:   */ 
/*  3:   */ public class MovelLabelsCommand
/*  4:   */   extends EditCommand
/*  5:   */ {
/*  6:   */   private double positionOld;
/*  7:   */   private double positionNew;
/*  8:   */   private DraftingPanel.Labels labels;
/*  9:   */   
/* 10:   */   public MovelLabelsCommand(EditableBridgeModel bridge, DraftingPanel.Labels labels, double positionNew)
/* 11:   */   {
/* 12:22 */     super(bridge);
/* 13:23 */     this.labels = labels;
/* 14:24 */     this.positionNew = positionNew;
/* 15:25 */     this.positionOld = bridge.getLabelPosition();
/* 16:26 */     this.presentationName = getString("moveLabels.text");
/* 17:   */   }
/* 18:   */   
/* 19:   */   public void go()
/* 20:   */   {
/* 21:30 */     this.bridge.setLabelPosition(this.positionNew);
/* 22:31 */     this.labels.initialize();
/* 23:   */   }
/* 24:   */   
/* 25:   */   public void goBack()
/* 26:   */   {
/* 27:35 */     this.bridge.setLabelPosition(this.positionOld);
/* 28:36 */     this.labels.initialize();
/* 29:   */   }
/* 30:   */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.MovelLabelsCommand
 * JD-Core Version:    0.7.0.1
 */