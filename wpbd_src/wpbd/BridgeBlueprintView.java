/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.awt.BasicStroke;
/*   4:    */ import java.awt.Color;
/*   5:    */ import java.awt.FontMetrics;
/*   6:    */ import java.awt.Graphics2D;
/*   7:    */ import java.awt.Polygon;
/*   8:    */ import java.awt.Rectangle;
/*   9:    */ import java.awt.Stroke;
/*  10:    */ import java.awt.geom.AffineTransform;
/*  11:    */ import java.awt.geom.Rectangle2D;
/*  12:    */ import java.awt.geom.Rectangle2D.Double;
/*  13:    */ import java.util.Locale;
/*  14:    */ 
/*  15:    */ public class BridgeBlueprintView
/*  16:    */   extends BridgeView
/*  17:    */ {
/*  18:    */   private static final int arrowHalfWidth = 20;
/*  19:    */   private static final int arrowLength = 80;
/*  20:    */   private static final int tickInside = 180;
/*  21:    */   private static final int tickOutside = 90;
/*  22:    */   private static final int dimensionOffset = 270;
/*  23:    */   private static final int rollerHalfSize = 20;
/*  24:    */   private static final int rollerSize = 40;
/*  25:    */   private static final int groundHalfWidth = 130;
/*  26:    */   private static final int groundHeight = 35;
/*  27:    */   private static final int halfTickSpacing = 15;
/*  28:    */   private static final int tickSpacing = 30;
/*  29:    */   private static final int tickHalfSlant = 15;
/*  30:    */   private static final int anchorHalfWidth = 80;
/*  31:    */   private static final int anchorHeight = 140;
/*  32:    */   private static final int rollerHalfSpacing = 30;
/*  33:    */   private static final int rollerSpacing = 60;
/*  34:    */   
/*  35:    */   public BridgeBlueprintView(BridgeModel bridge)
/*  36:    */   {
/*  37: 50 */     this.bridge = bridge;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public Rectangle getPaintedExtent(Rectangle extent, ViewportTransform viewportTransform)
/*  41:    */   {
/*  42: 61 */     if (extent == null) {
/*  43: 62 */       extent = new Rectangle();
/*  44:    */     }
/*  45: 64 */     int x0 = viewportTransform.worldToViewportX(this.preferredDrawingWindow.getMinX());
/*  46: 65 */     int y0 = viewportTransform.worldToViewportY(this.preferredDrawingWindow.getMinY());
/*  47: 66 */     int x1 = viewportTransform.worldToViewportX(this.preferredDrawingWindow.getMaxX());
/*  48: 67 */     int y1 = viewportTransform.worldToViewportY(this.preferredDrawingWindow.getMaxY());
/*  49: 68 */     extent.x = (x0 - 270 - 90 - 20);
/*  50: 69 */     extent.y = (y0 + 270 + 90 + 20);
/*  51: 70 */     extent.width = (x1 - extent.x);
/*  52: 71 */     extent.height = (y1 - extent.y);
/*  53: 72 */     return extent;
/*  54:    */   }
/*  55:    */   
/*  56:    */   protected void loadPreferredDrawingWindow()
/*  57:    */   {
/*  58: 80 */     this.bridge.getExtent(this.preferredDrawingWindow);
/*  59:    */   }
/*  60:    */   
/*  61:    */   private void paintLabel(Graphics2D g, String text, int x, int y)
/*  62:    */   {
/*  63: 93 */     AffineTransform savedTransform = g.getTransform();
/*  64: 94 */     g.translate(x, y);
/*  65: 95 */     g.scale(20.0D, 20.0D);
/*  66: 96 */     Rectangle2D bounds = g.getFontMetrics().getStringBounds(text, g);
/*  67: 97 */     g.translate(-0.5D * bounds.getWidth(), 0.3D * bounds.getHeight());
/*  68: 98 */     g.setColor(Color.white);
/*  69: 99 */     g.fill(bounds);
/*  70:100 */     g.setColor(Color.black);
/*  71:101 */     g.drawString(text, 0, 0);
/*  72:102 */     g.setTransform(savedTransform);
/*  73:    */   }
/*  74:    */   
/*  75:    */   protected void paint(Graphics2D g, ViewportTransform viewportTransform)
/*  76:    */   {
/*  77:114 */     g.setStroke(new BasicStroke(5.0F, 0, 1));
/*  78:115 */     paintAnchorages(g, viewportTransform);
/*  79:116 */     paintAbutmentsAndPier(g, viewportTransform);
/*  80:117 */     paintBridge(g, viewportTransform, new BridgePaintContext(Gusset.getGussets(this.bridge)));
/*  81:    */     
/*  82:119 */     double x0World = this.preferredDrawingWindow.getMinX();
/*  83:120 */     double y0World = this.preferredDrawingWindow.getMinY();
/*  84:121 */     double x1World = this.preferredDrawingWindow.getMaxX();
/*  85:122 */     double y1World = this.preferredDrawingWindow.getMaxY();
/*  86:    */     
/*  87:124 */     int x0Viewport = viewportTransform.worldToViewportX(x0World);
/*  88:125 */     int y0Viewport = viewportTransform.worldToViewportY(y0World);
/*  89:126 */     int x1Viewport = viewportTransform.worldToViewportX(x1World);
/*  90:127 */     int y1Viewport = viewportTransform.worldToViewportY(y1World);
/*  91:    */     
/*  92:    */ 
/*  93:130 */     int xDimension = x0Viewport - 270;
/*  94:131 */     if ((y0World < -0.5D) && (y1World > 0.5D))
/*  95:    */     {
/*  96:133 */       int yDeckViewport = viewportTransform.worldToViewportY(0.0D);
/*  97:134 */       Utility.drawDoubleArrow(g, xDimension, y0Viewport, xDimension, yDeckViewport, 20, 80);
/*  98:135 */       Utility.drawDoubleArrow(g, xDimension, yDeckViewport, xDimension, y1Viewport, 20, 80);
/*  99:136 */       g.drawLine(xDimension - 90, yDeckViewport, xDimension + 180, yDeckViewport);
/* 100:137 */       paintLabel(g, String.format(Locale.US, "%.2fm", new Object[] { Double.valueOf(-y0World) }), xDimension, (y0Viewport + yDeckViewport) / 2);
/* 101:138 */       paintLabel(g, String.format(Locale.US, "%.2fm", new Object[] { Double.valueOf(y1World) }), xDimension, (yDeckViewport + y1Viewport) / 2);
/* 102:139 */       xDimension -= 270;
/* 103:    */     }
/* 104:141 */     Utility.drawDoubleArrow(g, xDimension, y0Viewport, xDimension, y1Viewport, 20, 80);
/* 105:142 */     g.drawLine(xDimension - 90, y0Viewport, x0Viewport - 270 + 180, y0Viewport);
/* 106:143 */     g.drawLine(xDimension - 90, y1Viewport, x0Viewport - 270 + 180, y1Viewport);
/* 107:144 */     paintLabel(g, String.format(Locale.US, "%.2fm", new Object[] { Double.valueOf(this.preferredDrawingWindow.height) }), xDimension, (y0Viewport + y1Viewport) / 2);
/* 108:    */     
/* 109:    */ 
/* 110:147 */     int yDimension = y0Viewport + 270;
/* 111:148 */     int yTickInside = yDimension - 180;
/* 112:    */     
/* 113:150 */     DesignConditions dc = this.bridge.getDesignConditions();
/* 114:151 */     if (dc.isLeftAnchorage())
/* 115:    */     {
/* 116:153 */       double x0DeckWorld = dc.getXLeftmostDeckJoint();
/* 117:154 */       double x1DeckWorld = dc.getXRightmostDeckJoint();
/* 118:155 */       int x0DeckViewport = viewportTransform.worldToViewportX(x0DeckWorld);
/* 119:156 */       int x1DeckViewport = viewportTransform.worldToViewportX(x1DeckWorld);
/* 120:    */       
/* 121:158 */       Utility.drawDoubleArrow(g, x0Viewport, yDimension, x0DeckViewport, yDimension, 20, 80);
/* 122:159 */       g.drawLine(x0Viewport, yDimension + 90, x0Viewport, yTickInside);
/* 123:160 */       paintLabel(g, String.format(Locale.US, "%.2fm", new Object[] { Double.valueOf(x0DeckWorld - x0World) }), (x0Viewport + x0DeckViewport) / 2, yDimension);
/* 124:    */       
/* 125:162 */       Utility.drawDoubleArrow(g, x0DeckViewport, yDimension, x1DeckViewport, yDimension, 20, 80);
/* 126:163 */       g.drawLine(x0DeckViewport, yDimension + 90, x0DeckViewport, yTickInside);
/* 127:164 */       g.drawLine(x1DeckViewport, yDimension + 90, x1DeckViewport, yTickInside);
/* 128:165 */       paintLabel(g, String.format(Locale.US, "%.2fm", new Object[] { Double.valueOf(x1DeckWorld - x0DeckWorld) }), (x0DeckViewport + x1DeckViewport) / 2, yDimension);
/* 129:167 */       if (dc.isRightAnchorage())
/* 130:    */       {
/* 131:168 */         Utility.drawDoubleArrow(g, x1DeckViewport, yDimension, x1Viewport, yDimension, 20, 80);
/* 132:169 */         g.drawLine(x1Viewport, yDimension + 90, x1Viewport, yTickInside);
/* 133:170 */         paintLabel(g, String.format(Locale.US, "%.2fm", new Object[] { Double.valueOf(x1World - x1DeckWorld) }), (x1DeckViewport + x1Viewport) / 2, yDimension);
/* 134:    */       }
/* 135:173 */       yDimension += 270;
/* 136:    */     }
/* 137:175 */     Utility.drawDoubleArrow(g, x0Viewport, yDimension, x1Viewport, yDimension, 20, 80);
/* 138:176 */     g.drawLine(x0Viewport, yDimension + 90, x0Viewport, yTickInside);
/* 139:177 */     g.drawLine(x1Viewport, yDimension + 90, x1Viewport, yTickInside);
/* 140:178 */     paintLabel(g, String.format(Locale.US, "%.2fm", new Object[] { Double.valueOf(this.preferredDrawingWindow.width) }), (x0Viewport + x1Viewport) / 2, yDimension);
/* 141:    */   }
/* 142:    */   
/* 143:199 */   private static final int[] xAnchor = { -80, 0, 80 };
/* 144:200 */   private static final int[] yAnchor = { 140, 0, 140 };
/* 145:201 */   private static final Polygon anchor = new Polygon(xAnchor, yAnchor, xAnchor.length);
/* 146:    */   
/* 147:    */   private void paintAnchor(Graphics2D g, Affine.Point location, int nConstraints, ViewportTransform viewportTransform)
/* 148:    */   {
/* 149:212 */     AffineTransform savedTransform = g.getTransform();
/* 150:213 */     Stroke savedStroke = g.getStroke();
/* 151:214 */     int x = viewportTransform.worldToViewportX(location.x);
/* 152:215 */     int y = viewportTransform.worldToViewportY(location.y);
/* 153:216 */     g.translate(x, y);
/* 154:217 */     g.setStroke(new BasicStroke(5.0F));
/* 155:218 */     g.setColor(Color.white);
/* 156:219 */     g.fill(anchor);
/* 157:220 */     g.setColor(Color.black);
/* 158:221 */     g.draw(anchor);
/* 159:222 */     g.drawLine(-130, 140, 130, 140);
/* 160:223 */     switch (nConstraints)
/* 161:    */     {
/* 162:    */     case 1: 
/* 163:226 */       for (int xRoller = 30; xRoller < 130; xRoller += 60)
/* 164:    */       {
/* 165:227 */         g.drawOval(xRoller - 20, 140, 40, 40);
/* 166:228 */         g.drawOval(-xRoller - 20, 140, 40, 40);
/* 167:    */       }
/* 168:230 */       g.drawLine(-130, 180, 130, 180);
/* 169:232 */       for (int xTick = 15; xTick < 130; xTick += 30)
/* 170:    */       {
/* 171:233 */         g.drawLine(xTick + 15, 180, xTick - 15, 215);
/* 172:    */         
/* 173:235 */         g.drawLine(-xTick + 15, 180, -xTick - 15, 215);
/* 174:    */       }
/* 175:238 */       break;
/* 176:    */     case 2: 
/* 177:241 */       for (int xTick = 15; xTick < 130; xTick += 30)
/* 178:    */       {
/* 179:242 */         g.drawLine(xTick + 15, 140, xTick - 15, 175);
/* 180:    */         
/* 181:244 */         g.drawLine(-xTick + 15, 140, -xTick - 15, 175);
/* 182:    */       }
/* 183:    */     }
/* 184:249 */     g.setTransform(savedTransform);
/* 185:250 */     g.setStroke(savedStroke);
/* 186:    */   }
/* 187:    */   
/* 188:    */   public void paintStandardAbutment(Graphics2D g, Affine.Point location, boolean right, int nConstraints, ViewportTransform viewportTransform)
/* 189:    */   {
/* 190:264 */     paintAnchor(g, location, nConstraints, viewportTransform);
/* 191:    */   }
/* 192:    */   
/* 193:    */   protected void paintPier(Graphics2D g, Affine.Point location, double pierHeight, ViewportTransform viewportTransform)
/* 194:    */   {
/* 195:278 */     paintAnchor(g, location, 2, viewportTransform);
/* 196:    */   }
/* 197:    */   
/* 198:    */   protected void paintArchAbutment(Graphics2D g, Affine.Point location, boolean right, double archHeight, ViewportTransform viewportTransform)
/* 199:    */   {
/* 200:293 */     paintAnchor(g, location.plus(0.0D, archHeight), 2, viewportTransform);
/* 201:    */   }
/* 202:    */   
/* 203:    */   protected void paintAnchorage(Graphics2D g, Affine.Point location, ViewportTransform viewportTransform)
/* 204:    */   {
/* 205:305 */     paintAnchor(g, location, 2, viewportTransform);
/* 206:    */   }
/* 207:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.BridgeBlueprintView
 * JD-Core Version:    0.7.0.1
 */