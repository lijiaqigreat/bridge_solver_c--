/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.awt.BasicStroke;
/*   4:    */ import java.awt.Color;
/*   5:    */ import java.awt.Graphics2D;
/*   6:    */ import java.awt.Stroke;
/*   7:    */ 
/*   8:    */ public class FixedEyeTruckModel
/*   9:    */ {
/*  10: 19 */   private final Renderer3d renderer = new Renderer3d();
/*  11: 20 */   private final Homogeneous.Matrix loadTransform = new Homogeneous.Matrix();
/*  12: 21 */   private final Homogeneous.Matrix loadRotation = new Homogeneous.Matrix(Homogeneous.Matrix.Ia);
/*  13: 22 */   private final Stroke thickStroke = new BasicStroke(2.0F);
/*  14: 23 */   private static final Color darkerOrange = new Color(180, 80, 0);
/*  15: 24 */   private static final Color darkOrange = new Color(200, 90, 0);
/*  16: 25 */   private static final Color orange = new Color(225, 108, 0);
/*  17: 26 */   private static final Color lightOrange = new Color(255, 148, 0);
/*  18: 27 */   private static final Color veryLightOrange = new Color(255, 214, 64);
/*  19: 28 */   private static final Color darkerGray = new Color(40, 40, 40);
/*  20: 29 */   private static final Color lighterGray = new Color(210, 210, 210);
/*  21: 30 */   private static final Color veryLightGray = new Color(230, 230, 230);
/*  22:    */   private static final int nTirePoints = 12;
/*  23:    */   private static final int nSpokePoints = 4;
/*  24:    */   private static final float outerRadius = 0.4F;
/*  25:    */   private static final float innerRadius = 0.25F;
/*  26:    */   private static final float spokeRadius = 0.14F;
/*  27:    */   private static final float spokeHoleRadius = 0.04F;
/*  28:    */   private static final float hubRadius = 0.07F;
/*  29: 39 */   private static final float[] dualOuterRear = new float[39];
/*  30: 40 */   private static final float[] tireOuterRear = new float[39];
/*  31: 41 */   private static final float[] tireOuterFront = new float[39];
/*  32: 42 */   private static final float[] tireInnerFront = new float[39];
/*  33: 43 */   private static final float[] spokes = new float[48];
/*  34: 44 */   private static final float[] hub = new float[12];
/*  35:    */   
/*  36:    */   static
/*  37:    */   {
/*  38: 47 */     int i = 0;
/*  39: 48 */     for (int i3 = 0; i3 < tireOuterRear.length; i++)
/*  40:    */     {
/*  41: 49 */       double theta = 6.283185307179586D * i / 12.0D;
/*  42: 50 */       float s = (float)Math.sin(theta);
/*  43: 51 */       float c = (float)Math.cos(theta); float 
/*  44: 52 */         tmp241_240 = (dualOuterRear[(i3 + 0)] = c * 0.4F);tireOuterFront[(i3 + 0)] = tmp241_240;tireOuterRear[(i3 + 0)] = tmp241_240; float 
/*  45: 53 */         tmp269_268 = (dualOuterRear[(i3 + 1)] = s * 0.4F);tireOuterFront[(i3 + 1)] = tmp269_268;tireOuterRear[(i3 + 1)] = tmp269_268;
/*  46: 54 */       tireInnerFront[(i3 + 0)] = (c * 0.25F);
/*  47: 55 */       tireInnerFront[(i3 + 1)] = (s * 0.25F);
/*  48: 56 */       dualOuterRear[(i3 + 2)] = -0.5F;
/*  49: 57 */       tireOuterRear[(i3 + 2)] = -0.25F;
/*  50: 58 */       tireOuterFront[(i3 + 2)] = 0.0F;i3 += 3;
/*  51:    */     }
/*  52: 60 */     for (i = 0; i < 4; i++)
/*  53:    */     {
/*  54: 61 */       double theta = 6.283185307179586D * i / 4.0D;
/*  55: 62 */       float s = (float)Math.sin(theta);
/*  56: 63 */       float c = (float)Math.cos(theta);
/*  57: 64 */       float xc = c * 0.14F;
/*  58: 65 */       float yc = s * 0.14F;
/*  59: 66 */       float dx = c * 0.04F;
/*  60: 67 */       float dy = s * 0.04F;
/*  61: 68 */       int i12 = 12 * i;
/*  62: 69 */       int i0 = i12 + 0;
/*  63: 70 */       int i1 = i12 + 3;
/*  64: 71 */       int i2 = i12 + 6;
/*  65: 72 */       int i3 = i12 + 9;
/*  66:    */       
/*  67: 74 */       spokes[(i0 + 0)] = (xc + dx + dy);
/*  68: 75 */       spokes[(i0 + 1)] = (yc + dy - dx);
/*  69: 76 */       spokes[(i1 + 0)] = (xc + dx - dy);
/*  70: 77 */       spokes[(i1 + 1)] = (yc + dy + dx);
/*  71: 78 */       spokes[(i2 + 0)] = (xc - dx - dy);
/*  72: 79 */       spokes[(i2 + 1)] = (yc - dy + dx);
/*  73: 80 */       spokes[(i3 + 0)] = (xc - dx + dy);
/*  74: 81 */       spokes[(i3 + 1)] = (yc - dy - dx); float 
/*  75: 82 */         tmp581_580 = (spokes[(i1 + 2)] = spokes[(i0 + 2)] = 0.0F);spokes[(i2 + 2)] = tmp581_580;spokes[(i3 + 2)] = tmp581_580;
/*  76: 83 */       int ih = 3 * i;
/*  77: 84 */       hub[(ih + 0)] = (c * 0.07F);
/*  78: 85 */       hub[(ih + 1)] = (s * 0.07F);
/*  79: 86 */       hub[(ih + 2)] = 0.0F;
/*  80:    */     }
/*  81:    */   }
/*  82:    */   
/*  83: 92 */   private final Homogeneous.Matrix wheelTranslation = new Homogeneous.Matrix(Homogeneous.Matrix.Ia);
/*  84: 93 */   private final Homogeneous.Matrix wheelRotation = new Homogeneous.Matrix(Homogeneous.Matrix.Ia);
/*  85: 94 */   private float thetaWheel = 0.0F;
/*  86:    */   
/*  87:    */   private abstract class Drawable
/*  88:    */   {
/*  89:    */     Color color;
/*  90:    */     float[] pts;
/*  91:    */     
/*  92:    */     private Drawable() {}
/*  93:    */     
/*  94:    */     public abstract void paint(Graphics2D paramGraphics2D, ViewportTransform paramViewportTransform);
/*  95:    */   }
/*  96:    */   
/*  97:    */   private class Polygon
/*  98:    */     extends FixedEyeTruckModel.Drawable
/*  99:    */   {
/* 100:    */     public Polygon(Color color, float[] pts)
/* 101:    */     {
/* 102:105 */       super(null);
/* 103:106 */       this.color = color;
/* 104:107 */       this.pts = pts;
/* 105:    */     }
/* 106:    */     
/* 107:    */     public void paint(Graphics2D g, ViewportTransform viewportTransform)
/* 108:    */     {
/* 109:112 */       FixedEyeTruckModel.this.renderer.setPaint(this.color);
/* 110:113 */       FixedEyeTruckModel.this.renderer.begin(5);
/* 111:114 */       FixedEyeTruckModel.this.renderer.addVertex(g, viewportTransform, this.pts, 0, this.pts.length / 3);
/* 112:115 */       FixedEyeTruckModel.this.renderer.end(g);
/* 113:    */     }
/* 114:    */   }
/* 115:    */   
/* 116:    */   private class RuledPolygon
/* 117:    */     extends FixedEyeTruckModel.Drawable
/* 118:    */   {
/* 119:121 */     Color ruleColor = Color.BLACK;
/* 120:    */     
/* 121:    */     public RuledPolygon(Color color, float[] pts)
/* 122:    */     {
/* 123:123 */       super(null);
/* 124:124 */       this.color = color;
/* 125:125 */       this.pts = pts;
/* 126:    */     }
/* 127:    */     
/* 128:    */     public RuledPolygon(Color color, Color ruleColor, float[] pts)
/* 129:    */     {
/* 130:128 */       super(null);
/* 131:129 */       this.color = color;
/* 132:130 */       this.ruleColor = ruleColor;
/* 133:131 */       this.pts = pts;
/* 134:    */     }
/* 135:    */     
/* 136:    */     public void paint(Graphics2D g, ViewportTransform viewportTransform)
/* 137:    */     {
/* 138:136 */       FixedEyeTruckModel.this.renderer.setPaint(this.color);
/* 139:137 */       FixedEyeTruckModel.this.renderer.setRulePaint(this.ruleColor);
/* 140:138 */       FixedEyeTruckModel.this.renderer.begin(9);
/* 141:139 */       FixedEyeTruckModel.this.renderer.addVertex(g, viewportTransform, this.pts, 0, this.pts.length / 3);
/* 142:140 */       FixedEyeTruckModel.this.renderer.end(g);
/* 143:    */     }
/* 144:    */   }
/* 145:    */   
/* 146:    */   private class Lines
/* 147:    */     extends FixedEyeTruckModel.Drawable
/* 148:    */   {
/* 149:    */     final byte thickness;
/* 150:    */     
/* 151:    */     public Lines(Color color, float[] pts)
/* 152:    */     {
/* 153:146 */       super(null);
/* 154:147 */       this.color = color;
/* 155:148 */       this.pts = pts;
/* 156:149 */       this.thickness = 0;
/* 157:    */     }
/* 158:    */     
/* 159:    */     public Lines(Color color, int thickness, float[] pts)
/* 160:    */     {
/* 161:151 */       super(null);
/* 162:152 */       this.color = color;
/* 163:153 */       this.pts = pts;
/* 164:154 */       this.thickness = ((byte)thickness);
/* 165:    */     }
/* 166:    */     
/* 167:    */     public void paint(Graphics2D g, ViewportTransform viewportTransform)
/* 168:    */     {
/* 169:159 */       FixedEyeTruckModel.this.renderer.setPaint(this.color);
/* 170:160 */       if (this.thickness > 0)
/* 171:    */       {
/* 172:161 */         Stroke savedStroke = g.getStroke();
/* 173:162 */         g.setStroke(FixedEyeTruckModel.this.thickStroke);
/* 174:163 */         FixedEyeTruckModel.this.renderer.begin(3);
/* 175:164 */         FixedEyeTruckModel.this.renderer.addVertex(g, viewportTransform, this.pts, 0, this.pts.length / 3);
/* 176:165 */         FixedEyeTruckModel.this.renderer.end(g);
/* 177:166 */         g.setStroke(savedStroke);
/* 178:    */       }
/* 179:    */       else
/* 180:    */       {
/* 181:169 */         FixedEyeTruckModel.this.renderer.begin(3);
/* 182:170 */         FixedEyeTruckModel.this.renderer.addVertex(g, viewportTransform, this.pts, 0, this.pts.length / 3);
/* 183:171 */         FixedEyeTruckModel.this.renderer.end(g);
/* 184:    */       }
/* 185:    */     }
/* 186:    */   }
/* 187:    */   
/* 188:    */   private class FrontRightWheel
/* 189:    */     extends FixedEyeTruckModel.Drawable
/* 190:    */   {
/* 191:    */     private FrontRightWheel()
/* 192:    */     {
/* 193:176 */       super(null);
/* 194:    */     }
/* 195:    */     
/* 196:    */     public void paint(Graphics2D g, ViewportTransform viewportTransform)
/* 197:    */     {
/* 198:179 */       FixedEyeTruckModel.this.renderer.pushModelTransform();
/* 199:180 */       FixedEyeTruckModel.this.wheelTranslation.setTranslation(0.0F, 0.4F, 1.15F);
/* 200:181 */       FixedEyeTruckModel.this.renderer.appendTransform(FixedEyeTruckModel.this.wheelTranslation);
/* 201:    */       
/* 202:183 */       FixedEyeTruckModel.this.renderer.setPaint(Color.GRAY);
/* 203:184 */       FixedEyeTruckModel.this.renderer.setRulePaint(Color.BLACK);
/* 204:185 */       FixedEyeTruckModel.this.renderer.setRuleFlags(1);
/* 205:186 */       FixedEyeTruckModel.this.renderer.begin(8);
/* 206:187 */       FixedEyeTruckModel.this.renderer.addVertex(g, viewportTransform, FixedEyeTruckModel.tireOuterRear, 0, FixedEyeTruckModel.tireOuterFront, 0, null, 0, 13);
/* 207:188 */       FixedEyeTruckModel.this.renderer.end(g);
/* 208:    */       
/* 209:190 */       FixedEyeTruckModel.this.renderer.begin(8);
/* 210:191 */       FixedEyeTruckModel.this.renderer.addVertex(g, viewportTransform, FixedEyeTruckModel.tireOuterFront, 0, FixedEyeTruckModel.tireInnerFront, 0, null, 0, 13);
/* 211:192 */       FixedEyeTruckModel.this.renderer.end(g);
/* 212:    */       
/* 213:194 */       FixedEyeTruckModel.this.renderer.begin(9);
/* 214:195 */       FixedEyeTruckModel.this.renderer.setPaint(Color.LIGHT_GRAY);
/* 215:196 */       FixedEyeTruckModel.this.renderer.setRulePaint(Color.WHITE);
/* 216:197 */       FixedEyeTruckModel.this.renderer.addVertex(g, viewportTransform, FixedEyeTruckModel.tireInnerFront, 0, 13);
/* 217:198 */       FixedEyeTruckModel.this.renderer.end(g);
/* 218:    */       
/* 219:200 */       FixedEyeTruckModel.this.renderer.setPaint(FixedEyeTruckModel.darkerOrange);
/* 220:201 */       FixedEyeTruckModel.this.renderer.begin(5);
/* 221:202 */       FixedEyeTruckModel.this.renderer.addVertex(g, viewportTransform, FixedEyeTruckModel.hub, 0, 4);
/* 222:203 */       FixedEyeTruckModel.this.renderer.end(g);
/* 223:    */       
/* 224:205 */       FixedEyeTruckModel.this.renderer.appendTransform(FixedEyeTruckModel.this.wheelRotation);
/* 225:206 */       FixedEyeTruckModel.this.renderer.setPaint(Color.BLACK);
/* 226:207 */       for (int i12 = 0; i12 < FixedEyeTruckModel.spokes.length; i12 += 12)
/* 227:    */       {
/* 228:208 */         FixedEyeTruckModel.this.renderer.begin(5);
/* 229:209 */         FixedEyeTruckModel.this.renderer.addVertex(g, viewportTransform, FixedEyeTruckModel.spokes, i12, 4);
/* 230:210 */         FixedEyeTruckModel.this.renderer.end(g);
/* 231:    */       }
/* 232:212 */       FixedEyeTruckModel.this.renderer.popModelTransform();
/* 233:    */     }
/* 234:    */   }
/* 235:    */   
/* 236:    */   private class RearRightWheel
/* 237:    */     extends FixedEyeTruckModel.Drawable
/* 238:    */   {
/* 239:    */     private RearRightWheel()
/* 240:    */     {
/* 241:216 */       super(null);
/* 242:    */     }
/* 243:    */     
/* 244:    */     public void paint(Graphics2D g, ViewportTransform viewportTransform)
/* 245:    */     {
/* 246:219 */       FixedEyeTruckModel.this.renderer.pushModelTransform();
/* 247:220 */       FixedEyeTruckModel.this.wheelTranslation.setTranslation(-4.0F, 0.4F, 1.15F);
/* 248:221 */       FixedEyeTruckModel.this.renderer.appendTransform(FixedEyeTruckModel.this.wheelTranslation);
/* 249:222 */       FixedEyeTruckModel.this.renderer.setPaint(Color.GRAY);
/* 250:223 */       FixedEyeTruckModel.this.renderer.setRulePaint(Color.BLACK);
/* 251:224 */       FixedEyeTruckModel.this.renderer.setRuleFlags(1);
/* 252:    */       
/* 253:226 */       FixedEyeTruckModel.this.renderer.begin(8);
/* 254:227 */       FixedEyeTruckModel.this.renderer.addVertex(g, viewportTransform, FixedEyeTruckModel.dualOuterRear, 0, FixedEyeTruckModel.tireOuterRear, 0, null, 0, 13);
/* 255:228 */       FixedEyeTruckModel.this.renderer.end(g);
/* 256:    */       
/* 257:230 */       FixedEyeTruckModel.this.renderer.begin(8);
/* 258:231 */       FixedEyeTruckModel.this.renderer.addVertex(g, viewportTransform, FixedEyeTruckModel.tireOuterRear, 0, FixedEyeTruckModel.tireOuterFront, 0, null, 0, 13);
/* 259:232 */       FixedEyeTruckModel.this.renderer.end(g);
/* 260:    */       
/* 261:234 */       FixedEyeTruckModel.this.renderer.begin(8);
/* 262:235 */       FixedEyeTruckModel.this.renderer.addVertex(g, viewportTransform, FixedEyeTruckModel.tireOuterFront, 0, FixedEyeTruckModel.tireInnerFront, 0, null, 0, 13);
/* 263:236 */       FixedEyeTruckModel.this.renderer.end(g);
/* 264:    */       
/* 265:238 */       FixedEyeTruckModel.this.renderer.begin(9);
/* 266:239 */       FixedEyeTruckModel.this.renderer.setRulePaint(Color.WHITE);
/* 267:240 */       FixedEyeTruckModel.this.renderer.setPaint(FixedEyeTruckModel.darkerGray);
/* 268:241 */       FixedEyeTruckModel.this.renderer.addVertex(g, viewportTransform, FixedEyeTruckModel.tireInnerFront, 0, 12);
/* 269:242 */       FixedEyeTruckModel.this.renderer.end(g);
/* 270:    */       
/* 271:244 */       FixedEyeTruckModel.this.renderer.setPaint(FixedEyeTruckModel.darkerOrange);
/* 272:245 */       FixedEyeTruckModel.this.renderer.begin(5);
/* 273:246 */       FixedEyeTruckModel.this.renderer.addVertex(g, viewportTransform, FixedEyeTruckModel.hub, 0, 4);
/* 274:247 */       FixedEyeTruckModel.this.renderer.end(g);
/* 275:    */       
/* 276:249 */       FixedEyeTruckModel.this.renderer.popModelTransform();
/* 277:    */     }
/* 278:    */   }
/* 279:    */   
/* 280:    */   private class FrontLeftWheel
/* 281:    */     extends FixedEyeTruckModel.Drawable
/* 282:    */   {
/* 283:    */     private FrontLeftWheel()
/* 284:    */     {
/* 285:253 */       super(null);
/* 286:    */     }
/* 287:    */     
/* 288:    */     public void paint(Graphics2D g, ViewportTransform viewportTransform)
/* 289:    */     {
/* 290:256 */       FixedEyeTruckModel.this.renderer.pushModelTransform();
/* 291:257 */       FixedEyeTruckModel.this.wheelTranslation.setTranslation(0.0F, 0.4F, -0.9F);
/* 292:258 */       FixedEyeTruckModel.this.renderer.appendTransform(FixedEyeTruckModel.this.wheelTranslation);
/* 293:    */       
/* 294:260 */       FixedEyeTruckModel.this.renderer.setPaint(FixedEyeTruckModel.darkerGray);
/* 295:261 */       FixedEyeTruckModel.this.renderer.setRulePaint(Color.BLACK);
/* 296:262 */       FixedEyeTruckModel.this.renderer.setRuleFlags(1);
/* 297:263 */       FixedEyeTruckModel.this.renderer.begin(8);
/* 298:264 */       FixedEyeTruckModel.this.renderer.addVertex(g, viewportTransform, FixedEyeTruckModel.tireOuterRear, 0, FixedEyeTruckModel.tireOuterFront, 0, null, 0, 13);
/* 299:265 */       FixedEyeTruckModel.this.renderer.end(g);
/* 300:    */       
/* 301:267 */       FixedEyeTruckModel.this.renderer.begin(8);
/* 302:268 */       FixedEyeTruckModel.this.renderer.addVertex(g, viewportTransform, FixedEyeTruckModel.tireOuterFront, 0, FixedEyeTruckModel.tireInnerFront, 0, null, 0, 13);
/* 303:269 */       FixedEyeTruckModel.this.renderer.end(g);
/* 304:    */       
/* 305:271 */       FixedEyeTruckModel.this.renderer.begin(9);
/* 306:272 */       FixedEyeTruckModel.this.renderer.setPaint(Color.BLACK);
/* 307:273 */       FixedEyeTruckModel.this.renderer.setRulePaint(Color.GRAY);
/* 308:274 */       FixedEyeTruckModel.this.renderer.addVertex(g, viewportTransform, FixedEyeTruckModel.tireInnerFront, 0, 12);
/* 309:275 */       FixedEyeTruckModel.this.renderer.end(g);
/* 310:276 */       FixedEyeTruckModel.this.renderer.popModelTransform();
/* 311:    */     }
/* 312:    */   }
/* 313:    */   
/* 314:    */   private class RearLeftWheel
/* 315:    */     extends FixedEyeTruckModel.Drawable
/* 316:    */   {
/* 317:    */     private RearLeftWheel()
/* 318:    */     {
/* 319:280 */       super(null);
/* 320:    */     }
/* 321:    */     
/* 322:    */     public void paint(Graphics2D g, ViewportTransform viewportTransform)
/* 323:    */     {
/* 324:283 */       FixedEyeTruckModel.this.renderer.pushModelTransform();
/* 325:284 */       FixedEyeTruckModel.this.wheelTranslation.setTranslation(-4.0F, 0.4F, -0.65F);
/* 326:285 */       FixedEyeTruckModel.this.renderer.appendTransform(FixedEyeTruckModel.this.wheelTranslation);
/* 327:286 */       FixedEyeTruckModel.this.renderer.setPaint(FixedEyeTruckModel.darkerGray);
/* 328:287 */       FixedEyeTruckModel.this.renderer.setRulePaint(Color.BLACK);
/* 329:288 */       FixedEyeTruckModel.this.renderer.setRuleFlags(1);
/* 330:    */       
/* 331:290 */       FixedEyeTruckModel.this.renderer.begin(8);
/* 332:291 */       FixedEyeTruckModel.this.renderer.addVertex(g, viewportTransform, FixedEyeTruckModel.dualOuterRear, 0, FixedEyeTruckModel.tireOuterRear, 0, null, 0, 13);
/* 333:292 */       FixedEyeTruckModel.this.renderer.end(g);
/* 334:    */       
/* 335:294 */       FixedEyeTruckModel.this.renderer.begin(8);
/* 336:295 */       FixedEyeTruckModel.this.renderer.addVertex(g, viewportTransform, FixedEyeTruckModel.tireOuterRear, 0, FixedEyeTruckModel.tireOuterFront, 0, null, 0, 13);
/* 337:296 */       FixedEyeTruckModel.this.renderer.end(g);
/* 338:    */       
/* 339:298 */       FixedEyeTruckModel.this.renderer.begin(8);
/* 340:299 */       FixedEyeTruckModel.this.renderer.addVertex(g, viewportTransform, FixedEyeTruckModel.tireOuterFront, 0, FixedEyeTruckModel.tireInnerFront, 0, null, 0, 13);
/* 341:300 */       FixedEyeTruckModel.this.renderer.end(g);
/* 342:    */       
/* 343:302 */       FixedEyeTruckModel.this.renderer.begin(9);
/* 344:303 */       FixedEyeTruckModel.this.renderer.setPaint(Color.BLACK);
/* 345:304 */       FixedEyeTruckModel.this.renderer.setRulePaint(Color.GRAY);
/* 346:305 */       FixedEyeTruckModel.this.renderer.addVertex(g, viewportTransform, FixedEyeTruckModel.tireInnerFront, 0, 12);
/* 347:306 */       FixedEyeTruckModel.this.renderer.end(g);
/* 348:307 */       FixedEyeTruckModel.this.renderer.popModelTransform();
/* 349:    */     }
/* 350:    */   }
/* 351:    */   
/* 352:311 */   private final Drawable[] rightWheels = { new FrontRightWheel(null), new RearRightWheel(null) };
/* 353:316 */   private final Drawable[] leftWheels = { new FrontLeftWheel(null), new RearLeftWheel(null) };
/* 354:321 */   private final Drawable[] cabLeftInside = { new Polygon(Color.DARK_GRAY, new float[] { -0.4F, 0.85F, -1.15F, 1.2F, 0.85F, -1.15F, 1.2F, 1.5F, -1.15F, 1.0F, 1.5F, -1.15F, 0.8F, 1.8F, -1.15F, 0.0F, 1.9F, -1.15F, 0.0F, 2.5F, -1.15F, -0.4F, 2.5F, -1.15F }), new Polygon(Color.BLACK, new float[] { 0.8F, 2.7F, -0.95F, -0.2F, 2.7F, -0.95F, -0.4F, 2.5F, -1.15F, 1.0F, 2.5F, -1.15F }), new Lines(Color.DARK_GRAY, 2, new float[] { 1.2F, 1.5F, -1.15F, 1.0F, 2.5F, -1.15F, 0.8F, 1.8F, -1.15F, 0.8F, 2.5F, -1.15F }), new Lines(Color.WHITE, 2, new float[] { 1.4F, 1.5F, -0.95F, 1.2F, 2.6F, -0.95F }) };
/* 355:353 */   private final Drawable[] cabFrontAboveBumper = { new Polygon(veryLightOrange, new float[] { 1.4F, 1.5F, 0.95F, 1.4F, 0.95F, 0.95F, 1.4F, 0.95F, -0.95F, 1.4F, 1.5F, -0.95F }) };
/* 356:362 */   private final Drawable[] dashboard = { new Polygon(Color.LIGHT_GRAY, new float[] { 1.0F, 1.5F, 1.15F, 1.2F, 1.5F, 1.15F, 1.4F, 1.5F, 0.95F, 1.4F, 1.5F, -0.95F, 1.2F, 1.5F, -1.15F, 1.0F, 1.5F, -1.15F }) };
/* 357:373 */   private final Drawable[] cabFront = { new Polygon(Color.GRAY, new float[] { 1.5F, 0.95F, 0.95F, 1.5F, 0.65F, 0.95F, 1.5F, 0.65F, -0.95F, 1.5F, 0.95F, -0.95F }), new Polygon(veryLightOrange, new float[] { 1.5F, 0.65F, 0.95F, 1.5F, 0.4F, 0.95F, 1.5F, 0.4F, -0.95F, 1.5F, 0.65F, -0.95F }) };
/* 358:388 */   private final Drawable[] cabCorner = { new Polygon(lightOrange, new float[] { 1.4F, 1.5F, 0.95F, 1.2F, 1.5F, 1.15F, 1.2F, 0.95F, 1.15F, 1.4F, 0.95F, 0.95F }) };
/* 359:398 */   private final Drawable[] bumperTop = { new Polygon(darkerGray, new float[] { 1.2F, 0.95F, 1.15F, 1.3F, 0.95F, 1.15F, 1.5F, 0.95F, 0.95F, 1.5F, 0.95F, -0.95F, 1.3F, 0.95F, -1.15F, 1.2F, 0.95F, -1.15F, 1.4F, 0.95F, -0.95F, 1.4F, 0.95F, 0.95F }) };
/* 360:411 */   private final Drawable[] cabTop = { new Polygon(Color.DARK_GRAY, new float[] { -0.4F, 2.5F, -1.15F, -0.2F, 2.7F, -0.95F, -0.2F, 2.7F, 0.95F, -0.4F, 2.5F, 1.15F }), new Polygon(Color.DARK_GRAY, new float[] { -0.2F, 2.7F, -0.95F, 0.8F, 2.7F, -0.95F, 0.8F, 2.7F, 0.95F, -0.2F, 2.7F, 0.95F }), new Polygon(Color.BLACK, new float[] { 0.8F, 2.7F, -0.95F, 1.2F, 2.6F, -0.95F, 1.2F, 2.6F, 0.95F, 0.8F, 2.7F, 0.95F }), new Polygon(Color.BLACK, new float[] { 1.2F, 2.6F, -0.95F, 0.8F, 2.7F, -0.95F, 1.0F, 2.5F, -1.15F }), new Polygon(lightOrange, new float[] { 0.8F, 2.7F, 0.95F, 1.2F, 2.6F, 0.95F, 1.2F, 2.6F, -0.95F, 0.8F, 2.7F, -0.95F }), new Polygon(orange, new float[] { -0.2F, 2.7F, 0.95F, 0.8F, 2.7F, 0.95F, 0.8F, 2.7F, -0.95F, -0.2F, 2.7F, -0.95F }), new Polygon(darkOrange, new float[] { -0.4F, 2.5F, 1.15F, -0.2F, 2.7F, 0.95F, -0.2F, 2.7F, -0.95F, -0.4F, 2.5F, -1.15F }) };
/* 361:459 */   private final Drawable[] cabRear = { new Polygon(darkerOrange, new float[] { -0.4F, 2.0F, -0.7F, -0.4F, 2.5F, -0.7F, -0.4F, 2.5F, -1.15F, -0.4F, 0.85F, -1.15F, -0.4F, 0.85F, 1.15F, -0.4F, 2.5F, 1.15F, -0.4F, 2.5F, 0.7F, -0.4F, 2.0F, 0.7F }) };
/* 362:472 */   private final Drawable[] cabRearInside = { new Polygon(Color.GRAY, new float[] { -0.4F, 2.0F, 0.7F, -0.4F, 2.5F, 0.7F, -0.4F, 2.5F, 1.15F, -0.4F, 0.85F, 1.15F, -0.4F, 0.85F, -1.15F, -0.4F, 2.5F, -1.15F, -0.4F, 2.5F, -0.7F, -0.4F, 2.0F, -0.7F }) };
/* 363:485 */   private final Drawable[] cabBottom = { new Polygon(Color.BLACK, new float[] { 0.45F, 0.4F, -1.15F, 1.3F, 0.4F, -1.15F, 1.5F, 0.4F, -0.95F, 1.5F, 0.4F, 0.95F, 1.3F, 0.4F, 1.15F, 0.45F, 0.4F, 1.15F }), new Polygon(Color.BLACK, new float[] { 0.45F, 0.4F, 1.15F, 0.45F, 0.85F, 1.15F, 0.45F, 0.85F, -1.15F, 0.45F, 0.4F, -1.15F }), new Polygon(Color.BLACK, new float[] { 0.45F, 0.85F, 1.15F, -0.4F, 0.85F, 1.15F, -0.4F, 0.85F, -1.15F, 0.45F, 0.85F, -1.15F }) };
/* 364:511 */   private final Drawable[] cabRight = { new Polygon(orange, new float[] { 0.0F, 0.85F, 1.15F, 0.116F, 0.835F, 1.15F, 0.225F, 0.79F, 1.15F, 0.318F, 0.718F, 1.15F, 0.39F, 0.625F, 1.15F, 0.435F, 0.516F, 1.15F, 0.45F, 0.4F, 1.15F, 1.3F, 0.4F, 1.15F, 1.3F, 0.65F, 1.15F, 1.2F, 0.65F, 1.15F, 1.2F, 1.5F, 1.15F, 1.0F, 1.5F, 1.15F, 0.8F, 1.8F, 1.15F, 0.0F, 1.9F, 1.15F, 0.0F, 2.5F, 1.15F, -0.4F, 2.5F, 1.15F, -0.4F, 0.85F, 1.15F }), new Polygon(Color.RED, new float[] { 1.2F, 0.65F, 1.15F, 1.3F, 0.65F, 1.15F, 1.3F, 0.8F, 1.15F, 1.2F, 0.8F, 1.15F }), new Polygon(Color.BLACK, new float[] { 1.2F, 0.8F, 1.15F, 1.3F, 0.8F, 1.15F, 1.3F, 0.95F, 1.15F, 1.2F, 0.95F, 1.15F }), new Polygon(lightOrange, new float[] { 1.3F, 0.4F, 1.15F, 1.5F, 0.4F, 0.95F, 1.5F, 0.65F, 0.95F, 1.3F, 0.659F, 1.15F }), new Polygon(Color.WHITE, new float[] { 1.3F, 0.65F, 1.15F, 1.5F, 0.65F, 0.95F, 1.5F, 0.8F, 0.95F, 1.3F, 0.8F, 1.15F }), new Polygon(Color.DARK_GRAY, new float[] { 1.3F, 0.8F, 1.15F, 1.5F, 0.8F, 0.95F, 1.5F, 0.95F, 0.95F, 1.3F, 0.95F, 1.15F }), new Polygon(lightOrange, new float[] { 1.2F, 2.6F, 0.95F, 0.8F, 2.7F, 0.95F, 1.0F, 2.5F, 1.15F }), new Lines(lightOrange, 2, new float[] { 1.2F, 1.5F, 1.15F, 1.0F, 2.5F, 1.15F, 0.8F, 1.8F, 1.15F, 0.8F, 2.5F, 1.15F }), new Lines(Color.WHITE, 2, new float[] { 1.4F, 1.5F, 0.95F, 1.2F, 2.6F, 0.95F }), new Polygon(lightOrange, new float[] { 0.8F, 2.7F, 0.95F, -0.2F, 2.7F, 0.95F, -0.4F, 2.5F, 1.15F, 1.0F, 2.5F, 1.15F }) };
/* 365:594 */   private final Drawable[] mirror = { new Lines(Color.WHITE, 2, new float[] { 0.8F, 2.5F, 1.15F, 0.8F, 2.5F, 1.45F, 0.8F, 2.5F, 1.45F, 1.0F, 2.5F, 1.15F, 0.8F, 1.8F, 1.15F, 0.8F, 1.8F, 1.45F, 0.8F, 1.8F, 1.45F, 1.0F, 1.5F, 1.15F }), new Polygon(Color.WHITE, new float[] { 0.8F, 1.8F, 1.45F, 0.8F, 2.5F, 1.45F, 0.72F, 2.5F, 1.65F, 0.72F, 1.8F, 1.65F }), new Polygon(Color.LIGHT_GRAY, new float[] { 0.72F, 1.8F, 1.65F, 0.72F, 2.5F, 1.65F, 0.8F, 2.5F, 1.45F, 0.8F, 1.8F, 1.45F }) };
/* 366:621 */   private final Drawable[] chasis = { new Polygon(Color.GRAY, new float[] { -0.8F, 0.85F, 0.8F, -0.4F, 0.85F, 0.8F, -0.4F, 0.85F, -0.8F, -0.8F, 0.85F, -0.8F }), new Polygon(Color.DARK_GRAY, new float[] { 0.45F, 0.65F, 0.8F, 0.45F, 0.85F, 0.8F, -0.4F, 0.85F, 0.8F, -0.8F, 0.85F, 0.8F, -6.0F, 0.85F, 0.8F, -6.0F, 0.65F, 0.8F }), new Polygon(Color.BLACK, new float[] { 0.45F, 0.65F, 0.8F, -6.0F, 0.65F, 0.8F, -6.0F, 0.65F, -0.8F, 0.45F, 0.65F, -0.8F }) };
/* 367:647 */   private final Drawable[] gasTank = { new Polygon(Color.LIGHT_GRAY, new float[] { -0.8F, 0.8F, 1.3F, -2.0F, 0.8F, 1.3F, -2.0F, 0.35F, 1.3F, -0.8F, 0.35F, 1.3F }), new Polygon(lighterGray, new float[] { -0.8F, 0.8F, 1.3F, -0.8F, 0.35F, 1.3F, -0.8F, 0.35F, 0.8F, -0.8F, 0.8F, 0.8F }), new Polygon(darkerGray, new float[] { -2.0F, 0.8F, 0.8F, -2.0F, 0.35F, 0.8F, -2.0F, 0.35F, 1.3F, -2.0F, 0.8F, 1.3F }), new Polygon(Color.BLACK, new float[] { -0.8F, 0.35F, 1.3F, -2.0F, 0.35F, 1.3F, -2.0F, 0.35F, 0.8F, -0.8F, 0.35F, 0.8F }) };
/* 368:678 */   private final Drawable[] rightRearBumper = { new Lines(Color.GRAY, 2, new float[] { -6.0F, 0.85F, 1.3F, -6.0F, 0.3F, 1.3F, -6.0F, 0.3F, 0.8F, -6.0F, 0.65F, 0.8F, -5.5F, 0.85F, 1.3F, -6.0F, 0.4F, 1.3F, -6.0F, 0.3F, 1.3F, -6.0F, 0.3F, -1.3F }) };
/* 369:691 */   private final Drawable[] leftRearBumper = { new Lines(Color.GRAY, 2, new float[] { -6.0F, 0.85F, -1.3F, -6.0F, 0.3F, -1.3F, -6.0F, 0.3F, -0.8F, -6.0F, 0.65F, -0.8F, -5.5F, 0.85F, -1.3F, -6.0F, 0.4F, -1.3F }) };
/* 370:702 */   private final Drawable[] cargoBoxTopRightRear = { new Polygon(lighterGray, new float[] { -0.8F, 0.85F, 1.3F, -0.8F, 0.95F, 1.3F, -6.0F, 0.95F, 1.3F, -6.0F, 0.85F, 1.3F }), new Polygon(lightOrange, new float[] { -0.8F, 0.95F, 1.3F, -0.8F, 3.1F, 1.3F, -6.0F, 3.1F, 1.3F, -6.0F, 0.95F, 1.3F }), new Polygon(lighterGray, new float[] { -0.8F, 3.1F, 1.3F, -0.8F, 3.3F, 1.3F, -6.0F, 3.3F, 1.3F, -6.0F, 3.1F, 1.3F }), new Polygon(Color.LIGHT_GRAY, new float[] { -6.0F, 0.95F, -1.3F, -6.0F, 0.85F, -1.3F, -6.0F, 0.85F, 1.3F, -6.0F, 0.95F, 1.3F }), new Polygon(darkOrange, new float[] { -6.0F, 3.1F, -1.3F, -6.0F, 0.95F, -1.3F, -6.0F, 0.95F, 1.3F, -6.0F, 3.1F, 1.3F }), new Polygon(Color.LIGHT_GRAY, new float[] { -6.0F, 3.3F, -1.3F, -6.0F, 3.1F, -1.3F, -6.0F, 3.1F, 1.3F, -6.0F, 3.3F, 1.3F }), new Polygon(new Color(160, 160, 160), new float[] { -6.0F, 3.3F, 1.3F, -0.8F, 3.3F, 1.3F, -0.8F, 3.3F, -1.3F, -6.0F, 3.3F, -1.3F }), new Polygon(darkerGray, new float[] { -6.0F, 0.65F, 0.8F, -6.0F, 0.85F, 0.8F, -6.0F, 0.85F, -0.8F, -6.0F, 0.65F, -0.8F }), new Polygon(Color.RED, new float[] { -6.0F, 0.93F, -1.2F, -6.0F, 0.87F, -1.2F, -6.0F, 0.87F, -1.0F, -6.0F, 0.93F, -1.0F }), new Polygon(Color.RED, new float[] { -6.0F, 0.93F, 1.0F, -6.0F, 0.87F, 1.0F, -6.0F, 0.87F, 1.2F, -6.0F, 0.93F, 1.2F }), new RuledPolygon(Color.LIGHT_GRAY, Color.GRAY, new float[] { -6.0F, 2.9F, -1.1F, -6.0F, 0.95F, -1.1F, -6.0F, 0.95F, 1.1F, -6.0F, 2.9F, 1.1F }) };
/* 371:781 */   private final Drawable[] cargoBoxFrontBottom = { new Polygon(Color.WHITE, new float[] { -0.85F, 0.95F, 1.3F, -0.85F, 0.85F, 1.3F, -0.85F, 0.85F, -1.3F, -0.85F, 0.95F, -1.3F }), new Polygon(veryLightOrange, new float[] { -0.85F, 3.1F, 1.3F, -0.85F, 0.95F, 1.3F, -0.85F, 0.95F, -1.3F, -0.85F, 3.1F, -1.3F }), new Polygon(Color.WHITE, new float[] { -0.85F, 3.3F, 1.3F, -0.85F, 3.1F, 1.3F, -0.85F, 3.1F, -1.3F, -0.85F, 3.3F, -1.3F }), new Polygon(Color.BLACK, new float[] { -0.8F, 0.85F, 1.3F, -6.0F, 0.85F, 1.3F, -6.0F, 0.85F, 0.8F, -0.8F, 0.85F, 0.8F }), new Polygon(Color.BLACK, new float[] { -0.8F, 0.85F, -0.8F, -6.0F, 0.85F, -0.8F, -6.0F, 0.85F, -1.3F, -0.8F, 0.85F, -1.3F }) };
/* 372:819 */   private final Drawable[][] lowRightView = { this.leftWheels, this.cargoBoxFrontBottom, this.cabLeftInside, this.cabTop, this.cabFrontAboveBumper, this.cabRearInside, this.cabBottom, this.leftRearBumper, this.chasis, this.rightWheels, this.gasTank, this.rightRearBumper, this.cabFront, this.cabCorner, this.cabRight, this.cargoBoxTopRightRear, this.mirror };
/* 373:838 */   private final Drawable[][] lowLeftView = { this.leftWheels, this.cabLeftInside, this.cabTop, this.cabBottom, this.cabRear, this.cargoBoxFrontBottom, this.leftRearBumper, this.chasis, this.gasTank, this.rightWheels, this.rightRearBumper, this.cabCorner, this.cabRight, this.cargoBoxTopRightRear, this.mirror };
/* 374:855 */   private final Drawable[][] highRightView = { this.leftWheels, this.leftRearBumper, this.chasis, this.gasTank, this.rightRearBumper, this.rightWheels, this.cargoBoxFrontBottom, this.cabLeftInside, this.cabFront, this.bumperTop, this.cabFrontAboveBumper, this.dashboard, this.cabRearInside, this.cabTop, this.cabRight, this.cabCorner, this.cargoBoxTopRightRear, this.mirror };
/* 375:875 */   private final Drawable[][] highLeftView = { this.leftWheels, this.leftRearBumper, this.chasis, this.gasTank, this.rightWheels, this.rightRearBumper, this.cabLeftInside, this.bumperTop, this.dashboard, this.cabTop, this.cabRear, this.cabRight, this.cabCorner, this.cargoBoxFrontBottom, this.cargoBoxTopRightRear };
/* 376:    */   
/* 377:    */   public void paint(Graphics2D g, ViewportTransform viewportTransform, Affine.Point ptLoad, Affine.Vector rotLoad, double distanceMoved)
/* 378:    */   {
/* 379:897 */     this.loadTransform.setTranslation((float)ptLoad.x, (float)ptLoad.y, 3.5F);
/* 380:898 */     this.renderer.enableModelTransform(this.loadTransform);
/* 381:899 */     this.renderer.setCulling(true); float 
/* 382:900 */       tmp60_59 = ((float)rotLoad.x);this.loadRotation.a[5] = tmp60_59;this.loadRotation.a[0] = tmp60_59;
/* 383:901 */     this.loadRotation.a[1] = ((float)rotLoad.y);
/* 384:902 */     this.loadRotation.a[4] = (-(float)rotLoad.y);
/* 385:903 */     this.renderer.appendTransform(this.loadRotation);
/* 386:    */     
/* 387:    */ 
/* 388:    */ 
/* 389:    */ 
/* 390:    */ 
/* 391:    */ 
/* 392:910 */     this.thetaWheel += (float)distanceMoved * 2.5F;
/* 393:911 */     float twoPi = 6.283185F;
/* 394:912 */     while (this.thetaWheel > 6.283185F) {
/* 395:913 */       this.thetaWheel -= 6.283185F;
/* 396:    */     }
/* 397:915 */     float cos = (float)Math.cos(this.thetaWheel);
/* 398:916 */     float sin = (float)Math.sin(this.thetaWheel);
/* 399:917 */     this.wheelRotation.setIdentity(); float 
/* 400:918 */       tmp195_193 = cos;this.wheelRotation.a[5] = tmp195_193;this.wheelRotation.a[0] = tmp195_193;
/* 401:919 */     this.wheelRotation.a[1] = (-sin);
/* 402:920 */     this.wheelRotation.a[4] = sin;
/* 403:    */     
/* 404:    */ 
/* 405:923 */     boolean lowView = viewportTransform.isAboveVanishingPoint(ptLoad.y + 2.5D);
/* 406:    */     
/* 407:925 */     boolean leftView = viewportTransform.isRightOfVanishingPoint(ptLoad.x - 0.85D);
/* 408:926 */     Drawable[][] sequence = leftView ? this.highLeftView : lowView ? this.lowRightView : leftView ? this.lowLeftView : this.highRightView;
/* 409:929 */     for (int iSeq = 0; iSeq < sequence.length; iSeq++)
/* 410:    */     {
/* 411:930 */       Drawable[] drawables = sequence[iSeq];
/* 412:931 */       for (int iDrbl = 0; iDrbl < drawables.length; iDrbl++) {
/* 413:932 */         drawables[iDrbl].paint(g, viewportTransform);
/* 414:    */       }
/* 415:    */     }
/* 416:936 */     this.renderer.setCulling(false);
/* 417:937 */     this.renderer.disableModelTransform();
/* 418:    */   }
/* 419:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.FixedEyeTruckModel
 * JD-Core Version:    0.7.0.1
 */