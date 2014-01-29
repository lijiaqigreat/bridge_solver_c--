/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.awt.Color;
/*   4:    */ import java.awt.Component;
/*   5:    */ import java.awt.Point;
/*   6:    */ import java.awt.Rectangle;
/*   7:    */ import java.awt.event.ActionEvent;
/*   8:    */ import java.awt.event.ActionListener;
/*   9:    */ import java.awt.event.KeyEvent;
/*  10:    */ import java.awt.event.MouseAdapter;
/*  11:    */ import java.awt.event.MouseEvent;
/*  12:    */ import javax.swing.ButtonModel;
/*  13:    */ import javax.swing.DefaultListCellRenderer;
/*  14:    */ import javax.swing.JList;
/*  15:    */ import javax.swing.JMenuItem;
/*  16:    */ import javax.swing.JPopupMenu;
/*  17:    */ import javax.swing.JScrollPane;
/*  18:    */ import javax.swing.JToggleButton;
/*  19:    */ import javax.swing.ListModel;
/*  20:    */ import javax.swing.ListSelectionModel;
/*  21:    */ import javax.swing.MenuElement;
/*  22:    */ import javax.swing.MenuSelectionManager;
/*  23:    */ import javax.swing.SwingUtilities;
/*  24:    */ import javax.swing.event.PopupMenuEvent;
/*  25:    */ import javax.swing.event.PopupMenuListener;
/*  26:    */ import javax.swing.undo.UndoableEdit;
/*  27:    */ import org.jdesktop.application.ResourceMap;
/*  28:    */ 
/*  29:    */ public class UndoRedoDropList
/*  30:    */   extends JPopupMenu
/*  31:    */ {
/*  32:    */   private final JToggleButton dropButton;
/*  33:    */   private final boolean redo;
/*  34:    */   private final ListMenuItem listMenuItem;
/*  35:    */   private final JMenuItem cancelMenuItem;
/*  36:    */   private final String undoOrRedo;
/*  37:    */   private final ExtendedUndoManager undoManager;
/*  38: 75 */   private final ResourceMap resourceMap = WPBDApp.getResourceMap(UndoRedoDropList.class);
/*  39:    */   
/*  40:    */   public UndoRedoDropList(JToggleButton button, ExtendedUndoManager undoMgr, boolean redoVsUndo)
/*  41:    */   {
/*  42: 86 */     this.undoManager = undoMgr;
/*  43: 87 */     this.redo = redoVsUndo;
/*  44: 88 */     this.dropButton = button;
/*  45: 89 */     this.listMenuItem = new ListMenuItem();
/*  46: 90 */     add(this.listMenuItem);
/*  47: 91 */     this.cancelMenuItem = new JMenuItem();
/*  48: 92 */     add(this.cancelMenuItem);
/*  49: 93 */     this.undoOrRedo = this.resourceMap.getString(this.redo ? "redo.text" : "undo.text", new Object[0]);
/*  50:    */     
/*  51: 95 */     this.cancelMenuItem.addActionListener(new ActionListener()
/*  52:    */     {
/*  53:    */       public void actionPerformed(ActionEvent e)
/*  54:    */       {
/*  55: 97 */         UndoRedoDropList.this.close();
/*  56:    */       }
/*  57:    */     });
/*  58:    */   }
/*  59:    */   
/*  60:    */   public void close()
/*  61:    */   {
/*  62:106 */     setVisible(false);
/*  63:107 */     this.dropButton.setSelected(false);
/*  64:    */   }
/*  65:    */   
/*  66:    */   private class ActionList
/*  67:    */     extends JList
/*  68:    */   {
/*  69:115 */     private int mouseOverIndex = -1;
/*  70:    */     
/*  71:    */     public ActionList()
/*  72:    */     {
/*  73:118 */       setPrototypeCellValue("Undo Redo Undo Redo Undo Redo");
/*  74:    */       
/*  75:120 */       setCellRenderer(new DefaultListCellRenderer()
/*  76:    */       {
/*  77:    */         public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
/*  78:    */         {
/*  79:124 */           return super.getListCellRendererComponent(list, (value instanceof UndoableEdit) ? ((UndoableEdit)value).getPresentationName() : value, index, isSelected, cellHasFocus);
/*  80:    */         }
/*  81:128 */       });
/*  82:129 */       addMouseListener(this.mouseListener);
/*  83:130 */       addMouseMotionListener(this.mouseListener);
/*  84:131 */       getSelectionModel().setSelectionMode(1);
/*  85:    */     }
/*  86:    */     
/*  87:    */     public Color getSelectionBackground()
/*  88:    */     {
/*  89:141 */       return new Color(224, 232, 248);
/*  90:    */     }
/*  91:    */     
/*  92:    */     public Color getSelectionForeground()
/*  93:    */     {
/*  94:151 */       return Color.BLACK;
/*  95:    */     }
/*  96:    */     
/*  97:154 */     private MouseAdapter mouseListener = new MouseAdapter()
/*  98:    */     {
/*  99:156 */       private final Point pt = new Point();
/* 100:    */       
/* 101:    */       public void mouseExited(MouseEvent e)
/* 102:    */       {
/* 103:160 */         if (UndoRedoDropList.ActionList.this.mouseOverIndex != -1)
/* 104:    */         {
/* 105:161 */           UndoRedoDropList.ActionList.this.getSelectionModel().clearSelection();
/* 106:162 */           UndoRedoDropList.this.cancelMenuItem.setText(UndoRedoDropList.this.resourceMap.getString("cancelButton.text", new Object[0]));
/* 107:163 */           UndoRedoDropList.ActionList.this.mouseOverIndex = -1;
/* 108:    */         }
/* 109:    */       }
/* 110:    */       
/* 111:    */       public void mouseMoved(MouseEvent e)
/* 112:    */       {
/* 113:169 */         this.pt.setLocation(e.getX(), e.getY());
/* 114:170 */         int index = UndoRedoDropList.ActionList.this.locationToIndex(this.pt);
/* 115:171 */         if (!UndoRedoDropList.ActionList.this.getCellBounds(index, index).contains(this.pt)) {
/* 116:172 */           index = -1;
/* 117:    */         }
/* 118:174 */         if (index != UndoRedoDropList.ActionList.this.mouseOverIndex)
/* 119:    */         {
/* 120:175 */           if (index == -1)
/* 121:    */           {
/* 122:176 */             UndoRedoDropList.ActionList.this.clearSelection();
/* 123:177 */             UndoRedoDropList.this.cancelMenuItem.setText(UndoRedoDropList.this.resourceMap.getString("cancelButton.text", new Object[0]));
/* 124:    */           }
/* 125:    */           else
/* 126:    */           {
/* 127:180 */             UndoRedoDropList.ActionList.this.getSelectionModel().setSelectionInterval(0, index);
/* 128:181 */             UndoRedoDropList.this.cancelMenuItem.setText(UndoRedoDropList.this.resourceMap.getString("messageButton.text", new Object[] { UndoRedoDropList.this.undoOrRedo, Integer.valueOf(index + 1) }));
/* 129:    */           }
/* 130:183 */           UndoRedoDropList.ActionList.this.mouseOverIndex = index;
/* 131:    */         }
/* 132:    */       }
/* 133:    */       
/* 134:    */       public void mousePressed(MouseEvent e)
/* 135:    */       {
/* 136:189 */         UndoRedoDropList.this.close();
/* 137:190 */         int index = UndoRedoDropList.ActionList.this.getMaxSelectionIndex();
/* 138:191 */         if (index >= 0)
/* 139:    */         {
/* 140:192 */           UndoableEdit edit = (UndoableEdit)UndoRedoDropList.ActionList.this.getModel().getElementAt(index);
/* 141:193 */           if (UndoRedoDropList.this.redo) {
/* 142:194 */             UndoRedoDropList.this.undoManager.redoTo(edit);
/* 143:    */           } else {
/* 144:197 */             UndoRedoDropList.this.undoManager.undoTo(edit);
/* 145:    */           }
/* 146:    */         }
/* 147:    */       }
/* 148:    */     };
/* 149:    */   }
/* 150:    */   
/* 151:    */   private class ListMenuItem
/* 152:    */     extends JScrollPane
/* 153:    */     implements MenuElement
/* 154:    */   {
/* 155:    */     private final JList actionList;
/* 156:218 */     private final Runnable closePopup = new Runnable()
/* 157:    */     {
/* 158:    */       public void run()
/* 159:    */       {
/* 160:220 */         if (!UndoRedoDropList.this.dropButton.getModel().isPressed()) {
/* 161:221 */           UndoRedoDropList.this.close();
/* 162:    */         }
/* 163:    */       }
/* 164:    */     };
/* 165:    */     
/* 166:    */     public ListMenuItem()
/* 167:    */     {
/* 168:227 */       super(31);
/* 169:228 */       this.actionList = new UndoRedoDropList.ActionList(UndoRedoDropList.this);
/* 170:229 */       setViewportView(this.actionList);
/* 171:230 */       UndoRedoDropList.this.addPopupMenuListener(new PopupMenuListener()
/* 172:    */       {
/* 173:    */         public void popupMenuWillBecomeVisible(PopupMenuEvent e)
/* 174:    */         {
/* 175:232 */           UndoRedoDropList.ListMenuItem.this.actionList.setModel(UndoRedoDropList.this.redo ? UndoRedoDropList.this.undoManager.getRedoListModel() : UndoRedoDropList.this.undoManager.getUndoListModel());
/* 176:233 */           UndoRedoDropList.ListMenuItem.this.actionList.setVisibleRowCount(Math.min(UndoRedoDropList.ListMenuItem.this.actionList.getModel().getSize(), 24));
/* 177:    */           
/* 178:235 */           UndoRedoDropList.ListMenuItem.this.actionList.invalidate();
/* 179:236 */           UndoRedoDropList.this.cancelMenuItem.setText(UndoRedoDropList.this.resourceMap.getString("cancelButton.text", new Object[0]));
/* 180:    */         }
/* 181:    */         
/* 182:    */         public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}
/* 183:    */         
/* 184:    */         public void popupMenuCanceled(PopupMenuEvent e)
/* 185:    */         {
/* 186:240 */           SwingUtilities.invokeLater(UndoRedoDropList.ListMenuItem.this.closePopup);
/* 187:    */         }
/* 188:    */       });
/* 189:    */     }
/* 190:    */     
/* 191:270 */     private final MenuElement[] nada = new MenuElement[0];
/* 192:    */     
/* 193:    */     public void processMouseEvent(MouseEvent event, MenuElement[] path, MenuSelectionManager manager) {}
/* 194:    */     
/* 195:    */     public void processKeyEvent(KeyEvent event, MenuElement[] path, MenuSelectionManager manager) {}
/* 196:    */     
/* 197:    */     public void menuSelectionChanged(boolean isIncluded) {}
/* 198:    */     
/* 199:    */     public MenuElement[] getSubElements()
/* 200:    */     {
/* 201:278 */       return this.nada;
/* 202:    */     }
/* 203:    */     
/* 204:    */     public Component getComponent()
/* 205:    */     {
/* 206:287 */       return this;
/* 207:    */     }
/* 208:    */   }
/* 209:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.UndoRedoDropList
 * JD-Core Version:    0.7.0.1
 */