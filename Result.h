#ifndef RESULT_H
#define RESULT_H

#include "base_type.h"
#include "BridgeInfo.h"

typedef struct _Result{
 /**
  * coordinate of all joints
  */
 Double XY[MAX_EQUATION];
 /**
  * main matrix to be calculated
  */
 Double matrix[MAX_EQUATION*MAX_EQUATION];
 /**
  * length of each memeber
  */
 Double memberLength[MAX_MEMBER];
 /**
  * weight on each joints
  */
 Double loads[MAX_JOINT];
 /**
  * displacement of each variable in each load cases
  */
 Double jdisplacement[MAX_LOAD*MAX_EQUATION];
 /**
  * force of each member in each load cases + means tension - means
  * compression
  */
 Double memberForce[MAX_LOAD*MAX_MEMBER];
 /**
  * maxForce is always positive minForce is always negative
  */
 Double maxForce[MAX_MEMBER];
 Double minForce[MAX_MEMBER];
 Dollar totalCost;
 Status status;
} Result;
void result_print(GLogLevelFlags flags, const Result* result, const struct _BridgeInfo *bridgeInfo,TypeHintCostB *thc);

#endif
