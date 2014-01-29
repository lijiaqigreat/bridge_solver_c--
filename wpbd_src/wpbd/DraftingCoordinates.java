/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.awt.Point;
/*   4:    */ import java.awt.geom.Rectangle2D.Double;
/*   5:    */ 
/*   6:    */ public class DraftingCoordinates
/*   7:    */   extends DraftingGrid
/*   8:    */ {
/*   9:    */   private BridgeView bridgeView;
/*  10:    */   private static final double abutmentClearance = 1.0D;
/*  11:    */   private static final double pierClearance = 1.0D;
/*  12: 22 */   private final Point grid = new Point();
/*  13:    */   private static final int searchRadiusInMeters = 8;
/*  14:    */   
/*  15:    */   public DraftingCoordinates(BridgeView view)
/*  16:    */   {
/*  17: 37 */     super(0);
/*  18: 38 */     this.bridgeView = view;
/*  19:    */   }
/*  20:    */   
/*  21:    */   public Rectangle2D.Double getExtent()
/*  22:    */   {
/*  23: 47 */     return this.bridgeView.getDrawingExtent();
/*  24:    */   }
/*  25:    */   
/*  26:    */   public void getNearbyPointOnGrid(Affine.Point dst, Affine.Point src, int dx, int dy)
/*  27:    */   {
/*  28: 61 */     BridgeModel bridge = this.bridgeView.getBridgeModel();
/*  29: 62 */     int tryDx = dx;
/*  30: 63 */     int tryDy = dy;
/*  31: 64 */     int nSearchSteps = 8 / this.snapMultiple;
/*  32: 65 */     for (int i = 0; i < nSearchSteps; i++)
/*  33:    */     {
/*  34: 66 */       src.x += tryDx * this.snapMultiple * 0.25D;
/*  35: 67 */       src.y += tryDy * this.snapMultiple * 0.25D;
/*  36: 68 */       shiftToNearestValidWorldPoint(dst, this.grid, dst);
/*  37: 70 */       if ((!dst.equals(src)) && ((bridge == null) || (bridge.findJointAt(dst) == null))) {
/*  38: 71 */         return;
/*  39:    */       }
/*  40: 73 */       tryDx += dx;
/*  41: 74 */       tryDy += dy;
/*  42:    */     }
/*  43: 76 */     dst.setLocation(src);
/*  44:    */   }
/*  45:    */   
/*  46:    */   public void shiftToNearestValidWorldPoint(Affine.Point dst, Point dstGrid, Affine.Point src)
/*  47:    */   {
/*  48: 90 */     double x = src.x;
/*  49: 91 */     double y = src.y;
/*  50:    */     
/*  51: 93 */     double yTop = getExtent().getMaxY();
/*  52: 94 */     double yBottom = getExtent().getMinY();
/*  53: 95 */     double xLeft = getExtent().getMinX();
/*  54: 96 */     double xRight = getExtent().getMaxX();
/*  55:    */     
/*  56:    */ 
/*  57: 99 */     double tol = 0.125D;
/*  58:102 */     if ((!this.bridgeView.getConditions().isArch()) && (y <= 0.125D))
/*  59:    */     {
/*  60:103 */       xLeft += 1.0D;
/*  61:104 */       xRight -= 1.0D;
/*  62:105 */       double dy = this.bridgeView.getYGradeLevel() - y;
/*  63:106 */       double xLeftSlope = this.bridgeView.getLeftBankX() + 0.5D * dy - 0.5D;
/*  64:107 */       if (xLeftSlope > xLeft) {
/*  65:108 */         xLeft = xLeftSlope;
/*  66:    */       }
/*  67:110 */       double xRightSlope = this.bridgeView.getRightBankX() - 0.5D * dy + 0.5D;
/*  68:111 */       if (xRightSlope < xRight) {
/*  69:112 */         xRight = xRightSlope;
/*  70:    */       }
/*  71:    */     }
/*  72:117 */     if (this.bridgeView.getConditions().isHiPier())
/*  73:    */     {
/*  74:118 */       Affine.Point pierLocation = this.bridgeView.getPierLocation();
/*  75:119 */       if ((y <= pierLocation.y + 0.125D) && 
/*  76:120 */         (pierLocation.x - 1.0D <= x) && (x <= pierLocation.x + 1.0D)) {
/*  77:121 */         x = x < pierLocation.x ? pierLocation.x - 1.0D : pierLocation.x + 1.0D;
/*  78:    */       }
/*  79:    */     }
/*  80:125 */     dst.x = (x > xRight ? xRight : x < xLeft ? xLeft : x);
/*  81:126 */     dst.y = (y > yTop ? yTop : y < yBottom ? yBottom : y);
/*  82:    */     
/*  83:    */ 
/*  84:129 */     worldToGrid(dstGrid, dst);
/*  85:130 */     gridToWorld(dst, dstGrid);
/*  86:133 */     if (dst.x < xLeft)
/*  87:    */     {
/*  88:134 */       dstGrid.x += this.snapMultiple;
/*  89:135 */       gridToWorld(dst, dstGrid);
/*  90:    */     }
/*  91:137 */     else if (dst.x > xRight)
/*  92:    */     {
/*  93:138 */       dstGrid.x -= this.snapMultiple;
/*  94:139 */       gridToWorld(dst, dstGrid);
/*  95:    */     }
/*  96:    */   }
/*  97:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.DraftingCoordinates
 * JD-Core Version:    0.7.0.1
 */