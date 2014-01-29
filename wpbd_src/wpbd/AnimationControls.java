package wpbd;

import java.awt.Dialog;

public abstract interface AnimationControls
{
  public abstract void saveState();
  
  public abstract void restoreState();
  
  public abstract Dialog getDialog();
  
  public abstract void startAnimation();
  
  public abstract void saveVisibilityAndHide();
  
  public abstract boolean getVisibleState();
}


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.AnimationControls
 * JD-Core Version:    0.7.0.1
 */