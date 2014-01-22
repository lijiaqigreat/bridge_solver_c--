#ifndef BASE_TYPE_H 
#define BASE_TYPE_H 

#define FFSLL __builtin_ffsll
#define MAX_DECK_TYPE 4
#define MAX_TRUCK_TYPE 4
#define MAX_TYPE 256
#define MAX_BUNDLE 8
#define MAX_MEMBER 64
#define MAX_FREE_JOINT 30
#define MAX_FIXED_JOINT 20
#define MAX_JOINT (MAX_FREE_JOINT+MAX_FIXED_JOINT)
#define MAX_EQUATION (MAX_JOINT*2)
#define SQR(a) ((a)*(a))
//TODO double check max_load
#define MAX_LOAD 12
#include <stdio.h>

/**
 * useful for space critical application
 */
typedef unsigned char Byte;
/**
 * TODO make sure it's 64bit to be capatible with original Java implementation.
 */
typedef double Double;
/**
 * money unit
 */
typedef Double Dollar;
/**
 * index for relugar use
 */
typedef int Int;
/**
 * nth bit is 1 if member n pass the type
 */
typedef unsigned long long TestMask;

/**
 * each byte is divided into 2 parts, each 4 bit is
 * a signed integer representing relative coordinate
 * to positionHint.
 */
struct PositionHintB{
 Byte joints[MAX_FREE_JOINT];
};

/**
 * coordinate of all joints in unit of quarter meter.
 */
struct PositionHint{
 Int xy[MAX_EQUATION];
};
/**
 * value of the main table
 */
struct TypeHintCostB{
 /**
  * typeIndex of each bundle, have to be in order
  */
 Byte bundle[MAX_BUNDLE];
 /**
  * bundleIndex for each member.
  * MAX_BUNDLE is 8. Only 3 bits are neseccary,
  * but whole 8 bits are used for convidience. 
  */
 Byte member[MAX_MEMBER];
 /**
  * accurate cost for bridge design.
  */
 Dollar cost;
};

/**
 * struct for GPU? to use 
 * WARNING: positionHintB is not contained
 */
struct OptimizeTask{
 /*
bitarray: member_size*valid_type_size
length for each member
cost for each type,
index for each type,
bundle cost,
cap_cost
x=member_size, 64?
y=valid_type_size, 256? 100?
x*y*(1/8) + x*8 + y*(8+1) + 8 + 8

MAX_BUNDLE
 */
 
 //postionHint sensitive

 /**
  * testMask for each type worth considering
  */
 TestMask typeTestMask[MAX_TYPE];
 /**
  * length of each member
  */
 Double length[MAX_MEMBER];
 /**
  * cost of each type
  * unit: Dollar/meter
  */
 Dollar cost[MAX_TYPE];
 /**
  * actual global index of each type
  */
 Byte index[MAX_TYPE];
 /**
  * number of types worth considering
  */
 Int typeSize;
 Int memberSize;
 /**
  * used to speed up optimization
  * TODO test speed difference, might not be significant.
  */
 Dollar capCost;

 //bridgeInfo sensitive

 Dollar bundleCost;
 Double minLength;
};
struct OptimizeTask2{
 //postionHint sensitive

 /**
  * testMask for each type worth considering
  */
 TestMask typeTestMask[MAX_TYPE];
 /**
  * length of each member
  */
 Double length[MAX_MEMBER];
 /**
  * cost of each type
  * unit: Dollar/meter
  */
 //Dollar cost[MAX_TYPE];
 /**
  * actual global index of each type
  */
 //Byte index[MAX_TYPE];
 /**
  * number of types worth considering
  */
 //Int typeSize;
 /**
  * used to speed up optimization
  * TODO test speed difference, might not be significant.
  */
 Dollar capCost;

 //bridgeInfo sensitive

 //Dollar bundleCost;
 //Int memberSize;
 //Double minLength;
};
/**
 * store the connection configuration of a member.
 * j1,j2 is the 
 */
struct MemberLink{
 Int j1,j2;
};
/**
 * TODO more comment
 */
struct Type{
 Double E;
 Double Fy;
 Double Density;
 Double Area;
 Double Moment;
 Double CostVol;

 Double weight;
 Double AE;
 Double cost;
 Double tensionStrength;
 Double inverseRadiusOfGyration;
 Double FyArea_d_CEMoment;
 Double FyArea;
 Double compressionResistanceFactor;
 
 Double getCompressionStrength(Double length);
 bool ifPass(Double compression,Double tension,Double length,Double slenderness);
};
/**
 * TODO need perfection
 */
enum Status{
 LOADED,
 SOLVED_GOOD,
 SOLVED_UNSTABLE,
 OPTIMIZED_BETTER,
 OPTIMIZED_UNCHANGED,
 OPTIMIZED_WORSE,
 OPTIMIZED_MAX_BUNDLE,
 OPTIMIZED_MAX_COST
};


#endif
