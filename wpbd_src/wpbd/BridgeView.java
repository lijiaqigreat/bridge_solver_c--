/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.awt.Color;
/*   4:    */ import java.awt.Graphics;
/*   5:    */ import java.awt.Graphics2D;
/*   6:    */ import java.awt.Paint;
/*   7:    */ import java.awt.Polygon;
/*   8:    */ import java.awt.geom.AffineTransform;
/*   9:    */ import java.awt.geom.Rectangle2D.Double;
/*  10:    */ import java.util.ArrayList;
/*  11:    */ import java.util.Iterator;
/*  12:    */ import javax.swing.JLabel;
/*  13:    */ 
/*  14:    */ public abstract class BridgeView
/*  15:    */ {
/*  16:    */   protected BridgeModel bridge;
/*  17:    */   protected DesignConditions conditions;
/*  18:    */   protected BridgeSketchView bridgeSketchView;
/*  19:    */   protected static final double ARCH_OFFSET = 1000.0D;
/*  20:    */   protected static final double ARCH_OFFSET_WINDOW_EDGE = 500.0D;
/*  21:    */   protected static final double INF = 100.0D;
/*  22:    */   protected static final double PIER_OFFSET = 1000.0D;
/*  23:    */   protected static final double PIER_OFFSET_WINDOW_EDGE = 500.0D;
/*  24:    */   public static final double deckCantilever = 0.32D;
/*  25:    */   public static final double wearSurfaceHeight = 0.8D;
/*  26:    */   protected static final double beamHeight = 0.9D;
/*  27:    */   public static final double abutmentInterfaceOffset = 1.0D;
/*  28: 88 */   public static final Affine.Point[] accessCurve = initializeAccessCurve();
/*  29:    */   protected static final double tangentOffset = 8.0D;
/*  30:    */   public static final double accessLength = 92.0D;
/*  31:    */   public static final double accessSlope = 0.1666666666666667D;
/*  32:    */   public static final double abutmentStepInset = -0.45D;
/*  33:    */   public static final double abutmentStepHeight = -0.35D;
/*  34:    */   public static final double abutmentStepWidth = 0.25D;
/*  35:117 */   protected static final Affine.Point[] standardAbutmentPoints = { new Affine.Point(-1.0D, 0.8D), new Affine.Point(-0.45D, 0.8D), new Affine.Point(-0.45D, -0.35D), new Affine.Point(0.25D, -0.35D), new Affine.Point(0.25D, -5.0D), new Affine.Point(0.75D, -5.0D), new Affine.Point(0.75D, -5.5D), new Affine.Point(-2.0D, -5.5D), new Affine.Point(-2.0D, -5.0D), new Affine.Point(-1.0D, -5.0D) };
/*  36:132 */   protected static final Affine.Point[] archAbutmentPoints = { new Affine.Point(-1.0D, 0.8D), new Affine.Point(-0.45D, 0.8D), new Affine.Point(-0.45D, 999.64999999999998D), new Affine.Point(0.25D, 999.64999999999998D), new Affine.Point(0.25D, 995.0D), new Affine.Point(0.75D, 995.0D), new Affine.Point(0.75D, 994.5D), new Affine.Point(-2.0D, 994.5D), new Affine.Point(-2.0D, 995.0D), new Affine.Point(-1.0D, 995.0D) };
/*  37:146 */   protected static final Affine.Point[] pierPoints = { new Affine.Point(0.5D, -0.35D), new Affine.Point(0.5D, 999.64999999999998D), new Affine.Point(0.75D, 999.64999999999998D), new Affine.Point(0.75D, 992.5D), new Affine.Point(1.4D, 992.5D), new Affine.Point(1.4D, 992.0D), new Affine.Point(-1.4D, 992.0D), new Affine.Point(-1.4D, 992.5D), new Affine.Point(-0.75D, 992.5D), new Affine.Point(-0.75D, 999.64999999999998D), new Affine.Point(-0.5D, 999.64999999999998D), new Affine.Point(-0.5D, -0.35D) };
/*  38:    */   protected static final double drawingXMargin = 3.0D;
/*  39:    */   protected static final double waterBelowGrade = 26.399999999999999D;
/*  40:    */   public static final double overheadClearance = 8.0D;
/*  41:    */   protected static final int leftShoreIndex = 25;
/*  42:    */   protected static final int rightShoreIndex = 16;
/*  43:    */   protected static final double halfNaturalGapWidth = 22.0D;
/*  44:189 */   protected static final Affine.Point[] elevationTerrainPoints = { new Affine.Point(100.0D, -100.0D), new Affine.Point(100.0D, 0.0D), new Affine.Point(25.030000000000001D, 0.0D), new Affine.Point(24.510000000000002D, -0.3D), new Affine.Point(22.75D, -0.71D), new Affine.Point(21.93D, -2.95D), new Affine.Point(21.329999999999998D, -4.75D), new Affine.Point(20.699999999999999D, -6.85D), new Affine.Point(19.559999999999999D, -7.48D), new Affine.Point(19.109999999999999D, -8.94D), new Affine.Point(18.620000000000001D, -10.81D), new Affine.Point(17.800000000000001D, -12.84D), new Affine.Point(16.219999999999999D, -14.0D), new Affine.Point(14.5D, -17.66D), new Affine.Point(12.359999999999999D, -21.329999999999998D), new Affine.Point(10.98D, -24.59D), new Affine.Point(9.58D, -26.399999999999999D), new Affine.Point(8.119999999999999D, -27.66D), new Affine.Point(6.54D, -28.629999999999999D), new Affine.Point(5.04D, -29.640000000000001D), new Affine.Point(4.48D, -29.829999999999998D), new Affine.Point(0.28D, -30.469999999999999D), new Affine.Point(-4.18D, -29.829999999999998D), new Affine.Point(-5.46D, -29.190000000000001D), new Affine.Point(-6.96D, -27.32D), new Affine.Point(-8.24D, -26.399999999999999D), new Affine.Point(-10.369999999999999D, -25.600000000000001D), new Affine.Point(-12.140000000000001D, -23.210000000000001D), new Affine.Point(-12.48D, -21.890000000000001D), new Affine.Point(-13.039999999999999D, -20.140000000000001D), new Affine.Point(-14.5D, -17.25D), new Affine.Point(-16.039999999999999D, -15.380000000000001D), new Affine.Point(-16.530000000000001D, -13.880000000000001D), new Affine.Point(-18.199999999999999D, -11.17D), new Affine.Point(-19.899999999999999D, -7.93D), new Affine.Point(-21.850000000000001D, -3.07D), new Affine.Point(-22.57D, -0.74D), new Affine.Point(-23.920000000000002D, 0.0D), new Affine.Point(-100.0D, 0.0D), new Affine.Point(-100.0D, -100.0D) };
/*  45:238 */   protected final Rectangle2D.Double drawingExtent = new Rectangle2D.Double(0.0D, -24.0D, 44.0D, 32.0D);
/*  46:    */   protected double halfCutGapWidth;
/*  47:246 */   protected final int[] leftAccessX = new int[accessCurve.length];
/*  48:250 */   protected final int[] leftAccessY = new int[accessCurve.length];
/*  49:    */   protected int nLeftAccessPoints;
/*  50:259 */   protected final int[] rightAccessX = new int[accessCurve.length];
/*  51:263 */   protected final int[] rightAccessY = new int[accessCurve.length];
/*  52:    */   protected int nRightAccessPoints;
/*  53:272 */   protected Polygon polygon = new Polygon();
/*  54:277 */   protected final Rectangle2D.Double preferredDrawingWindow = new Rectangle2D.Double(-12.0D, -29.0D, 68.0D, 42.0D);
/*  55:    */   protected int leftAbutmentInterfaceTerrainIndex;
/*  56:    */   protected int rightAbutmentInterfaceTerrainIndex;
/*  57:    */   protected double xLeftmostDeckJoint;
/*  58:    */   protected double xRightmostDeckJoint;
/*  59:    */   protected double yGradeLevel;
/*  60:    */   
/*  61:    */   protected void paintAbutmentWearSurface(Graphics2D g, int x0, int x1, int y) {}
/*  62:    */   
/*  63:    */   protected void paintDeck(Graphics2D g, ViewportTransform viewportTransform) {}
/*  64:    */   
/*  65:    */   protected void paintEarthCrossSection(Graphics2D g, ViewportTransform viewportTransform) {}
/*  66:    */   
/*  67:    */   protected void paintPier(Graphics2D g, Affine.Point location, double pierHeight, ViewportTransform viewportTransform) {}
/*  68:    */   
/*  69:    */   protected void paintArchAbutment(Graphics2D g, Affine.Point location, boolean mirror, double archHeight, ViewportTransform viewportTransform) {}
/*  70:    */   
/*  71:    */   protected void paintAnchorage(Graphics2D g, Affine.Point location, ViewportTransform viewportTransform) {}
/*  72:    */   
/*  73:    */   public void paintStandardAbutment(Graphics2D g, Affine.Point location, boolean right, int nConstraints, ViewportTransform viewportTransform) {}
/*  74:    */   
/*  75:    */   protected void paintTerrainProfile(Graphics2D g, ViewportTransform viewportTransform) {}
/*  76:    */   
/*  77:    */   protected abstract void loadPreferredDrawingWindow();
/*  78:    */   
/*  79:    */   protected void loadStandardDraftingWindow()
/*  80:    */   {
/*  81:389 */     this.preferredDrawingWindow.x = (this.drawingExtent.x - 3.0D);
/*  82:390 */     this.preferredDrawingWindow.width = (this.drawingExtent.width + 6.0D);
/*  83:391 */     if (this.conditions.isLeftAnchorage())
/*  84:    */     {
/*  85:392 */       this.preferredDrawingWindow.x -= 8.0D;
/*  86:393 */       this.preferredDrawingWindow.width += 8.0D;
/*  87:    */     }
/*  88:395 */     if (this.conditions.isRightAnchorage()) {
/*  89:396 */       this.preferredDrawingWindow.width += 8.0D;
/*  90:    */     }
/*  91:399 */     this.preferredDrawingWindow.y = (this.yGradeLevel - 26.399999999999999D - 3.5D);
/*  92:400 */     this.preferredDrawingWindow.height = (this.yGradeLevel + 8.0D + 1.0D - this.preferredDrawingWindow.y);
/*  93:    */   }
/*  94:    */   
/*  95:    */   protected abstract void paint(Graphics2D paramGraphics2D, ViewportTransform paramViewportTransform);
/*  96:    */   
/*  97:    */   protected static Affine.Point[] initializeAccessCurve()
/*  98:    */   {
/*  99:419 */     Affine.Point[] rtn = new Affine.Point[6];
/* 100:420 */     double xInc = 8.0D / (rtn.length - 2);
/* 101:421 */     double A = 0.01041666666666667D;
/* 102:422 */     double x = 0.0D;
/* 103:423 */     int i = 0;
/* 104:424 */     while (i < rtn.length - 1)
/* 105:    */     {
/* 106:425 */       rtn[i] = new Affine.Point(x, A * x * x);
/* 107:426 */       x += xInc;
/* 108:427 */       i++;
/* 109:    */     }
/* 110:429 */     rtn[i] = new Affine.Point(rtn[(i - 1)].x + 92.0D, rtn[(i - 1)].y + 15.333333333333332D);
/* 111:430 */     return rtn;
/* 112:    */   }
/* 113:    */   
/* 114:    */   public Rectangle2D.Double getDrawingExtent()
/* 115:    */   {
/* 116:439 */     return this.drawingExtent;
/* 117:    */   }
/* 118:    */   
/* 119:    */   public DesignConditions getConditions()
/* 120:    */   {
/* 121:448 */     return this.conditions;
/* 122:    */   }
/* 123:    */   
/* 124:    */   public Rectangle2D.Double getPreferredDrawingWindow()
/* 125:    */   {
/* 126:457 */     return this.preferredDrawingWindow;
/* 127:    */   }
/* 128:    */   
/* 129:    */   public BridgeModel getBridgeModel()
/* 130:    */   {
/* 131:466 */     return this.bridge;
/* 132:    */   }
/* 133:    */   
/* 134:    */   public BridgeSketchView getBridgeSketchView()
/* 135:    */   {
/* 136:475 */     return this.bridgeSketchView;
/* 137:    */   }
/* 138:    */   
/* 139:    */   public void setBridgeSketchView(BridgeSketchView bridgeSketchView)
/* 140:    */   {
/* 141:485 */     this.bridgeSketchView = bridgeSketchView;
/* 142:    */   }
/* 143:    */   
/* 144:    */   public double getLeftBankX()
/* 145:    */   {
/* 146:494 */     return this.halfCutGapWidth - 22.0D;
/* 147:    */   }
/* 148:    */   
/* 149:    */   public double getRightBankX()
/* 150:    */   {
/* 151:503 */     return this.halfCutGapWidth + 22.0D;
/* 152:    */   }
/* 153:    */   
/* 154:    */   public double getYGradeLevel()
/* 155:    */   {
/* 156:512 */     return this.yGradeLevel;
/* 157:    */   }
/* 158:    */   
/* 159:    */   private class Drawing
/* 160:    */     extends JLabel
/* 161:    */   {
/* 162:519 */     private final ViewportTransform viewportTransform = new ViewportTransform();
/* 163:520 */     private double magFactor = 1.0D;
/* 164:    */     
/* 165:    */     public Drawing(double magFactor, String noBridgeMessage)
/* 166:    */     {
/* 167:524 */       this.magFactor = magFactor;
/* 168:525 */       setText(noBridgeMessage);
/* 169:526 */       setHorizontalAlignment(0);
/* 170:527 */       setVerticalAlignment(0);
/* 171:528 */       setOpaque(true);
/* 172:    */     }
/* 173:    */     
/* 174:    */     protected void paintComponent(Graphics g0)
/* 175:    */     {
/* 176:533 */       Graphics2D g = (Graphics2D)g0;
/* 177:534 */       if (BridgeView.this.conditions == null)
/* 178:    */       {
/* 179:535 */         super.paintComponent(g0);
/* 180:536 */         return;
/* 181:    */       }
/* 182:538 */       AffineTransform savedTransform = g.getTransform();
/* 183:539 */       g.scale(1.0D / this.magFactor, 1.0D / this.magFactor);
/* 184:540 */       this.viewportTransform.setViewport(0.0D, this.magFactor * (getHeight() - 1), this.magFactor * getWidth(), -this.magFactor * getHeight());
/* 185:    */       
/* 186:    */ 
/* 187:543 */       this.viewportTransform.setWindow(BridgeView.this.preferredDrawingWindow);
/* 188:544 */       BridgeView.this.paint(g, this.viewportTransform);
/* 189:545 */       g.setTransform(savedTransform);
/* 190:546 */       g.setColor(Color.BLACK);
/* 191:547 */       g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
/* 192:    */     }
/* 193:    */   }
/* 194:    */   
/* 195:    */   public JLabel getDrawing(double magFactor)
/* 196:    */   {
/* 197:558 */     return new Drawing(magFactor, null);
/* 198:    */   }
/* 199:    */   
/* 200:    */   public JLabel getDrawing(double magFactor, String noBridgeMessage)
/* 201:    */   {
/* 202:569 */     return new Drawing(magFactor, noBridgeMessage);
/* 203:    */   }
/* 204:    */   
/* 205:    */   public void initialize(DesignConditions conditions)
/* 206:    */   {
/* 207:578 */     this.conditions = conditions;
/* 208:579 */     if (conditions == null)
/* 209:    */     {
/* 210:581 */       if (this.bridgeSketchView != null) {
/* 211:582 */         this.bridgeSketchView.setModel(null);
/* 212:    */       }
/* 213:584 */       this.drawingExtent.x = (this.drawingExtent.y = this.preferredDrawingWindow.x = this.preferredDrawingWindow.y = 0.0D);
/* 214:585 */       this.drawingExtent.width = (this.drawingExtent.height = this.preferredDrawingWindow.width = this.preferredDrawingWindow.height = 1.0D);
/* 215:586 */       return;
/* 216:    */     }
/* 217:590 */     if (this.bridgeSketchView != null)
/* 218:    */     {
/* 219:591 */       BridgeSketchModel model = this.bridgeSketchView.getModel();
/* 220:592 */       if ((model != null) && (!model.getDesignConditions().isGeometricallyIdentical(conditions))) {
/* 221:593 */         this.bridgeSketchView.setModel(null);
/* 222:    */       }
/* 223:    */     }
/* 224:598 */     this.xLeftmostDeckJoint = conditions.getXLeftmostDeckJoint();
/* 225:599 */     this.xRightmostDeckJoint = conditions.getXRightmostDeckJoint();
/* 226:600 */     this.yGradeLevel = (24.0D - conditions.getDeckElevation() + 0.8D);
/* 227:601 */     this.halfCutGapWidth = (0.5D * (this.xRightmostDeckJoint - this.xLeftmostDeckJoint));
/* 228:    */     
/* 229:    */ 
/* 230:    */ 
/* 231:    */ 
/* 232:606 */     this.leftAbutmentInterfaceTerrainIndex = (elevationTerrainPoints.length - 1);
/* 233:607 */     while (elevationTerrainPoints[this.leftAbutmentInterfaceTerrainIndex].x + this.halfCutGapWidth < this.xLeftmostDeckJoint - 1.0D) {
/* 234:608 */       this.leftAbutmentInterfaceTerrainIndex -= 1;
/* 235:    */     }
/* 236:610 */     this.rightAbutmentInterfaceTerrainIndex = 0;
/* 237:611 */     while (elevationTerrainPoints[this.rightAbutmentInterfaceTerrainIndex].x + this.halfCutGapWidth > this.xRightmostDeckJoint + 1.0D) {
/* 238:612 */       this.rightAbutmentInterfaceTerrainIndex += 1;
/* 239:    */     }
/* 240:614 */     assert (this.leftAbutmentInterfaceTerrainIndex > this.rightAbutmentInterfaceTerrainIndex);
/* 241:    */     
/* 242:    */ 
/* 243:617 */     this.drawingExtent.x = this.xLeftmostDeckJoint;
/* 244:618 */     this.drawingExtent.y = (-conditions.getUnderClearance());
/* 245:619 */     this.drawingExtent.width = conditions.getSpanLength();
/* 246:620 */     this.drawingExtent.height = (conditions.getUnderClearance() + conditions.getOverClearance());
/* 247:    */     
/* 248:    */ 
/* 249:623 */     loadPreferredDrawingWindow();
/* 250:    */   }
/* 251:    */   
/* 252:    */   protected void paintStandardAbutment(Graphics2D g, Paint fillPaint, Color strokePaint, Affine.Point location, boolean mirror, ViewportTransform viewportTransform)
/* 253:    */   {
/* 254:638 */     this.polygon.npoints = 0;
/* 255:639 */     for (int i = 0; i < standardAbutmentPoints.length; i++)
/* 256:    */     {
/* 257:640 */       double xWorldAbutment = mirror ? -standardAbutmentPoints[i].x : standardAbutmentPoints[i].x;
/* 258:641 */       this.polygon.addPoint(viewportTransform.worldToViewportX(location.x + xWorldAbutment), viewportTransform.worldToViewportY(location.y + standardAbutmentPoints[i].y));
/* 259:    */     }
/* 260:643 */     g.setPaint(fillPaint);
/* 261:644 */     g.fill(this.polygon);
/* 262:645 */     g.setPaint(strokePaint);
/* 263:646 */     g.draw(this.polygon);
/* 264:647 */     g.setColor(strokePaint);
/* 265:648 */     if (this.polygon.xpoints[0] < this.polygon.xpoints[1]) {
/* 266:649 */       paintAbutmentWearSurface(g, this.polygon.xpoints[0], this.polygon.xpoints[1], this.polygon.ypoints[1]);
/* 267:    */     } else {
/* 268:652 */       paintAbutmentWearSurface(g, this.polygon.xpoints[1], this.polygon.xpoints[0], this.polygon.ypoints[1]);
/* 269:    */     }
/* 270:    */   }
/* 271:    */   
/* 272:    */   protected void paintArchAbutment(Graphics2D g, Paint fillPaint, Color strokePaint, Affine.Point location, boolean mirror, double archHeight, ViewportTransform viewportTransform)
/* 273:    */   {
/* 274:669 */     this.polygon.npoints = 0;
/* 275:670 */     for (int i = 0; i < archAbutmentPoints.length; i++)
/* 276:    */     {
/* 277:671 */       double xWorldAbutment = mirror ? -archAbutmentPoints[i].x : archAbutmentPoints[i].x;
/* 278:672 */       double yWorldAbutment = archAbutmentPoints[i].y > 500.0D ? archAbutmentPoints[i].y - 1000.0D + archHeight : archAbutmentPoints[i].y;
/* 279:673 */       this.polygon.addPoint(viewportTransform.worldToViewportX(location.x + xWorldAbutment), viewportTransform.worldToViewportY(location.y + yWorldAbutment));
/* 280:    */     }
/* 281:675 */     g.setPaint(fillPaint);
/* 282:676 */     g.fill(this.polygon);
/* 283:677 */     g.setPaint(strokePaint);
/* 284:678 */     g.draw(this.polygon);
/* 285:679 */     g.setColor(strokePaint);
/* 286:680 */     if (this.polygon.xpoints[0] < this.polygon.xpoints[1]) {
/* 287:681 */       paintAbutmentWearSurface(g, this.polygon.xpoints[0], this.polygon.xpoints[1], this.polygon.ypoints[1]);
/* 288:    */     } else {
/* 289:684 */       paintAbutmentWearSurface(g, this.polygon.xpoints[1], this.polygon.xpoints[0], this.polygon.ypoints[1]);
/* 290:    */     }
/* 291:    */   }
/* 292:    */   
/* 293:    */   protected void paintPier(Graphics2D g, Paint fillPaint, Color strokePaint, Affine.Point location, double pierHeight, ViewportTransform viewportTransform)
/* 294:    */   {
/* 295:700 */     this.polygon.npoints = 0;
/* 296:701 */     for (int i = 0; i < pierPoints.length; i++)
/* 297:    */     {
/* 298:702 */       double xWorldPier = pierPoints[i].x;
/* 299:703 */       double yWorldPier = pierPoints[i].y > 500.0D ? pierPoints[i].y - 1000.0D - pierHeight : pierPoints[i].y;
/* 300:704 */       this.polygon.addPoint(viewportTransform.worldToViewportX(location.x + xWorldPier), viewportTransform.worldToViewportY(location.y + yWorldPier));
/* 301:    */     }
/* 302:706 */     g.setPaint(fillPaint);
/* 303:707 */     g.fill(this.polygon);
/* 304:708 */     g.setPaint(strokePaint);
/* 305:709 */     g.draw(this.polygon);
/* 306:    */   }
/* 307:    */   
/* 308:    */   protected void setEarthProfilePolygonAndAccesses(ViewportTransform viewportTransform)
/* 309:    */   {
/* 310:720 */     int x = 0;
/* 311:721 */     int y = 0;
/* 312:722 */     this.polygon.npoints = 0;
/* 313:    */     
/* 314:724 */     double xBase = this.xLeftmostDeckJoint - 1.0D;
/* 315:725 */     if (this.conditions.isAtGrade())
/* 316:    */     {
/* 317:726 */       this.nLeftAccessPoints = 2; int 
/* 318:727 */         tmp46_43 = viewportTransform.worldToViewportX(xBase);x = tmp46_43;this.leftAccessX[0] = tmp46_43; int 
/* 319:728 */         tmp61_58 = viewportTransform.worldToViewportY(0.8D);y = tmp61_58;this.leftAccessY[0] = tmp61_58;
/* 320:729 */       this.polygon.addPoint(x, y); int 
/* 321:730 */         tmp92_89 = viewportTransform.worldToViewportX(xBase - 8.0D - 92.0D);x = tmp92_89;this.leftAccessX[1] = tmp92_89;
/* 322:731 */       this.leftAccessY[1] = y;
/* 323:732 */       this.polygon.addPoint(x, y);
/* 324:    */     }
/* 325:    */     else
/* 326:    */     {
/* 327:734 */       this.nLeftAccessPoints = accessCurve.length;
/* 328:735 */       for (int i = 0; i < this.nLeftAccessPoints; i++)
/* 329:    */       {
/* 330:736 */         int tmp156_153 = viewportTransform.worldToViewportX(xBase - accessCurve[i].x);x = tmp156_153;this.leftAccessX[i] = tmp156_153; int 
/* 331:737 */           tmp182_179 = viewportTransform.worldToViewportY(0.8D + accessCurve[i].y);y = tmp182_179;this.leftAccessY[i] = tmp182_179;
/* 332:738 */         this.polygon.addPoint(x, y);
/* 333:    */       }
/* 334:    */     }
/* 335:742 */     y = viewportTransform.worldToViewportY(-100.0D);
/* 336:743 */     this.polygon.addPoint(x, y);
/* 337:    */     
/* 338:745 */     xBase = this.xRightmostDeckJoint + 1.0D;
/* 339:746 */     x = viewportTransform.worldToViewportX(xBase + 8.0D + 92.0D);
/* 340:747 */     this.polygon.addPoint(x, y);
/* 341:749 */     if (this.conditions.isAtGrade())
/* 342:    */     {
/* 343:750 */       this.nRightAccessPoints = 2;
/* 344:751 */       this.rightAccessX[0] = x; int 
/* 345:752 */         tmp283_280 = viewportTransform.worldToViewportY(0.8D);y = tmp283_280;this.rightAccessY[0] = tmp283_280;
/* 346:753 */       this.polygon.addPoint(x, y); int 
/* 347:754 */         tmp306_303 = viewportTransform.worldToViewportX(xBase);x = tmp306_303;this.rightAccessX[1] = tmp306_303;
/* 348:755 */       this.rightAccessY[1] = y;
/* 349:756 */       this.polygon.addPoint(x, y);
/* 350:    */     }
/* 351:    */     else
/* 352:    */     {
/* 353:758 */       this.nRightAccessPoints = accessCurve.length;
/* 354:759 */       for (int i = this.nRightAccessPoints - 1; i >= 0; i--)
/* 355:    */       {
/* 356:760 */         int tmp371_368 = viewportTransform.worldToViewportX(xBase + accessCurve[i].x);x = tmp371_368;this.rightAccessX[i] = tmp371_368; int 
/* 357:761 */           tmp397_394 = viewportTransform.worldToViewportY(0.8D + accessCurve[i].y);y = tmp397_394;this.rightAccessY[i] = tmp397_394;
/* 358:762 */         this.polygon.addPoint(x, y);
/* 359:    */       }
/* 360:    */     }
/* 361:766 */     y = viewportTransform.worldToViewportY(elevationTerrainPoints[this.rightAbutmentInterfaceTerrainIndex].y + this.yGradeLevel);
/* 362:767 */     this.polygon.addPoint(x, y);
/* 363:769 */     for (int i = this.rightAbutmentInterfaceTerrainIndex; i <= this.leftAbutmentInterfaceTerrainIndex; i++)
/* 364:    */     {
/* 365:770 */       x = viewportTransform.worldToViewportX(elevationTerrainPoints[i].x + this.halfCutGapWidth);
/* 366:771 */       y = viewportTransform.worldToViewportY(elevationTerrainPoints[i].y + this.yGradeLevel);
/* 367:772 */       this.polygon.addPoint(x, y);
/* 368:    */     }
/* 369:775 */     x = viewportTransform.worldToViewportX(this.xLeftmostDeckJoint - 1.0D);
/* 370:776 */     this.polygon.addPoint(x, y);
/* 371:    */   }
/* 372:    */   
/* 373:    */   public Affine.Point getPierLocation()
/* 374:    */   {
/* 375:785 */     return this.conditions.getPrescribedJointLocation(this.conditions.getPierJointIndex());
/* 376:    */   }
/* 377:    */   
/* 378:    */   protected void paintAbutmentsAndPier(Graphics2D g, ViewportTransform viewportTransform)
/* 379:    */   {
/* 380:795 */     Affine.Point pLeft = this.conditions.getPrescribedJoint(0).getPointWorld();
/* 381:796 */     Affine.Point pRight = this.conditions.getPrescribedJointLocation(this.conditions.getNLoadedJoints() - 1);
/* 382:797 */     if (this.conditions.isArch())
/* 383:    */     {
/* 384:798 */       double archHeight = -this.conditions.getUnderClearance();
/* 385:799 */       paintArchAbutment(g, pLeft, false, archHeight, viewportTransform);
/* 386:800 */       paintArchAbutment(g, pRight, true, archHeight, viewportTransform);
/* 387:    */     }
/* 388:    */     else
/* 389:    */     {
/* 390:802 */       paintStandardAbutment(g, pLeft, false, this.conditions.isHiPier() ? 1 : 2, viewportTransform);
/* 391:803 */       paintStandardAbutment(g, pRight, true, 1, viewportTransform);
/* 392:    */     }
/* 393:805 */     if (this.conditions.isPier()) {
/* 394:806 */       paintPier(g, getPierLocation(), this.conditions.getPierHeight(), viewportTransform);
/* 395:    */     }
/* 396:    */   }
/* 397:    */   
/* 398:    */   protected void paintAnchorages(Graphics2D g, ViewportTransform viewportTransform)
/* 399:    */   {
/* 400:817 */     if (this.conditions.isLeftAnchorage()) {
/* 401:818 */       paintAnchorage(g, this.conditions.getPrescribedJointLocation(this.conditions.getLeftAnchorageJointIndex()), viewportTransform);
/* 402:    */     }
/* 403:820 */     if (this.conditions.isRightAnchorage()) {
/* 404:821 */       paintAnchorage(g, this.conditions.getPrescribedJointLocation(this.conditions.getRightAnchorageJointIndex()), viewportTransform);
/* 405:    */     }
/* 406:    */   }
/* 407:    */   
/* 408:828 */   private final BridgePaintContext defaultPaintContext = new BridgePaintContext();
/* 409:    */   
/* 410:    */   public void paintBridge(Graphics2D g, ViewportTransform viewportTransform, BridgePaintContext ctx)
/* 411:    */   {
/* 412:839 */     if (this.bridge != null)
/* 413:    */     {
/* 414:840 */       if (ctx == null) {
/* 415:841 */         ctx = this.defaultPaintContext;
/* 416:    */       }
/* 417:843 */       Iterator<Member> me = this.bridge.getMembers().iterator();
/* 418:844 */       while (me.hasNext()) {
/* 419:845 */         ((Member)me.next()).paint(g, viewportTransform, ctx);
/* 420:    */       }
/* 421:847 */       Iterator<Joint> je = this.bridge.getJoints().iterator();
/* 422:848 */       while (je.hasNext()) {
/* 423:849 */         ((Joint)je.next()).paint(g, viewportTransform, ctx);
/* 424:    */       }
/* 425:    */     }
/* 426:    */   }
/* 427:    */   
/* 428:    */   protected void paintBridgeSketch(Graphics2D g, ViewportTransform viewportTransform)
/* 429:    */   {
/* 430:861 */     if (this.bridgeSketchView != null) {
/* 431:862 */       this.bridgeSketchView.paint(g, viewportTransform);
/* 432:    */     }
/* 433:    */   }
/* 434:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.BridgeView
 * JD-Core Version:    0.7.0.1
 */