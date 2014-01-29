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
/*  11:    */ import javax.swing.GroupLayout;
/*  12:    */ import javax.swing.GroupLayout.Alignment;
/*  13:    */ import javax.swing.GroupLayout.ParallelGroup;
/*  14:    */ import javax.swing.GroupLayout.SequentialGroup;
/*  15:    */ import javax.swing.JButton;
/*  16:    */ import javax.swing.JDialog;
/*  17:    */ import javax.swing.JLabel;
/*  18:    */ import javax.swing.JRootPane;
/*  19:    */ import javax.swing.JScrollPane;
/*  20:    */ import javax.swing.JTable;
/*  21:    */ import javax.swing.JViewport;
/*  22:    */ import javax.swing.LayoutStyle.ComponentPlacement;
/*  23:    */ import javax.swing.table.JTableHeader;
/*  24:    */ import org.jdesktop.application.Application;
/*  25:    */ import org.jdesktop.application.ApplicationContext;
/*  26:    */ import org.jdesktop.application.ResourceMap;
/*  27:    */ 
/*  28:    */ public class LoadTestReport
/*  29:    */   extends JDialog
/*  30:    */ {
/*  31:    */   private EditableBridgeModel bridge;
/*  32:    */   private JButton closeButton;
/*  33:    */   private JLabel commentLabel;
/*  34:    */   private JButton copyButton;
/*  35:    */   private JTable memberTable;
/*  36:    */   private JScrollPane memberTableScroll;
/*  37:    */   private JButton printButton;
/*  38:    */   
/*  39:    */   public LoadTestReport(Frame parent, EditableBridgeModel bridge)
/*  40:    */   {
/*  41: 40 */     super(parent, true);
/*  42: 41 */     this.bridge = bridge;
/*  43: 42 */     initComponents();
/*  44: 43 */     getRootPane().setDefaultButton(this.closeButton);
/*  45: 44 */     ((LoadTestReportTable)this.memberTable).initialize();
/*  46: 45 */     pack();
/*  47: 46 */     int heightDiff = this.memberTableScroll.getViewport().getHeight() - this.memberTable.getHeight();
/*  48: 47 */     Dimension size = getSize();
/*  49: 48 */     if (heightDiff > 0)
/*  50:    */     {
/*  51: 49 */       size.height -= heightDiff;
/*  52: 50 */       setSize(size);
/*  53: 51 */       setResizable(false);
/*  54:    */     }
/*  55:    */   }
/*  56:    */   
/*  57:    */   private void initComponents()
/*  58:    */   {
/*  59: 69 */     this.closeButton = new JButton();
/*  60: 70 */     this.printButton = new JButton();
/*  61: 71 */     this.copyButton = new JButton();
/*  62: 72 */     this.memberTableScroll = new JScrollPane();
/*  63: 73 */     this.memberTable = new LoadTestReportTable();
/*  64: 74 */     this.commentLabel = new DisappearingLabel(2000);
/*  65:    */     
/*  66: 76 */     ResourceMap resourceMap = ((WPBDApp)Application.getInstance(WPBDApp.class)).getContext().getResourceMap(LoadTestReport.class);
/*  67: 77 */     setTitle(resourceMap.getString("Form.title", new Object[0]));
/*  68: 78 */     setName("Form");
/*  69:    */     
/*  70: 80 */     this.closeButton.setText(resourceMap.getString("closeButton.text", new Object[0]));
/*  71: 81 */     this.closeButton.setName("closeButton");
/*  72: 82 */     this.closeButton.addActionListener(new ActionListener()
/*  73:    */     {
/*  74:    */       public void actionPerformed(ActionEvent evt)
/*  75:    */       {
/*  76: 84 */         LoadTestReport.this.closeButtonActionPerformed(evt);
/*  77:    */       }
/*  78: 87 */     });
/*  79: 88 */     this.printButton.setText(resourceMap.getString("printButton.text", new Object[0]));
/*  80: 89 */     this.printButton.setName("printButton");
/*  81: 90 */     this.printButton.addActionListener(new ActionListener()
/*  82:    */     {
/*  83:    */       public void actionPerformed(ActionEvent evt)
/*  84:    */       {
/*  85: 92 */         LoadTestReport.this.printButtonActionPerformed(evt);
/*  86:    */       }
/*  87: 95 */     });
/*  88: 96 */     this.copyButton.setText(resourceMap.getString("copyButton.text", new Object[0]));
/*  89: 97 */     this.copyButton.setName("copyButton");
/*  90: 98 */     this.copyButton.addActionListener(new ActionListener()
/*  91:    */     {
/*  92:    */       public void actionPerformed(ActionEvent evt)
/*  93:    */       {
/*  94:100 */         LoadTestReport.this.copyButtonActionPerformed(evt);
/*  95:    */       }
/*  96:103 */     });
/*  97:104 */     this.memberTableScroll.setName("memberTableScroll");
/*  98:    */     
/*  99:106 */     this.memberTable.setModel(new LoadTestReportTableModel(this.bridge));
/* 100:107 */     this.memberTable.setName("memberTable");
/* 101:108 */     this.memberTable.setRowSelectionAllowed(false);
/* 102:109 */     this.memberTable.getTableHeader().setReorderingAllowed(false);
/* 103:110 */     this.memberTableScroll.setViewportView(this.memberTable);
/* 104:    */     
/* 105:112 */     this.commentLabel.setHorizontalAlignment(4);
/* 106:113 */     this.commentLabel.setText(null);
/* 107:114 */     this.commentLabel.setName("commentLabel");
/* 108:    */     
/* 109:116 */     GroupLayout layout = new GroupLayout(getContentPane());
/* 110:117 */     getContentPane().setLayout(layout);
/* 111:118 */     layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(this.commentLabel, -1, 631, 32767).addGap(18, 18, 18).addComponent(this.copyButton).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(this.printButton).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(this.closeButton).addContainerGap()).addComponent(this.memberTableScroll, -1, 940, 32767));
/* 112:    */     
/* 113:    */ 
/* 114:    */ 
/* 115:    */ 
/* 116:    */ 
/* 117:    */ 
/* 118:    */ 
/* 119:    */ 
/* 120:    */ 
/* 121:    */ 
/* 122:    */ 
/* 123:    */ 
/* 124:131 */     layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(this.memberTableScroll, -1, 563, 32767).addGap(18, 18, 18).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.printButton).addComponent(this.copyButton).addComponent(this.closeButton).addComponent(this.commentLabel)).addContainerGap()));
/* 125:    */     
/* 126:    */ 
/* 127:    */ 
/* 128:    */ 
/* 129:    */ 
/* 130:    */ 
/* 131:    */ 
/* 132:    */ 
/* 133:    */ 
/* 134:    */ 
/* 135:    */ 
/* 136:    */ 
/* 137:144 */     pack();
/* 138:    */   }
/* 139:    */   
/* 140:    */   private void copyButtonActionPerformed(ActionEvent evt)
/* 141:    */   {
/* 142:148 */     Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
/* 143:149 */     StringSelection selection = new StringSelection(this.bridge.toTabDelimitedText());
/* 144:150 */     clipboard.setContents(selection, selection);
/* 145:151 */     this.commentLabel.setText(WPBDApp.getResourceMap(LoadTestReport.class).getString("copied.text", new Object[0]));
/* 146:    */   }
/* 147:    */   
/* 148:    */   private void closeButtonActionPerformed(ActionEvent evt)
/* 149:    */   {
/* 150:155 */     setVisible(false);
/* 151:    */   }
/* 152:    */   
/* 153:    */   private void printButtonActionPerformed(ActionEvent evt)
/* 154:    */   {
/* 155:159 */     PrinterUI.print(this, this.memberTable, LoadTestReport.class, this.bridge.getNotes());
/* 156:    */   }
/* 157:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.LoadTestReport
 * JD-Core Version:    0.7.0.1
 */