/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.awt.BasicStroke;
/*   4:    */ import java.awt.Color;
/*   5:    */ import java.awt.Graphics2D;
/*   6:    */ import java.awt.GraphicsConfiguration;
/*   7:    */ import java.awt.GraphicsDevice;
/*   8:    */ import java.awt.GraphicsEnvironment;
/*   9:    */ import java.awt.Image;
/*  10:    */ import java.awt.Polygon;
/*  11:    */ import java.awt.Rectangle;
/*  12:    */ import java.awt.Stroke;
/*  13:    */ import java.awt.TexturePaint;
/*  14:    */ import java.awt.geom.Rectangle2D.Double;
/*  15:    */ import java.awt.image.BufferedImage;
/*  16:    */ import org.jdesktop.application.ResourceMap;
/*  17:    */ 
/*  18:    */ public class BridgeCartoonView
/*  19:    */   extends BridgeView
/*  20:    */ {
/*  21: 42 */   protected final Color skyColor = new Color(192, 255, 255);
/*  22: 46 */   protected final Color concreteColor = new Color(153, 153, 153);
/*  23: 50 */   protected final Color earthColor = new Color(220, 208, 188);
/*  24: 54 */   protected final TexturePaint excavatationPaint = initializeExcavation();
/*  25: 58 */   protected final BasicStroke wearSurfaceStroke = new BasicStroke(2.0F, 0, 0);
/*  26: 62 */   protected final Rectangle slab = new Rectangle();
/*  27: 66 */   protected final Image titleBlock = WPBDApp.getApplication().getImageResource("titleblock.png");
/*  28:    */   public static final int jointRadius = 2;
/*  29:    */   public static final int MODE_TERRAIN_ONLY = 0;
/*  30:    */   public static final int MODE_MEASUREMENTS = 1;
/*  31:    */   public static final int MODE_STANDARD_ITEMS = 2;
/*  32:    */   public static final int MODE_TITLE_BLOCK = 4;
/*  33:    */   public static final int MODE_JOINTS = 8;
/*  34: 94 */   private int mode = 2;
/*  35:    */   
/*  36:    */   public BridgeCartoonView()
/*  37:    */   {
/*  38:100 */     this.bridgeSketchView = new BridgeSketchCartoonView();
/*  39:    */   }
/*  40:    */   
/*  41:    */   public BridgeCartoonView(BridgeModel bridge)
/*  42:    */   {
/*  43:109 */     this();
/*  44:110 */     this.bridge = bridge;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public void setMode(int mode)
/*  48:    */   {
/*  49:120 */     this.mode = mode;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public void paint(Graphics2D g, ViewportTransform viewportTransform)
/*  53:    */   {
/*  54:124 */     g.setColor(this.skyColor);
/*  55:125 */     g.fillRect(0, 0, viewportTransform.getAbsWidthViewport(), viewportTransform.getAbsHeightViewport());
/*  56:126 */     if (this.conditions != null)
/*  57:    */     {
/*  58:127 */       paintTerrainProfile(g, viewportTransform);
/*  59:128 */       if ((this.mode & 0x1) != 0) {
/*  60:129 */         paintMeasurements(g, viewportTransform);
/*  61:    */       }
/*  62:131 */       if ((this.mode & 0x2) != 0)
/*  63:    */       {
/*  64:132 */         paintEarthCrossSection(g, viewportTransform);
/*  65:133 */         paintAbutmentsAndPier(g, viewportTransform);
/*  66:134 */         paintDeck(g, viewportTransform);
/*  67:135 */         paintBridgeSketch(g, viewportTransform);
/*  68:136 */         paintBridge(g, viewportTransform, null);
/*  69:    */       }
/*  70:138 */       if ((this.mode & 0x4) != 0) {
/*  71:139 */         g.drawImage(this.titleBlock, viewportTransform.getAbsWidthViewport() - this.titleBlock.getWidth(null) - 4, viewportTransform.getAbsHeightViewport() - this.titleBlock.getHeight(null) - 4, null);
/*  72:    */       }
/*  73:    */     }
/*  74:    */   }
/*  75:    */   
/*  76:    */   private void paintMeasurements(Graphics2D g, ViewportTransform viewportTransform)
/*  77:    */   {
/*  78:148 */     ResourceMap resourceMap = WPBDApp.getResourceMap(BridgeCartoonView.class);
/*  79:149 */     g.setColor(Color.GRAY);
/*  80:    */     
/*  81:151 */     int yGrade = viewportTransform.worldToViewportY(this.yGradeLevel);
/*  82:    */     
/*  83:153 */     int yWater = viewportTransform.worldToViewportY(this.yGradeLevel - 24.0D);
/*  84:    */     
/*  85:155 */     int xGapLeft = viewportTransform.worldToViewportX(getLeftBankX());
/*  86:156 */     int xGapRight = viewportTransform.worldToViewportX(getRightBankX());
/*  87:157 */     int xGapMiddle = (xGapLeft + xGapRight) / 2;
/*  88:    */     
/*  89:159 */     int tickHalfSize = 3;
/*  90:    */     
/*  91:161 */     int yGapDim = yGrade - 20;
/*  92:162 */     int yTickTop = yGapDim - 3;
/*  93:163 */     int yTickBottom = yGrade + 3;
/*  94:164 */     Utility.drawDoubleArrow(g, xGapLeft, yGapDim, xGapRight, yGapDim);
/*  95:165 */     g.drawLine(xGapLeft, yTickTop, xGapLeft, yTickBottom);
/*  96:166 */     g.drawLine(xGapRight, yTickTop, xGapRight, yTickBottom);
/*  97:167 */     Labeler.drawJustified(g, resourceMap.getString("gapDimension.text", new Object[0]), xGapMiddle, yGapDim - 3, 2, 6, null);
/*  98:    */     
/*  99:    */ 
/* 100:170 */     int xGapHeightDim = xGapMiddle - 40;
/* 101:171 */     Utility.drawDoubleArrow(g, xGapHeightDim, yGrade, xGapHeightDim, yWater);
/* 102:172 */     int yAir = (yGrade + yWater) / 2;
/* 103:173 */     Labeler.drawJustified(g, resourceMap.getString("gapHeightDimension.text", new Object[0]), xGapHeightDim + 3, yAir, 1, 2, null);
/* 104:    */     
/* 105:175 */     g.drawLine(xGapHeightDim - 3, yWater, xGapHeightDim + 3, yWater);
/* 106:176 */     g.drawLine(xGapLeft - 3, yGrade, xGapHeightDim + 3, yGrade);
/* 107:    */     
/* 108:178 */     int xSlopeIcon = xGapMiddle + 75;
/* 109:179 */     int widthSlopeIcon = 24;
/* 110:180 */     int heightSlopeIcon = 48;
/* 111:181 */     int ySlopeIcon = yAir + 24;
/* 112:182 */     int xSlopeIconTop = xSlopeIcon + 24;
/* 113:183 */     int ySlopeIconTop = ySlopeIcon - 48;
/* 114:184 */     int widthSlopeIconTail = 4;
/* 115:185 */     g.drawLine(xSlopeIcon, ySlopeIcon, xSlopeIcon, ySlopeIconTop);
/* 116:186 */     Labeler.drawJustified(g, "2", xSlopeIcon - 3, yAir, 3, 2, null);
/* 117:187 */     g.drawLine(xSlopeIcon, ySlopeIconTop, xSlopeIconTop, ySlopeIconTop);
/* 118:188 */     Labeler.drawJustified(g, "1", xSlopeIcon + 12, ySlopeIconTop - 3, 2, 6, null);
/* 119:    */     
/* 120:190 */     g.drawLine(xSlopeIcon - 4, ySlopeIcon + 8, xSlopeIconTop + 4, ySlopeIconTop - 8);
/* 121:    */   }
/* 122:    */   
/* 123:    */   public void paintStandardAbutment(Graphics2D g, Affine.Point location, boolean mirror, int nConstraints, ViewportTransform viewportTransform)
/* 124:    */   {
/* 125:196 */     paintStandardAbutment(g, this.concreteColor, Color.BLACK, location, mirror, viewportTransform);
/* 126:    */   }
/* 127:    */   
/* 128:    */   public void paintArchAbutment(Graphics2D g, Affine.Point location, boolean mirror, double arch_height, ViewportTransform viewportTransform)
/* 129:    */   {
/* 130:201 */     paintArchAbutment(g, this.concreteColor, Color.BLACK, location, mirror, arch_height, viewportTransform);
/* 131:    */   }
/* 132:    */   
/* 133:    */   public void paintPier(Graphics2D g, Affine.Point location, double pier_height, ViewportTransform viewportTransform)
/* 134:    */   {
/* 135:206 */     paintPier(g, this.concreteColor, Color.BLACK, location, pier_height, viewportTransform);
/* 136:    */   }
/* 137:    */   
/* 138:    */   protected void paintAbutmentWearSurface(Graphics2D g, int x0, int x1, int y)
/* 139:    */   {
/* 140:211 */     Stroke savedStroke = g.getStroke();
/* 141:212 */     g.setStroke(this.wearSurfaceStroke);
/* 142:213 */     g.drawLine(x0, y, x1 + 1, y);
/* 143:214 */     g.setStroke(savedStroke);
/* 144:    */   }
/* 145:    */   
/* 146:    */   protected void paintDeck(Graphics2D g, ViewportTransform viewportTransform)
/* 147:    */   {
/* 148:219 */     int ySlabTop = viewportTransform.worldToViewportY(0.8D);
/* 149:220 */     int ySlabBottom = ySlabTop + 2;
/* 150:221 */     int yBeamBottom = ySlabBottom + 3;
/* 151:    */     
/* 152:223 */     int xSlabLeft = viewportTransform.worldToViewportX(this.conditions.getXLeftmostDeckJoint() - 0.32D);
/* 153:224 */     int xSlabRight = viewportTransform.worldToViewportX(this.conditions.getXRightmostDeckJoint() + 0.32D);
/* 154:    */     
/* 155:226 */     this.slab.setFrameFromDiagonal(xSlabLeft, ySlabBottom, xSlabRight, ySlabTop);
/* 156:227 */     g.setPaint(Color.WHITE);
/* 157:228 */     g.fill(this.slab);
/* 158:229 */     g.setPaint(Color.BLACK);
/* 159:230 */     g.draw(this.slab);
/* 160:231 */     Stroke savedStroke = g.getStroke();
/* 161:232 */     g.setStroke(this.wearSurfaceStroke);
/* 162:    */     
/* 163:234 */     g.drawLine(xSlabLeft, ySlabTop, xSlabRight + 1, ySlabTop);
/* 164:235 */     g.setStroke(savedStroke);
/* 165:237 */     for (int i = 0; i < this.conditions.getNLoadedJoints(); i++)
/* 166:    */     {
/* 167:238 */       int x = viewportTransform.worldToViewportX(this.conditions.getPrescribedJointLocation(i).x);
/* 168:239 */       g.drawLine(x, ySlabTop, x, yBeamBottom);
/* 169:240 */       if ((this.mode & 0x8) != 0)
/* 170:    */       {
/* 171:241 */         g.setColor(Color.WHITE);
/* 172:242 */         g.fillOval(x - 2, ySlabBottom, 4, 4);
/* 173:243 */         g.setColor(Color.BLACK);
/* 174:244 */         g.drawOval(x - 2, ySlabBottom, 4, 4);
/* 175:    */       }
/* 176:    */     }
/* 177:249 */     for (int i = this.conditions.getNLoadedJoints(); i < this.conditions.getNPrescribedJoints(); i++) {
/* 178:250 */       if (((this.mode & 0x8) != 0) || (i == this.conditions.getLeftAnchorageJointIndex()) || (i == this.conditions.getRightAnchorageJointIndex())) {
/* 179:251 */         drawJoint(g, viewportTransform, this.conditions.getPrescribedJointLocation(i));
/* 180:    */       }
/* 181:    */     }
/* 182:    */   }
/* 183:    */   
/* 184:    */   private void drawJoint(Graphics2D g, ViewportTransform viewportTransform, Affine.Point pt)
/* 185:    */   {
/* 186:257 */     int x = viewportTransform.worldToViewportX(pt.x);
/* 187:258 */     int y = viewportTransform.worldToViewportY(pt.y);
/* 188:259 */     g.setColor(Color.WHITE);
/* 189:260 */     g.fillOval(x - 2, y - 2, 4, 4);
/* 190:261 */     g.setColor(Color.BLACK);
/* 191:262 */     g.drawOval(x - 2, y - 2, 4, 4);
/* 192:    */   }
/* 193:    */   
/* 194:    */   protected void paintEarthCrossSection(Graphics2D g, ViewportTransform viewportTransform)
/* 195:    */   {
/* 196:267 */     setEarthProfilePolygonAndAccesses(viewportTransform);
/* 197:268 */     g.setPaint(this.earthColor);
/* 198:269 */     g.fill(this.polygon);
/* 199:270 */     g.setPaint(Color.BLACK);
/* 200:271 */     g.draw(this.polygon);
/* 201:272 */     Stroke savedStroke = g.getStroke();
/* 202:273 */     g.setStroke(this.wearSurfaceStroke);
/* 203:274 */     for (int i = 1; i < this.nLeftAccessPoints; i++) {
/* 204:275 */       g.drawLine(this.leftAccessX[(i - 1)], this.leftAccessY[(i - 1)], this.leftAccessX[i], this.leftAccessY[i]);
/* 205:    */     }
/* 206:277 */     for (int i = 1; i < this.nRightAccessPoints; i++) {
/* 207:278 */       g.drawLine(this.rightAccessX[(i - 1)], this.rightAccessY[(i - 1)], this.rightAccessX[i], this.rightAccessY[i]);
/* 208:    */     }
/* 209:280 */     g.setStroke(savedStroke);
/* 210:    */   }
/* 211:    */   
/* 212:    */   protected void paintTerrainProfile(Graphics2D g, ViewportTransform viewportTransform)
/* 213:    */   {
/* 214:285 */     this.polygon.npoints = 0;
/* 215:286 */     for (int i = 16; i <= 25; i++)
/* 216:    */     {
/* 217:287 */       int x = viewportTransform.worldToViewportX(elevationTerrainPoints[i].x + this.halfCutGapWidth);
/* 218:288 */       int y = viewportTransform.worldToViewportY(elevationTerrainPoints[i].y + this.yGradeLevel);
/* 219:289 */       this.polygon.addPoint(x, y);
/* 220:    */     }
/* 221:291 */     g.setPaint(Color.BLUE);
/* 222:292 */     g.fill(this.polygon);
/* 223:293 */     this.polygon.npoints = 0;
/* 224:294 */     for (int i = 0; i < elevationTerrainPoints.length; i++) {
/* 225:295 */       this.polygon.addPoint(viewportTransform.worldToViewportX(elevationTerrainPoints[i].x + this.halfCutGapWidth), viewportTransform.worldToViewportY(elevationTerrainPoints[i].y + this.yGradeLevel));
/* 226:    */     }
/* 227:299 */     if ((this.mode & 0x2) != 0) {
/* 228:300 */       g.setPaint(this.excavatationPaint);
/* 229:    */     } else {
/* 230:302 */       g.setPaint(this.earthColor);
/* 231:    */     }
/* 232:304 */     g.fill(this.polygon);
/* 233:305 */     g.setPaint(Color.BLACK);
/* 234:306 */     g.draw(this.polygon);
/* 235:307 */     if (((this.mode & 0x2) != 0) && (this.conditions.isArch()) && (this.bridgeSketchView.getModel() == null))
/* 236:    */     {
/* 237:309 */       g.setColor(Color.LIGHT_GRAY);
/* 238:310 */       int iArchJoints = this.conditions.getArchJointIndex();
/* 239:311 */       Affine.Point p1 = this.conditions.getPrescribedJointLocation(iArchJoints);
/* 240:312 */       Affine.Point p2 = this.conditions.getPrescribedJointLocation(this.conditions.getNPanels() / 2);
/* 241:313 */       Affine.Point p3 = this.conditions.getPrescribedJointLocation(iArchJoints + 1);
/* 242:314 */       double xMid = 0.5D * (p1.x + p3.x);
/* 243:315 */       double x1 = p1.x - xMid;
/* 244:316 */       double y1 = p1.y;
/* 245:317 */       double x2 = p2.x - xMid;
/* 246:318 */       double y2 = p2.y - 0.25D * (p2.y - p1.y);
/* 247:319 */       double a = (y2 - y1) / (x2 * x2 - x1 * x1);
/* 248:320 */       double b = y1 - a * x1 * x1;
/* 249:    */       
/* 250:322 */       double xp = p1.x;
/* 251:323 */       double yp = p1.y;
/* 252:324 */       int ixq = viewportTransform.worldToViewportX(xp);
/* 253:325 */       int iyq = viewportTransform.worldToViewportY(yp);
/* 254:326 */       while (xp < p3.x)
/* 255:    */       {
/* 256:327 */         xp += 2.0D;
/* 257:328 */         double x = xp - xMid;
/* 258:329 */         yp = a * x * x + b;
/* 259:330 */         int ixp = viewportTransform.worldToViewportX(xp);
/* 260:331 */         int iyp = viewportTransform.worldToViewportY(yp);
/* 261:332 */         g.drawLine(ixq, iyq, ixp, iyp);
/* 262:333 */         ixq = ixp;
/* 263:334 */         iyq = iyp;
/* 264:    */       }
/* 265:    */     }
/* 266:    */   }
/* 267:    */   
/* 268:    */   private TexturePaint initializeExcavation()
/* 269:    */   {
/* 270:345 */     int size = 8;
/* 271:346 */     GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
/* 272:347 */     GraphicsDevice gs = e.getDefaultScreenDevice();
/* 273:348 */     GraphicsConfiguration gc = gs.getDefaultConfiguration();
/* 274:349 */     BufferedImage hatch = gc.createCompatibleImage(8, 8, 1);
/* 275:350 */     Graphics2D g = hatch.createGraphics();
/* 276:351 */     g.setColor(Color.WHITE);
/* 277:352 */     g.fillRect(0, 0, 8, 8);
/* 278:353 */     g.setColor(new Color(128, 64, 64));
/* 279:354 */     g.drawLine(0, 0, 7, 7);
/* 280:355 */     return new TexturePaint(hatch, new Rectangle(0, 0, 8, 8));
/* 281:    */   }
/* 282:    */   
/* 283:    */   protected void loadPreferredDrawingWindow()
/* 284:    */   {
/* 285:360 */     double xMargin = 3.0D + (44.0D - this.drawingExtent.width) / 2.0D + 8.0D;
/* 286:361 */     this.preferredDrawingWindow.x = (this.drawingExtent.x - xMargin);
/* 287:362 */     this.preferredDrawingWindow.width = (this.drawingExtent.width + 2.0D * xMargin);
/* 288:    */     
/* 289:364 */     this.preferredDrawingWindow.y = (this.yGradeLevel - 26.399999999999999D - 3.5D);
/* 290:365 */     this.preferredDrawingWindow.height = (this.yGradeLevel + 8.0D + 1.0D - this.preferredDrawingWindow.y);
/* 291:    */   }
/* 292:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.BridgeCartoonView
 * JD-Core Version:    0.7.0.1
 */