/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.awt.Component;
/*   4:    */ import java.awt.Container;
/*   5:    */ import java.awt.Dimension;
/*   6:    */ import java.awt.Frame;
/*   7:    */ import java.awt.event.ActionEvent;
/*   8:    */ import java.awt.event.ActionListener;
/*   9:    */ import java.awt.event.MouseAdapter;
/*  10:    */ import java.awt.event.MouseEvent;
/*  11:    */ import javax.swing.BorderFactory;
/*  12:    */ import javax.swing.GroupLayout;
/*  13:    */ import javax.swing.GroupLayout.Alignment;
/*  14:    */ import javax.swing.GroupLayout.ParallelGroup;
/*  15:    */ import javax.swing.GroupLayout.SequentialGroup;
/*  16:    */ import javax.swing.Icon;
/*  17:    */ import javax.swing.JButton;
/*  18:    */ import javax.swing.JDialog;
/*  19:    */ import javax.swing.JLabel;
/*  20:    */ import javax.swing.JPanel;
/*  21:    */ import javax.swing.JRootPane;
/*  22:    */ import javax.swing.JScrollPane;
/*  23:    */ import javax.swing.JTabbedPane;
/*  24:    */ import javax.swing.JTable;
/*  25:    */ import javax.swing.JTextPane;
/*  26:    */ import javax.swing.JTree;
/*  27:    */ import javax.swing.LayoutStyle.ComponentPlacement;
/*  28:    */ import javax.swing.ListSelectionModel;
/*  29:    */ import javax.swing.event.ListSelectionEvent;
/*  30:    */ import javax.swing.event.ListSelectionListener;
/*  31:    */ import javax.swing.event.TreeSelectionEvent;
/*  32:    */ import javax.swing.event.TreeSelectionListener;
/*  33:    */ import javax.swing.table.DefaultTableCellRenderer;
/*  34:    */ import javax.swing.table.JTableHeader;
/*  35:    */ import javax.swing.table.TableCellRenderer;
/*  36:    */ import javax.swing.table.TableColumn;
/*  37:    */ import javax.swing.table.TableColumnModel;
/*  38:    */ import javax.swing.tree.DefaultTreeCellRenderer;
/*  39:    */ import javax.swing.tree.DefaultTreeModel;
/*  40:    */ import javax.swing.tree.TreeCellRenderer;
/*  41:    */ import javax.swing.tree.TreePath;
/*  42:    */ import javax.swing.tree.TreeSelectionModel;
/*  43:    */ import org.jdesktop.application.Application;
/*  44:    */ import org.jdesktop.application.ApplicationContext;
/*  45:    */ import org.jdesktop.application.ResourceMap;
/*  46:    */ 
/*  47:    */ public class DesignIterationDialog
/*  48:    */   extends JDialog
/*  49:    */ {
/*  50:    */   private final DesignIterationTableModel designIterationTableModel;
/*  51:    */   private final DefaultTreeModel designIterationTreeModel;
/*  52: 37 */   private boolean ok = false;
/*  53:    */   private JButton cancelButton;
/*  54:    */   private JButton okButton;
/*  55:    */   private JLabel previewCartoon;
/*  56:    */   private JLabel previewLabel;
/*  57:    */   private JLabel selectLabel;
/*  58:    */   private JTable selectList;
/*  59:    */   private JScrollPane selectListScroll;
/*  60:    */   private JTree selectTree;
/*  61:    */   private JScrollPane selectTreeScroll;
/*  62:    */   private JTextPane tipPane;
/*  63:    */   private JPanel tipPanel;
/*  64:    */   private JTabbedPane viewTabs;
/*  65:    */   
/*  66:    */   public DesignIterationDialog(Frame parent, final EditableBridgeModel bridge)
/*  67:    */   {
/*  68: 47 */     super(parent, true);
/*  69: 48 */     this.designIterationTableModel = new DesignIterationTableModel(bridge);
/*  70: 49 */     this.designIterationTreeModel = new DefaultTreeModel(bridge.getDesignIterationTreeRoot());
/*  71: 50 */     initComponents();
/*  72: 51 */     getRootPane().setDefaultButton(this.okButton);
/*  73:    */     
/*  74:    */ 
/*  75: 54 */     ((DefaultTableCellRenderer)this.selectList.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(0);
/*  76:    */     
/*  77:    */ 
/*  78: 57 */     TableCellRenderer renderer = new DefaultTableCellRenderer()
/*  79:    */     {
/*  80:    */       public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
/*  81:    */       {
/*  82: 61 */         JLabel cellLabel = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
/*  83:    */         
/*  84:    */ 
/*  85:    */ 
/*  86:    */ 
/*  87:    */ 
/*  88: 67 */         cellLabel.setIcon(null);
/*  89: 68 */         switch (column)
/*  90:    */         {
/*  91:    */         case 0: 
/*  92: 70 */           setHorizontalAlignment(0);
/*  93: 71 */           cellLabel.setIcon((Icon)value);
/*  94: 72 */           cellLabel.setText(null);
/*  95: 73 */           break;
/*  96:    */         case 1: 
/*  97: 75 */           setHorizontalAlignment(0);
/*  98: 76 */           break;
/*  99:    */         case 2: 
/* 100: 78 */           setHorizontalAlignment(4);
/* 101: 79 */           break;
/* 102:    */         case 3: 
/* 103: 81 */           setHorizontalAlignment(10);
/* 104:    */         }
/* 105: 84 */         return this;
/* 106:    */       }
/* 107:    */     };
/* 108: 88 */     for (int i = 0; i < this.selectList.getColumnCount(); i++) {
/* 109: 89 */       this.selectList.getColumnModel().getColumn(i).setCellRenderer(renderer);
/* 110:    */     }
/* 111: 91 */     this.selectList.getSelectionModel().addListSelectionListener(new ListSelectionListener()
/* 112:    */     {
/* 113:    */       public void valueChanged(ListSelectionEvent e)
/* 114:    */       {
/* 115: 93 */         if (!e.getValueIsAdjusting())
/* 116:    */         {
/* 117: 94 */           int index = DesignIterationDialog.this.selectList.getSelectedRow();
/* 118: 95 */           if (index >= 0)
/* 119:    */           {
/* 120: 96 */             DesignIterationDialog.this.designIterationTableModel.loadCartoon(index);
/* 121: 97 */             DesignIterationDialog.this.previewCartoon.repaint();
/* 122: 98 */             DesignIteration iteration = bridge.getDesignIteration(index);
/* 123: 99 */             DesignIterationDialog.this.selectTree.setSelectionPath(new TreePath(iteration.getPath()));
/* 124:    */           }
/* 125:    */         }
/* 126:    */       }
/* 127:105 */     });
/* 128:106 */     TreeCellRenderer treeRenderer = new DefaultTreeCellRenderer()
/* 129:    */     {
/* 130:    */       public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus)
/* 131:    */       {
/* 132:110 */         super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
/* 133:111 */         DesignIteration designIteration = (DesignIteration)value;
/* 134:112 */         setIcon(IconFactory.bridgeStatus(designIteration.getBridgeStatus()));
/* 135:113 */         return this;
/* 136:    */       }
/* 137:115 */     };
/* 138:116 */     this.selectTree.setCellRenderer(treeRenderer);
/* 139:    */     
/* 140:118 */     this.selectTree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener()
/* 141:    */     {
/* 142:    */       public void valueChanged(TreeSelectionEvent e)
/* 143:    */       {
/* 144:120 */         int index = bridge.getDesignIterationIndex(e.getPath().getLastPathComponent());
/* 145:121 */         DesignIterationDialog.this.selectList.getSelectionModel().setSelectionInterval(index, index);
/* 146:    */       }
/* 147:    */     });
/* 148:    */   }
/* 149:    */   
/* 150:    */   public boolean isOk()
/* 151:    */   {
/* 152:132 */     return this.ok;
/* 153:    */   }
/* 154:    */   
/* 155:    */   public void loadSelectedIteration()
/* 156:    */   {
/* 157:139 */     this.designIterationTableModel.getBridge().loadIteration(this.selectList.getSelectedRow());
/* 158:    */   }
/* 159:    */   
/* 160:    */   public void setVisible(boolean b)
/* 161:    */   {
/* 162:150 */     if (b)
/* 163:    */     {
/* 164:151 */       this.ok = false;
/* 165:152 */       this.designIterationTableModel.getBridge().saveSnapshot();
/* 166:153 */       ((DefaultTreeModel)this.selectTree.getModel()).reload();
/* 167:154 */       int index = this.designIterationTableModel.getBridge().getCurrentIterationIndex();
/* 168:156 */       if (index != this.selectList.getSelectedRow())
/* 169:    */       {
/* 170:157 */         this.selectList.setRowSelectionInterval(index, index);
/* 171:    */       }
/* 172:    */       else
/* 173:    */       {
/* 174:161 */         DesignIteration iteration = this.designIterationTableModel.getBridge().getDesignIteration(index);
/* 175:162 */         this.selectTree.setSelectionPath(new TreePath(iteration.getPath()));
/* 176:    */       }
/* 177:164 */       this.designIterationTableModel.loadCartoon(index);
/* 178:165 */       AutofitTableColumns.autoResizeTable(this.selectList, true, 4);
/* 179:167 */       if (this.viewTabs.getSelectedIndex() == 0)
/* 180:    */       {
/* 181:168 */         this.selectList.scrollRectToVisible(this.selectList.getCellRect(this.selectList.getSelectedRow(), 0, true));
/* 182:169 */         this.selectList.requestFocusInWindow();
/* 183:    */       }
/* 184:    */       else
/* 185:    */       {
/* 186:172 */         this.selectTree.scrollPathToVisible(this.selectTree.getSelectionPath());
/* 187:173 */         this.selectTree.requestFocusInWindow();
/* 188:    */       }
/* 189:    */     }
/* 190:176 */     super.setVisible(b);
/* 191:    */   }
/* 192:    */   
/* 193:    */   private void initComponents()
/* 194:    */   {
/* 195:183 */     this.selectLabel = new JLabel();
/* 196:184 */     this.tipPanel = new JPanel();
/* 197:185 */     this.tipPane = new TipTextPane();
/* 198:186 */     this.okButton = new JButton();
/* 199:187 */     this.cancelButton = new JButton();
/* 200:188 */     this.previewLabel = new JLabel();
/* 201:189 */     this.previewCartoon = this.designIterationTableModel.getBridgeView().getDrawing(2.0D);
/* 202:    */     
/* 203:191 */     this.viewTabs = new JTabbedPane();
/* 204:192 */     this.selectListScroll = new JScrollPane();
/* 205:193 */     this.selectList = new JTable();
/* 206:194 */     this.selectTreeScroll = new JScrollPane();
/* 207:195 */     this.selectTree = new JTree(this.designIterationTreeModel);
/* 208:    */     
/* 209:197 */     ResourceMap resourceMap = ((WPBDApp)Application.getInstance(WPBDApp.class)).getContext().getResourceMap(DesignIterationDialog.class);
/* 210:198 */     setTitle(resourceMap.getString("Form.title", new Object[0]));
/* 211:199 */     setName("Form");
/* 212:    */     
/* 213:201 */     this.selectLabel.setText(resourceMap.getString("selectLabel.text", new Object[0]));
/* 214:202 */     this.selectLabel.setName("selectLabel");
/* 215:    */     
/* 216:204 */     this.tipPanel.setBorder(BorderFactory.createTitledBorder(resourceMap.getString("tipPanel.border.title", new Object[0])));
/* 217:205 */     this.tipPanel.setName("tipPanel");
/* 218:    */     
/* 219:207 */     this.tipPane.setBorder(null);
/* 220:208 */     this.tipPane.setText(resourceMap.getString("tipPane.text", new Object[0]));
/* 221:209 */     this.tipPane.setName("tipPane");
/* 222:    */     
/* 223:211 */     GroupLayout tipPanelLayout = new GroupLayout(this.tipPanel);
/* 224:212 */     this.tipPanel.setLayout(tipPanelLayout);
/* 225:213 */     tipPanelLayout.setHorizontalGroup(tipPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(tipPanelLayout.createSequentialGroup().addContainerGap().addComponent(this.tipPane, -1, 185, 32767).addContainerGap()));
/* 226:    */     
/* 227:    */ 
/* 228:    */ 
/* 229:    */ 
/* 230:    */ 
/* 231:    */ 
/* 232:220 */     tipPanelLayout.setVerticalGroup(tipPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(tipPanelLayout.createSequentialGroup().addComponent(this.tipPane).addContainerGap()));
/* 233:    */     
/* 234:    */ 
/* 235:    */ 
/* 236:    */ 
/* 237:    */ 
/* 238:    */ 
/* 239:227 */     this.okButton.setText(resourceMap.getString("okButton.text", new Object[0]));
/* 240:228 */     this.okButton.setName("okButton");
/* 241:229 */     this.okButton.addActionListener(new ActionListener()
/* 242:    */     {
/* 243:    */       public void actionPerformed(ActionEvent evt)
/* 244:    */       {
/* 245:231 */         DesignIterationDialog.this.okButtonActionPerformed(evt);
/* 246:    */       }
/* 247:234 */     });
/* 248:235 */     this.cancelButton.setText(resourceMap.getString("cancelButton.text", new Object[0]));
/* 249:236 */     this.cancelButton.setName("cancelButton");
/* 250:237 */     this.cancelButton.addActionListener(new ActionListener()
/* 251:    */     {
/* 252:    */       public void actionPerformed(ActionEvent evt)
/* 253:    */       {
/* 254:239 */         DesignIterationDialog.this.cancelButtonActionPerformed(evt);
/* 255:    */       }
/* 256:242 */     });
/* 257:243 */     this.previewLabel.setText(resourceMap.getString("previewLabel.text", new Object[0]));
/* 258:244 */     this.previewLabel.setName("previewLabel");
/* 259:    */     
/* 260:246 */     this.previewCartoon.setText(null);
/* 261:247 */     this.previewCartoon.setBorder(BorderFactory.createBevelBorder(1));
/* 262:248 */     this.previewCartoon.setName("previewCartoon");
/* 263:    */     
/* 264:250 */     this.viewTabs.setName("viewTabs");
/* 265:    */     
/* 266:252 */     this.selectListScroll.setName("selectListScroll");
/* 267:    */     
/* 268:254 */     this.selectList.setModel(this.designIterationTableModel);
/* 269:255 */     this.selectList.setFillsViewportHeight(true);
/* 270:256 */     this.selectList.setIntercellSpacing(new Dimension(0, 2));
/* 271:257 */     this.selectList.setName("selectList");
/* 272:258 */     this.selectList.setSelectionMode(0);
/* 273:259 */     this.selectList.setShowHorizontalLines(false);
/* 274:260 */     this.selectList.setShowVerticalLines(false);
/* 275:261 */     this.selectList.addMouseListener(new MouseAdapter()
/* 276:    */     {
/* 277:    */       public void mouseClicked(MouseEvent evt)
/* 278:    */       {
/* 279:263 */         DesignIterationDialog.this.selectListMouseClicked(evt);
/* 280:    */       }
/* 281:265 */     });
/* 282:266 */     this.selectListScroll.setViewportView(this.selectList);
/* 283:    */     
/* 284:268 */     this.viewTabs.addTab(resourceMap.getString("selectListScroll.TabConstraints.tabTitle", new Object[0]), null, this.selectListScroll, resourceMap.getString("selectListScroll.TabConstraints.tabToolTip", new Object[0]));
/* 285:    */     
/* 286:270 */     this.selectTreeScroll.setName("selectTreeScroll");
/* 287:    */     
/* 288:272 */     this.selectTree.setName("selectTree");
/* 289:273 */     this.selectTree.setRootVisible(false);
/* 290:274 */     this.selectTree.setShowsRootHandles(true);
/* 291:275 */     this.selectTree.getSelectionModel().setSelectionMode(1);
/* 292:276 */     this.selectTree.addMouseListener(new MouseAdapter()
/* 293:    */     {
/* 294:    */       public void mouseClicked(MouseEvent evt)
/* 295:    */       {
/* 296:278 */         DesignIterationDialog.this.selectTreeMouseClicked(evt);
/* 297:    */       }
/* 298:280 */     });
/* 299:281 */     this.selectTreeScroll.setViewportView(this.selectTree);
/* 300:    */     
/* 301:283 */     this.viewTabs.addTab(resourceMap.getString("selectTreeScroll.TabConstraints.tabTitle", new Object[0]), null, this.selectTreeScroll, resourceMap.getString("selectTreeScroll.TabConstraints.tabToolTip", new Object[0]));
/* 302:    */     
/* 303:285 */     GroupLayout layout = new GroupLayout(getContentPane());
/* 304:286 */     getContentPane().setLayout(layout);
/* 305:287 */     layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.selectLabel).addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.viewTabs, -1, 460, 32767).addComponent(this.previewCartoon, -1, 460, 32767).addComponent(this.previewLabel, -1, 460, 32767)).addGap(12, 12, 12).addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false).addGroup(layout.createSequentialGroup().addComponent(this.cancelButton, -2, 106, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.okButton, -2, 108, -2)).addComponent(this.tipPanel, -1, -1, 32767)))).addContainerGap()));
/* 306:    */     
/* 307:    */ 
/* 308:    */ 
/* 309:    */ 
/* 310:    */ 
/* 311:    */ 
/* 312:    */ 
/* 313:    */ 
/* 314:    */ 
/* 315:    */ 
/* 316:    */ 
/* 317:    */ 
/* 318:    */ 
/* 319:    */ 
/* 320:    */ 
/* 321:    */ 
/* 322:    */ 
/* 323:    */ 
/* 324:    */ 
/* 325:307 */     layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(this.selectLabel).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(this.tipPanel, -1, -1, 32767).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.okButton).addComponent(this.cancelButton))).addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(this.viewTabs, -1, 262, 32767).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.previewLabel).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.previewCartoon, -2, 214, -2))).addContainerGap()));
/* 326:    */     
/* 327:    */ 
/* 328:    */ 
/* 329:    */ 
/* 330:    */ 
/* 331:    */ 
/* 332:    */ 
/* 333:    */ 
/* 334:    */ 
/* 335:    */ 
/* 336:    */ 
/* 337:    */ 
/* 338:    */ 
/* 339:    */ 
/* 340:    */ 
/* 341:    */ 
/* 342:    */ 
/* 343:    */ 
/* 344:    */ 
/* 345:    */ 
/* 346:    */ 
/* 347:329 */     pack();
/* 348:    */   }
/* 349:    */   
/* 350:    */   private void okButtonActionPerformed(ActionEvent evt)
/* 351:    */   {
/* 352:333 */     this.ok = true;
/* 353:334 */     setVisible(false);
/* 354:    */   }
/* 355:    */   
/* 356:    */   private void cancelButtonActionPerformed(ActionEvent evt)
/* 357:    */   {
/* 358:338 */     setVisible(false);
/* 359:    */   }
/* 360:    */   
/* 361:    */   private void selectListMouseClicked(MouseEvent evt)
/* 362:    */   {
/* 363:342 */     if (evt.getClickCount() == 2)
/* 364:    */     {
/* 365:343 */       this.ok = true;
/* 366:344 */       setVisible(false);
/* 367:    */     }
/* 368:    */   }
/* 369:    */   
/* 370:    */   private void selectTreeMouseClicked(MouseEvent evt)
/* 371:    */   {
/* 372:349 */     selectListMouseClicked(evt);
/* 373:    */   }
/* 374:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.DesignIterationDialog
 * JD-Core Version:    0.7.0.1
 */