/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.awt.BasicStroke;
/*   4:    */ import java.awt.Color;
/*   5:    */ import java.awt.Graphics2D;
/*   6:    */ import java.awt.Point;
/*   7:    */ import java.awt.Stroke;
/*   8:    */ import java.util.ArrayList;
/*   9:    */ import java.util.Iterator;
/*  10:    */ 
/*  11:    */ public class Bridge3dView
/*  12:    */   extends BridgeView
/*  13:    */ {
/*  14:    */   private final FixedEyeTerrainModel terrain;
/*  15:    */   private final FixedEyeAnimation.Config config;
/*  16: 22 */   private final Point[] jointViewportCoordsFront = new Point[100];
/*  17: 23 */   private final Point[] jointViewportCoordsRear = new Point[100];
/*  18: 24 */   private int jointRadius = 3;
/*  19:    */   private Stroke crossMemberStroke;
/*  20: 26 */   private int deckThickness = 12;
/*  21: 27 */   private int deckBeamHeight = 12;
/*  22: 28 */   public static final Color darkRed = new Color(92, 0, 0);
/*  23: 29 */   public static final Color darkBlue = new Color(0, 0, 92);
/*  24: 30 */   public static final Color gray00 = Color.BLACK;
/*  25: 31 */   public static final Color gray25 = Color.DARK_GRAY;
/*  26: 32 */   public static final Color gray30 = new Color(80, 80, 80);
/*  27: 33 */   public static final Color gray40 = new Color(95, 95, 95);
/*  28: 34 */   public static final Color gray50 = Color.GRAY;
/*  29: 35 */   public static final Color gray75 = Color.LIGHT_GRAY;
/*  30: 36 */   public static final Color gray99 = Color.WHITE;
/*  31: 37 */   private final int[] vpXmember = new int[4];
/*  32: 38 */   private final int[] vpYmember = new int[4];
/*  33:    */   private static final float deckHalfWidth = 5.0F;
/*  34: 40 */   private static final PierModel pierModel = new PierModel();
/*  35: 41 */   private final FixedEyeTruckModel truck = new FixedEyeTruckModel();
/*  36:    */   
/*  37:    */   Bridge3dView(BridgeModel bridge, FixedEyeTerrainModel terrain, FixedEyeAnimation.Config config)
/*  38:    */   {
/*  39: 44 */     this.bridge = bridge;
/*  40: 45 */     this.terrain = terrain;
/*  41: 46 */     this.config = config;
/*  42: 47 */     for (int i = 0; i < this.jointViewportCoordsFront.length; i++)
/*  43:    */     {
/*  44: 48 */       this.jointViewportCoordsFront[i] = new Point();
/*  45: 49 */       this.jointViewportCoordsRear[i] = new Point();
/*  46:    */     }
/*  47:    */   }
/*  48:    */   
/*  49:    */   public void initialize(DesignConditions conditions)
/*  50:    */   {
/*  51: 55 */     super.initialize(conditions);
/*  52: 56 */     if (conditions.isPier()) {
/*  53: 57 */       pierModel.initialize((float)conditions.getPierHeight(), 4.8F);
/*  54:    */     }
/*  55:    */   }
/*  56:    */   
/*  57:    */   protected void loadPreferredDrawingWindow()
/*  58:    */   {
/*  59: 63 */     loadStandardDraftingWindow();
/*  60:    */   }
/*  61:    */   
/*  62:    */   private Color interpolatedColor(double forceRatio)
/*  63:    */   {
/*  64: 68 */     float f = Utility.clamp((float)forceRatio, -1.0F, 1.0F);
/*  65:    */     int g;
/*  66:    */     int g;
/*  67:    */     int r;
/*  68:    */     int b;
/*  69: 69 */     if (f < 0.0F)
/*  70:    */     {
/*  71: 70 */       int r = 255;
/*  72:    */       int b;
/*  73: 71 */       g = b = (int)(255.0F * (1.0F + f));
/*  74:    */     }
/*  75:    */     else
/*  76:    */     {
/*  77: 74 */       r = g = (int)(255.0F * (1.0F - f));
/*  78: 75 */       b = 255;
/*  79:    */     }
/*  80: 77 */     return new Color(r, g, b);
/*  81:    */   }
/*  82:    */   
/*  83:    */   private void xformJoints(ViewportTransform viewportTransform, Analysis.Interpolation interpolation, float z, Point[] ptBuf)
/*  84:    */   {
/*  85: 84 */     Iterator<Joint> je = this.bridge.getJoints().iterator();
/*  86: 85 */     while (je.hasNext())
/*  87:    */     {
/*  88: 86 */       Joint joint = (Joint)je.next();
/*  89: 87 */       int jointIndex = joint.getIndex();
/*  90: 88 */       Affine.Vector disp = interpolation.getDisplacement(jointIndex);
/*  91: 89 */       Affine.Point ptWorld = joint.getPointWorld();
/*  92: 90 */       viewportTransform.worldToViewport(ptBuf[jointIndex], ptWorld.x + disp.x, ptWorld.y + disp.y, z);
/*  93:    */     }
/*  94:    */   }
/*  95:    */   
/*  96:    */   private void paintMembers(Graphics2D g, ViewportTransform viewportTransform, float z, Analysis.Interpolation interpolation, Point[] ptBuf, Color color)
/*  97:    */   {
/*  98: 97 */     Iterator<Member> me = this.bridge.getMembers().iterator();
/*  99: 98 */     while (me.hasNext())
/* 100:    */     {
/* 101: 99 */       Member member = (Member)me.next();
/* 102:100 */       int width = Math.max(6, viewportTransform.worldToViewportDistance(2.0F * member.getWidthInMeters()));
/* 103:101 */       int memberIndex = member.getIndex();
/* 104:102 */       double status = interpolation.getMemberStatus(memberIndex);
/* 105:103 */       double forceRatio = interpolation.getForceRatio(memberIndex);
/* 106:104 */       if (!Analysis.isStatusBaseLength(status))
/* 107:    */       {
/* 108:105 */         paintMember(g, ptBuf[member.getJointA().getIndex()], ptBuf[member.getJointB().getIndex()], width, color == null ? interpolatedColor(forceRatio) : color, color == null ? darkBlue : forceRatio < 0.0D ? darkRed : gray25, false);
/* 109:    */       }
/* 110:112 */       else if (forceRatio < 0.0D)
/* 111:    */       {
/* 112:114 */         paintParabola(g, viewportTransform, interpolation, z, member.getJointA(), member.getJointB(), width, (float)status, color == null ? Color.RED : color, color == null ? darkRed : gray25);
/* 113:    */       }
/* 114:    */       else
/* 115:    */       {
/* 116:122 */         Color mainColor = color == null ? Color.BLUE : color;
/* 117:123 */         Color ruleColor = color == null ? darkBlue : gray25;
/* 118:124 */         Joint ja = member.getJointA();
/* 119:125 */         Joint jb = member.getJointB();
/* 120:126 */         Affine.Point pa = ja.getPointWorld();
/* 121:127 */         Affine.Point pb = jb.getPointWorld();
/* 122:    */         
/* 123:    */ 
/* 124:130 */         viewportTransform.worldToViewport(this.vpBreakA, pa.x, pa.y, z);
/* 125:131 */         viewportTransform.worldToViewport(this.vpBreakB, pb.x, pb.y, z);
/* 126:132 */         Point a = ptBuf[ja.getIndex()];
/* 127:133 */         Point b = ptBuf[jb.getIndex()];
/* 128:    */         double tBreak;
/* 129:    */         try
/* 130:    */         {
/* 131:136 */           tBreak = 0.5D * this.vpBreakA.distance(this.vpBreakB) / a.distance(b);
/* 132:    */         }
/* 133:    */         catch (Exception e)
/* 134:    */         {
/* 135:139 */           tBreak = 0.5D;
/* 136:    */         }
/* 137:141 */         this.vpBreakA.x = ((int)(0.5D + a.x + tBreak * (b.x - a.x)));
/* 138:142 */         this.vpBreakA.y = ((int)(0.5D + a.y + tBreak * (b.y - a.y)));
/* 139:143 */         paintMember(g, a, this.vpBreakA, width, mainColor, ruleColor, true);
/* 140:144 */         this.vpBreakB.x = ((int)(0.5D + b.x + tBreak * (a.x - b.x)));
/* 141:145 */         this.vpBreakB.y = ((int)(0.5D + b.y + tBreak * (a.y - b.y)));
/* 142:146 */         paintMember(g, b, this.vpBreakB, width, mainColor, ruleColor, true);
/* 143:    */       }
/* 144:    */     }
/* 145:    */   }
/* 146:    */   
/* 147:    */   private void paintMember(Graphics2D g, Point a, Point b, int width, Color color, Color ruleColor, boolean broken)
/* 148:    */   {
/* 149:164 */     float dx = b.x - a.x;
/* 150:165 */     float dy = b.y - a.y;
/* 151:    */     float halfWidthOverLength;
/* 152:    */     try
/* 153:    */     {
/* 154:168 */       halfWidthOverLength = 0.5F * width / (float)Math.sqrt(dx * dx + dy * dy);
/* 155:    */     }
/* 156:    */     catch (Exception e)
/* 157:    */     {
/* 158:170 */       return;
/* 159:    */     }
/* 160:173 */     int tx = (int)(dx * halfWidthOverLength + 0.5F);
/* 161:174 */     int ty = (int)(dy * halfWidthOverLength + 0.5F);
/* 162:175 */     this.vpXmember[0] = (a.x + ty);
/* 163:176 */     this.vpYmember[0] = (a.y - tx);
/* 164:177 */     this.vpXmember[1] = (b.x + ty);
/* 165:178 */     this.vpYmember[1] = (b.y - tx);
/* 166:179 */     this.vpXmember[2] = (b.x - ty);
/* 167:180 */     this.vpYmember[2] = (b.y + tx);
/* 168:181 */     this.vpXmember[3] = (a.x - ty);
/* 169:182 */     this.vpYmember[3] = (a.y + tx);
/* 170:183 */     if (broken)
/* 171:    */     {
/* 172:184 */       this.vpXmember[1] += tx;
/* 173:185 */       this.vpYmember[1] += ty;
/* 174:186 */       this.vpXmember[2] -= tx;
/* 175:187 */       this.vpYmember[2] -= ty;
/* 176:    */     }
/* 177:189 */     g.setPaint(color);
/* 178:190 */     g.fillPolygon(this.vpXmember, this.vpYmember, 4);
/* 179:191 */     g.setPaint(ruleColor);
/* 180:192 */     g.drawPolygon(this.vpXmember, this.vpYmember, 4);
/* 181:    */   }
/* 182:    */   
/* 183:197 */   private final float[] pts = new float[Math.max(16, 34)];
/* 184:198 */   private final float[] normals = new float[this.pts.length];
/* 185:199 */   private final int[] vpXfailedMember = new int[this.pts.length];
/* 186:200 */   private final int[] vpYfailedMember = new int[this.pts.length];
/* 187:201 */   private final double[] clx = new double[1];
/* 188:202 */   private final double[] cly = new double[1];
/* 189:203 */   private final Point vpBreakA = new Point();
/* 190:204 */   private final Point vpBreakB = new Point();
/* 191:    */   
/* 192:    */   private void paintParabola(Graphics2D g, ViewportTransform viewportTransform, Analysis.Interpolation interpolation, double z, Joint a, Joint b, int width, float arcLen, Color color, Color ruleColor)
/* 193:    */   {
/* 194:211 */     Affine.Point pa = a.getPointWorld();
/* 195:212 */     Affine.Point pb = b.getPointWorld();
/* 196:213 */     Affine.Vector da = interpolation.getDisplacement(a.getIndex());
/* 197:214 */     Affine.Vector db = interpolation.getDisplacement(b.getIndex());
/* 198:215 */     float xa = (float)(pa.x + da.x);
/* 199:216 */     float ya = (float)(pa.y + da.y);
/* 200:217 */     float xb = (float)(pb.x + db.x);
/* 201:218 */     float yb = (float)(pb.y + db.y);
/* 202:219 */     float dx = xb - xa;
/* 203:220 */     float dy = yb - ya;
/* 204:221 */     float len = (float)Math.sqrt(dx * dx + dy * dy);
/* 205:222 */     Member.makeParabola(this.pts, this.normals, len, Member.getParabolaHeight(len, arcLen));
/* 206:223 */     float cos = dx / len;
/* 207:224 */     float sin = dy / len;
/* 208:225 */     for (int i2 = 0; i2 < this.pts.length; i2 += 2)
/* 209:    */     {
/* 210:226 */       float x = cos * this.pts[(i2 + 0)] - sin * this.pts[(i2 + 1)];
/* 211:227 */       float y = cos * this.pts[(i2 + 1)] + sin * this.pts[(i2 + 0)];
/* 212:228 */       this.pts[(i2 + 0)] = (x + xa);
/* 213:229 */       this.pts[(i2 + 1)] = (y + ya);
/* 214:230 */       x = cos * this.normals[(i2 + 0)] - sin * this.normals[(i2 + 1)];
/* 215:231 */       y = cos * this.normals[(i2 + 1)] + sin * this.normals[(i2 + 0)];
/* 216:232 */       this.normals[(i2 + 0)] = x;
/* 217:233 */       this.normals[(i2 + 1)] = y;
/* 218:    */     }
/* 219:235 */     int i0 = 0;
/* 220:236 */     int i1 = this.vpXfailedMember.length - 1;
/* 221:237 */     for (int i2 = 0; i2 < this.pts.length; i1--)
/* 222:    */     {
/* 223:238 */       viewportTransform.worldToViewport(this.clx, this.cly, 0, this.pts[(i2 + 0)], this.pts[(i2 + 1)], z);
/* 224:239 */       float px = (float)this.clx[0];
/* 225:240 */       float py = (float)this.cly[0];
/* 226:    */       
/* 227:242 */       float nx = this.normals[(i2 + 0)] * 0.5F * width;
/* 228:243 */       float ny = this.normals[(i2 + 1)] * -0.5F * width;
/* 229:244 */       this.vpXfailedMember[i0] = ((int)(0.5F + px + nx));
/* 230:245 */       this.vpYfailedMember[i0] = ((int)(0.5F + py + ny));
/* 231:246 */       this.vpXfailedMember[i1] = ((int)(0.5F + px - nx));
/* 232:247 */       this.vpYfailedMember[i1] = ((int)(0.5F + py - ny));i2 += 2;i0++;
/* 233:    */     }
/* 234:249 */     g.setPaint(color);
/* 235:250 */     g.fillPolygon(this.vpXfailedMember, this.vpYfailedMember, this.vpXfailedMember.length);
/* 236:251 */     g.setPaint(ruleColor);
/* 237:252 */     g.drawPolygon(this.vpXfailedMember, this.vpYfailedMember, this.vpXfailedMember.length);
/* 238:    */   }
/* 239:    */   
/* 240:    */   private void paintJoint(Graphics2D g, Point p)
/* 241:    */   {
/* 242:256 */     g.setPaint(Color.LIGHT_GRAY);
/* 243:257 */     int d = 2 * this.jointRadius;
/* 244:258 */     g.fillOval(p.x - this.jointRadius, p.y - this.jointRadius, d, d);
/* 245:259 */     g.setPaint(Color.BLACK);
/* 246:260 */     g.drawOval(p.x - this.jointRadius, p.y - this.jointRadius, d, d);
/* 247:    */   }
/* 248:    */   
/* 249:263 */   private static final Stroke deckBeamStroke = new BasicStroke(2.5F, 2, 2);
/* 250:    */   
/* 251:    */   private void drawDeckBeam(Graphics2D g, Point a, Point b)
/* 252:    */   {
/* 253:266 */     this.vpXmember[0] = a.x;
/* 254:267 */     this.vpYmember[0] = a.y;
/* 255:268 */     this.vpXmember[1] = b.x;
/* 256:269 */     this.vpYmember[1] = b.y;
/* 257:270 */     this.vpXmember[2] = b.x;
/* 258:271 */     this.vpYmember[2] = (b.y - this.deckBeamHeight);
/* 259:272 */     this.vpXmember[3] = a.x;
/* 260:273 */     this.vpYmember[3] = (a.y - this.deckBeamHeight);
/* 261:274 */     g.setPaint(gray30);
/* 262:275 */     g.fillPolygon(this.vpXmember, this.vpYmember, 4);
/* 263:276 */     Stroke savedStroke = g.getStroke();
/* 264:277 */     g.setStroke(deckBeamStroke);
/* 265:278 */     g.setPaint(gray00);
/* 266:279 */     g.drawPolygon(this.vpXmember, this.vpYmember, 4);
/* 267:280 */     g.setStroke(savedStroke);
/* 268:    */   }
/* 269:    */   
/* 270:    */   private void paintCrossMember(Graphics2D g, Point a, Point b)
/* 271:    */   {
/* 272:284 */     Stroke savedStroke = g.getStroke();
/* 273:285 */     g.setStroke(this.crossMemberStroke);
/* 274:286 */     g.setPaint(gray40);
/* 275:287 */     g.drawLine(a.x, a.y, b.x, b.y);
/* 276:288 */     g.setStroke(savedStroke);
/* 277:    */   }
/* 278:    */   
/* 279:    */   private void paintDeckSurface(Graphics2D g, Color color, int dy)
/* 280:    */   {
/* 281:292 */     g.setPaint(color);
/* 282:293 */     this.vpXmember[0] = this.jointViewportCoordsFront[0].x;
/* 283:294 */     this.vpYmember[0] = (this.jointViewportCoordsFront[0].y - dy);
/* 284:295 */     this.vpXmember[1] = this.jointViewportCoordsRear[0].x;
/* 285:296 */     this.vpYmember[1] = (this.jointViewportCoordsRear[0].y - dy);
/* 286:297 */     int nPanels = this.conditions.getNPanels();
/* 287:298 */     for (int i = 0; i < nPanels; i++)
/* 288:    */     {
/* 289:299 */       this.vpXmember[2] = this.jointViewportCoordsRear[(i + 1)].x;
/* 290:300 */       this.vpYmember[2] = (this.jointViewportCoordsRear[(i + 1)].y - dy);
/* 291:301 */       this.vpXmember[3] = this.jointViewportCoordsFront[(i + 1)].x;
/* 292:302 */       this.vpYmember[3] = (this.jointViewportCoordsFront[(i + 1)].y - dy);
/* 293:303 */       g.fillPolygon(this.vpXmember, this.vpYmember, 4);
/* 294:304 */       this.vpXmember[1] = this.vpXmember[2];
/* 295:305 */       this.vpYmember[1] = this.vpYmember[2];
/* 296:306 */       this.vpXmember[0] = this.vpXmember[3];
/* 297:307 */       this.vpYmember[0] = this.vpYmember[3];
/* 298:    */     }
/* 299:    */   }
/* 300:    */   
/* 301:    */   private void paintDeckEdge(Graphics2D g)
/* 302:    */   {
/* 303:312 */     g.setPaint(gray75);
/* 304:313 */     int nPanels = this.conditions.getNPanels();
/* 305:314 */     this.vpXmember[0] = this.jointViewportCoordsFront[0].x;
/* 306:315 */     this.vpYmember[0] = (this.jointViewportCoordsFront[0].y - this.deckBeamHeight);
/* 307:316 */     this.vpXmember[1] = this.jointViewportCoordsFront[0].x;
/* 308:317 */     this.vpYmember[1] = (this.jointViewportCoordsFront[0].y - this.deckBeamHeight - this.deckThickness);
/* 309:318 */     for (int i = 0; i < nPanels; i++)
/* 310:    */     {
/* 311:319 */       this.vpXmember[2] = this.jointViewportCoordsFront[(i + 1)].x;
/* 312:320 */       this.vpYmember[2] = (this.jointViewportCoordsFront[(i + 1)].y - this.deckBeamHeight - this.deckThickness);
/* 313:321 */       this.vpXmember[3] = this.jointViewportCoordsFront[(i + 1)].x;
/* 314:322 */       this.vpYmember[3] = (this.jointViewportCoordsFront[(i + 1)].y - this.deckBeamHeight);
/* 315:323 */       g.fillPolygon(this.vpXmember, this.vpYmember, 4);
/* 316:324 */       this.vpXmember[1] = this.vpXmember[2];
/* 317:325 */       this.vpYmember[1] = this.vpYmember[2];
/* 318:326 */       this.vpXmember[0] = this.vpXmember[3];
/* 319:327 */       this.vpYmember[0] = this.vpYmember[3];
/* 320:    */     }
/* 321:    */   }
/* 322:    */   
/* 323:    */   private void paintDiagonalCrossMembers(Graphics2D g)
/* 324:    */   {
/* 325:332 */     g.setPaint(Color.DARK_GRAY);
/* 326:333 */     Iterator<Member> me = this.bridge.getMembers().iterator();
/* 327:334 */     while (me.hasNext())
/* 328:    */     {
/* 329:335 */       Member member = (Member)me.next();
/* 330:336 */       Joint a = member.getJointA();
/* 331:337 */       Joint b = member.getJointB();
/* 332:338 */       Affine.Point aPtWorld = a.getPointWorld();
/* 333:339 */       Affine.Point bPtWorld = b.getPointWorld();
/* 334:341 */       if (((aPtWorld.y >= 5.0D) && (bPtWorld.y >= 5.0D)) || ((aPtWorld.y <= 0.0D) && (bPtWorld.y <= 0.0D)))
/* 335:    */       {
/* 336:344 */         Point af = this.jointViewportCoordsFront[a.getIndex()];
/* 337:345 */         Point bf = this.jointViewportCoordsFront[b.getIndex()];
/* 338:346 */         Point ar = this.jointViewportCoordsRear[a.getIndex()];
/* 339:347 */         Point br = this.jointViewportCoordsRear[b.getIndex()];
/* 340:348 */         g.drawLine(ar.x, ar.y, bf.x, bf.y);
/* 341:349 */         g.drawLine(br.x, br.y, af.x, af.y);
/* 342:    */       }
/* 343:    */     }
/* 344:    */   }
/* 345:    */   
/* 346:    */   private void paintTransverseMembers(Graphics2D g)
/* 347:    */   {
/* 348:355 */     Iterator<Joint> je = this.bridge.getJoints().iterator();
/* 349:356 */     int nLoadedJoints = this.conditions.getNLoadedJoints();
/* 350:357 */     int nPrescribedJoints = this.conditions.getNPrescribedJoints();
/* 351:358 */     while (je.hasNext())
/* 352:    */     {
/* 353:359 */       Joint joint = (Joint)je.next();
/* 354:360 */       int i = joint.getIndex();
/* 355:361 */       if (i < nLoadedJoints)
/* 356:    */       {
/* 357:362 */         drawDeckBeam(g, this.jointViewportCoordsFront[i], this.jointViewportCoordsRear[i]);
/* 358:    */       }
/* 359:    */       else
/* 360:    */       {
/* 361:365 */         double y = joint.getPointWorld().y;
/* 362:366 */         if ((i >= nPrescribedJoints) && ((y < 0.0D) || (y > 5.0D))) {
/* 363:367 */           paintCrossMember(g, this.jointViewportCoordsFront[i], this.jointViewportCoordsRear[i]);
/* 364:    */         }
/* 365:    */       }
/* 366:    */     }
/* 367:    */   }
/* 368:    */   
/* 369:    */   protected void paint(Graphics2D g, ViewportTransform viewportTransform) {}
/* 370:    */   
/* 371:    */   protected void paint(Graphics2D g, ViewportTransform viewportTransform, Analysis.Interpolation interpolation, double distanceTraveled)
/* 372:    */   {
/* 373:388 */     this.jointRadius = viewportTransform.worldToViewportDistance(0.2D);
/* 374:389 */     this.crossMemberStroke = new BasicStroke(2 * this.jointRadius - 2, 1, 0);
/* 375:    */     
/* 376:391 */     this.deckThickness = viewportTransform.worldToViewportDistance(this.conditions.getDeckThickness());
/* 377:392 */     this.deckBeamHeight = (viewportTransform.worldToViewportDistance(0.8D) - this.deckThickness);
/* 378:393 */     boolean lowView = viewportTransform.isAboveVanishingPoint(((Joint)this.bridge.getJoints().get(0)).getPointWorld().y);
/* 379:394 */     int nJoints = this.bridge.getJoints().size();
/* 380:    */     
/* 381:396 */     xformJoints(viewportTransform, interpolation, -5.0F, this.jointViewportCoordsRear);
/* 382:397 */     xformJoints(viewportTransform, interpolation, 5.0F, this.jointViewportCoordsFront);
/* 383:    */     
/* 384:    */ 
/* 385:400 */     paintMembers(g, viewportTransform, -5.0F, interpolation, this.jointViewportCoordsRear, Color.GRAY);
/* 386:403 */     if (lowView)
/* 387:    */     {
/* 388:404 */       if (this.config.showTruck) {
/* 389:405 */         this.truck.paint(g, viewportTransform, interpolation.getPtLoad(), interpolation.getLoadRotation(), distanceTraveled);
/* 390:    */       }
/* 391:411 */       this.terrain.drawAbutmentFaces(g, viewportTransform);
/* 392:    */       
/* 393:413 */       paintDeckSurface(g, gray00, this.deckBeamHeight);
/* 394:    */       
/* 395:415 */       paintDiagonalCrossMembers(g);
/* 396:    */       
/* 397:417 */       paintTransverseMembers(g);
/* 398:    */       
/* 399:419 */       paintDeckEdge(g);
/* 400:421 */       if ((this.terrain.pierLocation != null) && (this.config.showAbutments)) {
/* 401:422 */         pierModel.paint(g, viewportTransform, (float)this.terrain.pierLocation.x, (float)this.terrain.pierLocation.y, 0.0F);
/* 402:    */       }
/* 403:    */     }
/* 404:    */     else
/* 405:    */     {
/* 406:426 */       if ((this.terrain.pierLocation != null) && (this.config.showAbutments)) {
/* 407:427 */         pierModel.paint(g, viewportTransform, (float)this.terrain.pierLocation.x, (float)this.terrain.pierLocation.y, 0.0F);
/* 408:    */       }
/* 409:430 */       paintDiagonalCrossMembers(g);
/* 410:    */       
/* 411:432 */       paintTransverseMembers(g);
/* 412:    */       
/* 413:434 */       this.terrain.drawAbutmentTops(g, viewportTransform);
/* 414:435 */       this.terrain.patchRoadway(g, viewportTransform);
/* 415:    */       
/* 416:437 */       paintDeckSurface(g, gray00, this.deckBeamHeight + this.deckThickness);
/* 417:438 */       if (this.config.showTruck) {
/* 418:439 */         this.truck.paint(g, viewportTransform, interpolation.getPtLoad(), interpolation.getLoadRotation(), distanceTraveled);
/* 419:    */       }
/* 420:445 */       paintDeckEdge(g);
/* 421:    */     }
/* 422:449 */     paintMembers(g, viewportTransform, 5.0F, interpolation, this.jointViewportCoordsFront, this.config.showForcesAsColors ? null : gray99);
/* 423:454 */     for (int i = 0; i < nJoints; i++) {
/* 424:455 */       paintJoint(g, this.jointViewportCoordsFront[i]);
/* 425:    */     }
/* 426:459 */     this.terrain.patchTerrain(g, viewportTransform);
/* 427:462 */     if ((this.terrain.pierLocation != null) && (this.config.showAbutments)) {
/* 428:463 */       pierModel.patch(g, viewportTransform, (float)this.terrain.pierLocation.x, (float)this.terrain.pierLocation.y, 0.0F);
/* 429:    */     }
/* 430:    */   }
/* 431:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.Bridge3dView
 * JD-Core Version:    0.7.0.1
 */