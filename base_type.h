#ifndef BASE_TYPE_H 
#define BASE_TYPE_H 

//specs
#define MAX_DECK_TYPE 4
#define MAX_TRUCK_TYPE 4
#define MAX_BUNDLE 8
#define MAX_MEMBER 120
#define MAX_FREE_JOINT 30
#define MAX_FIXED_JOINT 20
#define MAX_JOINT 50
#define MAX_EQUATION (MAX_JOINT*2)
#define MAX_LOAD 12

//type size
#define SIZE_SIZE 33
#define MATERIAL_SIZE 3
#define SHAPE_SIZE 2
#define MAX_TYPE 198

//physical constants
#define GRAVITY 9.8066
#define DEAD_LOAD_FACTOR 1.25
#define LIVE_LOAD_FACTOR 2.3275
#define PI2 9.8696044
#define COMPRESSION_RESISTANCE_FACTOR 0.9
#define TENSION_RESISTANCE_FACTOR 0.95
#define BUNDLE_COST 1000.
#define JOINT_COST 500.
#define DEADLOADFACTOR 1.25
#define LIVELOADFACTOR 2.3275

//defaults for data structure
#define EMPTY_VALUE -1.
#define INVALID_VALUE 10000000.

//quick hands
#define _FFSLL __builtin_ffsll
//TODO mask can't be 0
#define FFSLLL(mask) (_FFSLL(mask)==0?_FFSLL((mask>>64))+64:_FFSLL(mask))
#define GET_BYTE(p) (*(const guchar*)(p))
#define GET_DOLLAR(p) (*(Dollar*)(p))
#define SQR(a) ((a)*(a))

//TODO double check max_load
#include <stdio.h>
#include <glib.h>
#include <stdlib.h>
#include <string.h>

/**************//**
 * basic types
 *****************/

//gboolean
typedef gboolean Bool;
/**
 * useful for space critical application
 */
//gchar
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
 * TODO ensure size on various application
 */
typedef __uint128_t TestMask;

/**************//**
 * small structs
 *****************/

/**
 * each byte is divided into 2 parts, each 4 bit is
 * a signed integer representing relative coordinate
 * to positionHint.
 */
typedef struct {
 Byte joints[MAX_FREE_JOINT];
} PositionHintB;

/**
 * coordinate of all joints in unit of quarter meter.
 */
typedef struct {
 Int xy[MAX_EQUATION];
} PositionHint;
/**
 * value of the main table
 */
#define TYPE_HINT_COST_SIZE(memberSize) (sizeof(Dollar)+MAX_BUNDLE*sizeof(Byte)+(memberSize))
typedef struct {
 /**
  * accurate cost for bridge design.
  */
 Dollar cost;
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
} TypeHintCostB;

/**
 * struct for GPU? to use 
 * WARNING: positionHintB is not contained
 */
typedef struct {
    
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
} OptimizeTask;

/**
 * store the connection configuration of a member.
 * j1,j2 is the 
 */
typedef struct {
 Int j1,j2;
} MemberLink;
/**
 * TODO need perfection
 */
typedef enum {
 LOADED,
 SOLVED_GOOD,
 SOLVED_UNSTABLE,
 OPTIMIZED_BETTER,
 OPTIMIZED_UNCHANGED,
 OPTIMIZED_WORSE,
 OPTIMIZED_MAX_BUNDLE,
 OPTIMIZED_MAX_COST
} Status;

/**************//**
 * big class
 *****************/
struct _BridgeInfo;
struct _Manager;
struct _Result;
struct _TaskQueue;
struct _CostTable;


char* print_bytes(gconstpointer p,int size);

#endif
