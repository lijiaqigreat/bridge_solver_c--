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
/*  29:    */ public class LoadTemplateDialog
/*  30:    */   extends JDialog
/*  31:    */ {
/*  32:    */   private boolean ok;
/*  33: 28 */   private final BridgeCartoonView bridgeCartoonView = new BridgeCartoonView();
/*  34:    */   private JButton cancelButton;
/*  35:    */   private JLabel cartoonLabel;
/*  36:    */   private JButton okButton;
/*  37:    */   private JLabel previewLabel;
/*  38:    */   private JList templateList;
/*  39:    */   private JLabel templateListLabel;
/*  40:    */   private JScrollPane templateScroll;
/*  41:    */   private JTextPane tipTextPane;
/*  42:    */   private JPanel tipTextPanel;
/*  43:    */   
/*  44:    */   public LoadTemplateDialog(Frame parent)
/*  45:    */   {
/*  46: 36 */     super(parent, true);
/*  47: 37 */     initComponents();
/*  48: 38 */     getRootPane().setDefaultButton(this.okButton);
/*  49:    */   }
/*  50:    */   
/*  51:    */   public void setVisible(boolean visible)
/*  52:    */   {
/*  53: 48 */     if (visible)
/*  54:    */     {
/*  55: 49 */       this.ok = false;
/*  56: 50 */       this.templateList.requestFocusInWindow();
/*  57:    */     }
/*  58: 52 */     super.setVisible(visible);
/*  59:    */   }
/*  60:    */   
/*  61:    */   public boolean isOk()
/*  62:    */   {
/*  63: 61 */     return this.ok;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public BridgeSketchModel getSketchModel()
/*  67:    */   {
/*  68: 70 */     return this.bridgeCartoonView.getBridgeSketchView().getModel();
/*  69:    */   }
/*  70:    */   
/*  71:    */   public void initialize(DesignConditions conditions, BridgeSketchModel toSelect)
/*  72:    */   {
/*  73: 80 */     this.templateList.setListData(BridgeSketchModel.getList(conditions));
/*  74: 81 */     if (toSelect == null) {
/*  75: 82 */       this.templateList.setSelectedIndex(0);
/*  76:    */     } else {
/*  77: 85 */       this.templateList.setSelectedValue(toSelect, true);
/*  78:    */     }
/*  79: 87 */     this.bridgeCartoonView.initialize(conditions);
/*  80:    */   }
/*  81:    */   
/*  82:    */   private void initComponents()
/*  83:    */   {
/*  84: 99 */     this.templateListLabel = new JLabel();
/*  85:100 */     this.templateScroll = new JScrollPane();
/*  86:101 */     this.templateList = new JList();
/*  87:102 */     this.previewLabel = new JLabel();
/*  88:103 */     this.cartoonLabel = this.bridgeCartoonView.getDrawing(1.0D);
/*  89:104 */     this.tipTextPanel = new JPanel();
/*  90:105 */     this.tipTextPane = new TipTextPane();
/*  91:106 */     this.cancelButton = new JButton();
/*  92:107 */     this.okButton = new JButton();
/*  93:    */     
/*  94:109 */     ResourceMap resourceMap = ((WPBDApp)Application.getInstance(WPBDApp.class)).getContext().getResourceMap(LoadTemplateDialog.class);
/*  95:110 */     setTitle(resourceMap.getString("Form.title", new Object[0]));
/*  96:111 */     setName("Form");
/*  97:112 */     setResizable(false);
/*  98:    */     
/*  99:114 */     this.templateListLabel.setText(resourceMap.getString("templateListLabel.text", new Object[0]));
/* 100:115 */     this.templateListLabel.setName("templateListLabel");
/* 101:    */     
/* 102:117 */     this.templateScroll.setName("templateScroll");
/* 103:    */     
/* 104:119 */     this.templateList.setSelectionMode(0);
/* 105:120 */     this.templateList.setName("templateList");
/* 106:121 */     this.templateList.addMouseListener(new MouseAdapter()
/* 107:    */     {
/* 108:    */       public void mouseClicked(MouseEvent evt)
/* 109:    */       {
/* 110:123 */         LoadTemplateDialog.this.templateListMouseClicked(evt);
/* 111:    */       }
/* 112:125 */     });
/* 113:126 */     this.templateList.addListSelectionListener(new ListSelectionListener()
/* 114:    */     {
/* 115:    */       public void valueChanged(ListSelectionEvent evt)
/* 116:    */       {
/* 117:128 */         LoadTemplateDialog.this.templateListValueChanged(evt);
/* 118:    */       }
/* 119:130 */     });
/* 120:131 */     this.templateScroll.setViewportView(this.templateList);
/* 121:    */     
/* 122:133 */     this.previewLabel.setText(resourceMap.getString("previewLabel.text", new Object[0]));
/* 123:134 */     this.previewLabel.setName("previewLabel");
/* 124:    */     
/* 125:136 */     this.cartoonLabel.setBackground(resourceMap.getColor("cartoonLabel.background"));
/* 126:137 */     this.cartoonLabel.setForeground(resourceMap.getColor("cartoonLabel.foreground"));
/* 127:138 */     this.cartoonLabel.setHorizontalAlignment(0);
/* 128:139 */     this.cartoonLabel.setText(resourceMap.getString("cartoonLabel.text", new Object[0]));
/* 129:140 */     this.cartoonLabel.setVerticalAlignment(1);
/* 130:141 */     this.cartoonLabel.setBorder(BorderFactory.createBevelBorder(1));
/* 131:142 */     this.cartoonLabel.setHorizontalTextPosition(0);
/* 132:143 */     this.cartoonLabel.setName("cartoonLabel");
/* 133:144 */     this.cartoonLabel.setOpaque(true);
/* 134:145 */     this.cartoonLabel.setVerticalTextPosition(1);
/* 135:    */     
/* 136:147 */     this.tipTextPanel.setBorder(BorderFactory.createTitledBorder(resourceMap.getString("tipTextPanel.border.title", new Object[0])));
/* 137:148 */     this.tipTextPanel.setName("tipTextPanel");
/* 138:    */     
/* 139:150 */     this.tipTextPane.setBorder(null);
/* 140:151 */     this.tipTextPane.setText(resourceMap.getString("tipTextPane.text", new Object[0]));
/* 141:152 */     this.tipTextPane.setName("tipTextPane");
/* 142:    */     
/* 143:154 */     GroupLayout tipTextPanelLayout = new GroupLayout(this.tipTextPanel);
/* 144:155 */     this.tipTextPanel.setLayout(tipTextPanelLayout);
/* 145:156 */     tipTextPanelLayout.setHorizontalGroup(tipTextPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(tipTextPanelLayout.createSequentialGroup().addContainerGap().addComponent(this.tipTextPane, -1, 80, 32767).addContainerGap()));
/* 146:    */     
/* 147:    */ 
/* 148:    */ 
/* 149:    */ 
/* 150:    */ 
/* 151:    */ 
/* 152:163 */     tipTextPanelLayout.setVerticalGroup(tipTextPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, tipTextPanelLayout.createSequentialGroup().addComponent(this.tipTextPane, -1, 323, 32767).addContainerGap()));
/* 153:    */     
/* 154:    */ 
/* 155:    */ 
/* 156:    */ 
/* 157:    */ 
/* 158:    */ 
/* 159:170 */     this.cancelButton.setText(resourceMap.getString("cancelButton.text", new Object[0]));
/* 160:171 */     this.cancelButton.setName("cancelButton");
/* 161:172 */     this.cancelButton.addActionListener(new ActionListener()
/* 162:    */     {
/* 163:    */       public void actionPerformed(ActionEvent evt)
/* 164:    */       {
/* 165:174 */         LoadTemplateDialog.this.cancelButtonActionPerformed(evt);
/* 166:    */       }
/* 167:177 */     });
/* 168:178 */     this.okButton.setText(resourceMap.getString("okButton.text", new Object[0]));
/* 169:179 */     this.okButton.setName("okButton");
/* 170:180 */     this.okButton.addActionListener(new ActionListener()
/* 171:    */     {
/* 172:    */       public void actionPerformed(ActionEvent evt)
/* 173:    */       {
/* 174:182 */         LoadTemplateDialog.this.okButtonActionPerformed(evt);
/* 175:    */       }
/* 176:185 */     });
/* 177:186 */     GroupLayout layout = new GroupLayout(getContentPane());
/* 178:187 */     getContentPane().setLayout(layout);
/* 179:188 */     layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.cartoonLabel, -1, 431, 32767).addComponent(this.templateListLabel, -2, 122, -2).addComponent(this.previewLabel).addComponent(this.templateScroll, -1, 431, 32767)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(this.tipTextPanel, -2, -1, -2).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false).addComponent(this.cancelButton, GroupLayout.Alignment.TRAILING, -1, -1, 32767).addComponent(this.okButton, GroupLayout.Alignment.TRAILING, -1, 111, 32767))).addContainerGap()));
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
/* 190:    */ 
/* 191:    */ 
/* 192:    */ 
/* 193:    */ 
/* 194:    */ 
/* 195:    */ 
/* 196:205 */     layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(this.templateListLabel).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING).addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addComponent(this.templateScroll, -2, -1, -2).addGap(18, 18, 18).addComponent(this.previewLabel).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.cartoonLabel, -1, 255, 32767)).addGroup(layout.createSequentialGroup().addComponent(this.tipTextPanel, -1, -1, 32767).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.cancelButton).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.okButton))).addContainerGap()));
/* 197:    */     
/* 198:    */ 
/* 199:    */ 
/* 200:    */ 
/* 201:    */ 
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
/* 218:227 */     pack();
/* 219:    */   }
/* 220:    */   
/* 221:    */   private void cancelButtonActionPerformed(ActionEvent evt)
/* 222:    */   {
/* 223:231 */     setVisible(false);
/* 224:    */   }
/* 225:    */   
/* 226:    */   private void okButtonActionPerformed(ActionEvent evt)
/* 227:    */   {
/* 228:235 */     this.ok = true;
/* 229:236 */     setVisible(false);
/* 230:    */   }
/* 231:    */   
/* 232:    */   private void templateListValueChanged(ListSelectionEvent evt)
/* 233:    */   {
/* 234:240 */     this.bridgeCartoonView.getBridgeSketchView().setModel(this.templateList.getSelectedIndex() == 0 ? null : (BridgeSketchModel)this.templateList.getSelectedValue());
/* 235:241 */     this.cartoonLabel.repaint();
/* 236:    */   }
/* 237:    */   
/* 238:    */   private void templateListMouseClicked(MouseEvent evt)
/* 239:    */   {
/* 240:245 */     if (evt.getClickCount() == 2)
/* 241:    */     {
/* 242:246 */       this.ok = true;
/* 243:247 */       setVisible(false);
/* 244:    */     }
/* 245:    */   }
/* 246:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.LoadTemplateDialog
 * JD-Core Version:    0.7.0.1
 */