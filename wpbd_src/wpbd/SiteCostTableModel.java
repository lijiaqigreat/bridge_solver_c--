/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.text.NumberFormat;
/*   4:    */ import java.util.Locale;
/*   5:    */ import javax.swing.table.DefaultTableModel;
/*   6:    */ import org.jdesktop.application.ResourceMap;
/*   7:    */ 
/*   8:    */ public class SiteCostTableModel
/*   9:    */   extends DefaultTableModel
/*  10:    */ {
/*  11: 29 */   private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
/*  12: 30 */   private static final ResourceMap resourceMap = WPBDApp.getResourceMap(SiteCostTableModel.class);
/*  13:    */   
/*  14:    */   public SiteCostTableModel()
/*  15:    */   {
/*  16: 36 */     super(6, 3);
/*  17: 37 */     loadAt("deckCost.text", 0, 0);
/*  18: 38 */     loadAt("excavationCost.text", 1, 0);
/*  19: 39 */     loadAt("abutmentCost.text", 2, 0);
/*  20: 40 */     loadAt("pierCost.text", 3, 0);
/*  21: 41 */     loadAt("anchorageCost.text", 4, 0);
/*  22: 42 */     loadAt("totalSiteCostNote.text", 5, 1);
/*  23:    */   }
/*  24:    */   
/*  25:    */   private void loadAt(String key, int row, int column)
/*  26:    */   {
/*  27: 46 */     String s = resourceMap.getString(key, new Object[0]);
/*  28: 47 */     setValueAt(s, row, column);
/*  29:    */   }
/*  30:    */   
/*  31:    */   public void initialize(DesignConditions conditions)
/*  32:    */   {
/*  33: 57 */     String abutmentType = resourceMap.getString(conditions.isArch() ? "arch.text" : "standard.text", new Object[0]);
/*  34: 58 */     NumberFormat intFormat = NumberFormat.getIntegerInstance();
/*  35:    */     
/*  36: 60 */     setValueAt(resourceMap.getString("deckCostNote.text", new Object[] { Integer.valueOf(conditions.getNPanels()), this.currencyFormat.format(conditions.getDeckCostRate()) }), 0, 1);
/*  37: 61 */     double deckCost = conditions.getNPanels() * conditions.getDeckCostRate();
/*  38: 62 */     setValueAt(Double.valueOf(deckCost), 0, 2);
/*  39:    */     
/*  40: 64 */     setValueAt(resourceMap.getString("excavationCostNote.text", new Object[] { intFormat.format(conditions.getExcavationVolume()), this.currencyFormat.format(1.0D) }), 1, 1);
/*  41:    */     
/*  42: 66 */     setValueAt(Double.valueOf(conditions.getExcavationCost()), 1, 2);
/*  43:    */     
/*  44: 68 */     setValueAt(resourceMap.getString("abutmentCostNote.text", new Object[] { abutmentType, this.currencyFormat.format(conditions.getAbutmentCost()) }), 2, 1);
/*  45: 69 */     setValueAt(Double.valueOf(2.0D * conditions.getAbutmentCost()), 2, 2);
/*  46: 71 */     if (conditions.isPier()) {
/*  47: 72 */       setValueAt(resourceMap.getString("pierNote.text", new Object[] { intFormat.format(conditions.getPierHeight()) }), 3, 1);
/*  48:    */     } else {
/*  49: 74 */       setValueAt(resourceMap.getString("noPierNote.text", new Object[0]), 3, 1);
/*  50:    */     }
/*  51: 76 */     setValueAt(Double.valueOf(conditions.getPierCost()), 3, 2);
/*  52:    */     
/*  53: 78 */     int nAnchorages = conditions.getNAnchorages();
/*  54: 79 */     if (nAnchorages == 0) {
/*  55: 80 */       setValueAt(resourceMap.getString("noAnchoragesNote.text", new Object[0]), 4, 1);
/*  56:    */     } else {
/*  57: 82 */       setValueAt(resourceMap.getString("anchorageNote.text", new Object[] { Integer.valueOf(nAnchorages), this.currencyFormat.format(6000.0D) }), 4, 1);
/*  58:    */     }
/*  59: 84 */     double anchorageCost = nAnchorages * 6000.0D;
/*  60: 85 */     setValueAt(Double.valueOf(anchorageCost), 4, 2);
/*  61:    */     
/*  62: 87 */     double totalCost = conditions.getTotalFixedCost();
/*  63: 88 */     setValueAt(Double.valueOf(totalCost), 5, 2);
/*  64:    */   }
/*  65:    */   
/*  66:    */   public boolean isCellEditable(int row, int column)
/*  67:    */   {
/*  68:100 */     return false;
/*  69:    */   }
/*  70:    */   
/*  71:    */   public Class<?> getColumnClass(int columnIndex)
/*  72:    */   {
/*  73:111 */     return columnIndex < 2 ? String.class : Double.class;
/*  74:    */   }
/*  75:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.SiteCostTableModel
 * JD-Core Version:    0.7.0.1
 */