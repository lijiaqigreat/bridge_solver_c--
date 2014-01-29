/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import javax.swing.undo.AbstractUndoableEdit;
/*   5:    */ import javax.swing.undo.CannotRedoException;
/*   6:    */ import javax.swing.undo.CannotUndoException;
/*   7:    */ import org.jdesktop.application.ResourceMap;
/*   8:    */ 
/*   9:    */ public abstract class EditCommand
/*  10:    */   extends AbstractUndoableEdit
/*  11:    */ {
/*  12:    */   protected EditableBridgeModel bridge;
/*  13:    */   protected ExtendedUndoManager undoManager;
/*  14: 41 */   protected static final ResourceMap resourceMap = WPBDApp.getResourceMap(EditCommand.class);
/*  15: 45 */   protected String presentationName = defaultPresentationName;
/*  16: 46 */   private static final String defaultPresentationName = getString("command.text");
/*  17:    */   
/*  18:    */   abstract void go();
/*  19:    */   
/*  20:    */   abstract void goBack();
/*  21:    */   
/*  22:    */   public EditCommand(EditableBridgeModel bridge)
/*  23:    */   {
/*  24: 65 */     this.bridge = bridge;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public static String getString(String key)
/*  28:    */   {
/*  29: 75 */     return resourceMap.getString(key, new Object[0]);
/*  30:    */   }
/*  31:    */   
/*  32:    */   public String getPresentationName()
/*  33:    */   {
/*  34: 80 */     return this.presentationName;
/*  35:    */   }
/*  36:    */   
/*  37:    */   int execute(ExtendedUndoManager undoManager)
/*  38:    */   {
/*  39: 90 */     this.undoManager = undoManager;
/*  40: 91 */     go();
/*  41: 92 */     undoManager.addEdit(this);
/*  42:    */     
/*  43: 94 */     undoManager.fireAfter(this);
/*  44: 95 */     return 0;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public void redo()
/*  48:    */     throws CannotRedoException
/*  49:    */   {
/*  50:100 */     super.redo();
/*  51:101 */     go();
/*  52:102 */     this.undoManager.fireAfter(this);
/*  53:    */   }
/*  54:    */   
/*  55:    */   public void undo()
/*  56:    */     throws CannotUndoException
/*  57:    */   {
/*  58:107 */     super.undo();
/*  59:108 */     goBack();
/*  60:109 */     this.undoManager.fireAfter(this);
/*  61:    */   }
/*  62:    */   
/*  63:    */   public void die()
/*  64:    */   {
/*  65:114 */     super.die();
/*  66:115 */     this.undoManager = null;
/*  67:    */   }
/*  68:    */   
/*  69:    */   protected static String getMembersMessage(String key, Member[] members)
/*  70:    */   {
/*  71:132 */     if (members.length <= 0) {
/*  72:133 */       return getString(key) + ".";
/*  73:    */     }
/*  74:135 */     if (members.length == 1) {
/*  75:136 */       return getString(key) + " " + members[0].getNumber() + ".";
/*  76:    */     }
/*  77:138 */     StringBuilder s = new StringBuilder(64);
/*  78:139 */     s.append(getString(key + ".many"));
/*  79:140 */     s.append(" ");
/*  80:141 */     s.append(members[0].getNumber());
/*  81:142 */     int i = 1;
/*  82:143 */     while (i < members.length - 1)
/*  83:    */     {
/*  84:144 */       s.append(", ");
/*  85:145 */       s.append(members[(i++)].getNumber());
/*  86:    */     }
/*  87:147 */     s.append(" ");
/*  88:148 */     s.append(getString("and.text"));
/*  89:149 */     s.append(" ");
/*  90:150 */     s.append(members[i].getNumber());
/*  91:151 */     s.append(".");
/*  92:152 */     return s.toString();
/*  93:    */   }
/*  94:    */   
/*  95:    */   private static void setSize(ArrayList<?> a, int size)
/*  96:    */   {
/*  97:158 */     while (a.size() < size) {
/*  98:159 */       a.add(null);
/*  99:    */     }
/* 100:161 */     while (a.size() > size) {
/* 101:162 */       a.remove(a.size() - 1);
/* 102:    */     }
/* 103:    */   }
/* 104:    */   
/* 105:    */   protected static <T extends Editable> void insert(ArrayList<T> v, T[] items)
/* 106:    */   {
/* 107:176 */     int oldBmSize = v.size();
/* 108:177 */     int newBmSize = oldBmSize + items.length;
/* 109:178 */     setSize(v, newBmSize);
/* 110:179 */     int iSrc = oldBmSize - 1;
/* 111:180 */     int iDst = newBmSize - 1;
/* 112:181 */     for (int iCmd = items.length - 1; iCmd >= 0; iCmd--)
/* 113:    */     {
/* 114:182 */       while (iDst > items[iCmd].getIndex())
/* 115:    */       {
/* 116:183 */         ((Editable)v.get(iSrc)).setIndex(iDst);
/* 117:184 */         v.set(iDst--, v.get(iSrc--));
/* 118:    */       }
/* 119:186 */       v.set(iDst--, items[iCmd]);
/* 120:    */     }
/* 121:    */   }
/* 122:    */   
/* 123:    */   protected static <T extends Editable> void delete(ArrayList<T> v, T[] items)
/* 124:    */   {
/* 125:200 */     int oldBmSize = v.size();
/* 126:201 */     int newBmSize = oldBmSize - items.length;
/* 127:202 */     int iDst = 0;
/* 128:203 */     int iSrc = 0;
/* 129:204 */     for (int iCmd = 0; iCmd < items.length; iCmd++)
/* 130:    */     {
/* 131:205 */       while (iSrc < items[iCmd].getIndex())
/* 132:    */       {
/* 133:206 */         ((Editable)v.get(iSrc)).setIndex(iDst);
/* 134:207 */         v.set(iDst++, v.get(iSrc++));
/* 135:    */       }
/* 136:209 */       items[iCmd] = ((Editable)v.get(iSrc++));
/* 137:    */     }
/* 138:211 */     while (iSrc < oldBmSize)
/* 139:    */     {
/* 140:212 */       ((Editable)v.get(iSrc)).setIndex(iDst);
/* 141:213 */       v.set(iDst++, v.get(iSrc++));
/* 142:    */     }
/* 143:215 */     setSize(v, newBmSize);
/* 144:    */   }
/* 145:    */   
/* 146:    */   protected static <T extends Editable> void exchange(ArrayList<T> v, T item)
/* 147:    */   {
/* 148:226 */     ((Editable)v.get(item.getIndex())).swapContents(item);
/* 149:    */   }
/* 150:    */   
/* 151:    */   protected static <T extends Editable> void exchange(ArrayList<T> v, T[] items)
/* 152:    */   {
/* 153:237 */     for (int i = 0; i < items.length; i++) {
/* 154:238 */       ((Editable)v.get(items[i].getIndex())).swapContents(items[i]);
/* 155:    */     }
/* 156:    */   }
/* 157:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.EditCommand
 * JD-Core Version:    0.7.0.1
 */