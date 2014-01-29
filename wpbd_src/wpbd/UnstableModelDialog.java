/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.awt.Container;
/*   4:    */ import java.awt.EventQueue;
/*   5:    */ import java.awt.Font;
/*   6:    */ import java.awt.Frame;
/*   7:    */ import java.awt.event.ActionEvent;
/*   8:    */ import java.awt.event.ActionListener;
/*   9:    */ import java.awt.event.WindowAdapter;
/*  10:    */ import java.awt.event.WindowEvent;
/*  11:    */ import javax.swing.BorderFactory;
/*  12:    */ import javax.swing.GroupLayout;
/*  13:    */ import javax.swing.GroupLayout.Alignment;
/*  14:    */ import javax.swing.GroupLayout.ParallelGroup;
/*  15:    */ import javax.swing.GroupLayout.SequentialGroup;
/*  16:    */ import javax.swing.ImageIcon;
/*  17:    */ import javax.swing.JButton;
/*  18:    */ import javax.swing.JDialog;
/*  19:    */ import javax.swing.JFrame;
/*  20:    */ import javax.swing.JLabel;
/*  21:    */ import javax.swing.JPanel;
/*  22:    */ import javax.swing.JRootPane;
/*  23:    */ import javax.swing.JTabbedPane;
/*  24:    */ import javax.swing.JTextPane;
/*  25:    */ import javax.swing.LayoutStyle.ComponentPlacement;
/*  26:    */ import javax.swing.event.ChangeEvent;
/*  27:    */ import javax.swing.event.ChangeListener;
/*  28:    */ import org.jdesktop.application.Application;
/*  29:    */ import org.jdesktop.application.ApplicationContext;
/*  30:    */ import org.jdesktop.application.ResourceMap;
/*  31:    */ 
/*  32:    */ public class UnstableModelDialog
/*  33:    */   extends JDialog
/*  34:    */ {
/*  35:    */   private ResourceMap resourceMap;
/*  36:    */   private int[] pointCounts;
/*  37: 49 */   private int pointIndex = 0;
/*  38:    */   private JButton backButton;
/*  39:    */   private JLabel cartoonLabel;
/*  40:    */   private JButton closeButton;
/*  41:    */   private JPanel examplePanel;
/*  42:    */   private JTabbedPane exampleTabs;
/*  43:    */   private JTextPane exampleTextPane;
/*  44:    */   private JTextPane explanationTextPane;
/*  45:    */   private JButton nextButton;
/*  46:    */   private JTextPane tipTextPane;
/*  47:    */   private JLabel titleLabel;
/*  48:    */   
/*  49:    */   public UnstableModelDialog(Frame parent)
/*  50:    */   {
/*  51: 57 */     super(parent, true);
/*  52:    */     
/*  53: 59 */     initComponents();
/*  54: 60 */     getRootPane().setDefaultButton(this.closeButton);
/*  55:    */   }
/*  56:    */   
/*  57:    */   private void initialize(ResourceMap resourceMap)
/*  58:    */   {
/*  59: 71 */     this.resourceMap = resourceMap;
/*  60: 72 */     this.pointCounts = Utility.mapToInt(resourceMap.getString("exampleCounts.intArray", new Object[0]).split(" +"));
/*  61: 73 */     for (int i = 1; i < this.pointCounts.length; i++) {
/*  62: 74 */       this.exampleTabs.addTab(resourceMap.getString("examplePanel.TabConstraints.tabTitleMore", new Object[] { Integer.valueOf(i + 1) }), null, null);
/*  63:    */     }
/*  64: 76 */     update();
/*  65:    */   }
/*  66:    */   
/*  67:    */   private int getExampleIndex()
/*  68:    */   {
/*  69: 86 */     return this.exampleTabs.getSelectedIndex();
/*  70:    */   }
/*  71:    */   
/*  72:    */   private String getResourceKey()
/*  73:    */   {
/*  74: 95 */     int exampleNumber = getExampleIndex() + 1;
/*  75: 96 */     int pointNumber = this.pointIndex + 1;
/*  76: 97 */     return "ex" + exampleNumber + "pt" + pointNumber;
/*  77:    */   }
/*  78:    */   
/*  79:    */   private void update()
/*  80:    */   {
/*  81:104 */     String key = getResourceKey();
/*  82:    */     
/*  83:106 */     ImageIcon icon = WPBDApp.getApplication().getIconResource(key + ".gif");
/*  84:107 */     this.cartoonLabel.setIcon(icon);
/*  85:108 */     icon.setImageObserver(this.cartoonLabel);
/*  86:110 */     if (this.resourceMap != null) {
/*  87:111 */       this.exampleTextPane.setText(this.resourceMap.getString(key + ".text", new Object[0]));
/*  88:    */     }
/*  89:114 */     this.backButton.setEnabled(this.pointIndex > 0);
/*  90:    */   }
/*  91:    */   
/*  92:    */   private void initComponents()
/*  93:    */   {
/*  94:121 */     this.titleLabel = new JLabel();
/*  95:122 */     this.explanationTextPane = new TipTextPane();
/*  96:123 */     this.exampleTabs = new JTabbedPane();
/*  97:124 */     this.examplePanel = new JPanel();
/*  98:125 */     this.cartoonLabel = new JLabel();
/*  99:126 */     this.exampleTextPane = new TipTextPane();
/* 100:127 */     this.backButton = new JButton();
/* 101:128 */     this.nextButton = new JButton();
/* 102:129 */     this.tipTextPane = new TipTextPane();
/* 103:130 */     this.closeButton = new JButton();
/* 104:    */     
/* 105:132 */     ResourceMap resourceMap = ((WPBDApp)Application.getInstance(WPBDApp.class)).getContext().getResourceMap(UnstableModelDialog.class);
/* 106:133 */     setTitle(resourceMap.getString("Form.title", new Object[0]));
/* 107:134 */     setName("Form");
/* 108:    */     
/* 109:136 */     this.titleLabel.setFont(this.titleLabel.getFont().deriveFont(this.titleLabel.getFont().getStyle() | 0x1, this.titleLabel.getFont().getSize() + 10));
/* 110:137 */     this.titleLabel.setText(resourceMap.getString("titleLabel.text", new Object[0]));
/* 111:138 */     this.titleLabel.setName("titleLabel");
/* 112:    */     
/* 113:140 */     this.explanationTextPane.setContentType(resourceMap.getString("explanationTextPane.contentType", new Object[0]));
/* 114:141 */     this.explanationTextPane.setEditable(false);
/* 115:142 */     this.explanationTextPane.setText(resourceMap.getString("explanationTextPane.text", new Object[0]));
/* 116:143 */     this.explanationTextPane.setName("explanationTextPane");
/* 117:144 */     this.explanationTextPane.setOpaque(false);
/* 118:    */     
/* 119:146 */     this.exampleTabs.setName("exampleTabs");
/* 120:147 */     this.exampleTabs.addChangeListener(new ChangeListener()
/* 121:    */     {
/* 122:    */       public void stateChanged(ChangeEvent evt)
/* 123:    */       {
/* 124:149 */         UnstableModelDialog.this.exampleTabsStateChanged(evt);
/* 125:    */       }
/* 126:152 */     });
/* 127:153 */     this.examplePanel.setName("examplePanel");
/* 128:    */     
/* 129:155 */     this.cartoonLabel.setIcon(resourceMap.getIcon("cartoonLabel.icon"));
/* 130:156 */     this.cartoonLabel.setText(null);
/* 131:157 */     this.cartoonLabel.setBorder(BorderFactory.createBevelBorder(1));
/* 132:158 */     this.cartoonLabel.setDoubleBuffered(true);
/* 133:159 */     this.cartoonLabel.setName("cartoonLabel");
/* 134:    */     
/* 135:161 */     this.exampleTextPane.setBorder(BorderFactory.createBevelBorder(1));
/* 136:162 */     this.exampleTextPane.setContentType(resourceMap.getString("exampleTextPane.contentType", new Object[0]));
/* 137:163 */     this.exampleTextPane.setEditable(false);
/* 138:164 */     this.exampleTextPane.setName("exampleTextPane");
/* 139:    */     
/* 140:166 */     this.backButton.setText(resourceMap.getString("backButton.text", new Object[0]));
/* 141:167 */     this.backButton.setName("backButton");
/* 142:168 */     this.backButton.addActionListener(new ActionListener()
/* 143:    */     {
/* 144:    */       public void actionPerformed(ActionEvent evt)
/* 145:    */       {
/* 146:170 */         UnstableModelDialog.this.backButtonActionPerformed(evt);
/* 147:    */       }
/* 148:173 */     });
/* 149:174 */     this.nextButton.setText(resourceMap.getString("nextButton.text", new Object[0]));
/* 150:175 */     this.nextButton.setName("nextButton");
/* 151:176 */     this.nextButton.addActionListener(new ActionListener()
/* 152:    */     {
/* 153:    */       public void actionPerformed(ActionEvent evt)
/* 154:    */       {
/* 155:178 */         UnstableModelDialog.this.nextButtonActionPerformed(evt);
/* 156:    */       }
/* 157:181 */     });
/* 158:182 */     GroupLayout examplePanelLayout = new GroupLayout(this.examplePanel);
/* 159:183 */     this.examplePanel.setLayout(examplePanelLayout);
/* 160:184 */     examplePanelLayout.setHorizontalGroup(examplePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(examplePanelLayout.createSequentialGroup().addContainerGap().addGroup(examplePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(examplePanelLayout.createSequentialGroup().addComponent(this.cartoonLabel, -2, 109, -2).addGap(18, 18, 18).addComponent(this.exampleTextPane, -2, 394, -2).addContainerGap()).addGroup(GroupLayout.Alignment.TRAILING, examplePanelLayout.createSequentialGroup().addComponent(this.backButton).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.nextButton).addGap(8, 8, 8)))));
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
/* 176:200 */     examplePanelLayout.setVerticalGroup(examplePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(examplePanelLayout.createSequentialGroup().addContainerGap().addGroup(examplePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.cartoonLabel).addComponent(this.exampleTextPane, -2, 63, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 11, 32767).addGroup(examplePanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.nextButton).addComponent(this.backButton)).addGap(8, 8, 8)));
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
/* 190:214 */     this.exampleTabs.addTab(resourceMap.getString("examplePanel.TabConstraints.tabTitle", new Object[0]), this.examplePanel);
/* 191:    */     
/* 192:216 */     initialize(resourceMap);
/* 193:    */     
/* 194:218 */     this.tipTextPane.setBorder(BorderFactory.createTitledBorder(resourceMap.getString("tipTextPane.border.title", new Object[0])));
/* 195:219 */     this.tipTextPane.setContentType(resourceMap.getString("tipTextPane.contentType", new Object[0]));
/* 196:220 */     this.tipTextPane.setText(resourceMap.getString("tipTextPane.text", new Object[0]));
/* 197:221 */     this.tipTextPane.setName("tipTextPane");
/* 198:222 */     this.tipTextPane.setOpaque(false);
/* 199:    */     
/* 200:224 */     this.closeButton.setText(resourceMap.getString("closeButton.text", new Object[0]));
/* 201:225 */     this.closeButton.setName("closeButton");
/* 202:226 */     this.closeButton.addActionListener(new ActionListener()
/* 203:    */     {
/* 204:    */       public void actionPerformed(ActionEvent evt)
/* 205:    */       {
/* 206:228 */         UnstableModelDialog.this.closeButtonActionPerformed(evt);
/* 207:    */       }
/* 208:231 */     });
/* 209:232 */     GroupLayout layout = new GroupLayout(getContentPane());
/* 210:233 */     getContentPane().setLayout(layout);
/* 211:234 */     layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.explanationTextPane, -2, 550, -2).addComponent(this.titleLabel).addComponent(this.exampleTabs, -1, 555, 32767).addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(this.tipTextPane, -2, 472, -2).addGap(18, 18, 18).addComponent(this.closeButton))).addContainerGap()));
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
/* 223:    */ 
/* 224:    */ 
/* 225:248 */     layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(this.titleLabel).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.explanationTextPane, -2, 106, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.exampleTabs, -2, 147, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.closeButton, GroupLayout.Alignment.TRAILING).addComponent(this.tipTextPane, -1, 99, 32767)).addContainerGap()));
/* 226:    */     
/* 227:    */ 
/* 228:    */ 
/* 229:    */ 
/* 230:    */ 
/* 231:    */ 
/* 232:    */ 
/* 233:    */ 
/* 234:    */ 
/* 235:    */ 
/* 236:    */ 
/* 237:    */ 
/* 238:    */ 
/* 239:    */ 
/* 240:    */ 
/* 241:264 */     pack();
/* 242:    */   }
/* 243:    */   
/* 244:    */   private void closeButtonActionPerformed(ActionEvent evt)
/* 245:    */   {
/* 246:268 */     setVisible(false);
/* 247:    */   }
/* 248:    */   
/* 249:    */   private void exampleTabsStateChanged(ChangeEvent evt)
/* 250:    */   {
/* 251:272 */     this.pointIndex = 0;
/* 252:273 */     update();
/* 253:    */   }
/* 254:    */   
/* 255:    */   private void backButtonActionPerformed(ActionEvent evt)
/* 256:    */   {
/* 257:277 */     int n = this.pointCounts[getExampleIndex()];
/* 258:278 */     this.pointIndex = ((this.pointIndex + n - 1) % n);
/* 259:279 */     update();
/* 260:    */   }
/* 261:    */   
/* 262:    */   private void nextButtonActionPerformed(ActionEvent evt)
/* 263:    */   {
/* 264:283 */     int n = this.pointCounts[getExampleIndex()];
/* 265:284 */     this.pointIndex = ((this.pointIndex + 1) % n);
/* 266:285 */     update();
/* 267:    */   }
/* 268:    */   
/* 269:    */   public static void main(String[] args)
/* 270:    */   {
/* 271:292 */     EventQueue.invokeLater(new Runnable()
/* 272:    */     {
/* 273:    */       public void run()
/* 274:    */       {
/* 275:294 */         UnstableModelDialog dialog = new UnstableModelDialog(new JFrame());
/* 276:295 */         dialog.addWindowListener(new WindowAdapter()
/* 277:    */         {
/* 278:    */           public void windowClosing(WindowEvent e)
/* 279:    */           {
/* 280:298 */             System.exit(0);
/* 281:    */           }
/* 282:300 */         });
/* 283:301 */         dialog.setVisible(true);
/* 284:    */       }
/* 285:    */     });
/* 286:    */   }
/* 287:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.UnstableModelDialog
 * JD-Core Version:    0.7.0.1
 */