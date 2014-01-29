/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import com.jogamp.opengl.util.texture.Texture;
/*   4:    */ import java.awt.Color;
/*   5:    */ import java.awt.Graphics2D;
/*   6:    */ import java.awt.Paint;
/*   7:    */ import javax.media.opengl.GL2;
/*   8:    */ 
/*   9:    */ public class PierModel
/*  10:    */ {
/*  11:    */   private static final float pierHalfWidth = 0.5F;
/*  12:    */   private static final float pierCusp = 0.3F;
/*  13:    */   private static final float pierTaper = 1.3F;
/*  14:    */   private static final float pierBaseShoulder = 0.3F;
/*  15:    */   private static final float pillowHeight = 0.4F;
/*  16:    */   private float height;
/*  17: 38 */   private float[] pillowFrontFace = new float[9];
/*  18: 39 */   private float[] pillowRearFace = new float[9];
/*  19: 40 */   private static final float[] pillowMaterial = { 0.4F, 0.4F, 0.4F, 1.0F };
/*  20: 41 */   private static final float[] pierMaterial = { 0.8F, 0.8F, 0.8F, 1.0F };
/*  21: 42 */   private final Renderer3d renderer = new Renderer3d();
/*  22:    */   
/*  23:    */   private class Prism
/*  24:    */   {
/*  25: 48 */     private final float[] topPolygon = new float[21];
/*  26: 49 */     private final float[] bottomPolygon = new float[21];
/*  27: 50 */     private final float[] sideNormals = new float[18];
/*  28: 51 */     private final float[] topTexCoords = new float[14];
/*  29: 52 */     private final float[] bottomTexCoords = new float[14];
/*  30: 53 */     private final float[] capTexCoords = new float[12];
/*  31: 54 */     private float texScale = 1.0F;
/*  32:    */     
/*  33:    */     public Prism(float w, float h, float d, float c, float taper, float texSize)
/*  34:    */     {
/*  35: 57 */       initialize(w, h, d, c, taper, texSize);
/*  36:    */     }
/*  37:    */     
/*  38:    */     public Prism(float w, float h, float d, float c, float taper)
/*  39:    */     {
/*  40: 61 */       initialize(w, h, d, c, taper);
/*  41:    */     }
/*  42:    */     
/*  43:    */     public Prism() {}
/*  44:    */     
/*  45:    */     public final void initialize(float w, float h, float d, float c, float taper)
/*  46:    */     {
/*  47: 78 */       this.topPolygon[0] = 0.0F;this.topPolygon[1] = 0.0F;this.topPolygon[2] = (-(d + c));
/*  48: 79 */       this.topPolygon[3] = (-w);this.topPolygon[4] = 0.0F;this.topPolygon[5] = (-d);
/*  49: 80 */       this.topPolygon[6] = (-w);this.topPolygon[7] = 0.0F;this.topPolygon[8] = d;
/*  50: 81 */       this.topPolygon[9] = 0.0F;this.topPolygon[10] = 0.0F;this.topPolygon[11] = (d + c);
/*  51: 82 */       this.topPolygon[12] = w;this.topPolygon[13] = 0.0F;this.topPolygon[14] = d;
/*  52: 83 */       this.topPolygon[15] = w;this.topPolygon[16] = 0.0F;this.topPolygon[17] = (-d);
/*  53: 84 */       this.topPolygon[18] = 0.0F;this.topPolygon[19] = 0.0F;this.topPolygon[20] = (-(d + c));
/*  54: 86 */       for (int i = 0; i < this.topPolygon.length; i += 3)
/*  55:    */       {
/*  56: 87 */         this.bottomPolygon[(i + 0)] = (taper * this.topPolygon[(i + 0)]);
/*  57: 88 */         this.bottomPolygon[(i + 1)] = (-h);
/*  58: 89 */         this.bottomPolygon[(i + 2)] = (taper * this.topPolygon[(i + 2)]);
/*  59:    */       }
/*  60: 91 */       for (int i = 3; i < this.topPolygon.length; i += 3)
/*  61:    */       {
/*  62: 92 */         float ax = this.bottomPolygon[(i + 0 - 0)] - this.bottomPolygon[(i + 0 - 3)];
/*  63: 93 */         float ay = this.bottomPolygon[(i + 1 - 0)] - this.bottomPolygon[(i + 1 - 3)];
/*  64: 94 */         float az = this.bottomPolygon[(i + 2 - 0)] - this.bottomPolygon[(i + 2 - 3)];
/*  65: 95 */         float bx = this.topPolygon[(i + 0 - 3)] - this.bottomPolygon[(i + 0 - 3)];
/*  66: 96 */         float by = this.topPolygon[(i + 1 - 3)] - this.bottomPolygon[(i + 1 - 3)];
/*  67: 97 */         float bz = this.topPolygon[(i + 2 - 3)] - this.bottomPolygon[(i + 2 - 3)];
/*  68: 98 */         float nx = ay * bz - az * by;
/*  69: 99 */         float ny = az * bx - ax * bz;
/*  70:100 */         float nz = ax * by - ay * bx;
/*  71:101 */         float len = (float)Math.sqrt(nx * nx + ny * ny + nz * nz);
/*  72:102 */         this.sideNormals[(i + 0 - 3)] = (nx / len);
/*  73:103 */         this.sideNormals[(i + 1 - 3)] = (ny / len);
/*  74:104 */         this.sideNormals[(i + 2 - 3)] = (nz / len);
/*  75:    */       }
/*  76:    */     }
/*  77:    */     
/*  78:    */     public final void initialize(float w, float h, float d, float c, float taper, float texSize)
/*  79:    */     {
/*  80:119 */       initialize(w, h, d, c, taper);
/*  81:120 */       float len = 0.0F;
/*  82:121 */       for (int i = 3; i < this.topPolygon.length; i += 3)
/*  83:    */       {
/*  84:122 */         float dx = this.topPolygon[(i + 0)] - this.topPolygon[(i + 0 - 3)];
/*  85:123 */         float dz = this.topPolygon[(i + 2)] - this.topPolygon[(i + 2 - 3)];
/*  86:124 */         len += (float)Math.sqrt(dx * dx + dz * dz);
/*  87:    */       }
/*  88:127 */       this.texScale = (len / Math.round(len / texSize));
/*  89:128 */       float s = 0.0F;
/*  90:129 */       this.topTexCoords[0] = 0.0F;this.topTexCoords[1] = 0.0F;
/*  91:130 */       this.bottomTexCoords[0] = 0.0F;this.bottomTexCoords[1] = (-h * this.texScale);
/*  92:131 */       int j = 2;
/*  93:132 */       for (int i = 3; i < this.topPolygon.length; j += 2)
/*  94:    */       {
/*  95:133 */         float dx = this.topPolygon[(i + 0)] - this.topPolygon[(i + 0 - 3)];
/*  96:134 */         float dz = this.topPolygon[(i + 2)] - this.topPolygon[(i + 2 - 3)];
/*  97:135 */         s += (float)Math.sqrt(dx * dx + dz * dz);
/*  98:136 */         this.topTexCoords[(j + 0)] = (s * this.texScale);this.topTexCoords[(j + 1)] = 0.0F;
/*  99:137 */         this.bottomTexCoords[(j + 0)] = (s * this.texScale);this.bottomTexCoords[(j + 1)] = (-h * this.texScale);i += 3;
/* 100:    */       }
/* 101:139 */       j = 0;
/* 102:140 */       for (int i = 0; i < this.topPolygon.length - 3; j += 2)
/* 103:    */       {
/* 104:141 */         this.capTexCoords[(j + 0)] = (this.texScale * this.topPolygon[(i + 0)]);
/* 105:142 */         this.capTexCoords[(j + 1)] = (-this.texScale * this.topPolygon[(i + 2)]);i += 3;
/* 106:    */       }
/* 107:    */     }
/* 108:    */     
/* 109:146 */     private Paint[] prismFaceColors = { Bridge3dView.gray00, Bridge3dView.gray25, Bridge3dView.gray30, Bridge3dView.gray50, Bridge3dView.gray75, Bridge3dView.gray00 };
/* 110:155 */     private final Homogeneous.Matrix offset = new Homogeneous.Matrix();
/* 111:    */     
/* 112:    */     public void draw(Graphics2D g, ViewportTransform viewportTransform, float dx, float dy, float dz, int beginFlag)
/* 113:    */     {
/* 114:159 */       this.offset.setTranslation(dx, dy, dz);
/* 115:160 */       PierModel.this.renderer.enableModelTransform(this.offset);
/* 116:161 */       PierModel.this.renderer.begin(beginFlag);
/* 117:162 */       PierModel.this.renderer.setRuleFlags(2);
/* 118:163 */       PierModel.this.renderer.setRulePaint(Color.WHITE);
/* 119:164 */       if (!viewportTransform.isRightOfVanishingPoint(dx)) {
/* 120:165 */         PierModel.this.renderer.addVertex(g, viewportTransform, this.topPolygon, 15, this.bottomPolygon, 15, this.prismFaceColors, 5, -4);
/* 121:    */       } else {
/* 122:172 */         PierModel.this.renderer.addVertex(g, viewportTransform, this.topPolygon, 3, this.bottomPolygon, 3, this.prismFaceColors, 0, 4);
/* 123:    */       }
/* 124:178 */       PierModel.this.renderer.end(g);
/* 125:179 */       if (!viewportTransform.isAboveVanishingPoint(dy))
/* 126:    */       {
/* 127:180 */         PierModel.this.renderer.setPaint(Bridge3dView.gray75);
/* 128:181 */         PierModel.this.renderer.begin(5);
/* 129:182 */         PierModel.this.renderer.addVertex(g, viewportTransform, this.topPolygon, 0, 6);
/* 130:183 */         PierModel.this.renderer.end(g);
/* 131:    */       }
/* 132:185 */       PierModel.this.renderer.disableModelTransform();
/* 133:    */     }
/* 134:    */     
/* 135:    */     public void patch(Graphics2D g, ViewportTransform viewportTransform, float dx, float dy, float dz)
/* 136:    */     {
/* 137:200 */       this.offset.setTranslation(dx, dy, dz);
/* 138:201 */       PierModel.this.renderer.enableModelTransform(this.offset);
/* 139:202 */       PierModel.this.renderer.begin(8);
/* 140:203 */       PierModel.this.renderer.setRuleFlags(2);
/* 141:204 */       PierModel.this.renderer.setRulePaint(Color.WHITE);
/* 142:205 */       PierModel.this.renderer.addVertex(g, viewportTransform, this.topPolygon, 6, this.bottomPolygon, 6, this.prismFaceColors, 1, 3);
/* 143:    */       
/* 144:    */ 
/* 145:    */ 
/* 146:    */ 
/* 147:210 */       PierModel.this.renderer.end(g);
/* 148:211 */       PierModel.this.renderer.disableModelTransform();
/* 149:    */     }
/* 150:    */     
/* 151:    */     public void display(GL2 gl)
/* 152:    */     {
/* 153:215 */       gl.glBegin(7);
/* 154:216 */       int i2 = 2;
/* 155:217 */       for (int i3 = 3; i3 < PierModel.this.pier.topPolygon.length; i2 += 2)
/* 156:    */       {
/* 157:218 */         gl.glNormal3fv(this.sideNormals, i3 - 3);
/* 158:219 */         gl.glTexCoord2fv(this.topTexCoords, i2 - 2);
/* 159:220 */         gl.glVertex3fv(this.topPolygon, i3 - 3);
/* 160:221 */         gl.glTexCoord2fv(this.bottomTexCoords, i2 - 2);
/* 161:222 */         gl.glVertex3fv(this.bottomPolygon, i3 - 3);
/* 162:223 */         gl.glTexCoord2fv(this.bottomTexCoords, i2);
/* 163:224 */         gl.glVertex3fv(this.bottomPolygon, i3);
/* 164:225 */         gl.glTexCoord2fv(this.topTexCoords, i2);
/* 165:226 */         gl.glVertex3fv(this.topPolygon, i3);i3 += 3;
/* 166:    */       }
/* 167:228 */       gl.glEnd();
/* 168:229 */       gl.glBegin(9);
/* 169:230 */       gl.glNormal3f(0.0F, 1.0F, 0.0F);
/* 170:231 */       i2 = 0;
/* 171:232 */       for (int i = 0; i2 < this.capTexCoords.length; i2 += 2)
/* 172:    */       {
/* 173:233 */         gl.glTexCoord2fv(this.capTexCoords, i2);
/* 174:234 */         gl.glVertex3fv(this.topPolygon, i);i += 3;
/* 175:    */       }
/* 176:236 */       gl.glEnd();
/* 177:    */     }
/* 178:    */   }
/* 179:    */   
/* 180:240 */   private final Prism pier = new Prism();
/* 181:241 */   private final Prism base = new Prism();
/* 182:    */   
/* 183:    */   private void initializePillow(float halfDepth)
/* 184:    */   {
/* 185:244 */     this.pillowFrontFace[0] = -0.5F;this.pillowFrontFace[1] = -0.4F;this.pillowFrontFace[2] = halfDepth;
/* 186:245 */     this.pillowFrontFace[3] = 0.0F;this.pillowFrontFace[4] = 0.0F;this.pillowFrontFace[5] = halfDepth;
/* 187:246 */     this.pillowFrontFace[6] = 0.5F;this.pillowFrontFace[7] = -0.4F;this.pillowFrontFace[8] = halfDepth;
/* 188:247 */     this.pillowRearFace[0] = -0.5F;this.pillowRearFace[1] = -0.4F;this.pillowRearFace[2] = (-halfDepth);
/* 189:248 */     this.pillowRearFace[3] = 0.0F;this.pillowRearFace[4] = 0.0F;this.pillowRearFace[5] = (-halfDepth);
/* 190:249 */     this.pillowRearFace[6] = 0.5F;this.pillowRearFace[7] = -0.4F;this.pillowRearFace[8] = (-halfDepth);
/* 191:    */   }
/* 192:    */   
/* 193:    */   public void initialize(float height, float halfDepth, float texSize)
/* 194:    */   {
/* 195:260 */     this.height = height;
/* 196:261 */     initializePillow(halfDepth);
/* 197:262 */     this.pier.initialize(0.5F, height - 0.4F, halfDepth, 0.3F, 1.3F, texSize);
/* 198:263 */     this.base.initialize(0.95F, 2.0F, halfDepth * 1.3F + 0.15F, 0.54F, 1.0F, texSize);
/* 199:    */   }
/* 200:    */   
/* 201:    */   public void initialize(float height, float halfDepth)
/* 202:    */   {
/* 203:280 */     this.height = height;
/* 204:281 */     this.pier.initialize(0.5F, height - 0.4F, halfDepth, 0.3F, 1.3F);
/* 205:282 */     this.base.initialize(0.95F, 2.0F, halfDepth * 1.3F + 0.15F, 0.54F, 1.0F);
/* 206:    */   }
/* 207:    */   
/* 208:    */   public void paint(Graphics2D g, ViewportTransform viewportTransform, float dx, float dy, float dz)
/* 209:    */   {
/* 210:291 */     this.base.draw(g, viewportTransform, dx, dy - this.height, dz, 8);
/* 211:292 */     this.pier.draw(g, viewportTransform, dx, dy - 0.4F, dz, 4);
/* 212:    */   }
/* 213:    */   
/* 214:    */   public void patch(Graphics2D g, ViewportTransform viewportTransform, float dx, float dy, float dz)
/* 215:    */   {
/* 216:296 */     this.base.patch(g, viewportTransform, dx, dy - this.height, dz);
/* 217:    */   }
/* 218:    */   
/* 219:    */   public void display(GL2 gl, Texture texture)
/* 220:    */   {
/* 221:305 */     gl.glColor3fv(pierMaterial, 0);
/* 222:306 */     gl.glActiveTexture(33984);
/* 223:307 */     texture.enable(gl);
/* 224:308 */     texture.bind(gl);
/* 225:309 */     gl.glPushMatrix();
/* 226:310 */     gl.glTranslatef(0.0F, -0.4F, 0.0F);
/* 227:311 */     this.pier.display(gl);
/* 228:312 */     gl.glPopMatrix();
/* 229:313 */     gl.glPushMatrix();
/* 230:314 */     gl.glTranslatef(0.0F, -this.height, 0.0F);
/* 231:315 */     this.base.display(gl);
/* 232:316 */     gl.glPopMatrix();
/* 233:317 */     texture.disable(gl);
/* 234:    */     
/* 235:    */ 
/* 236:320 */     gl.glColor3fv(pillowMaterial, 0);
/* 237:321 */     gl.glBegin(4);
/* 238:322 */     gl.glNormal3f(0.0F, 0.0F, 1.0F);
/* 239:323 */     for (int i = this.pillowFrontFace.length - 3; i >= 0; i -= 3) {
/* 240:324 */       gl.glVertex3fv(this.pillowFrontFace, i);
/* 241:    */     }
/* 242:326 */     gl.glNormal3f(0.0F, 0.0F, -1.0F);
/* 243:327 */     for (int i = 0; i < this.pillowRearFace.length; i += 3) {
/* 244:328 */       gl.glVertex3fv(this.pillowRearFace, i);
/* 245:    */     }
/* 246:330 */     gl.glEnd();
/* 247:331 */     gl.glBegin(7);
/* 248:332 */     int j = 0;
/* 249:333 */     for (int i = 3; i < this.pillowFrontFace.length; i += 3)
/* 250:    */     {
/* 251:334 */       float dx = this.pillowFrontFace[(i + 0)] - this.pillowFrontFace[(j + 0)];
/* 252:335 */       float dy = this.pillowFrontFace[(i + 1)] - this.pillowFrontFace[(j + 1)];
/* 253:336 */       gl.glNormal3f(-dy, dx, 0.0F);
/* 254:337 */       gl.glVertex3fv(this.pillowRearFace, j);
/* 255:338 */       gl.glVertex3fv(this.pillowFrontFace, j);
/* 256:339 */       gl.glVertex3fv(this.pillowFrontFace, i);
/* 257:340 */       gl.glVertex3fv(this.pillowRearFace, i);j = i;
/* 258:    */     }
/* 259:342 */     gl.glEnd();
/* 260:    */   }
/* 261:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.PierModel
 * JD-Core Version:    0.7.0.1
 */