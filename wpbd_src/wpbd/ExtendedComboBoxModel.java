/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.Arrays;
/*   6:    */ import javax.swing.AbstractListModel;
/*   7:    */ import javax.swing.MutableComboBoxModel;
/*   8:    */ 
/*   9:    */ public class ExtendedComboBoxModel
/*  10:    */   extends AbstractListModel
/*  11:    */   implements MutableComboBoxModel, Serializable
/*  12:    */ {
/*  13: 29 */   private int base = 0;
/*  14:    */   private Object selectedObject;
/*  15: 31 */   private ArrayList<Object> items = new ArrayList();
/*  16:    */   
/*  17:    */   public ExtendedComboBoxModel() {}
/*  18:    */   
/*  19:    */   public ExtendedComboBoxModel(Object[] items)
/*  20:    */   {
/*  21: 42 */     initialize(items);
/*  22:    */   }
/*  23:    */   
/*  24:    */   public final void initialize(Object[] items)
/*  25:    */   {
/*  26: 51 */     this.items.clear();
/*  27: 52 */     this.items.addAll(Arrays.asList(items));
/*  28: 53 */     if (getSize() > 0) {
/*  29: 54 */       this.selectedObject = getElementAt(0);
/*  30:    */     }
/*  31: 56 */     fireContentsChanged(this, 0, items.length - 1);
/*  32:    */   }
/*  33:    */   
/*  34:    */   public void setBase(int base)
/*  35:    */   {
/*  36: 66 */     if (base != this.base)
/*  37:    */     {
/*  38: 67 */       this.base = base;
/*  39: 69 */       while (this.items.size() < base) {
/*  40: 70 */         this.items.add(null);
/*  41:    */       }
/*  42: 72 */       int index = this.items.indexOf(this.selectedObject);
/*  43: 73 */       if ((0 <= index) && (index < base)) {
/*  44: 74 */         setSelectedItem(base < this.items.size() ? this.items.get(base) : null);
/*  45:    */       }
/*  46: 76 */       fireContentsChanged(this, 0, this.items.size() - base - 1);
/*  47:    */     }
/*  48:    */   }
/*  49:    */   
/*  50:    */   public int getBase()
/*  51:    */   {
/*  52: 87 */     return this.base;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public int getSize()
/*  56:    */   {
/*  57: 96 */     return this.items.size() - this.base;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public Object getElementAt(int index)
/*  61:    */   {
/*  62:106 */     index += this.base;
/*  63:107 */     if ((index >= 0) && (index < this.items.size())) {
/*  64:108 */       return this.items.get(index);
/*  65:    */     }
/*  66:110 */     return null;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public void addElement(Object anObject)
/*  70:    */   {
/*  71:120 */     this.items.add(anObject);
/*  72:121 */     fireIntervalAdded(this, this.items.size() - this.base - 1, this.items.size() - this.base - 1);
/*  73:122 */     if ((this.items.size() == this.base + 1) && (this.selectedObject == null) && (anObject != null)) {
/*  74:123 */       setSelectedItem(anObject);
/*  75:    */     }
/*  76:    */   }
/*  77:    */   
/*  78:    */   public int getIndexOf(Object anObject)
/*  79:    */   {
/*  80:134 */     int index = this.items.indexOf(anObject);
/*  81:135 */     return index < this.base ? -1 : index - this.base;
/*  82:    */   }
/*  83:    */   
/*  84:    */   public void removeElement(Object anObject)
/*  85:    */   {
/*  86:144 */     int index = getIndexOf(anObject);
/*  87:145 */     if (index != -1) {
/*  88:146 */       removeElementAt(index);
/*  89:    */     }
/*  90:    */   }
/*  91:    */   
/*  92:    */   public void insertElementAt(Object anObject, int index)
/*  93:    */   {
/*  94:157 */     this.items.add(index + this.base + 1, anObject);
/*  95:158 */     fireIntervalAdded(this, index, index);
/*  96:    */   }
/*  97:    */   
/*  98:    */   public void removeElementAt(int index)
/*  99:    */   {
/* 100:167 */     if (getElementAt(index) == this.selectedObject) {
/* 101:168 */       if (index == 0) {
/* 102:169 */         setSelectedItem(getSize() == 1 ? null : getElementAt(index + 1));
/* 103:    */       } else {
/* 104:171 */         setSelectedItem(getElementAt(index - 1));
/* 105:    */       }
/* 106:    */     }
/* 107:174 */     this.items.remove(index + this.base);
/* 108:175 */     fireIntervalRemoved(this, index, index);
/* 109:    */   }
/* 110:    */   
/* 111:    */   public void setSelectedItem(Object anObject)
/* 112:    */   {
/* 113:184 */     if (((this.selectedObject != null) && (!this.selectedObject.equals(anObject))) || ((this.selectedObject == null) && (anObject != null)))
/* 114:    */     {
/* 115:186 */       this.selectedObject = anObject;
/* 116:187 */       fireContentsChanged(this, -1, -1);
/* 117:    */     }
/* 118:    */   }
/* 119:    */   
/* 120:    */   public Object getSelectedItem()
/* 121:    */   {
/* 122:197 */     return this.selectedObject;
/* 123:    */   }
/* 124:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.ExtendedComboBoxModel
 * JD-Core Version:    0.7.0.1
 */