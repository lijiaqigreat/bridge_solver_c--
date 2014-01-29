/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.text.NumberFormat;
/*   5:    */ import java.util.Locale;
/*   6:    */ import javax.swing.event.ChangeEvent;
/*   7:    */ import javax.swing.event.ChangeListener;
/*   8:    */ import javax.swing.table.AbstractTableModel;
/*   9:    */ import org.jdesktop.application.ResourceMap;
/*  10:    */ 
/*  11:    */ public class DesignIterationTableModel
/*  12:    */   extends AbstractTableModel
/*  13:    */ {
/*  14:    */   private final EditableBridgeModel bridge;
/*  15:    */   private final BridgeModel cartoonBridge;
/*  16:    */   private final BridgeDraftingView cartoonView;
/*  17:    */   private int iterationCount;
/*  18: 36 */   private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
/*  19:    */   private final String[] headers;
/*  20:    */   
/*  21:    */   public DesignIterationTableModel(final EditableBridgeModel bridge)
/*  22:    */   {
/*  23: 45 */     this.bridge = bridge;
/*  24: 46 */     ResourceMap resourceMap = WPBDApp.getResourceMap(DesignIterationTableModel.class);
/*  25: 47 */     this.headers = resourceMap.getString("tableHeaders.text", new Object[0]).split(";");
/*  26: 48 */     this.cartoonBridge = new BridgeModel();
/*  27: 49 */     this.cartoonView = new BridgeDraftingView(this.cartoonBridge);
/*  28: 50 */     this.iterationCount = bridge.getIterationCount();
/*  29: 51 */     bridge.addIterationChangeListener(new ChangeListener()
/*  30:    */     {
/*  31:    */       public void stateChanged(ChangeEvent e)
/*  32:    */       {
/*  33: 54 */         int newIterationCount = bridge.getIterationCount();
/*  34: 55 */         int delta = newIterationCount - DesignIterationTableModel.this.iterationCount;
/*  35: 56 */         if (delta < 0) {
/*  36: 57 */           DesignIterationTableModel.this.fireTableRowsDeleted(DesignIterationTableModel.this.iterationCount + delta, DesignIterationTableModel.this.iterationCount - 1);
/*  37: 59 */         } else if (delta > 0) {
/*  38: 60 */           DesignIterationTableModel.this.fireTableRowsInserted(newIterationCount - delta, newIterationCount - 1);
/*  39:    */         }
/*  40: 62 */         DesignIterationTableModel.this.iterationCount = newIterationCount;
/*  41:    */       }
/*  42:    */     });
/*  43:    */   }
/*  44:    */   
/*  45:    */   public EditableBridgeModel getBridge()
/*  46:    */   {
/*  47: 73 */     return this.bridge;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public BridgeDraftingView getBridgeView()
/*  51:    */   {
/*  52: 82 */     return this.cartoonView;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public void loadCartoon(int index)
/*  56:    */   {
/*  57: 91 */     if ((0 <= index) && (index < this.bridge.getIterationCount())) {
/*  58:    */       try
/*  59:    */       {
/*  60: 93 */         this.cartoonBridge.parseBytes(this.bridge.getDesignIteration(index).getBridgeModelAsBytes());
/*  61: 94 */         this.cartoonView.initialize(this.cartoonBridge.getDesignConditions());
/*  62:    */       }
/*  63:    */       catch (IOException ex) {}
/*  64:    */     }
/*  65:    */   }
/*  66:    */   
/*  67:    */   public int getRowCount()
/*  68:    */   {
/*  69:100 */     return this.bridge.getIterationCount();
/*  70:    */   }
/*  71:    */   
/*  72:    */   public int getColumnCount()
/*  73:    */   {
/*  74:104 */     return 4;
/*  75:    */   }
/*  76:    */   
/*  77:    */   public String getColumnName(int column)
/*  78:    */   {
/*  79:109 */     return this.headers[column];
/*  80:    */   }
/*  81:    */   
/*  82:    */   public Object getValueAt(int rowIndex, int columnIndex)
/*  83:    */   {
/*  84:113 */     switch (columnIndex)
/*  85:    */     {
/*  86:    */     case 0: 
/*  87:115 */       return IconFactory.bridgeStatus(this.bridge.getDesignIteration(rowIndex).getBridgeStatus());
/*  88:    */     case 1: 
/*  89:117 */       return Integer.valueOf(this.bridge.getDesignIteration(rowIndex).getNumber());
/*  90:    */     case 2: 
/*  91:119 */       return this.currencyFormat.format(this.bridge.getDesignIteration(rowIndex).getCost()) + " ";
/*  92:    */     case 3: 
/*  93:121 */       return " " + this.bridge.getDesignIteration(rowIndex).getProjectId();
/*  94:    */     }
/*  95:123 */     return null;
/*  96:    */   }
/*  97:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.DesignIterationTableModel
 * JD-Core Version:    0.7.0.1
 */