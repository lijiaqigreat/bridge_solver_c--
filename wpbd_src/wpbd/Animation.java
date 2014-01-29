/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.awt.Canvas;
/*   4:    */ import java.awt.Component;
/*   5:    */ import java.awt.Dimension;
/*   6:    */ import java.util.logging.Level;
/*   7:    */ import java.util.logging.Logger;
/*   8:    */ import jogamp.common.Debug;
/*   9:    */ 
/*  10:    */ public abstract class Animation
/*  11:    */ {
/*  12: 31 */   protected static final Logger logger = Logger.getLogger(Animation.class.getName());
/*  13:    */   protected EditableBridgeModel bridge;
/*  14:    */   
/*  15:    */   static
/*  16:    */   {
/*  17: 32 */     logger.setLevel(Debug.isPropertyDefined("wpbd.develop", false) ? Level.ALL : Level.OFF);
/*  18:    */   }
/*  19:    */   
/*  20: 43 */   public static final Homogeneous.Point lightPosition = new Homogeneous.Point(15.0F, 180.0F, 190.0F, 0.0F);
/*  21:    */   public static final double deckHalfWidth = 5.0D;
/*  22:    */   public static final double standardExaggeration = 30.0D;
/*  23:    */   private static final double defaultTruckSpeedKmPerHr = 25.0D;
/*  24:    */   private static final double failureDuration = 1.2D;
/*  25:    */   private final Config config;
/*  26:    */   protected static final int UNINITIALIZED_STATE = 0;
/*  27:    */   protected static final int UNLOADED_STATE = 1;
/*  28:    */   protected static final int DEAD_LOADING_STATE = 2;
/*  29:    */   protected static final int LOAD_MOVING_STATE = 3;
/*  30:    */   protected static final int FAILING_STATE = 4;
/*  31:    */   protected static final int FAILED_STATE = 5;
/*  32:    */   protected static final int LOAD_MOVING_TO_DEAD_LOAD_FAILURE = 6;
/*  33:    */   protected static final double kmPerHrToPanelsPerSec = 0.06944444444444445D;
/*  34:    */   
/*  35:    */   public static class Config
/*  36:    */   {
/*  37: 65 */     public double displacementExaggeration = 30.0D;
/*  38: 66 */     public double truckSpeed = 25.0D;
/*  39: 67 */     public boolean paused = false;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public Animation(EditableBridgeModel bridge, TerrainModel terrain, Config config)
/*  43:    */   {
/*  44: 73 */     this.bridge = bridge;
/*  45: 74 */     this.config = config;
/*  46: 75 */     this.failureAnalysis = new Analysis();
/*  47: 76 */     this.animationInterpolation = bridge.getAnalysis().getNewInterpolation(terrain);
/*  48: 77 */     this.failureInterpolation = this.failureAnalysis.getNewInterpolation(terrain);
/*  49: 78 */     this.failureAnimationInterpolation = bridge.getAnalysis().getNewInterpolation(terrain);
/*  50:    */   }
/*  51:    */   
/*  52:    */   public void reset()
/*  53:    */   {
/*  54: 92 */     resetState();
/*  55:    */   }
/*  56:    */   
/*  57:133 */   protected double loadLocationRunup = 3.0D;
/*  58:    */   protected static final double initialPauseDuration = 0.5D;
/*  59:    */   protected static final double deadLoadingDuration = 1.2D;
/*  60:    */   protected static final double emergencyStopLocation = -0.5D;
/*  61:149 */   protected int state = 0;
/*  62:153 */   protected double deadLoadApplied = 0.0D;
/*  63:157 */   protected double loadLocation = -this.loadLocationRunup;
/*  64:161 */   protected double loadEndDistanceTraveled = this.loadLocationRunup;
/*  65:165 */   protected long lastDisplayTime = -1L;
/*  66:169 */   protected long lastStateChangeTime = -1L;
/*  67:174 */   private double distanceMoved = 0.0D;
/*  68:    */   private final Analysis.Interpolation animationInterpolation;
/*  69:    */   private final Analysis failureAnalysis;
/*  70:    */   private final Analysis.Interpolation failureInterpolation;
/*  71:    */   private final Analysis.Interpolation failureAnimationInterpolation;
/*  72:198 */   private Affine.Point lastPtLoad = new Affine.Point();
/*  73:    */   
/*  74:    */   private void updateDistanceMoved()
/*  75:    */   {
/*  76:206 */     Affine.Point ptLoad = this.animationInterpolation.getPtLoad();
/*  77:207 */     this.distanceMoved += ptLoad.distance(this.lastPtLoad);
/*  78:208 */     this.lastPtLoad.setLocation(ptLoad);
/*  79:    */   }
/*  80:    */   
/*  81:    */   public double getDistanceMoved()
/*  82:    */   {
/*  83:217 */     double rtnVal = this.distanceMoved;
/*  84:218 */     this.distanceMoved = 0.0D;
/*  85:219 */     return rtnVal;
/*  86:    */   }
/*  87:    */   
/*  88:    */   private void updateLoadLocation(double elapsed)
/*  89:    */   {
/*  90:230 */     if (!this.config.paused)
/*  91:    */     {
/*  92:231 */       double speed = this.config.truckSpeed * 0.06944444444444445D;
/*  93:232 */       if ((this.loadLocation <= 0.0D) || (this.loadLocation >= this.bridge.getDesignConditions().getNLoadedJoints())) {
/*  94:233 */         speed *= 3.0D;
/*  95:    */       }
/*  96:235 */       this.loadLocation += elapsed * speed;
/*  97:236 */       if (this.loadLocation >= this.loadEndDistanceTraveled) {
/*  98:237 */         this.loadLocation = (-this.loadLocationRunup);
/*  99:    */       }
/* 100:    */     }
/* 101:    */   }
/* 102:    */   
/* 103:    */   protected Analysis.Interpolation interpolate(long time)
/* 104:    */   {
/* 105:256 */     if (this.state == 0)
/* 106:    */     {
/* 107:257 */       this.lastDisplayTime = (this.lastStateChangeTime = time);
/* 108:258 */       this.state = 1;
/* 109:259 */       this.loadLocation = (-this.loadLocationRunup);
/* 110:    */       
/* 111:261 */       updateView(0.0D);
/* 112:262 */       this.animationInterpolation.initializeDeadLoadOnly(0.0D, this.loadLocation, 0.0D);
/* 113:263 */       getDistanceMoved();
/* 114:264 */       return this.animationInterpolation;
/* 115:    */     }
/* 116:268 */     double elapsed = (time - this.lastDisplayTime) * 1.E-009D;
/* 117:269 */     this.lastDisplayTime = time;
/* 118:270 */     updateView(elapsed);
/* 119:    */     
/* 120:    */ 
/* 121:273 */     double stateElapsed = (time - this.lastStateChangeTime) * 1.E-009D;
/* 122:274 */     switch (this.state)
/* 123:    */     {
/* 124:    */     case 1: 
/* 125:276 */       if (stateElapsed >= 0.5D)
/* 126:    */       {
/* 127:277 */         this.state = 2;
/* 128:278 */         this.lastStateChangeTime = time;
/* 129:    */       }
/* 130:280 */       this.loadLocation = (-this.loadLocationRunup);
/* 131:281 */       this.animationInterpolation.initializeDeadLoadOnly(0.0D, this.loadLocation, 0.0D);
/* 132:282 */       updateDistanceMoved();
/* 133:283 */       return this.animationInterpolation;
/* 134:    */     case 2: 
/* 135:285 */       this.deadLoadApplied = (stateElapsed / 1.2D);
/* 136:286 */       if (this.deadLoadApplied > 1.0D)
/* 137:    */       {
/* 138:287 */         this.deadLoadApplied = 1.0D;
/* 139:288 */         this.state = 3;
/* 140:289 */         this.lastStateChangeTime = time;
/* 141:290 */         this.loadEndDistanceTraveled = (this.bridge.getDesignConditions().getNLoadedJoints() + this.loadLocationRunup);
/* 142:    */       }
/* 143:292 */       this.loadLocation = (-this.loadLocationRunup);
/* 144:293 */       this.animationInterpolation.initializeDeadLoadOnly(this.deadLoadApplied, this.loadLocation, this.config.displacementExaggeration);
/* 145:294 */       updateDistanceMoved();
/* 146:295 */       checkForFailure(this.animationInterpolation, time);
/* 147:296 */       return this.animationInterpolation;
/* 148:    */     case 3: 
/* 149:298 */       updateLoadLocation(elapsed);
/* 150:299 */       this.animationInterpolation.initialize(this.loadLocation, this.config.displacementExaggeration);
/* 151:300 */       updateDistanceMoved();
/* 152:301 */       checkForFailure(this.animationInterpolation, time);
/* 153:302 */       return this.animationInterpolation;
/* 154:    */     case 6: 
/* 155:304 */       updateLoadLocation(elapsed);
/* 156:305 */       if (this.loadLocation >= -0.5D) {
/* 157:306 */         this.state = 5;
/* 158:    */       }
/* 159:310 */       this.animationInterpolation.initialize(this.loadLocation, this.config.displacementExaggeration);
/* 160:311 */       this.failureAnimationInterpolation.initialize(this.animationInterpolation, this.failureInterpolation, 1.0D);
/* 161:312 */       updateDistanceMoved();
/* 162:313 */       return this.failureAnimationInterpolation;
/* 163:    */     case 4: 
/* 164:315 */       double t = stateElapsed / 1.2D;
/* 165:316 */       if (t > 1.0D)
/* 166:    */       {
/* 167:317 */         t = 1.0D;
/* 168:318 */         this.state = 5;
/* 169:    */       }
/* 170:320 */       this.failureAnimationInterpolation.initialize(this.animationInterpolation, this.failureInterpolation, t);
/* 171:321 */       return this.failureAnimationInterpolation;
/* 172:    */     case 5: 
/* 173:323 */       if (this.loadLocation < -0.5D) {
/* 174:324 */         this.state = 6;
/* 175:    */       }
/* 176:326 */       return this.failureAnimationInterpolation;
/* 177:    */     }
/* 178:328 */     return null;
/* 179:    */   }
/* 180:    */   
/* 181:    */   private void checkForFailure(Analysis.Interpolation interpolation, long time)
/* 182:    */   {
/* 183:340 */     if ((this.state < 4) && (interpolation.isFailure()))
/* 184:    */     {
/* 185:341 */       this.failureAnalysis.initialize(this.bridge, interpolation.getFailureStatus());
/* 186:342 */       this.failureInterpolation.initialize(this.loadLocation, this.config.displacementExaggeration);
/* 187:343 */       this.state = 4;
/* 188:344 */       this.lastStateChangeTime = time;
/* 189:    */     }
/* 190:    */   }
/* 191:    */   
/* 192:    */   protected void resetState()
/* 193:    */   {
/* 194:353 */     this.state = 0;
/* 195:    */   }
/* 196:    */   
/* 197:    */   protected void applyCanvasResizeBugWorkaround()
/* 198:    */   {
/* 199:361 */     Component c = getCanvas();
/* 200:362 */     while (c != null)
/* 201:    */     {
/* 202:363 */       c.setMinimumSize(new Dimension(0, 0));
/* 203:364 */       c = c.getParent();
/* 204:    */     }
/* 205:    */   }
/* 206:    */   
/* 207:    */   public abstract Canvas getCanvas();
/* 208:    */   
/* 209:    */   public abstract AnimationControls getControls();
/* 210:    */   
/* 211:    */   public abstract void updateView(double paramDouble);
/* 212:    */   
/* 213:    */   public abstract void start();
/* 214:    */   
/* 215:    */   public abstract void stop();
/* 216:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.Animation
 * JD-Core Version:    0.7.0.1
 */