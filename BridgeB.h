#ifndef BRIDGEB_H
#define BRIDGEB_H

#include "base_type.h"

struct ConditionB{
 char name[16];
 
};
struct BridgeInfo{
 //Condition
 
 char conditionName[16];
 
 /**
  * number of total joint size
  */
 Int fixedJointSize;
 /**
  * number of decks
  * also the load size
  * actual load size is deckSize+1, since there is an empty load.
  */
 Int deckSize;

 Int deckType;
 Int truckType;
 /**
  * maximum allowed slenderness
  */
 Double slenderness;
 /**
  * index of XY to be fixed.
  * stops at -1
  * rest is 0
  */
 Int fixedIndex[20];

//Bridge

 /**
  * number of total joint size
  */
 Int totalJointSize;
 /**
  * number of members
  */
 Int memberSize;
 /**
  * index of j1,j2 of each member
  */
 MemberLink memberLinks[MAX_MEMBER];
 /**
  * all cost except member cost and bundle cost.
  */
 Dollar baseCost;

//Hints

 /**
  * first deckSize+1 joints have to be deck joints
  * first fixedJointSize joints have to be fixed joints
  */
 PositionHint positionHint;
 TypeHintCostB typeHint;
 
};

#endif
