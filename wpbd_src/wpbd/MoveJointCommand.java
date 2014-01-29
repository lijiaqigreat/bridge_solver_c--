/*  1:   */ package wpbd;
/*  2:   */ 
/*  3:   */ public class MoveJointCommand
/*  4:   */   extends JointCommand
/*  5:   */ {
/*  6:   */   protected Joint joint;
/*  7:   */   private Affine.Point ptWorld;
/*  8:   */   
/*  9:   */   public MoveJointCommand(EditableBridgeModel bridge, Joint joint, Affine.Point ptWorld)
/* 10:   */   {
/* 11:28 */     super(bridge);
/* 12:29 */     this.joint = new Joint(joint.getIndex(), ptWorld);
/* 13:30 */     this.ptWorld = new Affine.Point(ptWorld);
/* 14:31 */     fixUpMembers(ptWorld, joint);
/* 15:32 */     this.presentationName = String.format(getString("moveJoint.text"), new Object[] { Integer.valueOf(joint.getNumber()), ptWorld.toString() });
/* 16:   */   }
/* 17:   */   
/* 18:   */   public void go()
/* 19:   */   {
/* 20:37 */     super.go();
/* 21:38 */     EditCommand.exchange(this.bridge.getJoints(), this.joint);
/* 22:   */   }
/* 23:   */   
/* 24:   */   public void goBack()
/* 25:   */   {
/* 26:43 */     EditCommand.exchange(this.bridge.getJoints(), this.joint);
/* 27:44 */     super.goBack();
/* 28:   */   }
/* 29:   */ }


/* Location:           C:\Users\jli41\Desktop\WPBD.jar
 * Qualified Name:     wpbd.MoveJointCommand
 * JD-Core Version:    0.7.0.1
 */