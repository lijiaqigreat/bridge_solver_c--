/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.awt.BasicStroke;
/*   4:    */ import java.awt.Color;
/*   5:    */ import java.awt.Graphics2D;
/*   6:    */ import java.awt.Paint;
/*   7:    */ import java.awt.Rectangle;
/*   8:    */ import java.awt.Stroke;
/*   9:    */ import java.awt.TexturePaint;
/*  10:    */ import java.awt.image.BufferedImage;
/*  11:    */ import java.util.Arrays;
/*  12:    */ 
/*  13:    */ public class FixedEyeTerrainModel
/*  14:    */   extends TerrainModel
/*  15:    */ {
/*  16:    */   private final FixedEyeAnimation.Config config;
/*  17: 34 */   private final FixedEyeTowerModel tower = new FixedEyeTowerModel();
/*  18:    */   
/*  19:    */   public FixedEyeTerrainModel(FixedEyeAnimation.Config config)
/*  20:    */   {
/*  21: 44 */     super(32);
/*  22: 45 */     this.config = config;
/*  23: 46 */     this.renderer.setGouraudColor(terrainColor);
/*  24:    */   }
/*  25:    */   
/*  26: 49 */   public static final Homogeneous.Point unitLight = new Homogeneous.Point();
/*  27:    */   public static final float ambientIntensity = 0.08F;
/*  28:    */   private static final float lambertIntensity = 0.92F;
/*  29:    */   
/*  30:    */   static
/*  31:    */   {
/*  32: 51 */     Homogeneous.Point l = Animation.lightPosition;
/*  33: 52 */     float r = 1.0F / (float)Math.sqrt(l.a[0] * l.a[0] + l.a[1] * l.a[1] + l.a[2] * l.a[2]);
/*  34: 53 */     unitLight.a[0] = (r * l.a[0]);
/*  35: 54 */     unitLight.a[1] = (r * l.a[1]);
/*  36: 55 */     unitLight.a[2] = (r * l.a[2]);
/*  37: 56 */     unitLight.a[3] = 0.0F;
/*  38:    */   }
/*  39:    */   
/*  40:    */   private float lightIntensityAtPost(TerrainModel.TerrainPost p)
/*  41:    */   {
/*  42: 63 */     float s = p.xNormal * unitLight.x() + p.yNormal * unitLight.y() + p.zNormal * unitLight.z();
/*  43:    */     
/*  44:    */ 
/*  45: 66 */     return s <= 0.0F ? 0.08F : 0.08F + s * 0.92F;
/*  46:    */   }
/*  47:    */   
/*  48: 69 */   public static final int[] terrainColor = { 110, 160, 35 };
/*  49: 70 */   public static final int[] waterColor = { 75, 150, 150 };
/*  50:    */   private static final float cFog = 0.0025F;
/*  51:    */   
/*  52:    */   private Paint interpolateWaterColor(float z)
/*  53:    */   {
/*  54: 75 */     float tFog = z >= 0.0F ? 0.0F : 0.0025F * z / (0.0025F * z - 1.0F);
/*  55: 76 */     int r = (int)((1.0F - tFog) * waterColor[0] + tFog * 192.0F);
/*  56: 77 */     int g = (int)((1.0F - tFog) * waterColor[1] + tFog * 255.0F);
/*  57: 78 */     int b = (int)((1.0F - tFog) * waterColor[2] + tFog * 255.0F);
/*  58: 79 */     return new Color(r, g, b);
/*  59:    */   }
/*  60:    */   
/*  61: 82 */   private final Renderer3d renderer = new Renderer3d();
/*  62: 84 */   private final Homogeneous.Point waterNorthWest = new Homogeneous.Point();
/*  63: 85 */   private final Homogeneous.Point waterSouthWest = new Homogeneous.Point();
/*  64: 86 */   private final Homogeneous.Point waterNorthEast = new Homogeneous.Point();
/*  65: 87 */   private final Homogeneous.Point waterSouthEast = new Homogeneous.Point();
/*  66:    */   private float[] westAbutmentFlank;
/*  67:    */   private float[] eastAbutmentFlank;
/*  68:    */   
/*  69:    */   private void fillToWater(Graphics2D g, ViewportTransform viewportTransform, float dx, float x1, int dj, int j1, int iUnder, float zUnder, Homogeneous.Point pUnder, int iOver, float zOver, Homogeneous.Point pOver)
/*  70:    */   {
/*  71: 93 */     this.renderer.end(g);
/*  72:    */     
/*  73:    */ 
/*  74:    */ 
/*  75:    */ 
/*  76:    */ 
/*  77: 99 */     this.renderer.begin(11);
/*  78:    */     
/*  79:101 */     float y0 = this.posts[iUnder][(j1 - dj)].elevation;
/*  80:102 */     float y1 = this.posts[iUnder][j1].elevation;
/*  81:103 */     pUnder.set(x1 - dx * (this.yWater - y1) / (y0 - y1), this.yWater, zUnder);
/*  82:    */     
/*  83:105 */     TerrainModel.TerrainPost tpUnder = this.posts[iUnder][(j1 - dj)];
/*  84:106 */     float sUnder = lightIntensityAtPost(tpUnder);
/*  85:107 */     this.renderer.addVertex(g, viewportTransform, pUnder.a[0], pUnder.a[1], pUnder.a[2], sUnder);
/*  86:    */     
/*  87:109 */     this.renderer.addVertex(g, viewportTransform, x1 - dx, tpUnder.elevation, zUnder, sUnder);
/*  88:    */     
/*  89:111 */     TerrainModel.TerrainPost tpOver = this.posts[iOver][(j1 - dj)];
/*  90:112 */     float sOver = lightIntensityAtPost(tpOver);
/*  91:113 */     this.renderer.addVertex(g, viewportTransform, x1 - dx, tpOver.elevation, zOver, sOver);
/*  92:115 */     while ((0 <= j1) && (j1 < this.postCount))
/*  93:    */     {
/*  94:116 */       TerrainModel.TerrainPost tp = this.posts[iOver][j1];
/*  95:117 */       y1 = tp.elevation;
/*  96:118 */       if (y1 < this.yWater) {
/*  97:    */         break;
/*  98:    */       }
/*  99:121 */       float s = lightIntensityAtPost(tp);
/* 100:122 */       this.renderer.addVertex(g, viewportTransform, x1, y1, zOver, s);
/* 101:123 */       j1 += dj;
/* 102:124 */       x1 += dx;
/* 103:    */     }
/* 104:128 */     if ((0 <= j1) && (j1 < this.postCount))
/* 105:    */     {
/* 106:130 */       TerrainModel.TerrainPost tpAbove = this.posts[iOver][(j1 - dj)];
/* 107:131 */       float sAbove = lightIntensityAtPost(tpAbove);
/* 108:132 */       y0 = tpAbove.elevation;
/* 109:133 */       y1 = this.posts[iOver][j1].elevation;
/* 110:134 */       pOver.set(x1 - dx * (this.yWater - y1) / (y0 - y1), this.yWater, zOver);
/* 111:135 */       this.renderer.addVertex(g, viewportTransform, pOver, sAbove);
/* 112:    */     }
/* 113:    */   }
/* 114:    */   
/* 115:    */   private void drawTerrainStrip(Graphics2D g, ViewportTransform viewportTransform, int i0, int i1)
/* 116:    */   {
/* 117:149 */     this.renderer.setApproximateGouraud(!this.config.showSmoothTerrain);
/* 118:    */     
/* 119:    */ 
/* 120:152 */     float zNorth = zGridToWorld(i0);
/* 121:153 */     float zSouth = zNorth + this.metersPerGrid;
/* 122:    */     
/* 123:    */ 
/* 124:156 */     int j0 = this.postCount / 5;
/* 125:158 */     for (int iNorth = i0; iNorth < i1; iNorth++)
/* 126:    */     {
/* 127:159 */       int iSouth = iNorth + 1;
/* 128:160 */       float x = xGridToWorld(j0);
/* 129:    */       
/* 130:    */ 
/* 131:    */ 
/* 132:164 */       this.waterNorthWest.set(x, this.yWater, zNorth);
/* 133:165 */       this.waterSouthWest.set(x, this.yWater, zSouth);
/* 134:166 */       this.renderer.begin(10);
/* 135:167 */       for (int j = j0; j < this.postCount; j++)
/* 136:    */       {
/* 137:168 */         TerrainModel.TerrainPost n = this.posts[iNorth][j];
/* 138:169 */         TerrainModel.TerrainPost s = this.posts[iSouth][j];
/* 139:170 */         if (j > j0)
/* 140:    */         {
/* 141:171 */           if (n.elevation <= this.yWater)
/* 142:    */           {
/* 143:172 */             fillToWater(g, viewportTransform, this.metersPerGrid, x, 1, j, iNorth, zNorth, this.waterNorthWest, iSouth, zSouth, this.waterSouthWest);
/* 144:    */             
/* 145:    */ 
/* 146:    */ 
/* 147:    */ 
/* 148:177 */             break;
/* 149:    */           }
/* 150:179 */           if (s.elevation <= this.yWater)
/* 151:    */           {
/* 152:180 */             fillToWater(g, viewportTransform, this.metersPerGrid, x, 1, j, iSouth, zSouth, this.waterSouthWest, iNorth, zNorth, this.waterNorthWest);
/* 153:    */             
/* 154:    */ 
/* 155:    */ 
/* 156:    */ 
/* 157:185 */             break;
/* 158:    */           }
/* 159:    */         }
/* 160:188 */         float sn = lightIntensityAtPost(n);
/* 161:189 */         this.renderer.addVertex(g, viewportTransform, x, n.elevation, zNorth, sn);
/* 162:190 */         float ss = lightIntensityAtPost(s);
/* 163:191 */         this.renderer.addVertex(g, viewportTransform, x, s.elevation, zSouth, ss);
/* 164:192 */         x += this.metersPerGrid;
/* 165:    */       }
/* 166:194 */       this.renderer.end(g);
/* 167:195 */       x = xGridToWorld(this.gridCount - j0);
/* 168:196 */       this.waterNorthEast.set(x, this.yWater, zNorth);
/* 169:197 */       this.waterSouthEast.set(x, this.yWater, zSouth);
/* 170:198 */       this.renderer.begin(10);
/* 171:199 */       for (int j = this.gridCount - j0; j >= 0; j--)
/* 172:    */       {
/* 173:200 */         TerrainModel.TerrainPost n = this.posts[iNorth][j];
/* 174:201 */         TerrainModel.TerrainPost s = this.posts[iSouth][j];
/* 175:202 */         if (j < this.gridCount - j0)
/* 176:    */         {
/* 177:203 */           if (n.elevation <= this.yWater)
/* 178:    */           {
/* 179:204 */             fillToWater(g, viewportTransform, -this.metersPerGrid, x, -1, j, iNorth, zNorth, this.waterNorthEast, iSouth, zSouth, this.waterSouthEast);
/* 180:    */             
/* 181:    */ 
/* 182:    */ 
/* 183:    */ 
/* 184:209 */             break;
/* 185:    */           }
/* 186:211 */           if (s.elevation <= this.yWater)
/* 187:    */           {
/* 188:212 */             fillToWater(g, viewportTransform, -this.metersPerGrid, x, -1, j, iSouth, zSouth, this.waterSouthEast, iNorth, zNorth, this.waterNorthEast);
/* 189:    */             
/* 190:    */ 
/* 191:    */ 
/* 192:    */ 
/* 193:217 */             break;
/* 194:    */           }
/* 195:    */         }
/* 196:220 */         float sn = lightIntensityAtPost(n);
/* 197:221 */         this.renderer.addVertex(g, viewportTransform, x, n.elevation, zNorth, sn);
/* 198:222 */         float ss = lightIntensityAtPost(s);
/* 199:223 */         this.renderer.addVertex(g, viewportTransform, x, s.elevation, zSouth, ss);
/* 200:224 */         x -= this.metersPerGrid;
/* 201:    */       }
/* 202:226 */       this.renderer.end(g);
/* 203:    */       
/* 204:    */ 
/* 205:229 */       this.renderer.setPaint(interpolateWaterColor(zSouth));
/* 206:230 */       this.renderer.begin(1);
/* 207:231 */       this.renderer.addVertex(g, viewportTransform, this.waterNorthWest);
/* 208:232 */       this.renderer.addVertex(g, viewportTransform, this.waterSouthWest);
/* 209:233 */       this.renderer.addVertex(g, viewportTransform, this.waterNorthEast);
/* 210:234 */       this.renderer.addVertex(g, viewportTransform, this.waterSouthEast);
/* 211:235 */       this.renderer.end(g);
/* 212:    */       
/* 213:237 */       this.renderer.setPaint(Color.LIGHT_GRAY);
/* 214:238 */       this.renderer.begin(3);
/* 215:239 */       this.renderer.addVertex(g, viewportTransform, this.waterNorthWest);
/* 216:240 */       this.renderer.addVertex(g, viewportTransform, this.waterSouthWest);
/* 217:241 */       this.renderer.addVertex(g, viewportTransform, this.waterNorthEast);
/* 218:242 */       this.renderer.addVertex(g, viewportTransform, this.waterSouthEast);
/* 219:243 */       this.renderer.end(g);
/* 220:244 */       zNorth = zSouth;
/* 221:245 */       zSouth += this.metersPerGrid;
/* 222:    */     }
/* 223:    */   }
/* 224:    */   
/* 225:    */   private boolean isRoadwayHidden(ViewportTransform viewportTransform)
/* 226:    */   {
/* 227:250 */     return viewportTransform.isAboveVanishingPoint(this.westAbutmentFrontFace[1]);
/* 228:    */   }
/* 229:    */   
/* 230:    */   private void drawRoadway(Graphics2D g, ViewportTransform viewportTransform, int j0)
/* 231:    */   {
/* 232:254 */     if (isRoadwayHidden(viewportTransform)) {
/* 233:255 */       return;
/* 234:    */     }
/* 235:257 */     float x = xGridToWorld(j0);
/* 236:258 */     this.renderer.setPaint(Bridge3dView.gray00);
/* 237:259 */     this.renderer.begin(4);
/* 238:260 */     for (int j = j0; j < this.postCount - j0; j++)
/* 239:    */     {
/* 240:261 */       if (x >= -0.45F)
/* 241:    */       {
/* 242:262 */         this.renderer.addVertex(g, viewportTransform, -0.45F, 0.8F, -5.0F);
/* 243:263 */         this.renderer.addVertex(g, viewportTransform, -0.45F, 0.8F, 5.0F);
/* 244:264 */         break;
/* 245:    */       }
/* 246:266 */       this.renderer.addVertex(g, viewportTransform, x, this.roadCenterline[j].elevation, -5.0F);
/* 247:267 */       this.renderer.addVertex(g, viewportTransform, x, this.roadCenterline[j].elevation, 5.0F);
/* 248:268 */       x += this.metersPerGrid;
/* 249:    */     }
/* 250:270 */     this.renderer.end(g);
/* 251:271 */     this.renderer.begin(4);
/* 252:272 */     x = xGridToWorld(this.gridCount - j0);
/* 253:273 */     float xDeckEnd = 2.0F * this.halfSpanLength - -0.45F;
/* 254:274 */     for (int j = this.gridCount - j0; j >= 0; j--)
/* 255:    */     {
/* 256:275 */       if (x <= xDeckEnd)
/* 257:    */       {
/* 258:276 */         this.renderer.addVertex(g, viewportTransform, xDeckEnd, 0.8F, 5.0F);
/* 259:277 */         this.renderer.addVertex(g, viewportTransform, xDeckEnd, 0.8F, -5.0F);
/* 260:278 */         break;
/* 261:    */       }
/* 262:280 */       this.renderer.addVertex(g, viewportTransform, x, this.roadCenterline[j].elevation, 5.0F);
/* 263:281 */       this.renderer.addVertex(g, viewportTransform, x, this.roadCenterline[j].elevation, -5.0F);
/* 264:282 */       x -= this.metersPerGrid;
/* 265:    */     }
/* 266:284 */     this.renderer.end(g);
/* 267:    */   }
/* 268:    */   
/* 269:    */   private void drawWires(Graphics2D g, ViewportTransform viewportTransform, int i0, int i1)
/* 270:    */   {
/* 271:288 */     g.setPaint(Color.GRAY);
/* 272:289 */     int[] xWire = new int[21];
/* 273:290 */     int[] yWire = new int[21];
/* 274:291 */     for (int iOffset = i0; iOffset < i1; iOffset++)
/* 275:    */     {
/* 276:292 */       float xOfs = xUnitPerpTower * wireOffsets[iOffset].x();
/* 277:293 */       float yOfs = wireOffsets[iOffset].y();
/* 278:294 */       float zOfs = zUnitPerpTower * wireOffsets[iOffset].x();
/* 279:295 */       for (int iTower = 0; iTower < 2; iTower++)
/* 280:    */       {
/* 281:296 */         int nGoodPts = 0;
/* 282:297 */         for (int iWire = 0; iWire < this.wirePt[iTower].length; iWire++)
/* 283:    */         {
/* 284:298 */           Homogeneous.Point p = this.wirePt[iTower][iWire];
/* 285:299 */           if (!viewportTransform.worldToViewport(xWire, yWire, iWire, p.x() + xOfs, p.y() + yOfs, p.z() + zOfs)) {
/* 286:    */             break;
/* 287:    */           }
/* 288:303 */           nGoodPts++;
/* 289:    */         }
/* 290:305 */         g.drawPolyline(xWire, yWire, nGoodPts);
/* 291:    */       }
/* 292:    */     }
/* 293:    */   }
/* 294:    */   
/* 295:    */   private void drawPowerLines(Graphics2D g, ViewportTransform viewportTransform)
/* 296:    */   {
/* 297:311 */     drawWires(g, viewportTransform, 0, 3);
/* 298:312 */     for (int i = 0; i < this.towers.length; i++) {
/* 299:313 */       this.tower.paint(g, viewportTransform, this.towers[i]);
/* 300:    */     }
/* 301:315 */     drawWires(g, viewportTransform, 3, 6);
/* 302:    */   }
/* 303:    */   
/* 304:319 */   private final float[] westAbutmentFrontFace = new float[9];
/* 305:320 */   private final float[] westAbutmentRearFace = new float[9];
/* 306:321 */   private final float[] westSkirt = new float[15];
/* 307:322 */   private final float[] eastAbutmentFrontFace = new float[9];
/* 308:323 */   private final float[] eastAbutmentRearFace = new float[9];
/* 309:324 */   private final float[] eastSkirt = new float[15];
/* 310:    */   
/* 311:    */   private void trimFlankBase(float[] flank)
/* 312:    */   {
/* 313:327 */     int i = flank.length - 6;
/* 314:328 */     float xFoot = getElevationAt(flank[i], flank[(i + 2)]);
/* 315:329 */     if (xFoot > this.yWater)
/* 316:    */     {
/* 317:330 */       flank[(i + 1)] = xFoot;
/* 318:331 */       setToTerrainY(flank, i - 3, -1.0F);
/* 319:    */     }
/* 320:    */   }
/* 321:    */   
/* 322:336 */   FixedEyeTowerModel.Triangle[][] towers = new FixedEyeTowerModel.Triangle[2][];
/* 323:    */   
/* 324:    */   public void initializeTerrain(DesignConditions conditions, float trussCenterOffset, float abutmentHalfWidth)
/* 325:    */   {
/* 326:340 */     super.initializeTerrain(conditions, trussCenterOffset, abutmentHalfWidth);
/* 327:341 */     for (int i = 0; i < this.towers.length; i++) {
/* 328:342 */       this.towers[i] = this.tower.getTriangles(90.0F, 70.0F, this.towerPt[i]);
/* 329:    */     }
/* 330:344 */     this.westAbutmentFlank = Arrays.copyOf(this.abutmentFrontFlank, this.abutmentFrontFlank.length);
/* 331:345 */     this.eastAbutmentFlank = Arrays.copyOf(this.abutmentFrontFlank, this.abutmentFrontFlank.length);
/* 332:346 */     System.arraycopy(this.abutmentFrontFace, 0, this.westAbutmentFrontFace, 0, this.westAbutmentFrontFace.length);
/* 333:347 */     System.arraycopy(this.abutmentRearFace, 0, this.westAbutmentRearFace, 0, this.westAbutmentFrontFace.length);
/* 334:348 */     System.arraycopy(this.abutmentFrontFace, 0, this.eastAbutmentFrontFace, 0, this.eastAbutmentFrontFace.length);
/* 335:349 */     System.arraycopy(this.abutmentRearFace, 0, this.eastAbutmentRearFace, 0, this.eastAbutmentFrontFace.length);
/* 336:352 */     for (int i3 = 0; i3 < this.eastAbutmentFrontFace.length; i3 += 3)
/* 337:    */     {
/* 338:353 */       this.eastAbutmentFrontFace[(i3 + 0)] = (2.0F * this.halfSpanLength - this.eastAbutmentFrontFace[(i3 + 0)]);
/* 339:354 */       this.eastAbutmentRearFace[(i3 + 0)] = (2.0F * this.halfSpanLength - this.eastAbutmentRearFace[(i3 + 0)]);
/* 340:    */     }
/* 341:356 */     for (int i3 = 0; i3 < this.eastAbutmentFlank.length; i3 += 3) {
/* 342:357 */       this.eastAbutmentFlank[(i3 + 0)] = (2.0F * this.halfSpanLength - this.eastAbutmentFlank[(i3 + 0)]);
/* 343:    */     }
/* 344:361 */     trimFlankBase(this.westAbutmentFlank);
/* 345:362 */     trimFlankBase(this.eastAbutmentFlank);
/* 346:365 */     for (int i3 = 0; i3 < this.eastSkirt.length; i3 += 3)
/* 347:    */     {
/* 348:366 */       float dz = i3 * (this.eastAbutmentFrontFace[8] - this.eastAbutmentRearFace[8]) / (this.eastSkirt.length - 3);
/* 349:367 */       this.westSkirt[(i3 + 0)] = this.westAbutmentFrontFace[6];
/* 350:368 */       this.westSkirt[(i3 + 2)] = (this.westAbutmentFrontFace[8] - dz);
/* 351:369 */       setToTerrainY(this.westSkirt, i3, 0.0F);
/* 352:370 */       this.eastSkirt[(i3 + 0)] = this.eastAbutmentFrontFace[6];
/* 353:371 */       this.eastSkirt[(i3 + 2)] = (this.eastAbutmentFrontFace[8] - dz);
/* 354:372 */       setToTerrainY(this.eastSkirt, i3, 0.0F);
/* 355:    */     }
/* 356:    */   }
/* 357:    */   
/* 358:376 */   private static final Color[] abutmentFaceColors = { Bridge3dView.gray50, Bridge3dView.gray40, Bridge3dView.gray25, Bridge3dView.gray50 };
/* 359:377 */   private static BufferedImage abutmentTextureImage = WPBDApp.getApplication().getBufferedImageResource("bricktile.png");
/* 360:378 */   private static final Stroke waterlineStroke = new BasicStroke(3.0F);
/* 361:379 */   private Paint abutmentPaint = null;
/* 362:380 */   private int viewportUpdateKey = -1;
/* 363:    */   
/* 364:    */   public void drawAbutmentFlanks(Graphics2D g, ViewportTransform viewportTransform)
/* 365:    */   {
/* 366:383 */     if (!this.config.showAbutments) {
/* 367:384 */       return;
/* 368:    */     }
/* 369:387 */     if ((this.abutmentPaint == null) || (this.viewportUpdateKey != viewportTransform.getUpdateKey()))
/* 370:    */     {
/* 371:388 */       int textureSize = viewportTransform.worldToViewportDistance(3.0D);
/* 372:389 */       this.abutmentPaint = new TexturePaint(abutmentTextureImage, new Rectangle(textureSize, textureSize));
/* 373:390 */       this.viewportUpdateKey = viewportTransform.getUpdateKey();
/* 374:    */     }
/* 375:392 */     this.renderer.setPaint(this.abutmentPaint);
/* 376:393 */     this.renderer.begin(5);
/* 377:394 */     this.renderer.addVertex(g, viewportTransform, this.westAbutmentFlank, 0, this.westAbutmentFlank.length / 3);
/* 378:395 */     this.renderer.end(g);
/* 379:396 */     this.renderer.begin(5);
/* 380:397 */     this.renderer.addVertex(g, viewportTransform, this.eastAbutmentFlank, 0, this.eastAbutmentFlank.length / 3);
/* 381:398 */     this.renderer.end(g);
/* 382:400 */     if (this.westAbutmentFlank[(this.westAbutmentFlank.length - 6 + 1)] == this.yWater)
/* 383:    */     {
/* 384:401 */       Stroke savedStroke = g.getStroke();
/* 385:402 */       g.setStroke(waterlineStroke);
/* 386:403 */       this.renderer.setPaint(Color.LIGHT_GRAY);
/* 387:404 */       this.renderer.begin(6);
/* 388:405 */       this.renderer.addVertex(g, viewportTransform, this.westAbutmentFlank, this.westAbutmentFlank.length - 9, 2);
/* 389:406 */       this.renderer.end(g);
/* 390:407 */       this.renderer.begin(6);
/* 391:408 */       this.renderer.addVertex(g, viewportTransform, this.eastAbutmentFlank, this.eastAbutmentFlank.length - 9, 2);
/* 392:409 */       this.renderer.end(g);
/* 393:410 */       g.setStroke(savedStroke);
/* 394:    */     }
/* 395:    */   }
/* 396:    */   
/* 397:    */   public void drawAbutmentTops(Graphics2D g, ViewportTransform viewportTransform)
/* 398:    */   {
/* 399:415 */     if (!this.config.showAbutments) {
/* 400:416 */       return;
/* 401:    */     }
/* 402:419 */     this.renderer.setPaint(Bridge3dView.gray30);
/* 403:420 */     this.renderer.begin(4);
/* 404:421 */     this.renderer.addVertex(g, viewportTransform, this.abutmentRearTop, 0, this.abutmentFrontTop, 0, null, 0, this.abutmentFrontTop.length / 3);
/* 405:    */     
/* 406:    */ 
/* 407:    */ 
/* 408:    */ 
/* 409:426 */     this.renderer.begin(4);
/* 410:427 */     for (int i3 = 0; i3 < this.abutmentRearTop.length; i3 += 3)
/* 411:    */     {
/* 412:428 */       this.renderer.addVertex(g, viewportTransform, 2.0F * this.halfSpanLength - this.abutmentRearTop[i3], this.abutmentRearTop[(i3 + 1)], this.abutmentRearTop[(i3 + 2)]);
/* 413:    */       
/* 414:    */ 
/* 415:    */ 
/* 416:432 */       this.renderer.addVertex(g, viewportTransform, 2.0F * this.halfSpanLength - this.abutmentFrontTop[i3], this.abutmentFrontTop[(i3 + 1)], this.abutmentFrontTop[(i3 + 2)]);
/* 417:    */     }
/* 418:437 */     this.renderer.end(g);
/* 419:    */   }
/* 420:    */   
/* 421:    */   public void drawAbutmentFaces(Graphics2D g, ViewportTransform viewportTransform)
/* 422:    */   {
/* 423:441 */     if (!this.config.showAbutments) {
/* 424:442 */       return;
/* 425:    */     }
/* 426:444 */     this.renderer.begin(4);
/* 427:445 */     this.renderer.addVertex(g, viewportTransform, this.westAbutmentRearFace, 0, this.westAbutmentFrontFace, 0, abutmentFaceColors, 0, this.westAbutmentRearFace.length / 3);
/* 428:446 */     this.renderer.end(g);
/* 429:    */     
/* 430:448 */     this.renderer.setPaint(abutmentFaceColors[(abutmentFaceColors.length - 1)]);
/* 431:449 */     this.renderer.begin(5, 2);
/* 432:450 */     this.renderer.addVertex(g, viewportTransform, this.westSkirt, 0, this.eastSkirt.length / 3);
/* 433:451 */     this.renderer.end(g);
/* 434:    */     
/* 435:453 */     this.renderer.begin(4);
/* 436:454 */     this.renderer.addVertex(g, viewportTransform, this.eastAbutmentRearFace, 0, this.eastAbutmentFrontFace, 0, abutmentFaceColors, 0, this.eastAbutmentRearFace.length / 3);
/* 437:455 */     this.renderer.end(g);
/* 438:    */     
/* 439:457 */     this.renderer.setPaint(abutmentFaceColors[(abutmentFaceColors.length - 1)]);
/* 440:458 */     this.renderer.begin(5, 2);
/* 441:459 */     this.renderer.addVertex(g, viewportTransform, this.eastSkirt, 0, this.eastSkirt.length / 3);
/* 442:460 */     this.renderer.end(g);
/* 443:462 */     if (this.westSkirt[1] == this.yWater)
/* 444:    */     {
/* 445:463 */       this.renderer.setPaint(Color.LIGHT_GRAY);
/* 446:464 */       this.renderer.begin(3);
/* 447:465 */       this.renderer.addVertex(g, viewportTransform, this.westSkirt, 0);
/* 448:466 */       this.renderer.addVertex(g, viewportTransform, this.westSkirt, this.westSkirt.length - 3);
/* 449:467 */       this.renderer.addVertex(g, viewportTransform, this.eastSkirt, 0);
/* 450:468 */       this.renderer.addVertex(g, viewportTransform, this.eastSkirt, this.westSkirt.length - 3);
/* 451:469 */       this.renderer.end(g);
/* 452:    */     }
/* 453:    */   }
/* 454:    */   
/* 455:    */   public void patchRoadway(Graphics2D g, ViewportTransform viewportTransform)
/* 456:    */   {
/* 457:474 */     if (this.config.showBackground) {
/* 458:475 */       drawRoadway(g, viewportTransform, 3 * this.gridCount / 8);
/* 459:    */     }
/* 460:    */   }
/* 461:    */   
/* 462:    */   public void patchTerrain(Graphics2D g, ViewportTransform viewportTransform)
/* 463:    */   {
/* 464:480 */     drawAbutmentFlanks(g, viewportTransform);
/* 465:481 */     if (this.config.showBackground)
/* 466:    */     {
/* 467:482 */       int iRoadCenterline = this.gridCount / 2;
/* 468:483 */       drawTerrainStrip(g, viewportTransform, iRoadCenterline + this.roadEdgeIndexOffset, iRoadCenterline + 5 * this.roadEdgeIndexOffset);
/* 469:    */     }
/* 470:    */   }
/* 471:    */   
/* 472:    */   public void paint(Graphics2D g, ViewportTransform viewportTransform)
/* 473:    */   {
/* 474:489 */     int iRoadCenterline = this.gridCount / 2;
/* 475:490 */     if (this.config.showBackground)
/* 476:    */     {
/* 477:491 */       drawPowerLines(g, viewportTransform);
/* 478:492 */       drawTerrainStrip(g, viewportTransform, 0, iRoadCenterline + this.roadEdgeIndexOffset);
/* 479:    */       
/* 480:    */ 
/* 481:495 */       drawRoadway(g, viewportTransform, this.gridCount / 4);
/* 482:    */     }
/* 483:497 */     drawAbutmentFaces(g, viewportTransform);
/* 484:498 */     if (this.config.showBackground) {
/* 485:499 */       drawTerrainStrip(g, viewportTransform, iRoadCenterline + this.roadEdgeIndexOffset, iRoadCenterline + 8 * this.roadEdgeIndexOffset);
/* 486:    */     }
/* 487:    */   }
/* 488:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.FixedEyeTerrainModel
 * JD-Core Version:    0.7.0.1
 */