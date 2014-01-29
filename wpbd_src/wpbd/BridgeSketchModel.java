/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.io.PrintStream;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.HashMap;
/*   6:    */ import java.util.Iterator;
/*   7:    */ import java.util.Set;
/*   8:    */ import org.jdesktop.application.ResourceMap;
/*   9:    */ 
/*  10:    */ public class BridgeSketchModel
/*  11:    */ {
/*  12:    */   protected String name;
/*  13:    */   protected DesignConditions conditions;
/*  14:    */   protected Affine.Point[] jointLocations;
/*  15:    */   protected SketchMember[] memberLocations;
/*  16:    */   protected static final int gridScale = -2;
/*  17:    */   private ArrayList<Affine.Point> pts;
/*  18:    */   private ArrayList<SketchMember> mrs;
/*  19:    */   
/*  20:    */   public BridgeSketchModel()
/*  21:    */   {
/*  22: 49 */     this.pts = new ArrayList();
/*  23: 50 */     this.mrs = new ArrayList();
/*  24:    */   }
/*  25:    */   
/*  26:    */   public static class SketchMember
/*  27:    */   {
/*  28:    */     public Affine.Point jointA;
/*  29:    */     public Affine.Point jointB;
/*  30:    */     
/*  31:    */     public SketchMember(Affine.Point a, Affine.Point b)
/*  32:    */     {
/*  33: 72 */       this.jointA = a;
/*  34: 73 */       this.jointB = b;
/*  35:    */     }
/*  36:    */   }
/*  37:    */   
/*  38:    */   public DesignConditions getDesignConditions()
/*  39:    */   {
/*  40: 81 */     return this.conditions;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public Affine.Point getJointLocation(int i)
/*  44:    */   {
/*  45: 91 */     return this.jointLocations[i];
/*  46:    */   }
/*  47:    */   
/*  48:    */   public int getJointLocationCount()
/*  49:    */   {
/*  50:100 */     return this.jointLocations.length;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public SketchMember getSketchMember(int i)
/*  54:    */   {
/*  55:110 */     return this.memberLocations[i];
/*  56:    */   }
/*  57:    */   
/*  58:    */   public int getSketchMemberCount()
/*  59:    */   {
/*  60:119 */     return this.memberLocations.length;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public int getSnapMultiple()
/*  64:    */   {
/*  65:129 */     int rtn = DraftingGrid.maxSnapMultiple;
/*  66:130 */     for (int i = 0; i < this.jointLocations.length; i++) {
/*  67:131 */       rtn = Math.min(rtn, Math.min(DraftingGrid.snapMultipleOf(this.jointLocations[i].x), DraftingGrid.snapMultipleOf(this.jointLocations[i].y)));
/*  68:    */     }
/*  69:135 */     return rtn;
/*  70:    */   }
/*  71:    */   
/*  72:    */   private void setPrescribedJoints(DesignConditions conditions)
/*  73:    */   {
/*  74:144 */     this.conditions = conditions;
/*  75:145 */     this.pts.clear();
/*  76:146 */     this.mrs.clear();
/*  77:147 */     int nPrescribedJoints = conditions.getNPrescribedJoints();
/*  78:148 */     for (int i = 0; i < nPrescribedJoints; i++) {
/*  79:149 */       this.pts.add(conditions.getPrescribedJointLocation(i));
/*  80:    */     }
/*  81:152 */     int iFirstBottomJoint = 0;
/*  82:153 */     int iLastBottomJoint = conditions.getNPanels();
/*  83:154 */     for (int i = 0; i < iLastBottomJoint; i++) {
/*  84:155 */       this.mrs.add(new SketchMember((Affine.Point)this.pts.get(i), (Affine.Point)this.pts.get(i + 1)));
/*  85:    */     }
/*  86:    */   }
/*  87:    */   
/*  88:    */   private void closeSketch()
/*  89:    */   {
/*  90:163 */     this.jointLocations = ((Affine.Point[])this.pts.toArray(new Affine.Point[this.pts.size()]));
/*  91:164 */     this.memberLocations = ((SketchMember[])this.mrs.toArray(new SketchMember[this.mrs.size()]));
/*  92:    */   }
/*  93:    */   
/*  94:    */   private void setPrattThruTrussImpl(DesignConditions conditions, double jointY)
/*  95:    */   {
/*  96:171 */     setPrescribedJoints(conditions);
/*  97:172 */     int iFirstBottomJoint = 0;
/*  98:173 */     int iLastBottomJoint = conditions.getNPanels();
/*  99:174 */     int iFirstTopJoint = this.pts.size();
/* 100:175 */     for (int i = 1; i < iLastBottomJoint; i++) {
/* 101:176 */       this.pts.add(new Affine.Point(((Affine.Point)this.pts.get(i)).x, jointY));
/* 102:    */     }
/* 103:178 */     int iLastTopJoint = this.pts.size() - 1;
/* 104:179 */     this.mrs.add(new SketchMember((Affine.Point)this.pts.get(0), (Affine.Point)this.pts.get(iFirstTopJoint)));
/* 105:180 */     for (int i = iFirstTopJoint; i < iLastTopJoint; i++) {
/* 106:181 */       this.mrs.add(new SketchMember((Affine.Point)this.pts.get(i), (Affine.Point)this.pts.get(i + 1)));
/* 107:    */     }
/* 108:183 */     this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iLastBottomJoint), (Affine.Point)this.pts.get(iLastTopJoint)));
/* 109:184 */     int iLeftTop = iFirstTopJoint;
/* 110:185 */     int iRightTop = iLastTopJoint;
/* 111:186 */     int iLeftBottom = 1;
/* 112:187 */     int iRightBottom = iLastBottomJoint - 1;
/* 113:188 */     while (iLeftTop < iRightTop)
/* 114:    */     {
/* 115:189 */       this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iLeftTop), (Affine.Point)this.pts.get(iLeftBottom)));
/* 116:190 */       this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iLeftTop), (Affine.Point)this.pts.get(iLeftBottom + 1)));
/* 117:191 */       this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iRightTop), (Affine.Point)this.pts.get(iRightBottom)));
/* 118:192 */       this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iRightTop), (Affine.Point)this.pts.get(iRightBottom - 1)));
/* 119:193 */       iLeftTop++;
/* 120:194 */       iRightTop--;
/* 121:195 */       iLeftBottom++;
/* 122:196 */       iRightBottom--;
/* 123:    */     }
/* 124:198 */     if (iLeftTop == iRightTop) {
/* 125:199 */       this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iLeftTop), (Affine.Point)this.pts.get(iLeftBottom)));
/* 126:    */     }
/* 127:201 */     closeSketch();
/* 128:    */   }
/* 129:    */   
/* 130:    */   private void setHoweThruTrussImpl(DesignConditions conditions, double jointY)
/* 131:    */   {
/* 132:205 */     setPrescribedJoints(conditions);
/* 133:206 */     int iFirstBottomJoint = 0;
/* 134:207 */     int iLastBottomJoint = conditions.getNPanels();
/* 135:208 */     int iFirstTopJoint = this.pts.size();
/* 136:209 */     for (int i = 1; i < iLastBottomJoint; i++) {
/* 137:210 */       this.pts.add(new Affine.Point(((Affine.Point)this.pts.get(i)).x, jointY));
/* 138:    */     }
/* 139:212 */     int iLastTopJoint = this.pts.size() - 1;
/* 140:213 */     this.mrs.add(new SketchMember((Affine.Point)this.pts.get(0), (Affine.Point)this.pts.get(iFirstTopJoint)));
/* 141:214 */     for (int i = iFirstTopJoint; i < iLastTopJoint; i++) {
/* 142:215 */       this.mrs.add(new SketchMember((Affine.Point)this.pts.get(i), (Affine.Point)this.pts.get(i + 1)));
/* 143:    */     }
/* 144:217 */     this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iLastBottomJoint), (Affine.Point)this.pts.get(iLastTopJoint)));
/* 145:218 */     int iLeftTop = iFirstTopJoint;
/* 146:219 */     int iRightTop = iLastTopJoint;
/* 147:220 */     int iLeftBottom = 1;
/* 148:221 */     int iRightBottom = iLastBottomJoint - 1;
/* 149:222 */     while (iLeftTop < iRightTop)
/* 150:    */     {
/* 151:223 */       this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iLeftTop), (Affine.Point)this.pts.get(iLeftBottom)));
/* 152:224 */       this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iLeftTop + 1), (Affine.Point)this.pts.get(iLeftBottom)));
/* 153:225 */       this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iRightTop), (Affine.Point)this.pts.get(iRightBottom)));
/* 154:226 */       this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iRightTop - 1), (Affine.Point)this.pts.get(iRightBottom)));
/* 155:227 */       iLeftTop++;
/* 156:228 */       iRightTop--;
/* 157:229 */       iLeftBottom++;
/* 158:230 */       iRightBottom--;
/* 159:    */     }
/* 160:232 */     if (iLeftTop == iRightTop) {
/* 161:233 */       this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iLeftTop), (Affine.Point)this.pts.get(iLeftBottom)));
/* 162:    */     }
/* 163:235 */     closeSketch();
/* 164:    */   }
/* 165:    */   
/* 166:    */   private void setWarrenTrussImpl(DesignConditions conditions, double jointY)
/* 167:    */   {
/* 168:239 */     setPrescribedJoints(conditions);
/* 169:240 */     int iFirstBottomJoint = 0;
/* 170:241 */     int iLastBottomJoint = conditions.getNPanels();
/* 171:242 */     int iFirstTopJoint = this.pts.size();
/* 172:243 */     for (int i = 0; i < iLastBottomJoint; i++) {
/* 173:244 */       this.pts.add(new Affine.Point(((Affine.Point)this.pts.get(i)).x + 2.0D, jointY));
/* 174:    */     }
/* 175:246 */     int iLastTopJoint = this.pts.size() - 1;
/* 176:248 */     for (int i = iFirstTopJoint; i < iLastTopJoint; i++) {
/* 177:249 */       this.mrs.add(new SketchMember((Affine.Point)this.pts.get(i), (Affine.Point)this.pts.get(i + 1)));
/* 178:    */     }
/* 179:251 */     this.mrs.add(new SketchMember((Affine.Point)this.pts.get(0), (Affine.Point)this.pts.get(iFirstTopJoint)));
/* 180:252 */     this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iLastBottomJoint), (Affine.Point)this.pts.get(iLastTopJoint)));
/* 181:253 */     int iTop = iFirstTopJoint;
/* 182:254 */     int iBottom = 0;
/* 183:255 */     while (iTop <= iLastTopJoint)
/* 184:    */     {
/* 185:256 */       this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iTop), (Affine.Point)this.pts.get(iBottom)));
/* 186:257 */       this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iTop), (Affine.Point)this.pts.get(iBottom + 1)));
/* 187:258 */       iTop++;
/* 188:259 */       iBottom++;
/* 189:    */     }
/* 190:    */   }
/* 191:    */   
/* 192:    */   private void setWarrenTruss(DesignConditions conditions, double jointY)
/* 193:    */   {
/* 194:264 */     setWarrenTrussImpl(conditions, jointY);
/* 195:265 */     closeSketch();
/* 196:    */   }
/* 197:    */   
/* 198:    */   private void setCableStayedWarrenTruss(DesignConditions conditions, double jointY)
/* 199:    */   {
/* 200:269 */     setWarrenTrussImpl(conditions, jointY);
/* 201:270 */     if (conditions.getNAnchorages() > 0)
/* 202:    */     {
/* 203:271 */       Affine.Point leftAbutmentJoint = (Affine.Point)this.pts.get(0);
/* 204:272 */       Affine.Point leftAnchorageJoint = (Affine.Point)this.pts.get(conditions.getNPanels() + 1);
/* 205:273 */       Affine.Point mastJoint = new Affine.Point(leftAbutmentJoint.x, leftAbutmentJoint.y + 8.0D);
/* 206:274 */       this.pts.add(mastJoint);
/* 207:275 */       this.mrs.add(new SketchMember(mastJoint, leftAbutmentJoint));
/* 208:276 */       this.mrs.add(new SketchMember(mastJoint, leftAnchorageJoint));
/* 209:277 */       for (int i = 1; i <= conditions.getNPanels() / 2; i++) {
/* 210:278 */         this.mrs.add(new SketchMember(mastJoint, (Affine.Point)this.pts.get(i)));
/* 211:    */       }
/* 212:    */     }
/* 213:281 */     if (conditions.getNAnchorages() > 1)
/* 214:    */     {
/* 215:282 */       Affine.Point rightAbutmentJoint = (Affine.Point)this.pts.get(conditions.getNPanels());
/* 216:283 */       Affine.Point rightAnchorageJoint = (Affine.Point)this.pts.get(conditions.getNPanels() + 2);
/* 217:284 */       Affine.Point mastJoint = new Affine.Point(rightAbutmentJoint.x, rightAbutmentJoint.y + 8.0D);
/* 218:285 */       this.pts.add(mastJoint);
/* 219:286 */       this.mrs.add(new SketchMember(mastJoint, rightAbutmentJoint));
/* 220:287 */       this.mrs.add(new SketchMember(mastJoint, rightAnchorageJoint));
/* 221:288 */       for (int i = 1; i <= conditions.getNPanels() / 2; i++) {
/* 222:289 */         this.mrs.add(new SketchMember(mastJoint, (Affine.Point)this.pts.get(conditions.getNPanels() - i)));
/* 223:    */       }
/* 224:    */     }
/* 225:292 */     closeSketch();
/* 226:    */   }
/* 227:    */   
/* 228:    */   private void setSuspendedWarrenTruss(DesignConditions conditions, double jointY)
/* 229:    */   {
/* 230:296 */     setWarrenTrussImpl(conditions, jointY);
/* 231:297 */     if (conditions.getNAnchorages() == 2)
/* 232:    */     {
/* 233:298 */       Affine.Point leftAbutmentJoint = (Affine.Point)this.pts.get(0);
/* 234:299 */       Affine.Point leftAnchorageJoint = (Affine.Point)this.pts.get(conditions.getNPanels() + 1);
/* 235:300 */       Affine.Point leftMastJoint = new Affine.Point(leftAbutmentJoint.x, leftAbutmentJoint.y + 8.0D);
/* 236:301 */       this.pts.add(leftMastJoint);
/* 237:302 */       this.mrs.add(new SketchMember(leftMastJoint, leftAbutmentJoint));
/* 238:303 */       this.mrs.add(new SketchMember(leftMastJoint, leftAnchorageJoint));
/* 239:304 */       Affine.Point rightAbutmentJoint = (Affine.Point)this.pts.get(conditions.getNPanels());
/* 240:305 */       Affine.Point rightAnchorageJoint = (Affine.Point)this.pts.get(conditions.getNLoadedJoints() + 1);
/* 241:306 */       Affine.Point rightMastJoint = new Affine.Point(rightAbutmentJoint.x, rightAbutmentJoint.y + 8.0D);
/* 242:307 */       this.pts.add(rightMastJoint);
/* 243:308 */       this.mrs.add(new SketchMember(rightMastJoint, rightAbutmentJoint));
/* 244:309 */       this.mrs.add(new SketchMember(rightMastJoint, rightAnchorageJoint));
/* 245:    */       
/* 246:311 */       double x0 = 0.5D * (leftMastJoint.x + rightMastJoint.x);
/* 247:312 */       double xb = rightMastJoint.x - x0;
/* 248:313 */       double yb = rightMastJoint.y;
/* 249:314 */       Affine.Point aPt = (Affine.Point)this.pts.get((conditions.getNPanels() + 1) / 2);
/* 250:315 */       double xa = aPt.x - x0;
/* 251:316 */       double ya = aPt.y + 1.0D;
/* 252:317 */       double a = (ya - yb) / (xa * xa - xb * xb);
/* 253:318 */       double b = ya - a * xa * xa;
/* 254:319 */       double x = xb - 4.0D;
/* 255:320 */       Affine.Point lastJoint = rightMastJoint;
/* 256:321 */       for (int i = conditions.getNPanels() - 1; i > 0; i--)
/* 257:    */       {
/* 258:322 */         double y = a * x * x + b;
/* 259:323 */         Affine.Point currentJoint = new Affine.Point(x0 + x, roundToScale(y, -2));
/* 260:324 */         this.pts.add(currentJoint);
/* 261:325 */         this.mrs.add(new SketchMember((Affine.Point)this.pts.get(i), currentJoint));
/* 262:326 */         this.mrs.add(new SketchMember(lastJoint, currentJoint));
/* 263:327 */         x -= 4.0D;
/* 264:328 */         lastJoint = currentJoint;
/* 265:    */       }
/* 266:330 */       this.mrs.add(new SketchMember(lastJoint, leftMastJoint));
/* 267:    */     }
/* 268:332 */     closeSketch();
/* 269:    */   }
/* 270:    */   
/* 271:    */   private void setNameFromResource(String key)
/* 272:    */   {
/* 273:341 */     this.name = WPBDApp.getResourceMap(BridgeSketchModel.class).getString(key, new Object[0]);
/* 274:    */   }
/* 275:    */   
/* 276:    */   public BridgeSketchModel setPrattThruTruss(DesignConditions conditions)
/* 277:    */   {
/* 278:351 */     setNameFromResource("prattThruTruss.text");
/* 279:352 */     setPrattThruTrussImpl(conditions, 4.0D);
/* 280:353 */     return this;
/* 281:    */   }
/* 282:    */   
/* 283:    */   public BridgeSketchModel setPrattDeckTruss(DesignConditions conditions)
/* 284:    */   {
/* 285:363 */     setNameFromResource("prattDeckTruss.text");
/* 286:    */     
/* 287:365 */     setHoweThruTrussImpl(conditions, -4.0D);
/* 288:366 */     return this;
/* 289:    */   }
/* 290:    */   
/* 291:    */   public BridgeSketchModel setHoweThruTruss(DesignConditions conditions)
/* 292:    */   {
/* 293:376 */     setNameFromResource("howeThruTruss.text");
/* 294:377 */     setHoweThruTrussImpl(conditions, 4.0D);
/* 295:378 */     return this;
/* 296:    */   }
/* 297:    */   
/* 298:    */   public BridgeSketchModel setHoweDeckTruss(DesignConditions conditions)
/* 299:    */   {
/* 300:388 */     setNameFromResource("howeDeckTruss.text");
/* 301:    */     
/* 302:390 */     setPrattThruTrussImpl(conditions, -4.0D);
/* 303:391 */     return this;
/* 304:    */   }
/* 305:    */   
/* 306:    */   public BridgeSketchModel setWarrenThruTruss(DesignConditions conditions)
/* 307:    */   {
/* 308:401 */     setNameFromResource("warrenThruTruss.text");
/* 309:402 */     setWarrenTruss(conditions, 4.0D);
/* 310:403 */     return this;
/* 311:    */   }
/* 312:    */   
/* 313:    */   public BridgeSketchModel setWarrenDeckTruss(DesignConditions conditions)
/* 314:    */   {
/* 315:413 */     setNameFromResource("warrenDeckTruss.text");
/* 316:414 */     setWarrenTruss(conditions, -4.0D);
/* 317:415 */     return this;
/* 318:    */   }
/* 319:    */   
/* 320:    */   public BridgeSketchModel setCableStayedWarrenTruss(DesignConditions conditions)
/* 321:    */   {
/* 322:425 */     setNameFromResource("warrenCableStayed.text");
/* 323:426 */     setCableStayedWarrenTruss(conditions, -2.0D);
/* 324:427 */     return this;
/* 325:    */   }
/* 326:    */   
/* 327:    */   public BridgeSketchModel setSuspendedWarrenTruss(DesignConditions conditions)
/* 328:    */   {
/* 329:437 */     setNameFromResource("warrenSuspended.text");
/* 330:438 */     setSuspendedWarrenTruss(conditions, -2.0D);
/* 331:439 */     return this;
/* 332:    */   }
/* 333:    */   
/* 334:    */   private static double roundToScale(double x, int scale)
/* 335:    */   {
/* 336:450 */     return Math.scalb(Math.rint(Math.scalb(x, -scale)), scale);
/* 337:    */   }
/* 338:    */   
/* 339:    */   public BridgeSketchModel setPrattContArch(DesignConditions conditions)
/* 340:    */   {
/* 341:460 */     setNameFromResource("prattContArch.text");
/* 342:461 */     setPrescribedJoints(conditions);
/* 343:462 */     int iAbutmentArchJoints = conditions.getArchJointIndex();
/* 344:463 */     int iFirstDeckJoint = 0;
/* 345:464 */     int iLastDeckJoint = conditions.getNPanels();
/* 346:    */     
/* 347:466 */     Affine.Point p1 = (Affine.Point)this.pts.get(iAbutmentArchJoints);
/* 348:467 */     Affine.Point p2 = (Affine.Point)this.pts.get((0 + iLastDeckJoint) / 2);
/* 349:468 */     Affine.Point p3 = (Affine.Point)this.pts.get(iAbutmentArchJoints + 1);
/* 350:469 */     double xMid = 0.5D * (p1.x + p3.x);
/* 351:470 */     double x1 = p1.x - xMid;
/* 352:471 */     double y1 = p1.y;
/* 353:472 */     double x2 = p2.x - xMid;
/* 354:    */     
/* 355:    */ 
/* 356:475 */     double y2 = p2.y - 0.25D * (p2.y - p1.y);
/* 357:    */     
/* 358:477 */     double a = (y2 - y1) / (x2 * x2 - x1 * x1);
/* 359:478 */     double b = y1 - a * x1 * x1;
/* 360:    */     
/* 361:480 */     int iFirstArchJoint = this.pts.size();
/* 362:481 */     for (int i = 1; i < iLastDeckJoint; i++)
/* 363:    */     {
/* 364:482 */       double x = ((Affine.Point)this.pts.get(i)).x - xMid;
/* 365:483 */       double y = a * x * x + b;
/* 366:484 */       this.pts.add(new Affine.Point(((Affine.Point)this.pts.get(i)).x, roundToScale(y, -2)));
/* 367:    */     }
/* 368:486 */     int iLastArchJoint = this.pts.size() - 1;
/* 369:    */     
/* 370:488 */     this.mrs.add(new SketchMember((Affine.Point)this.pts.get(0), (Affine.Point)this.pts.get(iAbutmentArchJoints)));
/* 371:489 */     this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iAbutmentArchJoints), (Affine.Point)this.pts.get(iFirstArchJoint)));
/* 372:490 */     for (int i = iFirstArchJoint; i < iLastArchJoint; i++) {
/* 373:491 */       this.mrs.add(new SketchMember((Affine.Point)this.pts.get(i), (Affine.Point)this.pts.get(i + 1)));
/* 374:    */     }
/* 375:493 */     this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iLastArchJoint), (Affine.Point)this.pts.get(iAbutmentArchJoints + 1)));
/* 376:494 */     this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iAbutmentArchJoints + 1), (Affine.Point)this.pts.get(iLastDeckJoint)));
/* 377:    */     
/* 378:496 */     int iLeftTop = 1;
/* 379:497 */     int iRightTop = iLastDeckJoint - 1;
/* 380:498 */     int iLeftBottom = iFirstArchJoint;
/* 381:499 */     int iRightBottom = iLastArchJoint;
/* 382:500 */     while (iLeftTop < iRightTop)
/* 383:    */     {
/* 384:501 */       this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iLeftTop), (Affine.Point)this.pts.get(iLeftBottom)));
/* 385:502 */       this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iLeftTop - 1), (Affine.Point)this.pts.get(iLeftBottom)));
/* 386:503 */       this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iRightTop), (Affine.Point)this.pts.get(iRightBottom)));
/* 387:504 */       this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iRightTop + 1), (Affine.Point)this.pts.get(iRightBottom)));
/* 388:505 */       iLeftTop++;
/* 389:506 */       iRightTop--;
/* 390:507 */       iLeftBottom++;
/* 391:508 */       iRightBottom--;
/* 392:    */     }
/* 393:511 */     this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iLeftTop - 1), (Affine.Point)this.pts.get(iLeftBottom)));
/* 394:512 */     this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iRightTop + 1), (Affine.Point)this.pts.get(iRightBottom)));
/* 395:514 */     if (iLeftTop == iRightTop) {
/* 396:515 */       this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iLeftTop), (Affine.Point)this.pts.get(iLeftBottom)));
/* 397:    */     }
/* 398:517 */     closeSketch();
/* 399:518 */     return this;
/* 400:    */   }
/* 401:    */   
/* 402:    */   public BridgeSketchModel setHoweContArch(DesignConditions conditions)
/* 403:    */   {
/* 404:528 */     setNameFromResource("howeContArch.text");
/* 405:529 */     setPrescribedJoints(conditions);
/* 406:530 */     int iAbutmentArchJoints = conditions.getArchJointIndex();
/* 407:531 */     int iFirstDeckJoint = 0;
/* 408:532 */     int iLastDeckJoint = conditions.getNPanels();
/* 409:    */     
/* 410:534 */     Affine.Point p1 = (Affine.Point)this.pts.get(iAbutmentArchJoints);
/* 411:535 */     Affine.Point p2 = (Affine.Point)this.pts.get((0 + iLastDeckJoint) / 2);
/* 412:536 */     Affine.Point p3 = (Affine.Point)this.pts.get(iAbutmentArchJoints + 1);
/* 413:537 */     double xMid = 0.5D * (p1.x + p3.x);
/* 414:538 */     double x1 = p1.x - xMid;
/* 415:539 */     double y1 = p1.y;
/* 416:540 */     double x2 = p2.x - xMid;
/* 417:    */     
/* 418:542 */     double y2 = p2.y - 0.25D * (p2.y - p1.y);
/* 419:    */     
/* 420:544 */     double a = (y2 - y1) / (x2 * x2 - x1 * x1);
/* 421:545 */     double b = y1 - a * x1 * x1;
/* 422:    */     
/* 423:547 */     int iFirstArchJoint = this.pts.size();
/* 424:548 */     for (int i = 1; i < iLastDeckJoint; i++)
/* 425:    */     {
/* 426:549 */       double x = ((Affine.Point)this.pts.get(i)).x - xMid;
/* 427:550 */       double y = a * x * x + b;
/* 428:551 */       this.pts.add(new Affine.Point(((Affine.Point)this.pts.get(i)).x, roundToScale(y, -2)));
/* 429:    */     }
/* 430:553 */     int iLastArchJoint = this.pts.size() - 1;
/* 431:    */     
/* 432:555 */     this.mrs.add(new SketchMember((Affine.Point)this.pts.get(0), (Affine.Point)this.pts.get(iAbutmentArchJoints)));
/* 433:556 */     this.mrs.add(new SketchMember((Affine.Point)this.pts.get(1), (Affine.Point)this.pts.get(iAbutmentArchJoints)));
/* 434:557 */     this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iFirstArchJoint), (Affine.Point)this.pts.get(iAbutmentArchJoints)));
/* 435:558 */     for (int i = iFirstArchJoint; i < iLastArchJoint; i++) {
/* 436:559 */       this.mrs.add(new SketchMember((Affine.Point)this.pts.get(i), (Affine.Point)this.pts.get(i + 1)));
/* 437:    */     }
/* 438:561 */     this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iLastArchJoint), (Affine.Point)this.pts.get(iAbutmentArchJoints + 1)));
/* 439:562 */     this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iLastDeckJoint - 1), (Affine.Point)this.pts.get(iAbutmentArchJoints + 1)));
/* 440:563 */     this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iLastDeckJoint), (Affine.Point)this.pts.get(iAbutmentArchJoints + 1)));
/* 441:    */     
/* 442:565 */     int iLeftTop = 1;
/* 443:566 */     int iRightTop = iLastDeckJoint - 1;
/* 444:567 */     int iLeftBottom = iFirstArchJoint;
/* 445:568 */     int iRightBottom = iLastArchJoint;
/* 446:569 */     while (iLeftTop < iRightTop)
/* 447:    */     {
/* 448:570 */       this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iLeftTop), (Affine.Point)this.pts.get(iLeftBottom)));
/* 449:571 */       this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iLeftTop + 1), (Affine.Point)this.pts.get(iLeftBottom)));
/* 450:572 */       this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iRightTop - 1), (Affine.Point)this.pts.get(iRightBottom)));
/* 451:573 */       this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iRightTop), (Affine.Point)this.pts.get(iRightBottom)));
/* 452:574 */       iLeftTop++;
/* 453:575 */       iRightTop--;
/* 454:576 */       iLeftBottom++;
/* 455:577 */       iRightBottom--;
/* 456:    */     }
/* 457:580 */     if (iLeftTop == iRightTop) {
/* 458:581 */       this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iLeftTop), (Affine.Point)this.pts.get(iLeftBottom)));
/* 459:    */     }
/* 460:583 */     closeSketch();
/* 461:584 */     return this;
/* 462:    */   }
/* 463:    */   
/* 464:    */   public BridgeSketchModel setWarrenContArch(DesignConditions conditions)
/* 465:    */   {
/* 466:594 */     setNameFromResource("warrenContArch.text");
/* 467:595 */     setPrescribedJoints(conditions);
/* 468:596 */     int iAbutmentArchJoints = conditions.getArchJointIndex();
/* 469:597 */     int iFirstDeckJoint = 0;
/* 470:598 */     int iLastDeckJoint = conditions.getNPanels();
/* 471:    */     
/* 472:600 */     Affine.Point p1 = (Affine.Point)this.pts.get(iAbutmentArchJoints);
/* 473:601 */     Affine.Point p2 = (Affine.Point)this.pts.get((0 + iLastDeckJoint) / 2);
/* 474:602 */     Affine.Point p3 = (Affine.Point)this.pts.get(iAbutmentArchJoints + 1);
/* 475:603 */     double xMid = 0.5D * (p1.x + p3.x);
/* 476:604 */     double x1 = p1.x - xMid;
/* 477:605 */     double y1 = p1.y;
/* 478:606 */     double x2 = p2.x - xMid;
/* 479:    */     
/* 480:    */ 
/* 481:    */ 
/* 482:610 */     double y2 = p2.y - 0.25D * (p2.y - p1.y);
/* 483:    */     
/* 484:612 */     double a = (y2 - y1) / (x2 * x2 - x1 * x1);
/* 485:613 */     double b = y1 - a * x1 * x1;
/* 486:    */     
/* 487:615 */     int iFirstArchJoint = this.pts.size();
/* 488:616 */     for (int i = 0; i < iLastDeckJoint; i++)
/* 489:    */     {
/* 490:617 */       double x0 = ((Affine.Point)this.pts.get(i)).x + 2.0D;
/* 491:618 */       double x = x0 - xMid;
/* 492:619 */       double y = a * x * x + b;
/* 493:620 */       this.pts.add(new Affine.Point(x0, roundToScale(y, -2)));
/* 494:    */     }
/* 495:622 */     int iLastArchJoint = this.pts.size() - 1;
/* 496:    */     
/* 497:624 */     this.mrs.add(new SketchMember((Affine.Point)this.pts.get(0), (Affine.Point)this.pts.get(iAbutmentArchJoints)));
/* 498:625 */     this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iAbutmentArchJoints), (Affine.Point)this.pts.get(iFirstArchJoint)));
/* 499:626 */     for (int i = iFirstArchJoint; i < iLastArchJoint; i++) {
/* 500:627 */       this.mrs.add(new SketchMember((Affine.Point)this.pts.get(i), (Affine.Point)this.pts.get(i + 1)));
/* 501:    */     }
/* 502:629 */     this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iLastArchJoint), (Affine.Point)this.pts.get(iAbutmentArchJoints + 1)));
/* 503:630 */     this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iAbutmentArchJoints + 1), (Affine.Point)this.pts.get(iLastDeckJoint)));
/* 504:631 */     int iDeck = 0;
/* 505:632 */     int iArch = iFirstArchJoint;
/* 506:633 */     while (iDeck < iLastDeckJoint)
/* 507:    */     {
/* 508:634 */       this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iDeck), (Affine.Point)this.pts.get(iArch)));
/* 509:635 */       this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iDeck + 1), (Affine.Point)this.pts.get(iArch)));
/* 510:636 */       iDeck++;
/* 511:637 */       iArch++;
/* 512:    */     }
/* 513:639 */     closeSketch();
/* 514:640 */     return this;
/* 515:    */   }
/* 516:    */   
/* 517:    */   public BridgeSketchModel setPratt3HingeArch(DesignConditions conditions)
/* 518:    */   {
/* 519:650 */     setNameFromResource("pratt3HingeArch.text");
/* 520:651 */     setPrescribedJoints(conditions);
/* 521:652 */     int iAbutmentArchJoints = conditions.getArchJointIndex();
/* 522:653 */     int iFirstDeckJoint = 0;
/* 523:654 */     int iLastDeckJoint = conditions.getNPanels();
/* 524:    */     
/* 525:656 */     Affine.Point p1 = (Affine.Point)this.pts.get(iAbutmentArchJoints);
/* 526:657 */     Affine.Point p2 = (Affine.Point)this.pts.get((0 + iLastDeckJoint) / 2);
/* 527:658 */     Affine.Point p3 = (Affine.Point)this.pts.get(iAbutmentArchJoints + 1);
/* 528:659 */     double xMid = 0.5D * (p1.x + p3.x);
/* 529:660 */     double x1 = p1.x - xMid;
/* 530:661 */     double y1 = p1.y;
/* 531:662 */     double x2 = p2.x - xMid;
/* 532:    */     
/* 533:    */ 
/* 534:665 */     double y2 = p2.y - 0.25D * (p2.y - p1.y);
/* 535:    */     
/* 536:667 */     double a = (y2 - y1) / (x2 * x2 - x1 * x1);
/* 537:668 */     double b = y1 - a * x1 * x1;
/* 538:    */     
/* 539:670 */     int iFirstArchJoint = this.pts.size();
/* 540:671 */     int iArchJointLeftOfHinge = 0;
/* 541:672 */     int iHinge = (0 + iLastDeckJoint) / 2;
/* 542:673 */     for (int i = 1; i < iLastDeckJoint; i++) {
/* 543:674 */       if (i == iHinge)
/* 544:    */       {
/* 545:675 */         iArchJointLeftOfHinge = this.pts.size() - 1;
/* 546:    */       }
/* 547:    */       else
/* 548:    */       {
/* 549:677 */         double x = ((Affine.Point)this.pts.get(i)).x - xMid;
/* 550:678 */         double y = a * x * x + b;
/* 551:679 */         this.pts.add(new Affine.Point(((Affine.Point)this.pts.get(i)).x, roundToScale(y, -2)));
/* 552:    */       }
/* 553:    */     }
/* 554:682 */     int iLastArchJoint = this.pts.size() - 1;
/* 555:    */     
/* 556:684 */     this.mrs.add(new SketchMember((Affine.Point)this.pts.get(0), (Affine.Point)this.pts.get(iAbutmentArchJoints)));
/* 557:685 */     this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iAbutmentArchJoints), (Affine.Point)this.pts.get(iFirstArchJoint)));
/* 558:686 */     for (int i = iFirstArchJoint; i < iArchJointLeftOfHinge; i++) {
/* 559:687 */       this.mrs.add(new SketchMember((Affine.Point)this.pts.get(i), (Affine.Point)this.pts.get(i + 1)));
/* 560:    */     }
/* 561:689 */     this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iArchJointLeftOfHinge), (Affine.Point)this.pts.get(iHinge)));
/* 562:690 */     this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iHinge), (Affine.Point)this.pts.get(iArchJointLeftOfHinge + 1)));
/* 563:691 */     for (int i = iArchJointLeftOfHinge + 1; i < iLastArchJoint; i++) {
/* 564:692 */       this.mrs.add(new SketchMember((Affine.Point)this.pts.get(i), (Affine.Point)this.pts.get(i + 1)));
/* 565:    */     }
/* 566:694 */     this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iLastArchJoint), (Affine.Point)this.pts.get(iAbutmentArchJoints + 1)));
/* 567:695 */     this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iAbutmentArchJoints + 1), (Affine.Point)this.pts.get(iLastDeckJoint)));
/* 568:    */     
/* 569:697 */     int iLeftTop = 1;
/* 570:698 */     int iRightTop = iLastDeckJoint - 1;
/* 571:699 */     int iLeftBottom = iFirstArchJoint;
/* 572:700 */     int iRightBottom = iLastArchJoint;
/* 573:701 */     while (iLeftTop < iRightTop)
/* 574:    */     {
/* 575:702 */       this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iLeftTop), (Affine.Point)this.pts.get(iLeftBottom)));
/* 576:703 */       this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iLeftTop - 1), (Affine.Point)this.pts.get(iLeftBottom)));
/* 577:704 */       this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iRightTop), (Affine.Point)this.pts.get(iRightBottom)));
/* 578:705 */       this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iRightTop + 1), (Affine.Point)this.pts.get(iRightBottom)));
/* 579:706 */       iLeftTop++;
/* 580:707 */       iRightTop--;
/* 581:708 */       iLeftBottom++;
/* 582:709 */       iRightBottom--;
/* 583:    */     }
/* 584:711 */     closeSketch();
/* 585:712 */     return this;
/* 586:    */   }
/* 587:    */   
/* 588:    */   public BridgeSketchModel setHowe3HingeArch(DesignConditions conditions)
/* 589:    */   {
/* 590:722 */     setNameFromResource("howe3HingeArch.text");
/* 591:723 */     setPrescribedJoints(conditions);
/* 592:724 */     int iAbutmentArchJoints = conditions.getArchJointIndex();
/* 593:725 */     int iFirstDeckJoint = 0;
/* 594:726 */     int iLastDeckJoint = conditions.getNPanels();
/* 595:    */     
/* 596:728 */     Affine.Point p1 = (Affine.Point)this.pts.get(iAbutmentArchJoints);
/* 597:729 */     Affine.Point p2 = (Affine.Point)this.pts.get((0 + iLastDeckJoint) / 2);
/* 598:730 */     Affine.Point p3 = (Affine.Point)this.pts.get(iAbutmentArchJoints + 1);
/* 599:731 */     double xMid = 0.5D * (p1.x + p3.x);
/* 600:732 */     double x1 = p1.x - xMid;
/* 601:733 */     double y1 = p1.y;
/* 602:734 */     double x2 = p2.x - xMid;
/* 603:    */     
/* 604:736 */     double y2 = p2.y - 0.25D * (p2.y - p1.y);
/* 605:    */     
/* 606:738 */     double a = (y2 - y1) / (x2 * x2 - x1 * x1);
/* 607:739 */     double b = y1 - a * x1 * x1;
/* 608:    */     
/* 609:741 */     int iFirstArchJoint = this.pts.size();
/* 610:742 */     for (int i = 1; i < iLastDeckJoint; i++) {
/* 611:743 */       if (i != (0 + iLastDeckJoint) / 2)
/* 612:    */       {
/* 613:744 */         double x = ((Affine.Point)this.pts.get(i)).x - xMid;
/* 614:745 */         double y = a * x * x + b;
/* 615:746 */         this.pts.add(new Affine.Point(((Affine.Point)this.pts.get(i)).x, roundToScale(y, -2)));
/* 616:    */       }
/* 617:    */     }
/* 618:749 */     int iLastArchJoint = this.pts.size() - 1;
/* 619:    */     
/* 620:751 */     this.mrs.add(new SketchMember((Affine.Point)this.pts.get(0), (Affine.Point)this.pts.get(iAbutmentArchJoints)));
/* 621:752 */     this.mrs.add(new SketchMember((Affine.Point)this.pts.get(1), (Affine.Point)this.pts.get(iAbutmentArchJoints)));
/* 622:753 */     this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iFirstArchJoint), (Affine.Point)this.pts.get(iAbutmentArchJoints)));
/* 623:754 */     int iLeftArch = iFirstArchJoint + 1;
/* 624:755 */     int iRightArch = iLastArchJoint - 1;
/* 625:756 */     while (iLeftArch < iRightArch)
/* 626:    */     {
/* 627:757 */       this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iLeftArch - 1), (Affine.Point)this.pts.get(iLeftArch)));
/* 628:758 */       this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iRightArch + 1), (Affine.Point)this.pts.get(iRightArch)));
/* 629:759 */       iLeftArch++;
/* 630:760 */       iRightArch--;
/* 631:    */     }
/* 632:762 */     this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iLastArchJoint), (Affine.Point)this.pts.get(iAbutmentArchJoints + 1)));
/* 633:763 */     this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iLastDeckJoint - 1), (Affine.Point)this.pts.get(iAbutmentArchJoints + 1)));
/* 634:764 */     this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iLastDeckJoint), (Affine.Point)this.pts.get(iAbutmentArchJoints + 1)));
/* 635:    */     
/* 636:766 */     int iLeftTop = 1;
/* 637:767 */     int iRightTop = iLastDeckJoint - 1;
/* 638:768 */     int iLeftBottom = iFirstArchJoint;
/* 639:769 */     int iRightBottom = iLastArchJoint;
/* 640:770 */     while (iLeftTop < iRightTop)
/* 641:    */     {
/* 642:771 */       this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iLeftTop), (Affine.Point)this.pts.get(iLeftBottom)));
/* 643:772 */       this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iLeftTop + 1), (Affine.Point)this.pts.get(iLeftBottom)));
/* 644:773 */       this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iRightTop - 1), (Affine.Point)this.pts.get(iRightBottom)));
/* 645:774 */       this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iRightTop), (Affine.Point)this.pts.get(iRightBottom)));
/* 646:775 */       iLeftTop++;
/* 647:776 */       iRightTop--;
/* 648:777 */       iLeftBottom++;
/* 649:778 */       iRightBottom--;
/* 650:    */     }
/* 651:780 */     closeSketch();
/* 652:781 */     return this;
/* 653:    */   }
/* 654:    */   
/* 655:    */   public BridgeSketchModel setWarren3HingeArch(DesignConditions conditions)
/* 656:    */   {
/* 657:791 */     setNameFromResource("warren3HingeArch.text");
/* 658:792 */     setPrescribedJoints(conditions);
/* 659:793 */     int iAbutmentArchJoints = conditions.getArchJointIndex();
/* 660:794 */     int iFirstDeckJoint = 0;
/* 661:795 */     int iLastDeckJoint = conditions.getNPanels();
/* 662:    */     
/* 663:797 */     Affine.Point p1 = (Affine.Point)this.pts.get(iAbutmentArchJoints);
/* 664:798 */     Affine.Point p2 = (Affine.Point)this.pts.get((0 + iLastDeckJoint) / 2);
/* 665:799 */     Affine.Point p3 = (Affine.Point)this.pts.get(iAbutmentArchJoints + 1);
/* 666:800 */     double xMid = 0.5D * (p1.x + p3.x);
/* 667:801 */     double x1 = p1.x - xMid;
/* 668:802 */     double y1 = p1.y;
/* 669:803 */     double x2 = p2.x - xMid;
/* 670:    */     
/* 671:    */ 
/* 672:    */ 
/* 673:807 */     double y2 = p2.y - 0.25D * (p2.y - p1.y);
/* 674:    */     
/* 675:809 */     double a = (y2 - y1) / (x2 * x2 - x1 * x1);
/* 676:810 */     double b = y1 - a * x1 * x1;
/* 677:    */     
/* 678:812 */     int iFirstArchJoint = this.pts.size();
/* 679:813 */     for (int i = 0; i < iLastDeckJoint; i++)
/* 680:    */     {
/* 681:814 */       double x0 = ((Affine.Point)this.pts.get(i)).x + 2.0D;
/* 682:815 */       double x = x0 - xMid;
/* 683:816 */       double y = a * x * x + b;
/* 684:817 */       this.pts.add(new Affine.Point(x0, roundToScale(y, -2)));
/* 685:    */     }
/* 686:819 */     int iLastArchJoint = this.pts.size() - 1;
/* 687:    */     
/* 688:821 */     this.mrs.add(new SketchMember((Affine.Point)this.pts.get(0), (Affine.Point)this.pts.get(iAbutmentArchJoints)));
/* 689:822 */     this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iAbutmentArchJoints), (Affine.Point)this.pts.get(iFirstArchJoint)));
/* 690:823 */     for (int i = iFirstArchJoint; i < iLastArchJoint; i++) {
/* 691:825 */       if (i != (iFirstArchJoint + iLastArchJoint) / 2) {
/* 692:826 */         this.mrs.add(new SketchMember((Affine.Point)this.pts.get(i), (Affine.Point)this.pts.get(i + 1)));
/* 693:    */       }
/* 694:    */     }
/* 695:829 */     this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iLastArchJoint), (Affine.Point)this.pts.get(iAbutmentArchJoints + 1)));
/* 696:830 */     this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iAbutmentArchJoints + 1), (Affine.Point)this.pts.get(iLastDeckJoint)));
/* 697:831 */     int iDeck = 0;
/* 698:832 */     int iArch = iFirstArchJoint;
/* 699:833 */     while (iDeck < iLastDeckJoint)
/* 700:    */     {
/* 701:834 */       this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iDeck), (Affine.Point)this.pts.get(iArch)));
/* 702:835 */       this.mrs.add(new SketchMember((Affine.Point)this.pts.get(iDeck + 1), (Affine.Point)this.pts.get(iArch)));
/* 703:836 */       iDeck++;
/* 704:837 */       iArch++;
/* 705:    */     }
/* 706:839 */     closeSketch();
/* 707:840 */     return this;
/* 708:    */   }
/* 709:    */   
/* 710:    */   public BridgeSketchModel setFromTemplate(String name, String template)
/* 711:    */   {
/* 712:851 */     this.name = name;
/* 713:852 */     String[] tokens = template.split("\\|");
/* 714:853 */     int i = 0;
/* 715:    */     
/* 716:855 */     this.conditions = DesignConditions.getDesignConditions(tokens[(i++)]);
/* 717:    */     
/* 718:857 */     int nJoints = Integer.parseInt(tokens[(i++)]);
/* 719:858 */     this.jointLocations = new Affine.Point[nJoints];
/* 720:859 */     int nMembers = Integer.parseInt(tokens[(i++)]);
/* 721:860 */     this.memberLocations = new SketchMember[nMembers];
/* 722:861 */     for (int j = 0; j < nJoints; j++)
/* 723:    */     {
/* 724:862 */       double x = Double.parseDouble(tokens[(i++)]);
/* 725:863 */       double y = Double.parseDouble(tokens[(i++)]);
/* 726:864 */       this.jointLocations[j] = new Affine.Point(x, y);
/* 727:    */     }
/* 728:866 */     for (int j = 0; j < nMembers; j++)
/* 729:    */     {
/* 730:868 */       int ia = Integer.parseInt(tokens[(i++)]) - 1;
/* 731:869 */       int ib = Integer.parseInt(tokens[(i++)]) - 1;
/* 732:870 */       this.memberLocations[j] = new SketchMember(this.jointLocations[ia], this.jointLocations[ib]);
/* 733:    */     }
/* 734:872 */     return this;
/* 735:    */   }
/* 736:    */   
/* 737:878 */   private static final HashMap<String, Object[]> sketchModelListCache = new HashMap();
/* 738:    */   
/* 739:    */   public static Object[] getList(DesignConditions conditions)
/* 740:    */   {
/* 741:891 */     if (conditions == null) {
/* 742:892 */       return new Object[0];
/* 743:    */     }
/* 744:894 */     String conditionsTagNumber = conditions.getTag().substring(0, 2);
/* 745:895 */     Object[] rtn = (Object[])sketchModelListCache.get(conditionsTagNumber);
/* 746:896 */     if (rtn == null)
/* 747:    */     {
/* 748:898 */       ArrayList<Object> list = new ArrayList();
/* 749:    */       
/* 750:900 */       list.add(WPBDApp.getResourceMap(BridgeSketchModel.class).getString("noTemplate.text", new Object[0]));
/* 751:902 */       if (conditions.isArch())
/* 752:    */       {
/* 753:903 */         if (conditions.getUnderClearance() <= 16.0D)
/* 754:    */         {
/* 755:904 */           list.add(new BridgeSketchModel().setPrattContArch(conditions));
/* 756:905 */           list.add(new BridgeSketchModel().setHoweContArch(conditions));
/* 757:906 */           list.add(new BridgeSketchModel().setWarrenContArch(conditions));
/* 758:908 */           if (conditions.getNLoadedJoints() % 2 == 1)
/* 759:    */           {
/* 760:909 */             list.add(new BridgeSketchModel().setPratt3HingeArch(conditions));
/* 761:910 */             list.add(new BridgeSketchModel().setHowe3HingeArch(conditions));
/* 762:911 */             list.add(new BridgeSketchModel().setWarren3HingeArch(conditions));
/* 763:    */           }
/* 764:    */         }
/* 765:    */       }
/* 766:914 */       else if ((conditions.getNAnchorages() == 0) && (!conditions.isPier()))
/* 767:    */       {
/* 768:915 */         if (conditions.getOverClearance() >= 4.0D)
/* 769:    */         {
/* 770:916 */           list.add(new BridgeSketchModel().setHoweThruTruss(conditions));
/* 771:917 */           list.add(new BridgeSketchModel().setPrattThruTruss(conditions));
/* 772:918 */           list.add(new BridgeSketchModel().setWarrenThruTruss(conditions));
/* 773:    */         }
/* 774:920 */         if (conditions.getUnderClearance() >= 4.0D)
/* 775:    */         {
/* 776:921 */           list.add(new BridgeSketchModel().setHoweDeckTruss(conditions));
/* 777:922 */           list.add(new BridgeSketchModel().setPrattDeckTruss(conditions));
/* 778:923 */           list.add(new BridgeSketchModel().setWarrenDeckTruss(conditions));
/* 779:    */         }
/* 780:    */       }
/* 781:925 */       else if ((conditions.getNAnchorages() > 0) && (!conditions.isPier()) && (conditions.getUnderClearance() >= 2.0D))
/* 782:    */       {
/* 783:927 */         list.add(new BridgeSketchModel().setCableStayedWarrenTruss(conditions));
/* 784:928 */         list.add(new BridgeSketchModel().setSuspendedWarrenTruss(conditions));
/* 785:    */       }
/* 786:930 */       ResourceMap resourceMap = WPBDApp.getResourceMap(BridgeSketchModel.class);
/* 787:931 */       Iterator<String> i = resourceMap.keySet().iterator();
/* 788:932 */       while (i.hasNext())
/* 789:    */       {
/* 790:933 */         String nameKey = (String)i.next();
/* 791:934 */         if ((nameKey.endsWith(".bridgeSketchName")) && (conditionsTagNumber.equals(nameKey.substring(0, 2))))
/* 792:    */         {
/* 793:935 */           String sketchKey = nameKey.substring(0, nameKey.lastIndexOf('.')).concat(".bridgeSketch");
/* 794:936 */           list.add(new BridgeSketchModel().setFromTemplate(resourceMap.getString(nameKey, new Object[0]), resourceMap.getString(sketchKey, new Object[0])));
/* 795:    */         }
/* 796:    */       }
/* 797:940 */       rtn = list.toArray();
/* 798:941 */       sketchModelListCache.put(conditionsTagNumber, rtn);
/* 799:    */     }
/* 800:943 */     return rtn;
/* 801:    */   }
/* 802:    */   
/* 803:    */   public String toString()
/* 804:    */   {
/* 805:952 */     return this.name;
/* 806:    */   }
/* 807:    */   
/* 808:    */   public static void main(String[] argv)
/* 809:    */   {
/* 810:961 */     for (int i = 0; i < DesignConditions.conditions.length; i++)
/* 811:    */     {
/* 812:962 */       Object[] list = getList(DesignConditions.conditions[i]);
/* 813:963 */       if (list.length <= 1) {
/* 814:964 */         System.out.println(DesignConditions.conditions[i].getTag() + ": " + list.length);
/* 815:    */       }
/* 816:    */     }
/* 817:967 */     System.out.println();
/* 818:    */   }
/* 819:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.BridgeSketchModel
 * JD-Core Version:    0.7.0.1
 */