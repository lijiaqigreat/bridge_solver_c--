/*  1:   */ package wpbd;
/*  2:   */ 
/*  3:   */ import java.awt.Component;
/*  4:   */ import java.util.HashMap;
/*  5:   */ import java.util.Iterator;
/*  6:   */ import java.util.Map.Entry;
/*  7:   */ import java.util.Set;
/*  8:   */ import javax.swing.AbstractButton;
/*  9:   */ import javax.swing.Action;
/* 10:   */ 
/* 11:   */ public class EnabledStateManager
/* 12:   */ {
/* 13:   */   private int guiState;
/* 14:   */   private int guiStateCount;
/* 15:37 */   private final HashMap<Component, State> componentToStateMap = new HashMap(42);
/* 16:   */   
/* 17:   */   private static void setEnabledImpl(Component component, boolean enable)
/* 18:   */   {
/* 19:42 */     if ((component instanceof AbstractButton))
/* 20:   */     {
/* 21:43 */       AbstractButton button = (AbstractButton)component;
/* 22:44 */       if (button.getAction() != null)
/* 23:   */       {
/* 24:45 */         button.getAction().setEnabled(enable);
/* 25:46 */         return;
/* 26:   */       }
/* 27:   */     }
/* 28:49 */     component.setEnabled(enable);
/* 29:   */   }
/* 30:   */   
/* 31:   */   private static class State
/* 32:   */   {
/* 33:   */     boolean componentEnabled;
/* 34:   */     boolean[] enablable;
/* 35:   */     
/* 36:   */     State(Component component, boolean[] enablable)
/* 37:   */     {
/* 38:56 */       this.enablable = enablable;
/* 39:57 */       this.componentEnabled = component.isEnabled();
/* 40:   */     }
/* 41:   */   }
/* 42:   */   
/* 43:   */   public EnabledStateManager(int guiStateCount)
/* 44:   */   {
/* 45:62 */     this.guiStateCount = guiStateCount;
/* 46:   */   }
/* 47:   */   
/* 48:   */   public void add(Component component, boolean[] enabledVsGuiState)
/* 49:   */   {
/* 50:66 */     if (enabledVsGuiState.length != this.guiStateCount) {
/* 51:67 */       throw new IllegalArgumentException();
/* 52:   */     }
/* 53:69 */     this.componentToStateMap.put(component, new State(component, enabledVsGuiState));
/* 54:   */   }
/* 55:   */   
/* 56:   */   public void remove(Component component)
/* 57:   */   {
/* 58:73 */     this.componentToStateMap.remove(component);
/* 59:   */   }
/* 60:   */   
/* 61:   */   public void setEnabled(Component component, boolean enable)
/* 62:   */   {
/* 63:77 */     State state = (State)this.componentToStateMap.get(component);
/* 64:78 */     if (state == null)
/* 65:   */     {
/* 66:79 */       setEnabledImpl(component, enable);
/* 67:   */     }
/* 68:   */     else
/* 69:   */     {
/* 70:82 */       setEnabledImpl(component, (enable) && (state.enablable[this.guiState] != 0));
/* 71:83 */       state.componentEnabled = enable;
/* 72:   */     }
/* 73:   */   }
/* 74:   */   
/* 75:   */   public void setGUIState(int guiState)
/* 76:   */   {
/* 77:88 */     Iterator<Map.Entry<Component, State>> i = this.componentToStateMap.entrySet().iterator();
/* 78:89 */     while (i.hasNext())
/* 79:   */     {
/* 80:90 */       Map.Entry<Component, State> entry = (Map.Entry)i.next();
/* 81:91 */       State state = (State)entry.getValue();
/* 82:92 */       setEnabledImpl((Component)entry.getKey(), (state.componentEnabled) && (state.enablable[guiState] != 0));
/* 83:   */     }
/* 84:94 */     this.guiState = guiState;
/* 85:   */   }
/* 86:   */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.EnabledStateManager
 * JD-Core Version:    0.7.0.1
 */