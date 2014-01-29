/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.awt.event.ActionEvent;
/*   4:    */ import java.awt.event.ActionListener;
/*   5:    */ import java.awt.event.ItemEvent;
/*   6:    */ import java.awt.event.ItemListener;
/*   7:    */ import javax.swing.ComboBoxModel;
/*   8:    */ import javax.swing.JButton;
/*   9:    */ import javax.swing.JComboBox;
/*  10:    */ import javax.swing.event.ListDataEvent;
/*  11:    */ import javax.swing.event.ListDataListener;
/*  12:    */ import org.jdesktop.application.ResourceMap;
/*  13:    */ 
/*  14:    */ public class ExtendedComboBox
/*  15:    */   extends JComboBox
/*  16:    */ {
/*  17:    */   private JButton up;
/*  18:    */   private JButton down;
/*  19: 38 */   private final ListDataListener listener = new ListDataListener()
/*  20:    */   {
/*  21:    */     public void intervalAdded(ListDataEvent e)
/*  22:    */     {
/*  23: 41 */       ExtendedComboBox.this.synchSpinButtons();
/*  24:    */     }
/*  25:    */     
/*  26:    */     public void intervalRemoved(ListDataEvent e)
/*  27:    */     {
/*  28: 45 */       ExtendedComboBox.this.synchSpinButtons();
/*  29:    */     }
/*  30:    */     
/*  31:    */     public void contentsChanged(ListDataEvent e)
/*  32:    */     {
/*  33: 49 */       ExtendedComboBox.this.synchSpinButtons();
/*  34:    */     }
/*  35:    */   };
/*  36:    */   
/*  37:    */   public ExtendedComboBox()
/*  38:    */   {
/*  39: 57 */     this(null, null);
/*  40:    */   }
/*  41:    */   
/*  42:    */   public ExtendedComboBox(JButton up, JButton down)
/*  43:    */   {
/*  44: 67 */     this.up = up;
/*  45: 68 */     this.down = down;
/*  46: 69 */     if (up != null) {
/*  47: 70 */       up.addActionListener(new ActionListener()
/*  48:    */       {
/*  49:    */         public void actionPerformed(ActionEvent e)
/*  50:    */         {
/*  51: 73 */           ExtendedComboBox.this.incrementIndex(-1);
/*  52:    */         }
/*  53:    */       });
/*  54:    */     }
/*  55: 77 */     if (down != null) {
/*  56: 78 */       down.addActionListener(new ActionListener()
/*  57:    */       {
/*  58:    */         public void actionPerformed(ActionEvent e)
/*  59:    */         {
/*  60: 81 */           ExtendedComboBox.this.incrementIndex(1);
/*  61:    */         }
/*  62:    */       });
/*  63:    */     }
/*  64: 85 */     if ((up != null) || (down != null)) {
/*  65: 86 */       addItemListener(new ItemListener()
/*  66:    */       {
/*  67:    */         public void itemStateChanged(ItemEvent evt)
/*  68:    */         {
/*  69: 89 */           if (evt.getStateChange() == 1) {
/*  70: 90 */             ExtendedComboBox.this.synchSpinButtons();
/*  71:    */           }
/*  72:    */         }
/*  73:    */       });
/*  74:    */     }
/*  75:    */   }
/*  76:    */   
/*  77:    */   public void setEnabled(boolean b)
/*  78:    */   {
/*  79:104 */     super.setEnabled(b);
/*  80:105 */     synchSpinButtons();
/*  81:    */   }
/*  82:    */   
/*  83:    */   private void incrementIndex(int increment)
/*  84:    */   {
/*  85:109 */     setSelectedIndex(getSelectedIndex() + increment);
/*  86:110 */     synchSpinButtons();
/*  87:    */   }
/*  88:    */   
/*  89:    */   private void synchSpinButtons()
/*  90:    */   {
/*  91:118 */     if (isEnabled())
/*  92:    */     {
/*  93:119 */       int selectedIndex = getSelectedIndex();
/*  94:120 */       if (this.down != null) {
/*  95:121 */         this.down.setEnabled(selectedIndex < getItemCount() - 1);
/*  96:    */       }
/*  97:123 */       if (this.up != null) {
/*  98:124 */         this.up.setEnabled(selectedIndex > 0);
/*  99:    */       }
/* 100:    */     }
/* 101:    */     else
/* 102:    */     {
/* 103:127 */       if (this.down != null) {
/* 104:128 */         this.down.setEnabled(false);
/* 105:    */       }
/* 106:130 */       if (this.up != null) {
/* 107:131 */         this.up.setEnabled(false);
/* 108:    */       }
/* 109:    */     }
/* 110:    */   }
/* 111:    */   
/* 112:    */   public void setRawSelectedIndex(int index)
/* 113:    */   {
/* 114:142 */     setSelectedIndex(index - ((ExtendedComboBoxModel)getModel()).getBase());
/* 115:    */   }
/* 116:    */   
/* 117:    */   public int getRawSelectedIndex()
/* 118:    */   {
/* 119:151 */     return getSelectedIndex() + ((ExtendedComboBoxModel)getModel()).getBase();
/* 120:    */   }
/* 121:    */   
/* 122:    */   public void setModel(ComboBoxModel model)
/* 123:    */   {
/* 124:162 */     model.addListDataListener(this.listener);
/* 125:163 */     super.setModel(model);
/* 126:    */   }
/* 127:    */   
/* 128:    */   public void fillUsingResources(String[] keys, Class mapSrc)
/* 129:    */   {
/* 130:173 */     String[] items = new String[keys.length];
/* 131:174 */     ResourceMap resourceMap = WPBDApp.getResourceMap(mapSrc);
/* 132:175 */     for (int i = 0; i < items.length; i++) {
/* 133:176 */       items[i] = resourceMap.getString(keys[i] + ".text", new Object[0]);
/* 134:    */     }
/* 135:178 */     ExtendedComboBoxModel model = new ExtendedComboBoxModel(items);
/* 136:179 */     setModel(model);
/* 137:    */   }
/* 138:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.ExtendedComboBox
 * JD-Core Version:    0.7.0.1
 */