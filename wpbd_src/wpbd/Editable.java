package wpbd;

public abstract interface Editable
{
  public abstract boolean isSelected();
  
  public abstract boolean setSelected(boolean paramBoolean);
  
  public abstract int getIndex();
  
  public abstract void setIndex(int paramInt);
  
  public abstract void swapContents(Editable paramEditable);
}


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.Editable
 * JD-Core Version:    0.7.0.1
 */