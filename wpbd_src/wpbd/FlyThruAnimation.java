/*    1:     */ package wpbd;
/*    2:     */ 
/*    3:     */ import com.jogamp.opengl.util.FPSAnimator;
/*    4:     */ import com.jogamp.opengl.util.gl2.GLUT;
/*    5:     */ import com.jogamp.opengl.util.texture.Texture;
/*    6:     */ import com.jogamp.opengl.util.texture.TextureIO;
/*    7:     */ import java.awt.Frame;
/*    8:     */ import java.awt.event.KeyEvent;
/*    9:     */ import java.awt.event.KeyListener;
/*   10:     */ import java.awt.event.MouseEvent;
/*   11:     */ import java.awt.event.MouseListener;
/*   12:     */ import java.awt.event.MouseMotionListener;
/*   13:     */ import java.awt.geom.Rectangle2D;
/*   14:     */ import java.util.ArrayList;
/*   15:     */ import java.util.Iterator;
/*   16:     */ import java.util.logging.Level;
/*   17:     */ import java.util.logging.Logger;
/*   18:     */ import javax.media.opengl.GL;
/*   19:     */ import javax.media.opengl.GL2;
/*   20:     */ import javax.media.opengl.GLAutoDrawable;
/*   21:     */ import javax.media.opengl.GLCapabilities;
/*   22:     */ import javax.media.opengl.GLEventListener;
/*   23:     */ import javax.media.opengl.GLException;
/*   24:     */ import javax.media.opengl.awt.GLCanvas;
/*   25:     */ import javax.media.opengl.glu.GLU;
/*   26:     */ import jogamp.common.Debug;
/*   27:     */ 
/*   28:     */ public class FlyThruAnimation
/*   29:     */   extends Animation
/*   30:     */ {
/*   31:     */   private AnimationCanvas canvas;
/*   32:     */   private final FlyThruTerrainModel terrain;
/*   33:     */   public static final double deckClearance = 5.0D;
/*   34:     */   private static final double deckBeamWidth = 0.12D;
/*   35:     */   private static final double driverEyeHeight = 2.4D;
/*   36:     */   private static final double driverEyeLead = 0.6D;
/*   37:     */   private static final double loadFadeInDistance = 2.0D;
/*   38:     */   private static final double maxTilt = 1.178097245096172D;
/*   39:     */   private static final double linearUIRate = 0.1D;
/*   40:     */   private static final double rotationalUIRate = 0.003141592653589793D;
/*   41:     */   private static final double tiltUIRate = 0.3141592653589793D;
/*   42:  95 */   private static final float[] deckVerticesLeft = { 0.0F, 0.0F, 0.5F, 0.0F, 0.0F, -0.5F, 0.0F, -1.0F, -0.5F, 0.0F, -1.0F, 0.5F, 0.0F, 0.0F, 0.5F };
/*   43: 101 */   private static final float[] deckVerticesRight = { 1.0F, 0.0F, 0.5F, 1.0F, 0.0F, -0.5F, 1.0F, -1.0F, -0.5F, 1.0F, -1.0F, 0.5F, 1.0F, 0.0F, 0.5F };
/*   44: 107 */   private static final float[] deckNormals = { 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, -1.0F, 0.0F, -1.0F, 0.0F, 0.0F, 0.0F, 1.0F };
/*   45: 118 */   private static final Homogeneous.Matrix biasMatrix = new Homogeneous.Matrix(0.5F, 0.0F, 0.0F, 0.5F, 0.0F, 0.5F, 0.0F, 0.5F, 0.0F, 0.0F, 0.5F, 0.5F, 0.0F, 0.0F, 0.0F, 1.0F);
/*   46:     */   private final Config config;
/*   47:     */   private final AnimationControls controls;
/*   48:     */   
/*   49:     */   public static class Config
/*   50:     */     extends Animation.Config
/*   51:     */   {
/*   52: 129 */     public boolean showForcesAsColors = true;
/*   53: 130 */     public float[] lightBrightness = { 0.6F, 0.6F, 0.6F, 1.0F };
/*   54: 131 */     public boolean canShowShadows = !Debug.isPropertyDefined("wpbd.noshadows", false);
/*   55: 132 */     public boolean showShadows = false;
/*   56: 133 */     public boolean showTruck = true;
/*   57: 134 */     public boolean showSky = true;
/*   58: 135 */     public boolean showTerrain = true;
/*   59: 136 */     public boolean showAbutments = true;
/*   60: 137 */     public boolean showErrosion = false;
/*   61:     */   }
/*   62:     */   
/*   63:     */   public final AnimationControls getControls()
/*   64:     */   {
/*   65: 154 */     return this.controls;
/*   66:     */   }
/*   67:     */   
/*   68:     */   private FlyThruAnimation(Frame frame, EditableBridgeModel bridge, FlyThruTerrainModel terrain, Config config)
/*   69:     */   {
/*   70: 166 */     super(bridge, terrain, config);
/*   71: 167 */     this.terrain = terrain;
/*   72: 168 */     this.config = config;
/*   73: 169 */     this.controls = new FlyThruControls(frame, this);
/*   74: 170 */     this.canvas = new AnimationCanvas();
/*   75:     */   }
/*   76:     */   
/*   77:     */   public static FlyThruAnimation create(Frame frame, EditableBridgeModel bridge)
/*   78:     */   {
/*   79: 181 */     Config config = new Config();
/*   80: 182 */     FlyThruTerrainModel terrain = new FlyThruTerrainModel(config);
/*   81: 183 */     return new FlyThruAnimation(frame, bridge, terrain, config);
/*   82:     */   }
/*   83:     */   
/*   84:     */   public AnimationCanvas getCanvas()
/*   85:     */   {
/*   86: 188 */     return this.canvas;
/*   87:     */   }
/*   88:     */   
/*   89:     */   public void start()
/*   90:     */   {
/*   91: 193 */     this.canvas.start();
/*   92:     */   }
/*   93:     */   
/*   94:     */   public void stop()
/*   95:     */   {
/*   96: 198 */     this.canvas.stop();
/*   97:     */   }
/*   98:     */   
/*   99:     */   public Config getConfig()
/*  100:     */   {
/*  101: 207 */     return this.config;
/*  102:     */   }
/*  103:     */   
/*  104:     */   private static GLCapabilities getAnimationCapabilities()
/*  105:     */   {
/*  106: 216 */     GLCapabilities capabilities = new GLCapabilities(WPBDApp.getGLProfile());
/*  107: 217 */     capabilities.setAccumAlphaBits(0);
/*  108: 218 */     capabilities.setAccumRedBits(0);
/*  109: 219 */     capabilities.setAccumGreenBits(0);
/*  110: 220 */     capabilities.setAccumBlueBits(0);
/*  111: 221 */     capabilities.setAlphaBits(8);
/*  112: 222 */     capabilities.setRedBits(8);
/*  113: 223 */     capabilities.setGreenBits(8);
/*  114: 224 */     capabilities.setBlueBits(8);
/*  115: 225 */     capabilities.setDepthBits(24);
/*  116: 226 */     capabilities.setDoubleBuffered(true);
/*  117: 227 */     capabilities.setHardwareAccelerated(true);
/*  118: 228 */     capabilities.setOnscreen(true);
/*  119: 229 */     capabilities.setPBuffer(false);
/*  120: 230 */     capabilities.setSampleBuffers(false);
/*  121: 231 */     capabilities.setStencilBits(0);
/*  122: 232 */     capabilities.setStereo(false);
/*  123: 233 */     capabilities.setBackgroundOpaque(true);
/*  124: 234 */     return capabilities;
/*  125:     */   }
/*  126:     */   
/*  127:     */   private static int getInteger(GL2 gl, int pname)
/*  128:     */   {
/*  129: 245 */     int[] val = new int[1];
/*  130: 246 */     gl.glGetIntegerv(pname, val, 0);
/*  131: 247 */     return val[0];
/*  132:     */   }
/*  133:     */   
/*  134:     */   private static int getTexLevelParameteri(GL2 gl, int target, int level, int pname)
/*  135:     */   {
/*  136: 260 */     int[] val = new int[1];
/*  137: 261 */     gl.glGetTexLevelParameteriv(target, level, pname, val, 0);
/*  138: 262 */     return val[0];
/*  139:     */   }
/*  140:     */   
/*  141:     */   private static int genFramebuffer(GL2 gl)
/*  142:     */   {
/*  143: 272 */     int[] val = new int[1];
/*  144: 273 */     gl.glGenFramebuffers(1, val, 0);
/*  145: 274 */     return val[0];
/*  146:     */   }
/*  147:     */   
/*  148:     */   private static void deleteFramebuffer(GL2 gl, int bufferId)
/*  149:     */   {
/*  150: 284 */     gl.glDeleteFramebuffers(1, new int[] { bufferId }, 0);
/*  151:     */   }
/*  152:     */   
/*  153:     */   private static int powerOf2AtMost(int x)
/*  154:     */   {
/*  155: 294 */     int i = 1;
/*  156: 295 */     while (i <= x) {
/*  157: 296 */       i <<= 1;
/*  158:     */     }
/*  159: 298 */     return i >> 1;
/*  160:     */   }
/*  161:     */   
/*  162:     */   public class AnimationCanvas
/*  163:     */     extends GLCanvas
/*  164:     */     implements GLEventListener, MouseListener, MouseMotionListener, KeyListener
/*  165:     */   {
/*  166: 306 */     private int maxShadowTextureSize = 4096;
/*  167:     */     private Texture shadowMapTexture;
/*  168:     */     private int shadowMapWidth;
/*  169:     */     private int shadowMapHeight;
/*  170: 318 */     private int shadowFrameBuffer = -1;
/*  171:     */     private int windowX;
/*  172:     */     private int windowY;
/*  173:     */     private int windowWidth;
/*  174:     */     private int windowHeight;
/*  175: 326 */     private final float[] white = { 1.0F, 1.0F, 1.0F, 1.0F };
/*  176: 327 */     private final float[] lowWhite = { 0.4F, 0.4F, 0.45F, 1.0F };
/*  177: 328 */     private final float[] black = { 0.0F, 0.0F, 0.0F, 1.0F };
/*  178: 332 */     private final float[] deckColor = { 0.6F, 0.6F, 0.6F, 1.0F };
/*  179: 336 */     private final float[] beamMaterial = { 0.3F, 0.2F, 0.1F, 1.0F };
/*  180: 340 */     private final float[] fogColor = { 0.6F, 0.6F, 0.8F, 1.0F };
/*  181: 344 */     private final Homogeneous.Matrix eyeProjectionMatrix = new Homogeneous.Matrix();
/*  182: 348 */     private final Homogeneous.Matrix mouseProjectionMatrix = new Homogeneous.Matrix();
/*  183: 352 */     private final Homogeneous.Matrix trapezoidalProjectionMatrix = new Homogeneous.Matrix();
/*  184: 357 */     private final Homogeneous.Matrix lightProjectionMatrix = new Homogeneous.Matrix();
/*  185: 361 */     private final Homogeneous.Matrix lightViewMatrix = new Homogeneous.Matrix();
/*  186: 365 */     private final Homogeneous.Matrix eyeViewMatrix = new Homogeneous.Matrix();
/*  187: 369 */     private final Homogeneous.Matrix textureMatrix = new Homogeneous.Matrix();
/*  188: 373 */     private final Affine.Point ptDisplacedJoint = new Affine.Point();
/*  189:     */     private FlyThruTruckModel truckModel;
/*  190:     */     private FlyThruTruckCabModel truckCabModel;
/*  191:     */     private FlyThruWheelModel wheelModel;
/*  192:     */     private FPSAnimator animator;
/*  193: 394 */     private boolean ignoreBoundaries = false;
/*  194:     */     private GLU glu;
/*  195:     */     private GLUT glut;
/*  196: 403 */     private int x0Mouse = 0;
/*  197: 404 */     private int y0Mouse = 0;
/*  198: 405 */     private int x1Mouse = 0;
/*  199: 406 */     private int y1Mouse = 0;
/*  200: 410 */     private AnimationViewLocalStorable view = new AnimationViewLocalStorable();
/*  201: 414 */     private final String animationViewStorage = "animationViewState.xml";
/*  202: 418 */     private double xLookAt = 30.0D;
/*  203: 419 */     private double yLookAt = 0.0D;
/*  204: 420 */     private double zLookAt = 0.0D;
/*  205: 421 */     private double xEyeMin = 0.0D;
/*  206: 422 */     private double xEyeMax = 0.0D;
/*  207: 423 */     private double yEyeMin = 0.0D;
/*  208: 424 */     private double yEyeMax = 0.0D;
/*  209: 425 */     private double zEyeMin = 0.0D;
/*  210: 426 */     private double zEyeMax = 0.0D;
/*  211: 427 */     private double thetaEyeRate = 0.0D;
/*  212: 428 */     private double phiEyeRate = 0.0D;
/*  213: 429 */     private double xzEyeVelocity = 0.0D;
/*  214: 430 */     private double yEyeVelocity = 0.0D;
/*  215: 431 */     private double phiDriverHead = 0.0D;
/*  216: 432 */     private double thetaDriverHead = 0.0D;
/*  217:     */     private float alpha;
/*  218: 440 */     private boolean dataDisplay = false;
/*  219:     */     private static final int LIGHT_VIEW_NONE = 0;
/*  220:     */     private static final int LIGHT_VIEW_ORTHO = 1;
/*  221:     */     private static final int LIGHT_VIEW_TRAPEZIOD = 2;
/*  222: 447 */     private int lightView = 0;
/*  223: 452 */     private double trussCenterOffset = 5.0D;
/*  224:     */     private Gusset[] gussets;
/*  225: 460 */     private final Overlay walkOverlay = new Overlay();
/*  226: 464 */     private final Overlay headTiltOverlay = new Overlay();
/*  227: 468 */     private final Overlay strafeOverlay = new Overlay();
/*  228: 472 */     private final Overlay homeOverlay = new Overlay();
/*  229: 476 */     private final Overlay driveOverlay = new Overlay();
/*  230: 480 */     private final Overlay[] overlays = { this.walkOverlay, this.strafeOverlay, this.headTiltOverlay, this.homeOverlay, this.driveOverlay };
/*  231: 481 */     private final String[] overlayImageFileNames = { "walk.png", "strafe.png", "headtilt.png", "home.png", "drive.png" };
/*  232: 485 */     private Overlay activeOverlay = null;
/*  233: 489 */     private final Frustum frustum = new Frustum();
/*  234:     */     
/*  235:     */     public AnimationCanvas()
/*  236:     */     {
/*  237: 498 */       super();
/*  238:     */       
/*  239: 500 */       this.truckModel = new FlyThruTruckModel();
/*  240: 501 */       this.truckCabModel = new FlyThruTruckCabModel();
/*  241: 502 */       this.wheelModel = new FlyThruWheelModel();
/*  242: 503 */       if (WPBDApp.isLegacyGraphics()) {
/*  243: 504 */         FlyThruAnimation.this.config.canShowShadows = false;
/*  244:     */       }
/*  245: 506 */       addGLEventListener(this);
/*  246: 507 */       addMouseListener(this);
/*  247: 508 */       addMouseMotionListener(this);
/*  248: 509 */       addKeyListener(this);
/*  249:     */     }
/*  250:     */     
/*  251:     */     public void start()
/*  252:     */     {
/*  253: 516 */       if (this.gussets != null) {
/*  254: 518 */         return;
/*  255:     */       }
/*  256: 522 */       this.gussets = Gusset.getGussets(FlyThruAnimation.this.bridge);
/*  257: 523 */       double deckInterferingHalfGussetDepth = 0.0D;
/*  258: 524 */       for (int i = 0; i < this.gussets.length; i++) {
/*  259: 525 */         if (this.gussets[i].isInterferingWithLoad()) {
/*  260: 526 */           deckInterferingHalfGussetDepth = Math.max(deckInterferingHalfGussetDepth, this.gussets[i].getHalfDepth());
/*  261:     */         }
/*  262:     */       }
/*  263: 530 */       this.trussCenterOffset = (deckInterferingHalfGussetDepth + 5.0D + 0.03D);
/*  264:     */       
/*  265: 532 */       DesignConditions conditions = FlyThruAnimation.this.bridge.getDesignConditions();
/*  266:     */       
/*  267:     */ 
/*  268: 535 */       this.yEyeMax = (conditions.getOverMargin() + 15.0D);
/*  269: 536 */       this.xEyeMin = -100.0D;
/*  270: 537 */       this.xEyeMax = (100.0D + conditions.getSpanLength());
/*  271: 538 */       this.zEyeMin = -100.0D;
/*  272: 539 */       this.zEyeMax = 100.0D;
/*  273:     */       
/*  274:     */ 
/*  275: 542 */       int[] abutmentJointIndices = conditions.getAbutmentJointIndices();
/*  276: 543 */       double leastAbutmentGussetHalfDepth = this.gussets[abutmentJointIndices[0]].getHalfDepth();
/*  277: 544 */       for (int i = 1; i < abutmentJointIndices.length; i++) {
/*  278: 545 */         leastAbutmentGussetHalfDepth = Math.min(leastAbutmentGussetHalfDepth, this.gussets[abutmentJointIndices[i]].getHalfDepth());
/*  279:     */       }
/*  280: 549 */       FlyThruAnimation.this.terrain.initializeTerrain(conditions, (float)this.trussCenterOffset, (float)(this.trussCenterOffset + leastAbutmentGussetHalfDepth - 0.03D));
/*  281:     */       
/*  282:     */ 
/*  283:     */ 
/*  284:     */ 
/*  285: 554 */       FlyThruAnimation.this.resetState();
/*  286:     */       
/*  287:     */ 
/*  288: 557 */       String tag = conditions.getTag();
/*  289: 558 */       AnimationViewLocalStorable v = AnimationViewLocalStorable.load("animationViewState.xml");
/*  290: 559 */       if ((v != null) && (tag.equals(v.getScenarioTag())))
/*  291:     */       {
/*  292: 560 */         Animation.logger.log(Level.INFO, "Loaded view for {0} ({1})", new Object[] { tag, v });
/*  293: 561 */         this.view = v;
/*  294:     */       }
/*  295:     */       else
/*  296:     */       {
/*  297: 563 */         Animation.logger.log(Level.INFO, "Reset view for {0} ({1})", new Object[] { tag, v });
/*  298: 564 */         resetView();
/*  299:     */       }
/*  300: 569 */       this.view.setScenarioTag(tag);
/*  301:     */       
/*  302:     */ 
/*  303: 572 */       FlyThruAnimation.this.config.paused = false;
/*  304: 578 */       if (this.animator == null)
/*  305:     */       {
/*  306: 579 */         this.animator = new FPSAnimator(this, 40);
/*  307:     */         
/*  308: 581 */         this.animator.setUpdateFPSFrames(30, null);
/*  309: 582 */         this.animator.resetFPSCounter();
/*  310: 583 */         this.animator.start();
/*  311:     */       }
/*  312:     */       else
/*  313:     */       {
/*  314: 586 */         this.animator.resume();
/*  315:     */       }
/*  316:     */     }
/*  317:     */     
/*  318:     */     public void stop()
/*  319:     */     {
/*  320: 594 */       if (this.gussets != null)
/*  321:     */       {
/*  322: 595 */         this.animator.pause();
/*  323: 596 */         String tag = FlyThruAnimation.this.bridge.getDesignConditions().getTag();
/*  324: 597 */         Animation.logger.log(Level.INFO, "Save view for {0}", tag);
/*  325: 598 */         this.view.save("animationViewState.xml");
/*  326:     */         
/*  327: 600 */         this.gussets = null;
/*  328:     */       }
/*  329:     */     }
/*  330:     */     
/*  331: 604 */     private final byte[] alphaTestEnabled = new byte[1];
/*  332: 606 */     private double thetaWheels = 0.0D;
/*  333:     */     private static final int controlMargin = 200;
/*  334:     */     private static final int controlSeparation = 64;
/*  335:     */     
/*  336:     */     private void drawScene(GL2 gl, Analysis.Interpolation interpolation, int pass)
/*  337:     */     {
/*  338: 615 */       FlyThruAnimation.this.terrain.paint(gl, pass == 1);
/*  339:     */       
/*  340: 617 */       int nLoadedJoints = FlyThruAnimation.this.bridge.getDesignConditions().getNLoadedJoints();
/*  341: 618 */       Iterator<Joint> je = FlyThruAnimation.this.bridge.getJoints().iterator();
/*  342: 619 */       double xa = 0.0D;
/*  343: 620 */       double ya = 0.0D;
/*  344: 621 */       double dxa = 1.0D;
/*  345: 622 */       double dya = 0.0D;
/*  346: 623 */       double deckWidth = 10.0D;
/*  347: 624 */       double deckThickness = FlyThruAnimation.this.bridge.getDesignConditions().getDeckThickness();
/*  348: 625 */       double beamHeight = 0.8D - deckThickness - 0.1D;
/*  349: 626 */       while (je.hasNext())
/*  350:     */       {
/*  351: 627 */         Joint joint = (Joint)je.next();
/*  352: 628 */         int i = joint.getIndex();
/*  353: 629 */         Gusset gusset = this.gussets[i];
/*  354: 630 */         joint.paint(gl, interpolation.getDisplacement(i), (float)this.trussCenterOffset, gusset, this.ptDisplacedJoint);
/*  355: 631 */         double xb = this.ptDisplacedJoint.x;
/*  356: 632 */         double yb = this.ptDisplacedJoint.y;
/*  357: 633 */         double dxb = 1.0D;
/*  358: 634 */         double dyb = 0.0D;
/*  359: 635 */         if ((0 < i) && (i < nLoadedJoints))
/*  360:     */         {
/*  361: 637 */           dxb = xb - xa;
/*  362: 638 */           dyb = yb - ya;
/*  363: 639 */           double len = Math.sqrt(dxb * dxb + dyb * dyb);
/*  364: 640 */           dxb /= len;
/*  365: 641 */           dyb /= len;
/*  366: 643 */           if (i == 1) {
/*  367: 644 */             paintDeckBeam(gl, xa, ya, dxb, dyb, dxb, dyb, beamHeight, 10.0D);
/*  368:     */           } else {
/*  369: 646 */             paintDeckBeam(gl, xa, ya, dxa, dya, dxb, dyb, beamHeight, 10.0D);
/*  370:     */           }
/*  371: 648 */           if (i == nLoadedJoints - 1) {
/*  372: 649 */             paintDeckBeam(gl, xb, yb, dxb, dyb, dxb, dyb, beamHeight, 10.0D);
/*  373:     */           }
/*  374: 652 */           if (i == 1)
/*  375:     */           {
/*  376: 653 */             xa -= 0.32D * dxb;
/*  377: 654 */             ya -= 0.32D * dyb;
/*  378: 655 */             len += 0.32D;
/*  379:     */           }
/*  380: 656 */           else if (i == nLoadedJoints - 1)
/*  381:     */           {
/*  382: 657 */             len += 0.32D;
/*  383:     */           }
/*  384: 659 */           paintDeckPanel(gl, xa, ya, dxb, dyb, len, 10.0D, deckThickness);
/*  385:     */         }
/*  386: 661 */         xa = xb;
/*  387: 662 */         ya = yb;
/*  388: 663 */         dxa = dxb;
/*  389: 664 */         dya = dyb;
/*  390:     */       }
/*  391: 667 */       if (FlyThruAnimation.this.state >= 3)
/*  392:     */       {
/*  393: 668 */         Affine.Point ptLoad = interpolation.getPtLoad();
/*  394: 669 */         if (FlyThruAnimation.this.config.showTruck)
/*  395:     */         {
/*  396: 672 */           this.thetaWheels += 114.59155902616465D * FlyThruAnimation.this.getDistanceMoved();
/*  397: 673 */           while (this.thetaWheels > 360.0D) {
/*  398: 674 */             this.thetaWheels -= 360.0D;
/*  399:     */           }
/*  400: 677 */           gl.glPushMatrix();
/*  401: 678 */           gl.glTranslated(ptLoad.x, ptLoad.y, 0.0D);
/*  402: 679 */           gl.glMultMatrixf(interpolation.getLoadRotationMatrix(), 0);
/*  403:     */           
/*  404: 681 */           this.alpha = 1.0F;
/*  405: 682 */           float leftAlpha = (float)((FlyThruAnimation.this.loadLocation + FlyThruAnimation.this.loadLocationRunup) / 2.0D);
/*  406: 683 */           if (leftAlpha < this.alpha) {
/*  407: 684 */             this.alpha = leftAlpha;
/*  408:     */           }
/*  409: 686 */           float rightAlpha = (float)((FlyThruAnimation.this.loadEndDistanceTraveled - FlyThruAnimation.this.loadLocation) / 2.0D);
/*  410: 687 */           if (rightAlpha < this.alpha) {
/*  411: 688 */             this.alpha = rightAlpha;
/*  412:     */           }
/*  413: 690 */           this.alpha = Math.max(this.alpha, 0.0F);
/*  414:     */           
/*  415:     */ 
/*  416: 693 */           gl.glBlendFunc(770, 771);
/*  417: 694 */           gl.glEnable(3042);
/*  418:     */           
/*  419: 696 */           gl.glGetBooleanv(3008, this.alphaTestEnabled, 0);
/*  420: 697 */           boolean disableAlpha = (this.alpha < 1.0F) && (pass > 1) && (this.alphaTestEnabled[0] != 0);
/*  421: 698 */           if (disableAlpha) {
/*  422: 699 */             gl.glDisable(3008);
/*  423:     */           }
/*  424: 701 */           if (driving())
/*  425:     */           {
/*  426: 702 */             this.truckCabModel.display(gl);
/*  427:     */           }
/*  428:     */           else
/*  429:     */           {
/*  430: 704 */             this.truckModel.setAlpha(this.alpha);
/*  431: 705 */             this.wheelModel.setAlpha(this.alpha);
/*  432:     */             
/*  433:     */ 
/*  434: 708 */             gl.glPushMatrix();
/*  435: 709 */             gl.glTranslated(0.0D, 0.5D, 0.949999988079071D);
/*  436: 710 */             gl.glRotated(this.thetaWheels, 0.0D, 0.0D, -1.0D);
/*  437: 711 */             this.wheelModel.displaySingle(gl);
/*  438: 712 */             gl.glPopMatrix();
/*  439:     */             
/*  440:     */ 
/*  441: 715 */             gl.glPushMatrix();
/*  442: 716 */             gl.glTranslated(-4.0D, 0.5D, 0.949999988079071D);
/*  443: 717 */             gl.glRotated(this.thetaWheels, 0.0D, 0.0D, -1.0D);
/*  444: 718 */             this.wheelModel.displayDual(gl);
/*  445: 719 */             gl.glPopMatrix();
/*  446:     */             
/*  447:     */ 
/*  448: 722 */             gl.glPushMatrix();
/*  449: 723 */             gl.glTranslated(0.0D, 0.5D, -0.949999988079071D);
/*  450: 724 */             gl.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
/*  451: 725 */             gl.glRotated(this.thetaWheels, 0.0D, 0.0D, 1.0D);
/*  452: 726 */             this.wheelModel.displaySingle(gl);
/*  453: 727 */             gl.glPopMatrix();
/*  454:     */             
/*  455:     */ 
/*  456: 730 */             gl.glPushMatrix();
/*  457: 731 */             gl.glTranslated(-4.0D, 0.5D, -0.949999988079071D);
/*  458: 732 */             gl.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
/*  459: 733 */             gl.glRotated(this.thetaWheels, 0.0D, 0.0D, 1.0D);
/*  460: 734 */             this.wheelModel.displayDual(gl);
/*  461: 735 */             gl.glPopMatrix();
/*  462:     */             
/*  463: 737 */             this.truckModel.display(gl);
/*  464: 738 */             if (disableAlpha) {
/*  465: 739 */               gl.glEnable(3008);
/*  466:     */             }
/*  467: 741 */             gl.glDisable(3042);
/*  468:     */           }
/*  469: 743 */           gl.glPopMatrix();
/*  470:     */         }
/*  471:     */       }
/*  472: 747 */       Iterator<Member> me = FlyThruAnimation.this.bridge.getMembers().iterator();
/*  473: 748 */       while (me.hasNext())
/*  474:     */       {
/*  475: 749 */         Member m = (Member)me.next();
/*  476: 750 */         int im = m.getIndex();
/*  477: 751 */         m.paint(gl, interpolation.getDisplacement(m.getJointA().getIndex()), interpolation.getDisplacement(m.getJointB().getIndex()), this.trussCenterOffset, interpolation.getForceRatio(im), FlyThruAnimation.this.config.showForcesAsColors, interpolation.getMemberStatus(im));
/*  478:     */       }
/*  479:     */     }
/*  480:     */     
/*  481:     */     private void resetView()
/*  482:     */     {
/*  483: 762 */       Rectangle2D extent = FlyThruAnimation.this.bridge.getExtent(null);
/*  484: 763 */       double width = extent.getWidth();
/*  485: 764 */       double height = extent.getHeight();
/*  486: 765 */       this.view.zEye = (1.2D * Math.max(width, 1.75D * height));
/*  487: 766 */       this.view.xEye = (this.xLookAt = extent.getMinX() + 0.5D * width);
/*  488:     */       
/*  489: 768 */       this.yLookAt = (extent.getMinY() + 0.5D * height);
/*  490:     */       
/*  491: 770 */       this.view.yEye = 1.0D;
/*  492:     */       
/*  493: 772 */       this.view.xEye -= this.view.zEye * 0.1D;
/*  494: 773 */       this.zLookAt = 0.0D;
/*  495: 774 */       this.yEyeVelocity = 0.0D;
/*  496:     */       
/*  497:     */ 
/*  498: 777 */       this.view.thetaEye = Math.atan2(this.xLookAt - this.view.xEye, this.view.zEye - this.zLookAt);
/*  499: 778 */       this.thetaEyeRate = 0.0D;
/*  500:     */       
/*  501: 780 */       this.view.phiEye = Math.atan2(this.yLookAt - this.view.yEye, this.view.zEye - this.zLookAt);
/*  502: 781 */       this.phiEyeRate = 0.0D;
/*  503: 782 */       this.lightView = 0;
/*  504:     */     }
/*  505:     */     
/*  506:     */     public void init(GLAutoDrawable glDrawable)
/*  507:     */     {
/*  508: 789 */       this.glu = new GLU();
/*  509: 790 */       this.glut = new GLUT();
/*  510:     */       
/*  511: 792 */       GL2 gl = glDrawable.getGL().getGL2();
/*  512: 795 */       if ((FlyThruAnimation.this.config.canShowShadows) && (FlyThruAnimation.getInteger(gl, 34018) < 2))
/*  513:     */       {
/*  514: 796 */         Animation.logger.log(Level.INFO, "No shadows possible with {0} TUs", Integer.valueOf(FlyThruAnimation.getInteger(gl, 34018)));
/*  515: 797 */         FlyThruAnimation.this.config.canShowShadows = false;
/*  516:     */       }
/*  517: 801 */       FlyThruAnimation.this.terrain.initializeTerrainTextures(gl);
/*  518: 804 */       for (int i = 0; i < this.overlays.length; i++) {
/*  519: 805 */         this.overlays[i].initialize(gl, this.overlayImageFileNames[i]);
/*  520:     */       }
/*  521: 810 */       if (FlyThruAnimation.this.config.canShowShadows)
/*  522:     */       {
/*  523: 811 */         this.shadowMapTexture = null;
/*  524: 812 */         this.shadowFrameBuffer = -1;
/*  525: 813 */         tryShadowFrameBufferAllocation(gl);
/*  526:     */       }
/*  527: 817 */       gl.glMatrixMode(5888);
/*  528: 818 */       gl.glLoadIdentity();
/*  529:     */       
/*  530:     */ 
/*  531: 821 */       gl.glShadeModel(7425);
/*  532: 822 */       gl.glClearColor(0.0F, 0.75F, 1.0F, 1.0F);
/*  533: 823 */       gl.glHint(3152, 4354);
/*  534:     */       
/*  535:     */ 
/*  536: 826 */       gl.glEnable(2912);
/*  537: 827 */       gl.glFogi(2917, 2049);
/*  538: 828 */       gl.glFogfv(2918, this.fogColor, 0);
/*  539: 829 */       gl.glFogf(2914, 0.0038F);
/*  540: 830 */       gl.glHint(3156, 4352);
/*  541:     */       
/*  542:     */ 
/*  543: 833 */       gl.glClearDepth(1.0D);
/*  544: 834 */       gl.glDepthFunc(515);
/*  545:     */       
/*  546: 836 */       gl.glEnable(2884);
/*  547:     */       
/*  548:     */ 
/*  549: 839 */       gl.glEnable(2977);
/*  550:     */       
/*  551:     */ 
/*  552: 842 */       gl.glPushMatrix();
/*  553:     */       
/*  554:     */ 
/*  555: 845 */       gl.glLoadIdentity();
/*  556: 846 */       this.glu.gluLookAt(0.0F, 0.0F, 0.0F, -Animation.lightPosition.x(), -Animation.lightPosition.y(), -Animation.lightPosition.z(), 0.0F, 1.0F, 0.0F);
/*  557:     */       
/*  558:     */ 
/*  559: 849 */       gl.glGetFloatv(2982, this.lightViewMatrix.a, 0);
/*  560:     */       
/*  561:     */ 
/*  562: 852 */       gl.glEnable(2903);
/*  563:     */       
/*  564:     */ 
/*  565: 855 */       gl.glLoadIdentity();
/*  566: 856 */       gl.glOrtho(-150.0D, 150.0D, -150.0D, 150.0D, -150.0D, 150.0D);
/*  567: 857 */       gl.glGetFloatv(2982, this.lightProjectionMatrix.a, 0);
/*  568: 858 */       gl.glPopMatrix();
/*  569:     */     }
/*  570:     */     
/*  571:     */     public void display(GLAutoDrawable glDrawable)
/*  572:     */     {
/*  573: 864 */       if (this.gussets == null) {
/*  574: 865 */         return;
/*  575:     */       }
/*  576: 868 */       GL2 gl = glDrawable.getGL().getGL2();
/*  577:     */       
/*  578: 870 */       long time = System.nanoTime();
/*  579:     */       
/*  580:     */ 
/*  581:     */ 
/*  582: 874 */       Analysis.Interpolation interpolation = FlyThruAnimation.this.interpolate(time);
/*  583: 876 */       if ((FlyThruAnimation.this.config.canShowShadows) && (FlyThruAnimation.this.config.showShadows))
/*  584:     */       {
/*  585: 880 */         gl.glEnable(2929);
/*  586:     */         
/*  587: 882 */         gl.glDepthFunc(515);
/*  588: 883 */         if (FlyThruAnimation.this.config.showSky)
/*  589:     */         {
/*  590: 885 */           if (!isShadowFrameBuffer()) {
/*  591: 886 */             gl.glClear(256);
/*  592:     */           }
/*  593:     */         }
/*  594:     */         else {
/*  595: 889 */           gl.glClear(isShadowFrameBuffer() ? 16384 : 16640);
/*  596:     */         }
/*  597: 893 */         gl.glMatrixMode(5888);
/*  598: 894 */         setLookAtMatrix(gl, interpolation);
/*  599: 895 */         gl.glGetFloatv(2982, this.eyeViewMatrix.a, 0);
/*  600: 896 */         this.frustum.getTrapezoidalProjection(this.trapezoidalProjectionMatrix, this.eyeViewMatrix, this.lightViewMatrix, -750.0F, 750.0F);
/*  601:     */         
/*  602:     */ 
/*  603:     */ 
/*  604:     */ 
/*  605:     */ 
/*  606:     */ 
/*  607:     */ 
/*  608:     */ 
/*  609:     */ 
/*  610:     */ 
/*  611:     */ 
/*  612:     */ 
/*  613:     */ 
/*  614:     */ 
/*  615:     */ 
/*  616:     */ 
/*  617:     */ 
/*  618:     */ 
/*  619:     */ 
/*  620:     */ 
/*  621: 917 */         gl.glMatrixMode(5889);
/*  622: 918 */         gl.glLoadMatrixf(this.trapezoidalProjectionMatrix.a, 0);
/*  623:     */         
/*  624:     */ 
/*  625: 921 */         gl.glMatrixMode(5889);
/*  626:     */         
/*  627: 923 */         gl.glLoadMatrixf(this.trapezoidalProjectionMatrix.a, 0);
/*  628: 924 */         gl.glMatrixMode(5888);
/*  629: 925 */         gl.glLoadMatrixf(this.lightViewMatrix.a, 0);
/*  630:     */         
/*  631:     */ 
/*  632: 928 */         gl.glViewport(0, 0, this.shadowMapWidth, this.shadowMapHeight);
/*  633:     */         
/*  634:     */ 
/*  635: 931 */         gl.glCullFace(1028);
/*  636:     */         
/*  637:     */ 
/*  638: 934 */         gl.glColorMask(false, false, false, false);
/*  639: 937 */         if (isShadowFrameBuffer())
/*  640:     */         {
/*  641: 939 */           gl.glBindFramebuffer(36160, this.shadowFrameBuffer);
/*  642: 940 */           gl.glClear(256);
/*  643: 941 */           drawScene(gl, interpolation, 1);
/*  644: 942 */           gl.glBindFramebuffer(36160, 0);
/*  645:     */         }
/*  646:     */         else
/*  647:     */         {
/*  648: 945 */           drawScene(gl, interpolation, 1);
/*  649:     */           
/*  650: 947 */           gl.glActiveTexture(33985);
/*  651: 948 */           this.shadowMapTexture.bind(gl);
/*  652: 949 */           gl.glCopyTexSubImage2D(3553, 0, 0, 0, 0, 0, this.shadowMapWidth, this.shadowMapHeight);
/*  653:     */         }
/*  654: 953 */         gl.glCullFace(1029);
/*  655: 954 */         gl.glShadeModel(7425);
/*  656: 955 */         gl.glColorMask(true, true, true, true);
/*  657: 956 */         gl.glClear(256);
/*  658: 959 */         if (this.lightView > 0)
/*  659:     */         {
/*  660: 960 */           gl.glDisable(2912);
/*  661: 961 */           gl.glMatrixMode(5889);
/*  662: 962 */           gl.glLoadMatrixf(this.lightView == 2 ? this.trapezoidalProjectionMatrix.a : this.lightProjectionMatrix.a, 0);
/*  663:     */           
/*  664:     */ 
/*  665: 965 */           gl.glMatrixMode(5888);
/*  666: 966 */           gl.glLoadMatrixf(this.lightViewMatrix.a, 0);
/*  667:     */         }
/*  668:     */         else
/*  669:     */         {
/*  670: 968 */           gl.glEnable(2912);
/*  671: 969 */           gl.glMatrixMode(5889);
/*  672: 970 */           gl.glLoadMatrixf(this.eyeProjectionMatrix.a, 0);
/*  673:     */           
/*  674: 972 */           gl.glMatrixMode(5888);
/*  675: 973 */           gl.glLoadMatrixf(this.eyeViewMatrix.a, 0);
/*  676:     */         }
/*  677: 975 */         gl.glViewport(this.windowX, this.windowY, this.windowWidth, this.windowHeight);
/*  678:     */         
/*  679:     */ 
/*  680:     */ 
/*  681:     */ 
/*  682: 980 */         gl.glLightfv(16384, 4610, this.black, 0);
/*  683: 981 */         gl.glLightModelfv(2899, FlyThruAnimation.this.config.lightBrightness, 0);
/*  684: 982 */         gl.glLightfv(16384, 4611, Animation.lightPosition.a, 0);
/*  685: 983 */         gl.glLightfv(16384, 4609, this.black, 0);
/*  686: 984 */         gl.glEnable(16384);
/*  687: 985 */         gl.glEnable(2896);
/*  688:     */         
/*  689: 987 */         drawScene(gl, interpolation, 2);
/*  690:     */         
/*  691:     */ 
/*  692:     */ 
/*  693: 991 */         gl.glLightfv(16384, 4609, this.white, 0);
/*  694:     */         
/*  695:     */ 
/*  696: 994 */         gl.glPushMatrix();
/*  697: 995 */         gl.glLoadMatrixf(FlyThruAnimation.biasMatrix.a, 0);
/*  698: 996 */         gl.glMultMatrixf(this.trapezoidalProjectionMatrix.a, 0);
/*  699: 997 */         gl.glMultMatrixf(this.lightViewMatrix.a, 0);
/*  700: 998 */         gl.glGetFloatv(2982, this.textureMatrix.a, 0);
/*  701: 999 */         this.textureMatrix.transposeInPlace();
/*  702:1000 */         gl.glPopMatrix();
/*  703:     */         
/*  704:     */ 
/*  705:1003 */         gl.glActiveTexture(33985);
/*  706:     */         
/*  707:1005 */         gl.glTexGeni(8192, 9472, 9216);
/*  708:1006 */         gl.glTexGenfv(8192, 9474, this.textureMatrix.a, 0);
/*  709:1007 */         gl.glEnable(3168);
/*  710:     */         
/*  711:1009 */         gl.glTexGeni(8193, 9472, 9216);
/*  712:1010 */         gl.glTexGenfv(8193, 9474, this.textureMatrix.a, 4);
/*  713:1011 */         gl.glEnable(3169);
/*  714:     */         
/*  715:1013 */         gl.glTexGeni(8194, 9472, 9216);
/*  716:1014 */         gl.glTexGenfv(8194, 9474, this.textureMatrix.a, 8);
/*  717:1015 */         gl.glEnable(3170);
/*  718:     */         
/*  719:1017 */         gl.glTexGeni(8195, 9472, 9216);
/*  720:1018 */         gl.glTexGenfv(8195, 9474, this.textureMatrix.a, 12);
/*  721:1019 */         gl.glEnable(3171);
/*  722:     */         
/*  723:     */ 
/*  724:1022 */         this.shadowMapTexture.bind(gl);
/*  725:1023 */         this.shadowMapTexture.enable(gl);
/*  726:     */         
/*  727:     */ 
/*  728:1026 */         this.shadowMapTexture.setTexParameteri(gl, 34892, 34894);
/*  729:     */         
/*  730:     */ 
/*  731:1029 */         this.shadowMapTexture.setTexParameteri(gl, 34893, 515);
/*  732:     */         
/*  733:     */ 
/*  734:1032 */         this.shadowMapTexture.setTexParameteri(gl, 34891, 32841);
/*  735:     */         
/*  736:     */ 
/*  737:1035 */         gl.glAlphaFunc(518, 1.0F);
/*  738:1036 */         gl.glEnable(3008);
/*  739:     */         
/*  740:1038 */         drawScene(gl, interpolation, 3);
/*  741:     */         
/*  742:     */ 
/*  743:1041 */         gl.glActiveTexture(33985);
/*  744:1042 */         this.shadowMapTexture.disable(gl);
/*  745:1043 */         gl.glDisable(3168);
/*  746:1044 */         gl.glDisable(3169);
/*  747:1045 */         gl.glDisable(3170);
/*  748:1046 */         gl.glDisable(3171);
/*  749:     */         
/*  750:     */ 
/*  751:1049 */         gl.glDisable(3008);
/*  752:1050 */         gl.glDisable(2896);
/*  753:1051 */         if (this.lightView == 1) {
/*  754:1052 */           this.frustum.draw(gl, this.lightViewMatrix, this.lightProjectionMatrix);
/*  755:     */         }
/*  756:     */       }
/*  757:     */       else
/*  758:     */       {
/*  759:1055 */         gl.glClear(16640);
/*  760:1056 */         gl.glEnable(2929);
/*  761:     */         
/*  762:1058 */         gl.glDepthFunc(515);
/*  763:     */         
/*  764:1060 */         gl.glEnable(2912);
/*  765:1061 */         gl.glMatrixMode(5889);
/*  766:1062 */         gl.glLoadMatrixf(this.eyeProjectionMatrix.a, 0);
/*  767:     */         
/*  768:1064 */         gl.glMatrixMode(5888);
/*  769:1065 */         setLookAtMatrix(gl, interpolation);
/*  770:     */         
/*  771:1067 */         gl.glViewport(this.windowX, this.windowY, this.windowWidth, this.windowHeight);
/*  772:     */         
/*  773:1069 */         gl.glShadeModel(7425);
/*  774:1070 */         gl.glEnable(2896);
/*  775:1071 */         gl.glEnable(16384);
/*  776:1072 */         gl.glLightfv(16384, 4611, Animation.lightPosition.a, 0);
/*  777:1073 */         gl.glLightfv(16384, 4609, this.white, 0);
/*  778:1074 */         gl.glLightModelfv(2899, FlyThruAnimation.this.config.lightBrightness, 0);
/*  779:     */         
/*  780:1076 */         drawScene(gl, interpolation, 4);
/*  781:     */       }
/*  782:1079 */       gl.glDisable(2929);
/*  783:1080 */       gl.glMatrixMode(5889);
/*  784:1081 */       gl.glLoadMatrixf(this.mouseProjectionMatrix.a, 0);
/*  785:1082 */       gl.glMatrixMode(5888);
/*  786:1083 */       gl.glLoadIdentity();
/*  787:     */       
/*  788:     */ 
/*  789:1086 */       gl.glBegin(1);
/*  790:1087 */       gl.glColor3fv(this.white, 0);
/*  791:1088 */       gl.glVertex2d(this.x0Mouse, this.y0Mouse);
/*  792:1089 */       gl.glVertex2d(this.x1Mouse, this.y1Mouse);
/*  793:1090 */       gl.glEnd();
/*  794:1092 */       for (int i = 0; i < this.overlays.length; i++) {
/*  795:1093 */         this.overlays[i].display(gl);
/*  796:     */       }
/*  797:1096 */       if (this.dataDisplay)
/*  798:     */       {
/*  799:1098 */         String msg = String.format("%.1f fps eye=(%.1f,%.1f,%.1f) look=(%.1f,%.1f,%.1f) yTerrain=%.1f", new Object[] { Float.valueOf(this.animator.getLastFPS()), Double.valueOf(this.view.xEye), Double.valueOf(this.view.yEye), Double.valueOf(this.view.zEye), Double.valueOf(this.xLookAt), Double.valueOf(this.yLookAt), Double.valueOf(this.zLookAt), Double.valueOf(this.yEyeMin) });
/*  800:     */         
/*  801:1100 */         gl.glRasterPos2i(4, this.windowHeight - 4);
/*  802:1101 */         this.glut.glutBitmapString(6, msg);
/*  803:     */       }
/*  804:     */     }
/*  805:     */     
/*  806:     */     private void setLookAtMatrix(GL2 gl, Analysis.Interpolation interpolation)
/*  807:     */     {
/*  808:1106 */       gl.glLoadIdentity();
/*  809:1107 */       if (driving())
/*  810:     */       {
/*  811:1108 */         Affine.Point eye = interpolation.getPtLoad();
/*  812:1109 */         Affine.Vector look = interpolation.getLoadRotation();
/*  813:1110 */         gl.glRotated(this.phiDriverHead, -1.0D, 0.0D, 0.0D);
/*  814:1111 */         gl.glRotated(this.thetaDriverHead, 0.0D, 1.0D, 0.0D);
/*  815:1112 */         this.glu.gluLookAt(eye.x + 0.6D, eye.y + 2.4D, 0.0D, eye.x + look.x, eye.y + look.y + 2.4D, 0.0D, 0.0D, 1.0D, 0.0D);
/*  816:     */       }
/*  817:     */       else
/*  818:     */       {
/*  819:1114 */         this.glu.gluLookAt(this.view.xEye, this.view.yEye, this.view.zEye, this.xLookAt, this.yLookAt, this.zLookAt, 0.0D, 1.0D, 0.0D);
/*  820:     */       }
/*  821:     */     }
/*  822:     */     
/*  823:     */     public void reshape(GLAutoDrawable glDrawable, int x, int y, int width, int height)
/*  824:     */     {
/*  825:1131 */       GL2 gl = glDrawable.getGL().getGL2();
/*  826:1133 */       if ((x == this.windowX) && (y == this.windowY) && (width == this.windowWidth) && (height == this.windowHeight)) {
/*  827:1134 */         Animation.logger.log(Level.INFO, "Redundant reshape.");
/*  828:     */       }
/*  829:1138 */       this.windowX = x;
/*  830:1139 */       this.windowY = y;
/*  831:1140 */       this.windowWidth = width;
/*  832:1141 */       this.windowHeight = height;
/*  833:1144 */       for (int i = 0; i < this.overlays.length; i++) {
/*  834:1145 */         this.overlays[i].setPosition(200, 200 + (i + 1) * 64);
/*  835:     */       }
/*  836:1149 */       gl.glPushMatrix();
/*  837:     */       
/*  838:1151 */       this.frustum.set(45.0F, width / height, 0.333333F, 400.0F, 0.5F);
/*  839:1152 */       gl.glLoadIdentity();
/*  840:1153 */       this.frustum.apply(gl);
/*  841:1154 */       gl.glGetFloatv(2982, this.eyeProjectionMatrix.a, 0);
/*  842:     */       
/*  843:1156 */       gl.glLoadIdentity();
/*  844:1157 */       this.glu.gluOrtho2D(0.0D, width - 1, height - 1, 0.0D);
/*  845:1158 */       gl.glGetFloatv(2982, this.mouseProjectionMatrix.a, 0);
/*  846:     */       
/*  847:1160 */       gl.glPopMatrix();
/*  848:1163 */       if ((FlyThruAnimation.this.config.canShowShadows) && (!isShadowFrameBuffer()))
/*  849:     */       {
/*  850:1164 */         Animation.logger.log(Level.INFO, "No frame buffer: shadows using back buffer.");
/*  851:1165 */         reallocateShadowMapTexture(gl, FlyThruAnimation.powerOf2AtMost(width), FlyThruAnimation.powerOf2AtMost(height), false);
/*  852:     */       }
/*  853:1167 */       FlyThruAnimation.this.applyCanvasResizeBugWorkaround();
/*  854:1168 */       Animation.logger.log(Level.INFO, "Reshape {0} to [{1},{2};{3},{4}]min={5}", new Object[] { glDrawable, Integer.valueOf(x), Integer.valueOf(y), Integer.valueOf(width), Integer.valueOf(height), getMinimumSize() });
/*  855:     */     }
/*  856:     */     
/*  857:     */     public void displayChanged(GLAutoDrawable glDrawable, boolean arg1, boolean arg2) {}
/*  858:     */     
/*  859:     */     public void mouseClicked(MouseEvent e) {}
/*  860:     */     
/*  861:     */     private boolean moveLateral()
/*  862:     */     {
/*  863:1179 */       return this.activeOverlay == this.strafeOverlay;
/*  864:     */     }
/*  865:     */     
/*  866:     */     private boolean lookUpDown()
/*  867:     */     {
/*  868:1183 */       return this.activeOverlay == this.headTiltOverlay;
/*  869:     */     }
/*  870:     */     
/*  871:     */     private boolean walking()
/*  872:     */     {
/*  873:1187 */       return this.activeOverlay == this.walkOverlay;
/*  874:     */     }
/*  875:     */     
/*  876:     */     private boolean home()
/*  877:     */     {
/*  878:1191 */       return this.activeOverlay == this.homeOverlay;
/*  879:     */     }
/*  880:     */     
/*  881:     */     private boolean driving()
/*  882:     */     {
/*  883:1195 */       return this.activeOverlay == this.driveOverlay;
/*  884:     */     }
/*  885:     */     
/*  886:1200 */     private final float[] tubeVerticesLeft = { -0.5F, 1.0F, 0.5F, -0.5F, 1.0F, -0.5F, -0.5F, 0.0F, -0.5F, -0.5F, 0.0F, 0.5F, -0.5F, 1.0F, 0.5F };
/*  887:1206 */     private final float[] tubeVerticesRight = { 0.5F, 1.0F, 0.5F, 0.5F, 1.0F, -0.5F, 0.5F, 0.0F, -0.5F, 0.5F, 0.0F, 0.5F, 0.5F, 1.0F, 0.5F };
/*  888:1212 */     private final float[] tubeNormals = { 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, -1.0F, 0.0F, -1.0F, 0.0F, 0.0F, 0.0F, 1.0F };
/*  889:     */     
/*  890:     */     private void paintDeckBeam(GL2 gl, double x, double y, double uxa, double uya, double uxb, double uyb, double height, double length)
/*  891:     */     {
/*  892:1232 */       gl.glPushMatrix();
/*  893:1233 */       gl.glTranslated(x, y, 0.0D);
/*  894:     */       
/*  895:1235 */       double dx = 0.5D * (uxa + uxb);
/*  896:1236 */       double dy = 0.5D * (uya + uyb);
/*  897:1237 */       double len = Math.sqrt(dx * dx + dy * dy);
/*  898:1238 */       gl.glMultMatrixf(Utility.rotateAboutZ(dx / len, dy / len), 0);
/*  899:1239 */       gl.glTranslated(0.0D, 0.1D, 0.0D);
/*  900:1240 */       gl.glScaled(0.12D, height, length);
/*  901:1241 */       gl.glColor3fv(this.beamMaterial, 0);
/*  902:1242 */       gl.glBegin(7);
/*  903:1243 */       for (int i = 0; i < this.tubeNormals.length; i += 3)
/*  904:     */       {
/*  905:1244 */         gl.glNormal3fv(this.tubeNormals, i);
/*  906:1245 */         gl.glVertex3fv(this.tubeVerticesLeft, i + 0);
/*  907:1246 */         gl.glVertex3fv(this.tubeVerticesRight, i + 0);
/*  908:1247 */         gl.glVertex3fv(this.tubeVerticesRight, i + 3);
/*  909:1248 */         gl.glVertex3fv(this.tubeVerticesLeft, i + 3);
/*  910:     */       }
/*  911:1250 */       gl.glNormal3f(-1.0F, 0.0F, 0.0F);
/*  912:1251 */       for (int i = 0; i < 12; i += 3) {
/*  913:1252 */         gl.glVertex3fv(this.tubeVerticesLeft, i);
/*  914:     */       }
/*  915:1254 */       gl.glNormal3f(1.0F, 0.0F, 0.0F);
/*  916:1255 */       for (int i = 9; i >= 0; i -= 3) {
/*  917:1256 */         gl.glVertex3fv(this.tubeVerticesRight, i);
/*  918:     */       }
/*  919:1258 */       gl.glEnd();
/*  920:1259 */       gl.glPopMatrix();
/*  921:     */     }
/*  922:     */     
/*  923:     */     private void paintDeckPanel(GL2 gl, double x, double y, double ux, double uy, double length, double width, double thickness)
/*  924:     */     {
/*  925:1275 */       gl.glPushMatrix();
/*  926:1276 */       gl.glTranslated(x, y + 0.8D, 0.0D);
/*  927:1277 */       gl.glMultMatrixf(Utility.rotateAboutZ(ux, uy), 0);
/*  928:1278 */       gl.glScaled(length, thickness, width);
/*  929:1279 */       gl.glShadeModel(7424);
/*  930:1280 */       gl.glColor3fv(this.deckColor, 0);
/*  931:1281 */       gl.glBegin(7);
/*  932:1282 */       for (int i = 0; i < FlyThruAnimation.deckNormals.length; i += 3)
/*  933:     */       {
/*  934:1283 */         gl.glNormal3fv(FlyThruAnimation.deckNormals, i);
/*  935:1284 */         gl.glVertex3fv(FlyThruAnimation.deckVerticesLeft, i + 0);
/*  936:1285 */         gl.glVertex3fv(FlyThruAnimation.deckVerticesRight, i + 0);
/*  937:1286 */         gl.glVertex3fv(FlyThruAnimation.deckVerticesRight, i + 3);
/*  938:1287 */         gl.glVertex3fv(FlyThruAnimation.deckVerticesLeft, i + 3);
/*  939:     */       }
/*  940:1289 */       gl.glEnd();
/*  941:1290 */       gl.glPopMatrix();
/*  942:     */     }
/*  943:     */     
/*  944:     */     public void mousePressed(MouseEvent e)
/*  945:     */     {
/*  946:1299 */       for (int i = 0; i < this.overlays.length; i++) {
/*  947:1300 */         if (this.overlays[i].inside(e.getX(), e.getY()))
/*  948:     */         {
/*  949:1301 */           this.activeOverlay = this.overlays[i];
/*  950:1302 */           this.x0Mouse = (this.x1Mouse = this.activeOverlay.getX());
/*  951:1303 */           this.y0Mouse = (this.y1Mouse = this.activeOverlay.getY());
/*  952:1304 */           if (home()) {
/*  953:1305 */             resetView();
/*  954:     */           }
/*  955:1307 */           display();
/*  956:1308 */           return;
/*  957:     */         }
/*  958:     */       }
/*  959:1311 */       this.activeOverlay = null;
/*  960:     */     }
/*  961:     */     
/*  962:     */     public void mouseDragged(MouseEvent e)
/*  963:     */     {
/*  964:1320 */       if (this.activeOverlay != null)
/*  965:     */       {
/*  966:1321 */         this.x1Mouse = e.getX();
/*  967:1322 */         this.y1Mouse = e.getY();
/*  968:1323 */         int dx = this.x1Mouse - this.x0Mouse;
/*  969:1324 */         int dy = this.y0Mouse - this.y1Mouse;
/*  970:1325 */         if (moveLateral())
/*  971:     */         {
/*  972:1326 */           this.xzEyeVelocity = (dx * 0.1D);
/*  973:1327 */           this.yEyeVelocity = (dy * 0.1D);
/*  974:     */         }
/*  975:1328 */         else if (lookUpDown())
/*  976:     */         {
/*  977:1329 */           this.phiEyeRate = (dy * 0.003141592653589793D);
/*  978:1330 */           this.thetaEyeRate = (dx * 0.003141592653589793D);
/*  979:     */         }
/*  980:1331 */         else if (walking())
/*  981:     */         {
/*  982:1332 */           this.xzEyeVelocity = (dy * 0.1D);
/*  983:1333 */           this.thetaEyeRate = (dx * 0.003141592653589793D);
/*  984:     */         }
/*  985:1334 */         else if (home())
/*  986:     */         {
/*  987:1335 */           this.xzEyeVelocity = (dy * 0.1D);
/*  988:1336 */           this.thetaEyeRate = (dx * 0.003141592653589793D);
/*  989:     */         }
/*  990:1337 */         else if (driving())
/*  991:     */         {
/*  992:1338 */           this.phiDriverHead = Utility.clamp(dy * 0.3141592653589793D, -45.0D, 20.0D);
/*  993:1339 */           this.thetaDriverHead = Utility.clamp(1.5D * dx * 0.3141592653589793D, -100.0D, 100.0D);
/*  994:     */         }
/*  995:     */       }
/*  996:     */     }
/*  997:     */     
/*  998:     */     public void mouseReleased(MouseEvent e)
/*  999:     */     {
/* 1000:1350 */       if (this.activeOverlay != null)
/* 1001:     */       {
/* 1002:1351 */         this.x0Mouse = this.x1Mouse;
/* 1003:1352 */         this.y0Mouse = this.y1Mouse;
/* 1004:1353 */         this.activeOverlay = null;
/* 1005:1354 */         this.thetaEyeRate = (this.phiEyeRate = this.xzEyeVelocity = this.yEyeVelocity = this.phiDriverHead = this.thetaDriverHead = 0.0D);
/* 1006:1355 */         display();
/* 1007:     */       }
/* 1008:     */     }
/* 1009:     */     
/* 1010:     */     public void mouseEntered(MouseEvent e)
/* 1011:     */     {
/* 1012:1365 */       mouseMoved(e);
/* 1013:     */     }
/* 1014:     */     
/* 1015:     */     public void mouseExited(MouseEvent e)
/* 1016:     */     {
/* 1017:1374 */       mouseMoved(e);
/* 1018:     */     }
/* 1019:     */     
/* 1020:     */     public void mouseMoved(MouseEvent e)
/* 1021:     */     {
/* 1022:1383 */       int x = e.getX();
/* 1023:1384 */       int y = e.getY();
/* 1024:1385 */       for (int i = 0; i < this.overlays.length; i++) {
/* 1025:1386 */         this.overlays[i].mouseMoved(x, y);
/* 1026:     */       }
/* 1027:     */     }
/* 1028:     */     
/* 1029:     */     public void keyTyped(KeyEvent e)
/* 1030:     */     {
/* 1031:1391 */       switch (e.getKeyChar())
/* 1032:     */       {
/* 1033:     */       case 'D': 
/* 1034:     */       case 'd': 
/* 1035:1394 */         this.dataDisplay = (!this.dataDisplay);
/* 1036:1395 */         break;
/* 1037:     */       case 'L': 
/* 1038:     */       case 'l': 
/* 1039:1398 */         this.lightView = ((this.lightView + 1) % 3);
/* 1040:1399 */         break;
/* 1041:     */       case 'R': 
/* 1042:     */       case 'r': 
/* 1043:1402 */         resetView();
/* 1044:1403 */         break;
/* 1045:     */       case 'I': 
/* 1046:     */       case 'i': 
/* 1047:1412 */         this.ignoreBoundaries = (!this.ignoreBoundaries);
/* 1048:     */       }
/* 1049:     */     }
/* 1050:     */     
/* 1051:     */     public void keyPressed(KeyEvent e)
/* 1052:     */     {
/* 1053:1418 */       switch (e.getKeyCode())
/* 1054:     */       {
/* 1055:     */       case 38: 
/* 1056:     */         break;
/* 1057:     */       case 40: 
/* 1058:     */         break;
/* 1059:     */       case 37: 
/* 1060:     */         break;
/* 1061:     */       }
/* 1062:     */     }
/* 1063:     */     
/* 1064:     */     public void keyReleased(KeyEvent e)
/* 1065:     */     {
/* 1066:1431 */       switch (e.getKeyCode())
/* 1067:     */       {
/* 1068:     */       case 38: 
/* 1069:     */         break;
/* 1070:     */       case 40: 
/* 1071:     */         break;
/* 1072:     */       case 37: 
/* 1073:     */         break;
/* 1074:     */       }
/* 1075:     */     }
/* 1076:     */     
/* 1077:     */     private void reallocateShadowMapTexture(GL2 gl, int width, int height, boolean bindFBO)
/* 1078:     */       throws GLException
/* 1079:     */     {
/* 1080:1454 */       if (this.shadowMapTexture != null)
/* 1081:     */       {
/* 1082:1455 */         if ((width == this.shadowMapWidth) && (height == this.shadowMapHeight)) {
/* 1083:1456 */           return;
/* 1084:     */         }
/* 1085:1458 */         this.shadowMapTexture.destroy(gl);
/* 1086:1459 */         this.shadowMapTexture = null;
/* 1087:     */       }
/* 1088:1462 */       Animation.logger.log(Level.INFO, "Reallocating shadow map texture...");
/* 1089:     */       
/* 1090:     */ 
/* 1091:1465 */       gl.glActiveTexture(33985);
/* 1092:     */       for (;;)
/* 1093:     */       {
/* 1094:1467 */         this.shadowMapTexture = TextureIO.newTexture(3553);
/* 1095:1468 */         this.shadowMapTexture.bind(gl);
/* 1096:     */         
/* 1097:     */ 
/* 1098:1471 */         gl.glTexImage2D(3553, 0, 6402, width, height, 0, 6402, 5121, null);
/* 1099:1472 */         int actualWidth = FlyThruAnimation.getTexLevelParameteri(gl, 3553, 0, 4096);
/* 1100:1473 */         int actualHeight = FlyThruAnimation.getTexLevelParameteri(gl, 3553, 0, 4097);
/* 1101:1474 */         Animation.logger.log(Level.INFO, "... @ {0} x {1}, got {2} x {3}", new Object[] { Integer.valueOf(width), Integer.valueOf(height), Integer.valueOf(actualWidth), Integer.valueOf(actualHeight) });
/* 1102:1476 */         if (actualWidth > 0)
/* 1103:     */         {
/* 1104:1477 */           this.shadowMapTexture.setTexParameteri(gl, 10241, 9728);
/* 1105:1478 */           this.shadowMapTexture.setTexParameteri(gl, 10240, 9728);
/* 1106:     */           
/* 1107:1480 */           this.shadowMapTexture.setTexParameteri(gl, 10242, 33069);
/* 1108:1481 */           this.shadowMapTexture.setTexParameteri(gl, 10243, 33069);
/* 1109:     */           
/* 1110:     */ 
/* 1111:1484 */           this.shadowMapTexture.setTexParameterfv(gl, 4100, new float[] { 1.0F, 1.0F, 1.0F, 1.0F }, 0);
/* 1112:1486 */           if (bindFBO)
/* 1113:     */           {
/* 1114:1487 */             if (isShadowFrameBuffer())
/* 1115:     */             {
/* 1116:1488 */               FlyThruAnimation.deleteFramebuffer(gl, this.shadowFrameBuffer);
/* 1117:1489 */               this.shadowFrameBuffer = -1;
/* 1118:     */             }
/* 1119:1491 */             this.shadowFrameBuffer = FlyThruAnimation.genFramebuffer(gl);
/* 1120:1492 */             gl.glBindFramebuffer(36160, this.shadowFrameBuffer);
/* 1121:1493 */             gl.glFramebufferTexture2D(36160, 36096, 3553, this.shadowMapTexture.getTextureObject(gl), 0);
/* 1122:     */             
/* 1123:1495 */             gl.glDrawBuffer(0);
/* 1124:1496 */             gl.glReadBuffer(0);
/* 1125:1497 */             int status = gl.glCheckFramebufferStatus(36160);
/* 1126:1498 */             if (status == 36053)
/* 1127:     */             {
/* 1128:1499 */               gl.glBindFramebuffer(36160, 0);
/* 1129:1500 */               Animation.logger.log(Level.INFO, "Success binding frame buffer");
/* 1130:1501 */               break;
/* 1131:     */             }
/* 1132:1503 */             Animation.logger.log(Level.WARNING, "Frame buffer status incomplete @ {0}", Integer.toHexString(status));
/* 1133:     */             
/* 1134:1505 */             FlyThruAnimation.deleteFramebuffer(gl, this.shadowFrameBuffer);
/* 1135:1506 */             this.shadowFrameBuffer = -1;
/* 1136:     */           }
/* 1137:     */         }
/* 1138:     */         else
/* 1139:     */         {
/* 1140:1509 */           this.shadowMapTexture.destroy(gl);
/* 1141:     */         }
/* 1142:1511 */         width /= 2;
/* 1143:1512 */         height /= 2;
/* 1144:1513 */         if ((width <= 1024) || (height <= 512))
/* 1145:     */         {
/* 1146:1514 */           width = height = 0;
/* 1147:1515 */           FlyThruAnimation.this.config.canShowShadows = false;
/* 1148:1516 */           this.shadowMapTexture = null;
/* 1149:1517 */           this.shadowFrameBuffer = -1;
/* 1150:1518 */           break;
/* 1151:     */         }
/* 1152:     */       }
/* 1153:1522 */       this.shadowMapWidth = width;
/* 1154:1523 */       this.shadowMapHeight = height;
/* 1155:     */     }
/* 1156:     */     
/* 1157:     */     public void dispose(GLAutoDrawable drawable)
/* 1158:     */     {
/* 1159:1533 */       GL2 gl = drawable.getGL().getGL2();
/* 1160:1534 */       if (this.shadowMapTexture != null)
/* 1161:     */       {
/* 1162:1535 */         this.shadowMapTexture.destroy(gl);
/* 1163:1536 */         this.shadowMapTexture = null;
/* 1164:     */       }
/* 1165:1538 */       if (this.shadowFrameBuffer != -1)
/* 1166:     */       {
/* 1167:1539 */         FlyThruAnimation.deleteFramebuffer(gl, this.shadowFrameBuffer);
/* 1168:1540 */         this.shadowFrameBuffer = -1;
/* 1169:     */       }
/* 1170:1542 */       if (this.animator != null) {
/* 1171:1543 */         this.animator.stop();
/* 1172:     */       }
/* 1173:     */     }
/* 1174:     */     
/* 1175:     */     private boolean isShadowFrameBuffer()
/* 1176:     */     {
/* 1177:1554 */       return this.shadowFrameBuffer >= 0;
/* 1178:     */     }
/* 1179:     */     
/* 1180:     */     private void tryShadowFrameBufferAllocation(GL2 gl)
/* 1181:     */       throws GLException
/* 1182:     */     {
/* 1183:1566 */       if ((gl.isExtensionAvailable("GL_EXT_framebuffer_object")) && (gl.isFunctionAvailable("glFramebufferTexture2DEXT")))
/* 1184:     */       {
/* 1185:1567 */         Animation.logger.log(Level.INFO, "Frame buffer is available");
/* 1186:1568 */         reallocateShadowMapTexture(gl, this.maxShadowTextureSize, this.maxShadowTextureSize, true);
/* 1187:1569 */         Animation.logger.log(Level.INFO, "Frame buffer allocation complete");
/* 1188:     */       }
/* 1189:     */     }
/* 1190:     */     
/* 1191:     */     public void updateView(double elapsed)
/* 1192:     */     {
/* 1193:1580 */       this.view.phiEye = Utility.clamp(this.view.phiEye + this.phiEyeRate * elapsed, -1.178097245096172D, 1.178097245096172D);
/* 1194:1581 */       double dy = Math.sin(this.view.phiEye);
/* 1195:1582 */       double c = Math.cos(this.view.phiEye);
/* 1196:1583 */       this.view.thetaEye = Math.IEEEremainder(this.view.thetaEye + this.thetaEyeRate * elapsed, 6.283185307179586D);
/* 1197:1584 */       double dx = c * Math.sin(this.view.thetaEye);
/* 1198:1585 */       double dz = -c * Math.cos(this.view.thetaEye);
/* 1199:1586 */       if (this.ignoreBoundaries)
/* 1200:     */       {
/* 1201:1587 */         if (moveLateral())
/* 1202:     */         {
/* 1203:1588 */           this.view.xEye -= dz * this.xzEyeVelocity * elapsed;
/* 1204:1589 */           this.view.zEye += dx * this.xzEyeVelocity * elapsed;
/* 1205:     */         }
/* 1206:     */         else
/* 1207:     */         {
/* 1208:1591 */           this.view.xEye += dx * this.xzEyeVelocity * elapsed;
/* 1209:1592 */           this.view.zEye += dz * this.xzEyeVelocity * elapsed;
/* 1210:     */         }
/* 1211:1594 */         this.view.yEye += this.yEyeVelocity * elapsed;
/* 1212:     */       }
/* 1213:     */       else
/* 1214:     */       {
/* 1215:1597 */         if (moveLateral())
/* 1216:     */         {
/* 1217:1598 */           this.view.xEye = Utility.clamp(this.view.xEye - dz * this.xzEyeVelocity * elapsed, this.xEyeMin, this.xEyeMax);
/* 1218:1599 */           this.view.zEye = Utility.clamp(this.view.zEye + dx * this.xzEyeVelocity * elapsed, this.zEyeMin, this.zEyeMax);
/* 1219:     */         }
/* 1220:     */         else
/* 1221:     */         {
/* 1222:1601 */           this.view.xEye = Utility.clamp(this.view.xEye + dx * this.xzEyeVelocity * elapsed, this.xEyeMin, this.xEyeMax);
/* 1223:1602 */           this.view.zEye = Utility.clamp(this.view.zEye + dz * this.xzEyeVelocity * elapsed, this.zEyeMin, this.zEyeMax);
/* 1224:     */         }
/* 1225:1604 */         this.yEyeMin = (FlyThruAnimation.this.terrain.getElevationAt((float)this.view.xEye, (float)this.view.zEye) + 2.0F);
/* 1226:1605 */         this.view.yEye = Utility.clamp(this.view.yEye + this.yEyeVelocity * elapsed, this.yEyeMin, this.yEyeMax);
/* 1227:     */       }
/* 1228:1607 */       this.xLookAt = (this.view.xEye + dx);
/* 1229:1608 */       this.zLookAt = (this.view.zEye + dz);
/* 1230:1609 */       this.yLookAt = (this.view.yEye + dy);
/* 1231:     */     }
/* 1232:     */   }
/* 1233:     */   
/* 1234:     */   public void updateView(double elapsed)
/* 1235:     */   {
/* 1236:1619 */     this.canvas.updateView(elapsed);
/* 1237:     */   }
/* 1238:     */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.FlyThruAnimation
 * JD-Core Version:    0.7.0.1
 */