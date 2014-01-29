/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.awt.BasicStroke;
/*   4:    */ import java.awt.Color;
/*   5:    */ import java.awt.Graphics2D;
/*   6:    */ import java.awt.GraphicsConfiguration;
/*   7:    */ import java.awt.GraphicsDevice;
/*   8:    */ import java.awt.GraphicsEnvironment;
/*   9:    */ import java.awt.Polygon;
/*  10:    */ import java.awt.Rectangle;
/*  11:    */ import java.awt.Stroke;
/*  12:    */ import java.awt.TexturePaint;
/*  13:    */ import java.awt.image.BufferedImage;
/*  14:    */ import java.util.Random;
/*  15:    */ 
/*  16:    */ public class BridgeDraftingView
/*  17:    */   extends BridgeView
/*  18:    */ {
/*  19: 25 */   protected final Color earthColor = new Color(128, 64, 64);
/*  20: 29 */   protected final Color concreteColor = new Color(128, 128, 0);
/*  21: 33 */   protected static final float[] terrainProfileDashes = { 4.0F, 3.0F };
/*  22: 37 */   protected static final BasicStroke terrainProfileStroke = new BasicStroke(0.0F, 0, 0, 10.0F, terrainProfileDashes, 0.0F);
/*  23: 47 */   protected static final Stroke wearSurfaceStroke = new BasicStroke(3.0F, 0, 0);
/*  24: 51 */   protected static final Stroke beamStroke = new BasicStroke(3.0F, 0, 0);
/*  25: 55 */   protected final TexturePaint earthPaint = initializeEarth();
/*  26: 59 */   protected final TexturePaint concretePaint = initializeConcrete();
/*  27: 63 */   protected final TexturePaint subgradePaint = initializeSubgrade();
/*  28: 67 */   protected boolean label = false;
/*  29: 71 */   protected boolean templateVisible = true;
/*  30:    */   
/*  31:    */   public BridgeDraftingView()
/*  32:    */   {
/*  33: 77 */     this.bridgeSketchView = new BridgeSketchDraftingView();
/*  34:    */   }
/*  35:    */   
/*  36:    */   public BridgeDraftingView(BridgeModel bridge)
/*  37:    */   {
/*  38: 86 */     this();
/*  39: 87 */     this.bridge = bridge;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public void setLabel(boolean label)
/*  43:    */   {
/*  44: 96 */     this.label = label;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public boolean isLabel()
/*  48:    */   {
/*  49:105 */     return this.label;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public void setTemplateVisible(boolean templateVisible)
/*  53:    */   {
/*  54:114 */     this.templateVisible = templateVisible;
/*  55:    */   }
/*  56:    */   
/*  57:117 */   private final BridgePaintContext ctx = new BridgePaintContext();
/*  58:    */   
/*  59:    */   public void paint(Graphics2D g, ViewportTransform viewportTransform)
/*  60:    */   {
/*  61:120 */     int w = viewportTransform.getAbsWidthViewport();
/*  62:121 */     int h = viewportTransform.getAbsHeightViewport();
/*  63:122 */     g.setColor(Color.WHITE);
/*  64:123 */     g.fillRect(0, 0, w, h);
/*  65:124 */     if (this.conditions != null)
/*  66:    */     {
/*  67:125 */       paintDeck(g, viewportTransform);
/*  68:126 */       paintEarthCrossSection(g, viewportTransform);
/*  69:127 */       paintTerrainProfile(g, viewportTransform);
/*  70:128 */       paintAbutmentsAndPier(g, viewportTransform);
/*  71:129 */       if (this.templateVisible) {
/*  72:130 */         paintBridgeSketch(g, viewportTransform);
/*  73:    */       }
/*  74:132 */       this.ctx.label = this.label;
/*  75:133 */       this.ctx.allowableSlenderness = this.conditions.getAllowableSlenderness();
/*  76:134 */       paintBridge(g, viewportTransform, this.ctx);
/*  77:    */     }
/*  78:136 */     g.setColor(Color.BLACK);
/*  79:137 */     g.drawRect(0, 0, w, h + 1);
/*  80:    */   }
/*  81:    */   
/*  82:    */   public void paintStandardAbutment(Graphics2D g, Affine.Point location, boolean mirror, int nConstraints, ViewportTransform viewportTransform)
/*  83:    */   {
/*  84:142 */     paintStandardAbutment(g, this.concretePaint, this.concreteColor, location, mirror, viewportTransform);
/*  85:    */   }
/*  86:    */   
/*  87:    */   public void paintArchAbutment(Graphics2D g, Affine.Point location, boolean mirror, double arch_height, ViewportTransform viewportTransform)
/*  88:    */   {
/*  89:147 */     paintArchAbutment(g, this.concretePaint, this.concreteColor, location, mirror, arch_height, viewportTransform);
/*  90:    */   }
/*  91:    */   
/*  92:    */   public void paintPier(Graphics2D g, Affine.Point location, double pier_height, ViewportTransform viewportTransform)
/*  93:    */   {
/*  94:152 */     paintPier(g, this.concretePaint, this.concreteColor, location, pier_height, viewportTransform);
/*  95:    */   }
/*  96:    */   
/*  97:    */   protected void paintEarthCrossSection(Graphics2D g, ViewportTransform viewportTransform)
/*  98:    */   {
/*  99:157 */     Stroke savedStroke = g.getStroke();
/* 100:158 */     setEarthProfilePolygonAndAccesses(viewportTransform);
/* 101:159 */     g.setPaint(this.earthPaint);
/* 102:160 */     g.fill(this.polygon);
/* 103:    */     
/* 104:162 */     g.setColor(this.earthColor);
/* 105:163 */     int x0 = viewportTransform.worldToViewportX(elevationTerrainPoints[this.rightAbutmentInterfaceTerrainIndex].x + this.halfCutGapWidth);
/* 106:164 */     int y0 = viewportTransform.worldToViewportY(elevationTerrainPoints[this.rightAbutmentInterfaceTerrainIndex].y + this.yGradeLevel);
/* 107:165 */     for (int i = this.rightAbutmentInterfaceTerrainIndex + 1; i <= this.leftAbutmentInterfaceTerrainIndex; i++)
/* 108:    */     {
/* 109:166 */       int x1 = viewportTransform.worldToViewportX(elevationTerrainPoints[i].x + this.halfCutGapWidth);
/* 110:167 */       int y1 = viewportTransform.worldToViewportY(elevationTerrainPoints[i].y + this.yGradeLevel);
/* 111:168 */       g.drawLine(x0, y0, x1, y1);
/* 112:169 */       x0 = x1;
/* 113:170 */       y0 = y1;
/* 114:    */     }
/* 115:173 */     int subgradeHeight = viewportTransform.worldToViewportDistance(0.3D);
/* 116:174 */     this.polygon.npoints = 0;
/* 117:175 */     for (int i = 0; i < this.nRightAccessPoints; i++) {
/* 118:176 */       this.polygon.addPoint(this.rightAccessX[i], this.rightAccessY[i]);
/* 119:    */     }
/* 120:178 */     for (int i = this.nRightAccessPoints - 1; i >= 0; i--) {
/* 121:179 */       this.polygon.addPoint(this.rightAccessX[i], this.rightAccessY[i] + subgradeHeight);
/* 122:    */     }
/* 123:181 */     g.setPaint(this.subgradePaint);
/* 124:182 */     g.fill(this.polygon);
/* 125:183 */     g.setPaint(this.earthColor);
/* 126:184 */     g.draw(this.polygon);
/* 127:    */     
/* 128:186 */     g.setStroke(wearSurfaceStroke);
/* 129:187 */     g.setColor(this.concreteColor);
/* 130:188 */     for (int i = 1; i < this.polygon.npoints / 2; i++) {
/* 131:189 */       g.drawLine(this.polygon.xpoints[(i - 1)], this.polygon.ypoints[(i - 1)], this.polygon.xpoints[i], this.polygon.ypoints[i]);
/* 132:    */     }
/* 133:191 */     g.setStroke(savedStroke);
/* 134:    */     
/* 135:193 */     this.polygon.npoints = 0;
/* 136:194 */     for (int i = 0; i < this.nLeftAccessPoints; i++) {
/* 137:195 */       this.polygon.addPoint(this.leftAccessX[i], this.leftAccessY[i]);
/* 138:    */     }
/* 139:197 */     for (int i = this.nLeftAccessPoints - 1; i >= 0; i--) {
/* 140:198 */       this.polygon.addPoint(this.leftAccessX[i], this.leftAccessY[i] + subgradeHeight);
/* 141:    */     }
/* 142:200 */     g.setPaint(this.subgradePaint);
/* 143:201 */     g.fill(this.polygon);
/* 144:202 */     g.setPaint(this.earthColor);
/* 145:203 */     g.draw(this.polygon);
/* 146:    */     
/* 147:205 */     g.setStroke(wearSurfaceStroke);
/* 148:206 */     g.setColor(this.concreteColor);
/* 149:207 */     for (int i = 1; i < this.polygon.npoints / 2; i++) {
/* 150:208 */       g.drawLine(this.polygon.xpoints[(i - 1)], this.polygon.ypoints[(i - 1)], this.polygon.xpoints[i], this.polygon.ypoints[i]);
/* 151:    */     }
/* 152:210 */     g.setStroke(savedStroke);
/* 153:    */   }
/* 154:    */   
/* 155:213 */   private final Rectangle slab = new Rectangle();
/* 156:    */   private int xBeamFlangeAnchor;
/* 157:    */   private int yBeamFlangeAnchor;
/* 158:    */   private int yDeckAnchor;
/* 159:    */   private int yRoadAnchor;
/* 160:    */   
/* 161:    */   public int getXBeamFlangeAnchor()
/* 162:    */   {
/* 163:226 */     return this.xBeamFlangeAnchor;
/* 164:    */   }
/* 165:    */   
/* 166:    */   public int getYBeamFlangeAnchor()
/* 167:    */   {
/* 168:236 */     return this.yBeamFlangeAnchor;
/* 169:    */   }
/* 170:    */   
/* 171:    */   public int getYDeckAnchor()
/* 172:    */   {
/* 173:246 */     return this.yDeckAnchor;
/* 174:    */   }
/* 175:    */   
/* 176:    */   public int getYRoadAnchor()
/* 177:    */   {
/* 178:256 */     return this.yRoadAnchor;
/* 179:    */   }
/* 180:    */   
/* 181:    */   protected void paintDeck(Graphics2D g, ViewportTransform viewportTransform)
/* 182:    */   {
/* 183:262 */     int halfBeamFlangeWidth = viewportTransform.worldToViewportDistance(0.18D);
/* 184:263 */     int ySlabTop = viewportTransform.worldToViewportY(0.8D);
/* 185:264 */     int ySlabBottom = viewportTransform.worldToViewportY(0.8D - this.conditions.getDeckThickness());
/* 186:265 */     int yBeamTop = ySlabBottom + 2;
/* 187:266 */     int yBeamBottom = yBeamTop + viewportTransform.worldToViewportDistance(0.9D);
/* 188:    */     
/* 189:268 */     int xSlabLeft = viewportTransform.worldToViewportX(this.conditions.getXLeftmostDeckJoint() - 0.32D);
/* 190:269 */     int xSlabRight = viewportTransform.worldToViewportX(this.conditions.getXRightmostDeckJoint() + 0.32D);
/* 191:270 */     Stroke savedStroke = g.getStroke();
/* 192:    */     
/* 193:272 */     this.slab.setFrameFromDiagonal(xSlabLeft, ySlabBottom, xSlabRight, ySlabTop);
/* 194:273 */     g.setPaint(this.concretePaint);
/* 195:274 */     g.fill(this.slab);
/* 196:275 */     g.setPaint(this.concreteColor);
/* 197:276 */     g.draw(this.slab);
/* 198:277 */     g.setStroke(wearSurfaceStroke);
/* 199:    */     
/* 200:279 */     g.drawLine(xSlabLeft, ySlabTop, xSlabRight + 1, ySlabTop);
/* 201:281 */     for (int i = 0; i < this.conditions.getNLoadedJoints(); i++)
/* 202:    */     {
/* 203:282 */       int x = viewportTransform.worldToViewportX(this.conditions.getPrescribedJointLocation(i).x);
/* 204:283 */       g.setStroke(beamStroke);
/* 205:284 */       g.setColor(Color.GRAY);
/* 206:285 */       g.drawLine(x - halfBeamFlangeWidth + 1, yBeamTop, x + halfBeamFlangeWidth, yBeamTop);
/* 207:286 */       g.drawLine(x - halfBeamFlangeWidth + 1, yBeamBottom, x + halfBeamFlangeWidth, yBeamBottom);
/* 208:287 */       g.drawLine(x, yBeamTop, x, yBeamBottom);
/* 209:288 */       if ((i != 0) && (i != this.conditions.getNLoadedJoints() - 1))
/* 210:    */       {
/* 211:289 */         g.setColor(this.concreteColor);
/* 212:290 */         g.setStroke(savedStroke);
/* 213:291 */         g.drawLine(x, ySlabTop, x, ySlabBottom);
/* 214:    */       }
/* 215:    */     }
/* 216:294 */     g.setStroke(savedStroke);
/* 217:    */     
/* 218:296 */     this.xBeamFlangeAnchor = (viewportTransform.worldToViewportX(this.conditions.getPrescribedJointLocation(1).x) + halfBeamFlangeWidth);
/* 219:297 */     this.yBeamFlangeAnchor = yBeamTop;
/* 220:298 */     this.yDeckAnchor = ((ySlabBottom + ySlabTop) / 2);
/* 221:299 */     this.yRoadAnchor = (ySlabTop - 2);
/* 222:    */   }
/* 223:    */   
/* 224:    */   protected void paintAbutmentWearSurface(Graphics2D g, int x0, int x1, int y)
/* 225:    */   {
/* 226:304 */     Stroke savedStroke = g.getStroke();
/* 227:305 */     g.setStroke(wearSurfaceStroke);
/* 228:306 */     g.drawLine(x0, y, x1 + 1, y);
/* 229:307 */     g.setStroke(savedStroke);
/* 230:    */   }
/* 231:    */   
/* 232:    */   protected void paintTerrainProfile(Graphics2D g, ViewportTransform viewportTransform)
/* 233:    */   {
/* 234:313 */     g.setColor(Color.BLUE);
/* 235:314 */     int x0 = viewportTransform.worldToViewportX(elevationTerrainPoints[25].x + this.halfCutGapWidth);
/* 236:315 */     int y0 = viewportTransform.worldToViewportY(elevationTerrainPoints[25].y + this.yGradeLevel);
/* 237:316 */     int x1 = viewportTransform.worldToViewportX(elevationTerrainPoints[16].x + this.halfCutGapWidth);
/* 238:317 */     int y1 = viewportTransform.worldToViewportY(elevationTerrainPoints[16].y + this.yGradeLevel);
/* 239:318 */     for (int i = 0; i < 3; i++)
/* 240:    */     {
/* 241:319 */       g.drawLine(x0, y0, x1, y1);
/* 242:320 */       x0 += 30;
/* 243:321 */       x1 -= 30;
/* 244:322 */       y0 += 4;
/* 245:323 */       y1 += 4;
/* 246:    */     }
/* 247:326 */     g.setColor(this.earthColor);
/* 248:327 */     Stroke savedStroke = g.getStroke();
/* 249:328 */     g.setStroke(terrainProfileStroke);
/* 250:    */     
/* 251:    */ 
/* 252:331 */     x0 = viewportTransform.worldToViewportX(elevationTerrainPoints[1].x + this.halfCutGapWidth);
/* 253:332 */     y0 = viewportTransform.worldToViewportY(elevationTerrainPoints[1].y + this.yGradeLevel);
/* 254:333 */     for (int i = 2; i <= this.rightAbutmentInterfaceTerrainIndex; i++)
/* 255:    */     {
/* 256:334 */       x1 = viewportTransform.worldToViewportX(elevationTerrainPoints[i].x + this.halfCutGapWidth);
/* 257:335 */       y1 = viewportTransform.worldToViewportY(elevationTerrainPoints[i].y + this.yGradeLevel);
/* 258:336 */       g.drawLine(x0, y0, x1, y1);
/* 259:337 */       x0 = x1;
/* 260:338 */       y0 = y1;
/* 261:    */     }
/* 262:341 */     x0 = viewportTransform.worldToViewportX(elevationTerrainPoints[this.leftAbutmentInterfaceTerrainIndex].x + this.halfCutGapWidth);
/* 263:342 */     y0 = viewportTransform.worldToViewportY(elevationTerrainPoints[this.leftAbutmentInterfaceTerrainIndex].y + this.yGradeLevel);
/* 264:343 */     for (int i = this.leftAbutmentInterfaceTerrainIndex + 1; i < elevationTerrainPoints.length - 1; i++)
/* 265:    */     {
/* 266:344 */       x1 = viewportTransform.worldToViewportX(elevationTerrainPoints[i].x + this.halfCutGapWidth);
/* 267:345 */       y1 = viewportTransform.worldToViewportY(elevationTerrainPoints[i].y + this.yGradeLevel);
/* 268:346 */       g.drawLine(x0, y0, x1, y1);
/* 269:347 */       x0 = x1;
/* 270:348 */       y0 = y1;
/* 271:    */     }
/* 272:350 */     g.setStroke(savedStroke);
/* 273:    */   }
/* 274:    */   
/* 275:    */   private TexturePaint initializeConcrete()
/* 276:    */   {
/* 277:354 */     int size = 64;
/* 278:355 */     GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
/* 279:356 */     GraphicsDevice gs = e.getDefaultScreenDevice();
/* 280:357 */     GraphicsConfiguration gc = gs.getDefaultConfiguration();
/* 281:358 */     BufferedImage hatch = gc.createCompatibleImage(64, 64, 1);
/* 282:359 */     Graphics2D g = hatch.createGraphics();
/* 283:360 */     g.setColor(Color.WHITE);
/* 284:361 */     g.fillRect(0, 0, 64, 64);
/* 285:362 */     g.setColor(this.concreteColor);
/* 286:363 */     Random r = new Random();
/* 287:364 */     for (int i = 0; i < 1024; i++)
/* 288:    */     {
/* 289:365 */       int x = r.nextInt(64);
/* 290:366 */       int y = r.nextInt(64);
/* 291:367 */       g.drawLine(x, y, x, y);
/* 292:    */     }
/* 293:369 */     return new TexturePaint(hatch, new Rectangle(0, 0, 64, 64));
/* 294:    */   }
/* 295:    */   
/* 296:    */   private TexturePaint initializeEarth()
/* 297:    */   {
/* 298:373 */     int size = 8;
/* 299:374 */     GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
/* 300:375 */     GraphicsDevice gs = e.getDefaultScreenDevice();
/* 301:376 */     GraphicsConfiguration gc = gs.getDefaultConfiguration();
/* 302:377 */     BufferedImage hatch = gc.createCompatibleImage(8, 8, 1);
/* 303:378 */     Graphics2D g = hatch.createGraphics();
/* 304:379 */     g.setColor(Color.WHITE);
/* 305:380 */     g.fillRect(0, 0, 8, 8);
/* 306:381 */     g.setColor(this.earthColor);
/* 307:    */     
/* 308:    */ 
/* 309:    */ 
/* 310:385 */     g.drawLine(1, 7, 7, 1);
/* 311:386 */     g.drawLine(0, 0, 0, 0);
/* 312:387 */     return new TexturePaint(hatch, new Rectangle(0, 0, 8, 8));
/* 313:    */   }
/* 314:    */   
/* 315:    */   private TexturePaint initializeSubgrade()
/* 316:    */   {
/* 317:391 */     int size = 8;
/* 318:392 */     GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
/* 319:393 */     GraphicsDevice gs = e.getDefaultScreenDevice();
/* 320:394 */     GraphicsConfiguration gc = gs.getDefaultConfiguration();
/* 321:395 */     BufferedImage hatch = gc.createCompatibleImage(8, 8, 1);
/* 322:396 */     Graphics2D g = hatch.createGraphics();
/* 323:397 */     g.setColor(Color.WHITE);
/* 324:398 */     g.fillRect(0, 0, 8, 8);
/* 325:399 */     g.setColor(this.earthColor);
/* 326:400 */     g.drawLine(4, 0, 4, 7);
/* 327:401 */     return new TexturePaint(hatch, new Rectangle(0, 0, 8, 8));
/* 328:    */   }
/* 329:    */   
/* 330:    */   protected void loadPreferredDrawingWindow()
/* 331:    */   {
/* 332:405 */     loadStandardDraftingWindow();
/* 333:    */   }
/* 334:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.BridgeDraftingView
 * JD-Core Version:    0.7.0.1
 */