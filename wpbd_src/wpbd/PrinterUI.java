/*   1:    */ package wpbd;
/*   2:    */ 
/*   3:    */ import java.awt.Component;
/*   4:    */ import java.awt.print.Printable;
/*   5:    */ import java.awt.print.PrinterException;
/*   6:    */ import java.awt.print.PrinterJob;
/*   7:    */ import java.text.MessageFormat;
/*   8:    */ import javax.print.attribute.HashPrintRequestAttributeSet;
/*   9:    */ import javax.print.attribute.PrintRequestAttributeSet;
/*  10:    */ import javax.print.attribute.standard.JobName;
/*  11:    */ import javax.print.attribute.standard.MediaPrintableArea;
/*  12:    */ import javax.swing.JOptionPane;
/*  13:    */ import javax.swing.JTable;
/*  14:    */ import javax.swing.JTable.PrintMode;
/*  15:    */ import org.jdesktop.application.ResourceMap;
/*  16:    */ 
/*  17:    */ public class PrinterUI
/*  18:    */ {
/*  19: 38 */   private static PrintRequestAttributeSet attr = null;
/*  20:    */   
/*  21:    */   public static void print(Component parent, JTable table, Class stringResourceClass, String[] notes)
/*  22:    */   {
/*  23: 49 */     ResourceMap resourceMap = WPBDApp.getResourceMap(stringResourceClass);
/*  24: 50 */     Printable printable = new TabularReportPrintable(table, JTable.PrintMode.FIT_WIDTH, new MessageFormat(resourceMap.getString("printHeader.message", new Object[0])), notes, new MessageFormat(resourceMap.getString("printFooter.message", new Object[0])));
/*  25:    */     
/*  26:    */ 
/*  27:    */ 
/*  28: 54 */     print(parent, printable, resourceMap, true);
/*  29:    */   }
/*  30:    */   
/*  31:    */   public static void print(Component parent, String fileName, BridgeModel bridge, Class stringResourceClass, boolean dialog)
/*  32:    */   {
/*  33: 67 */     ResourceMap resourceMap = WPBDApp.getResourceMap(stringResourceClass);
/*  34: 68 */     Printable printable = new BridgeBlueprintPrintable(fileName, bridge);
/*  35: 69 */     print(parent, printable, resourceMap, dialog);
/*  36:    */   }
/*  37:    */   
/*  38: 72 */   private static PrinterJob job = null;
/*  39:    */   
/*  40:    */   private static void print(Component parent, Printable printable, ResourceMap resourceMap, boolean dialog)
/*  41:    */   {
/*  42: 85 */     boolean firstPrint = false;
/*  43: 88 */     if (job == null)
/*  44:    */     {
/*  45: 89 */       job = PrinterJob.getPrinterJob();
/*  46: 90 */       firstPrint = true;
/*  47:    */     }
/*  48: 94 */     job.setPrintable(printable);
/*  49: 97 */     if (attr == null)
/*  50:    */     {
/*  51: 98 */       attr = new HashPrintRequestAttributeSet();
/*  52: 99 */       attr.add(new JobName(WPBDApp.getResourceMap(PrinterUI.class).getString("jobName.text", new Object[0]), null));
/*  53:    */       
/*  54:101 */       attr.add(new MediaPrintableArea(0.0F, 0.0F, 100.0F, 100.0F, 25400));
/*  55:    */     }
/*  56:106 */     boolean printAccepted = true;
/*  57:108 */     if ((firstPrint) || (dialog)) {
/*  58:109 */       printAccepted = job.printDialog(attr);
/*  59:    */     }
/*  60:113 */     if (printAccepted) {
/*  61:    */       try
/*  62:    */       {
/*  63:115 */         job.print(attr);
/*  64:    */       }
/*  65:    */       catch (PrinterException ex)
/*  66:    */       {
/*  67:117 */         JOptionPane.showMessageDialog(parent, resourceMap.getString("printingHalted.text", new Object[] { ex.getMessage() }), resourceMap.getString("printingHaltedDialogTitle.text", new Object[0]), 1);
/*  68:    */       }
/*  69:    */     }
/*  70:    */   }
/*  71:    */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.PrinterUI
 * JD-Core Version:    0.7.0.1
 */