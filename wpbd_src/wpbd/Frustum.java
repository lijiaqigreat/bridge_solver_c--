/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import javax.media.opengl.GL2;
/*   4:    */ 
/*   5:    */ public class Frustum
/*   6:    */ {
/*   7:    */   private float left;
/*   8:    */   private float right;
/*   9:    */   private float bottom;
/*  10:    */   private float top;
/*  11:    */   private float near;
/*  12:    */   private float far;
/*  13:    */   
/*  14:    */   private static Homogeneous.Point[] arrayOf3dPoints(int size)
/*  15:    */   {
/*  16: 46 */     Homogeneous.Point[] a = new Homogeneous.Point[size];
/*  17: 47 */     for (int i = 0; i < size; i++) {
/*  18: 48 */       a[i] = new Homogeneous.Point();
/*  19:    */     }
/*  20: 50 */     return a;
/*  21:    */   }
/*  22:    */   
/*  23:    */   private static Affine.Point[] arrayOf2dPoints(int size)
/*  24:    */   {
/*  25: 53 */     Affine.Point[] a = new Affine.Point[size];
/*  26: 54 */     for (int i = 0; i < size; i++) {
/*  27: 55 */       a[i] = new Affine.Point();
/*  28:    */     }
/*  29: 57 */     return a;
/*  30:    */   }
/*  31:    */   
/*  32:    */   private class Pyramid
/*  33:    */   {
/*  34: 62 */     private final Homogeneous.Point[] vCanon = Frustum.arrayOf3dPoints(10);
/*  35: 63 */     private final Affine.Point[] vActual = Frustum.arrayOf2dPoints(10);
/*  36: 64 */     private final ConvexHullFactory hullFactory = new ConvexHullFactory();
/*  37:    */     
/*  38:    */     private Pyramid() {}
/*  39:    */     
/*  40:    */     public void set(float left, float right, float bottom, float top, float near, float far)
/*  41:    */     {
/*  42: 67 */       float zn = -near;
/*  43: 68 */       this.vCanon[0].set(right, bottom, zn);
/*  44: 69 */       this.vCanon[1].set(right, top, zn);
/*  45: 70 */       this.vCanon[2].set(left, top, zn);
/*  46: 71 */       this.vCanon[3].set(left, bottom, zn);
/*  47: 72 */       float zf = -far;
/*  48: 73 */       float r = zf / zn;
/*  49: 74 */       float farLeft = r * left;
/*  50: 75 */       float farRight = r * right;
/*  51: 76 */       float farBottom = r * bottom;
/*  52: 77 */       float farTop = r * top;
/*  53: 78 */       this.vCanon[4].set(farRight, farBottom, zf);
/*  54: 79 */       this.vCanon[5].set(farRight, farTop, zf);
/*  55: 80 */       this.vCanon[6].set(farLeft, farTop, zf);
/*  56: 81 */       this.vCanon[7].set(farLeft, farBottom, zf);
/*  57:    */       
/*  58: 83 */       this.vCanon[8].set(0.5F * (right + left), 0.5F * (top + bottom), zn);
/*  59: 84 */       this.vCanon[9].set(0.5F * (farRight + farLeft), 0.5F * (farTop + farBottom), zf);
/*  60:    */     }
/*  61:    */     
/*  62:    */     public Affine.Point[] getHull(Affine.Point[] hull, Homogeneous.Matrix xForm)
/*  63:    */     {
/*  64: 89 */       this.hullFactory.clear();
/*  65: 90 */       for (int i = 0; i < this.vCanon.length; i++)
/*  66:    */       {
/*  67: 92 */         this.vActual[i].x = (xForm.a[0] * this.vCanon[i].a[0] + xForm.a[4] * this.vCanon[i].a[1] + xForm.a[8] * this.vCanon[i].a[2] + xForm.a[12] * this.vCanon[i].a[3]);
/*  68:    */         
/*  69:    */ 
/*  70:    */ 
/*  71: 96 */         this.vActual[i].y = (xForm.a[1] * this.vCanon[i].a[0] + xForm.a[5] * this.vCanon[i].a[1] + xForm.a[9] * this.vCanon[i].a[2] + xForm.a[13] * this.vCanon[i].a[3]);
/*  72:    */         
/*  73:    */ 
/*  74:    */ 
/*  75:    */ 
/*  76:101 */         this.hullFactory.add(this.vActual[i]);
/*  77:    */       }
/*  78:103 */       return this.hullFactory.getHull(hull);
/*  79:    */     }
/*  80:    */     
/*  81:    */     public Affine.Point getNearCenter()
/*  82:    */     {
/*  83:107 */       return this.vActual[8];
/*  84:    */     }
/*  85:    */     
/*  86:    */     public Affine.Point getFarCenter()
/*  87:    */     {
/*  88:111 */       return this.vActual[9];
/*  89:    */     }
/*  90:    */     
/*  91:    */     public void getAxis(Affine.Vector axis)
/*  92:    */     {
/*  93:115 */       axis.diffInto(this.vActual[9], this.vActual[8]);
/*  94:116 */       double r = 1.0D / axis.length();
/*  95:117 */       if (Double.isInfinite(r))
/*  96:    */       {
/*  97:119 */         axis.diffInto(this.vActual[5], this.vActual[4]);
/*  98:120 */         r = 1.0D / axis.length();
/*  99:    */       }
/* 100:122 */       axis.scaleInPlace(r);
/* 101:    */     }
/* 102:    */     
/* 103:    */     public void draw(GL2 gl)
/* 104:    */     {
/* 105:126 */       gl.glBegin(2);
/* 106:127 */       for (int i = 0; i < 4; i++) {
/* 107:128 */         gl.glVertex3fv(this.vCanon[i].a, 0);
/* 108:    */       }
/* 109:130 */       gl.glEnd();
/* 110:    */       
/* 111:132 */       gl.glBegin(2);
/* 112:133 */       for (int i = 4; i < 8; i++) {
/* 113:134 */         gl.glVertex3fv(this.vCanon[i].a, 0);
/* 114:    */       }
/* 115:136 */       gl.glEnd();
/* 116:    */       
/* 117:138 */       gl.glBegin(1);
/* 118:139 */       for (int i = 0; i < 4; i++)
/* 119:    */       {
/* 120:140 */         gl.glVertex3fv(this.vCanon[i].a, 0);
/* 121:141 */         gl.glVertex3fv(this.vCanon[(i + 4)].a, 0);
/* 122:    */       }
/* 123:143 */       gl.glEnd();
/* 124:    */     }
/* 125:    */   }
/* 126:    */   
/* 127:150 */   private final Pyramid frustum = new Pyramid(null);
/* 128:151 */   private final Pyramid focusArea = new Pyramid(null);
/* 129:153 */   private final Homogeneous.Matrix tmpMatrix = new Homogeneous.Matrix();
/* 130:154 */   private final Affine.Vector tmpVector = new Affine.Vector();
/* 131:155 */   private final Homogeneous.Matrix invModelView = new Homogeneous.Matrix();
/* 132:156 */   private final Affine.Point[] trapezoid = arrayOf2dPoints(4);
/* 133:157 */   private final Affine.Vector unitL = new Affine.Vector();
/* 134:158 */   private final Affine.Vector unitR = new Affine.Vector();
/* 135:160 */   private final Affine.Point[] frustumHull = new Affine.Point[7];
/* 136:161 */   private final Affine.Point[] focusHull = new Affine.Point[7];
/* 137:162 */   private final Affine.Point[] focusProjected = arrayOf2dPoints(6);
/* 138:163 */   private final Affine.Vector axisDirection = new Affine.Vector();
/* 139:164 */   private final Affine.Point pointQ = new Affine.Point();
/* 140:    */   private double tBase;
/* 141:    */   private double tTop;
/* 142:    */   private double tFocus;
/* 143:    */   
/* 144:    */   void set(float fovy, float aspect, float near, float far, float focusRatio)
/* 145:    */   {
/* 146:173 */     this.near = near;
/* 147:174 */     this.far = far;
/* 148:175 */     this.top = (near * (float)Math.tan(fovy * 3.141592653589793D / 360.0D));
/* 149:176 */     this.bottom = (-this.top);
/* 150:177 */     this.left = (aspect * this.bottom);
/* 151:178 */     this.right = (aspect * this.top);
/* 152:    */     
/* 153:    */ 
/* 154:181 */     far = (float)(far * 0.35D);
/* 155:182 */     this.frustum.set(this.left, this.right, this.bottom, this.top, near, far);
/* 156:183 */     this.focusArea.set(this.left, this.right, this.bottom, this.top, near, focusRatio * far + (1.0F - focusRatio) * near);
/* 157:    */   }
/* 158:    */   
/* 159:    */   void apply(GL2 gl)
/* 160:    */   {
/* 161:192 */     gl.glFrustum(this.left, this.right, this.bottom, this.top, this.near, this.far);
/* 162:    */   }
/* 163:    */   
/* 164:    */   public void draw(GL2 gl, Homogeneous.Matrix lightView, Homogeneous.Matrix lightProjection)
/* 165:    */   {
/* 166:205 */     gl.glMatrixMode(5889);
/* 167:206 */     gl.glPushMatrix();
/* 168:207 */     gl.glLoadMatrixf(lightProjection.a, 0);
/* 169:208 */     gl.glMatrixMode(5888);
/* 170:209 */     gl.glPushMatrix();
/* 171:210 */     gl.glLoadIdentity();
/* 172:    */     
/* 173:    */ 
/* 174:213 */     gl.glDepthFunc(519);
/* 175:214 */     gl.glColor3f(1.0F, 0.0F, 0.0F);
/* 176:215 */     gl.glBegin(2);
/* 177:216 */     for (int i = 0; this.frustumHull[i] != null; i++) {
/* 178:217 */       gl.glVertex3d(this.frustumHull[i].x, this.frustumHull[i].y, -100.0D);
/* 179:    */     }
/* 180:219 */     gl.glEnd();
/* 181:    */     
/* 182:    */ 
/* 183:222 */     gl.glColor3f(1.0F, 0.0F, 1.0F);
/* 184:223 */     gl.glBegin(2);
/* 185:224 */     for (int i = 0; this.focusHull[i] != null; i++) {
/* 186:225 */       gl.glVertex3d(this.focusHull[i].x, this.focusHull[i].y, -100.0D);
/* 187:    */     }
/* 188:227 */     gl.glEnd();
/* 189:    */     
/* 190:    */ 
/* 191:230 */     gl.glColor3f(0.0F, 1.0F, 0.0F);
/* 192:231 */     gl.glBegin(2);
/* 193:232 */     for (int i = 0; i < 4; i++) {
/* 194:233 */       gl.glVertex3d(this.trapezoid[i].x, this.trapezoid[i].y, -100.0D);
/* 195:    */     }
/* 196:235 */     gl.glEnd();
/* 197:    */     
/* 198:    */ 
/* 199:238 */     this.tmpMatrix.multiplyInto(lightView, this.invModelView);
/* 200:239 */     gl.glLoadMatrixf(this.tmpMatrix.a, 0);
/* 201:240 */     gl.glColor3f(0.2F, 0.2F, 1.0F);
/* 202:241 */     this.frustum.draw(gl);
/* 203:242 */     gl.glColor3f(0.0F, 1.0F, 1.0F);
/* 204:243 */     this.focusArea.draw(gl);
/* 205:244 */     gl.glDepthFunc(515);
/* 206:    */     
/* 207:    */ 
/* 208:247 */     gl.glPopMatrix();
/* 209:248 */     gl.glMatrixMode(5889);
/* 210:249 */     gl.glPopMatrix();
/* 211:250 */     gl.glMatrixMode(5888);
/* 212:    */   }
/* 213:    */   
/* 214:    */   public void getTrapezoidalProjection(Homogeneous.Matrix m, Homogeneous.Matrix modelView, Homogeneous.Matrix lightView, float near, float far)
/* 215:    */   {
/* 216:258 */     this.invModelView.invertInto(modelView, -5.0F);
/* 217:259 */     this.tmpMatrix.multiplyInto(lightView, this.invModelView);
/* 218:    */     
/* 219:    */ 
/* 220:262 */     this.frustum.getHull(this.frustumHull, this.tmpMatrix);
/* 221:263 */     this.frustum.getAxis(this.axisDirection);
/* 222:    */     
/* 223:    */ 
/* 224:266 */     this.focusArea.getHull(this.focusHull, this.tmpMatrix);
/* 225:    */     
/* 226:    */ 
/* 227:    */ 
/* 228:270 */     this.tmpVector.diffInto(this.frustumHull[0], this.frustum.getNearCenter());
/* 229:271 */     this.tBase = (this.tTop = this.axisDirection.dot(this.tmpVector));
/* 230:272 */     for (int i = 1; this.frustumHull[i] != null; i++)
/* 231:    */     {
/* 232:273 */       this.tmpVector.diffInto(this.frustumHull[i], this.frustum.getNearCenter());
/* 233:274 */       double t = this.axisDirection.dot(this.tmpVector);
/* 234:275 */       if (t < this.tTop) {
/* 235:276 */         this.tTop = t;
/* 236:278 */       } else if (t > this.tBase) {
/* 237:279 */         this.tBase = t;
/* 238:    */       }
/* 239:    */     }
/* 240:284 */     this.tmpVector.diffInto(this.focusArea.getFarCenter(), this.focusArea.getNearCenter());
/* 241:285 */     this.tFocus = this.axisDirection.dot(this.tmpVector);
/* 242:    */     
/* 243:    */ 
/* 244:288 */     double lambda = this.tBase - this.tTop;
/* 245:289 */     double deltaPrime = this.tFocus - this.tTop;
/* 246:    */     
/* 247:    */ 
/* 248:292 */     Affine.Point p0 = this.trapezoid[0];
/* 249:293 */     Affine.Point p1 = this.trapezoid[1];
/* 250:294 */     Affine.Point p2 = this.trapezoid[2];
/* 251:295 */     Affine.Point p3 = this.trapezoid[3];
/* 252:296 */     Affine.Vector a = this.axisDirection;
/* 253:    */     
/* 254:298 */     double xi = -0.6D;
/* 255:    */     
/* 256:300 */     double xiInc = 0.001953125D;
/* 257:301 */     double lastArea = 0.0D;
/* 258:    */     double m00;
/* 259:    */     double m01;
/* 260:    */     double m02;
/* 261:    */     double m10;
/* 262:    */     double m11;
/* 263:    */     double m12;
/* 264:    */     double m20;
/* 265:    */     double m21;
/* 266:    */     double m22;
/* 267:    */     do
/* 268:    */     {
/* 269:306 */       double eta = lambda * deltaPrime * (1.0D + xi) / (lambda - 2.0D * deltaPrime - lambda * xi);
/* 270:310 */       if (eta > lambda * 100.0D)
/* 271:    */       {
/* 272:314 */         double orthoL = 0.0D;
/* 273:315 */         double orthoR = 0.0D;
/* 274:316 */         this.pointQ.offsetInto(this.frustum.getNearCenter(), a, this.tTop);
/* 275:317 */         for (int i = 0; this.frustumHull[i] != null; i++)
/* 276:    */         {
/* 277:318 */           this.tmpVector.diffInto(this.frustumHull[i], this.pointQ);
/* 278:319 */           double ortho = a.cross(this.tmpVector);
/* 279:320 */           if (ortho > orthoL) {
/* 280:321 */             orthoL = ortho;
/* 281:323 */           } else if (ortho < orthoR) {
/* 282:324 */             orthoR = ortho;
/* 283:    */           }
/* 284:    */         }
/* 285:327 */         p0.orthoOffsetInto(this.pointQ, a, orthoL);
/* 286:328 */         p1.orthoOffsetInto(this.pointQ, a, orthoR);
/* 287:329 */         this.pointQ.offsetInto(this.frustum.getNearCenter(), a, this.tBase);
/* 288:330 */         p2.orthoOffsetInto(this.pointQ, a, orthoR);
/* 289:331 */         p3.orthoOffsetInto(this.pointQ, a, orthoL);
/* 290:    */       }
/* 291:    */       else
/* 292:    */       {
/* 293:336 */         this.pointQ.offsetInto(this.frustum.getNearCenter(), this.axisDirection, this.tTop - eta);
/* 294:    */         
/* 295:    */ 
/* 296:    */ 
/* 297:340 */         double crossL = 0.0D;
/* 298:341 */         double crossR = 0.0D;
/* 299:342 */         for (int i = 0; this.frustumHull[i] != null; i++)
/* 300:    */         {
/* 301:343 */           this.tmpVector.diffInto(this.frustumHull[i], this.pointQ);
/* 302:344 */           this.tmpVector.scaleInPlace(1.0D / this.tmpVector.length());
/* 303:345 */           double cross = this.axisDirection.cross(this.tmpVector);
/* 304:346 */           if (cross > crossL)
/* 305:    */           {
/* 306:347 */             crossL = cross;
/* 307:348 */             this.unitL.setLocation(this.tmpVector);
/* 308:    */           }
/* 309:350 */           else if (cross < crossR)
/* 310:    */           {
/* 311:351 */             crossR = cross;
/* 312:352 */             this.unitR.setLocation(this.tmpVector);
/* 313:    */           }
/* 314:    */         }
/* 315:356 */         double dotLi = 1.0D / this.axisDirection.dot(this.unitL);
/* 316:357 */         double dotRi = 1.0D / this.axisDirection.dot(this.unitR);
/* 317:358 */         double tt = eta;
/* 318:359 */         double tb = eta + lambda;
/* 319:360 */         p0.offsetInto(this.pointQ, this.unitL, tt * dotLi);
/* 320:361 */         p1.offsetInto(this.pointQ, this.unitR, tt * dotRi);
/* 321:362 */         p2.offsetInto(this.pointQ, this.unitR, tb * dotRi);
/* 322:363 */         p3.offsetInto(this.pointQ, this.unitL, tb * dotLi);
/* 323:    */       }
/* 324:367 */       m00 = a.y;
/* 325:368 */       m01 = -a.x;
/* 326:369 */       m02 = a.x * p0.y - a.y * p0.x;
/* 327:370 */       m10 = a.x;
/* 328:371 */       m11 = a.y;
/* 329:372 */       m12 = -(a.x * p0.x + a.y * p0.y);
/* 330:373 */       double xc3 = m00 * p3.x + m01 * p3.y + m02;
/* 331:374 */       double yc3 = m10 * p3.x + m11 * p3.y + m12;
/* 332:375 */       double s = -xc3 / yc3;
/* 333:376 */       m00 += s * m10;
/* 334:377 */       m01 += s * m11;
/* 335:378 */       m02 += s * m12;
/* 336:379 */       double xd1 = m00 * p1.x + m01 * p1.y + m02;
/* 337:380 */       double xd2 = m00 * p2.x + m01 * p2.y + m02;
/* 338:381 */       double d = yc3 / (xd2 - xd1);
/* 339:382 */       if ((0.0D <= d) && (d < 10000.0D))
/* 340:    */       {
/* 341:383 */         d *= xd1;
/* 342:384 */         m12 += d;
/* 343:385 */         double sx = 2.0D / xd2;
/* 344:386 */         double sy = 1.0D / (yc3 + d);
/* 345:387 */         double u = 2.0D * (sy * d) / (1.0D - sy * d);
/* 346:388 */         double m20 = m10 * sy;
/* 347:389 */         double m21 = m11 * sy;
/* 348:390 */         double m22 = m12 * sy;
/* 349:391 */         m10 = (u + 1.0D) * m20;
/* 350:392 */         m11 = (u + 1.0D) * m21;
/* 351:393 */         m12 = (u + 1.0D) * m22 - u;
/* 352:394 */         m00 = sx * m00 - m20;
/* 353:395 */         m01 = sx * m01 - m21;
/* 354:396 */         m02 = sx * m02 - m22;
/* 355:    */       }
/* 356:    */       else
/* 357:    */       {
/* 358:398 */         double sx = 2.0D / xd2;
/* 359:399 */         double sy = 2.0D / yc3;
/* 360:400 */         m00 *= sx;
/* 361:401 */         m01 *= sx;
/* 362:402 */         m02 = m02 * sx - 1.0D;
/* 363:403 */         m10 *= sy;
/* 364:404 */         m11 *= sy;
/* 365:405 */         m12 = m12 * sy - 1.0D;
/* 366:406 */         m20 = 0.0D;
/* 367:407 */         m21 = 0.0D;
/* 368:408 */         m22 = 1.0D;
/* 369:    */       }
/* 370:411 */       int n = 0;
/* 371:412 */       while (this.focusHull[n] != null)
/* 372:    */       {
/* 373:413 */         double w = m20 * this.focusHull[n].x + m21 * this.focusHull[n].y + m22;
/* 374:414 */         this.focusProjected[n].x = ((m00 * this.focusHull[n].x + m01 * this.focusHull[n].y + m02) / w);
/* 375:415 */         this.focusProjected[n].y = ((m10 * this.focusHull[n].x + m11 * this.focusHull[n].y + m12) / w);
/* 376:416 */         n++;
/* 377:    */       }
/* 378:420 */       double area = Affine.getPolygonArea(this.focusProjected, n);
/* 379:421 */       if (area < lastArea) {
/* 380:    */         break;
/* 381:    */       }
/* 382:425 */       lastArea = area;
/* 383:426 */       xi += xiInc;
/* 384:427 */     } while (xi < 1.0D);
/* 385:430 */     m.a[0] = ((float)m00);
/* 386:431 */     m.a[4] = ((float)m01);
/* 387:432 */     m.a[8] = 0.0F;
/* 388:433 */     m.a[12] = ((float)m02);
/* 389:    */     
/* 390:435 */     m.a[1] = ((float)m10);
/* 391:436 */     m.a[5] = ((float)m11);
/* 392:437 */     m.a[9] = 0.0F;
/* 393:438 */     m.a[13] = ((float)m12);
/* 394:    */     
/* 395:440 */     m.a[2] = 0.0F;
/* 396:441 */     m.a[6] = 0.0F;
/* 397:442 */     m.a[10] = (2.0F / (near - far));
/* 398:443 */     m.a[14] = ((near + far) / (near - far));
/* 399:    */     
/* 400:445 */     m.a[3] = ((float)m20);
/* 401:446 */     m.a[7] = ((float)m21);
/* 402:447 */     m.a[11] = 0.0F;
/* 403:448 */     m.a[15] = ((float)m22);
/* 404:    */   }
/* 405:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.Frustum
 * JD-Core Version:    0.7.0.1
 */