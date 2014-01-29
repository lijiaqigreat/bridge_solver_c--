/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.awt.Component;
/*   4:    */ import java.awt.event.ActionEvent;
/*   5:    */ import java.awt.event.ActionListener;
/*   6:    */ import java.io.File;
/*   7:    */ import java.util.Iterator;
/*   8:    */ import java.util.Vector;
/*   9:    */ import javax.swing.JMenu;
/*  10:    */ import javax.swing.JMenuItem;
/*  11:    */ import javax.swing.JPopupMenu;
/*  12:    */ import javax.swing.JPopupMenu.Separator;
/*  13:    */ import javax.swing.JSeparator;
/*  14:    */ 
/*  15:    */ public class RecentFileManager
/*  16:    */   implements ActionListener
/*  17:    */ {
/*  18:    */   private Vector<String> recentPaths;
/*  19: 37 */   private int maxFileCount = 0;
/*  20:    */   private static final String recentFileStorageName = "recentFiles.xml";
/*  21:    */   private Listener listener;
/*  22:    */   
/*  23:    */   public RecentFileManager(int maxFileCount, Listener listener)
/*  24:    */   {
/*  25: 56 */     this.maxFileCount = maxFileCount;
/*  26: 57 */     this.listener = listener;
/*  27: 58 */     load();
/*  28:    */   }
/*  29:    */   
/*  30:    */   public final void load()
/*  31:    */   {
/*  32: 66 */     Vector<String> paths = (Vector)WPBDApp.loadFromLocalStorage("recentFiles.xml");
/*  33: 67 */     if (paths != null)
/*  34:    */     {
/*  35: 69 */       Iterator<String> i = paths.iterator();
/*  36: 70 */       while (i.hasNext()) {
/*  37: 71 */         if (!new File((String)i.next()).canRead()) {
/*  38: 72 */           i.remove();
/*  39:    */         }
/*  40:    */       }
/*  41:    */     }
/*  42: 76 */     this.recentPaths = (paths == null ? new Vector(this.maxFileCount) : paths);
/*  43:    */   }
/*  44:    */   
/*  45:    */   public void save()
/*  46:    */   {
/*  47: 83 */     WPBDApp.saveToLocalStorage(this.recentPaths, "recentFiles.xml");
/*  48:    */   }
/*  49:    */   
/*  50:    */   public void addNew(File file)
/*  51:    */   {
/*  52: 93 */     if ((file != null) && (file.canRead()))
/*  53:    */     {
/*  54: 94 */       String path = file.getAbsolutePath();
/*  55: 95 */       this.recentPaths.remove(path);
/*  56: 96 */       this.recentPaths.add(0, path);
/*  57: 97 */       while (this.recentPaths.size() > this.maxFileCount) {
/*  58: 98 */         this.recentPaths.remove(this.recentPaths.size() - 1);
/*  59:    */       }
/*  60:    */     }
/*  61:    */   }
/*  62:    */   
/*  63:    */   private class Separator
/*  64:    */     extends JPopupMenu.Separator
/*  65:    */   {
/*  66:    */     private Separator() {}
/*  67:    */   }
/*  68:    */   
/*  69:    */   private class MenuItem
/*  70:    */     extends JMenuItem
/*  71:    */   {
/*  72:    */     final int index;
/*  73:    */     
/*  74:    */     public MenuItem(String s, int index)
/*  75:    */     {
/*  76:117 */       super(index + 49);
/*  77:118 */       this.index = index;
/*  78:    */     }
/*  79:    */   }
/*  80:    */   
/*  81:    */   public String getSimplifiedPath(String filePath, String basePath)
/*  82:    */   {
/*  83:137 */     if (basePath == null) {
/*  84:138 */       return filePath;
/*  85:    */     }
/*  86:140 */     String filePathCaseAdjusted = filePath;
/*  87:141 */     String basePathCaseAdjusted = basePath;
/*  88:142 */     if (System.getProperty("os.name").contains("Windows"))
/*  89:    */     {
/*  90:143 */       filePathCaseAdjusted = filePath.toLowerCase();
/*  91:144 */       basePathCaseAdjusted = basePath.toLowerCase();
/*  92:    */     }
/*  93:147 */     return filePathCaseAdjusted.startsWith(basePathCaseAdjusted) ? filePath.substring(basePath.length() + 1) : filePath;
/*  94:    */   }
/*  95:    */   
/*  96:    */   public void removeRecentFileMenuItems(JMenu menu)
/*  97:    */   {
/*  98:157 */     Component[] items = menu.getPopupMenu().getComponents();
/*  99:158 */     for (int i = 0; i < items.length; i++) {
/* 100:159 */       if (((items[i] instanceof MenuItem)) || ((items[i] instanceof Separator))) {
/* 101:160 */         menu.remove(items[i]);
/* 102:    */       }
/* 103:    */     }
/* 104:    */   }
/* 105:    */   
/* 106:    */   public void addRecentFileMenuItemsAt(JMenu menu, int atIndex, File base)
/* 107:    */   {
/* 108:176 */     removeRecentFileMenuItems(menu);
/* 109:177 */     if (this.recentPaths.size() > 0)
/* 110:    */     {
/* 111:178 */       JPopupMenu popup = menu.getPopupMenu();
/* 112:179 */       if (atIndex > 0) {
/* 113:180 */         popup.insert(new Separator(null), atIndex++);
/* 114:    */       }
/* 115:182 */       Iterator<String> e = this.recentPaths.iterator();
/* 116:183 */       int i = 0;
/* 117:184 */       while (e.hasNext())
/* 118:    */       {
/* 119:185 */         MenuItem menuItem = new MenuItem(getSimplifiedPath((String)e.next(), base.getAbsolutePath()), i++);
/* 120:186 */         menuItem.addActionListener(this);
/* 121:187 */         popup.insert(menuItem, atIndex++);
/* 122:    */       }
/* 123:    */     }
/* 124:    */   }
/* 125:    */   
/* 126:    */   public void addRecentFileMenuItemsAtSep(JMenu menu, int atSepCount, File base)
/* 127:    */   {
/* 128:202 */     int sepCount = 0;
/* 129:203 */     JPopupMenu popup = menu.getPopupMenu();
/* 130:204 */     int itemCount = popup.getComponentCount();
/* 131:205 */     for (int index = 0; index < itemCount; index++)
/* 132:    */     {
/* 133:206 */       Component component = popup.getComponent(index);
/* 134:207 */       if ((component instanceof JSeparator)) {
/* 135:208 */         sepCount++;
/* 136:    */       }
/* 137:210 */       if (sepCount == atSepCount)
/* 138:    */       {
/* 139:211 */         addRecentFileMenuItemsAt(menu, index, base);
/* 140:212 */         return;
/* 141:    */       }
/* 142:    */     }
/* 143:216 */     removeRecentFileMenuItems(menu);
/* 144:    */   }
/* 145:    */   
/* 146:    */   public void actionPerformed(ActionEvent e)
/* 147:    */   {
/* 148:226 */     MenuItem menuItem = (MenuItem)e.getSource();
/* 149:227 */     String path = (String)this.recentPaths.get(menuItem.index);
/* 150:228 */     this.recentPaths.remove(menuItem.index);
/* 151:229 */     this.recentPaths.add(0, path);
/* 152:230 */     this.listener.openRecentFile(new File(path));
/* 153:    */   }
/* 154:    */   
/* 155:    */   public static abstract interface Listener
/* 156:    */   {
/* 157:    */     public abstract void openRecentFile(File paramFile);
/* 158:    */   }
/* 159:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.RecentFileManager
 * JD-Core Version:    0.7.0.1
 */