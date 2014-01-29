/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import javax.swing.AbstractListModel;
/*   4:    */ import javax.swing.ComboBoxModel;
/*   5:    */ 
/*   6:    */ public class Inventory
/*   7:    */ {
/*   8:    */   public static final int SHAPE_INCREASE_SIZE = 1;
/*   9:    */   public static final int SHAPE_DECREASE_SIZE = 2;
/*  10:    */   public static final double compressionResistanceFactor = 0.9D;
/*  11:    */   public static final double tensionResistanceFactor = 0.95D;
/*  12:    */   private static final double orderingFee = 1000.0D;
/*  13:    */   private static final double connectionFee = 500.0D;
/*  14: 55 */   private final CrossSection[] crossSections = { new BarCrossSection(), new TubeCrossSection() };
/*  15: 63 */   private final Material[] materials = { new Material(0, "Carbon Steel", "CS", 200000000.0D, 250000.0D, 7850.0D, new double[] { 4.5D, 6.3D }), new Material(1, "High-Strength Low-Alloy Steel", "HSS", 200000000.0D, 345000.0D, 7850.0D, new double[] { 5.0D, 7.0D }), new Material(2, "Quenched & Tempered Steel", "QTS", 200000000.0D, 485000.0D, 7850.0D, new double[] { 5.55D, 7.75D }) };
/*  16: 72 */   private final Shape[][] shapes = new Shape[this.crossSections.length][];
/*  17:    */   
/*  18:    */   public Inventory()
/*  19:    */   {
/*  20: 78 */     for (int i = 0; i < this.crossSections.length; i++) {
/*  21: 79 */       this.shapes[i] = this.crossSections[i].getShapes();
/*  22:    */     }
/*  23:    */   }
/*  24:    */   
/*  25:    */   public static double compressiveStrength(Material material, Shape shape, double length)
/*  26:    */   {
/*  27: 91 */     double Fy = material.getFy();
/*  28: 92 */     double area = shape.getArea();
/*  29: 93 */     double E = material.getE();
/*  30: 94 */     double moment = shape.getMoment();
/*  31: 95 */     double lambda = length * length * Fy * area / (9.8696044D * E * moment);
/*  32: 96 */     return lambda <= 2.25D ? 0.9D * Math.pow(0.66D, lambda) * Fy * area : 0.792D * Fy * area / lambda;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public static double tensileStrength(Material material, Shape shape)
/*  36:    */   {
/*  37:109 */     return 0.95D * material.getFy() * shape.getArea();
/*  38:    */   }
/*  39:    */   
/*  40:    */   public double getConnectionFee()
/*  41:    */   {
/*  42:118 */     return 500.0D;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public double getOrderingFee()
/*  46:    */   {
/*  47:127 */     return 1000.0D;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public Material getMaterial(int index)
/*  51:    */   {
/*  52:136 */     return this.materials[index];
/*  53:    */   }
/*  54:    */   
/*  55:    */   public int getNShapes(int sectionIndex)
/*  56:    */   {
/*  57:146 */     return this.shapes[sectionIndex].length;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public Shape getShape(int sectionIndex, int sizeIndex)
/*  61:    */   {
/*  62:157 */     return this.shapes[sectionIndex][sizeIndex];
/*  63:    */   }
/*  64:    */   
/*  65:    */   public Shape getShape(Shape reference, int sizeIncrement)
/*  66:    */   {
/*  67:170 */     int sectionIndex = reference.getSection().getIndex();
/*  68:171 */     int newSizeIndex = Math.min(this.shapes[sectionIndex].length - 1, Math.max(0, reference.getSizeIndex() + sizeIncrement));
/*  69:    */     
/*  70:173 */     return this.shapes[sectionIndex][newSizeIndex];
/*  71:    */   }
/*  72:    */   
/*  73:    */   public int getAllowedShapeChanges(Shape shape)
/*  74:    */   {
/*  75:185 */     int sizeIndex = shape.getSizeIndex();
/*  76:186 */     int mask = 0;
/*  77:187 */     if (sizeIndex > 0) {
/*  78:188 */       mask |= 0x2;
/*  79:    */     }
/*  80:190 */     if (sizeIndex < this.shapes[shape.getSection().getIndex()].length - 1) {
/*  81:191 */       mask |= 0x1;
/*  82:    */     }
/*  83:193 */     return mask;
/*  84:    */   }
/*  85:    */   
/*  86:    */   public ComboBoxModel getMaterialBoxModel()
/*  87:    */   {
/*  88:203 */     return new MaterialBoxModel(false);
/*  89:    */   }
/*  90:    */   
/*  91:    */   public ComboBoxModel getMaterialBoxModel(boolean shortForm)
/*  92:    */   {
/*  93:214 */     return new MaterialBoxModel(shortForm);
/*  94:    */   }
/*  95:    */   
/*  96:    */   public ComboBoxModel getSectionBoxModel()
/*  97:    */   {
/*  98:224 */     return new SectionBoxModel(false);
/*  99:    */   }
/* 100:    */   
/* 101:    */   public ComboBoxModel getSectionBoxModel(boolean shortForm)
/* 102:    */   {
/* 103:235 */     return new SectionBoxModel(shortForm);
/* 104:    */   }
/* 105:    */   
/* 106:    */   public ComboBoxModel getSizeBoxModel()
/* 107:    */   {
/* 108:245 */     return new SizeBoxModel(false);
/* 109:    */   }
/* 110:    */   
/* 111:    */   public ComboBoxModel getSizeBoxModel(boolean shortForm)
/* 112:    */   {
/* 113:256 */     return new SizeBoxModel(shortForm);
/* 114:    */   }
/* 115:    */   
/* 116:    */   private class MaterialBoxModel
/* 117:    */     extends AbstractListModel
/* 118:    */     implements ComboBoxModel
/* 119:    */   {
/* 120:    */     private Object selectedObject;
/* 121:    */     private boolean shortForm;
/* 122:    */     
/* 123:    */     public MaterialBoxModel(boolean shortForm)
/* 124:    */     {
/* 125:265 */       this.shortForm = shortForm;
/* 126:    */     }
/* 127:    */     
/* 128:    */     public int getSize()
/* 129:    */     {
/* 130:269 */       return Inventory.this.materials.length;
/* 131:    */     }
/* 132:    */     
/* 133:    */     public Object getElementAt(int index)
/* 134:    */     {
/* 135:273 */       return this.shortForm ? Inventory.this.materials[index].getShortName() : Inventory.this.materials[index].getName();
/* 136:    */     }
/* 137:    */     
/* 138:    */     public void setSelectedItem(Object anObject)
/* 139:    */     {
/* 140:277 */       if (!Utility.areEqual(anObject, this.selectedObject))
/* 141:    */       {
/* 142:278 */         this.selectedObject = anObject;
/* 143:279 */         fireContentsChanged(this, -1, -1);
/* 144:    */       }
/* 145:    */     }
/* 146:    */     
/* 147:    */     public Object getSelectedItem()
/* 148:    */     {
/* 149:284 */       return this.selectedObject;
/* 150:    */     }
/* 151:    */   }
/* 152:    */   
/* 153:    */   private class SectionBoxModel
/* 154:    */     extends AbstractListModel
/* 155:    */     implements ComboBoxModel
/* 156:    */   {
/* 157:    */     private Object selectedObject;
/* 158:    */     private boolean shortForm;
/* 159:    */     
/* 160:    */     public SectionBoxModel(boolean shortForm)
/* 161:    */     {
/* 162:294 */       this.shortForm = shortForm;
/* 163:    */     }
/* 164:    */     
/* 165:    */     public int getSize()
/* 166:    */     {
/* 167:298 */       return Inventory.this.crossSections.length;
/* 168:    */     }
/* 169:    */     
/* 170:    */     public Object getElementAt(int index)
/* 171:    */     {
/* 172:302 */       return this.shortForm ? Inventory.this.crossSections[index].shortName : Inventory.this.crossSections[index].name;
/* 173:    */     }
/* 174:    */     
/* 175:    */     public void setSelectedItem(Object anObject)
/* 176:    */     {
/* 177:306 */       if (!Utility.areEqual(anObject, this.selectedObject))
/* 178:    */       {
/* 179:307 */         this.selectedObject = anObject;
/* 180:308 */         fireContentsChanged(this, -1, -1);
/* 181:    */       }
/* 182:    */     }
/* 183:    */     
/* 184:    */     public Object getSelectedItem()
/* 185:    */     {
/* 186:313 */       return this.selectedObject;
/* 187:    */     }
/* 188:    */   }
/* 189:    */   
/* 190:    */   public class SizeBoxModel
/* 191:    */     extends AbstractListModel
/* 192:    */     implements ComboBoxModel
/* 193:    */   {
/* 194:322 */     private int sectionIndex = 0;
/* 195:    */     private boolean shortForm;
/* 196:324 */     private int selectedIndex = -1;
/* 197:    */     
/* 198:    */     public SizeBoxModel(boolean shortForm)
/* 199:    */     {
/* 200:332 */       this.shortForm = shortForm;
/* 201:    */     }
/* 202:    */     
/* 203:    */     public int getSectionIndex()
/* 204:    */     {
/* 205:341 */       return this.sectionIndex;
/* 206:    */     }
/* 207:    */     
/* 208:    */     public void setSectionIndex(int newSectionIndex)
/* 209:    */     {
/* 210:350 */       if (newSectionIndex != this.sectionIndex)
/* 211:    */       {
/* 212:351 */         int newLen = newSectionIndex == -1 ? 0 : Inventory.this.shapes[newSectionIndex].length;
/* 213:352 */         int oldLen = this.sectionIndex == -1 ? 0 : Inventory.this.shapes[this.sectionIndex].length;
/* 214:355 */         if (this.selectedIndex >= newLen) {
/* 215:356 */           this.selectedIndex = (newLen - 1);
/* 216:    */         }
/* 217:359 */         this.sectionIndex = newSectionIndex;
/* 218:362 */         if (newLen > oldLen)
/* 219:    */         {
/* 220:363 */           fireIntervalAdded(this, oldLen, newLen - 1);
/* 221:364 */           fireContentsChanged(this, 0, oldLen - 1);
/* 222:    */         }
/* 223:366 */         else if (newLen < oldLen)
/* 224:    */         {
/* 225:367 */           fireIntervalRemoved(this, newLen, oldLen - 1);
/* 226:368 */           fireContentsChanged(this, 0, newLen - 1);
/* 227:    */         }
/* 228:    */         else
/* 229:    */         {
/* 230:371 */           fireContentsChanged(this, 0, newLen - 1);
/* 231:    */         }
/* 232:    */       }
/* 233:    */     }
/* 234:    */     
/* 235:    */     public int getSize()
/* 236:    */     {
/* 237:382 */       return this.sectionIndex == -1 ? Inventory.this.shapes[0].length : Inventory.this.shapes[this.sectionIndex].length;
/* 238:    */     }
/* 239:    */     
/* 240:    */     public Object getElementAt(int index)
/* 241:    */     {
/* 242:393 */       int si = this.sectionIndex == -1 ? 0 : this.sectionIndex;
/* 243:394 */       return this.shortForm ? Integer.valueOf(Inventory.this.shapes[si][index].getNominalWidth()) : Inventory.this.shapes[si][index].getName();
/* 244:    */     }
/* 245:    */     
/* 246:    */     public int indexOf(Object element)
/* 247:    */     {
/* 248:404 */       for (int i = 0; i < getSize(); i++) {
/* 249:405 */         if (Utility.areEqual(element, getElementAt(i))) {
/* 250:406 */           return i;
/* 251:    */         }
/* 252:    */       }
/* 253:409 */       return -1;
/* 254:    */     }
/* 255:    */     
/* 256:    */     public void setSelectedItem(Object element)
/* 257:    */     {
/* 258:418 */       if (!Utility.areEqual(element, getSelectedItem()))
/* 259:    */       {
/* 260:419 */         int newSelectedIndex = indexOf(element);
/* 261:420 */         if (newSelectedIndex != this.selectedIndex)
/* 262:    */         {
/* 263:421 */           this.selectedIndex = newSelectedIndex;
/* 264:422 */           fireContentsChanged(this, -1, -1);
/* 265:    */         }
/* 266:    */       }
/* 267:    */     }
/* 268:    */     
/* 269:    */     public Object getSelectedItem()
/* 270:    */     {
/* 271:433 */       return this.selectedIndex >= 0 ? getElementAt(this.selectedIndex) : null;
/* 272:    */     }
/* 273:    */   }
/* 274:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.Inventory
 * JD-Core Version:    0.7.0.1
 */