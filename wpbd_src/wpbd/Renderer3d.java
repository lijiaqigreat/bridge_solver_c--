/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.awt.Color;
/*   4:    */ import java.awt.Component;
/*   5:    */ import java.awt.GradientPaint;
/*   6:    */ import java.awt.Graphics2D;
/*   7:    */ import java.awt.Paint;
/*   8:    */ 
/*   9:    */ public class Renderer3d
/*  10:    */ {
/*  11:    */   public static final int TRIANGLE_STRIP = 1;
/*  12:    */   public static final int TRIANGLE_FAN = 2;
/*  13:    */   public static final int LINES = 3;
/*  14:    */   public static final int QUAD_STRIP = 4;
/*  15:    */   public static final int POLYGON = 5;
/*  16:    */   public static final int LINE_STRIP = 6;
/*  17:    */   public static final int TRIANGLES = 7;
/*  18:    */   public static final int RULED_QUAD_STRIP = 8;
/*  19:    */   public static final int RULED_POLYGON = 9;
/*  20:    */   public static final int RULE_U = 1;
/*  21:    */   public static final int RULE_V = 2;
/*  22:    */   public static final int RULE_UV = 4;
/*  23:    */   public static final int RULE_VU = 8;
/*  24:    */   public static final int GOURAUD_TRIANGLE_STRIP = 10;
/*  25:    */   public static final int GOURAUD_TRIANGLE_FAN = 11;
/*  26: 30 */   private final double[] xViewportPoints = new double[32];
/*  27: 31 */   private final double[] yViewportPoints = new double[32];
/*  28: 32 */   private final float[] zViewportPoints = new float[32];
/*  29: 33 */   private final float[] sViewportPoints = new float[32];
/*  30: 34 */   private final int[] xBuf = new int[32];
/*  31: 35 */   private final int[] yBuf = new int[32];
/*  32: 36 */   private final Homogeneous.Matrix M = new Homogeneous.Matrix();
/*  33: 37 */   private final Homogeneous.Matrix T = new Homogeneous.Matrix();
/*  34: 38 */   private final Homogeneous.Matrix[] stack = { new Homogeneous.Matrix(), new Homogeneous.Matrix(), new Homogeneous.Matrix() };
/*  35: 42 */   private int stackPointer = 0;
/*  36: 43 */   private Homogeneous.Matrix modelTransform = null;
/*  37: 44 */   private int nPoints = 0;
/*  38: 45 */   private int kind = 0;
/*  39: 46 */   private Paint paint = Color.WHITE;
/*  40: 47 */   private Paint rulePaint = Color.BLACK;
/*  41: 48 */   private int parity = 0;
/*  42: 49 */   private boolean cull = false;
/*  43: 50 */   private boolean approximateGouraud = false;
/*  44: 51 */   private int ruleFlags = 3;
/*  45:    */   private int[] gouraudColor;
/*  46:    */   private static final float cFog = 0.0025F;
/*  47:    */   
/*  48:    */   private void convert()
/*  49:    */   {
/*  50: 57 */     for (int i = 0; i < this.nPoints; i++)
/*  51:    */     {
/*  52: 58 */       this.xBuf[i] = ((int)(0.5D + this.xViewportPoints[i]));
/*  53: 59 */       this.yBuf[i] = ((int)(0.5D + this.yViewportPoints[i]));
/*  54:    */     }
/*  55:    */   }
/*  56:    */   
/*  57:    */   public void begin(int kind)
/*  58:    */   {
/*  59: 64 */     begin(kind, 0);
/*  60:    */   }
/*  61:    */   
/*  62:    */   public void begin(int kind, int salvage)
/*  63:    */   {
/*  64: 68 */     this.nPoints = salvage;
/*  65: 69 */     this.kind = kind;
/*  66: 70 */     this.parity = 0;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public void setCulling(boolean cull)
/*  70:    */   {
/*  71: 74 */     this.cull = cull;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public void setApproximateGouraud(boolean approximateGouraud)
/*  75:    */   {
/*  76: 79 */     this.approximateGouraud = approximateGouraud;
/*  77:    */   }
/*  78:    */   
/*  79:    */   private boolean cull()
/*  80:    */   {
/*  81: 82 */     if ((!this.cull) || (this.nPoints <= 2)) {
/*  82: 83 */       return false;
/*  83:    */     }
/*  84: 86 */     double nz = 0.0D;
/*  85: 87 */     int j = this.nPoints - 1;
/*  86: 88 */     for (int i = 0; i < this.nPoints; j = i++) {
/*  87: 89 */       nz += (this.xViewportPoints[j] - this.xViewportPoints[i]) * (this.yViewportPoints[j] + this.yViewportPoints[i]);
/*  88:    */     }
/*  89: 91 */     return nz >= 0.0D;
/*  90:    */   }
/*  91:    */   
/*  92:    */   public void pushModelTransform()
/*  93:    */   {
/*  94: 95 */     this.stack[(this.stackPointer++)].set(this.M);
/*  95:    */   }
/*  96:    */   
/*  97:    */   public void popModelTransform()
/*  98:    */   {
/*  99: 99 */     this.M.set(this.stack[(--this.stackPointer)]);
/* 100:    */   }
/* 101:    */   
/* 102:    */   public void restoreModelTransform()
/* 103:    */   {
/* 104:103 */     this.M.set(this.stack[(this.stackPointer - 1)]);
/* 105:    */   }
/* 106:    */   
/* 107:    */   public void enableModelTransform()
/* 108:    */   {
/* 109:107 */     this.modelTransform = this.M;
/* 110:108 */     this.M.setIdentity();
/* 111:    */   }
/* 112:    */   
/* 113:    */   public void enableModelTransform(Homogeneous.Matrix initial)
/* 114:    */   {
/* 115:112 */     this.modelTransform = this.M;
/* 116:113 */     this.M.set(initial);
/* 117:    */   }
/* 118:    */   
/* 119:    */   public void disableModelTransform()
/* 120:    */   {
/* 121:117 */     this.modelTransform = null;
/* 122:    */   }
/* 123:    */   
/* 124:    */   public void prependTransform(Homogeneous.Matrix t)
/* 125:    */   {
/* 126:121 */     this.T.set(this.M);
/* 127:122 */     this.M.multiplyInto(t, this.T);
/* 128:    */   }
/* 129:    */   
/* 130:    */   public void appendTransform(Homogeneous.Matrix t)
/* 131:    */   {
/* 132:126 */     this.T.set(this.M);
/* 133:127 */     this.M.multiplyInto(this.T, t);
/* 134:    */   }
/* 135:    */   
/* 136:    */   public void setPaint(Paint paint)
/* 137:    */   {
/* 138:131 */     this.paint = paint;
/* 139:    */   }
/* 140:    */   
/* 141:    */   public void setRuleFlags(int ruleFlags)
/* 142:    */   {
/* 143:135 */     this.ruleFlags = ruleFlags;
/* 144:    */   }
/* 145:    */   
/* 146:    */   public void setRulePaint(Paint rulePaint)
/* 147:    */   {
/* 148:139 */     this.rulePaint = rulePaint;
/* 149:    */   }
/* 150:    */   
/* 151:    */   public void addVertex(Graphics2D g, ViewportTransform viewportTransform, Homogeneous.Point p)
/* 152:    */   {
/* 153:143 */     addVertex(g, viewportTransform, p.a[0], p.a[1], p.a[2]);
/* 154:    */   }
/* 155:    */   
/* 156:    */   public void addVertex(Graphics2D g, ViewportTransform viewportTransform, float[] v, int i)
/* 157:    */   {
/* 158:147 */     addVertex(g, viewportTransform, v[(i + 0)], v[(i + 1)], v[(i + 2)]);
/* 159:    */   }
/* 160:    */   
/* 161:    */   public void addVertex(Graphics2D g, ViewportTransform viewportTransform, float[] v, int i, int n)
/* 162:    */   {
/* 163:151 */     while (n > 0)
/* 164:    */     {
/* 165:152 */       addVertex(g, viewportTransform, v[(i + 0)], v[(i + 1)], v[(i + 2)]);
/* 166:153 */       i += 3;
/* 167:154 */       n--;
/* 168:    */     }
/* 169:156 */     while (n < 0)
/* 170:    */     {
/* 171:157 */       addVertex(g, viewportTransform, v[(i + 0)], v[(i + 1)], v[(i + 2)]);
/* 172:158 */       i -= 3;
/* 173:159 */       n++;
/* 174:    */     }
/* 175:    */   }
/* 176:    */   
/* 177:    */   public void addVertex(Graphics2D g, ViewportTransform viewportTransform, float[] u, int iu, float[] v, int iv, Paint[] paints, int ip, int n)
/* 178:    */   {
/* 179:168 */     while (n > 0)
/* 180:    */     {
/* 181:169 */       if (paints != null)
/* 182:    */       {
/* 183:170 */         this.paint = paints[ip];
/* 184:171 */         ip++;
/* 185:    */       }
/* 186:173 */       addVertex(g, viewportTransform, u[(iu + 0)], u[(iu + 1)], u[(iu + 2)]);
/* 187:174 */       addVertex(g, viewportTransform, v[(iv + 0)], v[(iv + 1)], v[(iv + 2)]);
/* 188:175 */       iu += 3;
/* 189:176 */       iv += 3;
/* 190:177 */       n--;
/* 191:    */     }
/* 192:179 */     while (n < 0)
/* 193:    */     {
/* 194:180 */       if (paints != null)
/* 195:    */       {
/* 196:181 */         this.paint = paints[ip];
/* 197:182 */         ip--;
/* 198:    */       }
/* 199:184 */       addVertex(g, viewportTransform, u[(iu + 0)], u[(iu + 1)], u[(iu + 2)]);
/* 200:185 */       addVertex(g, viewportTransform, v[(iv + 0)], v[(iv + 1)], v[(iv + 2)]);
/* 201:186 */       iu -= 3;
/* 202:187 */       iv -= 3;
/* 203:188 */       n++;
/* 204:    */     }
/* 205:    */   }
/* 206:    */   
/* 207:    */   public void addVertex(Graphics2D g, ViewportTransform viewportTransform, float x, float y, float z, float s)
/* 208:    */   {
/* 209:193 */     this.sViewportPoints[this.nPoints] = s;
/* 210:194 */     addVertex(g, viewportTransform, x, y, z);
/* 211:    */   }
/* 212:    */   
/* 213:    */   public void addVertex(Graphics2D g, ViewportTransform viewportTransform, Homogeneous.Point p, float s)
/* 214:    */   {
/* 215:198 */     addVertex(g, viewportTransform, p.a[0], p.a[1], p.a[2], s);
/* 216:    */   }
/* 217:    */   
/* 218:    */   public void addVertex(Graphics2D g, ViewportTransform viewportTransform, float x, float y, float z)
/* 219:    */   {
/* 220:202 */     if (this.modelTransform != null)
/* 221:    */     {
/* 222:203 */       float[] a = this.modelTransform.a;
/* 223:204 */       float xp = x * a[0] + y * a[4] + z * a[8] + a[12];
/* 224:205 */       float yp = x * a[1] + y * a[5] + z * a[9] + a[13];
/* 225:206 */       float zp = x * a[2] + y * a[6] + z * a[10] + a[14];
/* 226:207 */       x = xp;
/* 227:208 */       y = yp;
/* 228:209 */       z = zp;
/* 229:    */     }
/* 230:211 */     if (!viewportTransform.worldToViewport(this.xViewportPoints, this.yViewportPoints, this.nPoints, x, y, z))
/* 231:    */     {
/* 232:212 */       this.nPoints = 0;
/* 233:213 */       return;
/* 234:    */     }
/* 235:215 */     this.zViewportPoints[this.nPoints] = z;
/* 236:216 */     this.nPoints += 1;
/* 237:217 */     switch (this.kind)
/* 238:    */     {
/* 239:    */     case 1: 
/* 240:219 */       if (this.nPoints == 3)
/* 241:    */       {
/* 242:220 */         if (!cull())
/* 243:    */         {
/* 244:221 */           g.setPaint(this.paint);
/* 245:222 */           convert();
/* 246:223 */           g.fillPolygon(this.xBuf, this.yBuf, 3);
/* 247:    */         }
/* 248:225 */         this.xViewportPoints[this.parity] = this.xViewportPoints[2];
/* 249:226 */         this.yViewportPoints[this.parity] = this.yViewportPoints[2];
/* 250:227 */         this.parity = (1 - this.parity);
/* 251:228 */         this.nPoints = 2;
/* 252:    */       }
/* 253:    */       break;
/* 254:    */     case 10: 
/* 255:232 */       if (this.nPoints == 3)
/* 256:    */       {
/* 257:233 */         if (!cull())
/* 258:    */         {
/* 259:234 */           convert();
/* 260:235 */           drawGouraudTriangle(null, g, this.xBuf, this.yBuf, this.sViewportPoints, this.zViewportPoints);
/* 261:    */         }
/* 262:237 */         this.xViewportPoints[this.parity] = this.xViewportPoints[2];
/* 263:238 */         this.yViewportPoints[this.parity] = this.yViewportPoints[2];
/* 264:239 */         this.zViewportPoints[this.parity] = this.zViewportPoints[2];
/* 265:240 */         this.sViewportPoints[this.parity] = this.sViewportPoints[2];
/* 266:241 */         this.parity = (1 - this.parity);
/* 267:242 */         this.nPoints = 2;
/* 268:    */       }
/* 269:    */       break;
/* 270:    */     case 2: 
/* 271:246 */       if (this.nPoints == 3)
/* 272:    */       {
/* 273:247 */         if (!cull())
/* 274:    */         {
/* 275:248 */           g.setPaint(this.paint);
/* 276:249 */           convert();
/* 277:250 */           g.fillPolygon(this.xBuf, this.yBuf, 3);
/* 278:    */         }
/* 279:252 */         this.xViewportPoints[1] = this.xViewportPoints[2];
/* 280:253 */         this.yViewportPoints[1] = this.yViewportPoints[2];
/* 281:254 */         this.nPoints = 2;
/* 282:    */       }
/* 283:    */       break;
/* 284:    */     case 11: 
/* 285:258 */       if (this.nPoints == 3)
/* 286:    */       {
/* 287:259 */         if (!cull())
/* 288:    */         {
/* 289:260 */           convert();
/* 290:261 */           drawGouraudTriangle(null, g, this.xBuf, this.yBuf, this.sViewportPoints, this.zViewportPoints);
/* 291:    */         }
/* 292:263 */         this.xViewportPoints[1] = this.xViewportPoints[2];
/* 293:264 */         this.yViewportPoints[1] = this.yViewportPoints[2];
/* 294:265 */         this.zViewportPoints[1] = this.zViewportPoints[2];
/* 295:266 */         this.sViewportPoints[1] = this.sViewportPoints[2];
/* 296:267 */         this.nPoints = 2;
/* 297:    */       }
/* 298:    */       break;
/* 299:    */     case 3: 
/* 300:271 */       if (this.nPoints == 2)
/* 301:    */       {
/* 302:272 */         g.setPaint(this.paint);
/* 303:273 */         convert();
/* 304:274 */         g.drawLine(this.xBuf[0], this.yBuf[0], this.xBuf[1], this.yBuf[1]);
/* 305:275 */         this.nPoints = 0;
/* 306:    */       }
/* 307:    */       break;
/* 308:    */     case 4: 
/* 309:279 */       if (this.nPoints == 4)
/* 310:    */       {
/* 311:280 */         double tx = this.xViewportPoints[0];
/* 312:281 */         double ty = this.yViewportPoints[0];
/* 313:282 */         this.xViewportPoints[0] = this.xViewportPoints[2];
/* 314:283 */         this.yViewportPoints[0] = this.yViewportPoints[2];
/* 315:284 */         this.xViewportPoints[2] = this.xViewportPoints[1];
/* 316:285 */         this.yViewportPoints[2] = this.yViewportPoints[1];
/* 317:286 */         this.xViewportPoints[1] = this.xViewportPoints[3];
/* 318:287 */         this.yViewportPoints[1] = this.yViewportPoints[3];
/* 319:288 */         this.xViewportPoints[3] = tx;
/* 320:289 */         this.yViewportPoints[3] = ty;
/* 321:290 */         if (!cull())
/* 322:    */         {
/* 323:291 */           g.setPaint(this.paint);
/* 324:292 */           convert();
/* 325:293 */           g.fillPolygon(this.xBuf, this.yBuf, 4);
/* 326:    */         }
/* 327:295 */         this.nPoints = 2;
/* 328:    */       }
/* 329:296 */       break;
/* 330:    */     case 8: 
/* 331:299 */       if (this.nPoints == 4)
/* 332:    */       {
/* 333:300 */         double tx = this.xViewportPoints[0];
/* 334:301 */         double ty = this.yViewportPoints[0];
/* 335:302 */         this.xViewportPoints[0] = this.xViewportPoints[2];
/* 336:303 */         this.yViewportPoints[0] = this.yViewportPoints[2];
/* 337:304 */         this.xViewportPoints[2] = this.xViewportPoints[1];
/* 338:305 */         this.yViewportPoints[2] = this.yViewportPoints[1];
/* 339:306 */         this.xViewportPoints[1] = this.xViewportPoints[3];
/* 340:307 */         this.yViewportPoints[1] = this.yViewportPoints[3];
/* 341:308 */         this.xViewportPoints[3] = tx;
/* 342:309 */         this.yViewportPoints[3] = ty;
/* 343:310 */         if (!cull())
/* 344:    */         {
/* 345:311 */           g.setPaint(this.paint);
/* 346:312 */           convert();
/* 347:313 */           g.fillPolygon(this.xBuf, this.yBuf, 4);
/* 348:314 */           g.setPaint(this.rulePaint);
/* 349:315 */           if ((this.ruleFlags & 0x1) != 0) {
/* 350:316 */             g.drawLine(this.xBuf[3], this.yBuf[3], this.xBuf[0], this.yBuf[0]);
/* 351:    */           }
/* 352:318 */           if ((this.ruleFlags & 0x2) != 0) {
/* 353:319 */             g.drawLine(this.xBuf[1], this.yBuf[1], this.xBuf[2], this.yBuf[2]);
/* 354:    */           }
/* 355:    */         }
/* 356:322 */         this.nPoints = 2;
/* 357:    */       }
/* 358:323 */       break;
/* 359:    */     case 7: 
/* 360:326 */       if (this.nPoints == 3)
/* 361:    */       {
/* 362:327 */         if (!cull())
/* 363:    */         {
/* 364:328 */           g.setPaint(this.paint);
/* 365:329 */           convert();
/* 366:330 */           g.fillPolygon(this.xBuf, this.yBuf, 3);
/* 367:    */         }
/* 368:332 */         this.nPoints = 0;
/* 369:    */       }
/* 370:    */       break;
/* 371:    */     }
/* 372:    */   }
/* 373:    */   
/* 374:    */   public void end(Graphics2D g)
/* 375:    */   {
/* 376:339 */     switch (this.kind)
/* 377:    */     {
/* 378:    */     case 5: 
/* 379:    */     case 9: 
/* 380:342 */       if (!cull())
/* 381:    */       {
/* 382:343 */         g.setPaint(this.paint);
/* 383:344 */         convert();
/* 384:345 */         g.fillPolygon(this.xBuf, this.yBuf, this.nPoints);
/* 385:346 */         if (this.kind == 9)
/* 386:    */         {
/* 387:347 */           g.setPaint(this.rulePaint);
/* 388:348 */           g.drawPolygon(this.xBuf, this.yBuf, this.nPoints);
/* 389:    */         }
/* 390:    */       }
/* 391:351 */       this.nPoints = 0;
/* 392:352 */       break;
/* 393:    */     case 6: 
/* 394:354 */       if (!cull())
/* 395:    */       {
/* 396:355 */         g.setPaint(this.paint);
/* 397:356 */         convert();
/* 398:357 */         g.drawPolyline(this.xBuf, this.yBuf, this.nPoints);
/* 399:    */       }
/* 400:359 */       this.nPoints = 0;
/* 401:    */     }
/* 402:    */   }
/* 403:    */   
/* 404:    */   public void setGouraudColor(int[] color)
/* 405:    */   {
/* 406:366 */     this.gouraudColor = color;
/* 407:    */   }
/* 408:    */   
/* 409:    */   public void drawGouraudTriangle(Component c, Graphics2D g, int[] x, int[] y, float[] s, float[] z)
/* 410:    */   {
/* 411:374 */     if (this.approximateGouraud)
/* 412:    */     {
/* 413:375 */       float sAvg = (s[0] + s[1] + s[2]) * 0.333333F;
/* 414:376 */       float zAvg = (z[0] + z[1] + z[2]) * 0.333333F;
/* 415:377 */       float f = zAvg >= 0.0F ? 0.0F : 0.0025F * zAvg / (0.0025F * zAvg - 1.0F);
/* 416:378 */       Color color = new Color((int)((1.0F - f) * sAvg * this.gouraudColor[0] + f * 192.0F), (int)((1.0F - f) * sAvg * this.gouraudColor[1] + f * 255.0F), (int)((1.0F - f) * sAvg * this.gouraudColor[2] + f * 255.0F));
/* 417:    */       
/* 418:    */ 
/* 419:    */ 
/* 420:382 */       g.setPaint(color);
/* 421:383 */       g.fillPolygon(x, y, 3);
/* 422:384 */       return;
/* 423:    */     }
/* 424:388 */     int i0 = 0;
/* 425:389 */     int i1 = 1;
/* 426:390 */     int i2 = 2;
/* 427:391 */     if (s[i1] < s[i0])
/* 428:    */     {
/* 429:392 */       int t = i0;i0 = i1;i1 = t;
/* 430:    */     }
/* 431:394 */     if (s[i2] < s[i0])
/* 432:    */     {
/* 433:395 */       int t = i0;i0 = i2;i2 = t;
/* 434:    */     }
/* 435:397 */     if (s[i2] < s[i1])
/* 436:    */     {
/* 437:398 */       int t = i1;i1 = i2;i2 = t;
/* 438:    */     }
/* 439:400 */     float x0 = x[i0];
/* 440:401 */     float y0 = y[i0];
/* 441:402 */     float s0 = s[i0];
/* 442:403 */     float s2 = s[i2];
/* 443:    */     
/* 444:    */ 
/* 445:    */ 
/* 446:    */ 
/* 447:    */ 
/* 448:    */ 
/* 449:    */ 
/* 450:    */ 
/* 451:412 */     float dx1 = x[i1] - x0;
/* 452:413 */     float dy1 = y[i1] - y0;
/* 453:414 */     float ds1 = s[i1] - s0;
/* 454:    */     
/* 455:416 */     float dx2 = x[i2] - x0;
/* 456:417 */     float dy2 = y[i2] - y0;
/* 457:418 */     float ds2 = s2 - s0;
/* 458:    */     
/* 459:    */ 
/* 460:    */ 
/* 461:422 */     float C = dx1 * ds2 - dx2 * ds1;
/* 462:423 */     float S = dy1 * ds2 - dy2 * ds1;
/* 463:    */     
/* 464:425 */     float cos = 0.0F;
/* 465:426 */     float sin = 1.0F;
/* 466:427 */     if (Math.abs(S) > -5.0F)
/* 467:    */     {
/* 468:428 */       float p = C / S;
/* 469:429 */       cos = (float)Math.sqrt(1.0F / (p * p + 1.0F));
/* 470:430 */       sin = (float)Math.sqrt(1.0F - cos * cos);
/* 471:    */     }
/* 472:433 */     if (((S < 0.0F) && (C < 0.0F)) || ((S > 0.0F) && (C > 0.0F))) {
/* 473:434 */       sin = -sin;
/* 474:    */     }
/* 475:438 */     float len = cos * dx2 + sin * dy2;
/* 476:    */     
/* 477:440 */     float z0 = z[0];
/* 478:441 */     float z2 = z[2];
/* 479:442 */     float f0 = z0 >= 0.0F ? 0.0F : 0.0025F * z0 / (0.0025F * z0 - 1.0F);
/* 480:443 */     float f2 = z2 >= 0.0F ? 0.0F : 0.0025F * z2 / (0.0025F * z2 - 1.0F);
/* 481:444 */     Color c0 = new Color((int)((1.0F - f0) * s0 * this.gouraudColor[0] + f0 * 192.0F), (int)((1.0F - f0) * s0 * this.gouraudColor[1] + f0 * 255.0F), (int)((1.0F - f0) * s0 * this.gouraudColor[2] + f0 * 255.0F));
/* 482:    */     
/* 483:    */ 
/* 484:    */ 
/* 485:448 */     Color c2 = new Color((int)((1.0F - f2) * s2 * this.gouraudColor[0] + f2 * 192.0F), (int)((1.0F - f2) * s2 * this.gouraudColor[1] + f2 * 255.0F), (int)((1.0F - f2) * s2 * this.gouraudColor[2] + f2 * 255.0F));
/* 486:    */     
/* 487:    */ 
/* 488:    */ 
/* 489:452 */     g.setPaint(new GradientPaint(x0, y0, c0, x0 + len * cos, y0 + len * sin, c2));
/* 490:453 */     g.fillPolygon(x, y, 3);
/* 491:    */   }
/* 492:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.Renderer3d
 * JD-Core Version:    0.7.0.1
 */