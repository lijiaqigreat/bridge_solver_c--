/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.awt.Canvas;
/*   4:    */ import java.awt.Color;
/*   5:    */ import java.awt.Dimension;
/*   6:    */ import java.awt.Frame;
/*   7:    */ import java.awt.GradientPaint;
/*   8:    */ import java.awt.Graphics;
/*   9:    */ import java.awt.Graphics2D;
/*  10:    */ import java.awt.Image;
/*  11:    */ import java.awt.Paint;
/*  12:    */ import java.awt.Point;
/*  13:    */ import java.awt.Rectangle;
/*  14:    */ import java.awt.Toolkit;
/*  15:    */ import java.awt.event.ActionEvent;
/*  16:    */ import java.awt.event.ActionListener;
/*  17:    */ import java.awt.event.ComponentAdapter;
/*  18:    */ import java.awt.event.ComponentEvent;
/*  19:    */ import java.awt.image.BufferStrategy;
/*  20:    */ import javax.swing.Timer;
/*  21:    */ 
/*  22:    */ public class FixedEyeAnimation
/*  23:    */   extends Animation
/*  24:    */ {
/*  25:    */   private final AnimationControls controls;
/*  26:    */   private final Config config;
/*  27:    */   private final FixedEyeAnimationCanvas canvas;
/*  28:    */   
/*  29:    */   public Canvas getCanvas()
/*  30:    */   {
/*  31: 55 */     return this.canvas;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public void updateView(double elapsed) {}
/*  35:    */   
/*  36:    */   public void start()
/*  37:    */   {
/*  38: 71 */     this.canvas.start();
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void stop()
/*  42:    */   {
/*  43: 79 */     this.canvas.stop();
/*  44:    */   }
/*  45:    */   
/*  46:    */   public void invalidateBackground()
/*  47:    */   {
/*  48: 87 */     this.canvas.invalidateBackground();
/*  49:    */   }
/*  50:    */   
/*  51:    */   public static class Config
/*  52:    */     extends Animation.Config
/*  53:    */   {
/*  54: 94 */     public boolean showForcesAsColors = true;
/*  55: 95 */     public boolean showBackground = true;
/*  56: 96 */     public boolean showTruck = true;
/*  57: 97 */     public boolean showAbutments = true;
/*  58: 98 */     public boolean showSmoothTerrain = true;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public Config getConfig()
/*  62:    */   {
/*  63:107 */     return this.config;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public final AnimationControls getControls()
/*  67:    */   {
/*  68:116 */     return this.controls;
/*  69:    */   }
/*  70:    */   
/*  71:    */   private FixedEyeAnimation(Frame frame, EditableBridgeModel bridge, FixedEyeTerrainModel terrain, Config config)
/*  72:    */   {
/*  73:120 */     super(bridge, terrain, config);
/*  74:121 */     this.config = config;
/*  75:    */     
/*  76:123 */     this.loadLocationRunup = 8.0D;
/*  77:124 */     this.canvas = new FixedEyeAnimationCanvas(terrain);
/*  78:    */     
/*  79:126 */     this.controls = new FixedEyeControls(frame, this);
/*  80:    */   }
/*  81:    */   
/*  82:    */   public static FixedEyeAnimation create(Frame frame, EditableBridgeModel bridge)
/*  83:    */   {
/*  84:137 */     Config config = new Config();
/*  85:138 */     FixedEyeTerrainModel terrain = new FixedEyeTerrainModel(config);
/*  86:139 */     return new FixedEyeAnimation(frame, bridge, terrain, config);
/*  87:    */   }
/*  88:    */   
/*  89:    */   private class FixedEyeAnimationCanvas
/*  90:    */     extends Canvas
/*  91:    */   {
/*  92:    */     private final ViewportTransform viewportTransform;
/*  93:    */     private final Bridge3dView bridgeView;
/*  94:    */     private final Timer timer;
/*  95:    */     private Image background;
/*  96:    */     private Paint sky;
/*  97:    */     private BufferStrategy backBuffer;
/*  98:    */     private final FixedEyeTerrainModel terrain;
/*  99:154 */     private final int frameRate = 20;
/* 100:    */     
/* 101:    */     FixedEyeAnimationCanvas(FixedEyeTerrainModel terrain)
/* 102:    */     {
/* 103:157 */       this.terrain = terrain;
/* 104:158 */       this.viewportTransform = new ViewportTransform();
/* 105:159 */       this.bridgeView = new Bridge3dView(FixedEyeAnimation.this.bridge, terrain, FixedEyeAnimation.this.config);
/* 106:160 */       this.timer = new Timer(20, new ActionListener()
/* 107:    */       {
/* 108:    */         public void actionPerformed(ActionEvent e)
/* 109:    */         {
/* 110:162 */           FixedEyeAnimation.FixedEyeAnimationCanvas.this.repaint();
/* 111:    */         }
/* 112:164 */       });
/* 113:165 */       this.timer.setCoalesce(true);
/* 114:166 */       this.timer.setInitialDelay(0);
/* 115:167 */       addComponentListener(new ComponentAdapter()
/* 116:    */       {
/* 117:    */         public void componentResized(ComponentEvent e)
/* 118:    */         {
/* 119:170 */           FixedEyeAnimation.FixedEyeAnimationCanvas.this.setViewport();
/* 120:    */         }
/* 121:    */       });
/* 122:    */     }
/* 123:    */     
/* 124:    */     private void paintBackground()
/* 125:    */     {
/* 126:178 */       if (this.background == null)
/* 127:    */       {
/* 128:179 */         Dimension screenSize = Utility.getMaxScreenSize();
/* 129:180 */         this.background = FixedEyeAnimation.this.canvas.createImage(screenSize.width, screenSize.height);
/* 130:    */       }
/* 131:182 */       if (this.sky == null)
/* 132:    */       {
/* 133:184 */         Point vp = this.viewportTransform.getVanishingPoint(null);
/* 134:185 */         this.sky = new GradientPaint(new Point(vp.x, 0), new Color(128, 255, 255), vp, Color.WHITE);
/* 135:186 */         Graphics2D g = (Graphics2D)this.background.getGraphics();
/* 136:187 */         int w = this.viewportTransform.getAbsWidthViewport();
/* 137:188 */         int h = this.viewportTransform.getAbsHeightViewport();
/* 138:189 */         g.setPaint(this.sky);
/* 139:190 */         g.fillRect(0, 0, w, h);
/* 140:191 */         if (FixedEyeAnimation.this.config.showBackground) {
/* 141:192 */           this.terrain.paint(g, this.viewportTransform);
/* 142:    */         }
/* 143:194 */         g.dispose();
/* 144:    */       }
/* 145:    */     }
/* 146:    */     
/* 147:    */     private void restoreBackground(Graphics2D g, Rectangle b)
/* 148:    */     {
/* 149:199 */       paintBackground();
/* 150:200 */       if (b == null)
/* 151:    */       {
/* 152:201 */         g.drawImage(this.background, 0, 0, FixedEyeAnimation.this.canvas);
/* 153:    */       }
/* 154:    */       else
/* 155:    */       {
/* 156:203 */         int x1 = b.x;
/* 157:204 */         int y1 = b.y;
/* 158:205 */         int x2 = b.x + b.width;
/* 159:206 */         int y2 = b.y + b.height;
/* 160:207 */         g.drawImage(this.background, x1, y1, x2, y2, x1, y1, x2, y2, FixedEyeAnimation.this.canvas);
/* 161:    */       }
/* 162:    */     }
/* 163:    */     
/* 164:    */     void drawFrame()
/* 165:    */     {
/* 166:214 */       long clock = System.nanoTime();
/* 167:215 */       Analysis.Interpolation interpolation = FixedEyeAnimation.this.interpolate(clock);
/* 168:    */       do
/* 169:    */       {
/* 170:    */         do
/* 171:    */         {
/* 172:221 */           Graphics2D g = (Graphics2D)this.backBuffer.getDrawGraphics();
/* 173:    */           try
/* 174:    */           {
/* 175:223 */             restoreBackground(g, null);
/* 176:224 */             this.bridgeView.paint(g, this.viewportTransform, interpolation, FixedEyeAnimation.this.getDistanceMoved());
/* 177:    */           }
/* 178:    */           finally
/* 179:    */           {
/* 180:227 */             g.dispose();
/* 181:    */           }
/* 182:229 */         } while (this.backBuffer.contentsRestored());
/* 183:230 */         this.backBuffer.show();
/* 184:    */         
/* 185:232 */         Toolkit.getDefaultToolkit().sync();
/* 186:233 */       } while (this.backBuffer.contentsLost());
/* 187:    */     }
/* 188:    */     
/* 189:    */     void invalidateBackground()
/* 190:    */     {
/* 191:237 */       this.sky = null;
/* 192:    */     }
/* 193:    */     
/* 194:    */     void setViewport()
/* 195:    */     {
/* 196:241 */       int w = getWidth();
/* 197:242 */       int h = getHeight();
/* 198:243 */       this.viewportTransform.setWindow(this.bridgeView.getPreferredDrawingWindow());
/* 199:244 */       this.viewportTransform.setZScale(0.026D);
/* 200:245 */       this.viewportTransform.setVanishingPoint(0.5D, 0.5D, 5.0D);
/* 201:246 */       this.viewportTransform.setViewport(0.0D, h - 1, w - 1, 1 - h);
/* 202:247 */       this.sky = null;
/* 203:    */     }
/* 204:    */     
/* 205:    */     void start()
/* 206:    */     {
/* 207:251 */       stop();
/* 208:252 */       DesignConditions conditions = FixedEyeAnimation.this.bridge.getDesignConditions();
/* 209:253 */       this.bridgeView.initialize(conditions);
/* 210:254 */       this.terrain.initializeTerrain(conditions, 0.0F, 6.0F);
/* 211:256 */       if (this.backBuffer == null)
/* 212:    */       {
/* 213:257 */         FixedEyeAnimation.this.canvas.createBufferStrategy(2);
/* 214:258 */         this.backBuffer = FixedEyeAnimation.this.canvas.getBufferStrategy();
/* 215:    */       }
/* 216:260 */       setViewport();
/* 217:261 */       FixedEyeAnimation.this.resetState();
/* 218:262 */       this.timer.start();
/* 219:    */     }
/* 220:    */     
/* 221:    */     void stop()
/* 222:    */     {
/* 223:266 */       this.timer.stop();
/* 224:    */     }
/* 225:    */     
/* 226:    */     public void paint(Graphics g0)
/* 227:    */     {
/* 228:271 */       drawFrame();
/* 229:    */     }
/* 230:    */     
/* 231:    */     public void update(Graphics g)
/* 232:    */     {
/* 233:276 */       drawFrame();
/* 234:    */     }
/* 235:    */   }
/* 236:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.FixedEyeAnimation
 * JD-Core Version:    0.7.0.1
 */