/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.text.NumberFormat;
/*   4:    */ import java.util.Locale;
/*   5:    */ import javax.swing.tree.DefaultMutableTreeNode;
/*   6:    */ 
/*   7:    */ public class DesignIteration
/*   8:    */   extends DefaultMutableTreeNode
/*   9:    */ {
/*  10:    */   private int number;
/*  11:    */   private double cost;
/*  12:    */   private String projectId;
/*  13:    */   private byte[] bridgeModelAsBytes;
/*  14:    */   private int analysisStatus;
/*  15: 37 */   private final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
/*  16:    */   
/*  17:    */   public DesignIteration(int number, double cost, String projectId, byte[] bridgeModelAsBytes, int analysisStatus)
/*  18:    */   {
/*  19: 50 */     super.setUserObject(this);
/*  20: 51 */     initialize(number, cost, projectId, bridgeModelAsBytes, analysisStatus);
/*  21:    */   }
/*  22:    */   
/*  23:    */   public final void initialize(int number, double cost, String projectId, byte[] bridgeModelAsBytes, int analysisStatus)
/*  24:    */   {
/*  25: 64 */     this.number = number;
/*  26: 65 */     this.cost = cost;
/*  27: 66 */     this.projectId = projectId;
/*  28: 67 */     this.bridgeModelAsBytes = bridgeModelAsBytes;
/*  29: 68 */     this.analysisStatus = analysisStatus;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public void setAnalysisStatus(int analysisStatus)
/*  33:    */   {
/*  34: 79 */     this.analysisStatus = analysisStatus;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public DesignIteration()
/*  38:    */   {
/*  39: 87 */     super.setUserObject(null);
/*  40: 88 */     this.number = -1;
/*  41: 89 */     this.cost = 0.0D;
/*  42: 90 */     this.projectId = "<root>";
/*  43: 91 */     this.bridgeModelAsBytes = null;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public byte[] getBridgeModelAsBytes()
/*  47:    */   {
/*  48:100 */     return this.bridgeModelAsBytes;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public double getCost()
/*  52:    */   {
/*  53:109 */     return this.cost;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public int getNumber()
/*  57:    */   {
/*  58:118 */     return this.number;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public String getProjectId()
/*  62:    */   {
/*  63:127 */     return this.projectId;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public int getBridgeStatus()
/*  67:    */   {
/*  68:137 */     return this.analysisStatus;
/*  69:    */   }
/*  70:    */   
/*  71:    */   public String toString()
/*  72:    */   {
/*  73:148 */     String rtn = this.number + " - " + this.currencyFormatter.format(this.cost);
/*  74:149 */     if (this.projectId.trim().length() > 0) {
/*  75:150 */       rtn = rtn + " - " + this.projectId;
/*  76:    */     }
/*  77:152 */     return rtn;
/*  78:    */   }
/*  79:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.DesignIteration
 * JD-Core Version:    0.7.0.1
 */