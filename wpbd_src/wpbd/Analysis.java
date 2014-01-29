/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.PrintStream;
/*   6:    */ import java.util.ArrayList;
/*   7:    */ import java.util.Iterator;
/*   8:    */ 
/*   9:    */ public class Analysis
/*  10:    */ {
/*  11:    */   private static final double deadLoadFactor = 1.25D;
/*  12:    */   private static final double liveLoadFactor = 2.3275D;
/*  13:    */   private static final double failedMemberDegradation = 0.02D;
/*  14:    */   private static final double NOT_FAILED = -1.0D;
/*  15:    */   private static final double FAILED = 1000000.0D;
/*  16:    */   public static final int NO_STATUS = 0;
/*  17:    */   public static final int FAILS_SLENDERNESS = 1;
/*  18:    */   public static final int UNSTABLE = 2;
/*  19:    */   public static final int FAILS_LOAD_TEST = 3;
/*  20:    */   public static final int PASSES = 4;
/*  21:    */   private BridgeModel bridge;
/*  22:    */   private double[][] memberForce;
/*  23:    */   private double[][] jointDisplacement;
/*  24:    */   private boolean[][] memberFails;
/*  25:    */   private double[] memberCompressiveStrength;
/*  26:    */   private double[] memberTensileStrength;
/*  27:    */   private double[] maxMemberCompressiveForces;
/*  28:    */   private double[] maxMemberTensileForces;
/*  29:    */   private int status;
/*  30:    */   
/*  31:    */   public Analysis()
/*  32:    */   {
/*  33: 90 */     this.status = 0;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public int getStatus()
/*  37:    */   {
/*  38:104 */     return this.status;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public double getMemberForce(int ilc, int im)
/*  42:    */   {
/*  43:116 */     return this.memberForce[ilc][im];
/*  44:    */   }
/*  45:    */   
/*  46:    */   public double getXJointDisplacement(int ilc, int ij)
/*  47:    */   {
/*  48:128 */     return this.jointDisplacement[ilc][(2 * ij)];
/*  49:    */   }
/*  50:    */   
/*  51:    */   public double getYJointDisplacement(int ilc, int ij)
/*  52:    */   {
/*  53:140 */     return this.jointDisplacement[ilc][(2 * ij + 1)];
/*  54:    */   }
/*  55:    */   
/*  56:    */   public double getMemberCompressiveForce(int i)
/*  57:    */   {
/*  58:151 */     return this.maxMemberCompressiveForces[i];
/*  59:    */   }
/*  60:    */   
/*  61:    */   public double getMemberTensileForce(int i)
/*  62:    */   {
/*  63:162 */     return this.maxMemberTensileForces[i];
/*  64:    */   }
/*  65:    */   
/*  66:    */   public double getMemberCompressiveStrength(int i)
/*  67:    */   {
/*  68:174 */     return this.memberCompressiveStrength[i];
/*  69:    */   }
/*  70:    */   
/*  71:    */   public double getMemberTensileStrength(int i)
/*  72:    */   {
/*  73:188 */     return this.memberTensileStrength[i];
/*  74:    */   }
/*  75:    */   
/*  76:    */   public void initialize(BridgeModel bridge)
/*  77:    */   {
/*  78:198 */     initialize(bridge, null);
/*  79:    */   }
/*  80:    */   
/*  81:    */   public void initialize(BridgeModel bridge, double[] failureStatus)
/*  82:    */   {
/*  83:210 */     this.bridge = bridge;
/*  84:211 */     DesignConditions conditions = bridge.getDesignConditions();
/*  85:212 */     this.status = 0;
/*  86:213 */     int nJoints = bridge.getJoints().size();
/*  87:214 */     int nEquations = 2 * nJoints;
/*  88:215 */     int nMembers = bridge.getMembers().size();
/*  89:216 */     Member[] members = (Member[])bridge.getMembers().toArray(new Member[nMembers]);
/*  90:217 */     double[] length = new double[members.length];
/*  91:218 */     double[] cosX = new double[members.length];
/*  92:219 */     double[] cosY = new double[members.length];
/*  93:220 */     for (int i = 0; i < members.length; i++)
/*  94:    */     {
/*  95:221 */       Affine.Point a = members[i].getJointA().getPointWorld();
/*  96:222 */       Affine.Point b = members[i].getJointB().getPointWorld();
/*  97:223 */       double dx = b.x - a.x;
/*  98:224 */       double dy = b.y - a.y;
/*  99:225 */       length[i] = hypot(dx, dy);
/* 100:226 */       cosX[i] = (dx / length[i]);
/* 101:227 */       cosY[i] = (dy / length[i]);
/* 102:    */     }
/* 103:229 */     int nLoadInstances = conditions.getNLoadedJoints();
/* 104:230 */     double[][] pointLoads = new double[nLoadInstances][nEquations];
/* 105:231 */     for (int im = 0; im < nMembers; im++)
/* 106:    */     {
/* 107:232 */       double deadLoad = 1.25D * members[im].getShape().getArea() * length[im] * members[im].getMaterial().getDensity() * 9.8066D / 2.0D / 1000.0D;
/* 108:    */       
/* 109:    */ 
/* 110:    */ 
/* 111:    */ 
/* 112:237 */       int dof1 = 2 * members[im].getJointA().getIndex() + 1;
/* 113:238 */       int dof2 = 2 * members[im].getJointB().getIndex() + 1;
/* 114:239 */       for (int ilc = 0; ilc < nLoadInstances; ilc++)
/* 115:    */       {
/* 116:240 */         pointLoads[ilc][dof1] -= deadLoad;
/* 117:241 */         pointLoads[ilc][dof2] -= deadLoad;
/* 118:    */       }
/* 119:    */     }
/* 120:244 */     double pointDeadLoad = conditions.getDeckType() == 0 ? 183.42825000000002D : 136.357D;
/* 121:247 */     for (int ij = 0; ij < conditions.getNLoadedJoints(); ij++)
/* 122:    */     {
/* 123:248 */       int dof = 2 * ij + 1;
/* 124:249 */       for (int ilc = 0; ilc < nLoadInstances; ilc++)
/* 125:    */       {
/* 126:250 */         double load = pointDeadLoad;
/* 127:251 */         if ((ij == 0) || (ij == conditions.getNLoadedJoints() - 1)) {
/* 128:252 */           load /= 2.0D;
/* 129:    */         }
/* 130:254 */         pointLoads[ilc][dof] -= load;
/* 131:    */       }
/* 132:    */     }
/* 133:258 */     double frontAxleLoad = 44.0D;
/* 134:259 */     double rearAxleLoad = 181.0D;
/* 135:260 */     if (conditions.getLoadType() != 0)
/* 136:    */     {
/* 137:262 */       frontAxleLoad = 120.0D;
/* 138:263 */       rearAxleLoad = 120.0D;
/* 139:    */     }
/* 140:265 */     for (int ilc = 1; ilc < nLoadInstances; ilc++)
/* 141:    */     {
/* 142:266 */       int iFront = 2 * ilc + 1;
/* 143:267 */       int iRear = iFront - 2;
/* 144:268 */       pointLoads[ilc][iFront] -= 2.3275D * frontAxleLoad;
/* 145:269 */       pointLoads[ilc][iRear] -= 2.3275D * rearAxleLoad;
/* 146:    */     }
/* 147:271 */     boolean[] xRestraint = new boolean[nJoints];
/* 148:272 */     boolean[] yRestraint = new boolean[nJoints]; int 
/* 149:273 */       tmp560_559 = (yRestraint[(conditions.getNLoadedJoints() - 1)] = 1);yRestraint[0] = tmp560_559;xRestraint[0] = tmp560_559;
/* 150:274 */     if (conditions.isPier())
/* 151:    */     {
/* 152:275 */       int i = conditions.getPierJointIndex(); int 
/* 153:276 */         tmp585_584 = 1;yRestraint[i] = tmp585_584;xRestraint[i] = tmp585_584;
/* 154:277 */       if (conditions.isHiPier()) {
/* 155:278 */         xRestraint[0] = false;
/* 156:    */       }
/* 157:    */     }
/* 158:281 */     if (conditions.isArch())
/* 159:    */     {
/* 160:282 */       int i = conditions.getArchJointIndex(); int 
/* 161:283 */         tmp630_629 = (yRestraint[(conditions.getNLoadedJoints() - 1)] = 0);yRestraint[0] = tmp630_629;xRestraint[0] = tmp630_629; int 
/* 162:284 */         tmp642_641 = 1;yRestraint[i] = tmp642_641;xRestraint[i] = tmp642_641; int 
/* 163:285 */         tmp658_657 = 1;yRestraint[(i + 1)] = tmp658_657;xRestraint[(i + 1)] = tmp658_657;
/* 164:    */     }
/* 165:287 */     if (conditions.isLeftAnchorage())
/* 166:    */     {
/* 167:288 */       int i = conditions.getLeftAnchorageJointIndex(); int 
/* 168:289 */         tmp683_682 = 1;yRestraint[i] = tmp683_682;xRestraint[i] = tmp683_682;
/* 169:    */     }
/* 170:291 */     if (conditions.isRightAnchorage())
/* 171:    */     {
/* 172:292 */       int i = conditions.getRightAnchorageJointIndex(); int 
/* 173:293 */         tmp708_707 = 1;yRestraint[i] = tmp708_707;xRestraint[i] = tmp708_707;
/* 174:    */     }
/* 175:295 */     double[][] stiffness = new double[nEquations][nEquations];
/* 176:296 */     for (int im = 0; im < nMembers; im++)
/* 177:    */     {
/* 178:297 */       double e = members[im].getMaterial().getE();
/* 179:298 */       if ((failureStatus != null) && (failureStatus[im] != -1.0D)) {
/* 180:299 */         e *= 0.02D;
/* 181:    */       }
/* 182:301 */       double aEOverL = members[im].getShape().getArea() * e / length[im];
/* 183:302 */       double xx = aEOverL * sqr(cosX[im]);
/* 184:303 */       double yy = aEOverL * sqr(cosY[im]);
/* 185:304 */       double xy = aEOverL * cosX[im] * cosY[im];
/* 186:305 */       int j1 = members[im].getJointA().getIndex();
/* 187:306 */       int j2 = members[im].getJointB().getIndex();
/* 188:307 */       int j1x = 2 * j1;
/* 189:308 */       int j1y = 2 * j1 + 1;
/* 190:309 */       int j2x = 2 * j2;
/* 191:310 */       int j2y = 2 * j2 + 1;
/* 192:311 */       stiffness[j1x][j1x] += xx;
/* 193:312 */       stiffness[j1x][j1y] += xy;
/* 194:313 */       stiffness[j1x][j2x] -= xx;
/* 195:314 */       stiffness[j1x][j2y] -= xy;
/* 196:315 */       stiffness[j1y][j1x] += xy;
/* 197:316 */       stiffness[j1y][j1y] += yy;
/* 198:317 */       stiffness[j1y][j2x] -= xy;
/* 199:318 */       stiffness[j1y][j2y] -= yy;
/* 200:319 */       stiffness[j2x][j1x] -= xx;
/* 201:320 */       stiffness[j2x][j1y] -= xy;
/* 202:321 */       stiffness[j2x][j2x] += xx;
/* 203:322 */       stiffness[j2x][j2y] += xy;
/* 204:323 */       stiffness[j2y][j1x] -= xy;
/* 205:324 */       stiffness[j2y][j1y] -= yy;
/* 206:325 */       stiffness[j2y][j2x] += xy;
/* 207:326 */       stiffness[j2y][j2y] += yy;
/* 208:    */     }
/* 209:328 */     for (int ilc = 0; ilc < nLoadInstances; ilc++) {
/* 210:329 */       for (int ij = 0; ij < nJoints; ij++)
/* 211:    */       {
/* 212:330 */         if (xRestraint[ij] != 0)
/* 213:    */         {
/* 214:331 */           int ix = 2 * ij;
/* 215:332 */           for (int ie = 0; ie < nEquations; ie++)
/* 216:    */           {
/* 217:333 */             double tmp1158_1157 = 0.0D;stiffness[ie][ix] = tmp1158_1157;stiffness[ix][ie] = tmp1158_1157;
/* 218:    */           }
/* 219:335 */           stiffness[ix][ix] = 1.0D;
/* 220:336 */           pointLoads[ilc][ix] = 0.0D;
/* 221:    */         }
/* 222:338 */         if (yRestraint[ij] != 0)
/* 223:    */         {
/* 224:339 */           int iy = 2 * ij + 1;
/* 225:340 */           for (int ie = 0; ie < nEquations; ie++)
/* 226:    */           {
/* 227:341 */             double tmp1226_1225 = 0.0D;stiffness[ie][iy] = tmp1226_1225;stiffness[iy][ie] = tmp1226_1225;
/* 228:    */           }
/* 229:343 */           stiffness[iy][iy] = 1.0D;
/* 230:344 */           pointLoads[ilc][iy] = 0.0D;
/* 231:    */         }
/* 232:    */       }
/* 233:    */     }
/* 234:348 */     for (int ie = 0; ie < nEquations; ie++)
/* 235:    */     {
/* 236:349 */       double pivot = stiffness[ie][ie];
/* 237:350 */       if (Math.abs(pivot) < 0.99D)
/* 238:    */       {
/* 239:351 */         this.status = 2;
/* 240:352 */         return;
/* 241:    */       }
/* 242:354 */       double pivr = 1.0D / pivot;
/* 243:355 */       for (int k = 0; k < nEquations; k++) {
/* 244:356 */         stiffness[ie][k] /= pivot;
/* 245:    */       }
/* 246:358 */       for (int k = 0; k < nEquations; k++) {
/* 247:359 */         if (k != ie)
/* 248:    */         {
/* 249:360 */           pivot = stiffness[k][ie];
/* 250:361 */           for (int j = 0; j < nEquations; j++) {
/* 251:362 */             stiffness[k][j] -= stiffness[ie][j] * pivot;
/* 252:    */           }
/* 253:364 */           stiffness[k][ie] = (-pivot * pivr);
/* 254:    */         }
/* 255:    */       }
/* 256:367 */       stiffness[ie][ie] = pivr;
/* 257:    */     }
/* 258:369 */     this.memberForce = new double[nLoadInstances][nMembers];
/* 259:370 */     this.memberFails = new boolean[nLoadInstances][nMembers];
/* 260:371 */     this.jointDisplacement = new double[nLoadInstances][nEquations];
/* 261:372 */     for (int ilc = 0; ilc < nLoadInstances; ilc++)
/* 262:    */     {
/* 263:373 */       for (int ie = 0; ie < nEquations; ie++)
/* 264:    */       {
/* 265:374 */         double tmp = 0.0D;
/* 266:375 */         for (int je = 0; je < nEquations; je++) {
/* 267:376 */           tmp += stiffness[ie][je] * pointLoads[ilc][je];
/* 268:    */         }
/* 269:378 */         this.jointDisplacement[ilc][ie] = tmp;
/* 270:    */       }
/* 271:381 */       for (int im = 0; im < nMembers; im++)
/* 272:    */       {
/* 273:382 */         double e = members[im].getMaterial().getE();
/* 274:383 */         if ((failureStatus != null) && (failureStatus[im] != -1.0D)) {
/* 275:384 */           e *= 0.02D;
/* 276:    */         }
/* 277:386 */         double aeOverL = members[im].getShape().getArea() * e / length[im];
/* 278:387 */         int ija = members[im].getJointA().getIndex();
/* 279:388 */         int ijb = members[im].getJointB().getIndex();
/* 280:389 */         this.memberForce[ilc][im] = (aeOverL * (cosX[im] * (getXJointDisplacement(ilc, ijb) - getXJointDisplacement(ilc, ija)) + cosY[im] * (getYJointDisplacement(ilc, ijb) - getYJointDisplacement(ilc, ija))));
/* 281:    */       }
/* 282:    */     }
/* 283:395 */     this.memberCompressiveStrength = new double[nMembers];
/* 284:396 */     this.memberTensileStrength = new double[nMembers];
/* 285:397 */     this.maxMemberCompressiveForces = new double[nMembers];
/* 286:398 */     this.maxMemberTensileForces = new double[nMembers];
/* 287:400 */     for (int im = 0; im < nMembers; im++)
/* 288:    */     {
/* 289:401 */       Material material = members[im].getMaterial();
/* 290:402 */       Shape shape = members[im].getShape();
/* 291:403 */       this.memberCompressiveStrength[im] = Inventory.compressiveStrength(material, shape, length[im]);
/* 292:404 */       this.memberTensileStrength[im] = Inventory.tensileStrength(material, shape);
/* 293:    */     }
/* 294:406 */     this.status = 4;
/* 295:407 */     for (int im = 0; im < nMembers; im++)
/* 296:    */     {
/* 297:408 */       double maxCompression = 0.0D;
/* 298:409 */       double maxTension = 0.0D;
/* 299:410 */       for (int ilc = 0; ilc < nLoadInstances; ilc++)
/* 300:    */       {
/* 301:411 */         double force = this.memberForce[ilc][im];
/* 302:412 */         if (force < 0.0D)
/* 303:    */         {
/* 304:413 */           force = -force;
/* 305:414 */           if (force > maxCompression) {
/* 306:415 */             maxCompression = force;
/* 307:    */           }
/* 308:417 */           this.memberFails[ilc][im] = (force / this.memberCompressiveStrength[im] > 1.0D ? 1 : 0);
/* 309:    */         }
/* 310:    */         else
/* 311:    */         {
/* 312:419 */           if (force > maxTension) {
/* 313:420 */             maxTension = force;
/* 314:    */           }
/* 315:422 */           this.memberFails[ilc][im] = (force / this.memberTensileStrength[im] > 1.0D ? 1 : 0);
/* 316:    */         }
/* 317:    */       }
/* 318:425 */       double cRatio = maxCompression / this.memberCompressiveStrength[im];
/* 319:426 */       double tRatio = maxTension / this.memberTensileStrength[im];
/* 320:428 */       if ((cRatio > 1.0D) || (tRatio > 1.0D)) {
/* 321:429 */         this.status = 3;
/* 322:    */       }
/* 323:433 */       if (failureStatus == null)
/* 324:    */       {
/* 325:434 */         members[im].setCompressionForceStrengthRatio(cRatio);
/* 326:435 */         members[im].setTensionForceStrengthRatio(tRatio);
/* 327:    */       }
/* 328:437 */       this.maxMemberCompressiveForces[im] = maxCompression;
/* 329:438 */       this.maxMemberTensileForces[im] = maxTension;
/* 330:    */     }
/* 331:440 */     if (!bridge.isPassingSlendernessCheck()) {
/* 332:441 */       this.status = 1;
/* 333:    */     }
/* 334:    */   }
/* 335:    */   
/* 336:    */   private static double hypot(double x, double y)
/* 337:    */   {
/* 338:447 */     return Math.sqrt(x * x + y * y);
/* 339:    */   }
/* 340:    */   
/* 341:    */   public static boolean isStatusBaseLength(double status)
/* 342:    */   {
/* 343:451 */     return (status != 1000000.0D) && (status != -1.0D);
/* 344:    */   }
/* 345:    */   
/* 346:    */   public class Interpolation
/* 347:    */   {
/* 348:    */     private final TerrainModel terrain;
/* 349:463 */     private final Affine.Vector[] displacement = initialDisp();
/* 350:464 */     private final double[] forceRatio = new double['È'];
/* 351:465 */     private final Affine.Point ptLoad = new Affine.Point();
/* 352:466 */     private final Affine.Vector zeroDisp = new Affine.Vector(0.0D, 0.0D);
/* 353:467 */     private final Affine.Point ptRightApproach = new Affine.Point(-100.0D, 0.0D);
/* 354:468 */     private final Affine.Vector loadRotation = new Affine.Vector();
/* 355:    */     private double xLoadParameter;
/* 356:470 */     private int nFailures = 0;
/* 357:471 */     private final double[] failureStatus = new double['È'];
/* 358:    */     
/* 359:    */     public Interpolation(TerrainModel terrain)
/* 360:    */     {
/* 361:479 */       this.terrain = terrain;
/* 362:    */     }
/* 363:    */     
/* 364:    */     public double getMemberStatus(int i)
/* 365:    */     {
/* 366:489 */       return this.failureStatus[i];
/* 367:    */     }
/* 368:    */     
/* 369:    */     private Affine.Vector[] initialDisp()
/* 370:    */     {
/* 371:498 */       Affine.Vector[] v = new Affine.Vector[100];
/* 372:499 */       for (int i = 0; i < v.length; i++) {
/* 373:500 */         v[i] = new Affine.Vector();
/* 374:    */       }
/* 375:502 */       return v;
/* 376:    */     }
/* 377:    */     
/* 378:    */     public boolean isFailure()
/* 379:    */     {
/* 380:511 */       return this.nFailures > 0;
/* 381:    */     }
/* 382:    */     
/* 383:    */     public boolean isFailure(int i)
/* 384:    */     {
/* 385:521 */       return this.failureStatus[i] >= 0.0D;
/* 386:    */     }
/* 387:    */     
/* 388:    */     public Affine.Point getPtLoad()
/* 389:    */     {
/* 390:530 */       return this.ptLoad;
/* 391:    */     }
/* 392:    */     
/* 393:    */     public float[] getLoadRotationMatrix()
/* 394:    */     {
/* 395:539 */       return Utility.rotateAboutZ(this.loadRotation.x, this.loadRotation.y);
/* 396:    */     }
/* 397:    */     
/* 398:    */     public Affine.Vector getLoadRotation()
/* 399:    */     {
/* 400:548 */       return this.loadRotation;
/* 401:    */     }
/* 402:    */     
/* 403:    */     public Affine.Vector getDisplacement(int i)
/* 404:    */     {
/* 405:558 */       return this.displacement[i];
/* 406:    */     }
/* 407:    */     
/* 408:    */     public double[] getFailureStatus()
/* 409:    */     {
/* 410:568 */       return this.failureStatus;
/* 411:    */     }
/* 412:    */     
/* 413:    */     public double getForceRatio(int i)
/* 414:    */     {
/* 415:579 */       return this.forceRatio[i];
/* 416:    */     }
/* 417:    */     
/* 418:    */     private boolean setLoadRotation(Affine.Point pt, Affine.Point p0, Affine.Vector d0, Affine.Point p1, Affine.Vector d1, double dist)
/* 419:    */     {
/* 420:600 */       double xP0 = p0.x + d0.x;
/* 421:601 */       double yP0 = p0.y + d0.y;
/* 422:602 */       if (Analysis.hypot(pt.x - xP0, pt.y - yP0) < dist) {
/* 423:603 */         return false;
/* 424:    */       }
/* 425:605 */       double xP1 = p1.x + d1.x;
/* 426:606 */       double yP1 = p1.y + d1.y;
/* 427:607 */       double t0 = -0.5D;
/* 428:608 */       double t1 = 1.5D;
/* 429:609 */       for (int i = 0; i < 20; i++)
/* 430:    */       {
/* 431:610 */         double t = (t0 + t1) / 2.0D;
/* 432:611 */         double x = (1.0D - t) * xP0 + t * xP1;
/* 433:612 */         double y = (1.0D - t) * yP0 + t * yP1;
/* 434:613 */         double eps = pt.distance(x, y) - dist;
/* 435:614 */         if (eps > 0.01D)
/* 436:    */         {
/* 437:615 */           t0 = t;
/* 438:    */         }
/* 439:617 */         else if (eps < -0.01D)
/* 440:    */         {
/* 441:618 */           t1 = t;
/* 442:    */         }
/* 443:    */         else
/* 444:    */         {
/* 445:621 */           this.loadRotation.x = (pt.x - x);
/* 446:622 */           this.loadRotation.y = (pt.y - y);
/* 447:623 */           double len = this.loadRotation.length();
/* 448:624 */           if (len > 1.0E-006D)
/* 449:    */           {
/* 450:625 */             this.loadRotation.x /= len;
/* 451:626 */             this.loadRotation.y /= len;
/* 452:    */           }
/* 453:    */           else
/* 454:    */           {
/* 455:629 */             this.loadRotation.x = 1.0D;
/* 456:630 */             this.loadRotation.y = 0.0D;
/* 457:    */           }
/* 458:632 */           return true;
/* 459:    */         }
/* 460:    */       }
/* 461:635 */       return false;
/* 462:    */     }
/* 463:    */     
/* 464:    */     public void initializeDeadLoadOnly(double deadLoadApplied, double xLoadParameter, double displacementExaggeration)
/* 465:    */     {
/* 466:647 */       this.xLoadParameter = xLoadParameter;
/* 467:648 */       Iterator<Joint> je = Analysis.this.bridge.getJoints().iterator();
/* 468:649 */       while (je.hasNext())
/* 469:    */       {
/* 470:650 */         Joint joint = (Joint)je.next();
/* 471:651 */         int i = joint.getIndex();
/* 472:652 */         this.displacement[i].x = (deadLoadApplied * displacementExaggeration * Analysis.this.getXJointDisplacement(0, i));
/* 473:653 */         this.displacement[i].y = (deadLoadApplied * displacementExaggeration * Analysis.this.getYJointDisplacement(0, i));
/* 474:    */       }
/* 475:655 */       this.nFailures = 0;
/* 476:656 */       Iterator<Member> me = Analysis.this.bridge.getMembers().iterator();
/* 477:657 */       while (me.hasNext())
/* 478:    */       {
/* 479:658 */         Member member = (Member)me.next();
/* 480:659 */         int i = member.getIndex();
/* 481:660 */         double force = deadLoadApplied * Analysis.this.getMemberForce(0, i);
/* 482:661 */         double ratio = force > 0.0D ? force / Analysis.this.getMemberTensileStrength(i) : force / Analysis.this.getMemberCompressiveStrength(i);
/* 483:662 */         this.forceRatio[i] = (ratio * deadLoadApplied);
/* 484:663 */         if ((this.forceRatio[i] < -1.0D) || (this.forceRatio[i] > 1.0D))
/* 485:    */         {
/* 486:664 */           this.failureStatus[i] = 1000000.0D;
/* 487:665 */           this.nFailures += 1;
/* 488:    */         }
/* 489:    */         else
/* 490:    */         {
/* 491:668 */           this.failureStatus[i] = -1.0D;
/* 492:    */         }
/* 493:    */       }
/* 494:671 */       this.ptLoad.x = (xLoadParameter * 4.0D + ((Joint)Analysis.this.bridge.getJoints().get(0)).getPointWorld().x + this.displacement[0].x);
/* 495:672 */       this.ptLoad.y = this.terrain.getRoadCenterlineElevation((float)this.ptLoad.x);
/* 496:    */       
/* 497:674 */       double x = this.ptLoad.x - 4.0D;
/* 498:675 */       Affine.Point ptSearchLeft = new Affine.Point(x, this.terrain.getRoadCenterlineElevation((float)x));
/* 499:676 */       this.loadRotation.setLocation(1.0D, 0.0D);
/* 500:677 */       setLoadRotation(this.ptLoad, ptSearchLeft, this.zeroDisp, this.ptLoad, this.zeroDisp, 4.0D);
/* 501:    */     }
/* 502:    */     
/* 503:    */     public void initialize(double xLoadParameter, double displacementExaggeration)
/* 504:    */     {
/* 505:688 */       this.xLoadParameter = xLoadParameter;
/* 506:689 */       DesignConditions dc = Analysis.this.bridge.getDesignConditions();
/* 507:690 */       this.ptRightApproach.x = (dc.getXRightmostDeckJoint() + 100.0D);
/* 508:691 */       int nLoadedJoints = dc.getNLoadedJoints();
/* 509:692 */       this.loadRotation.setLocation(1.0D, 0.0D);
/* 510:695 */       if ((xLoadParameter <= 0.0D) || (xLoadParameter >= nLoadedJoints))
/* 511:    */       {
/* 512:696 */         Iterator<Joint> je = Analysis.this.bridge.getJoints().iterator();
/* 513:697 */         while (je.hasNext())
/* 514:    */         {
/* 515:698 */           Joint joint = (Joint)je.next();
/* 516:699 */           int i = joint.getIndex();
/* 517:700 */           this.displacement[i].x = (displacementExaggeration * Analysis.this.getXJointDisplacement(0, i));
/* 518:701 */           this.displacement[i].y = (displacementExaggeration * Analysis.this.getYJointDisplacement(0, i));
/* 519:    */         }
/* 520:703 */         this.nFailures = 0;
/* 521:704 */         Iterator<Member> me = Analysis.this.bridge.getMembers().iterator();
/* 522:705 */         while (me.hasNext())
/* 523:    */         {
/* 524:706 */           Member member = (Member)me.next();
/* 525:707 */           int i = member.getIndex();
/* 526:708 */           double force = Analysis.this.getMemberForce(0, i);
/* 527:709 */           this.forceRatio[i] = (force > 0.0D ? force / Analysis.this.getMemberTensileStrength(i) : force / Analysis.this.getMemberCompressiveStrength(i));
/* 528:710 */           if ((this.forceRatio[i] < -1.0D) || (this.forceRatio[i] > 1.0D))
/* 529:    */           {
/* 530:711 */             this.failureStatus[i] = 1000000.0D;
/* 531:712 */             this.nFailures += 1;
/* 532:    */           }
/* 533:    */           else
/* 534:    */           {
/* 535:715 */             this.failureStatus[i] = -1.0D;
/* 536:    */           }
/* 537:    */         }
/* 538:719 */         if (xLoadParameter <= 0.0D)
/* 539:    */         {
/* 540:720 */           this.ptLoad.x = (xLoadParameter * 4.0D + ((Joint)Analysis.this.bridge.getJoints().get(0)).getPointWorld().x + this.displacement[0].x);
/* 541:    */         }
/* 542:    */         else
/* 543:    */         {
/* 544:724 */           int iLast = nLoadedJoints - 1;
/* 545:725 */           this.ptLoad.x = ((xLoadParameter - iLast) * 4.0D + ((Joint)Analysis.this.bridge.getJoints().get(iLast)).getPointWorld().x + this.displacement[iLast].x);
/* 546:    */         }
/* 547:728 */         this.ptLoad.y = this.terrain.getRoadCenterlineElevation((float)this.ptLoad.x);
/* 548:729 */         double x = this.ptLoad.x - 4.0D;
/* 549:730 */         Affine.Point ptSearchLeft = new Affine.Point(x, this.terrain.getRoadCenterlineElevation((float)x));
/* 550:731 */         setLoadRotation(this.ptLoad, ptSearchLeft, this.zeroDisp, this.ptLoad, this.zeroDisp, 4.0D);
/* 551:732 */         return;
/* 552:    */       }
/* 553:736 */       int ilcLeft = (int)xLoadParameter;
/* 554:737 */       int ilcRight = ilcLeft < nLoadedJoints - 1 ? ilcLeft + 1 : 0;
/* 555:738 */       double t1 = xLoadParameter - ilcLeft;
/* 556:739 */       double t0 = 1.0D - t1;
/* 557:740 */       Iterator<Joint> je = Analysis.this.bridge.getJoints().iterator();
/* 558:741 */       while (je.hasNext())
/* 559:    */       {
/* 560:742 */         int i = ((Joint)je.next()).getIndex();
/* 561:743 */         this.displacement[i].x = (displacementExaggeration * (t0 * Analysis.this.getXJointDisplacement(ilcLeft, i) + t1 * Analysis.this.getXJointDisplacement(ilcRight, i)));
/* 562:744 */         this.displacement[i].y = (displacementExaggeration * (t0 * Analysis.this.getYJointDisplacement(ilcLeft, i) + t1 * Analysis.this.getYJointDisplacement(ilcRight, i)));
/* 563:    */       }
/* 564:746 */       this.nFailures = 0;
/* 565:747 */       Iterator<Member> me = Analysis.this.bridge.getMembers().iterator();
/* 566:748 */       while (me.hasNext())
/* 567:    */       {
/* 568:749 */         Member member = (Member)me.next();
/* 569:750 */         int i = member.getIndex();
/* 570:751 */         double force = t0 * Analysis.this.memberForce[ilcLeft][i] + t1 * Analysis.this.memberForce[ilcRight][i];
/* 571:752 */         double ratio = force > 0.0D ? force / Analysis.this.memberTensileStrength[i] : force / Analysis.this.memberCompressiveStrength[i];
/* 572:754 */         if ((Analysis.this.memberFails[ilcLeft][i] != 0) || (ratio < -1.0D) || (ratio > 1.0D))
/* 573:    */         {
/* 574:755 */           this.failureStatus[i] = 1000000.0D;
/* 575:756 */           this.nFailures += 1;
/* 576:    */         }
/* 577:    */         else
/* 578:    */         {
/* 579:759 */           this.failureStatus[i] = -1.0D;
/* 580:    */         }
/* 581:761 */         this.forceRatio[i] = ratio;
/* 582:    */       }
/* 583:763 */       Affine.Point ptLeft = ((Joint)Analysis.this.bridge.getJoints().get(ilcLeft)).getPointWorld();
/* 584:764 */       Affine.Point ptRight = ((Joint)Analysis.this.bridge.getJoints().get(ilcRight)).getPointWorld();
/* 585:765 */       if (ilcLeft < nLoadedJoints - 1)
/* 586:    */       {
/* 587:767 */         this.ptLoad.x = (t0 * (ptLeft.x + this.displacement[ilcLeft].x) + t1 * (ptRight.x + this.displacement[ilcRight].x));
/* 588:768 */         this.ptLoad.y = (t0 * (ptLeft.y + this.displacement[ilcLeft].y) + t1 * (ptRight.y + this.displacement[ilcRight].y) + 0.8D);
/* 589:    */       }
/* 590:    */       else
/* 591:    */       {
/* 592:772 */         this.ptLoad.x = (ptLeft.x + this.displacement[ilcLeft].x + t1 * 4.0D);
/* 593:773 */         this.ptLoad.y = (t0 * (ptLeft.y + this.displacement[ilcLeft].y + 0.8D) + t1 * this.terrain.getRoadCenterlineElevation((float)this.ptLoad.x));
/* 594:    */       }
/* 595:    */       Affine.Vector dispSearchRight;
/* 596:    */       Affine.Point ptSearchRight;
/* 597:    */       Affine.Vector dispSearchRight;
/* 598:780 */       if (ilcLeft < nLoadedJoints - 1)
/* 599:    */       {
/* 600:781 */         Affine.Point ptSearchRight = ptRight;
/* 601:782 */         dispSearchRight = this.displacement[ilcRight];
/* 602:    */       }
/* 603:    */       else
/* 604:    */       {
/* 605:785 */         ptSearchRight = this.ptLoad;
/* 606:786 */         dispSearchRight = this.zeroDisp;
/* 607:    */       }
/* 608:790 */       for (int i = ilcLeft; i >= -1; i--)
/* 609:    */       {
/* 610:    */         Affine.Vector dispSearchLeft;
/* 611:    */         Affine.Point ptSearchLeft;
/* 612:    */         Affine.Vector dispSearchLeft;
/* 613:793 */         if (i < 0)
/* 614:    */         {
/* 615:794 */           double x = this.ptLoad.x - 4.0D;
/* 616:795 */           Affine.Point ptSearchLeft = new Affine.Point(x, this.terrain.getRoadCenterlineElevation((float)x));
/* 617:796 */           dispSearchLeft = this.zeroDisp;
/* 618:    */         }
/* 619:    */         else
/* 620:    */         {
/* 621:799 */           ptSearchLeft = ((Joint)Analysis.this.bridge.getJoints().get(i)).getPointWorld().plus(0.0D, 0.8D);
/* 622:800 */           dispSearchLeft = this.displacement[i];
/* 623:    */         }
/* 624:802 */         if (setLoadRotation(this.ptLoad, ptSearchLeft, dispSearchLeft, ptSearchRight, dispSearchRight, 4.0D)) {
/* 625:    */           break;
/* 626:    */         }
/* 627:805 */         ptSearchRight = ptSearchLeft;
/* 628:806 */         dispSearchRight = dispSearchLeft;
/* 629:    */       }
/* 630:    */     }
/* 631:    */     
/* 632:    */     public void initialize(Interpolation base, Interpolation target, double displacementParameter)
/* 633:    */     {
/* 634:821 */       this.xLoadParameter = base.xLoadParameter;
/* 635:822 */       DesignConditions dc = Analysis.this.bridge.getDesignConditions();
/* 636:823 */       this.ptRightApproach.x = (dc.getXRightmostDeckJoint() + 100.0D);
/* 637:824 */       int nLoadedJoints = dc.getNLoadedJoints();
/* 638:825 */       this.loadRotation.setLocation(1.0D, 0.0D);
/* 639:826 */       double ta = 1.0D - displacementParameter;
/* 640:827 */       double tf = displacementParameter;
/* 641:828 */       int nJoints = Analysis.this.bridge.getJoints().size();
/* 642:829 */       for (int i = 0; i < nJoints; i++)
/* 643:    */       {
/* 644:830 */         this.displacement[i].x = (ta * base.displacement[i].x + tf * target.displacement[i].x);
/* 645:831 */         this.displacement[i].y = (ta * base.displacement[i].y + tf * target.displacement[i].y);
/* 646:    */       }
/* 647:833 */       int nMembers = Analysis.this.bridge.getMembers().size();
/* 648:834 */       for (int i = 0; i < nMembers; i++)
/* 649:    */       {
/* 650:835 */         this.forceRatio[i] = base.forceRatio[i];
/* 651:836 */         if (base.failureStatus[i] == -1.0D)
/* 652:    */         {
/* 653:837 */           this.failureStatus[i] = -1.0D;
/* 654:    */         }
/* 655:    */         else
/* 656:    */         {
/* 657:842 */           Member m = (Member)Analysis.this.bridge.getMembers().get(i);
/* 658:843 */           Joint a = m.getJointA();
/* 659:844 */           Joint b = m.getJointB();
/* 660:845 */           Affine.Point pa = a.getPointWorld();
/* 661:846 */           Affine.Point pb = b.getPointWorld();
/* 662:847 */           Affine.Vector da = base.displacement[a.getIndex()];
/* 663:848 */           Affine.Vector db = base.displacement[b.getIndex()];
/* 664:849 */           double dx = pa.x + da.x - (pb.x + db.x);
/* 665:850 */           double dy = pa.y + da.y - (pb.y + db.y);
/* 666:851 */           this.failureStatus[i] = Math.sqrt(dx * dx + dy * dy);
/* 667:    */         }
/* 668:    */       }
/* 669:854 */       this.nFailures = base.nFailures;
/* 670:857 */       if ((this.xLoadParameter <= 0.0D) || (this.xLoadParameter >= nLoadedJoints))
/* 671:    */       {
/* 672:859 */         if (this.xLoadParameter <= 0.0D)
/* 673:    */         {
/* 674:860 */           this.ptLoad.x = (this.xLoadParameter * 4.0D + ((Joint)Analysis.this.bridge.getJoints().get(0)).getPointWorld().x + this.displacement[0].x);
/* 675:    */         }
/* 676:    */         else
/* 677:    */         {
/* 678:864 */           int iLast = nLoadedJoints - 1;
/* 679:865 */           this.ptLoad.x = ((this.xLoadParameter - iLast) * 4.0D + ((Joint)Analysis.this.bridge.getJoints().get(iLast)).getPointWorld().x + this.displacement[iLast].x);
/* 680:    */         }
/* 681:868 */         this.ptLoad.y = this.terrain.getRoadCenterlineElevation((float)this.ptLoad.x);
/* 682:869 */         double x = this.ptLoad.x - 4.0D;
/* 683:870 */         Affine.Point ptSearchLeft = new Affine.Point(x, this.terrain.getRoadCenterlineElevation((float)x));
/* 684:871 */         setLoadRotation(this.ptLoad, ptSearchLeft, this.zeroDisp, this.ptLoad, this.zeroDisp, 4.0D);
/* 685:872 */         return;
/* 686:    */       }
/* 687:876 */       int ilcLeft = (int)this.xLoadParameter;
/* 688:877 */       int ilcRight = ilcLeft < nLoadedJoints - 1 ? ilcLeft + 1 : 0;
/* 689:878 */       double t1 = this.xLoadParameter - ilcLeft;
/* 690:879 */       double t0 = 1.0D - t1;
/* 691:880 */       Affine.Point ptLeft = ((Joint)Analysis.this.bridge.getJoints().get(ilcLeft)).getPointWorld();
/* 692:881 */       Affine.Point ptRight = ((Joint)Analysis.this.bridge.getJoints().get(ilcRight)).getPointWorld();
/* 693:882 */       if (ilcLeft < nLoadedJoints - 1)
/* 694:    */       {
/* 695:884 */         this.ptLoad.x = (t0 * (ptLeft.x + this.displacement[ilcLeft].x) + t1 * (ptRight.x + this.displacement[ilcRight].x));
/* 696:885 */         this.ptLoad.y = (t0 * (ptLeft.y + this.displacement[ilcLeft].y) + t1 * (ptRight.y + this.displacement[ilcRight].y) + 0.8D);
/* 697:    */       }
/* 698:    */       else
/* 699:    */       {
/* 700:889 */         this.ptLoad.x = (ptLeft.x + this.displacement[ilcLeft].x + t1 * 4.0D);
/* 701:890 */         this.ptLoad.y = (t0 * (ptLeft.y + this.displacement[ilcLeft].y + 0.8D) + t1 * this.terrain.getRoadCenterlineElevation((float)this.ptLoad.x));
/* 702:    */       }
/* 703:    */       Affine.Vector dispSearchRight;
/* 704:    */       Affine.Point ptSearchRight;
/* 705:    */       Affine.Vector dispSearchRight;
/* 706:897 */       if (ilcLeft < nLoadedJoints - 1)
/* 707:    */       {
/* 708:898 */         Affine.Point ptSearchRight = ptRight.plus(0.0D, 0.8D);
/* 709:899 */         dispSearchRight = this.displacement[ilcRight];
/* 710:    */       }
/* 711:    */       else
/* 712:    */       {
/* 713:902 */         ptSearchRight = this.ptLoad;
/* 714:903 */         dispSearchRight = this.zeroDisp;
/* 715:    */       }
/* 716:907 */       for (int i = ilcLeft; i >= -1; i--)
/* 717:    */       {
/* 718:    */         Affine.Vector dispSearchLeft;
/* 719:    */         Affine.Point ptSearchLeft;
/* 720:    */         Affine.Vector dispSearchLeft;
/* 721:910 */         if (i < 0)
/* 722:    */         {
/* 723:911 */           double x = this.ptLoad.x - 4.0D;
/* 724:912 */           Affine.Point ptSearchLeft = new Affine.Point(x, this.terrain.getRoadCenterlineElevation((float)x));
/* 725:913 */           dispSearchLeft = this.zeroDisp;
/* 726:    */         }
/* 727:    */         else
/* 728:    */         {
/* 729:916 */           ptSearchLeft = ((Joint)Analysis.this.bridge.getJoints().get(i)).getPointWorld().plus(0.0D, 0.8D);
/* 730:917 */           dispSearchLeft = this.displacement[i];
/* 731:    */         }
/* 732:919 */         if (setLoadRotation(this.ptLoad, ptSearchLeft, dispSearchLeft, ptSearchRight, dispSearchRight, 4.0D)) {
/* 733:    */           break;
/* 734:    */         }
/* 735:922 */         ptSearchRight = ptSearchLeft;
/* 736:923 */         dispSearchRight = dispSearchLeft;
/* 737:    */       }
/* 738:    */     }
/* 739:    */   }
/* 740:    */   
/* 741:    */   public Interpolation getNewInterpolation(TerrainModel terrain)
/* 742:    */   {
/* 743:936 */     return new Interpolation(terrain);
/* 744:    */   }
/* 745:    */   
/* 746:    */   private static double sqr(double x)
/* 747:    */   {
/* 748:946 */     return x * x;
/* 749:    */   }
/* 750:    */   
/* 751:    */   private static class Runnable
/* 752:    */   {
/* 753:952 */     private BridgeModel bridge = new BridgeModel();
/* 754:953 */     private Analysis analysis = new Analysis();
/* 755:    */     
/* 756:    */     private void run(String fileName)
/* 757:    */     {
/* 758:    */       try
/* 759:    */       {
/* 760:957 */         this.bridge.read(new File(fileName));
/* 761:958 */         this.analysis.initialize(this.bridge);
/* 762:959 */         if (this.analysis.getStatus() >= 1)
/* 763:    */         {
/* 764:960 */           System.out.print(fileName + ": ");
/* 765:961 */           System.out.println(this.analysis.getStatus() == 4 ? "passes." : "fails.");
/* 766:    */         }
/* 767:    */       }
/* 768:    */       catch (IOException ex)
/* 769:    */       {
/* 770:964 */         System.err.println("could not open '" + fileName + "' as a bridge file.");
/* 771:    */       }
/* 772:    */     }
/* 773:    */   }
/* 774:    */   
/* 775:    */   public static void main(String[] args)
/* 776:    */   {
/* 777:970 */     if (args.length != 1) {
/* 778:971 */       System.err.println("usage: java Analysis FileName");
/* 779:    */     } else {
/* 780:974 */       new Runnable(null).run(args[0]);
/* 781:    */     }
/* 782:    */   }
/* 783:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.Analysis
 * JD-Core Version:    0.7.0.1
 */