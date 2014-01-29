/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.text.DecimalFormat;
/*   4:    */ import java.text.NumberFormat;
/*   5:    */ import java.util.ArrayList;
/*   6:    */ import javax.swing.table.AbstractTableModel;
/*   7:    */ 
/*   8:    */ class MemberTableModel
/*   9:    */   extends AbstractTableModel
/*  10:    */ {
/*  11:    */   private EditableBridgeModel bridge;
/*  12:    */   
/*  13:    */   public MemberTableModel(EditableBridgeModel bridge)
/*  14:    */   {
/*  15: 14 */     this.bridge = bridge;
/*  16:    */   }
/*  17:    */   
/*  18:    */   public ArrayList<Member> getMembers()
/*  19:    */   {
/*  20: 18 */     return this.bridge.getMembers();
/*  21:    */   }
/*  22:    */   
/*  23:    */   public Inventory getInventory()
/*  24:    */   {
/*  25: 22 */     return this.bridge.getInventory();
/*  26:    */   }
/*  27:    */   
/*  28:    */   public void loadSelection(MemberTable memberTable)
/*  29:    */   {
/*  30: 32 */     ArrayList<Member> members = this.bridge.getMembers();
/*  31:    */     
/*  32: 34 */     int i0 = 0;
/*  33:    */     for (;;)
/*  34:    */     {
/*  35: 36 */       if (i0 == members.size())
/*  36:    */       {
/*  37: 37 */         memberTable.clearSelection();
/*  38: 38 */         return;
/*  39:    */       }
/*  40: 40 */       if (((Member)members.get(memberTable.convertRowIndexToModel(i0))).isSelected()) {
/*  41:    */         break;
/*  42:    */       }
/*  43: 43 */       i0++;
/*  44:    */     }
/*  45: 45 */     int i1 = i0 + 1;
/*  46: 46 */     while ((i1 < members.size()) && (((Member)members.get(memberTable.convertRowIndexToModel(i1))).isSelected())) {
/*  47: 47 */       i1++;
/*  48:    */     }
/*  49: 49 */     memberTable.setRowSelectionInterval(i0, i1 - 1);
/*  50:    */     for (;;)
/*  51:    */     {
/*  52: 53 */       i0 = i1;
/*  53:    */       for (;;)
/*  54:    */       {
/*  55: 55 */         if (i0 == members.size()) {
/*  56: 56 */           return;
/*  57:    */         }
/*  58: 58 */         if (((Member)members.get(memberTable.convertRowIndexToModel(i0))).isSelected()) {
/*  59:    */           break;
/*  60:    */         }
/*  61: 61 */         i0++;
/*  62:    */       }
/*  63: 63 */       i1 = i0 + 1;
/*  64: 64 */       while ((i1 < members.size()) && (((Member)members.get(memberTable.convertRowIndexToModel(i1))).isSelected())) {
/*  65: 65 */         i1++;
/*  66:    */       }
/*  67: 67 */       memberTable.addRowSelectionInterval(i0, i1 - 1);
/*  68:    */     }
/*  69:    */   }
/*  70:    */   
/*  71: 73 */   public static final String[] columnNames = { "number", "material", "crossSection", "size", "length", "SPACER", "slenderness", "compression", "tension" };
/*  72:    */   
/*  73:    */   public String getColumnName(int column)
/*  74:    */   {
/*  75: 87 */     return columnNames[column];
/*  76:    */   }
/*  77:    */   
/*  78: 90 */   private static final Class[] columnClasses = { Integer.class, String.class, String.class, Integer.class, Double.class, String.class, Double.class, Double.class, Double.class };
/*  79:    */   
/*  80:    */   public Class<?> getColumnClass(int columnIndex)
/*  81:    */   {
/*  82:104 */     return columnClasses[columnIndex];
/*  83:    */   }
/*  84:    */   
/*  85:    */   public int getRowCount()
/*  86:    */   {
/*  87:108 */     return this.bridge.getMembers().size();
/*  88:    */   }
/*  89:    */   
/*  90:    */   public int getColumnCount()
/*  91:    */   {
/*  92:112 */     return columnClasses.length;
/*  93:    */   }
/*  94:    */   
/*  95:115 */   private final NumberFormat doubleFormatter = new DecimalFormat("0.00");
/*  96:    */   
/*  97:    */   public Object getValueAt(int rowIndex, int columnIndex)
/*  98:    */   {
/*  99:118 */     Member member = (Member)this.bridge.getMembers().get(rowIndex);
/* 100:119 */     switch (columnIndex)
/* 101:    */     {
/* 102:    */     case 0: 
/* 103:122 */       return Integer.valueOf(member.getNumber());
/* 104:    */     case 1: 
/* 105:125 */       return member.getMaterial().getShortName();
/* 106:    */     case 2: 
/* 107:128 */       return member.getShape().getSection().getShortName();
/* 108:    */     case 3: 
/* 109:131 */       return Integer.valueOf(member.getShape().getNominalWidth());
/* 110:    */     case 4: 
/* 111:134 */       return Double.valueOf(member.getLength());
/* 112:    */     case 5: 
/* 113:136 */       return "";
/* 114:    */     case 6: 
/* 115:138 */       return Double.valueOf(member.getSlenderness());
/* 116:    */     case 7: 
/* 117:140 */       return Double.valueOf(member.getCompressionForceStrengthRatio());
/* 118:    */     case 8: 
/* 119:142 */       return Double.valueOf(member.getTensionForceStrengthRatio());
/* 120:    */     }
/* 121:144 */     return null;
/* 122:    */   }
/* 123:    */   
/* 124:    */   public Member getMember(int i)
/* 125:    */   {
/* 126:148 */     return (Member)this.bridge.getMembers().get(i);
/* 127:    */   }
/* 128:    */   
/* 129:    */   public boolean isAnalysisValid()
/* 130:    */   {
/* 131:152 */     return this.bridge.isAnalysisValid();
/* 132:    */   }
/* 133:    */   
/* 134:    */   public double getAllowableSlenderness()
/* 135:    */   {
/* 136:156 */     return this.bridge.getDesignConditions().getAllowableSlenderness();
/* 137:    */   }
/* 138:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.MemberTableModel
 * JD-Core Version:    0.7.0.1
 */