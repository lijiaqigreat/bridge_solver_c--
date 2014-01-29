/*  1:   */ package wpbd;
/*  2:   */ 
/*  3:   */ public class ChangeMembersCommand
/*  4:   */   extends EditCommand
/*  5:   */ {
/*  6:   */   private Member[] members;
/*  7:   */   
/*  8:   */   public ChangeMembersCommand(EditableBridgeModel bridge, int materialIndex, int sectionIndex, int sizeIndex)
/*  9:   */   {
/* 10:35 */     super(bridge);
/* 11:36 */     this.members = bridge.getSelectedMembers();
/* 12:37 */     for (int i = 0; i < this.members.length; i++)
/* 13:   */     {
/* 14:38 */       this.members[i] = new Member(this.members[i], bridge.getInventory(), materialIndex, sectionIndex, sizeIndex);
/* 15:   */       
/* 16:40 */       this.members[i].setSelected(true);
/* 17:   */     }
/* 18:42 */     this.presentationName = getMembersMessage("changeMaterial.text", this.members);
/* 19:   */   }
/* 20:   */   
/* 21:   */   public ChangeMembersCommand(EditableBridgeModel bridge, int sizeOffset)
/* 22:   */   {
/* 23:52 */     super(bridge);
/* 24:53 */     this.members = bridge.getSelectedMembers();
/* 25:54 */     for (int i = 0; i < this.members.length; i++)
/* 26:   */     {
/* 27:55 */       this.members[i] = new Member(this.members[i], bridge.getInventory().getShape(this.members[i].getShape(), sizeOffset));
/* 28:56 */       this.members[i].setSelected(true);
/* 29:   */     }
/* 30:58 */     this.presentationName = getMembersMessage(sizeOffset > 0 ? "increaseSize.text" : "decreaseSize.text", this.members);
/* 31:   */   }
/* 32:   */   
/* 33:   */   public void go()
/* 34:   */   {
/* 35:63 */     EditCommand.exchange(this.bridge.getMembers(), this.members);
/* 36:   */   }
/* 37:   */   
/* 38:   */   void goBack()
/* 39:   */   {
/* 40:68 */     EditCommand.exchange(this.bridge.getMembers(), this.members);
/* 41:   */   }
/* 42:   */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.ChangeMembersCommand
 * JD-Core Version:    0.7.0.1
 */