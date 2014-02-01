#ifndef BRIDGEINFO_H
#define BRIDGEINFO_H

#include "base_type.h"
#include "type.h"

/**
 * to be converted from .bdc
 * IMPORTANT should be fixed once loaded
 */
struct BridgeInfo{
 const char* buf;
 
 //Condition
 
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
 /**
  * weight of deck after considering all factors Unit: kN/deck (kN/(4m))
  */
 Double deckWeight;
 /**
  * weight of back wheel of the truck
  * considered all factors
  */
 Double backWeight;
 /**
  * weight of front wheel of the truck
  * considered all factors
  */
 Double frontWeight;
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

 Int typeSize;
 /**
  * all avaliable types for this optimization
  * TODO pointer or array?
  */
 TypeB types[MAX_TYPE];
 /**
  * additional cost per bundle used
  */
 Dollar bundleCost;
 

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
