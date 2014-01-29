/*  1:   */ package wpbd;
/*  2:   */ 
/*  3:   */ public class DeleteJointCommand
/*  4:   */   extends EditCommand
/*  5:   */ {
/*  6:   */   protected Joint[] joints;
/*  7:   */   protected Member[] members;
/*  8:   */   
/*  9:   */   public DeleteJointCommand(EditableBridgeModel bridge, Joint joint)
/* 10:   */   {
/* 11:40 */     super(bridge);
/* 12:41 */     this.members = bridge.findMembersWithJoint(joint);
/* 13:42 */     this.joints = new Joint[] { joint };
/* 14:43 */     this.presentationName = (getString("deleteJoint.text") + " " + this.joints[0].getPointWorld() + ".");
/* 15:   */   }
/* 16:   */   
/* 17:   */   public DeleteJointCommand(EditableBridgeModel bridge)
/* 18:   */   {
/* 19:53 */     this(bridge, bridge.getSelectedJoint());
/* 20:   */   }
/* 21:   */   
/* 22:   */   public void go()
/* 23:   */   {
/* 24:58 */     this.joints[0].setSelected(false);
/* 25:59 */     EditCommand.delete(this.bridge.getJoints(), this.joints);
/* 26:60 */     EditCommand.delete(this.bridge.getMembers(), this.members);
/* 27:   */   }
/* 28:   */   
/* 29:   */   void goBack()
/* 30:   */   {
/* 31:65 */     EditCommand.insert(this.bridge.getJoints(), this.joints);
/* 32:66 */     EditCommand.insert(this.bridge.getMembers(), this.members);
/* 33:   */   }
/* 34:   */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.DeleteJointCommand
 * JD-Core Version:    0.7.0.1
 */