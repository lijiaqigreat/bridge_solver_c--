/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.util.Random;
/*   4:    */ 
/*   5:    */ public class TerrainModel
/*   6:    */ {
/*   7:    */   protected final int halfGridCount;
/*   8:    */   protected final float metersPerGrid;
/*   9:    */   protected final int gridCount;
/*  10:    */   protected final int postCount;
/*  11:    */   protected final TerrainPost[][] posts;
/*  12:    */   protected final CenterlinePost[] roadCenterline;
/*  13:    */   protected final Affine.Point[] naturalizedRiverAxis;
/*  14:    */   protected static final float halfTerrainSize = 192.0F;
/*  15:    */   protected static final float halfGapWidth = 24.0F;
/*  16:    */   protected static final float bankSlope = 2.0F;
/*  17:    */   protected static final float waterLevel = -26.0F;
/*  18:    */   protected static final float blufSetback = 4.8F;
/*  19:    */   protected static final float accessSlope = 0.1666667F;
/*  20:    */   protected static final float tangentOffset = 8.0F;
/*  21:    */   protected static final float wearSurfaceHeight = 0.8F;
/*  22:    */   protected static final float abutmentStepInset = -0.45F;
/*  23:    */   protected static final float abutmentStepHeight = -0.35F;
/*  24:    */   protected static final float abutmentStepWidth = 0.25F;
/*  25:    */   protected static final float anchorOffset = 8.0F;
/*  26:    */   protected static final float deckHalfWidth = 5.0F;
/*  27:    */   protected static final float stoneTextureSize = 0.3F;
/*  28:    */   protected static final float tBlufAtBridge = 28.799999F;
/*  29:    */   protected static final float tInflection = 19.200001F;
/*  30:    */   protected static final float blufCoeff = 0.1041667F;
/*  31:    */   protected static final float yGorgeBottom = -48.0F;
/*  32:    */   protected static final float tWaterEdge = 11.0F;
/*  33:    */   protected static final float roadCutSlope = 1.0F;
/*  34:    */   protected static final float epsPaint = 0.05F;
/*  35:    */   protected static final float terrainBrightness = 1.5F;
/*  36: 59 */   protected static final float[] flatTerrainMaterial = { 0.045F, 0.15F, 0.0135F, 1.0F };
/*  37: 64 */   protected static final float[] verticalTerrainMaterial = { 0.3F, 0.3F, 0.12F, 1.0F };
/*  38:    */   protected static final float yNormalMaterialThreshhold = 0.8F;
/*  39: 70 */   protected static final float[] white = { 1.0F, 1.0F, 1.0F, 1.0F };
/*  40: 71 */   protected static final float[] roadMaterial = { 0.2F, 0.2F, 0.2F, 1.0F };
/*  41: 72 */   protected static final float[] abutmentMaterial = { 0.8F, 0.8F, 0.8F, 1.0F };
/*  42: 73 */   protected static final float[] pillowMaterial = { 0.4F, 0.4F, 0.4F, 1.0F };
/*  43: 74 */   protected float halfSpanLength = 0.0F;
/*  44: 75 */   protected float yWater = 0.0F;
/*  45: 76 */   protected float yGrade = 0.0F;
/*  46: 77 */   protected float abutmentHalfWidth = 0.0F;
/*  47: 78 */   protected float trussCenterOffset = 0.0F;
/*  48: 79 */   protected boolean leftCable = false;
/*  49: 80 */   protected boolean rightCable = false;
/*  50: 81 */   protected long tLast = -1L;
/*  51: 82 */   protected float dWater = 0.0F;
/*  52: 83 */   protected boolean drawingShadows = false;
/*  53: 84 */   protected Affine.Point pierLocation = null;
/*  54: 85 */   protected int roadEdgeIndexOffset = 1;
/*  55: 88 */   protected static final Affine.Point[] riverAxisSouth = { new Affine.Point(0.0D, 16.0D), new Affine.Point(15.0D, 40.0D), new Affine.Point(-20.0D, 120.0D), new Affine.Point(80.0D, 180.0D) };
/*  56: 96 */   protected static final Affine.Point[] riverAxisNorth = { new Affine.Point(0.0D, -14.0D), new Affine.Point(-4.0D, -50.0D), new Affine.Point(22.0D, -130.0D), new Affine.Point(80.0D, -190.0D) };
/*  57:    */   protected static final double majorPeriod = 183.0D;
/*  58:    */   protected static final double majorMagnitude = 18.0D;
/*  59:    */   protected static final double minorPeriod = 17.0D;
/*  60:    */   protected static final double minorMagnitude = 7.0D;
/*  61:    */   
/*  62:    */   public TerrainModel()
/*  63:    */   {
/*  64:109 */     this(32);
/*  65:    */   }
/*  66:    */   
/*  67:    */   public TerrainModel(int halfGridCount)
/*  68:    */   {
/*  69:113 */     this.halfGridCount = halfGridCount;
/*  70:114 */     this.metersPerGrid = (192.0F / halfGridCount);
/*  71:115 */     this.gridCount = (2 * halfGridCount);
/*  72:116 */     this.postCount = (this.gridCount + 1);
/*  73:117 */     this.roadEdgeIndexOffset = ((int)(0.9999F + 5.0F / this.metersPerGrid));
/*  74:118 */     this.posts = new TerrainPost[this.postCount][this.postCount];
/*  75:119 */     this.roadCenterline = new CenterlinePost[this.postCount];
/*  76:120 */     this.yFractal = getFractalTerrain(this.postCount, 15.0F, 1.8F, 0L);
/*  77:121 */     this.naturalizedRiverAxis = initialNaturalizedRiverAxis();
/*  78:    */   }
/*  79:    */   
/*  80:    */   protected static class TerrainPost
/*  81:    */   {
/*  82:    */     float elevation;
/*  83:    */     float xNormal;
/*  84:    */     float yNormal;
/*  85:    */     float zNormal;
/*  86:    */     
/*  87:    */     public TerrainPost(float elevation)
/*  88:    */     {
/*  89:126 */       this.elevation = elevation;
/*  90:127 */       this.xNormal = (this.zNormal = 0.0F);
/*  91:128 */       this.yNormal = 1.0F;
/*  92:    */     }
/*  93:    */     
/*  94:    */     public TerrainPost() {}
/*  95:    */   }
/*  96:    */   
/*  97:    */   protected static class CenterlinePost
/*  98:    */   {
/*  99:    */     float elevation;
/* 100:    */     float xNormal;
/* 101:    */     float yNormal;
/* 102:    */     
/* 103:    */     public CenterlinePost(float elevation)
/* 104:    */     {
/* 105:140 */       this.elevation = elevation;
/* 106:141 */       this.xNormal = 0.0F;
/* 107:142 */       this.yNormal = 1.0F;
/* 108:    */     }
/* 109:    */     
/* 110:    */     public CenterlinePost() {}
/* 111:    */   }
/* 112:    */   
/* 113:    */   public float getRoadCenterlineElevation(float x)
/* 114:    */   {
/* 115:158 */     x += 192.0F - this.halfSpanLength;
/* 116:159 */     float x0 = x / this.metersPerGrid;
/* 117:160 */     int i0 = (int)x0;
/* 118:161 */     int i1 = i0 + 1;
/* 119:163 */     if (i0 < 0) {
/* 120:164 */       return this.roadCenterline[0].elevation;
/* 121:    */     }
/* 122:166 */     if (i1 >= this.postCount) {
/* 123:167 */       return this.roadCenterline[(this.postCount - 1)].elevation;
/* 124:    */     }
/* 125:169 */     float t = x0 - i0;
/* 126:170 */     return (1.0F - t) * this.roadCenterline[i0].elevation + t * this.roadCenterline[i1].elevation;
/* 127:    */   }
/* 128:    */   
/* 129:    */   protected static Affine.Point[] getPerturbedAxis(Affine.Point[] axis, int nPoints)
/* 130:    */   {
/* 131:182 */     Affine.Point[] rtn = new Affine.Point[nPoints];
/* 132:183 */     double len = 0.0D;
/* 133:184 */     for (int i = 1; i < axis.length; i++) {
/* 134:185 */       len += axis[(i - 1)].distance(axis[i]);
/* 135:    */     }
/* 136:187 */     double dt = len / (nPoints - 1);
/* 137:188 */     double t = 0.0D;
/* 138:189 */     int iSeg = -1;
/* 139:190 */     double tSegStart = 0.0D;
/* 140:191 */     double tSegEnd = 0.0D;
/* 141:192 */     Affine.Vector v = null;
/* 142:193 */     Affine.Vector vPerp = null;
/* 143:194 */     for (int i = 0; i < nPoints; i++)
/* 144:    */     {
/* 145:195 */       if ((t >= tSegEnd) && (iSeg + 2 < axis.length))
/* 146:    */       {
/* 147:196 */         iSeg++;
/* 148:197 */         tSegStart = tSegEnd;
/* 149:198 */         tSegEnd += axis[iSeg].distance(axis[(iSeg + 1)]);
/* 150:199 */         v = axis[(iSeg + 1)].minus(axis[iSeg]).unit(1.0D);
/* 151:200 */         vPerp = v.perp();
/* 152:    */       }
/* 153:202 */       Affine.Point p = axis[iSeg].plus(v.times(t - tSegStart));
/* 154:203 */       double ofs = 18.0D * Math.sin(t / 183.0D * 2.0D * 3.141592653589793D) + 7.0D * Math.sin(t / 17.0D * 2.0D * 3.141592653589793D);
/* 155:    */       
/* 156:205 */       rtn[i] = p.plus(vPerp.times(ofs));
/* 157:206 */       t += dt;
/* 158:    */     }
/* 159:208 */     return rtn;
/* 160:    */   }
/* 161:    */   
/* 162:    */   protected static Affine.Point[] initialNaturalizedRiverAxis()
/* 163:    */   {
/* 164:212 */     Affine.Point[] north = getPerturbedAxis(riverAxisNorth, 32);
/* 165:213 */     Affine.Point[] south = getPerturbedAxis(riverAxisSouth, 32);
/* 166:214 */     Affine.Point[] rtn = new Affine.Point[north.length + south.length];
/* 167:215 */     Utility.reverse(north);
/* 168:216 */     System.arraycopy(north, 0, rtn, 0, north.length);
/* 169:217 */     System.arraycopy(south, 0, rtn, north.length, south.length);
/* 170:218 */     return rtn;
/* 171:    */   }
/* 172:    */   
/* 173:    */   protected float distToRiver(float x, float z)
/* 174:    */   {
/* 175:222 */     float dist = 1000000.0F;
/* 176:223 */     for (int i = 0; i < this.naturalizedRiverAxis.length - 1; i++) {
/* 177:224 */       dist = Math.min(dist, (float)new Affine.Point(x, z).distanceToSegment(this.naturalizedRiverAxis[i], this.naturalizedRiverAxis[(i + 1)]));
/* 178:    */     }
/* 179:226 */     return dist;
/* 180:    */   }
/* 181:    */   
/* 182:229 */   protected final Random generator = new Random();
/* 183:    */   protected final float[][] yFractal;
/* 184:    */   
/* 185:    */   protected float random()
/* 186:    */   {
/* 187:232 */     return this.generator.nextFloat() * 2.0F - 1.0F;
/* 188:    */   }
/* 189:    */   
/* 190:    */   private float[][] getFractalTerrain(int size, float initDy, float decay, long seed)
/* 191:    */   {
/* 192:252 */     this.generator.setSeed(seed);
/* 193:    */     
/* 194:    */ 
/* 195:255 */     float[][] y = new float[size][size];
/* 196:256 */     int iMax = size - 1; float 
/* 197:257 */       tmp51_50 = (y[iMax][iMax] = y[0][iMax] = 0.0F);y[iMax][0] = tmp51_50;y[0][0] = tmp51_50;
/* 198:    */     
/* 199:    */ 
/* 200:260 */     float dy = initDy;
/* 201:    */     
/* 202:    */ 
/* 203:    */ 
/* 204:264 */     int halfStride = iMax / 2;
/* 205:265 */     for (int stride = iMax; stride > 1; dy /= decay)
/* 206:    */     {
/* 207:267 */       for (int i = 0; i < iMax; i += stride) {
/* 208:268 */         for (int j = 0; j < iMax; j += stride)
/* 209:    */         {
/* 210:269 */           float avg = 0.25F * (y[i][j] + y[(i + stride)][j] + y[i][(j + stride)] + y[(i + stride)][(j + stride)]);
/* 211:270 */           y[(i + halfStride)][(j + halfStride)] = (avg + random() * dy);
/* 212:    */         }
/* 213:    */       }
/* 214:274 */       for (int i = 0; i < size; i += stride) {
/* 215:275 */         for (int j = halfStride; j < size; j += stride)
/* 216:    */         {
/* 217:276 */           float e = y[i][(j - halfStride)] + y[i][(j + halfStride)];
/* 218:277 */           int in = i - halfStride;
/* 219:278 */           int is = i + halfStride;
/* 220:279 */           int n = 2;
/* 221:280 */           if (in >= 0)
/* 222:    */           {
/* 223:281 */             e += y[in][j];
/* 224:282 */             n++;
/* 225:    */           }
/* 226:284 */           if (is < size)
/* 227:    */           {
/* 228:285 */             e += y[is][j];
/* 229:286 */             n++;
/* 230:    */           }
/* 231:288 */           y[i][j] = (e / n + random() * dy);
/* 232:    */         }
/* 233:    */       }
/* 234:291 */       for (int i = halfStride; i < size; i += stride) {
/* 235:292 */         for (int j = 0; j < size; j += stride)
/* 236:    */         {
/* 237:293 */           float e = y[(i - halfStride)][j] + y[(i + halfStride)][j];
/* 238:294 */           int jw = j - halfStride;
/* 239:295 */           int je = j + halfStride;
/* 240:296 */           int n = 2;
/* 241:297 */           if (jw >= 0)
/* 242:    */           {
/* 243:298 */             e += y[i][jw];
/* 244:299 */             n++;
/* 245:    */           }
/* 246:301 */           if (je < size)
/* 247:    */           {
/* 248:302 */             e += y[i][je];
/* 249:303 */             n++;
/* 250:    */           }
/* 251:305 */           y[i][j] = (e / n + random() * dy);
/* 252:    */         }
/* 253:    */       }
/* 254:265 */       stride = halfStride;halfStride /= 2;
/* 255:    */     }
/* 256:309 */     return y;
/* 257:    */   }
/* 258:    */   
/* 259:    */   protected float yAnchor(float x, float z, float xAnchor, float zAnchor)
/* 260:    */   {
/* 261:322 */     return -0.35F + Math.max(0.0F, Math.max(Math.abs(x - xAnchor), Math.abs(z - zAnchor)) - this.metersPerGrid);
/* 262:    */   }
/* 263:    */   
/* 264:    */   protected float syntheticElevation(int i, int j, float grade)
/* 265:    */   {
/* 266:334 */     float x = (j - this.halfGridCount) * this.metersPerGrid;
/* 267:335 */     float z = (i - this.halfGridCount) * this.metersPerGrid;
/* 268:    */     
/* 269:337 */     float tWater = distToRiver(x, z);
/* 270:    */     
/* 271:339 */     float tRoadway = Math.abs(z);
/* 272:    */     
/* 273:341 */     float tFractalA = Math.min(1.0F, Math.max(0.0F, 0.1F * (tWater - 11.0F)));
/* 274:    */     
/* 275:343 */     float tFractalB = Math.min(1.0F, Math.max(0.0F, 0.2F * (tRoadway - 5.0F - 4.0F)));
/* 276:    */     
/* 277:345 */     float tFractal = Math.min(tFractalA, tFractalB);
/* 278:    */     
/* 279:347 */     float y = this.yFractal[i][j] * tFractal;
/* 280:    */     
/* 281:    */ 
/* 282:    */ 
/* 283:351 */     float tBluf = 28.799999F + 0.3F * tFractalB;
/* 284:357 */     if (tWater <= tBluf) {
/* 285:358 */       y -= 0.1041667F * Utility.sqr(tWater - tBluf);
/* 286:    */     }
/* 287:361 */     y = Math.max(-31.0F, y);
/* 288:    */     
/* 289:363 */     y += grade;
/* 290:    */     
/* 291:    */ 
/* 292:366 */     float tCut = Math.abs(z);
/* 293:367 */     float yRoad = this.roadCenterline[j].elevation - 0.05F;
/* 294:368 */     float yRise = (tCut - this.abutmentHalfWidth - this.metersPerGrid) * 1.0F;
/* 295:    */     
/* 296:    */ 
/* 297:371 */     float yCut = yRise >= 0.0F ? yRoad + yRise : yRoad;
/* 298:372 */     if (yCut <= y)
/* 299:    */     {
/* 300:373 */       y = yCut;
/* 301:    */     }
/* 302:376 */     else if ((-24.0F > x) || (x > 24.0F))
/* 303:    */     {
/* 304:377 */       float yFill = yRise >= 0.0F ? yRoad - yRise : yRoad;
/* 305:378 */       if (yFill >= y) {
/* 306:379 */         y = yFill;
/* 307:    */       }
/* 308:    */     }
/* 309:384 */     if (this.leftCable)
/* 310:    */     {
/* 311:385 */       if (z < -this.trussCenterOffset)
/* 312:    */       {
/* 313:386 */         float yAnchorNW = yAnchor(x, z, -8.0F - this.halfSpanLength, -this.trussCenterOffset);
/* 314:387 */         if (yAnchorNW < y) {
/* 315:388 */           y = yAnchorNW;
/* 316:    */         }
/* 317:    */       }
/* 318:391 */       if (z > this.trussCenterOffset)
/* 319:    */       {
/* 320:392 */         float yAnchorSW = yAnchor(x, z, -8.0F - this.halfSpanLength, this.trussCenterOffset);
/* 321:393 */         if (yAnchorSW < y) {
/* 322:394 */           y = yAnchorSW;
/* 323:    */         }
/* 324:    */       }
/* 325:    */     }
/* 326:398 */     if (this.rightCable)
/* 327:    */     {
/* 328:399 */       if (z < -this.trussCenterOffset)
/* 329:    */       {
/* 330:400 */         float yAnchorNE = yAnchor(x, z, 8.0F + this.halfSpanLength, -this.trussCenterOffset);
/* 331:401 */         if (yAnchorNE < y) {
/* 332:402 */           y = yAnchorNE;
/* 333:    */         }
/* 334:    */       }
/* 335:405 */       if (z > this.trussCenterOffset)
/* 336:    */       {
/* 337:406 */         float yAnchorSE = yAnchor(x, z, 8.0F + this.halfSpanLength, this.trussCenterOffset);
/* 338:407 */         if (yAnchorSE < y) {
/* 339:408 */           y = yAnchorSE;
/* 340:    */         }
/* 341:    */       }
/* 342:    */     }
/* 343:413 */     return y;
/* 344:    */   }
/* 345:    */   
/* 346:    */   protected float getElevationAt(int i, int j)
/* 347:    */   {
/* 348:417 */     if (i < 0) {
/* 349:418 */       i = 0;
/* 350:    */     }
/* 351:420 */     if (i >= this.postCount) {
/* 352:421 */       i = this.postCount - 1;
/* 353:    */     }
/* 354:423 */     if (j < 0) {
/* 355:424 */       j = 0;
/* 356:    */     }
/* 357:426 */     if (j >= this.postCount) {
/* 358:427 */       j = this.postCount - 1;
/* 359:    */     }
/* 360:429 */     return this.posts[i][j].elevation;
/* 361:    */   }
/* 362:    */   
/* 363:    */   public float getElevationAt(float x, float z)
/* 364:    */   {
/* 365:441 */     float i0f = (z + 192.0F) / this.metersPerGrid;
/* 366:442 */     int i0 = (int)i0f;
/* 367:443 */     float ti = i0f - i0;
/* 368:444 */     float j0f = (x - this.halfSpanLength + 192.0F) / this.metersPerGrid;
/* 369:445 */     int j0 = (int)j0f;
/* 370:446 */     float tj = j0f - j0;
/* 371:447 */     float e00 = getElevationAt(i0, j0);
/* 372:448 */     float e01 = getElevationAt(i0, j0 + 1);
/* 373:449 */     float et0 = e00 * (1.0F - tj) + e01 * tj;
/* 374:450 */     float e10 = getElevationAt(i0 + 1, j0);
/* 375:451 */     float e11 = getElevationAt(i0 + 1, j0 + 1);
/* 376:452 */     float et1 = e10 * (1.0F - tj) + e11 * tj;
/* 377:453 */     return Math.max(this.yWater, et0 * (1.0F - ti) + et1 * ti);
/* 378:    */   }
/* 379:    */   
/* 380:    */   protected float xGridToWorld(int j)
/* 381:    */   {
/* 382:457 */     return (j - this.halfGridCount) * this.metersPerGrid + this.halfSpanLength;
/* 383:    */   }
/* 384:    */   
/* 385:    */   protected float zGridToWorld(int i)
/* 386:    */   {
/* 387:461 */     return (i - this.halfGridCount) * this.metersPerGrid;
/* 388:    */   }
/* 389:    */   
/* 390:    */   public void initializeTerrain(DesignConditions conditions, float trussCenterOffset, float abutmentHalfWidth)
/* 391:    */   {
/* 392:473 */     this.abutmentHalfWidth = abutmentHalfWidth;
/* 393:474 */     this.halfSpanLength = ((float)(0.5D * conditions.getSpanLength()));
/* 394:    */     
/* 395:    */ 
/* 396:477 */     this.yGrade = ((float)(24.0D - conditions.getDeckElevation() + 0.800000011920929D));
/* 397:    */     
/* 398:    */ 
/* 399:480 */     this.yWater = (-26.0F + this.yGrade);
/* 400:481 */     this.trussCenterOffset = trussCenterOffset;
/* 401:    */     
/* 402:    */ 
/* 403:484 */     int iMax = this.postCount - 1;
/* 404:485 */     float x = 0.0F;
/* 405:486 */     float dy = this.yGrade - 0.8F;
/* 406:487 */     if (dy == 0.0F)
/* 407:    */     {
/* 408:488 */       for (int i = 0; i < this.postCount; i++) {
/* 409:489 */         this.roadCenterline[i] = new CenterlinePost(this.yGrade);
/* 410:    */       }
/* 411:    */     }
/* 412:    */     else
/* 413:    */     {
/* 414:493 */       float A = 0.01041667F;
/* 415:    */       
/* 416:495 */       float x0 = this.halfSpanLength;
/* 417:    */       
/* 418:497 */       float x1 = this.halfSpanLength + 8.0F;
/* 419:    */       
/* 420:499 */       float y1 = A * 8.0F * 8.0F;
/* 421:    */       
/* 422:501 */       float x2 = this.halfSpanLength + dy / 0.1666667F;
/* 423:    */       
/* 424:503 */       float x3 = x2 + 8.0F;
/* 425:504 */       int j = this.halfGridCount;
/* 426:505 */       for (int i = this.halfGridCount; i < this.postCount; j--)
/* 427:    */       {
/* 428:506 */         float y = 0.0F;
/* 429:507 */         if (x <= this.halfSpanLength)
/* 430:    */         {
/* 431:508 */           y = 0.8F;
/* 432:    */         }
/* 433:510 */         else if (x <= x1)
/* 434:    */         {
/* 435:511 */           float xp = x - x0;
/* 436:512 */           y = A * xp * xp + 0.8F;
/* 437:    */         }
/* 438:514 */         else if (x <= x2)
/* 439:    */         {
/* 440:515 */           float xp = x - x1;
/* 441:516 */           y = y1 + xp * 0.1666667F + 0.8F;
/* 442:    */         }
/* 443:518 */         else if (x <= x3)
/* 444:    */         {
/* 445:519 */           float xp = x - x3;
/* 446:520 */           y = this.yGrade - A * xp * xp;
/* 447:    */         }
/* 448:    */         else
/* 449:    */         {
/* 450:523 */           y = this.yGrade;
/* 451:    */         }
/* 452:525 */         this.roadCenterline[i] = new CenterlinePost(y);
/* 453:526 */         this.roadCenterline[j] = new CenterlinePost(y);
/* 454:527 */         x += this.metersPerGrid;i++;
/* 455:    */       }
/* 456:    */     }
/* 457:533 */     for (int i = 1; i < iMax - 1; i++)
/* 458:    */     {
/* 459:534 */       float e0 = this.roadCenterline[i].elevation;
/* 460:535 */       float xw = -this.metersPerGrid;
/* 461:536 */       float yw = this.roadCenterline[(i - 1)].elevation - e0;
/* 462:537 */       float xe = this.metersPerGrid;
/* 463:538 */       float ye = this.roadCenterline[(i + 1)].elevation - e0;
/* 464:539 */       double rw = 1.0D / Math.sqrt(xw * xw + yw * yw);
/* 465:540 */       xw = (float)(xw * rw);
/* 466:541 */       yw = (float)(yw * rw);
/* 467:542 */       double re = 1.0D / Math.sqrt(xe * xe + ye * ye);
/* 468:543 */       xe = (float)(xe * re);
/* 469:544 */       ye = (float)(ye * re);
/* 470:545 */       float xnw = yw;
/* 471:546 */       float ynw = -xw;
/* 472:547 */       float xne = -ye;
/* 473:548 */       float yne = xe;
/* 474:549 */       float nx = 0.5F * (xnw + xne);
/* 475:550 */       float ny = 0.5F * (ynw + yne);
/* 476:551 */       double rn = 1.0D / Math.sqrt(nx * nx + ny * ny);
/* 477:552 */       nx = (float)(nx * rn);
/* 478:553 */       ny = (float)(ny * rn);
/* 479:554 */       this.roadCenterline[i].xNormal = nx;
/* 480:555 */       this.roadCenterline[i].yNormal = ny;
/* 481:    */     }
/* 482:557 */     this.roadCenterline[0] = this.roadCenterline[1];
/* 483:558 */     this.roadCenterline[iMax] = this.roadCenterline[(iMax - 1)];
/* 484:    */     
/* 485:    */ 
/* 486:561 */     this.leftCable = conditions.isLeftAnchorage();
/* 487:562 */     this.rightCable = conditions.isRightAnchorage();
/* 488:566 */     for (int i = 0; i < this.postCount; i++) {
/* 489:567 */       for (int j = 0; j < this.postCount; j++) {
/* 490:568 */         this.posts[i][j] = new TerrainPost(syntheticElevation(i, j, this.yGrade));
/* 491:    */       }
/* 492:    */     }
/* 493:573 */     for (int i = 0; i < this.postCount; i++) {
/* 494:574 */       for (int j = 0; j < this.postCount; j++) {
/* 495:575 */         initializeTerrainNormal(i, j);
/* 496:    */       }
/* 497:    */     }
/* 498:579 */     initializeAbutment(conditions.isArch() ? (float)conditions.getUnderClearance() : 0.0F, abutmentHalfWidth);
/* 499:580 */     if (conditions.isPier()) {
/* 500:581 */       this.pierLocation = conditions.getPrescribedJointLocation(conditions.getPierJointIndex());
/* 501:    */     } else {
/* 502:584 */       this.pierLocation = null;
/* 503:    */     }
/* 504:587 */     initializePowerLines();
/* 505:    */   }
/* 506:    */   
/* 507:    */   private void initializeTerrainNormal(int i, int j)
/* 508:    */   {
/* 509:591 */     float xL = Animation.lightPosition.x();
/* 510:592 */     float yL = Animation.lightPosition.y();
/* 511:593 */     float zL = Animation.lightPosition.z();
/* 512:594 */     int iMax = this.postCount - 1;
/* 513:595 */     float xNormal = 0.0F;
/* 514:596 */     float yNormal = 0.0F;
/* 515:597 */     float zNormal = 0.0F;
/* 516:598 */     float e0 = this.posts[i][j].elevation;
/* 517:599 */     float xe = this.metersPerGrid;
/* 518:600 */     float ye = j < iMax ? this.posts[i][(j + 1)].elevation - e0 : 0.0F;
/* 519:601 */     float ze = 0.0F;
/* 520:    */     
/* 521:603 */     float xn = 0.0F;
/* 522:604 */     float yn = i > 0 ? this.posts[(i - 1)][j].elevation - e0 : 0.0F;
/* 523:605 */     float zn = -this.metersPerGrid;
/* 524:    */     
/* 525:607 */     float xl = -this.metersPerGrid;
/* 526:608 */     float yl = j > 0 ? this.posts[i][(j - 1)].elevation - e0 : 0.0F;
/* 527:609 */     float zl = 0.0F;
/* 528:    */     
/* 529:611 */     float xd = 0.0F;
/* 530:612 */     float yd = i < iMax ? this.posts[(i + 1)][j].elevation - e0 : 0.0F;
/* 531:613 */     float zd = this.metersPerGrid;
/* 532:    */     
/* 533:615 */     float xne = ye * zn - 0.0F * yn;
/* 534:616 */     float yne = 0.0F - xe * zn;
/* 535:617 */     float zne = xe * yn - ye * 0.0F;
/* 536:618 */     float rne = (float)(1.0D / Math.sqrt(xne * xne + yne * yne + zne * zne));
/* 537:625 */     if (xne * xL + yne * yL + zne * zL < 0.0F)
/* 538:    */     {
/* 539:626 */       this.posts[i][j].xNormal = (xne * rne);
/* 540:627 */       this.posts[i][j].yNormal = (yne * rne);
/* 541:628 */       this.posts[i][j].zNormal = (zne * rne);
/* 542:629 */       return;
/* 543:    */     }
/* 544:631 */     xNormal += xne * rne;
/* 545:632 */     yNormal += yne * rne;
/* 546:633 */     zNormal += zne * rne;
/* 547:    */     
/* 548:635 */     float xnw = yn * 0.0F - zn * yl;
/* 549:636 */     float ynw = zn * xl - 0.0F;
/* 550:637 */     float znw = 0.0F * yl - yn * xl;
/* 551:638 */     float rnw = (float)(1.0D / Math.sqrt(xnw * xnw + ynw * ynw + znw * znw));
/* 552:639 */     if (xnw * xL + ynw * yL + znw * zL < 0.0F)
/* 553:    */     {
/* 554:640 */       this.posts[i][j].xNormal = (xnw * rnw);
/* 555:641 */       this.posts[i][j].yNormal = (ynw * rnw);
/* 556:642 */       this.posts[i][j].zNormal = (znw * rnw);
/* 557:643 */       return;
/* 558:    */     }
/* 559:645 */     xNormal += xnw * rnw;
/* 560:646 */     yNormal += ynw * rnw;
/* 561:647 */     zNormal += znw * rnw;
/* 562:    */     
/* 563:649 */     float xsw = yl * zd - 0.0F * yd;
/* 564:650 */     float ysw = 0.0F - xl * zd;
/* 565:651 */     float zsw = xl * yd - yl * 0.0F;
/* 566:652 */     float rsw = (float)(1.0D / Math.sqrt(xsw * xsw + ysw * ysw + zsw * zsw));
/* 567:653 */     if (xsw * xL + ysw * yL + zsw * zL < 0.0F)
/* 568:    */     {
/* 569:654 */       this.posts[i][j].xNormal = (xsw * rsw);
/* 570:655 */       this.posts[i][j].yNormal = (ysw * rsw);
/* 571:656 */       this.posts[i][j].zNormal = (zsw * rsw);
/* 572:657 */       return;
/* 573:    */     }
/* 574:659 */     xNormal += xsw * rsw;
/* 575:660 */     yNormal += ysw * rsw;
/* 576:661 */     zNormal += zsw * rsw;
/* 577:662 */     float xse = yd * 0.0F - zd * ye;
/* 578:663 */     float yse = zd * xe - 0.0F;
/* 579:664 */     float zse = 0.0F * ye - yd * xe;
/* 580:665 */     float rse = (float)(1.0D / Math.sqrt(xse * xse + yse * yse + zse * zse));
/* 581:666 */     if (xse * xL + yse * yL + zse * zL < 0.0F)
/* 582:    */     {
/* 583:667 */       this.posts[i][j].xNormal = (xse * rse);
/* 584:668 */       this.posts[i][j].yNormal = (yse * rse);
/* 585:669 */       this.posts[i][j].zNormal = (zse * rse);
/* 586:670 */       return;
/* 587:    */     }
/* 588:672 */     xNormal += xse * rse;
/* 589:673 */     yNormal += yse * rse;
/* 590:674 */     zNormal += zse * rse;
/* 591:    */     
/* 592:676 */     float r = (float)(1.0D / Math.sqrt(xNormal * xNormal + yNormal * yNormal + zNormal * zNormal));
/* 593:677 */     this.posts[i][j].xNormal = (xNormal * r);
/* 594:678 */     this.posts[i][j].yNormal = (yNormal * r);
/* 595:679 */     this.posts[i][j].zNormal = (zNormal * r);
/* 596:    */   }
/* 597:    */   
/* 598:682 */   protected float[] abutmentFrontFlank = null;
/* 599:683 */   protected float[] abutmentFrontFlankTexture = null;
/* 600:684 */   protected float[] abutmentRearFlank = null;
/* 601:685 */   protected float[] abutmentRearFlankTexture = null;
/* 602:686 */   protected float[] abutmentFrontFace = null;
/* 603:687 */   protected float[] abutmentRearFace = null;
/* 604:688 */   protected float[] abutmentFrontTop = null;
/* 605:689 */   protected float[] abutmentRearTop = null;
/* 606:690 */   protected float[] abutmentFaceNormals = null;
/* 607:691 */   protected float[] abutmentFrontFaceTexture = null;
/* 608:692 */   protected float[] abutmentRearFaceTexture = null;
/* 609:693 */   protected float[] pillowFrontFace = new float[9];
/* 610:694 */   protected float[] pillowRearFace = new float[9];
/* 611:    */   protected static final float xWestTower = -116.0F;
/* 612:    */   protected static final float zWestTower = -102.0F;
/* 613:    */   protected static final float dxTower = 90.0F;
/* 614:    */   protected static final float dzTower = 70.0F;
/* 615:    */   
/* 616:    */   private void initializeAbutment(float archHeight, float halfWidth)
/* 617:    */   {
/* 618:705 */     this.abutmentFaceNormals = new float[] { 1.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 0.0F, 0.0F };
/* 619:    */     
/* 620:    */ 
/* 621:    */ 
/* 622:    */ 
/* 623:710 */     this.abutmentRearFace = new float[this.abutmentFaceNormals.length + 3];
/* 624:711 */     this.abutmentFrontFace = new float[this.abutmentFaceNormals.length + 3];
/* 625:    */     
/* 626:713 */     int iFace = 0;
/* 627:714 */     int iFlank = 0;
/* 628:715 */     int iPillow = 0; float 
/* 629:    */     
/* 630:717 */       tmp97_95 = -0.45F;this.abutmentFrontFace[(iFace + 0)] = tmp97_95;this.abutmentRearFace[(iFace + 0)] = tmp97_95; float 
/* 631:718 */       tmp116_114 = 0.8F;this.abutmentFrontFace[(iFace + 1)] = tmp116_114;this.abutmentRearFace[(iFace + 1)] = tmp116_114;
/* 632:719 */     this.abutmentRearFace[(iFace + 2)] = (-halfWidth);
/* 633:720 */     this.abutmentFrontFace[(iFace + 2)] = halfWidth;
/* 634:721 */     iFace += 3;
/* 635:    */     
/* 636:    */ 
/* 637:724 */     float[] flank = new float[3 * (this.postCount + 4)];
/* 638:728 */     for (int jAbutmentLeft = this.halfGridCount; jAbutmentLeft > 1; jAbutmentLeft--) {
/* 639:729 */       if (this.roadCenterline[jAbutmentLeft].elevation - this.posts[this.halfGridCount][jAbutmentLeft].elevation <= 0.1F) {
/* 640:    */         break;
/* 641:    */       }
/* 642:    */     }
/* 643:733 */     jAbutmentLeft--;
/* 644:734 */     float xAbutmentLeft = xGridToWorld(jAbutmentLeft); float 
/* 645:    */     
/* 646:    */ 
/* 647:737 */       tmp261_260 = (flank[(iFlank + 0)] = this.pillowRearFace[(iPillow + 0)] = this.pillowFrontFace[(iPillow + 0)] = -0.45F);this.abutmentFrontFace[(iFace + 0)] = tmp261_260;this.abutmentRearFace[(iFace + 0)] = tmp261_260; long 
/* 648:    */     
/* 649:739 */       tmp310_309 = (flank[(iFlank + 1)] = this.pillowRearFace[(iPillow + 1)] = this.pillowFrontFace[(iPillow + 1)] = -0.35F - archHeight);this.abutmentFrontFace[(iFace + 1)] = tmp310_309;this.abutmentRearFace[(iFace + 1)] = tmp310_309;
/* 650:    */     
/* 651:741 */     flank[(iFlank + 2)] = (-halfWidth); float 
/* 652:742 */       tmp339_338 = (-halfWidth);this.pillowRearFace[(iPillow + 2)] = tmp339_338;this.abutmentRearFace[(iFace + 2)] = tmp339_338; float 
/* 653:743 */       tmp358_357 = halfWidth;this.pillowFrontFace[(iPillow + 2)] = tmp358_357;this.abutmentFrontFace[(iFace + 2)] = tmp358_357;
/* 654:744 */     iFace += 3;
/* 655:745 */     iFlank += 3;
/* 656:746 */     iPillow += 3; float 
/* 657:    */     
/* 658:    */ 
/* 659:749 */       tmp388_386 = -0.09999999F;this.pillowFrontFace[(iPillow + 0)] = tmp388_386;this.pillowRearFace[(iPillow + 0)] = tmp388_386; float 
/* 660:750 */       tmp409_408 = (-archHeight);this.pillowFrontFace[(iPillow + 1)] = tmp409_408;this.pillowRearFace[(iPillow + 1)] = tmp409_408;
/* 661:751 */     this.pillowRearFace[(iPillow + 2)] = (-halfWidth);
/* 662:752 */     this.pillowFrontFace[(iPillow + 2)] = halfWidth;
/* 663:753 */     iPillow += 3; float 
/* 664:    */     
/* 665:    */ 
/* 666:756 */       tmp480_479 = (flank[(iFlank + 0)] = this.pillowRearFace[(iPillow + 0)] = this.pillowFrontFace[(iPillow + 0)] = 0.25F);this.abutmentFrontFace[(iFace + 0)] = tmp480_479;this.abutmentRearFace[(iFace + 0)] = tmp480_479; long 
/* 667:    */     
/* 668:758 */       tmp529_528 = (flank[(iFlank + 1)] = this.pillowRearFace[(iPillow + 1)] = this.pillowFrontFace[(iPillow + 1)] = -0.35F - archHeight);this.abutmentFrontFace[(iFace + 1)] = tmp529_528;this.abutmentRearFace[(iFace + 1)] = tmp529_528;
/* 669:    */     
/* 670:760 */     flank[(iFlank + 2)] = (-halfWidth); float 
/* 671:761 */       tmp558_557 = (-halfWidth);this.pillowRearFace[(iPillow + 2)] = tmp558_557;this.abutmentRearFace[(iFace + 2)] = tmp558_557; float 
/* 672:762 */       tmp577_576 = halfWidth;this.pillowFrontFace[(iPillow + 2)] = tmp577_576;this.abutmentFrontFace[(iFace + 2)] = tmp577_576;
/* 673:763 */     iFlank += 3;
/* 674:764 */     iFace += 3; float 
/* 675:    */     
/* 676:    */ 
/* 677:767 */       tmp610_609 = (flank[(iFlank + 0)] = 0.25F);this.abutmentFrontFace[(iFace + 0)] = tmp610_609;this.abutmentRearFace[(iFace + 0)] = tmp610_609; float 
/* 678:768 */       tmp639_638 = (flank[(iFlank + 1)] = this.yWater);this.abutmentFrontFace[(iFace + 1)] = tmp639_638;this.abutmentRearFace[(iFace + 1)] = tmp639_638;
/* 679:769 */     flank[(iFlank + 2)] = (-halfWidth);
/* 680:770 */     this.abutmentRearFace[(iFace + 2)] = (-halfWidth);
/* 681:771 */     this.abutmentFrontFace[(iFace + 2)] = halfWidth;
/* 682:772 */     iFlank += 3;
/* 683:773 */     iFace += 3;
/* 684:    */     
/* 685:    */ 
/* 686:776 */     flank[(iFlank + 0)] = xAbutmentLeft;
/* 687:777 */     flank[(iFlank + 1)] = this.yWater;
/* 688:778 */     flank[(iFlank + 2)] = (-halfWidth);
/* 689:779 */     iFlank += 3;
/* 690:    */     
/* 691:    */ 
/* 692:782 */     int iTop = iFlank;
/* 693:783 */     float x = xAbutmentLeft;
/* 694:784 */     for (int j = jAbutmentLeft; j < this.postCount; j++)
/* 695:    */     {
/* 696:785 */       if (x >= -0.45F)
/* 697:    */       {
/* 698:787 */         flank[(iFlank + 0)] = -0.45F;
/* 699:788 */         flank[(iFlank + 1)] = 0.8F;
/* 700:789 */         flank[(iFlank + 2)] = (-halfWidth);
/* 701:790 */         iFlank += 3;
/* 702:791 */         break;
/* 703:    */       }
/* 704:794 */       flank[(iFlank + 0)] = x;
/* 705:795 */       flank[(iFlank + 1)] = (this.roadCenterline[j].elevation - 0.03F);
/* 706:796 */       flank[(iFlank + 2)] = (-halfWidth);
/* 707:797 */       iFlank += 3;
/* 708:    */       
/* 709:799 */       x += this.metersPerGrid;
/* 710:    */     }
/* 711:802 */     this.abutmentFrontTop = new float[iFlank - iTop];
/* 712:803 */     this.abutmentRearTop = new float[iFlank - iTop];
/* 713:804 */     for (int i = 0; i < this.abutmentFrontTop.length; i += 3)
/* 714:    */     {
/* 715:805 */       float tmp887_886 = flank[(iTop + i + 0)];this.abutmentFrontTop[(i + 0)] = tmp887_886;this.abutmentRearTop[(i + 0)] = tmp887_886; float 
/* 716:806 */         tmp916_915 = flank[(iTop + i + 1)];this.abutmentFrontTop[(i + 1)] = tmp916_915;this.abutmentRearTop[(i + 1)] = tmp916_915;
/* 717:807 */       this.abutmentRearTop[(i + 2)] = flank[(iTop + i + 2)];
/* 718:808 */       this.abutmentFrontTop[(i + 2)] = (-flank[(iTop + i + 2)]);
/* 719:    */     }
/* 720:810 */     this.abutmentFrontFlank = new float[iFlank];
/* 721:811 */     this.abutmentRearFlank = new float[iFlank];
/* 722:    */     
/* 723:    */ 
/* 724:814 */     System.arraycopy(flank, 0, this.abutmentRearFlank, 0, iFlank);
/* 725:    */     
/* 726:816 */     this.abutmentFrontFlank[0] = this.abutmentRearFlank[0];
/* 727:817 */     this.abutmentFrontFlank[1] = this.abutmentRearFlank[1];
/* 728:818 */     this.abutmentFrontFlank[2] = (-this.abutmentRearFlank[2]);
/* 729:819 */     int j = this.abutmentRearFlank.length - 3;
/* 730:820 */     for (int i = 3; i < iFlank; j -= 3)
/* 731:    */     {
/* 732:821 */       this.abutmentFrontFlank[(i + 0)] = this.abutmentRearFlank[(j + 0)];
/* 733:822 */       this.abutmentFrontFlank[(i + 1)] = this.abutmentRearFlank[(j + 1)];
/* 734:823 */       this.abutmentFrontFlank[(i + 2)] = (-this.abutmentRearFlank[(j + 2)]);i += 3;
/* 735:    */     }
/* 736:826 */     this.abutmentFrontFaceTexture = new float[this.abutmentFrontFace.length * 2 / 3];
/* 737:827 */     this.abutmentRearFaceTexture = new float[this.abutmentFrontFace.length * 2 / 3];
/* 738:828 */     int i3 = 0;
/* 739:829 */     for (int i2 = 0; i2 < this.abutmentFrontFaceTexture.length; i3 += 3)
/* 740:    */     {
/* 741:830 */       this.abutmentFrontFaceTexture[i2] = (0.3F * this.abutmentFrontFace[(i3 + 2)]);
/* 742:831 */       this.abutmentRearFaceTexture[i2] = (0.3F * this.abutmentRearFace[(i3 + 2)]); float 
/* 743:832 */         tmp1225_1224 = (0.3F * this.abutmentFrontFace[(i3 + 1)]);this.abutmentRearFaceTexture[(i2 + 1)] = tmp1225_1224;this.abutmentFrontFaceTexture[(i2 + 1)] = tmp1225_1224;i2 += 2;
/* 744:    */     }
/* 745:834 */     this.abutmentFrontFlankTexture = new float[this.abutmentFrontFlank.length * 2 / 3];
/* 746:835 */     this.abutmentRearFlankTexture = new float[this.abutmentFrontFlank.length * 2 / 3];
/* 747:836 */     j = 0;
/* 748:837 */     for (int i = 0; i < this.abutmentFrontFlankTexture.length; j += 3)
/* 749:    */     {
/* 750:838 */       this.abutmentFrontFlankTexture[i] = (0.3F * this.abutmentFrontFlank[j]);
/* 751:839 */       this.abutmentFrontFlankTexture[(i + 1)] = (0.3F * this.abutmentFrontFlank[(j + 1)]);
/* 752:840 */       this.abutmentRearFlankTexture[i] = (0.3F * this.abutmentRearFlank[j]);
/* 753:841 */       this.abutmentRearFlankTexture[(i + 1)] = (0.3F * this.abutmentRearFlank[(j + 1)]);i += 2;
/* 754:    */     }
/* 755:    */   }
/* 756:    */   
/* 757:    */   protected void setToTerrainY(float[] v, int i, float dy)
/* 758:    */   {
/* 759:846 */     v[(i + 1)] = (getElevationAt(v[(i + 0)], v[(i + 2)]) + dy);
/* 760:    */   }
/* 761:    */   
/* 762:855 */   protected static final float dTower = (float)Math.sqrt(13000.0D);
/* 763:856 */   protected static final float xUnitPerpTower = -70.0F / dTower;
/* 764:857 */   protected static final float zUnitPerpTower = 90.0F / dTower;
/* 765:858 */   protected static final float thetaTower = -(float)Math.toDegrees(Math.atan2(70.0D, 90.0D));
/* 766:    */   protected static final int towerCount = 4;
/* 767:    */   protected static final int wirePostCountPerTower = 20;
/* 768:    */   protected static final float dxWire = 4.5F;
/* 769:    */   protected static final float dzWire = 3.5F;
/* 770:    */   protected static final float droopSlope = -0.1F;
/* 771:864 */   protected static final float[] wireColor = { 0.4F, 0.4F, 0.4F, 1.0F };
/* 772:868 */   protected static final Homogeneous.Point[] wireOffsets = { new Homogeneous.Point(-2.48F, 10.9F, 0.0F, 0.0F), new Homogeneous.Point(-2.48F, 12.4F, 0.0F, 0.0F), new Homogeneous.Point(-2.48F, 13.9F, 0.0F, 0.0F), new Homogeneous.Point(2.48F, 10.9F, 0.0F, 0.0F), new Homogeneous.Point(2.48F, 12.4F, 0.0F, 0.0F), new Homogeneous.Point(2.48F, 13.9F, 0.0F, 0.0F) };
/* 773:877 */   protected final Homogeneous.Point[] towerPt = new Homogeneous.Point[4];
/* 774:878 */   protected final Homogeneous.Point[][] wirePt = new Homogeneous.Point[3][21];
/* 775:    */   
/* 776:    */   private void initializePowerLines()
/* 777:    */   {
/* 778:885 */     for (int iTower = 0; iTower < 4; iTower++)
/* 779:    */     {
/* 780:886 */       float xTower = -116.0F + iTower * 90.0F;
/* 781:887 */       float zTower = -102.0F + iTower * 70.0F;
/* 782:    */       
/* 783:889 */       this.towerPt[iTower] = new Homogeneous.Point(xTower, getElevationAt(xTower, zTower) - 0.5F, zTower);
/* 784:890 */       if (iTower > 0)
/* 785:    */       {
/* 786:891 */         Homogeneous.Point p0 = this.towerPt[(iTower - 1)];Homogeneous.Point p1 = this.towerPt[iTower];
/* 787:892 */         float dx = p1.x() - p0.x();
/* 788:893 */         float dy = p1.y() - p0.y();
/* 789:894 */         float dz = p1.z() - p0.z();
/* 790:895 */         float du = (float)Math.sqrt(dx * dx + dz * dz);
/* 791:896 */         float m = dy / du + -0.1F;
/* 792:897 */         float a = (dy - m * du) / (du * du);
/* 793:898 */         for (int iWire = 0; iWire <= 20; iWire++)
/* 794:    */         {
/* 795:899 */           float t = iWire / 20.0F;
/* 796:900 */           float u = du * t;
/* 797:901 */           this.wirePt[(iTower - 1)][iWire] = new Homogeneous.Point(p0.x() + dx * t, p0.y() + (a * u + m) * u, p0.z() + dz * t);
/* 798:    */         }
/* 799:    */       }
/* 800:    */     }
/* 801:    */   }
/* 802:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.TerrainModel
 * JD-Core Version:    0.7.0.1
 */