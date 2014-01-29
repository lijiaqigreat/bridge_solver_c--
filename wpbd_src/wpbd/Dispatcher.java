/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import javax.swing.ListSelectionModel;
/*   4:    */ import javax.swing.event.ChangeEvent;
/*   5:    */ import javax.swing.event.ChangeListener;
/*   6:    */ import javax.swing.event.ListSelectionEvent;
/*   7:    */ import javax.swing.event.ListSelectionListener;
/*   8:    */ 
/*   9:    */ public class Dispatcher
/*  10:    */ {
/*  11:    */   private EditableBridgeModel bridge;
/*  12:    */   private MemberTable memberTable;
/*  13:    */   private MemberDetail memberDetail;
/*  14:    */   private StockSelector stockSelector;
/*  15:    */   private StockSelector popupStockSelector;
/*  16:    */   private DraftingPanel draftingPanel;
/*  17:    */   private ListSelectionListener memberTableSelectionListener;
/*  18:    */   private ChangeListener stockSelectorListener;
/*  19:    */   private ChangeListener popupStockSelectorListener;
/*  20:    */   private ChangeListener bridgeSelectionChangeListener;
/*  21:    */   private ChangeListener bridgeStructureChangeListener;
/*  22:    */   private ChangeListener bridgeAnalysisChangeListener;
/*  23:    */   
/*  24:    */   private void enable()
/*  25:    */   {
/*  26: 47 */     this.memberTable.getSelectionModel().addListSelectionListener(this.memberTableSelectionListener);
/*  27: 48 */     this.stockSelector.addChangeListener(this.stockSelectorListener);
/*  28: 49 */     this.popupStockSelector.addChangeListener(this.popupStockSelectorListener);
/*  29: 50 */     this.bridge.addSelectionChangeListener(this.bridgeSelectionChangeListener);
/*  30: 51 */     this.bridge.addStructureChangeListener(this.bridgeStructureChangeListener);
/*  31: 52 */     this.bridge.addAnalysisChangeListener(this.bridgeAnalysisChangeListener);
/*  32:    */   }
/*  33:    */   
/*  34:    */   private void disable()
/*  35:    */   {
/*  36: 56 */     this.bridge.removeAnalysisChangeListener(this.bridgeAnalysisChangeListener);
/*  37: 57 */     this.bridge.removeStructureChangeListener(this.bridgeStructureChangeListener);
/*  38: 58 */     this.bridge.removeSelectionChangeListener(this.bridgeSelectionChangeListener);
/*  39: 59 */     this.popupStockSelector.removeChangeListener(this.popupStockSelectorListener);
/*  40: 60 */     this.stockSelector.removeChangeListener(this.stockSelectorListener);
/*  41: 61 */     this.memberTable.getSelectionModel().removeListSelectionListener(this.memberTableSelectionListener);
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void initialize(EditableBridgeModel b, MemberTable mt, MemberDetail md, StockSelector ss, StockSelector pss, DraftingPanel dp)
/*  45:    */   {
/*  46: 75 */     this.bridge = b;
/*  47: 76 */     this.memberTable = mt;
/*  48: 77 */     this.memberDetail = md;
/*  49: 78 */     this.stockSelector = ss;
/*  50: 79 */     this.popupStockSelector = pss;
/*  51: 80 */     this.draftingPanel = dp;
/*  52: 81 */     this.memberTableSelectionListener = new ListSelectionListener()
/*  53:    */     {
/*  54:    */       public void valueChanged(ListSelectionEvent e)
/*  55:    */       {
/*  56: 86 */         if (((!e.getValueIsAdjusting()) || (!Dispatcher.this.memberTable.isSorting())) && (e.getFirstIndex() >= 0))
/*  57:    */         {
/*  58: 88 */           Dispatcher.this.disable();
/*  59:    */           
/*  60: 90 */           Dispatcher.this.bridge.selectMembers(Dispatcher.this.memberTable, e.getFirstIndex(), e.getLastIndex());
/*  61: 93 */           if (!e.getValueIsAdjusting())
/*  62:    */           {
/*  63: 95 */             Dispatcher.this.stockSelector.matchSelection(Dispatcher.this.bridge);
/*  64:    */             
/*  65: 97 */             Dispatcher.this.memberDetail.update(true);
/*  66:    */           }
/*  67:100 */           Dispatcher.this.draftingPanel.paintBackingStore();
/*  68:101 */           Dispatcher.this.draftingPanel.repaint();
/*  69:102 */           Dispatcher.this.enable();
/*  70:    */         }
/*  71:    */       }
/*  72:105 */     };
/*  73:106 */     this.stockSelectorListener = new ChangeListener()
/*  74:    */     {
/*  75:    */       public void stateChanged(ChangeEvent e)
/*  76:    */       {
/*  77:111 */         Dispatcher.this.disable();
/*  78:112 */         Dispatcher.this.bridge.changeSelectedMembers(Dispatcher.this.stockSelector.getMaterialIndex(), Dispatcher.this.stockSelector.getSectionIndex(), Dispatcher.this.stockSelector.getSizeIndex());
/*  79:    */         
/*  80:114 */         Dispatcher.this.memberTable.fireTableDataChanged();
/*  81:115 */         Dispatcher.this.memberTable.loadSelection();
/*  82:116 */         Dispatcher.this.memberDetail.update(false);
/*  83:117 */         Dispatcher.this.draftingPanel.paintBackingStore();
/*  84:118 */         Dispatcher.this.draftingPanel.repaint();
/*  85:119 */         Dispatcher.this.enable();
/*  86:    */       }
/*  87:121 */     };
/*  88:122 */     this.popupStockSelectorListener = new ChangeListener()
/*  89:    */     {
/*  90:    */       public void stateChanged(ChangeEvent e)
/*  91:    */       {
/*  92:127 */         Dispatcher.this.disable();
/*  93:128 */         Dispatcher.this.bridge.changeSelectedMembers(Dispatcher.this.popupStockSelector.getMaterialIndex(), Dispatcher.this.popupStockSelector.getSectionIndex(), Dispatcher.this.popupStockSelector.getSizeIndex());
/*  94:    */         
/*  95:130 */         Dispatcher.this.stockSelector.match(Dispatcher.this.popupStockSelector);
/*  96:131 */         Dispatcher.this.memberTable.fireTableDataChanged();
/*  97:132 */         Dispatcher.this.memberTable.loadSelection();
/*  98:133 */         Dispatcher.this.memberDetail.update(false);
/*  99:134 */         Dispatcher.this.draftingPanel.paintBackingStore();
/* 100:135 */         Dispatcher.this.draftingPanel.repaint();
/* 101:136 */         Dispatcher.this.enable();
/* 102:    */       }
/* 103:138 */     };
/* 104:139 */     this.bridgeSelectionChangeListener = new ChangeListener()
/* 105:    */     {
/* 106:    */       public void stateChanged(ChangeEvent e)
/* 107:    */       {
/* 108:142 */         Dispatcher.this.disable();
/* 109:    */         
/* 110:    */ 
/* 111:145 */         Dispatcher.this.stockSelector.matchSelection(Dispatcher.this.bridge);
/* 112:    */         
/* 113:147 */         Dispatcher.this.memberTable.loadSelection();
/* 114:148 */         Dispatcher.this.memberDetail.update(true);
/* 115:    */         
/* 116:150 */         Dispatcher.this.draftingPanel.paintBackingStore();
/* 117:151 */         Dispatcher.this.draftingPanel.repaint();
/* 118:152 */         Dispatcher.this.enable();
/* 119:    */       }
/* 120:154 */     };
/* 121:155 */     this.bridgeStructureChangeListener = new ChangeListener()
/* 122:    */     {
/* 123:    */       public void stateChanged(ChangeEvent e)
/* 124:    */       {
/* 125:158 */         Dispatcher.this.disable();
/* 126:    */         
/* 127:160 */         Dispatcher.this.stockSelector.matchSelection(Dispatcher.this.bridge);
/* 128:    */         
/* 129:162 */         Dispatcher.this.memberTable.fireTableDataChanged();
/* 130:163 */         Dispatcher.this.memberTable.loadSelection();
/* 131:164 */         Dispatcher.this.memberDetail.update(true);
/* 132:165 */         Dispatcher.this.draftingPanel.paintBackingStore();
/* 133:166 */         Dispatcher.this.draftingPanel.repaint();
/* 134:    */         
/* 135:    */ 
/* 136:169 */         Dispatcher.this.enable();
/* 137:    */       }
/* 138:171 */     };
/* 139:172 */     this.bridgeAnalysisChangeListener = new ChangeListener()
/* 140:    */     {
/* 141:    */       public void stateChanged(ChangeEvent e)
/* 142:    */       {
/* 143:175 */         Dispatcher.this.disable();
/* 144:    */         
/* 145:177 */         Dispatcher.this.memberTable.fireTableDataChanged();
/* 146:178 */         Dispatcher.this.memberTable.loadSelection();
/* 147:179 */         Dispatcher.this.memberDetail.update(false);
/* 148:180 */         Dispatcher.this.draftingPanel.paintBackingStore();
/* 149:181 */         Dispatcher.this.draftingPanel.repaint();
/* 150:182 */         Dispatcher.this.enable();
/* 151:    */       }
/* 152:185 */     };
/* 153:186 */     enable();
/* 154:    */   }
/* 155:    */   
/* 156:    */   public void incrementMemberSize(int inc)
/* 157:    */   {
/* 158:195 */     disable();
/* 159:196 */     this.bridge.incrementMemberSize(inc);
/* 160:197 */     this.stockSelector.matchSelection(this.bridge);
/* 161:198 */     this.popupStockSelector.match(this.stockSelector);
/* 162:199 */     this.memberTable.fireTableDataChanged();
/* 163:200 */     this.memberTable.loadSelection();
/* 164:201 */     this.memberDetail.update(false);
/* 165:202 */     this.draftingPanel.paintBackingStore();
/* 166:203 */     this.draftingPanel.repaint();
/* 167:204 */     enable();
/* 168:    */   }
/* 169:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.Dispatcher
 * JD-Core Version:    0.7.0.1
 */