/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.awt.event.ItemEvent;
/*   4:    */ import javax.swing.JCheckBox;
/*   5:    */ import javax.swing.JComboBox;
/*   6:    */ import javax.swing.JLabel;
/*   7:    */ import javax.swing.JPanel;
/*   8:    */ import javax.swing.JTabbedPane;
/*   9:    */ import javax.swing.JTable;
/*  10:    */ import javax.swing.event.ChangeEvent;
/*  11:    */ import javax.swing.event.ChangeListener;
/*  12:    */ import org.jdesktop.application.ResourceMap;
/*  13:    */ 
/*  14:    */ public class MemberDetail
/*  15:    */   implements ChangeListener
/*  16:    */ {
/*  17:    */   private final EditableBridgeModel bridge;
/*  18:    */   private final StockSelector stockSelector;
/*  19:    */   private final JTabbedPane tabs;
/*  20:    */   private final JPanel memberDetailPanel;
/*  21:    */   private final JTable materialTable;
/*  22:    */   private final JTable dimensionsTable;
/*  23:    */   private final JTable costTable;
/*  24:    */   private final CrossSectionSketch crossSectionSketch;
/*  25:    */   private final JComboBox memberSelectBox;
/*  26:    */   private final JCheckBox graphAllCheck;
/*  27:    */   private final StrengthCurve strengthCurve;
/*  28:    */   private final ResourceMap resourceMap;
/*  29:    */   private Member[][] memberLists;
/*  30:    */   
/*  31:    */   public MemberDetail(EditableBridgeModel bridge, StockSelector stockSelector, JTabbedPane tabs, JPanel memberDetailPanel, JTable materialTable, JTable dimensionsTable, JTable costTable, JLabel crossSectionSketch, JComboBox memberSelectBox, JCheckBox graphAllCheck, JLabel strengthCurve)
/*  32:    */   {
/*  33: 75 */     this.bridge = bridge;
/*  34: 76 */     this.stockSelector = stockSelector;
/*  35: 77 */     this.tabs = tabs;
/*  36: 78 */     this.memberDetailPanel = memberDetailPanel;
/*  37: 79 */     this.materialTable = materialTable;
/*  38: 80 */     this.dimensionsTable = dimensionsTable;
/*  39: 81 */     this.costTable = costTable;
/*  40: 82 */     this.crossSectionSketch = ((CrossSectionSketch)crossSectionSketch);
/*  41: 83 */     this.memberSelectBox = memberSelectBox;
/*  42: 84 */     this.graphAllCheck = graphAllCheck;
/*  43: 85 */     this.strengthCurve = ((StrengthCurve)strengthCurve);
/*  44: 86 */     this.resourceMap = WPBDApp.getResourceMap(MemberDetail.class);
/*  45: 87 */     tabs.addChangeListener(new ChangeListener()
/*  46:    */     {
/*  47:    */       public void stateChanged(ChangeEvent e)
/*  48:    */       {
/*  49: 89 */         MemberDetail.this.updateTabDependencies();
/*  50:    */       }
/*  51:    */     });
/*  52:    */   }
/*  53:    */   
/*  54:    */   private String tabLabel(Member[] members)
/*  55:    */   {
/*  56: 95 */     StringBuilder s = new StringBuilder();
/*  57: 96 */     if (members.length == 1)
/*  58:    */     {
/*  59: 97 */       s.append(this.resourceMap.getString("tab1.text", new Object[] { Integer.valueOf(members[0].getNumber()) }));
/*  60:    */     }
/*  61:    */     else
/*  62:    */     {
/*  63:100 */       int startRange = members[0].getNumber();
/*  64:101 */       int endRange = startRange;
/*  65:102 */       s.append(this.resourceMap.getString("tabMany.text", new Object[] { Integer.valueOf(startRange) }));
/*  66:103 */       for (int i = 1; i < members.length; i++)
/*  67:    */       {
/*  68:104 */         int n = members[i].getNumber();
/*  69:105 */         if (n == endRange + 1)
/*  70:    */         {
/*  71:106 */           endRange = n;
/*  72:    */         }
/*  73:    */         else
/*  74:    */         {
/*  75:109 */           if (endRange > startRange)
/*  76:    */           {
/*  77:110 */             s.append(endRange == startRange + 1 ? ',' : '-');
/*  78:111 */             s.append(endRange);
/*  79:    */           }
/*  80:113 */           s.append("," + n);
/*  81:114 */           startRange = endRange = n;
/*  82:    */         }
/*  83:    */       }
/*  84:117 */       if (endRange > startRange)
/*  85:    */       {
/*  86:118 */         s.append(endRange == startRange + 1 ? ',' : '-');
/*  87:119 */         s.append(endRange);
/*  88:    */       }
/*  89:    */     }
/*  90:122 */     return s.toString();
/*  91:    */   }
/*  92:    */   
/*  93:    */   private String truncate(String s, int length)
/*  94:    */   {
/*  95:126 */     for (int i = length; i < s.length(); i++) {
/*  96:127 */       if (s.charAt(i) == ',') {
/*  97:128 */         return s.substring(0, i) + "...";
/*  98:    */       }
/*  99:    */     }
/* 100:131 */     return s;
/* 101:    */   }
/* 102:    */   
/* 103:    */   private void updateTabDependencies()
/* 104:    */   {
/* 105:135 */     int tabSelectedIndex = this.tabs.getSelectedIndex();
/* 106:136 */     if (tabSelectedIndex >= 0)
/* 107:    */     {
/* 108:137 */       Material material = null;
/* 109:138 */       Shape shape = null;
/* 110:139 */       Member[] members = null;
/* 111:140 */       if ((this.memberLists == null) || (this.memberLists.length == 0))
/* 112:    */       {
/* 113:141 */         Inventory inventory = this.bridge.getInventory();
/* 114:142 */         int materialIndex = this.stockSelector.getMaterialIndex();
/* 115:143 */         material = materialIndex == -1 ? null : inventory.getMaterial(materialIndex);
/* 116:144 */         int sectionIndex = this.stockSelector.getSectionIndex();
/* 117:145 */         int sizeIndex = this.stockSelector.getSizeIndex();
/* 118:146 */         shape = (sectionIndex == -1) || (sizeIndex == -1) ? null : inventory.getShape(sectionIndex, sizeIndex);
/* 119:    */       }
/* 120:    */       else
/* 121:    */       {
/* 122:149 */         members = this.memberLists[tabSelectedIndex];
/* 123:150 */         material = members[0].getMaterial();
/* 124:151 */         shape = members[0].getShape();
/* 125:    */       }
/* 126:153 */       this.crossSectionSketch.initialize(shape);
/* 127:154 */       if (material == null)
/* 128:    */       {
/* 129:155 */         for (int i = 0; i < 4; i++) {
/* 130:156 */           this.materialTable.setValueAt("--", i, 1);
/* 131:    */         }
/* 132:    */       }
/* 133:    */       else
/* 134:    */       {
/* 135:160 */         this.materialTable.setValueAt(material.getName(), 0, 1);
/* 136:161 */         this.materialTable.setValueAt(this.resourceMap.getString("fyFormat.text", new Object[] { Double.valueOf(material.getFy()) }), 1, 1);
/* 137:162 */         this.materialTable.setValueAt(this.resourceMap.getString("eFormat.text", new Object[] { Double.valueOf(material.getE()) }), 2, 1);
/* 138:163 */         this.materialTable.setValueAt(this.resourceMap.getString("densityFormat.text", new Object[] { Double.valueOf(material.getDensity()) }), 3, 1);
/* 139:    */       }
/* 140:165 */       if (shape == null)
/* 141:    */       {
/* 142:166 */         for (int i = 0; i < 4; i++) {
/* 143:167 */           this.dimensionsTable.setValueAt("--", i, 1);
/* 144:    */         }
/* 145:    */       }
/* 146:    */       else
/* 147:    */       {
/* 148:171 */         this.dimensionsTable.setValueAt(shape.getSection().getName(), 0, 1);
/* 149:172 */         this.dimensionsTable.setValueAt(shape.getName(), 1, 1);
/* 150:173 */         this.dimensionsTable.setValueAt(this.resourceMap.getString("areaFormat.text", new Object[] { Double.valueOf(shape.getArea()) }), 2, 1);
/* 151:174 */         this.dimensionsTable.setValueAt(this.resourceMap.getString("momentFormat.text", new Object[] { Double.valueOf(shape.getMoment()) }), 3, 1);
/* 152:    */       }
/* 153:176 */       double memberLength = 0.0D;
/* 154:177 */       if ((members == null) || (members.length > 1))
/* 155:    */       {
/* 156:178 */         this.dimensionsTable.setValueAt("--", 4, 1);
/* 157:    */       }
/* 158:    */       else
/* 159:    */       {
/* 160:181 */         memberLength = members[0].getLength();
/* 161:182 */         this.dimensionsTable.setValueAt(this.resourceMap.getString("lengthFormat.text", new Object[] { Double.valueOf(memberLength) }), 4, 1);
/* 162:    */       }
/* 163:184 */       if ((material == null) || (shape == null))
/* 164:    */       {
/* 165:185 */         this.costTable.setValueAt("--", 0, 1);
/* 166:186 */         this.costTable.setValueAt("--", 1, 1);
/* 167:    */       }
/* 168:    */       else
/* 169:    */       {
/* 170:189 */         double cost = material.getCost(shape.getSection()) * shape.getArea() * material.getDensity();
/* 171:190 */         this.costTable.setValueAt(this.resourceMap.getString("costPerMeterFormat.text", new Object[] { Double.valueOf(cost) }), 0, 1);
/* 172:191 */         if (memberLength == 0.0D) {
/* 173:192 */           this.costTable.setValueAt("--", 1, 1);
/* 174:    */         } else {
/* 175:195 */           this.costTable.setValueAt(this.resourceMap.getString("totalCostFormat.text", new Object[] { Double.valueOf(cost * memberLength) }), 1, 1);
/* 176:    */         }
/* 177:    */       }
/* 178:198 */       DesignConditions conditions = this.bridge.getDesignConditions();
/* 179:199 */       this.strengthCurve.initialize(material, shape, members, this.memberLists, this.bridge.isAnalysisValid() ? this.bridge.getAnalysis() : null, conditions != null);
/* 180:202 */       if ((this.memberLists != null) && (tabSelectedIndex < this.memberLists.length) && (this.memberLists[tabSelectedIndex].length > 1))
/* 181:    */       {
/* 182:203 */         this.memberSelectBox.setEnabled(true);
/* 183:204 */         Object[] memberNumbers = new Object[this.memberLists[tabSelectedIndex].length];
/* 184:205 */         for (int i = 0; i < memberNumbers.length; i++) {
/* 185:206 */           memberNumbers[i] = Integer.valueOf(this.memberLists[tabSelectedIndex][i].getNumber());
/* 186:    */         }
/* 187:208 */         ((ExtendedComboBoxModel)this.memberSelectBox.getModel()).initialize(memberNumbers);
/* 188:    */       }
/* 189:    */       else
/* 190:    */       {
/* 191:211 */         this.memberSelectBox.removeAllItems();
/* 192:212 */         this.memberSelectBox.setEnabled(false);
/* 193:    */       }
/* 194:    */     }
/* 195:    */   }
/* 196:    */   
/* 197:    */   private void initializeTabs(Member[][] memberLists)
/* 198:    */   {
/* 199:218 */     this.memberLists = memberLists;
/* 200:    */     
/* 201:    */ 
/* 202:221 */     this.tabs.removeAll();
/* 203:222 */     if ((memberLists == null) || (memberLists.length == 0))
/* 204:    */     {
/* 205:223 */       this.tabs.addTab(this.resourceMap.getString("current.text", new Object[0]), this.memberDetailPanel);
/* 206:224 */       this.graphAllCheck.setEnabled(false);
/* 207:225 */       this.memberSelectBox.setEnabled(false);
/* 208:    */     }
/* 209:    */     else
/* 210:    */     {
/* 211:228 */       for (int i = 0; i < memberLists.length; i++)
/* 212:    */       {
/* 213:229 */         String label = tabLabel(memberLists[i]);
/* 214:230 */         this.tabs.addTab(truncate(label, 16), null, i == 0 ? this.memberDetailPanel : null, label);
/* 215:    */       }
/* 216:232 */       this.graphAllCheck.setEnabled(memberLists.length > 1);
/* 217:    */     }
/* 218:    */   }
/* 219:    */   
/* 220:    */   public void update(boolean memberListChange)
/* 221:    */   {
/* 222:242 */     if (memberListChange) {
/* 223:243 */       initializeTabs(this.bridge.getSelectedStockLists());
/* 224:    */     }
/* 225:245 */     updateTabDependencies();
/* 226:246 */     this.strengthCurve.repaint();
/* 227:    */   }
/* 228:    */   
/* 229:    */   public void handleShowAllStateChange(ItemEvent e)
/* 230:    */   {
/* 231:255 */     this.strengthCurve.setShowAll(e.getStateChange() == 1);
/* 232:    */   }
/* 233:    */   
/* 234:    */   public void handleMemberSelectChange(ItemEvent e)
/* 235:    */   {
/* 236:264 */     if (e.getStateChange() == 1) {
/* 237:265 */       this.strengthCurve.setSelectedMemberIndex(this.memberSelectBox.getSelectedIndex());
/* 238:    */     }
/* 239:    */   }
/* 240:    */   
/* 241:    */   public void stateChanged(ChangeEvent e)
/* 242:    */   {
/* 243:275 */     update(true);
/* 244:    */   }
/* 245:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.MemberDetail
 * JD-Core Version:    0.7.0.1
 */