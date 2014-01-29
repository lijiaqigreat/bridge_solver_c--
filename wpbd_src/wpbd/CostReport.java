/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.awt.Container;
/*   4:    */ import java.awt.Dimension;
/*   5:    */ import java.awt.Frame;
/*   6:    */ import java.awt.Toolkit;
/*   7:    */ import java.awt.datatransfer.Clipboard;
/*   8:    */ import java.awt.datatransfer.StringSelection;
/*   9:    */ import java.awt.event.ActionEvent;
/*  10:    */ import java.awt.event.ActionListener;
/*  11:    */ import javax.accessibility.AccessibleContext;
/*  12:    */ import javax.help.HelpBroker;
/*  13:    */ import javax.swing.GroupLayout;
/*  14:    */ import javax.swing.GroupLayout.Alignment;
/*  15:    */ import javax.swing.GroupLayout.ParallelGroup;
/*  16:    */ import javax.swing.GroupLayout.SequentialGroup;
/*  17:    */ import javax.swing.JButton;
/*  18:    */ import javax.swing.JDialog;
/*  19:    */ import javax.swing.JLabel;
/*  20:    */ import javax.swing.JRootPane;
/*  21:    */ import javax.swing.JScrollPane;
/*  22:    */ import javax.swing.JTable;
/*  23:    */ import javax.swing.JViewport;
/*  24:    */ import javax.swing.LayoutStyle.ComponentPlacement;
/*  25:    */ import javax.swing.Timer;
/*  26:    */ import org.jdesktop.application.Application;
/*  27:    */ import org.jdesktop.application.ApplicationContext;
/*  28:    */ import org.jdesktop.application.ResourceMap;
/*  29:    */ 
/*  30:    */ public class CostReport
/*  31:    */   extends JDialog
/*  32:    */ {
/*  33:    */   private BridgeModel.Costs costs;
/*  34: 34 */   private Timer commentHider = null;
/*  35:    */   private JButton closeButton;
/*  36:    */   private JLabel commentLabel;
/*  37:    */   private JButton copyButton;
/*  38:    */   private JTable costTable;
/*  39:    */   private JScrollPane costTableScroll;
/*  40:    */   private JButton helpButton;
/*  41:    */   private JButton printButton;
/*  42:    */   
/*  43:    */   public CostReport(Frame parent)
/*  44:    */   {
/*  45: 42 */     super(parent, true);
/*  46: 43 */     initComponents();
/*  47: 44 */     Help.getBroker().enableHelpOnButton(this.helpButton, "hlp_cost", Help.getSet());
/*  48: 45 */     getRootPane().setDefaultButton(this.closeButton);
/*  49:    */   }
/*  50:    */   
/*  51:    */   public void initialize(BridgeModel.Costs costs)
/*  52:    */   {
/*  53: 54 */     this.costs = costs;
/*  54: 55 */     ((CostReportTableModel)this.costTable.getModel()).initialize(costs);
/*  55: 56 */     ((CostReportTable)this.costTable).initalize();
/*  56:    */     
/*  57: 58 */     pack();
/*  58: 59 */     int heightDiff = this.costTableScroll.getViewport().getHeight() - this.costTable.getHeight();
/*  59: 60 */     if (heightDiff > 0)
/*  60:    */     {
/*  61: 61 */       Dimension size = getSize();
/*  62: 62 */       size.height -= heightDiff;
/*  63: 63 */       setSize(size);
/*  64: 64 */       setResizable(false);
/*  65:    */     }
/*  66:    */   }
/*  67:    */   
/*  68:    */   private void initComponents()
/*  69:    */   {
/*  70: 72 */     this.costTableScroll = new JScrollPane();
/*  71: 73 */     this.costTable = new CostReportTable();
/*  72: 74 */     this.helpButton = new JButton();
/*  73: 75 */     this.copyButton = new JButton();
/*  74: 76 */     this.printButton = new JButton();
/*  75: 77 */     this.closeButton = new JButton();
/*  76: 78 */     this.commentLabel = new DisappearingLabel(2000);
/*  77:    */     
/*  78: 80 */     ResourceMap resourceMap = ((WPBDApp)Application.getInstance(WPBDApp.class)).getContext().getResourceMap(CostReport.class);
/*  79: 81 */     setTitle(resourceMap.getString("costReport.title", new Object[0]));
/*  80: 82 */     setName("costReport");
/*  81:    */     
/*  82: 84 */     this.costTableScroll.setName("costTableScroll");
/*  83:    */     
/*  84: 86 */     this.costTable.setModel(new CostReportTableModel());
/*  85: 87 */     this.costTable.setFocusable(false);
/*  86: 88 */     this.costTable.setIntercellSpacing(new Dimension(4, 1));
/*  87: 89 */     this.costTable.setName("costTable");
/*  88: 90 */     this.costTable.setRowSelectionAllowed(false);
/*  89: 91 */     this.costTable.setShowVerticalLines(false);
/*  90: 92 */     this.costTableScroll.setViewportView(this.costTable);
/*  91:    */     
/*  92: 94 */     this.helpButton.setText(resourceMap.getString("helpButton.text", new Object[0]));
/*  93: 95 */     this.helpButton.setName("helpButton");
/*  94:    */     
/*  95: 97 */     this.copyButton.setText(resourceMap.getString("copyButton.text", new Object[0]));
/*  96: 98 */     this.copyButton.setName("copyButton");
/*  97: 99 */     this.copyButton.addActionListener(new ActionListener()
/*  98:    */     {
/*  99:    */       public void actionPerformed(ActionEvent evt)
/* 100:    */       {
/* 101:101 */         CostReport.this.copyButtonActionPerformed(evt);
/* 102:    */       }
/* 103:104 */     });
/* 104:105 */     this.printButton.setText(resourceMap.getString("printButton.text", new Object[0]));
/* 105:106 */     this.printButton.setName("printButton");
/* 106:107 */     this.printButton.addActionListener(new ActionListener()
/* 107:    */     {
/* 108:    */       public void actionPerformed(ActionEvent evt)
/* 109:    */       {
/* 110:109 */         CostReport.this.printButtonActionPerformed(evt);
/* 111:    */       }
/* 112:112 */     });
/* 113:113 */     this.closeButton.setText(resourceMap.getString("closeButton.text", new Object[0]));
/* 114:114 */     this.closeButton.setName("closeButton");
/* 115:115 */     this.closeButton.addActionListener(new ActionListener()
/* 116:    */     {
/* 117:    */       public void actionPerformed(ActionEvent evt)
/* 118:    */       {
/* 119:117 */         CostReport.this.closeButtonActionPerformed(evt);
/* 120:    */       }
/* 121:120 */     });
/* 122:121 */     this.commentLabel.setHorizontalAlignment(4);
/* 123:122 */     this.commentLabel.setText(null);
/* 124:123 */     this.commentLabel.setName("commentLabel");
/* 125:    */     
/* 126:125 */     GroupLayout layout = new GroupLayout(getContentPane());
/* 127:126 */     getContentPane().setLayout(layout);
/* 128:127 */     layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addGap(7, 7, 7).addComponent(this.commentLabel, -2, 388, -2).addGap(18, 18, 18).addComponent(this.helpButton).addGap(12, 12, 12).addComponent(this.copyButton, -1, 198, 32767).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(this.printButton, -1, 124, 32767).addGap(12, 12, 12).addComponent(this.closeButton, -2, 85, -2).addContainerGap()).addComponent(this.costTableScroll, GroupLayout.Alignment.TRAILING, -1, 937, 32767));
/* 129:    */     
/* 130:    */ 
/* 131:    */ 
/* 132:    */ 
/* 133:    */ 
/* 134:    */ 
/* 135:    */ 
/* 136:    */ 
/* 137:    */ 
/* 138:    */ 
/* 139:    */ 
/* 140:    */ 
/* 141:    */ 
/* 142:    */ 
/* 143:    */ 
/* 144:143 */     layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(this.costTableScroll, -1, 265, 32767).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.closeButton).addComponent(this.printButton).addComponent(this.helpButton).addComponent(this.copyButton).addComponent(this.commentLabel)).addContainerGap()));
/* 145:    */     
/* 146:    */ 
/* 147:    */ 
/* 148:    */ 
/* 149:    */ 
/* 150:    */ 
/* 151:    */ 
/* 152:    */ 
/* 153:    */ 
/* 154:    */ 
/* 155:    */ 
/* 156:    */ 
/* 157:    */ 
/* 158:157 */     this.commentLabel.getAccessibleContext().setAccessibleName(null);
/* 159:    */     
/* 160:159 */     pack();
/* 161:    */   }
/* 162:    */   
/* 163:    */   private void closeButtonActionPerformed(ActionEvent evt)
/* 164:    */   {
/* 165:163 */     setVisible(false);
/* 166:    */   }
/* 167:    */   
/* 168:    */   private void copyButtonActionPerformed(ActionEvent evt)
/* 169:    */   {
/* 170:167 */     Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
/* 171:168 */     StringSelection selection = new StringSelection(this.costs.toTabDelimitedText());
/* 172:169 */     clipboard.setContents(selection, selection);
/* 173:    */     
/* 174:171 */     this.commentLabel.setText(WPBDApp.getResourceMap(CostReport.class).getString("copied.text", new Object[0]));
/* 175:    */   }
/* 176:    */   
/* 177:    */   private void printButtonActionPerformed(ActionEvent evt)
/* 178:    */   {
/* 179:175 */     PrinterUI.print(this, this.costTable, CostReport.class, this.costs.notes);
/* 180:    */   }
/* 181:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.CostReport
 * JD-Core Version:    0.7.0.1
 */