/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.awt.Color;
/*   4:    */ import java.awt.Cursor;
/*   5:    */ import java.awt.Graphics2D;
/*   6:    */ import java.awt.Image;
/*   7:    */ import java.awt.Point;
/*   8:    */ import java.awt.Rectangle;
/*   9:    */ import javax.media.opengl.GL2;
/*  10:    */ import org.jdesktop.application.ResourceMap;
/*  11:    */ 
/*  12:    */ public class Joint
/*  13:    */   implements HotEditableItem<BridgePaintContext>
/*  14:    */ {
/*  15:    */   public static final int pixelRadius = 8;
/*  16:    */   public static final double radiusWorld = 0.1D;
/*  17: 34 */   public static final float[] jointMaterial = { 0.5F, 0.25F, 0.0F, 1.0F };
/*  18:    */   private static final float capHeight = 0.1F;
/*  19:    */   private static final float capProtrusion = 0.03F;
/*  20: 46 */   private Affine.Point ptWorld = new Affine.Point();
/*  21:    */   private final boolean fixed;
/*  22: 54 */   private boolean selected = false;
/*  23: 58 */   private int index = -1;
/*  24: 62 */   public static final Image fixedJointImage = WPBDApp.getApplication().getImageResource("fixedjoint.png");
/*  25: 63 */   public static final Image normalJointImage = WPBDApp.getApplication().getImageResource("normaljoint.png");
/*  26: 64 */   public static final Image selectedJointImage = WPBDApp.getApplication().getImageResource("selectedjoint.png");
/*  27: 65 */   public static final Image hotJointImage = WPBDApp.getApplication().getImageResource("hotjoint.png");
/*  28: 66 */   public static final Image hotSelectedJointImage = WPBDApp.getApplication().getImageResource("hotselectedjoint.png");
/*  29:    */   private static final int nSegments = 16;
/*  30:    */   private static final float[] cylinderNormals;
/*  31:    */   private static final float[] cylinderFrontVertices;
/*  32:    */   private static final float[] cylinderRearVertices;
/*  33:    */   private static final float[] acornVertices;
/*  34: 81 */   private static final Homogeneous.Matrix rotateAboutX = new Homogeneous.Matrix(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
/*  35:    */   
/*  36:    */   public Joint(int index, Affine.Point ptWorld, boolean fixed)
/*  37:    */   {
/*  38: 95 */     this.index = index;
/*  39: 96 */     this.fixed = fixed;
/*  40: 97 */     this.ptWorld.setLocation(ptWorld);
/*  41:    */   }
/*  42:    */   
/*  43:    */   public Joint(int index, Affine.Point ptWorld)
/*  44:    */   {
/*  45:107 */     this(index, ptWorld, false);
/*  46:    */   }
/*  47:    */   
/*  48:    */   public Joint(Affine.Point ptWorld)
/*  49:    */   {
/*  50:116 */     this(-1, ptWorld, false);
/*  51:    */   }
/*  52:    */   
/*  53:    */   public void swapContents(Editable otherJoint)
/*  54:    */   {
/*  55:125 */     Joint other = (Joint)otherJoint;
/*  56:126 */     Affine.Point tmpPtWorld = this.ptWorld;
/*  57:127 */     this.ptWorld = other.ptWorld;
/*  58:128 */     other.ptWorld = tmpPtWorld;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public boolean isFixed()
/*  62:    */   {
/*  63:136 */     return this.fixed;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public Affine.Point getPointWorld()
/*  67:    */   {
/*  68:145 */     return this.ptWorld;
/*  69:    */   }
/*  70:    */   
/*  71:    */   public void setIndex(int index)
/*  72:    */   {
/*  73:154 */     this.index = index;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public int getIndex()
/*  77:    */   {
/*  78:163 */     return this.index;
/*  79:    */   }
/*  80:    */   
/*  81:    */   public int getNumber()
/*  82:    */   {
/*  83:172 */     return this.index + 1;
/*  84:    */   }
/*  85:    */   
/*  86:    */   public boolean isSelected()
/*  87:    */   {
/*  88:181 */     return this.selected;
/*  89:    */   }
/*  90:    */   
/*  91:    */   public boolean setSelected(boolean selected)
/*  92:    */   {
/*  93:191 */     if (this.selected != selected)
/*  94:    */     {
/*  95:192 */       this.selected = selected;
/*  96:193 */       return true;
/*  97:    */     }
/*  98:195 */     return false;
/*  99:    */   }
/* 100:    */   
/* 101:    */   boolean isAt(Affine.Point ptWorld)
/* 102:    */   {
/* 103:205 */     return this.ptWorld.distanceSq(ptWorld) < 1.0E-012D;
/* 104:    */   }
/* 105:    */   
/* 106:208 */   private final Point ptViewport = new Point();
/* 107:    */   
/* 108:    */   private void drawJointImage(Graphics2D g, ViewportTransform viewportTransform, Image image)
/* 109:    */   {
/* 110:211 */     viewportTransform.worldToViewport(this.ptViewport, this.ptWorld);
/* 111:212 */     g.drawImage(image, this.ptViewport.x - 8, this.ptViewport.y - 8, null);
/* 112:    */   }
/* 113:    */   
/* 114:    */   public void paint(Graphics2D g, ViewportTransform viewportTransform, BridgePaintContext ctx)
/* 115:    */   {
/* 116:223 */     if (ctx.blueprint)
/* 117:    */     {
/* 118:225 */       viewportTransform.worldToViewport(this.ptViewport, this.ptWorld);
/* 119:226 */       int r = viewportTransform.worldToViewportDistance(0.1D);
/* 120:228 */       if (ctx.gussets != null)
/* 121:    */       {
/* 122:229 */         ctx.gussets[this.index].paint(g, viewportTransform);
/* 123:    */       }
/* 124:    */       else
/* 125:    */       {
/* 126:233 */         g.setColor(Color.WHITE);
/* 127:234 */         g.fillOval(this.ptViewport.x - r, this.ptViewport.y - r, 2 * r, 2 * r);
/* 128:235 */         g.setColor(Color.BLACK);
/* 129:236 */         g.drawOval(this.ptViewport.x - r, this.ptViewport.y - r, 2 * r, 2 * r);
/* 130:    */       }
/* 131:239 */       r = (int)(r * 0.5D);
/* 132:240 */       g.drawLine(this.ptViewport.x - r - 1, this.ptViewport.y, this.ptViewport.x + r, this.ptViewport.y);
/* 133:241 */       g.drawLine(this.ptViewport.x, this.ptViewport.y - r - 1, this.ptViewport.x, this.ptViewport.y + r);
/* 134:    */     }
/* 135:    */     else
/* 136:    */     {
/* 137:245 */       drawJointImage(g, viewportTransform, isSelected() ? selectedJointImage : isFixed() ? fixedJointImage : normalJointImage);
/* 138:    */     }
/* 139:    */   }
/* 140:    */   
/* 141:    */   public void paintHot(Graphics2D g, ViewportTransform viewportTransform, BridgePaintContext ctx)
/* 142:    */   {
/* 143:259 */     drawJointImage(g, viewportTransform, isSelected() ? hotSelectedJointImage : hotJointImage);
/* 144:    */   }
/* 145:    */   
/* 146:    */   static
/* 147:    */   {
/* 148:266 */     int nNormalComponents = 48;
/* 149:267 */     cylinderNormals = new float[48];
/* 150:268 */     cylinderFrontVertices = new float[51];
/* 151:269 */     cylinderRearVertices = new float[51];
/* 152:270 */     acornVertices = new float[51]; float 
/* 153:    */     
/* 154:272 */       tmp153_152 = (acornVertices[0] = 1.0F);cylinderRearVertices[0] = tmp153_152;cylinderFrontVertices[0] = tmp153_152; float 
/* 155:273 */       tmp171_170 = (acornVertices[1] = 0.0F);cylinderRearVertices[1] = tmp171_170;cylinderFrontVertices[1] = tmp171_170;
/* 156:274 */     cylinderFrontVertices[2] = 1.0F;
/* 157:275 */     cylinderRearVertices[2] = -1.0F;
/* 158:276 */     acornVertices[2] = 0.0F;
/* 159:277 */     double dTheta = 0.3926990816987241D;
/* 160:278 */     double theta = 0.0D;
/* 161:279 */     for (int i = 0; i < 48; tmp274_273 += 3)
/* 162:    */     {
/* 163:280 */       theta += 0.3926990816987241D; float 
/* 164:281 */         tmp243_242 = (acornVertices[(i + 3)] = (float)Math.cos(theta));cylinderRearVertices[(i + 3)] = tmp243_242;cylinderFrontVertices[(i + 3)] = tmp243_242; float 
/* 165:282 */         tmp274_273 = (acornVertices[(i + 4)] = (float)Math.sin(theta));cylinderRearVertices[(i + 4)] = tmp274_273;cylinderFrontVertices[(i + 4)] = tmp274_273;
/* 166:283 */       cylinderFrontVertices[(tmp274_273 + 5)] = 1.0F;cylinderRearVertices[(tmp274_273 + 5)] = -1.0F;acornVertices[(tmp274_273 + 5)] = 0.0F;
/* 167:284 */       cylinderNormals[(tmp274_273 + 0)] = cylinderFrontVertices[(tmp274_273 + 3)];
/* 168:285 */       cylinderNormals[(tmp274_273 + 1)] = cylinderFrontVertices[(tmp274_273 + 4)];
/* 169:286 */       cylinderNormals[(tmp274_273 + 2)] = 0.0F;
/* 170:    */     }
/* 171:    */   }
/* 172:    */   
/* 173:    */   private void paintAcorn(GL2 gl)
/* 174:    */   {
/* 175:291 */     gl.glBegin(6);
/* 176:292 */     gl.glNormal3f(0.0F, 0.0F, 1.0F);
/* 177:293 */     gl.glVertex3f(0.0F, 0.0F, 0.1F);
/* 178:294 */     float zNormal = 0.1F;
/* 179:295 */     for (int i = 0; i < acornVertices.length; i += 3)
/* 180:    */     {
/* 181:296 */       gl.glNormal3f(acornVertices[i], acornVertices[(i + 1)], 0.1F);
/* 182:297 */       gl.glVertex3fv(acornVertices, i);
/* 183:    */     }
/* 184:299 */     gl.glNormal3f(acornVertices[0], acornVertices[1], 0.1F);
/* 185:300 */     gl.glVertex3fv(acornVertices, 0);
/* 186:301 */     gl.glEnd();
/* 187:    */   }
/* 188:    */   
/* 189:    */   private void paintRod(GL2 gl, double halfLength)
/* 190:    */   {
/* 191:307 */     gl.glShadeModel(7425);
/* 192:    */     
/* 193:    */ 
/* 194:310 */     gl.glPushMatrix();
/* 195:311 */     gl.glScaled(0.1D, 0.1D, halfLength);
/* 196:312 */     gl.glColor3fv(jointMaterial, 0);
/* 197:313 */     gl.glBegin(8);
/* 198:314 */     for (int i = 0; i < cylinderNormals.length; i += 3)
/* 199:    */     {
/* 200:315 */       gl.glNormal3fv(cylinderNormals, i);
/* 201:316 */       gl.glVertex3fv(cylinderFrontVertices, i);
/* 202:317 */       gl.glVertex3fv(cylinderRearVertices, i);
/* 203:    */     }
/* 204:319 */     gl.glNormal3fv(cylinderNormals, 0);
/* 205:320 */     gl.glVertex3fv(cylinderFrontVertices, 0);
/* 206:321 */     gl.glVertex3fv(cylinderRearVertices, 0);
/* 207:322 */     gl.glEnd();
/* 208:323 */     gl.glPopMatrix();
/* 209:    */     
/* 210:    */ 
/* 211:326 */     gl.glPushMatrix();
/* 212:327 */     gl.glTranslated(0.0D, 0.0D, halfLength);
/* 213:328 */     gl.glScaled(0.1D, 0.1D, 1.0D);
/* 214:329 */     paintAcorn(gl);
/* 215:330 */     gl.glPopMatrix();
/* 216:    */     
/* 217:332 */     gl.glPushMatrix();
/* 218:333 */     gl.glMultMatrixf(rotateAboutX.a, 0);
/* 219:334 */     gl.glTranslated(0.0D, 0.0D, halfLength);
/* 220:335 */     gl.glScaled(0.1D, 0.1D, 1.0D);
/* 221:336 */     paintAcorn(gl);
/* 222:337 */     gl.glPopMatrix();
/* 223:    */   }
/* 224:    */   
/* 225:    */   public void paint(Graphics2D g, Affine.Vector disp, Affine.Point pt)
/* 226:    */   {
/* 227:342 */     pt.x = (this.ptWorld.x + disp.x);
/* 228:343 */     pt.y = (this.ptWorld.y + disp.y);
/* 229:    */   }
/* 230:    */   
/* 231:    */   public void paint(GL2 gl, Affine.Vector disp, float halfWidth, Gusset gusset, Affine.Point pt)
/* 232:    */   {
/* 233:356 */     gl.glPushMatrix();
/* 234:    */     
/* 235:    */ 
/* 236:359 */     pt.x = (this.ptWorld.x + disp.x);
/* 237:360 */     pt.y = (this.ptWorld.y + disp.y);
/* 238:    */     
/* 239:    */ 
/* 240:363 */     gl.glTranslated(pt.x, pt.y, 0.0D);
/* 241:    */     
/* 242:    */ 
/* 243:366 */     gusset.paint(gl, halfWidth);
/* 244:367 */     gusset.paint(gl, -halfWidth);
/* 245:    */     
/* 246:369 */     double gussetHalfDepth = gusset.getHalfDepth();
/* 247:371 */     if ((0.0D < this.ptWorld.y) && (this.ptWorld.y < 5.0D))
/* 248:    */     {
/* 249:372 */       gl.glTranslatef(0.0F, 0.0F, halfWidth);
/* 250:373 */       paintRod(gl, gussetHalfDepth + 0.02999999932944775D);
/* 251:374 */       gl.glTranslatef(0.0F, 0.0F, -2.0F * halfWidth);
/* 252:375 */       paintRod(gl, gussetHalfDepth + 0.02999999932944775D);
/* 253:    */     }
/* 254:    */     else
/* 255:    */     {
/* 256:378 */       paintRod(gl, halfWidth + 0.03F + gussetHalfDepth);
/* 257:    */     }
/* 258:381 */     gl.glPopMatrix();
/* 259:    */   }
/* 260:    */   
/* 261:    */   public void getViewportExtent(Rectangle dst, ViewportTransform viewportTransform)
/* 262:    */   {
/* 263:390 */     if (dst == null) {
/* 264:391 */       dst = new Rectangle();
/* 265:    */     }
/* 266:393 */     viewportTransform.worldToViewport(this.ptViewport, this.ptWorld);
/* 267:394 */     dst.setBounds(this.ptViewport.x, this.ptViewport.y, 0, 0);
/* 268:395 */     dst.grow(8, 8);
/* 269:    */   }
/* 270:    */   
/* 271:    */   public String toString()
/* 272:    */   {
/* 273:405 */     return WPBDApp.getResourceMap(Joint.class).getString(isFixed() ? "fixedrollover.text" : "rollover.text", new Object[] { getPointWorld() });
/* 274:    */   }
/* 275:    */   
/* 276:    */   public Cursor getCursor()
/* 277:    */   {
/* 278:416 */     return null;
/* 279:    */   }
/* 280:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.Joint
 * JD-Core Version:    0.7.0.1
 */