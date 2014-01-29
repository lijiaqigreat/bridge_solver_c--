/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import com.jogamp.opengl.util.texture.Texture;
/*   4:    */ import javax.media.opengl.GL2;
/*   5:    */ 
/*   6:    */ public class FlyThruTerrainModel
/*   7:    */   extends TerrainModel
/*   8:    */ {
/*   9: 31 */   private final FlyThruTowerModel towerModel = new FlyThruTowerModel();
/*  10: 32 */   private final PierModel pierModel = new PierModel();
/*  11:    */   private final FlyThruAnimation.Config config;
/*  12: 36 */   private Texture pierTexture = null;
/*  13: 37 */   private Texture waterTexture = null;
/*  14: 38 */   private String[] skyTextureNames = { "skyup.jpg", "skye.jpg", "skyn.jpg", "skyw.jpg", "skys.jpg" };
/*  15: 39 */   private Texture[] skyTextures = new Texture[5];
/*  16: 40 */   private static final float[] skyQuadsInTerrainCoords = { 192.0F, 192.0F, 192.0F, -192.0F, 192.0F, 192.0F, -192.0F, 192.0F, -192.0F, 192.0F, 192.0F, -192.0F, 192.0F, 192.0F, 192.0F, 192.0F, 192.0F, -192.0F, 192.0F, -192.0F, -192.0F, 192.0F, -192.0F, 192.0F, 192.0F, 192.0F, -192.0F, -192.0F, 192.0F, -192.0F, -192.0F, -192.0F, -192.0F, 192.0F, -192.0F, -192.0F, -192.0F, 192.0F, -192.0F, -192.0F, 192.0F, 192.0F, -192.0F, -192.0F, 192.0F, -192.0F, -192.0F, -192.0F, -192.0F, 192.0F, 192.0F, 192.0F, 192.0F, 192.0F, 192.0F, -192.0F, 192.0F, -192.0F, -192.0F, 192.0F };
/*  17: 68 */   private final float[] skyQuads = new float[skyQuadsInTerrainCoords.length];
/*  18: 70 */   private float[] skyQuadTexCoords = { 1.0F, 1.0F, 0.0F, 1.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, 1.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, 1.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, 1.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, 1.0F, 0.0F, 0.0F, 1.0F, 0.0F };
/*  19:    */   
/*  20:    */   public FlyThruTerrainModel(FlyThruAnimation.Config config)
/*  21:    */   {
/*  22:134 */     this.config = config;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public void initializeTerrainTextures(GL2 gl)
/*  26:    */   {
/*  27:143 */     gl.glActiveTexture(33984);
/*  28:144 */     this.pierTexture = WPBDApp.getApplication().getTextureResource("bricktile.png", true, "png");
/*  29:145 */     this.pierTexture.setTexParameteri(gl, 10242, 10497);
/*  30:146 */     this.pierTexture.setTexParameteri(gl, 10243, 10497);
/*  31:147 */     this.pierTexture.setTexParameteri(gl, 10240, 9729);
/*  32:148 */     this.pierTexture.setTexParameteri(gl, 10241, 9985);
/*  33:    */     
/*  34:150 */     this.waterTexture = WPBDApp.getApplication().getTextureResource("water.jpg", true, "jpg");
/*  35:151 */     this.waterTexture.setTexParameteri(gl, 10242, 10497);
/*  36:152 */     this.waterTexture.setTexParameteri(gl, 10243, 10497);
/*  37:153 */     this.waterTexture.setTexParameteri(gl, 10240, 9729);
/*  38:154 */     this.waterTexture.setTexParameteri(gl, 10241, 9985);
/*  39:156 */     for (int i = 0; i < 5; i++)
/*  40:    */     {
/*  41:157 */       this.skyTextures[i] = WPBDApp.getApplication().getTextureResource(this.skyTextureNames[i], false, "jpg");
/*  42:158 */       this.skyTextures[i].setTexParameteri(gl, 10242, 33071);
/*  43:159 */       this.skyTextures[i].setTexParameteri(gl, 10243, 33071);
/*  44:160 */       this.skyTextures[i].setTexParameteri(gl, 10240, 9729);
/*  45:161 */       this.skyTextures[i].setTexParameteri(gl, 10241, 9728);
/*  46:    */     }
/*  47:    */   }
/*  48:    */   
/*  49:    */   public void initializeTerrain(DesignConditions conditions, float trussCenterOffset, float abutmentHalfWidth)
/*  50:    */   {
/*  51:176 */     super.initializeTerrain(conditions, trussCenterOffset, abutmentHalfWidth);
/*  52:179 */     if (conditions.isPier()) {
/*  53:180 */       this.pierModel.initialize((float)conditions.getPierHeight(), abutmentHalfWidth, 0.3F);
/*  54:    */     }
/*  55:184 */     for (int i = 0; i < this.skyQuads.length; i += 3)
/*  56:    */     {
/*  57:185 */       this.skyQuads[(i + 0)] = (skyQuadsInTerrainCoords[(i + 0)] + this.halfSpanLength);
/*  58:186 */       this.skyQuads[(i + 1)] = skyQuadsInTerrainCoords[(i + 1)];
/*  59:187 */       this.skyQuads[(i + 2)] = skyQuadsInTerrainCoords[(i + 2)];
/*  60:    */     }
/*  61:    */   }
/*  62:    */   
/*  63:    */   private void paintAbutment(GL2 gl)
/*  64:    */   {
/*  65:198 */     gl.glColor3fv(abutmentMaterial, 0);
/*  66:199 */     gl.glActiveTexture(33984);
/*  67:200 */     this.pierTexture.enable(gl);
/*  68:201 */     this.pierTexture.bind(gl);
/*  69:    */     
/*  70:203 */     gl.glBegin(6);
/*  71:204 */     gl.glNormal3f(0.0F, 0.0F, 1.0F);
/*  72:205 */     int i2 = 0;
/*  73:206 */     for (int i3 = 0; i3 < this.abutmentFrontFlank.length; i2 += 2)
/*  74:    */     {
/*  75:207 */       gl.glTexCoord2fv(this.abutmentFrontFlankTexture, i2);
/*  76:208 */       gl.glVertex3fv(this.abutmentFrontFlank, i3);i3 += 3;
/*  77:    */     }
/*  78:210 */     gl.glEnd();
/*  79:    */     
/*  80:212 */     gl.glBegin(6);
/*  81:213 */     gl.glNormal3f(0.0F, 0.0F, -1.0F);
/*  82:214 */     i2 = 0;
/*  83:215 */     for (int i3 = 0; i3 < this.abutmentRearFlank.length; i2 += 2)
/*  84:    */     {
/*  85:216 */       gl.glTexCoord2fv(this.abutmentRearFlankTexture, i2);
/*  86:217 */       gl.glVertex3fv(this.abutmentRearFlank, i3);i3 += 3;
/*  87:    */     }
/*  88:219 */     gl.glEnd();
/*  89:    */     
/*  90:221 */     gl.glBegin(7);
/*  91:222 */     i2 = 0;
/*  92:223 */     for (int i3 = 0; i3 < this.abutmentFaceNormals.length; i2 += 2)
/*  93:    */     {
/*  94:224 */       gl.glNormal3fv(this.abutmentFaceNormals, i3);
/*  95:    */       
/*  96:226 */       gl.glTexCoord2fv(this.abutmentRearFaceTexture, i2);
/*  97:227 */       gl.glVertex3fv(this.abutmentRearFace, i3);
/*  98:    */       
/*  99:229 */       gl.glTexCoord2fv(this.abutmentFrontFaceTexture, i2);
/* 100:230 */       gl.glVertex3fv(this.abutmentFrontFace, i3);
/* 101:    */       
/* 102:232 */       gl.glTexCoord2fv(this.abutmentFrontFaceTexture, i2 + 2);
/* 103:233 */       gl.glVertex3fv(this.abutmentFrontFace, i3 + 3);
/* 104:    */       
/* 105:235 */       gl.glTexCoord2fv(this.abutmentRearFaceTexture, i2 + 2);
/* 106:236 */       gl.glVertex3fv(this.abutmentRearFace, i3 + 3);i3 += 3;
/* 107:    */     }
/* 108:238 */     gl.glEnd();
/* 109:239 */     this.pierTexture.disable(gl);
/* 110:    */     
/* 111:    */ 
/* 112:242 */     gl.glColor3fv(flatTerrainMaterial, 0);
/* 113:243 */     gl.glBegin(8);
/* 114:244 */     gl.glNormal3f(0.0F, 1.0F, 0.0F);
/* 115:245 */     for (int i = 0; i < this.abutmentFrontTop.length; i += 3)
/* 116:    */     {
/* 117:246 */       gl.glVertex3fv(this.abutmentRearTop, i);
/* 118:247 */       gl.glVertex3fv(this.abutmentFrontTop, i);
/* 119:    */     }
/* 120:249 */     gl.glEnd();
/* 121:    */     
/* 122:    */ 
/* 123:252 */     gl.glColor3fv(pillowMaterial, 0);
/* 124:253 */     gl.glBegin(4);
/* 125:254 */     gl.glNormal3f(0.0F, 0.0F, 1.0F);
/* 126:255 */     for (int i = this.pillowFrontFace.length - 3; i >= 0; i -= 3) {
/* 127:256 */       gl.glVertex3fv(this.pillowFrontFace, i);
/* 128:    */     }
/* 129:258 */     gl.glNormal3f(0.0F, 0.0F, -1.0F);
/* 130:259 */     for (int i = 0; i < this.pillowRearFace.length; i += 3) {
/* 131:260 */       gl.glVertex3fv(this.pillowRearFace, i);
/* 132:    */     }
/* 133:262 */     gl.glEnd();
/* 134:263 */     gl.glBegin(7);
/* 135:264 */     int j = 0;
/* 136:265 */     for (int i = 3; i < this.pillowFrontFace.length; i += 3)
/* 137:    */     {
/* 138:266 */       float dx = this.pillowFrontFace[(i + 0)] - this.pillowFrontFace[(j + 0)];
/* 139:267 */       float dy = this.pillowFrontFace[(i + 1)] - this.pillowFrontFace[(j + 1)];
/* 140:268 */       gl.glNormal3f(-dy, dx, 0.0F);
/* 141:269 */       gl.glVertex3fv(this.pillowRearFace, j);
/* 142:270 */       gl.glVertex3fv(this.pillowFrontFace, j);
/* 143:271 */       gl.glVertex3fv(this.pillowFrontFace, i);
/* 144:272 */       gl.glVertex3fv(this.pillowRearFace, i);j = i;
/* 145:    */     }
/* 146:274 */     gl.glEnd();
/* 147:    */   }
/* 148:    */   
/* 149:    */   private void drawPowerLines(GL2 gl)
/* 150:    */   {
/* 151:285 */     for (int iTower = 0; iTower < 4; iTower++)
/* 152:    */     {
/* 153:286 */       gl.glPushMatrix();
/* 154:287 */       Homogeneous.Point p = this.towerPt[iTower];
/* 155:288 */       gl.glTranslatef(p.x(), p.y(), p.z());
/* 156:289 */       gl.glRotatef(thetaTower, 0.0F, 1.0F, 0.0F);
/* 157:290 */       this.towerModel.display(gl);
/* 158:291 */       gl.glPopMatrix();
/* 159:    */     }
/* 160:295 */     if (!this.drawingShadows)
/* 161:    */     {
/* 162:296 */       gl.glColor3fv(wireColor, 0);
/* 163:297 */       for (int iOffset = 0; iOffset < wireOffsets.length; iOffset++)
/* 164:    */       {
/* 165:298 */         float xOfs = xUnitPerpTower * wireOffsets[iOffset].x();
/* 166:299 */         float yOfs = wireOffsets[iOffset].y();
/* 167:300 */         float zOfs = zUnitPerpTower * wireOffsets[iOffset].x();
/* 168:301 */         gl.glBegin(3);
/* 169:302 */         for (int iTower = 0; iTower < this.wirePt.length; iTower++) {
/* 170:303 */           for (int iWire = iTower == 0 ? 0 : 1; iWire < this.wirePt[0].length; iWire++)
/* 171:    */           {
/* 172:304 */             Homogeneous.Point p = this.wirePt[iTower][iWire];
/* 173:305 */             gl.glVertex3f(p.x() + xOfs, p.y() + yOfs, p.z() + zOfs);
/* 174:    */           }
/* 175:    */         }
/* 176:308 */         gl.glEnd();
/* 177:    */       }
/* 178:    */     }
/* 179:    */   }
/* 180:    */   
/* 181:    */   private void drawStaticTerrain(GL2 gl)
/* 182:    */   {
/* 183:314 */     if (this.config.showTerrain)
/* 184:    */     {
/* 185:316 */       float zNorth = zGridToWorld(0);
/* 186:317 */       float zSouth = zNorth + this.metersPerGrid;
/* 187:318 */       gl.glShadeModel(7425);
/* 188:319 */       gl.glColor3fv(flatTerrainMaterial, 0);
/* 189:320 */       for (int iNorth = 0; iNorth < this.gridCount; iNorth++)
/* 190:    */       {
/* 191:321 */         int iSouth = iNorth + 1;
/* 192:322 */         gl.glBegin(5);
/* 193:323 */         float x = xGridToWorld(0);
/* 194:324 */         for (int j = 0; j < this.postCount; j++)
/* 195:    */         {
/* 196:325 */           TerrainModel.TerrainPost n = this.posts[iNorth][j];
/* 197:326 */           TerrainModel.TerrainPost s = this.posts[iSouth][j];
/* 198:329 */           if (this.config.showErrosion) {
/* 199:330 */             gl.glColor3fv(n.yNormal > 0.8F ? flatTerrainMaterial : verticalTerrainMaterial, 0);
/* 200:    */           }
/* 201:332 */           gl.glNormal3f(n.xNormal, n.yNormal, n.zNormal);
/* 202:333 */           gl.glVertex3f(x, n.elevation, zNorth);
/* 203:334 */           if (this.config.showErrosion) {
/* 204:335 */             gl.glColor3fv(s.yNormal > 0.8F ? flatTerrainMaterial : verticalTerrainMaterial, 0);
/* 205:    */           }
/* 206:337 */           gl.glNormal3f(s.xNormal, s.yNormal, s.zNormal);
/* 207:338 */           gl.glVertex3f(x, s.elevation, zSouth);
/* 208:339 */           x += this.metersPerGrid;
/* 209:    */         }
/* 210:341 */         gl.glEnd();
/* 211:342 */         zNorth = zSouth;
/* 212:343 */         zSouth += this.metersPerGrid;
/* 213:    */       }
/* 214:347 */       gl.glColor3fv(roadMaterial, 0);
/* 215:348 */       gl.glBegin(8);
/* 216:349 */       float x = xGridToWorld(0);
/* 217:350 */       for (int j = 0; j < this.postCount; j++)
/* 218:    */       {
/* 219:351 */         gl.glNormal3f(this.roadCenterline[j].xNormal, this.roadCenterline[j].yNormal, 0.0F);
/* 220:352 */         if (x >= -0.45F)
/* 221:    */         {
/* 222:353 */           gl.glVertex3f(-0.45F, 0.8F, -5.0F);
/* 223:354 */           gl.glVertex3f(-0.45F, 0.8F, 5.0F);
/* 224:355 */           break;
/* 225:    */         }
/* 226:358 */         gl.glVertex3f(x, this.roadCenterline[j].elevation, -5.0F);
/* 227:359 */         gl.glVertex3f(x, this.roadCenterline[j].elevation, 5.0F);
/* 228:    */         
/* 229:361 */         x += this.metersPerGrid;
/* 230:    */       }
/* 231:363 */       gl.glEnd();
/* 232:    */       
/* 233:    */ 
/* 234:366 */       gl.glColor3fv(roadMaterial, 0);
/* 235:367 */       gl.glBegin(8);
/* 236:368 */       x = xGridToWorld(this.gridCount);
/* 237:369 */       float xDeckEnd = 2.0F * this.halfSpanLength - -0.45F;
/* 238:370 */       for (int j = this.gridCount; j >= 0; j--)
/* 239:    */       {
/* 240:371 */         gl.glNormal3f(this.roadCenterline[j].xNormal, this.roadCenterline[j].yNormal, 0.0F);
/* 241:372 */         if (x <= xDeckEnd)
/* 242:    */         {
/* 243:373 */           gl.glVertex3f(xDeckEnd, 0.8F, 5.0F);
/* 244:374 */           gl.glVertex3f(xDeckEnd, 0.8F, -5.0F);
/* 245:375 */           break;
/* 246:    */         }
/* 247:378 */         gl.glVertex3f(x, this.roadCenterline[j].elevation, 5.0F);
/* 248:379 */         gl.glVertex3f(x, this.roadCenterline[j].elevation, -5.0F);
/* 249:    */         
/* 250:381 */         x -= this.metersPerGrid;
/* 251:    */       }
/* 252:383 */       gl.glEnd();
/* 253:    */       
/* 254:385 */       drawPowerLines(gl);
/* 255:    */     }
/* 256:388 */     if (this.config.showAbutments)
/* 257:    */     {
/* 258:390 */       paintAbutment(gl);
/* 259:    */       
/* 260:    */ 
/* 261:393 */       gl.glPushMatrix();
/* 262:394 */       gl.glTranslatef(2.0F * this.halfSpanLength, 0.0F, 0.0F);
/* 263:395 */       gl.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
/* 264:396 */       paintAbutment(gl);
/* 265:397 */       gl.glPopMatrix();
/* 266:400 */       if (this.pierLocation != null)
/* 267:    */       {
/* 268:401 */         gl.glPushMatrix();
/* 269:402 */         gl.glTranslated(this.pierLocation.x, this.pierLocation.y - 0.1D, 0.0D);
/* 270:403 */         this.pierModel.display(gl, this.pierTexture);
/* 271:404 */         gl.glPopMatrix();
/* 272:    */       }
/* 273:    */     }
/* 274:    */   }
/* 275:    */   
/* 276:    */   private void drawEnvironment(GL2 gl)
/* 277:    */   {
/* 278:410 */     gl.glActiveTexture(33984);
/* 279:412 */     if (this.config.showTerrain)
/* 280:    */     {
/* 281:413 */       long t = System.nanoTime();
/* 282:414 */       float elapsed = this.tLast >= 0L ? (float)(t - this.tLast) * 1.0E-009F : 0.0F;
/* 283:415 */       this.tLast = t;
/* 284:416 */       this.waterTexture.enable(gl);
/* 285:417 */       this.waterTexture.bind(gl);
/* 286:418 */       gl.glColor3fv(white, 0);
/* 287:419 */       gl.glBegin(7);
/* 288:420 */       gl.glNormal3f(0.0F, 1.0F, 0.0F);
/* 289:421 */       float x0 = xGridToWorld(0);
/* 290:422 */       float x1 = xGridToWorld(this.postCount - 1);
/* 291:423 */       float z0 = zGridToWorld(0);
/* 292:424 */       float z1 = zGridToWorld(this.postCount - 1);
/* 293:425 */       float tf = 0.2F;
/* 294:426 */       this.dWater += elapsed * 0.2F;
/* 295:427 */       while (this.dWater > 1.0F) {
/* 296:428 */         this.dWater -= 1.0F;
/* 297:    */       }
/* 298:430 */       float x0t = tf * x0 - this.dWater;
/* 299:431 */       float x1t = tf * x1 - this.dWater;
/* 300:432 */       float z0t = tf * z0 - this.dWater;
/* 301:433 */       float z1t = tf * z1 - this.dWater;
/* 302:434 */       gl.glTexCoord2f(x0t, z0t);
/* 303:435 */       gl.glVertex3f(x0, this.yWater, z0);
/* 304:436 */       gl.glTexCoord2f(x0t, z1t);
/* 305:437 */       gl.glVertex3f(x0, this.yWater, z1);
/* 306:438 */       gl.glTexCoord2f(x1t, z1t);
/* 307:439 */       gl.glVertex3f(x1, this.yWater, z1);
/* 308:440 */       gl.glTexCoord2f(x1t, z0t);
/* 309:441 */       gl.glVertex3f(x1, this.yWater, z0);
/* 310:442 */       gl.glEnd();
/* 311:443 */       this.waterTexture.disable(gl);
/* 312:    */     }
/* 313:447 */     if ((this.config.showSky) && (!this.drawingShadows))
/* 314:    */     {
/* 315:448 */       int iTex = 0;
/* 316:449 */       int iQuad = 0;
/* 317:450 */       gl.glColor3fv(white, 0);
/* 318:452 */       for (int i = 0; i < this.skyTextureNames.length; i++)
/* 319:    */       {
/* 320:453 */         this.skyTextures[i].enable(gl);
/* 321:    */         
/* 322:455 */         this.skyTextures[i].bind(gl);
/* 323:456 */         gl.glBegin(7);
/* 324:457 */         for (int j = 0; j < 4; iQuad += 3)
/* 325:    */         {
/* 326:458 */           gl.glTexCoord2fv(this.skyQuadTexCoords, iTex);
/* 327:459 */           gl.glVertex3fv(this.skyQuads, iQuad);j++;iTex += 2;
/* 328:    */         }
/* 329:461 */         gl.glEnd();
/* 330:462 */         this.skyTextures[i].disable(gl);
/* 331:    */       }
/* 332:    */     }
/* 333:    */   }
/* 334:    */   
/* 335:    */   public void paint(GL2 gl, boolean shadows)
/* 336:    */   {
/* 337:474 */     this.drawingShadows = shadows;
/* 338:    */     
/* 339:476 */     gl.glDisable(2977);
/* 340:477 */     drawStaticTerrain(gl);
/* 341:478 */     drawEnvironment(gl);
/* 342:479 */     gl.glEnable(2977);
/* 343:    */   }
/* 344:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.FlyThruTerrainModel
 * JD-Core Version:    0.7.0.1
 */