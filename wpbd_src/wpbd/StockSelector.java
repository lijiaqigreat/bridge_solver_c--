/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.awt.event.ItemEvent;
/*   4:    */ import java.awt.event.ItemListener;
/*   5:    */ import java.util.ArrayList;
/*   6:    */ import java.util.Iterator;
/*   7:    */ import javax.swing.JComboBox;
/*   8:    */ import javax.swing.event.ChangeEvent;
/*   9:    */ import javax.swing.event.ChangeListener;
/*  10:    */ 
/*  11:    */ public class StockSelector
/*  12:    */ {
/*  13:    */   private final JComboBox materialBox;
/*  14:    */   private final JComboBox sectionBox;
/*  15:    */   private final JComboBox sizeBox;
/*  16:    */   private final ItemListener materialBoxSelectionListener;
/*  17:    */   private final ItemListener sectionBoxSelectionListener;
/*  18:    */   private final ItemListener sizeBoxSelectionListener;
/*  19: 37 */   private ArrayList<ChangeListener> changeListeners = new ArrayList();
/*  20:    */   
/*  21:    */   public StockSelector(JComboBox material, JComboBox section, JComboBox size)
/*  22:    */   {
/*  23: 47 */     this.materialBox = material;
/*  24: 48 */     this.sectionBox = section;
/*  25: 49 */     this.sizeBox = size;
/*  26:    */     
/*  27:    */ 
/*  28:    */ 
/*  29:    */ 
/*  30:    */ 
/*  31: 55 */     this.materialBoxSelectionListener = new ItemListener()
/*  32:    */     {
/*  33:    */       public void itemStateChanged(ItemEvent e)
/*  34:    */       {
/*  35: 58 */         if (e.getStateChange() == 1)
/*  36:    */         {
/*  37: 59 */           StockSelector.this.disable();
/*  38: 60 */           StockSelector.this.fireStateChanged();
/*  39: 61 */           StockSelector.this.enable();
/*  40:    */         }
/*  41:    */       }
/*  42: 64 */     };
/*  43: 65 */     this.sectionBoxSelectionListener = new ItemListener()
/*  44:    */     {
/*  45:    */       public void itemStateChanged(ItemEvent e)
/*  46:    */       {
/*  47: 68 */         if (e.getStateChange() == 1)
/*  48:    */         {
/*  49: 69 */           StockSelector.this.disable();
/*  50: 70 */           StockSelector.this.synchSizeBoxToSection();
/*  51: 71 */           StockSelector.this.fireStateChanged();
/*  52: 72 */           StockSelector.this.enable();
/*  53:    */         }
/*  54:    */       }
/*  55: 75 */     };
/*  56: 76 */     this.sizeBoxSelectionListener = new ItemListener()
/*  57:    */     {
/*  58:    */       public void itemStateChanged(ItemEvent e)
/*  59:    */       {
/*  60: 79 */         if (e.getStateChange() == 1)
/*  61:    */         {
/*  62: 80 */           StockSelector.this.disable();
/*  63: 81 */           StockSelector.this.fireStateChanged();
/*  64: 82 */           StockSelector.this.enable();
/*  65:    */         }
/*  66:    */       }
/*  67: 85 */     };
/*  68: 86 */     enable();
/*  69:    */   }
/*  70:    */   
/*  71:    */   public int getMaterialIndex()
/*  72:    */   {
/*  73: 95 */     return this.materialBox.getSelectedIndex();
/*  74:    */   }
/*  75:    */   
/*  76:    */   public int getSectionIndex()
/*  77:    */   {
/*  78:104 */     return this.sectionBox.getSelectedIndex();
/*  79:    */   }
/*  80:    */   
/*  81:    */   public int getSizeIndex()
/*  82:    */   {
/*  83:113 */     return this.sizeBox.getSelectedIndex();
/*  84:    */   }
/*  85:    */   
/*  86:    */   public void clear()
/*  87:    */   {
/*  88:120 */     this.materialBox.setSelectedIndex(-1);
/*  89:121 */     this.sectionBox.setSelectedIndex(-1);
/*  90:122 */     this.sizeBox.setSelectedIndex(-1);
/*  91:    */   }
/*  92:    */   
/*  93:    */   public void addChangeListener(ChangeListener listener)
/*  94:    */   {
/*  95:131 */     this.changeListeners.add(listener);
/*  96:    */   }
/*  97:    */   
/*  98:    */   public void removeChangeListener(ChangeListener listener)
/*  99:    */   {
/* 100:140 */     this.changeListeners.remove(listener);
/* 101:    */   }
/* 102:    */   
/* 103:    */   public int getAllowedShapeChanges()
/* 104:    */   {
/* 105:151 */     int sizeIndex = this.sizeBox.getSelectedIndex();
/* 106:152 */     if (sizeIndex < 0) {
/* 107:153 */       return 0;
/* 108:    */     }
/* 109:155 */     int mask = 0;
/* 110:156 */     if (sizeIndex > 0) {
/* 111:157 */       mask |= 0x2;
/* 112:    */     }
/* 113:159 */     if (sizeIndex < this.sizeBox.getItemCount() - 1) {
/* 114:160 */       mask |= 0x1;
/* 115:    */     }
/* 116:162 */     return mask;
/* 117:    */   }
/* 118:    */   
/* 119:    */   public void incrementSize(int inc)
/* 120:    */   {
/* 121:171 */     int index = this.sizeBox.getSelectedIndex();
/* 122:172 */     if ((index >= 0) && (0 <= index + inc) && (index + inc < this.sizeBox.getItemCount())) {
/* 123:173 */       this.sizeBox.setSelectedIndex(index + inc);
/* 124:    */     }
/* 125:    */   }
/* 126:    */   
/* 127:    */   public static class Descriptor
/* 128:    */   {
/* 129:    */     public int materialIndex;
/* 130:    */     public int sectionIndex;
/* 131:    */     public int sizeIndex;
/* 132:    */     
/* 133:    */     public Descriptor()
/* 134:    */     {
/* 135:198 */       this.materialIndex = (this.sectionIndex = this.sizeIndex = -1);
/* 136:    */     }
/* 137:    */     
/* 138:    */     public Descriptor(int materialIndex, int sectionIndex, int sizeIndex)
/* 139:    */     {
/* 140:209 */       this.materialIndex = materialIndex;
/* 141:210 */       this.sectionIndex = sectionIndex;
/* 142:211 */       this.sizeIndex = sizeIndex;
/* 143:    */     }
/* 144:    */     
/* 145:    */     public Descriptor(Member member)
/* 146:    */     {
/* 147:220 */       this.materialIndex = member.getMaterial().getIndex();
/* 148:221 */       this.sectionIndex = member.getShape().getSection().getIndex();
/* 149:222 */       this.sizeIndex = member.getShape().getSizeIndex();
/* 150:    */     }
/* 151:    */     
/* 152:    */     public boolean equals(Object obj)
/* 153:    */     {
/* 154:234 */       if ((obj instanceof Descriptor))
/* 155:    */       {
/* 156:235 */         Descriptor other = (Descriptor)obj;
/* 157:236 */         return (this.materialIndex == other.materialIndex) && (this.sectionIndex == other.sectionIndex) && (this.sizeIndex == other.sizeIndex);
/* 158:    */       }
/* 159:240 */       return false;
/* 160:    */     }
/* 161:    */     
/* 162:    */     public int hashCode()
/* 163:    */     {
/* 164:250 */       int hash = 7;
/* 165:251 */       hash = 79 * hash + this.materialIndex;
/* 166:252 */       hash = 79 * hash + this.sectionIndex;
/* 167:253 */       hash = 79 * hash + this.sizeIndex;
/* 168:254 */       return hash;
/* 169:    */     }
/* 170:    */   }
/* 171:    */   
/* 172:    */   private void synchSizeBoxToSection()
/* 173:    */   {
/* 174:262 */     ((Inventory.SizeBoxModel)this.sizeBox.getModel()).setSectionIndex(this.sectionBox.getSelectedIndex());
/* 175:    */   }
/* 176:    */   
/* 177:    */   public void match(StockSelector other)
/* 178:    */   {
/* 179:271 */     disable();
/* 180:272 */     this.materialBox.setSelectedIndex(other.getMaterialIndex());
/* 181:273 */     this.sectionBox.setSelectedIndex(other.getSectionIndex());
/* 182:274 */     synchSizeBoxToSection();
/* 183:275 */     this.sizeBox.setSelectedIndex(other.getSizeIndex());
/* 184:276 */     enable();
/* 185:    */   }
/* 186:    */   
/* 187:    */   private void initialize(Descriptor descriptor)
/* 188:    */   {
/* 189:280 */     if (descriptor != null)
/* 190:    */     {
/* 191:281 */       this.materialBox.setSelectedIndex(descriptor.materialIndex);
/* 192:282 */       this.sectionBox.setSelectedIndex(descriptor.sectionIndex);
/* 193:283 */       synchSizeBoxToSection();
/* 194:284 */       this.sizeBox.setSelectedIndex(descriptor.sizeIndex);
/* 195:    */     }
/* 196:    */   }
/* 197:    */   
/* 198:    */   public void matchSelection(EditableBridgeModel bridge)
/* 199:    */   {
/* 200:295 */     disable();
/* 201:296 */     Descriptor descriptor = bridge.getSelectedStock();
/* 202:297 */     if (descriptor == null)
/* 203:    */     {
/* 204:298 */       descriptor = bridge.getMostCommonStock();
/* 205:299 */       if (getMaterialIndex() != -1) {
/* 206:300 */         descriptor.materialIndex = getMaterialIndex();
/* 207:    */       }
/* 208:302 */       if (getSectionIndex() != -1) {
/* 209:303 */         descriptor.sectionIndex = getSectionIndex();
/* 210:    */       }
/* 211:305 */       if (getSizeIndex() != -1) {
/* 212:306 */         descriptor.sizeIndex = getSizeIndex();
/* 213:    */       }
/* 214:    */     }
/* 215:309 */     initialize(descriptor);
/* 216:310 */     enable();
/* 217:    */   }
/* 218:    */   
/* 219:    */   public void setMostCommonStockOf(BridgeModel bridge)
/* 220:    */   {
/* 221:319 */     initialize(bridge.getMostCommonStock());
/* 222:    */   }
/* 223:    */   
/* 224:    */   private void enable()
/* 225:    */   {
/* 226:323 */     this.materialBox.addItemListener(this.materialBoxSelectionListener);
/* 227:324 */     this.sectionBox.addItemListener(this.sectionBoxSelectionListener);
/* 228:325 */     this.sizeBox.addItemListener(this.sizeBoxSelectionListener);
/* 229:    */   }
/* 230:    */   
/* 231:    */   private void disable()
/* 232:    */   {
/* 233:329 */     this.sizeBox.removeItemListener(this.sizeBoxSelectionListener);
/* 234:330 */     this.sectionBox.removeItemListener(this.sectionBoxSelectionListener);
/* 235:331 */     this.materialBox.removeItemListener(this.materialBoxSelectionListener);
/* 236:    */   }
/* 237:    */   
/* 238:    */   private void fireStateChanged()
/* 239:    */   {
/* 240:335 */     Iterator<ChangeListener> e = new ArrayList(this.changeListeners).iterator();
/* 241:336 */     while (e.hasNext()) {
/* 242:337 */       ((ChangeListener)e.next()).stateChanged(new ChangeEvent(this));
/* 243:    */     }
/* 244:    */   }
/* 245:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.StockSelector
 * JD-Core Version:    0.7.0.1
 */