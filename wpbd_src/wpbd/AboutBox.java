/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.awt.Component;
/*   4:    */ import java.awt.Container;
/*   5:    */ import java.awt.Dimension;
/*   6:    */ import java.awt.Frame;
/*   7:    */ import java.awt.Insets;
/*   8:    */ import javax.help.HelpBroker;
/*   9:    */ import javax.swing.ActionMap;
/*  10:    */ import javax.swing.BorderFactory;
/*  11:    */ import javax.swing.GroupLayout;
/*  12:    */ import javax.swing.GroupLayout.Alignment;
/*  13:    */ import javax.swing.GroupLayout.ParallelGroup;
/*  14:    */ import javax.swing.GroupLayout.SequentialGroup;
/*  15:    */ import javax.swing.JButton;
/*  16:    */ import javax.swing.JDialog;
/*  17:    */ import javax.swing.JLabel;
/*  18:    */ import javax.swing.JRootPane;
/*  19:    */ import javax.swing.JTextPane;
/*  20:    */ import javax.swing.LayoutStyle.ComponentPlacement;
/*  21:    */ import org.jdesktop.application.Action;
/*  22:    */ import org.jdesktop.application.Application;
/*  23:    */ import org.jdesktop.application.ApplicationContext;
/*  24:    */ import org.jdesktop.application.ResourceMap;
/*  25:    */ 
/*  26:    */ public class AboutBox
/*  27:    */   extends JDialog
/*  28:    */ {
/*  29:    */   private JButton closeButton;
/*  30:    */   private JTextPane developerTextPane;
/*  31:    */   private JLabel headerLabel;
/*  32:    */   private JButton howItWorksButton;
/*  33:    */   private JButton purposesButton;
/*  34:    */   private JTextPane restrictionsTextPane;
/*  35:    */   private JLabel sysInfoLabel;
/*  36:    */   private JLabel versionLabel;
/*  37:    */   
/*  38:    */   public AboutBox(Frame parent)
/*  39:    */   {
/*  40: 33 */     super(parent);
/*  41: 34 */     initComponents();
/*  42: 35 */     this.sysInfoLabel.setText("JRE " + System.getProperty("java.version"));
/*  43: 36 */     getRootPane().setDefaultButton(this.closeButton);
/*  44: 37 */     Help.getBroker().enableHelpOnButton(this.purposesButton, "hlp_purposes", Help.getSet());
/*  45: 38 */     Help.getBroker().enableHelpOnButton(this.howItWorksButton, "hlp_how_wpbd_works", Help.getSet());
/*  46:    */   }
/*  47:    */   
/*  48:    */   @Action
/*  49:    */   public void closeAboutBox()
/*  50:    */   {
/*  51: 46 */     setVisible(false);
/*  52:    */   }
/*  53:    */   
/*  54:    */   private void initComponents()
/*  55:    */   {
/*  56: 52 */     this.closeButton = new JButton();
/*  57: 53 */     JLabel flashImageLabel = new JLabel();
/*  58: 54 */     this.versionLabel = new JLabel();
/*  59: 55 */     this.purposesButton = new JButton();
/*  60: 56 */     this.howItWorksButton = new JButton();
/*  61: 57 */     this.developerTextPane = new TipTextPane();
/*  62: 58 */     this.restrictionsTextPane = new TipTextPane();
/*  63: 59 */     this.headerLabel = new JLabel();
/*  64: 60 */     this.sysInfoLabel = new JLabel();
/*  65:    */     
/*  66: 62 */     ResourceMap resourceMap = ((WPBDApp)Application.getInstance(WPBDApp.class)).getContext().getResourceMap(AboutBox.class);
/*  67: 63 */     setTitle(resourceMap.getString("title", new Object[0]));
/*  68: 64 */     setModal(true);
/*  69: 65 */     setName("aboutBox");
/*  70: 66 */     setResizable(false);
/*  71:    */     
/*  72: 68 */     ActionMap actionMap = ((WPBDApp)Application.getInstance(WPBDApp.class)).getContext().getActionMap(AboutBox.class, this);
/*  73: 69 */     this.closeButton.setAction(actionMap.get("closeAboutBox"));
/*  74: 70 */     this.closeButton.setName("closeButton");
/*  75:    */     
/*  76: 72 */     flashImageLabel.setIcon(resourceMap.getIcon("flashImageLabel.icon"));
/*  77: 73 */     flashImageLabel.setAutoscrolls(true);
/*  78: 74 */     flashImageLabel.setBorder(BorderFactory.createBevelBorder(1));
/*  79: 75 */     flashImageLabel.setName("flashImageLabel");
/*  80:    */     
/*  81: 77 */     this.versionLabel.setHorizontalAlignment(0);
/*  82: 78 */     this.versionLabel.setText(resourceMap.getString("versionLabel.text", new Object[0]));
/*  83: 79 */     this.versionLabel.setName("versionLabel");
/*  84:    */     
/*  85: 81 */     this.purposesButton.setText(resourceMap.getString("purposesButton.text", new Object[0]));
/*  86: 82 */     this.purposesButton.setName("purposesButton");
/*  87:    */     
/*  88: 84 */     this.howItWorksButton.setText(resourceMap.getString("howItWorksButton.text", new Object[0]));
/*  89: 85 */     this.howItWorksButton.setMargin(new Insets(2, 4, 2, 4));
/*  90: 86 */     this.howItWorksButton.setName("howItWorksButton");
/*  91:    */     
/*  92: 88 */     this.developerTextPane.setBorder(BorderFactory.createTitledBorder(resourceMap.getString("developerTextPane.border.title", new Object[0])));
/*  93: 89 */     this.developerTextPane.setText(resourceMap.getString("developerTextPane.text", new Object[0]));
/*  94: 90 */     this.developerTextPane.setName("developerTextPane");
/*  95: 91 */     this.developerTextPane.setOpaque(false);
/*  96: 92 */     this.developerTextPane.setPreferredSize(new Dimension(624, 132));
/*  97:    */     
/*  98: 94 */     this.restrictionsTextPane.setBorder(BorderFactory.createTitledBorder(resourceMap.getString("restrictionsTextPane.border.title", new Object[0])));
/*  99: 95 */     this.restrictionsTextPane.setText(resourceMap.getString("restrictionsTextPane.text", new Object[0]));
/* 100: 96 */     this.restrictionsTextPane.setName("restrictionsTextPane");
/* 101: 97 */     this.restrictionsTextPane.setOpaque(false);
/* 102:    */     
/* 103: 99 */     this.headerLabel.setIcon(resourceMap.getIcon("headerLabel.icon"));
/* 104:100 */     this.headerLabel.setText(resourceMap.getString("headerLabel.text", new Object[0]));
/* 105:101 */     this.headerLabel.setName("headerLabel");
/* 106:    */     
/* 107:103 */     this.sysInfoLabel.setFont(resourceMap.getFont("sysInfoLabel.font"));
/* 108:104 */     this.sysInfoLabel.setHorizontalAlignment(0);
/* 109:105 */     this.sysInfoLabel.setText(null);
/* 110:106 */     this.sysInfoLabel.setName("sysInfoLabel");
/* 111:    */     
/* 112:108 */     GroupLayout layout = new GroupLayout(getContentPane());
/* 113:109 */     getContentPane().setLayout(layout);
/* 114:110 */     layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.headerLabel, -1, -1, 32767).addComponent(this.developerTextPane, -2, 0, 32767).addComponent(this.restrictionsTextPane, -2, 0, 32767)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.purposesButton, GroupLayout.Alignment.TRAILING, -1, 133, 32767).addComponent(this.closeButton, -1, 133, 32767).addComponent(this.howItWorksButton, 0, 0, 32767).addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addGap(0, 0, 32767).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false).addComponent(this.versionLabel, -1, -1, 32767).addComponent(flashImageLabel, -1, -1, 32767).addComponent(this.sysInfoLabel, -1, -1, 32767)))).addContainerGap()));
/* 115:    */     
/* 116:    */ 
/* 117:    */ 
/* 118:    */ 
/* 119:    */ 
/* 120:    */ 
/* 121:    */ 
/* 122:    */ 
/* 123:    */ 
/* 124:    */ 
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
/* 136:132 */     layout.linkSize(0, new Component[] { this.closeButton, this.howItWorksButton, this.purposesButton });
/* 137:    */     
/* 138:134 */     layout.linkSize(0, new Component[] { this.developerTextPane, this.headerLabel, this.restrictionsTextPane });
/* 139:    */     
/* 140:136 */     layout.linkSize(0, new Component[] { flashImageLabel, this.versionLabel });
/* 141:    */     
/* 142:138 */     layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(flashImageLabel).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.versionLabel).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.sysInfoLabel, -2, 10, -2)).addGroup(layout.createSequentialGroup().addComponent(this.headerLabel).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(this.developerTextPane, -2, 153, -2))).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(this.purposesButton).addGap(18, 18, 18).addComponent(this.howItWorksButton).addGap(18, 18, 18).addComponent(this.closeButton)).addComponent(this.restrictionsTextPane, -2, 145, -2)).addContainerGap(-1, 32767)));
/* 143:    */     
/* 144:    */ 
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
/* 158:    */ 
/* 159:    */ 
/* 160:    */ 
/* 161:    */ 
/* 162:    */ 
/* 163:    */ 
/* 164:    */ 
/* 165:    */ 
/* 166:    */ 
/* 167:    */ 
/* 168:    */ 
/* 169:165 */     pack();
/* 170:    */   }
/* 171:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.AboutBox
 * JD-Core Version:    0.7.0.1
 */