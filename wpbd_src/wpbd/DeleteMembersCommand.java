/*  1:   */ package wpbd;
/*  2:   */ 
/*  3:   */ public class DeleteMembersCommand
/*  4:   */   extends EditCommand
/*  5:   */ {
/*  6:   */   protected Joint[] joints;
/*  7:   */   protected Member[] members;
/*  8:   */   
/*  9:   */   public DeleteMembersCommand(EditableBridgeModel bridge)
/* 10:   */   {
/* 11:38 */     super(bridge);
/* 12:39 */     this.members = bridge.getSelectedMembers();
/* 13:40 */     this.joints = bridge.getJointsToDeleteWithSelectedMembers();
/* 14:41 */     this.presentationName = getMembersMessage("deleteMember.text", this.members);
/* 15:   */   }
/* 16:   */   
/* 17:   */   public DeleteMembersCommand(EditableBridgeModel bridge, Member member)
/* 18:   */   {
/* 19:51 */     super(bridge);
/* 20:52 */     this.members = new Member[] { member };
/* 21:53 */     this.joints = new Joint[0];
/* 22:54 */     this.presentationName = getMembersMessage("deleteMember.text", this.members);
/* 23:   */   }
/* 24:   */   
/* 25:   */   public void go()
/* 26:   */   {
/* 27:59 */     for (int i = 0; i < this.members.length; i++) {
/* 28:60 */       this.members[i].setSelected(false);
/* 29:   */     }
/* 30:62 */     EditCommand.delete(this.bridge.getJoints(), this.joints);
/* 31:63 */     EditCommand.delete(this.bridge.getMembers(), this.members);
/* 32:   */   }
/* 33:   */   
/* 34:   */   public void goBack()
/* 35:   */   {
/* 36:68 */     EditCommand.insert(this.bridge.getJoints(), this.joints);
/* 37:69 */     EditCommand.insert(this.bridge.getMembers(), this.members);
/* 38:   */   }
/* 39:   */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.DeleteMembersCommand
 * JD-Core Version:    0.7.0.1
 */