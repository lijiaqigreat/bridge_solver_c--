/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.awt.Color;
/*   4:    */ import java.awt.Graphics2D;
/*   5:    */ import java.awt.Point;
/*   6:    */ import java.util.ArrayList;
/*   7:    */ import java.util.Iterator;
/*   8:    */ import java.util.List;
/*   9:    */ import javax.media.opengl.GL2;
/*  10:    */ 
/*  11:    */ public class Gusset
/*  12:    */ {
/*  13:    */   public static final float gussetMaterialThickness = 0.02F;
/*  14:    */   private final Joint joint;
/*  15: 42 */   private static final ConvexHullFactory hullFactory = new ConvexHullFactory();
/*  16: 45 */   private final List<MemberGeometry> members = new ArrayList(4);
/*  17: 48 */   private Affine.Point[] hull = null;
/*  18: 51 */   private double halfDepth = 0.0D;
/*  19: 54 */   private boolean interferingWithLoad = false;
/*  20:    */   
/*  21:    */   public Gusset(Joint joint)
/*  22:    */   {
/*  23: 62 */     this.joint = joint;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public double getHalfDepth()
/*  27:    */   {
/*  28: 71 */     return this.halfDepth;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public boolean isInterferingWithLoad()
/*  32:    */   {
/*  33: 80 */     return this.interferingWithLoad;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public static Gusset[] getGussets(BridgeModel bridge)
/*  37:    */   {
/*  38: 92 */     Gusset[] gussets = new Gusset[bridge.getJoints().size()];
/*  39: 93 */     Iterator<Joint> je = bridge.getJoints().iterator();
/*  40: 94 */     while (je.hasNext())
/*  41:    */     {
/*  42: 95 */       Joint joint = (Joint)je.next();
/*  43: 96 */       gussets[joint.getIndex()] = new Gusset(joint);
/*  44:    */     }
/*  45:100 */     Iterator<Member> me = bridge.getMembers().iterator();
/*  46:101 */     while (me.hasNext())
/*  47:    */     {
/*  48:102 */       Member member = (Member)me.next();
/*  49:103 */       gussets[member.getJointA().getIndex()].add(member);
/*  50:104 */       gussets[member.getJointB().getIndex()].add(member);
/*  51:    */     }
/*  52:110 */     for (int i = 0; i < gussets.length; i++) {
/*  53:111 */       gussets[i].initialize();
/*  54:    */     }
/*  55:113 */     return gussets;
/*  56:    */   }
/*  57:    */   
/*  58:    */   private class MemberGeometry
/*  59:    */   {
/*  60:    */     public Affine.Point p0;
/*  61:    */     public Affine.Point p1;
/*  62:    */     public Affine.Point p2;
/*  63:    */     public Affine.Vector u;
/*  64:    */     public Affine.Vector uPerp;
/*  65:    */     public double halfWidth;
/*  66:    */     
/*  67:    */     public MemberGeometry(Member member)
/*  68:    */     {
/*  69:140 */       Joint otherJoint = member.otherJoint(Gusset.this.joint);
/*  70:141 */       this.halfWidth = (0.5D * member.getWidthInMeters() + 0.01999999955296516D);
/*  71:142 */       Affine.Vector v = otherJoint.getPointWorld().minus(Gusset.this.joint.getPointWorld());
/*  72:143 */       this.u = v.unit(this.halfWidth);
/*  73:144 */       this.uPerp = this.u.perp();
/*  74:145 */       this.p0 = this.u.minus().toPoint();
/*  75:146 */       this.p1 = this.u.toPoint();
/*  76:147 */       this.p2 = v.minus(this.u).toPoint();
/*  77:    */     }
/*  78:    */   }
/*  79:    */   
/*  80:    */   public void add(Member member)
/*  81:    */   {
/*  82:157 */     this.members.add(new MemberGeometry(member));
/*  83:    */   }
/*  84:    */   
/*  85:    */   public void initialize()
/*  86:    */   {
/*  87:165 */     this.halfDepth = 0.0D;
/*  88:166 */     Iterator<MemberGeometry> iMGLeft = this.members.iterator();
/*  89:167 */     while (iMGLeft.hasNext())
/*  90:    */     {
/*  91:168 */       MemberGeometry mgLeft = (MemberGeometry)iMGLeft.next();
/*  92:171 */       if (mgLeft.halfWidth > this.halfDepth) {
/*  93:172 */         this.halfDepth = mgLeft.halfWidth;
/*  94:    */       }
/*  95:176 */       hullFactory.add(mgLeft.p0.plus(mgLeft.uPerp));
/*  96:177 */       hullFactory.add(mgLeft.p0.minus(mgLeft.uPerp));
/*  97:178 */       hullFactory.add(mgLeft.p1.plus(mgLeft.uPerp));
/*  98:179 */       hullFactory.add(mgLeft.p1.minus(mgLeft.uPerp));
/*  99:    */       
/* 100:    */ 
/* 101:182 */       Iterator<MemberGeometry> iMGRight = this.members.iterator();
/* 102:183 */       while (iMGRight.hasNext())
/* 103:    */       {
/* 104:184 */         MemberGeometry mgRight = (MemberGeometry)iMGRight.next();
/* 105:185 */         if (iMGLeft != mgRight)
/* 106:    */         {
/* 107:186 */           Affine.Point isect = Utility.intersection(mgLeft.p0.plus(mgLeft.uPerp), mgLeft.p2.plus(mgLeft.uPerp), mgRight.p0.minus(mgRight.uPerp), mgRight.p2.minus(mgRight.uPerp));
/* 108:191 */           if (isect != null)
/* 109:    */           {
/* 110:192 */             hullFactory.add(isect);
/* 111:193 */             hullFactory.add(isect.minus(mgLeft.uPerp.times(2.0D)));
/* 112:194 */             hullFactory.add(isect.plus(mgRight.uPerp.times(2.0D)));
/* 113:    */           }
/* 114:    */         }
/* 115:    */       }
/* 116:    */     }
/* 117:201 */     this.hull = hullFactory.getHull(null);
/* 118:202 */     hullFactory.clear();
/* 119:204 */     if (this.hull.length > 0)
/* 120:    */     {
/* 121:206 */       double yTop = this.hull[0].y;
/* 122:207 */       double yBottom = yTop;
/* 123:208 */       for (int i = 1; i < this.hull.length; i++)
/* 124:    */       {
/* 125:209 */         yTop = Math.max(yTop, this.hull[i].y);
/* 126:210 */         yBottom = Math.min(yBottom, this.hull[i].y);
/* 127:    */       }
/* 128:212 */       double yJoint = this.joint.getPointWorld().y;
/* 129:213 */       this.interferingWithLoad = ((yJoint + yTop > 0.8D) && (yJoint + yBottom < 2.5D));
/* 130:    */     }
/* 131:    */   }
/* 132:    */   
/* 133:    */   public void paint(Graphics2D g, ViewportTransform viewportTransform)
/* 134:    */   {
/* 135:219 */     if (this.hull.length < 2) {
/* 136:220 */       return;
/* 137:    */     }
/* 138:222 */     int j = this.hull.length - 1;
/* 139:223 */     int[] xPoints = new int[this.hull.length];
/* 140:224 */     int[] yPoints = new int[this.hull.length];
/* 141:225 */     Point ptViewport = new Point();
/* 142:226 */     Affine.Vector jointPosition = this.joint.getPointWorld().position();
/* 143:227 */     for (int i = 0; i < this.hull.length; i++)
/* 144:    */     {
/* 145:228 */       viewportTransform.worldToViewport(ptViewport, this.hull[i].plus(jointPosition));
/* 146:229 */       xPoints[i] = ptViewport.x;
/* 147:230 */       yPoints[i] = ptViewport.y;
/* 148:    */     }
/* 149:232 */     g.setColor(Color.WHITE);
/* 150:233 */     g.fillPolygon(xPoints, yPoints, this.hull.length);
/* 151:234 */     g.setColor(Color.BLACK);
/* 152:235 */     g.drawPolygon(xPoints, yPoints, this.hull.length);
/* 153:    */   }
/* 154:    */   
/* 155:    */   public void paint(GL2 gl, double z)
/* 156:    */   {
/* 157:248 */     if (this.hull.length < 2) {
/* 158:249 */       return;
/* 159:    */     }
/* 160:253 */     gl.glColor3fv(Joint.jointMaterial, 0);
/* 161:254 */     double zFront = z + this.halfDepth;
/* 162:255 */     gl.glBegin(6);
/* 163:256 */     gl.glNormal3d(0.0D, 0.0D, 1.0D);
/* 164:257 */     gl.glVertex3d(0.0D, 0.0D, zFront);
/* 165:258 */     for (int i = 0; i < this.hull.length; i++) {
/* 166:259 */       gl.glVertex3d(this.hull[i].x, this.hull[i].y, zFront);
/* 167:    */     }
/* 168:261 */     gl.glVertex3d(this.hull[0].x, this.hull[0].y, zFront);
/* 169:262 */     gl.glEnd();
/* 170:    */     
/* 171:    */ 
/* 172:265 */     gl.glBegin(6);
/* 173:266 */     double zRear = z - this.halfDepth;
/* 174:267 */     gl.glNormal3d(0.0D, 0.0D, -1.0D);
/* 175:268 */     gl.glVertex3d(0.0D, 0.0D, zRear);
/* 176:269 */     for (int i = this.hull.length - 1; i >= 0; i--) {
/* 177:270 */       gl.glVertex3d(this.hull[i].x, this.hull[i].y, zRear);
/* 178:    */     }
/* 179:272 */     gl.glVertex3d(this.hull[(this.hull.length - 1)].x, this.hull[(this.hull.length - 1)].y, zRear);
/* 180:273 */     gl.glEnd();
/* 181:    */     
/* 182:    */ 
/* 183:276 */     gl.glBegin(7);
/* 184:277 */     int j = this.hull.length - 1;
/* 185:278 */     for (int i = 0; i < this.hull.length; i++)
/* 186:    */     {
/* 187:279 */       gl.glNormal3d(this.hull[i].y - this.hull[j].y, this.hull[j].x - this.hull[i].x, 0.0D);
/* 188:280 */       gl.glVertex3d(this.hull[i].x, this.hull[i].y, zFront);
/* 189:281 */       gl.glVertex3d(this.hull[j].x, this.hull[j].y, zFront);
/* 190:282 */       gl.glVertex3d(this.hull[j].x, this.hull[j].y, zRear);
/* 191:283 */       gl.glVertex3d(this.hull[i].x, this.hull[i].y, zRear);
/* 192:284 */       j = i;
/* 193:    */     }
/* 194:286 */     gl.glEnd();
/* 195:    */   }
/* 196:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.Gusset
 * JD-Core Version:    0.7.0.1
 */