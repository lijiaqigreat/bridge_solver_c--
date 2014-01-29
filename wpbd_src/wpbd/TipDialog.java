/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.awt.Container;
/*   4:    */ import java.awt.Frame;
/*   5:    */ import java.awt.Insets;
/*   6:    */ import java.awt.event.ActionEvent;
/*   7:    */ import java.awt.event.ActionListener;
/*   8:    */ import java.util.ArrayList;
/*   9:    */ import java.util.Arrays;
/*  10:    */ import java.util.Comparator;
/*  11:    */ import java.util.Iterator;
/*  12:    */ import java.util.Set;
/*  13:    */ import javax.swing.GroupLayout;
/*  14:    */ import javax.swing.GroupLayout.Alignment;
/*  15:    */ import javax.swing.GroupLayout.ParallelGroup;
/*  16:    */ import javax.swing.GroupLayout.SequentialGroup;
/*  17:    */ import javax.swing.JButton;
/*  18:    */ import javax.swing.JCheckBox;
/*  19:    */ import javax.swing.JDialog;
/*  20:    */ import javax.swing.JOptionPane;
/*  21:    */ import javax.swing.JRootPane;
/*  22:    */ import javax.swing.JScrollPane;
/*  23:    */ import javax.swing.JTextPane;
/*  24:    */ import javax.swing.LayoutStyle.ComponentPlacement;
/*  25:    */ import org.jdesktop.application.Application;
/*  26:    */ import org.jdesktop.application.ApplicationContext;
/*  27:    */ import org.jdesktop.application.ResourceMap;
/*  28:    */ 
/*  29:    */ public class TipDialog
/*  30:    */   extends JDialog
/*  31:    */ {
/*  32:    */   private static String[] index;
/*  33: 36 */   private int n = -1;
/*  34: 37 */   private boolean showOnStart = true;
/*  35:    */   private static final String tipNumberStorageName = "tipNumber.xml";
/*  36:    */   private static final String showOnStartStorageName = "showOnStartState.xml";
/*  37:    */   private JButton backTipButton;
/*  38:    */   private JButton closeButton;
/*  39:    */   private JButton nextTipButton;
/*  40:    */   private JCheckBox showTipsCheck;
/*  41:    */   private JTextPane tipPane;
/*  42:    */   private JScrollPane tipScroll;
/*  43:    */   
/*  44:    */   public TipDialog(Frame parent)
/*  45:    */   {
/*  46: 45 */     super(parent, true);
/*  47: 46 */     initComponents();
/*  48: 47 */     getRootPane().setDefaultButton(this.closeButton);
/*  49: 48 */     this.tipPane.setOpaque(true);
/*  50:    */   }
/*  51:    */   
/*  52:    */   public void showTip(boolean startup, int inc)
/*  53:    */   {
/*  54: 58 */     ResourceMap resourceMap = WPBDApp.getResourceMap(TipDialog.class);
/*  55: 59 */     Object obj = WPBDApp.loadFromLocalStorage("showOnStartState.xml");
/*  56: 60 */     if (obj != null) {
/*  57: 61 */       this.showOnStart = ((Boolean)obj).booleanValue();
/*  58:    */     }
/*  59: 63 */     if ((startup) && (!this.showOnStart)) {
/*  60: 64 */       return;
/*  61:    */     }
/*  62: 66 */     if (index == null)
/*  63:    */     {
/*  64: 67 */       ArrayList<String> tipKeys = new ArrayList();
/*  65: 68 */       Iterator<String> i = resourceMap.keySet().iterator();
/*  66: 69 */       while (i.hasNext())
/*  67:    */       {
/*  68: 70 */         String nameKey = (String)i.next();
/*  69: 71 */         if (nameKey.endsWith(".tipText")) {
/*  70: 72 */           tipKeys.add(nameKey);
/*  71:    */         }
/*  72:    */       }
/*  73: 75 */       index = (String[])tipKeys.toArray(new String[tipKeys.size()]);
/*  74: 76 */       Arrays.sort(index, new Comparator()
/*  75:    */       {
/*  76:    */         public int compare(String a, String b)
/*  77:    */         {
/*  78: 78 */           return a.compareTo(b);
/*  79:    */         }
/*  80:    */       });
/*  81:    */     }
/*  82: 82 */     obj = WPBDApp.loadFromLocalStorage("tipNumber.xml");
/*  83: 83 */     if (obj != null) {
/*  84: 84 */       this.n = ((Integer)obj).intValue();
/*  85:    */     }
/*  86: 86 */     this.n = ((this.n + inc) % index.length);
/*  87: 87 */     if (this.n < 0) {
/*  88: 88 */       this.n += index.length;
/*  89:    */     }
/*  90: 90 */     WPBDApp.saveToLocalStorage(new Integer(this.n), "tipNumber.xml");
/*  91: 91 */     this.tipPane.setText(resourceMap.getString("tipTemplate.text", new Object[] { resourceMap.getString(index[this.n], new Object[0]) }));
/*  92: 92 */     if (!isVisible())
/*  93:    */     {
/*  94: 93 */       this.showTipsCheck.setSelected(this.showOnStart);
/*  95: 94 */       setVisible(true);
/*  96:    */     }
/*  97:    */   }
/*  98:    */   
/*  99:    */   private void initComponents()
/* 100:    */   {
/* 101:102 */     this.showTipsCheck = new JCheckBox();
/* 102:103 */     this.closeButton = new JButton();
/* 103:104 */     this.nextTipButton = new JButton();
/* 104:105 */     this.backTipButton = new JButton();
/* 105:106 */     this.tipScroll = new JScrollPane();
/* 106:107 */     this.tipPane = new TipTextPane();
/* 107:    */     
/* 108:109 */     ResourceMap resourceMap = ((WPBDApp)Application.getInstance(WPBDApp.class)).getContext().getResourceMap(TipDialog.class);
/* 109:110 */     setTitle(resourceMap.getString("TipForm.title", new Object[0]));
/* 110:111 */     setLocationByPlatform(true);
/* 111:112 */     setModal(true);
/* 112:113 */     setName("TipForm");
/* 113:114 */     setResizable(false);
/* 114:    */     
/* 115:116 */     this.showTipsCheck.setText(resourceMap.getString("showTipsCheck.text", new Object[0]));
/* 116:117 */     this.showTipsCheck.setToolTipText(resourceMap.getString("showTipsCheck.toolTipText", new Object[0]));
/* 117:118 */     this.showTipsCheck.setName("showTipsCheck");
/* 118:119 */     this.showTipsCheck.addActionListener(new ActionListener()
/* 119:    */     {
/* 120:    */       public void actionPerformed(ActionEvent evt)
/* 121:    */       {
/* 122:121 */         TipDialog.this.showTipsCheckActionPerformed(evt);
/* 123:    */       }
/* 124:124 */     });
/* 125:125 */     this.closeButton.setText(resourceMap.getString("closeButton.text", new Object[0]));
/* 126:126 */     this.closeButton.setName("closeButton");
/* 127:127 */     this.closeButton.addActionListener(new ActionListener()
/* 128:    */     {
/* 129:    */       public void actionPerformed(ActionEvent evt)
/* 130:    */       {
/* 131:129 */         TipDialog.this.closeButtonActionPerformed(evt);
/* 132:    */       }
/* 133:132 */     });
/* 134:133 */     this.nextTipButton.setText(resourceMap.getString("nextTipButton.text", new Object[0]));
/* 135:134 */     this.nextTipButton.setName("nextTipButton");
/* 136:135 */     this.nextTipButton.addActionListener(new ActionListener()
/* 137:    */     {
/* 138:    */       public void actionPerformed(ActionEvent evt)
/* 139:    */       {
/* 140:137 */         TipDialog.this.nextTipButtonActionPerformed(evt);
/* 141:    */       }
/* 142:140 */     });
/* 143:141 */     this.backTipButton.setText(resourceMap.getString("backTipButton.text", new Object[0]));
/* 144:142 */     this.backTipButton.setName("backTipButton");
/* 145:143 */     this.backTipButton.addActionListener(new ActionListener()
/* 146:    */     {
/* 147:    */       public void actionPerformed(ActionEvent evt)
/* 148:    */       {
/* 149:145 */         TipDialog.this.backTipButtonActionPerformed(evt);
/* 150:    */       }
/* 151:148 */     });
/* 152:149 */     this.tipScroll.setName("tipScroll");
/* 153:    */     
/* 154:151 */     this.tipPane.setMargin(new Insets(16, 16, 16, 16));
/* 155:152 */     this.tipPane.setName("tipPane");
/* 156:153 */     this.tipScroll.setViewportView(this.tipPane);
/* 157:    */     
/* 158:155 */     GroupLayout layout = new GroupLayout(getContentPane());
/* 159:156 */     getContentPane().setLayout(layout);
/* 160:157 */     layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING).addGroup(layout.createSequentialGroup().addComponent(this.showTipsCheck).addGap(18, 18, 18).addComponent(this.backTipButton).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.nextTipButton).addGap(12, 12, 12).addComponent(this.closeButton)).addComponent(this.tipScroll, -1, 413, 32767)).addContainerGap()));
/* 161:    */     
/* 162:    */ 
/* 163:    */ 
/* 164:    */ 
/* 165:    */ 
/* 166:    */ 
/* 167:    */ 
/* 168:    */ 
/* 169:    */ 
/* 170:    */ 
/* 171:    */ 
/* 172:    */ 
/* 173:    */ 
/* 174:    */ 
/* 175:    */ 
/* 176:173 */     layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(this.tipScroll, -2, 144, -2).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.backTipButton).addComponent(this.nextTipButton).addComponent(this.closeButton).addComponent(this.showTipsCheck)).addContainerGap(-1, 32767)));
/* 177:    */     
/* 178:    */ 
/* 179:    */ 
/* 180:    */ 
/* 181:    */ 
/* 182:    */ 
/* 183:    */ 
/* 184:    */ 
/* 185:    */ 
/* 186:    */ 
/* 187:    */ 
/* 188:    */ 
/* 189:    */ 
/* 190:187 */     pack();
/* 191:    */   }
/* 192:    */   
/* 193:    */   private void closeButtonActionPerformed(ActionEvent evt)
/* 194:    */   {
/* 195:191 */     setVisible(false);
/* 196:    */   }
/* 197:    */   
/* 198:    */   private void nextTipButtonActionPerformed(ActionEvent evt)
/* 199:    */   {
/* 200:195 */     showTip(false, 1);
/* 201:    */   }
/* 202:    */   
/* 203:    */   private void showTipsCheckActionPerformed(ActionEvent evt)
/* 204:    */   {
/* 205:199 */     this.showOnStart = this.showTipsCheck.isSelected();
/* 206:200 */     if (!WPBDApp.saveToLocalStorage(Boolean.valueOf(this.showOnStart), "showOnStartState.xml")) {
/* 207:201 */       JOptionPane.showMessageDialog(null, "Can't save show on start checkbox state. It will remain the same next time!", "Design Tip of the Day", 2);
/* 208:    */     }
/* 209:    */   }
/* 210:    */   
/* 211:    */   private void backTipButtonActionPerformed(ActionEvent evt)
/* 212:    */   {
/* 213:208 */     showTip(false, -1);
/* 214:    */   }
/* 215:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.TipDialog
 * JD-Core Version:    0.7.0.1
 */