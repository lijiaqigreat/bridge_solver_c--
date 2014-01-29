/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ 
/*   5:    */ public class AnimationViewLocalStorable
/*   6:    */   implements Serializable
/*   7:    */ {
/*   8:    */   public double xEye;
/*   9:    */   public double yEye;
/*  10:    */   public double zEye;
/*  11:    */   public double thetaEye;
/*  12:    */   public double phiEye;
/*  13:    */   private String scenarioTag;
/*  14:    */   
/*  15:    */   public double getPhiEye()
/*  16:    */   {
/*  17: 41 */     return this.phiEye;
/*  18:    */   }
/*  19:    */   
/*  20:    */   public void setPhiEye(double phiEye)
/*  21:    */   {
/*  22: 50 */     this.phiEye = phiEye;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public double getThetaEye()
/*  26:    */   {
/*  27: 59 */     return this.thetaEye;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public void setThetaEye(double thetaEye)
/*  31:    */   {
/*  32: 68 */     this.thetaEye = thetaEye;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public double getXEye()
/*  36:    */   {
/*  37: 77 */     return this.xEye;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public void setXEye(double xEye)
/*  41:    */   {
/*  42: 86 */     this.xEye = xEye;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public double getYEye()
/*  46:    */   {
/*  47: 95 */     return this.yEye;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public void setYEye(double yEye)
/*  51:    */   {
/*  52:104 */     this.yEye = yEye;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public double getZEye()
/*  56:    */   {
/*  57:113 */     return this.zEye;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public void setZEye(double zEye)
/*  61:    */   {
/*  62:122 */     this.zEye = zEye;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public String getScenarioTag()
/*  66:    */   {
/*  67:131 */     return this.scenarioTag;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public void setScenarioTag(String scenarioTag)
/*  71:    */   {
/*  72:140 */     this.scenarioTag = scenarioTag;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public void save(String fileName)
/*  76:    */   {
/*  77:149 */     WPBDApp.saveToLocalStorage(this, fileName);
/*  78:    */   }
/*  79:    */   
/*  80:    */   public static AnimationViewLocalStorable load(String fileName)
/*  81:    */   {
/*  82:159 */     return (AnimationViewLocalStorable)WPBDApp.loadFromLocalStorage(fileName);
/*  83:    */   }
/*  84:    */   
/*  85:    */   public String toString()
/*  86:    */   {
/*  87:168 */     return this.scenarioTag;
/*  88:    */   }
/*  89:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.AnimationViewLocalStorable
 * JD-Core Version:    0.7.0.1
 */