/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.awt.Container;
/*   4:    */ import java.awt.Frame;
/*   5:    */ import java.awt.event.ActionEvent;
/*   6:    */ import java.awt.event.ActionListener;
/*   7:    */ import java.awt.event.MouseAdapter;
/*   8:    */ import java.awt.event.MouseEvent;
/*   9:    */ import javax.swing.BorderFactory;
/*  10:    */ import javax.swing.ButtonGroup;
/*  11:    */ import javax.swing.GroupLayout;
/*  12:    */ import javax.swing.GroupLayout.Alignment;
/*  13:    */ import javax.swing.GroupLayout.ParallelGroup;
/*  14:    */ import javax.swing.GroupLayout.SequentialGroup;
/*  15:    */ import javax.swing.JButton;
/*  16:    */ import javax.swing.JDialog;
/*  17:    */ import javax.swing.JLabel;
/*  18:    */ import javax.swing.JPanel;
/*  19:    */ import javax.swing.JRadioButton;
/*  20:    */ import javax.swing.JRootPane;
/*  21:    */ import javax.swing.LayoutStyle.ComponentPlacement;
/*  22:    */ import org.jdesktop.application.Application;
/*  23:    */ import org.jdesktop.application.ApplicationContext;
/*  24:    */ import org.jdesktop.application.ResourceMap;
/*  25:    */ 
/*  26:    */ public class WelcomeDialog
/*  27:    */   extends JDialog
/*  28:    */ {
/*  29:    */   public static final int CANCEL = 0;
/*  30:    */   public static final int CREATE_NEW = 1;
/*  31:    */   public static final int LOAD_SAMPLE = 2;
/*  32:    */   public static final int LOAD = 3;
/*  33:    */   private int result;
/*  34:    */   private AboutProvider aboutProvider;
/*  35:    */   private JButton aboutButton;
/*  36:    */   private JLabel bannerLabel;
/*  37:    */   private JButton cancelButton;
/*  38:    */   private JRadioButton createButton;
/*  39:    */   private JLabel createIcon;
/*  40:    */   private ButtonGroup createLoadSampleGroup;
/*  41:    */   private JPanel createLoadSamplePanel;
/*  42:    */   private JRadioButton loadButton;
/*  43:    */   private JLabel loadIcon;
/*  44:    */   private JPanel loadPanel;
/*  45:    */   private JRadioButton loadSampleButton;
/*  46:    */   private JLabel loadSampleIcon;
/*  47:    */   private JLabel motifLabel;
/*  48:    */   private JButton okButton;
/*  49:    */   private JLabel tipLabel;
/*  50:    */   private JPanel tipPanel;
/*  51:    */   
/*  52:    */   public WelcomeDialog(Frame parent, AboutProvider aboutProvider)
/*  53:    */   {
/*  54: 57 */     super(parent, true);
/*  55: 58 */     this.aboutProvider = aboutProvider;
/*  56: 59 */     initComponents();
/*  57: 60 */     getRootPane().setDefaultButton(this.okButton);
/*  58:    */   }
/*  59:    */   
/*  60:    */   public int getResult()
/*  61:    */   {
/*  62: 70 */     return this.result;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public void setVisible(boolean b)
/*  66:    */   {
/*  67: 80 */     if (b) {
/*  68: 81 */       this.result = 0;
/*  69:    */     }
/*  70: 83 */     super.setVisible(b);
/*  71:    */   }
/*  72:    */   
/*  73:    */   private void initComponents()
/*  74:    */   {
/*  75: 90 */     this.createLoadSampleGroup = new ButtonGroup();
/*  76: 91 */     this.motifLabel = new JLabel();
/*  77: 92 */     this.aboutButton = new JButton();
/*  78: 93 */     this.cancelButton = new JButton();
/*  79: 94 */     this.okButton = new JButton();
/*  80: 95 */     this.bannerLabel = new JLabel();
/*  81: 96 */     this.createLoadSamplePanel = new JPanel();
/*  82: 97 */     this.createButton = new JRadioButton();
/*  83: 98 */     this.loadSampleButton = new JRadioButton();
/*  84: 99 */     this.createIcon = new JLabel();
/*  85:100 */     this.loadSampleIcon = new JLabel();
/*  86:101 */     this.loadPanel = new JPanel();
/*  87:102 */     this.loadIcon = new JLabel();
/*  88:103 */     this.loadButton = new JRadioButton();
/*  89:104 */     this.tipPanel = new JPanel();
/*  90:105 */     this.tipLabel = new JLabel();
/*  91:    */     
/*  92:107 */     ResourceMap resourceMap = ((WPBDApp)Application.getInstance(WPBDApp.class)).getContext().getResourceMap(WelcomeDialog.class);
/*  93:108 */     setTitle(resourceMap.getString("Form.title", new Object[0]));
/*  94:109 */     setName("Form");
/*  95:110 */     setResizable(false);
/*  96:    */     
/*  97:112 */     this.motifLabel.setIcon(resourceMap.getIcon("motifLabel.icon"));
/*  98:113 */     this.motifLabel.setBorder(BorderFactory.createBevelBorder(1));
/*  99:114 */     this.motifLabel.setName("motifLabel");
/* 100:    */     
/* 101:116 */     this.aboutButton.setText(resourceMap.getString("aboutButton.text", new Object[0]));
/* 102:117 */     this.aboutButton.setName("aboutButton");
/* 103:118 */     this.aboutButton.addActionListener(new ActionListener()
/* 104:    */     {
/* 105:    */       public void actionPerformed(ActionEvent evt)
/* 106:    */       {
/* 107:120 */         WelcomeDialog.this.aboutButtonActionPerformed(evt);
/* 108:    */       }
/* 109:123 */     });
/* 110:124 */     this.cancelButton.setText(resourceMap.getString("cancelButton.text", new Object[0]));
/* 111:125 */     this.cancelButton.setName("cancelButton");
/* 112:126 */     this.cancelButton.addActionListener(new ActionListener()
/* 113:    */     {
/* 114:    */       public void actionPerformed(ActionEvent evt)
/* 115:    */       {
/* 116:128 */         WelcomeDialog.this.cancelButtonActionPerformed(evt);
/* 117:    */       }
/* 118:131 */     });
/* 119:132 */     this.okButton.setText(resourceMap.getString("okButton.text", new Object[0]));
/* 120:133 */     this.okButton.setName("okButton");
/* 121:134 */     this.okButton.addActionListener(new ActionListener()
/* 122:    */     {
/* 123:    */       public void actionPerformed(ActionEvent evt)
/* 124:    */       {
/* 125:136 */         WelcomeDialog.this.okButtonActionPerformed(evt);
/* 126:    */       }
/* 127:139 */     });
/* 128:140 */     this.bannerLabel.setIcon(resourceMap.getIcon("bannerLabel.icon"));
/* 129:141 */     this.bannerLabel.setFocusable(false);
/* 130:142 */     this.bannerLabel.setName("bannerLabel");
/* 131:    */     
/* 132:144 */     this.createLoadSamplePanel.setBorder(BorderFactory.createEtchedBorder());
/* 133:145 */     this.createLoadSamplePanel.setName("createLoadSamplePanel");
/* 134:    */     
/* 135:147 */     this.createLoadSampleGroup.add(this.createButton);
/* 136:148 */     this.createButton.setSelected(true);
/* 137:149 */     this.createButton.setText(resourceMap.getString("createButton.text", new Object[0]));
/* 138:150 */     this.createButton.setName("createButton");
/* 139:151 */     this.createButton.addMouseListener(new MouseAdapter()
/* 140:    */     {
/* 141:    */       public void mouseClicked(MouseEvent evt)
/* 142:    */       {
/* 143:153 */         WelcomeDialog.this.createButtonMouseClicked(evt);
/* 144:    */       }
/* 145:156 */     });
/* 146:157 */     this.createLoadSampleGroup.add(this.loadSampleButton);
/* 147:158 */     this.loadSampleButton.setText(resourceMap.getString("loadSampleButton.text", new Object[0]));
/* 148:159 */     this.loadSampleButton.setName("loadSampleButton");
/* 149:160 */     this.loadSampleButton.addMouseListener(new MouseAdapter()
/* 150:    */     {
/* 151:    */       public void mouseClicked(MouseEvent evt)
/* 152:    */       {
/* 153:162 */         WelcomeDialog.this.loadSampleButtonMouseClicked(evt);
/* 154:    */       }
/* 155:165 */     });
/* 156:166 */     this.createIcon.setIcon(resourceMap.getIcon("createIcon.icon"));
/* 157:167 */     this.createIcon.setName("createIcon");
/* 158:    */     
/* 159:169 */     this.loadSampleIcon.setIcon(resourceMap.getIcon("loadSampleIcon.icon"));
/* 160:170 */     this.loadSampleIcon.setName("loadSampleIcon");
/* 161:    */     
/* 162:172 */     GroupLayout createLoadSamplePanelLayout = new GroupLayout(this.createLoadSamplePanel);
/* 163:173 */     this.createLoadSamplePanel.setLayout(createLoadSamplePanelLayout);
/* 164:174 */     createLoadSamplePanelLayout.setHorizontalGroup(createLoadSamplePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(createLoadSamplePanelLayout.createSequentialGroup().addContainerGap().addGroup(createLoadSamplePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.createIcon).addComponent(this.loadSampleIcon)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(createLoadSamplePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.createButton).addComponent(this.loadSampleButton)).addGap(53, 53, 53)));
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
/* 176:    */ 
/* 177:187 */     createLoadSamplePanelLayout.setVerticalGroup(createLoadSamplePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, createLoadSamplePanelLayout.createSequentialGroup().addContainerGap(14, 32767).addGroup(createLoadSamplePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.createIcon).addComponent(this.createButton)).addGap(5, 5, 5).addGroup(createLoadSamplePanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(this.loadSampleIcon).addComponent(this.loadSampleButton)).addGap(13, 13, 13)));
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
/* 190:    */ 
/* 191:201 */     this.loadPanel.setBorder(BorderFactory.createEtchedBorder());
/* 192:202 */     this.loadPanel.setName("loadPanel");
/* 193:    */     
/* 194:204 */     this.loadIcon.setIcon(resourceMap.getIcon("loadIcon.icon"));
/* 195:205 */     this.loadIcon.setName("loadIcon");
/* 196:    */     
/* 197:207 */     this.createLoadSampleGroup.add(this.loadButton);
/* 198:208 */     this.loadButton.setText(resourceMap.getString("loadButton.text", new Object[0]));
/* 199:209 */     this.loadButton.setName("loadButton");
/* 200:210 */     this.loadButton.addMouseListener(new MouseAdapter()
/* 201:    */     {
/* 202:    */       public void mouseClicked(MouseEvent evt)
/* 203:    */       {
/* 204:212 */         WelcomeDialog.this.loadButtonMouseClicked(evt);
/* 205:    */       }
/* 206:215 */     });
/* 207:216 */     GroupLayout loadPanelLayout = new GroupLayout(this.loadPanel);
/* 208:217 */     this.loadPanel.setLayout(loadPanelLayout);
/* 209:218 */     loadPanelLayout.setHorizontalGroup(loadPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(loadPanelLayout.createSequentialGroup().addContainerGap().addComponent(this.loadIcon).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.loadButton).addContainerGap(65, 32767)));
/* 210:    */     
/* 211:    */ 
/* 212:    */ 
/* 213:    */ 
/* 214:    */ 
/* 215:    */ 
/* 216:    */ 
/* 217:    */ 
/* 218:227 */     loadPanelLayout.setVerticalGroup(loadPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(loadPanelLayout.createSequentialGroup().addGap(12, 12, 12).addGroup(loadPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(this.loadButton).addComponent(this.loadIcon)).addContainerGap(-1, 32767)));
/* 219:    */     
/* 220:    */ 
/* 221:    */ 
/* 222:    */ 
/* 223:    */ 
/* 224:    */ 
/* 225:    */ 
/* 226:    */ 
/* 227:    */ 
/* 228:237 */     this.tipPanel.setBorder(BorderFactory.createTitledBorder(resourceMap.getString("tipPanel.border.title", new Object[0])));
/* 229:238 */     this.tipPanel.setName("tipPanel");
/* 230:239 */     this.tipPanel.setOpaque(false);
/* 231:    */     
/* 232:241 */     this.tipLabel.setIcon(resourceMap.getIcon("tipLabel.icon"));
/* 233:242 */     this.tipLabel.setText(resourceMap.getString("tipLabel.text", new Object[0]));
/* 234:243 */     this.tipLabel.setVerticalAlignment(1);
/* 235:244 */     this.tipLabel.setName("tipLabel");
/* 236:245 */     this.tipLabel.setVerticalTextPosition(1);
/* 237:    */     
/* 238:247 */     GroupLayout tipPanelLayout = new GroupLayout(this.tipPanel);
/* 239:248 */     this.tipPanel.setLayout(tipPanelLayout);
/* 240:249 */     tipPanelLayout.setHorizontalGroup(tipPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(tipPanelLayout.createSequentialGroup().addContainerGap().addComponent(this.tipLabel, -1, 228, 32767).addContainerGap()));
/* 241:    */     
/* 242:    */ 
/* 243:    */ 
/* 244:    */ 
/* 245:    */ 
/* 246:    */ 
/* 247:256 */     tipPanelLayout.setVerticalGroup(tipPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.tipLabel, -1, 119, 32767));
/* 248:    */     
/* 249:    */ 
/* 250:    */ 
/* 251:    */ 
/* 252:261 */     GroupLayout layout = new GroupLayout(getContentPane());
/* 253:262 */     getContentPane().setLayout(layout);
/* 254:263 */     layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false).addComponent(this.createLoadSamplePanel, -1, 298, 32767).addComponent(this.loadPanel, -1, -1, 32767)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.tipPanel, -1, -1, 32767)).addComponent(this.bannerLabel)).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.motifLabel, -1, -1, 32767).addComponent(this.okButton, -1, 118, 32767).addComponent(this.cancelButton, -1, 118, 32767).addComponent(this.aboutButton, -1, 118, 32767)).addContainerGap()));
/* 255:    */     
/* 256:    */ 
/* 257:    */ 
/* 258:    */ 
/* 259:    */ 
/* 260:    */ 
/* 261:    */ 
/* 262:    */ 
/* 263:    */ 
/* 264:    */ 
/* 265:    */ 
/* 266:    */ 
/* 267:    */ 
/* 268:    */ 
/* 269:    */ 
/* 270:    */ 
/* 271:    */ 
/* 272:    */ 
/* 273:    */ 
/* 274:283 */     layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(this.bannerLabel).addGap(13, 13, 13).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(this.createLoadSamplePanel, -2, -1, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.loadPanel, -2, -1, -2)).addComponent(this.tipPanel, -1, -1, 32767))).addGroup(layout.createSequentialGroup().addComponent(this.motifLabel, -2, 175, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.aboutButton).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.cancelButton).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.okButton))).addContainerGap()));
/* 275:    */     
/* 276:    */ 
/* 277:    */ 
/* 278:    */ 
/* 279:    */ 
/* 280:    */ 
/* 281:    */ 
/* 282:    */ 
/* 283:    */ 
/* 284:    */ 
/* 285:    */ 
/* 286:    */ 
/* 287:    */ 
/* 288:    */ 
/* 289:    */ 
/* 290:    */ 
/* 291:    */ 
/* 292:    */ 
/* 293:    */ 
/* 294:    */ 
/* 295:    */ 
/* 296:    */ 
/* 297:    */ 
/* 298:    */ 
/* 299:308 */     pack();
/* 300:    */   }
/* 301:    */   
/* 302:    */   private void aboutButtonActionPerformed(ActionEvent evt)
/* 303:    */   {
/* 304:312 */     this.aboutProvider.showAbout();
/* 305:    */   }
/* 306:    */   
/* 307:    */   private void handleOk()
/* 308:    */   {
/* 309:316 */     if (this.createButton.isSelected()) {
/* 310:317 */       this.result = 1;
/* 311:318 */     } else if (this.loadSampleButton.isSelected()) {
/* 312:319 */       this.result = 2;
/* 313:320 */     } else if (this.loadButton.isSelected()) {
/* 314:321 */       this.result = 3;
/* 315:    */     } else {
/* 316:323 */       this.result = 0;
/* 317:    */     }
/* 318:324 */     setVisible(false);
/* 319:    */   }
/* 320:    */   
/* 321:    */   private void okButtonActionPerformed(ActionEvent evt)
/* 322:    */   {
/* 323:328 */     handleOk();
/* 324:    */   }
/* 325:    */   
/* 326:    */   private void cancelButtonActionPerformed(ActionEvent evt)
/* 327:    */   {
/* 328:332 */     setVisible(false);
/* 329:    */   }
/* 330:    */   
/* 331:    */   private void loadSampleButtonMouseClicked(MouseEvent evt)
/* 332:    */   {
/* 333:336 */     if (evt.getClickCount() == 2) {
/* 334:337 */       handleOk();
/* 335:    */     }
/* 336:    */   }
/* 337:    */   
/* 338:    */   private void createButtonMouseClicked(MouseEvent evt)
/* 339:    */   {
/* 340:342 */     if (evt.getClickCount() == 2) {
/* 341:343 */       handleOk();
/* 342:    */     }
/* 343:    */   }
/* 344:    */   
/* 345:    */   private void loadButtonMouseClicked(MouseEvent evt)
/* 346:    */   {
/* 347:348 */     if (evt.getClickCount() == 2) {
/* 348:349 */       handleOk();
/* 349:    */     }
/* 350:    */   }
/* 351:    */   
/* 352:    */   public static abstract interface AboutProvider
/* 353:    */   {
/* 354:    */     public abstract void showAbout();
/* 355:    */   }
/* 356:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.WelcomeDialog
 * JD-Core Version:    0.7.0.1
 */