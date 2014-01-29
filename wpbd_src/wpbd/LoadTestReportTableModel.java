/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.text.DecimalFormat;
/*   4:    */ import java.text.NumberFormat;
/*   5:    */ import java.util.ArrayList;
/*   6:    */ import javax.swing.table.AbstractTableModel;
/*   7:    */ 
/*   8:    */ public class LoadTestReportTableModel
/*   9:    */   extends AbstractTableModel
/*  10:    */ {
/*  11:    */   private EditableBridgeModel bridge;
/*  12: 33 */   public static final String[] columnNames = { "number", "material", "crossSection", "size", "length", "slenderness", "SPACER", "compressionForce", "compressionStrength", "compressionStatus", "SPACER", "tensionForce", "tensionStrength", "tensionStatus" };
/*  13:    */   
/*  14:    */   public LoadTestReportTableModel(EditableBridgeModel bridge)
/*  15:    */   {
/*  16: 56 */     this.bridge = bridge;
/*  17:    */   }
/*  18:    */   
/*  19:    */   public int getRowCount()
/*  20:    */   {
/*  21: 65 */     return this.bridge.getMembers().size();
/*  22:    */   }
/*  23:    */   
/*  24:    */   public int getColumnCount()
/*  25:    */   {
/*  26: 74 */     return columnNames.length;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public String getColumnName(int column)
/*  30:    */   {
/*  31: 85 */     return columnNames[column];
/*  32:    */   }
/*  33:    */   
/*  34: 88 */   private final NumberFormat doubleFormatter = new DecimalFormat("0.00");
/*  35:    */   
/*  36:    */   public Object getValueAt(int rowIndex, int columnIndex)
/*  37:    */   {
/*  38: 98 */     Member member = (Member)this.bridge.getMembers().get(rowIndex);
/*  39:100 */     if ((columnIndex >= 6) && (columnIndex != 9) && (!this.bridge.isAnalysisValid())) {
/*  40:101 */       return "--";
/*  41:    */     }
/*  42:103 */     switch (columnIndex)
/*  43:    */     {
/*  44:    */     case 0: 
/*  45:106 */       return Integer.valueOf(member.getNumber());
/*  46:    */     case 1: 
/*  47:109 */       return member.getMaterial().getShortName();
/*  48:    */     case 2: 
/*  49:112 */       return member.getShape().getSection().getShortName();
/*  50:    */     case 3: 
/*  51:115 */       return Integer.valueOf(member.getShape().getNominalWidth());
/*  52:    */     case 4: 
/*  53:118 */       return this.doubleFormatter.format(member.getLength());
/*  54:    */     case 5: 
/*  55:120 */       return this.doubleFormatter.format(member.getSlenderness());
/*  56:    */     case 6: 
/*  57:122 */       return "";
/*  58:    */     case 7: 
/*  59:124 */       return this.doubleFormatter.format(this.bridge.getAnalysis().getMemberCompressiveForce(member.getIndex()));
/*  60:    */     case 8: 
/*  61:126 */       return this.doubleFormatter.format(this.bridge.getAnalysis().getMemberCompressiveStrength(member.getIndex()));
/*  62:    */     case 9: 
/*  63:128 */       return MemberTable.getMemberStatusString(member.getCompressionForceStrengthRatio() <= 1.0D);
/*  64:    */     case 10: 
/*  65:130 */       return "";
/*  66:    */     case 11: 
/*  67:132 */       return this.doubleFormatter.format(this.bridge.getAnalysis().getMemberTensileForce(member.getIndex()));
/*  68:    */     case 12: 
/*  69:134 */       return this.doubleFormatter.format(this.bridge.getAnalysis().getMemberTensileStrength(member.getIndex()));
/*  70:    */     case 13: 
/*  71:136 */       return MemberTable.getMemberStatusString(member.getTensionForceStrengthRatio() <= 1.0D);
/*  72:    */     }
/*  73:138 */     return null;
/*  74:    */   }
/*  75:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.LoadTestReportTableModel
 * JD-Core Version:    0.7.0.1
 */