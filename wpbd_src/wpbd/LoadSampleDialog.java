/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.awt.Container;
/*   4:    */ import java.awt.Frame;
/*   5:    */ import java.awt.event.ActionEvent;
/*   6:    */ import java.awt.event.ActionListener;
/*   7:    */ import java.awt.event.MouseAdapter;
/*   8:    */ import java.awt.event.MouseEvent;
/*   9:    */ import javax.swing.BorderFactory;
/*  10:    */ import javax.swing.GroupLayout;
/*  11:    */ import javax.swing.GroupLayout.Alignment;
/*  12:    */ import javax.swing.GroupLayout.ParallelGroup;
/*  13:    */ import javax.swing.GroupLayout.SequentialGroup;
/*  14:    */ import javax.swing.JButton;
/*  15:    */ import javax.swing.JDialog;
/*  16:    */ import javax.swing.JLabel;
/*  17:    */ import javax.swing.JList;
/*  18:    */ import javax.swing.JPanel;
/*  19:    */ import javax.swing.JRootPane;
/*  20:    */ import javax.swing.JScrollPane;
/*  21:    */ import javax.swing.JTextPane;
/*  22:    */ import javax.swing.LayoutStyle.ComponentPlacement;
/*  23:    */ import javax.swing.event.ListSelectionEvent;
/*  24:    */ import javax.swing.event.ListSelectionListener;
/*  25:    */ import org.jdesktop.application.Application;
/*  26:    */ import org.jdesktop.application.ApplicationContext;
/*  27:    */ import org.jdesktop.application.ResourceMap;
/*  28:    */ 
/*  29:    */ public class LoadSampleDialog
/*  30:    */   extends JDialog
/*  31:    */ {
/*  32:    */   private boolean ok;
/*  33:    */   private final BridgeModel cartoonBridge;
/*  34:    */   private final BridgeView cartoonView;
/*  35:    */   private JButton cancelButton;
/*  36:    */   private JLabel cartoonLabel;
/*  37:    */   private JButton okButton;
/*  38:    */   private JLabel previewLabel;
/*  39:    */   private JList sampleList;
/*  40:    */   private JLabel selectSampleLabel;
/*  41:    */   private JScrollPane templateScroll;
/*  42:    */   private JTextPane tipTextPane;
/*  43:    */   private JPanel tipTextPanel;
/*  44:    */   
/*  45:    */   public LoadSampleDialog(Frame parent)
/*  46:    */   {
/*  47: 29 */     super(parent, true);
/*  48: 30 */     this.cartoonBridge = new BridgeModel();
/*  49: 31 */     this.cartoonView = new BridgeDraftingView(this.cartoonBridge);
/*  50: 32 */     initComponents();
/*  51: 33 */     getRootPane().setDefaultButton(this.okButton);
/*  52:    */   }
/*  53:    */   
/*  54:    */   public void setVisible(boolean visible)
/*  55:    */   {
/*  56: 43 */     if (visible)
/*  57:    */     {
/*  58: 44 */       this.ok = false;
/*  59: 45 */       this.sampleList.requestFocusInWindow();
/*  60:    */     }
/*  61: 47 */     super.setVisible(visible);
/*  62:    */   }
/*  63:    */   
/*  64:    */   public boolean isOk()
/*  65:    */   {
/*  66: 56 */     return this.ok;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public void initialize()
/*  70:    */   {
/*  71: 63 */     this.sampleList.setListData(BridgeSample.getList());
/*  72: 64 */     this.sampleList.setSelectedIndex(0);
/*  73:    */   }
/*  74:    */   
/*  75:    */   public void loadUsingSelectedSample(BridgeModel bridge)
/*  76:    */   {
/*  77: 73 */     Object val = this.sampleList.getSelectedValue();
/*  78: 74 */     if ((val instanceof BridgeSample)) {
/*  79: 75 */       bridge.read(((BridgeSample)val).getBridgeAsString());
/*  80:    */     }
/*  81:    */   }
/*  82:    */   
/*  83:    */   private void initComponents()
/*  84:    */   {
/*  85: 82 */     this.selectSampleLabel = new JLabel();
/*  86: 83 */     this.templateScroll = new JScrollPane();
/*  87: 84 */     this.sampleList = new JList();
/*  88: 85 */     this.previewLabel = new JLabel();
/*  89: 86 */     this.cartoonLabel = this.cartoonView.getDrawing(2.0D, null);
/*  90: 87 */     this.tipTextPanel = new JPanel();
/*  91: 88 */     this.tipTextPane = new TipTextPane();
/*  92: 89 */     this.okButton = new JButton();
/*  93: 90 */     this.cancelButton = new JButton();
/*  94:    */     
/*  95: 92 */     ResourceMap resourceMap = ((WPBDApp)Application.getInstance(WPBDApp.class)).getContext().getResourceMap(LoadSampleDialog.class);
/*  96: 93 */     setTitle(resourceMap.getString("Form.title", new Object[0]));
/*  97: 94 */     setName("Form");
/*  98: 95 */     setResizable(false);
/*  99:    */     
/* 100: 97 */     this.selectSampleLabel.setText(resourceMap.getString("selectSampleLabel.text", new Object[0]));
/* 101: 98 */     this.selectSampleLabel.setName("selectSampleLabel");
/* 102:    */     
/* 103:100 */     this.templateScroll.setName("templateScroll");
/* 104:    */     
/* 105:102 */     this.sampleList.setSelectionMode(0);
/* 106:103 */     this.sampleList.setName("sampleList");
/* 107:104 */     this.sampleList.addMouseListener(new MouseAdapter()
/* 108:    */     {
/* 109:    */       public void mouseClicked(MouseEvent evt)
/* 110:    */       {
/* 111:106 */         LoadSampleDialog.this.sampleListMouseClicked(evt);
/* 112:    */       }
/* 113:108 */     });
/* 114:109 */     this.sampleList.addListSelectionListener(new ListSelectionListener()
/* 115:    */     {
/* 116:    */       public void valueChanged(ListSelectionEvent evt)
/* 117:    */       {
/* 118:111 */         LoadSampleDialog.this.sampleListValueChanged(evt);
/* 119:    */       }
/* 120:113 */     });
/* 121:114 */     this.templateScroll.setViewportView(this.sampleList);
/* 122:    */     
/* 123:116 */     this.previewLabel.setText(resourceMap.getString("previewLabel.text", new Object[0]));
/* 124:117 */     this.previewLabel.setName("previewLabel");
/* 125:    */     
/* 126:119 */     this.cartoonLabel.setBackground(resourceMap.getColor("cartoonLabel.background"));
/* 127:120 */     this.cartoonLabel.setForeground(resourceMap.getColor("cartoonLabel.foreground"));
/* 128:121 */     this.cartoonLabel.setHorizontalAlignment(0);
/* 129:122 */     this.cartoonLabel.setText(resourceMap.getString("cartoonLabel.text", new Object[0]));
/* 130:123 */     this.cartoonLabel.setVerticalAlignment(1);
/* 131:124 */     this.cartoonLabel.setBorder(BorderFactory.createBevelBorder(1));
/* 132:125 */     this.cartoonLabel.setHorizontalTextPosition(0);
/* 133:126 */     this.cartoonLabel.setName("cartoonLabel");
/* 134:127 */     this.cartoonLabel.setOpaque(true);
/* 135:128 */     this.cartoonLabel.setVerticalTextPosition(1);
/* 136:    */     
/* 137:130 */     this.tipTextPanel.setBorder(BorderFactory.createTitledBorder("Design TIp:"));
/* 138:131 */     this.tipTextPanel.setName("tipTextPanel");
/* 139:    */     
/* 140:133 */     this.tipTextPane.setBorder(null);
/* 141:134 */     this.tipTextPane.setText(resourceMap.getString("tipTextPane.text", new Object[0]));
/* 142:135 */     this.tipTextPane.setName("tipTextPane");
/* 143:    */     
/* 144:137 */     GroupLayout tipTextPanelLayout = new GroupLayout(this.tipTextPanel);
/* 145:138 */     this.tipTextPanel.setLayout(tipTextPanelLayout);
/* 146:139 */     tipTextPanelLayout.setHorizontalGroup(tipTextPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(tipTextPanelLayout.createSequentialGroup().addContainerGap().addComponent(this.tipTextPane, -1, 81, 32767).addContainerGap()));
/* 147:    */     
/* 148:    */ 
/* 149:    */ 
/* 150:    */ 
/* 151:    */ 
/* 152:    */ 
/* 153:146 */     tipTextPanelLayout.setVerticalGroup(tipTextPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, tipTextPanelLayout.createSequentialGroup().addComponent(this.tipTextPane, -1, 323, 32767).addContainerGap()));
/* 154:    */     
/* 155:    */ 
/* 156:    */ 
/* 157:    */ 
/* 158:    */ 
/* 159:    */ 
/* 160:153 */     this.okButton.setText(resourceMap.getString("okButton.text", new Object[0]));
/* 161:154 */     this.okButton.setName("okButton");
/* 162:155 */     this.okButton.addActionListener(new ActionListener()
/* 163:    */     {
/* 164:    */       public void actionPerformed(ActionEvent evt)
/* 165:    */       {
/* 166:157 */         LoadSampleDialog.this.okButtonActionPerformed(evt);
/* 167:    */       }
/* 168:160 */     });
/* 169:161 */     this.cancelButton.setText(resourceMap.getString("cancelButton.text", new Object[0]));
/* 170:162 */     this.cancelButton.setName("cancelButton");
/* 171:163 */     this.cancelButton.addActionListener(new ActionListener()
/* 172:    */     {
/* 173:    */       public void actionPerformed(ActionEvent evt)
/* 174:    */       {
/* 175:165 */         LoadSampleDialog.this.cancelButtonActionPerformed(evt);
/* 176:    */       }
/* 177:168 */     });
/* 178:169 */     GroupLayout layout = new GroupLayout(getContentPane());
/* 179:170 */     getContentPane().setLayout(layout);
/* 180:171 */     layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.selectSampleLabel).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.previewLabel).addComponent(this.templateScroll, -2, 431, -2).addComponent(this.cartoonLabel, -2, 430, -2)).addGap(7, 7, 7).addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false).addComponent(this.tipTextPanel, -2, -1, -2).addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addGap(10, 10, 10).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.okButton, GroupLayout.Alignment.TRAILING, -1, 107, 32767).addComponent(this.cancelButton, -1, -1, 32767)))))).addContainerGap()));
/* 181:    */     
/* 182:    */ 
/* 183:    */ 
/* 184:    */ 
/* 185:    */ 
/* 186:    */ 
/* 187:    */ 
/* 188:    */ 
/* 189:    */ 
/* 190:    */ 
/* 191:    */ 
/* 192:    */ 
/* 193:    */ 
/* 194:    */ 
/* 195:    */ 
/* 196:    */ 
/* 197:    */ 
/* 198:    */ 
/* 199:    */ 
/* 200:    */ 
/* 201:192 */     layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(this.selectSampleLabel).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(this.tipTextPanel, -1, -1, 32767).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.cancelButton).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.okButton)).addGroup(layout.createSequentialGroup().addComponent(this.templateScroll, -2, -1, -2).addGap(18, 18, 18).addComponent(this.previewLabel).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.cartoonLabel, -1, 255, 32767))).addContainerGap()));
/* 202:    */     
/* 203:    */ 
/* 204:    */ 
/* 205:    */ 
/* 206:    */ 
/* 207:    */ 
/* 208:    */ 
/* 209:    */ 
/* 210:    */ 
/* 211:    */ 
/* 212:    */ 
/* 213:    */ 
/* 214:    */ 
/* 215:    */ 
/* 216:    */ 
/* 217:    */ 
/* 218:    */ 
/* 219:    */ 
/* 220:    */ 
/* 221:    */ 
/* 222:    */ 
/* 223:214 */     pack();
/* 224:    */   }
/* 225:    */   
/* 226:    */   private void sampleListMouseClicked(MouseEvent evt)
/* 227:    */   {
/* 228:218 */     if (evt.getClickCount() == 2)
/* 229:    */     {
/* 230:219 */       this.ok = true;
/* 231:220 */       setVisible(false);
/* 232:    */     }
/* 233:    */   }
/* 234:    */   
/* 235:    */   private void sampleListValueChanged(ListSelectionEvent evt)
/* 236:    */   {
/* 237:225 */     loadUsingSelectedSample(this.cartoonBridge);
/* 238:226 */     this.cartoonView.initialize(this.cartoonBridge.getDesignConditions());
/* 239:227 */     this.cartoonLabel.repaint();
/* 240:    */   }
/* 241:    */   
/* 242:    */   private void okButtonActionPerformed(ActionEvent evt)
/* 243:    */   {
/* 244:231 */     this.ok = true;
/* 245:232 */     setVisible(false);
/* 246:    */   }
/* 247:    */   
/* 248:    */   private void cancelButtonActionPerformed(ActionEvent evt)
/* 249:    */   {
/* 250:236 */     setVisible(false);
/* 251:    */   }
/* 252:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.LoadSampleDialog
 * JD-Core Version:    0.7.0.1
 */