/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.awt.Container;
/*   4:    */ import java.awt.Font;
/*   5:    */ import java.awt.Frame;
/*   6:    */ import java.awt.event.ActionEvent;
/*   7:    */ import java.awt.event.ActionListener;
/*   8:    */ import javax.help.HelpBroker;
/*   9:    */ import javax.swing.BorderFactory;
/*  10:    */ import javax.swing.GroupLayout;
/*  11:    */ import javax.swing.GroupLayout.Alignment;
/*  12:    */ import javax.swing.GroupLayout.ParallelGroup;
/*  13:    */ import javax.swing.GroupLayout.SequentialGroup;
/*  14:    */ import javax.swing.ImageIcon;
/*  15:    */ import javax.swing.JButton;
/*  16:    */ import javax.swing.JDialog;
/*  17:    */ import javax.swing.JLabel;
/*  18:    */ import javax.swing.JPanel;
/*  19:    */ import javax.swing.JRootPane;
/*  20:    */ import javax.swing.JTextPane;
/*  21:    */ import javax.swing.LayoutStyle.ComponentPlacement;
/*  22:    */ import org.jdesktop.application.Application;
/*  23:    */ import org.jdesktop.application.ApplicationContext;
/*  24:    */ import org.jdesktop.application.ResourceMap;
/*  25:    */ 
/*  26:    */ public class SlendernessTestFailDialog
/*  27:    */   extends JDialog
/*  28:    */ {
/*  29:    */   private ResourceMap resourceMap;
/*  30:    */   private int[] pointCounts;
/*  31: 38 */   private int pointIndex = 0;
/*  32:    */   private JButton backButton;
/*  33:    */   private JLabel cartoonLabel;
/*  34:    */   private JButton closeButton;
/*  35:    */   private JPanel examplePanel;
/*  36:    */   private JTextPane exampleTextPane;
/*  37:    */   private JButton helpButton;
/*  38:    */   private JButton nextButton;
/*  39:    */   private JTextPane tipTextPane;
/*  40:    */   private JLabel titleLabel;
/*  41:    */   
/*  42:    */   public SlendernessTestFailDialog(Frame parent)
/*  43:    */   {
/*  44: 47 */     super(parent, true);
/*  45: 48 */     initComponents();
/*  46: 49 */     getRootPane().setDefaultButton(this.closeButton);
/*  47: 50 */     Help.getBroker().enableHelpOnButton(this.helpButton, "hlp_slenderness", Help.getSet());
/*  48: 51 */     update();
/*  49:    */   }
/*  50:    */   
/*  51:    */   private void initialize(ResourceMap resourceMap)
/*  52:    */   {
/*  53: 62 */     this.resourceMap = resourceMap;
/*  54: 63 */     this.pointCounts = Utility.mapToInt(resourceMap.getString("exampleCounts.intArray", new Object[0]).split(" +"));
/*  55: 64 */     update();
/*  56:    */   }
/*  57:    */   
/*  58:    */   private void update()
/*  59:    */   {
/*  60: 71 */     String key = getResourceKey();
/*  61:    */     
/*  62: 73 */     ImageIcon icon = WPBDApp.getApplication().getIconResource(key + ".gif");
/*  63: 74 */     icon.setImageObserver(this.cartoonLabel);
/*  64: 75 */     this.cartoonLabel.setIcon(icon);
/*  65: 77 */     if (this.resourceMap != null) {
/*  66: 78 */       this.exampleTextPane.setText(this.resourceMap.getString(key + ".text", new Object[0]));
/*  67:    */     }
/*  68: 81 */     this.backButton.setEnabled(this.pointIndex > 0);
/*  69:    */   }
/*  70:    */   
/*  71:    */   private String getResourceKey()
/*  72:    */   {
/*  73: 90 */     int pointNumber = this.pointIndex + 1;
/*  74: 91 */     return "ex5pt" + pointNumber;
/*  75:    */   }
/*  76:    */   
/*  77:    */   private int getExampleIndex()
/*  78:    */   {
/*  79: 95 */     return 0;
/*  80:    */   }
/*  81:    */   
/*  82:    */   private void initComponents()
/*  83:    */   {
/*  84:102 */     this.titleLabel = new JLabel();
/*  85:103 */     this.examplePanel = new JPanel();
/*  86:104 */     this.exampleTextPane = new TipTextPane();
/*  87:105 */     this.cartoonLabel = new JLabel();
/*  88:106 */     this.nextButton = new JButton();
/*  89:107 */     this.backButton = new JButton();
/*  90:108 */     this.tipTextPane = new TipTextPane();
/*  91:109 */     this.helpButton = new JButton();
/*  92:110 */     this.closeButton = new JButton();
/*  93:    */     
/*  94:112 */     ResourceMap resourceMap = ((WPBDApp)Application.getInstance(WPBDApp.class)).getContext().getResourceMap(SlendernessTestFailDialog.class);
/*  95:113 */     setTitle(resourceMap.getString("Form.title", new Object[0]));
/*  96:114 */     setName("Form");
/*  97:115 */     setResizable(false);
/*  98:    */     
/*  99:117 */     this.titleLabel.setFont(this.titleLabel.getFont().deriveFont(this.titleLabel.getFont().getStyle() | 0x1, this.titleLabel.getFont().getSize() + 10));
/* 100:118 */     this.titleLabel.setText(resourceMap.getString("titleLabel.text", new Object[0]));
/* 101:119 */     initialize(resourceMap);
/* 102:120 */     this.titleLabel.setName("titleLabel");
/* 103:    */     
/* 104:122 */     this.examplePanel.setBorder(BorderFactory.createBevelBorder(0));
/* 105:123 */     this.examplePanel.setName("examplePanel");
/* 106:    */     
/* 107:125 */     this.exampleTextPane.setBorder(BorderFactory.createBevelBorder(1));
/* 108:126 */     this.exampleTextPane.setContentType(resourceMap.getString("exampleTextPane.contentType", new Object[0]));
/* 109:127 */     this.exampleTextPane.setName("exampleTextPane");
/* 110:128 */     this.exampleTextPane.setOpaque(false);
/* 111:    */     
/* 112:130 */     this.cartoonLabel.setIcon(resourceMap.getIcon("cartoonLabel.icon"));
/* 113:131 */     this.cartoonLabel.setText(null);
/* 114:132 */     this.cartoonLabel.setBorder(BorderFactory.createBevelBorder(1));
/* 115:133 */     this.cartoonLabel.setName("cartoonLabel");
/* 116:    */     
/* 117:135 */     this.nextButton.setText(resourceMap.getString("nextButton.text", new Object[0]));
/* 118:136 */     this.nextButton.setName("nextButton");
/* 119:137 */     this.nextButton.addActionListener(new ActionListener()
/* 120:    */     {
/* 121:    */       public void actionPerformed(ActionEvent evt)
/* 122:    */       {
/* 123:139 */         SlendernessTestFailDialog.this.nextButtonActionPerformed(evt);
/* 124:    */       }
/* 125:142 */     });
/* 126:143 */     this.backButton.setText(resourceMap.getString("backButton.text", new Object[0]));
/* 127:144 */     this.backButton.setName("backButton");
/* 128:145 */     this.backButton.addActionListener(new ActionListener()
/* 129:    */     {
/* 130:    */       public void actionPerformed(ActionEvent evt)
/* 131:    */       {
/* 132:147 */         SlendernessTestFailDialog.this.backButtonActionPerformed(evt);
/* 133:    */       }
/* 134:150 */     });
/* 135:151 */     GroupLayout examplePanelLayout = new GroupLayout(this.examplePanel);
/* 136:152 */     this.examplePanel.setLayout(examplePanelLayout);
/* 137:153 */     examplePanelLayout.setHorizontalGroup(examplePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(examplePanelLayout.createSequentialGroup().addContainerGap().addGroup(examplePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, examplePanelLayout.createSequentialGroup().addComponent(this.backButton).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.nextButton)).addGroup(examplePanelLayout.createSequentialGroup().addComponent(this.cartoonLabel, -2, 165, -2).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(this.exampleTextPane, -1, 254, 32767))).addContainerGap()));
/* 138:    */     
/* 139:    */ 
/* 140:    */ 
/* 141:    */ 
/* 142:    */ 
/* 143:    */ 
/* 144:    */ 
/* 145:    */ 
/* 146:    */ 
/* 147:    */ 
/* 148:    */ 
/* 149:    */ 
/* 150:    */ 
/* 151:    */ 
/* 152:168 */     examplePanelLayout.setVerticalGroup(examplePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(examplePanelLayout.createSequentialGroup().addContainerGap().addGroup(examplePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.exampleTextPane, -1, 166, 32767).addComponent(this.cartoonLabel)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(examplePanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.nextButton).addComponent(this.backButton)).addContainerGap()));
/* 153:    */     
/* 154:    */ 
/* 155:    */ 
/* 156:    */ 
/* 157:    */ 
/* 158:    */ 
/* 159:    */ 
/* 160:    */ 
/* 161:    */ 
/* 162:    */ 
/* 163:    */ 
/* 164:    */ 
/* 165:    */ 
/* 166:182 */     this.tipTextPane.setBorder(BorderFactory.createTitledBorder(resourceMap.getString("tipTextPane.border.title", new Object[0])));
/* 167:183 */     this.tipTextPane.setContentType(resourceMap.getString("tipTextPane.contentType", new Object[0]));
/* 168:184 */     this.tipTextPane.setText(resourceMap.getString("tipTextPane.text", new Object[0]));
/* 169:185 */     this.tipTextPane.setName("tipTextPane");
/* 170:186 */     this.tipTextPane.setOpaque(false);
/* 171:    */     
/* 172:188 */     this.helpButton.setText(resourceMap.getString("helpButton.text", new Object[0]));
/* 173:189 */     this.helpButton.setName("helpButton");
/* 174:    */     
/* 175:191 */     this.closeButton.setText(resourceMap.getString("closeButton.text", new Object[0]));
/* 176:192 */     this.closeButton.setName("closeButton");
/* 177:193 */     this.closeButton.addActionListener(new ActionListener()
/* 178:    */     {
/* 179:    */       public void actionPerformed(ActionEvent evt)
/* 180:    */       {
/* 181:195 */         SlendernessTestFailDialog.this.closeButtonActionPerformed(evt);
/* 182:    */       }
/* 183:198 */     });
/* 184:199 */     GroupLayout layout = new GroupLayout(getContentPane());
/* 185:200 */     getContentPane().setLayout(layout);
/* 186:201 */     layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.titleLabel).addComponent(this.examplePanel, -1, -1, 32767).addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(this.tipTextPane, -1, 381, 32767).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false).addComponent(this.closeButton, -1, -1, 32767).addComponent(this.helpButton, -1, -1, 32767)))).addContainerGap()));
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
/* 201:216 */     layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(this.titleLabel).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(this.examplePanel, -2, 222, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(this.helpButton).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.closeButton)).addComponent(this.tipTextPane)).addContainerGap()));
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
/* 218:233 */     pack();
/* 219:    */   }
/* 220:    */   
/* 221:    */   private void closeButtonActionPerformed(ActionEvent evt)
/* 222:    */   {
/* 223:237 */     setVisible(false);
/* 224:    */   }
/* 225:    */   
/* 226:    */   private void backButtonActionPerformed(ActionEvent evt)
/* 227:    */   {
/* 228:241 */     int n = this.pointCounts[getExampleIndex()];
/* 229:242 */     this.pointIndex = ((this.pointIndex + n - 1) % n);
/* 230:243 */     update();
/* 231:    */   }
/* 232:    */   
/* 233:    */   private void nextButtonActionPerformed(ActionEvent evt)
/* 234:    */   {
/* 235:247 */     int n = this.pointCounts[getExampleIndex()];
/* 236:248 */     this.pointIndex = ((this.pointIndex + 1) % n);
/* 237:249 */     update();
/* 238:    */   }
/* 239:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.SlendernessTestFailDialog
 * JD-Core Version:    0.7.0.1
 */