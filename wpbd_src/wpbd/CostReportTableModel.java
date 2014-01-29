/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.text.NumberFormat;
/*   4:    */ import java.util.Iterator;
/*   5:    */ import java.util.Locale;
/*   6:    */ import java.util.Set;
/*   7:    */ import java.util.TreeMap;
/*   8:    */ import javax.swing.table.DefaultTableModel;
/*   9:    */ import org.jdesktop.application.ResourceMap;
/*  10:    */ 
/*  11:    */ public class CostReportTableModel
/*  12:    */   extends DefaultTableModel
/*  13:    */ {
/*  14:    */   private static String[] columnIds;
/*  15: 31 */   private static final ResourceMap resourceMap = WPBDApp.getResourceMap(CostReportTableModel.class);
/*  16: 32 */   private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
/*  17: 33 */   private final NumberFormat intFormat = NumberFormat.getIntegerInstance();
/*  18:    */   
/*  19:    */   public CostReportTableModel()
/*  20:    */   {
/*  21: 39 */     super(0, 4);
/*  22:    */   }
/*  23:    */   
/*  24:    */   public void initialize(BridgeModel.Costs costs)
/*  25:    */   {
/*  26: 48 */     if (columnIds == null) {
/*  27: 49 */       columnIds = resourceMap.getString("columnIds.text", new Object[0]).split("\\|");
/*  28:    */     }
/*  29: 51 */     setColumnIdentifiers(columnIds);
/*  30: 52 */     int nMaterialRows = costs.materialSectionPairs.size();
/*  31: 53 */     int nProductRows = costs.materialShapePairs.size();
/*  32: 54 */     setRowCount(nMaterialRows + 1 + nProductRows + 5 + 1 + 4);
/*  33:    */     
/*  34: 56 */     int row = 0;
/*  35: 57 */     Iterator<BridgeModel.MaterialSectionPair> mtlSecIt = costs.materialSectionPairs.keySet().iterator();
/*  36: 58 */     boolean initial = true;
/*  37: 59 */     double totalMtlCost = 0.0D;
/*  38: 60 */     while (mtlSecIt.hasNext())
/*  39:    */     {
/*  40: 61 */       BridgeModel.MaterialSectionPair pair = (BridgeModel.MaterialSectionPair)mtlSecIt.next();
/*  41: 63 */       if (initial)
/*  42:    */       {
/*  43: 64 */         loadAt("materialCost.text", row, 0);
/*  44: 65 */         initial = false;
/*  45:    */       }
/*  46:    */       else
/*  47:    */       {
/*  48: 68 */         setValueAt(null, row, 0);
/*  49:    */       }
/*  50: 71 */       setValueAt(pair, row, 1);
/*  51:    */       
/*  52: 73 */       double weight = ((Double)costs.materialSectionPairs.get(pair)).doubleValue();
/*  53: 74 */       double cost = pair.material.getCost(pair.section);
/*  54: 75 */       setValueAt(resourceMap.getString("materialCostNote.text", new Object[] { Double.valueOf(weight), this.currencyFormat.format(cost) }), row, 2);
/*  55:    */       
/*  56: 77 */       double mtlCost = 2.0D * weight * cost;
/*  57: 78 */       setValueAt(Double.valueOf(mtlCost), row, 3);
/*  58: 79 */       totalMtlCost += mtlCost;
/*  59: 80 */       row++;
/*  60:    */     }
/*  61: 83 */     for (int col = 0; col < 4; col++) {
/*  62: 84 */       setValueAt(null, row, col);
/*  63:    */     }
/*  64: 86 */     row++;
/*  65:    */     
/*  66: 88 */     loadAt("connectionCost.text", row, 0);
/*  67: 89 */     setValueAt(null, row, 1);
/*  68: 90 */     int nConnections = costs.nConnections;
/*  69: 91 */     double connectionFee = costs.inventory.getConnectionFee();
/*  70: 92 */     setValueAt(resourceMap.getString("connectionCostNote.text", new Object[] { Integer.valueOf(nConnections), Double.valueOf(connectionFee) }), row, 2);
/*  71: 93 */     double connectionCost = 2 * nConnections * connectionFee;
/*  72: 94 */     setValueAt(Double.valueOf(connectionCost), row, 3);
/*  73: 95 */     row++;
/*  74: 97 */     for (int col = 0; col < 4; col++) {
/*  75: 98 */       setValueAt(null, row, col);
/*  76:    */     }
/*  77:100 */     row++;
/*  78:    */     
/*  79:102 */     Iterator<BridgeModel.MaterialShapePair> mtlShpIt = costs.materialShapePairs.keySet().iterator();
/*  80:103 */     initial = true;
/*  81:104 */     while (mtlShpIt.hasNext())
/*  82:    */     {
/*  83:105 */       BridgeModel.MaterialShapePair pair = (BridgeModel.MaterialShapePair)mtlShpIt.next();
/*  84:107 */       if (initial)
/*  85:    */       {
/*  86:108 */         loadAt("productCost.text", row, 0);
/*  87:109 */         initial = false;
/*  88:    */       }
/*  89:    */       else
/*  90:    */       {
/*  91:112 */         setValueAt(null, row, 0);
/*  92:    */       }
/*  93:115 */       String nUsed = ((Integer)costs.materialShapePairs.get(pair)).toString();
/*  94:116 */       nUsed = "   ".substring(nUsed.length()) + nUsed;
/*  95:117 */       setValueAt(nUsed + " - " + pair, row, 1);
/*  96:    */       
/*  97:119 */       setValueAt(resourceMap.getString("productCostNote.text", new Object[] { this.currencyFormat.format(costs.inventory.getOrderingFee()) }), row, 2);
/*  98:    */       
/*  99:    */ 
/* 100:122 */       setValueAt(Double.valueOf(costs.inventory.getOrderingFee()), row, 3);
/* 101:123 */       row++;
/* 102:    */     }
/* 103:126 */     for (int col = 0; col < 4; col++) {
/* 104:127 */       setValueAt(null, row, col);
/* 105:    */     }
/* 106:129 */     row++;
/* 107:130 */     double totalProductCost = nProductRows * costs.inventory.getOrderingFee();
/* 108:    */     
/* 109:132 */     loadAt("siteCost.text", row, 0);
/* 110:133 */     loadAt("deckCost.text", row, 1);
/* 111:134 */     setValueAt(resourceMap.getString("deckCostNote.text", new Object[] { Integer.valueOf(costs.conditions.getNPanels()), this.currencyFormat.format(costs.conditions.getDeckCostRate()) }), row, 2);
/* 112:    */     
/* 113:136 */     double deckCost = costs.conditions.getNPanels() * costs.conditions.getDeckCostRate();
/* 114:137 */     setValueAt(Double.valueOf(deckCost), row, 3);
/* 115:138 */     row++;
/* 116:139 */     setValueAt(null, row, 0);
/* 117:140 */     loadAt("excavationCost.text", row, 1);
/* 118:141 */     setValueAt(resourceMap.getString("excavationCostNote.text", new Object[] { this.intFormat.format(costs.conditions.getExcavationVolume()), this.currencyFormat.format(1.0D) }), row, 2);
/* 119:    */     
/* 120:    */ 
/* 121:144 */     setValueAt(Double.valueOf(costs.conditions.getExcavationCost()), row, 3);
/* 122:145 */     row++;
/* 123:146 */     setValueAt(null, row, 0);
/* 124:147 */     loadAt("abutmentCost.text", row, 1);
/* 125:148 */     String abutmentType = resourceMap.getString(costs.conditions.isArch() ? "arch.text" : "standard.text", new Object[0]);
/* 126:149 */     setValueAt(resourceMap.getString("abutmentCostNote.text", new Object[] { abutmentType, this.currencyFormat.format(costs.conditions.getAbutmentCost()) }), row, 2);
/* 127:    */     
/* 128:151 */     setValueAt(Double.valueOf(2.0D * costs.conditions.getAbutmentCost()), row, 3);
/* 129:152 */     row++;
/* 130:153 */     setValueAt(null, row, 0);
/* 131:154 */     loadAt("pierCost.text", row, 1);
/* 132:155 */     if (costs.conditions.isPier()) {
/* 133:156 */       setValueAt(resourceMap.getString("pierNote.text", new Object[] { this.intFormat.format(costs.conditions.getPierHeight()) }), row, 2);
/* 134:    */     } else {
/* 135:159 */       setValueAt(resourceMap.getString("noPierNote.text", new Object[0]), row, 2);
/* 136:    */     }
/* 137:161 */     setValueAt(Double.valueOf(costs.conditions.getPierCost()), row, 3);
/* 138:162 */     row++;
/* 139:163 */     setValueAt(null, row, 0);
/* 140:164 */     loadAt("anchorageCost.text", row, 1);
/* 141:165 */     int nAnchorages = costs.conditions.getNAnchorages();
/* 142:166 */     if (nAnchorages == 0) {
/* 143:167 */       setValueAt(resourceMap.getString("noAnchoragesNote.text", new Object[0]), row, 2);
/* 144:    */     } else {
/* 145:169 */       setValueAt(resourceMap.getString("anchorageNote.text", new Object[] { Integer.valueOf(nAnchorages), this.currencyFormat.format(6000.0D) }), row, 2);
/* 146:    */     }
/* 147:172 */     double anchorageCost = nAnchorages * 6000.0D;
/* 148:173 */     setValueAt(Double.valueOf(anchorageCost), row, 3);
/* 149:174 */     row++;
/* 150:176 */     for (int col = 0; col < 4; col++) {
/* 151:177 */       setValueAt(null, row, col);
/* 152:    */     }
/* 153:179 */     row++;
/* 154:180 */     loadAt("totalCost.text", row, 0);
/* 155:181 */     loadAt("sum.text", row, 1);
/* 156:182 */     setValueAt(resourceMap.getString("sumNote.text", new Object[] { this.currencyFormat.format(totalMtlCost), this.currencyFormat.format(connectionCost), this.currencyFormat.format(totalProductCost), this.currencyFormat.format(costs.conditions.getTotalFixedCost()) }), row, 2);
/* 157:    */     
/* 158:    */ 
/* 159:    */ 
/* 160:    */ 
/* 161:187 */     setValueAt(Double.valueOf(totalMtlCost + connectionCost + totalProductCost + costs.conditions.getTotalFixedCost()), row, 3);
/* 162:    */   }
/* 163:    */   
/* 164:    */   private void loadAt(String key, int row, int column)
/* 165:    */   {
/* 166:198 */     setValueAt(resourceMap.getString(key, new Object[0]), row, column);
/* 167:    */   }
/* 168:    */   
/* 169:    */   public boolean isCellEditable(int row, int column)
/* 170:    */   {
/* 171:209 */     return false;
/* 172:    */   }
/* 173:    */   
/* 174:    */   public Class<?> getColumnClass(int columnIndex)
/* 175:    */   {
/* 176:220 */     return columnIndex < 3 ? String.class : Double.class;
/* 177:    */   }
/* 178:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.CostReportTableModel
 * JD-Core Version:    0.7.0.1
 */